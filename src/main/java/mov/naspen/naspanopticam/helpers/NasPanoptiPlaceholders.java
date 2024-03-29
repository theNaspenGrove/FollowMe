package mov.naspen.naspanopticam.helpers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import mov.naspen.naspanopticam.NasPanoptiCam;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import static mov.naspen.naspanopticam.NasPanoptiCam.followerWatcher;
import static mov.naspen.naspanopticam.helpers.TrackedSessionManager.minSessionTime;

public class NasPanoptiPlaceholders extends PlaceholderExpansion {

    private final NasPanoptiCam plugin;

    public NasPanoptiPlaceholders(NasPanoptiCam plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return "Naspen";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "NasPanoptiCam";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        if(params.equalsIgnoreCase("isBeingFollowed")){
            if(!followerWatcher.getPlayerFollower().isFollowingPlayer()) return "false";
            if(p.getUniqueId().equals(followerWatcher.getPlayerFollower().getFollowThisPlayer().getUniqueId())){
                if(followerWatcher.getPlayerFollower().getPlayerTarget().getFollowingFor() > minSessionTime){
                    return "true";
                }else{
                    return "true*";
                }
            }else{
                return "false";
            }
        }
        return null; // Placeholder is unknown by the Expansion
    }
}
