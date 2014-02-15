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
        updateJoysticks();

        // Driving
        if (driverController.getButton(XboxController.ButtonA)) {
            drivetrain.turnToAngle(0);
        } else if (driverController.getButton(XboxController.ButtonY)) {
            drivetrain.driveStraight(0.6);
        } else if (driverController.getButton(XboxController.ButtonB)) {
            drivetrain.turn(90, 50);
        } else if (driverController.getButton(XboxController.ButtonX)) {
            drivetrain.driveStraightDistance(20700);
        } else {
            double move = Util.deadband(driverController.getLeftY(), Constants.JoystickDeadband.getValue());
            double turn = Util.deadband(driverController.getRightX(), Constants.JoystickDeadband.getValue());
            drivetrain.arcadeDrive(move, turn);
        }


        // Shifting
        if (driverController.getButton(XboxController.RightBumper)) {
            drivetrain.shiftUp();
        } else {
            drivetrain.shiftDown();
        }

        // Pickup Position
        if (operatorJoystick.getButtonPressed(4)) {
            pickup.raise();
        } else if (operatorJoystick.getButtonPressed(5)) {
            pickup.lower();
        }

        // Pickup Rollers
        double rollerSpeed = ((0.25 * -operatorJoystick.getZ()) + 0.75);
        if (operatorJoystick.getButton(7) && (shooter.atLoadingPosition() || Robot.manualOverride()))
            pickup.setRollerSpeed(-rollerSpeed);
        else if (operatorJoystick.getButton(6))
            pickup.setRollerSpeed(rollerSpeed);
        else
            pickup.setRollerSpeed(0.0);

        // Shooter
        if (operatorJoystick.getButtonPressed(1) && (pickup.isLowered() || Robot.manualOverride())) {
            shooter.fire();
        } else if (operatorJoystick.getButtonPressed(11)) {
            shooter.bumpUp();
        } else if (operatorJoystick.getButtonPressed(10)) {
            shooter.bumpDown();
        } else if (operatorJoystick.getButton(2)) {
            shooter.setPosition(Constants.Shooter10ft.getValue());
        } else if (operatorJoystick.getButton(3)) {
            shooter.autoAim();
        } else if (operatorJoystick.getButtonPressed(8)) {
            shooter.reload();
        } else {
            double value = Util.deadband(operatorJoystick.getY(), Constants.JoystickDeadband.getValue());
            if (value != 0 || shooter.isManualControl() || operatorJoystick.getRawButton(7))
                shooter.setWinch(value);
        }
    }

    /**
     * Read the buttons on the joysticks. Required before using
     * edge detection methods.
     */
    public void updateJoysticks() {
        driverController.update();
        operatorJoystick.update();
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
