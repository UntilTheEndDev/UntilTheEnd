package HamsterYDS.UntilTheEnd.item.combat;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlowArrow2 implements Listener{
	public BlowArrow2() {		
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6芦苇"),3);
		materials.put(ItemManager.namesAndItems.get("§6狗牙"),2);
		materials.put(ItemManager.namesAndItems.get("§6骨片"),2);
		materials.put(ItemManager.namesAndItems.get("§6暖石"),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6火吹箭"),"§6战斗");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(!player.isSneaking()) return;
		if(!(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK)) return;
		ItemStack itemClone=player.getItemInHand().clone();
		if(itemClone==null) return;
		itemClone.setAmount(1);
		if(itemClone.equals(ItemManager.namesAndItems.get("§6火吹箭"))) {
			ItemStack item=player.getItemInHand();
			item.setAmount(item.getAmount()-1);
			Entity entity=player.getWorld().spawnEntity(player.getLocation().add(0,1.0,0),EntityType.ARMOR_STAND);
			ArmorStand armor=(ArmorStand) entity;
			armor.setItemInHand(new ItemStack(Material.GOLD_SWORD));
			Vector vec=player.getEyeLocation().getDirection().multiply(2.0);
			armor.setInvulnerable(true);
			armor.setSmall(true);
			armor.setRightArmPose(new EulerAngle(0,0,0));
			armor.setVisible(false);
			armor.setAI(false);
			new Task(vec,armor,player);
		}
	}
	public class Task extends BukkitRunnable{
		int range=300;
		Vector vec;
		ArmorStand armor;
		Player player;
		@Override
		public void run() {
			range--;
			if(range==0) {
				cancel();
				return;
			}
			Location loc=armor.getLocation().add(0.0,1.0,0.0);
			armor.getWorld().spawnParticle(Particle.LAVA,armor.getLocation().add(0,1.0,0),3);
			armor.setVelocity(vec);
			for(Entity entity:armor.getNearbyEntities(0.5,0.5,0.5)) {
				if(entity==player) continue;
				if(entity instanceof LivingEntity) {
					LivingEntity creature=(LivingEntity) entity;
					creature.damage(10.0,player);
					creature.setFireTicks(400); 
					armor.remove();
					cancel();
					return;
				}
			}
			loc.add(vec);
			if(!loc.getBlock().getType().isTransparent()) {
				armor.teleport(loc.add(0.0, -1.0, 0.0));
				armor.setGravity(false);
				new BukkitRunnable() {
					@Override
					public void run() {
						armor.remove();
						cancel();
					}
				}.runTaskTimer(ItemManager.plugin,ItemManager.plugin.getConfig().getInt("item.blowarrow.autoclear"),1);
				cancel();
				return;
			}
		}
		public Task(Vector vec,ArmorStand armor,Player player) {
			this.vec=vec;
			this.armor=armor;
			this.player=player;
			runTaskTimer(ItemManager.plugin,0L,1L);
		}
	}
}
