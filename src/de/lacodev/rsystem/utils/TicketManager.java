package de.lacodev.rsystem.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.api.events.TicketCreateEvent;
import de.lacodev.rsystem.enums.XMaterial;
import de.lacodev.rsystem.listeners.Listener_Chat;
import de.lacodev.rsystem.mysql.MySQL;

public class TicketManager {

	// TicketManager for WebUI
	
	public static void createTicket(Player player) {
		
		if(SystemManager.isVerified(player.getUniqueId().toString())) {
			
			MySQL.update("INSERT INTO ReportSystem_ticketdb(CREATOR_UUID) VALUES ('"+ player.getUniqueId().toString() +"')");
			
			player.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ticket-System.Create.Success"));
			
			Listener_Chat.spam.put(player, System.currentTimeMillis() + Main.getInstance().getConfig().getInt("TicketSystem.Cooldown-In-Seconds") * 1000);
			
			Bukkit.getServer().getPluginManager().callEvent(new TicketCreateEvent(player));
		} else {
			
			player.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ticket-System.Create.Error"));
			
		}
		
	}
	
	public static boolean hasTicket(Player player) {
		
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT CREATOR_UUID FROM ReportSystem_ticketdb WHERE CREATOR_UUID = '"+ player.getUniqueId().toString() +"'");
			try {
				if(rs != null) {
					if(rs.next()) {
						return rs.getString("CREATOR_UUID") != null;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
		
	}
	
	public static void openTicketOverview(Player player, int page) throws SQLException {
		
		String title = Main.getMSG("Messages.Ticket-System.List.Inventory-Title");
		if(title.length() > 32) {
			title = title.substring(0, 32);
		}
		
		Inventory inv = player.getServer().createInventory(null, 54, title);
		
		for(int i = 0; i < 9; i++) {
			inv.setItem(i, Data.buildPlace());
		}
		
		inv.setItem(4, Data.buildItem(XMaterial.BARRIER, "§cLoading..."));
		
		for(int i = 45; i < 54; i++) {
			inv.setItem(i, Data.buildPlace());
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				ArrayList<ItemStack> items = new ArrayList<>();
				
				ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_ticketdb WHERE CREATOR_UUID = '"+ player.getUniqueId().toString() +"' ORDER BY id DESC");
				
				try {
					while(rs.next()) {
						
						items.add(Data.buildItemStackLore(XMaterial.PAPER, "§cTicket §8(§7#"+ rs.getInt("id") +"§8)", Main.getMSG("Messages.Ticket-System.List.Lore-1"), 
								Main.getMSG("Messages.Ticket-System.List.Lore-2").replace("%team%", SystemManager.getUsernameByUUID(rs.getString("TEAM_UUID"))), 
								Main.getMSG("Messages.Ticket-System.List.Lore-3").replace("%created%", rs.getString("reg_date").substring(0, rs.getString("reg_date").length() - 2))));
						
					}
				} catch (SQLException e) {
					inv.setItem(23, Data.buildItem(XMaterial.BARRIER, Main.getMSG("Messages.System.No-Connection.Notify")));
				}
				
				if(PageManager.page.containsKey(player)) {
					PageManager.page.remove(player);
				}
				
				PageManager.page.put(player, page);
				
				if(PageManager.isProtectValid(items, page + 1, 36)) {
					ItemStack stack = new ItemStack(Material.ARROW);
					ItemMeta meta = stack.getItemMeta();
					meta.setDisplayName(Main.getMSG("Messages.Ticket-System.Inventory.NextPage.Available"));
					stack.setItemMeta(meta);
					
					inv.setItem(52, stack);
				}
				
				if(PageManager.isProtectValid(items, page - 1, 36)) {
					ItemStack stack = new ItemStack(Material.ARROW);
					ItemMeta meta = stack.getItemMeta();
					meta.setDisplayName(Main.getMSG("Messages.Ticket-System.Inventory.PreviousPage.Available"));
					stack.setItemMeta(meta);
					
					inv.setItem(46, stack);
				}
				
				for(ItemStack s : PageManager.getPageProtect(items, page, 36)) {
					inv.setItem(inv.firstEmpty(), s);
				}
				
				for(int i = 0; i < 9; i++) {
					inv.setItem(i, Data.buildPlace());
				}
			}
			
		}.runTaskAsynchronously(Main.getInstance());
		
		player.openInventory(inv);
		
	}

	
}
