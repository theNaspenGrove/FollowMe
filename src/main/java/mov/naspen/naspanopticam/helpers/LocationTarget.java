package mov.naspen.naspanopticam.helpers;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("LocationTarget")
public class LocationTarget implements ConfigurationSerializable {
    private final Location centerLocation;
    private final double radius;
    private final int yOffset;
    private final double radPerTick;
    private final boolean invertedLook;

    public LocationTarget(Location centerLocation, double radius, int yOffset, double radPerTick, boolean invertedLook) {
        this.centerLocation = centerLocation;
        this.radius = radius;
        this.yOffset = yOffset;
        this.radPerTick = radPerTick;
        this.invertedLook = invertedLook;
    }

    public Location getLocationAroundCircle(int tick) {
        double x = centerLocation.getX() + radius * Math.cos((tick * radPerTick));
        double z = centerLocation.getZ() + radius * Math.sin((tick * radPerTick));
        double y = centerLocation.getY() + yOffset;

        Location loc = new Location(centerLocation.getWorld(), x, y, z);

        loc.setDirection(centerLocation.toVector().subtract(loc.toVector()).normalize());
        if(invertedLook){
            loc.setYaw(loc.getYaw() + 180);
        }
        return loc;
    }


    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("centerLocation", centerLocation);
        serialized.put("radius", radius);
        serialized.put("yOffset", yOffset);
        serialized.put("radPerTick", radPerTick);
        serialized.put("invertedLook", invertedLook);
        return serialized;
    }

    public static LocationTarget deserialize(Map<?,?> map) {
        return new LocationTarget((Location) map.get("centerLocation"), (double) map.get("radius"), (int) map.get("yOffset"), (double) map.get("radPerTick"), (boolean) map.get("invertedLook"));
    }
}
