package org.lunatecs316.frc2014.lib;

/**
 * PID Controller that can run in an iterative loop
 * @author Domenic Rodriguez
 */
public final class IterativePIDController {
    private double kP, kI, kD;
    private double integral, prev_error;

    private IterativeTimer deltaTimer = new IterativeTimer();

    /**
     * Construct a new PIDController
     * @param kP proportional constant
     * @param kI integral constant
     * @param kD derivative constant
     */
    public IterativePIDController(double kP, double kI, double kD) {
        setPID(kP, kI, kD);
        reset();
    }

    /**
     * Set new values for kP, kI, and kD
     * @param kP proportional constant
     * @param kI integral constant
     * @param kD derivative constant
     */
    public void setPID(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    /**
     * Reset the controller
     */
    public void reset() {
        integral = prev_error = 0;
        deltaTimer.reset();
    }

    /**
     * Run one iteration of the PID controller
     * @param sp setpoint (target value)
     * @param pv process variable (current value)
     * @return output of the PID algorithm
     */
    public double run(double sp, double pv) {
        return run(sp, pv, -1.0, 1.0);
    }

    /**
     * Run one iteration of the PID controller
     * @param sp setpoint (target value)
     * @param pv process variable (current value)
     * @param min the minimum output
     * @param max the maximum output
     * @return output of the PID algorithm
     */
    public double run(double sp, double pv, double min, double max) {
        // Get the time since the last call
        double dt = deltaTimer.getValue();

        // Calculate output
        double error = sp - pv;
        integral += error * dt;
        double derivative = (error - prev_error) / dt;
        double output = kP * error + kI * integral + kD * derivative;
        
        // Check against limits
        if (output > max)
            output = max;
        else if (output < min)
            output = min;

        // Reset for next run
        prev_error = error;
        deltaTimer.reset();
        return output;
    }
}
