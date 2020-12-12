package ute.crops;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import ute.Config;
import ute.UntilTheEnd;

public class TakeWaterUp implements Listener {
    static double percent = Crops.yaml.getDouble("crops.takewaterup.takeWaterPercent");

    public TakeWaterUp() {
        Bukkit.getServer().getPluginManager().registerEvents(this, UntilTheEnd.getInstance());
    }

    @EventHandler
    public void onLiquidRun(BlockFromToEvent event) {
        Block block1 = event.getBlock();
        Block block2 = event.getToBlock();
        if (!Config.enableWorlds.contains(block1.getWorld())) return;
        if (!Config.enableWorlds.contains(block2.getWorld())) return;
        if (!block1.isLiquid()) return;
        if (block2.isLiquid()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTakingUp(BlockGrowEvent event) {
        Block block = event.getBlock();
        Location loc = block.getLocation();
        World world = loc.getWorld();
        if (!Config.enableWorlds.contains(world)) return;
        boolean flag = true;
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    Location locNew = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
                    Block iblock = world.getBlockAt(locNew);
                    if (iblock.isLiquid()) {
                        if (Math.random() <= percent)
                            iblock.setType(Material.AIR);
                        flag = false;
                    }
                }
            }
        }
        if (flag)
            if (Math.random() <= 0.1)
                block.breakNaturally();
    }
}
