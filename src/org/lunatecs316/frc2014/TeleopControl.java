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

    private boolean cheesyMode;

    /**
     * Setup for Teleop mode
     */
    public void init() {
        drivetrain.setSafetyEnabled(false);
        cheesyMode = true;
    }

    /**
     * Run one iteration of Teleop mode
     */
    public void run() {
        updateJoysticks();

        // Driving
        if (driverController.getButtonPressed(XboxController.ButtonA)) {
            if (cheesyMode) {
                drivetrain.setSafetyEnabled(true);
                Logger.info("TeleopControl#run", "Switching to Arcade Drive Mode");
                cheesyMode = false;
            } else {
                Logger.info("TeleopControl#run", "Switching to Cheesy Drive Mode");
                drivetrain.setSafetyEnabled(false);
                cheesyMode = true;
            }
        }

        if (driverController.getButton(XboxController.ButtonB)) {
            drivetrain.driveStraight(-0.5);
        } else if (driverController.getButton(XboxController.ButtonX)) {
            drivetrain.driveStraightDistance(Constants.DrivetrainSetpoint.getValue());
        } else if (driverController.getButton(XboxController.ButtonY)) {
            drivetrain.resetGyro();
            drivetrain.resetEncoders();
        } else {
            double move = Util.deadband(driverController.getLeftY(), Constants.JoystickDeadband.getValue());
            double turn = Util.deadband(driverController.getRightX(), Constants.JoystickDeadband.getValue());

            if (cheesyMode)
                drivetrain.cheesyDrive(move, turn);
            else
                drivetrain.arcadeDrive(move, turn);
        }

        // Shifting
        if (driverController.getButton(XboxController.RightBumper))
            drivetrain.shiftDown();
        else
            drivetrain.shiftUp();

        // Catching Aid
        if (driverController.getButtonPressed(XboxController.LeftBumper))
            drivetrain.toggleCatchingAid();

        // Pickup Position
        if (operatorJoystick.getButtonPressed(11) || operatorJoystick.getButtonPressed(4))
            pickup.raise();
        else if (operatorJoystick.getButtonPressed(5)) //button 5 be broke
            pickup.lower();

        // Pickup Rollers
        double rollerSpeed = ((0.25 * -operatorJoystick.getZ()) + 0.75);
        if (operatorJoystick.getButton(7))
            pickup.setRollerSpeed(-rollerSpeed);
        else if (operatorJoystick.getButton(6))
            pickup.setRollerSpeed(rollerSpeed);
        else
            pickup.stopRollers();

        // Shooter
        if (operatorJoystick.getButtonPressed(1) && ((pickup.isLowered() && shooter.ballIsLoaded()) || SamXV.manualOverride())) {
            logShot();
            shooter.fire();
        } else if (operatorJoystick.getButton(2)) {
            shooter.setPosition(Constants.StaticShooterSetpoint.getValue());
        } else if (operatorJoystick.getButton(9)) {
            shooter.setPosition(1.8);
        } else if (operatorJoystick.getButton(3)) {
            shooter.autoAim(drivetrain.getRangeFinderDistance());
        } else if (operatorJoystick.getButtonPressed(8)) {
            shooter.reload();
        } else {
            double value = Util.deadband(operatorJoystick.getY(), Constants.JoystickDeadband.getValue());
            if (shooter.isManualControl() || operatorJoystick.getRawButton(10))
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
        Logger.debug("logShot", "ArmPos: " + armPosition + " Dist: " + distance + " GyroAngle: " + angle);
        Logger.enableFileLogging(false);
    }
}
