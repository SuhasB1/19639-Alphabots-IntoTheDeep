package alphabots.config.subsystem;

import static alphabots.config.util.RobotConstants.*;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class ExtendSubsystem {
    private Telemetry telemetry;
    public DcMotorEx extend;
    private int currentPos = 0;
    public int extendLimit = extendFullSample;
    public PIDController extendPID;
    public static int target = 0;
    public static double p = 0.011, i = 0, d = 0;
    public ExtendSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        extend = hardwareMap.get(DcMotorEx.class, "extend");
        extend.setDirection(DcMotor.Direction.REVERSE);
        extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        extendPID = new PIDController(p, i, d);

    }

    public void updatePIDF(){
            extendPID.setPID(p,i,d);
            extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            double pid = extendPID.calculate(getPos(), target);
            double power = pid;
            power = Math.max(power, -0.9);
            extend.setPower(power);
    }

    public void targetCurrent() {
        setTarget(getPos());
    }


    public void toZero() {
        setTarget(extendZero);
    }

    public void toQuarter() {
        setTarget(100);
    }

    public void toHalf() {
        setTarget(200);
    }

    public void toFull() {
        setTarget(extendLimit);
    }

    // Util
    public int getPos() {
        currentPos = extend.getCurrentPosition();
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
    }

    public void start() {
        target = 0;
    }

    public void telemetry() {
        telemetry.addData("Extend Pos: ", getPos());
        telemetry.addData("extend targer:", getTarget());
        telemetry.addData("extend current", extend.getCurrent(CurrentUnit.AMPS));
    }
}