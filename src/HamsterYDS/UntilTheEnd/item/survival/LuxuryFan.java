package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

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
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        ItemStack item = player.getItemInHand().clone();
        if (item == null) return;
        item.setAmount(1);
        if (item.equals(ItemManager.namesAndItems.get("§6豪华风扇"))) {
            event.setCancelled(true);
            if (!player.isSneaking()) return;
            if (PlayerManager.check(player, "tem") >= 45) PlayerManager.change(player, "tem", -30);
            else PlayerManager.change(player, "tem", 15 - PlayerManager.check(player, "tem"));
            for (Entity entity : player.getNearbyEntities(5.0, 5.0, 5.0)) {
                if (entity instanceof Player) {
                    if (PlayerManager.check((Player) entity, "tem") >= 45)
                        PlayerManager.change((Player) entity, "tem", -30);
                    else
                        PlayerManager.change((Player) entity, "tem", 15 - PlayerManager.check((Player) entity, "tem"));
                }
            }
        }
    }
}
