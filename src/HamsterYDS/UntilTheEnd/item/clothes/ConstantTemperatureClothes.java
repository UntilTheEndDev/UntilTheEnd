package HamsterYDS.UntilTheEnd.item.clothes;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.cap.tem.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class ConstantTemperatureClothes {
    public ConstantTemperatureClothes() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.namesAndItems.get("§6齿轮"), 2);
        materials.put(ItemManager.namesAndItems.get("§6电器元件"), 2);
        materials.put(ItemManager.namesAndItems.get("§6噩梦燃料"), 3);
        materials.put(ItemManager.namesAndItems.get("§6绳子"), 2);
        ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6恒温服"), "§6衣物");

        ChangeTasks.clothesChangeTemperature.put("§6恒温服",2.0);
    }
}
