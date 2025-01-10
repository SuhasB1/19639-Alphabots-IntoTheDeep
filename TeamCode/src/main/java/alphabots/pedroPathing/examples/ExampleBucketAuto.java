package alphabots.pedroPathing.examples;

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

import alphabots.pedroPathing.constants.FConstants;
import alphabots.pedroPathing.constants.LConstants;

/**
 * This is an example auto that showcases movement and control of two servos autonomously.
 * It is a 0+4 (Specimen + Sample) bucket auto. It scores a neutral preload and then pickups 3 samples from the ground and scores them before parking.
 * There are examples of different ways to build paths.
 * A path progression method has been created and can advance based on time, position, or other factors.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 2.0, 11/28/2024
 */

@Autonomous(name = "Example Auto Blue", group = "Examples")
public class ExampleBucketAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private Timer wait = new Timer(), wiperTimer = new Timer(), sampleTimer = new Timer(), resetTimer = new Timer();
    /** This is the variable where we store the state of our auto.
     * It is used by the pathUpdate method. */
    private int pathState;

    /** Start Pose of our robot */
    private final Pose startPose = new Pose(13, 115, Math.toRadians(270));

    /** Scoring Pose of our robot. */
    private final Pose scorePose = new Pose(23.5, 126.5, Math.toRadians(315));
    private final Pose scorePose2 = new Pose(26.5, 129.5, Math.toRadians(315));

    /** Lowest (First) Sample from the Spike Mark */
    private final Pose pickup1Pose = new Pose(27.5, 128.5, Math.toRadians(0));

    /** Middle (Second) Sample from the Spike Mark */
    private final Pose pickup2Pose = new Pose(27.5, 134, Math.toRadians(0));

    /** Highest (Third) Sample from the Spike Mark */
    private final Pose pickup3Pose = new Pose(49, 135, Math.toRadians(0));

    /** Park Pose for our robot, after we do all of the scoring. */
    private final Pose parkPose = new Pose(60, 98, Math.toRadians(90));

    /** Park Control Pose for our robot */
    private final Pose parkControlPose = new Pose(60, 98, Math.toRadians(90));

    private Path scorePreload, park;
    private PathChain grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3;

    public void buildPaths() {
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup1Pose)))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        scorePickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup2Pose)))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        scorePickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup2Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .build();

        scorePickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();

        park = new Path(new BezierCurve(new Point(scorePose), new Point(parkControlPose), new Point(parkPose)));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading());
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (follower.getPose().getX() > (startPose.getX() - 1) && follower.getPose().getY() > (startPose.getY() - 1)) {
                    if (wait.getElapsedTime() >= 2) {  // 2-second wait
                        follower.followPath(scorePreload);
                        setPathState(2);
                    }
                }
                break;
            case 1:
                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
                    if (wait.getElapsedTime() >= 2) {  // 2-second wait
                        follower.followPath(grabPickup1);
                        telemetry.addData("case 1?", true);
                        setPathState(2);
                    }
                }
                break;
            case 2:
                if (follower.getPose().getX() > (pickup1Pose.getX() - 1) && follower.getPose().getY() > (pickup1Pose.getY() - 1)) {
                    if (wait.getElapsedTime() >= 2) {  // 2-second wait
                        follower.followPath(scorePickup1);
                        telemetry.addData("case 2?", true);
                        setPathState(3);
                    }
                }
                break;
            case 3:
                if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
                    follower.followPath(grabPickup2);
                    setPathState(4);
                }

                break;
            // Commented out cases for future use:
             case 4:
                 if (follower.getPose().getX() > (pickup2Pose.getX() - 1) && follower.getPose().getY() > (pickup2Pose.getY() - 1)) {
                     follower.followPath(scorePickup2);
                     setPathState(5);
                 }
                 break;
            // case 5:
            //     if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
            //         follower.followPath(grabPickup3);
            //         setPathState(6);
            //     }
            //     break;
            // case 6:
            //     if (follower.getPose().getX() > (pickup3Pose.getX() - 1) && follower.getPose().getY() > (pickup3Pose.getY() - 1)) {
            //         follower.followPath(scorePickup3);
            //         setPathState(7);
            //     }
            //     break;
            // case 7:
            //     if (follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
            //         follower.followPath(park);
            //         setPathState(8);
            //     }
            //     break;
            // case 8:
            //     if (follower.getPose().getX() > (parkPose.getX() - 1) && follower.getPose().getY() > (parkPose.getY() - 1)) {
            //         setPathState(-1);
            //     }
            //     break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {
        follower.update();
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
        buildPaths();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {}
}
