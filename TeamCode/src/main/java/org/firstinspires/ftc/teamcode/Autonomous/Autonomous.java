package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.teamcode.Hardware;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous Parent Class", group = "Autonomous")
@Disabled
/**
 * Autonomous provides the basis for all autonomous methods. It has a robot, a runtime, four motors, four servos, and a camera. It can rotate, strafe, and wait for a certain amount of time
 * This class is not run on its own, but rather other classes will run the OpMode of this class through a call of "super.runOpMode()"
 */
//TODO Check if @Disabled makes the code break
public class Autonomous extends LinearOpMode {

    //Declare parts of the robot
    //These will be instantiated in the OpMode so that "robot" does not need to be referenced to access the hardware of the robot
    Hardware robot;
    ElapsedTime runtime = new ElapsedTime();
    DcMotor leftFront, rightFront, leftBack, rightBack;
    Servo armClasp, armPivot, platform;

    //Encoder Constants
    static final double COUNTS_PER_MOTOR_REV = 2240;    // change for mecanum
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    /**
     * Instantiates the robot and gets it ready to move in autonomous mode
     * These lines are run first before lines from any other autonomous class
     */
    public void runOpMode() {

        //Map Hardware
        robot = new Hardware(hardwareMap);
        leftFront = robot.leftFront;
        rightFront = robot.rightFront;
        leftBack = robot.leftBack;
        rightBack = robot.rightBack;
        platform = robot.platform;
        armPivot = robot.armPivot;
        armClasp = robot.armClasp;


        //Set Beginning Position Servos
        platform.setPosition(Servo.MAX_POSITION);
        armPivot.setPosition(Servo.MAX_POSITION);
        armClasp.setPosition(Servo.MAX_POSITION);

    }

    /**
     * Rotates the robot to the left? right?
     //TODO Check rotation method, maybe even determine times that correspond to angle measures
     * @param right whether the robot is turning clockwise or not
     * @param time how long the robot is turning
     * @param power how much power the robot will turn
     */
    public void rotate(boolean right, double time, double power) {

        //Clockwise Turning
        if (right) {
            rightFront.setPower(1 * (power * .5));
            rightBack.setPower(1 * (power * .5));
            leftFront.setPower(-1 * (power));
            leftBack.setPower(-1 * (power));
        }
        //Counter-clockwise Turning
        else {
            rightFront.setPower(-1 * (power));
            rightBack.setPower(-1 * (power));
            leftFront.setPower(1 * (power * .5));
            leftBack.setPower(1 * (power * .5));
        }

        //Turn until the time stops
        waitFor(time);
        StopDriveMotors();

    }

    /**
     * Allows the robot to strafe during autonomous
     * @param vertical how forward the robot needs to move
     * @param horizontal how much to the side the robot needs to move
     * @param time how long the robot will move
     * Note: Negative horizontal means left
     * Note: Negative vertical means forward
     */
    public void strafe(double vertical, double horizontal, double time) {

        //Get the directions of the strafe
        double movement = vertical;
        double strafe = horizontal;
        double magnitude = Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
        double direction = Math.atan2(-horizontal, vertical);

        //Calculate power for each motor
        double lf = magnitude * Math.sin(direction + Math.PI / 4);
        double lb = magnitude * Math.cos(direction + Math.PI / 4);
        double rf = magnitude * Math.cos(direction + Math.PI / 4);
        double rb = magnitude * Math.sin(direction + Math.PI / 4);
        double hypot = Math.hypot(movement, strafe);
        double ratio;

        //TODO Ask purpose of this
        if (movement == 0 && strafe == 0)
            ratio = 1;
        else
            ratio = hypot / (Math.max(Math.max(Math.max(Math.abs(lf), Math.abs(lb)), Math.abs(rb)), Math.abs(rf)));

        //Set power of motors
        leftFront.setPower(ratio * lf);
        leftBack.setPower(ratio * lb);
        rightFront.setPower(ratio * rf);
        rightBack.setPower(ratio * rb);

        //Run motors for a certain time before stopping
        waitFor(time);
        StopDriveMotors();

    }

