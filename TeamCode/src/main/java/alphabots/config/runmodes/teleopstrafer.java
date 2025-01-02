package alphabots.config.runmodes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="TeleOpStarfer", group="basic_move")
public class teleopstrafer  extends OpMode{
    DcMotor rightFront;
    DcMotor leftFront;
    DcMotor leftRear;
    DcMotor rightRear;

    @Override
    public void init() {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD); //leftFront
        leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);


        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    @Override

    public void loop()
    {
        moveDriveTrain();
    }

    public void moveDriveTrain()
    {
        double driveForward = 0; //Moves forwards and backwards
        double driveBackward = 0;
        double strafe = 0; //Move side-to-side
        double rotate = 0;
        double drive = 0;
        double denominator = 1;

        drive = gamepad1.left_stick_y;
        strafe = gamepad1.right_stick_x * 1.1;
        rotate = gamepad1.left_stick_x;
        int multiplier = 1;

        driveForward = gamepad1.right_trigger;
        driveBackward = gamepad1.left_trigger;

        denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(rotate), 1);
        if(driveForward > 0) {
            rightFront.setPower(-driveForward * (multiplier));
            rightRear.setPower(-driveForward * multiplier);
            leftFront.setPower(-driveForward * multiplier);
            leftRear.setPower(-driveForward * multiplier);
        } else if(driveBackward > 0) {
            rightFront.setPower(driveBackward * multiplier);
            rightRear.setPower(driveBackward * multiplier);
            leftFront.setPower(driveBackward * multiplier);
            leftRear.setPower(driveBackward * multiplier);

        } else {
            rightFront.setPower(((drive + strafe  + rotate) / denominator) * multiplier);
            rightRear.setPower(((drive - rotate + strafe) / denominator) * multiplier);
            leftFront.setPower(((drive - rotate - strafe) / denominator) * multiplier);
            leftRear.setPower(((drive + rotate - strafe) / denominator) * multiplier);
        }
    }
}
