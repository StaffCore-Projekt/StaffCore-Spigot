package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.objects.BanManagerPlayerInput;
import de.lacodev.rsystem.objects.ReasonEDuration;
import de.lacodev.rsystem.objects.ReasonRename;
import de.lacodev.rsystem.utils.PanelManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Listener_PanelManager implements Listener {

    private final StaffCore staffCore;

    public Listener_PanelManager(StaffCore staffCore) {
        this.staffCore = staffCore;
        Bukkit.getPluginManager().registerEvents(this, staffCore);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPanel(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PanelManager manager = staffCore.getStaffCoreLoader().getPanelManager();

        if (e.getView().getTitle().equalsIgnoreCase(
                ChatColor.RED + "Staffcore" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "MainMenu")) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "Report a Bug")) {
                        p.closeInventory();

                        p.sendMessage("");
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Bug-Report.Created"));

                        TextComponent tc = new TextComponent();
                        tc.setText(staffCore.getStaffCoreLoader().getMessage("Messages.System.Bug-Report.Button"));
                        URL url;
                        try {
                            url = new URL(
                                    "https://bugreport.lacodev.de/?product=staffcore&version=" + staffCore
                                            .getDescription().getVersion() + "&env=" + Bukkit.getVersion()
                                            .replace("(", "").replace(")", "").replace(" ", "%20") + "&mysql=" + staffCore.getStaffCoreLoader().getMySQL()
                                            .isConnected());
                            tc.setClickEvent(new ClickEvent(Action.OPEN_URL, url.toURI().toASCIIString()));
                            tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder(staffCore.getStaffCoreLoader().getMessage("Messages.System.Bug-Report.Hover-Text"))
                                            .create()));

                            p.spigot().sendMessage(tc);
                        } catch (MalformedURLException | URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                        p.sendMessage("");
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Player-Management")) {
                        manager.openPlayerManagement(p);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Settings")) {
                        manager.openSettingsMenu(p);
                    }
                }
            }
        }

        if (e.getView().getTitle().equalsIgnoreCase(
                ChatColor.RED + "StaffCore " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Settings")) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
                        manager.openMainMenu(p);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GREEN + "Download Latest Release")) {
                        p.closeInventory();

                        if (!staffCore.getStaffCoreLoader().getSystemManager().isLatest()) {
                            staffCore.getStaffCoreLoader().getSystemManager().downloadLatestVersion(p);
                        } else {
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "You are using the " + ChatColor.GREEN
                                            + "latest build" + ChatColor.GRAY + "!");
                        }
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GOLD + "Download Latest Experimental")) {
                        p.closeInventory();

                        staffCore.getStaffCoreLoader().getSystemManager().downloadExperimentalVersion(p);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GREEN + "►")) {
                        String lang = ChatColor
                                .stripColor(e.getInventory().getItem(31).getItemMeta().getDisplayName());

                        if (lang.matches("de")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "us");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("us")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "nl");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("nl")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "dk");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("dk")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "ru");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("ru")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "es");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("es")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "fr");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("fr")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "it");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("it")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "cz");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("cz")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "fi");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("fi")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "ee");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("ee")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "hr");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("hr")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "cn");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("cn")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "de");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        staffCore.getStaffCoreLoader().getTranslationHandler().fetch(
                                staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().getString("General.Language").substring(0, 2));
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GREEN + "◄")) {
                        String lang = ChatColor
                                .stripColor(e.getInventory().getItem(31).getItemMeta().getDisplayName());

                        if (lang.matches("de")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "cn");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("cn")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "hr");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("hr")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "ee");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("ee")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "fi");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("fi")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "cz");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("cz")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "it");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("it")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "fr");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("fr")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "es");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("es")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "ru");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("ru")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "dk");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("dk")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "nl");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("nl")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "us");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }
                        if (lang.matches("us")) {
                            staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().set("General.Language", "de");
                            staffCore.getStaffCoreLoader().getConfigProvider().save();
                            staffCore.getStaffCoreLoader().getConfigProvider().init();

                            PanelManager.settings.setItem(31, manager.getLanguageHead());
                        }

                        staffCore.getStaffCoreLoader().getTranslationHandler().fetch(
                                staffCore.getStaffCoreLoader().getConfigProvider().getYamlConfiguration().getString("General.Language").substring(0, 2));
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Reload " + ChatColor.RED + "StaffCore")) {
                        p.closeInventory();

                        staffCore.getStaffCoreLoader().getSystemManager().reloadStaffCore(p);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Setup " + ChatColor.RED + "StaffCore-UI")) {
                        p.closeInventory();

                        staffCore.getStaffCoreLoader().getWebUIHandler().setupStaffCoreUI(p);
                    }
                }
            }
        }

        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Player - Management")) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
                        manager.openMainMenu(p);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Manage OnlinePlayers")) {
                        manager.openOnlinePlayerMenu(p, 1);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Protect a player")) {
                        manager.openProtectionInventory(p, 1);
                    }
                }
            }
        }

        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Manage OnlinePlayers")) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
                        manager.openPlayerManagement(p);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GREEN + "◄")) {
                        manager.openOnlinePlayerMenu(p, staffCore.getStaffCoreLoader().getPageManager().getPage().get(p.getPlayer()) - 1);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GREEN + "►")) {
                        manager.openOnlinePlayerMenu(p, staffCore.getStaffCoreLoader().getPageManager().getPage().get(p.getPlayer()) - 1);
                    }
                    if (e.getSlot() > 8 && e.getSlot() < 45) {
                        String target = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

                        manager.openPlayerMenu(p, target);
                    }
                }
            }
        }

        if (e.getView().getTitle().endsWith(ChatColor.DARK_GRAY + "- " + ChatColor.RED + "StaffCore")) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    String s = e.getView().getTitle()
                            .replace(ChatColor.DARK_GRAY + "- " + ChatColor.RED + "StaffCore", "");
                    String targetname = ChatColor.stripColor(s);

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
                        manager.openOnlinePlayerMenu(p, staffCore.getStaffCoreLoader().getPageManager().getPage().get(p.getPlayer()));
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "Check Player")) {
                        p.closeInventory();
                        p.performCommand("check " + targetname);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "Mute Player")) {
                        p.closeInventory();
                        p.performCommand("mute " + targetname);
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "Ban Player")) {
                        p.closeInventory();
                        p.performCommand("ban " + targetname);
                    }
                }
            }
        }

        if (e.getView().getTitle()
                .equalsIgnoreCase(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Main-Gui-Title"))) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason"))) {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Name-Reason"));
                        BanManagerPlayerInput bmpi = new BanManagerPlayerInput(p, null, -1, null, "BAN");
                        staffCore.getStaffCoreLoader().getReasonEditManager().getBanManagerPlayerInputs().add(bmpi);
                        p.closeInventory();
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Add-Reason-Mute"))) {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Name-Reason"));
                        BanManagerPlayerInput bmpi = new BanManagerPlayerInput(p, null, -1, null, "MUTE");
                        staffCore.getStaffCoreLoader().getReasonEditManager().getBanManagerPlayerInputs().add(bmpi);
                        p.closeInventory();
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith(
                            staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Ban-Reason-Button-Title").replace("%reason%", ""))) {
                        p.closeInventory();
                        Integer id = staffCore.getStaffCoreLoader().getBanManager().getIDFromBanReason(
                                e.getCurrentItem().getItemMeta().getDisplayName().replace(
                                        staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Ban-Reason-Button-Title")
                                                .replace("%reason%", ""), ""));
                        assert id != null;
                        manager.openBanReasonUtils(p, id);
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith(
                            staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Mute-Reason-Button-Title")
                                    .replace("%reason%", ""))) {
                        p.closeInventory();
                        Integer id = staffCore.getStaffCoreLoader().getBanManager().getIDFromMuteReason(ChatColor.stripColor(
                                e.getCurrentItem().getItemMeta().getDisplayName().replace(
                                        staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Mute-Reason-Button-Title")
                                                .replace("%reason%", ""), "")));
                        assert id != null;
                        manager.openMuteReasonUtils(p, id);
                    }
                }
            }
        }

        if (e.getView().getTitle()
                .startsWith(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Ban-Reason-Title").replace("%reason%", ""))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
                        manager.openBanManagerManu(p);
                    }
                    Integer reason = Integer.parseInt(ChatColor.stripColor(e.getView().getTitle()
                            .replace(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Ban-Reason-Title").replace("%reason%", ""),
                                    "")));
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "DELETE")) {

                        if (staffCore.getStaffCoreLoader().getBanManager().existsBanReason(staffCore.getStaffCoreLoader().getBanManager().getBanReasonFromID(reason))) {
                            staffCore.getStaffCoreLoader().getBanManager().deleteBanReason(staffCore.getStaffCoreLoader().getBanManager().getBanReasonFromID(reason));
                            p.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Reason " + ChatColor.RED + "DELETED");
                            p.closeInventory();
                        } else {
                            p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.RED + " Something went wrong");
                            p.closeInventory();
                        }
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Edit")) {
                        p.closeInventory();

                        manager.openBanReasonEdit(p, reason);

                    }
                }
            }
        }

        if (e.getView().getTitle()
                .startsWith(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Mute-Reason-Title").replace("%reason%", ""))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {

                    Integer reason = Integer.parseInt(ChatColor.stripColor(e.getView().getTitle()
                            .replace(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Mute-Reason-Title").replace("%reason%", ""),
                                    "")));

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
                        manager.openBanManagerManu(p);
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "DELETE")) {
                        p.closeInventory();
                        staffCore.getStaffCoreLoader().getBanManager().deleteMuteReason(staffCore.getStaffCoreLoader().getBanManager().getMuteReasonFromID(reason));
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Reason " + ChatColor.RED + "DELETED");
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Edit")) {
                        manager.openMuteReasonEdit(p, reason);
                    }
                }
            }
        }

        if (e.getView().getTitle()
                .startsWith(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Mute-Edit-Title").replace("%reason%", ""))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {
                    e.setCancelled(true);
                    Integer reason = Integer.parseInt(ChatColor.stripColor(e.getView().getTitle()
                            .replace(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Mute-Edit-Title").replace("%reason%", ""),
                                    "")));
                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
                        manager.openMuteReasonUtils(p, reason);
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Rename Reason")) {
                        ReasonRename rr = new ReasonRename();
                        rr.setP(p);
                        rr.setOldName(staffCore.getStaffCoreLoader().getBanManager().getBanReasonFromID(reason));
                        rr.setId(reason);
                        staffCore.getStaffCoreLoader().getReasonEditManager().reasonRename.add(rr);

                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY
                                + "Please Type now the new Name in the Chat, to Rename: " + rr.getOldName() + "!");
                        p.closeInventory();
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Edit Duration")) {
                        ReasonEDuration red = new ReasonEDuration();
                        red.setP(p);
                        red.setId(reason);
                        staffCore.getStaffCoreLoader().getReasonEditManager().reasonEDurations.add(red);
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY
                                + "Please Type now the new Duration, [time] [perma | d | m | h ]");
                        p.closeInventory();
                    }
                }
            }
        }

        if (e.getView().getTitle()
                .startsWith(staffCore.getStaffCoreLoader().getMessage("Messages.BanManager.Ban-Edit-Title").replace("%reason%", ""))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().hasItemMeta()) {

                    Integer reason = Integer
                            .parseInt(ChatColor.stripColor(e.getView().getTitle().replace("Ban Edit: ", "")));

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.RED + "◄ Go back")) {
                        manager.openBanReasonUtils(p, reason);
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Rename Reason")) {
                        ReasonRename rr = new ReasonRename();
                        rr.setP(p);
                        rr.setOldName(staffCore.getStaffCoreLoader().getBanManager().getBanReasonFromID(reason));
                        rr.setId(reason);
                        staffCore.getStaffCoreLoader().getReasonEditManager().reasonRename.add(rr);

                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY
                                + "Please Type now the new Name in the Chat, to Rename: " + rr.getOldName() + "!");
                        p.closeInventory();
                    }

                    if (e.getCurrentItem().getItemMeta().getDisplayName()
                            .equalsIgnoreCase(ChatColor.GRAY + "Edit Duration")) {
                        ReasonEDuration red = new ReasonEDuration();
                        red.setP(p);
                        red.setId(reason);

                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY
                                + "Please Type now the new Duration, [time] [perma | d | m | h ]");
                        p.closeInventory();
                        staffCore.getStaffCoreLoader().getReasonEditManager().reasonEDurations.add(red);
                    }
                }
            }
        }


    }

}
