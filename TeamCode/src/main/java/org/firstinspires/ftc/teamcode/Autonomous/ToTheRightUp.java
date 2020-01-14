package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Right Up", group = "Autonomous")

public class ToTheRightUp extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {


    @Override
    public void runOpMode() {

        super.runOpMode();

        platform.setPosition(Servo.MAX_POSITION);
        arm.setPosition(Servo.MAX_POSITION);
        waitForStart();
        strafe(1, 0, 1, 1.5);
        strafe(0, 1, 1, 1.5);
        stop();

    }
}

