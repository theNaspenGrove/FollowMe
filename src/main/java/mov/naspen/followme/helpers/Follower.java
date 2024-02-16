package mov.naspen.followme.helpers;

import io.papermc.paper.entity.LookAnchor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Objects;

import static mov.naspen.followme.FollowMe.*;

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
                        if(canFollow(Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID()))){
                            Player followThisPlayer = Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID());
                            tick = 0;
                            if(isAttached(thisPlayerFollows, followThisPlayer)){
                                Spectate(thisPlayerFollows, followThisPlayer);
                            }
                        }else{
                            if(tick == 0){
                                thisPlayerFollows.setSpectatorTarget(null);
                            }
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

    private static void Spectate(Player thisPlayerFollows, Player followThisPlayer) {
        thisPlayerFollows.setSpectatorTarget(null);
        thisPlayerFollows.teleport(followThisPlayer);
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                        thisPlayerFollows.setSpectatorTarget(followThisPlayer),
                4);
    }

    private static boolean isPlayerAFK(Player player){
        return configHelper.useEssentials && ess.getUser(player).isAfk();
    }

    private static boolean isAttached(Player spectator, Player target){
        return spectator.getSpectatorTarget() != target || spectator.getLocation().getWorld() != target.getLocation().getWorld() || spectator.getLocation().distance(target.getLocation()) > 3;
    }

    private static boolean canFollow(Player player){
        return player != null && player.isOnline() && !isPlayerAFK(player) && player.getGameMode() == GameMode.SURVIVAL;
    }
}
