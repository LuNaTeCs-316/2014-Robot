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
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.lunatecs316.frc2014.autonomous.AutonomousMode;
import org.lunatecs316.frc2014.autonomous.BasicAutonomous;
import org.lunatecs316.frc2014.lib.Logger;
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
    public static NetworkTable visionData;
    private Compressor compressor = new Compressor(RobotMap.kPressureSwitch, RobotMap.kCompressorRelay);
    private TeleopControl teleop = new TeleopControl();
    private AutonomousMode auto = new BasicAutonomous();

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

        visionData = NetworkTable.getTable("visionData");
        
        SmartDashboard.putBoolean("EmergencyMode", false);
        SmartDashboard.putBoolean("DebugMode", false);

        Logger.setLevel(Logger.Level.DEBUG);
        Logger.info("Robot#robotInit()", "Robot initalization complete!");
    }

    /**
     * This function is called once at the start of autonomous
     */
    public void autonomousInit() {
        Logger.info("Robot#autonomousInit()", "Entering autonomous mode...");
        auto.init();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        auto.run();
    }

    /**
     * This function is called once at the start of operator control
     */
    public void teleopInit() {
        Logger.info("Robot#teleopInit()", "Entering teleop mode...");
        teleop.init();
        loopCount = 0;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        teleop.run();
        
        updateSmartDashboard();
    }

    /**
     * This function is called once at the start of being disabled
     */
    public void disabledInit() {
        Logger.info("Robot#disabledInit()", "Entering disabled mode...");
        Constants.update();
        loopCount = 0;
    }

    /**
     * This function is called periodically while the robot is disabled
     */
    public void disabledPeriodic() {
        if (teleop.getDriverController().getButtonA()) {
            drivetrain.resetGyro();
        }

        if (teleop.getDriverController().getButtonB()) {
            drivetrain.resetEncoders();
        }
        
        updateSmartDashboard();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    /**
     * Send data to the SmartDashboard
     */
    private void updateSmartDashboard() {
        if (loopCount >= Constants.kDashboardUpdateFrequency.getValue()) {
            drivetrain.updateSmartDashboard();
            pickup.updateSmartDashboard();
            shooter.updateSmartDashboard();
            loopCount = 0;
        } else {
            loopCount++;
        }
    }
}
