package HamsterYDS.UntilTheEnd.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import HamsterYDS.UntilTheEnd.item.UTEItemStack;

public class CustomBlockBreakEvent extends BlockBreakEvent{
	private UTEItemStack item;
	public CustomBlockBreakEvent(Block theBlock, Player player,UTEItemStack item) {
		super(theBlock, player);
		this.item=item;
	}
	public UTEItemStack getCustomItem() {
		return this.item;
	}
}
