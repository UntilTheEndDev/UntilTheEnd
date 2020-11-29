package ute.item.clothes;

import ute.cap.tem.ChangeTasks;
import ute.item.ItemManager;

public class Earmuff {
    public static double percent = ItemManager.itemAttributes.getDouble("Earmuff.percent");

    public Earmuff() {
        ChangeTasks.clothesChangeTemperature.put(ItemManager.items.get("Earmuff").displayName, -percent);
    }
}
