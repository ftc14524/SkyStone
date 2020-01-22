package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Left Park", group = "Autonomous")

public class ToTheLeftUp extends org.firstinspires.ftc.teamcode.Autonomous.AutonomousLeft {


    @Override
    public void runOpMode() {

        super.runOpMode();

        platform.setPosition(Servo.MIN_POSITION);
        armPivot.setPosition(Servo.MAX_POSITION);
        waitForStart();
        strafe(1, 0.2, 1, 3);
        strafe(0, -1, 1, 1.5);
        stop();

    }
}

