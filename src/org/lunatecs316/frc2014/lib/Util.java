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

    /**
     * Check if a value is within a given range
     * @param value the value to check
     * @param lower the lower bound
     * @param upper the upper bound
     * @return true if the value is in between the lower and upper bounds, else
     */
    public static boolean inRange(double value, double lower, double upper) {
        return value >= lower && value < upper;
    }
}
