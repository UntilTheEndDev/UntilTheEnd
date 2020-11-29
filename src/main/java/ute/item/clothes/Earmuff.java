package ute.item.clothes;

import ute.cap.tem.ChangeTasks;
import ute.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Earmuff {
    public static double percent = ItemManager.itemAttributes.getDouble("Earmuff.percent");

    public Earmuff() {
        ChangeTasks.clothesChangeTemperature.put(ItemManager.items.get("Earmuff").displayName, -percent);
    }
}
