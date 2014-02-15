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
            drivetrain.driveStraight(0.6);
        } else if (driverController.getButton(XboxController.ButtonB)) {
            drivetrain.turn(90, 50);
        } else if (driverController.getButton(XboxController.ButtonX)) {
            drivetrain.driveStraightDistance(4500, 0.6);
        } else {
            double move = Util.deadband(driverController.getLeftY(), Constants.JoystickDeadband.getValue());
            double turn = Util.deadband(driverController.getRightX(), Constants.JoystickDeadband.getValue());

            // TODO: try using Drivetrain#driveStraight() when turn == 0
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
        if (operatorJoystick.getButton(2))
            pickup.setRollerSpeed(-rollerSpeed);
        else if (operatorJoystick.getButton(3))
            pickup.setRollerSpeed(rollerSpeed);
        else
            pickup.setRollerSpeed(0.0);

        // Shooter
        if (operatorJoystick.getButtonPressed(1) && (pickup.isLowered() || Robot.manualOverride())) {
            logShot();
            shooter.fire();
        } else if (operatorJoystick.getButtonPressed(6)) {
            shooter.bumpUp();
        } else if (operatorJoystick.getButtonPressed(7)) {
            shooter.bumpDown();
        } else if (operatorJoystick.getButton(10)) {
            shooter.setPosition(Constants.Shooter6ft.getValue());
        } else if (operatorJoystick.getButton(11)) {
            shooter.autoAim();
        } else if (operatorJoystick.getButtonPressed(9)) {
            shooter.reload();
        } else {
            double value = Util.deadband(operatorJoystick.getY(), Constants.JoystickDeadband.getValue());
            if (value != 0 || shooter.isManualControl() || operatorJoystick.getRawButton(8))
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

    /**
     * Record information while shooting
     */
    public void logShot() {
        double distance = drivetrain.getRangeFinderDistance();
        double angle = drivetrain.getGyroAngle();
        double armPosition = shooter.getArmPosition();
        Logger.enableFileLogging(true);
        Logger.debug("logShot", "ArmPos: " + armPosition + " Dist: " + distance + " Angle: " + angle);
        Logger.enableFileLogging(false);
    }
}
