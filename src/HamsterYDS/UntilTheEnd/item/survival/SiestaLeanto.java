package HamsterYDS.UntilTheEnd.item.survival;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

public class SiestaLeanto implements Listener{
	public SiestaLeanto() {
		HashMap<ItemStack,Integer> materials=new HashMap<ItemStack,Integer>();
		materials.put(ItemManager.namesAndItems.get("§6板条"),4);
		materials.put(ItemManager.namesAndItems.get("§6绳子"),3);
		materials.put(ItemManager.namesAndItems.get("§6毛皮卷"),1);
		materials.put(new ItemStack(Material.BANNER),1);
		ItemManager.registerRecipe(materials,ItemManager.namesAndItems.get("§6简易小木棚"),"§6生存");
		ItemManager.plugin.getServer().getPluginManager().registerEvents(this,ItemManager.plugin);
		
		ItemManager.canPlaceBlocks.put("SiestaLeanto",ItemManager.namesAndItems.get("§6简易小木棚"));
	}
	@EventHandler public void onClick(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		Player player=event.getPlayer();
		if(event.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		Block block=event.getClickedBlock();
		String toString=BlockApi.locToStr(block.getLocation());
		if(BlockApi.getSpecialBlocks("SiestaLeanto").contains(toString)) {
			event.setCancelled(true);
			if(player.getWorld().getTime()<=23000&&player.getWorld().getTime()>=16000) {
				player.sendMessage("§6[§cUntilTheEnd§6]§r 夜间不可使用此物品！");
				return;
			}
			boolean isBroken=Math.random()<=0.1;
			Entity bed=player.getWorld().spawnEntity(player.getLocation(),EntityType.ARROW);
			bed.teleport(block.getLocation().add(0.5,-0.2,0.5));
			bed.setPassenger(player);
			new BukkitRunnable() {
				@Override
				public void run() {
					player.removePotionEffect(PotionEffectType.BLINDNESS);
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,50,1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,50,1));
					if(Math.random()<=0.13) {
						if(player.getHealth()+1<player.getMaxHealth())
							player.setHealth(player.getHealth()+1); 
						PlayerManager.change(player.getName(),"san",1); 
					}
					if(Math.random()<=0.05) {
						if(player.getFoodLevel()>=1) player.setFoodLevel(player.getFoodLevel()-1);
						else {
							cancel();
							bed.remove();
							if(isBroken) block.breakNaturally();
						}
					}
					if(!player.isInsideVehicle()) {
						cancel();
						bed.remove();
						if(isBroken) block.breakNaturally();
					}
				}
			}.runTaskTimer(ItemManager.plugin,0L,1L); 
		}
	}
}
