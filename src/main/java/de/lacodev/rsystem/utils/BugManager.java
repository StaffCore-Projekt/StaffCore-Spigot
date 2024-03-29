package de.lacodev.rsystem.utils;

import de.lacodev.rsystem.StaffCore;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.objects.BugReport;
import lombok.AccessLevel;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class BugManager {
    @Getter(AccessLevel.NONE)
    private final StaffCore staffCore;

    public BugManager(StaffCore staffCore) {
        this.staffCore = staffCore;
    }

    public void createBug(BugReport bugReport) {
        try {
            PreparedStatement ps = staffCore.getStaffCoreLoader().getMySQL().getCon().prepareStatement(
                    "INSERT INTO ReportSystem_bugs(CREATOR_NAME,CREATOR_UUID,MESSAGE) VALUES ( ? , ? , ? )");
            ps.setString(1, bugReport.getPlayerName());
            ps.setString(2, "" + bugReport.getPlayerUUID());
            ps.setString(3, bugReport.getBugReport());
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public ArrayList<BugReport> getBugs() {
        try {
            ArrayList<BugReport> bugs = new ArrayList<>();
            ResultSet rs = staffCore.getStaffCoreLoader().getMySQL().getResult("SELECT * FROM ReportSystem_bugs");
            while (rs.next()) {
                // id INT, CREATOR_NAME VARCHAR, CREATOR_UUID VARCHAR, MESSAGE VARCHAR
                int id = rs.getInt("id");
                String creatorName = rs.getString("CREATOR_NAME");
                String creatorUUID = rs.getString("CREATOR_UUID");
                String report = rs.getString("MESSAGE");
                BugReport bugReport = new BugReport(id, creatorName, UUID.fromString(creatorUUID), report);
                bugs.add(bugReport);
            }
            return bugs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void deleteBug(BugReport report) {
        try {
            PreparedStatement ps = staffCore.getStaffCoreLoader().getMySQL().getCon()
                    .prepareStatement("DELETE FROM `reportsystem_bugs` WHERE `id` = ? ");
            ps.setInt(1, report.getId());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
