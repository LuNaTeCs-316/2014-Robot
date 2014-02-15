package org.lunatecs316.frc2014.autonomous;

import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.lib.IterativeTimer;
import org.lunatecs316.frc2014.lib.Logger;

/**
 * More advanced autonomous. Fire two balls in autonomous
 * @author Domenic Rodriguez
 */
public class TwoBallAutonomous extends AutonomousMode {
    private static final int kDriveForwards = 0;
    private static final int kFire = 1;
    private static final int kDriveBackAndReload = 2;
    private static final int kPickupSecondBall = 3;
    private static final int kWaitForReload = 4;
    private static final int kDone = 5;

    private IterativeTimer stateTimer = new IterativeTimer();
    private int state;
    private boolean firstShot;

    /**
     * @see AutonomousMode#init()
     */
    public void init() {
        // Set the intial states for the robot subsystems
        pickup.lower();
        pickup.setRollerSpeed(-1.0);
        drivetrain.shiftDown();
        drivetrain.resetGyro();
        drivetrain.resetEncoders();
        drivetrain.disableSafety();

        // Reset the state timer
        stateTimer.setExpiration(3250);

        // Set the default state
        state = kDriveForwards;
        firstShot = true;

        Logger.debug("BasicAutonomous#init", "State: kDrivingForwards");
    }

    /**
     * @see AutonomousMode#run()
     */
    public void run() {
        switch (state) {
            case kDriveForwards:
                drivetrain.driveStraightDistance(20700);
                shooter.setPosition(Constants.Shooter10ft.getValue());
                if (stateTimer.hasExpired()) {
                    pickup.setRollerSpeed(0.0);
                    drivetrain.arcadeDrive(0.0, 0.0);
                    shooter.setWinch(0.0);
                    state = kFire;
                    Logger.debug("TwoBallAutonomous#run", "State: kFire");
                    stateTimer.setExpiration(Constants.ShooterResetTime.getValue() + 100);
                }
                break;
            case kFire:
                shooter.fire();
                if (stateTimer.hasExpired()) {
                    if (firstShot) {
                        firstShot = false;
                        state = kDriveBackAndReload;
                        Logger.debug("TwoBallAutonomous#run", "State: kDriveBackAndReload");
                        stateTimer.setExpiration(3250);
                    } else {
                        state = kWaitForReload;
                    }
                }
                break;
            case kDriveBackAndReload:
                drivetrain.driveStraightDistance(-20700);
                if (stateTimer.hasExpired()) {
                    drivetrain.arcadeDrive(0.0, 0.0);
                    pickup.setRollerSpeed(-1.0);
                    state = kPickupSecondBall;
                    Logger.debug("TwoBallAutonomous#run", "State: kPickupSecondBall");
                    stateTimer.setExpiration(1500);
                }
                break;
            case kPickupSecondBall:
                if (stateTimer.hasExpired()) {
                    pickup.setRollerSpeed(0.0);
                    state = kDriveForwards;
                }
                break;
            case kWaitForReload:
                if (shooter.atLoadingPosition() || stateTimer.hasExpired()) {
                    state = kDone;
                    Logger.debug("BasicAutonomous#run", "State: kDone");
                }
                break;
            case kDone:
                shooter.setWinch(0.0);
                drivetrain.enableSafety();
                drivetrain.arcadeDrive(0.0, 0.0);
                break;
            default:
                Logger.error("BasicAutonomous", "Invalid autonomous state");
                break;
        }
    }
}
