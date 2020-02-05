package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import java.lang.Math;

/*
 Notes:
   Encoders are doubles
   .getCurrentPosition() retrieves encoder values
   left and right triggers on controllers are scaled 0-1
   .getMode() exists
*/
@TeleOp(name = "TeleOp", group = "Linear Opmode")

/**
 * MainTeleOp is the class responsible for all of the TeleOp methods. It has a robot, movement, rotation, strafe, seven motors, and seven servos
 */
public class MainTeleOp extends OpMode {

    //Create a robot---responsible for connecting hardware of Hardware class to methods
    Hardware robot;

    //Directions
    double movement;
    double rotation;
    double strafe;

    //Define the Motors and Servos here to not rely on referencing the robot variable to access the motors and servos
    DcMotor leftFront, rightFront, leftBack, rightBack, greenWheelLeft, greenWheelRight, lift;
    Servo liftLeft, liftRight, liftRotate, armPivot, armClasp, platformLeft, platformRight;

    @Override
    /**
     * Initializes the robot by mapping the hardware, resetting encoders, and setting servos to the correct starting positions
     * Runs when pressing "init" button for a TeleOp Mode
     */
    public void init() {

        //Map hardware
        robot = new Hardware(hardwareMap);
        //Assign the motors and servos to the ones on the robot to not require calling "robot" every time a method or servo needs to be called.
        leftFront = robot.leftFront;
        rightFront = robot.rightFront;
        rightBack = robot.rightBack;
        leftBack = robot.leftBack;
        greenWheelLeft = robot.greenWheelLeft;
        greenWheelRight = robot.greenWheelRight;
        lift = robot.lift;
        liftLeft = robot.liftLeft;
        liftRight = robot.liftRight;
        liftRotate = robot.liftRotate;
        armPivot = robot.armPivot;
        armClasp = robot.armClasp;
        platformLeft = robot.platformLeft;
        platformRight = robot.platformRight;

        //Sets encoders back to 0 so that they are not messed up.
        robot.resetDriveEncoders();

        //Set starting position for the servos so they don't exceed the limit
        platformLeft.setPosition(Servo.MIN_POSITION);
        platformRight.setPosition(Servo.MAX_POSITION);
        armPivot.setPosition(Servo.MAX_POSITION);
        armClasp.setPosition(Servo.MIN_POSITION);
        liftLeft.setPosition(Servo.MAX_POSITION);
        liftRight.setPosition(Servo.MIN_POSITION);
        liftRotate.setPosition(Servo.MIN_POSITION);

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
        telemetry.addData("Right Back", rightBack.getPower());
        telemetry.addData("Left Back", leftBack.getPower());
        telemetry.addData("Right Front", rightFront.getPower());
        telemetry.addData("Left Front", leftFront.getPower());
        telemetry.addData("JS LX", gamepad1.left_stick_x);
        telemetry.addData("JS LY", gamepad1.left_stick_y);
        telemetry.addData("JS RX", gamepad1.right_stick_x);
        telemetry.addData("JS RY", gamepad1.right_stick_y);

        //Show Telemetry on Driver Station Phone
        telemetry.update();

    }

    /**
     * To control the intake, holding gamepad 2 right trigger will reverse the intake to spit
     * The left trigger turns off the beginning function
     */
    public void Intake() {
        double speed = 0.5;
        boolean beginning = true;

        if (gamepad1.left_trigger > 0)
            beginning = false;

        //Regular intake
        if (!beginning) {
            greenWheelRight.setPower(speed);
            greenWheelLeft.setPower(-1 * speed);
        }

        //Spit function
        if (gamepad1.right_trigger > 0) {
            greenWheelRight.setPower(-1);
            greenWheelLeft.setPower(1);
        }
    }

    /**
     * Convert the joystick directions on gamepad 1 to the drive train movement
     * The right joystick controls rotation --- pushing right leads to turning right and pushing left leads to turning left
     * The left joystick controls strafing --- the direction pushed is the direction the robot moves, taking into account how far the joystick is being pushed
     */
    public void DriveControl() {

        //Get movement and directions from the joysticks
        movement = gamepad1.left_stick_y;
        rotation = gamepad1.right_stick_x;
        strafe = gamepad1.left_stick_x;
        //Use trig to convert coordinates into a direction and radius
        double magnitude = Math.sqrt(Math.pow(gamepad1.left_stick_x, 2) + Math.pow(gamepad1.left_stick_y, 2));
        double direction = Math.atan2(-gamepad1.left_stick_x, gamepad1.left_stick_y);
        double rotation = gamepad1.right_stick_x *1.2; //INFO the below line of code makes the robot's rotation a little more sensitive (faster)

        //INFO Increasing speed to maximum of 1
        //Calculate the power of each motor based on radius and direction
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

        //Motor power and ration combined
        leftFront.setPower(ratio * lf);
        leftBack.setPower(ratio * lb);
        rightFront.setPower(ratio * rf);
        rightBack.setPower(ratio * rb);

    }

    /**
     * Control the lifting mechanisms of the robot
     * The lift motor uses encoders for stages and preventing unspooling
     * The controls for the block holders and rotate is also listed here
     */
    public void LiftControl() {

        //Encoder limits to prevent breaking the lift
        final int UPPER_LIFT_LIMIT = 3000;
        final int LOWER_LIFT_LIMIT = 0;

        //Run lift with encoders in mind
        while(lift.getCurrentPosition() <= UPPER_LIFT_LIMIT && lift.getCurrentPosition() >= LOWER_LIFT_LIMIT)
            lift.setPower(gamepad2.left_stick_y);
        while(lift.getCurrentPosition() > UPPER_LIFT_LIMIT)
            lift.setPower(-1);
        while(lift.getCurrentPosition() < LOWER_LIFT_LIMIT)
            lift.setPower(1);

        //Controls for the block holders
        if(gamepad2.left_bumper)
        {
            liftLeft.setPosition(Servo.MIN_POSITION);
            liftRight.setPosition(Servo.MAX_POSITION);
        }
        if(gamepad2.right_bumper)
        {
            liftLeft.setPosition(Servo.MAX_POSITION);
            liftRight.setPosition(Servo.MIN_POSITION);
        }

        //Controls for the lift rotate
        if (gamepad2.right_stick_x > 0.5)
            liftRotate.setPosition(Servo.MAX_POSITION);

        if (gamepad2.right_stick_x < -0.5)
            liftRotate.setPosition(Servo.MIN_POSITION);

    }

    /**
     * Provides controls for the servos that control the arm and platform of the robot
     */
    public void ArmAndPlatformControl() {

        //Main Arm Control
        if (gamepad1.a)
            armPivot.setPosition(Servo.MAX_POSITION);
        if (gamepad1.b)
            armPivot.setPosition(Servo.MIN_POSITION);

        //Grasper Control
        if(gamepad1.dpad_up)
            armClasp.setPosition(Servo.MIN_POSITION);
        if(gamepad1.dpad_down)
            armClasp.setPosition(Servo.MAX_POSITION);

        //Platform Control
        if (gamepad1.x) {
            platformLeft.setPosition(Servo.MAX_POSITION);
            platformRight.setPosition(Servo.MIN_POSITION);
        }
        if (gamepad1.y) {
            platformLeft.setPosition(Servo.MIN_POSITION);
            platformRight.setPosition(Servo.MAX_POSITION);
        }

    }

}



