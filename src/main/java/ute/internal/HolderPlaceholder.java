/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/04/20 21:47:57
 *
 * UntilTheEnd/UntilTheEnd/HolderPlaceholder.java
 */

package ute.internal;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface HolderPlaceholder extends InventoryHolder {
    Inventory INVENTORY = Bukkit.createInventory(null, 9, "");

    @Override
    default Inventory getInventory() {
        return INVENTORY;
    }
}
