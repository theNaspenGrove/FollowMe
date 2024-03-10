package mov.naspen.naspanopticam;

import com.earth2me.essentials.Essentials;
import mov.naspen.naspanopticam.config.ConfigHelper;
import mov.naspen.naspanopticam.events.PlayerJoinEventListener;
import mov.naspen.naspanopticam.events.PlayerQuitEventListener;
import mov.naspen.naspanopticam.helpers.follow.FollowerWatcher;
import mov.naspen.naspanopticam.helpers.target.LocationTarget;
import mov.naspen.naspanopticam.helpers.NasPanoptiPlaceholders;
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
    public static FollowerWatcher followerWatcher;
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
        followerWatcher = new FollowerWatcher();

        this.getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitEventListener(), this);

        Objects.requireNonNull(this.getCommand("dontfollowme")).setExecutor(new CommandHandler());
        Objects.requireNonNull(this.getCommand("followme")).setExecutor(new CommandHandler());

        Objects.requireNonNull(this.getCommand("naspanopticam")).setExecutor(new CommandHandler());
        Objects.requireNonNull(this.getCommand("naspanopticam")).setTabCompleter(new TabCompleteHandler());

        //create PlaceHolder API expansion
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new NasPanoptiPlaceholders(this).register();
            logHelper.sendLogInfo("Placeholders holding!");
        }

        logHelper.sendLogInfo("Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
