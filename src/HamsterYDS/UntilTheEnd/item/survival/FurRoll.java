package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import HamsterYDS.UntilTheEnd.internal.EventHelper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.event.hud.SanityChangeEvent;
import HamsterYDS.UntilTheEnd.event.hud.SanityChangeEvent.ChangeCause;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.death.DeathCause;
import HamsterYDS.UntilTheEnd.player.death.DeathMessage;

public class FurRoll implements Listener {
    public FurRoll() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    public static Set<UUID> sleeping = new HashSet<UUID>();

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!EventHelper.isRight(event.getAction())) return;
        if (!event.hasItem()) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
            event.setCancelled(true);
            if (player.getWorld().getEnvironment() != Environment.NORMAL) {
                player.sendMessage("[§cUntilTheEnd]§r 非主世界不可使用此物品！");
                player.getWorld().createExplosion(player.getLocation(), 3);
                if (player.isDead()) DeathMessage.causes.put(player.getName(), DeathCause.INVALIDSLEEPNESS);
                return;
            }
            if (player.getWorld().getTime() >= 23000 || player.getWorld().getTime() <= 16000) {
                player.sendMessage("[§cUntilTheEnd]§r 白天不可使用此物品！");
                return;
            }
            if (Math.random() <= 0.3) {
                ItemStack itemr = event.getItem();
                itemr.setAmount(itemr.getAmount() - 1);
            }
            sleeping.add(player.getUniqueId());
            Location loc = player.getLocation();
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 50, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 50, 1));
                    if (Math.random() <= 0.1) {
                        if (player.getHealth() + 1 < player.getMaxHealth())
                            player.setHealth(player.getHealth() + 1);
                        SanityChangeEvent event=new SanityChangeEvent(player,ChangeCause.FURROLL,1);
                        Bukkit.getPluginManager().callEvent(event);
                        if(!event.isCancelled())
                        	PlayerManager.change(player, PlayerManager.CheckType.SANITY, 1);
                    }
                    if (Math.random() <= 0.07) {
                        if (player.getFoodLevel() >= 1) player.setFoodLevel(player.getFoodLevel() - 1);
                        else {
                            cancel();
                            sleeping.remove(player.getUniqueId());
                        }
                    }
                    if (!BlockApi.locToStr(player.getLocation()).equalsIgnoreCase(BlockApi.locToStr(loc))) {
                        cancel();
                        sleeping.remove(player.getUniqueId());
                    }
                }
            }.runTaskTimer(ItemManager.plugin, 0L, 1L);
        }
    }
}
