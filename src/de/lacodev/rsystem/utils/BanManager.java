package de.lacodev.rsystem.utils;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.enums.XMaterial;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.objects.MuteReasons;
import de.lacodev.rsystem.objects.ReasonEDuration;
import de.lacodev.rsystem.objects.ReasonRename;
import de.lacodev.staffcore.api.events.BanReasonCreateEvent;
import de.lacodev.staffcore.api.events.BanReasonDeleteEvent;
import de.lacodev.staffcore.api.events.MuteReasonCreateEvent;
import de.lacodev.staffcore.api.events.MuteReasonDeleteEvent;
import de.lacodev.staffcore.api.events.PlayerBanEvent;
import de.lacodev.staffcore.api.events.PlayerIPAdressBanEvent;
import de.lacodev.staffcore.api.events.PlayerIPAdressUnBanEvent;
import de.lacodev.staffcore.api.events.PlayerMuteEvent;
import de.lacodev.staffcore.api.events.PlayerUnMuteEvent;
import de.lacodev.staffcore.api.events.PlayerUnbanEvent;
import de.lacodev.staffcore.api.events.PlayerWarnEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class BanManager {

  public static ArrayList<Player> freezed = new ArrayList<>();
  public static ArrayList<String> sound_enums = new ArrayList<>();

  public static Inventory baninv;
  public static Inventory muteinv;
  private static boolean gMute;

  //Glob Mute

  public static void setGlobalMute(boolean mute) {
    gMute = mute;
  }

  public static boolean isGMute() {
    return gMute;
  }

  public static boolean clearWans(String uuid) {
    if (MySQL.isConnected()) {
      try {
        //UPDATE `reportsystem_playerdb` SET `WARNS` = '1' WHERE `UUID` = 1;
        PreparedStatement st = MySQL.getCon().prepareStatement(
            "UPDATE reportsystem_playerdb SET WARNS = 0 WHERE UUID = '" + uuid + "'");
        if (st.executeUpdate() > 0) {
          return true;
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static void renameReason(ReasonRename rr) {
    if (MySQL.isConnected()) {
      try {
        PreparedStatement st = MySQL.getCon().prepareStatement(
            "UPDATE reportsystem_reasonsdb SET NAME = '" + rr.getNewName() + "' WHERE id = '" + rr
                .getId() + "'");
        st.executeUpdate();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public static void updateDuration(ReasonEDuration red) {
    if (MySQL.isConnected()) {
      long time = 0;
      if (red.getUnit().equalsIgnoreCase("perma")) {
        time = 1;
      } else if (red.getUnit().toLowerCase().matches("d")) {
        time = 1000 * 60 * 60 * 24;
      } else if (red.getUnit().toLowerCase().matches("h")) {
        time = 1000 * 60 * 60;
      } else if (red.getUnit().toLowerCase().matches("m")) {
        time = 1000 * 60;
      }
      try {
        PreparedStatement st = MySQL.getCon().prepareStatement(
            "UPDATE reportsystem_reasonsdb SET BAN_LENGTH = '" + (time * red.getDuration())
                + "' WHERE id = '" + red.getId() + "'");
        st.executeUpdate();
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
  }


  public static int getWarns(String UUID) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL
          .getResult("SELECT WARNS FROM ReportSystem_playerdb WHERE UUID = '" + UUID + "'");

      if (rs != null) {
        try {
          if (rs.next()) {
            return rs.getInt("WARNS");
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return 0;
  }

  public static void warnPlayer(Player t, String team_uuid, String reason) {
    if (!SystemManager.isProtected(t.getUniqueId().toString())) {
      Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
        MySQL.update(
            "INSERT INTO ReportSystem_warnsdb(UUID,TEAM_UUID,REASON) VALUES ('" + t.getUniqueId()
                .toString() + "','" + team_uuid + "','" + reason + "')");

        MySQL.update(
            "UPDATE ReportSystem_playerdb SET WARNS = '" + (getWarns(t.getUniqueId().toString())
                + 1) + "' WHERE UUID = '" + t.getUniqueId().toString() + "'");

        String teamUser = "";
        if (!team_uuid.matches("Console")) {
          teamUser = SystemManager.getUsernameByUUID(team_uuid);
        } else {
          teamUser = "Console";
        }
        new BukkitRunnable() {
          @Override
          public void run() {
            Bukkit.getPluginManager().callEvent(new PlayerWarnEvent(t, team_uuid, reason));
          }
        }.runTask(Main.getInstance());

        t.sendMessage(Main.getMSG("Messages.Layouts.Warn").replace("%team%", teamUser)
            .replace("%reason%", reason)
            .replace("%warns%", "" + (getWarns(t.getUniqueId().toString()) + 1)));
      });

    } else {
      sendConsoleNotify("PROTECT", t.getUniqueId().toString());
    }
  }

  public static void openPagedBanInv(Player p, String name, int page) {
    String title =
        Main.getMSG("Messages.Ban-System.Inventory.Title") + ChatColor.YELLOW + "" + name;
    if (title.length() > 32) {
      title = title.substring(0, 32);
    }
    baninv = p.getServer().createInventory(null, 54, title);

    if (MySQL.isConnected()) {

      ArrayList<BanReasons> rows = new ArrayList<>();
      ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'BAN'");
      try {
        while (rs.next()) {
          rows.add(new BanReasons(rs.getString("NAME"), rs.getInt("id"), rs.getLong("BAN_LENGTH")));
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

      for (int i2 = 0; i2 < 9; i2++) {
        baninv.setItem(i2, Data.buildPlace());
      }
      if (PageManager.page.containsKey(p.getPlayer())) {
        PageManager.page.remove(p.getPlayer());
      }
      PageManager.page.put(p.getPlayer(), page);

      for (int i2 = 45; i2 < 54; i2++) {
        baninv.setItem(i2, Data.buildPlace());
      }
      if (PageManager.isPageValid2(rows, page - 1, 36)) {
        baninv.setItem(46, Data.buildItem(XMaterial.LIME_STAINED_GLASS_PANE,
            Main.getMSG("Messages.Ban-System.Inventory.PreviousPage.Available")));
      } else {
        baninv.setItem(46, Data.buildItem(XMaterial.RED_STAINED_GLASS_PANE,
            Main.getMSG("Messages.Ban-System.Inventory.PreviousPage.NotAvailable")));
      }

      if (PageManager.isPageValid2(rows, page + 1, 36)) {
        baninv.setItem(52, Data.buildItem(XMaterial.LIME_STAINED_GLASS_PANE,
            Main.getMSG("Messages.Ban-System.Inventory.NextPage.Available")));
      } else {
        baninv.setItem(52, Data.buildItem(XMaterial.RED_STAINED_GLASS_PANE,
            Main.getMSG("Messages.Ban-System.Inventory.NextPage.NotAvailable")));
      }

      for (BanReasons item : PageManager.getPageItems2(rows, page, 36)) {
        baninv.setItem(baninv.firstEmpty(),
            Data.buildItemStack(Material.PAPER, 1, 0, ChatColor.YELLOW + "" + item.getName(),
                Main.getMSG("Messages.Ban-System.Inventory.BanItem-Lore.1")
                    .replace("%target%", name),
                Main.getMSG("Messages.Ban-System.Inventory.BanItem-Lore.2")
                    .replace("%reason%", item.getName())));
      }
    } else {
      baninv.setItem(13, Data.buildItem(Material.BARRIER, 1, 0, ChatColor.RED + "No Connection"));
    }

    p.openInventory(baninv);
  }

  public static void openPagedMuteInv(Player p, String name, int page) {
    String title =
        Main.getMSG("Messages.Mute-System.Inventory.Title") + ChatColor.YELLOW + "" + name;
    if (title.length() > 32) {
      title = title.substring(0, 32);
    }
    muteinv = p.getServer().createInventory(null, 54, title);

    if (MySQL.isConnected()) {

      ArrayList<MuteReasons> rows = new ArrayList<>();
      ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'MUTE'");
      try {
        while (rs.next()) {
          rows.add(
              new MuteReasons(rs.getString("NAME"), rs.getInt("id"), rs.getLong("BAN_LENGTH")));
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

      for (int i2 = 0; i2 < 9; i2++) {
        muteinv.setItem(i2, Data.buildPlace());
      }
      if (PageManager.page.containsKey(p.getPlayer())) {
        PageManager.page.remove(p.getPlayer());
      }
      PageManager.page.put(p.getPlayer(), page);

      for (int i2 = 45; i2 < 54; i2++) {
        muteinv.setItem(i2, Data.buildPlace());
      }
      if (PageManager.isPageValid3(rows, page - 1, 36)) {
        muteinv.setItem(46, Data.buildItem(XMaterial.LIME_STAINED_GLASS_PANE,
            Main.getMSG("Messages.Mute-System.Inventory.PreviousPage.Available")));
      } else {
        muteinv.setItem(46, Data.buildItem(XMaterial.RED_STAINED_GLASS_PANE,
            Main.getMSG("Messages.Mute-System.Inventory.PreviousPage.NotAvailable")));
      }

      if (PageManager.isPageValid3(rows, page + 1, 36)) {
        muteinv.setItem(52, Data.buildItem(XMaterial.LIME_STAINED_GLASS_PANE,
            Main.getMSG("Messages.Mute-System.Inventory.NextPage.Available")));
      } else {
        muteinv.setItem(52, Data.buildItem(XMaterial.RED_STAINED_GLASS_PANE,
            Main.getMSG("Messages.Mute-System.Inventory.NextPage.NotAvailable")));
      }

      for (MuteReasons item : PageManager.getPageItems3(rows, page, 36)) {
        muteinv.setItem(muteinv.firstEmpty(),
            Data.buildItemStack(Material.PAPER, 1, 0, ChatColor.YELLOW + "" + item.getName(),
                Main.getMSG("Messages.Mute-System.Inventory.MuteItem-Lore.1")
                    .replace("%target%", name),
                Main.getMSG("Messages.Mute-System.Inventory.MuteItem-Lore.2")
                    .replace("%reason%", item.getName())));
      }
    } else {
      muteinv.setItem(13, Data.buildItem(Material.BARRIER, 1, 0, ChatColor.RED + "No Connection"));
    }

    p.openInventory(muteinv);

  }

  public static void createBanReason(String name, String unit, int length) {
    new BukkitRunnable() {

      @Override
      public void run() {
        if (MySQL.isConnected()) {
          if (!existsBanReason(name)) {

            long time = 0;

            if (unit.toLowerCase().matches("d")) {
              time = 1000 * 60 * 60 * 24;
            } else if (unit.toLowerCase().matches("h")) {
              time = 1000 * 60 * 60;
            } else if (unit.toLowerCase().matches("m")) {
              time = 1000 * 60;
            } else if (unit.toLowerCase().matches("perma")) {
              time = 1;
            }

            try {
              PreparedStatement st = MySQL.getCon().prepareStatement(
                  "INSERT INTO ReportSystem_reasonsdb(TYPE,NAME,BAN_LENGTH) VALUES ('BAN','" + name
                      + "','" + (time * length) + "')");
              st.executeUpdate();

              new BukkitRunnable() {
                @Override
                public void run() {
                  Bukkit.getServer().getPluginManager()
                      .callEvent(new BanReasonCreateEvent(name, unit, length));
                }
              }.runTask(Main.getInstance());

            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      }

    }.runTaskAsynchronously(Main.getInstance());
  }


  public static boolean existsBanID(int id) {

    if (MySQL.isConnected()) {

      ResultSet rs = MySQL.getResult(
          "SELECT id FROM ReportSystem_reasonsdb WHERE id = '" + id + "' AND TYPE = 'BAN'");

      try {

        if (rs.next()) {

          return rs.getString("id") != null;

        }

      } catch (SQLException e) {

        e.printStackTrace();

      }

    }

    return false;

  }

  public static Integer getIDFromBanReason(String reason) {
    ArrayList<BanReasons> r = getBanReasons();

    for (BanReasons b : r) {
      if (b.getName().equals(reason)) {
        return b.getID();
      }
    }
    return null;
  }

  public static Integer getIDFromMuteReason(String reason) {
    ArrayList<MuteReasons> r = getMuteReasons();

    for (MuteReasons b : r) {
      if (b.getName().equals(reason)) {
        return b.getID();
      }
    }
    return null;
  }

  public static String getBanReasonFromID(int id) {

    if (MySQL.isConnected()) {

      ResultSet rs = MySQL.getResult(
          "SELECT NAME FROM ReportSystem_reasonsdb WHERE id = '" + id + "' AND TYPE = 'BAN'");

      try {

        if (rs.next()) {

          return rs.getString("NAME");

        }

      } catch (SQLException e) {

        e.printStackTrace();

      }

    }

    return "Unknown";

  }

  public static boolean existsBanReason(String name) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL.getResult(
          "SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'BAN' AND NAME = '" + name + "'");
      try {
        if (rs != null) {
          if (rs.next()) {
            return true;
          }
        } else {
          return false;
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static void deleteBanReason(String name) {
    new BukkitRunnable() {

      @Override
      public void run() {
        if (MySQL.isConnected()) {
          try {
            PreparedStatement st = MySQL.getCon().prepareStatement(
                "DELETE FROM ReportSystem_reasonsdb WHERE NAME = '" + name + "' AND TYPE = 'BAN'");
            st.executeUpdate();

            new BukkitRunnable() {
              @Override
              public void run() {
                Bukkit.getServer().getPluginManager().callEvent(new BanReasonDeleteEvent(name));
              }
            }.runTask(Main.getInstance());

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }

    }.runTaskAsynchronously(Main.getInstance());
  }

  public static boolean isBanned(String uuid) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL
          .getResult("SELECT * FROM ReportSystem_bansdb WHERE BANNED_UUID = '" + uuid + "'");
      try {
        if (rs != null) {
          if (rs.next()) {
            if (rs.getLong("BAN_END") == -1) {
              return true;
            } else {
              if (rs.getLong("BAN_END") > System.currentTimeMillis()) {
                return true;
              } else {
                if (unban(uuid)) {
                  sendConsoleNotify("UNBAN", uuid);
                }
              }
            }
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static boolean unban(String uuid) {
    if (MySQL.isConnected()) {
      try {
        PreparedStatement st = MySQL.getCon()
            .prepareStatement("DELETE FROM ReportSystem_bansdb WHERE BANNED_UUID = '" + uuid + "'");
        if (st.executeUpdate() > 0) {

          Bukkit.getServer().getPluginManager().callEvent(new PlayerUnbanEvent(uuid));

          return true;
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static void sendConsoleNotify(String string, String uuid) {
    if (string.matches("UNBAN")) {
      for (Player all : Bukkit.getOnlinePlayers()) {
        if (all.hasPermission(Main.getPermissionNotice("Permissions.UnBan.Notify")) || all
            .hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
          if (Main.getInstance().actionbar) {
            ActionBarAPI.sendActionBar(all,
                Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Notify.Team.Unban")
                    .replace("%player%",
                        Main.getMSG("Messages.Ban-System.UnBan.Notify.Team.ConsoleName"))
                    .replace("%target%", SystemManager.getUsernameByUUID(uuid)));
          } else {
            all.sendMessage("");
            all.sendMessage(
                Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Notify.Team.Unban")
                    .replace("%player%",
                        Main.getMSG("Messages.Ban-System.UnBan.Notify.Team.ConsoleName"))
                    .replace("%target%", SystemManager.getUsernameByUUID(uuid)));
            all.sendMessage("");
          }
        }
      }
      String msg = Main.getPrefix() + Main.getMSG("Messages.Ban-System.UnBan.Notify.Team.Unban")
          .replace("%player%", Main.getMSG("Messages.Ban-System.UnBan.Notify.Team.ConsoleName"))
          .replace("%target%", SystemManager.getUsernameByUUID(uuid));
      Main.getInstance().getLogger().info(ChatColor.stripColor(msg));
    } else if (string.matches("UNMUTE")) {
      for (Player all : Bukkit.getOnlinePlayers()) {
        if (all.hasPermission(Main.getPermissionNotice("Permissions.UnMute.Notify")) || all
            .hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
          if (Main.getInstance().actionbar) {
            ActionBarAPI.sendActionBar(all,
                Main.getPrefix() + Main.getMSG("Messages.Mute-System.UnMute.Notify.Team.Unmute")
                    .replace("%player%",
                        Main.getMSG("Messages.Mute-System.UnMute.Notify.Team.ConsoleName"))
                    .replace("%target%", SystemManager.getUsernameByUUID(uuid)));
          } else {
            all.sendMessage("");
            all.sendMessage(
                Main.getPrefix() + Main.getMSG("Messages.Mute-System.UnMute.Notify.Team.Unmute")
                    .replace("%player%",
                        Main.getMSG("Messages.Mute-System.UnMute.Notify.Team.ConsoleName"))
                    .replace("%target%", SystemManager.getUsernameByUUID(uuid)));
            all.sendMessage("");
          }
        }
      }
      String msg = Main.getPrefix() + Main.getMSG("Messages.Mute-System.UnMute.Notify.Team.Unmute")
          .replace("%player%", Main.getMSG("Messages.Mute-System.UnMute.Notify.Team.ConsoleName"))
          .replace("%target%", SystemManager.getUsernameByUUID(uuid));
      Main.getInstance().getLogger().info(ChatColor.stripColor(msg));
    } else if (string.matches("PROTECT")) {
      for (Player all : Bukkit.getOnlinePlayers()) {
        if (all.hasPermission(Main.getPermissionNotice("Permissions.System.Notify")) || all
            .hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
          if (Main.getInstance().actionbar) {
            ActionBarAPI.sendActionBar(all,
                Main.getPrefix() + Main.getMSG("Messages.System.Player-Protected.Notify")
                    .replace("%target%", SystemManager.getUsernameByUUID(uuid)));
          } else {
            all.sendMessage("");
            all.sendMessage(
                Main.getPrefix() + Main.getMSG("Messages.System.Player-Protected.Notify")
                    .replace("%target%", SystemManager.getUsernameByUUID(uuid)));
            all.sendMessage("");
          }
        }
      }
      String msg = Main.getPrefix() + Main.getMSG("Messages.System.Player-Protected.Notify")
          .replace("%target%", SystemManager.getUsernameByUUID(uuid));
      Main.getInstance().getLogger().info(ChatColor.stripColor(msg));
    }
  }

  public static long getRawBanLength(String reason) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL.getResult(
          "SELECT BAN_LENGTH FROM ReportSystem_reasonsdb WHERE NAME = '" + reason
              + "' AND TYPE = 'BAN'");
      try {
        if (rs.next()) {
          return rs.getLong("BAN_LENGTH");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return 0;
  }

  public static long getBanEnd(String uuid) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL
          .getResult("SELECT BAN_END FROM ReportSystem_bansdb WHERE BANNED_UUID = '" + uuid + "'");
      try {
        if (rs.next()) {
          return rs.getLong("BAN_END");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return 0;
  }

  public static String getBanReason(String uuid) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL
          .getResult("SELECT REASON FROM ReportSystem_bansdb WHERE BANNED_UUID = '" + uuid + "'");
      try {
        if (rs.next()) {
          return rs.getString("REASON");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return "Unknown";
  }


  public static ArrayList<BanReasons> getBanReasons() {

    ArrayList<BanReasons> reasons = new ArrayList<>();

    ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'BAN'");

    try {

      while (rs.next()) {

        reasons
            .add(new BanReasons(rs.getString("NAME"), rs.getInt("id"), rs.getLong("BAN_LENGTH")));

      }

      return reasons;

    } catch (SQLException e) {

      e.printStackTrace();

    }

    return reasons;

  }

  private static void addBan(String uuid) {
    new BukkitRunnable() {

      @Override
      public void run() {
        if (MySQL.isConnected()) {
          try {
            PreparedStatement st = MySQL.getCon().prepareStatement(
                "UPDATE ReportSystem_playerdb SET BANS = '" + (getBans(uuid) + 1)
                    + "' WHERE UUID = '" + uuid + "'");
            st.executeUpdate();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }

    }.runTaskAsynchronously(Main.getInstance());
  }

  public static int getBans(String uuid) {
    ResultSet rs = MySQL
        .getResult("SELECT BANS FROM ReportSystem_playerdb WHERE UUID = '" + uuid + "'");
    try {
      if (rs.next()) {
        return rs.getInt("BANS");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static String getBanFinalEnd(String UUID) {
    long uhrzeit = System.currentTimeMillis();
    long end = getBanEnd(UUID);

    long millis = end - uhrzeit;

    long sekunden = 0L;
    long minuten = 0L;
    long stunden = 0L;
    long tage = 0L;

    while (millis >= 1000L) {
      millis -= 1000L;
      sekunden += 1L;
    }
    while (sekunden >= 60L) {
      sekunden -= 60L;
      minuten += 1L;
    }
    while (minuten >= 60L) {
      minuten -= 60L;
      stunden += 1L;
    }
    while (stunden >= 24L) {
      stunden -= 24L;
      tage += 1L;
    }
    if (tage != 0L) {
      return ChatColor.GREEN + "" + tage + " " + ChatColor.GRAY + "" + Main
          .getMSG("Messages.Layouts.Ban.Remaining.Days") + ChatColor.GREEN + stunden + " "
          + ChatColor.GRAY + Main.getMSG("Messages.Layouts.Ban.Remaining.Hours") + ChatColor.GREEN
          + minuten + " " + ChatColor.GRAY + "" + Main
          .getMSG("Messages.Layouts.Ban.Remaining.Minutes");
    }
    if ((tage == 0L) && (stunden != 0L)) {
      return ChatColor.GREEN + "" + stunden + " " + ChatColor.GRAY + "" + Main
          .getMSG("Messages.Layouts.Ban.Remaining.Hours") + ChatColor.GREEN + minuten + " "
          + ChatColor.GRAY + Main.getMSG("Messages.Layouts.Ban.Remaining.Minutes") + ChatColor.GREEN
          + sekunden + " " + ChatColor.GRAY + "" + Main
          .getMSG("Messages.Layouts.Ban.Remaining.Seconds");
    }
    if ((tage == 0L) && (stunden == 0L) && (minuten != 0L)) {
      return ChatColor.GREEN + "" + minuten + " " + ChatColor.GRAY + "" + Main
          .getMSG("Messages.Layouts.Ban.Remaining.Minutes") + ChatColor.GREEN + sekunden + " "
          + ChatColor.GRAY + Main.getMSG("Messages.Layouts.Ban.Remaining.Seconds");
    }
    if ((tage == 0L) && (stunden == 0L) && (minuten == 0L) && (sekunden != 0L)) {
      return ChatColor.GREEN + "" + sekunden + " " + ChatColor.GRAY + "" + Main
          .getMSG("Messages.Layouts.Ban.Remaining.Seconds");
    }
    return ChatColor.DARK_RED + "Fehler in der Berechnung!";
  }

  public static String getBanLength(long end) {
    if (end != -1) {
      long millis = end;

      long sekunden = 0;
      long minuten = 0;
      long stunden = 0;
      long tage = 0;

      while (millis >= 1000) {
        millis -= 1000;
        sekunden += 1;
      }
      while (sekunden >= 60) {
        sekunden -= 60;
        minuten += 1;
      }
      while (minuten >= 60) {
        minuten -= 60;
        stunden += 1;
      }
      while (stunden >= 24) {
        stunden -= 24;
        tage += 1;
      }
      if (tage != 0) {
        if (stunden != 0) {
          if (minuten != 0) {
            if (sekunden != 0) {
              return ChatColor.GREEN + "" + tage + " " + ChatColor.GRAY + "" + Main
                  .getMSG("Messages.Layouts.Ban.Remaining.Days") + ChatColor.GREEN + stunden + " "
                  + ChatColor.GRAY + Main.getMSG("Messages.Layouts.Ban.Remaining.Hours") + " §a"
                  + minuten + " " + ChatColor.GRAY + "" + Main
                  .getMSG("Messages.Layouts.Ban.Remaining.Minutes");
            } else {
              return ChatColor.GREEN + "" + tage + " " + ChatColor.GRAY + "" + Main
                  .getMSG("Messages.Layouts.Ban.Remaining.Days") + ChatColor.GREEN + stunden + " "
                  + ChatColor.GRAY + Main.getMSG("Messages.Layouts.Ban.Remaining.Hours") + " §a"
                  + minuten + " " + ChatColor.GRAY + "" + Main
                  .getMSG("Messages.Layouts.Ban.Remaining.Minutes");
            }
          } else {
            return ChatColor.GREEN + "" + tage + " " + ChatColor.GRAY + "" + Main
                .getMSG("Messages.Layouts.Ban.Remaining.Days") + ChatColor.GREEN + stunden + " "
                + ChatColor.GRAY + Main.getMSG("Messages.Layouts.Ban.Remaining.Hours");
          }
        } else {
          if (minuten != 0) {
            return ChatColor.GREEN + "" + tage + " " + ChatColor.GRAY + "" + Main
                .getMSG("Messages.Layouts.Ban.Remaining.Days") + ChatColor.GREEN + stunden + " "
                + ChatColor.GRAY + Main.getMSG("Messages.Layouts.Ban.Remaining.Hours") + " §a"
                + minuten + " " + ChatColor.GRAY + "" + Main
                .getMSG("Messages.Layouts.Ban.Remaining.Minutes");
          } else {
            return ChatColor.GREEN + "" + tage + " " + ChatColor.GRAY + "" + Main
                .getMSG("Messages.Layouts.Ban.Remaining.Days");
          }
        }
      } else {
        if (stunden != 0) {
          if (minuten != 0) {
            if (sekunden != 0) {
              return ChatColor.GREEN + "" + stunden + " " + ChatColor.GRAY + "" + Main
                  .getMSG("Messages.Layouts.Ban.Remaining.Hours") + " §a" + minuten + " "
                  + ChatColor.GRAY + "" + Main.getMSG("Messages.Layouts.Ban.Remaining.Minutes")
                  + " §a" + sekunden + " " + ChatColor.GRAY + "" + Main
                  .getMSG("Messages.Layouts.Ban.Remaining.Seconds");
            } else {
              return ChatColor.GREEN + "" + stunden + " " + ChatColor.GRAY + "" + Main
                  .getMSG("Messages.Layouts.Ban.Remaining.Hours") + " §a" + minuten + " "
                  + ChatColor.GRAY + "" + Main.getMSG("Messages.Layouts.Ban.Remaining.Minutes");
            }
          } else {
            return ChatColor.GREEN + "" + stunden + " " + ChatColor.GRAY + "" + Main
                .getMSG("Messages.Layouts.Ban.Remaining.Hours");
          }
        } else {
          if (minuten != 0) {
            if (sekunden != 0) {
              return ChatColor.GREEN + "" + minuten + " " + ChatColor.GRAY + "" + Main
                  .getMSG("Messages.Layouts.Ban.Remaining.Minutes") + " §a" + sekunden + " "
                  + ChatColor.GRAY + "" + Main.getMSG("Messages.Layouts.Ban.Remaining.Seconds");
            } else {
              return ChatColor.GREEN + "" + minuten + " " + ChatColor.GRAY + "" + Main
                  .getMSG("Messages.Layouts.Ban.Remaining.Minutes");
            }
          } else {
            return ChatColor.DARK_RED + "ERROR";
          }
        }
      }
    } else {
      return Main.getMSG("Messages.Layouts.Ban.Length-Values.Permanently");
    }
  }

  public static String getMuteFinalEnd(String UUID) {
    long uhrzeit = System.currentTimeMillis();
    long end = getMuteEnd(UUID);

    long millis = end - uhrzeit;

    long sekunden = 0L;
    long minuten = 0L;
    long stunden = 0L;
    long tage = 0L;

    while (millis > 1000L) {
      millis -= 1000L;
      sekunden += 1L;
    }
    while (sekunden > 60L) {
      sekunden -= 60L;
      minuten += 1L;
    }
    while (minuten > 60L) {
      minuten -= 60L;
      stunden += 1L;
    }
    while (stunden > 24L) {
      stunden -= 24L;
      tage += 1L;
    }
    if (tage != 0L) {
      return ChatColor.GREEN + String.valueOf(tage) + ChatColor.GRAY + " " + Main
          .getMSG("Messages.Layouts.Mute.Remaining.Days") + ChatColor.GREEN + String
          .valueOf(stunden) + ChatColor.GRAY + " " + Main
          .getMSG("Messages.Layouts.Mute.Remaining.Hours") + ChatColor.GREEN + String
          .valueOf(minuten) + " " + ChatColor.GRAY + Main
          .getMSG("Messages.Layouts.Mute.Remaining.Minutes");
    }
    if ((tage == 0L) && (stunden != 0L)) {
      return ChatColor.GREEN + String.valueOf(stunden) + ChatColor.GRAY + " " + Main
          .getMSG("Messages.Layouts.Mute.Remaining.Hours") + ChatColor.GREEN + String
          .valueOf(minuten) + ChatColor.GRAY + " " + Main
          .getMSG("Messages.Layouts.Mute.Remaining.Minutes") + ChatColor.GREEN + String
          .valueOf(sekunden) + " " + ChatColor.GRAY + Main
          .getMSG("Messages.Layouts.Mute.Remaining.Seconds");
    }
    if ((tage == 0L) && (stunden == 0L) && (minuten != 0L)) {
      return ChatColor.GREEN + String.valueOf(minuten) + ChatColor.GRAY + " " + Main
          .getMSG("Messages.Layouts.Mute.Remaining.Minutes") + ChatColor.GREEN + String
          .valueOf(sekunden) + ChatColor.GRAY + " " + Main
          .getMSG("Messages.Layouts.Mute.Remaining.Seconds");
    }
    if ((tage == 0L) && (stunden == 0L) && (minuten == 0L) && (sekunden != 0L)) {
      return ChatColor.GREEN + "" + sekunden + " " + ChatColor.GRAY + "" + Main
          .getMSG("Messages.Layouts.Mute.Remaining.Seconds");
    }
    return ChatColor.DARK_RED + "Fehler in der Berechnung!";
  }

  public static void submitBan(String targetuuid, String reason, String team) {
    if (!SystemManager.isProtected(targetuuid)) {
      Player target = Bukkit.getPlayer(SystemManager.getUsernameByUUID(targetuuid));
      new BukkitRunnable() {
        @Override
        public void run() {
          if (MySQL.isConnected()) {
            if (existsBanReason(reason)) {

              long ban_end = 0;

              if (getRawBanLength(reason) == -1) {
                ban_end = getRawBanLength(reason);
              } else {
                ban_end = getRawBanLength(reason) + System.currentTimeMillis();
              }

              try {
                PreparedStatement st = MySQL.getCon().prepareStatement(
                    "INSERT INTO ReportSystem_bansdb(BANNED_UUID,TEAM_UUID,REASON,BAN_END) VALUES ('"
                        + targetuuid + "','" + team + "','" + reason + "','" + ban_end + "')");
                if (st.executeUpdate() > 0) {
                  MySQL.update(
                      "INSERT INTO ReportSystem_banhistory(BANNED_UUID,TEAM_UUID,REASON,BAN_START,BAN_END) VALUES ('"
                          + targetuuid + "','" + team + "','" + reason + "','" + System
                          .currentTimeMillis() + "','" + ban_end + "')");
                  addBan(targetuuid);
                  if (SystemManager.existsPlayerData(team)) {
                    Player p = Bukkit.getPlayer(SystemManager.getUsernameByUUID(team));
                    if (p != null) {
                      ReportManager.sendNotify("BAN", p.getName(),
                          SystemManager.getUsernameByUUID(targetuuid), reason);
                    }
                  } else {
                    ReportManager
                        .sendNotify("BAN", "Console", SystemManager.getUsernameByUUID(targetuuid),
                            reason);
                  }

                  new BukkitRunnable() {
                    @Override
                    public void run() {
                      Bukkit.getServer().getPluginManager()
                          .callEvent(new PlayerBanEvent(targetuuid, reason, team));
                    }
                  }.runTask(Main.getInstance());

                  if (Main.getInstance().getConfig().getBoolean("General.Include-Vault")) {
                    ArrayList<String> reporter_uuids = new ArrayList<>();

                    ResultSet rs1 = MySQL.getResult(
                        "SELECT * FROM ReportSystem_reportsdb WHERE REPORTED_UUID = '" + targetuuid
                            + "'");
                    if (rs1.next()) {
                      while (rs1.next()) {
                        if (!reporter_uuids.contains(rs1.getString("REPORTER_UUID"))) {
                          reporter_uuids.add(rs1.getString("REPORTER_UUID"));
                        }
                      }
                      for (int i = 0; i < reporter_uuids.size(); i++) {
                        if (SystemManager.existsPlayerData(reporter_uuids.get(i))) {
                          Player reporter = Bukkit
                              .getPlayer(SystemManager.getUsernameByUUID(reporter_uuids.get(i)));

                          if (reporter != null) {

                            Random rdm = new Random();
                            int rdmm = rdm.nextInt(
                                Main.getInstance().getConfig().getInt("Vault.Rewards.Report.MAX"));

                            Main.getEconomy().depositPlayer(reporter, rdmm);
                            reporter.sendMessage(Main.getPrefix() + Main
                                .getMSG("Messages.Vault.Rewards.Report.Success")
                                .replace("%reward%", String.valueOf(rdmm)));
                          } else {
                            ActionManager.createAction("REPORT_SUCCESS", reporter_uuids.get(i),
                                target.getName());
                          }
                        }
                      }
                    }
                  }
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            } else {
              Bukkit.getConsoleSender().sendMessage("");
              Bukkit.getConsoleSender().sendMessage(
                  ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED
                      + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                      + "Reason does not exist" + ChatColor.DARK_GRAY + ")");
              Bukkit.getConsoleSender().sendMessage("");
            }
          } else {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED
                    + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                    + "MySQL Connection" + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
          }
        }
      }.runTaskAsynchronously(Main.getInstance());

      new BukkitRunnable() {

        @Override
        public void run() {
          if (target != null) {
            if (Main.getInstance().getConfig().getBoolean("Ban-Animation.Enable")) {
              playBanAnimation(target, reason);
            } else {
              target.kickPlayer(Main.getMSG("Messages.Ban-System.Player-Kick-Screen")
                  .replace("%reason%", reason));
            }

            if (Main.getInstance().getConfig()
                .getBoolean("IP-Ban.Ban-IP-When-Player-Gets-Banned")) {
              banIPAddress("Console", target);
            }
          }
        }

      }.runTask(Main.getInstance());
    } else {
      sendConsoleNotify("PROTECT", targetuuid);
    }
  }

  @SuppressWarnings("deprecation")
  public static void playBanAnimation(Player t, String reason) {

    freeze(t);

    if (Main.getPermissionNotice("Ban-Animation.Type").toLowerCase().equalsIgnoreCase("guardian")) {
      if (Bukkit.getVersion().contains("(MC: 1.13") || Bukkit.getVersion().contains("(MC: 1.14")
          || Bukkit.getVersion().contains("(MC: 1.15") || Bukkit.getVersion()
          .contains("(MC: 1.16")) {
        int a;

        Entity en = t.getLocation().getWorld()
            .spawnEntity(t.getLocation().add(-2, 5, -2), EntityType.GUARDIAN);

        Guardian g = (Guardian) en;

        g.setGravity(false);
        g.setSwimming(true);
        g.setTarget(t);
        g.setCustomName("G1");
        g.setCustomNameVisible(false);

        Entity en2 = t.getLocation().getWorld()
            .spawnEntity(t.getLocation().add(2, 5, -2), EntityType.GUARDIAN);

        Guardian g2 = (Guardian) en2;

        g2.setGravity(false);
        g2.setSwimming(true);
        g2.setTarget(t);
        g2.setCustomName("G2");
        g2.setCustomNameVisible(false);

        Entity en3 = t.getLocation().getWorld()
            .spawnEntity(t.getLocation().add(0, 5, 2), EntityType.GUARDIAN);

        Guardian g3 = (Guardian) en3;

        g3.setInvulnerable(true);
        g3.setGravity(false);
        g3.setSwimming(true);
        g3.setTarget(t);
        g3.setCustomName("G3");
        g3.setCustomNameVisible(false);

        if (sound_enums.contains(Main.getPermissionNotice("Ban-Animation.Sound").toUpperCase())) {
          for (Player all : Bukkit.getOnlinePlayers()) {
            all.playSound(all.getLocation(),
                Sound.valueOf(Main.getPermissionNotice("Ban-Animation.Sound")), 100, 100);
          }
        } else {
          Bukkit.getConsoleSender().sendMessage(
              Main.getPrefix() + ChatColor.RED + "Invalid Sound " + ChatColor.DARK_GRAY + "- "
                  + ChatColor.GRAY + "It seems like this sound is not available in this version!");
        }

        a = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), () -> {
          for (Player all : Bukkit.getOnlinePlayers()) {
            all.playEffect(t.getLocation().add(0, 1, 0), Effect.ZOMBIE_INFECT, 10);
          }
        }, 0, 5);

        new BukkitRunnable() {

          @Override
          public void run() {
            g.setHealth(0);
            g2.setHealth(0);
            g3.setHealth(0);

            for (Player all : Bukkit.getOnlinePlayers()) {
              if (sound_enums.contains("ENTITY_LIGHTNING_BOLT_THUNDER")) {
                all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 100);
              } else {
                all.playSound(all.getLocation(), Sound.valueOf("AMBIENCE_THUNDER"), 100, 100);
              }
            }
            t.getLocation().getWorld().strikeLightning(t.getLocation());

            Bukkit.getScheduler().cancelTask(a);
            t.kickPlayer(
                Main.getMSG("Messages.Ban-System.Player-Kick-Screen").replace("%reason%", reason));
          }
        }.runTaskLater(Main.getInstance(), 5 * 20);
      } else {
        Bukkit.getConsoleSender().sendMessage("");
        t.kickPlayer(
            Main.getMSG("Messages.Ban-System.Player-Kick-Screen").replace("%reason%", reason));
        Bukkit.getConsoleSender().sendMessage(
            Main.getPrefix() + ChatColor.RED + "Invalid Enviroment " + ChatColor.DARK_GRAY + "- "
                + ChatColor.GRAY + "" + Bukkit.getVersion());
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender()
            .sendMessage(Main.getPrefix() + ChatColor.GRAY + "Choose one, which is available:");
        Bukkit.getConsoleSender().sendMessage(
            Main.getPrefix() + ChatColor.DARK_AQUA + "Guardian " + ChatColor.DARK_GRAY + "("
                + ChatColor.GRAY + "1.13+" + ChatColor.DARK_GRAY + ")");
        Bukkit.getConsoleSender().sendMessage(
            Main.getPrefix() + ChatColor.DARK_AQUA + "TNT " + ChatColor.DARK_GRAY + "("
                + ChatColor.GRAY + "1.7+" + ChatColor.DARK_GRAY + ")");
        Bukkit.getConsoleSender().sendMessage(
            Main.getPrefix() + ChatColor.DARK_AQUA + "Zombie " + ChatColor.DARK_GRAY + "("
                + ChatColor.GRAY + "1.7+" + ChatColor.DARK_GRAY + ")");
      }
    } else if (Main.getPermissionNotice("Ban-Animation.Type").toLowerCase()
        .equalsIgnoreCase("zombie")) {
      int a;

      Entity en = t.getLocation().getWorld().spawnEntity(t.getLocation(), EntityType.ZOMBIE);

      Zombie z = (Zombie) en;

      z.setTarget(null);
      z.setCustomName(
          ChatColor.RED + "Ban " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "" + t.getName());
      z.setCustomNameVisible(false);
      z.setFireTicks(0);

      if (sound_enums.contains(Main.getPermissionNotice("Ban-Animation.Sound").toUpperCase())) {
        for (Player all : Bukkit.getOnlinePlayers()) {
          all.hidePlayer(t);
          all.playSound(all.getLocation(),
              Sound.valueOf(Main.getPermissionNotice("Ban-Animation.Sound")), 100, 100);
        }
      } else {
        Bukkit.getConsoleSender().sendMessage(
            Main.getPrefix() + ChatColor.RED + "Invalid Sound " + ChatColor.DARK_GRAY + "- "
                + ChatColor.GRAY + "It seems like this sound is not available in this version!");
      }

      a = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {

        @Override
        public void run() {
          for (Player all : Bukkit.getOnlinePlayers()) {
            all.playEffect(t.getLocation().add(0, 1, 0), Effect.MOBSPAWNER_FLAMES, 10);
            z.teleport(t.getLocation());
            if (sound_enums.contains("ENTITY_LIGHTNING_BOLT_THUNDER")) {
              all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 100);
            } else {
              all.playSound(all.getLocation(), Sound.valueOf("AMBIENCE_THUNDER"), 100, 100);
            }
          }
        }
      }, 0, 5);

      new BukkitRunnable() {

        @Override
        public void run() {
          z.setHealth(0);

          for (Player all : Bukkit.getOnlinePlayers()) {
            if (sound_enums.contains("ENTITY_LIGHTNING_BOLT_THUNDER")) {
              all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 100);
            } else {
              all.playSound(all.getLocation(), Sound.valueOf("AMBIENCE_THUNDER"), 100, 100);
            }
          }
          t.getLocation().getWorld().strikeLightning(t.getLocation());

          Bukkit.getScheduler().cancelTask(a);
          t.kickPlayer(
              Main.getMSG("Messages.Ban-System.Player-Kick-Screen").replace("%reason%", reason));
        }
      }.runTaskLater(Main.getInstance(), 5 * 20);
    } else if (Main.getPermissionNotice("Ban-Animation.Type").toLowerCase()
        .equalsIgnoreCase("tnt")) {
      int a;

      if (sound_enums.contains(Main.getPermissionNotice("Ban-Animation.Sound").toUpperCase())) {
        for (Player all : Bukkit.getOnlinePlayers()) {
          all.playSound(all.getLocation(),
              Sound.valueOf(Main.getPermissionNotice("Ban-Animation.Sound")), 100, 100);
        }
      } else {
        Bukkit.getConsoleSender().sendMessage(
            Main.getPrefix() + ChatColor.RED + "Invalid Sound " + ChatColor.DARK_GRAY + "- "
                + ChatColor.GRAY + "It seems like this sound is not available in this version!");
      }

      a = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {

        @Override
        public void run() {
          for (Player all : Bukkit.getOnlinePlayers()) {
            all.playEffect(t.getLocation().add(0, 1, 0), Effect.EXTINGUISH, 10);

            if (sound_enums.contains("ENTITY_TNT_PRIMED")) {
              all.playSound(all.getLocation(), Sound.ENTITY_TNT_PRIMED, 100, 100);
            } else {
              all.playSound(all.getLocation(), Sound.valueOf("EXPLODE"), 100, 100);
            }
          }
        }
      }, 0, 5);

      new BukkitRunnable() {

        @Override
        public void run() {

          for (Player all : Bukkit.getOnlinePlayers()) {
            if (sound_enums.contains("ENTITY_LIGHTNING_BOLT_THUNDER")) {
              all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 100);
            } else {
              all.playSound(all.getLocation(), Sound.valueOf("AMBIENCE_THUNDER"), 100, 100);
            }
          }
          t.getLocation().getWorld().strikeLightning(t.getLocation());

          Bukkit.getScheduler().cancelTask(a);
          t.kickPlayer(
              Main.getMSG("Messages.Ban-System.Player-Kick-Screen").replace("%reason%", reason));
        }
      }.runTaskLater(Main.getInstance(), 2 * 20);
    } else {
      Bukkit.getConsoleSender().sendMessage("");
      t.kickPlayer(
          Main.getMSG("Messages.Ban-System.Player-Kick-Screen").replace("%reason%", reason));
      Bukkit.getConsoleSender()
          .sendMessage(Main.getPrefix() + ChatColor.RED + "Invalid AnimationType!");
      Bukkit.getConsoleSender().sendMessage("");
      Bukkit.getConsoleSender()
          .sendMessage(Main.getPrefix() + ChatColor.GRAY + "Choose one, which is available:");
      Bukkit.getConsoleSender().sendMessage(
          Main.getPrefix() + ChatColor.DARK_AQUA + "Guardian " + ChatColor.DARK_GRAY + "("
              + ChatColor.GRAY + "1.13+" + ChatColor.DARK_GRAY + ")");
      Bukkit.getConsoleSender().sendMessage(
          Main.getPrefix() + ChatColor.DARK_AQUA + "TNT " + ChatColor.DARK_GRAY + "("
              + ChatColor.GRAY + "1.7+" + ChatColor.DARK_GRAY + ")");
      Bukkit.getConsoleSender().sendMessage(
          Main.getPrefix() + ChatColor.DARK_AQUA + "Zombie " + ChatColor.DARK_GRAY + "("
              + ChatColor.GRAY + "1.7+" + ChatColor.DARK_GRAY + ")");
    }

  }


  public static void freeze(Player t) {
    freezed.add(t);

    if (t.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR) {
      t.getLocation().add(0, -1, 0).getBlock().setType(Material.BARRIER);
    }
  }

  public static void unfreeze(Player t) {
    freezed.remove(t);

    if (t.getLocation().add(0, -1, 0).getBlock().getType() == Material.BARRIER) {
      t.getLocation().add(0, -1, 0).getBlock().setType(Material.AIR);
    }
  }


  public static void banIPAddress(String team, Player target) {
    if (!SystemManager.isProtected(target.getUniqueId().toString())) {
      if (MySQL.isConnected()) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
          @Override
          public void run() {
            if (!isIPBanned(target.getAddress().getAddress().toString())) {

              long current = System.currentTimeMillis();
              long hours = 1000 * 60 * 60 * Main.getInstance().getConfig()
                  .getInt("IP-Ban.Duration-in-Hours");

              long end = current + hours;

              String ip = target.getAddress().getAddress().toString();

              MySQL.update(
                  "INSERT INTO ReportSystem_ipbans(PLAYERNAME,IP_ADDRESS,END) VALUES ('" + target
                      .getName() + "','" + ip + "','" + end + "')");

              new BukkitRunnable() {
                @Override
                public void run() {
                  Bukkit.getServer().getPluginManager()
                      .callEvent(new PlayerIPAdressBanEvent(team, target));
                }
              }.runTask(Main.getInstance());

              for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.hasPermission(Main.getPermissionNotice("Permissions.IpBan.Notify"))) {
                  all.sendMessage(
                      Main.getPrefix() + Main.getMSG("Messages.Ban-System.IP-Ban.Notify")
                          .replace("%player%", team).replace("%target%", target.getName())
                          .replace("%duration%",
                              Main.getInstance().getConfig().getInt("IP-Ban.Duration-in-Hours")
                                  + " " + Main.getMSG("Messages.Layouts.Ban.Remaining.Hours")));
                }
              }
            }
          }
        });
      }
      target.kickPlayer(Main.getMSG("IP-Ban.Kick-Message"));
    } else {
      sendConsoleNotify("PROTECT", target.getUniqueId().toString());
    }
  }

  public static void unbanIpAddress(String playername) {
    if (isIpBanned(playername)) {
      MySQL.update("DELETE FROM ReportSystem_ipbans WHERE PLAYERNAME = '" + playername + "'");

      Bukkit.getServer().getPluginManager().callEvent(new PlayerIPAdressUnBanEvent(playername));

    }
  }

  public static boolean isIPBanned(String address) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL
          .getResult("SELECT * FROM ReportSystem_ipbans WHERE IP_ADDRESS = '" + address + "'");
      try {
        if (rs != null) {
          if (rs.next()) {
            if (rs.getLong("END") > System.currentTimeMillis()) {
              return true;
            } else {
              return false;
            }
          } else {
            return false;
          }
        } else {
          return false;
        }
      } catch (SQLException e) {
        return false;
      }
    } else {
      return false;
    }
  }

  public static boolean isIpBanned(String playername) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL
          .getResult("SELECT * FROM ReportSystem_ipbans WHERE PLAYERNAME = '" + playername + "'");
      try {
        if (rs != null) {
          if (rs.next()) {
            if (rs.getLong("END") > System.currentTimeMillis()) {
              return true;
            } else {
              return false;
            }
          } else {
            return false;
          }
        } else {
          return false;
        }
      } catch (SQLException e) {
        return false;
      }
    } else {
      return false;
    }
  }

  public static void createMuteReason(String name, String unit, int length) {
    if (MySQL.isConnected()) {
      Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
        @Override
        public void run() {
          if (!existsMuteReason(name)) {

            long time = 0;

            if (unit.toLowerCase().matches("d")) {
              time = 1000 * 60 * 60 * 24;
            } else if (unit.toLowerCase().matches("h")) {
              time = 1000 * 60 * 60;
            } else if (unit.toLowerCase().matches("m")) {
              time = 1000 * 60;
            }

            try {
              PreparedStatement st = MySQL.getCon().prepareStatement(
                  "INSERT INTO ReportSystem_reasonsdb(TYPE,NAME,BAN_LENGTH) VALUES ('MUTE','" + name
                      + "','" + (time * length) + "')");
              st.executeUpdate();

              new BukkitRunnable() {
                @Override
                public void run() {
                  Bukkit.getServer().getPluginManager()
                      .callEvent(new MuteReasonCreateEvent(name, unit, length));
                }
              }.runTask(Main.getInstance());

            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      });
    }
  }


  public static boolean existsMuteID(int id) {

    if (MySQL.isConnected()) {

      ResultSet rs = MySQL.getResult(
          "SELECT id FROM ReportSystem_reasonsdb WHERE id = '" + id + "' AND TYPE = 'MUTE'");

      try {

        if (rs.next()) {

          return rs.getString("id") != null;

        }

      } catch (SQLException e) {

        e.printStackTrace();

      }

    }

    return false;

  }


  public static String getMuteReasonFromID(int id) {

    if (MySQL.isConnected()) {

      ResultSet rs = MySQL.getResult(
          "SELECT NAME FROM ReportSystem_reasonsdb WHERE id = '" + id + "' AND TYPE = 'MUTE'");

      try {

        if (rs.next()) {

          return rs.getString("NAME");

        }

      } catch (SQLException e) {

        e.printStackTrace();

      }

    }

    return "Unknown";

  }

  public static boolean existsMuteReason(String name) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL.getResult(
          "SELECT NAME FROM ReportSystem_reasonsdb WHERE NAME = '" + name + "' AND TYPE = 'MUTE'");
      try {
        if (rs.next()) {
          return rs.getString("NAME") != null;
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static void deleteMuteReason(String name) {
    if (MySQL.isConnected()) {
      Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
        @Override
        public void run() {
          if (existsMuteReason(name)) {
            try {
              PreparedStatement st = MySQL.getCon().prepareStatement(
                  "DELETE FROM ReportSystem_reasonsdb WHERE NAME = '" + name
                      + "' AND TYPE = 'MUTE'");
              st.executeUpdate();

              new BukkitRunnable() {
                @Override
                public void run() {
                  Bukkit.getServer().getPluginManager().callEvent(new MuteReasonDeleteEvent(name));
                }
              }.runTask(Main.getInstance());

            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      });
    }
  }

  public static boolean isMuted(String uuid) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL
          .getResult("SELECT * FROM ReportSystem_mutesdb WHERE MUTED_UUID = '" + uuid + "'");
      try {
        if (rs != null) {
          if (rs.next()) {
            if (rs.getLong("MUTE_END") > System.currentTimeMillis()) {
              return rs.getString("MUTED_UUID") != null;
            } else {
              if (unmute(uuid)) {
                sendConsoleNotify("UNMUTE", uuid);
              }
            }
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static boolean unmute(String uuid) {
    if (MySQL.isConnected()) {
      try {
        PreparedStatement st = MySQL.getCon()
            .prepareStatement("DELETE FROM ReportSystem_mutesdb WHERE MUTED_UUID = '" + uuid + "'");
        if (st.executeUpdate() > 0) {
          Bukkit.getScheduler().runTask(Main.getInstance(),
              () -> Bukkit.getPluginManager().callEvent(new PlayerUnMuteEvent(uuid)));

          return true;
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }


  public static ArrayList<MuteReasons> getMuteReasons() {
    ArrayList<MuteReasons> reasons = new ArrayList<>();
    ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'MUTE'");
    try {
      while (rs.next()) {
        reasons
            .add(new MuteReasons(rs.getString("NAME"), rs.getInt("id"), rs.getLong("BAN_LENGTH")));
      }
      return reasons;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return reasons;
  }

  public static long getRawMuteLength(String reason) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL.getResult(
          "SELECT BAN_LENGTH FROM ReportSystem_reasonsdb WHERE NAME = '" + reason
              + "' AND TYPE = 'MUTE'");
      try {
        if (rs.next()) {
          return rs.getLong("BAN_LENGTH");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return 0;
  }

  public static long getMuteEnd(String uuid) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL
          .getResult("SELECT MUTE_END FROM ReportSystem_mutesdb WHERE MUTED_UUID = '" + uuid + "'");
      try {
        if (rs.next()) {
          return rs.getLong("MUTE_END");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return 0;
  }

  public static String getMuteReason(String uuid) {
    if (MySQL.isConnected()) {
      ResultSet rs = MySQL
          .getResult("SELECT REASON FROM ReportSystem_mutesdb WHERE MUTED_UUID = '" + uuid + "'");
      try {
        if (rs.next()) {
          return rs.getString("REASON");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return "Unknown";
  }

  private static void addMute(String uuid) {
    MySQL.update(
        "UPDATE ReportSystem_playerdb SET MUTES = '" + (getMutes(uuid) + 1) + "' WHERE UUID = '"
            + uuid + "'");
  }

  public static int getMutes(String uuid) {
    ResultSet rs = MySQL
        .getResult("SELECT MUTES FROM ReportSystem_playerdb WHERE UUID = '" + uuid + "'");
    try {
      if (rs.next()) {
        return rs.getInt("MUTES");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static boolean submitMute(String targetuuid, String reason, String team) {
    if (!SystemManager.isProtected(targetuuid)) {
      if (MySQL.isConnected()) {
        if (existsMuteReason(reason)) {

          long ban_end = getRawMuteLength(reason) + System.currentTimeMillis();

          try {
            PreparedStatement st = MySQL.getCon().prepareStatement(
                "INSERT INTO ReportSystem_mutesdb(MUTED_UUID,TEAM_UUID,REASON,MUTE_END) VALUES ('"
                    + targetuuid + "','" + team + "','" + reason + "','" + ban_end + "')");
            if (st.executeUpdate() > 0) {
              addMute(targetuuid);
              if (SystemManager.existsPlayerData(team)) {
                Player p = Bukkit.getPlayer(SystemManager.getUsernameByUUID(targetuuid));
                if (p != null) {
                  p.sendMessage(
                      Main.getPrefix() + Main.getMSG("Messages.Mute-System.Message-If-Player-Muted")
                          .replace("%reason%", reason));
                }
                ReportManager.sendNotify("MUTE", SystemManager.getUsernameByUUID(team),
                    SystemManager.getUsernameByUUID(targetuuid), reason);
              } else {
                ReportManager
                    .sendNotify("MUTE", "Console", SystemManager.getUsernameByUUID(targetuuid),
                        reason);
              }
              new BukkitRunnable() {

                @Override
                public void run() {
                  Bukkit.getServer().getPluginManager()
                      .callEvent(new PlayerMuteEvent(targetuuid, reason, team));
                }
              }.runTask(Main.getInstance());

              return true;
            } else {
              return false;
            }
          } catch (SQLException e) {
            return false;
          }
        } else {
          return false;
        }
      } else {
        return false;
      }
    } else {
      sendConsoleNotify("PROTECT", targetuuid);
      return false;
    }
  }
}
