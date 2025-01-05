package alphabots.config.util;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.localization.Pose;

/**
 * Simple static field serving as a storage medium for the bot's pose.
 * This allows different classes/opmodes to set and read from a central source of truth.
 * A static field allows data to persist between opmodes.
 */
@Config
public class VariableStorage {

    public static Pose currentPose = new Pose(0,0,Math.toRadians(-90));

    public static int liftPos = 0;
    public static int extendPos = 0;
    public static Team currentTeam = Team.RED;
    public enum Team {
        BLUE, RED
    }
}
