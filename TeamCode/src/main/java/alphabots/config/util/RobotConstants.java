package alphabots.config.util;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants {

    // Outtake
    public static double outtakeGrabClose = 0.5; ///
    public static double outtakeGrabOpen = 0.7; ///
    public static double outtakeRotateTransfer = 0.1; ///
    public static double outtakeRotateSpecimenGrab = 0.33; ///
    public static double outtakeRotateScore = 0.47;
    public static double outtakeRotateSpecimenScore = 0.17;

    public static double outtakeRotateIdle = 0.2; ///
    public static double outtakePivotTransfer= 1; ///
    public static double outtakePivotScore = 0.68;
    public static double outtakePivotSpecimenGrab = 0.05; ///
    public static double outtakePivotSpecimenScore = 0.53;
    public static double outtakePivotIdle = 0.8; ///

    public static double outtakeSpinForward = 0.73; ///

    public static double outtakeSpinBackward = 0.62; ///

    // Intake
    public static double intakeBoxIntake = 0.7; //
    public static double intakeBoxIdle = 0; //
    public static double intakeBoxOuttake = -1; //
    public static double intakePivotTransfer= 0.32; //
    public static double intakePivotGround = 0.95; //
    public static double intakePivotHover = 0.8; //
    public static double intakeWiperOpen = 0.3; //
    public static double intakeWiperClose = 1; //

    // Lift Positions
    public static int liftToZero = -10;
    public static int liftToHumanPlayer = 0;
    public static int liftToHighChamber = 680;
    public static int liftToHighBucket = 2475;
    public static int liftToLowBucket = 0;
    public static int liftToPark = 415;

    // Extend Positions
    public static int extendManualIncrements = 20;
    public static int extendZero = -10;
    public static int extendFullSample = 500;
    public static int extendFullSpecimen = 500;



}