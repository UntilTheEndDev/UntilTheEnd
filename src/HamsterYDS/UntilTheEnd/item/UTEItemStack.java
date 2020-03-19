package HamsterYDS.UntilTheEnd.item;

import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.api.GuideApi;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;

public class UTEItemStack {
	private static int[] slots = new int[] { 24, 23, 25, 15, 14, 16, 33, 32, 34 };
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
		Inventory guideInv = CraftGuide.getCraftInventory();
		guideInv.setItem(20, this.item);
		int index=0;
		for (ItemStack material:craft.keySet()) {
			int amount = craft.get(material);
			craft.put(material, amount);
			for (;amount>0;amount--,index++){
				guideInv.setItem(slots[index], material);
			}
		}
		this.craft = craft;
		GuideApi.addCraftToItem(this.item, guideInv);
		GuideApi.addItemToCategory(category, this.item);
	}
}
