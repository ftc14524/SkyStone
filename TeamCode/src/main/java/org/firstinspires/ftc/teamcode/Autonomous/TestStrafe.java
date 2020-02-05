package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "STRAFE TESTER CLICK ME", group = "Autonomous")
public class TestStrafe extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {

    @Override
    public void runOpMode() {

        super.runOpMode();
        waitForStart();
        //Forward and to the left
        strafe(1, 1, 2);
        //Forward and to the right
        strafe(1, -1, 2);
        //Backward and to the left
        strafe(-1, 1, 2);
        //Backward and to the right
        strafe(-1, -1, 2);
        stop();

    }
}
