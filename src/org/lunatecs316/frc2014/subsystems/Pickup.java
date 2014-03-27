package org.lunatecs316.frc2014.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.lunatecs316.frc2014.RobotMap;
import org.lunatecs316.frc2014.lib.Logger;

/**
 * Pickup subsystem. <br>
 * Picks stuff up - what more do you need?
 * @author Christian Steward
 * @author Domenic Rodriguez
 */
public class Pickup implements Subsystem {
    private static Pickup instance;

    private Talon roller = new Talon(RobotMap.PickupRoller);
    private DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.PickupSolenoidForward, RobotMap.PickupSolenoidReverse);
    private DigitalInput loweredSwitch = new DigitalInput(RobotMap.PickupLoweredSwitch);

    /**
     * Default constructor
     */
    private Pickup() {
    }

    /**
     * Get the shared instance
     * @return the pickup subsystem
     */
    public static Pickup getInstance() {
        if (instance == null)
            instance = new Pickup();
        return instance;
    }

    /**
     * @inheritDoc
     */
    public void init() {
        Logger.debug("Pickup#init", "Initalizing Pickup");

        LiveWindow.addActuator("Pickup", "Roller", roller);
        LiveWindow.addActuator("Pickup", "Solenoid", solenoid);
        LiveWindow.addSensor("Pickup", "Lowered Switch", loweredSwitch);
    }

    /**
     * @inheritDoc
     */
    public void updateSmartDashboard() {
        SmartDashboard.putBoolean("Pickup Lowered", isLowered());
    }

    /**
     * @inheritDoc
     */
    public void updateConstants() {
    }

    /**
     * Move the pickup to the raised position.
     */
    public void raise() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Move the pickup to the lowered position through actuation of the Solenoid
     */
    public void lower() {
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Sets speed of the roller for max versatility
     * @param speed the speed of the roller. Use negative values for picking up
     * the ball, and positive values for letting go of the ball.
     */
    public void setRollerSpeed(double speed) {
        roller.set(speed);
    }

    /**
     * Convenience method to stop the rollers.
     */
    public void stopRollers() {
        roller.set(0.0);
    }

    /**
     * Get the state of the lower limit switch
     * @return true if pickup is lowered
     */
    public boolean isLowered() {
        return !loweredSwitch.get();
    }
}
