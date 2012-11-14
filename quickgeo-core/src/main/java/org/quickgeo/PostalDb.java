/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo;

import com.darkcorner.minotaur.util.GeoMath;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * The PostalDb instance is the primary interface for the QuickGeo API.  
 * 
 * Instances of this class are expensive to instantiate and store, so only one
 * per VM is advisable. To encourage this. all instances of PostalDb must be
 * obtained through a {@link PostalDbFactory}.  The PostalDbFactory uses
 * a static initializer to ensure that only once instance of PostalDb is 
 * created per VM.
 * 
 * @since 0.1.0
 * @author Jason Nichols (jason@kickroot.com)
 */
@Immutable
@ThreadSafe
public final class PostalDb {
  
  public static final class GeoRect {
  
    private final double topLeftLat;
    private final double topLeftLon;
    private final double bottomRightLat;
    private final double bottomRightLon;    
    
    private GeoRect(double topLeftLat, double topLeftLon, double bottomRightLat,
            double bottomRightLon) {
      this.topLeftLat = topLeftLat;
      this.topLeftLon = topLeftLon;
      this.bottomRightLat = bottomRightLat;
      this.bottomRightLon = bottomRightLon;
    }
    
    private boolean contains(Place p) {
      return contains(p.getLatitude(), p.getLongitude());
    }
    
    private boolean contains(double latitude, double longitude) {
      return (topLeftLat > latitude && latitude > bottomRightLat) 
              && (topLeftLon < longitude && longitude < bottomRightLon );
    }    
    
    public double getTopLeftLat() {
      return topLeftLat;
    }
    
    public double getTopLeftLon() {
      return topLeftLon;
    }    
    
    public double getBottomRightLat() {
      return bottomRightLat;
    }
    
    public double getBottomRightLon() {
      return bottomRightLon;
    }
  }
  
  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  private static final double MEAN_RADIUS_IN_MILES = 3963.191d;
  
  private static final double MEAN_RADIUS_IN_KILOMETERS = 6378.137d; 
  
  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
  //////////////////////////////// Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  private final LinkedHashSet<Place> places;
  
  /////////////////////////////// Constructors \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\  
  
  protected PostalDb(LinkedHashSet<Place> places) {
    this.places = places;
    Logger.getLogger(getClass().getName()).log(Level.INFO, "Initialized DB with {0} zips", places.size());
  }
  
  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\   
  
  /**
   * Search the in-memory database for all places within a given radius of the
   * supplied origin.
   * 
   * @param latitude The latitude of the origin
   * @param longitude The longitude of the origin
   * @param rangeInMiles The radius to use when finding places.
   * @since 0.1.0
   * @return A list of all places within the specified range.
   */
  public List<Place> withinMilesOf(double latitude, double longitude, int rangeInMiles) {
    
    List<Place> list = Lists.newArrayList();
    
    GeoRect rect = boundingBoxInMiles(latitude, longitude, rangeInMiles);
    
    // Iterate through all Places and find those that fit within the box. This 
    // will eliminate most places out of the specified range very quickly.  A 
    // closer check is done on the next pass below that gives a definitive answer.
    for (Place p : places) {
      if (rect.contains(p)) {
        list.add(p);
      }
    }
    
    
    // Trim to exact cutoff
    Iterator<Place> iter = list.iterator();
    while(iter.hasNext()) {
      Place p = iter.next();
      if (distanceInMiles(latitude, longitude, p.getLatitude(), p.getLongitude()) > rangeInMiles) {
        iter.remove();
      }
    }
    
    return list;
    
  }
  
  /**
   * Search the in-memory database for all places within a given radius of the
   * supplied origin.
   * 
   * @param p The place to use as the origin for the search
   * @param rangeInMiles The radius to use when finding places.
   * @since 0.1.0
   * @return A list of all places within the specified range.
   */  
  public List<Place> withinMilesOf(Place p, int rangeInMiles) {
    return withinMilesOf(p.getLatitude(), p.getLongitude(), rangeInMiles);
  }

  /**
   * Search the in-memory database for all places within a given radius of the
   * supplied origin.
   * 
   * @param latitude The latitude of the origin
   * @param longitude The longitude of the origin
   * @param rangeInKilometers The radius to use when finding places.
   * @since 0.1.0
   * @return A list of all places within the specified range.
   */  
  public List<Place> withinKilometersOf(double latitude, double longitude, int rangeInKilometers) {
    
    List<Place> list = Lists.newArrayList();
    
    GeoRect rect = boundingBoxInKilometers(latitude, longitude, rangeInKilometers);
    
    // Iterate through all Places and find those that fit within the box. This 
    // will eliminate most places out of the specified range very quickly.  A 
    // closer check is done on the next pass below that gives a definitive answer.
    for (Place p : places) {
      if (rect.contains(p)) {
        list.add(p);
      }
    }
        
    // Trim to exact cutoff
    Iterator<Place> iter = list.iterator();
    while(iter.hasNext()) {
      Place p = iter.next();
      if (distanceInKilometers(latitude, longitude, p.getLatitude(), p.getLongitude()) > rangeInKilometers) {
        iter.remove();
      }
    }
    
    return list;    
  }
  
