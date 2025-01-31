package alphabots.opmode;

import static alphabots.config.util.RobotConstants.*;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@Config
@TeleOp(name="IntakeTweaker", group="a")
public class ServoHubDemo extends OpMode {
    Servo rightPivot;
    Servo leftPivot;
    Servo outtakeRightPivot;
    Servo outtakeLeftPivot;
    Servo outtakeRotate;
    Servo outtakeSpin;
    Servo outtakeGrip;


    public static double iP = 1;
    public static double oP = 1;
    public static double oR = 1;
    public static double oS = 1;
    public static double oG = 1;

    @Override
    public void init() {
        rightPivot = hardwareMap.get(Servo.class, "iRP");
        leftPivot = hardwareMap.get(Servo.class, "iLP");
        outtakeRightPivot = hardwareMap.get(Servo.class, "oRP");
        outtakeLeftPivot = hardwareMap.get(Servo.class, "oLP");
        outtakeRotate = hardwareMap.get(Servo.class, "oR");
        outtakeSpin = hardwareMap.get(Servo.class, "oS");
        outtakeGrip = hardwareMap.get(Servo.class, "oG");
        outtakeLeftPivot.setDirection(Servo.Direction.REVERSE);
        leftPivot.setDirection(Servo.Direction.REVERSE);

        // Set initial position
        rightPivot.setPosition(iP);
        leftPivot.setPosition(iP);
        outtakeRightPivot.setPosition(oP);
        outtakeLeftPivot.setPosition(oP);
        outtakeRotate.setPosition(oR);
        outtakeGrip.setPosition(oG);
        outtakeSpin.setPosition(oS);
    }

    @Override
    public void loop() {
        rightPivot.setPosition(iP);
        leftPivot.setPosition(iP);
        outtakeRightPivot.setPosition(oP);
        outtakeLeftPivot.setPosition(oP);
        outtakeRotate.setPosition(oR);
        outtakeGrip.setPosition(oG);
        outtakeSpin.setPosition(oS);
    }
}
