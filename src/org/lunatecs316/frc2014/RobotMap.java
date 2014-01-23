package org.lunatecs316.frc2014;

/**
 * Mapping of robot IO ports to constants
 * @author Domenic Rodriguez
 */
public class RobotMap {
    
    // PWM Output
    public static final int kFrontLeftMotor = 1;
    public static final int kRearLeftMotor = 2;
    public static final int kFrontRightMotor = 3;
    public static final int kRearRightMotor = 4;
    public static final int kPickupRoller = 5;
    public static final int kShooterWinch =6;
    
    // Relay Output
    public static final int kCompressorRelay = 1;
    
    // Solenoid Output
    public static final int kShiftingSolenoid = 1;
    public static final int kPickupSolenoid = 2;
    public static final int kShooterClutch = 3;
    
    // Joystick Input
    public static final int kDriverJoystick = 1;
    public static final int kOperatorJoystick = 2;
    
    // Digital Input
    public static final int kPressureSwitch = 1;
    public static final int kLeftDriveEncoderA = 2;
    public static final int kLeftDriveEncoderB = 3;
    public static final int kRightDriveEncoderA = 4;
    public static final int kRightDriveEncoderB = 5;
    public static final int kPickupSensor = 6;
    public static final int kPickupLowerLimit = 7;
    public static final int kPickupUpperLimit = 8;
    public static final int kShooterLoad = 9;
    public static final int kShooterMax = 10;
    
    // Analog Input
    public static final int kGyro = 1;
}
