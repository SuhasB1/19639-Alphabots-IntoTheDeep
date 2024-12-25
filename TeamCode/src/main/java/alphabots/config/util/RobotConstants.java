package alphabots.config.util;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants {

    // Outtake
    public static double outtakeGrabClose = 0;
    public static double outtakeGrabOpen = 0;
    public static double outtakeRotateTransfer = 0;
    public static double outtakeRotateSpecimenGrab = 0;
    public static double outtakeRotateScore = 0;
    public static double outtakeRotateSpecimenScore = 0;
    public static double outtakePivotTransfer= 0; //.05
    public static double outtakePivotScore = 0;
    public static double outtakePivotSpecimenGrab = 0;
    public static double outtakePivotSpecimenScore = 0;

    public static double outtakeSpinForward = 0;

    public static double outtakeSpinBackward = 0;

    // Intake
    public static double intakeBoxIntake = 0;
    public static double intakeBoxIdle = 0;
    public static double intakeBoxOuttake = 0;
    public static double intakePivotTransfer= 0;
    public static double intakePivotGround = 0;
    public static double intakePivotHover = 0;

    // Lift Positions
    public static int liftToZero = 0;
    public static int liftToHumanPlayer = 0;
    public static int liftToHighChamber = 0;
    public static int liftToHighBucket = 0;
    public static int liftToLowBucket = 0;
    public static int liftToPark = 0;

    // Extend Positions
    public static int extendManualIncrements = 0;
    public static int extendZero = 0;
    public static int extendFullSample = 0;
    public static int extendFullSpecimen = 0;



}