package HamsterYDS.UntilTheEnd.cap.hum;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerInventoryAdapt;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Influencer extends BukkitRunnable implements Listener{
	public static UntilTheEnd plugin;
	public static final String wet="§8- §8§l潮湿的"; 
	public Influencer(UntilTheEnd plugin) {
		this.plugin=plugin;
		runTaskTimer(plugin,0L,200L);
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}
	@Override
	public void run() {
		for(World world:Config.enableWorlds) {
			for(Entity entity:world.getEntities()) {
				if(entity instanceof Item) {
					Item item=(Item) entity;
					ItemStack stack=item.getItemStack();
					if(Math.random()<=0.8) continue;
					if(isWet(stack)) continue;
					ItemMeta meta=stack.getItemMeta();
					List<String> lores=meta.getLore();
					if(!meta.hasLore()) lores=new ArrayList<String>();
					lores.add(wet);
					meta.setLore(lores);
					stack.setItemMeta(meta);
					item.setItemStack(stack);
				}
			}
			for(Player player:world.getPlayers()) {
				int hum=PlayerManager.check(player.getName(),"hum");
				if(hum>=10&&hum<20) becomeWet(player,1);						
				if(hum>=20&&hum<30) becomeWet(player,2);		
				if(hum>=30) becomeWet(player,3);	
				if(hum<10) becomeDry(player);
				if(hum>15) addEffect(player,hum);
			}
		}
	}
	private ArrayList<Integer> eaters=new ArrayList<Integer>();
	@EventHandler public void onEat1(PlayerItemConsumeEvent event) {
		ItemStack item=event.getItem();
		if(!item.getType().isEdible()) return;
		if(isWet(item)) 
			eaters.add(event.getPlayer().getEntityId());
	}
	@EventHandler public void onEat2(FoodLevelChangeEvent event) {
		Entity entity=event.getEntity();
		if(eaters.contains(entity.getEntityId())) {
			event.setFoodLevel((int)(event.getFoodLevel()*0.75));
			entity.sendMessage("§6[§c凌域§6]§r 潮湿的食物真难吃~");
			eaters.remove((Integer)entity.getEntityId());
		}
	}
	@EventHandler public void onDrag(InventoryDragEvent event) {
		Inventory inv=event.getInventory();
		if(inv==null) return;
		if(!(inv.getType()==InventoryType.WORKBENCH||inv.getType()==InventoryType.CRAFTING)) return;
		ItemStack item=event.getCursor();
		if(item==null) return;
		if(isWet(item)) {
			event.getWhoClicked().sendMessage("§6[§c凌域§6]§r 潮湿的物品貌似不能拖动，它们太笨重了！");
			event.setCancelled(true);
		}
	}
	@EventHandler public void onMove(InventoryMoveItemEvent event) {
		ItemStack item=event.getItem();
		if(item==null) return;
		if(isWet(item)) 
			event.setCancelled(true);
	}
	@EventHandler public void onClick(InventoryClickEvent event) {
		Inventory inv=event.getClickedInventory();
		if(inv==null) return;
		if(!(inv.getType()==InventoryType.WORKBENCH||inv.getType()==InventoryType.CRAFTING)) return;
		ItemStack item=event.getCursor();
		if(item==null) return;
		if(isWet(item)) {
			event.getWhoClicked().sendMessage("§6[§c凌域§6]§r 潮湿的物品貌似不能用于合成，它们太笨重了！");
			event.setCancelled(true);
		}
		ItemStack item2=inv.getItem(event.getSlot());
		if(item2==null) return;
		if(isWet(item2)) {
			event.getWhoClicked().sendMessage("§6[§c凌域§6]§r 潮湿的物品貌似不能用于合成，它们太笨重了！");
			event.setCancelled(true);
		}
	}
	@EventHandler public void onSweep(EntityDamageByEntityEvent event) {
		if(event.getCause()!=DamageCause.ENTITY_SWEEP_ATTACK) return;
		Entity entity=event.getDamager();
		if(PlayerManager.check(entity.getName(),"hum")>=10) {
			entity.sendMessage("§6[§c凌域§6]§r 您身上过于潮湿所以无法打出群体伤害！");
			event.setCancelled(true);
		}
	}
	private void addEffect(Player player, int hum) {
		if(!player.hasPotionEffect(PotionEffectType.SLOW_DIGGING))
			player.sendMessage("§6[§c凌域§6]§r 我全身都淋湿了！");
		player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,300,1));
	}
	private void becomeDry(Player player) {
		PlayerInventory inv=player.getInventory();
		for(int slot=0;slot<60;slot++) {
			double ran=Math.random();
			if(ran>0.7) continue;
			ItemStack item=inv.getItem(slot);
			if(item==null) return;
			if(!isWet(item)) continue;
			ItemMeta meta=item.getItemMeta();
			if(!meta.hasLore()) continue;
			List<String> lore=meta.getLore();
			lore.remove(wet);
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(slot,item);
		}
	}
	public void becomeWet(Player player,int level) {
		PlayerInventory inv=player.getInventory();
		for(int slot=0;slot<60;slot++) {
			if(slot>=9&&slot<9+4) {
				int bag=PlayerInventoryAdapt.getBag(inv.getChestplate());
				if(bag==4) {
					continue;
				}
			}
			double ran=0.5+Math.random()*level;
			if(ran<1.3) continue;
			ItemStack item=inv.getItem(slot);
			if(item==null) return;
			if(item.getType()==Material.STAINED_GLASS_PANE) continue;
			if(isWet(item)) continue;
			ItemMeta meta=item.getItemMeta();
			if(meta==null) return;
			List<String> lore;
			if(meta.hasLore()) lore=meta.getLore();
			else lore=new ArrayList<String>();
			lore.add(wet);
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(slot,item);
		}
	}
	public boolean isWet(ItemStack item) {
		ItemMeta meta=item.getItemMeta();
		if(meta==null) return false;
		List<String> lores=meta.getLore();
		if(lores==null) return false;
		for(String s:lores) 
			if(s.contains(wet))
				return true;
		return false;
	}
}
