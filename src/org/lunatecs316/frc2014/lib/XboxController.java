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

    public boolean getButtonA() {
        boolean current = getRawButton(1);
        previous[0] = current;
        return current;
    }

    public boolean getButtonB() {
        boolean current = getRawButton(2);
        previous[1] = current;
        return current;
    }

    public boolean getButtonX() {
        boolean current = getRawButton(3);
        previous[2] = current;
        return current;
    }

    public boolean getButtonY() {
        boolean current = getRawButton(4);
        previous[3] = current;
        return current;
    }

    public boolean getLeftBumper() {
        boolean current = getRawButton(5);
        previous[4] = current;
        return current;
    }

    public boolean getRightBumper() {
        boolean current = getRawButton(6);
        previous[5] = current;
        return current;
    }
    
    public boolean getButton(Button button) {
        boolean current = getRawButton(button.getNumber());
        previous[button.getNumber()-1] = current;
        return current;
    }
    
    public boolean getButtonPressed(Button button) {
        int number = button.getNumber();
        boolean current = getRawButton(number);
        boolean result = current && !previous[number-1];
        previous[number-1] = current;
        return result;
    }
    
    public boolean getButtonHeld(Button button) {
        int number = button.getNumber();
        boolean current = getRawButton(number);
        boolean result = current && previous[number-1];
        previous[number-1] = current;
        return result;
    }
    
    public boolean getButtonReleased(Button button) {
        int number = button.getNumber();
        boolean current = getRawButton(number);
        boolean result = !current && previous[number-1];
        previous[number-1] = current;
        return result;
    }
}
