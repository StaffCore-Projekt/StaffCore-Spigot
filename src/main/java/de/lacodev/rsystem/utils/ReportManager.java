package de.lacodev.rsystem.utils;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.enums.XMaterial;
import de.lacodev.rsystem.objects.Reasons;
import me.rerere.matrix.api.HackType;
import me.rerere.matrix.api.MatrixAPI;
import me.rerere.matrix.api.MatrixAPIProvider;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportManager {

    public static Inventory reportinv;
    private final StaffCore staffCore;

    public ReportManager(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    public void openPagedReportInv(Player p, String name, int page) {

        String title = staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.Title") + ChatColor.YELLOW + name;

        if (title.length() > 32) {

            title = title.substring(0, 32);

        }

        reportinv = p.getServer().createInventory(null, 54, title);

        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {

            ArrayList<Reasons> rows = new ArrayList<>();

            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'REPORT'");

            try {

                if (rs != null) {
                    while (rs.next()) {

                        rows.add(new Reasons(
                                XMaterial.matchXMaterial(Material.getMaterial(rs.getString("REPORT_ITEM")))
                                        .parseMaterial(), rs.getString("NAME")));

                    }
                }

            } catch (SQLException e) {

                e.printStackTrace();

            }

            for (int i2 = 0; i2 < 9; i2++) {
                reportinv.setItem(i2, Data.buildPlace());
            }

            staffCore.getStaffCoreLoader().getPageManager().getPage().remove(p.getPlayer());

            staffCore.getStaffCoreLoader().getPageManager().getPage().put(p.getPlayer(), page);

            for (int i2 = 45; i2 < 54; i2++) {
                reportinv.setItem(i2, Data.buildPlace());
            }

            if (staffCore.getStaffCoreLoader().getPageManager().isPageValid(rows, page - 1, 36)) {

                reportinv.setItem(46, Data.buildItem(XMaterial.LIME_STAINED_GLASS_PANE,
                        staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.PreviousPage.Available")));

            } else {

                reportinv.setItem(46, Data.buildItem(XMaterial.RED_STAINED_GLASS_PANE,
                        staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.PreviousPage.NotAvailable")));

            }

            if (staffCore.getStaffCoreLoader().getPageManager().isPageValid(rows, page + 1, 36)) {

                reportinv.setItem(52, Data.buildItem(XMaterial.LIME_STAINED_GLASS_PANE,
                        staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.NextPage.Available")));

            } else {

                reportinv.setItem(52, Data.buildItem(XMaterial.RED_STAINED_GLASS_PANE,
                        staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.NextPage.NotAvailable")));

            }
            for (Reasons item : staffCore.getStaffCoreLoader().getPageManager().getPageItems(rows, page, 36)) {

                reportinv.setItem(reportinv.firstEmpty(),
                        Data.buildItemStack(item.getItem(), 1, 0, ChatColor.YELLOW + item.getName(),
                                staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.ReportItem-Lore.1")
                                        .replace("%target%", name),
                                staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Inventory.ReportItem-Lore.2")
                                        .replace("%reason%", item.getName())));

            }
        } else {

            reportinv
                    .setItem(13, Data.buildItem(Material.BARRIER, 1, 0, ChatColor.RED + "No Connection"));

        }

        p.openInventory(reportinv);

    }

    public ArrayList<String> getReportReasons() {
        ArrayList<String> reasons = new ArrayList<>();

        ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'REPORT'");

        try {
            if (rs != null) {
                while (rs.next()) {
                    if (!reasons.contains(rs.getString("NAME").toLowerCase())) {
                        reasons.add(rs.getString("NAME").toLowerCase());
                    }
                }
            }
            return reasons;
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "MySQL"
                            + ChatColor.DARK_GRAY + ") - " + ChatColor.RED
                            + "ERROR while collecting data from MySQL-Table");
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "System " + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "MySQL"
                            + ChatColor.DARK_GRAY + ") - (" + ChatColor.GRAY + "Query: " + ChatColor.YELLOW
                            + "getReportReasons" + ChatColor.DARK_GRAY + ")");
        }
        return reasons;
    }

    public void createReport(String p, Player target, String reason) {
        if (!(p.matches(staffCore.getStaffCoreLoader().getPermission("MatrixAntiCheat.Autoreport.Name")) || p
                .matches(staffCore.getStaffCoreLoader().getPermission("Chatfilter.ReporterName")) || p
                .matches(staffCore.getStaffCoreLoader().getPermission("SpartanAntiCheat.Autoreport.Name")))) {
            Bukkit.getScheduler().runTaskAsynchronously(staffCore, () -> {
                try {
                    PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                            "INSERT INTO ReportSystem_reportsdb(REPORTER_UUID,REPORTED_UUID,REASON,TEAM_UUID,STATUS) VALUES ('"
                                    + staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(p) + "','" + target.getUniqueId().toString() + "','"
                                    + reason + "','null','0')");
                    st.executeUpdate();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // TODO: 26.07.2021 EVENT
                            //Bukkit.getServer().getPluginManager()
                            //        .callEvent(new ReportCreateEvent(staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(p), target, reason));
                        }
                    }.runTask(staffCore);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            addReport(target.getUniqueId().toString());

            Bukkit.getPlayer(p).sendMessage(
                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.User.Report-Created"));
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(staffCore, () -> {
                try {
                    PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                            "INSERT INTO ReportSystem_reportsdb(REPORTER_UUID,REPORTED_UUID,REASON,TEAM_UUID,STATUS) VALUES ('"
                                    + p + "','" + target.getUniqueId().toString() + "','" + reason + "','null','0')");
                    st.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            new BukkitRunnable() {

                @Override
                public void run() {
                    // TODO: 26.07.2021 EVENT
                    //Bukkit.getServer().getPluginManager().callEvent(new ReportCreateEvent(p, target, reason));
                }
            }.runTask(staffCore);

            addReport(target.getUniqueId().toString());
        }

        sendNotify("REPORT", p, target.getName(), reason);
    }

    private void addReport(String uuid) {
        try {
            PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                    "UPDATE ReportSystem_playerdb SET REPORTS = '" + (getReports(uuid) + 1)
                            + "' WHERE UUID = '" + uuid + "'");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getReports(String uuid) {
        ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
                .getResult("SELECT REPORTS FROM ReportSystem_playerdb WHERE UUID = '" + uuid + "'");
        try {
            if (rs.next()) {
                return rs.getInt("REPORTS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void sendNotify(String string, String p, String string2, String reason) {
        if (string.equalsIgnoreCase("REPORT")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                    if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Report.Notify")) || all
                            .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                        ActionBarAPI.sendActionBar(all,
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.Team.Reported")
                                        .replace("%player%", p).replace("%target%", string2) + ChatColor.DARK_GRAY
                                        + " \u00BB " + ChatColor.YELLOW + reason, 60);
                        if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("General.Include-MatrixAntiCheat")) {
                            String matrixalert;

                            MatrixAPI api = MatrixAPIProvider.getAPI();
                            File file = new File("plugins//" + staffCore.getDescription().getName()
                                    + "//logs//matrixlog.yml");
                            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                            if (staffCore.getStaffCoreLoader().getMatrixAntiCheatUtils().getHackTypes().contains(reason.toUpperCase())) {
                                if (api.isEnable(HackType.valueOf(reason.toUpperCase()))) {
                                    matrixalert =
                                            ChatColor.GRAY + "VL " + ChatColor.DARK_GRAY + "� " + ChatColor.YELLOW + cfg
                                                    .getInt("Log." + staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(string2) + "." + HackType
                                                            .valueOf(reason.toUpperCase()).toString().toUpperCase() + ".VL");
                                } else {
                                    matrixalert = ChatColor.RED + "Matrix was unable to find any Violations!";
                                }
                                ActionBarAPI.sendActionBar(all, staffCore.getStaffCoreLoader().getPrefix() + matrixalert, 120);
                            }
                        }
                        if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("General.Include-Worldname")) {
                            ActionBarAPI.sendActionBar(all,
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.Team.Worldname")
                                            .replace("%worldname%",
                                                    Bukkit.getPlayer(string2).getLocation().getWorld().getName()), 180);
                        }
                    }
                    if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Report.Claim")) || all
                            .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                        TextComponent tc = new TextComponent();

                        tc.setText(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.Team.Teleport-Button"));
                        tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND,
                                "/report claim " + staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(string2)));

                        all.sendMessage("");
                        all.spigot().sendMessage(tc);
                        all.sendMessage("");
                    }
                } else {
                    if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Report.Notify")) || all
                            .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                        all.sendMessage("");
                        all.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.Team.Reported")
                                        .replace("%player%", p).replace("%target%", string2));
                        all.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.YELLOW + reason);
                        if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("General.Include-MatrixAntiCheat")) {
                            String matrixalert;

                            MatrixAPI api = MatrixAPIProvider.getAPI();
                            File file = new File("plugins//" + staffCore.getDescription().getName()
                                    + "//logs//matrixlog.yml");
                            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                            if (staffCore.getStaffCoreLoader().getMatrixAntiCheatUtils().getHackTypes().contains(reason.toUpperCase())) {
                                if (api.isEnable(HackType.valueOf(reason.toUpperCase()))) {
                                    matrixalert =
                                            ChatColor.GRAY + "VL " + ChatColor.DARK_GRAY + "� " + ChatColor.YELLOW + cfg
                                                    .getInt("Log." + staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(string2) + "." + HackType
                                                            .valueOf(reason.toUpperCase()).toString().toUpperCase() + ".VL");
                                } else {
                                    matrixalert = ChatColor.RED + "Matrix was unable to find any Violations!";
                                }
                                all.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + matrixalert);
                            }
                        }
                        if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("General.Include-Worldname")) {
                            all.sendMessage(
                                    staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.Team.Worldname")
                                            .replace("%worldname%",
                                                    Bukkit.getPlayer(string2).getLocation().getWorld().getName()));
                        }
                        all.sendMessage("");
                    }
                    if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Report.Claim")) || all
                            .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                        TextComponent tc = new TextComponent();

                        tc.setText(staffCore.getStaffCoreLoader().getPrefix() +
                                staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.Team.Teleport-Button"));
                        tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND,
                                "/report claim " + staffCore.getStaffCoreLoader().getSystemManager().getUUIDByName(string2)));

                        all.spigot().sendMessage(tc);
                        all.sendMessage("");
                    }
                }
            }
        } else if (string.equalsIgnoreCase("BAN")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Ban.Notify")) || all
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                        ActionBarAPI.sendActionBar(all,
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Notify.Team.Banned")
                                        .replace("%player%", p).replace("%target%", string2) + ChatColor.DARK_GRAY
                                        + " � " + ChatColor.YELLOW + reason);
                    } else {
                        all.sendMessage("");
                        all.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.Notify.Team.Banned")
                                .replace("%player%", p).replace("%target%", string2));
                        all.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.YELLOW + reason);
                        all.sendMessage("");
                    }
                }
            }
        } else if (string.equalsIgnoreCase("MUTE")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Mute.Notify")) || all
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                        ActionBarAPI.sendActionBar(all,
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Notify.Team.Muted")
                                        .replace("%player%", p).replace("%target%", string2) + ChatColor.DARK_GRAY
                                        + " � " + ChatColor.YELLOW + reason);
                    } else {
                        all.sendMessage("");
                        all.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Mute-System.Notify.Team.Muted")
                                .replace("%player%", p).replace("%target%", string2));
                        all.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.YELLOW + reason);
                        all.sendMessage("");
                    }
                }
            }
        } else if (string.equalsIgnoreCase("UNBAN")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("UnBan.Notify")) || all
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                        ActionBarAPI.sendActionBar(all,
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Notify.Team.Unban")
                                        .replace("%player%", p).replace("%target%", string2));
                    } else {
                        all.sendMessage("");
                        all.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Ban-System.UnBan.Notify.Team.Unban")
                                        .replace("%player%", p).replace("%target%", string2));
                        all.sendMessage("");
                    }
                }
            }
        } else if (string.equalsIgnoreCase("WARN")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.hasPermission(staffCore.getStaffCoreLoader().getPermission("Warn.Notify")) || all
                        .hasPermission(staffCore.getStaffCoreLoader().getPermission("Everything"))) {
                    if (staffCore.getStaffCoreLoader().getActionBarHook().isEnabled()) {
                        ActionBarAPI.sendActionBar(all,
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Notify")
                                        .replace("%player%", p).replace("%target%", string2) + ChatColor.DARK_GRAY
                                        + " � " + ChatColor.YELLOW + reason);
                    } else {
                        all.sendMessage("");
                        all.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Warn-System.Warn.Notify")
                                .replace("%player%", p).replace("%target%", string2));
                        all.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + ChatColor.YELLOW + reason);
                        all.sendMessage("");
                    }
                }
            }
        }
    }

    public void createReportReason(String name, ItemStack item, String senderName) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            Bukkit.getScheduler().runTaskAsynchronously(staffCore, () -> {
                if (!existsReportReason(name)) {
                    try {
                        PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                "INSERT INTO ReportSystem_reasonsdb(TYPE,NAME,REPORT_ITEM) VALUES ('REPORT','"
                                        + name + "','" + item.getType().toString() + "')");
                        st.executeUpdate();

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // TODO: 26.07.2021 EVENT
                                //Bukkit.getServer().getPluginManager()
                                //        .callEvent(new ReportReasonCreateEvent(name, item));
                            }
                        }.runTask(staffCore);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public boolean existsReportReason(String name) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult("SELECT NAME FROM ReportSystem_reasonsdb WHERE NAME = '" + name
                    + "' AND TYPE = 'REPORT'");
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

    public void deleteReportReason(String name, String senderName) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            if (existsReportReason(name)) {
                Bukkit.getScheduler().runTaskAsynchronously(staffCore, () -> {
                    try {
                        PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                "DELETE FROM ReportSystem_reasonsdb WHERE NAME = '" + name
                                        + "' AND TYPE = 'REPORT'");
                        st.executeUpdate();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // TODO: 26.07.2021 EVENT
                                //Bukkit.getServer().getPluginManager().callEvent(new ReportReasonDeleteEvent(name));
                            }
                        }.runTask(staffCore);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

            }
        }
    }

    public void claimReport(Player p, String targetuuid) {
        Bukkit.getScheduler().runTaskAsynchronously(staffCore, () -> {
            try {
                Player target = Bukkit.getPlayer(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(targetuuid));

                ArrayList<String> reporter_uuids = new ArrayList<>();

                if (isReportOpen(targetuuid)) {
                    if (target != null) {
                        PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                "UPDATE ReportSystem_reportsdb SET STATUS = '1', TEAM_UUID = '" + p.getUniqueId()
                                        .toString() + "' WHERE REPORTED_UUID = '" + targetuuid + "'");
                        st.executeUpdate();

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                if (staffCore.getStaffCoreLoader().getConfigProvider().getBoolean("General.Force-SpectatorMode")) {

                                    p.setGameMode(GameMode.SPECTATOR);

                                }
                                // TODO: 26.07.2021 EVENT
                                //Bukkit.getPluginManager().callEvent(new ReportClaimEvent(p, targetuuid));
                                p.teleport(target.getLocation());
                            }

                        }.runTask(staffCore);
                        p.sendMessage(
                                staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.Team.Claimed")
                                        .replace("%target%", target.getName()));

                        ResultSet rs1 = staffCore.getStaffCoreLoader().getMySQL().getResult(
                                "SELECT * FROM ReportSystem_reportsdb WHERE REPORTED_UUID = '" + targetuuid + "'");
                        if (rs1.next()) {
                            while (rs1.next()) {
                                if (!reporter_uuids.contains(rs1.getString("REPORTER_UUID"))) {
                                    reporter_uuids.add(rs1.getString("REPORTER_UUID"));
                                }
                            }
                            for (int i = 0; i < reporter_uuids.size(); i++) {
                                if (staffCore.getStaffCoreLoader().getSystemManager().existsPlayerData(reporter_uuids.get(i))) {
                                    Player reporter = Bukkit
                                            .getPlayer(staffCore.getStaffCoreLoader().getSystemManager().getUsernameByUUID(reporter_uuids.get(i)));

                                    if (reporter != null) {
                                        reporter.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.User.Team-Claimed-Report"));
                                    } else {
                                        staffCore.getStaffCoreLoader().getActionManager()
                                                .createAction("CLAIMED_REPORT", reporter_uuids.get(i), target.getName());
                                    }
                                }
                            }
                        }
                    } else {
                        p.sendMessage(staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Target-offline"));
                    }
                } else {
                    p.sendMessage(
                            staffCore.getStaffCoreLoader().getPrefix() + staffCore.getStaffCoreLoader().getMessage("Messages.Report-System.Notify.Team.Already-Claimed"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {

            }
        });

    }

    public boolean isReportOpen(String targetuuid) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult(
                    "SELECT * FROM ReportSystem_reportsdb WHERE REPORTED_UUID = '" + targetuuid + "'");
            try {
                while (rs.next()) {
                    if (rs.getInt("STATUS") == 0) {
                        return true;
                    } else if (rs.getInt("STATUS") == 1) {
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
