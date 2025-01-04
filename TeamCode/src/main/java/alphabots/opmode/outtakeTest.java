package alphabots.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import alphabots.config.subsystem.IntakeSubsystem;
import alphabots.config.subsystem.OuttakeSubsystem;

@Config
@TeleOp(name="outtakeTest", group="a")
public class outtakeTest extends OpMode {

    private OuttakeSubsystem outtake;
    private OuttakeSubsystem.GrabState outtakeGrabState;
    private OuttakeSubsystem.PivotState outtakePivotState;
    private OuttakeSubsystem.RotateState outtakeRotateState;
    private OuttakeSubsystem.SpinState outtakeSpinState;

    private Follower follower;

    private IntakeSubsystem intake;
    private IntakeSubsystem.IntakeState intakeBoxState;
    private IntakeSubsystem.PivotState intakePivotState;

    @Override
    public void init() {
        outtake = new OuttakeSubsystem(hardwareMap, telemetry, outtakeGrabState, outtakeRotateState, outtakePivotState, outtakeSpinState);
        follower = new Follower(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap, telemetry, intakeBoxState, intakePivotState);
        follower = new Follower(hardwareMap);
        intake.init();

    }

    @Override
    public void start() {
        follower.startTeleopDrive();
        intake.start();
    }

    @Override
    public void loop() {

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
        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();


        outtake.telemetry();
        telemetry.update();
    }
}
