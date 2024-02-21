package mov.naspen.naspanopticam.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static mov.naspen.naspanopticam.NasPanoptiCam.*;
import static mov.naspen.naspanopticam.helpers.Follower.Follow;

public class PlayerJoinEventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(event.getPlayer().getUniqueId().equals(configHelper.getFollowThisUUID()) || event.getPlayer().getUniqueId().equals(configHelper.getFollowerUUID())){
            logHelper.sendLogInfo("Player " + event.getPlayer().getName() + " joined, starting to follow");
            logHelper.sendLogInfo(event.getPlayer().getLocation().toString());
            Follow();
        }
    }

}
