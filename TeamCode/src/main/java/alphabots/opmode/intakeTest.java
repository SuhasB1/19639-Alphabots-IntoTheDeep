package alphabots.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import alphabots.config.subsystem.IntakeSubsystem;
import com.pedropathing.follower.Follower;

@Config
@TeleOp(name="intakeTest", group="a")
public class intakeTest extends OpMode {

    private IntakeSubsystem intake;
    private IntakeSubsystem.IntakeState intakeBoxState;
    private IntakeSubsystem.PivotState intakePivotState;

    private Follower follower;

    @Override
    public void init() {
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
            intake.transfer();
        }

        if(gamepad1.y) {
            intake.ground();
        }
        if(gamepad1.a){
            intake.hover();
        }
        if(gamepad1.dpad_left){
            intake.swipeOpen();
        }
        if(gamepad1.dpad_right){
            intake.swipeClose();
        }
        if(gamepad1.right_bumper) {
            intake.idle();
        } else if (gamepad1.left_bumper) {
            intake.intake();
        }

        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();


        intake.telemetry();
        telemetry.update();
    }
}
