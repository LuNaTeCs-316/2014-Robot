package org.lunatecs316.frc2014.lib;

/**
 * Wrapper class for the Xbox Controller
 * @author Domenic Rodriguez
 */
public class XboxController extends EnhancedJoystick {
    public static final Button ButtonA = new Button(1);
    public static final Button ButtonB = new Button(2);
    public static final Button ButtonX = new Button(3);
    public static final Button ButtonY = new Button(4);
    public static final Button LeftBumper = new Button(5);
    public static final Button RightBumper = new Button(6);
    
    public static class Button {
        private int number;
        
        public Button(int number) {
            this.number = number;
        }
        
        public int getNumber() {
            return number;
        }
    }
    
    /**
     * Default constructor
     * @param port USB port of the controller
     */
    public XboxController(final int port) {
        super(port);
    }

    public double getLeftX() {
        return getX();
    }

    public double getLeftY() {
        return getY();
    }

    public double getRightX() {
        return getRawAxis(4);
    }

    public double getRightY() {
        return getRawAxis(5);
    }

    /**
     * Get the value of the specified button
     * @param button the button to check
     * @return the value of the button
     */
    public boolean getButton(Button button) {
        return getButton(button.getNumber());
    }

    /**
     * Check if the specified button was pressed
     * @param button the button to check
     * @return whether or not the button was pressed
     */
    public boolean getButtonPressed(Button button) {
        return getButtonPressed(button.getNumber());
    }

    /**
     * Check if the specified button is being held
     * @param button the button to check
     * @return whether or not the button is being held
     */
    public boolean getButtonHeld(Button button) {
        return getButtonHeld(button.getNumber());
    }

    /**
     * Check if the specified button was released
     * @param button the button to check
     * @return whether or not the button was released
     */
    public boolean getButtonReleased(Button button) {
        return getButtonReleased(button);
    }
}
