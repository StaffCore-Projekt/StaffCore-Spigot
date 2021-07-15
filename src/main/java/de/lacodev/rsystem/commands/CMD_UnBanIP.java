package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_UnBanIP implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p
                    .hasPermission(Main.getPermissionNotice("Permissions.IpUnBan.Use"))) {

                if (args.length == 1) {

                    String playername = args[0];

                    if (BanManager.isIpBanned(playername)) {

                        BanManager.unbanIpAddress(playername);

                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (all.hasPermission(Main.getPermissionNotice("Permissions.IpUnBan.Notify"))) {
                                all.sendMessage(
                                        Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-UnBan.Notify")
                                                .replace("%player%", p.getName()).replace("%target%", playername));
                            }
                        }
                    } else {
                        p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-UnBan.Not-banned")
                                .replace("%target%", playername));
                    }

                } else {
                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-UnBan.Usage"));
                }

            } else {
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission")
                        .replace("%permission%", Main.getPermissionNotice("Permissions.IpUnBan.Use")));
            }

        } else {

            if (args.length == 1) {

                String playername = args[0];

                if (BanManager.isIpBanned(playername)) {

                    BanManager.unbanIpAddress(playername);

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (all.hasPermission(Main.getPermissionNotice("Permissions.IpUnBan.Notify"))) {
                            all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-UnBan.Notify")
                                    .replace("%player%", "Console").replace("%target%", playername));
                        }
                    }
                    sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-UnBan.Notify")
                            .replace("%player%", "Console").replace("%target%", playername));
                } else {
                    sender.sendMessage(
                            Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-UnBan.Not-banned")
                                    .replace("%target%", playername));
                }

            } else {
                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-UnBan.Usage"));
            }

        }
        return true;
    }

}
