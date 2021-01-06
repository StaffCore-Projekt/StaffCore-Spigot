package de.lacodev.rsystem.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.enums.XMaterial;
import de.lacodev.rsystem.mysql.MySQL;

public class PanelManager {
	
	public PanelManager() {
		super();
	}
	
	private Inventory mainmenu;
	private Inventory playermanagement;
	private Inventory onlineplayermenu;
	private Inventory protect;
	private Inventory playermenu;
	
	public static Inventory settings;
	
	public void openMainMenu(Player player) {
		mainmenu = player.getServer().createInventory(null, 27, "§cStaffCore §8- §7MainMenu");
		
		for(int i = 0; i < mainmenu.getSize(); i++) {
			mainmenu.setItem(i, Data.buildPlace());
		}
		
		mainmenu.setItem(10, Data.buildPlayerHead("§7Player-Management", player.getName()));
		mainmenu.setItem(13, Data.buildItem(XMaterial.COMMAND_BLOCK.parseMaterial(), 1, 0, "§7Settings"));
		
		mainmenu.setItem(26, Data.buildItemStackLore(Material.PAPER, 1, 0, "§cReport a Bug", "§7If you find any error which", "§7might be caused by §cStaffCore","§cReport it with clicking on this item"));
		
		player.openInventory(mainmenu);
	}
	
	public void openPlayerManagement(Player player) {
		playermanagement = player.getServer().createInventory(null, 27, "§cPlayer-Management");
		
		for(int i = 0; i < playermanagement.getSize(); i++) {
			playermanagement.setItem(i, Data.buildPlace());
		}
		
		playermanagement.setItem(13, Data.buildPlayerHead("§7Manage OnlinePlayers", player.getName(), "§7Online §8» §c" + Bukkit.getOnlinePlayers().size() + " Player(s)"));
		playermanagement.setItem(16, Data.buildItem(XMaterial.DIAMOND_CHESTPLATE, "§7Protect a player"));
		
		ItemStack menu = Data.getHead("arrow");
		ItemMeta meta = menu.getItemMeta();
		meta.setDisplayName("§c◄ Go back");
		menu.setItemMeta(meta);
		
		playermanagement.setItem(18, menu);
		
		player.openInventory(playermanagement);
	}
	
	public void openOnlinePlayerMenu(Player player, int page) {
		onlineplayermenu = player.getServer().createInventory(null, 54, "§cManage OnlinePlayers");
		
		for(int i = 0; i < 9; i++) {
			onlineplayermenu.setItem(i, Data.buildPlace());
		}
		for(int i = 45; i < 54; i++) {
			onlineplayermenu.setItem(i, Data.buildPlace());
		}
		
		ArrayList<ItemStack> items = new ArrayList<>();
		for(Player all : Bukkit.getOnlinePlayers()) {
			items.add(Data.buildPlayerHead("§7" + all.getName(), all.getName()));
		}
		
		if (PageManager.page.containsKey(player.getPlayer())) {
			PageManager.page.remove(player.getPlayer());
		}
		PageManager.page.put(player.getPlayer(), page);
		
		ItemStack forward = Data.getHead("oak_arrow_right");
		ItemMeta fmeta = forward.getItemMeta();
		fmeta.setDisplayName("§a►");
		forward.setItemMeta(fmeta);
		
		ItemStack back = Data.getHead("oak_arrow_left");
		ItemMeta bmeta = back.getItemMeta();
		bmeta.setDisplayName("§a◄");
		back.setItemMeta(bmeta);
		
		if (PageManager.isProtectValid(items, page - 1, 36)) {
			onlineplayermenu.setItem(46, back);
		}

		if (PageManager.isProtectValid(items, page + 1, 36)) {
			onlineplayermenu.setItem(52, forward);
		}


		for (ItemStack item : PageManager.getPageProtect(items, page, 36)) {
			onlineplayermenu.setItem(onlineplayermenu.firstEmpty(), item);
		}
		
		ItemStack menu = Data.getHead("arrow");
		ItemMeta meta = menu.getItemMeta();
		meta.setDisplayName("§c◄ Go back");
		menu.setItemMeta(meta);
		
		onlineplayermenu.setItem(45, menu);
		
		player.openInventory(onlineplayermenu);
	}
	
