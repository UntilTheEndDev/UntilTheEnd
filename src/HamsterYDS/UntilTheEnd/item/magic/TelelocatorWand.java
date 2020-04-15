package HamsterYDS.UntilTheEnd.item.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.event.hud.SanityChangeEvent;
import HamsterYDS.UntilTheEnd.event.hud.SanityChangeEvent.ChangeCause;
import HamsterYDS.UntilTheEnd.internal.DisableManager;
import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.internal.ItemFactory;
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

import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.item.magic.Teleportage.TeleportMaster;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class TelelocatorWand implements Listener {
    public static int maxDist = ItemManager.itemAttributes.getInt("TelelocatorWand.maxDist");

    public TelelocatorWand() {
        ItemManager.plugin.getServer().getPluginManager().registerEvents(this, ItemManager.plugin);
    }

    private static HashMap<String, Integer> cd = new HashMap<String, Integer>();
    private static List<String> openers = new ArrayList<String>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled() && !DisableManager.bypass_right_action_cancelled) return;
        if (!event.hasItem()) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, getClass())) {
            event.setCancelled(true);
            if (!EventHelper.isRight(event.getAction())) {
                player.openInventory(getInventory(player));
                openers.add(player.getName());
                return;
            }
            if (cd.containsKey(player.getName()))
                if (cd.get(player.getName()) > 0) {
                    player.sendMessage("[§cUntilTheEnd]§r 您的魔咒未冷却！");
                    return;
                }
            cd.remove(player.getName());
            cd.put(player.getName(), 5);
            ItemStack itemr = player.getItemInHand();
            itemr.setDurability((short) (itemr.getDurability() + 3));
            if (itemr.getDurability() > ItemFactory.getType(itemr).getMaxDurability())
                player.setItemInHand(null);
            Location loc = player.getLocation().add(0.0, 1.0, 0.0);
            Vector vec = player.getEyeLocation().getDirection().multiply(0.5);
            SanityChangeEvent event2 = new SanityChangeEvent(player, ChangeCause.USEWAND, -5);
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
                            cd.remove(player.getName());
                            return;
                        }
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc, 1);
                        loc.add(vec);
                        if (loc.getBlock().getType() != Material.AIR) {
                            player.teleport(oldLoc);
                            cancel();
                            cd.remove(player.getName());
                            return;
                        }
                    }
                    oldLoc = loc.clone();
                }
            }.runTaskTimer(ItemManager.plugin, 0L, 1L);
        }
    }

    private Inventory getInventory(Player player) {
        Inventory inv = Bukkit.createInventory(player, 54, "§8§l可选择的传送矩阵");
        for (TeleportMaster master : Teleportage.teleportages.keySet()) {
            ItemStack item = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta = item.getItemMeta();
            if (master.master == player.getUniqueId().toString()) {
                meta.setDisplayName("§6§l玩家" + player.getName() + "的传送矩阵");
                List<String> lores = meta.getLore() == null ? new ArrayList<String>() : meta.getLore();
                lores.add(master.loc);
                meta.setLore(lores);
            }
            if (Teleportage.teleportages.get(master).permissioners.contains(player.getUniqueId().toString())) {
                meta.setDisplayName("§6§l玩家" + Bukkit.getOfflinePlayer(master.master).getName() + "的传送矩阵");
            }
            item.setItemMeta(meta);
            inv.addItem(item);
        }
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!openers.contains(player.getName())) return;
        if (event.getClickedInventory() != null) {
            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            if (!item.hasItemMeta()) return;
            if (!item.getItemMeta().hasLore()) ;
            List<String> lores = item.getItemMeta().getLore();
            player.teleport(BlockApi.strToLoc(lores.get(0)).add(0, 1.0, 0), TeleportCause.PLUGIN);
        }
    }
}
