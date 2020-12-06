package ute.api.event.player;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import ute.item.UTEItemStack;

public class CustomItemInteractEvent extends UTEItemEvent{
    private final Player who;
    private final Action action;
    private final ItemStack item;

    public Player getWho() {
        return who;
    }

    public Action getAction() {
        return action;
    }

    public ItemStack getItem() {
        return item;
    }

    public Block getClickedBlock() {
        return clickedBlock;
    }

    public BlockFace getClickedFace() {
        return clickedFace;
    }

    public UTEItemStack getUteItem() {
        return uteItem;
    }

    private final Block clickedBlock;
    private final BlockFace clickedFace;
    private final UTEItemStack uteItem;
    public static final HandlerList handlers = new HandlerList();

    public CustomItemInteractEvent(Player who, Action action, ItemStack item, Block clickedBlock, BlockFace clickedFace, UTEItemStack uteItem) {
        this.who=who;
        this.action=action;
        this.item=item;
        this.clickedBlock=clickedBlock;
        this.clickedFace=clickedFace;
        this.uteItem=uteItem;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
