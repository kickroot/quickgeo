/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo;

import java.io.Serializable;
import javax.annotation.concurrent.Immutable;

/**
 *
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
