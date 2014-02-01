package org.lunatecs316.frc2014.autonomous;

import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.Robot;
import org.lunatecs316.frc2014.lib.Logger;
import org.lunatecs316.frc2014.lib.Timer;

/**
 * Basic autonomous mode. Score the ball in the high goal
 * @author Domenic Rodriguez
 */
public class BasicAutonomous extends AutonomousMode {
    private static final int kDrivingForwards = 0;
    private static final int kCheckForHotGoal = 1;
    private static final int kWaitForHotGoal = 2;
    private static final int kFire = 3;
    private static final int kReload = 4;
    private static final int kDone = 5;

    private Timer timer = new Timer();
    private int state;

    /**
     * @see AutonomousMode#init()
     */
    public void init() {
        // Set the intial states for the robot subsystems
        pickup.lower();
        drivetrain.shiftDown();

        // Reset the state timer
        timer.setExpiration(3000);

        // Set the default state
        state = kDrivingForwards;
    }

    /**
     * @see AutonomousMode#run()
     */
    public void run() {
        switch (state) {
            case kDrivingForwards:
                drivetrain.arcadeDrive(0.7, 0.0);
                if (timer.hasExpired()) {
                    drivetrain.arcadeDrive(0.0, 0.0);
                    state = kCheckForHotGoal;
                }
                break;
            case kCheckForHotGoal:
                if (Robot.visionData.getBoolean("goalIsHot", true)) {
                    state = kFire;
                } else {
                    state = kWaitForHotGoal;
                    timer.setExpiration(5000);
                }
                break;
            case kWaitForHotGoal:
                if (timer.hasExpired()) {
                    state = kFire;
                    timer.setExpiration(3500);
                }
                break;
            case kFire:
                shooter.fire();
                if (timer.hasExpired()) {
                    state = kReload;
                    timer.setExpiration(Constants.ShooterResetTime.getValue() + 250);
                }
                break;
            case kReload:
                shooter.reload();
                if (shooter.atLoadingPosition() || timer.hasExpired()) {
                    state = kDone;
                }
                break;
            case kDone:
                shooter.setWinch(0.0);
            default:
                Logger.error("BasicAutonomous", "Invalid autonomous state");
                break;
        }
    }

}
