package HamsterYDS.UntilTheEnd.item;

import java.util.HashMap;

import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.BlockApi;
import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

import org.bukkit.GameMode;
import org.bukkit.Location;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;

public class ItemListener implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;
        ItemStack itemClone = event.getItemInHand().clone();
        itemClone.setAmount(1);
        itemClone.setDurability((short) 0); 
        if (ItemManager.ids.containsKey(itemClone)) {
            String id = ItemManager.ids.get(itemClone);
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
            ItemStack itemClone = item.clone();
            int amount = item.getAmount();
            itemClone.setAmount(1);
            itemClone.setDurability((short) 0);
            if (ItemManager.ids.containsKey(itemClone)){
            	 String id = ItemManager.ids.get(itemClone);
                 if (ItemManager.items.get(id).isConsume) 
                     item.setAmount(amount - 1);
            }
        }
    }

    @EventHandler
    public void onCraftVanillaRecipes(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        ItemStack resultClone = recipe.getResult().clone();
        resultClone.setAmount(1);
        if (ItemManager.ids.containsKey(resultClone))
            return;
        for (ItemStack item : event.getClickedInventory().getContents()) {
            if (item == null)
                return;
            ItemStack itemClone = item.clone();
            itemClone.setAmount(1);
            itemClone.setDurability((short) 0); 
            if (ItemManager.ids.containsKey(itemClone)) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(UTEi18n.cacheWithPrefix("item.system.no-crafting"));
            }
        } 
    }

    @EventHandler
    public void onCraftUTERecipes(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        ItemStack resultClone = recipe.getResult().clone();
        resultClone.setAmount(1);
        if (!ItemManager.ids.containsKey(resultClone))
            return;
        Inventory inv = event.getInventory();
        String id=ItemManager.ids.get(resultClone);
        UTEItemStack item=ItemManager.items.get(id);
        HashMap<ItemStack, Integer> craft = item.craft;
        for (ItemStack material : craft.keySet()){
        	if(!ItemManager.ids.containsKey(material)) return;
        	if (!inv.containsAtLeast(material, craft.get(material)))
        		 event.setCancelled(true);
        }
               
        boolean flag=false;
        Player player=(Player) event.getWhoClicked();
        if(PlayerManager.checkUnLockedRecipes(player).contains(id))
        	return;
        int level=item.needLevel;
        if(level!=0) {
        	String machineId=ItemManager.machines.get(level);
        	for(int i=-5;i<=5;i++)
        		for(int j=-5;j<=5;j++)
        			for(int k=-5;k<=5;k++){
        				Location newLoc=new Location(player.getWorld(),
        						player.getLocation().getX()+i,
        						player.getLocation().getY()+j,
        						player.getLocation().getZ()+k);
        				newLoc=newLoc.getBlock().getLocation();
        				if(BlockApi.getSpecialBlock(newLoc).equalsIgnoreCase(machineId)){
        					PlayerManager.addUnLockedRecipes(player,id);
        					return;
        				}
        			}
        }else return;
        if(!flag) {
        	event.getWhoClicked().sendMessage(UTEi18n.cacheWithPrefix("item.system.no-machine"));
        	event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        EntityType type = event.getEntityType();
        if (!ItemProvider.drops.containsKey(type))
            return;
        HashMap<String,Double> drop=ItemProvider.drops.get(type);
        World world = event.getEntity().getWorld();
        Location loc = event.getEntity().getLocation();
        for(String id:drop.keySet()) {
        	UTEItemStack item=ItemManager.items.get(id);
        	double percent=drop.get(id);
        	while (percent-- >= 1.0) 
                world.dropItemNaturally(loc, item.item);
            if (Math.random() <= percent)
                world.dropItemNaturally(loc, item.item);
        }
    }

    @EventHandler 
    public void onUseAnvil(InventoryClickEvent event) {
    	Inventory inv=event.getInventory();
    	if(inv==null) return;
    	ItemStack item=event.getCursor();
    	if(inv instanceof AnvilInventory) {
    		if(ItemManager.isUTEItem(item))
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