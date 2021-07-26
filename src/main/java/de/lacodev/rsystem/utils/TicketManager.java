package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.enums.XMaterial;
import de.lacodev.rsystem.listeners.ChatListener;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RequiredArgsConstructor
public class TicketManager {

    private final StaffCore staffCore;

    // TicketManager for WebUI

    public void createTicket(Player player) {
        if (staffCore.getStaffCoreLoader().getSystemManager().existsWebDatabases()) {

            if (staffCore.getStaffCoreLoader().getSystemManager().isVerified(player.getUniqueId().toString())) {

                staffCore.getStaffCoreLoader().getMySQL().update(
                        "INSERT INTO ReportSystem_ticketdb(CREATOR_UUID) VALUES ('" + player.getUniqueId()
                                .toString() + "')");

                player.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.Create.Success"));

                ChatListener.spam.put(player, System.currentTimeMillis()
                        + staffCore.getStaffCoreLoader().getConfigProvider().getInt("TicketSystem.Cooldown-In-Seconds") * 1000);

                //Bukkit.getServer().getPluginManager().callEvent(new TicketCreateEvent(player));
            } else {

                player.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.Create.Error"));

            }
        } else {
            // DATENBANKEN EXESTIEREN NICHT
            //
            player.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.NoWebUITables"));
        }
    }

    public boolean hasTicket(Player player) {

        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
                    "SELECT CREATOR_UUID FROM ReportSystem_ticketdb WHERE CREATOR_UUID = '" + player
                            .getUniqueId().toString() + "'");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        return rs.getString("CREATOR_UUID") != null;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public void openTicketOverview(Player player, int page) throws SQLException {

        String title = staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.List.Inventory-Title");
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        Inventory inv = player.getServer().createInventory(null, 54, title);

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, Data.buildPlace());
        }

        inv.setItem(4, Data.buildItem(XMaterial.BARRIER, ChatColor.RED + "Loading..."));

        for (int i = 45; i < 54; i++) {
            inv.setItem(i, Data.buildPlace());
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                ArrayList<ItemStack> items = new ArrayList<>();

                ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
                        "SELECT * FROM ReportSystem_ticketdb WHERE CREATOR_UUID = '" + player.getUniqueId()
                                .toString() + "' ORDER BY id DESC");

                try {
                    while (rs.next()) {

                        items.add(Data.buildItemStackLore(XMaterial.PAPER,
                                ChatColor.RED + "Ticket " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "#" + rs
                                        .getInt("id") + ChatColor.DARK_GRAY + ")",

                                staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.List.Lore-1"),
                                staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.List.Lore-2")
                                        .replace("%team%", staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(rs.getString("TEAM_UUID"))),
                                staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.List.Lore-3").replace("%created%",
                                        rs.getString("reg_date").substring(0, rs.getString("reg_date").length() - 2))));

                    }
                } catch (SQLException e) {
                    inv.setItem(23, Data.buildItem(XMaterial.BARRIER,
                            staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Connection.Notify")));
                }

                staffCore.getStaffCoreLoader().getPageManager().getPage().remove(player);

                staffCore.getStaffCoreLoader().getPageManager().getPage().put(player, page);

                if (staffCore.getStaffCoreLoader().getPageManager().isProtectValid(items, page + 1, 36)) {
                    ItemStack stack = new ItemStack(Material.ARROW);
                    ItemMeta meta = stack.getItemMeta();
                    meta.setDisplayName(staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.Inventory.NextPage.Available"));
                    stack.setItemMeta(meta);

                    inv.setItem(52, stack);
                }

                if (staffCore.getStaffCoreLoader().getPageManager().isProtectValid(items, page - 1, 36)) {
                    ItemStack stack = new ItemStack(Material.ARROW);
                    ItemMeta meta = stack.getItemMeta();
                    meta.setDisplayName(
                            staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.Inventory.PreviousPage.Available"));
                    stack.setItemMeta(meta);

                    inv.setItem(46, stack);
                }

                for (ItemStack s : staffCore.getStaffCoreLoader().getPageManager().getPageProtect(items, page, 36)) {
                    inv.setItem(inv.firstEmpty(), s);
                }

                for (int i = 0; i < 9; i++) {
                    inv.setItem(i, Data.buildPlace());
                }
            }

        }.runTaskAsynchronously(staffCore);

        player.openInventory(inv);

    }


}
