package HamsterYDS.UntilTheEnd.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.UTEItemStack;

public class CustomBlockPlaceEvent extends BlockPlaceEvent{
	public CustomBlockPlaceEvent(Block placedBlock, BlockState replacedBlockState, Block placedAgainst,
			ItemStack itemInHand, Player thePlayer, boolean canBuild, EquipmentSlot hand,UTEItemStack item) {
		super(placedBlock, replacedBlockState, placedAgainst, itemInHand, thePlayer, canBuild, hand);
		this.item=item;
	}
	private UTEItemStack item;
	
	public UTEItemStack getCustomItem() {
		return this.item;
	}
}
