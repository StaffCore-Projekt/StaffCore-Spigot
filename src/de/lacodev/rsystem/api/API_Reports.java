package de.lacodev.rsystem.api;

import de.lacodev.rsystem.errors.*;
import de.lacodev.rsystem.utils.ReportManager;
import de.lacodev.rsystem.utils.SystemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class API_Reports {


    /**
     * To get all existing Ban Reasons.
     *
     * @return  ArrayList with all Reasons.
     *
     *
     * **/
    public ArrayList<String> getReportReasons() {
        return ReportManager.getReportReasons();
    }

    /**
     * Creates a new Report
     *
     * @param reporterName the mc name of the player who reportet the target
     * @param target the player who is to report
     * @param reason the reason why he report the other one
     *
     * @throws ReasonNotFoundExeption when the reason was not created yet
     * **/
    public void createReport(String reporterName, Player target, String reason) throws ReasonNotFoundExeption {
        UUID uuid = UUID.fromString(SystemManager.getUUIDByName(reporterName));
        for (String reasons : getReportReasons()) {
            if (reasons.equalsIgnoreCase(reason)) {
                ReportManager.createReport(uuid.toString(), target, reason);
                return;
            }
        }
        throw new ReasonNotFoundExeption(reason);

    }

    /**
     * Creates a new Report
     *
     * @param reporterUUID the mc UUID of the player who reportet the target
     * @param target the player who is to report
     * @param reason the reason why he report the other one
     *
     * @throws ReasonNotFoundExeption when the reason was not created yet
     * **/
    public void createReport(UUID reporterUUID, Player target, String reason) throws ReasonNotFoundExeption {
        if (existsReportReason(reason)) {
            ReportManager.createReport(reporterUUID.toString(), target, reason);
        } else {
            throw new ReasonNotFoundExeption(reason);
        }
    }


    /**
     *
     * gets the number, how many reports a player have
     *
     * @param uuid the Player UUID witch checked
     *
     * @return integer, how many reports a player have
     *
     * **/
    public int getReports(UUID uuid) {
            return ReportManager.getReports(uuid.toString());
    }

    /**
     *
     * gets the number, how many reports a player have
     *
     * @param playerName the Player UUID witch checked
     *
     * @return integer, how many reports a player have
     * **/
    public int getReports(String playerName) {
            UUID uuid = UUID.fromString(SystemManager.getUUIDByName(playerName));
            return ReportManager.getReports(uuid.toString());
    }

    /**
     * creates a new Report Reason
     *
     * @param reason String witch reason you would create
     * @param itemStack ItemStack witch item is shown in the Staffcore Gui
     *
     * @throws ReasonAlreadyExistsExeption the reason was already created
     **/
    public void createReportReason(String reason, ItemStack itemStack, String senderName) throws ReasonAlreadyExistsExeption {
        if (!existsReportReason(reason)) {
            ReportManager.createReportReason(reason, itemStack, senderName);
        } else {
            throw new ReasonAlreadyExistsExeption(reason);
        }
    }

    /**
     * check if a reason is already existing
     *
     * @param reason the name of the reason you would like to check
     *
     * @return true if the reason is already existing | false if it's not
     * **/
    public boolean existsReportReason(String reason) {
            return ReportManager.existsReportReason(reason);
    }

    /**
     * Deletes the reason
     *
     * @param reason Name of the Reason you would like to delete
     *
     * @throws ReasonNotFoundExeption the Reason could not be found!
     * **/
    public void deleteReportReason(String reason, String senderName) throws ReasonNotFoundExeption{
            if (existsReportReason(reason)) {
                ReportManager.deleteReportReason(reason, senderName);
            } else {
                throw new ReasonNotFoundExeption(reason);
            }
    }

    /**
     *
     * @param target The UUID of the target, witch should be checked
     *
     * @return true if the Report is Open | false if its not
     *
     * **/
    public boolean isReportOpen(UUID target) {
        return ReportManager.isReportOpen(target.toString());
    }

    /**
     *
     * @param target The Player Name of the target, witch should be checked
     *
     * @return true if the Report is Open | false if its not
     *
     * **/
    public boolean isReportOpen(String target) {
        return ReportManager.isReportOpen(SystemManager.getUUIDByName(target));
    }

    /**
     *
     * to Claim the Report
     *
     * @param p the player you will claim it
     * @param targetuuid the Report ID (targetUUID) you would like to claim
     *
     * @throws ReportNotOpenExeption when the Report is already Claimed
     * @throws PlayerIsNotOnlineExeption when the target is offline
     * */
    public void claimReport(Player p, String targetuuid) throws ReportNotOpenExeption, PlayerIsNotOnlineExeption {
        if (isReportOpen(UUID.fromString(targetuuid))) {
            if (!(Bukkit.getPlayer(SystemManager.getUsernameByUUID(targetuuid)) == null)) {
                ReportManager.claimReport(p, targetuuid);
            } else {
                throw new PlayerIsNotOnlineExeption(SystemManager.getUsernameByUUID(targetuuid));
            }
        } else {
            throw new ReportNotOpenExeption(targetuuid);
        }
    }

}
