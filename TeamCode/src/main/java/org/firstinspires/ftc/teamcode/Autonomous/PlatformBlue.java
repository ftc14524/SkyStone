package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * This class makes the park the foundation in the building zone on the Blue Alliance
 */
@Autonomous(name = "Platform Blue", group = "Autonomous")
public class PlatformBlue extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous{

    @Override
    public void runOpMode() {

        super.runOpMode();
        waitForStart();
        strafe(1, 1, 2.4);
        waitFor(1);
        platform.setPosition(Servo.MIN_POSITION);
        waitFor(1);
        strafe(-1, 0, 1.3);
        waitFor(1);
        rotate(false, 2, 1);
        waitFor(1);
        platform.setPosition(Servo.MAX_POSITION);
        waitFor(1);
        strafe(1, 0, 4);
        stop();

    }
}
