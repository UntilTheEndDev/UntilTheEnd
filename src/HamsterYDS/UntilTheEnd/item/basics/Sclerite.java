package HamsterYDS.UntilTheEnd.item.basics;

import org.bukkit.Location;
import org.bukkit.Material;
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

import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Sclerite implements Listener{
	public static ItemStack item;
	public Sclerite() {		
		ItemProvider.addItem(this.getClass(),item);
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(!player.isSneaking()) return;
		if(event.getAction()!=Action.RIGHT_CLICK_AIR) return;
		ItemStack item=player.getItemInHand().clone();
		if(item==null) return;
		item.setAmount(1);
		if(item.equals(this.item)) {
			ItemStack itemr=player.getItemInHand();
			itemr.setAmount(itemr.getAmount()-1);
			//骨片扔出
			Entity entity=player.getWorld().spawnEntity(player.getLocation().add(0,1.0,0),EntityType.ARMOR_STAND);
			ArmorStand armor=(ArmorStand) entity;
			armor.setItemInHand(new ItemStack(Material.PRISMARINE_CRYSTALS));
			Vector vec=player.getEyeLocation().getDirection().multiply(5.0);
			armor.setInvulnerable(true);
			armor.setSmall(true);
			armor.setRightArmPose(new EulerAngle(0,0,0));
			armor.setVisible(false);
			armor.setAI(false);
			new Task(vec,armor,player);
		}
	}
	public class Task extends BukkitRunnable{
		Vector vec;
		ArmorStand armor;
		Player player;
		@Override
		public void run() {
			Location loc=armor.getLocation();
			armor.setVelocity(vec);
			for(Entity entity:armor.getNearbyEntities(0.5,0.5,0.5)) {
				if(entity==player) continue;
				if(entity instanceof LivingEntity) {
					LivingEntity creature=(LivingEntity) entity;
					creature.damage(1.0,player);
					armor.remove();
					cancel();
					return;
				}
			}
			loc=loc.add(vec);
			loc=loc.add(vec);
			if(!loc.getBlock().getType().isTransparent()) {
				if(loc.getBlock().getType()==Material.ARMOR_STAND) return;
				armor.teleport(loc);
				armor.setGravity(false);
				cancel();
				return;
			}
		}
		public Task(Vector vec,ArmorStand armor,Player player) {
			this.vec=vec;
			this.armor=armor;
			this.player=player;
			runTaskTimer(ItemLoader.plugin,0L,1L);
		}
	}
}
