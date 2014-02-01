package org.lunatecs316.frc2014.lib;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Wrapper class for the Xbox Controller
 * @author Domenic Rodriguez
 */
public class XboxController extends Joystick {
    
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
    
    private boolean[] previous = { false, false, false, false, false, false };

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
     * @deprecated Use XboxController#getButton() instead.
     * @return the button value
     */
    public boolean getButtonA() {
        return getButton(ButtonA);
    }

    /**
     * @deprecated Use XboxController#getButton() instead.
     * @return the button value
     */
    public boolean getButtonB() {
        return getButton(ButtonB);
    }

    /**
     * @deprecated Use XboxController#getButton() instead.
     * @return the button value
     */
    public boolean getButtonX() {
        return getButton(ButtonX);
    }

    /**
     * @deprecated Use XboxController#getButton() instead.
     * @return the button value
     */
    public boolean getButtonY() {
        return getButton(ButtonY);
    }

    /**
     * @deprecated Use XboxController#getButton() instead.
     * @return the button value
     */
    public boolean getLeftBumper() {
        return getButton(LeftBumper);
    }

    /**
     * @deprecated Use XboxController#getButton() instead.
     * @return the button value
     */
    public boolean getRightBumper() {
        return getButton(LeftBumper);
    }

    /**
     * Get the value of the specified button
     * @param button the button to check
     * @return the value of the button
     */
    public boolean getButton(Button button) {
        boolean current = getRawButton(button.getNumber());
        previous[button.getNumber()-1] = current;
        return current;
    }

    /**
     * Check if the specified button was pressed
     * @param button the button to check
     * @return whether or not the button was pressed
     */
    public boolean getButtonPressed(Button button) {
        int number = button.getNumber();
        boolean current = getRawButton(number);
        boolean result = current && !previous[number-1];
        previous[number-1] = current;
        return result;
    }

    /**
     * Check if the specified button is being held
     * @param button the button to check
     * @return whether or not the button is being held
     */
    public boolean getButtonHeld(Button button) {
        int number = button.getNumber();
        boolean current = getRawButton(number);
        boolean result = current && previous[number-1];
        previous[number-1] = current;
        return result;
    }

    /**
     * Check if the specified button was released
     * @param button the button to check
     * @return whether or not the button was released
     */
    public boolean getButtonReleased(Button button) {
        int number = button.getNumber();
        boolean current = getRawButton(number);
        boolean result = !current && previous[number-1];
        previous[number-1] = current;
        return result;
    }
}