	public void openProtectionInventory(Player p, int page) {
		String title = "§cStaffCore §8- §7Protections";
		if(title.length() > 32) {
			title = title.substring(0, 32);
		}
		protect = p.getServer().createInventory(null, 54, title);


		new BukkitRunnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if(MySQL.isConnected()) {

					ArrayList<ItemStack> rows = new ArrayList<>();
					ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_playerdb");
					try {
						while(rs.next()) {
							
							ItemStack player = XMaterial.PLAYER_HEAD.parseItem();
							SkullMeta meta = (SkullMeta) player.getItemMeta();
							meta.setOwner(rs.getString("PLAYERNAME"));
							if(SystemManager.isProtected(rs.getString("UUID"))) {
								meta.setDisplayName("§7" + rs.getString("PLAYERNAME") + " §8| §aProtected");
								meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
								meta.addEnchant(Enchantment.DURABILITY, 1, true);
							} else {
								meta.setDisplayName("§7" + rs.getString("PLAYERNAME") + " §8| §cUnprotected");
							}
							player.setItemMeta(meta);
							
							rows.add(player);
							
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					for (int i2 = 0; i2 < 9; i2++) {
						protect.setItem(i2, Data.buildPlace());

					}
					if (PageManager.page.containsKey(p.getPlayer())) {
						PageManager.page.remove(p.getPlayer());
					}
					PageManager.page.put(p.getPlayer(), page);

					for (int i2 = 45; i2 < 54; i2++) {
						protect.setItem(i2, Data.buildPlace());
					}
					
					ItemStack forward = Data.getHead("oak_arrow_right");
					ItemMeta fmeta = forward.getItemMeta();
					fmeta.setDisplayName("§a►");
					forward.setItemMeta(fmeta);
					
					ItemStack back = Data.getHead("oak_arrow_left");
					ItemMeta bmeta = back.getItemMeta();
					bmeta.setDisplayName("§a◄");
					back.setItemMeta(bmeta);
					
					if (PageManager.isProtectValid(rows, page - 1, 36)) {
						protect.setItem(46, back);
					}

					if (PageManager.isProtectValid(rows, page + 1, 36)) {
						protect.setItem(52, forward);
					}


					for (ItemStack item : PageManager.getPageProtect(rows, page, 36)) {
						protect.setItem(protect.firstEmpty(), item);
					}
				} else {
					protect.setItem(13, Data.buildItem(Material.BARRIER, 1, 0, "§cNo Connection"));
				}
				
				ItemStack menu = Data.getHead("arrow");
				ItemMeta meta = menu.getItemMeta();
				meta.setDisplayName("§c◄ Go back");
				menu.setItemMeta(meta);
				
				protect.setItem(45, menu);
			}
			
		}.runTaskAsynchronously(Main.getInstance());

		p.openInventory(protect);
	}
	
	public void openPlayerMenu(Player p, String target) {
		playermenu = p.getServer().createInventory(null, 9, target + "§8- §cStaffCore");
		
		for(int i = 0; i < 9; i++) {
			playermenu.setItem(i, Data.buildPlace());
		}
		
		ItemStack menu = Data.getHead("arrow");
		ItemMeta meta = menu.getItemMeta();
		meta.setDisplayName("§c◄ Go back");
		menu.setItemMeta(meta);
		
		playermenu.setItem(0, menu);
		
		playermenu.setItem(3, Data.buildItemStack(XMaterial.EXPERIENCE_BOTTLE, "§cCheck Player", "§7Get all important information", "§7about the player"));
		playermenu.setItem(5, Data.buildItem(XMaterial.BOOK, "§cMute Player"));
		playermenu.setItem(7, Data.buildItem(XMaterial.RED_DYE, "§cBan Player"));
		
		p.openInventory(playermenu);
	}
	
	public void openSettingsMenu(Player p) {
		settings = p.getServer().createInventory(null, 54, "§cStaffCore §8- §7Settings");
		
		for(int i = 0; i < 54; i++) {
			settings.setItem(i, Data.buildPlace());
		}
		
		settings.setItem(12, Data.buildHead("globe", "§aDownload Latest Release","§7This will download the latest release","§7which has been §asuccessfully tested §7in it's current state"));
		settings.setItem(14, Data.buildHead("warn", "§6Download Latest Experimental","§7This will download the latest dev-build","§7which is §cnot finished §7in it's current state"));
		
		settings.setItem(29, Data.buildHead("oak_arrow_left", "§a◄"));
		
		settings.setItem(31, getLanguageHead());
		
		settings.setItem(33, Data.buildHead("oak_arrow_right", "§a►"));
		
		settings.setItem(45, Data.buildHead("arrow", "§c◄ Go back"));
		settings.setItem(49, Data.buildHead("reload", "§7Reload §cStaffCore", "§7This will completly restart StaffCore", "§7including MySQL reconnect and Config reload"));
		settings.setItem(53, Data.buildHead("webui", "§7Setup §cStaffCore-UI", "§7This will create all MySQL-Tables which", "§7are required for StaffCore-UI to work!"));
		
		p.openInventory(settings);
	}
	
	public ItemStack getLanguageHead() {
		return Data.buildHead(Main.getInstance().getConfig().getString("General.Language"), "§a" + Main.getInstance().getConfig().getString("General.Language"));
	}

	public Inventory getMainMenu() {
		return mainmenu;
	}
	
	public Inventory getPlayerManagement() {
		return playermanagement;
	}
	
	public Inventory getOnlinePlayerMenu() {
		return onlineplayermenu;
	}
	
	public Inventory getProtectMenu() {
		return protect;
	}
	
	public Inventory getPlayerMenu() {
		return playermenu;
	}

}
