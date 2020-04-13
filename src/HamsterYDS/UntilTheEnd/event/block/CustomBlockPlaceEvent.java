package HamsterYDS.UntilTheEnd.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.UTEItemStack;

public class CustomBlockPlaceEvent extends UTEBlockEvent {
    private final Block placedBlock;
    private final BlockState replacedBlockState;
    private final Block placedAgainst;
    private final ItemStack itemInHand;
    private final Player player;
    private final boolean canBuild;
    private final EquipmentSlot hand;
    private final UTEItemStack item;

    public CustomBlockPlaceEvent(Block placedBlock, BlockState replacedBlockState, Block placedAgainst,
                                 ItemStack itemInHand, Player thePlayer, boolean canBuild, EquipmentSlot hand, UTEItemStack item) {
        this.placedBlock = placedBlock;
        this.replacedBlockState = replacedBlockState;
        this.placedAgainst = placedAgainst;
        this.itemInHand = itemInHand;
        this.player = thePlayer;
        this.canBuild = canBuild;
        this.hand = hand;
        this.item = item;
    }

    public Block getBlock() {
        return getPlacedBlock();
    }

    public Block getPlacedBlock() {
        return placedBlock;
    }

    public BlockState getReplacedBlockState() {
        return replacedBlockState;
    }

    public Block getPlacedAgainst() {
        return placedAgainst;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCanBuild() {
        return canBuild;
    }

    public EquipmentSlot getHand() {
        return hand;
    }

    public UTEItemStack getItem() {
        return item;
    }
}
