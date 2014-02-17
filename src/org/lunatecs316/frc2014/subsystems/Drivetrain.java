package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.RobotMap;
import org.lunatecs316.frc2014.lib.IterativePIDController;
import org.lunatecs316.frc2014.lib.Logger;

/**
 * Drivetrain subsystem
 * @author Domenic Rodriguez
 */
public class Drivetrain implements Subsystem {
    private static Drivetrain instance;

    // Drive Motors
    private Victor frontLeft = new Victor(RobotMap.FrontLeftMotor);
    private Victor frontRight = new Victor(RobotMap.FrontRightMotor);
    private Victor rearLeft = new Victor(RobotMap.RearLeftMotor);
    private Victor rearRight = new Victor(RobotMap.RearRightMotor);
    private RobotDrive driveMotors = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
    private Solenoid shiftingSolenoid = new Solenoid(RobotMap.ShiftingSolenoid);
    private DoubleSolenoid catchingAidSolenoid = new DoubleSolenoid(RobotMap.CatchingAidForward, RobotMap.CatchingAidReverse);

    // Sensors
    private Encoder leftEncoder = new Encoder(RobotMap.LeftDriveEncoderA, RobotMap.LeftDriveEncoderB,
            false, CounterBase.EncodingType.k4X);
    private Encoder rightEncoder = new Encoder(RobotMap.RightDriveEncoderA, RobotMap.RightDriveEncoderB,
            false, CounterBase.EncodingType.k4X);
    private Gyro gyro = new Gyro(RobotMap.Gyro);
    private Ultrasonic rangeFinder = new Ultrasonic(RobotMap.RangeFinderPing, RobotMap.RangeFinderEcho);

    // PID Controllers
    private IterativePIDController distanceController = new IterativePIDController(Constants.DrivetrainDistanceP.getValue(),
            Constants.DrivetrainDistanceI.getValue(), Constants.DrivetrainDistanceD.getValue());
    private IterativePIDController angleController = new IterativePIDController(Constants.DrivetrainAngleP.getValue(),
            Constants.DrivetrainAngleI.getValue(), Constants.DrivetrainAngleD.getValue());

    private double startAngle;
    private boolean manualControl;
    private boolean highGear;
    private boolean atTarget;

    /**
     * Default constructor
     */
    private Drivetrain() {
    }

    /**
     * Get the shared instance of the subsystem
     * @return the drivetrain subsystem
     */
    public static Drivetrain getInstance() {
        if (instance == null)
            instance = new Drivetrain();
        return instance;
    }

    /**
     * @inheritDoc
     */
    public void init() {
        Logger.debug("Drivetrain#init", "Initalizing Drivetrain");

        // Setup Gyro
        gyro.setSensitivity(0.007);
        resetGyro();

        // Setup Encoders
        leftEncoder.start();
        rightEncoder.start();
        resetEncoders();

        // Setup range finder
        rangeFinder.setAutomaticMode(true);

        // Setup LiveWindow for test mode
        LiveWindow.addActuator("Drivetrain", "frontLeft", frontLeft);
        LiveWindow.addActuator("Drivetrain", "rearLeft", rearLeft);
        LiveWindow.addActuator("Drivetrain", "frontRight", frontRight);
        LiveWindow.addActuator("Drivetrain", "rearRight", rearRight);
        LiveWindow.addSensor("Drivetrain", "leftEncoder", leftEncoder);
        LiveWindow.addSensor("Drivetrain", "rightEncoder", rightEncoder);
        LiveWindow.addSensor("Drivetrain", "gyro", gyro);
        LiveWindow.addSensor("Drivetrain", "rangeFinder", rangeFinder);
    }

    /**
     * @inheritDoc
     */
    public void updateSmartDashboard() {
        SmartDashboard.putNumber("LeftEncoder", leftEncoder.get());
        SmartDashboard.putNumber("RightEncoder", rightEncoder.get());
        SmartDashboard.putNumber("Gyro", gyro.getAngle());
        SmartDashboard.putNumber("Range Finder", rangeFinder.getRangeInches());
    }

    /**
     * @inheritDoc
     */
    public void updateConstants() {
        distanceController.setPID(Constants.DrivetrainDistanceP.getValue(),
                Constants.DrivetrainDistanceI.getValue(), Constants.DrivetrainDistanceD.getValue());
        angleController.setPID(Constants.DrivetrainAngleP.getValue(),
            Constants.DrivetrainAngleI.getValue(), Constants.DrivetrainAngleD.getValue());
    }

    /**
     * Arcade-style driving
     * @param move forward-reverse movement value
     * @param turn left-right turning value
     */
    public void arcadeDrive(double move, double turn) {
        manualControl = true;
        _arcadeDrive(move, turn);
    }

    /**
     * Arcade drive implementation
     * @param move the "throttle" value
     * @param turn the "steering" value
     */
    private void _arcadeDrive(double move, double turn) {
        driveMotors.arcadeDrive(move, turn);
    }

