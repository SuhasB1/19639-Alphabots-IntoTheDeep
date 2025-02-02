package alphabots.opmode;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
// Put intake box (iP) (iLP, iRP) at 0.5
// Rotate (oR) to 0.8
// Grip (oG)  to 0
// Pivot (oP) (oRP, oLP) 0.13
// Rotate (oR) to 0.58
// Grip (oG) to 1

// Pivot (oP) (oRP, oLP) to 0.33
@Config
@TeleOp(name="TransferFSM", group="a")
public class SampleTransferFSM extends OpMode {
    private enum State {
        INIT, ROTATE_UP, GRIP, PIVOT_DOWN, ROTATE_DOWN, RELEASE, PIVOT_FINAL, MIDDLE_SHI,  DONE
    }

    private State currentState = State.INIT;


    private long stateStartTime;

    private Servo rightPivot, leftPivot;
    private Servo outtakeRightPivot, outtakeLeftPivot, outtakeRotate, outtakeGrip;

    @Override
    public void init() {
        rightPivot = hardwareMap.get(Servo.class, "iRP");
        leftPivot = hardwareMap.get(Servo.class, "iLP");
        outtakeRightPivot = hardwareMap.get(Servo.class, "oRP");
        outtakeLeftPivot = hardwareMap.get(Servo.class, "oLP");
        outtakeRotate = hardwareMap.get(Servo.class, "oR");
        outtakeGrip = hardwareMap.get(Servo.class, "oG");

        outtakeLeftPivot.setDirection(Servo.Direction.REVERSE);
        leftPivot.setDirection(Servo.Direction.REVERSE);

        stateStartTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        long elapsedTime = System.currentTimeMillis() - stateStartTime;

        switch (currentState) {
            case INIT:
                rightPivot.setPosition(0.56);
                leftPivot.setPosition(0.56);
                if (elapsedTime > 500) nextState(State.ROTATE_UP);
                break;

            case ROTATE_UP:
                outtakeRotate.setPosition(0.8);
                if (elapsedTime > 500) nextState(State.GRIP);
                break;

            case GRIP:
                outtakeGrip.setPosition(0.2);
                if (elapsedTime > 500) nextState(State.PIVOT_DOWN);
                break;

            case PIVOT_DOWN:
                outtakeRightPivot.setPosition(0.13);
                outtakeLeftPivot.setPosition(0.13);
                if (elapsedTime > 500) nextState(State.ROTATE_DOWN);
                break;

            case ROTATE_DOWN:
                outtakeRotate.setPosition(0.68);
                if (elapsedTime > 500) nextState(State.RELEASE);
                break;

            case RELEASE:
                outtakeRightPivot.setPosition(0.015);
                outtakeLeftPivot.setPosition(0.015);
                if (elapsedTime > 500) nextState(State.PIVOT_FINAL);
                break;

            case PIVOT_FINAL:
                outtakeGrip.setPosition(1);
                if (elapsedTime > 500) nextState(State.MIDDLE_SHI);
                break;

            case MIDDLE_SHI:
                outtakeRightPivot.setPosition(0.33);
                outtakeLeftPivot.setPosition(0.33);
                if (elapsedTime > 500) nextState(State.DONE);

            case DONE:
                break;

        }

        telemetry.addData("State", currentState);
        telemetry.update();
    }

    private void nextState(State newState) {
        currentState = newState;
        stateStartTime = System.currentTimeMillis();
    }
}


