package ute.item.magic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ute.UntilTheEnd;
import ute.api.BlockApi;
import ute.item.ItemManager;

import java.util.*;

public class FlyCarpet implements Listener {
    public static int platformExistingTime=ItemManager.itemAttributes.getInt("FlyCarpet.platformExistingTime");
    public static int platformRadius=ItemManager.itemAttributes.getInt("FlyCarpet.platformRadius");
    public static Material platformMaterial=Material.valueOf(ItemManager.itemAttributes.getString("FlyCarpet.platformMaterial"));
    public static double breakRates=ItemManager.itemAttributes.getDouble("FlyCarpet.breakRates");

    public FlyCarpet() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event){
        if(event.getAction()!= Action.RIGHT_CLICK_AIR) return;

        if(ItemManager.isSimilar(event.getItem(), getClass())){
            Player player=event.getPlayer();
            event.setCancelled(true);

            if(Math.random()<=breakRates)
                player.setItemInHand(new ItemStack(Material.AIR));

            startEffect(player,platformRadius,platformMaterial);
            player.sendMessage("§c[UntilTheEnd]§r 飞毯启用成功");
        }
    }

    private static Set<String> banMiningBlocks=new HashSet<String>();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Location loc=event.getBlock().getLocation();
        if(banMiningBlocks.contains(BlockApi.locToStr(loc)))
            event.setCancelled(true);
    }

    private void startEffect(Player player,int radius,Material material) {
        new BukkitRunnable() {
            int counter=0;
            List<String> current = new ArrayList<String>();
            @Override
            public void run() {
                if (counter/4>=platformExistingTime || player == null || (!player.isOnline())) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            removeNewBlocks(current);
                            banMiningBlocks.removeAll(current);
                        }
                    }.runTaskLater(UntilTheEnd.getInstance(), 100L);
                    cancel();
                    return;
                }

                List<String> tmp;
                tmp = addNewBlocks(player, radius, material);
                banMiningBlocks.addAll(tmp);

                removeNewBlocks(current);
                banMiningBlocks.removeAll(current);
                current = tmp;

                counter++;
            }

            private void removeNewBlocks(List<String> oldBlocks) {
                for (String toString : oldBlocks) {
                    Location loc=BlockApi.strToLoc(toString);
                    loc.getWorld().getBlockAt(loc).setType(Material.AIR);
                }
            }

            private List<String> addNewBlocks(Player player, int radius, Material material) {
                List<String> blocks = new ArrayList<String>();
                Location centre = player.getLocation().getBlock().getLocation().add(0, -1, 0);
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        Location spot = centre.clone().add(x, 0, z);
                        if (spot.distance(centre) > radius)
                            continue;
                        if (spot.getBlock().getType() == Material.AIR || current.contains(BlockApi.locToStr(spot))) {
                            spot.getBlock().setType(material);
                            blocks.add(BlockApi.locToStr(spot));
                            current.remove(BlockApi.locToStr(spot));
                        }
                    }
                }
                return blocks;
            }
        }.runTaskTimer(UntilTheEnd.getInstance(), 0L, 5L);
    }
}
