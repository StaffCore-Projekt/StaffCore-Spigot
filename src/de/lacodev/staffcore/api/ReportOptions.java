package de.lacodev.staffcore.api;

import de.lacodev.rsystem.utils.ReportManager;
import de.lacodev.staffcore.errors.ReasonAlreadyExistsExeption;
import de.lacodev.staffcore.errors.ReasonNotExistsExeption;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ReportOptions {
    public void createReportReason(String reason, ItemStack itemStack, String senderName) throws ReasonAlreadyExistsExeption {
        if (!existsReportReason(reason)) {
            ReportManager.createReportReason(reason, itemStack, senderName);
        } else {
            throw new ReasonAlreadyExistsExeption(reason);
        }
    }

    public void createReport(String reporter, Player target, String reason) throws ReasonNotExistsExeption {
        if (existsReportReason(reason)) {
            ReportManager.createReport(reporter, target, reason);
        } else {
            throw new ReasonNotExistsExeption(reason);
        }
    }

    public void deleteReportReason(String reason, String senderName) throws ReasonNotExistsExeption {
        if (existsReportReason(reason)) {
            ReportManager.deleteReportReason(reason, senderName);
        } else {
            throw new ReasonNotExistsExeption(reason);
        }
    }

    public void claimReport(Player user, String targetuuid) {
        ReportManager.claimReport(user, targetuuid);
    }

    public boolean isReportOpen(String targetUuid) {
        return ReportManager.isReportOpen(targetUuid);
    }

    public boolean existsReportReason(String reason) {
        return ReportManager.existsReportReason(reason);
    }

    public ArrayList<String> getReportReasons() {
        return ReportManager.getReportReasons();
    }

    public int getReports(String uuid) {
        return ReportManager.getReports(uuid);
    }
}
