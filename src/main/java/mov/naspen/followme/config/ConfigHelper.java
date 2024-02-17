package mov.naspen.followme.config;

import mov.naspen.followme.helpers.LocationTarget;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static mov.naspen.followme.FollowMe.logHelper;
import static mov.naspen.followme.FollowMe.plugin;

public class ConfigHelper {
    private final UUID followThisUUID;
    private final UUID followerUUID;
    private List<LocationTarget> locationTargets;
    public boolean useEssentials = false;
    private int maxTimePerLocationInTicks;
    private int reAttachRadius = 3;

    public ConfigHelper(FileConfiguration config){
        followThisUUID = UUID.fromString(config.getString("followThisUUID") != null ? config.getString("followThisUUID") : "00000000-0000-0000-0000-000000000000");
        followerUUID = UUID.fromString(config.getString("followerUUID") != null ? config.getString("followerUUID") : "00000000-0000-0000-0000-000000000000");
        maxTimePerLocationInTicks = config.getInt("maxTimePerLocationInSeconds") != 0 ? config.getInt("maxTimePerLocationInSeconds") * 20 : 6000;
        reAttachRadius = config.getInt("reAttachRadius") != 0 ? config.getInt("reAttachRadius") : 5;
        loadLocationTargets();
    }

    public UUID getFollowThisUUID() {
        return followThisUUID;
    }

    public UUID getFollowerUUID() {
        return followerUUID;
    }

    public int getMaxTimePerLocationInTicks() {
        return maxTimePerLocationInTicks;
    }

    public int getReAttachRadius() {
        return reAttachRadius;
    }

    public void loadLocationTargets(){
        if(plugin.getConfig().get("locationTargets") != null){
            //load location targets from the serialized configuration map in the config file
            //loop through the map and add each location target to the locationTargets array
            locationTargets = (List<LocationTarget>) plugin.getConfig().getList("locationTargets");
        }else {
            logHelper.sendLogInfo("No location targets found");
            locationTargets = new ArrayList<>();
            addFallbackLocation(plugin.getServer().getWorlds().get(0).getSpawnLocation(), 40, 20, 0.1, false);
        }
    }

    public List<LocationTarget> getLocationTargets() {
        return locationTargets;
    }

    public void addFallbackLocation(Location location, double radius, int yOffset, double radPerSec, boolean invertedLook){
        locationTargets.add(new LocationTarget(location, radius, yOffset, radPerSec, invertedLook));
        plugin.getConfig().set("locationTargets", locationTargets);
        plugin.saveConfig();
    }

}
