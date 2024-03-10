package mov.naspen.naspanopticam.helpers.follow;

import mov.naspen.naspanopticam.helpers.target.LocationTarget;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Random;

import static mov.naspen.naspanopticam.NasPanoptiCam.configHelper;
import static mov.naspen.naspanopticam.NasPanoptiCam.plugin;

public class LocationFollower {
    private final FollowerWatcher followerWatcher;
    private LocationTarget locationTarget;
    private BukkitTask locationFollowerTask;
    private int tick = 0;
    private int maxTick;
    final int minTick = configHelper.getMaxTimePerLocationInTicks() / 2;

    public LocationFollower(FollowerWatcher followerWatcher) {
        this.followerWatcher = followerWatcher;
    }

    private void startFollowing() {
        //Start following the player
        locationFollowerTask = new BukkitRunnable(){
            @Override
            public void run() {
                Location loc = locationTarget.getLocationAroundCircle(tick++);
                followerWatcher.getThisPlayerFollows().setVelocity(new Vector(1, 0, 0));
                followerWatcher.getThisPlayerFollows().teleport(loc);
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public boolean isFollowing() {
        if(locationFollowerTask == null){
            return false;
        } else if(locationFollowerTask.isCancelled()){
            return false;
        }
        return true;
    }

    public void stopFollowing() {
        this.locationFollowerTask.cancel();
        tick = 0;
    }

    public boolean tick() {
        return this.tick > this.maxTick;
    }

    public void followLocation(LocationTarget locationTarget) {
        this.locationTarget = locationTarget;
        maxTick = (new Random().nextInt(configHelper.getMaxTimePerLocationInTicks() - minTick) + minTick);
        this.startFollowing();
    }

}

