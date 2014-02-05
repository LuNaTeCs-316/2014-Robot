package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    private DigitalInput loadSwitch = new DigitalInput(RobotMap.ShooterLoad);
    private DigitalInput maxSwitch = new DigitalInput(RobotMap.ShooterMax);
    private DigitalInput ballSwitch = new DigitalInput(RobotMap.BallSwitch);
    private IterativeTimer resetTimer = new IterativeTimer();

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
        // Default to clutch being engaged
        clutch.set(DoubleSolenoid.Value.kReverse);

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
        // Ensure we've waited long enough after firing
        if (resetTimer.hasExpired())
            setWinch(1.0);
    }
    
    /**
     * Directly control the winch
     * @param speed the speed of the winch
     */
    public void setWinch(double speed) {
        // Ensure we've waited long enough after firing
        if (resetTimer.hasExpired()) {
            if (SmartDashboard.getBoolean("EmergencyMode", false) ||
                    ((speed > 0 && atLoadingPosition()) || (speed < 0 && atMaxPosition())))
                speed = 0;

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

    public boolean ballIsLoaded() {
        return ballSwitch.get();
    }
}
