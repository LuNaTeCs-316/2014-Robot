/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lunatecs316.frc2014.autonomous;

import org.lunatecs316.frc2014.Constants;

/**
 *
 * @author 316Programming
 */
public class DriveForwardAutonomous extends AutonomousMode {
   

    public void init() {
        pickup.raise();
        
    }

    public void run() {
        drivetrain.driveStraightDistance(Constants.Drivetrain4ft.getValue());
        }
}
    


