package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Kick implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || player.hasPermission(Main.getPermissionNotice("Permissions.Kick.Use"))){
                if (args.length <= 1){

                }else {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    StringBuilder reasonBuilder = new StringBuilder(args[1]);
                    for (int i = 2; i < (args.length+1); i++){
                        reasonBuilder.append(" ").append(args[i]);
                    }
                    target.kickPlayer(Main.getMSG("Messages.System.Kick.Screen").replace("%reason%", reasonBuilder.toString()));

                    Bukkit.getOnlinePlayers().forEach(x -> {
                        if (x.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || x.hasPermission(Main.getPermissionNotice("Permissions.Kick.Notify"))){
                            x.sendMessage(Main.getPrefix() +
                                    Main.getMSG("Messages.System.Kick.Notify")
                                            .replace("%target%", target.getName())
                                            .replace("%player%", player.getName()));
                        }
                    });
                }

            }else{
                player.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
                        .replace("%permission%", Main.getPermissionNotice("Permissions.Kick.Use")));
            }

        }
        return false;
    }
}
