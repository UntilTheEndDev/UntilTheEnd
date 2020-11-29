package ute.item.clothes;

import ute.cap.san.ChangeTasks;
import ute.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Garland {
    public static int sanityImprove = ItemManager.itemAttributes.getInt("Garland.sanityImprove");

    public Garland() {
        ChangeTasks.clothesChangeSanity.put(ItemManager.items.get("Garland").displayName, sanityImprove);
    }
}
