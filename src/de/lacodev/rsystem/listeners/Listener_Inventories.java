package de.lacodev.rsystem.listeners;

import java.sql.SQLException;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.enums.XMaterial;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.InventoryHandler;
import de.lacodev.rsystem.utils.PageManager;
import de.lacodev.rsystem.utils.PanelManager;
import de.lacodev.rsystem.utils.ReportManager;
import de.lacodev.rsystem.utils.SystemManager;
import de.lacodev.rsystem.utils.TicketManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class Listener_Inventories implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInv(InventoryClickEvent e) throws SQLException {
		Player p = (Player) e.getWhoClicked();
		
		if(e.getView().getTitle().startsWith(Main.getMSG("Messages.Ticket-System.List.Inventory-Title"))) {

			e.setCancelled(true);

			if(e.getCurrentItem() != null) {

				if (e.getSlot() > 8 && e.getSlot() < 45) {

					if(e.getCurrentItem().hasItemMeta()) {

						p.sendMessage("");
						p.sendMessage(Main.getPrefix() + e.getCurrentItem().getItemMeta().getDisplayName());
						
						String name = e.getCurrentItem().getItemMeta().getDisplayName();
						
						String id = name.substring(15, name.length() - 3);
						
						TextComponent tc = new TextComponent();
						tc.setText(Main.getPrefix() + Main.getMSG("Messages.Ticket-System.Open-In-Browser-Button"));
						tc.setClickEvent(new ClickEvent(Action.OPEN_URL, Main.getPermissionNotice("TicketSystem.WebInterface-URL") + id));
						
						p.spigot().sendMessage(tc);
						
						p.sendMessage("");

					}

				} else {

					int page = PageManager.page.get(((Player) e.getWhoClicked()).getPlayer());

					if (e.getSlot() == 46) {

						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Ticket-System.Inventory.PreviousPage.NotAvailable"))) {

							return;

						} else {

							TicketManager.openTicketOverview(p, page - 1);

						}

					} else if (e.getSlot() == 52) {

						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Ticket-System.Inventory.NextPage.NotAvailable"))) {

							return;

						} else {

							TicketManager.openTicketOverview(p, page + 1);

						}

					}



				}

			}

		}
		
		if(e.getView().getTitle().startsWith(Main.getMSG("Messages.Report-System.Inventory.Title"))) {

			e.setCancelled(true);

			

			if(e.getCurrentItem() != null) {

				if (e.getSlot() > 8 && e.getSlot() < 45) {

					if(e.getCurrentItem().hasItemMeta()) {

						String s = e.getView().getTitle().replace(Main.getMSG("Messages.Report-System.Inventory.Title"), "");

						String f = ChatColor.stripColor(s);



						Player target = Bukkit.getPlayer(f);



						if(target != null) {

							if(target != p) {

								ReportManager.createReport(p.getName(), target, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

								p.closeInventory();

							} else {

								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Cannot-report-self"));

							}

						} else {

							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.Target-offline"));

						}

					}

				} else {

					int page = PageManager.page.get(((Player) e.getWhoClicked()).getPlayer());

					String s = e.getView().getTitle().replace(Main.getMSG("Messages.Report-System.Inventory.Title"), "");

					String f = ChatColor.stripColor(s);



					Player target = Bukkit.getPlayer(f);


					if (e.getSlot() == 46) {

						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Report-System.Inventory.PreviousPage.NotAvailable"))) {

							return;

						} else {

							ReportManager.openPagedReportInv(p, target.getName(), page-1);

						}

					} else if (e.getSlot() == 52) {

						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Report-System.Inventory.NextPage.NotAvailable"))) {

							return;

						} else {

							ReportManager.openPagedReportInv(p, target.getName(), page+1);

						}

					}



				}

			}

		}
		
		if(e.getView().getTitle().startsWith(Main.getMSG("Messages.Ban-System.Inventory.Title"))) {
			e.setCancelled(true);

			if(e.getCurrentItem() != null) {
				if (e.getSlot() > 8 && e.getSlot() < 45) {
					if(e.getCurrentItem().hasItemMeta()) {
						String s = e.getView().getTitle().replace(Main.getMSG("Messages.Ban-System.Inventory.Title"), "");
						String reason = e.getCurrentItem().getItemMeta().getDisplayName();
						reason = ChatColor.stripColor(reason);
						String target = ChatColor.stripColor(s);

						if (!BanManager.isBanned(SystemManager.getUUIDByName(target))) {
							if(target != p.getName()) {
								if(Main.getInstance().getConfig().getBoolean("Ban-System.Per-Reason-Permission.Toggle")) {
                            		if(p.hasPermission(Main.getInstance().getConfig().getString("Ban-System.Per-Reason-Permission.Prefix-For-Permission") + BanManager.getIDFromBanReason(reason))) {
        								BanManager.submitBan(SystemManager.getUUIDByName(target), reason, p.getUniqueId().toString());
        								if(Main.getInstance().actionbar) {
        									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Created"));
        								} else {
        									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Created"));
        								}
        								p.closeInventory();
                            		} else {
                            			p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getInstance().getConfig().getString("Ban-System.Per-Reason-Permission.Prefix-For-Permission") + BanManager.getIDFromBanReason(reason)));
                            			p.closeInventory();
                            		}
								} else {
									BanManager.submitBan(SystemManager.getUUIDByName(target), reason, p.getUniqueId().toString());
									if(Main.getInstance().actionbar) {
										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban-System.Created"));
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban-System.Created"));
									}
									p.closeInventory();	
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
					}
				} else {
					int page = PageManager.page.get(((Player) e.getWhoClicked()).getPlayer());
					String s = e.getView().getTitle().replace(Main.getMSG("Messages.Ban-System.Inventory.Title"), "");
					String target = ChatColor.stripColor(s);
					if (e.getSlot() == 46) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Ban-System.Inventory.PreviousPage.NotAvailable"))) {
							return;
						} else {
							BanManager.openPagedBanInv(p, target, page-1);
						}
					} else if (e.getSlot() == 52) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Ban-System.Inventory.NextPage.NotAvailable"))) {
							return;
						} else {
							BanManager.openPagedBanInv(p, target, page+1);
						}
					}

				}
			}
		}
		
		if(e.getView().getTitle().startsWith(ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Protections")) {
			e.setCancelled(true);
			PanelManager manager = new PanelManager();

			if(e.getCurrentItem() != null) {
				if (e.getSlot() > 8 && e.getSlot() < 45) {
					if(e.getCurrentItem().hasItemMeta()) {
						if(e.getCurrentItem().getItemMeta().getDisplayName().endsWith(ChatColor.DARK_GRAY + " | " + ChatColor.RED + "Unprotected")) {
							String s = e.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.DARK_GRAY + " | " + ChatColor.RED + "Unprotected", "");
							String playername = ChatColor.stripColor(s);
							
							SystemManager.changeProtectionState(playername);
							
							ItemStack player = XMaterial.PLAYER_HEAD.parseItem();
							SkullMeta meta = (SkullMeta) player.getItemMeta();
							meta.setOwner(playername);
							if(SystemManager.isProtected(SystemManager.getUUIDByName(playername))) {
								meta.setDisplayName(ChatColor.GRAY + playername + ChatColor.DARK_GRAY + " | " + ChatColor.GREEN + "Protected");
								meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
								meta.addEnchant(Enchantment.DURABILITY, 1, true);
							} else {
								meta.setDisplayName(ChatColor.GRAY + playername + ChatColor.DARK_GRAY + " | " + ChatColor.RED + "Unprotected");
							}
							player.setItemMeta(meta);

							e.getClickedInventory().setItem(e.getSlot(), player);
							
						} else if(e.getCurrentItem().getItemMeta().getDisplayName().endsWith(ChatColor.DARK_GRAY + " | " + ChatColor.GREEN + "Protected")) {
							String s = e.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.DARK_GRAY + " | " + ChatColor.GREEN + "Protected", "");
							String playername = ChatColor.stripColor(s);
							
							SystemManager.changeProtectionState(playername);
							
							ItemStack player = XMaterial.PLAYER_HEAD.parseItem();
							SkullMeta meta = (SkullMeta) player.getItemMeta();
							meta.setOwner(playername);
							if(SystemManager.isProtected(SystemManager.getUUIDByName(playername))) {
								meta.setDisplayName(ChatColor.GRAY + playername + ChatColor.DARK_GRAY + " | " + ChatColor.GREEN + "Protected");
								meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
								meta.addEnchant(Enchantment.DURABILITY, 1, true);
							} else {
								meta.setDisplayName(ChatColor.GRAY + playername + ChatColor.DARK_GRAY + " | " + ChatColor.RED + "Unprotected");
							}
							player.setItemMeta(meta);
								
							e.getClickedInventory().setItem(e.getSlot(), player);
						}
					}
				} else {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
						manager.openPlayerManagement(p);
					}
					int page = PageManager.page.get(((Player) e.getWhoClicked()).getPlayer());
					if (e.getSlot() == 46) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Ban-System.Inventory.PreviousPage.NotAvailable"))) {
							return;
						} else {
							manager.openProtectionInventory(p, page - 1);
						}
					} else if (e.getSlot() == 52) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Ban-System.Inventory.NextPage.NotAvailable"))) {
							return;
						} else {
							manager.openProtectionInventory(p, page + 1);
						}
					}

				}
			}
		}

		if(e.getView().getTitle().startsWith(Main.getMSG("Messages.Mute-System.Inventory.Title"))) {
			e.setCancelled(true);

			if(e.getCurrentItem() != null) {
				if (e.getSlot() > 8 && e.getSlot() < 45) {
					if(e.getCurrentItem().hasItemMeta()) {
						String s = e.getView().getTitle().replace(Main.getMSG("Messages.Mute-System.Inventory.Title"), "");
						String reason = e.getCurrentItem().getItemMeta().getDisplayName();
						reason = ChatColor.stripColor(reason);
						String target = ChatColor.stripColor(s);

						if (!BanManager.isMuted(SystemManager.getUUIDByName(target))) {
							if(target != p.getName()) {
								if(Main.getInstance().getConfig().getBoolean("Mute-System.Per-Reason-Permission.Toggle")) {
									if(p.hasPermission(Main.getInstance().getConfig().getString("Mute-System.Per-Reason-Permission.Prefix-For-Permission") + BanManager.getIDFromMuteReason(reason))) {
										if(BanManager.submitMute(SystemManager.getUUIDByName(target), reason, p.getUniqueId().toString())) {
											if(Main.getInstance().actionbar) {
												ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Created"));
											} else {
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Created"));
											}
											p.closeInventory();	
										} else {
											if(Main.getInstance().actionbar) {
												ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Error"));
											} else {
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Error"));
											}
											p.closeInventory();	
										}
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.No-Permission").replace("%permission%", Main.getInstance().getConfig().getString("Ban-System.Per-Reason-Permission.Prefix-For-Permission") + BanManager.getIDFromMuteReason(reason)));
										p.closeInventory();	
                            		}
								} else {
									if(BanManager.submitMute(SystemManager.getUUIDByName(target), reason, p.getUniqueId().toString())) {
										if(Main.getInstance().actionbar) {
											ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Created"));
										} else {
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Created"));
										}
										p.closeInventory();	
									} else {
										if(Main.getInstance().actionbar) {
											ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Error"));
										} else {
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Error"));
										}
									}
								}
								p.closeInventory();
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Cannot-mute-self"));
							}
						} else {
							if (Main.getInstance().actionbar) {
								ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute-System.Already-Muted"));
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute-System.Already-Muted"));
							}
						}
					}
				} else {
					int page = PageManager.page.get(((Player) e.getWhoClicked()).getPlayer());
					String s = e.getView().getTitle().replace(Main.getMSG("Messages.Mute-System.Inventory.Title"), "");
					String target = ChatColor.stripColor(s);
					if (e.getSlot() == 46) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Mute-System.Inventory.PreviousPage.NotAvailable"))) {
							return;
						} else {
							BanManager.openPagedMuteInv(p, target, page-1);
						}
					} else if (e.getSlot() == 52) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Mute-System.Inventory.NextPage.NotAvailable"))) {
							return;
						} else {
							BanManager.openPagedMuteInv(p, target, page+1);
						}
					}

				}
			}
		}
		
		if(e.getView().getTitle().startsWith(ChatColor.GRAY + "Settings " + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + "ChatFilter")) {
			e.setCancelled(true);
			
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().hasItemMeta()) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_AQUA + "Add Cursed-Words " + ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + "Deactivated")) {
						InventoryHandler.filter.add(p);
						p.closeInventory();
						p.sendMessage(Main.getPrefix() + ChatColor.GRAY + "You are now able to write your curse words which will be blocked afterwards!");
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_AQUA + "Add Cursed-Words " + ChatColor.DARK_GRAY + "| " + ChatColor.GREEN + "Activated")) {
						InventoryHandler.filter.remove(p);
						p.closeInventory();
						p.sendMessage(Main.getPrefix() + ChatColor.GRAY + "You can now use the chat as before!");
					}
				}
			}
		}
	}

}
