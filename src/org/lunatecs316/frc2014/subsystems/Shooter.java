package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import java.util.Timer;
import java.util.TimerTask;
import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.RobotMap;
import org.lunatecs316.frc2014.lib.IterativeTimer;

/**
 * Shooter subsystem
 * @author Christian Steward
 * @author Domenic Rodriguez
 */
public class Shooter implements Subsystem {
    private Victor winchLeft = new Victor(RobotMap.ShooterWinchLeft);
    private Victor winchRight = new Victor(RobotMap.ShooterWinchRight);
    private DoubleSolenoid clutch = new DoubleSolenoid(RobotMap.ShooterClutchForward, RobotMap.ShooterClutchReverse);
    private DigitalInput loadSwitch = new DigitalInput(RobotMap.ShooterLoadSwitch);
    private DigitalInput maxSwitch = new DigitalInput(RobotMap.ShooterMaxSwitch);
    private DigitalInput ballSwitch = new DigitalInput(RobotMap.BallSwitch);
    private IterativeTimer resetTimer = new IterativeTimer();
    private Timer taskTimer = new Timer();

    private static Shooter instance;

    /**
     * Default constructor
     */
    private Shooter() {
    }

    /**
     * Get the shared instance of the subsystem
     * @return the shooter subsystem
     */
    public static Shooter getInstance() {
        if (instance == null)
            instance = new Shooter();
        return instance;
    }

    /**
     * @inheritDoc
     */
    public void init(){
        // Setup LiveWindow
        LiveWindow.addActuator("Shooter", "winch", winchLeft);
        LiveWindow.addActuator("Shooter", "clutch", clutch);
        LiveWindow.addSensor("Shooter", "loadSwitch", loadSwitch);
        LiveWindow.addSensor("Shooter", "maxSwitch", maxSwitch);
    }
    
    /**
     * @inheritDoc
     */
    public void updateSmartDashboard() {
    }

    /**
     * @inheritDoc
     */
    public void updateConstants() {
    }

    /**
     * Fire the ball
     */
    public void fire() {
        clutch.set(DoubleSolenoid.Value.kForward);

        // IterativeTimer ensures we don't try to re-engage the clutch too soon
        resetTimer.setExpiration(Constants.ShooterResetTime.getValue());
    }

    /**
     * Reload the shooter
     */
    public void reload() {
        _setWinch(1.0);
    }

    /**
     * Move the shooter up by a tiny bit
     */
    public void bumpUp() {
        taskTimer.schedule(new TimerTask() {
            public void run() {
                setWinch(-0.2);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                }
                setWinch(0.0);
                taskTimer.cancel();
            }
        }, 0);
    }

    /**
     * Move the shooter down by a tiny bit
     */
    public void bumpDown() {
        taskTimer.schedule(new TimerTask() {
            public void run() {
                setWinch(0.2);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                }
                setWinch(0.0);
                taskTimer.cancel();
            }
        }, 0);
    }
    
    /**
     * Directly control the winch
     * @param speed the speed of the winch
     */
    public synchronized void setWinch(double speed) {
        taskTimer.cancel();
        _setWinch(speed);
    }

    /**
     * Control the winch motors
     * @param speed the output value for the winch motors
     */
    private void _setWinch(double speed) {
        // Ensure we've waited long enough after firing
        if (resetTimer.hasExpired()) {
            //if ((speed > 0 && atLoadingPosition()) || (speed < 0 && atMaxPosition()))
            //    speed = 0;

            // If we're trying to move, make sure the clutch is engaged
            clutch.set(DoubleSolenoid.Value.kReverse);

            // Set the winch motors
            winchLeft.set(speed);
            winchRight.set(speed);
        }
    }
    
    /**
     * Check if the shooter is in the loading position
     * @return the status of the loading limit switch
     */
    public boolean atLoadingPosition() {
        return loadSwitch.get();
    }

    /**
     * Check if the shooter is at the max firing position
     * @return the status of the firing limit switch
     */
    public boolean atMaxPosition() {
        return maxSwitch.get();
    }

    /**
     * Check if we have a ball loaded in the shooter
     * @return the status if the ball switch
     */
    public boolean ballIsLoaded() {
        return ballSwitch.get();
    }
}
