package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * This class makes the robot move to the left to park under the alliance bridge
 */
@Autonomous(name = "Parking Left", group = "Autonomous")
public class ParkLeft extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {

    @Override
    public void runOpMode() {

        super.runOpMode();
        waitForStart();
        strafe(0, 1, 1.5);
        stop();

    }
}
