package HamsterYDS.UntilTheEnd.world;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.LightingCompensation;
import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.death.DeathCause;
import HamsterYDS.UntilTheEnd.player.death.DeathMessage;

/**
 * @author 鍗楀涓朵粨榧�
 * @version V5.1.1
 */
public class InfluenceTasks {
    public static UntilTheEnd plugin;
    public static long up = HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getLong("world.blind.up");
    public static long down = HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getLong("world.blind.downTime");
    public static int warn = HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.darkness.warnTime");
    public static int attack = HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.darkness.attackTime");
    public static int damage = HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.darkness.darkDamage");
    public static int san_warn = HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.darkness.sanWarn");
    public static int san_attack = HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.darkness.sanAttack");
    public static int carrotEffect = HamsterYDS.UntilTheEnd.world.World.plugin.getConfig().getInt("world.darkness.carrotEffect");
 
    public InfluenceTasks(UntilTheEnd plugin) {
        this.plugin = plugin;
        new Blindness().runTaskTimer(plugin, 0L, 50L);
        Darkness dark = new Darkness();
        dark.runTaskTimer(plugin, 0L, 20L);
        plugin.getServer().getPluginManager().registerEvents(dark, plugin);
    }

    public class Blindness extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                long time = world.getTime();
                if (time >= up && time <= down)
                    for (Player player : world.getPlayers()) {
                        if (NPCChecker.isNPC(player)) continue;
                        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) continue;
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
                    }
            }
        }
    }

    public class Darkness extends BukkitRunnable implements Listener {
        private HashMap<String, Integer> darkness = new HashMap<String, Integer>();
        private HashMap<String, Integer> carrotEffects = new HashMap<String, Integer>();

        @EventHandler
        public void onUse(PlayerItemConsumeEvent event) {
            Player player = event.getPlayer();
            if (event.getItem().getType() == Material.CARROT) {
                int currentEffect = 0;
                if (carrotEffects.containsKey(player.getName()))
                    currentEffect += carrotEffects.get(player.getName());
                carrotEffects.remove(player.getName());
                carrotEffects.put(player.getName(), currentEffect + carrotEffect);
            }
            if (event.getItem().getType() == Material.GOLDEN_CARROT) {
                int currentEffect = 0;
                if (carrotEffects.containsKey(player.getName()))
                    currentEffect += carrotEffects.get(player.getName());
                carrotEffects.remove(player.getName());
                carrotEffects.put(player.getName(), currentEffect + carrotEffect * 5);
            }
        }

        @EventHandler
        public void onDeath(PlayerDeathEvent event) {
            if (darkness.containsKey(event.getEntity().getName())) {
                if (carrotEffects.containsKey(event.getEntity().getName())) {
                    int currentEffect = carrotEffects.get(event.getEntity().getName());
                    carrotEffects.remove(event.getEntity().getName());
                    carrotEffects.put(event.getEntity().getName(), 10 + currentEffect);
                } else carrotEffects.put(event.getEntity().getName(), 10);
                darkness.remove(event.getEntity().getName());
            }
        }

        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
                        continue;
                    if (carrotEffects.containsKey(player.getName())) {
                        int currentEffect = carrotEffects.get(player.getName());
                        carrotEffects.remove(player.getName());
                        carrotEffects.put(player.getName(), currentEffect - 1);
                        if (currentEffect >= 0) continue;
                    }
                    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) continue;
                    int needToCheck = 0; // TODO: Config setting.
                    final Location location = player.getLocation().add(0, 1, 0); // 鍗＄伒榄傛矙
                    if (location.getY() < 1) return;
                    final Block block = location.getBlock();
                    if (location.getY() >= location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ())) {
                        needToCheck -= LightingCompensation.getComp(player.getWorld().getUID());
                    }
                    if ((block.getLightFromBlocks() == 0 &&
                            player.getWorld().getTime() <= InfluenceTasks.down &&
                            player.getWorld().getTime() >= InfluenceTasks.up) || (
                            block.getLightLevel() <= needToCheck
                    )) {
                        if (darkness.containsKey(player.getName())) {
                            int second = darkness.get(player.getName());
                            darkness.remove(player.getName());
                            darkness.put(player.getName(), second + 1);
                        } else darkness.put(player.getName(), 1);
                    } else darkness.remove(player.getName());

                    if (darkness.containsKey(player.getName())) {
                        if (darkness.get(player.getName()) == warn) {
                            player.sendTitle(UTEi18n.cache("mechanism.darkness.who-is-there.main"), UTEi18n.cache("mechanism.darkness.who-is-there.sub"));
                            PlayerManager.change(player, PlayerManager.CheckType.SANITY, san_warn);
                        }
                        if (darkness.get(player.getName()) >= attack) {
                            player.sendTitle(UTEi18n.cache("mechanism.darkness.hurt-me.main"), UTEi18n.cache("mechanism.darkness.hurt-me.sub"));
                            player.damage(damage);
                            if (player.getHealth() <= san_attack)
                                DeathMessage.causes.put(player.getName(), DeathCause.DARKNESS);
                            PlayerManager.change(player, PlayerManager.CheckType.SANITY, san_attack);
                        }
                    }
                }
            }
        }
    }
}
