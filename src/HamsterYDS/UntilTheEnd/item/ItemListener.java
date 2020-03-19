package HamsterYDS.UntilTheEnd.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;

public class ItemListener implements Listener {
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		ItemStack item = event.getItemInHand();
		String id = ItemManager.isUTEItem(item);
		if (!id.equalsIgnoreCase("")) {
			if (ItemManager.items.get(id).canPlace)
				return;
			event.setCancelled(true);
			event.getPlayer().sendMessage(UTEi18n.cacheWithPrefix("item.system.no-place"));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onUse(PlayerInteractEvent event) {
		if (EventHelper.isRight(event.getAction())) {
			Player player = event.getPlayer();
			if (player.getGameMode() == GameMode.CREATIVE)
				return;
			PlayerInventory inv = player.getInventory();

			if (inv.getItemInMainHand() == null)
				return;

			ItemStack item = inv.getItemInMainHand();
			String id = ItemManager.isUTEItem(item);
			if (!id.equalsIgnoreCase("")) {
				if (ItemManager.items.get(id).isConsume) {
					item.setAmount(item.getAmount() - 1);
					player.updateInventory();
				}
			}
		}
	}

	@EventHandler
	public void onCraftVanillaRecipes(CraftItemEvent event) {
		Recipe recipe = event.getRecipe();
		ItemStack resultClone = recipe.getResult().clone();
		resultClone.setAmount(1);
		if (!ItemManager.isUTEItem(resultClone).equalsIgnoreCase(""))
			return;
		for (ItemStack item : event.getClickedInventory().getContents()) {
			if (item == null)
				return;
			if (!ItemManager.isUTEItem(item).equalsIgnoreCase("")) {
				event.setCancelled(true);
				event.getWhoClicked().sendMessage(UTEi18n.cacheWithPrefix("item.system.no-crafting"));
			}
		}
	}

	// @EventHandler
	// public void onCraftUTERecipes(CraftItemEvent event) {
	// Recipe recipe = event.getRecipe();
	// ItemStack resultClone = recipe.getResult().clone();
	// resultClone.setAmount(1);
	// if (ItemManager.isUTEItem(resultClone).equalsIgnoreCase(""))
	// return;
	// Inventory inv = event.getInventory();
	// String id=ItemManager.isUTEItem(resultClone);
	// UTEItemStack item=ItemManager.items.get(id);
	// int level=item.needLevel;
	// HashMap<ItemStack, Integer> craft = item.craft;
	// for (ItemStack material : craft.keySet()){
	// if(ItemManager.isUTEItem(material).equalsIgnoreCase("")) continue;
	// if (!inv.containsAtLeast(material, craft.get(material))){
	// event.setCancelled(true);
	// return;
	// }
	// }
	//
	// Player player=(Player) event.getWhoClicked();
	// if(PlayerManager.checkUnLockedRecipes(player).contains(id))
	// return;
	//
	//
	// if(level==0) return;
	// String machineId=ItemManager.machines.get(level);
	// for(int i=-5;i<=5;i++)
	// for(int j=-5;j<=5;j++)
	// for(int k=-5;k<=5;k++){
	// Location newLoc=new Location(player.getWorld(),
	// player.getLocation().getX()+i,
	// player.getLocation().getY()+j,
	// player.getLocation().getZ()+k);
	// newLoc=newLoc.getBlock().getLocation();
	// if(BlockApi.getSpecialBlock(newLoc).equalsIgnoreCase(machineId)){
	//
	// PlayerManager.addUnLockedRecipes(player,id);
	// return;
	// }
	// }
	// if(!flag) {
	// event.getWhoClicked().sendMessage(UTEi18n.cacheWithPrefix("item.system.no-machine"));
	// event.setCancelled(true);
	// }
	// }

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		if (inv == null)
			return;
		if (inv.getType() == InventoryType.WORKBENCH) {
			CraftingInventory craftInv = (CraftingInventory) inv;
			Player player = (Player) event.getWhoClicked();
			//点击合成栏的材料
			if (event.getSlot() < 9) {
				List<UTEItemStack> recipes = new ArrayList<UTEItemStack>();
				//检查所有item
				for (UTEItemStack uteitem : ItemManager.items.values())
					if (hasCraftItems(uteitem, craftInv))
						recipes.add(uteitem);
				//检测到有物品
				if (recipes.size() != 0) {
					craftInv.setResult(recipes.get(0).item.clone());
					player.updateInventory();
				}
				//检测到没有物品对应
				else {
					//取出先前的物品
					if (!ItemManager.isUTEItem(craftInv.getResult()).equalsIgnoreCase("")) {
						craftInv.setResult(new ItemStack(Material.AIR));
						player.updateInventory();
					}
				}
				
			}
			//点击成品
			if (event.getSlot() == 9) {
				ItemStack result = craftInv.getResult();
				String id = ItemManager.isUTEItem(result);
				UTEItemStack uteitem = ItemManager.items.get(id);
				if (uteitem == null)
					return;
				//是否已解锁
				if (!PlayerManager.checkUnLockedRecipes(player).contains(id))
					//没解锁看看有没有机器
					if (!hasMachine(id, player)) {
						event.setCancelled(true);
						player.sendMessage(UTEi18n.cacheWithPrefix("item.system.no-machine"));
						return;
					}
				//扣物品
				ItemStack[] items = craftInv.getMatrix();
				if (!id.equalsIgnoreCase("")) {
					for (int index = 0; index < items.length; index++) {
						if (items[index] == null)
							continue;
						ItemStack materialClone = items[index].clone();
						materialClone.setAmount(1);
						int amount = uteitem.craft.get(materialClone);
						items[index].setAmount(items[index].getAmount() - amount);
					}
					craftInv.setMatrix(items);
					player.updateInventory();
				}
			}

		}
	}

