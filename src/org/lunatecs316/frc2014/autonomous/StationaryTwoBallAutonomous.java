package org.lunatecs316.frc2014.autonomous;

import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.lib.IterativeTimer;
import org.lunatecs316.frc2014.lib.Logger;

/**
 * More advanced autonomous. Fire two balls in autonomous (without moving)
 * @author Domenic Rodriguez
 */
public class StationaryTwoBallAutonomous extends AutonomousMode {
    private static final int kFire = 0;
    private static final int kWaitForReload = 1;
    private static final int kReload = 2;
    private static final int kDriveForwards = 3;
    private static final int kDone = 4;

    private IterativeTimer stateTimer = new IterativeTimer();
    private int state;
    private boolean firstShot;

    public void init() {
        state = kFire;
        firstShot = true;
        stateTimer.reset();
        Logger.debug("StationaryTwoBallAutonomous#init", "State: kFire");
    }

    public void run() {
        switch (state) {
            case kFire:
                if (stateTimer.getValue() < 500) {
                    pickup.setRollerSpeed(-1.0);
                } else {
                    pickup.setRollerSpeed(0.0);
                }

                if (pickup.isLowered()) {
                    pickup.setRollerSpeed(0.0);
                    shooter.fire();

                    if (firstShot) {
                        firstShot = false;
                        state = kWaitForReload;
                        Logger.debug("StationaryTwoBallAutonomous#run", "State: kWaitForReload");
                        stateTimer.setExpiration(4000);
                    } else {
                        state = kDriveForwards;
                        Logger.debug("StationaryTwoBallAutonomous#run", "State: kDriveForwards");
                        stateTimer.setExpiration(2125);
                    }
                }
                break;
            case kWaitForReload:
                // Give the shooter some time to move
                if (stateTimer.getValue() < Constants.ShooterResetTime.getValue())
                    state = kWaitForReload;
                else if (shooter.atLoadingPosition() || stateTimer.hasExpired()) {
                    state = kReload;
                    Logger.debug("StationaryTwoBallAutonomous#run", "State: kReload");
                    stateTimer.setExpiration(2500);
                }
                break;
            case kReload:
                pickup.setRollerSpeed(-1.0);
                if (stateTimer.hasExpired()) {
                    pickup.setRollerSpeed(0.0);
                    state = kFire;
                    Logger.debug("StationaryTwoBallAutonomous#run", "State: kFire");
                }
                break;
            case kDriveForwards:
                if (stateTimer.getValue() < Constants.ShooterResetTime.getValue())  // Slight delay for the shooter
                    drivetrain.arcadeDrive(0.0, 0.0);
                else
                    drivetrain.driveStraight(-0.7);

                if (stateTimer.hasExpired()) {
                    drivetrain.arcadeDrive(0.0, 0.0);
                    state = kDone;
                    Logger.debug("StationaryTwoBallAutonomous#run", "State: kDone");
                }
                break;
            case kDone:
                // Cleanup
                drivetrain.arcadeDrive(0.0, 0.0);
                pickup.setRollerSpeed(0.0);
                break;
            default:
                Logger.error("StationaryTwoBallAutonomous#run", "Invalid autonomous state");
                break;
        }
    }
}
