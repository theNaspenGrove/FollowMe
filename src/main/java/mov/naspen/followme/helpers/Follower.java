package mov.naspen.followme.helpers;

import io.papermc.paper.entity.LookAnchor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import static mov.naspen.followme.FollowMe.configHelper;
import static mov.naspen.followme.FollowMe.plugin;

public class Follower {

    private static BukkitTask task;

    public static void Follow(){
        if(task == null || task.isCancelled()){
            task = new BukkitRunnable() {
                int tick = 0;
                final Location c = configHelper.getFallbackFollowThisLocation();

                @Override
                public void run() {
                    if(!Bukkit.getServer().getOfflinePlayer(configHelper.getFollowerUUID()).isOnline()){
                        cancel();
                    }
                    Player thisPlayerFollows = Bukkit.getServer().getPlayer(configHelper.getFollowerUUID());
                    if(thisPlayerFollows != null){
                        if(Bukkit.getServer().getOfflinePlayer(configHelper.getFollowThisUUID()).isOnline()){
                            tick = 0;
                            Player followThisPlayer = Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID());
                            if(followThisPlayer != null && followThisPlayer.isOnline()){
                                if(thisPlayerFollows.getSpectatorTarget() != followThisPlayer){
                                    thisPlayerFollows.teleport(followThisPlayer);
                                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                                            thisPlayerFollows.setSpectatorTarget(followThisPlayer),
                                            4);
                                }else if(thisPlayerFollows.getLocation().getWorld() != followThisPlayer.getLocation().getWorld() ||
                                        thisPlayerFollows.getLocation().distance(followThisPlayer.getLocation()) > 1){
                                    thisPlayerFollows.setSpectatorTarget(null);
                                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                                                    thisPlayerFollows.setSpectatorTarget(followThisPlayer),
                                            4);
                                }
                            }

                        }else{
                            ++tick;

                            Location loc = getLocationAroundCircle(c, configHelper.getCenterFollowRadius(), configHelper.getCenterFollowRadPerTick() * tick, configHelper.getCenterHeightOffset());
                            thisPlayerFollows.setVelocity(new Vector(1, 0, 0));
                            thisPlayerFollows.teleport(loc);
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }
    public static Location getLocationAroundCircle(Location center, double radius, double angleInRadian, double yOffSet) {
        double x = center.getX() + radius * Math.cos(angleInRadian);
        double z = center.getZ() + radius * Math.sin(angleInRadian);
        double y = center.getY() + yOffSet;

        Location loc = new Location(center.getWorld(), x, y, z);
        loc.setDirection(center.toVector().subtract(loc.toVector()).normalize());

        return loc;
    }
}
