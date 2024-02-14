package mov.naspen.followme.events;

import mov.naspen.followme.helpers.Follower;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static mov.naspen.followme.FollowMe.*;
import static mov.naspen.followme.helpers.Follower.Follow;

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
