package org.lunatecs316.frc2014.autonomous.commands;

import org.lunatecs316.frc2014.lib.IterativeTimer;

/**
 * Wait for the shooter to reload
 * @author Domenic Rodriguez
 */
public class WaitForReload extends AutonomousCommand {
    private IterativeTimer m_timer = new IterativeTimer();
    private double m_timeout;

    public WaitForReload(double timeout) {
        m_timeout = timeout;
    }

    public void init() {
        m_timer.setExpiration(m_timeout);
    }

    public void run() {
    }

    public boolean isFinished() {
        return shooter.atLoadingPosition() || m_timer.hasExpired();
    }

    public void end() {
    }
}
