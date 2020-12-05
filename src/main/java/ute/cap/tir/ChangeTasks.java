package ute.cap.tir;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import ute.Config;
import ute.UntilTheEnd;
import ute.api.PlayerApi;
import ute.event.cap.TirednessChangeEvent;
import ute.internal.ItemFactory;
import ute.internal.NPCChecker;
import ute.internal.ResidenceChecker;
import ute.item.survival.FurRoll;
import ute.item.survival.StrawRoll;
import ute.player.PlayerManager;
import ute.player.PlayerManager.CheckType;

import java.util.HashMap;

public class ChangeTasks {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static double defaultWeight = 0.01;
    public static HashMap<Material, Double> weights = new HashMap<>();

    public static void initialize() {
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

    public static class Behaviors extends BukkitRunnable {

        @Override
        public void run() {
            for (World world : Config.enableWorlds)
                for (Player player : world.getPlayers()) {
                    if (NPCChecker.isNPC(player) || ResidenceChecker.isProtected(player.getLocation())) continue;
                    if (player.isSprinting())
                        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.SPRINT,Tiredness.yaml.getDouble("change.task.sprint"));
                    if (player.isInsideVehicle())
                        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.DRIVE,Tiredness.yaml.getDouble("change.task.sit"));
                    if (player.isSleeping() || FurRoll.sleeping.contains(player.getUniqueId()) || StrawRoll.sleeping.contains(player.getUniqueId()))
                        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.SLEEP,Tiredness.yaml.getDouble("change.task.sleep"));
                    if (player.isBlocking())
                        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.BLOCK,Tiredness.yaml.getDouble("change.task.block"));
                    if (player.isGliding())
                        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.GLIDE,Tiredness.yaml.getDouble("change.task.glide"));
                    if (ChangeEvents.movingPlayers.get(player.getUniqueId()) == null)
                        PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.MOVE,Tiredness.yaml.getDouble("change.task.stop"));
                    else PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.NOTMOVE,Tiredness.yaml.getDouble("change.task.move"));
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
                    PlayerApi.TirednessOperations.changeTiredness(player, TirednessChangeEvent.ChangeCause.ITEMWEIGHT,tot);
                }

        }

    }
}
