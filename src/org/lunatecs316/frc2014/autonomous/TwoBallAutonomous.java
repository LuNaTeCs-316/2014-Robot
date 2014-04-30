package org.lunatecs316.frc2014.autonomous;

import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.lib.Logger;
import org.lunatecs316.frc2014.lib.IterativeTimer;

/**
 * Two ball autonomous. Drive forwards (dragging the second ball)
 * and score two balls in the high goal.
 * @author Domenic Rodriguez
 */
public class TwoBallAutonomous extends AutonomousMode {
    private static final int kDriveForwards = 0;
    private static final int kFireFirstShot = 1;
    private static final int kWaitForReload = 2;
    private static final int kReload = 3;
    private static final int kFireSecondShot = 4;

    private IterativeTimer stateTimer = new IterativeTimer();
    private int state;
    private boolean done;

    /**
     * @see AutonomousMode#init()
     */
    public void init() {
        // Set the intial states for the robot subsystems
        pickup.setRollerSpeed(-0.85);
        drivetrain.shiftUp();

        // Reset the state timer
        stateTimer.setExpiration(3500);

        // Set the default state
        state = kDriveForwards;
        done = false;

        Logger.debug("TwoBallAutonomous#init", "State: kDrivingForwards");
    }

    /**
     * @see AutonomousMode#run()
     */
    public void run() {
        if (!done) {
            switch (state) {
                case kDriveForwards:
                    if (!pickup.isLowered()) {
                        stateTimer.reset();
                    } else {
                        drivetrain.driveStraightDistance(Constants.Drivetrain8ft.getValue());
                        shooter.setPosition(Constants.AutonomousShooterSetpoint.getValue());
                        if (drivetrain.atTarget() || stateTimer.hasExpired()) {
                            pickup.setRollerSpeed(0.5);
                            drivetrain.arcadeDrive(0.0, 0.0);
                            state = kFireFirstShot;
                            Logger.debug("TwoBallAutonomous#run", "State: kFire");
                            stateTimer.reset();
                        }
                    }
                    break;
                case kFireFirstShot:
                    shooter.setWinch(0.0);
                    pickup.setRollerSpeed(0.0);
                    shooter.fire();
                    state = kWaitForReload;
                    Logger.debug("TwoBallAutonomous#run", "State: kWaitForReload");
                    stateTimer.setExpiration(3250);
                    break;
                case kWaitForReload:
                    if (stateTimer.getValue() > Constants.ShooterResetTime.getValue())
                        shooter.setPosition(Constants.AutonomousShooterSetpoint.getValue());
                    if (stateTimer.hasExpired()) {
                        state = kReload;
                        Logger.debug("TwoBallAutonomous#run", "State: kReload");
                        stateTimer.setExpiration(1350);
                    }
                    break;
                case kReload:
                    pickup.setRollerSpeed(-1.0);
                    shooter.setPosition(Constants.AutonomousShooterSetpoint.getValue());
                    if (stateTimer.hasExpired()) {
                        pickup.setRollerSpeed(0.0);
                        state = kFireSecondShot;
                        Logger.debug("TwoBallAutonomous#run", "State: kFireSecondShot");
                        stateTimer.reset();
                    }
                    break;
                case kFireSecondShot:
                    if (stateTimer.getValue() > 750) {
                        shooter.setWinch(0.0);
                        shooter.fire();
                        done = true;
                        Logger.debug("TwoBallAutonomous#run", "Done!");
                    }
                    break;
                default:
                    Logger.error("TwoBallAutonomous#run", "Invalid autonomous state");
                    break;
            }
        }
    }
}
