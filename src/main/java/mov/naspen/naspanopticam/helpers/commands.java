package mov.naspen.naspanopticam.helpers;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static mov.naspen.naspanopticam.NasPanoptiCam.*;

public class commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(command.getName().equals("naspanopticam")){
            if(args.length == 0){
                return false;
            } else {
                if(args[0].equals("add")){
                    if(args[1].equals("location")){
                        if(args.length == 9 && parseArgs(args)){
                            configHelper.addFallbackLocation(parseLocation(sender,args), Double.parseDouble(args[5]), Integer.parseInt(args[6]), Double.parseDouble(args[7]) / 20f, Boolean.parseBoolean(args[8]));
                            sender.sendMessage("Added location to the list of locations");
                            return true;
                        }else{
                            sender.sendMessage("usage /naspanopticam add location <X> <Y> <Z> <radius> <yOffset> <radPerSec> <invertedLook>");
                            return false;
                        }
                    }
                }else if(args[0].equals("reload")){
                    configHelper.reloadConfig();
                    sender.sendMessage("Reloaded the NasPanoptiCam config");
                    return true;
                }
            }
        }
        return false;
    }

    private Location parseLocation(CommandSender sender, String[] args) {
        return new Location(((Player) sender).getWorld(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
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
