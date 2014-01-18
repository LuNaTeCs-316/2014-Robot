package org.lunatecs316.frc2014;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;

/**
 * Robot Constants manager. All constants have a String key and double value
 * @author Domenic
 */
public class Constants {
    
    private static final String kFilename = "Constants.txt";
    private static Hashtable constants = new Hashtable();
    
    /**
     * Update the constants. Read the latest values from the constants file
     */
    public static void update() {
        try {
            // Open the connection to the file
            FileConnection file = (FileConnection) Connector.open("file:///" + kFilename, Connector.READ);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.openDataInputStream()));
            
            // Read in each line of the constants file
            Vector lines = new Vector();
            String l;
            while ((l = reader.readLine()) != null) {
                lines.addElement(l);
            }
            
            // Close the file connections
            reader.close();
            file.close();
            
            // Parse each line
            Enumeration e = lines.elements();
            while (e.hasMoreElements()) {
                // Get the next line
                l = (String) e.nextElement();

                // Ignore comment lines and whitespace
                if (l.startsWith("#") || l.equals(""))
                    continue;

                // Seperate into name and value
                int index = l.indexOf("=");
                
                // Ensure that the equals sign was found
                if (index != -1) {
                    String key = l.substring(0, index).trim();
                    double value = Double.parseDouble(l.substring(index + 1));
                
                    // Add to the constants vector
                    constants.put(key, new Double(value));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading constants file!");
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Get the value for the specified constant
     * @param key the name of the constant
     * @return the value for the specified constant; 0.0 if constant is not found
     */
    public static double get(String key) {
        Double value = (Double) constants.get(key);
        
        if (value != null)
            return value.doubleValue();
        else {
            System.err.println("Error: key " + key + " not found!");
            return -1.0;
        }
    }
}
