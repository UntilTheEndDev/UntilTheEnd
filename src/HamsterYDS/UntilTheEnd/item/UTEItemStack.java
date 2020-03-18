package HamsterYDS.UntilTheEnd.item;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;

public class UTEItemStack {
	private static int[] slots = new int[] { 24, 23, 25, 15, 14, 16, 33, 32, 34 };
	public boolean canPlace;
	public boolean isConsume;
	public String id;
	public String displayName;
	public int needLevel;
	public ItemStack item;
	public NamespacedKey nsk;
	public List<String> lores;
	public HashMap<ItemStack, Integer> craft;

	public UTEItemStack(boolean canPlace, boolean isConsume, String id, String displayName, int needLevel,
			ItemStack item, NamespacedKey nsk, List<String> lores) {
		this.canPlace = canPlace;
		this.isConsume = isConsume;
		this.id = id;
		this.displayName = displayName;
		this.needLevel = needLevel;
		this.item = item;
		this.nsk = nsk;
		this.lores = lores;
	}

	public void registerRecipe(HashMap<ItemStack, Integer> craft, String category) {
		ShapelessRecipe recipe = new ShapelessRecipe(this.nsk, this.item);
		Inventory guideInv = CraftGuide.getCraftInventory();
		guideInv.setItem(20, this.item);
		int index=0;
		for (ItemStack material:craft.keySet()) {
			int amount = craft.get(material);
			craft.put(material, amount);
			recipe.addIngredient(amount, material.getType());
			for (;amount>0;amount--,index++){
				guideInv.setItem(slots[index], material);
			}
		}
		this.craft = craft;
		try {
			Bukkit.addRecipe(recipe);
		} catch (Exception exception) {
		}
		UntilTheEndApi.GuideApi.addCraftToItem(this.item, guideInv);
		UntilTheEndApi.GuideApi.addItemToCategory(category, this.item);
	}
}
