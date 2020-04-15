package HamsterYDS.UntilTheEnd.item;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import HamsterYDS.UntilTheEnd.Logging;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.event.block.CustomBlockBreakEvent;
import HamsterYDS.UntilTheEnd.event.block.CustomBlockInteractEvent;
import HamsterYDS.UntilTheEnd.event.block.CustomBlockPlaceEvent;
import HamsterYDS.UntilTheEnd.item.combat.ToothTrap;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlockManager extends BukkitRunnable implements Listener {
    public static UntilTheEnd plugin;
    public static HashMap<String, String> blocks = new HashMap<String, String>();
    public static HashMap<String, ArrayList<String>> blockDatas = new HashMap<String, ArrayList<String>>();

    public BlockManager(UntilTheEnd plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        runTaskTimer(plugin, 0L, plugin.getConfig().getLong("block.fresh") * 20);
        loadBlocks();
        Logging.getLogger().info(String.valueOf(blockDatas));
    }

    public static void addBlockData(String blockName, String toString) {
        blocks.put(toString, blockName);
        ArrayList<String> array;
        if (blockDatas.containsKey(blockName))
            array = blockDatas.get(blockName);
        else array = new ArrayList<String>();
        array.add(toString);
        blockDatas.put(blockName, array);
    }

    public static void removeBlockData(String blockName, String toString) {
        blocks.remove(toString);
        ArrayList<String> array;
        if (blockDatas.containsKey(blockName))
            array = blockDatas.get(blockName);
        else array = new ArrayList<String>();
        array.remove(toString);
        blockDatas.put(blockName, array);
    }

    public static void saveBlocks() {
        File file = new File(plugin.getDataFolder() + "/data/", "blocks.yml");
        file.delete();
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (String loc : blocks.keySet()) {
            String item = blocks.get(loc);
            yaml.set(loc, item);
        }
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ToothTrap.saveBlocks();
    }

    public static void loadBlocks() {
        File file = new File(plugin.getDataFolder(), "data/blocks.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (String path : yaml.getKeys(false)) {
            blocks.put(path, yaml.getString(path));
            addBlockData(yaml.getString(path), path);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Location loc = event.getBlock().getLocation();
        String toString = BlockApi.locToStr(loc);
        if (blocks.get(toString) == null) return;

        CustomBlockBreakEvent evt = new CustomBlockBreakEvent(event.getBlock(), event.getPlayer(), ItemManager.items.get(blocks.get(toString)));
        Bukkit.getPluginManager().callEvent(evt);
        if (evt.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        event.setDropItems(false);
        Logging.getLogger().fine(() -> "[BlockManager] Breaking with " + toString + " is " + blocks.get(toString));
        loc.getWorld().spawnParticle(Particle.CRIT, loc.add(0.5, 0.5, 0.5), 3);
        HashMap<ItemStack, Integer> craft = ItemManager.items.get(blocks.get(toString)).craft;
        if (craft != null)
            for (ItemStack item : craft.keySet()) {
                ItemStack itemClone = item.clone();
                int amount = (int) (craft.get(item) * (1.0 - Math.random()));
                if (amount < 1) continue;
                itemClone.setAmount(amount);
                Logging.getLogger().fine(() -> "[BlockManager] Try drop " + itemClone + " at " + loc);
                loc.getWorld().dropItemNaturally(loc, itemClone);
            }
        removeBlockData(blocks.get(toString), toString);
    }

    private void onPlace$reset(String loc) {
        blocks.remove(loc);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        Location loc = event.getBlock().getLocation();
        String toString = BlockApi.locToStr(loc);
        if (event.getItemInHand() == null) {
            onPlace$reset(toString);
            return;
        }
        ItemStack item = event.getItemInHand();
        if (ItemManager.getUTEItemId(item, null) == null) {
            onPlace$reset(toString);
            return;
        }
        CustomBlockPlaceEvent evt = new CustomBlockPlaceEvent(event.getBlock(), event.getBlockReplacedState(),
                event.getBlockAgainst(), event.getItemInHand(), event.getPlayer(), event.canBuild(), event.getHand(), ItemManager.items.get(ItemManager.getUTEItemId(item)));
        Bukkit.getPluginManager().callEvent(evt);
        if (evt.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        addBlockData(ItemManager.getUTEItemId(item), toString);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled()) return;
        for (org.bukkit.block.Block block : event.getBlocks()) {
            Location loc = block.getLocation();
            String toString = BlockApi.locToStr(loc);
            if (blocks.containsKey(toString)) event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        Location loc = block.getLocation();
        String toString = BlockApi.locToStr(loc);
        if (blocks.containsKey(toString)) {
            String id = blocks.get(toString);
            CustomBlockInteractEvent evt = new CustomBlockInteractEvent(event.getPlayer(), event.getAction(),
                    event.getItem(), block, event.getBlockFace(), event.getHand(), ItemManager.items.get(id));
            Bukkit.getPluginManager().callEvent(evt);
            if (evt.isCancelled())
                event.setCancelled(true);
        }
    }

    @Override
    public void run() {
        saveBlocks();
    }
}
