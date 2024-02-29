package mov.naspen.naspanopticam.helpers.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteHandler implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String aliasUsed, String[] args) {
        if (command.getName().equalsIgnoreCase("naspanopticam")) {
            if (sender instanceof Player p) {
                if (args.length == 1) {
                    List<String> cl = new ArrayList<>();
                    if (p.hasPermission("naspanopticam.add")) {
                        cl.add("add");
                    }
                    if (p.hasPermission("naspanopticam.reload")) {
                        cl.add("reload");
                    }
                    cl.add("followme");
                    cl.add("dontfollowme");
                    return cl;
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("add") && p.hasPermission("naspanopticam.add.location")) {
                        return List.of("location");
                    }
                } else if (args.length >= 3) {
                    if (args[1].equalsIgnoreCase("location")) {
                        return locationTabComplete(p, args);
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }
    private List<String> locationTabComplete(Player p, String[] args){
        if(args.length == 3){
            if(p.getTargetBlockExact(5) != null){
                return List.of((String.valueOf(p.getTargetBlockExact(5).getLocation().getBlockX())));
            }else{
                return List.of(String.valueOf(p.getLocation().getBlockX()));
            }
        }
        if(args.length == 4){
            if(p.getTargetBlockExact(5) != null){
                return List.of(String.valueOf(p.getTargetBlockExact(5).getLocation().getBlockY()));
            }else{
                return List.of(String.valueOf(p.getLocation().getBlockY()));
            }
        }
        if(args.length == 5){
            if(p.getTargetBlockExact(5) != null){
                return List.of(String.valueOf(p.getTargetBlockExact(5).getLocation().getBlockZ()));
            }else{
                return List.of(String.valueOf(p.getLocation().getBlockZ()));
            }
        }
        if(args.length == 6){
            return List.of("radius");
        }
        if(args.length == 7){
            return List.of("yOffset");
        }
        if(args.length == 8){
            return List.of("radPerSec");
        }
        if(args.length == 9){
            return List.of("invertedLook");
        }
        return null;
    }
}
