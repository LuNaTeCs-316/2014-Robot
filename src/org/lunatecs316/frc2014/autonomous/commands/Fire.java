package org.lunatecs316.frc2014.autonomous.commands;

/**
 * Fire the ball
 * @author Domenic Rodriguez
 */
public class Fire extends AutonomousCommand {
    public void init() {
        shooter.fire();
    }

    public void run() {
    }

    public boolean isFinished() {
        return true;
    }

    public void end() {
    }
}
