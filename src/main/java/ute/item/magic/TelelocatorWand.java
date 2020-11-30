package ute.item.magic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ute.Config;
import ute.api.BlockApi;
import ute.event.cap.SanityChangeEvent;
import ute.internal.DisableManager;
import ute.internal.EventHelper;
import ute.internal.HolderPlaceholder;
import ute.internal.ItemFactory;
import ute.item.ItemManager;
import ute.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TelelocatorWand implements Listener, HolderPlaceholder {
    public static int maxDist = ItemManager.itemAttributes.getInt("TelelocatorWand.maxDist");

    public TelelocatorWand() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    private static final HashMap<UUID, Integer> cd = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        if (event.isCancelled() && !DisableManager.bypass_right_action_cancelled)
            return;
        if (!event.hasItem())
            return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
            event.setCancelled(true);
            if (!EventHelper.isRight(event.getAction())) {
                player.openInventory(getInventory(player));
                return;
            }
            if (cd.containsKey(player.getUniqueId()))
                if (cd.get(player.getUniqueId()) > 0) {
                    player.sendMessage("§c[UntilTheEnd]§r 您的魔咒未冷却！");
                    return;
                }
            cd.put(player.getUniqueId(), 5);
            item.setDurability((short) (item.getDurability() + 3));
            if (item.getDurability() > ItemFactory.getType(item).getMaxDurability())
                item.setType(ItemFactory.AIR);
            Location loc = player.getLocation().add(0.0, 1.0, 0.0);
            Vector vec = player.getEyeLocation().getDirection().multiply(0.5);
            SanityChangeEvent event2 = new SanityChangeEvent(player, SanityChangeEvent.ChangeCause.USEWAND, -5);
            Bukkit.getPluginManager().callEvent(event2);
            if (!event2.isCancelled())
                PlayerManager.change(player, PlayerManager.CheckType.SANITY, -5);
            new BukkitRunnable() {
                int range = maxDist;
                Location oldLoc = loc.clone();

                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        range--;
                        if (range <= 0) {
                            cancel();
                            cd.remove(player.getUniqueId());
                            return;
                        }
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc, 1);
                        loc.add(vec);
                        if (loc.getBlock().getType() != Material.AIR) {
                            player.teleport(oldLoc);
                            cancel();
                            cd.remove(player.getUniqueId());
                            return;
                        }
                    }
                    oldLoc = loc.clone();
                }
            }.runTaskTimer(ItemManager.plugin, 0L, 1L);
        }
    }

    private Inventory getInventory(Player player) {
        Inventory inv = Bukkit.createInventory(this, 54, "§8§l可选择的传送矩阵");
        for (String loc : Teleportage.teleportages.keySet()) {
            Teleportage.TeleportPoint point = Teleportage.teleportages.get(loc);
            if (point.master.equalsIgnoreCase(player.getUniqueId().toString())
                    || point.permissioners.contains(player.getName())) {
                ItemStack item = new ItemStack(Material.ENDER_PEARL);
                ItemMeta meta = item.getItemMeta();
                List<String> lores = new ArrayList<String>();
                lores.add(loc);
                meta.setLore(lores);
                meta.setDisplayName(
                        "§6§l玩家" + Bukkit.getOfflinePlayer(UUID.fromString(point.master)).getName() + "的传送矩阵");
                item.setItemMeta(meta);
                inv.addItem(item);
            }
        }
        return inv;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory view = event.getInventory();
        Inventory clicked = event.getClickedInventory();
        if (clicked == null) return;

        if (clicked.getHolder() instanceof TelelocatorWand) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null)
                return;
            if (!item.hasItemMeta())
                return;
            if (!item.getItemMeta().hasLore())
                return;
            List<String> lores = item.getItemMeta().getLore();
            player.teleport(BlockApi.strToLoc(lores.get(0)).add(0, 1.0, 0), TeleportCause.PLUGIN);
        }
    }
}
