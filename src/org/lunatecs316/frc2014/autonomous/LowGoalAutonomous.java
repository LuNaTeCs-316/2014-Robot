package org.lunatecs316.frc2014.autonomous;

import org.lunatecs316.frc2014.lib.IterativeTimer;

/**
 * The simplest autonomous mode that we hope to never use. Drive backwards
 * up to the low goal and score the ball for a measly 6 (or 11) points.
 * @author Domenic Rodriguez
 */
public class LowGoalAutonomous extends AutonomousMode {
    private static final int kDriveToGoal = 1;
    private static final int kScore = 2;

    private IterativeTimer stateTimer = new IterativeTimer();
    private int state;
    private boolean done;

    public void init() {
        pickup.raise();
        state = kDriveToGoal;
        done = false;
        stateTimer.setExpiration(5000);
    }

    public void run() {
        if (!done) {
            switch (state) {
                case kDriveToGoal:
                    drivetrain.arcadeDrive(0.75, 0.0);
                    if (stateTimer.hasExpired()) {
                        drivetrain.arcadeDrive(0.0, 0.0);
                        state = kScore;
                        stateTimer.setExpiration(3000);
                    }
                    break;
                case kScore:
                    pickup.setRollerSpeed(1.0);
                    if (stateTimer.hasExpired()) {
                        pickup.setRollerSpeed(0.0);
                        done = true;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
