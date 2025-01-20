package alphabots.config.runmodes;

import static alphabots.config.util.FieldConstants.*;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.pedropathing.pathgen.BezierCurve;
import alphabots.config.subsystem.OuttakeSubsystem;
import alphabots.config.subsystem.ExtendSubsystem;
import alphabots.config.subsystem.IntakeSubsystem;
import alphabots.config.subsystem.LiftSubsystem;
import alphabots.config.subsystem.DriveSubsystem;
import alphabots.config.util.RobotConstants;
import alphabots.config.util.VariableStorage;
import alphabots.pedroPathing.constants.FConstants;
import alphabots.pedroPathing.constants.LConstants;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;

public class Auto {

    public IntakeSubsystem intake;
    private IntakeSubsystem.IntakeState intakeBoxState;
    private IntakeSubsystem.PivotState intakePivotState;
    public OuttakeSubsystem outtake;
    private OuttakeSubsystem.GrabState outtakeGrabState;
    private OuttakeSubsystem.PivotState outtakePivotState;
    private OuttakeSubsystem.RotateState outtakeRotateState;
    private OuttakeSubsystem.SpinState outtakeSpinState;


    public LiftSubsystem lift;
    public ExtendSubsystem extend;


    public Telemetry telemetry;

    public boolean actionBusy;

    public Timer transferTimer = new Timer(), bucketTimer = new Timer(), chamberScoreTimer = new Timer(), intakeTimer = new Timer(), parkTimer = new Timer(), specimenTimer = new Timer();
    public int transferState = -1, bucketState = -1, chamberScoreState = -1, intakeState = -1, parkState = -1, specimenState = -1;

    public Auto(HardwareMap hardwareMap, Telemetry telemetry) {
        outtake = new OuttakeSubsystem(hardwareMap, telemetry, outtakeGrabState, outtakeRotateState, outtakePivotState, outtakeSpinState);
        lift = new LiftSubsystem(hardwareMap, telemetry);
        extend = new ExtendSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem(hardwareMap, telemetry, intakeBoxState, intakePivotState);

        this.telemetry = telemetry;

        init();
    }

    public void init() {
        telemetryUpdate();
        outtake.init();
        intake.start();
    }

    public void start() {

    }

    public void stop(){
        VariableStorage.extendPos = extend.getPos();
        VariableStorage.liftPos = lift.getPos();
    }

    public void update() {
        transfer();
        pick();
        bucket();
        specimen();
        chamberScore();
        lift.updatePIDF();
        extend.updatePIDF();
        telemetryUpdate();
    }

    public void transfer() {
        switch (transferState) {
            case 1:
                actionBusy = true;
                intake.transfer();
                lift.toZero();
                outtake.idle();
                outtake.open();
                intake.transfer();
                extend.toZero();
                setTransferState(1);
                transferTimer.resetTimer();
                setTransferState(2);
                break;
            case 2:
                if (transferTimer.getElapsedTimeSeconds() > 0.35 && extend.getPos() < 30 && lift.getPos() < 30) {
                    outtake.transfer();
                    transferTimer.resetTimer();
                    setTransferState(3);
                }
                break;
            case 3:
                if (transferTimer.getElapsedTimeSeconds() > 0.2) {
                    outtake.close();
                    transferTimer.resetTimer();
                    setTransferState(4);
                }
            case 4:
                if (transferTimer.getElapsedTimeSeconds() > 0.35) {
                    transferTimer.resetTimer();
                    actionBusy = false;
                    setTransferState(-1);
                }
                break;
        }
    }

    public void setTransferState(int x) {
        transferState = x;
    }



    public void startTransfer() {
        if (actionNotBusy()) {
            setTransferState(1);
        }
    }

