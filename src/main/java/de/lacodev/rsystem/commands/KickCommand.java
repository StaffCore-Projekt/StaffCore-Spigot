package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.StaffCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    private final StaffCore staffCore;

    public KickCommand(StaffCore staffCore) {
        this.staffCore = staffCore;
        this.staffCore.getCommand("kick").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || player.hasPermission(staffCore.getStaffCoreLoader().getPermission("Kick.Use"))) {
                if (args.length <= 1) {

                } else {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    StringBuilder reasonBuilder = new StringBuilder(args[1]);
                    for (int i = 2; i < (args.length + 1); i++) {
                        reasonBuilder.append(" ").append(args[i]);
                    }
                    target.kickPlayer(staffCore.getStaffCoreLoader().getMessage("Messages.System.Kick.Screen").replace("%reason%", reasonBuilder.toString()));

                    Bukkit.getOnlinePlayers().forEach(x -> {
                        if (x.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || x.hasPermission(staffCore.getStaffCoreLoader().getPermission("Kick.Notify"))) {
                            x.sendMessage(staffCore.getStaffCoreLoader().getPrefix() +
                                    staffCore.getStaffCoreLoader().getMessage("Messages.System.Kick.Notify")
                                            .replace("%target%", target.getName())
                                            .replace("%player%", player.getName()));
                        }
                    });
                }

            } else {
                player.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                        .replace("%permission%", staffCore.getStaffCoreLoader().getPermission("Kick.Use")));
            }

        }
        return false;
    }
}
