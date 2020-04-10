package HamsterYDS.UntilTheEnd.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.UTEItemStack;

public class CustomBlockInteractEvent extends PlayerInteractEvent{
	private UTEItemStack item;
	public CustomBlockInteractEvent(Player who, Action action, ItemStack item, Block clickedBlock,
			BlockFace clickedFace, EquipmentSlot hand, UTEItemStack newitem) {
		super(who, action, item, clickedBlock, clickedFace, hand);
		this.item=newitem;
	}
	public UTEItemStack getCustomItem() {
		return this.item;
	}
}
