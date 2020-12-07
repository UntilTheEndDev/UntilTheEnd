package ute.item.science;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.Inventory;
import ute.api.BlockApi;
import ute.api.event.block.CustomBlockBreakEvent;
import ute.api.event.block.CustomBlockPlaceEvent;
import ute.guide.craft.UTEInvHolder;
import ute.item.ItemManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AutoClicker implements Listener {
    public static HashMap<String,Clicker> clickers=new HashMap<String,Clicker>();
    @EventHandler
    public void onEnergy(BlockRedstoneEvent event){
        Block block=event.getBlock();
        if(BlockApi.getSpecialBlock(block.getLocation()).equalsIgnoreCase("AutoClicker")){
            
        }
    }
    public static class Clicker{
        public BlockFace facingDirection;
        public Action action;
        public String master;
        public ArrayList<String> permissioners;
    }

    public static class OperateGui{
        public static Inventory inv= Bukkit.createInventory(new HolderOperateGui(),9,"控制台：修改点击器");

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
