package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckAltsCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public CheckAltsCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("checkalts").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                    .hasPermission(staffCore.getStaffCoreLoader().getPermission("CheckAlts.Use"))) {

                if (args.length == 1) {

                    String target = args[0];

                    if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target))) {

                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Collecting-Data")
                                        .replace("%ip%",
                                                staffCore.getStaffCoreLoader().getSystemManager().getLastKnownIP(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target))));

                        new BukkitRunnable() {

                            @SuppressWarnings("deprecation")
                            @Override
                            public void run() {
                                ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
                                        "SELECT * FROM ReportSystem_playerdb WHERE LAST_KNOWN_IP LIKE '%"
                                                + staffCore.getStaffCoreLoader().getSystemManager().getLastKnownIP(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target)) + "%'");

                                try {
                                    if (rs != null) {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Possible-Alts-Title")
                                                .replace("%target%", target));
                                        while (rs.next()) {
                                            if (!rs.getString("PLAYERNAME").equalsIgnoreCase(target)) {
                                                p.sendMessage("");
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Prefix.Username") + rs
                                                        .getString("PLAYERNAME"));
                                                TextComponent tc = new TextComponent();
                                                tc.setText(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.More-Info-Button"));

                                                String listString =
                                                        staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Prefix.Last-Online")
                                                                + staffCore.getStaffCoreLoader().getSystemManager().getLastOnline(rs.getString("UUID")) + "\n" + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Prefix.Reports") + staffCore.getStaffCoreLoader().getReportManager().getReports(rs.getString("UUID")) + "\n" + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Prefix.Bans") + staffCore.getStaffCoreLoader().getBanManager().getBans(rs.getString("UUID")) + "\n" + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Prefix.Mutes") + staffCore.getStaffCoreLoader().getBanManager().getMutes(rs.getString("UUID")) + "\n" + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Prefix.Warns") + staffCore.getStaffCoreLoader().getBanManager().getWarns(rs.getString("UUID")) + "\n \n" + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Prefix.Banned") + getBanningState(rs.getString("UUID")) + "\n" + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Prefix.Muted") + getMutingState(rs.getString("UUID"));

                                                tc.setHoverEvent(
                                                        new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                                                                new ComponentBuilder(listString).create()));

                                                p.spigot().sendMessage(tc);
                                            }
                                        }
                                    } else {
                                        p.sendMessage(
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Nothing-found"));
                                    }
                                } catch (SQLException e) {
                                    Bukkit.getConsoleSender().sendMessage(
                                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                                    + "MySQL" + ChatColor.DARK_GRAY + ") " + ChatColor.DARK_GRAY + "- "
                                                    + ChatColor.RED + "ERROR while collecting data from MySQL-Table");
                                    Bukkit.getConsoleSender().sendMessage(
                                            ChatColor.RED + "System " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                                                    + "MySQL" + ChatColor.DARK_GRAY + ") " + ChatColor.DARK_GRAY + "- "
                                                    + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "Query: "
                                                    + ChatColor.YELLOW + "CheckAlts" + ChatColor.DARK_GRAY + ")");
                                }
                            }

                        }.runTaskLaterAsynchronously(staffCore, 3 * 20);

                    } else {
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Player-Not-Found"));
                    }

                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.Usage"));
                }

            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("CheckAlts.Use")));
            }

        }
        return true;
    }

    private String getMutingState(String uuidByName) {
        if (staffCore.getStaffCoreLoader().getBanManager().isMuted(uuidByName)) {
            return staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.State.Muted") + staffCore.getStaffCoreLoader().getBanManager()
                    .getMuteReason(uuidByName);
        } else {
            return staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.State.No-Entry");
        }
    }

    private String getBanningState(String uuidByName) {
        if (staffCore.getStaffCoreLoader().getBanManager().isBanned(uuidByName)) {
            return staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.State.Banned") + staffCore.getStaffCoreLoader().getBanManager()
                    .getBanReason(uuidByName);
        } else {
            return staffCore.getStaffCoreLoader().getMessage("Messages.Altaccount-Check.State.No-Entry");
        }
    }

}
