package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.mysql.MySQL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@RequiredArgsConstructor @Getter
public class SystemManager {
    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;
    private boolean experimental;
    private boolean latest;

    public boolean existsMaterial(String mat) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
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

    public void downloadLatestVersion(CommandSender sender) {
        try (BufferedInputStream in = new BufferedInputStream(
                new URL("https://downloads.lacodev.de/staffcore/files/" + getLatestDownloadPath())
                        .openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(
                     staffCore.getDataFolder().getAbsolutePath() + "/../StaffCore.jar")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;

            sender.sendMessage(
                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Downloading... " + ChatColor.DARK_GRAY + "("
                            + ChatColor.GREEN + "StaffCore v" + getLatestVersion() + ChatColor.DARK_GRAY + ")");
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            sender.sendMessage(
                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Download " + ChatColor.GREEN + "successful");
        } catch (IOException e) {
            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.RED + "Not available");
        }
    }

    private String getLatestVersion() {
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

    public boolean existsWebDatabases() {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult("SELECT UUID FROM staffcoreui_accounts");
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


    public boolean isVerified(String uuid) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
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

    private String getLatestDownloadPath() {
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

    public void downloadExperimentalVersion(CommandSender sender) {
        try (BufferedInputStream in = new BufferedInputStream(
                new URL("https://downloads.lacodev.de/staffcore/files/" + getExperimentalDownloadPath())
                        .openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(
                     staffCore.getDataFolder().getAbsolutePath() + "/../StaffCore.jar")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;

            sender.sendMessage(
                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Downloading... " + ChatColor.DARK_GRAY + "("
                            + ChatColor.GREEN + "StaffCore v" + getExperimentalVersion() + ChatColor.DARK_GRAY
                            + ")");
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            sender.sendMessage(
                    staffCore.getStaffCoreLoader().getPrefix() + ChatColor.GRAY + "Download " + ChatColor.GREEN + "successful");
        } catch (IOException e) {
            sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.RED + "Not available");
        }
    }

    private String getExperimentalVersion() {
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

    private String getExperimentalDownloadPath() {
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

    public void sendVerificationToPlayer(Player player) {
        player.sendMessage("");
        player.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Verification.Info-MSG"));

        TextComponent tc = new TextComponent();
        tc.setText(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Verification.Info-Button"));
        tc.setClickEvent(
                new ClickEvent(Action.RUN_COMMAND, "/staffui verify " + player.getUniqueId().toString()));

        player.spigot().sendMessage(tc);

        player.sendMessage("");
    }

    public void reloadStaffCore(CommandSender sender) {
        sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Reload.Started"));

        staffCore.getStaffCoreLoader().getMySQL().disconnect();

        new BukkitRunnable() {

            @Override
            public void run() {
                staffCore.getStaffCoreLoader().init(true);
                sender.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Reload.Success"));
            }

        }.runTaskLaterAsynchronously(staffCore, 3 * 20);
    }

    public void reloadStaffCore(Player player) {
        player.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Reload.Started"));

        staffCore.getStaffCoreLoader().getMySQL().disconnect();

        new BukkitRunnable() {

            @Override
            public void run() {
                staffCore.getStaffCoreLoader().init(true);
                player.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.System.Reload.Success"));
            }

        }.runTaskLaterAsynchronously(staffCore, 3 * 20);
    }

    public String getUsernameByUUID(String targetuuid) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
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

    public String getUUIDByName(String targetname) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
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

    public void createPlayerData(Player p) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
                    if (!existsPlayerData(p.getUniqueId().toString())) {
                        try {
                            PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
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

        }.runTaskAsynchronously(staffCore);
    }

    private void updateName(Player p) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            try {
                PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
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

    public boolean isProtected(String uuid) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
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

    public int getProtectionState(String uuid) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
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

    public void changeProtectionState(String playername) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            if (isProtected(getUUIDByName(playername))) {
                staffCore.getStaffCoreLoader().getMySQL().update(
                        "UPDATE ReportSystem_playerdb SET PROTECTED = '0' WHERE PLAYERNAME = '" + playername
                                + "'");
            } else {
                staffCore.getStaffCoreLoader().getMySQL().update(
                        "UPDATE ReportSystem_playerdb SET PROTECTED = '1' WHERE PLAYERNAME = '" + playername
                                + "'");
            }
        }
    }

    public boolean existsPlayerData(String uuid) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
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

    public String getLastKnownIP(String uuidByName) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            if (existsPlayerData(uuidByName)) {
                ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
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

    public String getLastOnline(String uuidByName) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            if (existsPlayerData(uuidByName)) {
                ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
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

    public void checkLizenz(URL url) {
        try {
            JSONObject ob = (JSONObject) readJsonFromUrl(url.toString()).get("1");
            String latestversion = (String) ob.get("version");

            double lizenz = Double.parseDouble(latestversion.replaceAll("\\D+", ""));
            double version = Double.parseDouble(staffCore.getDescription().getVersion().replaceAll("\\D+", ""));

            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + ""
                            + ChatColor.BOLD + "SUCCESS " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                            + "Versioncheck" + ChatColor.DARK_GRAY + ")");
            if (version >= lizenz) {
                if (version > lizenz) {
                    experimental = true;
                    Bukkit.getConsoleSender().sendMessage("");
                    Bukkit.getConsoleSender().sendMessage(
                            "" + ChatColor.GRAY + "You are using an " + ChatColor.LIGHT_PURPLE
                                    + "experimental build" + ChatColor.GRAY + "!");
                } else {
                    latest = true;
                    Bukkit.getConsoleSender().sendMessage("");
                    Bukkit.getConsoleSender().sendMessage(
                            "" + ChatColor.GRAY + "You are using the " + ChatColor.GREEN + "latest build"
                                    + ChatColor.GRAY + "!");
                }
            } else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There is an update available!");
                Bukkit.getConsoleSender().sendMessage("" + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW
                        + "https://www.spigotmc.org/resources/staffcore-1-7-1-15.48655/updates");
            }
            Bukkit.getConsoleSender().sendMessage("");
        } catch (NullPointerException e) {

        }
    }

    public JSONObject readJsonFromUrl(String url) {
        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = (JSONObject) parser.parse(
                    new InputStreamReader(new URL(url + "?latest").openStream(), StandardCharsets.UTF_8));

            return jsonObject;
        } catch (IOException | ParseException | ClassCastException e) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + ""
                            + ChatColor.BOLD + "FAILED " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY
                            + "Versioncheck" + ChatColor.DARK_GRAY + ")");
            Bukkit.getConsoleSender().sendMessage("");
        }
        return null;
    }
}
