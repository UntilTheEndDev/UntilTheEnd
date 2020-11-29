package ute.item.materials;

import ute.cap.san.ChangeTasks;
import ute.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class NightMare {
    public static int sanityReduce = ItemManager.itemAttributes.getInt("NightMare.sanityReduce");

    public NightMare() {
        ChangeTasks.itemsChangeSanity.put(ItemManager.items.get("NightMare").displayName, sanityReduce);
    }
}
