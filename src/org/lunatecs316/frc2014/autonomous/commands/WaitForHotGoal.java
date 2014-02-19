package org.lunatecs316.frc2014.autonomous.commands;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.lunatecs316.frc2014.lib.IterativeTimer;

/**
 * Wait for the goal to be hot
 * @author Domenic
 */
public class WaitForHotGoal extends AutonomousCommand {
    private NetworkTable m_visionData = NetworkTable.getTable("visionData");
    private IterativeTimer m_timer = new IterativeTimer();

    public void init() {
        m_timer.setExpiration(4000);
        m_visionData.putBoolean("enabled", true);
    }

    public void run() {
    }

    public boolean isFinished() {
        return m_timer.hasExpired() || m_visionData.getBoolean("goalIsHot", true);
    }

    public void end() {
        m_visionData.putBoolean("enabled", false);
    }

}
