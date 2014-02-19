package org.lunatecs316.frc2014.autonomous.commands;

import org.lunatecs316.frc2014.subsystems.Drivetrain;
import org.lunatecs316.frc2014.subsystems.Pickup;
import org.lunatecs316.frc2014.subsystems.Shooter;

/**
 * Command for an Autonomous sequence
 * @author Domenic Rodriguez
 */
public abstract class AutonomousCommand {
    protected Drivetrain drivetrain = Drivetrain.getInstance();
    protected Pickup pickup = Pickup.getInstance();
    protected Shooter shooter = Shooter.getInstance();

    /**
     * Called on the first run
     */
    public abstract void init();

    /**
     * Called every loop
     */
    public abstract void run();

    /**
     * Called every loop
     * @return if the command is finished
     */
    public abstract boolean isFinished();

    /**
     * Called once after isFinished() returns true
     */
    public abstract void end();
}
