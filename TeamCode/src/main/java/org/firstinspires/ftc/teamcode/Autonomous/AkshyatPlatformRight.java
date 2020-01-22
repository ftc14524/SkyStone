package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Akshyat Platform Right", group = "Autonomous")

public class AkshyatPlatformRight extends org.firstinspires.ftc.teamcode.Autonomous.AutonomousRight {
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
        //Turn counterclockwise 90 degrees
        timeTurn(-1, .5);
        //Go backward and strafe left for 1 second
        strafe(-1, -1, .5, 1);
        //Ungrip Platform
        platform.setPosition(Servo.MIN_POSITION);
        //Strafe Right for .75 seconds
        strafe(0, 1, 1, .75);
        stop();
    }
}
