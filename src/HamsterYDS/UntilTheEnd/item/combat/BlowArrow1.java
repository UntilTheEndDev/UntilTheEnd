package HamsterYDS.UntilTheEnd.item.combat;

import HamsterYDS.UntilTheEnd.item.ItemManager;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlowArrow1 implements Listener{
	public static double damage=ItemManager.yaml2.getDouble("吹箭.damage");
	public static double range=ItemManager.yaml2.getDouble("吹箭.range");
	public static int maxDist=ItemManager.yaml2.getInt("吹箭.maxDist");
	public BlowArrow1() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6芦苇"),3);
		materials.put(ItemManager.namesAndItems.get("§6狗牙"),2);
		materials.put(ItemManager.namesAndItems.get("§6骨片"),2);
		materials.put(ItemManager.namesAndItems.get("§6鳞片"),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6吹箭"),"§6战斗");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
		ItemManager.cosumeItems.add("BlowArrow1");
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		Player player=event.getPlayer();
		if(!player.isSneaking()) return;
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		ItemStack item = player.getInventory().getItemInMainHand();
		if (ItemManager.isSimilar(item, ItemManager.namesAndItems.get("§6吹箭"))) {
			event.setCancelled(true);
			Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
			ArmorStand armor=(ArmorStand) entity;
			Vector vec=player.getEyeLocation().getDirection();
			armor.setItemInHand(new ItemStack(Material.IRON_SWORD));
			armor.setGravity(false);
			armor.setVisible(false);
			armor.setRemoveWhenFarAway(true);
			new BukkitRunnable() {
				int dist = 0;

				@Override
				public void run() {
					armor.setVelocity(vec);
					armor.getWorld().spawnParticle(Particle.TOTEM,armor.getLocation().add(0,1.3,0),2);
					for (Entity entity : armor.getNearbyEntities(range, range, range)) {
						if (entity.getUniqueId() == player.getUniqueId())
							continue;
						if (!(entity instanceof LivingEntity))
							continue;
						((LivingEntity) entity).damage(damage);
						cancel();
					}
					if(!armor.getLocation().add(0,1.3,0).getBlock().getType().isTransparent())
						cancel();
					if (dist++ >= maxDist)
						cancel();
				}
			}.runTaskTimer(ItemManager.plugin, 0L, 1L);
		}
	}
}
