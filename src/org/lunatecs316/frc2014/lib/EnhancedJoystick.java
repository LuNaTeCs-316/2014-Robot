package org.lunatecs316.frc2014.lib;

import edu.wpi.first.wpilibj.Joystick;

/**
 * WPILib Joystick class with edge detection methods
 * @author Domenic Rodriguez
 */
public class EnhancedJoystick extends Joystick {
    public static final int kNumberOfButtons = 12;
    private boolean[] previous;
    private boolean[] current;

    /**
     * Default constructor
     * @param port the USB port for the joystick
     */
    public EnhancedJoystick(int port) {
        super(port);

        previous = new boolean[kNumberOfButtons];
        for (int i = 0; i < kNumberOfButtons; i++)
            previous[i] = false;
        current = new boolean[kNumberOfButtons];
        for (int i = 0; i < kNumberOfButtons; i++)
            current[i] = false;
    }

    /**
     * Read and store all the button values. Necessary so we can do multiple
     * edge detection checks in the same loop
     */
    public void update() {
        // Set the old values
        System.arraycopy(current, 0, previous, 0, kNumberOfButtons);

        // Read the new values
        for (int i = 0; i < kNumberOfButtons; i++) {
            current[i] = getRawButton(i+1);
        }
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
        return current[button-1];
    }

    /**
     * Check if the specified button was pressed
     * @param button the button to check
     * @return whether or not the button was pressed
     */
    public boolean getButtonPressed(int button) {
        if (button <= 0 || button > kNumberOfButtons)
            Logger.error("EnhancedJoystick#getButtonPressed", "Invalid button number");
        return current[button-1] && !previous[button-1];
    }

    /**
     * Check if the specified button is being held
     * @param button the button to check
     * @return whether or not the button is being held
     */
    public boolean getButtonHeld(int button) {
        if (button <= 0 || button > kNumberOfButtons)
            Logger.error("EnhancedJoystick#getButtonHeld", "Invalid button number");
        return current[button-1] && previous[button-1];
    }

    /**
     * Check if the specified button was released
     * @param button the button to check
     * @return whether or not the button was released
     */
    public boolean getButtonReleased(int button) {
        if (button <= 0 || button > kNumberOfButtons)
            Logger.error("EnhancedJoystick#getButtonReleased", "Invalid button number");
        return !current[button-1] && previous[button-1];
    }
}
