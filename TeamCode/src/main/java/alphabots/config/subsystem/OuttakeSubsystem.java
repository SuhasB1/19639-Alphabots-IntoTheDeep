package alphabots.config.subsystem;

import static alphabots.config.util.RobotConstants.*;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class OuttakeSubsystem {

    public enum GrabState {
        CLOSED, OPEN
    }

    public enum RotateState {
        TRANSFER, SCORE, SPECIMENGRAB, SPECIMENSCORE
    }
    
    public enum PivotState {
        TRANSFER, SCORE, SPECIMENGRAB, SPECIMENSCORE
    }

    public enum SpinState {
        FORWARD, BACKWARD
    }

    public Servo grab, rotate, spin, leftPivot, rightPivot;
    public GrabState grabState;
    public RotateState rotateState;
    public PivotState pivotState;
    public SpinState spinState;

    private Telemetry telemetry;

    public OuttakeSubsystem(HardwareMap hardwareMap, Telemetry telemetry, GrabState grabState, RotateState rotateState, PivotState pivotState, SpinState spinState) {
        grab = hardwareMap.get(Servo.class, "oG");
        rotate = hardwareMap.get(Servo.class, "oR");
        spin = hardwareMap.get(Servo.class, "oS");
        leftPivot = hardwareMap.get(Servo.class, "oLP");
        rightPivot = hardwareMap.get(Servo.class, "oRP");
        this.telemetry = telemetry;
        this.grabState = grabState;
        this.rotateState = rotateState;
        this.pivotState = pivotState;
        this.spinState = spinState;
    }

    public void setRotateState(RotateState state) {
        if (state == RotateState.TRANSFER) {
            rotate.setPosition(outtakeRotateTransfer);
            this.rotateState = RotateState.TRANSFER;
        } else if (state == RotateState.SCORE) {
            rotate.setPosition(outtakeRotateScore);
            this.rotateState = RotateState.SCORE;
        } else if (state == RotateState.SPECIMENGRAB) {
            rotate.setPosition(outtakeRotateSpecimenGrab);
            this.rotateState = RotateState.SPECIMENGRAB;
        } else if (state == RotateState.SPECIMENSCORE) {
            rotate.setPosition(outtakeRotateSpecimenScore);
            this.rotateState = RotateState.SPECIMENSCORE;
        }
    }

    public void setGrabState(GrabState grabState) {
        if (grabState == GrabState.CLOSED) {
            grab.setPosition(outtakeGrabClose);
            this.grabState = GrabState.CLOSED;
        } else if (grabState == GrabState.OPEN) {
            grab.setPosition(outtakeGrabOpen);
            this.grabState = GrabState.OPEN;
        }
    }

    public void setSpinState(SpinState spinState) {
        if (spinState == SpinState.FORWARD) {
            spin.setPosition(outtakeSpinForward);
            this.spinState = SpinState.FORWARD;
        } else if (spinState == SpinState.BACKWARD) {
            spin.setPosition(outtakeSpinBackward);
            this.spinState = SpinState.BACKWARD;
        }
    }

    public void switchGrabState() {
        if (grabState == GrabState.CLOSED) {
            setGrabState(GrabState.OPEN);
        } else if (grabState == GrabState.OPEN) {
            setGrabState(GrabState.CLOSED);
        }
    }

    public void setPivotState(PivotState pivotState) {
        if (pivotState == PivotState.TRANSFER) {
            leftPivot.setPosition(outtakePivotTransfer);
            rightPivot.setPosition(outtakePivotTransfer);
            this.pivotState = PivotState.TRANSFER;
        } else if (pivotState == PivotState.SCORE) {
            leftPivot.setPosition(outtakePivotScore);
            rightPivot.setPosition(outtakePivotScore);
            this.pivotState = PivotState.SCORE;
        } else if (pivotState == PivotState.SPECIMENGRAB) {
            leftPivot.setPosition(outtakePivotSpecimenGrab);
            rightPivot.setPosition(outtakePivotSpecimenGrab);
            this.pivotState = PivotState.SPECIMENGRAB;
        } else if (pivotState == PivotState.SPECIMENSCORE) {
            leftPivot.setPosition(outtakePivotSpecimenScore);
            rightPivot.setPosition(outtakePivotSpecimenScore);
            this.pivotState = PivotState.SPECIMENSCORE;
        }
    }

    public void open() {
        setGrabState(GrabState.OPEN);
    }

    public void close() {
        setGrabState(GrabState.CLOSED);
    }

    public void transfer() {
        setRotateState(RotateState.TRANSFER);
        setPivotState(PivotState.TRANSFER);
        setGrabState(GrabState.OPEN);
        setSpinState(SpinState.FORWARD);
    }

    public void score() {
        setRotateState(RotateState.SCORE);
        setPivotState(PivotState.SCORE);
        setGrabState(GrabState.CLOSED);
        setSpinState(SpinState.FORWARD);
    }

    public void specimenGrab() {
        setRotateState(RotateState.SPECIMENGRAB);
        setPivotState(PivotState.SPECIMENGRAB);
        setGrabState(GrabState.OPEN);
        setSpinState(SpinState.FORWARD);
    }

    public void specimenScore() {
        setRotateState(RotateState.SPECIMENSCORE);
        setPivotState(PivotState.SPECIMENSCORE);
        setGrabState(GrabState.CLOSED);
        setSpinState(SpinState.BACKWARD);
    } 

    public void init() {
        setPivotState(PivotState.TRANSFER);
        setRotateState(RotateState.TRANSFER);
        setGrabState(GrabState.CLOSED);
        setSpinState(SpinState.FORWARD);
    }

    public void start() {
        setPivotState(PivotState.TRANSFER);
        setRotateState(RotateState.TRANSFER);
        setGrabState(GrabState.CLOSED);
        setSpinState(SpinState.FORWARD);
    }

    public void telemetry() {
        telemetry.addData("Outtake Grab State: ", grabState);
        telemetry.addData("Outtake Rotate State: ", rotateState);
        telemetry.addData("Outtake Pivot State: ", pivotState);
        telemetry.addData("Outtake Spin State: ", spinState);


    }
}