    /**
     * Method used by waitFor() to have the code not jump to the next line
     * @param seconds amount of time for robot to execute a command
     */
    void waitAbsolute(double seconds) {

        /*
         * Keep the robot waiting until a certain time is reached.
         * */
        while (opModeIsActive() && runtime.seconds() <= seconds) {
            if (!opModeIsActive()) {
                StopDriveMotors();
                break;
            }
            telemetry.addData("Time Remaining ", Math.ceil(seconds - runtime.seconds()));
            telemetry.update();
            telemetry.addData("Current Time ", runtime.seconds());
            telemetry.update();
            idle();
        }
        if (!opModeIsActive())
            stop();

    }

    /**
     * Makes the robot wait for a certain time before moving to the next command
     * @param seconds amount of time for robot to execute a command
     */
    void waitFor(double seconds) {
        //adds the seconds to the current time
        waitAbsolute(getNewTime(seconds));
    }

    /**
     * Used by waitFor() method to make robot wait for a certain amount of time
     * @param addedSeconds time for robot to wait
     * @return a time calculation for waitAbsolute()
     */
    double getNewTime(double addedSeconds) {
        return runtime.seconds() + addedSeconds;
    }

    /**
     * Turns the robot in one direction for a certain amount of time
     * @param speed how fast the robot moves
     * @param time how long for the robot to turn
     */
    void timeTurn(double speed, double time) {

        leftFront.setPower(speed);
        leftBack.setPower(speed);
        rightFront.setPower(-speed);
        rightBack.setPower(-speed);

        waitFor(time);

        StopDriveMotors();

    }

    /**
     * Shifts the angle of referenced
     * @param angle given angle
     * @return angle in correct reference
     */
    protected double correctAngle(double angle) {

        // [-180, 180] → [0, 360]
        return angle + 180;

    }

    /**
     * Use encoders to control how much to drive
     * @param speed how fast
     * @param leftInches how much to the left
     * @param rightInches how much to the right
     * @param timeout how long to drive
     */
    void EncoderDrive(double speed, double leftInches, double rightInches, double timeout) {
        ElapsedTime runtime = new ElapsedTime();

        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = leftFront.getCurrentPosition() + (int) ((leftInches * COUNTS_PER_INCH));
            newRightTarget = rightFront.getCurrentPosition() + (int) ((rightInches * COUNTS_PER_INCH));
            leftFront.setTargetPosition(newLeftTarget);
            rightFront.setTargetPosition(newLeftTarget);
            leftBack.setTargetPosition(newRightTarget);
            rightBack.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.
            runtime.reset();

            leftFront.setPower(Math.abs(speed));
            rightFront.setPower(Math.abs(speed));
            leftBack.setPower(Math.abs(speed));
            rightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeout) &&
                    (leftFront.isBusy() && rightFront.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        leftFront.getCurrentPosition(),
                        rightFront.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            StopDriveMotors();

            // Turn off RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    /**
     * Use encoders to strafe
     * @param speed how fast
     * @param leftInches how much to the left
     * @param rightInches how much to the right
     * @param timeout how long
     */
    void StrafeEncoderDrive(double speed, double leftInches, double rightInches, double timeout) {
        ElapsedTime runtime = new ElapsedTime();

        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = leftFront.getCurrentPosition() + (int) ((leftInches * COUNTS_PER_INCH));
            newRightTarget = rightBack.getCurrentPosition() + (int) ((rightInches * COUNTS_PER_INCH));
            leftFront.setTargetPosition(newLeftTarget);
            rightFront.setTargetPosition(-newLeftTarget);
            leftBack.setTargetPosition(-newRightTarget);
            rightBack.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // reset the timeout time and start motion.
            runtime.reset();

            leftFront.setPower(Math.abs(speed));
            rightFront.setPower(Math.abs(speed));
            leftBack.setPower(Math.abs(speed));
            rightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeout) &&
                    (leftFront.isBusy() && rightFront.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        leftFront.getCurrentPosition(),
                        rightFront.getCurrentPosition());
                telemetry.update();
            }

            //platform.setPosition(Servo.MAX_POSITION);
            //Turn clockwise 90 degrees
            timeTurn(1, .5);
            //Go forward and strafe left for 1 second
            StopDriveMotors();

            // Turn off RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    /**
     * Sets all the drive motors to 0
     */
    protected void StopDriveMotors() {

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

}
