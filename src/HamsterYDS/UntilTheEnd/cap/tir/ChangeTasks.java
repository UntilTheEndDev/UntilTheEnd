package HamsterYDS.UntilTheEnd.cap.tir;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.ItemFactory;
import HamsterYDS.UntilTheEnd.internal.NPCChecker;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.item.survival.FurRoll;
import HamsterYDS.UntilTheEnd.item.survival.StrawRoll;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;

public class ChangeTasks {
    public UntilTheEnd plugin;
    public static double defaultWeight = 0.01;
    public static HashMap<Material, Double> weights = new HashMap<>();

    public ChangeTasks(UntilTheEnd plugin) {
        this.plugin = plugin;
        for (String path : Tiredness.yaml.getKeys(true)) {
            if (path.startsWith("change.task.item.")) {
                if (path.replace("change.task.item.", "").equalsIgnoreCase("default")) {
                    defaultWeight = Tiredness.yaml.getDouble(path);
                } else {
                    Material material = ItemFactory.valueOf(path.replace("change.task.item.", ""));
                    double weight = Tiredness.yaml.getDouble(path);
                    weights.put(material, weight);
                }

            }
        }
        new Behaviors().runTaskTimer(plugin, 0L, 40L);
    }

    public class Behaviors extends BukkitRunnable {

        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player)) continue;
                    if (player.isSprinting())
                        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getDouble("change.task.sprint"));
                    if (player.isInsideVehicle())
                        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getDouble("change.task.sit"));
                    if (player.isSleeping() || FurRoll.sleeping.contains(player.getUniqueId()) || StrawRoll.sleeping.contains(player.getUniqueId()))
                        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getDouble("change.task.sleep"));
                    if (player.isBlocking())
                        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getDouble("change.task.block"));
                    if (player.isGliding())
                        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getDouble("change.task.glide"));
                    if (ChangeEvents.movingPlayers.get(player.getUniqueId()) == null)
                        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getDouble("change.task.stop"));
                    else
                        PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getDouble("change.task.move"));
                    PlayerInventory inv = player.getInventory();
                    int tot = 0;
                    for (int slot = 0; slot < inv.getSize(); slot++) {
                        ItemStack item = inv.getItem(slot);
                        if (item == null) continue;
                        final Double weight = weights.get(ItemFactory.getType(item));
                        double weightV;
                        if (weight == null) {
                            weightV = defaultWeight;
                        } else {
                            weightV = weight;
                        }
                        tot += weightV * item.getAmount();
                    }
                    PlayerManager.change(player, CheckType.TIREDNESS, tot);
                }

        }

    }
}
