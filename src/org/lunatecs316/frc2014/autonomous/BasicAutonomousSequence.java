package org.lunatecs316.frc2014.autonomous;

import org.lunatecs316.frc2014.Constants;
import org.lunatecs316.frc2014.autonomous.commands.*;

/**
 * BasicAutonomous, but in sequence form
 * @author Domenic Rodriguez
 */
public class BasicAutonomousSequence extends AutonomousSequence {
    public BasicAutonomousSequence() {
        addCommand(new DriveStraight(Constants.Drivetrain8ft.getValue()));
        addCommand(new WaitForHotGoal());
        addCommand(new Fire());
        addCommand(new WaitForReload(5000));
    }
}
