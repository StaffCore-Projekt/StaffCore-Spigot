package de.lacodev.rsystem.api.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @version 1.0.0
 * @author ViaEnder
 * */
public class Events implements Listener {


    //Reports
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

    //Warns
    @EventHandler
    public void onWarnPlayer(WarnPlayerEvent event) {
    }

    // Bans
    @EventHandler
    public void onBanPlayer(BanPlayerEvent event) {
    }

    @EventHandler
    public void onUnBanPlayer(UnBanPlayerEvent event) {
    }

    @EventHandler
    public void onBanReasonCreate(BanReasonCreateEvent event) {
    }

    @EventHandler
    public void onBanReasonDelete(BanReasonDeleteEvent event) {
    }

    @EventHandler
    public void onIpBanPlayer(IpBanPlayerEvent event) {
    }

    @EventHandler
    public void onIpUnBanPlayer(IpUnBanPlayerEvent event) {
    }

    @EventHandler
    public void onMuteReasonCreate(MuteReasonCreateEvent event) {
    }

    @EventHandler
    public void onMuteReasonDelete(MuteReasonDeleteEvent event) {
    }
}
