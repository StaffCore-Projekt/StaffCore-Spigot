package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanIPCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public BanIPCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("banip").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("IpBan.Use"))) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (!staffCore.getStaffCoreLoader().getBanManager().isIPBanned(target.getAddress().getAddress().toString())) {
                            staffCore.getStaffCoreLoader().getBanManager().banIPAddress(p.getName(), target);
                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-Ban.Already-banned")
                                            .replace("%target%", target.getName()));
                        }
                    } else {
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-Ban.Target-Offline"));
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.IP-Ban.Usage"));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("IpBan.Use")));
            }
        }
        return true;
    }

}
