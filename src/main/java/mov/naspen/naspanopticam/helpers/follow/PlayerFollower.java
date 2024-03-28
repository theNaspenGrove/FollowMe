package mov.naspen.naspanopticam.helpers.follow;

import mov.naspen.naspanopticam.helpers.target.PlayerTarget;
import mov.naspen.naspanopticam.helpers.target.PlayerTargetSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.util.Random;

import static mov.naspen.naspanopticam.NasPanoptiCam.*;
import static mov.naspen.naspanopticam.NasPanoptiCam.followerWatcher;
import static mov.naspen.naspanopticam.helpers.TrackedSessionManager.saveSession;
import static mov.naspen.naspanopticam.helpers.follow.FollowerWatcher.isActive;

public class PlayerFollower {
    FollowerWatcher followerWatcher;
    PlayerTarget playerTarget;
    final int minTick = configHelper.getMaxTimePerLocationInTicks() / 2;
    //playerTargetSessions is an array of all the player targets that have been followed
    PlayerTargetSession activePlayerTargetSession;
    private BukkitTask playerWatcherTask;
    public PlayerFollower(FollowerWatcher followerWatcher){
        this.followerWatcher = followerWatcher;
    }
    public void followPlayer(Player playerToFollow){
        //The player target is null when the follower is first created
        //Follow this player not being equal to the player to be followed should be the only condition to start following a new player
        if(playerTarget == null || playerTarget.getFollowThisPlayer() != playerToFollow){
            if(playerTarget != null){
                stopFollowing();
            }
            int maxTick = (new Random().nextInt(configHelper.getMaxTimePerLocationInTicks() - minTick) + minTick);
            //create a new player target with the playerToFollow and the maxTick calculated above
            playerTarget = new PlayerTarget(playerToFollow, maxTick);

            //If the location follower is running, stop it
            if(followerWatcher.getLocationFollower().isFollowing()){
                followerWatcher.getLocationFollower().stopFollowing();
            }
            //Start this player follower
            this.startFollowing();
        }else{
            //If the player follower is already running with the correct player, check for attachment. If the player is not attached, reattach
            if(isNotAttached(followerWatcher.getThisPlayerFollows(), playerToFollow)){
                if(!playerTarget.getPlayerName().isBlank())
                    logHelper.sendLogInfo("Reattaching to player '" + playerToFollow.getName() + "' because the player is not attached or too far away.");
                this.spectate(followerWatcher.getThisPlayerFollows(),playerToFollow);
            }
        }

    }

    private void startFollowing() {
        //Start following the player
        followerWatcher.sendPrivateMessage(playerTarget.getFollowThisPlayer(),"I am watching you...");
        followerWatcher.sendPrivateMessage(playerTarget.getFollowThisPlayer(),"You can opt out using /dontfollowme");
        if(!playerTarget.getPlayerName().isBlank())
            logHelper.sendLogInfo("Following player '" + playerTarget.getPlayerName() + "' for " + playerTarget.getMaxTick() + " at " + playerTarget.getTimeStartedFollowing() + " UTC .");
        this.spectate(followerWatcher.getThisPlayerFollows(),playerTarget.getFollowThisPlayer());
        this.playerWatcherTask = new BukkitRunnable(){
            @Override
            public void run() {
                //Check if the player is still online
                if(playerTarget.tick()){
                    if(playerTarget.getFollowThisPlayer() == Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID())){
                        if(!isActive(playerTarget.getFollowThisPlayer())){
                            stopFollowing();
                        }
                        return;
                    }
                    if(followerWatcher.getValidPlayers().noneMatch(p -> p == playerTarget.getFollowThisPlayer())){
                        stopFollowing();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void stopFollowing(){
        if(!playerTarget.getPlayerName().isBlank()){
            followerWatcher.sendPrivateMessage(playerTarget.getFollowThisPlayer(),"I am no longer watching you.");
            int now = (int) (Instant.now().getEpochSecond());
            int timeFollowing = now - playerTarget.getTimeStartedFollowing();
            logHelper.sendLogInfo("Stopped following player '" + playerTarget.getPlayerName() + "' at " + now + " UTC. Followed for " + timeFollowing + " seconds.");
            saveSession(new PlayerTargetSession(
                    followerWatcher.getPlayerFollower().getPlayerTarget().getPlayerName(),
                    followerWatcher.getPlayerFollower().getPlayerTarget().getTimeStartedFollowing(),
                    now));
        }
        //dismount the current player target
        this.dismount(followerWatcher.getThisPlayerFollows());
        //Set the player target to null and the max time to 0
        //With a null target and a 0 max time, this target will never be followed
        this.playerTarget = new PlayerTarget(null, 0);
        playerWatcherTask.cancel();
    }
    
    public Player getFollowThisPlayer(){
        return playerTarget.getFollowThisPlayer();
    }

    public boolean isFollowingPlayer(){
        return  playerWatcherTask != null && !playerWatcherTask.isCancelled();
    }

    public boolean isFollowingPlayer(Player player){
        return playerTarget != null && playerTarget.getFollowThisPlayer() == player;
    }

    public PlayerTarget getPlayerTarget(){
        return playerTarget;
    }

    public void spectate(Player thisPlayerFollows, Player targetPlayer) {
        this.dismount(followerWatcher.getThisPlayerFollows());
        thisPlayerFollows.teleport(targetPlayer);
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                        thisPlayerFollows.setSpectatorTarget(targetPlayer),
                6);
    }
    public void dismount(Player thisPlayerFollows){
        thisPlayerFollows.setSpectatorTarget(null);
    }

    private boolean isNotAttached(Player spectator, Player target){
        return spectator.getSpectatorTarget() != target || spectator.getLocation().getWorld() != target.getLocation().getWorld() || spectator.getLocation().distance(target.getLocation()) > configHelper.getReAttachRadius();
    }
}
