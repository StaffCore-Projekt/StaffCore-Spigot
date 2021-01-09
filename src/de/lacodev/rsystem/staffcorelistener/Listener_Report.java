package de.lacodev.rsystem.staffcorelistener;

import de.lacodev.staffcore.api.events.ReportClaimEvent;
import de.lacodev.staffcore.api.events.ReportCreateEvent;
import de.lacodev.staffcore.api.events.ReportReasonCreateEvent;
import de.lacodev.staffcore.api.events.ReportReasonDeleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Listener_Report implements Listener {

    @EventHandler
    public void onReportClaim(ReportClaimEvent event) {
    }

    @EventHandler
    public void onReportCreate(ReportCreateEvent event) {
    }

    @EventHandler
    public void onReportReasonCreate(ReportReasonCreateEvent event) {
    }

    @EventHandler
    public void onReportReasonDelete(ReportReasonDeleteEvent event) {
    }
}
