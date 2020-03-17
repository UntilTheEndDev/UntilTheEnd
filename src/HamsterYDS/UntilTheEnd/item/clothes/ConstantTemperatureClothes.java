package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.cap.tem.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class ConstantTemperatureClothes {
    public ConstantTemperatureClothes() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.items.get("Gear").item, 2);
        materials.put(ItemManager.items.get("NightMare").item, 2);
        materials.put(ItemManager.items.get("NightMare").item, 3);
        materials.put(ItemManager.items.get("Rope").item, 2);
        ItemManager.items.get("ConstantTemperatureClothes").registerRecipe(materials, "衣物");

        ChangeTasks.clothesChangeTemperature.put(ItemManager.items.get("ConstantTemperatureClothes").displayName,2.0);
    }
}
