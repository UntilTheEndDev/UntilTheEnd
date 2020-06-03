/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/03 12:04:28
 *
 * until-the-end/until-the-end.main/PlayerRoleChangeByCommandEvent.java
 */

package HamsterYDS.UntilTheEnd.event.player;

import HamsterYDS.UntilTheEnd.player.role.Roles;
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
