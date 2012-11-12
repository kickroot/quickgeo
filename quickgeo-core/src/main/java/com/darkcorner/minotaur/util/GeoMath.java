 	/* ***** BEGIN LICENSE BLOCK *****
 * Version: DCSPL 1.1
 *
 * The contents of this file are subject to the Dark Corner Software Public
 * License Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.darkcornersoftware.com/DCSPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.  See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Initial Developer of the Original Code is Dark Corner Software LLC.
 * Portions created by Dark Corner Software LLC are Copyright (C) 2005 Dark
 * Corner Software LLC.  All Rights Reserved.
 *
 * Contributor(s):
 *   None
 *
 */
package com.darkcorner.minotaur.util;

import org.quickgeo.Place;

/**
 * More math functions than just {@code java.lang.Math}, including functions to
 * deal with great circles. <p>
 */
public final class GeoMath {
  
  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  /**
   * WGS84 earth eccentricity.
   */
  public static final double EARTH_ECCENTRICITY = 0.08181919084;
  
  /**
   * WGS84 arithmetic mean radius of the Earth.
   */
  public static final double EARTH_MEAN_RADIUS = 6371008.7714;

  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  /**
   * Compute the length of one arc degree of longitude for a given latitude.
   *
   * @param latitude The latitude of the arc length in question, in degrees.
   * @param earthsRadius The radius of the Earth at the equator, in whatever
   * units the desired result should be in. For kilometers use 6378.137, for
   * miles use 3963.191.
   * @return The length of one degree of longitudinal arc for a given latitude.
   * @see <a href="http://en.wikipedia.org/wiki/Longitude#Degree_length">Degree
   * Length</a>
   */
  public static final double lonArcDegreeLength(double latitude, double earthsRadius) {
    
    // Eccentricity of the Earth
    final double lat = Math.toRadians(latitude);

    /*
     * Radius of curvature in the east-west direction:
     * roc = a/sqrt(1 - e^2*sin^2(phi))
     * See http://mathforum.org/library/drmath/view/61089.html
     */
    double roc = earthsRadius / Math.sqrt(1.0 - Math.pow(EARTH_ECCENTRICITY, 2) * Math.pow(Math.sin(lat), 2));

    return (Math.PI / 180.0) * Math.cos(lat) * roc;
  }

  /**
   * Compute the length of one arc degree of latitude for a given latitude.
   *
   * @param latitude The latitude of the arc length in question, in degrees.
   * @param earthsRadius The radius of the Earth at the equator, in whatever
   * units the desired result should be in. For kilometers use 6378.137, for
   * miles use 3963.191.
   * @return The length of one degree of latitudinal arc for a given latitude.
   * @see <a href="http://en.wikipedia.org/wiki/Longitude#Degree_length">Degree
   * Length</a>
   */
  public static final double latArcDegreeLength(double latitude, double earthsRadius) {
    // Eccentricity of the Earth
    final double lat = Math.toRadians(latitude);

    /**
     * Radius of curvature in the north-south direction: roc = a(1 - e^2)/(1 -
     * e^2*sin^2(phi))^3/2 See
     * http://mathforum.org/library/drmath/view/61089.html
     */
    double roc = earthsRadius * (1.0 - Math.pow(EARTH_ECCENTRICITY, 2)) / Math.pow((1.0 - Math.pow(EARTH_ECCENTRICITY, 2)
            * Math.pow(Math.sin(lat), 2)), 1.5);

    return (Math.PI / 180.0) * roc;
  }

  /**
   * Compute the great circle distance between the given positions. The details
   * of this computation are largely based on the implementation in the Perl
   * Math::Trig library. To get a distance in meters, multiply by the
   * {@link #EARTH_MEAN_RADIUS}.
   *
   * @param p0 starting position
   * @param p1 ending position
   * @return the distance as an angle in radians
   * @see <a href="http://en.wikipedia.org/wiki/Great-circle_distance">Great
   * Circle Distance</a>
   */
  public static final double greatCircleDistance(double lat1, double lon1,
          double lat2, double lon2) {
    double long0 = Math.toRadians(lon1);
    double latitude0 = Math.toRadians(lat1);
    double long1 = Math.toRadians(lon2);
    double latitude1 = Math.toRadians(lat2);

    return Math.acos(
            Math.cos(latitude0) * Math.cos(latitude1) * Math.cos(long0 - long1)
            + Math.sin(latitude0) * Math.sin(latitude1));
  }

  /**
   * Compute the great circle direction between the given positions. This is
   * useful when trying to follow a great circle line between two points. <p>
   *
   * The details of this computation are largely based on the implementation in
   * the Perl Math::Trig library.
   *
   * @param p0 starting position
   * @param p1 ending position
   * @return the direction as an angle in radians in the range -PI .. PI, where
   * 0 is due north, and -PI/PI are due south
   */
  public static final double greatCircleDirection(Place p0, Place p1) {
    double long0 = Math.toRadians(p0.getLongitude());
    double lat0 = Math.toRadians(p0.getLatitude());
    double long1 = Math.toRadians(p1.getLongitude());
    double lat1 = Math.toRadians(p1.getLatitude());
    double distance = greatCircleDistance(p0.getLatitude(), p0.getLatitude(),
            p1.getLatitude(), p1.getLongitude());

    double value =
            (Math.sin(lat1) - Math.sin(lat0) * Math.cos(distance))
            / (Math.cos(lat0) * Math.sin(distance));

    // The above calculation is poorly behaved and suffers from rounding error.
    // Given valid values for the positions (verified by the position class),
    // we can safely surpress values greater than 1 to be 1.
    if (value > 1.0) {
      value = 1.0;
    } else if (value < -1.0) {
      value = -1.0;
    }

    double direction = Math.acos(value);

    if (Math.sin(long1 - long0) < 0) {
      direction = Math.PI * 2.0 - direction;
    }

    // This is due to a difference in the underlying algorithms function to 
    //  bring this into the right direction and our different IEEEremainder
    //  function.  The perl algorithm uses int() which rounds towards 0, never
    //  a good thing, except here.  IEEEremainder rounds towards even.  So to
    //  deal with this as a special case, handle Math.PI different, just return
    //  0.

    if (Math.abs(direction - Math.PI) <= Math.ulp(Math.PI)) {
      return 0.0;
    } else {
      return Math.IEEEremainder(direction, Math.PI * 2.0);
    }
  }

  //////////////////////////////// Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  /////////////////////////////// Constructors \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  /**
   * No instances of this class may be instaniated.
   */
  private GeoMath() {
  }
  
  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  //------------------------ Implements:
  
  //------------------------ Overrides:
  
  //---------------------------- Abstract Methods -----------------------------
  
  //---------------------------- Utility Methods ------------------------------
  
  //---------------------------- Property Methods -----------------------------
}
