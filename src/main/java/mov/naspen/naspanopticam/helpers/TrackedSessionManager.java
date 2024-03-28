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
    final static int minSessionTime = 120;
    private static final String fileName = plugin.getDataFolder().getAbsoluteFile() + File.separator + "NasPanoptiCamSessions.txt";
    public static boolean saveSession(PlayerTargetSession session){
        boolean success = true;
        if(session.getTimeFollowed() > minSessionTime){
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter(fileName, true));
                writer.write(session.toString());
                writer.newLine();
                writer.flush();
                writer.close();
            } catch (IOException e) {
                success = false;
                e.printStackTrace();
            }
        }
        return success;
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
                if(session.getTimeFollowed() > minSessionTime){
                    writer.write(session.toString());
                    writer.newLine();
                    followerWatcher.getPlayerFollower().getPlayerTarget().setStartedFollowTime(now);
                }
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
