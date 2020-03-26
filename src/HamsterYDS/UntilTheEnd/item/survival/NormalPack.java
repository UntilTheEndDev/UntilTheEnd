package HamsterYDS.UntilTheEnd.item.survival;

import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerInventoryAdapt;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class NormalPack {
    public NormalPack() {
        PlayerInventoryAdapt.containerSizes.put(ItemManager.items.get("NormalPack").displayName, 8);
    }
}
