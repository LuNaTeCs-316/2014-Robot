## SAM XV

This is the robot code for FRC Team 316's 2014 robot, SAM XV. It is released under a 2-clause BSD license, detailed in [LICENSE.md](LICENSE.md).

Vision Code: https://github.com/LuNaTeCs-316/LuNaCV

### Project Structure
Our code is structured using a custom subsystem pattern based on the `IterativeRobot` base class. Teleop mode is managed by the `TeleopControl` class, and we have a variety of autonomous modes to choose from.

##### org.lunatecs316.frc2014
This package contains the main robot class, [SamXV][], as well as the [teleop controller][TeleopControl] and [constants manager][Constants].

##### org.lunatecs316.frc2014.subsystems
This package contains all of the subsystem classes. Each subsystem ([Drivetrain][], [Pickup][], and [Shooter][]) implements the [Subsystem][] interface. They follow the singleton pattern and each subsystem manages its own shared instance.

##### org.lunatecs316.frc2014.lib
This package contains utility and helper classes such as a timer class, an Xbox controller wrapper class, a logger and more.

##### org.lunatecs316.frc2014.autonomous
This package contains all classes related to autonomous mode. All autonomous modes extend the abstract base class `AutonomousMode`. Most autonomous modes are based on finite state machines:
- [LowGoalAutonomous][]: Simplest autonomous mode. Drive backwards and score the ball in the low goal
- [HighGoalAutonomous][]: Score a single ball in the high goal. Uses the vision data to score when the goal is hot.
- [TwoBallAutonomous][]: Drive forwards and score two balls in the high goal. Does not worry whether the goal is hot or not.
- [StationaryTwoBallAutonomous][]: Score two balls in the high goal without moving, and then drive forwards.

[SamXV]:                       src/org/lunatecs316/frc2014/SamXV.java
[TeleopControl]:               src/org/lunatecs316/frc2014/TeleopControl.java
[Constants]:                   src/org/lunatecs316/frc2014/Constants.java
[Subsystem]:                   src/org/lunatecs316/frc2014/subsystems/Subsystem.java
[Drivetrain]:                  src/org/lunatecs316/frc2014/subsystems/Drivetrain.java
[Pickup]:                      src/org/lunatecs316/frc2014/subsystems/Pickup.java
[Shooter]:                     src/org/lunatecs316/frc2014/subsystems/Shooter.java
[LowGoalAutonomous]:           src/org/lunatecs316/frc2014/autonomous/LowGoalAutonomous.java
[HighGoalAutonomous]:          src/org/lunatecs316/frc2014/autonomous/HighGoalAutonomous.java
[TwoBallAutonomous]:           src/org/lunatecs316/frc2014/autonomous/TwoBallAutonomous.java
[StationaryTwoBallAutonomous]: src/org/lunatecs316/frc2014/autonomous/StationaryTwoBallAutonomous.java
