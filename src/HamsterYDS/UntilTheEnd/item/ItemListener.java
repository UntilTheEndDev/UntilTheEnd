package HamsterYDS.UntilTheEnd.item;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;

public class ItemListener implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;
        ItemStack item = event.getItemInHand().clone();
        item.setAmount(1);
        if (ItemManager.itemsAndIds.containsKey(item)) {
            String id = ItemManager.itemsAndIds.get(item);
            if (ItemManager.canPlaceBlocks.containsKey(id))
                return;
            event.setCancelled(true);
            event.getPlayer().sendMessage(UTEi18n.cacheWithPrefix("item.system.no-place"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onUse(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (player.getGameMode() == GameMode.CREATIVE)
                return;
            PlayerInventory inv = player.getInventory();
            if (inv.getItemInMainHand() == null)
                return;
            ItemStack item = inv.getItemInMainHand();
            ItemStack itemClone = item.clone();
            int amount = item.getAmount();
            itemClone.setAmount(1);
            itemClone.setDurability((short) 0);

            if (!ItemManager.itemsAndIds.containsKey(itemClone))
                return;
            String id = ItemManager.itemsAndIds.get(itemClone);
            if (ItemManager.cosumeItems.contains(id)) {
                item.setAmount(amount - 1);
            }
        }
    }

    @EventHandler
    public void onCraftVanillaRecipes(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        ItemStack result = recipe.getResult().clone();
        result.setAmount(1);
        if (ItemManager.itemsAndIds.containsKey(result))
            return;
        for (ItemStack item : event.getClickedInventory().getContents()) {
            if (item == null)
                return;
            ItemStack itemClone = item.clone();
            itemClone.setAmount(1);
            if (ItemManager.itemsAndIds.containsKey(itemClone)) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(UTEi18n.cacheWithPrefix("item.system.no-crafting"));
            }
        }
    }

    @EventHandler
    public void onCraftUTERecipes(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        ItemStack item = recipe.getResult().clone();
        item.setAmount(1);
        if (!ItemManager.itemsAndIds.containsKey(item))
            return;
        HashMap<ItemStack, Integer> materials = ItemManager.recipes.get(item);
        Inventory inv = event.getInventory();
        for (ItemStack material : materials.keySet())
            if (!inv.containsAtLeast(material, materials.get(material)))
                event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        EntityType type = event.getEntityType();
        if (!ItemProvider.drops.containsKey(type))
            return;
        String itemName = ItemProvider.drops.get(type);
        double percent = ItemProvider.percents.get(type);
        ItemStack item = ItemManager.namesAndItems.get(ItemManager.idsAndNames.get(itemName));
        World world = event.getEntity().getWorld();
        Location loc = event.getEntity().getLocation();
        while (percent >= 1.0) {
            percent -= 1.0;
            world.dropItemNaturally(loc, item);
        }
        if (Math.random() <= percent)
            world.dropItemNaturally(loc, item);
    }

    @EventHandler
    public void onDrop(ItemSpawnEvent event) {
        if (!ItemManager.plugin.getConfig().getBoolean("item.sawer.enable"))
            return;
        Item entityItem = event.getEntity();
        ItemStack item = entityItem.getItemStack();
        if (item.hasItemMeta())
            if (item.getItemMeta().hasDisplayName()) {
                entityItem.setCustomName(item.getItemMeta().getDisplayName());
                entityItem.setCustomNameVisible(true);
            }
    }
}