package alphabots.config.util;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConstants {

    // Outtake
    public static double outtakeGrabClose = 0.98; ///
    public static double outtakeGrabOpen = 0.82; ///
    public static double outtakeRotateTransfer = 0.74; ///
    public static double outtakeRotateSpecimenGrab = 0.52    ; ///
    public static double outtakeRotateScore = 0.2; ///
    public static double outtakeRotateSpecimenScore = 0.61;
    public static double outtakeRotateIdle = 0.76;//
    public static double outtakePivotTransfer= 0.2;
    public static double outtakePivotScore = 0.47;
    public static double outtakePivotSpecimenGrab = 0.75;
    public static double outtakePivotSpecimenScore =  0.21;//
    public static double outtakePivotIdle = 0.23;//
    public static double outtakeSpinForward = 0.1; ///

    public static double outtakeSpinBackward = 0.76; ///

    // Intake
    public static double intakeBoxIntake = 1; //
    public static double intakeBoxIdle = 0; //
    public static double intakeBoxOuttake = -1; //
    public static double intakePivotTransfer= 0.4; //
    public static double intakePivotGround = 0.81; //
    public static double intakePivotHover = 0.8; //
    public static double intakeWiperOpen = 0.3; //
    public static double intakeWiperClose = 0.985; //

    // Lift Positions
    public static int liftToZero = -10;
    public static int liftToHumanPlayer = 0;
    public static int liftToHighChamber = 950;
    public static int liftToHighBucket = 1230;
    public static int liftToLowBucket = 0;
    public static int liftToPark = 415;

    // Extend Positions
    public static int extendManualIncrements = 70;
    public static int extendZero = -8;
    public static int extendFullSample = 420;
    public static int extendFullSpecimen = 420;

    public static boolean software_are_bums = true;

    public static boolean ronit_wants_to_die = true;

}