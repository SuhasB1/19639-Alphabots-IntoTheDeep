package alphabots.opmode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.Range;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="LiftPIDControl", group="Testing")
public class liftPIDControl extends OpMode {

    private DcMotorEx bottomLift, topLift;

    // Tuned PIDF values
    private static double P = 0.01;
    private static double I = 0.0001;
    private static double D = 0.002;
    private static double F = 0.1;

    private int targetPosition = 500; // Start with a noticeable movement

    @Override
    public void init() {
        try {
            bottomLift = hardwareMap.get(DcMotorEx.class, "bottomLift");
            topLift = hardwareMap.get(DcMotorEx.class, "topLift");
            telemetry.addData("Motors", "Initialized Successfully ✅");
        } catch (Exception e) {
            telemetry.addData("Error", "Motor Initialization Failed ❌");
        }
        telemetry.update();

        // Reverse direction to fix negative position issue
        bottomLift.setDirection(DcMotor.Direction.REVERSE);
        topLift.setDirection(DcMotor.Direction.REVERSE);

        // Reset encoders
        bottomLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        bottomLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        topLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set behavior
        bottomLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Apply PIDF values
        bottomLift.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(P, I, D, F));
        topLift.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(P, I, D, F));

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Manual control using D-Pad
        if (gamepad1.a) {
            bottomLift.setPower(0.5);
            topLift.setPower(0.5);
        } else if (gamepad1.b) {
            bottomLift.setPower(-0.5);
            topLift.setPower(-0.5);
        } else {
            bottomLift.setPower(0);
            topLift.setPower(0);
        }

        if (gamepad1.dpad_up) {
            targetPosition += 100;
        } else if (gamepad1.dpad_down) {
            targetPosition -= 100;
        }

        targetPosition = Range.clip(targetPosition, 0, 3000);

        // Force set RUN_TO_POSITION mode every loop
        bottomLift.setTargetPosition(targetPosition);
        topLift.setTargetPosition(targetPosition);

        bottomLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        topLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        bottomLift.setPower(1.0);
        topLift.setPower(1.0);

        // Debug info
        telemetry.addData("Target Position", targetPosition);
        telemetry.addData("Bottom Lift Position", bottomLift.getCurrentPosition());
        telemetry.addData("Top Lift Position", topLift.getCurrentPosition());
        telemetry.update();
    }
}