    /**
     * Custom arcade drive inspired by Team 254
     * @param throttle forward-reverse movement
     * @param turn left-right steering
     */
    public void cheesyDrive(double throttle, double turn) {
        // Negate throttle to remain consistent with Joysticks
        throttle = -throttle;

        // Amp up the turn value
        if (Math.abs(throttle) > 0.5)
            turn = turn * (Constants.DrivetrainTurnGain.getValue() * Math.abs(throttle));

        // Calculate left and right motor values
        double t_left = throttle + turn;
        double t_right = throttle - turn;

        // Skim values and apply to the opposite side
        double left = t_left + (skim(t_right));
        double right = t_right + (skim(t_left));

        // Apply power to the motors
        setLeftAndRightMotors(left, right);
    }

    /**
     * Drive the robot straight forwards
     * @param speed the speed at which to move
     */
    public void driveStraight(double speed) {
        if (manualControl) {
            manualControl = false;
            startAngle = getGyroAngle();
        }
        double turn = angleController.run(startAngle, getGyroAngle());
        _arcadeDrive(speed, turn);
    }

    /**
     * Drive the robot straight for a specified distance
     * @param distance the distance to move
     */
    public void driveStraightDistance(double distance) {
        if (manualControl) {
            manualControl = false;
            startAngle = getGyroAngle();
            resetEncoders();
        }
        atTarget = (Math.abs(distance - getAverageEncoderValue()) < 200);
        double move = distanceController.run(distance, getAverageEncoderValue());
        double turn = angleController.run(startAngle, getGyroAngle());
        _arcadeDrive(move, turn);
    }

    /**
     * Use the range finder to drive the robot a set distance from whatever is
     * in front of it (typically the wall).
     * @param distance the desired distance between the bot and the wall
     * @param speed how fast to drive
     */
    public void driveToRangeFinderDistance(double distance, double speed) {
        manualControl = false;
        double current = rangeFinder.getRangeInches();
        if (distance > current)
            _arcadeDrive(speed, 0.0);
        else if (distance < current)
            _arcadeDrive(-speed, 0.0);
        else
            _arcadeDrive(0.0, 0.0);
    }

    /**
     * Turn the robot in place
     * @param angle the amount to turn by
     */
    public void turn(double angle) {
        if (manualControl) {
            startAngle = getGyroAngle();
            manualControl = false;
        }
        double turn = angleController.run(startAngle + angle, getGyroAngle());
        _arcadeDrive(0.0, turn);
    }

    /**
     * Turn the robot to the specified angle
     * @param angle the target angle for the robot
     */
    public void turnToAngle(double angle) {
        manualControl = false;
        Logger.debug("Drivetrain#turnToAngle", angle + " " + getGyroAngle());
        double turn = angleController.run(angle, getGyroAngle());
        _arcadeDrive(0.0, turn);
    }

    /**
     * Shift into high gear
     */
    public void shiftUp() {
        highGear = true;
        shiftingSolenoid.set(true);
    }

    /**
     * Shift into low gear
     */
    public void shiftDown() {
        highGear = false;
        shiftingSolenoid.set(false);
    }

    /**
     * Raise the catching aid arms
     */
    public void raiseCatchingAid() {
        catchingAidSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Lower the catching aid arms
     */
    public void lowerCatchingAid() {
        catchingAidSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Toggle the position of the catching aid arms
     */
    public void toggleCatchingAid() {
        if (catchingAidSolenoid.get() == DoubleSolenoid.Value.kForward)
            catchingAidSolenoid.set(DoubleSolenoid.Value.kReverse);
        else
            catchingAidSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Get the average value of the left and right encoders
     * @return the average between the left and right encoders
     */
    public double getAverageEncoderValue() {
        return (leftEncoder.get() + rightEncoder.get()) / 2;
    }

    /**
     * Get the current angle measured by the gyro
     * @return the gyro angle
     */
    public double getGyroAngle() {
        return gyro.getAngle();
    }

    /**
     * Get the current distance measured by the range finder
     * @return distance to the object in front of the range finder (inches)
     */
    public double getRangeFinderDistance() {
        return rangeFinder.getRangeInches();
    }

    /**
     * Check if the robot is being controlled manually
     * @return if the robot is being controlled manually
     */
    public boolean isManualControl() {
        return manualControl;
    }

    /**
     * Check if we're at the encoder distance target
     * @return if we've arrived at our target or not
     */
    public boolean atTarget() {
        return atTarget;
    }

    /**
     * Reset the gyro angle
     */
    public void resetGyro() {
        gyro.reset();
    }

    /**
     * Reset the left and right encoders
     */
    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void disableSafety() {
        driveMotors.setSafetyEnabled(false);
    }

    public void enableSafety() {
        driveMotors.setSafetyEnabled(true);
    }

    /**
     * Utility function. Skim excess values above 1.0
     * @param value the value to skim
     * @return If value is positive and greater than 1.0, return value - 1.0.
     * If value is negative and less than -1.0, return value + 1.0.
     */
    private double skim(double value) {
        if (value > 1.0)
            return -((value - 1.0) * Constants.DrivetrainSkimGain.getValue());
        else if (value < -1.0)
            return -((value + 1.0) * Constants.DrivetrainSkimGain.getValue());
        else
            return 0.0;
    }

    /**
     * Set the values of the left and right drive motors
     * @param left the value for the left motors
     * @param right the value for the right motors
     */
    private void setLeftAndRightMotors(double left, double right) {
        frontLeft.set(left);
        rearLeft.set(left);
        frontRight.set(right);
        rearRight.set(right);
    }
}
