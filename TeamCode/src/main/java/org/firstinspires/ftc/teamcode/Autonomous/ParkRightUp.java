package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * This class makes the robot move up and to the right to park under the alliance bridge
 */
@Autonomous(name = "Parking Right Up", group = "Autonomous")
public class ParkRightUp extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {


    @Override
    public void runOpMode() {

        super.runOpMode();
        waitForStart();
        strafe(-1, 0,  0.81);
        rotate(true, 1.28,1);
        strafe(-1, 0,  1.05);
        stop();

    }
}

