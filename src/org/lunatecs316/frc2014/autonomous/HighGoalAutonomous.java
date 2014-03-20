package org.lunatecs316.frc2014.autonomous;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.lib.Logger;
import org.lunatecs316.frc2014.lib.IterativeTimer;

/**
 * Basic autonomous mode. Score the ball in the high goal
 * @author Domenic Rodriguez
 */
public class HighGoalAutonomous extends AutonomousMode {
    private static final int kDriveForwards = 0;
    private static final int kCheckForHotGoal = 1;
    private static final int kWaitForHotGoal = 2;
    private static final int kFire = 3;
    private static final int kWaitForReload = 4;
    private static final int kDone = 5;

    private NetworkTable visionData;
    private IterativeTimer stateTimer = new IterativeTimer();
    private int state;

    /**
     * @see AutonomousMode#init()
     */
    public void init() {
        // Get the NetworkTable
        visionData = NetworkTable.getTable("visionData");
        visionData.putBoolean("enabled", true);

        // Set the intial states for the robot subsystems
        pickup.setRollerSpeed(-1.0);

        // Set the default state
        state = kCheckForHotGoal;

        Logger.debug("BasicAutonomous#init", "State: kDrivingForwards");

        // Reset the state timer
        stateTimer.setExpiration(1100);
    }

    /**
     * @see AutonomousMode#run()
     */
    public void run() {
        switch (state) {
            case kCheckForHotGoal:
                if (stateTimer.hasExpired()) {
                    if (visionData.getBoolean("goalIsHot", true)) {
                        Logger.debug("BasicAutonomous#run", "State: kFire");
                        state = kDriveForwards;
                        stateTimer.setExpiration(3250);
                    } else {
                        state = kWaitForHotGoal;
                        Logger.debug("BasicAutonomous#run", "State: kWaitForHotGoal");
                        stateTimer.setExpiration(3750);
                    }
                    break;
                }
            case kWaitForHotGoal:
                if (stateTimer.hasExpired()) {
                    state = kDriveForwards;
                    Logger.debug("BasicAutonomous#run", "State: kFire");
                    stateTimer.setExpiration(3250);
                }
                break;
            case kDriveForwards:
                drivetrain.driveStraightDistance(Constants.Drivetrain8ft.getValue());
                shooter.setPosition(Constants.AutonomousShooterSetpoint.getValue());
                if (drivetrain.atTarget() || stateTimer.hasExpired()) {
                    pickup.setRollerSpeed(0.0);
                    drivetrain.arcadeDrive(0.0, 0.0);
                    shooter.setWinch(0.0);
                    state = kFire;
                    Logger.debug("BasicAutonomous#run", "State: kCheckForHotGoal");
                }
                break;
            case kFire:
                shooter.fire();
                state = kWaitForReload;
                Logger.debug("BasicAutonomous#run", "State: kWaitForReload");
                stateTimer.setExpiration(4000);
                break;
            case kWaitForReload:
                if (shooter.atLoadingPosition() || stateTimer.hasExpired()) {
                    state = kDone;
                    Logger.debug("BasicAutonomous#run", "State: kDone");
                }
                break;
            case kDone:
                shooter.setWinch(0.0);
                break;
            default:
                Logger.error("BasicAutonomous#run", "Invalid autonomous state");
                break;
        }
    }
}
