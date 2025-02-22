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
    private final Pose startPose = new Pose(7.75, 112.696, Math.toRadians(270));

    private final Pose score1Pose = new Pose(17.5, 127, Math.toRadians(322));

    private final Pose score2Pose = new Pose(17.5, 127, Math.toRadians(322));
    private final Pose score3Pose = new Pose(17.5, 127, Math.toRadians(322));
    private final Pose score4Pose = new Pose(17.5, 127, Math.toRadians(322));
    private final Pose score5Pose = new Pose(17.5, 127, Math.toRadians(322));


    private final Pose pickup1Pose = new Pose(25.5, 120.2, Math.toRadians(0));

    private final Pose pickup2Pose = new Pose(25.25, 128.7, Math.toRadians(0));

    private final Pose pickup3Pose = new Pose(32.5, 126, Math.toRadians(35));

    private final Pose pickup4Pose = new Pose(68, 95, Math.toRadians(270));
    private final Pose pickup4ControlPose = new Pose(57, 135);

    private final Pose parkPose = new Pose(63, 95, Math.toRadians(90));

    private final Pose parkControlPose = new Pose(50, 145, Math.toRadians(90));

    private Path scorePreload, park;
    private PathChain grabPickup1, grabPickup2, grabPickup3, grabPickup4, scorePickup1, scorePickup2, scorePickup3, scorePickup4;

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

        grabPickup4 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score4Pose), new Point(pickup4ControlPose), new Point(pickup4Pose)))
                .setLinearHeadingInterpolation(score4Pose.getHeading(), pickup4Pose.getHeading())
                .build();

        scorePickup4 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup4Pose), new Point(score5Pose)))
                .setLinearHeadingInterpolation(pickup4Pose.getHeading(), score5Pose.getHeading())
                .build();

        park = new Path(new BezierCurve(new Point(score4Pose), new Point(parkControlPose), new Point(parkPose)));
        park.setLinearHeadingInterpolation(score4Pose.getHeading(), parkPose.getHeading());
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                auto.startTransfer();
                follower.followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startBucket();
                    setPathState(2);
                }
                break;
            case 2:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(grabPickup1,true);
                    setPathState(3);
                }
                break;
            case 3:
                if(follower.getCurrentTValue() > 0.99 && auto.actionNotBusy()) {
                    auto.startIntake();
                    setPathState(4);
                }
                break;
            case 4:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startTransfer();
                    setPathState(5);
                }
                break;
            case 5:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(scorePickup1,true);
                    setPathState(6);
                }
                break;
            case 6:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startBucket();
                    setPathState(7);
                }
                break;
            case 7:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(grabPickup2,true);
                    setPathState(8);
                }
                break;
            case 8:
                if(follower.getCurrentTValue() > 0.99 && auto.actionNotBusy()) {
                    auto.startIntake();
                    setPathState(9);
                }
                break;
            case 9:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startTransfer();
                    setPathState(10);
                }
                break;
            case 10:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(scorePickup2,true);
                    setPathState(11);
                }
                break;
            case 11:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startBucket();
                    setPathState(12);
                }
                break;
            case 12:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(grabPickup3,true);
                    setPathState(13);
                }
                break;
            case 13:
                if(follower.getCurrentTValue() > 0.99 && auto.actionNotBusy()) {
                    auto.startIntake();
                    setPathState(14);
                }
                break;
            case 14:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startTransfer();
                    setPathState(15);
                }
                break;
            case 15:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(scorePickup3, true);
                    setPathState(16);
                }
                break;
            case 16:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startBucket();
                    setPathState(17);
                }
                break;
            case 17:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.intake.swipeOpen();
                    follower.followPath(grabPickup4,true);
                    auto.intake.transfer();
                    setPathState(18);
                }
                break;
            case 18:
                if(follower.getCurrentTValue() > 0.65) {
                    auto.intake.swipeClose();
                    setPathState(19);
                }
                break;
            case 19:
                if(follower.getCurrentTValue() > 0.99 && auto.actionNotBusy()) {
                    auto.startSubPickUp();
                    setPathState(20);
                }
                break;
            case 20:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.intake.swipeClose();
                    auto.startTransfer();
                    setPathState(21);
                }
                break;
            case 21:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(scorePickup3, true);
                    setPathState(22);
                }
                break;
            case 22:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startBucket();
                    setPathState(23);
                }
                break;
            case 23:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.extend.toZero();
                    auto.intake.transfer();
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

