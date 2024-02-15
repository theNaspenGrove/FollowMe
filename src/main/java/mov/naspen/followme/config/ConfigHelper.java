package mov.naspen.followme.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

import static mov.naspen.followme.FollowMe.plugin;

public class ConfigHelper {
    private final UUID followThisUUID;
    private final UUID followerUUID;
    private Location fallbackFollowThisLocation;
    private final double playerFollowRadius;
    private double playerFollowRadPerTick;
    private final double playerHeightOffset;
    private final double centerFollowRadius;
    private final double centerFollowRadPerTick;
    private final double centerHeightOffset;
    public boolean useEssentials = false;

    public ConfigHelper(FileConfiguration config){
        followThisUUID = UUID.fromString(config.getString("followThisUUID") != null ? config.getString("followThisUUID") : "00000000-0000-0000-0000-000000000000");
        followerUUID = UUID.fromString(config.getString("followerUUID") != null ? config.getString("followerUUID") : "00000000-0000-0000-0000-000000000000");
        fallbackFollowThisLocation = config.getLocation("fallbackFollowThisLocation") != null ? config.getLocation("fallbackFollowThisLocation") : new Location(null, 0, 0, 0);
        playerFollowRadius = config.getDouble("playerFollowRadius") != 0 ? config.getDouble("playerFollowRadius") : 10;
        playerFollowRadPerTick = config.getDouble("playerFollowRadPerSec") != 0 ? config.getDouble("playerFollowRadPerSec") / 20f : 0.5 / 20f;
        playerHeightOffset = config.getDouble("playerHeightOffset") != 0 ? config.getDouble("playerHeightOffset") : 0;
        centerFollowRadius = config.getDouble("centerFollowRadius") != 0 ? config.getDouble("centerFollowRadius") : 100;
        centerFollowRadPerTick = config.getDouble("centerFollowRadPerSec") != 0 ? config.getDouble("centerFollowRadPerSec") / 20f : 0.1 / 20f;
        centerHeightOffset = config.getDouble("centerHeightOffset") != 0 ? config.getDouble("centerHeightOffset") : 10;

    }

    public UUID getFollowThisUUID() {
        return followThisUUID;
    }

    public UUID getFollowerUUID() {
        return followerUUID;
    }

    public double getPlayerFollowRadius() {
        return playerFollowRadius;
    }

    public double getPlayerFollowRadPerTick() {
        return playerFollowRadPerTick;
    }

    public void setPlayerFollowRadPerTick(double radPerSec){
        playerFollowRadPerTick = radPerSec / 20f;
        plugin.getConfig().set("playerFollowRadPerSec", radPerSec);
        plugin.saveConfig();
    }

    public void setCenterFollowRadPerTick(double radPerSec){
        playerFollowRadPerTick = radPerSec / 20f;
        plugin.getConfig().set("playerFollowRadPerSec", radPerSec);
        plugin.saveConfig();
    }

    public double getPlayerHeightOffset(){
        return playerHeightOffset;
    }

    public double getCenterFollowRadius() {
        return centerFollowRadius;
    }

    public double getCenterFollowRadPerTick() {
        return centerFollowRadPerTick;
    }

    public double getCenterHeightOffset(){
        return centerHeightOffset;
    }

    public Location getFallbackFollowThisLocation() {
        return fallbackFollowThisLocation;
    }

    public void setFallbackFollowThisLocation(Location location){
        fallbackFollowThisLocation = location;
        plugin.getConfig().set("fallbackFollowThisLocation", location);
        plugin.saveConfig();
    }

}
