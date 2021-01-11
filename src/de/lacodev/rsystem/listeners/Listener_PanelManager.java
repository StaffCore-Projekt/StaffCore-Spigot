package de.lacodev.rsystem.listeners;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.PageManager;
import de.lacodev.rsystem.utils.PanelManager;
import de.lacodev.rsystem.utils.SystemManager;
import de.lacodev.rsystem.utils.WebUIHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Listener_PanelManager implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPanel(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PanelManager manager = new PanelManager();
		
		if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Staffcore" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "MainMenu")) {
			e.setCancelled(true);
			
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().hasItemMeta()) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Report a Bug")) {
						p.closeInventory();
						
						p.sendMessage("");
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Bug-Report.Created"));
						
						TextComponent tc = new TextComponent();
						tc.setText(Main.getMSG("Messages.System.Bug-Report.Button"));
						URL url;
						try {
							url = new URL("https://bugreport.lacodev.de/?product=staffcore&version=" + Main.getInstance().getDescription().getVersion() + "&env=" + Bukkit.getVersion().replace("(", "").replace(")", "").replace(" ", "%20") + "&mysql=" + MySQL.isConnected());
							tc.setClickEvent(new ClickEvent(Action.OPEN_URL, url.toURI().toASCIIString()));
							tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.getMSG("Messages.System.Bug-Report.Hover-Text")).create()));
							
							p.spigot().sendMessage(tc);
						} catch (MalformedURLException | URISyntaxException e1) {
							e1.printStackTrace();
						}
						p.sendMessage("");
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Player-Management")) {
						manager.openPlayerManagement(p);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Settings")) {
						manager.openSettingsMenu(p);
					}
				}
			}
		}
		
		if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Settings")) {
			e.setCancelled(true);
			
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().hasItemMeta()) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
						manager.openMainMenu(p);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Download Latest Release")) {
						p.closeInventory();
						
						if(!Main.getInstance().latest) {
							SystemManager.downloadLatestVersion(p);
						} else {
							p.sendMessage(Main.getPrefix() + ChatColor.GRAY + "You are using the " + ChatColor.GREEN + "latest build" + ChatColor.GRAY + "!");
						}
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Download Latest Experimental")) {
						p.closeInventory();
						
						SystemManager.downloadExperimentalVersion(p);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "►")) {
						String lang = ChatColor.stripColor(e.getInventory().getItem(31).getItemMeta().getDisplayName());
						
						if(lang.matches("de")) {
							Main.getInstance().getConfig().set("General.Language", "us");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("us")) {
							Main.getInstance().getConfig().set("General.Language", "nl");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("nl")) {
							Main.getInstance().getConfig().set("General.Language", "dk");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("dk")) {
							Main.getInstance().getConfig().set("General.Language", "ru");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("ru")) {
							Main.getInstance().getConfig().set("General.Language", "es");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("es")) {
							Main.getInstance().getConfig().set("General.Language", "fr");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("fr")) {
							Main.getInstance().getConfig().set("General.Language", "it");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("it")) {
							Main.getInstance().getConfig().set("General.Language", "cz");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("cz")) {
							Main.getInstance().getConfig().set("General.Language", "fi");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("fi")) {
							Main.getInstance().getConfig().set("General.Language", "ee");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("ee")) {
							Main.getInstance().getConfig().set("General.Language", "hr");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("hr")) {
							Main.getInstance().getConfig().set("General.Language", "cn");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("cn")) {
							Main.getInstance().getConfig().set("General.Language", "de");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						Main.getTranslator().fetch(Main.getInstance().getConfig().getString("General.Language").substring(0, 2));
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "◄")) {
						String lang = ChatColor.stripColor(e.getInventory().getItem(31).getItemMeta().getDisplayName());
						
						if(lang.matches("de")) {
							Main.getInstance().getConfig().set("General.Language", "cn");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}				
						if(lang.matches("cn")) {
							Main.getInstance().getConfig().set("General.Language", "hr");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("hr")) {
							Main.getInstance().getConfig().set("General.Language", "ee");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("ee")) {
							Main.getInstance().getConfig().set("General.Language", "fi");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("fi")) {
							Main.getInstance().getConfig().set("General.Language", "cz");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("cz")) {
							Main.getInstance().getConfig().set("General.Language", "it");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("it")) {
							Main.getInstance().getConfig().set("General.Language", "fr");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("fr")) {
							Main.getInstance().getConfig().set("General.Language", "es");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("es")) {
							Main.getInstance().getConfig().set("General.Language", "ru");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("ru")) {
							Main.getInstance().getConfig().set("General.Language", "dk");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("dk")) {
							Main.getInstance().getConfig().set("General.Language", "nl");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("nl")) {
							Main.getInstance().getConfig().set("General.Language", "us");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						if(lang.matches("us")) {
							Main.getInstance().getConfig().set("General.Language", "de");
							Main.getInstance().saveConfig();
							Main.getInstance().reloadConfig();
							
							PanelManager.settings.setItem(31, manager.getLanguageHead());
						}
						
						Main.getTranslator().fetch(Main.getInstance().getConfig().getString("General.Language").substring(0, 2));
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Reload " + ChatColor.RED + "StaffCore")) {
						p.closeInventory();
						
						SystemManager.reloadStaffCore(p);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Setup " + ChatColor.RED + "StaffCore-UI")) {
						p.closeInventory();
						
						WebUIHandler.setupStaffCoreUI(p);
					}
				}
			}
		}
		
		if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Player - Management")) {
			e.setCancelled(true);
			
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().hasItemMeta()) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
						manager.openMainMenu(p);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Manage OnlinePlayers")) {
						manager.openOnlinePlayerMenu(p, 1);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Protect a player")) {
						manager.openProtectionInventory(p, 1);
					}
				}
			}
		}
		
		if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Manage OnlinePlayers")) {
			e.setCancelled(true);
			
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().hasItemMeta()) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
						manager.openPlayerManagement(p);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "◄")) {
						manager.openOnlinePlayerMenu(p, PageManager.page.get(p.getPlayer()) - 1);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "►")) {
						manager.openOnlinePlayerMenu(p, PageManager.page.get(p.getPlayer()) - 1);
					}
					if (e.getSlot() > 8 && e.getSlot() < 45) {
						String target = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
						
						manager.openPlayerMenu(p, target);
					}
				}
			}
		}
		
		if(e.getView().getTitle().endsWith(ChatColor.DARK_GRAY + "- " + ChatColor.RED + "StaffCore")) {
			e.setCancelled(true);
			
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().hasItemMeta()) {
					String s = e.getView().getTitle().replace(ChatColor.DARK_GRAY + "- " + ChatColor.RED + "StaffCore", "");
					String targetname = ChatColor.stripColor(s);
					
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
						manager.openOnlinePlayerMenu(p, PageManager.page.get(p.getPlayer()));
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Check Player")) {
						p.closeInventory();
						p.performCommand("check " + targetname);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Mute Player")) {
						p.closeInventory();
						p.performCommand("mute " + targetname);
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Ban Player")) {
						p.closeInventory();
						p.performCommand("ban " + targetname);
					}
				}
			}
		}
	}

}
