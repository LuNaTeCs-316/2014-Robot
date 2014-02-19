package org.lunatecs316.frc2014.autonomous;

import java.util.Enumeration;
import org.lunatecs316.frc2014.autonomous.commands.AutonomousCommand;
import java.util.Vector;

/**
 *
 * @author Domenic Rodriguez
 */
public abstract class AutonomousSequence extends AutonomousMode {
    private Vector commands = new Vector();
    private Enumeration elements;
    private AutonomousCommand command;
    private boolean initialized;
    private boolean done;

    /**
     * @inheritDoc
     */
    public void init() {
        // Common setup for all AutonomousSequences
        drivetrain.shiftDown();
        drivetrain.disableSafety();
        drivetrain.lowerCatchingAid();
        drivetrain.resetEncoders();
        drivetrain.resetGyro();
        pickup.lower();
        pickup.setRollerSpeed(-1.0);

        // Setup for first run
        done = false;
        initialized = false;
        elements = commands.elements();
        if (elements.hasMoreElements())
            command = (AutonomousCommand) elements.nextElement();
        else
            done = true;
    }

    /**
     * @inheritDoc
     */
    public void run() {
        if (!done) {
            if (!initialized) {
                initialized = true;
                command.init();
            }
            command.run();
            if (command.isFinished()) {
                command.end();
                if (elements.hasMoreElements()) {
                    command = (AutonomousCommand) elements.nextElement();
                    initialized = false;
                } else {
                    done = true;
                }
            }
        } else {
            // Cleanup
            drivetrain.enableSafety();
            drivetrain.arcadeDrive(0.0, 0.0);
            drivetrain.lowerCatchingAid();
            pickup.lower();
            shooter.setWinch(0.0);
        }
    }

    /**
     * Add a command to the sequence
     * @param command the command to be added to the sequence
     */
    public void addCommand(AutonomousCommand command) {
        commands.addElement(command);
    }
}
