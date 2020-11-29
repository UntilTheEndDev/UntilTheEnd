package ute.item.survival;

import ute.item.ItemManager;
import ute.player.PlayerInventoryAdapt;

public class NormalPack {
    public NormalPack() {
        PlayerInventoryAdapt.containerSizes.put(ItemManager.items.get("NormalPack").displayName, 8);
    }
}
