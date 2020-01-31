package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Hardware;
import org.tensorflow.lite.Interpreter;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//motor.setZeroPowerBehavior (if you want it float or brake)
//opModeIsActive() //if you running within 30 seconds
//robot is a hardware object so you can use hardware methods
//idle() -

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Test Auto Right", group = "Autonomous Right")

public class AutonomousRight extends LinearOpMode {

    //TODO The lift needs to drop at the end of autonomous period
    Hardware robot;
    ElapsedTime runtime = new ElapsedTime();
    DcMotor leftFront, rightFront, leftBack, rightBack;
    Servo armClasp, armPivot;
    public Servo platform, liftGripper;
    protected CameraName cameraName;

    public enum SkyStonePosition {
        FIRST, SECOND, THIRD;
    }

    AutonomousChoice choose;

    //TensorFlow
    protected static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    public Interpreter tflite;

    //imute
    BNO055IMU imu;
    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;
    double baseAngle;

    //Vuforia stuff
    protected static final String VUFORIA_KEY = "ATKKdVf/////AAABmb9SxtpqfUvxqCFmSowoT10see3Vz9mze+DVTbtqieMNjFxZverOpqc4OYMhAkuv9rnJMQZyuaweuLOXioXqVuYJ2P2yRohAKL//zPiF1drlPCUbzdhh3pFV8X4rnBILwoF9C3gWvpQfB//IJdZXNBkWYOZAp+UXGBW2WGdt2rQFHw4Y23GrGb2XCmPEHynO8tiNb6IzR6vOh/KOZ8GyTVES7+GyMVhFWNqgL969+ra6Ev5mgfDqaIt4DAqOoiMomDF9mm+Ixx7m6R2pwJC69XVvqAE6+fuotOs8fvA2XRtU+NNaD2ALR247keSC3qK0RnH8JGjYbSmiOHuRqHW9p9J/JrG1OPOxKnKuGEhhcgA7";
    protected VuforiaLocalizer vuforia;
    //TensorFlow Object detector
    protected TFObjectDetector tfod;

    //Constants
    protected static final double COUNTS_PER_INCH_HD_MECANUM = 1120 / Math.PI / 4;
    protected static final int COUNTS_PER_REV_CORE = 288;
    protected static final double TURN_DISTANCE_PER_DEGREE = Math.sqrt(1560.49) * Math.PI / 360 / 2;

    @Override
    public void runOpMode() {
        robot = new Hardware(hardwareMap);
        leftFront = robot.leftFront;
        rightFront = robot.rightFront;
        leftBack = robot.leftBack;
        rightBack = robot.rightBack;
        platform = robot.platformLeft;
        armPivot = robot.armPivot;
        liftGripper = robot.liftGripper;

        platform.setPosition(Servo.MIN_POSITION);
        robot.liftRotate.setPosition(Servo.MIN_POSITION);
        robot.armPivot.setPosition(Servo.MAX_POSITION);
        robot.armClasp.setPosition(Servo.MIN_POSITION);
        robot.liftGripper.setPosition(Servo.MAX_POSITION);
        robot.liftRotate.setPosition(Servo.MIN_POSITION);//Already repeating
        robot.pushToLift.setPosition(Servo.MAX_POSITION);
        armClasp = robot.armClasp;

        //INFO If it is on the right
        //choose = new Platform(false);

        //INFO For recognizing the skystones
        /*switch (CameraTime()) {
            case FIRST:
                choose = new AkshyatFirstSkyStoneLeft();
                break;
            case SECOND:
                choose = new AkshyatSecondSkystoneLeft();
                break;
            case THIRD:
                choose = new AkshyatThirdSkyStoneLeft();
                break;
            default:
                choose = new AkshyatFirstSkyStoneLeft();
                break;
        }*/

    }


