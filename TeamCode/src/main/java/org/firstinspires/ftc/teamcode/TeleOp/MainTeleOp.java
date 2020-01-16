package org.firstinspires.ftc.teamcode.TeleOp;

import android.os.Handler;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.lang.Math;

/*
 Notes:
   Encoders are doubles
   .getCurrentPosition() retrieves encoder values
   left and right triggers on controllers are scaled 0-1
   .getMode() exists
*/
@TeleOp(name = "Testing TeleOp", group = "Linear Opmode")

/**
 * MainTeleOp is the class responsible for all of the TeleOp methods. It has a robot, movement, rotation, strafe, eight motors, and five servos
 */
public class MainTeleOp extends OpMode {

    //TODO ArmClasp resets after starting
    //TODO ArmPivot unfroze
    //TODO Turning of robot is slow
    //TODO TensorFlow
    //Create a robot---responsible for connecting hardware of Hardware class to methods
    Hardware robot;
    private ElapsedTime runtime;
    //private ElaspedTime runTime; for if you need to drive by time

    //Directions
    double movement;
    double rotation;
    double strafe;

    //Define the Motors and Servos here to not rely on referencing the robot variable to access the motors and servos
    DcMotor leftFront, rightFront, leftBack, rightBack, greenWheelLeft, greenWheelRight, lift;
    Servo liftGripper, liftRotate, pushToLift, leftIntake, rightIntake, armPivot, armClasp, platform;

    @Override
    /**
     * Initializes the robot by mapping the hardware, resetting encoders, and setting servos to the correct starting positions
     */
    public void init() {
        //Map hardware
        robot = new Hardware(hardwareMap);
        //Assign the motors and servos to the ones on the robot to not require calling robot everytime a method or servo needs to be called.
        leftFront = robot.leftFront;
        rightFront = robot.rightFront;
        rightBack = robot.rightBack;
        leftBack = robot.leftBack;
        greenWheelLeft = robot.greenWheelLeft;
        greenWheelRight = robot.greenWheelRight;
        lift = robot.lift;
        liftGripper = robot.liftGripper;
        liftRotate = robot.liftRotate;
        pushToLift = robot.pushToLift;
        leftIntake = robot.leftIntake;
        rightIntake = robot.rightIntake;
        armPivot = robot.armPivot;
        armClasp = robot.armClasp;
        platform = robot.platform;

        //Sets encoders back to 0 so that they are not messed up.
        robot.resetDriveEncoders();

        //Set starting position for arm servo
        platform.setPosition(Servo.MIN_POSITION);
        liftRotate.setPosition(Servo.MIN_POSITION);
        armPivot.setPosition(Servo.MAX_POSITION);
        armClasp.setPosition(Servo.MIN_POSITION);
        liftGripper.setPosition(Servo.MAX_POSITION);
        liftRotate.setPosition(Servo.MIN_POSITION);
        pushToLift.setPosition(Servo.MAX_POSITION);

        //Variable to track time for running robot on time if needed
        //runtime = new ElapsedTime();
    }

    @Override
    /**
     * Runs the main methods of TeleOp and telemetry.
     * Loop repeats so that it is checking controllers and telemetry values at all times for continuous running
     */
    public void loop() {
        //Methods responsible for control of different parts of the the robot
        DriveControl();
        ArmAndPlatformControl();
        LiftControl();
        Intake();

        //Determine lift encoder limits
        telemetry.addData("Lift Encoders", lift.getCurrentPosition());

        //Show Telemetry on Driver Station Phone
        telemetry.update();
    }

    /**
     * To control the intake, holding gamepad 2 right trigger will reverse the intake to spit
     * The left trigger turns off the beginning function
     */
    public void Intake() {
        boolean forward = true;
        boolean beginning = true;
        double speed = 0.5;
        //Turn off the beginning stage
        if (gamepad2.left_trigger > 0) {
            beginning = false;
        }
        //The intake will spit out until we leave the beginning stage
        if (beginning) {
            greenWheelRight.setPower(-1);
            greenWheelLeft.setPower(1);
        }
        //Now, the intake should always be moving forward
        if (gamepad2.right_trigger > 0)
            forward = false;
        else
            forward = true;
        //The !beginning makes sure the regular functions don't run until we exit beginning stage

        //Regular intake
        if (!beginning && forward) {
            greenWheelRight.setPower(speed);
            greenWheelLeft.setPower(-1 * speed);
        }
        //Spit function
        if (!beginning && !forward) {
            greenWheelRight.setPower(-1);
            greenWheelLeft.setPower(1);
        }

    }

