package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * This class contains the code the robot executes if CameraRed determines the SkyStones are in positions 3 and 6
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Chosen Mode - Third Red", group = "Camera Red")
public class ThirdSkyStoneRed extends CameraRed{
    public ThirdSkyStoneRed()
    {
        //First Block
        //Strafe left and forward 1 second.
        strafe(1, -1, .6);
        //Lower the arm
        armPivot.setPosition(Servo.MAX_POSITION);
        //Grabs block
        armClasp.setPosition(Servo.MAX_POSITION);
        //Lifts Arm
        armPivot.setPosition(Servo.MIN_POSITION);
        //Strafe right for .2 seconds.
        strafe(0, 1, .2);
        //Go backward for 3 seconds
        strafe(1, 0, 2.6);
        //Strafe left for .2 seconds
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
        //Go forward for 1.5 seconds
        strafe(1, 0, 1.3);
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
        //Go backward for 1.5 seconds
        strafe(-1, 0, 1.3);
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
        //Turn counterclockwise 90 degrees
        timeTurn(-1, .5);
        //Go backward and strafe left for 1 second
        strafe(-1, -1, 1);
        //Ungrip Platform
        platform.setPosition(Servo.MIN_POSITION);
        //Strafe Right for .75 seconds
        strafe(0, 1, .75);
        stop();
    }
}
