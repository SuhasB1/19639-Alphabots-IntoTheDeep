package alphabots.config.subsystem;

import static alphabots.config.util.RobotConstants.*;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ExtendSubsystem {
    private Telemetry telemetry;
    public boolean manual = false;
    public DcMotorEx extend;
    private int currentPos = 0;
    public int extendLimit = extendFullSample;
    public PIDController extendPID;
    public static int target;
    public static double p = 0.03, i = 0, d = 0;
    public ExtendSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        extend = hardwareMap.get(DcMotorEx.class, "extend");
        extend.setDirection(DcMotor.Direction.REVERSE);
        extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        extendPID = new PIDController(p, i, d);

        telemetry.addData("Constructor", "Extend motor initialized");
        telemetry.update();
    }

    public void updatePIDF(){
        if (!manual) {
            extendPID.setPID(p,i,d);

            extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            double pid = extendPID.calculate(getPos(), target);
            double power = pid;
            if (Math.abs(power) < 0.1) {
                power = 0.1 * Math.signum(power);
            }
            extend.setPower(power);

            telemetry.addData("PID Target", target);
            telemetry.addData("PID Current Position", getPos());
            telemetry.addData("PID Output", pid);
            telemetry.addData("Motor Power", power);
            telemetry.update();
        }
    }

    public void targetCurrent() {
        setTarget(getPos());
        manual = false;
    }

    public void manual(int n){
        manual = true;

        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        addToTarget(n);

        telemetry.addData("Manual Mode", "Activated");
        telemetry.addData("Manual Adjustment", n);
        telemetry.addData("New Target", target);
        telemetry.update();
    }

    public void toZero() {
        setTarget(extendZero);
        telemetry.addData("Move Command", "toZero");
        telemetry.addData("New Target", target);
        telemetry.update();
    }

    public void toQuarter() {
        setTarget(extendLimit/4);
        telemetry.addData("Move Command", "toQuarter");
        telemetry.addData("New Target", target);
        telemetry.update();
    }

    public void toHalf() {
        setTarget(extendLimit/2);
        telemetry.addData("Move Command", "toHalf");
        telemetry.addData("New Target", target);
        telemetry.update();
    }

    public void toFull() {
        setTarget(extendLimit);
        telemetry.addData("Move Command", "toFull");
        telemetry.addData("New Target", target);
        telemetry.update();
    }

    // Util
    public int getPos() {
        currentPos = extend.getCurrentPosition();
        telemetry.addData("Encoder Position", currentPos);
        telemetry.update();
        return currentPos;
    }

    public void setLimitToSpecimen() {
        extendLimit = extendFullSpecimen;
    }

    public void setLimitToSample() {
        extendLimit = extendFullSample;
    }

    // Init + Start //
    public double getTarget() {
        return target;
    }

    public void setTarget(int b) {
        target = b;
    }

    public void addToTarget(int b) {
        target += b;
    }

    // OpMode
    public void init() {
        extendPID.setPID(p,i,d);
        telemetry.addData("Initialization", "PID Controller Initialized");
        telemetry.update();
    }

    public void start() {
        target = 0;
        telemetry.addData("Start", "Target Reset to 0");
        telemetry.update();
    }

    public void telemetry() {
        telemetry.addData("Extend Position", getPos());
        telemetry.addData("Target Position", target);
        telemetry.addData("Manual Mode", manual);
        telemetry.addData("Motor Power", extend.getPower());
        telemetry.update();
    }
}