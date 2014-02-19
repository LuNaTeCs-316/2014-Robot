package org.lunatecs316.frc2014.autonomous.commands;

/**
 * Drive straight for a distance
 * @author Domenic Rodriguez
 */
public class DriveStraight extends AutonomousCommand {
    private double m_distance;

    public DriveStraight(double distance) {
        m_distance = distance;
    }

    public void init() {
    }

    public void run() {
        drivetrain.driveStraightDistance(m_distance);
    }

    public boolean isFinished() {
        return drivetrain.atTarget();
    }

    public void end() {
        drivetrain.arcadeDrive(0.0, 0.0);
    }

}
