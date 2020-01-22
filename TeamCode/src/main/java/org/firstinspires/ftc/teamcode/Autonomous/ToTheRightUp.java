package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Right Park", group = "Autonomous")

public class ToTheRightUp extends org.firstinspires.ftc.teamcode.Autonomous.AutonomousRight {


    @Override
    public void runOpMode() {

        super.runOpMode();

        platform.setPosition(Servo.MAX_POSITION);
        armPivot.setPosition(Servo.MAX_POSITION);
        waitForStart();
        strafe(1, -0.2, 1, 3);
        strafe(0, 1, 1, 1.5);
        stop();

    }
}

