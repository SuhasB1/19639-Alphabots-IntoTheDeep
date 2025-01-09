package alphabots.opmode;

import static alphabots.config.util.FieldConstants.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import alphabots.config.runmodes.Teleop;


@TeleOp(name="Drive", group="A")
public class Drive extends OpMode {

    private Teleop teleop;
    DcMotor rightFront;
    DcMotor leftFront;
    DcMotor leftRear;
    DcMotor rightRear;

    @Override
    public void init() {
        teleop = new Teleop(hardwareMap, telemetry, new Follower(hardwareMap), observationParkPose, false, gamepad1, gamepad2);
        teleop.init();
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE); //leftFront
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);


        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        teleop.update();

    }


}