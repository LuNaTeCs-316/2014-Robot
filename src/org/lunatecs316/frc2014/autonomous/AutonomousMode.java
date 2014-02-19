package org.lunatecs316.frc2014.autonomous;

import org.lunatecs316.frc2014.subsystems.Drivetrain;
import org.lunatecs316.frc2014.subsystems.Pickup;
import org.lunatecs316.frc2014.subsystems.Shooter;

/**
 * Base for all Autonomous modes
 * @author Domenic Rodriguez
 */
public abstract class AutonomousMode {
    // Subsystem instances
    protected Drivetrain drivetrain = Drivetrain.getInstance();
    protected Pickup pickup = Pickup.getInstance();
    protected Shooter shooter = Shooter.getInstance();

    /**
     * Initialize the autonomous mode
     */
    public abstract void init();

    /**
     * Run one iteration of the autonomous mode
     */
    public abstract void run();
}
