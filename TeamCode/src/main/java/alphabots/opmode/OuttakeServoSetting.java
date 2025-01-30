package alphabots.opmode;

import static alphabots.config.util.RobotConstants.*;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name="outtake servo shi", group="a")
public class OuttakeServoSetting extends OpMode {
    Servo rightPivot;
    Servo leftPivot;
    Servo outtakeRotate;
    Servo outtakeSpin;
    Servo outtakeGrip;

    public static double servoIncrement = 0.001;
    public static double minPosition = 0.0;
    public static double maxPosition = 1.0;

    // Make servoPosition static and configurable through FTC Dashboard
    public static double servoPosition = 1;
    public static double outtakeRotatePos = 1;
    public static double outtakeSpinPos = 1;
    public static double outtakeGripPos = 1;
    @Override
    public void init() {
        rightPivot = hardwareMap.get(Servo.class, "oRP");
        leftPivot = hardwareMap.get(Servo.class, "oLP");
        outtakeRotate = hardwareMap.get(Servo.class, "oR");
        outtakeSpin = hardwareMap.get(Servo.class, "oS");
        outtakeGrip = hardwareMap.get(Servo.class, "oG");
        leftPivot.setDirection(Servo.Direction.REVERSE);

        // Set initial position
        rightPivot.setPosition(servoPosition);
        leftPivot.setPosition(servoPosition);
        outtakeRotate.setPosition(outtakeRotatePos);
        outtakeGrip.setPosition(outtakeGripPos);
        outtakeSpin.setPosition(outtakeSpinPos);
    }

    @Override
    public void loop() {
        // Clamp servo position to valid range
        servoPosition = Math.max(minPosition, Math.min(maxPosition, servoPosition));

        // Apply updated servo position
        rightPivot.setPosition(servoPosition);
        leftPivot.setPosition(servoPosition);
        outtakeRotate.setPosition(outtakeRotatePos);
        outtakeGrip.setPosition(outtakeGripPos);
        outtakeSpin.setPosition(outtakeSpinPos);

        telemetry.addData("Servo Position", servoPosition);
        telemetry.update();
    }
}
