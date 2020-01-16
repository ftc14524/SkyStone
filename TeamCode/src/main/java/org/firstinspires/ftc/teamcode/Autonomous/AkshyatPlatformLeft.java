package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Akshyat Platform Left", group = "Autonomous")

public class AkshyatPlatformLeft extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {
    @Override
    public void runOpMode() {

        super.runOpMode();

        waitForStart();
        //stoneGripper = stone grab auton

        //First Block
        //Strafe left 1 second.
        strafe(0, -1, 1, 1);
        //Grip Platform
        platform.setPosition(Servo.MAX_POSITION);
        //Turn clockwise 90 degrees
        timeTurn(1, .5);
        //Go forward and strafe left for 1 second
        strafe(1, -1, .5, 1);
        //Ungrip Platform
        platform.setPosition(Servo.MIN_POSITION);
        //Strafe Right for .75 seconds
        strafe(0, 1, 1, .75);
        stop();
    }
}
