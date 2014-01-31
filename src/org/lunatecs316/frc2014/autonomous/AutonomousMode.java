package org.lunatecs316.frc2014.autonomous;

/**
 * Base for all Autonomous modes
 * @author Domenic Rodriguez
 */
public interface AutonomousMode {
    /**
     * Initialize the autonomous mode
     */
    public void init();

    /**
     * Run one iteration of the autonomous mode
     */
    public void run();
}
