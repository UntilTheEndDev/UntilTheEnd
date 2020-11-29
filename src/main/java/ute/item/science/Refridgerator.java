package ute.item.science;

import ute.food.RottenFoodEvents;
import ute.item.ItemManager;

public class Refridgerator {
    public static int fridgeEffecience = ItemManager.itemAttributes.getInt("Refridgerator.fridgeEffecience");

    public Refridgerator() {
        RottenFoodEvents.titleFactors.put(ItemManager.items.get("Refridgerator").displayName, fridgeEffecience);
    }
}
