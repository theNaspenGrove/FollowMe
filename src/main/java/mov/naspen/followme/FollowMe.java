package mov.naspen.followme;

import com.earth2me.essentials.Essentials;
import mov.naspen.followme.config.ConfigHelper;
import mov.naspen.followme.events.PlayerJoinEventListener;
import mov.naspen.followme.helpers.LocationTarget;
import mov.naspen.followme.helpers.commands;
import mov.naspen.periderm.loging.AspenLogHelper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class FollowMe extends JavaPlugin {

    public static AspenLogHelper logHelper;
    public static ConfigHelper configHelper;
    public static FollowMe plugin;
    public static Essentials ess;
    static {
        ConfigurationSerialization.registerClass(LocationTarget.class, "LocationTarget");
    }
    @Override
    public void onEnable() {
        // Plugin startup logic;
        plugin = this;
        logHelper = new AspenLogHelper(this.getLogger());

        this.saveDefaultConfig();
        configHelper = new ConfigHelper(this.getConfig());

        ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        if(ess != null){
            configHelper.useEssentials = true;
            logHelper.sendLogInfo("EssentialsX found");
        }
        logHelper.sendLogInfo(String.valueOf(configHelper.useEssentials));

        this.getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), this);

        Objects.requireNonNull(this.getCommand("followme")).setExecutor(new commands());

        logHelper.sendLogInfo("Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
