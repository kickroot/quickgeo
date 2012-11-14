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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class for creating PostalDb instances.  It really is as simple as it
 * looks, just use the {@link #build() build} method and use the returned
 * PostalDb instance.
 * 
 * @author Jason Nichols (jason@kickroot.com)
 * @since 0.1.0
 */
public class PostalDbFactory {

  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  private static ServiceLoader<PostalSource> SERVICE_LOADER = ServiceLoader.load(PostalSource.class);

  private static final PostalDb db = build();
  
  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  /**
   * Returns a VM wide {@link PostalDb} instance.  Given that instantiating a PostalDb
   * instance is resource intensive, a single instance is generated at class
   * loading time with a static initializer and used throughout the VM.
   * 
   * Since PostalDb instances are immutable, sharing a single instance amongst
   * threads is perfectly safe, and in this case preferable.
   * 
   * @return The previously generated PostalDb instance.
   * @since 0.1.0
   */
  public static PostalDb getPostalDb() {
    return db;
  }
  
  /**
   * Builds a new PostalDb instance, with all postal data stored in memory.  This process
   * is somewhat resource intensive and should only be done once per application
   * instance. Given that {@link PostalDb} instances are immutable, the instance should 
   * be freely shared among threads.
   * 
   * @return A new PostalDb instance.
   * @since 0.1.0
   */
  private static PostalDb build() {

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
            Logger.getLogger(PostalDbFactory.class.getName()).log(Level.INFO, "Couldn''t read line : {0}", ex.getMessage());
          }
        }
        in.close();
      } catch (Exception ex) {
        Logger.getLogger(PostalDbFactory.class.getName()).log(Level.INFO, "Couldn''t read source : {0}", ex.getMessage());
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
