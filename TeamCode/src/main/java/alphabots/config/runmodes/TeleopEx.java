/*package alphabots.config.runmodes;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static alphabots.config.util.FieldConstants.humanPlayerPose;

import alphabots.config.subsystem.OuttakeSubsystem;
import alphabots.config.subsystem.ExtendSubsystem;
import alphabots.config.subsystem.IntakeSubsystem;
import alphabots.config.subsystem.LiftSubsystem;
import alphabots.config.util.RobotConstants;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
    public void update() {

            if (currentGamepad2.y && !previousGamepad2.y) {
                extend.setLimitToSample();
                outtake.transfer();
                intake.hover();
            }

            if (currentGamepad2.x && !previousGamepad2.x) {
                extend.setLimitToSample();
                outtake.score();
                intake.hover();
            }

            if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left)
                specimenGrabPos();

            if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right)
                specimenScorePos();

            if (currentGamepad2.b && !previousGamepad2.b)
                startTransfer();

            if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
                intake.switchGrabState();
            }

            if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
                startspeciman();
                outtake.score();
            }

            if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper) {
                intake.rotateCycle(false);
            }

            if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
                intake.rotateCycle(true);
            }

            if (gamepad2.left_stick_button) {
                outtake.hang();
                intake.transfer();
                extend.toZero();
            }

            if (gamepad2.right_stick_button) {
                intake.transfer();
            }
        }
        outtake.loop();

        autoBucket();
        transfer();
        speciman();
        autoSpecimen();

        follower.update();
        telemetry.addData("X: ", follower.getPose().getX());
        telemetry.addData("Y: ", follower.getPose().getY());
        telemetry.addData("Heading: ", follower.getPose().getHeading());
        telemetry.addData("Action Busy?: ", actionBusy);
        telemetry.addData("Drive Busy? ", driveBusy);
        telemetry.addData("Auto Bucket State", autoBucketState);
        telemetry.addData("Transfer State", transferState);
        telemetry.addData("speciman State", specimanState);
        extend.telemetry();
        lift.telemetry();
        outtake.telemetry();
        intake.telemetry();
        telemetry.update();
    }

    private void specimenGrabPos() {
        extend.setLimitToSpecimen();
        outtake.startSpecGrab();
        intake.specimen();
    }

    private void specimenScorePos() {
        extend.setLimitToSpecimen();
        outtake.specimenScore();
        intake.specimen();
    }

    private void transfer() {
        switch (transferState) {
            case 1:
                intake.close();
                outtake.transfer();
                intake.transfer();
                setTransferState(2);
                break;
            case 2:
                if (transferTimer.getElapsedTimeSeconds() > 0.1) {
                    outtake.setRotateState(OuttakeSubsystem.RotateState.TRANSFER);
                    extend.toZero();
                    setTransferState(3);
                }
                break;
            case 3:
                if (transferTimer.getElapsedTimeSeconds() > 0.2599) {
                    outtake.transfer();
                    setTransferState(4);
                }
                break;
            case 4:
                if (transferTimer.getElapsedTimeSeconds() > 0.25) {
                    outtake.close();
                    setTransferState(5);
                }
                break;
            case 5:
                if (transferTimer.getElapsedTimeSeconds() > 0.5) {
                    outtake.score();
                    setTransferState(6);
                }
                break;
            case 6:
                if (transferTimer.getElapsedTimeSeconds() > 0) {
                    intake.open();
                    setTransferState(7);
                }
                break;
            case 7:
                if (transferTimer.getElapsedTimeSeconds() > 0.25) {
                    intake.hover();
                    actionBusy = false;
                    setTransferState(-1);
                }
                break;
        }
    }

    public void setTransferState(int x) {
        transferState = x;
        transferTimer.resetTimer();
    }

    public void startTransfer() {
        setTransferState(1);
    }

    private void speciman() {
        switch (specimanState) {
            case 0:
                intake.ground();
                intake.open();
                outtake.transfer();
                setspecimanState(1);
                break;
            case 1:
                if(specimanTimer.getElapsedTimeSeconds() > 0.3) {
                    intake.close();
                    setspecimanState(2);
                }
                break;
            case 2:
                if (specimanTimer.getElapsedTimeSeconds() > 0.25) {
                    intake.hover();
                    setspecimanState(-1);
                }
                break;
        }
    }

    public void setspecimanState(int x) {
        specimanState = x;
        specimanTimer.resetTimer();
    }

    public void startspeciman() {
        setspecimanState(0);
    }
} */