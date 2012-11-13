/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo.generate;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 */
public final class TemplateWriter {
  
  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  public static void writeTemplates(ImmutableMap<String, InputStream> input) {
    for (Entry<String, InputStream> entry : input.entrySet()) {
      writeTemplate(entry.getKey(), entry.getValue());
    }
  }
  
  
  private static void writeTemplate(String key, InputStream data) {
  
    try {
      
      // Make required folders
      File moduleFolder = new File(Settings.getSettings().getProjectFolder(), "quickgeo-" + key.toLowerCase());
      moduleFolder.mkdirs();
      Settings.getSettings().getLogger().log(Level.INFO, "Writing folder{0}", moduleFolder.getPath());
      
      File pomFile = new File(moduleFolder, "pom.xml");
      
      copyPomFile(key, pomFile);
      
      writeServicesFile(key, moduleFolder);
      
      writeClassFile(key, moduleFolder);
      
      writeDataFile(key, moduleFolder, data);
      
    } catch (Exception ex) {
      Settings.getSettings().getLogger().log(Level.INFO, "Couldn't write out template", ex);
    }
  }
  
  
  private static void copyPomFile(String key, File pomFile) throws Exception {
  
    String pom = FileUtils.readFileToString(new File(new File(Settings.getSettings().getGenerationFolder(), "template"), "pom.xml"), "UTF-8");
    pom = pom.replaceAll(";cc;", key.toLowerCase());
    pom = pom.replaceAll(";CC;", key.toUpperCase());
    
    FileUtils.writeStringToFile(pomFile, pom, "UTF-8");
  }
  
  private static void writeServicesFile(String key, File moduleFolder) throws Exception {
    File servicesFolder = new File(new URI("file://" + moduleFolder.getPath() + "/src/main/resources/META-INF/services"));
    servicesFolder.mkdirs();
    String serviceString = "org.quickgeo.data." + key.toUpperCase();
    File servicesFile = new File(servicesFolder, "org.quickgeo.PostalSource");
    FileUtils.writeStringToFile(servicesFile, serviceString, "UTF-8");
    
  }
  
  private static void writeClassFile(String key, File moduleFolder) throws Exception {
    String classData = FileUtils.readFileToString(new File(new File(Settings.getSettings().getGenerationFolder(), "template"), "cc.java"), "UTF-8");
    classData = classData.replaceAll(";cc;", key.toLowerCase());
    classData = classData.replaceAll(";CC;", key.toUpperCase());
    
    File classFolder = new File(new URI("file://" + moduleFolder.getPath() + "/src/main/java/org/quickgeo/data"));
    classFolder.mkdirs();
    File classFile = new File(classFolder, key.toUpperCase() + ".java");
    FileUtils.writeStringToFile(classFile, classData, "UTF-8");    
  }  
  
  private static void writeDataFile(String key, File moduleFolder, InputStream data) throws Exception {
    File resourcesFolder = new File(new URI("file://" + moduleFolder.getPath() + "/src/main/resources"));
    resourcesFolder.mkdirs();
    File dataFile = new File(resourcesFolder, key.toUpperCase() + ".txt");
    FileOutputStream fos = new FileOutputStream(dataFile);
    IOUtils.copy(data, fos);
    data.close();
    fos.close();
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
