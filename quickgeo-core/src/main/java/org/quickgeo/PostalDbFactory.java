/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo;

import com.google.common.collect.Sets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 *
 */
public class PostalDbFactory {

  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  private static ServiceLoader<PostalSource> SERVICE_LOADER = ServiceLoader.load(PostalSource.class);

  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  public static PostalDb build() {

    LinkedHashSet<Place> set = Sets.newLinkedHashSet();
    
    for (PostalSource source : SERVICE_LOADER) {
      try {
        BufferedReader in = new BufferedReader(new InputStreamReader(source.getStream()));
        String line = null;

        while ((line = in.readLine()) != null) {
          try {
            Place p = PlaceFactory.fromLine(line);
            set.add(p);          
          } catch (Exception ex) {
            Logger.getLogger(PostalDbFactory.class.getName()).info("Couldn't read line : " + ex.getMessage());
          }
        }
        in.close();
      } catch (Exception ex) {
        Logger.getLogger(PostalDbFactory.class.getName()).info("Couldn't read source : " + ex.getMessage());
      }
    }

    return new PostalDb(set);
  }
  
  //////////////////////////////// Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  /////////////////////////////// Constructors \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\  
  
  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  //------------------------ Implements:
  
  //------------------------ Overrides:
  
  //---------------------------- Abstract Methods -----------------------------
  
  //---------------------------- Utility Methods ------------------------------
  
  //---------------------------- Property Methods -----------------------------     
}
