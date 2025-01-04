package alphabots.config.util;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants {

    // Outtake
    public static double outtakeGrabClose = 1; //
    public static double outtakeGrabOpen = 0; //
    public static double outtakeRotateTransfer = 0.23; //
    public static double outtakeRotateSpecimenGrab = 0.55; //
    public static double outtakeRotateScore = 0.7;
    public static double outtakeRotateSpecimenScore = 0.4;

    public static double outtakeRotateIdle = 0.38;
    public static double outtakePivotTransfer= 0.87; //
    public static double outtakePivotScore = 0.6;
    public static double outtakePivotSpecimenGrab = 0.35; //
    public static double outtakePivotSpecimenScore = 0.45;
    public static double outtakePivotIdle = 0.5;

    public static double outtakeSpinForward = 0.62;

    public static double outtakeSpinBackward = 0.725;

    // Intake
    public static double intakeBoxIntake = 0.7;
    public static double intakeBoxIdle = 0;
    public static double intakeBoxOuttake = -1;
    public static double intakePivotTransfer= 0.32;
    public static double intakePivotGround = 0.95;
    public static double intakePivotHover = 0.8;
    public static double intakeWiperOpen = 0.3;
    public static double intakeWiperClose = 1;

    // Lift Positions
    public static int liftToZero = -10;
    public static int liftToHumanPlayer = 0;
    public static int liftToHighChamber = 680;
    public static int liftToHighBucket = 2475;
    public static int liftToLowBucket = 0;
    public static int liftToPark = 415;

    // Extend Positions
    public static int extendManualIncrements = 0;
    public static int extendZero = -10;
    public static int extendFullSample = 500;
    public static int extendFullSpecimen = 500;



}