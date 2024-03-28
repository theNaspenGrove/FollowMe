package mov.naspen.naspanopticam.helpers.command;

import mov.naspen.periderm.helpers.luckPerms.AspenMetaKey;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static mov.naspen.naspanopticam.NasPanoptiCam.*;
import static mov.naspen.naspanopticam.helpers.TrackedSessionManager.dumpSessions;

public class CommandHandler implements CommandExecutor {
    public static AspenMetaKey dontFollowMe = new AspenMetaKey("dontFollowMe");
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(command.getName().equalsIgnoreCase("naspanopticam")){
            switch (args[0].toLowerCase()) {
                case "add" -> {
                    if(!(sender instanceof Player)){
                        sender.sendMessage("This command can only be used by a player");
                        return false;
                    }
                    if(!sender.hasPermission("naspanopticam.add")){
                        sender.sendMessage("You do not have permission to use this command");
                        return false;
                    }
                    if (args.length > 1 && args[1].equals("location")) {
                        if(!sender.hasPermission("naspanopticam.add.location")){
                            sender.sendMessage("You do not have permission to use this command");
                            return false;
                        }
                        if (args.length == 9 && parseArgs(args)) {
                            configHelper.addFallbackLocation(parseLocation(sender, args), Double.parseDouble(args[5]), Integer.parseInt(args[6]), Double.parseDouble(args[7]) / 20f, Boolean.parseBoolean(args[8]));
                            sender.sendMessage("Added location to the list of locations");
                            return true;
                        } else {
                            sender.sendMessage("usage /naspanopticam add location <X> <Y> <Z> <radius> <yOffset> <radPerSec> <invertedLook>");
                            return false;
                        }
                    }
                }
                case "reload" -> {
                    if(!sender.hasPermission("naspanopticam.reload")){
                        sender.sendMessage("You do not have permission to use this command");
                        return false;
                    }
                    configHelper.reloadConfig();
                    sender.sendMessage("Reloaded the NasPanoptiCam config");
                    return true;
                }
                case "followme" -> {
                    if(!(sender instanceof Player)){
                        sender.sendMessage("This command can only be used by a player");
                        return true;
                    }
                    metaHelper.clearMetaValue((Player) sender, dontFollowMe);
                    sender.sendMessage("NasPanoptiCam will now select you as a target.");
                    return true;
                }
                case "dontfollowme" -> {
                    if(!(sender instanceof Player)){
                        sender.sendMessage("This command can only be used by a player");
                        return true;
                    }
                    dontFollowPlayer((Player) sender);
                    return true;
                }
                case "dumpsessions" -> {
                    if(!sender.hasPermission("naspanopticam.dump")){
                        sender.sendMessage("You do not have permission to use this command");
                        return false;
                    }
                    if(dumpSessions()){
                        sender.sendMessage("Dumped player target sessions to file");
                        return true;
                    }else{
                        sender.sendMessage("Failed to dump player target sessions to file!");
                        sender.sendMessage("Check the server log for more information");
                        return false;
                    }
                }
            }
        }else if(command.getName().equalsIgnoreCase("dontfollowme")){
            if(sender instanceof Player){
                dontFollowPlayer((Player) sender);
            }else{
                sender.sendMessage("This command can only be used by a player");
            }
            return true;
        }else if(command.getName().equalsIgnoreCase("followme")){
            if(sender instanceof Player){
                metaHelper.clearMetaValue((Player) sender, dontFollowMe);
                followerWatcher.sendPrivateMessage((Player) sender, "I will now select you as a target.");
            }else{
                sender.sendMessage("This command can only be used by a player");
            }
            return true;
        }
        return false;
    }

    private Location parseLocation(CommandSender sender, String[] args) {
        return new Location(((Player) sender).getWorld(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
    }

    private static void dontFollowPlayer(Player player){
        if(followerWatcher.getPlayerFollower().isFollowingPlayer(player)){
            followerWatcher.getPlayerFollower().stopFollowing();
        }
        followerWatcher.sendPrivateMessage(player,"I will no longer select you as a target.");
        metaHelper.setMetaValue((Player) player, dontFollowMe, "true");
    }

    private boolean parseArgs(String[] args) {
        //make sure that the command args for 'add location' are in the correct order and the correct types
        if (args[0].equals("add") && args[1].equals("location")) {
            if (args.length == 9) {
                try {
                    Double.parseDouble(args[5]);
                    Integer.parseInt(args[6]);
                    Double.parseDouble(args[7]);
                } catch (NumberFormatException e) {
                    return false;
                }
        }
    }
        return true;
    }
}
