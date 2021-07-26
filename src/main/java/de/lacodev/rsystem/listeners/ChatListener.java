package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.objects.BanManagerPlayerInput;
import de.lacodev.rsystem.objects.ReasonEDuration;
import de.lacodev.rsystem.objects.ReasonRename;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatListener implements Listener {

    public static HashMap<Player, Long> spam = new HashMap<>();
    private final StaffCore staffCore;

    public ChatListener(StaffCore staffCore) {
        this.staffCore = staffCore;
        Bukkit.getPluginManager().registerEvents(this, staffCore);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (isEditingDuration(p)) {
            e.setCancelled(true);

            ReasonEDuration red = getReasonEditDuration(p);
            assert red != null;
            if (e.getMessage().equalsIgnoreCase("perma")) {
                red.setDuration(-1);
                red.setUnit("perma");
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "");
                p.sendMessage(
                        staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "New Duration: " + ChatColor.RED + ChatColor.BOLD
                                + staffCore.getStaffCoreLoader().getMessage("Messages.Layouts.Ban.Length-Values.Permanently"));
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "");

                staffCore.getStaffCoreLoader().getBanManager().updateDuration(red);

                staffCore.getStaffCoreLoader().getReasonEditManager().reasonEDurations.remove(getReasonEditDuration(p));
            } else if (e.getMessage().endsWith("d")) {
                red.setDuration(Integer.parseInt(e.getMessage().replace("d", "")));
                red.setUnit("d");
                p.sendMessage("");
                p.sendMessage(
                        staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Duration: " + red.getDuration() + red.getUnit());
                p.sendMessage("");

                staffCore.getStaffCoreLoader().getBanManager().updateDuration(red);

                staffCore.getStaffCoreLoader().getReasonEditManager().reasonEDurations.remove(getReasonEditDuration(p));
            } else if (e.getMessage().endsWith("h")) {
                red.setDuration(Integer.parseInt(e.getMessage().replace("h", "")));
                red.setUnit("h");
                p.sendMessage("");
                p.sendMessage(
                        staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Duration: " + red.getDuration() + red.getUnit());
                p.sendMessage("");

                staffCore.getStaffCoreLoader().getBanManager().updateDuration(red);

                staffCore.getStaffCoreLoader().getReasonEditManager().reasonEDurations.remove(getReasonEditDuration(p));
            } else if (e.getMessage().endsWith("m")) {
                red.setDuration(Integer.parseInt(e.getMessage().replace("m", "")));
                red.setUnit("m");

                p.sendMessage("");
                p.sendMessage(
                        staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Duration: " + red.getDuration() + red.getUnit());
                p.sendMessage("");

                staffCore.getStaffCoreLoader().getBanManager().updateDuration(red);

                staffCore.getStaffCoreLoader().getReasonEditManager().reasonEDurations.remove(getReasonEditDuration(p));
            } else {
                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.RED + "Something went wrong!");
            }


        } else if (isRenaming(p)) {
            e.setCancelled(true);

            ReasonRename rr = getReasonRename(p);

            rr.setNewName(e.getMessage());

            staffCore.getStaffCoreLoader().getBanManager().renameReason(rr);

            p.sendMessage(
                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "You Renamed the Reason: '" + rr.getOldName()
                            + "' to '" + rr.getNewName() + "'!");

            staffCore.getStaffCoreLoader().getReasonEditManager().reasonRename.remove(getReasonRename(p));

        } else if (isInBanOptions(p)) {
            e.setCancelled(true);
            BanManagerPlayerInput i;
            i = getBanManagerPlayerInput(e.getPlayer());

            if (i == null) {
                //ERROR
            } else {
                if ((i.getName() == null) && (i.getTime() == -1) && (i.getUnit() == null)) {
                    i.setName(e.getMessage());

                    p.sendMessage("");
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY
                            + "Now Please Type the Time a Player should banned!");
                } else if ((i.getName() != null) && (i.getTime() == -1) && (i.getUnit() == null)) {
                    if (e.getMessage().equalsIgnoreCase("perma")) {
                        i.setUnit("perma");
                        i.setTime(-1);
                    } else if (e.getMessage().toLowerCase().endsWith("d")) {
                        i.setUnit("d");
                        i.setTime(Integer.parseInt(e.getMessage().replace("d", "")));
                    } else if (e.getMessage().toLowerCase().endsWith("h")) {
                        i.setUnit("h");
                        i.setTime(Integer.parseInt(e.getMessage().replace("h", "")));
                    } else if (e.getMessage().toLowerCase().endsWith("m")) {
                        i.setUnit("m");
                        i.setTime(Integer.parseInt(e.getMessage().replace("m", "")));
                    } else {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.RED
                                + "Please Type the time like [number][ perma | d | h | m ]");
                    }

                    if (i.getUnit().equalsIgnoreCase("perma")) {
                        e.getPlayer().sendMessage("");
                        e.getPlayer().sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Reason: " + i.getName());
                        e.getPlayer().sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Duration: Permanent");

                    } else {
                        e.getPlayer().sendMessage("");
                        e.getPlayer().sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Reason: " + i.getName());
                        e.getPlayer().sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Duration: " + i.getTime() + i.getUnit());
                    }

                    if (i.getType().equalsIgnoreCase("BAN")) {
                        staffCore.getStaffCoreLoader().getBanManager().createBanReason(i.getName(), i.getUnit(), i.getTime());
                    } else if (i.getType().equalsIgnoreCase("MUTE")) {
                        staffCore.getStaffCoreLoader().getBanManager().createMuteReason(i.getName(), i.getUnit(), i.getTime());
                    }

                    e.getPlayer()
                            .sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "BanReason Successfully created!");
                    e.getPlayer().sendMessage("");
                    staffCore.getStaffCoreLoader().getReasonEditManager().banManagerPlayerInputs.remove(getBanManagerPlayerInput(e.getPlayer()));


                }
            }

        } else {
            if (staffCore.getStaffCoreLoader().getBanManager().isGMute()) {
                if (!(p.hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything")) || p
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Mute.Global.Write")))) {
                    p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Global-Mute-System.Message-when-a-player-writes-on-GlobalMute"));
                    e.setCancelled(true);
                }
            } else {
                if (staffCore.getStaffCoreLoader().getBanManager().isMuted(p.getUniqueId().toString())) {

                    p.sendMessage(staffCore.getStaffCoreLoader().getMessage("Messages.Layouts.Mute")
                            .replace("%reason%", staffCore.getStaffCoreLoader().getBanManager().getMuteReason(p.getUniqueId().toString()))
                            .replace("%remaining%", staffCore.getStaffCoreLoader().getBanManager().getMuteFinalEnd(p.getUniqueId().toString())));
                    e.setCancelled(true);

                }

                File file = new File(
                        "plugins//" + staffCore.getDescription().getName() + "//chatfilter.yml");
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

                if (cfg.contains("Cursed-Words")) {
                    if (wordIsBlocked(e.getMessage())) {
                        if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("Chatfilter.Cursed-Words.AutoReport.Enable")) {
                            if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("Chatfilter.Cursed-Words.Block-Message.Enable")) {
                                staffCore.getStaffCoreLoader().getReportManager().createReport(staffCore.getStaffCoreLoader().getMessage("Chatfilter.ReporterName"), p,
                                        staffCore.getStaffCoreLoader().getMessage("Chatfilter.Cursed-Words.AutoReport.Reason"));
                                e.setCancelled(true);
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("System.Cursed-Words.Block"));
                            } else {
                                staffCore.getStaffCoreLoader().getReportManager().createReport(staffCore.getStaffCoreLoader().getPermission("Chatfilter.ReporterName"), p,
                                        staffCore.getStaffCoreLoader().getPermission("Chatfilter.Cursed-Words.AutoReport.Reason"));
                            }
                        } else {
                            if (staffCore.getStaffCoreLoader().getConfigProvider()
                                    .getBoolean("Chatfilter.Cursed-Words.Block-Message")) {
                                e.setCancelled(true);
                                p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("System.Cursed-Words.Block"));
                            }
                        }
                    }
                }

                if (staffCore.getStaffCoreLoader().getInventoryHandler().filter.contains(p)) {

                    ArrayList<String> cursed = (ArrayList<String>) cfg.getStringList("Cursed-Words");

                    if (!cursed.contains(e.getMessage())) {
                        e.setCancelled(true);
                        cursed.add(e.getMessage());
                        cfg.set("Cursed-Words", cursed);
                        try {
                            cfg.save(file);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + "§7Word §aadded §7to the Cursed-Words");
                    } else {
                        e.setCancelled(true);
                    }

                }

                List<String> ad = staffCore.getStaffCoreLoader().getConfigProvider()
                        .getStringList("Chatfilter.Advertisment.Whitelist");
                List<String> blocked = staffCore.getStaffCoreLoader().getConfigProvider()
                        .getStringList("Chatfilter.Advertisment.Blocked-Domains");

                String[] adtest = e.getMessage().split(" ");

                for (String ads : adtest) {
                    if (!ad.contains(ads)) {
                        if (blocked.contains(ads)) {
                            if (staffCore.getStaffCoreLoader().getConfigProvider()
                                    .getBoolean("Chatfilter.Advertisment.AutoReport.Enable")) {
                                if (staffCore.getStaffCoreLoader().getConfigProvider()
                                        .getBoolean("Chatfilter.Advertisment.Block-Message.Enable")) {
                                    staffCore.getStaffCoreLoader().getReportManager().createReport(staffCore.getStaffCoreLoader().getPermission("Chatfilter.ReporterName"), p,
                                            staffCore.getStaffCoreLoader().getPermission("Chatfilter.Advertisment.AutoReport.Reason"));
                                    e.setCancelled(true);
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("System.Advertisment.Block"));
                                } else {
                                    staffCore.getStaffCoreLoader().getReportManager().createReport(staffCore.getStaffCoreLoader().getPermission("Chatfilter.ReporterName"), p,
                                            staffCore.getStaffCoreLoader().getPermission("Chatfilter.Advertisment.AutoReport.Reason"));
                                }
                            } else {
                                if (staffCore.getStaffCoreLoader().getConfigProvider()
                                        .getBoolean("Chatfilter.Advertisment.Block-Message")) {
                                    e.setCancelled(true);
                                    p.sendMessage(
                                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("System.Advertisment.Block"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private ReasonEDuration getReasonEditDuration(Player p) {
        for (ReasonEDuration rr : staffCore.getStaffCoreLoader().getReasonEditManager().reasonEDurations) {
            if (rr.getP().getUniqueId().equals(p.getUniqueId())) {
                return rr;
            }
        }
        return null;
    }

    private boolean isEditingDuration(Player p) {
        for (ReasonEDuration rr : staffCore.getStaffCoreLoader().getReasonEditManager().reasonEDurations) {
            if (rr.getP().getUniqueId().equals(p.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    private ReasonRename getReasonRename(Player p) {
        for (ReasonRename rr : staffCore.getStaffCoreLoader().getReasonEditManager().reasonRename) {
            if (rr.getP().getUniqueId().equals(p.getUniqueId())) {
                return rr;
            }
        }
        return null;
    }

    private boolean isRenaming(Player p) {
        for (ReasonRename rr : staffCore.getStaffCoreLoader().getReasonEditManager().reasonRename) {
            if (rr.getP().getUniqueId().equals(p.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    private BanManagerPlayerInput getBanManagerPlayerInput(Player p) {
        for (BanManagerPlayerInput input : staffCore.getStaffCoreLoader().getReasonEditManager().banManagerPlayerInputs) {
            if (input.getPlayer() == p) {
                return input;
            }
        }
        return null;
    }

    public boolean isInBanOptions(Player p) {
        for (BanManagerPlayerInput managerPlayerInput : staffCore.getStaffCoreLoader().getReasonEditManager().banManagerPlayerInputs) {
            if (managerPlayerInput.getPlayer() == p) {
                return true;
            }
        }
        return false;
    }

    private boolean wordIsBlocked(String message) {
        File file = new File(
                "plugins//" + staffCore.getDescription().getName() + "//chatfilter.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        ArrayList<String> cursed = (ArrayList<String>) cfg.getStringList("Cursed-Words");

        for (int i = 0; i < cursed.size(); i++) {
            int minpercent = staffCore.getStaffCoreLoader().getConfigProvider()
                    .getInt("Chatfilter.Cursed-Words.Match-for-action-in-Percentage");
            String[] msg = message.split(" ");
            for (String m : msg) {
                if (cursed.get(i).toLowerCase().contains(m.toLowerCase())) {
                    double total = cursed.get(i).length();
                    double score = m.length();
                    float percentage = (float) ((score * 100) / total);
                    if (percentage > minpercent) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onReport(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (!e.getMessage().startsWith("/report templates")) {
            if (e.getMessage().startsWith("/report ")) {
                if (spam.containsKey(p)) {
                    if (spam.get(p) > System.currentTimeMillis()) {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Report-System.AntiSpam"));
                        e.setCancelled(true);
                    }
                }
            }

            if (e.getMessage().startsWith("/ticket create")) {
                if (spam.containsKey(p)) {
                    if (spam.get(p) > System.currentTimeMillis()) {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Ticket-System.Antispam"));
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}
