package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Strafe Forward", group = "Autonomous")

public class StrafeForward extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {


    @Override
    public void runOpMode() {

        super.runOpMode();

        strafe(1, 0, 1, 30);
        stop();

    }
}