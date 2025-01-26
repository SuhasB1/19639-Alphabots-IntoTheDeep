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
    boolean detected;
    CRServo servo;

    DigitalChannel pin0, pin1;

    @Override
    public void init() {
        detected = false;
        pin0 = hardwareMap.digitalChannel.get("digital0");
        pin1 = hardwareMap.digitalChannel.get("digital1");
        servo = hardwareMap.get(CRServo.class, "servo1");
        servo.setDirection(CRServo.Direction.REVERSE);

    }

    @Override
    public void loop() {
        autoOuttake();
        telemetry.addData("digital 0", pin0.getState());
        telemetry.addData("digital 1", pin1.getState());
        telemetry.addData("isDetected", detected);
    }

    public void autoOuttake(){
        if(VariableStorage.currentTeam == VariableStorage.Team.RED){
            if(pin0.getState() && !pin1.getState()){
                servo.setPower(-power);
                detected = true;
            }else {
                servo.setPower(power);
            }
        }
        if(VariableStorage.currentTeam == VariableStorage.Team.BLUE){
            if(!pin0.getState() && pin1.getState()){
                servo.setPower(-power);
                detected = true;
            }else{
                servo.setPower(power);
            }
        }
    }
}
