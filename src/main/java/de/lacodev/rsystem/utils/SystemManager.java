package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SystemManager {

    public static boolean existsMaterial(String mat) {
        if (MySQL.isConnected()) {
            ResultSet rs = MySQL
                    .getResult("SELECT TYPE FROM ReportSystem_materialsdb WHERE TYPE = '" + mat + "'");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        return rs.getString("TYPE") != null;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void downloadLatestVersion(CommandSender sender) {
        try (BufferedInputStream in = new BufferedInputStream(
                new URL("https://downloads.lacodev.de/staffcore/files/" + getLatestDownloadPath())
                        .openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(
                     Main.getInstance().getDataFolder().getAbsolutePath() + "/../StaffCore.jar")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;

            sender.sendMessage(
                    Main.getPrefix() + ChatColor.GRAY + "Downloading... " + ChatColor.DARK_GRAY + "("
                            + ChatColor.GREEN + "StaffCore v" + getLatestVersion() + ChatColor.DARK_GRAY + ")");
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            sender.sendMessage(
                    Main.getPrefix() + ChatColor.GRAY + "Download " + ChatColor.GREEN + "successful");
        } catch (IOException e) {
            sender.sendMessage(Main.getPrefix() + ChatColor.RED + "Not available");
        }
    }

    private static String getLatestVersion() {
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(
                    new URL("https://api.lacodev.de/staffcore/versions/?latest").openStream(),
                    StandardCharsets.UTF_8));

            JSONObject obj = (JSONObject) jsonObject.get("1");

            return (String) obj.get("version");

        } catch (IOException | ParseException | ClassCastException e) {

        }
        return null;
    }

    public static boolean existsWebDatabases() {
        if (MySQL.isConnected()) {
            ResultSet rs = MySQL.getResult("SELECT UUID FROM staffcoreui_accounts");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        return true;
                    }
                }
            } catch (SQLException ignored) {
                return false;
            }
        }
        return false;
    }


    public static boolean isVerified(String uuid) {
        if (MySQL.isConnected()) {
            ResultSet rs = MySQL
                    .getResult("SELECT UUID FROM staffcoreui_accounts WHERE UUID = '" + uuid + "'");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        return rs.getString("UUID") != null;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static String getLatestDownloadPath() {
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(
                    new URL("https://api.lacodev.de/staffcore/versions/?latest").openStream(),
                    StandardCharsets.UTF_8));

            JSONObject obj = (JSONObject) jsonObject.get("1");

            return (String) obj.get("url");

        } catch (IOException | ParseException | ClassCastException e) {

        }
        return null;
    }

    public static void downloadExperimentalVersion(CommandSender sender) {
        try (BufferedInputStream in = new BufferedInputStream(
                new URL("https://downloads.lacodev.de/staffcore/files/" + getExperimentalDownloadPath())
                        .openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(
                     Main.getInstance().getDataFolder().getAbsolutePath() + "/../StaffCore.jar")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;

            sender.sendMessage(
                    Main.getPrefix() + ChatColor.GRAY + "Downloading... " + ChatColor.DARK_GRAY + "("
                            + ChatColor.GREEN + "StaffCore v" + getExperimentalVersion() + ChatColor.DARK_GRAY
                            + ")");
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            sender.sendMessage(
                    Main.getPrefix() + ChatColor.GRAY + "Download " + ChatColor.GREEN + "successful");
        } catch (IOException e) {
            sender.sendMessage(Main.getPrefix() + ChatColor.RED + "Not available");
        }
    }

    private static String getExperimentalVersion() {
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(
                    new URL("https://api.lacodev.de/staffcore/versions/?experimental_latest").openStream(),
                    StandardCharsets.UTF_8));

            JSONObject obj = (JSONObject) jsonObject.get("1");

            return (String) obj.get("version");

        } catch (IOException | ParseException | ClassCastException e) {

        }
        return null;
    }

    private static String getExperimentalDownloadPath() {
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(
                    new URL("https://api.lacodev.de/staffcore/versions/?experimental_latest").openStream(),
                    StandardCharsets.UTF_8));

            JSONObject obj = (JSONObject) jsonObject.get("1");

            return (String) obj.get("url");

        } catch (IOException | ParseException | ClassCastException e) {

        }
        return null;
    }

    public static void sendVerificationToPlayer(Player player) {
        player.sendMessage("");
        player.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Verification.Info-MSG"));

        TextComponent tc = new TextComponent();
        tc.setText(Main.getPrefix() + Main.getMSG("Messages.System.Verification.Info-Button"));
        tc.setClickEvent(
                new ClickEvent(Action.RUN_COMMAND, "/staffui verify " + player.getUniqueId().toString()));

        player.spigot().sendMessage(tc);

        player.sendMessage("");
    }

    public static void reloadStaffCore(CommandSender sender) {
        sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Reload.Started"));

        Main.getInstance().reloadConfig();
        Main.getInstance().applyConfigs();

        MySQL.disconnect();

        new BukkitRunnable() {

            @Override
            public void run() {
                Main.getInstance().onReload();
                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Reload.Success"));
            }

        }.runTaskLaterAsynchronously(Main.getInstance(), 3 * 20);
    }

    public static void reloadStaffCore(Player player) {
        player.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Reload.Started"));

        Main.getInstance().reloadConfig();
        Main.getInstance().applyConfigs();

        MySQL.disconnect();

        new BukkitRunnable() {

            @Override
            public void run() {
                Main.getInstance().onReload();
                player.sendMessage(Main.getPrefix() + Main.getMSG("Messages.System.Reload.Success"));
            }

        }.runTaskLaterAsynchronously(Main.getInstance(), 3 * 20);
    }

    public static String getUsernameByUUID(String targetuuid) {
        if (MySQL.isConnected()) {
            ResultSet rs = MySQL.getResult(
                    "SELECT PLAYERNAME FROM ReportSystem_playerdb WHERE UUID = '" + targetuuid + "'");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        return rs.getString("PLAYERNAME");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "UNKNOWN";
    }

    public static String getUUIDByName(String targetname) {
        if (MySQL.isConnected()) {
            ResultSet rs = MySQL.getResult(
                    "SELECT UUID FROM ReportSystem_playerdb WHERE PLAYERNAME = '" + targetname + "'");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        return rs.getString("UUID");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "UNKNOWN";
    }

    public static void createPlayerData(Player p) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (MySQL.isConnected()) {
                    if (!existsPlayerData(p.getUniqueId().toString())) {
                        try {
                            PreparedStatement st = MySQL.getCon().prepareStatement(
                                    "INSERT INTO ReportSystem_playerdb(UUID,PLAYERNAME,BANS,MUTES,REPORTS,WARNS,LAST_KNOWN_IP,LAST_ONLINE,PROTECTED) VALUES ('"
                                            + p.getUniqueId().toString() + "','" + p.getName() + "','0','0','0','0','" + p
                                            .getAddress().getAddress() + "','" + System.currentTimeMillis() + "','0')");
                            st.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        updateName(p);
                    }
                }
            }

        }.runTaskAsynchronously(Main.getInstance());
    }

    private static void updateName(Player p) {
        if (MySQL.isConnected()) {
            try {
                PreparedStatement st = MySQL.getCon().prepareStatement(
                        "UPDATE ReportSystem_playerdb SET PLAYERNAME = '" + p.getName() + "',LAST_ONLINE = '"
                                + System.currentTimeMillis() + "',PROTECTED = '" + getProtectionState(
                                p.getUniqueId().toString()) + "' WHERE UUID = '" + p.getUniqueId().toString()
                                + "'");
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isProtected(String uuid) {
        if (MySQL.isConnected()) {
            ResultSet rs = MySQL
                    .getResult("SELECT PROTECTED FROM ReportSystem_playerdb WHERE UUID = '" + uuid + "'");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        return rs.getInt("PROTECTED") == 1;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int getProtectionState(String uuid) {
        if (MySQL.isConnected()) {
            ResultSet rs = MySQL
                    .getResult("SELECT PROTECTED FROM ReportSystem_playerdb WHERE UUID = '" + uuid + "'");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        return rs.getInt("PROTECTED");
                    } else {
                        return 0;
                    }
                }
            } catch (SQLException e) {
                return 0;
            }
        }
        return 0;
    }

    public static void changeProtectionState(String playername) {
        if (MySQL.isConnected()) {
            if (isProtected(getUUIDByName(playername))) {
                MySQL.update(
                        "UPDATE ReportSystem_playerdb SET PROTECTED = '0' WHERE PLAYERNAME = '" + playername
                                + "'");
            } else {
                MySQL.update(
                        "UPDATE ReportSystem_playerdb SET PROTECTED = '1' WHERE PLAYERNAME = '" + playername
                                + "'");
            }
        }
    }

    public static boolean existsPlayerData(String uuid) {
        if (MySQL.isConnected()) {
            ResultSet rs = MySQL
                    .getResult("SELECT UUID FROM ReportSystem_playerdb WHERE UUID = '" + uuid + "'");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        return rs.getString("UUID") != null;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String getLastKnownIP(String uuidByName) {
        if (MySQL.isConnected()) {
            if (existsPlayerData(uuidByName)) {
                ResultSet rs = MySQL.getResult(
                        "SELECT LAST_KNOWN_IP FROM ReportSystem_playerdb WHERE UUID = '" + uuidByName + "'");
                try {
                    if (rs != null) {
                        if (rs.next()) {
                            return rs.getString("LAST_KNOWN_IP");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ChatColor.RED + "UNKNOWN";
    }

    public static String getLastOnline(String uuidByName) {
        if (MySQL.isConnected()) {
            if (existsPlayerData(uuidByName)) {
                ResultSet rs = MySQL.getResult(
                        "SELECT LAST_ONLINE FROM ReportSystem_playerdb WHERE UUID = '" + uuidByName + "'");
                try {
                    if (rs != null) {
                        if (rs.next()) {

                            Date date = new Date(rs.getLong("LAST_ONLINE"));

                            return date.toString();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ChatColor.RED + "UNKNOWN";
    }

}
