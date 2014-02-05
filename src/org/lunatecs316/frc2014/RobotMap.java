package org.lunatecs316.frc2014;

/**
 * Mapping of robot IO ports to constants
 * @author Domenic Rodriguez
 */
public class RobotMap {
    
    // PWM Output (Max 10)
    public static final int FrontLeftMotor = 1;
    public static final int RearLeftMotor = 2;
    public static final int FrontRightMotor = 3;
    public static final int kRearRightMotor = 4;
    public static final int PickupRoller = 5;
    public static final int ShooterWinchLeft = 6;
    public static final int ShooterWinchRight = 7;
    
    // Relay Output (Max 8)
    public static final int CompressorRelay = 1;
    
    // Solenoid Output (Max 8)
    public static final int ShiftingSolenoid = 1;
    public static final int PickupSolenoidForward = 2;
    public static final int PickupSolenoidReverse = 3;
    public static final int ShooterClutchForward = 4;
    public static final int ShooterClutchReverse = 5;
    
    // Joystick Input (Max 6)
    public static final int DriverController = 1;
    public static final int OperatorJoystick = 2;
    
    // Digital I/O (Max 14)
    public static final int PressureSwitch = 1;
    public static final int LeftDriveEncoderA = 2;
    public static final int LeftDriveEncoderB = 3;
    public static final int RightDriveEncoderA = 4;
    public static final int RightDriveEncoderB = 5;
    public static final int RangeFinderPing = 6;
    public static final int RangeFinderEcho = 7;
    public static final int PickupLoweredSwitch = 8;
    public static final int PickupRaisedSwitch = 9;
    public static final int ShooterLoad = 10;
    public static final int ShooterMax = 11;
    public static final int BallSwitch = 12;
    
    // Analog I/O (Max 8)
    public static final int Gyro = 1;
}
