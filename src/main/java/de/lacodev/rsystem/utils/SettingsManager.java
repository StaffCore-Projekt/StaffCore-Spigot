package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.mysql.MySQL;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsManager {

    private final StaffCore staffCore;

    public SettingsManager(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    public void set(String key, String value) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        //INSERT INTO `reportsystem_settings` (`id`, `KEY`, `VALUE`) VALUES (NULL, 'maintenance', '1');
                        PreparedStatement ps = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                "INSERT INTO `reportsystem_settings` (`KEY`, `VALUE`) VALUES ('" + key + "','"
                                        + value + "')");
                        ps.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(staffCore);
        }
    }

    public void set(String key, boolean value) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        PreparedStatement ps;
                        if (value) {
                            ps = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                    "INSERT INTO `reportsystem_settings` (`KEY`, `VALUE`) VALUES ('" + key
                                            + "','1')");
                        } else {
                            ps = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                    "INSERT INTO `ReportSystem_settings` (`KEY`,`VALUE`) VALUES ('" + key + "','0')");
                        }
                        ps.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(staffCore);
        }
    }

    public void set(String key, int value) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        PreparedStatement ps = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                "INSERT INTO `ReportSystem_settings` (`KEY`,`VALUE`) VALUES ('" + key + "','"
                                        + value + "')");
                        ps.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(staffCore);
        }
    }


    public boolean getBoolean(String key) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            try {
                ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
                        .getResult("SELECT `VALUE` FROM `ReportSystem_settings` WHERE `KEY` = '" + key + "'");

                if (rs.next()) {
                    if (Integer.parseInt(rs.getString("VALUE")) == 1) {
                        return true;
                    } else if (Integer.parseInt(rs.getString("VALUE")) == 0) {
                        return false;
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public String get(String key) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            try {
                ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
                        .getResult("SELECT `VALUE` FROM `ReportSystem_settings` WHERE `KEY` = '" + key + "'");
                if (rs.next()) {
                    return rs.getString("VALUE");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return null;
    }


    public void update(String key, String value) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        PreparedStatement ps = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                "UPDATE `ReportSystem_settings` SET `VALUE` = '" + value + "' WHERE `KEY` = '" + key
                                        + "'");
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(staffCore);
        }
    }

    public void update(String key, boolean value) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        PreparedStatement ps;
                        if (value) {
                            ps = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                    "UPDATE `ReportSystem_settings` SET `VALUE` = '1' WHERE `KEY` = '" + key + "'");
                        } else {
                            ps = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                                    "UPDATE `ReportSystem_settings` SET `VALUE` = '0' WHERE `KEY` = '" + key + "'");
                        }
                        ps.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(staffCore);
        }
    }


    public boolean isKey(String key) {
        if (staffCore.getStaffCoreLoader().getMySQL().isConnected()) {
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL()
                    .getResult("SELECT * FROM `ReportSystem_settings` WHERE `KEY` = '" + key + "'");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        if (rs.getString("VALUE") != null) {
                            return true;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
