/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.lunatecs316.frc2014;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.lunatecs316.frc2014.autonomous.AutonomousMode;
import org.lunatecs316.frc2014.autonomous.BasicAutonomous;
import org.lunatecs316.frc2014.autonomous.StationaryTwoBallAutonomous;
import org.lunatecs316.frc2014.autonomous.TwoBallAutonomous;
import org.lunatecs316.frc2014.lib.IterativeTimer;
import org.lunatecs316.frc2014.lib.Logger;
import org.lunatecs316.frc2014.lib.XboxController;
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
public class SamXV extends IterativeRobot {
    private Compressor compressor = new Compressor(RobotMap.PressureSwitch, RobotMap.CompressorRelay);
    private TeleopControl teleop = new TeleopControl();
    private AutonomousMode auto;

    // Subsystems
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Pickup pickup = Pickup.getInstance();
    private Shooter shooter = Shooter.getInstance();

    // Teleop loop count
    private int loopCount = 0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // Setup the Logger
        Logger.setLevel(Logger.Level.DEBUG);
        Logger.info("robotInit", "Robot intialization starting...");
        IterativeTimer initTimer = new IterativeTimer();

        // Start the compressor
        compressor.start();
        LiveWindow.addActuator("Default", "Compressor", compressor);

        // Intialize the subsystems
        drivetrain.init();
        pickup.init();
        shooter.init();

        // Set default subsystem states
        drivetrain.shiftUp();
        drivetrain.raiseCatchingAid();
        pickup.raise();

        Logger.info("robotInit", "Robot initalized! Completed in " + initTimer.getValue() + "ms");
    }

    /**
     * This function is called once at the start of autonomous
     */
    public void autonomousInit() {
        // Choose an autonomous mode
        int mode = (int) DriverStation.getInstance().getAnalogIn(1);
        switch (mode) {
            case 0:
                auto = new BasicAutonomous();
                Logger.info("autonomousInit", "Running BasicAutonomous");
                break;
            case 1:
                auto = new StationaryTwoBallAutonomous();
                Logger.info("autonomousInit", "Running StationaryTwoBallAutonomous");
                break;
            case 5:
                auto = new TwoBallAutonomous();
                Logger.info("autonomousInit", "Running TwoBallAutonomous");
                break;
            default:
                Logger.warning("autonomousInit", "Invalid Autonomous Mode");
                break;
        }

        // Common setup for all autonomous modes
        drivetrain.shiftDown();
        drivetrain.resetGyro();
        drivetrain.resetEncoders();
        drivetrain.disableSafety();
        drivetrain.lowerCatchingAid();
        pickup.lower();

        // Initialize the autonomous mode
        auto.init();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        auto.run();
        updateSmartDashboard();
    }

    /**
     * This function is called once at the start of operator control
     */
    public void teleopInit() {
        Logger.info("teleopInit", "Entering teleop mode...");
        teleop.init();
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
        Logger.info("disabledInit", "Entering disabled mode...");
        Constants.update();

        // Set default states of the subsystems
        drivetrain.arcadeDrive(0, 0);
        drivetrain.shiftDown();
        pickup.setRollerSpeed(0.0);
        shooter.setWinch(0.0);
    }

    /**
     * This function is called periodically while the robot is disabled
     */
    public void disabledPeriodic() {
        teleop.updateJoysticks();

        if (teleop.getDriverController().getButtonPressed(XboxController.ButtonA))
            drivetrain.reinitGyro();
        if (teleop.getDriverController().getButtonPressed(XboxController.ButtonB))
            drivetrain.resetEncoders();
        if (teleop.getDriverController().getButtonPressed(XboxController.ButtonX))
            Constants.update();

        updateSmartDashboard();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }

    /**
     * Check to see if manual override mode is active
     * @return
     */
    public static boolean manualOverride() {
        return DriverStation.getInstance().getDigitalIn(1);
    }

    /**
     * Send data to the SmartDashboard
     */
    private void updateSmartDashboard() {
        if (loopCount >= Constants.DashboardUpdateFrequency.getValue()) {
            drivetrain.updateSmartDashboard();
            pickup.updateSmartDashboard();
            shooter.updateSmartDashboard();
            loopCount = 0;
        } else {
            loopCount++;
        }
    }
}
