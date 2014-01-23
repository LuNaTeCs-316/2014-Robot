/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.lunatecs316.frc2014;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.lunatecs316.frc2014.subsystems.Drivetrain;
import org.lunatecs316.frc2014.subsystems.Pickup;
import org.lunatecs316.frc2014.subsystems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * 
 * @author Domenic Rodriguez
 * @author Christian Steward
 */
public class Robot extends IterativeRobot {
    private Compressor compressor = new Compressor(RobotMap.kPressureSwitch, RobotMap.kCompressorRelay);
    private TeleopControl teleop = new TeleopControl();

    // Subsystems
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Pickup pickup = Pickup.getInstance();
    private Shooter shooter = Shooter.getInstance();

    // Teleop loop count
    private int loopCount = 0;

    /**
     * Robot Constructor
     */
    public Robot() {
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        compressor.start();

        drivetrain.init();
        pickup.init();
        shooter.init();
    }

    /**
     * This function is called once at the start of autonomous
     */
    public void autonomousInit() {

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called once at the start of operator control
     */
    public void teleopInit() {
        teleop.init();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        teleop.run();
        
        // Update SmartDashboard
        if (loopCount >= Constants.kDashboardUpdateFrequency.getValue()) {
            drivetrain.updateSmartDashboard();
            pickup.updateSmartDashboard();
            shooter.updateSmartDashboard();
            loopCount = 0;
        } else {
            loopCount++;
        }
    }

    /**
     * This function is called once at the start of being disabled
     */
    public void disabledInit() {
        Constants.update();
    }

    /**
     * This function is called periodically while the robot is disabled
     */
    public void disabledPeriodic() {
        if (teleop.getDriverJoystick().getButtonA()) {
            drivetrain.resetGyro();
        }

        if (teleop.getDriverJoystick().getButtonB()) {
            drivetrain.resetEncoders();
        }
    }

    /**
     * This function is called once at the start of test mode
     */
    public void testInit() {

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
