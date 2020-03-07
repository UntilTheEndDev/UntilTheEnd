package HamsterYDS.UntilTheEnd.cap.san;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Influencer extends BukkitRunnable implements Listener {
    public static UntilTheEnd plugin;

    public Influencer(UntilTheEnd plugin) {
        this.plugin = plugin;
        runTaskTimer(plugin, 0L, 60L);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int san = PlayerManager.check(player.getName(), "san");
        if (san <= 120) {
            if (Math.random() >= 0.04) return;
            Location loc = event.getTo();
            float fac = (120 - san) / 3;
            if (fac < 0) return;
            loc.setYaw((float) (loc.getYaw() + Math.random() * fac - Math.random() * fac));
            loc.setPitch((float) (loc.getPitch() + Math.random() * fac - Math.random() * fac));
            event.setTo(loc);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        String newString = "";
        Player sender = event.getPlayer();
        int ssan = PlayerManager.check(sender.getName(), "san");
        if (ssan <= 30) event.setCancelled(true);
        if (ssan <= 60) {
            for (int i = 0; i < message.length(); i++)
                newString += (char) (10000 * Math.random() + 40);
            event.setMessage(newString);
        }
    }

    public static ArrayList<UUID> mobs = new ArrayList<UUID>();

    @Override
    public void run() {
        for (UUID mob : mobs) {
            Entity entity = Bukkit.getEntity(mob);
            if (entity == null) return;
            DisguiseAPI.undisguiseToAll(entity);
        }
        for (World world : Bukkit.getWorlds()) {
            if (!Config.enableWorlds.contains(world)) continue;
            for (Player player : world.getPlayers()) {
                int san = PlayerManager.check(player.getName(), "san");
                if (san > 120) continue;
                player.removePotionEffect(PotionEffectType.CONFUSION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 0));

                if (san <= 30) {
                    for (Entity entity : player.getNearbyEntities(10.0, 10.0, 10.0)) {
                        try {
                            MobDisguise mob = new MobDisguise(DisguiseType.values()[(int) (DisguiseType.values().length * Math.random() - 1)]);
                            DisguiseAPI.disguiseToPlayers(entity, mob, player.getName());
                            mob.setEntity(entity);
                            mob.startDisguise();
                        } catch (Exception ignore) {
                        }
                        mobs.add(entity.getUniqueId());
                    }
                }
            }
        }
    }
}
