package alphabots.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "ServoHubDemo", group = "Examples")
public class ServoHubDemo extends LinearOpMode {

    private Servo servo1;
    private Servo servo2;

    public static double servo1Position = 0.5;
    public static double servo2Position = 0.5;
    public static String servo1DisableBehavior = "kDoNotSupplyPower";
    public static String servo2DisableBehavior = "kDoNotSupplyPower";

    private enum DisableBehavior {
        kSupplyPower,
        kDoNotSupplyPower
    }

    @Override
    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");

        servo1.setPosition(servo1Position);
        servo2.setPosition(servo2Position);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            servo1.setPosition(servo1Position);
            servo2.setPosition(servo2Position);

            DisableBehavior servo1Behavior = DisableBehavior.valueOf(servo1DisableBehavior);
            DisableBehavior servo2Behavior = DisableBehavior.valueOf(servo2DisableBehavior);

            if (servo1Behavior == DisableBehavior.kSupplyPower) {
                servo1.setPosition(servo1.getPosition());
            } else if (servo1Behavior == DisableBehavior.kDoNotSupplyPower) {
                servo1.setPosition(0.5);
            }

            if (servo2Behavior == DisableBehavior.kSupplyPower) {
                servo2.setPosition(servo2.getPosition());
            } else if (servo2Behavior == DisableBehavior.kDoNotSupplyPower) {
                servo2.setPosition(0.5);
            }

            telemetry.addData("Servo1 Position", servo1Position);
            telemetry.addData("Servo2 Position", servo2Position);
            telemetry.addData("Servo1 Disable Behavior", servo1DisableBehavior);
            telemetry.addData("Servo2 Disable Behavior", servo2DisableBehavior);
            telemetry.update();
        }
    }
}
