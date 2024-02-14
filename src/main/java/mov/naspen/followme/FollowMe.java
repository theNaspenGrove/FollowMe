package mov.naspen.followme;

import mov.naspen.followme.config.ConfigHelper;
import mov.naspen.followme.events.PlayerJoinEventListener;
import mov.naspen.followme.helpers.commands;
import mov.naspen.periderm.loging.AspenLogHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class FollowMe extends JavaPlugin {

    public static AspenLogHelper logHelper;
    public static ConfigHelper configHelper;
    public static FollowMe plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic;
        plugin = this;
        logHelper = new AspenLogHelper(this.getLogger());
        this.saveDefaultConfig();
        configHelper = new ConfigHelper(this.getConfig());

        this.getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), this);

        Objects.requireNonNull(this.getCommand("followme")).setExecutor(new commands());

        logHelper.sendLogInfo("Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
