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
    private XboxController driverController = new XboxController(RobotMap.kDriverController);
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
    }

    /**
     * Run one iteration of Teleop mode
     */
    public void run() {
        boolean emergencyMode = SmartDashboard.getBoolean("EmergencyMode", false);

        // Driving
        if (driverController.getButton(XboxController.ButtonA)) {
            drivetrain.driveStraight(0.5);
        } else if (driverController.getButton(XboxController.ButtonB)) {
            drivetrain.turn(90, 0.5);
        } else {
            double move = Util.deadband(driverController.getLeftY(), Constants.JoystickDeadband.getValue());
            double turn = Util.deadband(driverController.getRightX(), Constants.JoystickDeadband.getValue());
            drivetrain.arcadeDrive(move, turn);
        }

        // Shifting
        if (driverController.getButtonPressed(XboxController.LeftBumper)) {
            drivetrain.shiftDown();
        } else if (driverController.getButtonPressed(XboxController.RightBumper)) {
            drivetrain.shiftUp();
        }

        // Pickup Position
        if (driverController.getButtonPressed(XboxController.ButtonY) && (shooter.atLoadingPosition() || emergencyMode)) {
            pickup.raise();
        } else if (driverController.getButtonPressed(XboxController.ButtonX)) {
            pickup.lower();
        }

        // Pickup Rollers
        pickup.setRollerSpeed(driverController.getZ());
        
        // Shooter
        if (pickup.isLowered() || emergencyMode) {
            if (operatorJoystick.getRawButton(1)) {
                shooter.fire();
            } else if (operatorJoystick.getRawButton(3)) {
                shooter.reload();
            } else {
                shooter.setWinch(Util.deadband(operatorJoystick.getY(), Constants.JoystickDeadband.getValue()));
            }
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
