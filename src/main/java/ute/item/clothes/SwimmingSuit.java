package ute.item.clothes;

import ute.cap.hum.ChangeTasks;
import ute.item.ItemManager;

public class SwimmingSuit {
    public SwimmingSuit() {
        ChangeTasks.waterProofSuits.add(ItemManager.items.get("SwimmingSuit").displayName);
    }
}
