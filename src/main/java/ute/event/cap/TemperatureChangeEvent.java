package ute.event.cap;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TemperatureChangeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private ChangeCause cause;
    private double change;
    private boolean isCancelled;

    public TemperatureChangeEvent(Player player, ChangeCause cause, double change) {
        super(player);
        this.cause = cause;
        this.change = change;
        this.isCancelled = false;
    }

    public ChangeCause getCause() {
        return this.cause;
    }

    public double getChange() {
        return this.change;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
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
        WARMSTONE,ENVIRONMENT,HUMIDITY,CLOTHES,ITEMS;
    }
}
