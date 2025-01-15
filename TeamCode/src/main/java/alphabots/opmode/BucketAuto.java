package alphabots.opmode;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import alphabots.config.runmodes.Auto;
import alphabots.pedroPathing.constants.FConstants;
import alphabots.pedroPathing.constants.LConstants;

@Autonomous(name = "Auto Bucket", group = "Examples")
public class BucketAuto extends OpMode {

    private Follower follower;
    public Auto auto;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private final Pose startPose = new Pose(7.75, 105.75, Math.toRadians(180));

    private final Pose score1Pose = new Pose(18, 121, Math.toRadians(325));

    private final Pose score2Pose = new Pose(18, 121, Math.toRadians(325));
    private final Pose score3Pose = new Pose(18, 121, Math.toRadians(325));
    private final Pose score4Pose = new Pose(18, 121, Math.toRadians(325));


    private final Pose pickup1Pose = new Pose(20.67, 122.2, Math.toRadians(350));

    private final Pose pickup2Pose = new Pose(19.6, 123.5, Math.toRadians(6.4));

    private final Pose pickup3Pose = new Pose(24.7, 121.5, Math.toRadians(45));

    private final Pose parkPose = new Pose(63, 95, Math.toRadians(90));

    private final Pose parkControlPose = new Pose(70, 110, Math.toRadians(90));

    private Path scorePreload, park;
    private PathChain grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3;

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(score1Pose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), score1Pose.getHeading());

        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(score1Pose), new Point(pickup1Pose)))
                .setLinearHeadingInterpolation(score1Pose.getHeading(), pickup1Pose.getHeading())
                .build();

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(score2Pose)))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), score2Pose.getHeading())
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(score2Pose), new Point(pickup2Pose)))
                .setLinearHeadingInterpolation(score2Pose.getHeading(), pickup2Pose.getHeading())
                .build();

        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup2Pose), new Point(score3Pose)))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), score3Pose.getHeading())
                .build();

        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(score3Pose), new Point(pickup3Pose)))
                .setLinearHeadingInterpolation(score3Pose.getHeading(), pickup3Pose.getHeading())
                .build();

        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup3Pose), new Point(score4Pose)))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), score4Pose.getHeading())
                .build();

        park = new Path(new BezierCurve(new Point(score4Pose), new Point(parkControlPose), new Point(parkPose)));
        park.setLinearHeadingInterpolation(score4Pose.getHeading(), parkPose.getHeading());
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                auto.startTransfer();
                follower.setMaxPower(0.6);
                follower.followPath(scorePreload);
                setPathState(50);
                break;
            case 50:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.startBucket();
                    setPathState(1);
                }
                break;
            case 1:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    follower.setMaxPower(0.6);
                    auto.startIntake();
                    follower.followPath(grabPickup1,true);
                    setPathState(150);
                }
                break;
            case 150:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.startTransfer();
                    setPathState(2);
                }
                break;
            case 2:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    follower.setMaxPower(0.6);
                    follower.followPath(scorePickup1,true);
                    setPathState(200);
                }
                break;
            case 200:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.startBucket();
                    setPathState(3);
                }
                break;
            case 3:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    follower.setMaxPower(0.7);
                    auto.startIntake();
                    follower.followPath(grabPickup2,true);
                    setPathState(250);
                }
                break;
            case 250:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.startTransfer();
                    setPathState(4);
                }
                break;
            case 4:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    follower.setMaxPower(0.6);
                    follower.followPath(scorePickup2,true);
                    setPathState(300);
                }
                break;
            case 300:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.startBucket();
                    setPathState(5);
                }
                break;
            case 5:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    follower.setMaxPower(0.7);
                    auto.startIntake();
                    follower.followPath(grabPickup3,true);
                    setPathState(350);
                }
                break;
            case 350:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.startTransfer();
                    setPathState(6);
                }
                break;
            case 6:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    follower.setMaxPower(0.6);
                    follower.followPath(scorePickup3, true);
                    setPathState(400);
                }
                break;
            case 400:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.startBucket();
                    auto.extend.toZero();
                    setPathState(7);
                }
                break;
            case 7:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.extend.toZero();
                    auto.intake.transfer();
                    auto.intake.idle();
                    setPathState(8);
                }
                break;
            case 8:
                if(!follower.isBusy() && auto.actionNotBusy() && pathTimer.getElapsedTimeSeconds() > 1) {
                    setPathState(-1);
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {

        follower.update();
        auto.update();
        autonomousPathUpdate();

        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        auto = new Auto(hardwareMap, telemetry);
        buildPaths();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        auto.start();
        setPathState(0);
    }

    @Override
    public void stop() {
        auto.stop();
    }
}

