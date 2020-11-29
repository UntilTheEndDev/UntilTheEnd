package ute.item.survival;

import ute.cap.hum.ChangeTasks;
import ute.item.ItemManager;

public class Umbrella {
    public Umbrella() {
        ChangeTasks.umbrellas.add(ItemManager.items.get("Umbrella").displayName);
    }
}
