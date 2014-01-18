

package org.lunatecs316.frc2014.subsystems;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.lunatecs316.frc2014.RobotMap;


/**
 *Subsystem for the shooter
 * @author christiansteward
 */
public class Shooter {
    private Victor winch = new Victor(RobotMap.kShooterWinch);
    private DigitalInput loadSwitch = new DigitalInput(RobotMap.kShooterLoad);
    private DigitalInput maxSwitch = new DigitalInput(RobotMap.kShooterMax);
    private Solenoid clutch =new Solenoid(RobotMap.kShooterClutch);
    
    public static double kForward = 1.0;
    public static double kReverse = -1.0;
    
    public Shooter(){
       
    }

    /**
     * Initialize the subsystem
     */
    public void init(){
        
    }
    
    /**
     * Prepare the shooter for firing
     */
    public void load() {
        while (!maxSwitch.get() )
            winch.set(kForward);
    }
    
    public void fire() {
        clutch.set(true);
    }
    
    public boolean isLoadable() {
        return loadSwitch.get();
    }
    
}
