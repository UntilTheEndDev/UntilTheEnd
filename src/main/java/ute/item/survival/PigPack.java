package ute.item.survival;

import ute.item.ItemManager;
import ute.player.PlayerInventoryAdapt;

public class PigPack {
    public PigPack() {
        PlayerInventoryAdapt.containerSizes.put(ItemManager.items.get("PigPack").displayName, 12);
    }
}
