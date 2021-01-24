package de.lacodev.rsystem.commands;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.SettingsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Maintenance implements CommandExecutor {
    private SettingsManager manager = new SettingsManager();
    private String key = "maintenance";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length != 1) {
                if (p.hasPermission(Main.getPermissionNotice("Permissions.Maintenance.Change")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {

                    if (manager.isKey(key)){
                        if (manager.getBoolean(key)){
                            p.sendMessage(Main.getPrefix() + "The Maintenance Mode is Enabled!");
                        }else{
                            p.sendMessage(Main.getPrefix() + "The Maintenance Mode is Disabled!");
                        }
                    }else{
                        setState(false);
                        p.sendMessage(Main.getPrefix() + "The Maintenance Mode is Disabled!");
                    }

                } else {
                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Maintenance.Change")));
                }
            }else {
                if (p.hasPermission(Main.getPermissionNotice("Permissions.Maintenance.Change")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {

                    switch (args[0].toLowerCase()){
                        case "on":
                            if (setState(true)){
                                p.sendMessage(Main.getPrefix() + "The Maintenance Mode is now Enabled!");
                            }else{
                                p.sendMessage(Main.getPrefix() + "The Maintenance Mode is already Enabled!");
                            }
                            break;
                        case "off":
                            if (setState(false)){
                                p.sendMessage(Main.getPrefix() + "The Maintenance Mode is now Disabled!");
                            }else{
                                p.sendMessage(Main.getPrefix() + "The Maintenance Mode is already Disabled!");
                            }
                            break;
                        case "info":
                            if (manager.isKey(key)){
                                if (manager.getBoolean(key)){
                                    p.sendMessage(Main.getPrefix() + "The Maintenance Mode is Enabled!");
                                }else{
                                    p.sendMessage(Main.getPrefix() + "The Maintenance Mode is Disabled!");
                                }
                            }else{
                                setState(false);
                                p.sendMessage(Main.getPrefix() + "The Maintenance Mode is Disabled!");
                            }

                            break;
                        default:
                            p.sendMessage(Main.getPrefix() + "TRY: /maintenance [ on | off | info ]");
                            break;
                    }

                }else{
                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Maintenance.Change")));
                }
            }
        }
        return false;
    }

    public boolean setState(boolean value){
        if (value) {
            if (manager.isKey(key)) {
                if (manager.getBoolean(key)) {
                    //bereits an!
                    return false;
                } else {
                    manager.update(key, true);
                    return true;
                }
            } else {
                manager.set(key, true);
                return true;
            }
        }else{
            if (manager.isKey(key)){
                if (!manager.getBoolean(key)) {
                    //bereits aus!
                    return false;
                } else {
                    manager.update(key, false);
                    return true;
                }
            } else {
                manager.set(key, true);
                return true;
            }
        }
    }
}
