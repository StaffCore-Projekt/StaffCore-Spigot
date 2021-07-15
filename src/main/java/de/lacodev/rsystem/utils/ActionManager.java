package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.mysql.MySQL;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActionManager {

    public static void createAction(String type, String uuid, String desc) {
        try {
            PreparedStatement st = MySQL.getCon().prepareStatement(
                    "INSERT INTO ReportSystem_actionsdb(ACTION,EXECUTOR_UUID,DESCRIPTION) VALUES ('" + type
                            + "','" + uuid + "','" + desc + "')");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAction(int id) {
        try {
            Bukkit.getConsoleSender().sendMessage("EXECUTING...");
            PreparedStatement st = MySQL.getCon()
                    .prepareStatement("DELETE FROM ReportSystem_actionsdb WHERE id = '" + id + "'");
            st.executeUpdate();
            Bukkit.getConsoleSender().sendMessage("EXECUTED");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
