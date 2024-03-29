package mov.naspen.naspanopticam.helpers.follow;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Stream;

import static mov.naspen.naspanopticam.NasPanoptiCam.*;
import static mov.naspen.naspanopticam.helpers.command.CommandHandler.dontFollowMe;

public class FollowerWatcher {
    private Player thisPlayerFollows;
    private BukkitTask task;
    private final PlayerFollower playerFollower;
    private final LocationFollower locationFollower;
    public FollowerWatcher(){
        playerFollower = new PlayerFollower(this);
        locationFollower = new LocationFollower(this);
        watch();
    }
    public void watch(){
        if(task == null || task.isCancelled()){
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    if(!Bukkit.getServer().getOfflinePlayer(configHelper.getFollowerUUID()).isOnline()){
                        cancel();
                    }
                    tick();

                }
            }.runTaskTimer(plugin, 0, 10);
        }
    }
    public PlayerFollower getPlayerFollower() {
        return playerFollower;
    }
    public LocationFollower getLocationFollower() {
        return locationFollower;
    }
    public void stopWatching(){
        if(task != null && !task.isCancelled()){
            task.cancel();
        }
    }
    private void tick() {
        thisPlayerFollows = Bukkit.getServer().getPlayer(configHelper.getFollowerUUID());
        //if follower is online
        if (isPlayerFollowerOnline()) {
            //if can follow primary target
            if (canFollow(Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID()))) {
                Player p = Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID());
                playerFollower.followPlayer(p);
            } else if (getValidPlayers().findAny().isPresent()) {
                //if not check for players
                Player[] validPlayers = getValidPlayers().toArray(Player[]::new);
                int rnd = new Random().nextInt(validPlayers.length);
                playerFollower.followPlayer(validPlayers[rnd]);
            }else{
                if(locationFollower.isFollowing()){
                    if(locationFollower.tick()){
                        locationFollower.stopFollowing();
                    }
                }else{
                    int rnd = new Random().nextInt(configHelper.getLocationTargets().size());
                    locationFollower.followLocation(configHelper.getLocationTargets().get(rnd));
                }
            }
        }
    }
    public boolean isPlayerFollowerOnline(){
        return thisPlayerFollows != null && thisPlayerFollows.isOnline();
    }
    public static boolean isActive(Player player) {
        return player != null && player.isOnline() && (configHelper.useEssentials && !(ess.getUser(player).isAfk()));
    }
    private boolean canFollow(Player player){
        return isActive(player) && metaHelper.getMetaValue(player, dontFollowMe) == null;
    }
    public Player getThisPlayerFollows() {
        return thisPlayerFollows;
    }
    Stream<Player> getValidPlayers(){
        Player[] ps = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
        Collection<Player> invalidPlayers = Arrays.asList(Bukkit.getServer().getPlayer(configHelper.getFollowerUUID()), Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID()));
        return Arrays.stream(ps).filter(p -> !invalidPlayers.contains(p) && canFollow(p));
    }
    public void sendPrivateMessage(Player player, String s){
        //wait 8 ticks and check if the player is still online, then send the messages
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(player != null && player.isOnline()){
                thisPlayerFollows.chat("/msg " + player.getName() + " " + s);
            }
        }, 8);

    }
}
