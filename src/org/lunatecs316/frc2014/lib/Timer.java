package org.lunatecs316.frc2014.lib;

/**
 * Time things within iterative loops
 * @author Domenic Rodriguez
 */
public class Timer {
    private double m_startTime;
    private double m_expiration;
    
    /**
     * Create a new Timer object
     */
    public Timer() {
        m_expiration = 0;
        reset();
    }
    
    /**
     * Start the timer
     */
    public final void reset() {
        m_startTime = System.currentTimeMillis();
    }
    
    /**
     * Get the value of the timer
     * @return the value of the timer
     */
    public double getValue() {
        return System.currentTimeMillis() - m_startTime;
    }
    
    /**
     * Set the timeout for the timer
     * @param ms the number of milliseconds until the timer expires
     */
    public void setExpiration(double ms) {
        reset();
        m_expiration = ms;
    }
    
    /**
     * Check if the timer has expired
     * @return if the timer value has exceeded the expiration value
     */
    public boolean hasExpired() {
        return (getValue() >= m_expiration);
    }
}
