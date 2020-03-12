package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.EventHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class LuxuryFan implements Listener {
    public LuxuryFan() {
        HashMap<ItemStack, Integer> materials = new HashMap<ItemStack, Integer>();
        materials.put(ItemManager.namesAndItems.get("§6牛毛"), 5);
        materials.put(ItemManager.namesAndItems.get("§6芦苇"), 2);
        materials.put(ItemManager.namesAndItems.get("§6绳子"), 2);
        ItemManager.registerRecipe(materials, ItemManager.namesAndItems.get("§6豪华风扇"), "§6生存");
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasItem()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack item = event.getItem().clone();
        if (item == null) return;
        item.setAmount(1);
        if (item.equals(ItemManager.namesAndItems.get("§6豪华风扇"))) {
            event.setCancelled(true);
            if (!player.isSneaking()) return;
            if (PlayerManager.check(player, PlayerManager.CheckType.TEMPERATURE) >= 45)
                PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, -30);
            else
                PlayerManager.change(player, PlayerManager.CheckType.TEMPERATURE, 15 - PlayerManager.check(player, PlayerManager.CheckType.TEMPERATURE));
            for (Entity entity : player.getNearbyEntities(5.0, 5.0, 5.0)) {
                if (entity instanceof Player) {
                    if (PlayerManager.check((Player) entity, PlayerManager.CheckType.TEMPERATURE) >= 45)
                        PlayerManager.change((Player) entity, PlayerManager.CheckType.TEMPERATURE, -30);
                    else
                        PlayerManager.change((Player) entity, PlayerManager.CheckType.TEMPERATURE, 15 - PlayerManager.check((Player) entity, PlayerManager.CheckType.TEMPERATURE));
                }
            }
        }
    }
}
