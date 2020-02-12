package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * This class makes the park the foundation in the building zone on the Red Alliance
 */
@Autonomous(name = "Platform Red", group = "Autonomous")
public class PlatformRed extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous{

    @Override
    public void runOpMode() {

        super.runOpMode();
        waitForStart();
        strafe(1, -1, 2.5);
        waitFor(1);
        platform.setPosition(Servo.MIN_POSITION);
        waitFor(1);
        strafe(-1, 0.8, 3);
        waitFor(1);
        rotate(true, 2.8, 1);
        waitFor(1);
        platform.setPosition(Servo.MAX_POSITION);
        waitFor(1);
        strafe(1, 0, 0.8);
        waitFor(1);
        strafe(-1, 0,2.7);
        stop();

    }
}