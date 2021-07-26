package de.lacodev.rsystem.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.StaffCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public CheckCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("check").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Check.Use"))) {

                    if (args.length == 1) {

                        if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {

                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    p.sendMessage("");
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Title"));
                                    p.sendMessage("");
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Name")
                                            + args[0]);
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Last-IP")
                                                    + staffCore.getStaffCoreLoader().getSystemManager().getLastKnownIP(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                    p.sendMessage("");
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Reports")
                                                    + staffCore.getStaffCoreLoader().getReportManager().getReports(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Bans")
                                            + staffCore.getStaffCoreLoader().getBanManager().getBans(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Mutes")
                                            + staffCore.getStaffCoreLoader().getBanManager().getMutes(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Warns")
                                            + staffCore.getStaffCoreLoader().getBanManager().getWarns(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                    p.sendMessage("");
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Banned")
                                                    + getBanningState(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Muted")
                                            + getMutingState(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                    p.sendMessage("");
                                }
                            }.runTaskAsynchronously(staffCore);

                        } else {
                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                ActionBarAPI.sendActionBar(p,
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.No-Player-Found"));
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.No-Player-Found"));
                            }
                        }

                    } else {
                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                            ActionBarAPI
                                    .sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Usage"));
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Usage"));
                        }
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Check.Use")));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        } else {
            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (args.length == 1) {

                    if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                sender.sendMessage("");
                                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Title"));
                                sender.sendMessage("");
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Name") + args[0]);
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Last-IP")
                                                + staffCore.getStaffCoreLoader().getSystemManager().getLastKnownIP(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                sender.sendMessage("");
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Reports")
                                                + staffCore.getStaffCoreLoader().getReportManager().getReports(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Bans") + staffCore.getStaffCoreLoader().getBanManager()
                                                .getBans(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Mutes")
                                                + staffCore.getStaffCoreLoader().getBanManager().getMutes(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Warns")
                                                + staffCore.getStaffCoreLoader().getBanManager().getWarns(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                sender.sendMessage("");
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Banned")
                                                + getBanningState(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Prefix.Muted")
                                                + getMutingState(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0])));
                                sender.sendMessage("");
                            }
                        }.runTaskAsynchronously(staffCore);

                    } else {
                        sender.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.No-Player-Found"));
                    }

                } else {
                    sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.Usage"));
                }
            } else {
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        }
        return true;
    }

    private String getMutingState(String uuidByName) {
        if (staffCore.getStaffCoreLoader().getBanManager().isMuted(uuidByName)) {
            return staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.State.Muted") + staffCore.getStaffCoreLoader().getBanManager()
                    .getMuteReason(uuidByName);
        } else {
            return staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.State.No-Entry");
        }
    }

    private String getBanningState(String uuidByName) {
        if (staffCore.getStaffCoreLoader().getBanManager().isBanned(uuidByName)) {
            return staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.State.Banned") + staffCore.getStaffCoreLoader().getBanManager()
                    .getBanReason(uuidByName);
        } else {
            return staffCore.getStaffCoreLoader().getMessage("Messages.Player-Check.State.No-Entry");
        }
    }

}
