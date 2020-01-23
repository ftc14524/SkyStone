package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.tensorflow.lite.Interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Test Auto Right", group = "Camera Red")

public class CameraRed extends Autonomous {

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

    public CameraRed(){

    }

    @Override
    public void runOpMode() {
        super.runOpMode();
        CameraTime();

    }

    public AutonomousRight.SkyStonePosition CameraTime() {
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
                        sortedRecognition.add(new Item(recognition));
                        if (recognition.getWidth() < recognition.getImageWidth() / 3) {
                            if (recognition.getLabel().equals(LABEL_FIRST_ELEMENT)) {
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
    }
}
