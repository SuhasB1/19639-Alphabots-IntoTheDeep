package alphabots.opmode;

import static alphabots.config.util.RobotConstants.*;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name="outtake servo shi", group="a")
public class OuttakeServoSetting extends OpMode {
    Servo outtakeRightPivot;
    Servo outtakeLeftPivot;
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
    public static double outtakeGripPos = 0.5;
    @Override
    public void init() {
        outtakeRightPivot = hardwareMap.get(Servo.class, "oRP");
        outtakeLeftPivot = hardwareMap.get(Servo.class, "oLP");
        outtakeRotate = hardwareMap.get(Servo.class, "oR");
        outtakeSpin = hardwareMap.get(Servo.class, "oS");
        outtakeGrip = hardwareMap.get(Servo.class, "oG");
        outtakeLeftPivot.setDirection(Servo.Direction.REVERSE);

        // Set initial position
        outtakeRightPivot.setPosition(servoPosition);
        outtakeLeftPivot.setPosition(servoPosition);
        outtakeRotate.setPosition(outtakeRotatePos);
        outtakeGrip.setPosition(outtakeGripPos);
        outtakeSpin.setPosition(outtakeSpinPos);
    }

    @Override
    public void loop() {
        servoPosition = Math.max(minPosition, Math.min(maxPosition, servoPosition));
        outtakeRotatePos = Math.max(minPosition, Math.min(maxPosition, outtakeRotatePos));
        outtakeSpinPos = Math.max(minPosition, Math.min(maxPosition, outtakeSpinPos));
        outtakeGripPos = Math.max(minPosition, Math.min(maxPosition, outtakeGripPos));

        outtakeRightPivot.setPosition(servoPosition);
        outtakeLeftPivot.setPosition(servoPosition);
        outtakeRotate.setPosition(outtakeRotatePos);
        outtakeSpin.setPosition(outtakeSpinPos);
        outtakeGrip.setPosition(outtakeGripPos);

        telemetry.addData("Servo Position", servoPosition);
        telemetry.addData("Outtake Rotate Position", outtakeRotatePos);
        telemetry.addData("Outtake Spin Position", outtakeSpinPos);
        telemetry.addData("Outtake Grip Position", outtakeGripPos);
        telemetry.update();
    }
}

