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
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import alphabots.config.runmodes.Auto;
import alphabots.pedroPathing.constants.FConstants;
import alphabots.pedroPathing.constants.LConstants;

@Autonomous(name = "Example Auto Specimen", group = "Examples")
public class ExampleSpecimenAuto extends OpMode {

    private Follower follower;
    public Auto auto;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    private final Pose startPose = new Pose(7.297, 52.888, Math.toRadians(180));
    private final Pose controlPose1 = new Pose(10.000, 80.000, Math.toRadians(180));
    private final Pose scorePreloadPose = new Pose(30.80346213691068, 66.75072702235357, Math.toRadians(180));
    private final Pose controlPose2 = new Pose(27.000, 25.000, Math.toRadians(0));
    private final Pose pushPose1 = new Pose(75.489, 17.576, Math.toRadians(0));
    private final Pose controlPose3 = new Pose(80.000, 20.000, Math.toRadians(0));
    private final Pose intakePose = new Pose(16.397, 17.137, Math.toRadians(0));
    private final Pose controlPose4 = new Pose(15.000, 40.000, Math.toRadians(0));
    private final Pose controlPose5 = new Pose(40.000, 15.000, Math.toRadians(0));
    private final Pose controlPose6 = new Pose(130.000, 10.000, Math.toRadians(0));
    private final Pose controlPose7 = new Pose(50.000, 10.000, Math.toRadians(0));
    private final Pose pushPose3 = new Pose(78.885, 4.374, Math.toRadians(0));
    private final Pose pushedPose3 = new Pose(16.764, 4.374, Math.toRadians(0));

    //private final Pose scorePose1 = new Pose(40.823, 66.327, Math.toRadians(180));
    //private final Pose scorePose2 = new Pose(40.823, 68.327, Math.toRadians(180));
    //private final Pose scorePose3 = new Pose(40.823, 70.327, Math.toRadians(180));
    //private final Pose scorePose4 = new Pose(40.823, 70.327, Math.toRadians(180));

    private Path scorePreload, park;
    private PathChain prePushSpecimen1, pushSpecimen1, pushSpecimen2, prePushSpecimen3, pushSpecimen3;

    public void buildPaths() {
        scorePreload = new Path(new BezierCurve(new Point(startPose), new Point(controlPose1), new Point(scorePreloadPose))); // Add 4th point if needed
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePreloadPose.getHeading());

        prePushSpecimen1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePreloadPose), new Point(controlPose2), new Point(pushPose1)))
                .setLinearHeadingInterpolation(scorePreloadPose.getHeading(), pushPose1.getHeading())
                .build();
        pushSpecimen1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(pushPose1), new Point(controlPose3), new Point(intakePose)))
                .setLinearHeadingInterpolation(pushPose1.getHeading(), intakePose.getHeading())
                .build();
        pushSpecimen2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(intakePose), new Point(controlPose4), new Point(controlPose5), new Point(controlPose6), new Point(controlPose7), new Point(intakePose)))
                .setLinearHeadingInterpolation(intakePose.getHeading(), intakePose.getHeading())
                .build();
        prePushSpecimen3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(intakePose), new Point(pushPose3)))
                .setLinearHeadingInterpolation(intakePose.getHeading(), pushPose3.getHeading())
                .build();
        pushSpecimen3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pushPose3), new Point(pushedPose3)))
                .setLinearHeadingInterpolation(pushPose3.getHeading(), pushedPose3.getHeading())
                .build();
        park = new Path(new BezierLine(new Point(pushedPose3), new Point(intakePose)));
        park.setLinearHeadingInterpolation(pushedPose3.getHeading(), intakePose.getHeading());

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                auto.startChamberScore();
                follower.setMaxPower(0.8);
                follower.followPath(scorePreload, true);
                setPathState(50);
                break;
            case 50:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.outtake.open();
                    auto.outtake.idle();
                    auto.lift.toZero();
                    setPathState(1);
                }
                break;
            case 1:
                follower.setMaxPower(0.7);
                follower.followPath(prePushSpecimen1,true);
                setPathState(150);
                break;
            case 150:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    setPathState(2);
                }
                break;
            case 2:
                follower.setMaxPower(0.8);
                follower.followPath(pushSpecimen1);
                setPathState(200);
                break;
            case 200:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    setPathState(3);
                }
                break;
            case 3:
                follower.setMaxPower(0.8);
                follower.followPath(pushSpecimen2);
                setPathState(250);
                break;
            case 250:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    setPathState(4);
                }
                break;
            case 4:
                follower.setMaxPower(0.8);
                follower.followPath(prePushSpecimen3);
                setPathState(300);
                break;
            case 300:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    setPathState(5);
                }
                break;
            case 5:
                follower.setMaxPower(0.8);
                follower.followPath(pushSpecimen3);
                setPathState(350);
                break;
            case 350:
                if(!follower.isBusy() && auto.actionNotBusy()) {
                    auto.startSpecimen();
                    setPathState(6);
                }
                break;
            case 6:
                follower.setMaxPower(0.8);
                follower.followPath(park);
                setPathState(7);
            case 7:
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
        auto.startTransfer();
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

