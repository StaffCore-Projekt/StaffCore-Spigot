package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActionManager  {
    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;

    public ActionManager(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    public void createAction(String type, String uuid, String desc) {
        try {
            PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                    "INSERT INTO ReportSystem_actionsdb(ACTION,EXECUTOR_UUID,DESCRIPTION) VALUES ('" + type
                            + "','" + uuid + "','" + desc + "')");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAction(int id) {
        try {
            Bukkit.getConsoleSender().sendMessage("EXECUTING...");
            PreparedStatement st = staffCore.getStaffCoreLoader().getMySQL().getCon()
                    .prepareStatement("DELETE FROM ReportSystem_actionsdb WHERE id = '" + id + "'");
            st.executeUpdate();
            Bukkit.getConsoleSender().sendMessage("EXECUTED");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
