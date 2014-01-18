package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.lunatecs316.frc2014.RobotMap;

/**
 * Drivetrain subsystem
 * @author 316Programming
 */
public class Drivetrain {
    // Drive Motors
    private Jaguar frontLeft = new Jaguar(RobotMap.kFrontLeftMotor);
    private Jaguar frontRight = new Jaguar(RobotMap.kFrontRightMotor);
    private Jaguar rearLeft = new Jaguar(RobotMap.kRearLeftMotor);
    private Jaguar rearRight = new Jaguar(RobotMap.kRearRightMotor);
    private RobotDrive driveMotors = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
    
    // Shifter
    private Relay shiftingRelay = new Relay(RobotMap.kShiftingRelay);
    
    // Sensors
    private Encoder leftEncoder = new Encoder(RobotMap.kLeftDriveEncoderA,
                                              RobotMap.kLeftDriveEncoderB,
                                              false,
                                              CounterBase.EncodingType.k4X);
    
    private Encoder rightEncoder = new Encoder(RobotMap.kRightDriveEncoderA,
                                               RobotMap.kRightDriveEncoderB,
                                               false,
                                               CounterBase.EncodingType.k4X);
    
    private Gyro gyro = new Gyro(RobotMap.kGyro);
    
    // Singleton instance
    private static Drivetrain instance;

    /**
     * Default constructor
     */
    private Drivetrain() {
    }

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }

        return instance;
    }

    /**
     * Initialize the subsystem
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
        
        // Setup LiveWindow for test mode
        LiveWindow.addActuator("Drivetrain", "frontLeft", frontLeft);
        LiveWindow.addActuator("Drivetrain", "rearLeft", rearLeft);
        LiveWindow.addActuator("Drivetrain", "frontRight", frontRight);
        LiveWindow.addActuator("Drivetrain", "rearRight", rearRight);
        LiveWindow.addSensor("Drivetrain", "leftEncoder", leftEncoder);
        LiveWindow.addSensor("Drivetrain", "rightEncoder", rightEncoder);
        LiveWindow.addSensor("Drivetrain", "gyro", gyro);
    }
    
    /**
     * Send data to the SmartDashboard
     */
    public void updateSmartDashboard() {
        SmartDashboard.putNumber("LeftEncoder", leftEncoder.get());
        SmartDashboard.putNumber("RightEncoder", rightEncoder.get());
        SmartDashboard.putNumber("Gyro", gyro.getAngle());
    }
    
    /**
     * Arcade-style driving
     * @param move forward-reverse movement value
     * @param turn left-right turning value
     */
    public void arcadeDrive(double move, double turn) {
        driveMotors.arcadeDrive(move, turn);
    }
    
    /**
     * Shift into high gear
     */
    public void shiftUp() {
        shiftingRelay.set(Relay.Value.kForward);
    }
    
    /**
     * Shift into low gear
     */
    public void shiftDown() {
        shiftingRelay.set(Relay.Value.kReverse);
    }
    
    /**
     * Reset the gyro angle
     */
    void resetGyro() {
        gyro.reset();
    }
    
    /**
     * Reset the left and right encoders
     */
    void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }
}
