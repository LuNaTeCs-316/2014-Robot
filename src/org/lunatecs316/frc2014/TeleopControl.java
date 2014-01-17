/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lunatecs316.frc2014;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import org.lunatecs316.frc2014.lib.XboxController;
import org.lunatecs316.frc2014.subsystems.Drivetrain;
import org.lunatecs316.frc2014.subsystems.Pickup;

/**
 *
 * @author christiansteward
 */
public class TeleopControl {
    private Compressor compressor = new Compressor(RobotMap.kPressureSwitch, RobotMap.kCompressorRelay);
    private XboxController driverJoystick = new XboxController(RobotMap.kDriverJoystick);
    private Joystick operatorJoystick = new Joystick(RobotMap.kOperatorJoystick);
    
    
    // Subsystems
    public static Drivetrain drivetrain = new Drivetrain();
    public static Pickup pickup = new Pickup();
    
    
    public TeleopControl() {
        
    }
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
        
        if (operatorJoystick.getRawButton(3))
            pickup.setRollerSpeed(Pickup.kForward);
        else if (operatorJoystick.getRawButton(2))
            pickup.setRollerSpeed(Pickup.kReverse);
    }
    
    /**
     * This function is called periodically during test mode
     */
}
