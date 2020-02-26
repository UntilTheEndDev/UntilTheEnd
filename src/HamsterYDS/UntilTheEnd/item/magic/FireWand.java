package HamsterYDS.UntilTheEnd.item.magic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import HamsterYDS.UntilTheEnd.cap.hum.Humidity;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemLoader;
import HamsterYDS.UntilTheEnd.item.ItemProvider;
import HamsterYDS.UntilTheEnd.item.basics.AnimateWood;
import HamsterYDS.UntilTheEnd.item.basics.RedGum;
import HamsterYDS.UntilTheEnd.item.materials.NightMare;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class FireWand implements Listener{
	public static ItemStack item;
	public static NamespacedKey nsk=new NamespacedKey(Humidity.plugin,"ute.redwand");
	public FireWand() {		
		ShapelessRecipe recipe=new ShapelessRecipe(nsk,item);
		recipe.addIngredient(2,RedGum.item.getType());
		recipe.addIngredient(3,AnimateWood.item.getType());
		recipe.addIngredient(2,NightMare.item.getType());
		Bukkit.addRecipe(recipe); 
		ItemLoader.plugin.getServer().getPluginManager().registerEvents(this,ItemLoader.plugin);
		ItemProvider.addItem(this.getClass(),item);

		Inventory inv=CraftGuide.getCraftInventory();
		inv.setItem(11,item);
		ItemStack item3=AnimateWood.item.clone();
		item3.setAmount(3); 
		ItemStack item2_1=RedGum.item.clone();
		item2_1.setAmount(2); 
		ItemStack item2_2=NightMare.item.clone();
		item2_2.setAmount(2); 
		inv.setItem(14,item2_1);
		inv.setItem(15,item2_2);
		inv.setItem(16,item3);
		UntilTheEndApi.GuideApi.addCraftToItem(item,inv);
		UntilTheEndApi.GuideApi.addItemToCategory("§6魔法",item);
	}
	@EventHandler public void onCraft(CraftItemEvent event) {
		ItemStack item=event.getRecipe().getResult();
        item.setAmount(1);
        if (item.equals(this.item)) {
            if (!event.getInventory().containsAtLeast(AnimateWood.item,3)) {
                event.setCancelled(true);
            }
            if (!event.getInventory().containsAtLeast(RedGum.item,2)) {
                event.setCancelled(true);
            }
            if (!event.getInventory().containsAtLeast(NightMare.item,2)) {
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
		item.setDurability((short) 0);
		if(item.equals(this.item)) {
			event.setCancelled(true);
			if(WandCooldown.cd.containsKey(player.getName()))
				if(WandCooldown.cd.get(player.getName())>0) {
					player.sendMessage("§6[§c凌域§6]§r 您的魔咒未冷却！"); 
					return;
				}
			WandCooldown.cd.remove(player.getName());
			WandCooldown.cd.put(player.getName(),10);
			ItemStack itemr=player.getItemInHand();
			itemr.setDurability((short) (itemr.getDurability()+3));
			if(itemr.getDurability()>itemr.getType().getMaxDurability()) 
				player.setItemInHand(null);
			Location loc=player.getLocation().add(0.0,1.0,0.0);
			Vector vec=player.getEyeLocation().getDirection().multiply(0.5);
			PlayerManager.change(player.getName(),"san",-5);
			new BukkitRunnable() {
				int range=100;
				
				@Override
				public void run() {
					for(int i=0;i<5;i++) {
						range--;
						if(range<=0) {
							cancel();
							return;
						}
						loc.getWorld().spawnParticle(Particle.LAVA,loc,1);
						loc.add(vec);
						for(Entity entity:loc.getWorld().getNearbyEntities(loc,0.1,0.1,0.1)) {
							if(entity.getEntityId()==player.getEntityId()) continue;
							entity.setFireTicks(200);
							cancel();
							return;
						}
					}
				}
			}.runTaskTimer(ItemLoader.plugin,0L,1L);
		}
	}
	@EventHandler public void onLeftClick(PlayerInteractEvent event) {
		if(event.getPlayer().getItemInHand().equals(this.item)) {
			event.setCancelled(true);
		}
	}
}
