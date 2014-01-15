package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
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
    
    /**
     * Default constructor
     */
    public Drivetrain() {
    }
    
    /**
     * Initialize the subsystem
     */
    public void init() {
        driveMotors.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        driveMotors.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        
        gyro.setSensitivity(0.007);
        resetGyro();
        
        leftEncoder.start();
        rightEncoder.start();
        resetEncoders();
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
    
    void resetGyro() {
        gyro.reset();
    }
    
    void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }
}
