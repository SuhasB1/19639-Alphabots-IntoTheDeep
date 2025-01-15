package alphabots.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import alphabots.config.subsystem.IntakeSubsystem;
import alphabots.config.subsystem.LiftSubsystem;
import alphabots.config.subsystem.OuttakeSubsystem;
import alphabots.config.util.RobotConstants;
@Disabled
@Config
@TeleOp(name="outtakeTest", group="a")
public class outtakeTest extends OpMode {

    private OuttakeSubsystem outtake;
    private OuttakeSubsystem.GrabState outtakeGrabState;
    private OuttakeSubsystem.PivotState outtakePivotState;
    private OuttakeSubsystem.RotateState outtakeRotateState;
    private OuttakeSubsystem.SpinState outtakeSpinState;

    private IntakeSubsystem intake;
    private IntakeSubsystem.IntakeState intakeBoxState;
    private IntakeSubsystem.PivotState intakePivotState;

    private LiftSubsystem lift;
    public DcMotor rightLift, leftLift;
    public static int posLeft, posRight;
    public PIDController liftPID;

    public static double p = 0.013, i = 0, d = 0.0002;
    public static double f = 0.01;

    @Override
    public void init() {
        outtake = new OuttakeSubsystem(hardwareMap, telemetry, outtakeGrabState, outtakeRotateState, outtakePivotState, outtakeSpinState);
        intake = new IntakeSubsystem(hardwareMap, telemetry, intakeBoxState, intakePivotState);
        posLeft = 0;
        posRight = 0;
        rightLift = hardwareMap.get(DcMotor.class, "bottomLift");
        leftLift = hardwareMap.get(DcMotor.class, "topLift");

        rightLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftPID = new PIDController(p, i, d);
        telemetry.setMsTransmissionInterval(50);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    }

    @Override
    public void start() {
        intake.start();
    }

    @Override
    public void loop() {
        updatePIDF();

        if(gamepad1.x) {
            outtake.setPivotState(OuttakeSubsystem.PivotState.TRANSFER);
        }
        if(gamepad1.y) {
            outtake.setRotateState(OuttakeSubsystem.RotateState.TRANSFER);
        }
        if(gamepad1.a){
            outtake.setSpinState(OuttakeSubsystem.SpinState.FORWARD);
        }
        if(gamepad1.left_stick_button){
            outtake.close();
        }
        if(gamepad1.right_stick_button){
            outtake.open();
        }
        if(gamepad1.dpad_left) {
            outtake.setPivotState(OuttakeSubsystem.PivotState.SPECIMENGRAB);
        }
        if(gamepad1.dpad_up) {
            outtake.setRotateState(OuttakeSubsystem.RotateState.SPECIMENGRAB);
        }

        if(gamepad2.x) {
            intake.transfer();
        }

        if(gamepad2.y) {
            intake.ground();
        }
        if(gamepad2.a){
            intake.hover();
        }
        if(gamepad2.dpad_left){
            intake.swipeOpen();
        }
        if(gamepad2.dpad_right){
            intake.swipeClose();
        }
        if(gamepad2.right_bumper) {
            intake.idle();
        } else if (gamepad2.left_bumper) {
            intake.intake();
        }

        telemetry.addData("left lift pos", getLeftPos());
        telemetry.addData("right lift pos", getRightPos());
        telemetry.addData("target", RobotConstants.liftToZero);
        telemetry.addData("P:", p);
        telemetry.addData("I:", i);
        telemetry.addData("D:", d);
        telemetry.addData("F:", f);
        telemetry.update();


        outtake.telemetry();
        telemetry.update();
    }

    public void updatePIDF(){
            liftPID.setPID(p,i,d);
            rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            double pid = liftPID.calculate(getLeftPos(), RobotConstants.liftToZero);
            double ticks_in_degrees = 537.7 / 360.0;
            double ff = Math.cos(Math.toRadians(RobotConstants.liftToZero / ticks_in_degrees)) * f;
            double power = pid + ff;

            rightLift.setPower(-power);
            leftLift.setPower(power);

            telemetry.addData("left lift pos", getLeftPos());
            telemetry.addData("right lift pos", getRightPos());
            telemetry.addData("lift target", RobotConstants.liftToZero);
    }

    //Util

    public int getRightPos() {
        posRight = rightLift.getCurrentPosition();
        return rightLift.getCurrentPosition();
    }
    public int getLeftPos() {
        posLeft = leftLift.getCurrentPosition();
        return leftLift.getCurrentPosition();
    }
}
