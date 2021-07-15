package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.enums.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class InventoryHandler {

    public static ArrayList<Player> filter = new ArrayList<>();

    public static void openChatFilterSettings(Player p) {
        Inventory inv = p.getServer().createInventory(null, 27,
                ChatColor.GRAY + "Settings " + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW
                        + "ChatFilter");

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, Data.buildPlace());
        }

        if (!filter.contains(p)) {
            inv.setItem(13, Data.buildItemStack(XMaterial.GRAY_DYE,
                    ChatColor.DARK_AQUA + "Add Cursed-Words " + ChatColor.DARK_GRAY + "| " + ChatColor.GRAY
                            + "Deactivated", ChatColor.GRAY + "Click to activate the chat input!",
                    ChatColor.GRAY + "When activated, just write the cursed words in chat!"));
        } else {
            inv.setItem(13, Data.buildItemStack(XMaterial.LIME_DYE,
                    ChatColor.DARK_AQUA + "Add Cursed-Words " + ChatColor.DARK_GRAY + "| " + ChatColor.GREEN
                            + "Activated", ChatColor.GRAY + "Click to activate the chat input!",
                    ChatColor.GRAY + "When activated, just write the cursed words in chat!"));
        }

        p.openInventory(inv);
    }
}
