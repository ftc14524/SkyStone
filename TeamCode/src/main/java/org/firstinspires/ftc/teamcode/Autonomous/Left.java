package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.Servo;



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

@Autonomous(name = "TEST Left", group = "Autonomous")

public class Left extends org.firstinspires.ftc.teamcode.Autonomous.AutonomousLeft {

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
        platform = robot.platform;
        armPivot = robot.armPivot;
        liftGripper = robot.liftGripper;
        armClasp = robot.armClasp;

        platform.setPosition(Servo.MIN_POSITION);
        robot.liftRotate.setPosition(Servo.MIN_POSITION);
        robot.armPivot.setPosition(Servo.MAX_POSITION);
        robot.armClasp.setPosition(Servo.MIN_POSITION);
        robot.liftGripper.setPosition(Servo.MAX_POSITION);
        robot.liftRotate.setPosition(Servo.MIN_POSITION);//Already repeating
        robot.pushToLift.setPosition(Servo.MAX_POSITION);


        platform.setPosition(Servo.MAX_POSITION);
        armPivot.setPosition(Servo.MAX_POSITION);
        waitForStart();
        strafe(0, -1, 1, 1.5);
        stop();

    }
}
