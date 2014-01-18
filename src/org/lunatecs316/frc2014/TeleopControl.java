package org.lunatecs316.frc2014;

import edu.wpi.first.wpilibj.Joystick;
import org.lunatecs316.frc2014.lib.XboxController;
import org.lunatecs316.frc2014.subsystems.Drivetrain;
import org.lunatecs316.frc2014.subsystems.Pickup;
import org.lunatecs316.frc2014.subsystems.Shooter;

/**
 * Manages control of the Robot during Teleop mode
 * @author christiansteward
 */
public class TeleopControl {
    private XboxController driverJoystick = new XboxController(RobotMap.kDriverJoystick);
    private Joystick operatorJoystick = new Joystick(RobotMap.kOperatorJoystick);

    //
    // Subsystem references
    //
    // It is important that there is only one copy of each subsystem. Instead of
    // creating new instances of each subsystems, we make a reference to the
    // shared instances from the Robot class.
    //
    private Drivetrain drivetrain = Robot.drivetrain;
    private Pickup pickup = Robot.pickup;
    private Shooter shooter = Robot.shooter;

    /**
     * Default constructor
     */
    public TeleopControl() {
    }

    /**
     * Setup for Teleop mode
     */
    public void init() {
        // Nothing to do yet
    }

    /**
     * Run one iteration of Teleop mode
     */
    public void run() {
        // Drivetrain
        drivetrain.arcadeDrive(driverJoystick.getLeftY(), driverJoystick.getRightX());

        if (driverJoystick.getLeftBumper())
            drivetrain.shiftDown();
        else if (driverJoystick.getRightBumper())
            drivetrain.shiftUp();

        // Pickup
        if (operatorJoystick.getRawButton(4))
            pickup.raise();
        else if (operatorJoystick.getRawButton(5))
            pickup.lower();

        if (operatorJoystick.getRawButton(3) && shooter.isReadyToLoad())
            pickup.setRollerSpeed(Pickup.kForward);
        else if (operatorJoystick.getRawButton(2) && shooter.isReadyToLoad())
            pickup.setRollerSpeed(Pickup.kReverse);
        
        // Shooter
        if (operatorJoystick.getRawButton(1) && pickup.isLowered())
            shooter.fire();
        else if (operatorJoystick.getRawButton(11) && pickup.isLowered())
            shooter.reload();
    }

    /**
     * Get the driver's joystick
     * @return the driver controller
     */
    public XboxController getDriverJoystick() {
        return driverJoystick;
    }

    /**
     * Get the operator's joystick
     * @return the operator joystick
     */
    public Joystick getOperatorJoystick() {
        return operatorJoystick;
    }
}
