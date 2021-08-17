package ute.item.combat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import ute.UntilTheEnd;
import ute.api.BlockApi;
import ute.internal.ItemFactory;
import ute.item.ItemManager;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class ChainingAxe implements Listener {
    public ChainingAxe(){
        Bukkit.getPluginManager().registerEvents(this, UntilTheEnd.getInstance());
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player=event.getPlayer();
        if(player==null) return;
        if(player.getInventory().getItemInMainHand()==null) return;
        ItemStack item=player.getInventory().getItemInMainHand();
        if(!event.getBlock().getType().toString().contains("LOG")) return;
        if(ItemManager.isSimilar(item,this.getClass())){
            Block block=event.getBlock();
            List<Location> mines=new ArrayList<Location>();
            Queue<String> queue=new PriorityQueue<String>();
            queue.add(BlockApi.locToStr(block.getLocation()));
            mines.add(block.getLocation());
            while(!queue.isEmpty()){
                if(mines.size()>=100) break;
                String toString=queue.element();
                Location current=BlockApi.strToLoc(toString);
                queue.remove();
                List<Location> locs=new ArrayList<Location>();
                locs.add(current.clone().add(0,1,0));
                locs.add(current.clone().add(0,-1,0));
                locs.add(current.clone().add(1,0,0));
                locs.add(current.clone().add(-1,0,0));
                locs.add(current.clone().add(0,0,-1));
                locs.add(current.clone().add(0,0,1));
                for(Location loc:locs){
                    if(loc.getBlock().getType()==current.getBlock().getType()&&(!mines.contains(loc))){
                        queue.add(BlockApi.locToStr(loc));
                        mines.add(loc);
                    }
                }
            }
            if(player.getFoodLevel()<(mines.size()/7)){
                player.sendMessage("§c[UntilTheEnd]§r 您的饱食度不足，无法连锁伐木");
                return;
            }
            player.setFoodLevel(player.getFoodLevel()-(mines.size()/7));
            for(Location loc:mines){
                BlockBreakEvent evt=new BlockBreakEvent(loc.getBlock(), player);
                Bukkit.getPluginManager().callEvent(evt);
                if(evt.isCancelled())
                    continue;
                loc.getBlock().breakNaturally();
                if(item.containsEnchantment(Enchantment.DURABILITY)){
                    if(Math.random()<=1-0.3*item.getEnchantmentLevel(Enchantment.DURABILITY))
                        item.setDurability((short) (item.getDurability()+1));
                }else
                    item.setDurability((short) (item.getDurability()+1));
                if (item.getDurability() > ItemFactory.getType(item).getMaxDurability())
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
        }
    }
}
