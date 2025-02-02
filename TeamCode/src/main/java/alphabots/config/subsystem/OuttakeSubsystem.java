package alphabots.config.subsystem;

import static alphabots.config.util.RobotConstants.*;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import alphabots.config.util.RobotConstants;


public class OuttakeSubsystem {

    public enum GrabState {
        CLOSED, OPEN
    }

    public enum RotateState {
        TRANSFER, SCORE, SPECIMENGRAB, SPECIMENSCORE, IDLE
    }
    
    public enum PivotState {
        TRANSFER, SCORE, SPECIMENGRAB, SPECIMENSCORE, IDLE
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

        leftPivot.setDirection(Servo.Direction.REVERSE);
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
        } else if (state == RotateState.IDLE) {
            rotate.setPosition(outtakeRotateIdle);
            this.rotateState = RotateState.IDLE;
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
        } else if (pivotState == PivotState.IDLE) {
            leftPivot.setPosition(outtakePivotIdle);
            rightPivot.setPosition(outtakePivotIdle);
            this.pivotState = PivotState.IDLE;
        }
    }

    public void open() {
        setGrabState(GrabState.OPEN);
    }

    public void close() {
        setGrabState(GrabState.CLOSED);
    }

    public void idle() {
        rotate.setPosition(RobotConstants.outtakeRotateIdle);
        leftPivot.setPosition(RobotConstants.outtakePivotIdle);
        rightPivot.setPosition(RobotConstants.outtakePivotIdle);
        grab.setPosition(RobotConstants.outtakeGrabOpen);
        spin.setPosition(outtakeSpinForward);

    }

    public void transfer() {
        grab.setPosition(RobotConstants.outtakeGrabOpen);
        rotate.setPosition(RobotConstants.outtakeRotateTransfer);
        leftPivot.setPosition(RobotConstants.outtakePivotTransfer);
        rightPivot.setPosition(RobotConstants.outtakePivotTransfer);
        spin.setPosition(RobotConstants.outtakeSpinForward);
    }

    public void score() {
        grab.setPosition(RobotConstants.outtakeGrabClose);
        rotate.setPosition(RobotConstants.outtakeRotateScore);
        leftPivot.setPosition(RobotConstants.outtakePivotScore);
        rightPivot.setPosition(RobotConstants.outtakePivotScore);
        spin.setPosition(RobotConstants.outtakeSpinForward);
    }

    public void specimenGrab() {
        setRotateState(RotateState.SPECIMENGRAB);
        setPivotState(PivotState.SPECIMENGRAB);
        setSpinState(SpinState.FORWARD);
        if(grabState == GrabState.CLOSED) {
            setGrabState(GrabState.CLOSED);
        }
        if(grabState == GrabState.OPEN) {
            setGrabState(GrabState.OPEN);
        }
    }

    public void specimenScore() {
        setRotateState(RotateState.SPECIMENSCORE);
        setPivotState(PivotState.SPECIMENSCORE);
        setGrabState(GrabState.CLOSED);
        setSpinState(SpinState.BACKWARD);
    } 

    public void init() {
        idle();
    }

    public void start() {
        idle();
    }

    public void telemetry() {
        telemetry.addData("Outtake Grab State: ", grabState);
        telemetry.addData("Outtake Rotate State: ", rotateState);
        telemetry.addData("Outtake Pivot State: ", pivotState);
        telemetry.addData("Outtake Spin State: ", spinState);


    }
}