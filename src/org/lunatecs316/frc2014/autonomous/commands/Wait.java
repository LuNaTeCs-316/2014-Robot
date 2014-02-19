package org.lunatecs316.frc2014.autonomous.commands;

import org.lunatecs316.frc2014.lib.IterativeTimer;

/**
 * Delay for a specified period
 * @author Domenic Rodriguez
 */
public class Wait extends AutonomousCommand {
    private IterativeTimer m_timer = new IterativeTimer();
    private double m_timeout;

    public Wait(double ms) {
        m_timeout = ms;
    }

    public void init() {
        m_timer.setExpiration(m_timeout);
    }

    public void run() {
    }

    public boolean isFinished() {
        return m_timer.hasExpired();
    }

    public void end() {
    }
}
