package ute.item.combat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ute.UntilTheEnd;
import ute.api.BlockApi;
import ute.internal.ItemFactory;
import ute.item.ItemManager;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class ChainingSpade implements Listener {
    public ChainingSpade(){
        Bukkit.getPluginManager().registerEvents(this, UntilTheEnd.getInstance());
    }
    @EventHandler
    public void onBreak(PlayerInteractEvent event){
        Player player=event.getPlayer();
        if(player==null) return;
        if(player.getInventory().getItemInMainHand()==null) return;
        if(event.getClickedBlock()==null) return;
        if(event.getAction()!= Action.RIGHT_CLICK_BLOCK) return;
        if(!event.getClickedBlock().getType().toString().contains("GRASS")) return;

        ItemStack item=player.getInventory().getItemInMainHand();
        if(ItemManager.isSimilar(item,this.getClass())){
            Block block=event.getClickedBlock();
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
                locs.add(current.clone().add(1,0,0));
                locs.add(current.clone().add(-1,0,0));
                locs.add(current.clone().add(0,0,-1));
                locs.add(current.clone().add(0,0,1));
                for(Location loc:locs){
                    if(loc.clone().add(0,1,0).getBlock().getType()!=Material.AIR) continue;
                    if(loc.getBlock().getType().toString().contains("GRASS")&&(!mines.contains(loc))){
                        queue.add(BlockApi.locToStr(loc));
                        mines.add(loc);
                    }
                }
            }
            for(int index=0;index<mines.size()&&player.getFoodLevel()>0;index++){
                Location loc=mines.get(index);
                loc.getBlock().setType(Material.GRASS_PATH);
                if(item.containsEnchantment(Enchantment.DURABILITY)){
                    if(Math.random()<=1-0.3*item.getEnchantmentLevel(Enchantment.DURABILITY))
                        item.setDurability((short) (item.getDurability()+1));
                }else
                    item.setDurability((short) (item.getDurability()+1));
                if (item.getDurability() > ItemFactory.getType(item).getMaxDurability())
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                if(index%5==0&&index!=0)
                    player.setFoodLevel(player.getFoodLevel()-1);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player=event.getPlayer();
        if(player==null) return;
        if(player.getInventory().getItemInMainHand()==null) return;
        ItemStack item=player.getInventory().getItemInMainHand();
        if(!(event.getBlock().getType().toString().contains("DIRT")
                ||event.getBlock().getType().toString().contains("GRASS")
                ||event.getBlock().getType().toString().contains("SOIL"))) return;
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
                    if((loc.getBlock().getType().toString().contains("DIRT")
                    ||loc.getBlock().getType().toString().contains("GRASS")
                    ||loc.getBlock().getType().toString().contains("SOIL"))&&(!mines.contains(loc))){
                        queue.add(BlockApi.locToStr(loc));
                        mines.add(loc);
                    }
                }
            }
            for(int index=0;index<mines.size()&&player.getFoodLevel()>0;index++){
                Location loc=mines.get(index);
                loc.getBlock().breakNaturally();
                if(item.containsEnchantment(Enchantment.DURABILITY)){
                    if(Math.random()<=1-0.3*item.getEnchantmentLevel(Enchantment.DURABILITY))
                        item.setDurability((short) (item.getDurability()+1));
                }else
                    item.setDurability((short) (item.getDurability()+1));
                if (item.getDurability() > ItemFactory.getType(item).getMaxDurability())
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                if(index%5==0&&index!=0)
                    player.setFoodLevel(player.getFoodLevel()-1);
            }
        }
    }
}
