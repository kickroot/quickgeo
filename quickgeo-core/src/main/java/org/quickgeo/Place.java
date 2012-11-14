/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo;

import java.io.Serializable;
import javax.annotation.concurrent.Immutable;

/**
 * Represents a physical village, town, city, etc.  Place instances are immutable
 * and may be freely shared amongst threads.
 * 
 * This class is a container for place information as defined by the GeoNames 
 * source at http://download.geonames.org/export/zip/
 * <pre>
 * country code      : iso country code, 2 characters
 * postal code       : varchar(20)
 * place name        : varchar(180)
 * admin name1       : 1. order subdivision (state) varchar(100)
 * admin code1       : 1. order subdivision (state) varchar(20)
 * admin name2       : 2. order subdivision (county/province) varchar(100)
 * admin code2       : 2. order subdivision (county/province) varchar(20)
 * admin name3       : 3. order subdivision (community) varchar(100)
 * admin code3       : 3. order subdivision (community) varchar(20)
 * latitude          : estimated latitude (wgs84)
 * longitude         : estimated longitude (wgs84)
 * accuracy          : accuracy of lat/lng from 1=estimated to 6=centroid
 * </pre>
 * 
 * Member names aren't exactly obvious, but in the US they are mapped as such:
 * 
 * <pre>
 * postal code = zip Code
 * place name = city name
 * adminName1 = state name (Washington)
 * adminCode1 = state abbreviation (WA)
 * adminName2 = county (King)
 * adminCode2 = county code (33)
 * adminName3 = unused
 * adminCode3 = unused
 * </pre>
 * 
 * @since 0.1.0
 * @author Jason Nichols (jason@kickroot.com
 */
@Immutable
public final class Place implements Serializable {
  
  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    
  
  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\  
  
  //////////////////////////////// Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  private final String countryCode;
  
  private final String postalCode;
  
  private final String placeName;
  
  private final String adminName1;
  
  private final String adminCode1;
  
  private final String adminName2;
  
  private final String adminCode2;
  
  private final String adminName3;
  
  private final String adminCode3;  
  
  private final double latitude;
  
  private final double longitude;
  
  private final Integer accuracy;
  
  /////////////////////////////// Constructors \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\  
  
  public Place(String countryCode, String postalCode, String placeName,
          String adminName1, String adminCode1, String adminName2, String adminCode2,
          String adminName3, String adminCode3, double latitude, double longitude,
          Integer accuracy) {
    this.countryCode = countryCode;
    this.postalCode = postalCode;
    this.placeName = placeName;
    this.adminName1 = adminName1;
    this.adminCode1 = adminCode1;
    this.adminName2 = adminName2;
    this.adminCode2 = adminCode2;
    this.adminName3 = adminName3;
    this.adminCode3 = adminCode3;
    this.latitude = latitude;
    this.longitude = longitude;
    this.accuracy = accuracy;    
  }
  
  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  //------------------------ Implements:
  
  //------------------------ Overrides:
  
  //---------------------------- Abstract Methods -----------------------------
  
  //---------------------------- Utility Methods ------------------------------
  
  //---------------------------- Property Methods -----------------------------     

  /**
   * @return the countryCode
   */
  public String getCountryCode() {
    return countryCode;
  }

  /**
   * @return the postalCode
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * @return the placeName
   */
  public String getPlaceName() {
    return placeName;
  }

  /**
   * @return the adminName1
   */
  public String getAdminName1() {
    return adminName1;
  }

  /**
   * @return the adminCode1
   */
  public String getAdminCode1() {
    return adminCode1;
  }

  /**
   * @return the adminName2
   */
  public String getAdminName2() {
    return adminName2;
  }

  /**
   * @return the adminCode2
   */
  public String getAdminCode2() {
    return adminCode2;
  }

  /**
   * @return the adminName3
   */
  public String getAdminName3() {
    return adminName3;
  }

  /**
   * @return the adminCode3
   */
  public String getAdminCode3() {
    return adminCode3;
  }

  /**
   * @return the latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * @return the longitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * @return the accuracy
   */
  public Integer getAccuracy() {
    return accuracy;
  }

}
