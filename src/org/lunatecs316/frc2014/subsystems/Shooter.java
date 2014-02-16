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
import org.lunatecs316.frc2014.Robot;
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
        Logger.debug("Shooter#init", "Initalizing Shooter");

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
        if (ballIsLoaded() || Robot.manualOverride()) {
            clutch.set(DoubleSolenoid.Value.kForward);
            // IterativeTimer ensures we don't try to re-engage the clutch too soon
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
        taskTimer.schedule(new TimerTask() {
            public void run() {
                _setWinch(1.0);
                if (atLoadingPosition() || manualControl) {
                    _setWinch(0.0);
                    manualControl = true;
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
     * Automatically position the shooter based on the distance to the target
     */
    public void autoAim() {
        double distance = Drivetrain.getInstance().getRangeFinderDistance();
        double target;

        // Determine the proper setpoint. Ugly, but it works
        // TODO: extrapolate between points instead of using ranges
        if (distance >= 0 && distance < 54.0) {             // 0ft - 4.5ft
            target = Constants.Shooter4ft.getValue();
        } else if (distance >= 54.0 && distance < 66.0) {   // 4.5ft - 5.5ft
            target = Constants.Shooter5ft.getValue();
        } else if (distance >= 66.0 && distance < 78.0) {   // 5.5ft - 6.5ft
            target = Constants.Shooter6ft.getValue();
        } else if (distance >= 78.0 && distance < 90.0) {   // 6.5ft - 7.5ft
            target = Constants.Shooter7ft.getValue();
        } else if (distance >= 90.0 && distance < 102.0) {  // 7.5ft - 8.5ft
            target = Constants.Shooter8ft.getValue();
        } else if (distance >= 102.0 && distance < 114.0) {  // 8.5ft - 9.5ft
            target = Constants.Shooter9ft.getValue();
        } else if (distance >= 114.0 && distance < 126.0) {  // 9.5ft - 10.5ft
            target = Constants.Shooter10ft.getValue();
        } else if (distance >= 126.0 && distance < 138.0) {  // 10.5ft - 11.5ft
            target = Constants.Shooter11ft.getValue();
        } else if (distance >= 138.0 && distance < 150.0) {  // 11.5ft - 12.5ft
            target = Constants.Shooter12ft.getValue();
        } else if (distance >= 150.0 && distance < 162.0) {  // 12.5ft - 13.5ft
            target = Constants.Shooter13ft.getValue();
        } else if (distance >= 162.0 && distance < 174.0) {  // 13.5ft - 14.5ft
            target = Constants.Shooter14ft.getValue();
        } else if (distance >= 174.0 && distance < 186.0) {  // 14.5ft - 15.5ft
            target = Constants.Shooter15ft.getValue();
        } else if (distance >= 186.0 && distance < 198.0) {  // 15.5ft - 16.5ft
            target = Constants.Shooter16ft.getValue();
        } else if (distance >= 198.0 && distance < 210.0) {  // 16.5ft - 17.5ft
            target = Constants.Shooter17ft.getValue();
        } else {                                            // > 17.5ft
            target = Constants.Shooter18ft.getValue();
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
            if (speed > 0 && atLoadingPosition() && !Robot.manualOverride())
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
        return (getArmPosition() >= Constants.ShooterLoadPosition.getValue()) || loadSwitch.get();
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
