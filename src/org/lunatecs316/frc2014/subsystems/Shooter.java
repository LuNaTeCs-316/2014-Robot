package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.lunatecs316.frc2014.RobotMap;

/**
 * Shooter subsystem
 * @author Christian Steward
 * @author Domenic Rodriguez
 */
public class Shooter implements Subsystem {
    private Victor winch = new Victor(RobotMap.kShooterWinch);
    private Solenoid clutch = new Solenoid(RobotMap.kShooterClutch);
    private DigitalInput loadSwitch = new DigitalInput(RobotMap.kShooterLoad);
    private DigitalInput maxSwitch = new DigitalInput(RobotMap.kShooterMax);

    private static Shooter instance;
    
    /**
     * Default constructor
     */
    private Shooter() {
    }

    /**
     * Get the shared instance of the subsystem
     * @return the shooter subsystem
     */
    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }

        return instance;
    }

    /**
     * @inheritDoc
     */
    public void init(){
        LiveWindow.addActuator("Shooter", "winch", winch);
        LiveWindow.addActuator("Shooter", "clutch", clutch);
        LiveWindow.addSensor("Shooter", "loadSwitch", loadSwitch);
        LiveWindow.addSensor("Shooter", "maxSwitch", maxSwitch);
    }
    
    /**
     * @inheritDoc
     */
    public void updateSmartDashboard() {
        
    }
    
    /**
     * Reload the shooter
     */
    public void reload() {
         clutch.set(true);
         if (!atLoadingPosition() || SmartDashboard.getBoolean("EmergencyMode"))
             winch.set(1.0);
         else 
             winch.set(0.0);
    }
    
    /**
     * Fire the ball
     */
    public void fire() {
        if (!atFiringPosition() || SmartDashboard.getBoolean("EmergencyMode"))    
            clutch.set(false);
    }
    
    /**
     * Stop movement of the winch
     */
    public void setWinch(double speed) {
        winch.set(speed);
    }
    
    /**
     * Check if the shooter is in the loading position
     * @return the status of the loading limit switch
     */
    public boolean atLoadingPosition() {
        return loadSwitch.get();
    }

    /**
     * Check if the shooter is at the max firing position
     * @return the status of the firing limit switch
     */
    public boolean atFiringPosition() {
        return maxSwitch.get();
    }
}