    public void bucket() {
        switch (bucketState) {
            case 1:
                actionBusy = true;
                outtake.idle();
                outtake.close();
                lift.toHighBucket();
                bucketTimer.resetTimer();
                setBucketState(2);
                break;
            case 2:
                if (lift.getPos() > RobotConstants.liftToHighBucket - 300) {
                    intake.ground();
                    intake.intake();
                    outtake.score();
                    outtake.close();
                    bucketTimer.resetTimer();
                    setBucketState(3);
                }
                break;
            case 3:
                if (lift.getPos() > RobotConstants.liftToHighBucket - 30) {
                    bucketTimer.resetTimer();
                    setBucketState(35);
                }
            case 35:
                if (bucketTimer.getElapsedTimeSeconds() > 0.2 ) {
                    outtake.open();
                    bucketTimer.resetTimer();
                    setBucketState(4);
                }
            case 4:
                if (bucketTimer.getElapsedTimeSeconds() > 0.2 ) {
                    outtake.idle();
                    bucketTimer.resetTimer();
                    setBucketState(5);
                }
            case 5:
                if (bucketTimer.getElapsedTimeSeconds() > 0.2) {
                    lift.toZero();
                    bucketTimer.resetTimer();
                    setBucketState(6);
                }
            case 6:
                if (lift.getPos() < 25) {
                    actionBusy = false;
                    setBucketState(-1);
                }
        }
    }

    public void setBucketState(int x) {
        bucketState = x;
    }

    public void startBucket() {
        if (actionNotBusy()) {
            setBucketState(1);
        }
    }


    public void specimen() {
        switch (specimenState) {
            case 1:
                actionBusy = true;
                lift.toZero();
                outtake.idle();
                outtake.open();
                specimenTimer.resetTimer();
                setSpecimenState(2);
                break;
            case 2:
                if (specimenTimer.getElapsedTimeSeconds() > 0.2 && lift.getPos() < 30) {
                    outtake.specimenGrab();
                    specimenTimer.resetTimer();
                    setSpecimenState(3);
                }
                break;
            case 3:
                if (specimenTimer.getElapsedTimeSeconds() > 0.1) {
                    outtake.close();
                    specimenTimer.resetTimer();
                    setSpecimenState(4);
                }
                break;
            case 4:
                if (specimenTimer.getElapsedTimeSeconds() > 0.4) {
                    outtake.specimenScore();
                    specimenTimer.resetTimer();
                    setSpecimenState(5);
                }
                break;
            case 5:
                if (specimenTimer.getElapsedTimeSeconds() > 0.2) {
                    specimenTimer.resetTimer();
                    actionBusy = false;
                    setSpecimenState(-1);
                }
                break;
        }
    }

    public void resetSpecimen() {
        actionBusy = true;
        outtake.open();
        lift.toZero();
        specimenTimer.resetTimer();
        setSpecimenState(1);
        actionBusy = false;
    }


    public void setSpecimenState(int x) {
        specimenState = x;
    }

    public void startSpecimen() {
        if (actionNotBusy()) {
            setSpecimenState(1);
        }
    }

    public void chamberScore() {
        switch (chamberScoreState) {
            case 1:
                actionBusy = true;
                outtake.close();
                outtake.specimenScore();
                chamberScoreTimer.resetTimer();
                setChamberScoreState(2);
                break;
            case 2:
                if (chamberScoreTimer.getElapsedTimeSeconds() > 0.3 && extend.getPos() < 30 && lift.getPos() < 30) {
                    lift.toHighChamber();
                    chamberScoreTimer.resetTimer();
                    setChamberScoreState(3);
                }
                break;
            case 3:
                if (chamberScoreTimer.getElapsedTimeSeconds() > 0.2) {
                    outtake.specimenScore();
                    chamberScoreTimer.resetTimer();
                    actionBusy = false;
                    setChamberScoreState(-1);
                }
                break;
        }
    }

