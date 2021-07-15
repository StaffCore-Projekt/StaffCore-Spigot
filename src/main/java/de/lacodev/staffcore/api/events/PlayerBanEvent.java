package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String targetuuid;

    private final String reason;

    private final String teamUuid;

    public PlayerBanEvent(String targetuuid, String reason, String teamUuid) {
        this.targetuuid = targetuuid;
        this.reason = reason;
        this.teamUuid = teamUuid;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public String getTargetuuid() {
        return this.targetuuid;
    }

    public String getReason() {
        return this.reason;
    }

    public String getTeamUuid() {
        return this.teamUuid;
    }
}
