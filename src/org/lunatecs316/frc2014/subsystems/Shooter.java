package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Timer;
import java.util.TimerTask;
import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.RobotMap;
import org.lunatecs316.frc2014.lib.IterativePIDController;
import org.lunatecs316.frc2014.lib.IterativeTimer;

/**
 * Shooter subsystem
 * @author Christian Steward
 * @author Domenic Rodriguez
 */
public class Shooter implements Subsystem {
    private static Shooter instance;

    private Victor winchLeft = new Victor(RobotMap.ShooterWinchLeft);
    private Victor winchRight = new Victor(RobotMap.ShooterWinchRight);
    private DoubleSolenoid clutch = new DoubleSolenoid(RobotMap.ShooterClutchForward, RobotMap.ShooterClutchReverse);
    private DigitalInput loadSwitch = new DigitalInput(RobotMap.ShooterLoadSwitch);
    private DigitalInput maxSwitch = new DigitalInput(RobotMap.ShooterMaxSwitch);
    private DigitalInput ballSwitch = new DigitalInput(RobotMap.BallSwitch);
    private AnalogChannel positionPot = new AnalogChannel(RobotMap.ShooterPot);
    private IterativePIDController positionController = new IterativePIDController(Constants.ShooterPositionP.getValue(),
                Constants.ShooterPositionI.getValue(), Constants.ShooterPositionD.getValue());
    private IterativeTimer clutchTimer = new IterativeTimer();
    private Timer taskTimer = new Timer();

    private boolean manualControl;

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
        positionPot.setVoltageForPID(true);

        // Setup LiveWindow
        LiveWindow.addActuator("Shooter", "winchLeft", winchLeft);
        LiveWindow.addActuator("Shooter", "winchRight", winchRight);
        LiveWindow.addActuator("Shooter", "clutch", clutch);
        LiveWindow.addSensor("Shooter", "loadSwitch", loadSwitch);
        LiveWindow.addSensor("Shooter", "maxSwitch", maxSwitch);
        LiveWindow.addSensor("Shooter", "ballSwitch", ballSwitch);
        LiveWindow.addSensor("Shooter", "positionPot", positionPot);
    }

    /**
     * @inheritDoc
     */
    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Arm Position", getArmPosition());
    }

    /**
     * @inheritDoc
     */
    public void updateConstants() {
        positionController.setPID(Constants.ShooterPositionP.getValue(),
                Constants.ShooterPositionI.getValue(), Constants.ShooterPositionD.getValue());
    }

    /**
     * Fire the ball
     */
    public void fire() {
        clutch.set(DoubleSolenoid.Value.kForward);

        // IterativeTimer ensures we don't try to re-engage the clutch too soon
        clutchTimer.setExpiration(Constants.ShooterResetTime.getValue());
    }

    /**
     * Reload the shooter
     */
    public void reload() {
        manualControl = false;
        taskTimer.schedule(new TimerTask() {
            public void run() {
                _setWinch(1.0);
                if (atLoadingPosition() || manualControl) {
                    _setWinch(0.0);
                    cancel();
                }
            }
        }, 0L, 50);
    }

    /**
     * Move the shooter up by a tiny bit
     */
    public void bumpUp() {
        manualControl = false;
        taskTimer.schedule(new TimerTask() {
            private int count = 0;
            public void run() {
                count++;
                _setWinch(-0.6);
                if (count >= Constants.ShooterBump.getValue() || manualControl) {
                    _setWinch(0.0);
                    cancel();
                }
            }
        }, 0L, 50);
    }

    /**
     * Move the shooter down by a tiny bit
     */
    public void bumpDown() {
        manualControl = false;
        taskTimer.schedule(new TimerTask() {
            private int count = 0;
            public void run() {
                count++;
                _setWinch(0.6);
                if (count >= Constants.ShooterBump.getValue() || manualControl) {
                    _setWinch(0.0);
                    cancel();
                }
            }
        }, 0L, 50);
    }

    /**
     * Set the target position for the shooter arm
     * @param target
     */
    public void setPosition(double target) {
        if (manualControl) {
            manualControl = false;
            positionController.reset();
        }
        double value = positionController.run(target, positionPot.getAverageVoltage());
        setWinch(value);
    }

    /**
     * Directly control the winch
     * @param speed the speed of the winch
     */
    public void setWinch(double speed) {
        manualControl = true;
        _setWinch(speed);
    }

    /**
     * Control the winch motors
     * @param speed the output value for the winch motors
     */
    private void _setWinch(double speed) {
        // Ensure we've waited long enough after firing
        if (clutchTimer.hasExpired()) {
            if ((speed > 0 && atLoadingPosition()))// || (speed < 0 && atMaxPosition()))
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

    /**
     * Check if we have a ball loaded in the shooter
     * @return the status if the ball switch
     */
    public boolean ballIsLoaded() {
        return ballSwitch.get();
    }

    /**
     * Check if the shooter is under manual control
     * @return if the shooter is being controlled manually
     */
    public boolean isManualControl() {
        return manualControl;
    }

    /**
     * Get the position of the arm
     * @return the value of the potentiometer
     */
    public double getArmPosition() {
        return positionPot.getAverageVoltage();
    }
}
