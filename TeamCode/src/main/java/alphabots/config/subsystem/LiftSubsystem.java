package alphabots.config.subsystem;

import static alphabots.config.util.RobotConstants.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import alphabots.config.util.RobotConstants;
import alphabots.config.util.action.RunAction;

public class LiftSubsystem {
    private Telemetry telemetry;
    public DcMotor rightLift, leftLift;
    public boolean hang = false;
    public int pos, bottom;
    public RunAction toZero, toHighBucket, toHighChamber, toHumanPlayer, toTransfer, toPark;
    public PIDController liftPID;
    public static int target;
    public static double p = 0.05, i = 0.0005, d = 0.00001;
    public static double f = 0.15;


    public LiftSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        rightLift = hardwareMap.get(DcMotor.class, "bottomLift");
        leftLift = hardwareMap.get(DcMotor.class, "topLift");
        //day b4 comp add variable storage
        rightLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setDirection(DcMotor.Direction.REVERSE);
        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        liftPID = new PIDController(p, i, d);
    }

    public void updatePIDF(){
            if (getTarget() == liftToZero && getPos() < 1) {
                leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                leftLift.setPower(0);
                rightLift.setPower(0);
            }else {
                liftPID.setPID(p, i, d);

                rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                double pid = liftPID.calculate(getPos(), target);
                double ticks_in_degrees = 537.7 / 360.0;
                double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;
                double power = pid + ff;
                power = Math.max(power,-0.5);
                leftLift.setPower(power);
                rightLift.setPower(power);
            }
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
    }

}