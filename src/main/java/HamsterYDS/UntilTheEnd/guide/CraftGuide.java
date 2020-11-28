package HamsterYDS.UntilTheEnd.guide;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.GuideApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CraftGuide implements Listener {
    public static Inventory guide=Bukkit.createInventory(new HolderCraftGuide(),45,"");
    public static HashMap<ItemStack,Inventory> button_to_gui=new HashMap<ItemStack,Inventory>();
    public static ArrayList<UUID> cheaters=new ArrayList<UUID>();

    public static ItemStack open_item=GuideApi.item_creater("§6§lUntilTheEnd指导",Material.ENCHANTED_BOOK, (short) 0);
    public static ItemStack guide_frame=GuideApi.item_creater("§8FRAME",Material.STAINED_GLASS_PANE, (short) 15);
    public static ItemStack back_button=GuideApi.item_creater("§e返回上一层",Material.STAINED_GLASS_PANE, (short) 6);
    public static ItemStack main_guide_button=GuideApi.item_creater("§e返回菜单",Material.STAINED_GLASS_PANE, (short) 9);
    public static ItemStack craft_button=GuideApi.item_creater("点我合成该物品",Material.STAINED_GLASS_PANE, (short) 9);


    public static void init(){
        guide=init_frame(guide,guide_frame);
        for(GuideApi.AvaliableCategories category:GuideApi.AvaliableCategories.values()){
            guide.setItem(category.slot_id,category.button);
            button_to_gui.put(category.button,get_simple_category_guide(category.button.getItemMeta().getDisplayName()));
        }
        Bukkit.getPluginManager().registerEvents(new CraftGuide(),UntilTheEnd.getInstance());
    }

    @EventHandler
    public void on_open_guide(PlayerInteractEvent event){
        if(event.hasItem()){
            ItemStack item=event.getItem();
            if(item.equals(open_item)){
                Player player=event.getPlayer();
                player.openInventory(guide);
                event.setCancelled(true);
            }
        }
    }
    private static ArrayList<UUID> openers=new ArrayList<UUID>();
    @EventHandler public void on_open_guide(InventoryOpenEvent event){
        if(event.getInventory().getHolder() instanceof HolderCraftGuide
                ||event.getInventory().getHolder() instanceof HolderItemCraftingHelp){
            openers.add(event.getPlayer().getUniqueId());
        }
    }
    @EventHandler public void on_close_guide(InventoryCloseEvent event){
        if(event.getInventory().getHolder() instanceof HolderCraftGuide
                ||event.getInventory().getHolder() instanceof HolderItemCraftingHelp){
            openers.remove(event.getPlayer().getUniqueId());
        }
    }
    @EventHandler public void on_operate(InventoryEvent event){
        if(event.getInventory().getHolder() instanceof HolderCraftGuide
                ||event.getInventory().getHolder() instanceof HolderItemCraftingHelp){
            //TODO 阻止操作
        }
    }
    @EventHandler public void on_click(InventoryClickEvent event){
        Inventory inv=event.getClickedInventory();
        if(inv.getHolder() instanceof HolderCraftGuide
                ||inv.getHolder() instanceof HolderItemCraftingHelp){
            if(button_to_gui.containsKey(event.getCurrentItem())) {
                Player player = (Player) event.getWhoClicked();
                player.openInventory(button_to_gui.get(event.getCurrentItem()));
            }
        }
    }
    //TODO 打开初始化
    //TODO 合成等按钮
    //TODO 作弊功能

    public static Inventory get_simple_category_guide(String title){
        Inventory guide=Bukkit.createInventory(new HolderItemCraftingHelp(),45,title);
        guide=init_frame(guide,guide_frame);
        guide.setItem(0,back_button);
        guide.setItem(8,main_guide_button);
        return guide;
    }

    public static Inventory get_simple_craft_guide(String title){
        Inventory guide=Bukkit.createInventory(new HolderItemCraftingHelp(),45,title);
        guide=init_frame(guide,guide_frame);
        guide.setItem(0,back_button);
        guide.setItem(8,main_guide_button);
        guide.setItem(40,craft_button);
        return guide;
    }

    public static Inventory init_frame(Inventory inv,ItemStack frame){
        int size=inv.getSize();
        for(int slot_id=0;slot_id<size;slot_id++){
            if(slot_id<=8||slot_id>=size-9||slot_id%9==0||slot_id%9==8){
                inv.setItem(slot_id,frame);
            }
        }
        return inv;
    }

    public static Inventory create_button(Inventory inv,ItemStack button,int slot_id,Inventory direct_inv){
        inv.setItem(slot_id,button);
        button_to_gui.put(button,direct_inv);
        return inv;
    }

    public static Inventory create_buttons(Inventory inv,ArrayList<ItemStack> buttons,ArrayList<Integer> slot_ids,ArrayList<Inventory> direct_invs){
        for(int index=0;index<buttons.size();index++){
            inv.setItem(slot_ids.get(index),buttons.get(index));
            button_to_gui.put(buttons.get(index),direct_invs.get(index));
        }
        return inv;
    }
}