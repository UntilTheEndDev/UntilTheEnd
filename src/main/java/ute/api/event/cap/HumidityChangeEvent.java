package ute.api.event.cap;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HumidityChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    private Player player;
    private ChangeCause cause;
    private double change;
    private boolean isCancelled;

    public HumidityChangeEvent(Player player, ChangeCause cause, double change) {
        this.player=player;
        this.cause = cause;
        this.change = change;
        this.isCancelled = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ChangeCause getCause() {
        return this.cause;
    }

    public double getChange() {
        return this.change;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public enum ChangeCause {
        WATER,RAIN,VAPOUR,ITEMS;
    }
}
