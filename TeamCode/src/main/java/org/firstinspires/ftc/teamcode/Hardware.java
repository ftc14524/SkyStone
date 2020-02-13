package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

/**
 * This class defines all the hardware components of the robot. It has seven DcMotors, seven Servos, and one WebcamName. It can reset drive encoders and stop all motors
 */
public class Hardware {

        //Define all the motors, servos, and cameras of the robot

        //Drive Train
        public DcMotor leftFront, rightFront, rightBack, leftBack;
        //Intake
        public DcMotor greenWheelLeft, greenWheelRight;
        //Lift
        public DcMotor lift;
        //Lift Servos
        //public Servo liftRight; //, liftRotate;
        public Servo liftLeft, liftRight, liftRotate;
        //Side Arm and Platform Servos
        public Servo armPivot, armClasp, platform;
        //Camera
        public WebcamName cameraName;

        /**
         * Creates a new Hardware with all parts connected to a name
         * @param hwmp map of robot parts on the control hub
         */
        public Hardware(HardwareMap hwmp) {

            //Drive Train
            leftFront = hwmp.dcMotor.get("Left Front");
            rightFront = hwmp.dcMotor.get("Right Front");
            rightBack = hwmp.dcMotor.get("Right Back");
            leftBack = hwmp.dcMotor.get("Left Back");
            //Intake
            greenWheelLeft = hwmp.dcMotor.get("Green Wheel Left");
            greenWheelRight = hwmp.dcMotor.get("Green Wheel Right");
            //Lift
            lift = hwmp.dcMotor.get("Lift");
            //Lift Servos
            liftLeft = hwmp.servo.get("Lift Left");
            liftRight = hwmp.servo.get("Lift Right");
            liftRotate = hwmp.servo.get("Lift Rotate");
            //Side Arm and Platform Servos
            armPivot = hwmp.servo.get("Arm Pivot");
            armClasp = hwmp.servo.get("Arm Clasp");
            platform = hwmp.servo.get("Platform");
            //Camera
            cameraName = hwmp.get(WebcamName.class, "Webcam 1");

            //Flips motors because they are placed in the opposite direction on the robot---allows for all motors to move in the same direction for one value
            rightFront.setDirection(DcMotor.Direction.REVERSE);
            leftFront.setDirection(DcMotor.Direction.REVERSE);
            lift.setDirection(DcMotor.Direction.REVERSE);
            //leftBack.setDirection(DcMotor.Direction.REVERSE);

        }

        /**
         * Resets drive encoders so that they are starting from 0 at every time
         * Encoders are used to control how much a motor moves---used for travelling by distance and setting levels for lifting
         * Some motors aren't using encoders, and those need to be specified as well
         */
        public void resetDriveEncoders() {

            //Motors with Encoders
            lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //Motors without Encoders
            leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            greenWheelRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            greenWheelLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }

        /**
         * Stop all the motors and resets servo positions
         * Essentially stop the robot
         * Note: Motor power ranges go from -1 to 1 as a double explicit parameter
         * Note: Servo positions go from 0 to 1 as a double explicit parameter. The actual position of 0 and 1 are based on servo limits programmed by the servo programmer
         */
        public void stopAllMotors() {

            //Motors
            leftFront.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            leftBack.setPower(0);
            lift.setPower(0);
            greenWheelLeft.setPower(0);
            greenWheelRight.setPower(0);

            //Servos
            platform.setPosition(Servo.MIN_POSITION);
            liftLeft.setPosition(Servo.MIN_POSITION);
            liftRight.setPosition(Servo.MAX_POSITION);
            liftRotate.setPosition(Servo.MIN_POSITION);
            armPivot.setPosition(Servo.MIN_POSITION);
            armClasp.setPosition(Servo.MIN_POSITION);

        }

}
