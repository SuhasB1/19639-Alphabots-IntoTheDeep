package alphabots.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import alphabots.config.subsystem.IntakeSubsystem;
import alphabots.config.util.VariableStorage;
@Disabled
@Config
@TeleOp(name="AutoOuttakeTest", group="a")
public class AutoOuttakeTest extends OpMode {

    public static double power = 1;
    boolean detected;

    private IntakeSubsystem intakeSubsystem;
    DigitalChannel pin0, pin1;

    @Override
    public void init() {
        detected = false;
        pin0 = hardwareMap.digitalChannel.get("digital0");
        pin1 = hardwareMap.digitalChannel.get("digital1");
        intakeSubsystem = new IntakeSubsystem(hardwareMap, null, IntakeSubsystem.IntakeState.IDLE, IntakeSubsystem.PivotState.TRANSFER);
        intakeSubsystem.ground();
    }

    @Override
    public void loop() {
        autoOuttake();
        intakeSubsystem.intake.setPower(power);
        telemetry.addData("digital 0", pin0.getState());
        telemetry.addData("digital 1", pin1.getState());
        telemetry.addData("isDetected", detected);
    }

    public void autoOuttake(){
        if(VariableStorage.currentTeam == VariableStorage.Team.RED){
            if(pin0.getState() && !pin1.getState()){
                intakeSubsystem.outtake();
                detected = true;
            }
        }
        if(VariableStorage.currentTeam == VariableStorage.Team.BLUE){
            if(!pin0.getState() && pin1.getState()){
                intakeSubsystem.outtake();
                detected = true;
            }
        }
    }
}
