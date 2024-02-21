package mov.naspen.naspanopticam.helpers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Random;

import static mov.naspen.naspanopticam.NasPanoptiCam.*;

public class Follower {

    private static BukkitTask task;

    public static void Follow(){
        if(task == null || task.isCancelled()){
            task = new BukkitRunnable() {
                int tick = 0;
                int maxTick = 0;
                LocationTarget currentLocationTarget;

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
                            maxTick = 0;
                            if(isAttached(thisPlayerFollows, followThisPlayer)){
                                Spectate(thisPlayerFollows, followThisPlayer);
                            }
                        }else{
                            if(tick == 0 || maxTick == 0){
                                thisPlayerFollows.setSpectatorTarget(null);
                                int rnd = new Random().nextInt(configHelper.getLocationTargets().size());
                                currentLocationTarget = configHelper.getLocationTargets().get(rnd);
                                maxTick = (new Random().nextInt(configHelper.getMaxTimePerLocationInTicks() - 1200) + 1200);
                                tick = 0;
                            }
                            ++tick;
                            --maxTick;
                            Location loc = currentLocationTarget.getLocationAroundCircle(tick);
                            thisPlayerFollows.setVelocity(new Vector(1, 0, 0));
                            thisPlayerFollows.teleport(loc);
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
        }
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
        return spectator.getSpectatorTarget() != target || spectator.getLocation().getWorld() != target.getLocation().getWorld() || spectator.getLocation().distance(target.getLocation()) > configHelper.getReAttachRadius();
    }

    private static boolean canFollow(Player player){
        return player != null && player.isOnline() && !isPlayerAFK(player) && player.getGameMode() == GameMode.SURVIVAL;
    }
}
