package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Chosen Mode - Second Blue", group = "Camera Blue")
public class SecondSkyStoneBlue extends CameraBlue{
    public SecondSkyStoneBlue()
    {
        //First Block
        //Strafe forward and back .8 second.
        strafe(-1, -1, .8);
        //Lower the arm
        armPivot.setPosition(Servo.MAX_POSITION);
        //Grabs block
        armClasp.setPosition(Servo.MAX_POSITION);
        //Lifts Arm
        armPivot.setPosition(Servo.MIN_POSITION);
        //Strafe right for .2 seconds.
        strafe(0, 1, .2);
        //Go forward for 3 seconds
        strafe(1, 0, 2.8);
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
        //Go backward for 1.5 seconds
        strafe(-1, 0, 1.4);
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
        //Go forward for 1.5 seconds
        strafe(1, 0, 1.4);
        //Strage left for .2 seconds
        strafe(0, -1, .2);

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
        platformLeft.setPosition(Servo.MAX_POSITION);
        //Turn clockwise 90 degrees
        timeTurn(1, .5);
        //Go forward and strafe left for 1 second
        strafe(1, -1, 1);
        //Ungrip Platform
        platformLeft.setPosition(Servo.MIN_POSITION);
        //Strafe Left for .75 seconds
        strafe(0, -1, .75);
        stop();
/*
        ////strafe right 1 second.
        strafe(0, 1, 1, 1);
        //Move forward 1/2 second
        strafe(1, 0, 1, .5);
        //Lower the arm
        armPivot.setPosition(Servo.MAX_POSITION);
        ////strafe left for .2 seconds.
        strafe(0, -1, 1, .2);
        //Go back for 3 seconds
        strafe(-1, 0, 1, 3);
        //Let go of arm
        armPivot.setPosition(Servo.MIN_POSITION);
        //Forward 1.5 seconds.
        strafe(1, 0, 1, 1.5);
        ////strafe right .2
        strafe(0, 1, 1, .2);
        //Clamp
        armPivot.setPosition(Servo.MAX_POSITION);
        ////strafe left .2
        strafe(0, -1, 1, .2);
        //Go back 1.5
        strafe(-1, 0, 1, 1.5);
        //Forward .5
        strafe(1, 0, 1, .5);
        stop();
 */
    }

}
