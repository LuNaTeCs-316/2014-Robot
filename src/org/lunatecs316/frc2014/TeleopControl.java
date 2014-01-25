package org.lunatecs316.frc2014;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.lunatecs316.frc2014.lib.Util;
import org.lunatecs316.frc2014.lib.XboxController;
import org.lunatecs316.frc2014.subsystems.Drivetrain;
import org.lunatecs316.frc2014.subsystems.Pickup;
import org.lunatecs316.frc2014.subsystems.Shooter;

/**
 * Manages control of the Robot during Teleop mode
 * @author Christian Steward
 * @author Domenic Rodriguez
 */
public class TeleopControl {
    private XboxController driverJoystick = new XboxController(RobotMap.kDriverJoystick);
    private Joystick operatorJoystick = new Joystick(RobotMap.kOperatorJoystick);

    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Pickup pickup = Pickup.getInstance();
    private Shooter shooter = Shooter.getInstance();

    /**
     * Default constructor
     */
    public TeleopControl() {
    }

    /**
     * Setup for Teleop mode
     */
    public void init() {
        // Nothing to do yet
    }

    /**
     * Run one iteration of Teleop mode
     */
    public void run() {
        boolean emergencyMode = SmartDashboard.getBoolean("EmergencyMode", false);
        
        // Driving
        double move = Util.deadband(driverJoystick.getLeftY(), Constants.kJoystickDeadband.getValue());
        double turn = Util.deadband(driverJoystick.getRightX(), Constants.kJoystickDeadband.getValue());
        drivetrain.arcadeDrive(move, turn);

        // Shifting
        if (driverJoystick.getButtonPressed(XboxController.kLeftBumper)) {
            drivetrain.shiftDown();
        } else if (driverJoystick.getButtonPressed(XboxController.kRightBumper)) {
            drivetrain.shiftUp();
        }

        // Pickup Position
        if (operatorJoystick.getRawButton(4) && (shooter.isReadyToLoad() || emergencyMode)) {
            pickup.raise();
        } else if (operatorJoystick.getRawButton(5)) {
            pickup.lower();
        }

        // Pickup Rollers
        if (operatorJoystick.getRawButton(3) && (shooter.isReadyToLoad() || emergencyMode)) {
            pickup.setRollerSpeed(Pickup.kForward);
        } else if (operatorJoystick.getRawButton(2) && (shooter.isReadyToLoad()) || emergencyMode) {
            pickup.setRollerSpeed(Pickup.kReverse);
        } else {
            pickup.setRollerSpeed(0.0);
        }
        
        // Shooter
        if (operatorJoystick.getRawButton(1) && (pickup.isLowered() || emergencyMode)) {
            shooter.fire();
        } else if (operatorJoystick.getRawButton(11) && (pickup.isLowered() || emergencyMode)) {
            shooter.reload();
        }
    }

    /**
     * Get the driver's joystick
     * @return the driver controller
     */
    public XboxController getDriverJoystick() {
        return driverJoystick;
    }

    /**
     * Get the operator's joystick
     * @return the operator joystick
     */
    public Joystick getOperatorJoystick() {
        return operatorJoystick;
    }
}
