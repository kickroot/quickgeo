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
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

/**
 *
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
    Logger.getLogger(getClass().getName()).info("Initialized DB with " + places.size() + " zips" );
  }
  
  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\   
  
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
  
  public List<Place> withinMilesOf(Place p, int rangeInMiles) {
    return withinMilesOf(p.getLatitude(), p.getLongitude(), rangeInMiles);
  }

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
  
  public List<Place> withinKilometersOf(Place p, int rangeInKilometers) {
    return withinKilometersOf(p.getLatitude(), p.getLongitude(), rangeInKilometers);
  }
  
  
  public List<Place> byPostalCode(String postalCode) {
    List<Place> list = Lists.newArrayList();
    
    Pattern p = Pattern.compile(postalCode, Pattern.CASE_INSENSITIVE);
    
    for (Place place : places) {
      if (p.matcher(place.getPostalCode()).matches()) {
        list.add(place);
      }
    }
        
    return list;    
  }
  
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
   * Compute the distance in Miles between two Places.
   * @param p1 The first place
   * @param p2 The second place
   * @return The distance in miles between the two places.
   */
  public double distanceInMiles(Place p1, Place p2) {
    return distanceInMiles(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(),p2.getLongitude());
  }
  
  public double distanceInMiles(double lat1, double lon1, double lat2, double lon2) {
    return GeoMath.greatCircleDistance(lat1, lon1, lat2, lon2) * MEAN_RADIUS_IN_MILES;    
  }
  
  /**
   * Compute the distance in kilometers between two Places.
   * @param p1 The first place
   * @param p2 The second place
   * @return The distance in kilometers between the two places.
   */
  public double distanceInKilometers(Place p1, Place p2) {
    return distanceInKilometers(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(),p2.getLongitude());
  }  
  
  public double distanceInKilometers(double lat1, double lon1, double lat2, double lon2) {
    return GeoMath.greatCircleDistance(lat1, lon1, lat2, lon2) * MEAN_RADIUS_IN_KILOMETERS;        
  }
  
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
  
  public int getSize() {
    return places.size();
  }
  
}
