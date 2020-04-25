package HamsterYDS.UntilTheEnd.item.clothes;

import HamsterYDS.UntilTheEnd.cap.tem.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

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
