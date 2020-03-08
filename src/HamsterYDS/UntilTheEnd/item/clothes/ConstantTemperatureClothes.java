package HamsterYDS.UntilTheEnd.item.clothes;

import HamsterYDS.UntilTheEnd.cap.tem.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ConstantTemperatureClothes {
    public ConstantTemperatureClothes() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.namesAndItems.get("§6绳子"), 2);
        materials.put(ItemManager.namesAndItems.get("§6兔毛"), 4);
        ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6恒温服"), "§6衣物");

        ChangeTasks.clothesChangeTemperature.put("§6兔毛耳罩",0.0);
    }
}
