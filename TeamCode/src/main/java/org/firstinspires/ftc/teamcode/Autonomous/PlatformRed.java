package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * This class makes the park the foundation in the building zone on the Red Alliance
 */
@Autonomous(name = "Platform Red", group = "Autonomous")
public class PlatformRed extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous{

    @Override
    public void runOpMode() {

        super.runOpMode();
        waitForStart();
        strafe(1, -0.3, 2);
        waitFor(2);
        platform.setPosition(Servo.MAX_POSITION);
        waitFor(2);
        strafe(-1, 0, 2.4);
        waitFor(2);
        rotate(false, 2, 1);
        waitFor(2);
        platform.setPosition(Servo.MIN_POSITION);
        waitFor(2);
        strafe(0, -1, 1.5);
        stop();

        /*waitForStart();
        //First Block
        //Strafe left 1 second.
        strafe(0, -1, 1, 1);
        //Grip Platform
        platformLeft.setPosition(Servo.MAX_POSITION);
        platformRight.setPosition(Servo.MIN_POSITION);
        //Turn counterclockwise 90 degrees
        timeTurn(-1, .5);
        //Go backward and strafe left for 1 second
        strafe(-1, -1, .5, 1);
        //Ungrip Platform
        platformLeft.setPosition(Servo.MIN_POSITION);
        platformRight.setPosition(Servo.MAX_POSITION);
        //Strafe Right for .75 seconds
        strafe(0, 1, 1, .75);
        stop();*/

    }
}