package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.SamXV;
import org.lunatecs316.frc2014.RobotMap;
import org.lunatecs316.frc2014.lib.IterativePIDController;
import org.lunatecs316.frc2014.lib.IterativeTimer;
import org.lunatecs316.frc2014.lib.Logger;

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
    private IterativeTimer reloadTimer = new IterativeTimer();
    private Timer taskTimer = new Timer();

    private Vector distances = new Vector();
    private Vector setpoints = new Vector();
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
        Logger.debug("Shooter#init", "Initalizing Shooter");

        // Update the setpoint lookup table
        updateSetpoints();

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
        updateSetpoints();
    }

    /**
     * Update the setpoints lookup table
     */
    private void updateSetpoints() {
        // Distances
        distances.removeAllElements();
        for (int i = 48; i <= 216; i += 12)
            distances.addElement(new Double(i + Constants.ShooterDistanceOffset.getValue()));

        // Angle setpoints
        setpoints.removeAllElements();
        setpoints.addElement(new Double(1.675 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.550 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.450 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.425 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.400 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.400 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.400 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.400 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.475 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.480 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.525 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.600 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.650 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.700 + Constants.ShooterAngleOffset.getValue()));
        setpoints.addElement(new Double(1.700 + Constants.ShooterAngleOffset.getValue()));
    }

    /**
     * Fire the ball
     */
    public void fire() {
        if (ballIsLoaded() || SamXV.manualOverride()) {
            clutch.set(DoubleSolenoid.Value.kForward);
            clutchTimer.setExpiration(Constants.ShooterResetTime.getValue());
            taskTimer.schedule(new TimerTask() {
                public void run() {
                    if (clutchTimer.hasExpired()) {
                        clutch.set(DoubleSolenoid.Value.kReverse);
                        reload();
                        cancel();
                    }
                }
            }, 0L, 30);
        }
    }

    /**
     * Reload the shooter
     */
    public void reload() {
        manualControl = false;
        reloadTimer.reset();
        taskTimer.schedule(new TimerTask() {
            public void run() {
                _setWinch(1.0);
                if (atLoadingPosition() || manualControl) {
                    _setWinch(0.0);
                    manualControl = true;
                    Logger.debug("Shooter#reload", "Reload time: " + reloadTimer.getValue());
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
                if (count > Constants.ShooterBump.getValue() || manualControl) {
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
                if (count > Constants.ShooterBump.getValue() || manualControl) {
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
     * Automatically position the shooter based on the distance to the target
     * @param distance the distance from the goal
     */
    public void autoAim(double distance) {
        double target = 1.700 + Constants.ShooterAngleOffset.getValue();

        // Calculate the shooter setpoint
        Double lowDistance = new Double(-1.0);
        Double lowSetpoint = new Double(-1.0);

        // Find the target range for the given distance and then interpolate between points
        for (int i = 0; i < distances.size(); i++)
        {
            Double highDistance = (Double) distances.elementAt(i);
            if (distance < highDistance.doubleValue())
            {
                Double highSetpoint = (Double) setpoints.elementAt(i);
                if (lowDistance.doubleValue() > 0.0)
                {
                    double m = (highSetpoint.doubleValue() - lowSetpoint.doubleValue())
                             / (highDistance.doubleValue() - lowDistance.doubleValue());
                    target = highSetpoint.doubleValue() + (m * (distance - lowDistance.doubleValue()));
                }
                else
                    target = highSetpoint.doubleValue();
                break;
            }
            else
            {
                lowDistance = highDistance;
                lowSetpoint = (Double) setpoints.elementAt(i);
            }
        }

        setPosition(target);
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
            if (speed > 0 && atLoadingPosition() && !SamXV.manualOverride())
                speed = 0;

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
