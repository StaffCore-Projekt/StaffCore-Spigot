package de.lacodev.rsystem.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.objects.MuteReasons;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MuteCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public MuteCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("mute").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Mute.Use"))) {
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
                            if (staffCore.getStaffCoreLoader().getBanManager().existsMuteID(id)) {
                                if (!staffCore.getStaffCoreLoader().getBanManager().isMuted(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                    if (!args[1].matches(p.getName())) {
                                        if (staffCore.getStaffCoreLoader().getConfigProvider()
                                                .getBoolean("Mute-System.Per-Reason-Permission.Enable")) {
                                            if (p.hasPermission(staffCore.getStaffCoreLoader().getConfigProvider()
                                                    .getString("Mute-System.Per-Reason-Permission.Prefix-For-Permission")
                                                    + args[1])) {
                                                if (staffCore.getStaffCoreLoader().getBanManager().submitMute(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]),
                                                        staffCore.getStaffCoreLoader().getBanManager().getMuteReasonFromID(id), p.getUniqueId().toString())) {
                                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                        ActionBarAPI.sendActionBar(p,
                                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Created"));
                                                    } else {
                                                        p.sendMessage(
                                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Created"));
                                                    }
                                                } else {
                                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                        ActionBarAPI.sendActionBar(p,
                                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Error"));
                                                    } else {
                                                        p.sendMessage(
                                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Error"));
                                                    }
                                                }
                                            } else {
                                                p.sendMessage(
                                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                                                                .replace("%permission%", staffCore.getStaffCoreLoader().getConfigProvider().getString(
                                                                        "Mute-System.Per-Reason-Permission.Prefix-For-Permission")
                                                                        + args[1]));
                                            }
                                        } else {
                                            if (staffCore.getStaffCoreLoader().getBanManager().submitMute(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]),
                                                    staffCore.getStaffCoreLoader().getBanManager().getMuteReasonFromID(id), p.getUniqueId().toString())) {
                                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                    ActionBarAPI.sendActionBar(p,
                                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Created"));
                                                } else {
                                                    p.sendMessage(
                                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Created"));
                                                }
                                            } else {
                                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                    ActionBarAPI.sendActionBar(p,
                                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Error"));
                                                } else {
                                                    p.sendMessage(
                                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Error"));
                                                }
                                            }
                                        }
                                    } else {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Cannot-mute-self"));
                                    }
                                } else {
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p,
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Already-Muted"));
                                    } else {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Already-Muted"));
                                    }
                                }
                            } else {
                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                    ActionBarAPI.sendActionBar(p,
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Reason-Not-Exists"));
                                } else {
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Reason-Not-Exists"));
                                }
                                p.sendMessage("");

                                ArrayList<MuteReasons> reasons = staffCore.getStaffCoreLoader().getBanManager().getMuteReasons();

                                if (reasons.size() > 0) {
                                    for (int i = 0; i < reasons.size(); i++) {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + ""
                                                        + reasons.get(i).getName() + ChatColor.DARK_GRAY + " ("
                                                        + ChatColor.YELLOW + "" + reasons.get(i).getID() + ChatColor.DARK_GRAY
                                                        + " | " + ChatColor.YELLOW + "" + staffCore.getStaffCoreLoader().getBanManager()
                                                        .getBanLength(reasons.get(i).getLength()) + ChatColor.DARK_GRAY + ")");
                                    }
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.No-Reasons"));
                                }
                                p.sendMessage("");
                            }
                        } else {
                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                ActionBarAPI.sendActionBar(p,
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Cannot-find-player"));
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Cannot-find-player"));
                            }
                        }

                    } else {
                        if (args.length == 1) {
                            if (staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]) != null) {
                                if (!staffCore.getStaffCoreLoader().getBanManager().isMuted(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                    staffCore.getStaffCoreLoader().getBanManager().openPagedMuteInv(p, args[0], 1);
                                } else {
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p,
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Already-Muted"));
                                    } else {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Already-Muted"));
                                    }
                                }

                            } else {
                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                    ActionBarAPI.sendActionBar(p,
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Cannot-find-player"));
                                } else {
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Cannot-find-player"));
                                }
                            }
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Usage"));
                            p.sendMessage("");
                            ArrayList<MuteReasons> reasons = staffCore.getStaffCoreLoader().getBanManager().getMuteReasons();

                            if (reasons.size() > 0) {
                                for (int i = 0; i < reasons.size(); i++) {
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + ""
                                                    + reasons.get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW
                                                    + "" + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | "
                                                    + ChatColor.YELLOW + "" + staffCore.getStaffCoreLoader().getBanManager()
                                                    .getBanLength(reasons.get(i).getLength()) + ChatColor.DARK_GRAY + ")");
                                }
                            } else {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.No-Reasons"));
                            }
                            p.sendMessage("");
                        }
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Mute.Use")));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }

        } else {
            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (args.length == 2) {
                    int id;

                    try {
                        id = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.NotValidID"));
                        return false;
                    }

                    if (staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]) != null) {
                        if (staffCore.getStaffCoreLoader().getBanManager().existsMuteID(id)) {
                            if (!staffCore.getStaffCoreLoader().getBanManager().isMuted(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {
                                if (staffCore.getStaffCoreLoader().getBanManager().submitMute(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]),
                                        staffCore.getStaffCoreLoader().getBanManager().getMuteReasonFromID(id), "Console")) {
                                    sender
                                            .sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Created"));
                                } else {
                                    sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Error"));
                                }
                            } else {
                                sender.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Already-Muted"));
                            }
                        } else {
                            sender.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Reason-Not-Exists"));

                            sender.sendMessage("");
                            ArrayList<MuteReasons> reasons = staffCore.getStaffCoreLoader().getBanManager().getMuteReasons();

                            if (reasons.size() > 0) {
                                for (int i = 0; i < reasons.size(); i++) {
                                    sender.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + ""
                                                    + reasons.get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW
                                                    + "" + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | "
                                                    + ChatColor.YELLOW + "" + staffCore.getStaffCoreLoader().getBanManager()
                                                    .getBanLength(reasons.get(i).getLength()) + ChatColor.DARK_GRAY + ")");
                                }
                            } else {
                                sender
                                        .sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.No-Reasons"));
                            }
                            sender.sendMessage("");
                        }
                    } else {
                        sender.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Cannot-find-player"));
                    }

                } else {
                    sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Usage"));
                    sender.sendMessage("");
                    ArrayList<MuteReasons> reasons = staffCore.getStaffCoreLoader().getBanManager().getMuteReasons();

                    if (reasons.size() > 0) {
                        for (int i = 0; i < reasons.size(); i++) {
                            sender.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "" + reasons
                                            .get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW + ""
                                            + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + ""
                                            + staffCore.getStaffCoreLoader().getBanManager().getBanLength(reasons.get(i).getLength()) + ChatColor.DARK_GRAY
                                            + ")");
                        }
                    } else {
                        sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.No-Reasons"));
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
