package ute.item.survival;

import ute.cap.hum.ChangeTasks;
import ute.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Umbrella {
    public Umbrella() {
        ChangeTasks.umbrellas.add(ItemManager.items.get("Umbrella").displayName);
    }
}
