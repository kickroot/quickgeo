/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo.generate;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 */
public final class Settings {
  
  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  private static final Settings SETTINGS = new Settings();
  
  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  public static Settings getSettings() {
    return SETTINGS;
  }
  
  //////////////////////////////// Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  private final Pattern modulePattern = Pattern.compile("<module>(.+)</module>", Pattern.CASE_INSENSITIVE);
  
  private final File projectFolder = new File(System.getProperty("user.dir")).getParentFile();
  
  private final File parentPomFile = new File(projectFolder, "pom.xml");
  
  private final ImmutableList<String> blacklist = ImmutableList.of("quickgeo-core", "quickgeo-generate");  
  
  private final Logger logger = Logger.getLogger("QuickGeo Generator");  

  private final File generationFolder = new File(projectFolder, "quickgeo-generate");
  
  /////////////////////////////// Constructors \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\  
  
  private Settings(){}
  
  ////////////////////////////////// Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  //------------------------ Implements:
  
  //------------------------ Overrides:
  
  //---------------------------- Abstract Methods -----------------------------
  
  //---------------------------- Utility Methods ------------------------------
  
  //---------------------------- Property Methods -----------------------------     


  /**
   * @return the modeulePattern
   */
  public Pattern getModulePattern() {
    return modulePattern;
  }

  /**
   * @return the projectFolder
   */
  public File getProjectFolder() {
    return projectFolder;
  }

  /**
   * @return the parentPomFile
   */
  public File getParentPomFile() {
    return parentPomFile;
  }

  /**
   * @return the blacklist
   */
  public ImmutableList<String> getBlacklist() {
    return blacklist;
  }
 
  public Logger getLogger() {
    return logger;
  }
  
  public File getGenerationFolder() {
    return generationFolder;
  }
}
