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
    // The minimum time a session must be followed to be saved when dumped in seconds
    public final static int minSessionTime = 120;
    //The file name and path using the plugin's data folder provided by bukkit
    private static final String fileName = plugin.getDataFolder().getAbsoluteFile() + File.separator + "NasPanoptiCamSessions.txt";
    public static void saveSession(PlayerTargetSession session){
        //If the session has been followed for more than the minimum time, save it to the file
        if(session.getTimeFollowed() > minSessionTime){
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter(fileName, true));
                writer.write(session.toString());
                writer.newLine();
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean dumpSessions(){
        //dump the active session to the sessions file and provide the "dumped at" timestamp
        boolean success = false;
        BufferedWriter writer;
        //get the current time in Epoch seconds
        int now = (int) Instant.now().getEpochSecond();
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            //is there an active session
            if(followerWatcher.getPlayerFollower().isFollowingPlayer()){
                PlayerTargetSession session = new PlayerTargetSession(
                        followerWatcher.getPlayerFollower().getPlayerTarget().getPlayerName(),
                        followerWatcher.getPlayerFollower().getPlayerTarget().getTimeStartedFollowing(),
                        now);
                //is the session long enough to be saved
                if(session.getTimeFollowed() > minSessionTime){
                    writer.write(session.toString());
                    writer.newLine();
                }
                //even if the session isn't long enough to be saved, reset the follow time
                followerWatcher.getPlayerFollower().getPlayerTarget().setStartedFollowTime(now);
            }
            //provide the dup time
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
