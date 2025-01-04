package alphabots.config.runmodes;

import alphabots.config.subsystem.OuttakeSubsystem;
import alphabots.config.subsystem.ExtendSubsystem;
import alphabots.config.subsystem.IntakeSubsystem;
import alphabots.config.subsystem.LiftSubsystem;
import alphabots.config.util.RobotConstants;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.pedropathing.util.Timer;

public class Teleop {


    private ExtendSubsystem extend;
    private LiftSubsystem lift;

    private IntakeSubsystem intake;
    private IntakeSubsystem.IntakeState intakeBoxState;
    private IntakeSubsystem.PivotState intakePivotState;
    private OuttakeSubsystem outtake;
    private OuttakeSubsystem.GrabState outtakeGrabState;
    private OuttakeSubsystem.PivotState outtakePivotState;
    private OuttakeSubsystem.RotateState outtakeRotateState;
    private OuttakeSubsystem.SpinState outtakeSpinState;




    private Follower follower;
    private Pose startPose;

    private Telemetry telemetry;

    private Gamepad gamepad1, gamepad2;
    private Gamepad currentGamepad1 = new Gamepad();
    private Gamepad currentGamepad2 = new Gamepad();
    private Gamepad previousGamepad1 = new Gamepad();
    private Gamepad previousGamepad2 = new Gamepad();


    private int flip = 1;
    public double speed = 0.75;

    private boolean fieldCentric;


    public Teleop(HardwareMap hardwareMap, Telemetry telemetry, Follower follower, Pose startPose, boolean fieldCentric, Gamepad gamepad1, Gamepad gamepad2) {
        outtake = new OuttakeSubsystem(hardwareMap, telemetry, outtakeGrabState, outtakeRotateState, outtakePivotState, outtakeSpinState);
        lift = new LiftSubsystem(hardwareMap, telemetry);
        extend = new ExtendSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem(hardwareMap, telemetry, intakeBoxState, intakePivotState);

        this.follower = follower;
        this.startPose = startPose;

        this.startPose = new Pose(56,102.25,Math.toRadians(270));

        this.fieldCentric = fieldCentric;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {}

    public void start() {
        extend.setLimitToSample();
        outtake.init();
        extend.start();
        intake.start();
        //follower.setPose(startPose);
        //follower.startTeleopDrive();
    }

    public void update() {
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);
        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        if (gamepad1.right_bumper)
            speed = 1;
        else if (gamepad1.left_bumper)
            speed = 0.25;
        else
            speed = 0.75;

        if (gamepad1.right_trigger > 0.1)
            extend.toFull();

        if (gamepad1.left_trigger > 0.1)
            extend.toZero();

        if (gamepad1.right_bumper)
            extend.manual(30);
        else if (gamepad1.left_bumper)
            extend.manual(-30);
        else
            extend.manual(0);

        lift.manual(gamepad2.right_trigger - gamepad2.left_trigger);

        if (currentGamepad2.triangle && !previousGamepad2.triangle){
            outtake.switchGrabState(); //claw open or close
        }

        if (currentGamepad2.cross && !previousGamepad2.cross){
            outtake.transfer();
            lift.toZero();
        }


        if (currentGamepad2.square && !previousGamepad2.square)
            outtake.score();

        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left)
            specimenGrabPos();

        if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right)
            specimenScorePos();

        if (currentGamepad1.circle && !previousGamepad1.circle)
            intake.switchIntakeState();

        if (currentGamepad2.cross && !previousGamepad2.cross)
            intake.switchOuttakeState();

        //follower.setTeleOpMovementVectors(flip * -gamepad1.left_stick_y * speed, flip * -gamepad1.left_stick_x * speed, -gamepad1.right_stick_x * speed * 0.5, !fieldCentric);


        lift.updatePIDF();

        follower.update();
        telemetry.addData("X: ", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading: ", follower.getPose().getHeading());
        extend.telemetry();
        lift.telemetry();
        outtake.telemetry();
        intake.telemetry();
        telemetry.update();
    }

    private void scoringPos() {
        extend.setLimitToSample();
        intake.transfer();
        outtake.score();
    }

    private void transferPos() {
        extend.setLimitToSample();
        intake.transfer();
        outtake.transfer();
    }

    private void specimenGrabPos() {
        extend.setLimitToSpecimen();
        outtake.specimenGrab();
    }

    private void specimenScorePos() {
        extend.setLimitToSpecimen();
        outtake.specimenScore();
    }

}