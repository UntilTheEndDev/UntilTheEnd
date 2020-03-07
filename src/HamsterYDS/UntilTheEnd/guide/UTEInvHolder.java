package HamsterYDS.UntilTheEnd.guide;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Create at 2020/3/7 23:29
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.guide
 */
public interface UTEInvHolder extends InventoryHolder {
    @Override
    default Inventory getInventory() {
        return null;
    }
}
