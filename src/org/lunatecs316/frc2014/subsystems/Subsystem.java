package org.lunatecs316.frc2014.subsystems;

/**
 * Interface for all robot subsystems
 * @author Domenic Rodriguez
 */
public interface Subsystem {
    /**
     * Initialize the subsystem
     */
    public void init();
    
    /**
     * Send data to the SmartDashboard
     */
    public void updateSmartDashboard();

    /**
     * Set any values read from constants
     */
    public void updateConstants();
}
