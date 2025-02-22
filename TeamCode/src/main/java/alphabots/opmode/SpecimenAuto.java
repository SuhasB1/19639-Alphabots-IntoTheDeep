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

@Autonomous(name = "Auto Specimen", group = "Examples")
public class SpecimenAuto extends OpMode {

    private Follower follower;
    public Auto auto;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    private final Pose startPose = new Pose(7.75, 79.696, Math.toRadians(0));
    private final Pose score1Pose = new Pose(42.24250325945241, 70.21642764015645, Math.toRadians(0));
    private final Pose control1Pose = new Pose(12, 20, Math.toRadians(0));
    private final Pose control2Pose = new Pose(43.36, 50, Math.toRadians(0));
    private final Pose prePush1Pose = new Pose(64.58409387222946, 26.09647979139504, Math.toRadians(0));
    private final Pose control3Pose = new Pose(62.706649282920466, 20.65189048239896, Math.toRadians(0));
    private final Pose push1Pose = new Pose(18.398956975228163, 23.468057366362455, Math.toRadians(0));
    private final Pose control4Pose = new Pose(50.127770534550194, 26.471968709256846, Math.toRadians(0));
    private final Pose control5Pose = new Pose(110.20599739243808, 13.142112125162967, Math.toRadians(0));
    private final Pose push2Pose = new Pose(12.578878748370272, 10.889178617992174, Math.toRadians(0));
    private final Pose control6Pose = new Pose(119.02998696219035, 14.644067796610162, Math.toRadians(0));
    private final Pose control7Pose = new Pose(44.49543676662321, 5.6323337679269905, Math.toRadians(0));
    private final Pose push3Pose = new Pose(18.58670143415906, 9.011734028683188, Math.toRadians(0));
    private final Pose control8Pose = new Pose(3.9426336375488917, 79.60365058670143, Math.toRadians(0));
    private final Pose score2Pose = new Pose(42.05475880052151, 66.83702737940025, Math.toRadians(0));






    private Path scorePreload, park;
    private PathChain prePush1, push1, push2, push3, score2;

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(score1Pose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), score1Pose.getHeading());

        prePush1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score1Pose), new Point(control1Pose), new Point(control2Pose), new Point(prePush1Pose)))
                .setLinearHeadingInterpolation(score1Pose.getHeading(), prePush1Pose.getHeading())
                .build();
        push1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(prePush1Pose), new Point(control3Pose), new Point(push1Pose)))
                .setLinearHeadingInterpolation(prePush1Pose.getHeading(), push1Pose.getHeading())
                .build();
        push2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(push1Pose), new Point(control4Pose), new Point(control5Pose), new Point(push2Pose)))
                .setLinearHeadingInterpolation(push1Pose.getHeading(), push2Pose.getHeading())
                .build();
        push3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(push2Pose), new Point(control6Pose), new Point(control7Pose), new Point(push3Pose)))
                .setLinearHeadingInterpolation(push2Pose.getHeading(), push3Pose.getHeading())
                .build();
        score2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(push3Pose), new Point(control8Pose), new Point(score2Pose)))
                .setLinearHeadingInterpolation(push3Pose.getHeading(), score2Pose.getHeading())
                .build();



        park = new Path(new BezierCurve(new Point(prePush1Pose), new Point(control3Pose), new Point(push1Pose)));
        park.setLinearHeadingInterpolation(prePush1Pose.getHeading(), push1Pose.getHeading());
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                auto.startSpecimenScore();
                follower.followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startSpecimenGrab();
                    setPathState(2);
                }
                break;
            case 2:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(prePush1,true);
                    setPathState(3);
                }
                break;
            case 3:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(push1,true);
                    setPathState(4);
                }
                break;
            case 4:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(push2,true);
                    setPathState(5);
                }
                break;
            case 5:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(push3,true);
                    setPathState(6);
                }
                break;
            case 6:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startSpecimenGrab();
                    setPathState(7);
                }
                break;
            case 7:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startSpecimenScore();
                    setPathState(8);
                }
                break;
            case 8:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    follower.followPath(score2,true);
                    setPathState(9);
                }
                break;
            case 9:
                if(follower.getCurrentTValue() > 0.95 && auto.actionNotBusy()) {
                    auto.startSpecimenGrab();
                    setPathState(100);
                }
                break;

            case 100:
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

