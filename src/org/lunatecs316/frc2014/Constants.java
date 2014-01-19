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
    // This MUST come before the creation of any constants!
    private static Hashtable constants = new Hashtable();
    
    public static final Constant kDashboardUpdateFrequency = new Constant("kDashboardUpdateFrequency", 10.0);
    public static final Constant kJoystickDeadband = new Constant("kJoystickDeadband", 0.2);
    public static final Constant kDrivetrainSkimGain = new Constant("kDrivetrainSkimGain", 0.5);
    
    /**
     * Representation of a single constant value
     */
    public static final class Constant {
        private String name;
        private double value;
        
        public Constant(String name, double val) {
            this.name = name;
            value = val;
            Constants.constants.put(name, this);
        }
        
        public void setValue(double val) {
            value = val;
        }
        
        public double getValue() {
            return value;
        }
        
        public String getName() {
            return name;
        }
    }

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
                
                    // Look for the matching Constant
                    Constant c = (Constant) constants.get(key);
                    if (c != null) {
                        c.setValue(value);
                    } else {
                        System.err.println("Constant '" + key + "' not found");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading constants file!");
            System.err.println(e.getMessage());
        }
    }
}
