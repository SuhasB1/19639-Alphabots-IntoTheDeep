package alphabots.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import alphabots.config.subsystem.IntakeSubsystem;

@Config
@TeleOp(name="intakeTest", group="a")
public class intakeTest extends OpMode {

    private IntakeSubsystem intake;
    private IntakeSubsystem.IntakeState intakeBoxState;
    private IntakeSubsystem.PivotState intakePivotState;


    @Override
    public void init() {
        intake = new IntakeSubsystem(hardwareMap, telemetry, intakeBoxState, intakePivotState);
        intake.init();
    }

    @Override
    public void start() {
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


        intake.telemetry();
        telemetry.update();
    }
}
