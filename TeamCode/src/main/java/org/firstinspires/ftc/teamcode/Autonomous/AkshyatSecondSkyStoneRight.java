package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Akshyat", group = "Autonomous")

public class AkshyatSecondSkyStoneRight extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous {
    @Override
    public void runOpMode() {

        super.runOpMode();

        waitForStart();
        //stoneGripper = stone grab auton

        //First Block
        //Strafe left and forward 1 second.
        strafe(1, -1, 1, .8);
        //Lower the arm
        arm.setPosition(Servo.MAX_POSITION);
        //Grabs block
        stoneGripper.setPosition(Servo.MAX_POSITION);
        //Lifts Arm
        arm.setPosition(Servo.MIN_POSITION);
        //Strafe right for .2 seconds.
        strafe(0, 1, 1, .2);
        //Go backward for 3 seconds
        strafe(1, 0, 1, 2.8);
        //Strafe left for .2 seconds
        strafe(0, -1, 1, .2);

        //Option 1
        //Release Grabber
        stoneGripper.setPosition(Servo.MIN_POSITION);

        /*Other Option
        //Drop arm
        arm.setPosition(Servo.MAX_POSITION);
        //Release Grabber
        stoneGrabber.setPosition(Servo.MIN_Position);
        */


        //Second Block
        //Strafe right for .2 seconds
        strafe(0, 1, 1, .2);
        //Go forward for 1.5 seconds
        strafe(1, 0, 1, 1.4);
        //Strafe Left .2 seconds
        strafe(0, -1, 1, .2);
        //Lower the arm
        arm.setPosition(Servo.MAX_POSITION);
        //Grabs block
        stoneGripper.setPosition(Servo.MAX_POSITION);
        //Lifts Arm
        arm.setPosition(Servo.MIN_POSITION);
        //Strafe right for .2 seconds.
        strafe(0, 1, 1, .2);
        //Go backward for 1.5 seconds
        strafe(-1, 0, 1, 1.4);
        //Strage left for .2 seconds
        strafe(0, -1, 1, .2);

        //Option 1
        //Release Grabber
        stoneGripper.setPosition(Servo.MIN_POSITION);

        /*Other Option
        //Drop arm
        arm.setPosition(Servo.MAX_POSITION);
        //Release Grabber
        stoneGrabber.setPosition(Servo.MIN_Position);
        */

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
