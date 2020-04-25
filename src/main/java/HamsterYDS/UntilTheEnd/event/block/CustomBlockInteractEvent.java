package HamsterYDS.UntilTheEnd.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.UTEItemStack;

public class CustomBlockInteractEvent extends UTEBlockEvent {
    private final Player who;
    private final Action action;
    private final Block clickedBlock;
    private final BlockFace clickedFace;

    public Player getWho() {
        return who;
    }

    public Action getAction() {
        return action;
    }

    public Block getClickedBlock() {
        return clickedBlock;
    }

    public BlockFace getClickedFace() {
        return clickedFace;
    }

    public EquipmentSlot getHand() {
        return hand;
    }

    public UTEItemStack getNewitem() {
        return newitem;
    }

    public ItemStack getItem() {
        return item;
    }

    private final EquipmentSlot hand;
    private final UTEItemStack newitem;
    private final ItemStack item;

    public CustomBlockInteractEvent(Player who, Action action, ItemStack item, Block clickedBlock,
                                    BlockFace clickedFace, EquipmentSlot hand, UTEItemStack newitem) {
        this.who = who;
        this.action = action;
        this.item = item;
        this.clickedBlock = clickedBlock;
        this.clickedFace = clickedFace;
        this.hand = hand;
        this.newitem = newitem;
    }

    public UTEItemStack getCustomItem() {
        return this.newitem;
    }
}
