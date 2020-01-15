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
        //armPivot.setPosition(Servo.MIN_POSITION);
        //armClasp.setPosition(Servo.MIN_POSITION);

        //Variable to track time for running robot on time if needed
        runtime = new ElapsedTime();
    }

    @Override
    /**
     * Runs the main methods of TeleOp and telemetry.
     * Loop repeats so that it is checking controllers and telemetry values at all times for continuous running
     */
    public void loop() {
        //Methods responsible for control of different parts of the the robot
        /////////////////DriveControl();
        //ArmAndPlatformControl();
        LiftControl();
        /////////////////////Intake();

        //TODO ask about horizontal lift constant motion && the specificity of the compliance wheels

        telemetry.addData("Lift Encoders", lift.getCurrentPosition());

        //Show Telemetry on Driver Station Phone
        telemetry.update();
    }

    /**
     * To control the intake, holding gamepad 2 B will reverse the intake to spit
     */
    public void Intake() {
        boolean forward = true;
        boolean beginning = true;
        double speed = 1;
        double spit = 0.5;
        if (gamepad2.x) {
            beginning = false;
        }
        if (beginning) {
           /*greenWheelRight.setPower(-1*spit);
           greenWheelLeft.setPower(spit);*/
            greenWheelRight.setPower(spit);
            greenWheelLeft.setPower(-1 * spit);
        }
        if (gamepad2.y)
            forward = false;
        else
            forward = true;
        if (!beginning && forward) {
           /*greenWheelRight.setPower(speed);
           greenWheelLeft.setPower(-1*speed);*/
            greenWheelRight.setPower(-1 * speed);
            greenWheelLeft.setPower(speed);
        } else if (!beginning && !forward) {
           /*greenWheelRight.setPower(-1*spit);
           greenWheelLeft.setPower(spit);*/
            greenWheelRight.setPower(spit);
            greenWheelLeft.setPower(-1 * spit);
        }

    }

    public void DriveControl() {
        movement = gamepad1.left_stick_y;
        rotation = gamepad1.right_stick_x;
        strafe = gamepad1.left_stick_x;
        double magnitude = Math.sqrt(Math.pow(gamepad1.left_stick_x, 2) + Math.pow(gamepad1.left_stick_y, 2));
        double direction = Math.atan2(-gamepad1.left_stick_x, gamepad1.left_stick_y);
        double rotation = gamepad1.right_stick_x;

        //trig implementation
        //double power = Math.hypot(x1, y1);
        //double angle = Math.atan2(y1, x1) - Math.PI/4;

        //INFO Increasing speed to maximum of 1
        double lf = magnitude * Math.sin(direction + Math.PI / 4) - rotation;
        double lb = magnitude * Math.cos(direction + Math.PI / 4) - rotation;
        double rf = magnitude * Math.cos(direction + Math.PI / 4) + rotation;
        double rb = magnitude * Math.sin(direction + Math.PI / 4) + rotation;
        double hypot = Math.hypot(movement, strafe);
        double ratio;

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

        double vertical = gamepad2.left_stick_y;
        double horizontal = gamepad2.right_stick_x;
        lift.setPower(vertical);

        boolean leftBumper = gamepad2.left_bumper;
        boolean rightBumper = gamepad2.right_bumper;


        if (leftBumper)
            liftGripper.setPosition(Servo.MAX_POSITION);
        if (rightBumper)
            liftGripper.setPosition(Servo.MIN_POSITION);

        //TODO Move verticalLifts and armRotate
        if (gamepad2.a) {
            liftGripper.setPosition(Servo.MAX_POSITION);
            pushToLift.setPosition(Servo.MIN_POSITION);
        }
        if (gamepad2.b) {
            liftGripper.setPosition(Servo.MIN_POSITION);
        }

        if (gamepad2.dpad_up) {
            double current = runtime.milliseconds();
            liftGripper.setPosition(Servo.MAX_POSITION);
            pushToLift.setPosition(Servo.MIN_POSITION);
            while (runtime.milliseconds() < current + 1000) ;

            liftGripper.setPosition(Servo.MIN_POSITION);
        }
    }

    public void ArmAndPlatformControl() {
        if (gamepad1.a)
            armPivot.setPosition(Servo.MAX_POSITION);
        if (gamepad1.b)
            armPivot.setPosition(Servo.MIN_POSITION);

        if(armPivot.getPosition()==Servo.MAX_POSITION)
            armClasp.setPosition(Servo.MAX_POSITION);
        else
            armClasp.setPosition(Servo.MIN_POSITION);

        if (gamepad1.x)
            platform.setPosition(Servo.MAX_POSITION);

        if (gamepad1.y)
            platform.setPosition(Servo.MIN_POSITION);
    }
}


