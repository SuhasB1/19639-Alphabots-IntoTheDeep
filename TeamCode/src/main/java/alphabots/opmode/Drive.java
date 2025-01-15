package alphabots.opmode;

import static alphabots.config.util.FieldConstants.*;

import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import alphabots.config.runmodes.Teleop;
import alphabots.config.util.FieldConstants;
import alphabots.pedroPathing.constants.FConstants;
import alphabots.pedroPathing.constants.LConstants;

@TeleOp(name="Drive", group="A")
public class Drive extends OpMode {

    private Teleop teleop;
    @Override
    public void init() {
        teleop = new Teleop(hardwareMap, telemetry, false, gamepad1, gamepad2);
        teleop.init();
    }

    @Override
    public void start(){
        teleop.start();
    }

    @Override
    public void loop() {
        teleop.update();

    }


}