    public void rotate(boolean right, double time, double power) {
        if (right) {
            rightFront.setPower(1 * (power * .5));
            rightBack.setPower(1 * (power * .5));
            leftFront.setPower(-1 * (power));
            leftBack.setPower(-1 * (power));
        } else {
            rightFront.setPower(-1 * (power));
            rightBack.setPower(-1 * (power));
            leftFront.setPower(1 * (power * .5));
            leftBack.setPower(1 * (power * .5));
        }

        waitFor(time);
        StopDriveMotors();
    }

    public void strafe(double vertical, double horizontal, double power, double time) {
        double movement = vertical;
        double strafe = horizontal;
        double magnitude = Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
        double direction = Math.atan2(-gamepad1.left_stick_x, gamepad1.left_stick_y);

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
        /////////////////////////////////////////////////////////////////////////////
//        double magnitude = power * 1;
//        double direction = Math.atan2(-vertical, horizontal);
//
//        double lf = magnitude * Math.sin(direction + Math.PI / 4);
//        double lb = magnitude * Math.cos(direction + Math.PI / 4);
//        double rf = magnitude * Math.cos(direction + Math.PI / 4);
//        double rb = magnitude * Math.sin(direction + Math.PI / 4);
//
//        leftFront.setPower(lf);
//        leftBack.setPower(lb);
//        rightFront.setPower(rf);
//        rightBack.setPower(rb);

        waitFor(time);

        StopDriveMotors();
    }

    public void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        //INFO Load the model and the classes.
        //TODO need to load the classes. I don't know the class names.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    protected void initVuforia() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
//        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraName = cameraName;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
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

    void AbsoluteTurn(double speed, double targetAngle) {

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

    }

    private double correctAngle(double angle) { // [-180, 180] → [0, 360]
        return angle + 180;
    }

    private double getCorrectedAngle() {
        return correctAngle(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
    }

    void AbsoluteTurnCorrected(double speed, double targetAngle) {
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
    }

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

            platform.setPosition(Servo.MAX_POSITION);
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

    public SkyStonePosition CameraTime() {
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        //waitForStart();

        SkyStonePosition pos = SkyStonePosition.FIRST;
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
                        sortedRecognition.add(new Item(recognition));
                        /* note: the following conditions mean:
                            recognition.getWidth() < recognition.getImageWidth() / 3
                                avoids a very wide false positive that can be caused by the background
                            recognition.getBottom() > recognition.getImageHeight() * 2 / 3
                                ignores any minerals in the crater
                            recognition.getWidth() < 1.5 * recognition.getHeight()
                                avoids a rectangular false positive generated by the red x
                        */
                        if (recognition.getWidth() < recognition.getImageWidth() / 3 /*&&
                                recognition.getBottom() > recognition.getImageHeight() * 2 / 3 &&
                                recognition.getWidth() < 1.5 * recognition.getHeight()*/) {
                            if (recognition.getLabel().equals(LABEL_FIRST_ELEMENT)) {
                                skyStoneCount++;
                                if (recognition.getLeft() < recognition.getImageWidth() / 3) {
                                    pos = SkyStonePosition.THIRD;
                                } else if (recognition.getLeft() < recognition.getImageWidth() / 3 * 2) {
                                    pos = SkyStonePosition.SECOND;
                                } else {
                                    pos = SkyStonePosition.FIRST;
                                }
                            }
                        }
                    }

                    Collections.sort(sortedRecognition);
                    telemetry.addData("Sorted: ", sortedRecognition.toString());
                    if (skyStoneCount <= 1) {
                        if (pos == SkyStonePosition.THIRD) {
                            telemetry.addData("Gold Mineral Position", "Left");
                        } else if (pos == SkyStonePosition.SECOND) {
                            telemetry.addData("Gold Mineral Position", "Center");
                        } else if (pos == SkyStonePosition.FIRST) {
                            telemetry.addData("Gold Mineral Position", "Right");
                        }
                    } else {
                        pos = SkyStonePosition.FIRST;
                    }
                    telemetry.update();
                }
            }
        }
        return pos;
    }


}

