package alphabots.config.runmodes;

import static java.lang.Math.abs;

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

    private Timer transferTimer = new Timer(), specimanTimer = new Timer(), sampleTimer = new Timer(), resetTimer = new Timer();

    private int transferState = -1, specimanState = -1, sampleState = -1, resetState = -1;




    private Follower follower;
    private Pose startPose;

    private Telemetry telemetry;

    private Gamepad gamepad1, gamepad2;
    private Gamepad currentGamepad1 = new Gamepad();
    private Gamepad currentGamepad2 = new Gamepad();
    private Gamepad previousGamepad1 = new Gamepad();
    private Gamepad previousGamepad2 = new Gamepad();


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

    public void init() {
        transferState = -1;
        specimanState = -1;
        sampleState = -1;
        resetState = -1;
    }

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

        if (currentGamepad1.circle && !previousGamepad1.circle)
            intake.switchIntakeState();

        if (currentGamepad1.cross && !previousGamepad1.cross)
            intake.switchOuttakeState();

        lift.manual(gamepad2.right_trigger - gamepad2.left_trigger);

        if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper){
            outtake.switchGrabState(); //claw open or close
        }

        if (currentGamepad2.cross && !previousGamepad2.cross){
            lift.toZero();
        }
        if (currentGamepad2.square && !previousGamepad2.square){
            startTransfer();
        }
        if (currentGamepad2.triangle && !previousGamepad2.triangle){
            startSample();
        }
        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left){
            specimenGrabPos();
        }
        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up){
            startspeciman();
        }



        if (currentGamepad2.square && !previousGamepad2.square)
            outtake.score();

        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left)
            specimenGrabPos();

        if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right)
            specimenScorePos();

        //follower.setTeleOpMovementVectors(flip * -gamepad1.left_stick_y * speed, flip * -gamepad1.left_stick_x * speed, -gamepad1.right_stick_x * speed * 0.5, !fieldCentric);


        lift.updatePIDF();
        transfer();
        sample();
        speciman();

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
        intake.transfer();
        outtake.score();
    }

    private void transferPos() {
        intake.transfer();
        outtake.transfer();
    }

    private void specimenGrabPos() {
        outtake.specimenGrab();
    }

    private void specimenScorePos() {
        outtake.specimenScore();
    }

    private void transfer() {
        switch (transferState) {
            case 0:
                lift.toZero();
                outtake.idle();
                outtake.open();
                intake.transfer();
                extend.toZero();
                telemetry.addLine("started 1");
                setTransferState(1);
                break;
            case 1:
                if (transferTimer.getElapsedTimeSeconds() > 1 && abs(extend.getPos()) < 30 && abs(lift.getPos()) < 30) {
                    outtake.transfer();
                    telemetry.addLine("started 2");
                    setTransferState(2);
                }
                break;
            case 2:
                if (transferTimer.getElapsedTimeSeconds() > 1) {
                    outtake.close();
                    telemetry.addLine("started 3");
                    setTransferState(3);
                }
                break;
            case 3:
                if (transferTimer.getElapsedTimeSeconds() > 0.25) {
                    outtake.close();
                    telemetry.addLine("started 4");
                    setTransferState(-1);
                }
                break;
        }
    }

    public void setTransferState(int x) {
        transferState = x;
        transferTimer.resetTimer();
    }

    public void startTransfer() {
        setTransferState(0);
    }

    private void sample() {
        switch (sampleState) {
            case 0:
                outtake.idle();
                lift.toHighBucket();
                setSampleState(1);
                break;
            case 1:
                if (abs(lift.getTarget() - lift.getPos()) < 70) {
                    outtake.score();
                    setSampleState(2);
                }
                break;
            case 2:
                if (abs(lift.getTarget() - lift.getPos()) < 20 && sampleTimer.getElapsedTimeSeconds() > 0.5) {
                    outtake.open();
                    setSampleState(3);
                }
                break;
            case 3:
                if (sampleTimer.getElapsedTimeSeconds() > 0.5) {
                    outtake.idle();
                    setSampleState(-1);
                }
                break;
        }
    }

    public void setSampleState(int x) {
        sampleState = x;
        sampleTimer.resetTimer();
    }

    public void startSample() {
        setSampleState(0);
    }

    private void speciman() {
        switch (specimanState) {
            case 0:
                setspecimanState(1);
                break;
            case 1:
                if(specimanTimer.getElapsedTimeSeconds() > 0.3) {
                    setspecimanState(2);
                }
                break;
            case 2:
                if (specimanTimer.getElapsedTimeSeconds() > 0.25) {
                    setspecimanState(-1);
                }
                break;
        }
    }

    public void setspecimanState(int x) {
        specimanState = x;
        specimanTimer.resetTimer();
    }

    public void startspeciman() {
        setspecimanState(0);
    }

}