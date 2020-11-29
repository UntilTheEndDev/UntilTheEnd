package ute.food;

import ute.Config;
import ute.internal.ItemFactory;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RottenFoodEvents implements Listener, Runnable {
    private static class WorldB3Loc {
        int cx;
        int cz;
        int x;
        int y;
        int z;
        transient int ticking;
        UUID world;

        public WorldB3Loc(int cx, int cz, int x, int y, int z, UUID world) {
            this.cx = cx;
            this.cz = cz;
            this.x = x;
            this.y = y;
            this.z = z;
            this.world = world;
        }

        public static WorldB3Loc from(Block block) {
            return from(block.getChunk(), block.getX(), block.getY(), block.getZ());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WorldB3Loc that = (WorldB3Loc) o;

            if (cx != that.cx) return false;
            if (cz != that.cz) return false;
            if (x != that.x) return false;
            if (y != that.y) return false;
            if (z != that.z) return false;
            return Objects.equals(world, that.world);
        }

        @Override
        public int hashCode() {
            int result = cx;
            result = 31 * result + cz;
            result = 31 * result + x;
            result = 31 * result + y;
            result = 31 * result + z;
            result = 31 * result + (world != null ? world.hashCode() : 0);
            return result;
        }

        static WorldB3Loc from(Chunk c, int x, int y, int z) {
            return new WorldB3Loc(c.getX(), c.getZ(), x & 0xF, y, z & 0xF, c.getWorld().getUID());
        }

        Block block() {
            return Bukkit.getWorld(world).getChunkAt(cx, cz).getBlock(x, y, z);
        }

        InventoryHolder check() {
            final Block block = block();
            final BlockState state = block.getState();
            if (!(state instanceof InventoryHolder)) {
                return null;
            }
            return (InventoryHolder) state;
        }
    }

    public static HashMap<String, Integer> titleFactors = new HashMap<>();
    private static final Integer ONE = 1;
    public static Map<WorldB3Loc, Integer> checking = new ConcurrentHashMap<>();

    RottenFoodEvents() {
        for (World w : Config.enableWorlds) {
            for (Chunk chunk : w.getLoadedChunks()) {
                for (BlockState bs : chunk.getTileEntities()) {
                    initialize(bs);
                }
            }
        }
    }

    @Override
    public void run() {
        final Iterator<Map.Entry<WorldB3Loc, Integer>> iterator = checking.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<WorldB3Loc, Integer> next = iterator.next();
            final WorldB3Loc key = next.getKey();
            final Integer value = next.getValue();
            final InventoryHolder check = key.check();
            if (check == null) iterator.remove();
            else {
                if (check instanceof Furnace) {
                    if (((Furnace) check).getBurnTime() > 0) {
                        continue;
                    }
                }
                if (key.ticking++ >= value) {
                    key.ticking = 0;
                    final Inventory inventory = check.getInventory();
                    for (ItemStack item : inventory) {
                        if (item == null) continue;
                        if (item.getType() == Material.ROTTEN_FLESH) {
                            continue;
                        }
                        if (hasTag(item)) return;
                        if (ItemFactory.getType(item).isEdible()) {
                            RottenFoodTask.setRottenLevel(item, RottenFoodTask.getRottenLevel(item) - 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler()
    public void onChunkLoading(ChunkLoadEvent event) {
        load(event.getChunk());
    }

    @EventHandler()
    public void onWorldUnloading(WorldUnloadEvent event) {
        final World world = event.getWorld();
        final UUID uid = world.getUID();
        final Iterator<Map.Entry<WorldB3Loc, Integer>> iterator = checking.entrySet().iterator();
        while (iterator.hasNext()) {
            final WorldB3Loc key = iterator.next().getKey();
            if (key.world.equals(uid)) iterator.remove();
        }
    }

    @EventHandler()
    public void onChunkUnloading(ChunkUnloadEvent event) {
        final Chunk chunk = event.getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        final Iterator<Map.Entry<WorldB3Loc, Integer>> iterator = checking.entrySet().iterator();
        while (iterator.hasNext()) {
            final WorldB3Loc key = iterator.next().getKey();
            if (key.cx == x && key.cz == z) iterator.remove();
        }
    }

    private void initialize(BlockState tileEntity) {
        if (Config.enableWorlds.contains(tileEntity.getWorld()))
            if (tileEntity instanceof InventoryHolder) {
                Integer d = ONE;
                if (tileEntity instanceof Nameable) {
                    d = titleFactors.getOrDefault(((Nameable) tileEntity).getCustomName(), d);
                }
                checking.put(WorldB3Loc.from(tileEntity.getChunk(), tileEntity.getX(), tileEntity.getY(), tileEntity.getZ()), d);
            }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onBlockBreak(BlockBreakEvent event) {
        checking.remove(WorldB3Loc.from(event.getBlock()));
    }

    private void load(Chunk chunk) {
        if (Config.enableWorlds.contains(chunk.getWorld()))
            for (BlockState bs : chunk.getTileEntities()) {
                initialize(bs);
            }
    }

    @EventHandler()
    public void onOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        final InventoryHolder holder = inv.getHolder();
        if (holder instanceof BlockState) {
            initialize((BlockState) holder);
        }
    }

    private static boolean hasTag(ItemStack stack) {
        final ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            final List<String> lore = meta.getLore();
            if (lore != null) {
                for (String str : lore)
                    if (str.contains("不可腐烂"))
                        return true;
            }
        }
        return false;
    }

}
