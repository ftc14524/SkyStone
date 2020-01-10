package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Strafe Left", group = "Autonomous")

public class StrafeLeft extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {


    @Override
    public void runOpMode() {

        super.runOpMode();

        strafe(0, -1, 1, 30);
        stop();

    }
}