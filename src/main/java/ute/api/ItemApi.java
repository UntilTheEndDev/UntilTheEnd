package ute.api;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ute.item.ItemManager;
import ute.item.UTEItemStack;
import ute.player.PlayerManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ItemApi {
    public static Collection<UTEItemStack> get_items() {
        return ItemManager.items.values();
    }

    public static UTEItemStack get_item(String id) {
        return ItemManager.items.get(id);
    }

    public static int get_current_science(Player player) {
        int maxLevel = 0;

        for (int i = -5; i <= 5; ++i) {
            for (int j = -5; j <= 5; ++j) {
                for (int k = -5; k <= 5; ++k) {
                    Location newLoc = new Location(player.getWorld(), player.getLocation().getX() + (double) i, player.getLocation().getY() + (double) j, player.getLocation().getZ() + (double) k);
                    newLoc = newLoc.getBlock().getLocation();
                    Iterator var6 = ItemManager.machines.keySet().iterator();

                    while (var6.hasNext()) {
                        int level = (Integer) var6.next();
                        String machineId = (String) ItemManager.machines.get(level);
                        if (machineId.equalsIgnoreCase(BlockApi.getSpecialBlock(newLoc))) {
                            maxLevel = Math.max(maxLevel, level);
                        }
                    }
                }
            }
        }

        return maxLevel;
    }

    /**
     * @param player
     * @param id
     * @return 0 可合成
     */
    public static int can_craft(Player player, String id) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return 0;
        }
        int level = ((UTEItemStack) ItemManager.items.get(id)).needLevel;
        if (level > 0) {
            if (!PlayerManager.checkUnLockedRecipes(player).contains(id)) {
                int player_level = get_current_science(player);
                if (player_level < level) {
                    return 2;
                }
            }
        }
        UTEItemStack item = (UTEItemStack) ItemManager.items.get(id);
        PlayerInventory inv = player.getInventory();
        Iterator var5 = item.craft.keySet().iterator();

        int needAmount;
        int amount;
        do {
            if (!var5.hasNext()) {
                return 0;
            }

            ItemStack material = (ItemStack) var5.next();
            needAmount = (Integer) item.craft.get(material);
            HashMap<Integer, ItemStack> slots = get_similar_slots(inv, material);
            amount = 0;

            int slot;
            for (Iterator var10 = slots.keySet().iterator(); var10.hasNext(); amount += ((ItemStack) slots.get(slot)).getAmount()) {
                slot = (Integer) var10.next();
            }
        } while (amount >= needAmount);
        return 1;
    }

    public static HashMap<Integer, ItemStack> get_similar_slots(PlayerInventory inv, ItemStack material) {
        HashMap<Integer, ItemStack> slots = new HashMap();

        for (int slot = 0; slot < inv.getSize(); ++slot) {
            if (ItemManager.isSimilar(inv.getItem(slot), material)) {
                slots.put(slot, inv.getItem(slot));
            }
        }

        return slots;
    }

    public static void go_craft(Player player, String id) {
        UTEItemStack uteitem = (UTEItemStack) ItemManager.items.get(id);
        PlayerInventory inv = player.getInventory();
        Iterator var5 = uteitem.craft.keySet().iterator();

        while (true) {
            while (var5.hasNext()) {
                ItemStack material = (ItemStack) var5.next();
                int needAmount = (Integer) uteitem.craft.get(material);
                HashMap<Integer, ItemStack> slots = get_similar_slots(inv, material);
                Iterator var9 = slots.keySet().iterator();

                while (var9.hasNext()) {
                    int slot = (Integer) var9.next();
                    if (needAmount <= 0) {
                        break;
                    }

                    ItemStack item = (ItemStack) slots.get(slot);
                    int amount = item.getAmount();
                    if (amount > needAmount) {
                        item.setAmount(amount - needAmount);
                        break;
                    }

                    item.setAmount(0);
                    needAmount -= amount;
                    inv.setItem(slot, item);
                    player.updateInventory();
                }
            }

            inv.addItem(new ItemStack[]{uteitem.item});
            player.updateInventory();
            return;
        }
    }
}
