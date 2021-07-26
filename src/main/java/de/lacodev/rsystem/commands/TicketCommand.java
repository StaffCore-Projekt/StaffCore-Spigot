package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class TicketCommand implements CommandExecutor {
    private final StaffCore staffCore;

    public TicketCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("ticket").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (args.length == 0) {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/ticket create");
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "/ticket list");
            }

            if (args.length == 1) {

                if (args[0].equalsIgnoreCase("create")) {

                    staffCore.getStaffCoreLoader().getTicketManager().createTicket(p);

                }

                if (args[0].equalsIgnoreCase("list")) {

                    if (staffCore.getStaffCoreLoader().getTicketManager().hasTicket(p)) {

                        try {

                            staffCore.getStaffCoreLoader().getTicketManager().openTicketOverview(p, 1);

                        } catch (SQLException e) {

                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify"));

                        }

                    }

                }

            }

        }

        return true;
    }

}