    public void DriveControl() {
        movement = gamepad1.left_stick_y;
        rotation = gamepad1.right_stick_x;
        strafe = gamepad1.left_stick_x;
        double magnitude = Math.sqrt(Math.pow(gamepad1.left_stick_x, 2) + Math.pow(gamepad1.left_stick_y, 2));
        double direction = Math.atan2(-gamepad1.left_stick_x, gamepad1.left_stick_y);
        double rotation = gamepad1.right_stick_x;

        //INFO Increasing speed to maximum of 1
        double lf = magnitude * Math.sin(direction + Math.PI / 4) - rotation;
        double lb = magnitude * Math.cos(direction + Math.PI / 4) - rotation;
        double rf = magnitude * Math.cos(direction + Math.PI / 4) + rotation;
        double rb = magnitude * Math.sin(direction + Math.PI / 4) + rotation;
        double hypot = Math.hypot(movement, strafe);
        double ratio;

        //TODO ask about the ratio purpose
        if (movement == 0 && strafe == 0)
            ratio = 1;
        else
            ratio = hypot / (Math.max(Math.max(Math.max(Math.abs(lf), Math.abs(lb)), Math.abs(rb)), Math.abs(rf)));

        leftFront.setPower(ratio * lf);
        leftBack.setPower(ratio * lb);
        rightFront.setPower(ratio * rf);
        rightBack.setPower(ratio * rb);
    }


    //Function for handling horizontal lift
    public void LiftControl() {
        //Encoder limits to prevent breaking the lift
        final int UPPER_LIFT_LIMIT = 3000;
        final int LOWER_LIFT_LIMIT = 0;

        //Control the lift
        if(lift.getCurrentPosition() < UPPER_LIFT_LIMIT && lift.getCurrentPosition() > LOWER_LIFT_LIMIT)
            lift.setPower(gamepad2.left_stick_y);
        else
        {
            if(lift.getCurrentPosition() > UPPER_LIFT_LIMIT)
                lift.setTargetPosition(UPPER_LIFT_LIMIT - 2);
            if(lift.getCurrentPosition() < LOWER_LIFT_LIMIT)
                lift.setTargetPosition(LOWER_LIFT_LIMIT + 2);
        }

        boolean leftBumper = gamepad2.left_bumper;
        boolean rightBumper = gamepad2.right_bumper;


        if (leftBumper)
            liftGripper.setPosition(Servo.MAX_POSITION);
        if (rightBumper)
            liftGripper.setPosition(Servo.MIN_POSITION);

        //TODO Move verticalLifts and armRotate
        if (gamepad2.x) {
            liftGripper.setPosition(Servo.MAX_POSITION);
            pushToLift.setPosition(Servo.MIN_POSITION);
        }
        if (gamepad2.y) {
            liftGripper.setPosition(Servo.MIN_POSITION);
        }

        if (gamepad2.dpad_up) {
            double current = runtime.milliseconds();
            //liftGripper.setPosition(Servo.MAX_POSITION);  //Should not be here
            pushToLift.setPosition(Servo.MIN_POSITION);
            while (runtime.milliseconds() < current + 1000) ;
            /*
                lift.setpower(1);

             */
            liftGripper.setPosition(Servo.MIN_POSITION);
        }

        //Direct control of push to Lift
        if(gamepad2.a)
            pushToLift.setPosition(Servo.MAX_POSITION);
        if(gamepad2.b)
            pushToLift.setPosition(Servo.MIN_POSITION);


        //Controls for the lift rotate --- also involve keeping the push set outwards
        if(gamepad2.right_stick_x > 0.5) {
            liftRotate.setPosition(Servo.MAX_POSITION);
            pushToLift.setPosition(Servo.MIN_POSITION);
        }
        if(gamepad2.right_stick_x < -0.5) {
            liftRotate.setPosition(Servo.MIN_POSITION);
            pushToLift.setPosition(Servo.MIN_POSITION);
        }

        /*
         if (gamepad2.dpad_down) {
            double current = runtime.milliseconds();
            //liftGripper.setPosition(Servo.MAX_POSITION); - Should not be here
            pushToLift.setPosition(Servo.MIN_POSITION);
            while (runtime.milliseconds() < current + 1000);
                lift.setpower(-1);
            liftGripper.setPosition(Servo.MIN_POSITION);
            }

            if(gamepad2.x)
            {
                pushToLift.setPosition(Servo.MIN_POSITION);
                liftGripper.setPosition(Servo.MIN_POSITION);
                //Encoder motor lift up
                liftRotate.setPosition(Servo.MAX_POSITION);
                pushToLift.setPosition(Servo.MAX_POSITION);
                //Encoder Motor lift down
            }
            if(gamepad2.y)
            {
                pushToLift.setPosition(Servo.MAX_POSITION);
                liftGripper.setPosition(Servo.MAX_POSITION);
                //Encoder motor lift up to proper height
                liftRotate.setPosition(Servo.MAX_POSITION);
                pushToLift.setPosition(Servo.MAX_POSITION);
                //Encoder Motor lift down

            }



         */


    }

    public void ArmAndPlatformControl() {
        if (gamepad1.a)
            armPivot.setPosition(Servo.MAX_POSITION);
        if (gamepad1.b)
            armPivot.setPosition(Servo.MIN_POSITION);

        if (armPivot.getPosition() == Servo.MAX_POSITION)
            armClasp.setPosition(Servo.MAX_POSITION);
        else
            armClasp.setPosition(Servo.MIN_POSITION);

        if (gamepad1.x)
            platform.setPosition(Servo.MAX_POSITION);

        if (gamepad1.y)
            platform.setPosition(Servo.MIN_POSITION);
    }
}


