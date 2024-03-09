package mov.naspen.naspanopticam.config;

import mov.naspen.naspanopticam.helpers.target.LocationTarget;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static mov.naspen.naspanopticam.NasPanoptiCam.logHelper;
import static mov.naspen.naspanopticam.NasPanoptiCam.plugin;

public class ConfigHelper {
    private UUID followThisUUID;
    private UUID followerUUID;
    private List<LocationTarget> locationTargets;
    public boolean useEssentials = false;
    private int maxTimePerLocationInTicks;
    private int reAttachRadius = 3;

    public ConfigHelper(FileConfiguration config){
        loadValuesFromConfig(config);
        loadLocationTargets(config);
    }

    private void loadValuesFromConfig(FileConfiguration config){
        followThisUUID = UUID.fromString(config.getString("followThisUUID") != null ? config.getString("followThisUUID") : "00000000-0000-0000-0000-000000000000");
        followerUUID = UUID.fromString(config.getString("followerUUID") != null ? config.getString("followerUUID") : "00000000-0000-0000-0000-000000000000");
        maxTimePerLocationInTicks = config.getInt("maxTimePerLocationInSeconds") != 0 ? config.getInt("maxTimePerLocationInSeconds") * 20 : 6000;
        reAttachRadius = config.getInt("reAttachRadius") != 0 ? config.getInt("reAttachRadius") : 5;
    }

    public void reloadConfig(){
        plugin.reloadConfig();
        loadValuesFromConfig(plugin.getConfig());
        loadLocationTargets(plugin.getConfig());
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

    public void loadLocationTargets(FileConfiguration config){
        if(plugin.getConfig().get("locationTargets") != null){
            locationTargets = (List<LocationTarget>) config.getList("locationTargets");
        }else {
            logHelper.sendLogInfo("No location targets found");
            locationTargets = new ArrayList<>();
            addFallbackLocation(plugin.getServer().getWorlds().get(0).getSpawnLocation(), 40, 20, 0.1, false);
        }
    }

    public List<LocationTarget> getLocationTargets() {
        return locationTargets;
    }

    public void addFallbackLocation(Location location, double radius, int yOffset, double radPerTick, boolean invertedLook){
        locationTargets.add(new LocationTarget(location, radius, yOffset, radPerTick, invertedLook));
        plugin.getConfig().set("locationTargets", locationTargets);
        plugin.saveConfig();
    }

}
