package alphabots.config.subsystem;

import static alphabots.config.util.RobotConstants.*;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import alphabots.config.util.VariableStorage;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.pedropathing.util.Timer;

import alphabots.config.util.RobotConstants;

public class IntakeSubsystem { //STILL NEED TO CODE WIPER
    public enum IntakeState {
        INTAKE, IDLE, OUTTAKE
    }

    public enum PivotState {
        TRANSFER, GROUND, HOVER
    }

    public Servo leftPivot, rightPivot, wiper;
    public CRServo intake;
    DigitalChannel pin0, pin1;
    public IntakeState intakeState;
    public PivotState pivotState;
    public Timer wiperTimer = new Timer();
    private Telemetry telemetry;

    public IntakeSubsystem(HardwareMap hardwareMap, Telemetry telemetry, IntakeState intakeState, PivotState pivotState) {
        intake = hardwareMap.get(CRServo.class, "iB");
        leftPivot = hardwareMap.get(Servo.class, "iLP");
        rightPivot = hardwareMap.get(Servo.class, "iRP");
        wiper = hardwareMap.get(Servo.class, "iW");
        pin0 = hardwareMap.digitalChannel.get("digital0");
        pin1 = hardwareMap.digitalChannel.get("digital1");
        leftPivot.setDirection(Servo.Direction.REVERSE);
        intake.setDirection(CRServo.Direction.REVERSE);
        this.telemetry = telemetry;
        this.intakeState = intakeState;
        this.pivotState = pivotState;
    }

    public void setIntakeState(IntakeState intakeState) {
        if (intakeState == IntakeState.IDLE) {
            intake.setPower(intakeBoxIdle);; // Explicitly referencing constant
            this.intakeState = IntakeState.IDLE;
        } else if (intakeState == IntakeState.INTAKE) {
            intake.setPower(intakeBoxIntake); // Explicitly referencing constant
            this.intakeState = IntakeState.INTAKE;
        } else if (intakeState == IntakeState.OUTTAKE) {
            intake.setPower(intakeBoxOuttake);
            this.intakeState = IntakeState.OUTTAKE;
        }
    }

    public void setPivotState(PivotState pivotState) {
        if (pivotState == PivotState.TRANSFER) {
            leftPivot.setPosition(RobotConstants.intakePivotTransfer);
            rightPivot.setPosition(RobotConstants.intakePivotTransfer);
            this.pivotState = PivotState.TRANSFER;
        } else if (pivotState == PivotState.GROUND) {
            leftPivot.setPosition(RobotConstants.intakePivotGround);
            rightPivot.setPosition(RobotConstants.intakePivotGround);
            this.pivotState = PivotState.GROUND;
        } else if (pivotState == PivotState.HOVER) {
            leftPivot.setPosition(RobotConstants.intakePivotHover);
            rightPivot.setPosition(RobotConstants.intakePivotHover);
            this.pivotState = PivotState.HOVER;
        }
    }

    public void switchIntakeState() {
        if (intakeState == IntakeState.INTAKE) {
            intake.setPower(RobotConstants.intakeBoxIdle);
        } else if (intakeState == IntakeState.IDLE || intakeState == IntakeState.OUTTAKE) {
            intake.setPower(RobotConstants.intakeBoxIntake);
        }
    }

    public void switchOuttakeState() {
        if (intakeState == IntakeState.OUTTAKE) {
            intake.setPower(RobotConstants.intakeBoxIdle);
        } else if (intakeState == IntakeState.INTAKE || intakeState == IntakeState.IDLE) {
            intake.setPower(RobotConstants.intakeBoxOuttake);
        }
    }

    public void intake() {
        intake.setPower(RobotConstants.intakeBoxIntake);
    }

    public void outtake(){
        setIntakeState(IntakeState.OUTTAKE);
        intake.setPower(intakeBoxOuttake);
    }

    public void swipeOpen() {
        wiper.setPosition(RobotConstants.intakeWiperOpen); // Explicitly referencing constant
    }

    public void swipeClose() {
        wiper.setPosition(RobotConstants.intakeWiperClose);; // Explicitly referencing constant
    }

    public void idle() {
        intake.setPower(RobotConstants.intakeBoxIdle);
    }

    public void transfer() {
        leftPivot.setPosition(RobotConstants.intakePivotTransfer);
        rightPivot.setPosition(RobotConstants.intakePivotTransfer);
        intake.setPower(RobotConstants.intakeBoxIdle);
    }

    public void ground() {
        leftPivot.setPosition(RobotConstants.intakePivotGround);
        rightPivot.setPosition(RobotConstants.intakePivotGround);
    }


    public void hover() {
        leftPivot.setPosition(RobotConstants.intakePivotHover);
        rightPivot.setPosition(RobotConstants.intakePivotHover);
    }

    public void init() {
        leftPivot.setPosition(RobotConstants.intakePivotTransfer);
        rightPivot.setPosition(RobotConstants.intakePivotTransfer);
        setIntakeState(IntakeState.IDLE);
        intake.setPower(RobotConstants.intakeBoxIdle);
        wiper.setPosition(RobotConstants.intakeWiperClose);
    }

    public void start() {
        setPivotState(PivotState.TRANSFER);
        setIntakeState(IntakeState.IDLE);
        wiper.setPosition(RobotConstants.intakeWiperClose);
    }

    //*
    public void autoOuttake(){
        if(VariableStorage.currentTeam == VariableStorage.Team.RED){
            if(pin0.getState() && !pin1.getState()){
                outtake();
            }
        }
        if(VariableStorage.currentTeam == VariableStorage.Team.BLUE){
            if(!pin0.getState() && pin1.getState()){
                outtake();
            }
        }
    }


    public void telemetry() {
        telemetry.addData("Intake Box State: ", intakeState);
        telemetry.addData("Intake Pivot State: ", pivotState);
        telemetry.addData("Pin0 State", pin0.getState());
        telemetry.addData("Pin1 State", pin1.getState());
    }
}
