package org.lunatecs316.frc2014.lib;

import edu.wpi.first.wpilibj.Joystick;

/**
 * WPILib Joystick class with edge detection methods
 * @author Domenic Rodriguez
 */
public class EnhancedJoystick extends Joystick {
    public static final int kNumberOfButtons = 12;
    private boolean[] previousValues;

    /**
     * Default constructor
     * @param port the USB port for the joystick
     */
    public EnhancedJoystick(int port) {
        super(port);
        previousValues = new boolean[kNumberOfButtons];
        for (int i = 0; i < kNumberOfButtons; i++)
            previousValues[i] = false;
    }

    /**
     * Get the current value of the button, saving the previous value for edge
     * detection
     * @param button which button to check
     * @return if the button is pressed or not
     */
    public boolean getButton(int button) {
        if (button <= 0 || button > kNumberOfButtons)
            Logger.error("EnhancedJoystick#getButton", "Invalid button number");
        boolean current = getRawButton(button);
        previousValues[button-1] = current;
        return current;
    }

    /**
     * Check if the specified button was pressed
     * @param button the button to check
     * @return whether or not the button was pressed
     */
    public boolean getButtonPressed(int button) {
        boolean current = getRawButton(button);
        boolean result = current && !previousValues[button-1];
        previousValues[button-1] = current;
        return result;
    }

    /**
     * Check if the specified button is being held
     * @param button the button to check
     * @return whether or not the button is being held
     */
    public boolean getButtonHeld(int button) {
        boolean current = getRawButton(button);
        boolean result = current && previousValues[button-1];
        previousValues[button-1] = current;
        return result;
    }

    /**
     * Check if the specified button was released
     * @param button the button to check
     * @return whether or not the button was released
     */
    public boolean getButtonReleased(int button) {
        boolean current = getRawButton(button);
        boolean result = !current && previousValues[button-1];
        previousValues[button-1] = current;
        return result;
    }
}
