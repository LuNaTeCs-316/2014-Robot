package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import org.lunatecs316.frc2014.RobotMap;

/**
 * Drivetrain subsystem
 * @author 316Programming
 */
public class Drivetrain {
    private Jaguar frontLeft = new Jaguar(RobotMap.kFrontLeftMotor);
    private Jaguar frontRight = new Jaguar(RobotMap.kFrontRightMotor);
    private Jaguar rearLeft = new Jaguar(RobotMap.kRearLeftMotor);
    private Jaguar rearRight = new Jaguar(RobotMap.kRearRightMotor);
    private RobotDrive driveMotors = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
    
    private Relay shiftingRelay = new Relay(RobotMap.kDrivetrainSolenoid);
    
    public Drivetrain() {
    }
    
    public void init() {
        driveMotors.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        driveMotors.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
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
}
