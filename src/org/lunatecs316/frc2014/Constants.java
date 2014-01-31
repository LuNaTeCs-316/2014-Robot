package org.lunatecs316.frc2014;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import org.lunatecs316.frc2014.lib.Logger;
import org.lunatecs316.frc2014.subsystems.Drivetrain;
import org.lunatecs316.frc2014.subsystems.Pickup;
import org.lunatecs316.frc2014.subsystems.Shooter;

/**
 * Robot Constants manager. All constants have a String key and double value
 * @author Domenic Rodriguez
 */
public class Constants {
    private static final String kFilename = "Constants.txt";
    // This MUST come before the creation of any constants!
    private static Hashtable constants = new Hashtable();
    
    public static final Constant DashboardUpdateFrequency = new Constant("DashboardUpdateFrequency", 10.0);
    public static final Constant JoystickDeadband = new Constant("JoystickDeadband", 0.2);
    public static final Constant DrivetrainSkimGain = new Constant("DrivetrainSkimGain", 0.5);
    public static final Constant DrivetrainDistanceP = new Constant("DrivetrainDistanceP", 1.0);
    public static final Constant DrivetrainDistanceI = new Constant("DrivetrainDistanceI", 0.0);
    public static final Constant DrivetrainDistanceD = new Constant("DrivetrainDistanceD", 0.0);
    public static final Constant DrivetrainAngleP = new Constant("DrivetrainAngleP", 1.0);
    public static final Constant DrivetrainAngleI = new Constant("DrivetrainAngleI", 0.0);
    public static final Constant DrivetrainAngleD = new Constant("DrivetrainAngleD", 0.0);
    public static final Constant ShooterResetTime = new Constant("ShooterResetTime", 3000);
    
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
        Logger.info("Constants.update()", "Reading constants from file '" + kFilename + "'");
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
                        Logger.error("Constants.update()", "Constant '" + key + "' not found");
                    }
                }
            }
        } catch (IOException e) {
            Logger.error("Constants.update()", "Error reading constants file!");
            Logger.error("Constants.update()", e.getMessage());
        }

        // Trigger update in subsystems
        Drivetrain.getInstance().updateConstants();
        Pickup.getInstance().updateConstants();
        Shooter.getInstance().updateConstants();
    }
}
