package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Akshyat Second SkyStone Left", group = "Autonomous")

public class AkshyatSecondSkystoneLeft extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {
    @Override
    public void runOpMode() {

        super.runOpMode();

        waitForStart();
        //stoneGripper = stone grab auton

        //First Block
        //Strafe forward and back .8 second.
        strafe(-1, -1, 1, .8);
        //Lower the arm
        armPivot.setPosition(Servo.MAX_POSITION);
        //Grabs block
        armClasp.setPosition(Servo.MAX_POSITION);
        //Lifts Arm
        armPivot.setPosition(Servo.MIN_POSITION);
        //Strafe right for .2 seconds.
        strafe(0, 1, 1, .2);
        //Go forward for 3 seconds
        strafe(1, 0, 1, 2.8);
        //Strage left for .2 seconds
        strafe(0, -1, 1, .2);

        //Option 1
        //Release Grabber
        armClasp.setPosition(Servo.MIN_POSITION);

        /*Other Option
        //Drop arm
        arm.setPosition(Servo.MAX_POSITION);
        //Release Grabber
        stoneGrabber.setPosition(Servo.MIN_Position);
        */


        //Second Block
        //Strafe right for .2 seconds
        strafe(0, 1, 1, .2);
        //Go backward for 1.5 seconds
        strafe(-1, 0, 1, 1.4);
        //Strafe Left .2 seconds
        strafe(0, -1, 1, .2);
        //Lower the arm
        armPivot.setPosition(Servo.MAX_POSITION);
        //Grabs block
        armClasp.setPosition(Servo.MAX_POSITION);
        //Lifts Arm
        armPivot.setPosition(Servo.MIN_POSITION);
        //Strafe right for .2 seconds.
        strafe(0, 1, 1, .2);
        //Go forward for 1.5 seconds
        strafe(1, 0, 1, 1.4);
        //Strage left for .2 seconds
        strafe(0, -1, 1, .2);

        //Option 1
        //Release Grabber
        armPivot.setPosition(Servo.MIN_POSITION);

        /*Other Option
        //Drop arm
        arm.setPosition(Servo.MAX_POSITION);
        //Release Grabber
        stoneGrabber.setPosition(Servo.MIN_Position);
        */

        //Grip Platform
        platform.setPosition(Servo.MAX_POSITION);
        //Turn clockwise 90 degrees
        timeTurn(1, .5);
        //Go forward and strafe left for 1 second
        strafe(1, -1, .5, 1);
        //Ungrip Platform
        platform.setPosition(Servo.MIN_POSITION);
        //Strafe Left for .75 seconds
        strafe(0, -1, 1, .75);
        stop();
    }
}
