package org.quickgeo.data;

import org.quickgeo.PostalDb;
import org.quickgeo.PostalDbFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        PostalDb db = PostalDbFactory.build();      
        db.withinMilesOf(db.byPostalCode("18431").get(0), 15);
        System.out.println("Initialized postalDB with " + db.getSize() + " places");
        
    }
}
