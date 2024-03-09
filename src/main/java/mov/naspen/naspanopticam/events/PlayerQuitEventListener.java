package mov.naspen.naspanopticam.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static mov.naspen.naspanopticam.NasPanoptiCam.*;

public class PlayerQuitEventListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(followerWatcher.getPlayerFollower().isFollowingPlayer(event.getPlayer())){
            followerWatcher.getPlayerFollower().stopFollowing();
        }
        if(event.getPlayer().getUniqueId().equals(configHelper.getFollowerUUID())){
            logHelper.sendLogInfo("Player " + event.getPlayer().getName() + " left, stopping");
            followerWatcher.stopWatching();
        }
    }
}
