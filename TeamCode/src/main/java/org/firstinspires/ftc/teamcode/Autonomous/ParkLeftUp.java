package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * This class makes the robot move up and to the left to park under the alliance bridge
 */
@Autonomous(name = "Parking Left Up", group = "Autonomous")
public class ParkLeftUp extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {


    @Override
    public void runOpMode() {

        super.runOpMode();

        waitForStart();
        strafe(-1, 0, 0.6);
        strafe(0, -1,  1.5);
        stop();

    }
}

