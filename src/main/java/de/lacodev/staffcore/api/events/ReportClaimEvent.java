package de.lacodev.staffcore.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReportClaimEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player teamUuid;

    private final String targetuuid;

    public ReportClaimEvent(Player teamUuid, String targetuuid) {
        this.teamUuid = teamUuid;
        this.targetuuid = targetuuid;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getTeamUuid() {
        return this.teamUuid;
    }

    public String getTargetuuid() {
        return this.targetuuid;
    }
}
