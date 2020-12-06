package ute.guide.craft;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Gets the custom name on a mob or block. If there is no name this method
     * will return null.
     * <p>
     * This value has no effect on players, they will always use their real
     * name.
     *
     * @return name of the mob/block or null
     */
    @Nullable
    public String getCustomName();

    /**
     * Sets a custom name on a mob or block. This name will be used in death
     * messages and can be sent to the client as a nameplate over the mob.
     * <p>
     * Setting the name to null or an empty string will clear it.
     * <p>
     * This value has no effect on players, they will always use their real
     * name.
     *
     * @param name the name to set
     */
    public void setCustomName(@Nullable String name);
}
