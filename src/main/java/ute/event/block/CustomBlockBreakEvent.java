package ute.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import ute.item.UTEItemStack;

public class CustomBlockBreakEvent extends UTEBlockEvent {

    private final Block block;
    private final Player player;
    private final UTEItemStack item;

    public CustomBlockBreakEvent(Block block, Player player, UTEItemStack item) {
        this.block = block;
        this.player = player;
        this.item = item;
    }

    public UTEItemStack getCustomItem() {
        return this.item;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }
}
