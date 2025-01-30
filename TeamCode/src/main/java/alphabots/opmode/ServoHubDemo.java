package alphabots.opmode;

import static alphabots.config.util.RobotConstants.*;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name="servo hubt", group="a")
public class ServoHubDemo extends OpMode {
    Servo rightPivot;
    Servo leftPivot;

    public static double servoIncrement = 0.001;
    public static double minPosition = 0.0;
    public static double maxPosition = 1.0;

    // Make servoPosition static and configurable through FTC Dashboard
    public static double servoPosition = 1;

    @Override
    public void init() {
        rightPivot = hardwareMap.get(Servo.class, "iRP");
        leftPivot = hardwareMap.get(Servo.class, "iLP");
        leftPivot.setDirection(Servo.Direction.REVERSE);

        // Set initial position
        rightPivot.setPosition(servoPosition);
        leftPivot.setPosition(servoPosition);
    }

    @Override
    public void loop() {
        // Clamp servo position to valid range
        servoPosition = Math.max(minPosition, Math.min(maxPosition, servoPosition));

        // Apply updated servo position
        rightPivot.setPosition(servoPosition);
        leftPivot.setPosition(servoPosition);

        telemetry.addData("Servo Position", servoPosition);
        telemetry.update();
    }
}
