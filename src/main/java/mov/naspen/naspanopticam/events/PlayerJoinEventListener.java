package mov.naspen.naspanopticam.events;

import mov.naspen.naspanopticam.helpers.follow.FollowerWatcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static mov.naspen.naspanopticam.NasPanoptiCam.*;

public class PlayerJoinEventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(event.getPlayer().getUniqueId().equals(configHelper.getFollowerUUID())){
            logHelper.sendLogInfo("Player " + event.getPlayer().getName() + " joined, starting to follow");
            followerWatcher.watch();
        }
    }

}
