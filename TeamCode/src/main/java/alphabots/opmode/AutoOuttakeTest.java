package alphabots.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import alphabots.config.subsystem.IntakeSubsystem;
import alphabots.config.util.VariableStorage;
@Config
@TeleOp(name="AutoOuttakeTest", group="a")
public class AutoOuttakeTest extends OpMode {

    public static double power = 1;
    private IntakeSubsystem intake;
    private IntakeSubsystem.IntakeState intakeBoxState;

    private IntakeSubsystem.PivotState intakePivotState;


    @Override
    public void init() {
        intake = new IntakeSubsystem(hardwareMap, telemetry, intakeBoxState, intakePivotState);
        intake.intake();
    }

    @Override
    public void loop() {
        intake.autoOuttake();
        intake.prevent2sample();
        intake.telemetry();
        telemetry.update();
    }
}
