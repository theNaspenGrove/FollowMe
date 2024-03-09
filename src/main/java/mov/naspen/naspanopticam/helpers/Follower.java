//package mov.naspen.naspanopticam.helpers;
//
//import mov.naspen.naspanopticam.helpers.target.LocationTarget;
//import mov.naspen.periderm.helpers.luckPerms.AspenMetaKey;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.scheduler.BukkitTask;
//import org.bukkit.util.Vector;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Random;
//import java.util.stream.Stream;
//
//import static mov.naspen.naspanopticam.NasPanoptiCam.*;
//
//public class Follower {
//
//    public static Player followThisPlayer;
//    private static Player thisPlayerFollows;
//    private static BukkitTask task;
//    public static AspenMetaKey dontFollowMe = new AspenMetaKey("dontFollowMe");
//
//    public static void Follow(){
//        if(task == null || task.isCancelled()){
//            task = new BukkitRunnable() {
//                int tick = 0;
//                int maxTick = 0;
//                int playerTick = 0;
//                final int minTick = configHelper.getMaxTimePerLocationInTicks() / 2;
//                LocationTarget currentLocationTarget = null;
//
//                private void reset(){
//                    followThisPlayer = null;
//                    maxTick = 0;
//                    playerTick = 0;
//                    tick = 0;
//                    currentLocationTarget = null;
//                    dismountThisPlayerFollows();
//                }
//                private boolean notReset(){
//                    return followThisPlayer != null || maxTick != 0 || playerTick != 0 || tick != 0 || currentLocationTarget != null;
//                }
//
//                @Override
//                public void run() {
//                    if(!Bukkit.getServer().getOfflinePlayer(configHelper.getFollowerUUID()).isOnline()){
//                        cancel();
//                    }
//                    thisPlayerFollows = Bukkit.getServer().getPlayer(configHelper.getFollowerUUID());
//                    if(thisPlayerFollows != null){
//                        if(canFollow(Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID()))){
//                            if(followThisPlayer != null && followThisPlayer.isOnline() && followThisPlayer != Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID())) {
//                                followThisPlayer.sendMessage("You are no longer being watched by NasPanoptiCam.");
//                            }
//                            if(followThisPlayer != Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID())){
//                                if(notReset()) reset();
//                                followThisPlayer = Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID());
//                            }
//                            if(isNotAttached(thisPlayerFollows, followThisPlayer)){
//                                Spectate(thisPlayerFollows, followThisPlayer);
//                            }
//                        }else if(canFollow(followThisPlayer) || getValidPlayers().findAny().isPresent()){
//                            if(playerTick == 0 || maxTick == 0 || followThisPlayer == null){
//                                if(notReset()) reset();
//                                Player[] validPlayers = getValidPlayers().toArray(Player[]::new);
//                                int rnd = new Random().nextInt(validPlayers.length);
//                                followThisPlayer = validPlayers[rnd];
//                                if(isNotAttached(thisPlayerFollows, followThisPlayer)){
//                                    Spectate(thisPlayerFollows, followThisPlayer);
//                                    followThisPlayer.sendMessage("You are being watched by NasPanoptiCam!");
//                                    followThisPlayer.sendMessage("You can opt out using /dontfollowme");
//                                }
//                                maxTick = (new Random().nextInt(configHelper.getMaxTimePerLocationInTicks() - minTick) + minTick);
//                            }
//                            ++playerTick;
//                            --maxTick;
//                            if(isNotAttached(thisPlayerFollows, followThisPlayer)){
//                                Spectate(thisPlayerFollows, followThisPlayer);
//                            }
//                        } else {
//                            if(tick == 0 || maxTick == 0){
//                                if(notReset()) reset();
//                                int rnd = new Random().nextInt(configHelper.getLocationTargets().size());
//                                currentLocationTarget = configHelper.getLocationTargets().get(rnd);
//                                maxTick = (new Random().nextInt(configHelper.getMaxTimePerLocationInTicks() - minTick) + minTick);
//                            }
//                            ++tick;
//                            --maxTick;
//                            Location loc = currentLocationTarget.getLocationAroundCircle(tick);
//                            thisPlayerFollows.setVelocity(new Vector(1, 0, 0));
//                            thisPlayerFollows.teleport(loc);
//                        }
//                    }
//                }
//            }.runTaskTimer(plugin, 0, 1);
//        }
//    }
//
//    private static void Spectate(Player thisPlayerFollows, Player followThisPlayer) {
//        dismountThisPlayerFollows();
//        thisPlayerFollows.teleport(followThisPlayer);
//        Bukkit.getScheduler().runTaskLater(plugin, () ->
//                        thisPlayerFollows.setSpectatorTarget(followThisPlayer),
//                4);
//    }
//
//    private static boolean isPlayerAFK(Player player){
//        return configHelper.useEssentials && ess.getUser(player).isAfk();
//    }
//
//    public static void dismountThisPlayerFollows(){
//        if(thisPlayerFollows.getSpectatorTarget() != null){
//            thisPlayerFollows.setSpectatorTarget(null);
//        }
//    }
//
//    private static Stream<Player> getValidPlayers(){
//        Player[] ps = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
//        Collection<Player> invalidPlayers = Arrays.asList(Bukkit.getServer().getPlayer(configHelper.getFollowerUUID()), Bukkit.getServer().getPlayer(configHelper.getFollowThisUUID()));
//        return Arrays.stream(ps).filter(p -> !invalidPlayers.contains(p) && canFollow(p));
//    }
//
//    private static boolean isNotAttached(Player spectator, Player target){
//        return spectator.getSpectatorTarget() != target || spectator.getLocation().getWorld() != target.getLocation().getWorld() || spectator.getLocation().distance(target.getLocation()) > configHelper.getReAttachRadius();
//    }
//
//    private static boolean canFollow(Player player){
//        return player != null && player.isOnline() && !isPlayerAFK(player) && metaHelper.getMetaValue(player, dontFollowMe) == null;
//    }
//}
