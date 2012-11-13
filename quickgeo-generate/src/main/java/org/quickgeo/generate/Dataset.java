/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo.generate;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.regex.Matcher;
import org.apache.commons.io.FileUtils;

/**
 * Generates a list of data modules based off the QuickGeo Parent POM.
 */
public class Dataset {
  
  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            
  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  public static Dataset generate() throws IOException {
    
    ImmutableList.Builder<String> builder = new ImmutableList.Builder<String>();        
    
    Settings settings = Settings.getSettings();
    
    String pom = FileUtils.readFileToString(settings.getParentPomFile(), "UTF-8");
    Matcher m = settings.getModulePattern().matcher(pom);
    while(m.find()) {
      String artifact = m.group(1);
      if (!settings.getBlacklist().contains(artifact)) {        
        builder.add(artifact);
      }
    }
    
    return new Dataset(builder.build());
  }
  
  //////////////////////////////// Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  private final ImmutableList<String> artifacts;
  
  /////////////////////////////// Constructors \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\  
  
  private Dataset(ImmutableList<String> artifacts) {
    this.artifacts = artifacts;
  }
  
  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  //------------------------ Implements:
  
  //------------------------ Overrides:
  
  //---------------------------- Abstract Methods -----------------------------
  
  //---------------------------- Utility Methods ------------------------------
  
  //---------------------------- Property Methods -----------------------------     
  
  public ImmutableList<String> getArtifacts() {
    return artifacts;
  }

}
