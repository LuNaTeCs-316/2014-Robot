package org.lunatecs316.frc2014;

import edu.wpi.first.wpilibj.Joystick;
import org.lunatecs316.frc2014.lib.EnhancedJoystick;
import org.lunatecs316.frc2014.lib.Logger;
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
    private XboxController driverController = new XboxController(RobotMap.DriverController);
    private EnhancedJoystick operatorJoystick = new EnhancedJoystick(RobotMap.OperatorJoystick);

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
    }

    /**
     * Run one iteration of Teleop mode
     */
    public void run() {
        // Driving
        double move = Util.deadband(driverController.getLeftY(), Constants.JoystickDeadband.getValue());
        double turn = Util.deadband(driverController.getRightX(), Constants.JoystickDeadband.getValue());
        drivetrain.arcadeDrive(move, turn);

        // Shifting
        if (driverController.getButtonPressed(XboxController.LeftBumper)) {
            drivetrain.shiftDown();
        } else if (driverController.getButtonPressed(XboxController.RightBumper)) {
            drivetrain.shiftUp();
        }

        // Pickup Position
        if (driverController.getButtonPressed(XboxController.ButtonX)) {
            pickup.raise();
        } else if (driverController.getButtonPressed(XboxController.ButtonY)) {
            pickup.lower();
        }

        // Pickup Rollers
        pickup.setRollerSpeed(driverController.getZ());
        
        // Shooter
        if (operatorJoystick.getButtonPressed(1)) {
            shooter.fire();
        } else if (operatorJoystick.getButtonReleased(1)) {
            shooter.reload();
        } else if (operatorJoystick.getButtonPressed(6)) {
            shooter.bumpUp();
        } else if (operatorJoystick.getButtonPressed(7)) {
            shooter.bumpDown();
        } else {
            double value = Util.deadband(operatorJoystick.getY(), Constants.JoystickDeadband.getValue());
            if (value != 0)
                shooter.setWinch(value);
        }
    }

    /**
     * Get the driver's joystick
     * @return the driver controller
     */
    public XboxController getDriverController() {
        return driverController;
    }

    /**
     * Get the operator's joystick
     * @return the operator joystick
     */
    public Joystick getOperatorJoystick() {
        return operatorJoystick;
    }
}
