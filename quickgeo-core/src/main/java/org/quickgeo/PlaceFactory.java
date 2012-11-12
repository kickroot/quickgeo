package org.quickgeo;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public final class PlaceFactory {
  
  ///////////////////////////// Class Attributes \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
  private static final Splitter SPLITTER = Splitter.on("\t").trimResults();

  ////////////////////////////// Class Methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  public static Place fromLine(String line) {
    Iterable<String> iter = SPLITTER.split(line);
       
    String[] items = Lists.newArrayList(iter).toArray(new String[0]);
    
    Integer accuracy = null;
    
    if (items[11] != null && !items[11].isEmpty()) {
      accuracy = Integer.parseInt(items[11]);
    }
    
    return new Place(items[0], items[1], items[2], items[3], items[4], items[5],
            items[6], items[7], items[8], Double.parseDouble(items[9]),
            Double.parseDouble(items[10]), accuracy);    
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
