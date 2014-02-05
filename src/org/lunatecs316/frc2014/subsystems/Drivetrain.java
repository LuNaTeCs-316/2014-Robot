package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.CounterBase;
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
    // Drive Motors
    private Victor frontLeft = new Victor(RobotMap.FrontLeftMotor);
    private Victor frontRight = new Victor(RobotMap.FrontRightMotor);
    private Victor rearLeft = new Victor(RobotMap.RearLeftMotor);
    private Victor rearRight = new Victor(RobotMap.kRearRightMotor);
    private RobotDrive driveMotors = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
    private Solenoid shiftingSolenoid = new Solenoid(RobotMap.ShiftingSolenoid);
    
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
    
    // Singleton instance
    private static Drivetrain instance;

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
        // Setup RobotDrive
        driveMotors.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        driveMotors.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        
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
     * Arcade drive
     * @param move forward-reverse movement value
     * @param turn left-right turning value
     */
    public void arcadeDrive(double move, double turn) {
        driveMotors.arcadeDrive(move, turn);

        // Calculate left and right motor values
        //double t_left = move + turn;
        //double t_right = move - turn;

        // Skim values and apply to the opposite side
        //double left = t_left - (skim(t_right) * Constants.kDrivetrainSkimGain.getValue());
        //double right = t_right - (skim(t_left) * Constants.kDrivetrainSkimGain.getValue());

        // Apply power to the motors
        //setLeftAndRightMotors(left, right);
    }

    /**
     * Drive the robot straight forwards
     * @param speed the speed at which to move
     */
    public void driveStraight(double speed) {
        // TODO: implement method
        Logger.warning("Drivetrain#driveStraight", "Method not yet implemented");
    }

    /**
     * Drive the robot straight for a specified distance
     * @param distance the distance to move
     * @param speed the speed at which to move
     */
    public void driveStraightDistance(double distance, double speed) {
        // TODO: needs to be implemented
        Logger.warning("Drivetrain#driveStraightDistance", "Method not yet implemented");
    }
    
    /**
     * Turn the robot in place
     * @param angle the amount to turn by
     * @param speed the speed at which to make the turn
     */
    public void turn(double angle, double speed) {
        Logger.warning("Drivetrain#turn", "Method not yet implemented");
    }
    
    /**
     * Shift into high gear
     */
    public void shiftUp() {
        shiftingSolenoid.set(true);
    }
    
    /**
     * Shift into low gear
     */
    public void shiftDown() {
        shiftingSolenoid.set(false);
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

    /**
     * Utility function. Skim excess values above 1.0
     * @param value the value to skim
     * @return If value is positive and greater than 1.0, return value - 1.0.
     * If value is negative and less than -1.0, return value + 1.0.
     */
    private double skim(double value) {
        if (value > 1.0)
            return value - 1.0;
        else if (value < -1.0)
            return value + 1.0;
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
