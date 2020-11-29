package ute.item.clothes;

import ute.cap.tem.ChangeTasks;
import ute.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class StrawHat {
    public static double percent = ItemManager.itemAttributes.getDouble("StrawHat.percent");

    public StrawHat() {
        ChangeTasks.clothesChangeTemperature.put(ItemManager.items.get("StrawHat").displayName, percent);
    }
}
