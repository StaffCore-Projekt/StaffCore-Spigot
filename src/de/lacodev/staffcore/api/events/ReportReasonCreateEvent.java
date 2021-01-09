package de.lacodev.staffcore.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ReportReasonCreateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private String name;

    private ItemStack item;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ReportReasonCreateEvent(String name, ItemStack item) {
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getReportItem() {
        return this.item;
    }
}
