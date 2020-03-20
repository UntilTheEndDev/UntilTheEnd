package HamsterYDS.UntilTheEnd.item;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import HamsterYDS.UntilTheEnd.internal.ItemFactory;
import HamsterYDS.UntilTheEnd.item.clothes.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.item.basics.Ashes;
import HamsterYDS.UntilTheEnd.item.basics.Sclerite;
import HamsterYDS.UntilTheEnd.item.basics.SpiderGland;
import HamsterYDS.UntilTheEnd.item.combat.BeeMine;
import HamsterYDS.UntilTheEnd.item.combat.BlowArrow1;
import HamsterYDS.UntilTheEnd.item.combat.BlowArrow2;
import HamsterYDS.UntilTheEnd.item.combat.BlowArrow3;
import HamsterYDS.UntilTheEnd.item.combat.ToothTrap;
import HamsterYDS.UntilTheEnd.item.combat.WeatherPain;
import HamsterYDS.UntilTheEnd.item.magic.FireWand;
import HamsterYDS.UntilTheEnd.item.materials.Fern;
import HamsterYDS.UntilTheEnd.item.materials.Hail;
import HamsterYDS.UntilTheEnd.item.materials.NightMare;
import HamsterYDS.UntilTheEnd.item.other.ClothesContainer;
import HamsterYDS.UntilTheEnd.item.science.Hygrometer;
import HamsterYDS.UntilTheEnd.item.science.IceFlingomatic;
import HamsterYDS.UntilTheEnd.item.science.LightningArrester;
import HamsterYDS.UntilTheEnd.item.science.Refridgerator;
import HamsterYDS.UntilTheEnd.item.science.Thermometer;
import HamsterYDS.UntilTheEnd.item.survival.ACDDrug;
import HamsterYDS.UntilTheEnd.item.survival.FlowerUmbrella;
import HamsterYDS.UntilTheEnd.item.survival.FurRoll;
import HamsterYDS.UntilTheEnd.item.survival.HealingSalve;
import HamsterYDS.UntilTheEnd.item.survival.HoneyPoultice;
import HamsterYDS.UntilTheEnd.item.survival.LuxuryFan;
import HamsterYDS.UntilTheEnd.item.survival.MovablePack;
import HamsterYDS.UntilTheEnd.item.survival.NormalPack;
import HamsterYDS.UntilTheEnd.item.survival.PigPack;
import HamsterYDS.UntilTheEnd.item.survival.SiestaLeanto;
import HamsterYDS.UntilTheEnd.item.survival.StrawRoll;
import HamsterYDS.UntilTheEnd.item.survival.Umbrella;
import HamsterYDS.UntilTheEnd.item.survival.WarmStone;
import HamsterYDS.UntilTheEnd.item.survival.WaterBalloon;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class ItemManager {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static HashMap<String, String> ids = new HashMap<String, String>();
    public static HashMap<String, UTEItemStack> items = new HashMap<String, UTEItemStack>();
    public static HashMap<Integer, String> machines = new HashMap<Integer, String>();
    public static YamlConfiguration itemSets = Config.autoUpdateConfigs("itemsets.yml");
    public static YamlConfiguration itemAttributes = Config.autoUpdateConfigs("itemattributes.yml");

    static {
        plugin.getLogger().log(Level.FINER, "[ItemManager] Pre Initializing ItemManager.");
        plugin.getLogger().log(Level.FINER, "[ItemManager] Loading Trace Stack: ", new Throwable("Loading Trace Stack"));
    }

    public ItemManager(UntilTheEnd plugin) {

        for (String path : itemAttributes.getKeys(false)) {
            ItemStack item = loadItem(path);
            plugin.getLogger().log(Level.FINER, () -> "Loading PItem[" + path + "] [" + item + "]");
            if (item == null)
                continue;

            ids.put(item.getItemMeta().getDisplayName(), path);

            UTEItemStack uteItem = new UTEItemStack(
                    itemAttributes.getBoolean(path + ".canPlace"),
                    itemAttributes.getBoolean(path + ".isConsume"),
                    path,
                    item.getItemMeta().getDisplayName(),
                    itemAttributes.getInt(path + ".needLevel"),
                    item,
                    item.getItemMeta().getLore());
            items.put(path, uteItem);


            if (itemAttributes.contains(path + ".provideLevel")) {
                int level = itemAttributes.getInt(path + ".provideLevel");
                machines.put(level, path);
            }
        }
        for (String path : itemAttributes.getKeys(false)) {
            if (!itemSets.getBoolean(path + ".enable"))
                continue;
            loadRecipe(path);
        }
        new NightMare();
        new Fern();
        new Hail();

        new Sclerite();
        new SpiderGland();
        new Ashes();

        new StrawHat();
        new Garland();
        new Earmuff();
        new BushesHat();
        new EyeballUmbrella();
        new ConstantTemperatureClothes();
        new SwimmingSuit();

        new MovablePack();
        new NormalPack();
        new PigPack();
        new WarmStone();
        new Umbrella();
        new FlowerUmbrella();
        new HealingSalve();
        new HoneyPoultice();
        new ACDDrug();
        new LuxuryFan();
        new StrawRoll();
        new FurRoll();
        new WaterBalloon();
        new SiestaLeanto();

        new BlowArrow1();
        new BlowArrow2();
        new BlowArrow3();
        new BeeMine();
        new ToothTrap();
        new WeatherPain();

        new Thermometer();
        new Hygrometer();
        new LightningArrester();
        new IceFlingomatic();
        new Refridgerator();

        new FireWand();
        
        new ClothesContainer();

        new BlockManager(plugin);
        ItemProvider.loadDrops();
        plugin.getServer().getPluginManager().registerEvents(new ItemListener(), plugin);
    }

    public static ItemStack loadItem(String path) {
        plugin.getLogger().log(Level.FINER, () -> "[ItemManager] Loading item stack [" + path + "]");
        try {
            Material material = ItemFactory.valueOf(itemSets.getString(path + ".type"));
            String name = itemSets.getString(path + ".name");
            List<String> lores = itemSets.getStringList(path + ".lores");

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lores);
            item.setItemMeta(meta);
            return item;
        } catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load [" + path + "] from itemset.yml", exception);
        }
        return null;
    }

    public static void loadRecipe(String path) {
        if (!items.containsKey(path)) return;
        HashMap<ItemStack, Integer> craft = new HashMap<ItemStack, Integer>();
        if (!itemSets.contains(path + ".materials")) return;
        List<String> materials = itemSets.getStringList(path + ".materials");
        List<Integer> amounts = itemSets.getIntegerList(path + ".amounts");
        for (String str : materials) {
            int amount = amounts.get(materials.indexOf(str));
            if (Material.getMaterial(str) != null) {
                Material material = Material.valueOf(str);
                craft.put(new ItemStack(material), amount);
            } else {
                try {
                    craft.put(items.get(str).item, amount);
                } catch (Exception exception2) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to load recipe [" + path + "] from itemsets.yml", exception2);
                }
            }
        }
        items.get(path).registerRecipe(craft, itemSets.getString(path + ".category"));
    }

    public static String isUTEItem(ItemStack item) {
        if (item == null) return "";
        if (item.hasItemMeta())
            if (item.getItemMeta().hasDisplayName())
                if (ids.containsKey(item.getItemMeta().getDisplayName()))
                    return ids.get(item.getItemMeta().getDisplayName());
        return "";
    }

    public static boolean isSimilar(ItemStack item, Class<?> clazz) {
        if (item == null)
            return false;
        String id = clazz.getSimpleName();
        if (!items.containsKey(id)) return false;
        ItemStack uteItem = items.get(id).item;
        if (item == uteItem)
            return true;
        if (item == null || uteItem == null)
            return false;
        Material m1 = ItemFactory.fromLegacy(ItemFactory.getType(item));
        Material m2 = ItemFactory.fromLegacy(ItemFactory.getType(uteItem));
        if (m1 == m2) {
            ItemMeta meta = item.getItemMeta();
            ItemMeta meta2 = uteItem.getItemMeta();
            if (Objects.equals(meta.getDisplayName(), meta2.getDisplayName())) {
                return Objects.equals(meta.getLore(), meta2.getLore());

            }
        }
        return false;
    }
    
    public static boolean isSimilar(ItemStack item, ItemStack uteItem) {
        if (item == uteItem)
            return true;
        if (item == null || uteItem == null)
            return false;
        Material m1 = ItemFactory.fromLegacy(ItemFactory.getType(item));
        Material m2 = ItemFactory.fromLegacy(ItemFactory.getType(uteItem));
        if (m1 == m2) {
            ItemMeta meta = item.getItemMeta();
            ItemMeta meta2 = uteItem.getItemMeta();
            if (Objects.equals(meta.getDisplayName(), meta2.getDisplayName())) {
                return Objects.equals(meta.getLore(), meta2.getLore());

            }
        }
        return false;
    }
}
