package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Chosen Mode - Second Red", group = "Camera Red")
public class SecondSkyStoneRed extends CameraRed{
    public SecondSkyStoneRed()
    {
        //First Block
        //Strafe left and forward 1 second.
        strafe(1, -1, 1, .8);
        //Lower the arm
        armPivot.setPosition(Servo.MAX_POSITION);
        //Grabs block
        armClasp.setPosition(Servo.MAX_POSITION);
        //Lifts Arm
        armPivot.setPosition(Servo.MIN_POSITION);
        //Strafe right for .2 seconds.
        strafe(0, 1, 1, .2);
        //Go backward for 3 seconds
        strafe(1, 0, 1, 2.8);
        //Strafe left for .2 seconds
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
        //Go forward for 1.5 seconds
        strafe(1, 0, 1, 1.4);
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
        //Go backward for 1.5 seconds
        strafe(-1, 0, 1, 1.4);
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

        //Grip Platform
        platformLeft.setPosition(Servo.MAX_POSITION);
        //Turn counterclockwise 90 degrees
        timeTurn(-1, .5);
        //Go backward and strafe left for 1 second
        strafe(-1, -1, .5, 1);
        //Ungrip Platform
        platformLeft.setPosition(Servo.MIN_POSITION);
        //Strafe Right for .75 seconds
        strafe(0, 1, 1, .75);
        stop();
    }
}
