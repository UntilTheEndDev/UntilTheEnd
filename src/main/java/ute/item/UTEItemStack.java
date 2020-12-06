package ute.item;

import ute.api.GuideApi;
import ute.guide.craft.CraftGuide;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class UTEItemStack {
    private static int[] slots = new int[]{24, 23, 25, 15, 14, 16, 33, 32, 34};
    public boolean canPlace;
    public boolean isConsume;
    public String id;
    public String displayName;
    public int needLevel;
    public ItemStack item;
    public List<String> lores;
    public HashMap<ItemStack, Integer> craft;

    public UTEItemStack(boolean canPlace, boolean isConsume, String id, String displayName, int needLevel,
                        ItemStack item, List<String> lores) {
        this.canPlace = canPlace;
        this.isConsume = isConsume;
        this.id = id;
        this.displayName = displayName;
        this.needLevel = needLevel;
        this.item = item;
        this.lores = lores;
    }

    public void registerRecipe(HashMap<ItemStack, Integer> craft, String category) {
        Inventory guideInv = CraftGuide.get_simple_craft_guide(this.displayName.replace("§6", "§8"));
        guideInv.setItem(19, this.item);
        switch(this.needLevel){
            case 0:
                break;
            case 1:
                guideInv.setItem(21, ItemManager.items.get("ScienceMachine").item);
                break;
            case 2:
                guideInv.setItem(21, ItemManager.items.get("AlchemyEngine").item);
                break;
            case 3:
                guideInv.setItem(21, ItemManager.items.get("Prestihatitator").item);
                break;
        }
        int index = 0;
        for (ItemStack material : craft.keySet()) {
            int amount = craft.get(material);
            craft.put(material, amount);
            ItemStack materialClone = material.clone();
            materialClone.setAmount(amount);
            guideInv.setItem(slots[index], materialClone);
            index++;
        }
        this.craft = craft;
        GuideApi.add_guide_to_item(this.item, guideInv);
        GuideApi.add_item_to_category(GuideApi.AvaliableCategories.valueOf(category), this.item);
    }
}
