/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo.generate;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;

/**
 *
 */
public final class Downloader {
  
  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  private static final String URL_STRING = "http://download.geonames.org/export/zip/;cc;.zip";
  
  private static final String REPLACE_ARTIFACT = "quickgeo-";
  
  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  public static final ImmutableMap<String, File> download(Dataset dataset) {
    
    ImmutableMap.Builder<String, File> builder = new ImmutableMap.Builder<String, File>();
    
    for (String artifact : dataset.getArtifacts()) {
      try {
        String cc = artifact.replaceAll(REPLACE_ARTIFACT, "").toUpperCase();
        String url = URL_STRING.replaceAll(";cc;", cc);        
        
        File tempFile = File.createTempFile("geo-" + cc + System.currentTimeMillis(), null);        
        FileUtils.copyURLToFile(new URL(url), tempFile);
        builder.put(cc, tempFile);
        
        Settings.getSettings().getLogger().log(Level.INFO, "Downloaded file from {0} to {1}", new Object[]{url, tempFile.getPath()}) ;
        
      } catch (Exception ex) {
        Settings.getSettings().getLogger().log(Level.WARNING, "Couldn't fetch artifact", ex);
      }
    }
    
    return builder.build();    
  }
  
  //////////////////////////////// Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  /////////////////////////////// Constructors \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\  
  
  private Downloader(){}
  
  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  //------------------------ Implements:
  
  //------------------------ Overrides:
  
  //---------------------------- Abstract Methods -----------------------------
  
  //---------------------------- Utility Methods ------------------------------
  
  //---------------------------- Property Methods -----------------------------     

}
