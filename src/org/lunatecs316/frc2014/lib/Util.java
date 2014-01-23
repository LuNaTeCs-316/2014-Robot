package org.lunatecs316.frc2014.lib;

/**
 * Utility functions
 * @author Domenic Rodriguez
 */
public class Util {
    /**
     * Deadband (or dead zone) function
     * @param value the value to check
     * @param db the deadband
     * @return If the value is within the deadband, return the value.
     * Otherwise, return 0
     */
    public static double deadband(double value, double db) {
        return (Math.abs(value) < Math.abs(db)) ? 0 : value;
    }
}
