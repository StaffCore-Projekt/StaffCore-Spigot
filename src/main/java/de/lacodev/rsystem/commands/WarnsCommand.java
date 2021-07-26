package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WarnsCommand implements CommandExecutor {
    private final StaffCore staffCore;

    public WarnsCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("warns").setExecutor(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;
            if (args.length == 0) {
                if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(p.getUniqueId().toString())) {
                    TextComponent w = new TextComponent();
                    w.setText(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Player-Info")
                            .replace("%target%", p.getName())
                            .replace("%warns%", "" + staffCore.getStaffCoreLoader().getBanManager().getWarns(p.getUniqueId().toString())));

                    String listString = "\n";
                    for (String s : getWarnReasonsFromPlayer(p.getUniqueId().toString())) {
                        listString += ChatColor.DARK_GRAY + "- " + ChatColor.RED + s + "\n";
                    }

                    w.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(listString).create()));

                    p.spigot().sendMessage(w);
                } else {
                    p.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Never-Joined"));
                }
            } else if (args.length == 1) {
                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Warns.See"))) {
                    if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {

                        String target = args[0];

                        TextComponent w = new TextComponent();
                        w.setText(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Player-Info")
                                .replace("%target%", target)
                                .replace("%warns%", "" + staffCore.getStaffCoreLoader().getBanManager().getWarns(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target))));

                        String listString = "\n";
                        for (String s : getWarnReasonsFromPlayer(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target))) {
                            listString += ChatColor.DARK_GRAY + "- " + ChatColor.RED + s + "\n";
                        }

                        w.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(listString).create()));

                        p.spigot().sendMessage(w);

                        TextComponent tc = new TextComponent();
                        tc.setText(" " + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Action-Ban-Button"));
                        tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/ban " + target));

                        TextComponent tc1 = new TextComponent();
                        tc1.setText(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Action-Mute-Button"));
                        tc1.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/mute " + target));
                        tc1.addExtra(tc);

                        p.spigot().sendMessage(tc1);

                    } else {
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Never-Joined"));
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Warns.See")));
                }
            } else if ((args.length == 2) && args[1].equalsIgnoreCase("clear")) {

                if (p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Warns.Clear"))) {
                    if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {

                        staffCore.getStaffCoreLoader().getBanManager().clearWans(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]));

                        //PREFIX + "You Cleared the "
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Clear")
                                .replace("%player%", args[0]));

                    } else {
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Never-Joined"));
                    }
                } else {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                            .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Warns.Clear")));
                }
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Usage"));
            }


        } else {
            if (args.length == 1) {

                if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))) {

                    sender.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Player-Info")
                                    .replace("%target%", args[0]).replace("%warns%",
                                            "" + staffCore.getStaffCoreLoader().getBanManager().getWarns(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(args[0]))));

                } else {
                    sender.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Never-Joined"));
                }

            } else {
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warns.Usage"));
            }
        }
        return true;
    }

    private ArrayList<String> getWarnReasonsFromPlayer(String uuid) {
        ArrayList<String> r = new ArrayList<>();

        ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
                .getResult("SELECT REASON FROM ReportSystem_warnsdb WHERE UUID = '" + uuid + "'");
        try {
            while (rs.next()) {
                r.add(rs.getString("REASON"));
            }
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

}
