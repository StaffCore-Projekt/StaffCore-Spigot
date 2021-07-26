package de.lacodev.rsystem.listeners;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.enums.XMaterial;
import de.lacodev.rsystem.utils.*;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
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

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class InventoryHandler implements Listener {

    private final StaffCore staffCore;

    public InventoryHandler(StaffCore staffCore) {
        this.staffCore = staffCore;
        Bukkit.getPluginManager().registerEvents(this, staffCore);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInv(InventoryClickEvent e) throws SQLException {
        Player p = (Player) e.getWhoClicked();

        if (e.getView().getTitle()
                .startsWith(staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.List.Inventory-Title"))) {

            e.setCancelled(true);

            if (e.getCurrentItem() != null) {

                if (e.getSlot() > 8 && e.getSlot() < 45) {

                    if (e.getCurrentItem().hasItemMeta()) {

                        p.sendMessage("");
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + e.getCurrentItem().getItemMeta().getDisplayName());

                        String name = e.getCurrentItem().getItemMeta().getDisplayName();

                        String id = name.substring(15, name.length() - 3);

                        TextComponent tc = new TextComponent();
                        tc.setText(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.Open-In-Browser-Button"));
                        tc.setClickEvent(new ClickEvent(Action.OPEN_URL,
                                staffCore.getStaffCoreLoader().getPermission("TicketSystem.WebInterface-URL") + id));

                        p.spigot().sendMessage(tc);

                        p.sendMessage("");

                    }

                } else {

                    int page = staffCore.getStaffCoreLoader().getPageManager().getPage().get(((Player) e.getWhoClicked()).getPlayer());

                    if (e.getSlot() == 46) {

                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
                                staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.Inventory.PreviousPage.NotAvailable"))) {

                            return;

                        } else {
                            staffCore.getStaffCoreLoader().getTicketManager().openTicketOverview(p, page - 1);

                        }

                    } else if (e.getSlot() == 52) {

                        if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .equals(staffCore.getStaffCoreLoader().getMessage("Messages.Ticket-System.Inventory.NextPage.NotAvailable"))) {

                            return;

                        } else {

                            staffCore.getStaffCoreLoader().getTicketManager().openTicketOverview(p, page + 1);

                        }

                    }


                }

            }

        }

        if (e.getView().getTitle().startsWith(staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.Title"))) {

            e.setCancelled(true);

            if (e.getCurrentItem() != null) {

                if (e.getSlot() > 8 && e.getSlot() < 45) {

                    if (e.getCurrentItem().hasItemMeta()) {

                        String s = e.getView().getTitle()
                                .replace(staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.Title"), "");

                        String f = ChatColor.stripColor(s);

                        Player target = Bukkit.getPlayer(f);

                        if (target != null) {

                            if (target != p) {

                                staffCore.getStaffCoreLoader().getReportManager().createReport(p.getName(), target,
                                        ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

                                p.closeInventory();

                            } else {

                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Cannot-report-self"));

                            }

                        } else {

                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Target-offline"));

                        }

                    }

                } else {

                    int page = staffCore.getStaffCoreLoader().getPageManager().getPage().get(((Player) e.getWhoClicked()).getPlayer());

                    String s = e.getView().getTitle()
                            .replace(staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.Title"), "");

                    String f = ChatColor.stripColor(s);

                    Player target = Bukkit.getPlayer(f);

                    if (e.getSlot() == 46) {

                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(
                                staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.PreviousPage.NotAvailable"))) {

                            return;

                        } else {

                            staffCore.getStaffCoreLoader().getReportManager().openPagedReportInv(p, target.getName(), page - 1);

                        }

                    } else if (e.getSlot() == 52) {

                        if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .equals(staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.NextPage.NotAvailable"))) {

                            return;

                        } else {

                            staffCore.getStaffCoreLoader().getReportManager().openPagedReportInv(p, target.getName(), page + 1);

                        }

                    }


                }

            }

        }

        if (e.getView().getTitle().startsWith(staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Inventory.Title"))) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getSlot() > 8 && e.getSlot() < 45) {
                    if (e.getCurrentItem().hasItemMeta()) {
                        String s = e.getView().getTitle()
                                .replace(staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Inventory.Title"), "");
                        String reason = e.getCurrentItem().getItemMeta().getDisplayName();
                        reason = ChatColor.stripColor(reason);
                        String target = ChatColor.stripColor(s);

                        if (!staffCore.getStaffCoreLoader().getBanManager().isBanned(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target))) {
                            if (target != p.getName()) {
                                if (staffCore.getStaffCoreLoader().getConfigProvider()
                                        .getBoolean("Ban-System.Per-Reason-Permission.Toggle")) {
                                    if (p.hasPermission(staffCore.getStaffCoreLoader().getConfigProvider()
                                            .getString("Ban-System.Per-Reason-Permission.Prefix-For-Permission")
                                            + staffCore.getStaffCoreLoader().getBanManager().getIDFromBanReason(reason))) {
                                        staffCore.getStaffCoreLoader().getBanManager().submitBan(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target), reason,
                                                p.getUniqueId().toString());
                                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                            ActionBarAPI.sendActionBar(p,
                                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Created"));
                                        } else {
                                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Created"));
                                        }
                                        p.closeInventory();
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                                                .replace("%permission%", staffCore.getStaffCoreLoader().getConfigProvider()
                                                        .getString("Ban-System.Per-Reason-Permission.Prefix-For-Permission")
                                                        + staffCore.getStaffCoreLoader().getBanManager().getIDFromBanReason(reason)));
                                        p.closeInventory();
                                    }
                                } else {
                                    staffCore.getStaffCoreLoader().getBanManager().submitBan(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target), reason,
                                            p.getUniqueId().toString());
                                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                        ActionBarAPI.sendActionBar(p,
                                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Created"));
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Created"));
                                    }
                                    p.closeInventory();
                                }
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Cannot-ban-self"));
                            }
                        } else {
                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                ActionBarAPI.sendActionBar(p,
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Already-Banned"));
                            } else {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Already-Banned"));
                            }
                        }
                    }
                } else {
                    int page = staffCore.getStaffCoreLoader().getPageManager().getPage().get(((Player) e.getWhoClicked()).getPlayer());
                    String s = e.getView().getTitle()
                            .replace(staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Inventory.Title"), "");
                    String target = ChatColor.stripColor(s);
                    if (e.getSlot() == 46) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .equals(staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Inventory.PreviousPage.NotAvailable"))) {
                            return;
                        } else {
                            staffCore.getStaffCoreLoader().getBanManager().openPagedBanInv(p, target, page - 1);
                        }
                    } else if (e.getSlot() == 52) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .equals(staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Inventory.NextPage.NotAvailable"))) {
                            return;
                        } else {
                            staffCore.getStaffCoreLoader().getBanManager().openPagedBanInv(p, target, page + 1);
                        }
                    }

                }
            }
        }

        if (e.getView().getTitle().startsWith(
                ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY
                        + "Protections")) {
            e.setCancelled(true);
            PanelManager manager = staffCore.getStaffCoreLoader().getPanelManager();

            if (e.getCurrentItem() != null) {
                if (e.getSlot() > 8 && e.getSlot() < 45) {
                    if (e.getCurrentItem().hasItemMeta()) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .endsWith(ChatColor.DARK_GRAY + " | " + ChatColor.RED + "Unprotected")) {
                            String s = e.getCurrentItem().getItemMeta().getDisplayName()
                                    .replace(ChatColor.DARK_GRAY + " | " + ChatColor.RED + "Unprotected", "");
                            String playername = ChatColor.stripColor(s);

                            staffCore.getStaffCoreLoader().getSystemManager().changeProtectionState(playername);

                            ItemStack player = XMaterial.PLAYER_HEAD.parseItem();
                            SkullMeta meta = (SkullMeta) player.getItemMeta();
                            meta.setOwner(playername);
                            if (staffCore.getStaffCoreLoader().getSystemManager().isProtected(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(playername))) {
                                meta.setDisplayName(
                                        ChatColor.GRAY + playername + ChatColor.DARK_GRAY + " | " + ChatColor.GREEN
                                                + "Protected");
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                            } else {
                                meta.setDisplayName(
                                        ChatColor.GRAY + playername + ChatColor.DARK_GRAY + " | " + ChatColor.RED
                                                + "Unprotected");
                            }
                            player.setItemMeta(meta);

                            e.getClickedInventory().setItem(e.getSlot(), player);

                        } else if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .endsWith(ChatColor.DARK_GRAY + " | " + ChatColor.GREEN + "Protected")) {
                            String s = e.getCurrentItem().getItemMeta().getDisplayName()
                                    .replace(ChatColor.DARK_GRAY + " | " + ChatColor.GREEN + "Protected", "");
                            String playername = ChatColor.stripColor(s);

                            staffCore.getStaffCoreLoader().getSystemManager().changeProtectionState(playername);

                            ItemStack player = XMaterial.PLAYER_HEAD.parseItem();
                            SkullMeta meta = (SkullMeta) player.getItemMeta();
                            meta.setOwner(playername);
                            if (staffCore.getStaffCoreLoader().getSystemManager().isProtected(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(playername))) {
                                meta.setDisplayName(
                                        ChatColor.GRAY + playername + ChatColor.DARK_GRAY + " | " + ChatColor.GREEN
                                                + "Protected");
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                            } else {
                                meta.setDisplayName(
                                        ChatColor.GRAY + playername + ChatColor.DARK_GRAY + " | " + ChatColor.RED
                                                + "Unprotected");
                            }
                            player.setItemMeta(meta);

                            e.getClickedInventory().setItem(e.getSlot(), player);
                        }
                    }
                } else {
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
                        manager.openPlayerManagement(p);
                    }
                    int page = staffCore.getStaffCoreLoader().getPageManager().getPage().get(((Player) e.getWhoClicked()).getPlayer());
                    if (e.getSlot() == 46) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .equals(staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Inventory.PreviousPage.NotAvailable"))) {
                            return;
                        } else {
                            manager.openProtectionInventory(p, page - 1);
                        }
                    } else if (e.getSlot() == 52) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .equals(staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Inventory.NextPage.NotAvailable"))) {
                            return;
                        } else {
                            manager.openProtectionInventory(p, page + 1);
                        }
                    }

                }
            }
        }

        if (e.getView().getTitle().startsWith(staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Inventory.Title"))) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getSlot() > 8 && e.getSlot() < 45) {
                    if (e.getCurrentItem().hasItemMeta()) {
                        String s = e.getView().getTitle()
                                .replace(staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Inventory.Title"), "");
                        String reason = e.getCurrentItem().getItemMeta().getDisplayName();
                        reason = ChatColor.stripColor(reason);
                        String target = ChatColor.stripColor(s);

                        if (!staffCore.getStaffCoreLoader().getBanManager().isMuted(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target))) {
                            if (target != p.getName()) {
                                if (staffCore.getStaffCoreLoader().getConfigProvider()
                                        .getBoolean("Mute-System.Per-Reason-Permission.Toggle")) {
                                    if (p.hasPermission(staffCore.getStaffCoreLoader().getConfigProvider()
                                            .getString("Mute-System.Per-Reason-Permission.Prefix-For-Permission")
                                            + staffCore.getStaffCoreLoader().getBanManager().getIDFromMuteReason(reason))) {
                                        if (staffCore.getStaffCoreLoader().getBanManager().submitMute(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target), reason,
                                                p.getUniqueId().toString())) {
                                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                ActionBarAPI.sendActionBar(p,
                                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Created"));
                                            } else {
                                                p.sendMessage(
                                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Created"));
                                            }
                                        } else {
                                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                                ActionBarAPI.sendActionBar(p,
                                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Error"));
                                            } else {
                                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Error"));
                                            }
                                        }
                                    } else {
                                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.No-Permission")
                                                .replace("%permission%", staffCore.getStaffCoreLoader().getConfigProvider()
                                                        .getString("Ban-System.Per-Reason-Permission.Prefix-For-Permission")
                                                        + staffCore.getStaffCoreLoader().getBanManager().getIDFromMuteReason(reason)));
                                    }
                                    p.closeInventory();
                                } else {
                                    if (staffCore.getStaffCoreLoader().getBanManager().submitMute(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(target), reason,
                                            p.getUniqueId().toString())) {
                                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                            ActionBarAPI.sendActionBar(p,
                                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Created"));
                                        } else {
                                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Created"));
                                        }
                                        p.closeInventory();
                                    } else {
                                        if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                            ActionBarAPI.sendActionBar(p,
                                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Error"));
                                        } else {
                                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Error"));
                                        }
                                    }
                                }
                                p.closeInventory();
                            } else {
                                p.sendMessage(
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Cannot-mute-self"));
                            }
                        } else {
                            if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                                ActionBarAPI.sendActionBar(p,
                                        staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Already-Muted"));
                            } else {
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Already-Muted"));
                            }
                        }
                    }
                } else {
                    int page = staffCore.getStaffCoreLoader().getPageManager().getPage().get(((Player) e.getWhoClicked()).getPlayer());
                    String s = e.getView().getTitle()
                            .replace(staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Inventory.Title"), "");
                    String target = ChatColor.stripColor(s);
                    if (e.getSlot() == 46) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .equals(staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Inventory.PreviousPage.NotAvailable"))) {
                            return;
                        } else {
                            staffCore.getStaffCoreLoader().getBanManager().openPagedMuteInv(p, target, page - 1);
                        }
                    } else if (e.getSlot() == 52) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName()
                                .equals(staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Inventory.NextPage.NotAvailable"))) {
                            return;
                        } else {
                            staffCore.getStaffCoreLoader().getBanManager().openPagedMuteInv(p, target, page + 1);
                        }
                    }

                }
            }
        }

        if (e.getView().getTitle().startsWith(
                ChatColor.GRAY + "Settings " + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW
                        + "ChatFilter")) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(
                            ChatColor.DARK_AQUA + "Add Cursed-Words " + ChatColor.DARK_GRAY + "| "
                                    + ChatColor.GRAY + "Deactivated")) {
                        staffCore.getStaffCoreLoader().getInventoryHandler().filter.add(p);
                        p.closeInventory();
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY
                                + "You are now able to write your curse words which will be blocked afterwards!");
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(
                            ChatColor.DARK_AQUA + "Add Cursed-Words " + ChatColor.DARK_GRAY + "| "
                                    + ChatColor.GREEN + "Activated")) {
                        staffCore.getStaffCoreLoader().getInventoryHandler().filter.remove(p);
                        p.closeInventory();
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "You can now use the chat as before!");
                    }
                }
            }
        }
    }

}
