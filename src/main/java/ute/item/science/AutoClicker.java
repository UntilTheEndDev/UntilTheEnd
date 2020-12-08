package ute.item.science;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import ute.UntilTheEnd;
import ute.api.BlockApi;
import ute.api.GuideApi;
import ute.api.event.block.CustomBlockBreakEvent;
import ute.api.event.block.CustomBlockInteractEvent;
import ute.api.event.block.CustomBlockPlaceEvent;
import ute.guide.craft.UTEInvHolder;
import ute.guide.craft.event.OperateLimiter;
import ute.item.ItemManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AutoClicker implements Listener {
    public static HashMap<String,Clicker> clickers=new HashMap<String,Clicker>();

    public AutoClicker(){
        loadBlocks();
        new OperateGui();
        Bukkit.getPluginManager().registerEvents(this,UntilTheEnd.getInstance());
    }

    @EventHandler
    public void onEnergy(BlockRedstoneEvent event){
        Block block=event.getBlock();
        if(event.getNewCurrent()==0) return;
        if(BlockApi.getSpecialBlock(block.getLocation()).equalsIgnoreCase("AutoClicker")){
            Clicker clicker=clickers.get(BlockApi.locToStr(event.getBlock().getLocation()));
//            PlayerInteractEvent event2=new PlayerInteractEvent(clicker.master,clicker.action,new ItemStack(Material.AIR),block,clicker.facingDirection);
        }
    }

    public static class Clicker{
        public BlockFace facingDirection;
        public Action action;
        public String master;
        public ArrayList<String> permissioners;
    }

    public static class OperateGui implements Listener{
        public static Inventory inv= Bukkit.createInventory(new HolderOperateGui(),9,"控制台：修改点击器");
        public static HashMap<UUID,String> operaters=new HashMap<UUID,String>();
        public OperateGui(){
            Bukkit.getPluginManager().registerEvents(this, UntilTheEnd.getInstance());
            inv.setItem(0,GuideApi.item_creater("§6§l动作设置为：左键", Material.IRON_INGOT, (short) 0));
            inv.setItem(1,GuideApi.item_creater("§6§l动作设置为：右键", Material.GOLD_INGOT, (short) 0));
            inv.setItem(3,GuideApi.item_creater("§6§l方向设置为：东", Material.WOOL, (short) 1));
            inv.setItem(4,GuideApi.item_creater("§6§l方向设置为：西", Material.WOOL, (short) 2));
            inv.setItem(5,GuideApi.item_creater("§6§l方向设置为：南", Material.WOOL, (short) 3));
            inv.setItem(6,GuideApi.item_creater("§6§l方向设置为：北", Material.WOOL, (short) 4));
            inv.setItem(7,GuideApi.item_creater("§6§l方向设置为：上", Material.WOOL, (short) 5));
            inv.setItem(8,GuideApi.item_creater("§6§l方向设置为：下", Material.WOOL, (short) 6));
        }
        @EventHandler
        public void onInteract(CustomBlockInteractEvent event){
            if(ItemManager.isSimilar(event.getNewitem().item,AutoClicker.class)&&event.getAction()==Action.RIGHT_CLICK_BLOCK&&!(event.getWho().isSneaking())){
                Clicker clicker=clickers.get(BlockApi.locToStr(event.getClickedBlock().getLocation()));
                Player player=event.getWho();
                if(clicker.master.equalsIgnoreCase(player.getUniqueId().toString())||
                    clicker.permissioners.contains(player.getUniqueId().toString())){
                    player.openInventory(inv);
                    operaters.put(player.getUniqueId(),BlockApi.locToStr(event.getClickedBlock().getLocation()));
                    OperateLimiter.openers.add(player.getUniqueId());
                }
            }
        }
        @EventHandler
        public void onClose(InventoryCloseEvent event){
            if(event.getInventory().getHolder() instanceof HolderOperateGui){
                Player player= (Player) event.getPlayer();
                operaters.remove(player.getUniqueId());
                OperateLimiter.openers.remove(player.getUniqueId());
            }
        }
        @EventHandler
        public void onClick(InventoryClickEvent event){
            if(event.getClickedInventory()==null) return;
            if(event.getClickedInventory().getHolder() instanceof HolderOperateGui){
                Player player= (Player) event.getWhoClicked();
                Clicker clicker=clickers.get(operaters.get(player.getUniqueId()));
                int slot=event.getSlot();
                if(slot==0)
                    clicker.action=Action.LEFT_CLICK_BLOCK;
                if(slot==1)
                    clicker.action=Action.RIGHT_CLICK_BLOCK;
                if(slot==2)
                    return;
                if(slot==3)
                    clicker.facingDirection=BlockFace.EAST;
                if(slot==4)
                    clicker.facingDirection=BlockFace.WEST;
                if(slot==5)
                    clicker.facingDirection=BlockFace.SOUTH;
                if(slot==6)
                    clicker.facingDirection=BlockFace.NORTH;
                if(slot==7)
                    clicker.facingDirection=BlockFace.UP;
                if(slot==8)
                    clicker.facingDirection=BlockFace.DOWN;
                player.sendMessage("§c[UntilTheEnd] §r设置成功");
                saveBlocks();
            }
        }
        @EventHandler
        public void onInteractSneaking(CustomBlockInteractEvent event){
            if(event.getCustomItem().id.equalsIgnoreCase("AutoClicker")&&(!event.isCancelled())&&event.getWho().isSneaking()){
                Clicker clicker=clickers.get(BlockApi.locToStr(event.getClickedBlock().getLocation()));
                if(clicker==null) return;
                Player player=event.getWho();
                if(player.getUniqueId().toString().equalsIgnoreCase(clicker.master)){
                    event.setCancelled(true);
                    player.sendMessage("§c[UntilTheEnd]§r 输入您给予或删除传送权限的玩家对象的名字");
                    player.sendMessage("§c[UntilTheEnd]§r 给予请在名字前加“+”，删除则使用“-”");
                    waitRequires.put(player.getUniqueId(),BlockApi.locToStr(event.getClickedBlock().getLocation()));
                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            if(waitRequires.containsKey(player.getUniqueId())){
                                player.sendMessage("§c[UntilTheEnd]§r 操作因为时间过长而取消");
                                waitRequires.remove(player.getUniqueId());
                            }
                        }
                    }.runTaskLater(UntilTheEnd.getInstance(),600L);
                }
            }
        }
        private static HashMap<UUID,String> waitRequires=new HashMap<UUID,String>();
        @EventHandler
        public void onChat(AsyncPlayerChatEvent event){
            Player player=event.getPlayer();
            String message=event.getMessage();
            if(waitRequires.containsKey(player.getUniqueId())){

                event.setCancelled(true);
                String loc=waitRequires.get(player.getUniqueId());
                Clicker clicker=clickers.get(loc);

                if(clicker==null) return;

                if(message.startsWith("+"))
                    clicker.permissioners.add(message.replace("+",""));
                if(message.startsWith("-"))
                    clicker.permissioners.remove(message.replace("-",""));

                waitRequires.remove(player.getUniqueId());
                player.sendMessage("§c[UntilTheEnd]§r 操作成功");
                clickers.remove(loc);
                clickers.put(loc,clicker);
                saveBlocks();
            }
        }
    }
    public static class HolderOperateGui implements UTEInvHolder {
        public static final HolderOperateGui INSTANCE = new HolderOperateGui();
        private String name;

        @Override
        public String getCustomName() {
            return name;
        }

        @Override
        public void setCustomName(String name) {
            this.name = name;
        }
    }

    @EventHandler
    public void addBlock(CustomBlockPlaceEvent event) {
        if (!event.getItem().id.equalsIgnoreCase("AutoClicker"))
            return;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        String loc = BlockApi.locToStr(block.getLocation());
        Clicker clicker=new Clicker();
        clicker.master = player.getUniqueId().toString();
        clicker.permissioners = new ArrayList<String>();
        clicker.action=Action.RIGHT_CLICK_BLOCK;
        clicker.facingDirection=BlockFace.EAST;
        clickers.put(loc,clicker);
        saveBlocks();
    }

    @EventHandler
    public static void removeBlock(CustomBlockBreakEvent event) {
        if (!event.getCustomItem().id.equalsIgnoreCase("AutoClicker"))
            return;
        Block block = event.getBlock();
        String loc = BlockApi.locToStr(block.getLocation());
        clickers.remove(loc);

        saveBlocks();
    }
    
    public static void saveBlocks() {
        File file = new File(ItemManager.plugin.getDataFolder() + "/data/", "clickers.yml");
        file.delete();
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (String loc : clickers.keySet()) {
            Clicker clicker = clickers.get(loc);
            yaml.set(loc + ".master", clicker.master);
            yaml.set(loc + ".permissioners", clicker.permissioners);
            yaml.set(loc + ".action", clicker.action.toString());
            yaml.set(loc + ".facingDirection", clicker.facingDirection.toString());
        }
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBlocks() {
        File file = new File(ItemManager.plugin.getDataFolder() + "/data/", "clickers.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        clickers.clear();
        for (String loc : yaml.getKeys(false)) {
            Clicker clicker = new Clicker();
            clicker.master = yaml.getString(loc + ".master");
            clicker.permissioners = (ArrayList<String>) yaml.getStringList(loc + ".permissioners");
            clicker.action = Action.valueOf(yaml.getString(loc + ".action"));
            clicker.facingDirection = BlockFace.valueOf(yaml.getString(loc + ".facingDirection"));
            clickers.put(loc, clicker);
        }
    }
}
