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
    public Timer autoTimer = new Timer();
    public Timer intakedTimer = new Timer();
    private Telemetry telemetry;
    private boolean isOuttaking = false;
    private double outtakeStartTime = 0;

    private double intakedStartTime = 0;

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

    public void outtake(){
        setIntakeState(IntakeState.OUTTAKE);
    }

    public void swipeOpen() {
        wiper.setPosition(RobotConstants.intakeWiperOpen); // Explicitly referencing constant
    }

    public void swipeClose() {
        wiper.setPosition(RobotConstants.intakeWiperClose);; // Explicitly referencing constant
    }

    public void idle() {
        setIntakeState(IntakeState.IDLE);
    }

    public void transfer() {
        setPivotState(PivotState.TRANSFER);
    }

    public void ground() {
        setPivotState(PivotState.GROUND);
    }


    public void hover() {
        setPivotState(PivotState.HOVER);
    }

    public void init() {
        setPivotState(PivotState.TRANSFER);
        setIntakeState(IntakeState.IDLE);
        wiper.setPosition(RobotConstants.intakeWiperClose);
    }

    public void start() {
        setPivotState(PivotState.TRANSFER);
        wiper.setPosition(RobotConstants.intakeWiperClose);
        autoTimer.resetTimer();
        intakedTimer.resetTimer();
    }
    public void autoOuttake() {
        if (VariableStorage.currentTeam == VariableStorage.Team.RED) {
            if (pin0.getState() && !pin1.getState()) {
                if (!isOuttaking) {
                    outtake();
                    outtakeStartTime = autoTimer.getElapsedTimeSeconds();
                    isOuttaking = true;
                }
            }
        } else if (VariableStorage.currentTeam == VariableStorage.Team.BLUE) {
            if (!pin0.getState() && pin1.getState()) {
                if (!isOuttaking) {
                    outtake();
                    outtakeStartTime = autoTimer.getElapsedTimeSeconds();
                    isOuttaking = true;
                }
            }
        }


        if (isOuttaking && (autoTimer.getElapsedTimeSeconds() - outtakeStartTime) > 1.0) {
            idle();
            isOuttaking = false;
        }
    }

    public void prevent2sample() {
        if (pivotState == PivotState.TRANSFER){
            if(detected()) {
                if(!isOuttaking) {
                    outtake();
                    intakedStartTime = intakedTimer.getElapsedTimeSeconds();
                    isOuttaking = true;
                }
            }
            if (intakedTimer.getElapsedTimeSeconds() - intakedStartTime > 1.5) {
                idle();
                isOuttaking = false;
            }
        }
    }

    public boolean detected(){
        if (!pin0.getState() && !pin1.getState()) {
            return false;
        }else {
            return true;
        }

    }

    public void telemetry() {
        telemetry.addData("Intake Box State: ", intakeState);
        telemetry.addData("Intake Pivot State: ", pivotState);
        telemetry.addData("Pin0 State", pin0.getState());
        telemetry.addData("Pin1 State", pin1.getState());
    }
}
