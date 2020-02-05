package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Parking Right Up", group = "Autonomous")

public class ParkRightUp extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {


    @Override
    public void runOpMode() {

        super.runOpMode();
        waitForStart();
        strafe(1, -0.2,  0.6);
        strafe(0, -1,  1.5);
        stop();

    }
}

