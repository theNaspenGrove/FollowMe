package mov.naspen.naspanopticam.helpers.follow;

import mov.naspen.naspanopticam.helpers.target.PlayerTarget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

import static mov.naspen.naspanopticam.NasPanoptiCam.*;

public class PlayerFollower {

    FollowerWatcher followerWatcher;
    PlayerTarget playerTarget;
    final int minTick = configHelper.getMaxTimePerLocationInTicks() / 2;
    private BukkitTask playerWatcherTask;
    public PlayerFollower(FollowerWatcher followerWatcher){
        this.followerWatcher = followerWatcher;
    }
    public void followPlayer(Player player){
        if(playerTarget == null || playerTarget.getFollowThisPlayer() != player){
            int maxTick = (new Random().nextInt(configHelper.getMaxTimePerLocationInTicks() - minTick) + minTick);
            playerTarget = new PlayerTarget(player, maxTick);
            if(followerWatcher.getLocationFollower().isFollowing()){
                followerWatcher.getLocationFollower().stopFollowing();
            }
            this.startFollowing();
        }
        if(isNotAttached(followerWatcher.getThisPlayerFollows(), player)){
            logHelper.sendLogInfo("Reattaching to player '" + player.getName() + "' because the player is not attached or too far away.");
            this.spectate(followerWatcher.getThisPlayerFollows(),player);
        }
    }

    private void startFollowing() {
        //Start following the player
        followerWatcher.sendPrivateMessage(getPlayerToFollow(),"I am watching you...");
        followerWatcher.sendPrivateMessage(getPlayerToFollow(),"You can opt out using /dontfollowme");
        logHelper.sendLogInfo("Following player '" + playerTarget.getFollowThisPlayer().getName() + "'.");
        this.spectate(followerWatcher.getThisPlayerFollows(),playerTarget.getFollowThisPlayer());
        this.playerWatcherTask = new BukkitRunnable(){
            @Override
            public void run() {
                //Check if the player is still online
                if(playerTarget.tick()){
                    stopFollowing();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public Player getPlayerToFollow(){
        return playerTarget.getFollowThisPlayer();
    }

    public void stopFollowing(){
        followerWatcher.sendPrivateMessage(getPlayerToFollow(),"I am no longer watching you.");
        logHelper.sendLogInfo("Stopped following player '" + playerTarget.getFollowThisPlayer().getName() + "'.");
        this.dismount(followerWatcher.getThisPlayerFollows());
        playerWatcherTask.cancel();
    }

    public boolean isNotFollowing(){
        return playerWatcherTask == null || playerWatcherTask.isCancelled();
    }

    public boolean isFollowingPlayer(Player player){
        return playerTarget != null && playerTarget.getFollowThisPlayer() == player;
    }

    public void spectate(Player thisPlayerFollows, Player targetPlayer) {
        this.dismount(followerWatcher.getThisPlayerFollows());
        thisPlayerFollows.teleport(targetPlayer);
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                        thisPlayerFollows.setSpectatorTarget(targetPlayer),
                4);
    }

    public void dismount(Player thisPlayerFollows){
        if(thisPlayerFollows.getSpectatorTarget() != null){
            thisPlayerFollows.setSpectatorTarget(null);
        }
    }

    private boolean isNotAttached(Player spectator, Player target){
        return spectator.getSpectatorTarget() != target || spectator.getLocation().getWorld() != target.getLocation().getWorld() || spectator.getLocation().distance(target.getLocation()) > configHelper.getReAttachRadius();
    }
}
