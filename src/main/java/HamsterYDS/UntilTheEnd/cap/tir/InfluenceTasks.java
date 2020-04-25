package HamsterYDS.UntilTheEnd.cap.tir;

import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import HamsterYDS.UntilTheEnd.internal.ResidenceChecker;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;

public class InfluenceTasks extends BukkitRunnable {
    public static int slowness = Tiredness.yaml.getInt("influence.slowness");
    public static int slowdigging = Tiredness.yaml.getInt("influence.slowdigging");
    public static int hunger = Tiredness.yaml.getInt("influence.hunger");
    public static int wither = Tiredness.yaml.getInt("influence.wither");
    public static int weak = Tiredness.yaml.getInt("influence.weak");
    public static int sprint = Tiredness.yaml.getInt("influence.event.sprint");

    @Override
    public void run() {
        for (World world : Config.enableWorlds)
            for (Player player : world.getPlayers()) {
                if (NPCChecker.isNPC(player)||ResidenceChecker.isProtected(player.getLocation())) continue;
                int tir = (int) PlayerManager.check(player, CheckType.TIREDNESS);
                if (tir >= slowness)
                    addEffect(player, PotionEffectType.SLOW, (tir - slowness) / 15);
                if (tir >= slowdigging)
                    addEffect(player, PotionEffectType.SLOW_DIGGING, (tir - slowdigging) / 15);
                if (tir >= hunger)
                    addEffect(player, PotionEffectType.HUNGER, (tir - hunger) / 5);
                if (tir >= wither)
                    addEffect(player, PotionEffectType.WITHER, (tir - wither) / 5);
                if (tir >= weak)
                    addEffect(player, PotionEffectType.WEAKNESS, (tir - weak) / 15);
                if (player.isSprinting() && tir >= sprint) {
                    player.sendMessage(UTEi18n.cache("cap.tir.influence.sprint"));
                    player.setSprinting(false);
                }
            }
    }

    public void addEffect(Player player, PotionEffectType type, int level) {
        player.removePotionEffect(type);
        player.addPotionEffect(new PotionEffect(type, 200, level));
    }

}
