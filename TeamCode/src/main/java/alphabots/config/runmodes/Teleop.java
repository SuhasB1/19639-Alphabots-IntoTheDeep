package alphabots.config.runmodes;

import static java.lang.Math.abs;

import alphabots.config.subsystem.OuttakeSubsystem;
import alphabots.config.subsystem.ExtendSubsystem;
import alphabots.config.subsystem.IntakeSubsystem;
import alphabots.config.subsystem.LiftSubsystem;
import alphabots.config.util.RobotConstants;
import alphabots.config.util.VariableStorage;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;

import com.qualcomm.robotcore.hardware.DcMotor;
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

    private Timer transferTimer = new Timer(), wiperTimer = new Timer(), sampleTimer = new Timer();

    private int transferState = -1, wiperState = -1, sampleState = -1;



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

        this.startPose = VariableStorage.currentPose;

        this.fieldCentric = fieldCentric;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {
        extend.setLimitToSample();
        outtake.init();
        extend.start();
        intake.start();
        lift.start();
        //follower.setPose(startPose);
    }

    public void start() {
        //follower.startTeleopDrive();
    }

    public void update() {
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);
        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        //driver
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
            extend.addToTarget(15);
        else if (gamepad1.left_bumper)
            extend.addToTarget(-15);
        else
            extend.addToTarget(0);

        if (currentGamepad1.dpad_left && !previousGamepad1.dpad_left)
            intake.switchIntakeState();

        if (currentGamepad1.dpad_right && !previousGamepad1.dpad_right)
            intake.switchOuttakeState();

        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down)
            intake.switchOuttakeState();

        if (currentGamepad1.square && !previousGamepad1.square)
            intake.ground();

        if (currentGamepad1.triangle && !previousGamepad1.triangle)
            intake.hover();

        if (currentGamepad1.circle && !previousGamepad1.circle)
            intake.transfer();

        if (currentGamepad1.share && !previousGamepad1.share)
            extend.extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        //operator
        if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper)
            outtake.switchGrabState(); //claw open or close

        if (currentGamepad2.cross && !previousGamepad2.cross)
            lift.toZero();

        if (currentGamepad2.square && !previousGamepad2.square)
            startTransfer();

        if (currentGamepad2.triangle && !previousGamepad2.triangle)
            startSample();

        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left)
            specimenGrabPos();

        if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right)
            specimenScorePos();

        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up)
            lift.toHighChamber();

        if (currentGamepad2.share && !previousGamepad2.share){
            lift.leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        //follower.setTeleOpMovementVectors(flip * -gamepad1.left_stick_y * speed, flip * -gamepad1.left_stick_x * speed, -gamepad1.right_stick_x * speed * 0.5, !fieldCentric);


        lift.updatePIDF();
        extend.updatePIDF();
        intake.autoOuttake();
        transfer();
        sample();
        wiper();

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
                if (transferTimer.getElapsedTimeSeconds() > 1 && extend.getPos() < 10 && lift.getPos() < 10) {
                    outtake.transfer();
                    telemetry.addLine("started 2");
                    setTransferState(2);
                }
                break;
            case 2:
                if (transferTimer.getElapsedTimeSeconds() > 1) {
                    outtake.close();
                    telemetry.addLine("started 3");
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
                telemetry.addLine("started 1");
                setSampleState(1);
                break;
            case 1:
                if (lift.getPos() > RobotConstants.liftToHighBucket - 70) {
                    outtake.score();
                    telemetry.addLine("started 2");
                    setSampleState(2);
                }
                break;
            case 2:
                if (lift.getPos() > RobotConstants.liftToHighBucket - 20 && sampleTimer.getElapsedTimeSeconds() > 0.5) {
                    outtake.open();
                    telemetry.addLine("started 3");
                    setSampleState(3);
                }
                break;
            case 3:
                if (sampleTimer.getElapsedTimeSeconds() > 0.5) {
                    outtake.idle();
                    telemetry.addLine("started 4");
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

    private void wiper() {
        switch (wiperState) {
            case 0:
                intake.swipeOpen();
                setwiperState(1);
                break;
            case 1:
                if(wiperTimer.getElapsedTimeSeconds() > 0.2) {
                    intake.swipeClose();
                    setwiperState(2);
                }
                break;
        }
    }

    public void setwiperState(int x) {
        wiperState = x;
        wiperTimer.resetTimer();
    }

    public void startwiper() {
        setwiperState(0);
    }

}