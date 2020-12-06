package ute.api.event.player;

import ute.player.role.Roles;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRoleChangeByCommandEvent
        extends Event
        implements Cancellable {
    private boolean cancelled = false;
    private String fallbackMessage;
    private boolean needCoins = true;

    public boolean isNeedCoins() {
        return needCoins;
    }

    public void setNeedCoins(boolean needCoins) {
        this.needCoins = needCoins;
    }

    public String getFallbackMessage() {
        return fallbackMessage;
    }

    public void setFallbackMessage(String fallbackMessage) {
        this.fallbackMessage = fallbackMessage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final Roles target;

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }

    public Roles getTarget() {
        return target;
    }

    public PlayerRoleChangeByCommandEvent(Player player, Roles target) {
        this.player = player;
        this.target = target;
    }

}
