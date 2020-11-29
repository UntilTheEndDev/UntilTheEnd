package ute.item.survival;

import ute.item.ItemManager;
import ute.player.PlayerInventoryAdapt;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class PigPack {
    public PigPack() {
        PlayerInventoryAdapt.containerSizes.put(ItemManager.items.get("PigPack").displayName, 12);
    }
}
