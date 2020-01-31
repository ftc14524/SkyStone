package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Parking Left Up", group = "Autonomous")

public class ParkLeftUp extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {


    @Override
    public void runOpMode() {

        super.runOpMode();

        waitForStart();
        strafe(1, 0.2, 1, 0.6);
        strafe(0, 1, 1, 1.5);
        stop();

    }
}

