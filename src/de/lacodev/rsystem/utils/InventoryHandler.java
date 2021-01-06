package de.lacodev.rsystem.utils;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.lacodev.rsystem.enums.XMaterial;

public class InventoryHandler {
	
	public static ArrayList<Player> filter = new ArrayList<>();

	public static void openChatFilterSettings(Player p) {
		Inventory inv = p.getServer().createInventory(null, 27, "§7Settings §8- §eChatFilter");
		
		for(int i = 0; i < 27; i++) {
			inv.setItem(i, Data.buildPlace());
		}
		
		if(!filter.contains(p)) {
			inv.setItem(13, Data.buildItemStack(XMaterial.GRAY_DYE, "§3Add Cursed-Words §8| §7Deactivated", "§7Click to activate the chat input!", "§7When activated, just write the cursed words in chat!"));
		} else {
			inv.setItem(13, Data.buildItemStack(XMaterial.LIME_DYE, "§3Add Cursed-Words §8| §aActivated", "§7Click to activate the chat input!", "§7When activated, just write the cursed words in chat!"));
		}
		
		p.openInventory(inv);
	}
}
