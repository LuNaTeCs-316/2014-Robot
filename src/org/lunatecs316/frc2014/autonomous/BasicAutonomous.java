package org.lunatecs316.frc2014.autonomous;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.lib.Logger;
import org.lunatecs316.frc2014.lib.IterativeTimer;

/**
 * Basic autonomous mode. Score the ball in the high goal
 * @author Domenic Rodriguez
 */
public class BasicAutonomous extends AutonomousMode {
    private static final int kDrivingForwards = 0;
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

        // Reset the state timer
        stateTimer.setExpiration(3250);

        // Set the default state
        state = kDrivingForwards;

        Logger.debug("BasicAutonomous#init", "State: kDrivingForwards");
    }

    /**
     * @see AutonomousMode#run()
     */
    public void run() {
        switch (state) {
            case kDrivingForwards:
                drivetrain.driveStraightDistance(Constants.Drivetrain8ft.getValue());
                shooter.setPosition(1.4 + Constants.ShooterAngleOffset.getValue());
                if (drivetrain.atTarget() || stateTimer.hasExpired()) {
                    pickup.setRollerSpeed(0.0);
                    drivetrain.arcadeDrive(0.0, 0.0);
                    shooter.setWinch(0.0);
                    state = kCheckForHotGoal;
                    Logger.debug("BasicAutonomous#run", "State: kCheckForHotGoal");
                }
                break;
            case kCheckForHotGoal:
                if (visionData.getBoolean("goalIsHot", true)) {
                    Logger.debug("BasicAutonomous#run", "State: kFire");
                    state = kFire;
                } else {
                    state = kWaitForHotGoal;
                    Logger.debug("BasicAutonomous#run", "State: kWaitForHotGoal");
                    stateTimer.setExpiration(2500);
                }
                break;
            case kWaitForHotGoal:
                if (stateTimer.hasExpired()) {
                    state = kFire;
                    Logger.debug("BasicAutonomous#run", "State: kFire");
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
                Logger.error("BasicAutonomous", "Invalid autonomous state");
                break;
        }
    }
}
