package de.lacodev.rsystem.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class ReportCreateEvent extends Event {
    private HandlerList handlerList = new HandlerList();

    private String reporterUUID;
    private Player target;
    private String reason;

    public ReportCreateEvent(String reporterUUID, Player target, String reason) {
        this.reporterUUID = reporterUUID;
        this.target = target;
        this.reason = reason;
    }

    /**
     *
     * @return the Player UUID who reported
     * */
    public String getReporterUUID() {
        return reporterUUID;
    }


    /**
     *
     * @return The Target Player
     * */
    public Player getTarget() {
        return target;
    }

    /**
     * @return the Reason
     * */
    public String getReason() {
        return reason;
    }


    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
