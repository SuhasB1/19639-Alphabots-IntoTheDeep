package alphabots.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import alphabots.config.subsystem.LimelightSubsystem;
import alphabots.config.util.FieldConstants;
import alphabots.config.util.RobotConstants;
import alphabots.pedroPathing.localization.Pose;

@TeleOp
@Config
public class LimelightTest extends OpMode {

    private LimelightSubsystem limelight;

    @Override
    public void init() {
        limelight = new LimelightSubsystem(hardwareMap, telemetry, FieldConstants.observationStartPose, new Pose(36,72, Math.toRadians(0)));
        limelight.init();
    }

    @Override
    public void loop() {

    }
}
