package ute.item.survival;

import ute.cap.hum.ChangeTasks;
import ute.item.ItemManager;

public class FlowerUmbrella {
    public FlowerUmbrella() {
        ChangeTasks.umbrellas.add(ItemManager.items.get("FlowerUmbrella").displayName);
    }
}
