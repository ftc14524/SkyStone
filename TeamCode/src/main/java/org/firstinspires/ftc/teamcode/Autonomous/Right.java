package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "TEST Right", group = "Autonomous")

public class Right extends org.firstinspires.ftc.teamcode.Autonomous.AutonomousRight {


    @Override
    public void runOpMode() {

        super.runOpMode();

        platform.setPosition(Servo.MIN_POSITION);
        armPivot.setPosition(Servo.MAX_POSITION);
        waitForStart();
        strafe(0, 1, 1, 1.5);
        stop();

    }
}
