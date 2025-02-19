package alphabots.config.subsystem;

import static alphabots.config.util.RobotConstants.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import alphabots.config.util.RobotConstants;
import alphabots.config.util.action.RunAction;

public class LiftSubsystem {
    private Telemetry telemetry;
    public DcMotorEx rightLift, leftLift;
    public boolean hang = false;
    public int pos, bottom;
    public RunAction toZero, toHighBucket, toHighChamber, toHumanPlayer, toTransfer, toPark;
    public PIDController liftPID;
    public static int target;
    public static double p = 0.012, i = 0, d = 0.0001;
    public static double f = 0.15;


    public LiftSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        rightLift = hardwareMap.get(DcMotorEx.class, "bottomLift");
        leftLift = hardwareMap.get(DcMotorEx.class, "topLift");
        //day b4 comp add variable storage
        rightLift.setDirection(DcMotorEx.Direction.FORWARD);
        leftLift.setDirection(DcMotorEx.Direction.REVERSE);
        leftLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        leftLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);


        liftPID = new PIDController(p, i, d);
    }

    public void updatePIDF(){
                liftPID.setPID(p, i, d);

                rightLift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
                leftLift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

                double pid = liftPID.calculate(getPos(), target);
                double ticks_in_degrees = 537.7 / 360.0;
                double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;
                double power = pid + ff;
                power = Math.max(power,-0.8);
                leftLift.setPower(power);
                rightLift.setPower(power);
    }



    //Util
    public void targetCurrent() {
        setTarget(getPos());
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(int b) {
        target = b;
    }

    public void addToTarget(int b) {
        target += b;
    }

    public int getPos() {
        pos = rightLift.getCurrentPosition();
        return rightLift.getCurrentPosition();
    }

    // OpMode
    public void init() {
        liftPID.setPID(p,i,d);
    }

    public void start() {
        target = 0;
    }

    //Presets

    public void toZero() {
        setTarget(liftToZero);
    }

    public void toHighBucket() {
        setTarget(RobotConstants.liftToHighBucket);
    }

    public void toHighChamber() {
        setTarget(liftToHighChamber);
    }

    public void toHumanPlayer() {
        setTarget(liftToHumanPlayer);
    }

    public void toPark() {
        setTarget(liftToPark);
    }

    public void telemetry() {
        telemetry.addData("Lift Pos: ", getPos());
        telemetry.addData("Lift Target: ", target);
        telemetry.addData("current draw",leftLift.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("current draw",rightLift.getCurrent(CurrentUnit.AMPS));
    }

}