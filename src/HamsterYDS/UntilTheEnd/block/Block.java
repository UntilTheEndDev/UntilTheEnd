package HamsterYDS.UntilTheEnd.block;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Block {
	public static UntilTheEnd plugin;
	public Block(UntilTheEnd plugin) {
		this.plugin=plugin;
		new BlockManager(plugin);
	}
}
