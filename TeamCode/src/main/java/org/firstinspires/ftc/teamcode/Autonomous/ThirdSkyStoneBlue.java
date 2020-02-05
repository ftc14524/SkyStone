package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * This class contains the code the robot executes if CameraBlue determines the SkyStones are in positions 3 and 6
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Chosen Mode - Third Blue", group = "Camera Blue")
public class ThirdSkyStoneBlue extends CameraBlue {
    public ThirdSkyStoneBlue(){
        //First Block
        //Strafe left and back 1 second.
        strafe(-1, -1, .6);
        //Lower the arm
        armPivot.setPosition(Servo.MAX_POSITION);
        //Grabs block
        armClasp.setPosition(Servo.MAX_POSITION);
        //Lifts Arm
        armPivot.setPosition(Servo.MIN_POSITION);
        //Strafe right for .2 seconds.
        strafe(0, 1, .2);
        //Go forward for 3 seconds
        strafe(1, 0, 2.6);
        //Strage left for .2 seconds
        strafe(0, -1, .2);

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
        strafe(0, 1, .2);
        //Go backward for 1.5 seconds
        strafe(-1, 0, 1.3);
        //Strafe Left .2 seconds
        strafe(0, -1, .2);
        //Lower the arm
        armPivot.setPosition(Servo.MAX_POSITION);
        //Grabs block
        armClasp.setPosition(Servo.MAX_POSITION);
        //Lifts Arm
        armPivot.setPosition(Servo.MIN_POSITION);
        //Strafe right for .2 seconds.
        strafe(0, 1, .2);
        //Go forward for 1.3 seconds
        strafe(1, 0, 1.3);
        //Strage left for .2 seconds
        strafe(0, -1, .2);

        //Option 1
        //Release Grabber
        armClasp.setPosition(Servo.MIN_POSITION);

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
        strafe(1, -1, 1);
        //Ungrip Platform
        platform.setPosition(Servo.MIN_POSITION);
        //Strafe Left for .75 seconds
        strafe(0, -1, .75);
        stop();
/*
        //Strafe right 1 second.
        strafe(0, 1, 1, 1);
        //Move forward 1/2 second
        strafe(1, 0, 1, .5);
        //Lower the arm
        armPivot.setPosition(Servo.MAX_POSITION);
        //Strafe left for .2 seconds.
        strafe(0, -1, 1, .2);
        //Go back for 3 seconds
        strafe(-1, 0, 1, 3);
        //Let go of arm
        armPivot.setPosition(Servo.MIN_POSITION);
        //Forward 1.5 seconds.
        strafe(1, 0, 1, 1.5);
        //Strafe right .2
        strafe(0, 1, 1, .2);
        //Clamp
        armPivot.setPosition(Servo.MAX_POSITION);
        //Strafe left .2
        strafe(0, -1, 1, .2);
        //Go back 1.5
        strafe(-1, 0, 1, 1.5);
        //Forward .5
        strafe(1, 0, 1, .5);
        stop();

 */
    }
}