  /**
   * Search the in-memory database for all places within a given radius of the
   * supplied origin.
   * 
   * @param p The place to use as the origin for the search
   * @param rangeInKilometers The radius to use when finding places.
   * @since 0.1.0
   * @return A list of all places within the specified range.
   */  
  public List<Place> withinKilometersOf(Place p, int rangeInKilometers) {
    return withinKilometersOf(p.getLatitude(), p.getLongitude(), rangeInKilometers);
  }
  
  
  /**
   * Search the in-memory database for all places matching the given postal code.
   * @param regex A regex pattern of the postal codes to search.  
   * @return A list of {@link Place Places} matching the supplied postal code pattern.
   * @since 0.1.0
   */
  public List<Place> byPostalCode(String regex) {
    List<Place> list = Lists.newArrayList();
    
    Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    
    for (Place place : places) {
      if (p.matcher(place.getPostalCode()).matches()) {
        list.add(place);
      }
    }
        
    return list;    
  }
  
  /**
   * Search the in-memory database for all places matching the given name.
   * @param regex A regex pattern of places to search.  
   * @return A list of {@link Place Places} matching the supplied place name pattern.
   * @since 0.1.0
   */  
  public List<Place> byName(String regex) {
    List<Place> list = Lists.newArrayList();
    
    Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    
    for (Place place : places) {
      if (p.matcher(place.getPlaceName()).matches()) {
        list.add(place);
      }
    }
    
    
    return list;
  }
  
  //------------------------ Implements:
  
  //------------------------ Overrides:
  
  //---------------------------- Abstract Methods -----------------------------
  
  //---------------------------- Utility Methods ------------------------------
  
  /**
   * Compute the distance in miles between two Places.
   * @param p1 The first place
   * @param p2 The second place
   * @return The distance in miles between the two places.
   * @since 0.1.0
   */
  public double distanceInMiles(Place p1, Place p2) {
    return distanceInMiles(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(),p2.getLongitude());
  }
  
  /**
   * Compute the distance in miles between two sets of coordinates.
   * @param lat1 The latitude for the first place
   * @param lon1 The longitude for the first place
   * @param lat2 The latitude for the second place
   * @param lon2 The longitude for the second place
   * @return The distance in miles between the sets of coordinates.
   * @since 0.1.0
   */
  public double distanceInMiles(double lat1, double lon1, double lat2, double lon2) {
    return GeoMath.greatCircleDistance(lat1, lon1, lat2, lon2) * MEAN_RADIUS_IN_MILES;    
  }
  
  /**
   * Compute the distance in kilometers between two Places.
   * @param p1 The first place
   * @param p2 The second place
   * @return The distance in kilometers between the two places.
   * @since 0.1.0
   */
  public double distanceInKilometers(Place p1, Place p2) {
    return distanceInKilometers(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(),p2.getLongitude());
  }  
  
  /**
   * Compute the distance in kilometers between two sets of coordinates.
   * @param lat1 The latitude for the first place
   * @param lon1 The longitude for the first place
   * @param lat2 The latitude for the second place
   * @param lon2 The longitude for the second place
   * @return The distance in kilometers between the sets of coordinates.
   * @since 0.1.0
   */  
  public double distanceInKilometers(double lat1, double lon1, double lat2, double lon2) {
    return GeoMath.greatCircleDistance(lat1, lon1, lat2, lon2) * MEAN_RADIUS_IN_KILOMETERS;        
  }
  
  /**
   * Get a {@link GeoRect} that represents the bounding box for the supplied origin and radius.
   * @param latitude The latitude of the origin
   * @param longitude The longitude of the origin
   * @param radiusInMiles The radius in miles to cover in each direction.
   * @return  A {@link GeoRect} that represents the area of the bounding box.
   * @since 0.1.0
   */
  public GeoRect boundingBoxInMiles(double latitude, double longitude, int radiusInMiles) {
    
    // Length of one arc degree in miles
    double latLength = GeoMath.latArcDegreeLength(latitude, MEAN_RADIUS_IN_MILES);
    double lonLength = GeoMath.lonArcDegreeLength(latitude, MEAN_RADIUS_IN_MILES);
    
    // How many degrees the supplied radius is for the given location
    double latDelta = radiusInMiles / latLength;
    double lonDelta = radiusInMiles / lonLength;
    
    latDelta /= 2.0;
    lonDelta /= 2.0;
    
    return new GeoRect(latitude + latDelta, longitude - lonDelta,
            latitude-latDelta, longitude + lonDelta);    
  }
  
  /**
   * Get a {@link GeoRect} that represents the bounding box for the supplied origin and radius.
   * @param latitude The latitude of the origin
   * @param longitude The longitude of the origin
   * @param radiusInKilometers The radius in miles to cover in each direction.
   * @return  A {@link GeoRect} that represents the area of the bounding box.
   * @since 0.1.0
   */  
  public GeoRect boundingBoxInKilometers(double latitude, double longitude, int radiusInKilometers) {
    
    // Length of one arc degree in miles
    double latLength = GeoMath.latArcDegreeLength(latitude, MEAN_RADIUS_IN_KILOMETERS);
    double lonLength = GeoMath.lonArcDegreeLength(latitude, MEAN_RADIUS_IN_KILOMETERS);
    
    // How many degrees the supplied radius is for the given location
    double latDelta = radiusInKilometers / latLength;
    double lonDelta = radiusInKilometers / lonLength;
    
    latDelta /= 2.0;
    lonDelta /= 2.0;
    
    return new GeoRect(latitude + latDelta, longitude - lonDelta,
            latitude-latDelta, longitude + lonDelta);    
  }  
  
  //---------------------------- Property Methods -----------------------------     
  
  /**
   * Get the number of {@link Place places} stored by this database.
   * @since 0.1.0
   */
  public int getSize() {
    return places.size();
  }
  
}
