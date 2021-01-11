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
import de.lacodev.rsystem.objects.MuteReasons;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_Mute implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {

			Player p = (Player)sender;

			if(MySQL.isConnected()) {
				if( p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Mute.Use")) ) {
					if( args.length == 2 ) {
						int id;
						if(SystemManager.getUUIDByName(args[0]) != null) {
							try {
								id = Integer.parseInt(args[1]);
							} catch (NumberFormatException e) {
								if(Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.NotValidID"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.NotValidID"));
								}
								return false;
							}
							if(BanManager.existsMuteID(id)) {
								if(!BanManager.isMuted(SystemManager.getUUIDByName(args[0]))) {
									if(!args[1].matches(p.getName())) {
										if(Main.getInstance().getConfig().getBoolean("Mute-System.Per-Reason-Permission.Enable")) {
											if(p.hasPermission(Main.getInstance().getConfig().getString("Mute-System.Per-Reason-Permission.Prefix-For-Permission") + args[1])) {
												if(BanManager.submitMute(SystemManager.getUUIDByName(args[0]), BanManager.getMuteReasonFromID(id), p.getUniqueId().toString())) {
													if(Main.getInstance().actionbar) {
														ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Created"));
													} else {
														p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Created"));
													}
												} else {
													if(Main.getInstance().actionbar) {
														ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Error"));
													} else {
														p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Error"));
													}
												}
											} else {
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getInstance().getConfig().getString("Mute-System.Per-Reason-Permission.Prefix-For-Permission") + args[1]));
                                    		}
										} else {
											if(BanManager.submitMute(SystemManager.getUUIDByName(args[0]), BanManager.getMuteReasonFromID(id), p.getUniqueId().toString())) {
												if(Main.getInstance().actionbar) {
													ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Created"));
												} else {
													p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Created"));
												}
											} else {
												if(Main.getInstance().actionbar) {
													ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Error"));
												} else {
													p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Error"));
												}
											}
										}
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Cannot-mute-self"));
									}
								} else {
									if(Main.getInstance().actionbar) {
										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Already-Muted"));
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Already-Muted"));
									}
								}
							} else {
								if(Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Reason-Not-Exists"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Reason-Not-Exists"));
								}
								p.sendMessage("");

								ArrayList<MuteReasons> reasons = BanManager.getMuteReasons();

								if(reasons.size() > 0) {
									for(int i = 0; i < reasons.size(); i++) {
										p.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "" + reasons.get(i).getName() + ChatColor.DARK_GRAY + " (" + ChatColor.YELLOW + "" + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + ""+ BanManager.getBanLength(reasons.get(i).getLength()) +ChatColor.DARK_GRAY + ")");
									}
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.No-Reasons"));
								}
								p.sendMessage("");
							}
						} else {
							if(Main.getInstance().actionbar) {
								ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Cannot-find-player"));
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Cannot-find-player"));
							}
						}

					} else {
						if (args.length == 1) {
							if (SystemManager.getUUIDByName(args[0]) != null) {
								if (!BanManager.isMuted(SystemManager.getUUIDByName(args[0]))) {
									BanManager.openPagedMuteInv(p, args[0], 1);
								} else {
									if (Main.getInstance().actionbar) {
										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Already-Muted"));
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Already-Muted"));
									}
								}

							} else {
								if (Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Cannot-find-player"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Cannot-find-player"));
								}
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Usage"));
							p.sendMessage("");
							ArrayList<MuteReasons> reasons = BanManager.getMuteReasons();

							if(reasons.size() > 0) {
								for(int i = 0; i < reasons.size(); i++) {
									p.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "" + reasons.get(i).getName() + ChatColor.DARK_GRAY+ " (" + ChatColor.YELLOW + "" + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + ""+ BanManager.getBanLength(reasons.get(i).getLength()) +ChatColor.DARK_GRAY + ")");
								}
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.No-Reasons"));
							}
							p.sendMessage("");
						}
					}
				} else {
	                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getPermissionNotice("Permissions.Mute.Use")));
	            }
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Connection.Notify"));
			}

		} else {
			if(MySQL.isConnected()) {
				if( args.length == 2 ) {
					int id;
					
					try {
						id = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.NotValidID"));
						return false;
					}

					if(SystemManager.getUUIDByName(args[0]) != null) {
						if(BanManager.existsMuteID(id)) {
							if(!BanManager.isMuted(SystemManager.getUUIDByName(args[0]))) {
								if(BanManager.submitMute(SystemManager.getUUIDByName(args[0]), BanManager.getMuteReasonFromID(id), "Console")) {
									sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Created"));
								} else {
									sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Error"));
								}
							} else {
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Already-Muted"));
							}
						} else {
							sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Reason-Not-Exists"));

							sender.sendMessage("");
							ArrayList<MuteReasons> reasons = BanManager.getMuteReasons();

							if(reasons.size() > 0) {
								for(int i = 0; i < reasons.size(); i++) {
									sender.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "" + reasons.get(i).getName() + ChatColor.DARK_GRAY+ " (" + ChatColor.YELLOW + "" + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + ""+ BanManager.getBanLength(reasons.get(i).getLength()) +ChatColor.DARK_GRAY + ")");
								}
							} else {
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.No-Reasons"));
							}
							sender.sendMessage("");
						}
					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Cannot-find-player"));
					}

				} else {
					sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Usage"));
					sender.sendMessage("");
					ArrayList<MuteReasons> reasons = BanManager.getMuteReasons();

					if(reasons.size() > 0) {
						for(int i = 0; i < reasons.size(); i++) {
							sender.sendMessage(Main.getPrefix() + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "" + reasons.get(i).getName() + ChatColor.DARK_GRAY+ " (" + ChatColor.YELLOW + "" + reasons.get(i).getID() + ChatColor.DARK_GRAY + " | " + ChatColor.YELLOW + ""+ BanManager.getBanLength(reasons.get(i).getLength()) +ChatColor.DARK_GRAY + ")");
						}
					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.No-Reasons"));
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
