package mov.naspen.followme.helpers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static mov.naspen.followme.FollowMe.configHelper;

public class commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(command.getName().equals("followme")){
            if(args.length == 0){
                return false;
            } else if (args.length > 1) {
                if(args[0].equals("set")){
                    if(args.length == 2){
                        if(args[1].equals("fallback-location")){
                            configHelper.setFallbackFollowThisLocation(((Player) sender).getLocation());
                            sender.sendMessage("Set fallback location to " + ((Player) sender).getLocation());
                            return true;
                        }
                    } else if (args.length == 3) {
                        if (args[1].equals("player-follow-speed")) {
                            configHelper.setPlayerFollowRadPerTick(Double.parseDouble(args[2]));
                            sender.sendMessage("The camera is now rotating at " + Double.parseDouble(args[2]) + " radians per second");
                        } else if (args[1].equals("center-follow-speed")) {
                            configHelper.setCenterFollowRadPerTick(Double.parseDouble(args[2]));
                            sender.sendMessage("The camera is now rotating at " + Double.parseDouble(args[2]) + " radians per second");
                        }
                    }

                }
            }

        }
        return false;
    }
}
