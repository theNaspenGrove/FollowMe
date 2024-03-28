package mov.naspen.naspanopticam.helpers;

import mov.naspen.naspanopticam.helpers.target.PlayerTargetSession;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import static mov.naspen.naspanopticam.NasPanoptiCam.followerWatcher;
import static mov.naspen.naspanopticam.NasPanoptiCam.plugin;

public class TrackedSessionManager {

    private static final String fileName = plugin.getDataFolder().getAbsoluteFile() + File.separator + "NasPanoptiCamSessions.txt";

    public static boolean saveSession(PlayerTargetSession session){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(session.toString());
            writer.newLine();
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean dumpSessions(){
        boolean success = false;
        BufferedWriter writer;
        int now = (int) Instant.now().getEpochSecond();
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            if(followerWatcher.getPlayerFollower().isFollowingPlayer()){
                PlayerTargetSession session = new PlayerTargetSession(
                        followerWatcher.getPlayerFollower().getPlayerTarget().getPlayerName(),
                        followerWatcher.getPlayerFollower().getPlayerTarget().getTimeStartedFollowing(),
                        now);
                writer.write(session.toString());
                writer.newLine();
                followerWatcher.getPlayerFollower().getPlayerTarget().setStartedFollowTime(now);
            }
            writer.write("Dumped at: " + now);
            writer.newLine();
            writer.flush();
            writer.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
