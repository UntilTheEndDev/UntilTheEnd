package ute.item.clothes;

import ute.cap.san.ChangeTasks;
import ute.item.ItemManager;

public class Garland {
    public static int sanityImprove = ItemManager.itemAttributes.getInt("Garland.sanityImprove");

    public Garland() {
        ChangeTasks.clothesChangeSanity.put(ItemManager.items.get("Garland").displayName, sanityImprove);
    }
}
