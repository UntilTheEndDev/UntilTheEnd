package HamsterYDS.UntilTheEnd.food;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class RottenFoodInfluence implements Listener {
    public static UntilTheEnd plugin;

    public RottenFoodInfluence(UntilTheEnd plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private HashMap<String, Integer> eatenFoodRottens = new HashMap<String, Integer>();
    private HashMap<String, Integer> eatenFoodLevels = new HashMap<String, Integer>();

    @EventHandler
    public void onUse(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (!item.getType().isEdible()) return;
        eatenFoodRottens.remove(event.getPlayer().getName());
        eatenFoodLevels.put(event.getPlayer().getName(), event.getPlayer().getFoodLevel());
        if (item.getType() == Material.ROTTEN_FLESH) eatenFoodRottens.put(event.getPlayer().getName(), -100);
        else eatenFoodRottens.put(event.getPlayer().getName(), RottenFoodTask.getRottenLevel(item));
    }

    @EventHandler
    public void onEat(FoodLevelChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        if (eatenFoodRottens.containsKey(entity.getName())) {
            int level = eatenFoodRottens.get(entity.getName());
            if (level == -100) {
                PlayerManager.change((Player) entity, PlayerManager.CheckType.SANITY, -30);
                entity.damage(2.0);
                entity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
                entity.sendMessage(UTEi18n.cacheWithPrefix("mechanism.food.rotten.high"));
                event.setFoodLevel(eatenFoodLevels.get(entity.getName()) - 1);
                eatenFoodRottens.remove(entity.getName());
                return;
            }
            int currentLevel = eatenFoodLevels.get(entity.getName());
            int foodLevel = event.getFoodLevel() - currentLevel;
            double percent = level / 100.0;
            int newLevel = (int) (percent * foodLevel + 1.0);
            event.setFoodLevel(currentLevel + newLevel);
            if (level <= 60) {
                PlayerManager.change((Player) entity, PlayerManager.CheckType.SANITY, (int) (-15.0D * (level / 100)));
                entity.sendMessage(UTEi18n.cacheWithPrefix("mechanism.food.rotten.low"));
                eatenFoodRottens.remove(entity.getName());
            }
        }
    }
}
