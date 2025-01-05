/*package alphabots.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import alphabots.config.subsystem.LimelightSubsystem;
import alphabots.config.util.FieldConstants;
import com.pedropathing.localization.Pose;

@TeleOp
@Config
public class LimelightTest extends OpMode {

    private Teleop teleop;
    private LimelightSubsystem limelightSubsystem;

    @Override
    public void init() {
        teleop = new Teleop(hardwareMap, telemetry, new Follower(hardwareMap), observationParkPose, false, gamepad1, gamepad2);
        teleop.init();

        limelightSubsystem = new LimelightSubsystem(hardwareMap, telemetry, observationParkPose, observationParkPose);
        limelightSubsystem.init();

        //checks for red
        limelightSubsystem.switchPipeline(LimelightSubsystem.limelightState.red);
    }

    @Override
    public void start() {
        teleop.start();
        limelightSubsystem.start();
    }

    @Override
    public void loop() {
        teleop.update();

        limelightSubsystem.update();

        //checks for red
        if (limelightSubsystem.state == LimelightSubsystem.limelightState.red) {
            telemetry.addData("Color Detection", "Red detected");
        } else {
            telemetry.addData("Color Detection", "No red detected");
        }

        telemetry.update();
    }
} */
