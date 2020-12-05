package ute.item.survival;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import ute.Config;
import ute.api.PlayerApi;
import ute.event.block.CustomBlockInteractEvent;
import ute.event.cap.SanityChangeEvent;
import ute.item.ItemManager;

public class SiestaLeanto implements Listener {
    public SiestaLeanto() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(CustomBlockInteractEvent event) {
        Player player = event.getWho();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (event.getCustomItem().id.equalsIgnoreCase("SiestaLeanto")) {
            event.setCancelled(true);
            if (player.getWorld().getTime() <= 23000 && player.getWorld().getTime() >= 16000) {
                player.sendMessage("[§cUntilTheEnd]§r 夜间不可使用此物品！");
                return;
            }
            boolean isBroken = Math.random() <= 0.1;
            Entity bed = player.getWorld().spawnEntity(player.getLocation(), EntityType.ARROW);
            bed.teleport(block.getLocation().add(0.5, -0.2, 0.5));
            bed.setPassenger(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 50, 1));
                    if (Math.random() <= 0.13) {
                        if (player.getHealth() + 1 < player.getMaxHealth())
                            player.setHealth(player.getHealth() + 1);
                        SanityChangeEvent event = new SanityChangeEvent(player, SanityChangeEvent.ChangeCause.SIESTALEANTO, 1);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled())
                            PlayerApi.SanityOperations.changeSanity(player,SanityChangeEvent.ChangeCause.SIESTALEANTO,1);
                    }
                    if (Math.random() <= 0.05) {
                        if (player.getFoodLevel() >= 1) player.setFoodLevel(player.getFoodLevel() - 1);
                        else {
                            cancel();
                            bed.remove();
                            if (isBroken) block.breakNaturally();
                        }
                    }
                    if (!player.isInsideVehicle()) {
                        cancel();
                        bed.remove();
                        if (isBroken) block.breakNaturally();
                    }
                }
            }.runTaskTimer(ItemManager.plugin, 0L, 1L);
        }
    }
}
