/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/08 10:06:44
 *
 * UntilTheEnd/UntilTheEnd/WetManager.java
 */

package HamsterYDS.UntilTheEnd.manager;

import java.util.ArrayList;
import java.util.List;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import HamsterYDS.UntilTheEnd.cap.hum.HumidityProvider;

public class WetManager {
    private static final String LORE = UTEi18n.cache("item.machine.wet.lore");

    public static void setWet(ItemStack stack, boolean state) {
        if (stack == null) return;
        if (getName(stack).equalsIgnoreCase(UTEi18n.cache("item.locked"))) return;
        if (state) {
            if (HumidityProvider.moistness.containsKey(stack.getType()))
                stack.setType(HumidityProvider.moistness.get(stack.getType()));
        } else {
            if (HumidityProvider.driness.containsKey(stack.getType()))
                stack.setType(HumidityProvider.driness.get(stack.getType()));
        }
        final ItemMeta meta = stack.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        if (state) {
            if (!lore.contains(LORE))
                lore.add(LORE);
        } else lore.remove(LORE);
        meta.setLore(lore);
        stack.setItemMeta(meta);
    } 
    public static String getName(ItemStack item) {
		if(item==null) return "";
		if(item.hasItemMeta())
			if(item.getItemMeta().hasDisplayName())
				return item.getItemMeta().getDisplayName();
		return "";
	}
    public static boolean isWet(ItemStack item) {
        if (item == null) return true;
        if (!item.hasItemMeta()) return false;
        if (!item.getItemMeta().hasLore()) return false;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore != null) return lore.contains(LORE);
        return false;
    }
}
