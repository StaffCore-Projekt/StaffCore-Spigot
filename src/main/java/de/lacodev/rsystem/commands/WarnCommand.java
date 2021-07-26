package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WarnCommand implements CommandExecutor {
    private final StaffCore staffCore;

    public WarnCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("warn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("Warn.Use"))) {

                if (args.length >= 2) {

                    Player target = Bukkit.getPlayer(args[0]);

                    if (target != null) {

                        if (target != p) {

                            new BukkitRunnable() {

                                @Override
                                public void run() {

                                    String listString = "";
                                    for (int i = 1; i < args.length; i++) {
                                        listString += args[i] + " ";
                                    }

                                    staffCore.getStaffCoreLoader().getBanManager().warnPlayer(target, p.getUniqueId().toString(), listString);

                                    staffCore.getStaffCoreLoader().getReportManager().sendNotify("WARN", p.getName(), target.getName(), listString);

                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Created"));
                                }

                            }.runTaskAsynchronously(staffCore);

                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Cant-Warn-Self"));
                        }

                    } else {
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Target-Offline"));
                    }

                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Usage"));
                }

            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Warn.Use")));
            }

        } else {
            if (args.length >= 2) {

                Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            String listString = "";
                            for (int i = 1; i < args.length; i++) {
                                listString += args[i] + " ";
                            }

                            staffCore.getStaffCoreLoader().getBanManager().warnPlayer(target, "Console", listString);

                            staffCore.getStaffCoreLoader().getReportManager().sendNotify("WARN", "Console", target.getName(), listString);

                            sender
                                    .sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Created"));
                        }

                    }.runTaskAsynchronously(staffCore);

                } else {
                    sender.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Target-Offline"));
                }

            } else {
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Usage"));
            }
        }
        return true;
    }

}