	private boolean hasMachine(String id, Player player) {
		//等级
		int level = ItemManager.items.get(id).needLevel;
		//不需要等级
		if (level == 0)
			return true;
		//提供等级的机器英文ID
		String machineId = ItemManager.machines.get(level);
		for (int i = -5; i <= 5; i++)
			for (int j = -5; j <= 5; j++)
				for (int k = -5; k <= 5; k++) {
					Location newLoc = new Location(player.getWorld(), player.getLocation().getX() + i,
							player.getLocation().getY() + j, player.getLocation().getZ() + k);
					newLoc = newLoc.getBlock().getLocation();
					if (BlockApi.getSpecialBlock(newLoc).equalsIgnoreCase(machineId)) {
						//旁边有机器
						PlayerManager.addUnLockedRecipes(player, id);
						return true;
					}
				}
		//旁边无机器
		return false;
	}

	private boolean hasCraftItems(UTEItemStack uteitem, CraftingInventory craftInv) {
		if(uteitem.craft==null) return false;
		for (int slot = 0; slot < 9; slot++) {
			ItemStack item = craftInv.getItem(slot);
			if (item == null)
				continue;
			//物品复制
			ItemStack itemClone = item.clone();
			itemClone.setAmount(1);
			itemClone.setDurability((short) 0);
			//多余物品
			if (!uteitem.craft.containsKey(itemClone)) 
				return false;
			//数量不够
			if (item.getAmount() < uteitem.craft.get(itemClone))
				return false;
		}
		return true;
	}

	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		EntityType type = event.getEntityType();
		if (!ItemProvider.drops.containsKey(type))
			return;
		HashMap<String, Double> drop = ItemProvider.drops.get(type);
		World world = event.getEntity().getWorld();
		Location loc = event.getEntity().getLocation();
		for (String id : drop.keySet()) {
			UTEItemStack item = ItemManager.items.get(id);
			double percent = drop.get(id);
			while (percent-- >= 1.0)
				world.dropItemNaturally(loc, item.item);
			if (Math.random() <= percent)
				world.dropItemNaturally(loc, item.item);
		}
	}

	@EventHandler
	public void onUseAnvil(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		if (inv == null)
			return;
		ItemStack item = event.getCursor();
		if (inv instanceof AnvilInventory) {
			if (!ItemManager.isUTEItem(item).equalsIgnoreCase(""))
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(ItemSpawnEvent event) {
		if (!ItemManager.plugin.getConfig().getBoolean("item.sawer.enable"))
			return;
		Item entityItem = event.getEntity();
		ItemStack item = entityItem.getItemStack();
		if (item.hasItemMeta())
			if (item.getItemMeta().hasDisplayName()) {
				entityItem.setCustomName(item.getItemMeta().getDisplayName());
				entityItem.setCustomNameVisible(true);
			}
	}
}