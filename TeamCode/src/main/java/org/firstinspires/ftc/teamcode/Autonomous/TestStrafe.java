package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "STRAFE TESTER CLICK ME", group = "Autonomous")
public class TestStrafe extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {

    @Override
    public void runOpMode() {

        super.runOpMode();
        waitForStart();
        rotate(true, 2, 1);
        //Forward
        strafe(-1, 0, .5);
        waitFor(3);
        //Backwards
        strafe(1, 0, .5);
        waitFor(3);
        //Left
        strafe(0, -1, 1);
        waitFor(3);
        //Right
        strafe(0, 1, 1);
        waitFor(3);
        //Forward and to the left
        strafe(-1, -1, 1);
        waitFor(3);
        //Forward and to the right
        strafe(-1, 1, 1);
        waitFor(3);
        //Backward and to the left
        strafe(1, -1, 1);
        waitFor(3);
        //Backward and to the right
        strafe(1, 1, 1);

        stop();

    }
}
