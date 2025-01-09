package alphabots.opmode;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import alphabots.pedroPathing.constants.FConstants;
import alphabots.pedroPathing.constants.LConstants;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "barontest2")
public class barontest2 extends OpMode {
    MultipleTelemetry telemetrya = new MultipleTelemetry(telemetry);
    Follower f;
    @Override
    public void init() {
        Constants.setConstants(FConstants.class, LConstants.class);
        telemetrya.addData("Follower Constants Localizer", FollowerConstants.localizers);
        telemetrya.addData("drive coeff", FollowerConstants.drivePIDFCoefficients);
        telemetrya.update();
    }
    @Override
    public void start() {
        f = new Follower(hardwareMap);
    }
    @Override
    public void loop() {
        telemetrya.addData("Follower Constants Localizer", FollowerConstants.localizers);
        telemetrya.addData("drive coeff", FollowerConstants.drivePIDFCoefficients);
        telemetrya.update();
    }
}