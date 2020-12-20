package ute.item;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ute.Config;
import ute.Logging;
import ute.UntilTheEnd;
import ute.internal.ItemFactory;
import ute.item.basics.Ashes;
import ute.item.basics.Sclerite;
import ute.item.basics.SpiderGland;
import ute.item.clothes.*;
import ute.item.combat.*;
import ute.item.magic.*;
import ute.item.materials.Fern;
import ute.item.materials.Hail;
import ute.item.materials.NightMare;
import ute.item.science.*;
import ute.item.survival.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class ItemManager {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static HashMap<String, String> ids = new HashMap<String, String>();
    public static HashMap<String, UTEItemStack> items = new HashMap<String, UTEItemStack>();
    public static HashMap<Integer, String> machines = new HashMap<Integer, String>();
    public static YamlConfiguration itemSets = Config.autoUpdateConfigs("itemsets.yml");
    public static YamlConfiguration itemAttributes = Config.autoUpdateConfigs("itemattributes.yml");

    static {
        Logging.getLogger().log(Level.FINER, "[ItemManager] Pre Initializing ItemManager.");
        Logging.getLogger().log(Level.FINER, "[ItemManager] Loading Trace Stack: ", new Throwable("Loading Trace Stack"));
    }

    public static void initialize(UntilTheEnd plugin) {

        for (String path : itemAttributes.getKeys(false)) {
            ItemStack item = loadItem(path);
            Logging.getLogger().log(Level.FINER, () -> "Loading PItem[" + path + "] [" + item + "]");
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
        new ChainingPickaxe();
        new ChainingAxe();
        new ChainingHoe();
        new ChainingSpade();

        new Thermometer();
        new Hygrometer();
        new LightningArrester();
        new IceFlingomatic();
        new Refridgerator();
        new CombinedTools();
        new BluePrint();
        new ColdFire();
        new AutoClicker();

        new FireWand();
        new IceWand();
        new TelelocatorWand();
        new LifeGivingAmulet();
        new ChilledAmulet();
        new Teleportage();
        new DecomposeWand();
        new FlyCarpet();
        new Tracker();

        new ClothesContainer();

        new BlockManager(plugin);
        ItemProvider.loadDrops();
        plugin.getServer().getPluginManager().registerEvents(new ItemListener(), plugin);
    }

    public static ItemStack loadItem(String path) {
        Logging.getLogger().log(Level.FINER, () -> "[ItemManager] Loading item stack [" + path + "]");
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
            Logging.getLogger().log(Level.SEVERE, "Failed to load [" + path + "] from itemset.yml", exception);
        }
        return null;
    }

    public static void loadRecipe(String path) {
        Logging.getLogger().fine("[ItemManager] Loading Recipe for " + path);
        if (!items.containsKey(path)) return;
        HashMap<ItemStack, Integer> craft = new HashMap<ItemStack, Integer>();
        if (!itemSets.contains(path + ".materials")) return;
        List<String> materials = itemSets.getStringList(path + ".materials");
        List<Integer> amounts = itemSets.getIntegerList(path + ".amounts");
        for (String str : materials) {
            int amount = amounts.get(materials.indexOf(str));
            Material m0 = ItemFactory.getMaterial(str);
            if (m0 == null) {
                try {
                    m0 = ItemFactory.fromLegacy(Material.valueOf(str));
                } catch (Throwable ignore) {
                    try {
                        m0 = ItemFactory.valueOf(str);
                    } catch (Throwable ignored) {
                    }
                }
            }
            if (m0 != null) {
                m0 = ItemFactory.fromLegacy(m0);
                Material m1 = m0;
                Logging.getLogger().fine(() -> "[ItemManager] Found Vanilla Material " + str + ": " + m1);
                craft.put(new ItemStack(m0), amount);
            } else {
                try {
                    Logging.getLogger().fine(() -> "[ItemManager] Vanilla Material not found. Load " + str + " with " + items.get(str).item);
                    craft.put(items.get(str).item, amount);
                } catch (Exception exception2) {
                    Logging.getLogger().log(Level.SEVERE, "Failed to load recipe [" + path + "] from itemsets.yml", exception2);
                }
            }
        }
        items.get(path).registerRecipe(craft, itemSets.getString(path + ".category"));
    }

    public static String getUTEItemId(ItemStack item, String default_) {
        if (item == null) return default_;
        if (item.hasItemMeta())
            if (item.getItemMeta().hasDisplayName())
                if (ids.containsKey(item.getItemMeta().getDisplayName()))
                    return ids.get(item.getItemMeta().getDisplayName());
        return default_;
    }

    public static String getUTEItemId(ItemStack item) {
        return getUTEItemId(item, "");
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
