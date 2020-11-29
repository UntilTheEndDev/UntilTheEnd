package ute.item.science;

import ute.food.RottenFoodEvents;
import ute.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Refridgerator {
    public static int fridgeEffecience = ItemManager.itemAttributes.getInt("Refridgerator.fridgeEffecience");

    public Refridgerator() {
        RottenFoodEvents.titleFactors.put(ItemManager.items.get("Refridgerator").displayName, fridgeEffecience);
    }
}
