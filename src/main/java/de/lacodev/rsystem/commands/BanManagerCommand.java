package de.lacodev.rsystem.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.utils.PanelManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BanManagerCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public BanManagerCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("banmanager").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (args.length >= 4) {
                    if (args[0].equalsIgnoreCase("addreason")) {
                        if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Permissions.BanManager.addreason")) || p
                                .hasPermission(staffCore.getStaffCoreLoader().getPermission("Permissions.Everything"))) {
                            String timeunit = args[2].substring(args[2].length() - 1);
                            String reason = args[3];
                            for (int i = 4; i < args.length; i++) {
                                reason = reason + " " + args[i];
                            }
                            if (timeunit.toLowerCase().matches("d") || timeunit.toLowerCase().matches("h")
                                    || timeunit.toLowerCase().matches("m") || args[2]
                                    .equalsIgnoreCase("perma")) {
                                if (args[1].toLowerCase().matches("ban")) {
                                    if (args[2].equalsIgnoreCase("perma")) {
                                        if (!staffCore.getStaffCoreLoader().getBanManager().existsBanReason(reason)) {
                                            staffCore.getStaffCoreLoader().getBanManager().createBanReason(reason, "Perma", -1);
                                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Success"));
                                            } else {
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Success"));
                                            }
                                        } else {
                                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Already-Existing"));
                                            } else {
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Already-Existing"));
                                            }
                                        }
                                    } else {
                                        if (!staffCore.getStaffCoreLoader().getBanManager().existsBanReason(reason)) {
                                            staffCore.getStaffCoreLoader().getBanManager().createBanReason(reason, timeunit,
                                                    Integer.valueOf(args[2].substring(0, args[2].length() - 1)));
                                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Success"));
                                            } else {
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Success"));
                                            }
                                        } else {
                                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Already-Existing"));
                                            } else {
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Already-Existing"));
                                            }
                                        }
                                    }
                                } else if (args[1].toLowerCase().matches("mute")) {
                                    if (!staffCore.getStaffCoreLoader().getBanManager().existsMuteReason(reason)) {
                                        staffCore.getStaffCoreLoader().getBanManager().createMuteReason(reason, timeunit,
                                                Integer.parseInt(args[2].substring(0, args[2].length() - 1)));
                                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                            ActionBarAPI.sendActionBar(p,
                                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Success"));
                                        } else {
                                            p.sendMessage(
                                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Success"));
                                        }
                                    } else {
                                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                            ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Already-Existing"));
                                        } else {
                                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Already-Existing"));
                                        }
                                    }
                                } else {
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Invalid-ReasonType"));
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Invalid-ReasonType"));
                                    }
                                }
                            } else {
                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                    ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Invalid-TimeUnit"));
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Invalid-TimeUnit"));
                                }
                            }
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                                    .replace("%permission%",
                                            staffCore.getStaffCoreLoader().getPermission("BanManager.addreason")));
                        }
                    } else {
                        p.sendMessage("");
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "BanManager"
                                        + ChatColor.DARK_GRAY + "]");
                        p.sendMessage("");
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY
                                + "/banmanager addreason <Ban/Mute> <Length: 30d / Perma> <Reason>");
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/banmanager removereason <Ban/Mute> <ID>");
                        p.sendMessage("");
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("removereason")) {
                        int id;
                        if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("BanManager.removereason"))
                                || p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                            try {
                                id = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.NotValidID"));
                                return false;
                            }
                            if (args[1].toLowerCase().matches("ban")) {
                                String reason = staffCore.getStaffCoreLoader().getBanManager().getBanReasonFromID(id);
                                if (staffCore.getStaffCoreLoader().getBanManager().existsBanReason(reason)) {
                                    staffCore.getStaffCoreLoader().getBanManager().deleteBanReason(reason);
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Remove-Reason.Success"));
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Remove-Reason.Success"));
                                    }
                                } else {
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Remove-Reason.Not-Exists"));
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Remove-Reason.Not-Exists"));
                                    }
                                }
                            } else if (args[1].toLowerCase().matches("mute")) {
                                String reason = staffCore.getStaffCoreLoader().getBanManager().getMuteReasonFromID(id);
                                if (staffCore.getStaffCoreLoader().getBanManager().existsMuteReason(reason)) {
                                    staffCore.getStaffCoreLoader().getBanManager().deleteMuteReason(reason);
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Remove-Reason.Success"));
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Remove-Reason.Success"));
                                    }
                                } else {
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Remove-Reason.Not-Exists"));
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Remove-Reason.Not-Exists"));
                                    }
                                }
                            } else {
                                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                    ActionBarAPI.sendActionBar(p, staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Invalid-ReasonType"));
                                } else {
                                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason.Invalid-ReasonType"));
                                }
                            }
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                                    .replace("%permission%",
                                            staffCore.getStaffCoreLoader().getPermission("BanManager.removereason")));
                        }
                    } else {
                        p.sendMessage("");
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "BanManager"
                                        + ChatColor.DARK_GRAY + "]");
                        p.sendMessage("");
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY
                                + "/banmanager addreason <Ban/Mute> <Length: 30d / Perma> <Reason>");
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/banmanager removereason <Ban/Mute> <ID>");
                        p.sendMessage("");
                    }
                } else if (args.length == 0) {
                    PanelManager pm = staffCore.getStaffCoreLoader().getPanelManager();
                    pm.openBanManagerManu(p);
                } else {
                    p.sendMessage("");
                    p.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "BanManager"
                                    + ChatColor.DARK_GRAY + "]");
                    p.sendMessage("");
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY
                            + "/banmanager addreason <Ban/Mute> <Length: 30d / Perma> <Reason>");
                    p.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/banmanager removereason <Ban/Mute> <ID>");
                    p.sendMessage("");
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "� " + ChatColor.RED + ChatColor.BOLD
                            + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Console-Input"
                            + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        }
        return true;
    }

}
