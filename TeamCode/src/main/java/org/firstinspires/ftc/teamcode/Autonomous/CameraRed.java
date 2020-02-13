package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.tensorflow.lite.Interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class determines which positions the SkyStones are in on the Red Alliance
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Camera Red", group = "Camera Red")
public class CameraRed extends Autonomous{

    protected CameraName cameraName;

    //Used to signify which position the SkyStones are in
    public enum SkyStonePosition {
        FIRST, SECOND, THIRD
    }

    //TensorFlow Variables
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

    //Instantiating this variables runs the correct code, located in the constructor of each class
    Autonomous choose;

    @Override
    public void runOpMode() {

        //First run the Autonomous class code of setting up hardware
        super.runOpMode();
        cameraName = robot.cameraName;

        //Now find the SkyStone and run the correct code
        switch (CameraTime()) {
            case FIRST:
                choose = new FirstSkyStoneRed();
                break;
            case SECOND:
                choose = new SecondSkyStoneRed();
                break;
            case THIRD:
                choose = new ThirdSkyStoneRed();
                break;
            default:
                choose = new FirstSkyStoneBlue();
                break;
        }
    }

    /**
     * Uses the camera to determine the position of the SkyStones
     * @return the enum representation of the position of the SkyStones
     */
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

        //Wait for the game to begin
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        SkyStonePosition pos = SkyStonePosition.FIRST;
        while (!isStarted() && !isStopRequested()) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                ArrayList<Item> sortedRecognition = new ArrayList<Item>();
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
                        if (recognition.getWidth()< recognition.getImageWidth() / 3 /*&&
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

    /**
     * Initialize TensorFlow
     */
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

    /**
     * Use the IMU to turn at a specified angle
     * @param speed how fast to turn
     * @param targetAngle how much to turn in terms of angles
     */
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

    /**
     * Get calibrated angle measures based on the imu
     * @return angle with correct direction
     */
    private double getCorrectedAngle() {
        return correctAngle(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
    }

    /**
     * More accurate turning
     * @param speed how fast to turn
     * @param targetAngle which angle to turn at
     */
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
}