    public void setChamberScoreState(int x) {
        chamberScoreState = x;
    }
    public void startChamberScore() {
        if (actionNotBusy()) {
            setChamberScoreState(1);
        }
    }


/*
    public void chamberScore() {
        switch (chamberScoreState) {
            case 1:
                actionBusy = true;
                outtake.specimenScore();
                extend.toZero();
                chamberScoreTimer.resetTimer();
                setChamberScoreState(2);
                break;
            case 2:
                if ((follower.getPose().getX() >= specimen1Pose.getX() - 0.5)) {
                    chamberScoreTimer.resetTimer();
                    setChamberScoreState(3);
                }
                break;
            case 3:
                if(chamberScoreTimer.getElapsedTimeSeconds() > 0.375) {
                    outtake.open();
                    actionBusy = false;
                    setChamberScoreState(-1);
                }
                break;
        }
    }

    public void setChamberScoreState(int x) {
        chamberScoreState = x;
    }

    public void startChamberScore() {
        if(actionNotBusy()) {
            setChamberScoreState(1);
        }
    }

    public void specimen() {
        switch (specimenState) {
            case 1:
                actionBusy = true;
                outtake.specimenGrab();
                extend.toZero();
                specimenTimer.resetTimer();
                setSpecimenState(2);
            case 2:
                if(specimenTimer.getElapsedTimeSeconds() > 0.25) {
                    outtake.close();
                    setSpecimenState(3);
                }
                break;
            case 3:
                actionBusy = false;
                setSpecimenState(-1);
                break;
        }
    }

    public void setSpecimenState(int x) {
        specimenState = x;
    }

    public void startSpecimen() {
        if(actionNotBusy()) {
            setSpecimenState(1);
        }
    }
    */

    public void pick() {
        switch (intakeState) {
            case 1:
                actionBusy = true;
                intake.intake();
                intake.ground();
                intakeTimer.resetTimer();
                setIntakeState(2);
                break;
            case 2:
                if(intakeTimer.getElapsedTimeSeconds() > 0) {
                    extend.setTarget(700);
                    intakeTimer.resetTimer();
                    setIntakeState(10);
                }
                break;
            case 10:
                if(700 - extend.getPos() < 30) {
                    intakeTimer.resetTimer();
                    setIntakeState(11);
                }
                break;
            case 11:
                if(intakeTimer.getElapsedTimeSeconds() > 0.2) {
                    extend.addToTarget(450);
                    intakeTimer.resetTimer();
                    setIntakeState(3);
                }
                break;
            case 3:
                if (intakeTimer.getElapsedTimeSeconds() > 0.8) {
                    intake.idle();
                    intake.intake();
                    extend.toZero();
                    intake.transfer();
                    intakeTimer.resetTimer();
                    setIntakeState(4);
                }
                break;
            case 4:
                if (intakeTimer.getElapsedTimeSeconds() > 0.4 && extend.getPos() < 15) {
                    actionBusy = false;
                    setIntakeState(-1);
                }
                break;
        }
    }

    public void setIntakeState(int x) {
        intakeState = x;
    }

    public void startIntake() {
        if (actionNotBusy()) {
            setIntakeState(1);
        }
    }

    public void park() {
        switch (parkState) {
            case 1:
                actionBusy = true;
                outtake.idle();
                intake.idle();
                intake.transfer();
                extend.toZero();
                parkTimer.resetTimer();
                setParkState(2);
                break;
            case 2:
                if(parkTimer.getElapsedTimeSeconds() > 0.3) {
                    lift.toPark();
                    actionBusy = false;
                    setParkState(-1);
                }
                break;
        }
    }

    public void setParkState(int x) {
        parkState = x;
    }

    public void startPark() {
        if (actionNotBusy()) {
            setParkState(1);
        }
    }

    public boolean actionNotBusy() {
        return !actionBusy;
    }

    public void telemetryUpdate() {
        telemetry.addData("Action Busy?: ", actionBusy);
        extend.telemetry();
        lift.telemetry();
        outtake.telemetry();
        intake.telemetry();
        telemetry.update();
    }

}