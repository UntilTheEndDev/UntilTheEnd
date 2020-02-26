package HamsterYDS.UntilTheEnd.item.combat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.item.basics.CatTail;
import HamsterYDS.UntilTheEnd.item.basics.DogTooth;
import HamsterYDS.UntilTheEnd.item.basics.Sclerite;
import HamsterYDS.UntilTheEnd.item.materials.Reed;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class BlowArrow3 implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.blowarrow3");
	public BlowArrow3() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(3,Reed.item.getType());
		recipe.addIngredient(2,DogTooth.item.getType());
		recipe.addIngredient(2,Sclerite.item.getType());
		recipe.addIngredient(1,CatTail.item.getType());
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item3=Reed.item.clone();
		item3.setAmount(3); 
		ItemStack item2_1=DogTooth.item.clone();
		item2_1.setAmount(2); 
		ItemStack item2_2=Sclerite.item.clone();
		item2_2.setAmount(2); 
		inv.setItem(13,item2_1);
		inv.setItem(14,item2_2);
		inv.setItem(15,item3);
		inv.setItem(16,CatTail.item.clone());
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6战斗",item);
	}
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(Reed.item,3)) {
                event.setCancelled(true);
            }
            if (!event.getInventory().containsAtLeast(DogTooth.item,2)) {
                event.setCancelled(true);
            }
            if (!event.getInventory().containsAtLeast(Sclerite.item,2)) {
                event.setCancelled(true);
            }
            if (!event.getInventory().containsAtLeast(CatTail.item,1)) {
                event.setCancelled(true);
            }
        }
	}
	@EventHandler public void onRight(PlayerInteractEvent event) {
		Player player=event.getPlayer();
		if(!player.isSneaking()) return;
		if(!(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK)) return;
		ItemStack item=player.getItemInHand().clone();
		if(item==null) return;
		item.setAmount(1);
		if(item.equals(this.item)) {
			ItemStack itemr=player.getItemInHand();
			itemr.setAmount(itemr.getAmount()-1);
			//吹箭射出 TODO
			Entity entity=player.getWorld().spawnEntity(player.getLocation().add(0,1.0,0),EntityType.ARMOR_STAND);
			ArmorStand armor=(ArmorStand) entity;
			armor.setItemInHand(new ItemStack(Material.STONE_SWORD));
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
			armor.getWorld().spawnParticle(Particle.SUSPENDED_DEPTH,armor.getLocation().add(0,1.0,0),3);
			armor.setVelocity(vec);
			for(Entity entity:armor.getNearbyEntities(0.5,0.5,0.5)) {
				if(entity==player) continue;
				if(entity instanceof LivingEntity) {
					LivingEntity creature=(LivingEntity) entity;
					creature.damage(10.0,player);
					creature.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,1200,0));
					creature.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,1200,0));
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
				}.runTaskTimer(ItemLoader.plugin,ItemLoader.plugin.getConfig().getInt("item.blowarrow.autoclear"),1);
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
