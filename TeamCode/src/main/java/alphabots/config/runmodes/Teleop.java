package alphabots.config.runmodes;

import static java.lang.Math.abs;

import alphabots.config.subsystem.OuttakeSubsystem;
import alphabots.config.subsystem.ExtendSubsystem;
import alphabots.config.subsystem.IntakeSubsystem;
import alphabots.config.subsystem.LiftSubsystem;
import alphabots.config.util.RobotConstants;
import alphabots.config.util.VariableStorage;
import alphabots.pedroPathing.constants.FConstants;
import alphabots.pedroPathing.constants.LConstants;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;

import com.pedropathing.localization.PoseUpdater;
import com.pedropathing.util.Constants;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.pedropathing.util.Timer;

import java.util.List;

public class Teleop {
    private PoseUpdater poseUpdater;
    DcMotor rightFront;
    DcMotor leftFront;
    DcMotor leftRear;
    DcMotor rightRear;

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

    private Timer transferTimer = new Timer(), wiperTimer = new Timer(), sampleTimer = new Timer(), resetTimer = new Timer(), intakeTimer = new Timer(), specimenGrabTimer = new Timer();

    private int transferState = -1, wiperState = -1, sampleState = -1, resetState = -1, intakeState = -1, specimenGrabState = -1;


    private Telemetry telemetry;

    private Gamepad gamepad1, gamepad2;
    private Gamepad currentGamepad1 = new Gamepad();
    private Gamepad currentGamepad2 = new Gamepad();
    private Gamepad previousGamepad1 = new Gamepad();
    private Gamepad previousGamepad2 = new Gamepad();


    public double speed = 0.75;

    private boolean fieldCentric;


    public Teleop(HardwareMap hardwareMap, Telemetry telemetry, boolean fieldCentric, Gamepad gamepad1, Gamepad gamepad2) {
        outtake = new OuttakeSubsystem(hardwareMap, telemetry, outtakeGrabState, outtakeRotateState, outtakePivotState, outtakeSpinState);
        lift = new LiftSubsystem(hardwareMap, telemetry);
        extend = new ExtendSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem(hardwareMap, telemetry, intakeBoxState, intakePivotState);

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE); //leftFront
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);


        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.fieldCentric = fieldCentric;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void init() {
        outtake.init();
        extend.setLimitToSample();
        intake.start();

        Constants.setConstants(FConstants.class,LConstants.class);

    }

    public void start(){
        lift.leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lift.leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        extend.extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extend.extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extend.toZero();
    }

    public void update() {
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);
        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        //driver

        if (gamepad1.right_trigger > 0.1)
            speed = 0.25;
        else{
            speed = 0.7;
        }

        if (gamepad1.left_trigger > 0.1) {
            extend.toZero();
            intake.hover();
        }

        if (gamepad1.right_bumper && extend.getTarget() < 1200)
            extend.addToTarget(40);
        else if (gamepad1.left_bumper)
            extend.addToTarget(-20);
        else
            extend.addToTarget(0);

        if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down)
            startwiper();

        if (currentGamepad1.square && !previousGamepad1.square) {
            intake.ground();
        }

        if (currentGamepad1.triangle && !previousGamepad1.triangle)
            intake.hover();

        if (currentGamepad1.circle && !previousGamepad1.circle)
            intake.transfer();
        if (currentGamepad1.share && !previousGamepad1.share){
            extend.extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            extend.extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            extend.toZero();
        }


        //operator
        if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper)
            outtake.switchGrabState(); //claw open or close

        if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper)
            intake.switchIntakeState();

        if (currentGamepad2.share && !previousGamepad2.share)
            intake.switchOuttakeState();

        if (currentGamepad2.options && !previousGamepad2.options)
            outtake.idle();

        if (currentGamepad2.cross && !previousGamepad2.cross) {
            startReset();
        }

        if (currentGamepad2.square && !previousGamepad2.square)
            startTransfer();

        if (currentGamepad2.circle && !previousGamepad2.circle)
            intake.transfer();

        if (currentGamepad2.triangle && !previousGamepad2.triangle){
            startSample();
        }

        if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left)
            specimenGrabPos();

        if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up)
            specimenScorePos();

        if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right){
            lift.toHighChamber();
        }
        if (gamepad2.right_trigger > 0.1) {
            extend.toQuarter();
        }
        if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down)
            startwiper();

        lift.updatePIDF();
        extend.updatePIDF();
        transfer();
        sample();
        wiper();
        reset();
        moveDriveTrain();

        //follower.update();
        addTelemetry();
    }

    public void addTelemetry(){
        extend.telemetry();
        lift.telemetry();
        outtake.telemetry();
        intake.telemetry();
    }

    private void specimenGrabPos() {
        outtake.specimenGrab();
    }

    private void specimenScorePos() {
        outtake.specimenScore();
    }

    public void setSpecimenGrabState(int x) {
        specimenGrabState = x;
        specimenGrabTimer.resetTimer();
    }

    public void startSpecimenGrab() {
        setSpecimenGrabState(0);
    }

    private void transfer() {
        switch (transferState) {
            case 0:
                outtake.setRotateState(OuttakeSubsystem.RotateState.IDLE);
                outtake.setPivotState(OuttakeSubsystem.PivotState.IDLE);
                outtake.open();
                telemetry.addLine("started 1");
                setTransferState(1);
                break;
            case 1:
                if (transferTimer.getElapsedTimeSeconds() > 0.35) {
                    outtake.setRotateState(OuttakeSubsystem.RotateState.TRANSFER);
                    outtake.setPivotState(OuttakeSubsystem.PivotState.TRANSFER);
                    telemetry.addLine("started 2");
                    setTransferState(2);
                }

                break;
            case 2:
                if (transferTimer.getElapsedTimeSeconds() > 0.2) {

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
                outtake.close();
                lift.toHighBucket();
                telemetry.addLine("started 1");
                setSampleState(1);
                break;
            case 1:
                if (lift.getPos() > RobotConstants.liftToHighBucket - 150) {
                    outtake.score();
                    outtake.close();
                    telemetry.addLine("started 2");
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
                if(wiperTimer.getElapsedTimeSeconds() > 0.25) {
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

    private void reset() {
        switch (resetState) {
            case 0:
                outtake.open();
                setResetState(1);
                break;
            case 1:
                if (resetTimer.getElapsedTimeSeconds() > 0.2) {
                    outtake.idle();
                    setResetState(2);
                }
                break;
            case 2:
                if (resetTimer.getElapsedTimeSeconds() > 0.2) {
                    lift.toZero();
                    setResetState(-1);
                }
                break;
        }
    }

    public void setResetState(int x) {
        resetState = x;
        resetTimer.resetTimer();
    }

    public void startReset() {
        setResetState(0);
    }

    public void moveDriveTrain()
    {

        double y = gamepad1.left_stick_y; // Remember, this is reversed!
        double x = gamepad1.left_stick_x; // this is strafing
        double rx = gamepad1.right_stick_x;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFrontPower = (y + x + rx) / denominator;
        double leftRearPower = (y - x + rx) / denominator;
        double rightFrontPower = (y - x - rx) / denominator;
        double rightRearPower = (y + x - rx) / denominator;

        leftFront.setPower(leftFrontPower * speed);
        leftRear.setPower(leftRearPower * speed);
        rightFront.setPower(rightFrontPower * speed);
        rightRear.setPower(rightRearPower * speed);

    }
}