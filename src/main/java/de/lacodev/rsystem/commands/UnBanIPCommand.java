package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnBanIPCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public UnBanIPCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("unbanip").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("IpUnBan.Use"))) {

                if (args.length == 1) {

                    String playername = args[0];

                    if (staffCore.getStaffCoreLoader().getBanManager().isIpBanned(playername)) {

                        staffCore.getStaffCoreLoader().getBanManager().unbanIpAddress(playername);

                        for (Player all : Bukkit.getOnlinePlayers()) {
                            if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("IpUnBan.Notify"))) {
                                all.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-UnBan.Notify")
                                                .replace("%player%", p.getName()).replace("%target%", playername));
                            }
                        }
                    } else {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-UnBan.Not-banned")
                                .replace("%target%", playername));
                    }

                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-UnBan.Usage"));
                }

            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("IpUnBan.Use")));
            }

        } else {

            if (args.length == 1) {

                String playername = args[0];

                if (staffCore.getStaffCoreLoader().getBanManager().isIpBanned(playername)) {

                    staffCore.getStaffCoreLoader().getBanManager().unbanIpAddress(playername);

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("IpUnBan.Notify"))) {
                            all.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-UnBan.Notify")
                                    .replace("%player%", "Console").replace("%target%", playername));
                        }
                    }
                    sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-UnBan.Notify")
                            .replace("%player%", "Console").replace("%target%", playername));
                } else {
                    sender.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-UnBan.Not-banned")
                                    .replace("%target%", playername));
                }

            } else {
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-UnBan.Usage"));
            }

        }
        return true;
    }

}
