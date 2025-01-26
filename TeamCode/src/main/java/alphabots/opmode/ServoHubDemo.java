package alphabots.opmode;

import static alphabots.config.util.RobotConstants.*;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.pedropathing.util.Timer;

import alphabots.config.util.VariableStorage;
@Config
@TeleOp(name="servo hubt", group="a")
public class ServoHubDemo extends OpMode {
    CRServo servo;
    public static double servoPwr = 0;
@Override
public void init() {
    servo = hardwareMap.get(CRServo.class, "servo1");
}
public void loop(){

    servo.setPower(servoPwr);

}

}