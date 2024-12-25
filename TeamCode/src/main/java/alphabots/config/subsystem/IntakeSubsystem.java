package alphabots.config.subsystem;

import static alphabots.config.util.RobotConstants.*;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeSubsystem { //STILL NEED TO CODE WIPER
    public enum IntakeState {
        INTAKE, IDLE, OUTTAKE
    }

    public enum PivotState {
        TRANSFER, GROUND, HOVER
    }

    public Servo leftPivot, rightPivot, wiper;
    public CRServo intake;
    public IntakeState intakeState;
    public PivotState pivotState;
    private Telemetry telemetry;

    public IntakeSubsystem(HardwareMap hardwareMap, Telemetry telemetry, IntakeState intakeState, PivotState pivotState) {
        intake = hardwareMap.get(CRServo.class, "iG");
        leftPivot = hardwareMap.get(Servo.class, "iLP");
        rightPivot = hardwareMap.get(Servo.class, "iRP");
        wiper = hardwareMap.get(Servo.class, "iW");
        this.telemetry = telemetry;
        this.intakeState = intakeState;
        this.pivotState = pivotState;
    }

    public void setIntakeState(IntakeState intakeState) {
        if (intakeState == IntakeState.IDLE) {
            intake.setPower(intakeBoxIdle);
            this.intakeState = IntakeState.IDLE;
        } else if (intakeState == IntakeState.INTAKE) {
            intake.setPower(intakeBoxIntake);
            this.intakeState = IntakeState.INTAKE;
        } else if (intakeState == IntakeState.OUTTAKE) {
            intake.setPower(intakeBoxOuttake);
            this.intakeState = IntakeState.OUTTAKE;
        }
    }

    public void setPivotState(PivotState pivotState) {
        if (pivotState == PivotState.TRANSFER) {
            leftPivot.setPosition(intakePivotTransfer);
            rightPivot.setPosition(intakePivotTransfer);
            this.pivotState = PivotState.TRANSFER;
        } else if (pivotState == PivotState.GROUND) {
            leftPivot.setPosition(intakePivotGround);
            rightPivot.setPosition(intakePivotGround);
            this.pivotState = PivotState.GROUND;
        } else if (pivotState == PivotState.HOVER) {
            leftPivot.setPosition(intakePivotHover);
            rightPivot.setPosition(intakePivotHover);
            this.pivotState = PivotState.HOVER;
        }
    }

    public void switchIntakeState() {
        if (intakeState == IntakeState.INTAKE) {
            setIntakeState(IntakeState.IDLE);
        } else if (intakeState == IntakeState.IDLE || intakeState == IntakeState.OUTTAKE) {
            setIntakeState(IntakeState.INTAKE);
        }
    }

    public void switchOuttakeState() {
        if (intakeState == IntakeState.OUTTAKE) {
            setIntakeState(IntakeState.IDLE);
        } else if (intakeState == IntakeState.INTAKE || intakeState == IntakeState.IDLE) {
            setIntakeState(IntakeState.OUTTAKE);
        }
    }

    public void intake() {
        setIntakeState(IntakeState.INTAKE);
    }

    public void idle() {
        setIntakeState(IntakeState.IDLE);
    }

    public void transfer() {
        setPivotState(PivotState.TRANSFER);
        setIntakeState(IntakeState.IDLE);
    }

    public void ground() {
        setPivotState(PivotState.GROUND);
    }

    public void hover() {
        setPivotState(PivotState.HOVER);
        setIntakeState(IntakeState.INTAKE);
    }

    public void init() {
        setPivotState(PivotState.TRANSFER);
        setIntakeState(IntakeState.IDLE);
    }

    public void start() {
        setPivotState(PivotState.TRANSFER);
        setIntakeState(IntakeState.IDLE);
    }

    public void telemetry() {
        telemetry.addData("Intake Box State: ", intakeState);
        telemetry.addData("Intake Pivot State: ", pivotState);
    }
}