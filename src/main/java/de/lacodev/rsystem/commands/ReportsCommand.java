package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportsCommand implements CommandExecutor {

    private final StaffCore staffCore;
    public ArrayList<String> reported = new ArrayList<>();

    public ReportsCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("reports").setExecutor(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Reports.See")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {

                    if (args.length == 0) {

                        // Get all Reported Players
                        try {
                            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
                                    .getResult("SELECT REPORTED_UUID FROM ReportSystem_reportsdb WHERE STATUS = '0'");
                            if (rs.next()) {
                                if (!reported.contains(rs.getString("REPORTED_UUID"))) {
                                    reported.add(rs.getString("REPORTED_UUID"));
                                }
                                while (rs.next()) {
                                    if (!reported.contains(rs.getString("REPORTED_UUID"))) {
                                        reported.add(rs.getString("REPORTED_UUID"));
                                    }
                                }

                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Reports.Title"));
                                p.sendMessage("");

                                for (int i = 0; i < reported.size(); i++) {
                                    TextComponent tc = new TextComponent();
                                    tc.setText(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Reports.Layout")
                                            .replace("%target%", staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(reported.get(i)))
                                            .replace("%status%", getStatusByUUID(reported.get(i))));
                                    tc.setClickEvent(
                                            new ClickEvent(Action.RUN_COMMAND, "/report claim " + reported.get(i)));

                                    String listString =
                                            staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Reports.Layout-Hover-Title") + "\n\n";
                                    for (String s : getReportReasonsFromPlayer(reported.get(i))) {
                                        listString += ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + s + "\n";
                                    }

                                    tc.setHoverEvent(
                                            new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                                                    new ComponentBuilder(listString).create()));

                                    p.spigot().sendMessage(tc);
                                }

                                reported.clear();

                                p.sendMessage("");
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Reports.No-Open"));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Reports.Usage"));
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Permissions.Reports.See")));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));
            }
        }
        return true;
    }

    private ArrayList<String> getReportReasonsFromPlayer(String uuid) {
        ArrayList<String> r = new ArrayList<>();

        ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
                "SELECT REASON FROM ReportSystem_reportsdb WHERE STATUS = '0' AND REPORTED_UUID = '" + uuid
                        + "'");
        try {
            while (rs.next()) {
                if (!r.contains(rs.getString("REASON"))) {
                    r.add(rs.getString("REASON"));
                }
            }
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    private String getStatusByUUID(String string) {
        Player target = Bukkit.getPlayer(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(string));

        if (target != null) {
            return ChatColor.GREEN + "Online";
        } else {
            return ChatColor.RED + "Offline";
        }
    }

}
