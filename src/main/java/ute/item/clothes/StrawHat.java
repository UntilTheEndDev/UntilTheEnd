package ute.item.clothes;

import ute.cap.tem.ChangeTasks;
import ute.item.ItemManager;

public class StrawHat {
    public static double percent = ItemManager.itemAttributes.getDouble("StrawHat.percent");

    public StrawHat() {
        ChangeTasks.clothesChangeTemperature.put(ItemManager.items.get("StrawHat").displayName, percent);
    }
}
