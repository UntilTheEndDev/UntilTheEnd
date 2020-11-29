package ute.item.survival;

import ute.item.ItemManager;
import ute.player.PlayerInventoryAdapt;

public class MovablePack {
    public MovablePack() {
        PlayerInventoryAdapt.containerSizes.put(ItemManager.items.get("MovablePack").displayName, 4);
    }
}
