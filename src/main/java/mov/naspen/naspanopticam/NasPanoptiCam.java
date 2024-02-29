package mov.naspen.naspanopticam;

import com.earth2me.essentials.Essentials;
import mov.naspen.naspanopticam.config.ConfigHelper;
import mov.naspen.naspanopticam.events.PlayerJoinEventListener;
import mov.naspen.naspanopticam.helpers.LocationTarget;
import mov.naspen.naspanopticam.helpers.command.CommandHandler;
import mov.naspen.naspanopticam.helpers.command.TabCompleteHandler;
import mov.naspen.periderm.helpers.luckPerms.AspenLuckPermsHelper;
import mov.naspen.periderm.loging.AspenLogHelper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class NasPanoptiCam extends JavaPlugin {

    public static AspenLogHelper logHelper;
    public static AspenLuckPermsHelper metaHelper;
    public static ConfigHelper configHelper;
    public static NasPanoptiCam plugin;
    public static Essentials ess;
    static {
        ConfigurationSerialization.registerClass(LocationTarget.class, "LocationTarget");
    }
    @Override
    public void onEnable() {
        // Plugin startup logic;
        plugin = this;
        logHelper = new AspenLogHelper(this.getLogger());
        metaHelper = new AspenLuckPermsHelper(logHelper.logger, "NasPanoptiCam");

        this.saveDefaultConfig();
        configHelper = new ConfigHelper(this.getConfig());

        ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        if(ess != null){
            configHelper.useEssentials = true;
            logHelper.sendLogInfo("EssentialsX found");
        }
        logHelper.sendLogInfo(String.valueOf(configHelper.useEssentials));

        this.getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), this);

        Objects.requireNonNull(this.getCommand("dontfollowme")).setExecutor(new CommandHandler());
        Objects.requireNonNull(this.getCommand("followme")).setExecutor(new CommandHandler());

        Objects.requireNonNull(this.getCommand("naspanopticam")).setExecutor(new CommandHandler());
        Objects.requireNonNull(this.getCommand("naspanopticam")).setTabCompleter(new TabCompleteHandler());

        logHelper.sendLogInfo("Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
