package HamsterYDS.UntilTheEnd.cap.tem;

import java.util.ArrayList;

import HamsterYDS.UntilTheEnd.internal.ItemFactory;
import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider.FMBlock;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.death.DeathCause;
import HamsterYDS.UntilTheEnd.player.death.DeathMessage;

public class InfluenceTasks {
    public static UntilTheEnd plugin;
    public static double smoulderPercent = Temperature.yaml.getLong("smoulderPercent");
    public static long smoulderSpeed = Temperature.yaml.getLong("smoulderSpeed");
    public static int smoulderTimeout = Temperature.yaml.getInt("smoulderTimeout");
    public static long smoulderCancellerFireTicks = Temperature.yaml.getLong("smoulderCancellerFireTicks");
    public static int coldTem = Temperature.yaml.getInt("coldTem");
    public static int hotTem = Temperature.yaml.getInt("hotTem");
    public static long fmChangeSpeed = Temperature.yaml.getLong("fmChangeSpeed");

    public InfluenceTasks(UntilTheEnd plugin) {
        this.plugin = plugin;
        if(Temperature.yaml.getBoolean("enable.smoulder")){
        	new Smoulder().runTaskTimer(plugin, 0L, smoulderSpeed);
        	plugin.getServer().getPluginManager().registerEvents(new Smoulder(),plugin);
        }
        new Damager().runTaskTimer(plugin, 0L, 20L);
        if(Temperature.yaml.getBoolean("enable.fmChange"))
        	new FMChange().runTaskTimer(plugin, 0L, fmChangeSpeed);
    }

    public class Damager extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    int playerTem = (int) PlayerManager.check(player, PlayerManager.CheckType.TEMPERATURE);
                    if (playerTem > hotTem) {
                        if (player.getHealth() <= 0.2 * (playerTem - hotTem))
                            DeathMessage.causes.put(player.getName(), DeathCause.HOTNESS);
                        player.damage(0.2 * (playerTem - hotTem));
                    }
                    if (playerTem < coldTem) {
                        if (player.getHealth() <= 0.2 * (coldTem - playerTem))
                            DeathMessage.causes.put(player.getName(), DeathCause.COLDNESS);
                        player.damage(0.2 * (coldTem - playerTem));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 100));
                    }
                }
        }
    }

    public class FMChange extends BukkitRunnable {
        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    Location playerLoc = player.getLocation();
                    for (int i = -3; i <= 3; i++)
                        for (int j = -3; j <= 3; j++)
                            for (int k = -3; k <= 3; k++) {
                                Location loc = new Location(playerLoc.getWorld(), playerLoc.getX() + i, playerLoc.getY() + j, playerLoc.getZ() + k);
                                if (TemperatureProvider.fmBlocks.containsKey(ItemFactory.getType(loc.getBlock()))) {
                                    FMBlock fmBlock = TemperatureProvider.fmBlocks.get(ItemFactory.getType(loc.getBlock()));
                                    double tem = ((
                                            TemperatureProvider.getBlockTemperature(loc.add(0, 1, 0)) +
                                                    TemperatureProvider.getBlockTemperature(loc.add(0, -1, 0)) +
                                                    TemperatureProvider.getBlockTemperature(loc.add(1, 0, 0)) +
                                                    TemperatureProvider.getBlockTemperature(loc.add(-1, 0, 0)) +
                                                    TemperatureProvider.getBlockTemperature(loc.add(0, 0, 1)) +
                                                    TemperatureProvider.getBlockTemperature(loc.add(0, 0, -1))
                                    ) / 6.0);
                                    if(Math.random()<=0.8) continue;
                                    if (fmBlock.upOrDown)
                                        if (tem >= fmBlock.temperature)
                                            loc.getBlock().setType(fmBlock.newMaterial);
                                    if (!fmBlock.upOrDown)
                                        if (tem <= fmBlock.temperature)
                                            loc.getBlock().setType(fmBlock.newMaterial);
                                }
                            }
                }
        }
    }

    public static class Smoulder extends BukkitRunnable implements Listener {
        public ArrayList<String> smoulderingBlocks = new ArrayList<String>();

        @EventHandler
        public void onRight(PlayerInteractEvent event) {
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            Location loc = block.getLocation();
            String toString = BlockApi.locToStr(loc);
            if (smoulderingBlocks.contains(toString)) {
                smoulderingBlocks.remove(toString);
                player.setFireTicks((int) smoulderCancellerFireTicks);
                player.sendTitle(UTEi18n.cache("cap.tem.smoulder.title.main"), UTEi18n.cache("cap.tem.smoulder.title.sub"));
            }
        }

        @Override
        public void run() {
            for (World world : Config.enableWorlds) {
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    Location playerLoc = player.getLocation();
                    for (int i = -3; i <= 3; i++)
                        for (int j = -3; j <= 3; j++)
                            for (int k = -3; k <= 3; k++) {
                                Location loc = new Location(playerLoc.getWorld(), playerLoc.getX() + i, playerLoc.getY() + j, playerLoc.getZ() + k);
                                if (TemperatureProvider.fmBlocks.containsKey(loc.getBlock().getType())) {
                                    FMBlock fmBlock = TemperatureProvider.fmBlocks.get(loc.getBlock().getType());
                                    double tem = TemperatureProvider.getBlockTemperature(loc);
                                    if (fmBlock.upOrDown)
                                        if (tem >= fmBlock.temperature)
                                            loc.getBlock().setType(fmBlock.newMaterial);
                                    if (!fmBlock.upOrDown)
                                        if (tem <= fmBlock.temperature)
                                            loc.getBlock().setType(fmBlock.newMaterial);
                                }
                            }
                    int x = (int) (Math.random() * 17 - Math.random() * 17);
                    int y = (int) (Math.random() * 17 - Math.random() * 17);
                    int z = (int) (Math.random() * 17 - Math.random() * 17);
                    Location loc = playerLoc.add(x, y, z);
                    if (loc.getBlock().getType()==Material.AIR) continue;
                    if (!loc.getBlock().getType().isFlammable()) continue;
                    int blockTem = (int) TemperatureProvider.getBlockTemperature(loc);
                    if (blockTem >= hotTem && Math.random() <= smoulderPercent) {
                        boolean isPrevented = false;
                        for (String str : BlockApi.getSpecialBlocks("IceFlingomatic")) {
                            Location iceLoc = BlockApi.strToLoc(str);
                            if (iceLoc.distance(loc) <= 20)
                                isPrevented = true;
                        }
                        if (isPrevented) {
                            loc.getWorld().spawnParticle(Particle.SNOWBALL, loc.add(0.0, 0.5, 0.0), 5);
                            continue;
                        }
                        String locStr = BlockApi.locToStr(loc);
                        smoulderingBlocks.add(locStr);
                        new BukkitRunnable() {
                            int counter = smoulderTimeout;

                            @Override
                            public void run() {
                                if (!smoulderingBlocks.contains(locStr)) cancel();
                                counter -= 20;
                                loc.getWorld().spawnParticle(Particle.LAVA, loc, 10);
                                if (counter <= 0) {
                                    Block block = loc.getBlock();
                                    block.breakNaturally();
                                    block.setType(Material.FIRE);
                                    cancel();
                                    smoulderingBlocks.remove(locStr);
                                }
                            }
                        }.runTaskTimer(plugin, 0L, 20L);
                    }
                }
            }
        }
    }
}
