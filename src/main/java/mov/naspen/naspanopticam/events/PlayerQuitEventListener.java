package mov.naspen.naspanopticam.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static mov.naspen.naspanopticam.helpers.Follower.dismountThisPlayerFollows;
import static mov.naspen.naspanopticam.helpers.Follower.followThisPlayer;

public class PlayerQuitEventListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(event.getPlayer() == followThisPlayer){
            followThisPlayer = null;
            dismountThisPlayerFollows();
        }
    }
}
