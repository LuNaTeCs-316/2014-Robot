package org.lunatecs316.frc2014;

/**
 * Mapping of robot IO ports to constants
 * @author Domenic Rodriguez
 */
public class RobotMap {
    
    // PWM Output (Max 10)
    public static final int kFrontLeftMotor = 1;
    public static final int kRearLeftMotor = 2;
    public static final int kFrontRightMotor = 3;
    public static final int kRearRightMotor = 4;
    public static final int kPickupRoller = 5;
    public static final int kShooterWinchLeft = 6;
    public static final int kShooterWinchRight = 7;
    
    // Relay Output (Max 8)
    public static final int kCompressorRelay = 1;
    
    // Solenoid Output (Max 8)
    public static final int kShiftingSolenoid = 1;
    public static final int kPickupSolenoidForward = 2;
    public static final int kPickupSolenoidReverse = 3;
    public static final int kShooterClutchForward = 4;
    public static final int kShooterClutchReverse = 5;
    
    // Joystick Input (Max 6)
    public static final int kDriverController = 1;
    public static final int kOperatorJoystick = 2;
    
    // Digital I/O (Max 14)
    public static final int kPressureSwitch = 1;
    public static final int kLeftDriveEncoderA = 2;
    public static final int kLeftDriveEncoderB = 3;
    public static final int kRightDriveEncoderA = 4;
    public static final int kRightDriveEncoderB = 5;
    public static final int kRangeFinderPing = 6;
    public static final int kRangeFinderEcho = 7;
    public static final int kPickupLowerLimit = 8;
    public static final int kPickupUpperLimit = 9;
    public static final int kShooterLoad = 10;
    public static final int kShooterMax = 11;
    
    // Analog I/O (Max 8)
    public static final int kGyro = 1;
}
