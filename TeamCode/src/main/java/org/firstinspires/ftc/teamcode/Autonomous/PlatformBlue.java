package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * This class makes the park the foundation in the building zone on the Red Alliance
 */
@Autonomous(name = "Platform Blue", group = "Autonomous")
public class PlatformBlue extends org.firstinspires.ftc.teamcode.Autonomous.Autonomous{

    @Override
    public void runOpMode() {

        super.runOpMode();
        waitForStart();
        //Approach foundation
        strafe(1, 0.97, 4.5);
        waitFor(1);
        //Grab foundation
        platform.setPosition(0.3);
        waitFor(1);

        strafe(-1, 0, 2.2);
        waitFor(0.3);

        rotate(true, 1, 1);
        waitFor(1);

        platform.setPosition(0);
        rotate(true, 1.5,1);
        strafe(-1, 0.2, 1.75);

        stop();

        /*
        //Pull foundation back
        strafe(-0.5, -0.4, 10);
        //strafe(-1, 0, 1.3);
        //waitFor(0.5);
        //Rotate foundation horizontally
        rotate(false, 2.5, 1);
        //waitFor(1);
        strafe(1,0,0.5);
        //Let go of platform
        platform.setPosition(Servo.MAX_POSITION);
        waitFor(1);
        //Push foundation against wall to keep foundation straight and ensure it fits in the building zone
        strafe(1, 0, 0.8);
        //waitFor(1);
        //Park under alliance bridge
        strafe(-1, 0,2.7);
        stop();
*/
    }
}