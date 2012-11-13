/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */

package org.quickgeo.generate;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;

/**
 *
 */
public class Generate {
  
  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  public static void main(String[] args) throws Exception {
    Dataset ds = Dataset.generate();    
    ImmutableMap<String, File> fileMap = Downloader.download(ds);
    ImmutableMap<String, InputStream> streamMap = convertToStreams(fileMap);
    deleteExisting(streamMap.keySet());
    TemplateWriter.writeTemplates(streamMap);
    System.out.println("asdf");
  }
  
  private static ImmutableMap<String, InputStream> convertToStreams(ImmutableMap<String, File> map) {
    ImmutableMap.Builder<String, InputStream> builder = new ImmutableMap.Builder<String, InputStream>();
    
    for (Entry<String, File> entry : map.entrySet()) {
      String cc = entry.getKey();
      File zipFile = entry.getValue();     
      try {
        ZipFile zf = new ZipFile(zipFile);
        ZipEntry ze = zf.getEntry(cc + ".txt");
        if (ze != null) {
          builder.put(cc, zf.getInputStream(ze));
        }
      }catch (Exception ex) {
        Settings.getSettings().getLogger().log(Level.WARNING, "Couldn't parse entry for " + cc, ex);
      }
      
    }
    
    return builder.build();
  }
  
  private static void deleteExisting(Set<String> entries) {
    for (String entry : entries) {
      File f = new File(Settings.getSettings().getProjectFolder(), "quickgeo-" + entry.toLowerCase());
      Settings.getSettings().getLogger().log(Level.INFO, "Deleting folder {0}", f.getPath());
      try {
        FileUtils.deleteDirectory(f);
      } catch (Exception ex) {
        Settings.getSettings().getLogger().log(Level.WARNING, "Couldn't delete folder", ex);
      }
    }
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
