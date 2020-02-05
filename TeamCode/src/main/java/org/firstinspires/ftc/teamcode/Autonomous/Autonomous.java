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
    Servo armClasp, armPivot, platformLeft, platformRight;
    protected CameraName cameraName;

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
        platformLeft = robot.platformLeft;
        platformRight = robot.platformRight;
        armPivot = robot.armPivot;
        armClasp = robot.armClasp;
        cameraName = robot.cameraName;

        //Set Beginning Position Servos
        platformLeft.setPosition(Servo.MAX_POSITION);
        platformRight.setPosition(Servo.MIN_POSITION);
        robot.armPivot.setPosition(Servo.MAX_POSITION);
        robot.armClasp.setPosition(Servo.MIN_POSITION);

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
     */
    public void strafe(double vertical, double horizontal, double time) {
        double movement = vertical;
        double strafe = horizontal;
        double magnitude = Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
        double direction = Math.atan2(-horizontal, vertical);

        //INFO Increasing speed to maximum of 1
        double lf = magnitude * Math.sin(direction + Math.PI / 4);
        double lb = magnitude * Math.cos(direction + Math.PI / 4);
        double rf = magnitude * Math.cos(direction + Math.PI / 4);
        double rb = magnitude * Math.sin(direction + Math.PI / 4);
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

        waitFor(time);

        StopDriveMotors();
    }

    void waitAbsolute(double seconds) {
        /*
         * TODO Keep the robot waiting until a certain time is reached.
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
    } //wait to move on to next step

    void waitFor(double seconds) {
        //adds the seconds to the current time
        waitAbsolute(getNewTime(seconds));
    }

    double getNewTime(double addedSeconds) {
        return runtime.seconds() + addedSeconds;
    }

    void timeTurn(double speed, double time) {

        leftFront.setPower(speed);
        leftBack.setPower(speed);
        rightFront.setPower(-speed);
        rightBack.setPower(-speed);

        waitFor(time);

        StopDriveMotors();

    }

    /*void AbsoluteTurn(double speed, double targetAngle) {

        double currentAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;


        if (currentAngle < targetAngle) {

            while (opModeIsActive() && imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle < targetAngle) {

                leftFront.setPower(-speed);
                rightFront.setPower(speed);
                leftBack.setPower(-speed);
                rightBack.setPower(speed);
            }


        } else if (currentAngle > targetAngle) {

            while (opModeIsActive() && imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle > targetAngle) {

                leftFront.setPower(speed);
                rightFront.setPower(-speed);
                leftBack.setPower(speed);
                rightBack.setPower(-speed);
            }
        }

        StopDriveMotors();

    }*/

    private double correctAngle(double angle) { // [-180, 180] → [0, 360]
        return angle + 180;
    }

    /*private double getCorrectedAngle() {
        return correctAngle(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
    }*/

    /*void AbsoluteTurnCorrected(double speed, double targetAngle) {
        double currentAngle = getCorrectedAngle();
        targetAngle = correctAngle(targetAngle);
        int rollovers = Math.abs((int) (targetAngle / 360));
        double targetAfterRollover = targetAngle % 360;
        if (targetAngle < 0) {
            rollovers++;
            targetAfterRollover += 360;
        }
        if (targetAngle > currentAngle) {
            leftFront.setPower(-speed);
            rightFront.setPower(speed);
            leftBack.setPower(-speed);
            rightBack.setPower(speed);

            for (int i = 0; i < rollovers; i++) {
                while (opModeIsActive() && getCorrectedAngle() <= 180) {
                    // do nothing
                }
                while (opModeIsActive() && getCorrectedAngle() >= 180) {
                    // do nothing
                }
                // this constitutes 1 rollover
            }
            while (opModeIsActive() && getCorrectedAngle() < targetAfterRollover) {
                // do nothing
            }
        } else if (targetAngle < currentAngle) {
            leftFront.setPower(speed);
            rightFront.setPower(-speed);
            leftBack.setPower(speed);
            rightBack.setPower(-speed);

            for (int i = 0; i < rollovers; i++) {
                while (opModeIsActive() && getCorrectedAngle() >= 180) {
                    // do nothing
                }
                while (opModeIsActive() && getCorrectedAngle() <= 180) {
                    // do nothing
                }
                // this constitutes 1 rollover
            }
            while (opModeIsActive() && getCorrectedAngle() > targetAfterRollover) {
                // do nothing
            }
        }
        StopDriveMotors();
    }*/

    //encoder constants
    static final double COUNTS_PER_MOTOR_REV = 2240;    // change for mecanum
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

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

    private void StopDriveMotors() {

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }

    /*public AutonomousRight.SkyStonePosition CameraTime() {
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }*/

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        /*if (tfod != null) {
            tfod.activate();
        }

        //Wait for the game to begin
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        //waitForStart();

        AutonomousRight.SkyStonePosition pos = AutonomousRight.SkyStonePosition.FIRST;
        while (!isStarted() && !isStopRequested()) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                ArrayList<Item> sortedRecognition = new ArrayList<>();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    int skyStoneCount = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        sortedRecognition.add(new Item(recognition));*/
                        /* note: the following conditions mean:
                            recognition.getWidth() < recognition.getImageWidth() / 3
                                avoids a very wide false positive that can be caused by the background
                            recognition.getBottom() > recognition.getImageHeight() * 2 / 3
                                ignores any minerals in the crater
                            recognition.getWidth() < 1.5 * recognition.getHeight()
                                avoids a rectangular false positive generated by the red x
                        */
                        //if (recognition.getWidth()< recognition.getImageWidth() / 3 /*&&
                        /*        recognition.getBottom() > recognition.getImageHeight() * 2 / 3 &&
                                recognition.getWidth() < 1.5 * recognition.getHeight()*///) {
                         /*   if (recognition.getLabel().equals(LABEL_FIRST_ELEMENT)) {
                                skyStoneCount++;
                                if (recognition.getLeft() < recognition.getImageWidth() / 3) {
                                    pos = AutonomousRight.SkyStonePosition.THIRD;
                                } else if (recognition.getLeft() < recognition.getImageWidth() / 3 * 2) {
                                    pos = AutonomousRight.SkyStonePosition.SECOND;
                                } else {
                                    pos = AutonomousRight.SkyStonePosition.FIRST;
                                }
                            }
                        }
                    }

                    Collections.sort(sortedRecognition);
                    telemetry.addData("Sorted: ", sortedRecognition.toString());
                    if (skyStoneCount <= 1) {
                        if (pos == AutonomousRight.SkyStonePosition.THIRD) {
                            telemetry.addData("Gold Mineral Position", "Left");
                        } else if (pos == AutonomousRight.SkyStonePosition.SECOND) {
                            telemetry.addData("Gold Mineral Position", "Center");
                        } else if (pos == AutonomousRight.SkyStonePosition.FIRST) {
                            telemetry.addData("Gold Mineral Position", "Right");
                        }
                    } else {
                        pos = AutonomousRight.SkyStonePosition.FIRST;
                    }
                    telemetry.update();
                }
            }
        }
        return pos;
    }*/
}
