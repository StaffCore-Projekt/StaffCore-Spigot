package de.lacodev.rsystem.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_Ban implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	       if (sender instanceof Player) {

	            Player p = (Player) sender;

	            if (MySQL.isConnected()) {
	                if (p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Ban.Use"))) {
	                    if (args.length == 2) {
	                        int id;
	                        if (SystemManager.getUUIDByName(args[0]) != null) {
	                            try {
	                                id = Integer.parseInt(args[1]);
	                            } catch (NumberFormatException e) {
	                                if (Main.getInstance().actionbar) {
	                                    ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.NotValidID"));
	                                } else {
	                                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.NotValidID"));
	                                }
	                                return false;
	                            }
	                            if (BanManager.existsBanID(id)) {
	                                if (!BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
	                                    if (!args[0].matches(p.getName())) {
	                                    	if(Main.getInstance().getConfig().getBoolean("Ban-System.Per-Reason-Permission.Enable")) {
	                                    		if(p.hasPermission(Main.getInstance().getConfig().getString("Ban-System.Per-Reason-Permission.Prefix-For-Permission") + args[1])) {
			                                        BanManager.submitBan(SystemManager.getUUIDByName(args[0]), BanManager.getBanReasonFromID(id), p.getUniqueId().toString());

			                                        if (Main.getInstance().actionbar) {
			                                            ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Created"));
			                                        } else {
			                                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Created"));
			                                        }
	                                    		} else {
	                                    			p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getInstance().getConfig().getString("Ban-System.Per-Reason-Permission.Prefix-For-Permission") + args[1]));
	                                    		}
	                                    	} else {
		                                        BanManager.submitBan(SystemManager.getUUIDByName(args[0]), BanManager.getBanReasonFromID(id), p.getUniqueId().toString());

		                                        if (Main.getInstance().actionbar) {
		                                            ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Created"));
		                                        } else {
		                                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Created"));
		                                        }
	                                    	}
	                                    } else {
	                                        p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Cannot-ban-self"));
	                                    }
	                                } else {
	                                    if (Main.getInstance().actionbar) {
	                                        ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Already-Banned"));
	                                    } else {
	                                        p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Already-Banned"));
	                                    }
	                                }
	                            } else {
	                                if (Main.getInstance().actionbar) {
	                                    ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Reason-Not-Exists"));
	                                } else {
	                                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Reason-Not-Exists"));
	                                }
	                                p.sendMessage("");

	                                ArrayList<BanReasons> reasons = BanManager.getBanReasons();

	                                if (reasons.size() > 0) {
	                                    for (int i = 0; i < reasons.size(); i++) {
	                                        p.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons.get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + ""+ BanManager.getBanLength(reasons.get(i).getLength()) +ChatColor.DARK_GRAY + ")");
	                                    }
	                                } else {
	                                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.No-Reasons"));
	                                }

	                                p.sendMessage("");
	                            }
	                        } else {
	                            if (Main.getInstance().actionbar) {
	                                ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Cannot-find-player"));
	                            } else {
	                                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Cannot-find-player"));
	                            }
	                        }

	                    } else {
	                        if (args.length == 1) {
	                            if (SystemManager.getUUIDByName(args[0]) != null) {
	                                if (!BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
	                                    BanManager.openPagedBanInv(p, args[0], 1);
	                                } else {
	                                    if (Main.getInstance().actionbar) {
	                                        ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Already-Banned"));
	                                    } else {
	                                        p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Already-Banned"));
	                                    }
	                                }

	                            } else {
	                                if (Main.getInstance().actionbar) {
	                                    ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Cannot-find-player"));
	                                } else {
	                                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Cannot-find-player"));
	                                }
	                            }

	                        } else {
	                            if (Main.getInstance().actionbar) {
	                                ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Usage"));
	                            } else {
	                                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Usage"));
	                            }
	                            p.sendMessage("");
	                            ArrayList<BanReasons> reasons = BanManager.getBanReasons();

	                            if (reasons.size() > 0) {
	                                for (int i = 0; i < reasons.size(); i++) {
	                                    p.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons.get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + ""+ BanManager.getBanLength(reasons.get(i).getLength()) +ChatColor.DARK_GRAY + ")");
	                                }
	                            } else {
	                                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.No-Reasons"));
	                            }
	                            p.sendMessage("");
	                        }

	                    }
	                } else {
		                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Ban.Use")));
		            }
	            } else {
	                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
	            }

	        } else {
	            if (MySQL.isConnected()) {
	                if (args.length == 2) {
	                    int id;
	                    if (SystemManager.getUUIDByName(args[0]) != null) {
	                        try {
	                            id = Integer.parseInt(args[1]);
	                        } catch (NumberFormatException e) {
	                            sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.NotValidID"));
	                            return false;
	                        }
	                        if (BanManager.existsBanID(id)) {
	                            if (!BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
	                                BanManager.submitBan(SystemManager.getUUIDByName(args[0]), BanManager.getBanReasonFromID(id), "Console");

	                                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Created"));
	                            } else {
	                                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Already-Banned"));
	                            }
	                        } else {

	                            sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Reason-Not-Exists"));

	                            sender.sendMessage("");

	                            ArrayList<BanReasons> reasons = BanManager.getBanReasons();
	                            if (reasons.size() > 0) {
	                                for (int i = 0; i < reasons.size(); i++) {
	                                    sender.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons.get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + ""+ BanManager.getBanLength(reasons.get(i).getLength()) +ChatColor.DARK_GRAY + ")");
	                                }
	                            } else {
	                                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.No-Reasons"));
	                            }

	                            sender.sendMessage("");
	                        }
	                    } else {
	                        sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Cannot-find-player"));
	                    }


	                } else {

	                    sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Usage"));

	                    sender.sendMessage("");
	                    ArrayList<BanReasons> reasons = BanManager.getBanReasons();

	                    if (reasons.size() > 0) {
	                        for (int i = 0; i < reasons.size(); i++) {
	                            sender.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + reasons.get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + ""+ BanManager.getBanLength(reasons.get(i).getLength()) +ChatColor.DARK_GRAY + ")");
	                        }
	                    } else {
	                        sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.No-Reasons"));
	                    }
	                    sender.sendMessage("");
	                }
	            } else {
	                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
	            }
	        }
		return true;
	}

}
