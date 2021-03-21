package de.lacodev.rsystem.listeners;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.objects.BanManagerPlayerInput;
import de.lacodev.rsystem.objects.ReasonEDuration;
import de.lacodev.rsystem.objects.ReasonRename;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.InventoryHandler;
import de.lacodev.rsystem.utils.ReportManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Listener_Chat implements Listener {

  public static HashMap<Player, Long> spam = new HashMap<>();

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
        p.sendMessage(Main.getPrefix() + ChatColor.GRAY + "");
        p.sendMessage(
            Main.getPrefix() + ChatColor.GRAY + "New Duration: " + ChatColor.RED + ChatColor.BOLD
                + Main.getMSG("Messages.Layouts.Ban.Length-Values.Permanently"));
        p.sendMessage(Main.getPrefix() + ChatColor.GRAY + "");

        BanManager.updateDuration(red);

        Main.reasonEDurations.remove(getReasonEditDuration(p));
      } else if (e.getMessage().endsWith("d")) {
        red.setDuration(Integer.parseInt(e.getMessage().replace("d", "")));
        red.setUnit("d");
        p.sendMessage("");
        p.sendMessage(
            Main.getPrefix() + ChatColor.GRAY + "Duration: " + red.getDuration() + red.getUnit());
        p.sendMessage("");

        BanManager.updateDuration(red);

        Main.reasonEDurations.remove(getReasonEditDuration(p));
      } else if (e.getMessage().endsWith("h")) {
        red.setDuration(Integer.parseInt(e.getMessage().replace("h", "")));
        red.setUnit("h");
        p.sendMessage("");
        p.sendMessage(
            Main.getPrefix() + ChatColor.GRAY + "Duration: " + red.getDuration() + red.getUnit());
        p.sendMessage("");

        BanManager.updateDuration(red);

        Main.reasonEDurations.remove(getReasonEditDuration(p));
      } else if (e.getMessage().endsWith("m")) {
        red.setDuration(Integer.parseInt(e.getMessage().replace("m", "")));
        red.setUnit("m");

        p.sendMessage("");
        p.sendMessage(
            Main.getPrefix() + ChatColor.GRAY + "Duration: " + red.getDuration() + red.getUnit());
        p.sendMessage("");

        BanManager.updateDuration(red);

        Main.reasonEDurations.remove(getReasonEditDuration(p));
      } else {
        p.sendMessage(Main.getPrefix() + ChatColor.RED + "Something went wrong!");
      }


    } else if (isRenaming(p)) {
      e.setCancelled(true);

      ReasonRename rr = getReasonRename(p);

      rr.setNewName(e.getMessage());

      BanManager.renameReason(rr);

      p.sendMessage(
          Main.getPrefix() + ChatColor.GRAY + "You Renamed the Reason: '" + rr.getOldName()
              + "' to '" + rr.getNewName() + "'!");

      Main.reasonRename.remove(getReasonRename(p));

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
          p.sendMessage(Main.getPrefix() + ChatColor.GRAY
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
            p.sendMessage(Main.getPrefix() + ChatColor.RED
                + "Please Type the time like [number][ perma | d | h | m ]");
          }

          if (i.getUnit().equalsIgnoreCase("perma")) {
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage(Main.getPrefix() + ChatColor.GRAY + "Reason: " + i.getName());
            e.getPlayer().sendMessage(Main.getPrefix() + ChatColor.GRAY + "Duration: Permanent");

          } else {
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage(Main.getPrefix() + ChatColor.GRAY + "Reason: " + i.getName());
            e.getPlayer().sendMessage(
                Main.getPrefix() + ChatColor.GRAY + "Duration: " + i.getTime() + i.getUnit());
          }

          if (i.getType().equalsIgnoreCase("BAN")) {
            BanManager.createBanReason(i.getName(), i.getUnit(), i.getTime());
          } else if (i.getType().equalsIgnoreCase("MUTE")) {
            BanManager.createMuteReason(i.getName(), i.getUnit(), i.getTime());
          }

          e.getPlayer()
              .sendMessage(Main.getPrefix() + ChatColor.GRAY + "BanReason Successfully created!");
          e.getPlayer().sendMessage("");
          Main.banManagerPlayerInputs.remove(getBanManagerPlayerInput(e.getPlayer()));


        }
      }

    } else {
      if (BanManager.isGMute()) {
        if (!(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p
            .hasPermission(Main.getPermissionNotice("Permissions.Mute.Global.Write")))) {
          p.sendMessage(Main.getPrefix() + Main
              .getMSG("Messages.Global-Mute-System.Message-when-a-player-writes-on-GlobalMute"));
          e.setCancelled(true);
        }
      } else {
        if (BanManager.isMuted(p.getUniqueId().toString())) {

          p.sendMessage(Main.getMSG("Messages.Layouts.Mute")
              .replace("%reason%", BanManager.getMuteReason(p.getUniqueId().toString()))
              .replace("%remaining%", BanManager.getMuteFinalEnd(p.getUniqueId().toString())));
          e.setCancelled(true);

        }

        File file = new File(
            "plugins//" + Main.getInstance().getDescription().getName() + "//chatfilter.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if (cfg.contains("Cursed-Words")) {
          if (wordIsBlocked(e.getMessage())) {
            if (Main.getInstance().getConfig()
                .getBoolean("Chatfilter.Cursed-Words.AutoReport.Enable")) {
              if (Main.getInstance().getConfig()
                  .getBoolean("Chatfilter.Cursed-Words.Block-Message.Enable")) {
                ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p,
                    Main.getPermissionNotice("Chatfilter.Cursed-Words.AutoReport.Reason"));
                e.setCancelled(true);
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Cursed-Words.Block"));
              } else {
                ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p,
                    Main.getPermissionNotice("Chatfilter.Cursed-Words.AutoReport.Reason"));
              }
            } else {
              if (Main.getInstance().getConfig()
                  .getBoolean("Chatfilter.Cursed-Words.Block-Message")) {
                e.setCancelled(true);
                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Cursed-Words.Block"));
              }
            }
          }
        }

        if (InventoryHandler.filter.contains(p)) {

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
            p.sendMessage(Main.getPrefix() + "§7Word §aadded §7to the Cursed-Words");
          } else {
            e.setCancelled(true);
          }

        }

        ArrayList<String> ad = (ArrayList<String>) Main.getInstance().getConfig()
            .getStringList("Chatfilter.Advertisment.Whitelist");
        ArrayList<String> blocked = (ArrayList<String>) Main.getInstance().getConfig()
            .getStringList("Chatfilter.Advertisment.Blocked-Domains");

        String[] adtest = e.getMessage().split(" ");

        for (String ads : adtest) {
          if (!ad.contains(ads)) {
            if (blocked.contains(ads)) {
              if (Main.getInstance().getConfig()
                  .getBoolean("Chatfilter.Advertisment.AutoReport.Enable")) {
                if (Main.getInstance().getConfig()
                    .getBoolean("Chatfilter.Advertisment.Block-Message.Enable")) {
                  ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p,
                      Main.getPermissionNotice("Chatfilter.Advertisment.AutoReport.Reason"));
                  e.setCancelled(true);
                  p.sendMessage(
                      Main.getPrefix() + Main.getMSG("Messages.System.Advertisment.Block"));
                } else {
                  ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p,
                      Main.getPermissionNotice("Chatfilter.Advertisment.AutoReport.Reason"));
                }
              } else {
                if (Main.getInstance().getConfig()
                    .getBoolean("Chatfilter.Advertisment.Block-Message")) {
                  e.setCancelled(true);
                  p.sendMessage(
                      Main.getPrefix() + Main.getMSG("Messages.System.Advertisment.Block"));
                }
              }
            }
          }
        }
      }
    }
  }

  private ReasonEDuration getReasonEditDuration(Player p) {
    for (ReasonEDuration rr : Main.reasonEDurations) {
      if (rr.getP().getUniqueId().equals(p.getUniqueId())) {
        return rr;
      }
    }
    return null;
  }

  private boolean isEditingDuration(Player p) {
    for (ReasonEDuration rr : Main.reasonEDurations) {
      if (rr.getP().getUniqueId().equals(p.getUniqueId())) {
        return true;
      }
    }
    return false;
  }

  private ReasonRename getReasonRename(Player p) {
    for (ReasonRename rr : Main.reasonRename) {
      if (rr.getP().getUniqueId().equals(p.getUniqueId())) {
        return rr;
      }
    }
    return null;
  }

  private boolean isRenaming(Player p) {
    for (ReasonRename rr : Main.reasonRename) {
      if (rr.getP().getUniqueId().equals(p.getUniqueId())) {
        return true;
      }
    }
    return false;
  }

  private BanManagerPlayerInput getBanManagerPlayerInput(Player p) {
    for (BanManagerPlayerInput input : Main.banManagerPlayerInputs) {
      if (input.getPlayer() == p) {
        return input;
      }
    }
    return null;
  }

  public boolean isInBanOptions(Player p) {
    for (BanManagerPlayerInput managerPlayerInput : Main.banManagerPlayerInputs) {
      if (managerPlayerInput.getPlayer() == p) {
        return true;
      }
    }
    return false;
  }

  private boolean wordIsBlocked(String message) {
    File file = new File(
        "plugins//" + Main.getInstance().getDescription().getName() + "//chatfilter.yml");
    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    ArrayList<String> cursed = (ArrayList<String>) cfg.getStringList("Cursed-Words");

    for (int i = 0; i < cursed.size(); i++) {
      int minpercent = Main.getInstance().getConfig()
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
            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report-System.AntiSpam"));
            e.setCancelled(true);
          }
        }
      }

      if (e.getMessage().startsWith("/ticket create")) {
        if (spam.containsKey(p)) {
          if (spam.get(p) > System.currentTimeMillis()) {
            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ticket-System.Antispam"));
            e.setCancelled(true);
          }
        }
      }
    }
  }

}
