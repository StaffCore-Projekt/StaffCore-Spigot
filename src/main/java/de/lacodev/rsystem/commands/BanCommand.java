package de.lacodev.rsystem.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.pojo.CommandExtender;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BanCommand extends CommandExtender {

    private final StaffCore staffCore;

    public BanCommand(StaffCore staffCore) {
        super(staffCore);
        this.staffCore = staffCore;
        this.staffCore.getCommand("ban").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (p.hasPermission(getPermission("Everything")) || p
                        .hasPermission(getPermission("Ban.Use"))) {
                    if (args.length == 2) {
                        int id;
                        if (staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]) != null) {
                            try {
                                id = Integer.parseInt(args[1]);
                            } catch (NumberFormatException e) {
                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                    ActionBarAPI.sendActionBar(p,
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.NotValidID"));
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.NotValidID"));
                                }
                                return false;
                            }
                            if (staffCore.getStaffCoreLoader().getBanManager().existsBanID(id)) {
                                if (!staffCore.getStaffCoreLoader().getBanManager().isBanned(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                    if (!args[0].matches(p.getName())) {
                                        if (staffCore.getStaffCoreLoader().getConfigProvider()
                                                .getBoolean("Ban-System.Per-Reason-Permission.Enable")) {
                                            if (p.hasPermission(staffCore.getStaffCoreLoader().getConfigProvider()
                                                    .getString("Ban-System.Per-Reason-Permission.Prefix-For-Permission")
                                                    + args[1])) {
                                                staffCore.getStaffCoreLoader().getBanManager().submitBan(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]),
                                                        staffCore.getStaffCoreLoader().getBanManager().getBanReasonFromID(id), p.getUniqueId().toString());
                                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                    ActionBarAPI.sendActionBar(p,
                                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Created"));
                                                } else {
                                                    p.sendMessage(
                                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Created"));
                                                }
                                            } else {
                                                p.sendMessage(
                                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                                                                .replace("%permission%", staffCore.getStaffCoreLoader().getConfigProvider().getString(
                                                                        "Ban-System.Per-Reason-Permission.Prefix-For-Permission")
                                                                        + args[1]));
                                            }
                                        } else {
                                            staffCore.getStaffCoreLoader().getBanManager().submitBan(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]),
                                                    staffCore.getStaffCoreLoader().getBanManager().getBanReasonFromID(id), p.getUniqueId().toString());
                                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                ActionBarAPI.sendActionBar(p,
                                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Created"));
                                            } else {
                                                p.sendMessage(
                                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Created"));
                                            }
                                        }
                                    } else {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Cannot-ban-self"));
                                    }
                                } else {
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p,
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Already-Banned"));
                                    } else {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Already-Banned"));
                                    }
                                }
                            } else {
                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                    ActionBarAPI.sendActionBar(p,
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Reason-Not-Exists"));
                                } else {
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Reason-Not-Exists"));
                                }
                                p.sendMessage("");
                                ArrayList<BanReasons> reasons = staffCore.getStaffCoreLoader().getBanManager().getBanReasons();
                                if (reasons.size() > 0) {
                                    for (int i = 0; i < reasons.size(); i++) {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons
                                                        .get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW
                                                        + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | "
                                                        + ChatColor.YELLOW + "" + staffCore.getStaffCoreLoader().getBanManager()
                                                        .getBanLength(reasons.get(i).getLength()) + ChatColor.DARK_GRAY + ")");
                                    }
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.No-Reasons"));
                                }
                                p.sendMessage("");
                            }
                        } else {
                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                ActionBarAPI.sendActionBar(p,
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Cannot-find-player"));
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Cannot-find-player"));
                            }
                        }
                    } else {
                        if (args.length == 1) {
                            if (staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]) != null) {
                                if (!staffCore.getStaffCoreLoader().getBanManager().isBanned(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                    staffCore.getStaffCoreLoader().getBanManager().openPagedBanInv(p, args[0], 1);
                                } else {
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p,
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Already-Banned"));
                                    } else {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Already-Banned"));
                                    }
                                }
                            } else {
                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                    ActionBarAPI.sendActionBar(p,
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Cannot-find-player"));
                                } else {
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Cannot-find-player"));
                                }
                            }
                        } else {
                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                ActionBarAPI
                                        .sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Usage"));
                            } else {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Usage"));
                            }
                            p.sendMessage("");
                            ArrayList<BanReasons> reasons = staffCore.getStaffCoreLoader().getBanManager().getBanReasons();
                            if (reasons.size() > 0) {
                                for (int i = 0; i < reasons.size(); i++) {
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons
                                                    .get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW
                                                    + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW
                                                    + "" + staffCore.getStaffCoreLoader().getBanManager().getBanLength(reasons.get(i).getLength())
                                                    + ChatColor.DARK_GRAY + ")");
                                }
                            } else {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.No-Reasons"));
                            }
                            p.sendMessage("");
                        }
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Permissions.Ban.Use")));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        } else {
            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (args.length == 2) {
                    int id;
                    if (staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]) != null) {
                        try {
                            id = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.NotValidID"));
                            return false;
                        }
                        if (staffCore.getStaffCoreLoader().getBanManager().existsBanID(id)) {
                            if (!staffCore.getStaffCoreLoader().getBanManager().isBanned(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                staffCore.getStaffCoreLoader().getBanManager().submitBan(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]),
                                        staffCore.getStaffCoreLoader().getBanManager().getBanReasonFromID(id), "Console");
                                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Created"));
                            } else {
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Already-Banned"));
                            }
                        } else {
                            sender.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Reason-Not-Exists"));
                            sender.sendMessage("");
                            ArrayList<BanReasons> reasons = staffCore.getStaffCoreLoader().getBanManager().getBanReasons();
                            if (reasons.size() > 0) {
                                for (int i = 0; i < reasons.size(); i++) {
                                    sender.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons
                                                    .get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW
                                                    + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW
                                                    + "" + staffCore.getStaffCoreLoader().getBanManager().getBanLength(reasons.get(i).getLength())
                                                    + ChatColor.DARK_GRAY + ")");
                                }
                            } else {
                                sender
                                        .sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.No-Reasons"));
                            }
                            sender.sendMessage("");
                        }
                    } else {
                        sender.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Cannot-find-player"));
                    }
                } else {
                    sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Usage"));
                    sender.sendMessage("");
                    ArrayList<BanReasons> reasons = staffCore.getStaffCoreLoader().getBanManager().getBanReasons();
                    if (reasons.size() > 0) {
                        for (int i = 0; i < reasons.size(); i++) {
                            sender.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons.get(i)
                                            .getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW + reasons.get(i)
                                            .getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + "" + staffCore.getStaffCoreLoader().getBanManager()
                                            .getBanLength(reasons.get(i).getLength()) + ChatColor.DARK_GRAY + ")");
                        }
                    } else {
                        sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.No-Reasons"));
                    }
                    sender.sendMessage("");
                }
            } else {
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        }
        return true;
    }
}
