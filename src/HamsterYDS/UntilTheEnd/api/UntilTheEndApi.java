package HamsterYDS.UntilTheEnd.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import HamsterYDS.UntilTheEnd.block.BlockManager;
import HamsterYDS.UntilTheEnd.cap.HudProvider;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.item.UTEItemStack;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;
import HamsterYDS.UntilTheEnd.player.role.Roles;
import HamsterYDS.UntilTheEnd.world.WorldProvider;
import HamsterYDS.UntilTheEnd.world.WorldProvider.Season;
import me.clip.placeholderapi.PlaceholderAPI;

public class UntilTheEndApi {
    public static class PluginApi {
        public static Class<?> getNMS(String name) {
            try {
                return Class.forName("net.minecraft.server." + getVersion() + "." + name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static String getVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }
    }

    public static class BlockApi {
        public static String getSpecialBlock(Location loc) {
            String toString = locToStr(loc);
            return BlockManager.blocks.containsKey(toString)?BlockManager.blocks.get(toString):" ";
        }

        public static HashMap<String, String> getSpecialBlocks(World world) {
            HashMap<String, String> map = new HashMap<String, String>();
            for (String str : BlockManager.blocks.keySet()) {
                if (!str.startsWith(world.getName())) continue;
                String name = BlockManager.blocks.get(str);
                map.put(str, name);
            }
            return map;
        }

        public static ArrayList<String> getSpecialBlocks(String name) {
            ArrayList<String> list = new ArrayList<String>();
            if (!BlockManager.blockDatas.containsKey(name)) return list;
            list.addAll(BlockManager.blockDatas.get(name));
            return list;
        }

        public static HashMap<String, String> getSpecialBlocks() {
            HashMap<String, String> map = new HashMap<String, String>();
            for (String str : BlockManager.blocks.keySet()) {
                String name = BlockManager.blocks.get(str);
                map.put(str, name);
            }
            return map;
        }

        public static Location strToLoc(String toString) {
            try {
                StringBuilder world = new StringBuilder();
                StringBuilder x = new StringBuilder();
                StringBuilder y = new StringBuilder();
                StringBuilder z = new StringBuilder();
                int tot = 0;
                for (int i = 0; i < toString.toCharArray().length; i++) {
                    char ch = toString.toCharArray()[i];
                    if (ch == '-') {
                        tot++;
                        if (toString.toCharArray()[i + 1] == '-')
                            tot--;
                        continue;
                    }
                    if (tot == 0) world.append(ch);
                    if (tot == 1) x.append(ch);
                    if (tot == 2) y.append(ch);
                    if (tot == 3) z.append(ch);
                }
                return new Location(Bukkit.getWorld(world.toString()), Integer.parseInt(x.toString()), Integer.parseInt(y.toString()), Integer.parseInt(z.toString()));
            } catch (Exception e) {
                return null;
            }
        }

        public static String locToStr(Location loc) {
            World world = loc.getWorld();
            String toString = world.getName();
            toString = toString + loc.getBlockX() + "-" + loc.getBlockY() + "-" + loc.getBlockZ();
            return toString;
        }
    }

    public static class ItemApi {
        public static Set<ItemStack> getItems() {
            return ItemManager.ids.keySet();
        }

        public static UTEItemStack getItem(String id) {
            return ItemManager.items.get(id);
        }
    }

    public static class PlayerApi {
        public static String getPAPI(Player player, String line) {
            String newLine = PlaceholderAPI.setPlaceholders(player, line);
            return newLine;
        }

        public static String getChangingTend(Player player, String type) {
            if (type.equalsIgnoreCase("san")) return HudProvider.sanity.get(player.getUniqueId());
            if (type.equalsIgnoreCase("tem")) return HudProvider.temperature.get(player.getUniqueId());
            if (type.equalsIgnoreCase("hum")) return HudProvider.humidity.get(player.getUniqueId());
            if (type.equalsIgnoreCase("tir")) return HudProvider.tiredness.get(player.getUniqueId());
            return "";
        }

        public static String getSanityColor(Player player) {
            double san = getValue(player, "san");
            if (san >= 120) return HudProvider.yaml.getString("sanityColor.120");
            if (san >= 90) return HudProvider.yaml.getString("sanityColor.90");
            if (san >= 60) return HudProvider.yaml.getString("sanityColor.60");
            if (san >= 30) return HudProvider.yaml.getString("sanityColor.30");
            if (san >= 0) return HudProvider.yaml.getString("sanityColor.0");
            return "";
        }

        public static String getHumidityColor(Player player) {
            double hum = getValue(player, "hum");
            if (hum <= 5) return HudProvider.yaml.getString("humidityColor.5");
            if (hum <= 15) return HudProvider.yaml.getString("humidityColor.15");
            if (hum <= 25) return HudProvider.yaml.getString("humidityColor.25");
            return "";
        }

        public static String getTemperatureColor(Player player) {
            double tem = getValue(player, "tem");
            if (tem <= 15) return HudProvider.yaml.getString("temperatureColor.15");
            if (tem <= 50) return HudProvider.yaml.getString("temperatureColor.50");
            if (tem <= 75) return HudProvider.yaml.getString("temperatureColor.75");
            return "";
        }
        
        public static String getTirednessColor(Player player) {
            double tir = getValue(player, "tir");
            if (tir <= 25) return HudProvider.yaml.getString("tirednessColor.25");
            if (tir <= 50) return HudProvider.yaml.getString("tirednessColor.50");
            if (tir <= 75) return HudProvider.yaml.getString("tirednessColor.75");
            if (tir <= 100) return HudProvider.yaml.getString("tirednessColor.100");
            return "";
        }

        public static double getValue(Player player, String type) {
            return PlayerManager.check(player, type);
        }

        public static void setValue(Player player, String type, int value) {
            PlayerManager.change(player, type, value);
        }
        
        public static Roles getRole(Player player) {
        	return PlayerManager.checkRole(player);
        }
        
        public static void changeRole(Player player,Roles role) {
        	PlayerManager.changeRole(player,role);
        }
        
        public static String getSanityBar(Player player) {
        	double san=PlayerManager.check(player,CheckType.SANITY);
        	double sanMax=PlayerManager.check(player,CheckType.SANMAX);
        	return getMessageBar(player,san,sanMax,HudProvider.yaml.getString("messageBar.sancolor")); 
        }
        
        public static String getHumidityBar(Player player) {
        	double hum=PlayerManager.check(player,CheckType.HUMIDITY);
        	double humMax=100;
        	return getMessageBar(player,hum,humMax,HudProvider.yaml.getString("messageBar.humcolor")); 
        }
        
        public static String getTemperatureBar(Player player) {
        	double tem=PlayerManager.check(player,CheckType.TEMPERATURE)+5;
        	double temMax=80;
        	return getMessageBar(player,tem,temMax,HudProvider.yaml.getString("messageBar.temcolor")); 
        }
        
        public static String getTirednessBar(Player player) {
        	double tir=PlayerManager.check(player,CheckType.TIREDNESS);
        	double tirMax=100;
        	return getMessageBar(player,tir,tirMax,HudProvider.yaml.getString("messageBar.tircolor")); 
        }
        
        public static String getMessageBar(Player player, double value, double maxValue, String color1) {
        	String bar="";
        	String newBar=color1;
        	int length=HudProvider.yaml.getInt("messageBar.length");
        	String color2=HudProvider.yaml.getString("messageBar.nocolor");
        	String unit=HudProvider.yaml.getString("messageBar.unit");
        	for(int i=0;i<length;i++) bar+=unit;
        	boolean flag=true;
        	if(value==0) newBar+=color2;
        	for(int i=0;i<bar.length();i++) {
        		double percent =(double)i/(double)bar.length();
        		newBar+=bar.charAt(i);
        		if(percent>=value/maxValue&&flag) {
        			newBar+=color2;
        			flag=false;
        		}
        	}
        	newBar+="§r";
        	return newBar;
        }
    }

    public static class WorldApi {
        public static String getSeasonColor(World world) {
            return HudProvider.yaml.getString("seasonColor." + getSeason(world).name(), "");
        }

        public static String getName(Season season) {
            return season.getName();
        }

        public static Season getSeason(World world) {
            if (WorldProvider.worldStates.containsKey(world.getName()))
                return WorldProvider.worldStates.get(world.getName()).season;
            return Season.NULL;
        }

        public static int getDay(World world) {
            if (WorldProvider.worldStates.containsKey(world.getName()))
                return WorldProvider.worldStates.get(world.getName()).day;
            return -1;
        }
    }

    public static class GuideApi {
        public static void addItemToCategory(String category, ItemStack item) {
            CraftGuide.addItem("§6"+category, item);
        }

        public static void addCategory(String categoryName, Material material, short data) {
            ItemStack item = CraftGuide.getItem(categoryName, material, data);
            CraftGuide.inv.setItem(CraftGuide.tot++, item);
            CraftGuide.helps.put(categoryName, CraftGuide.getTypeInventory());
            CraftGuide.crafts.put(item, CraftGuide.helps.get(categoryName).get(0));
        }

        public static void addCraftToItem(ItemStack item, Inventory inventory) {
            ItemStack back = CraftGuide.getItem(UTEi18n.cache("item.guide.action.previous"), Material.STAINED_GLASS_PANE, 6);
            ItemStack menu = CraftGuide.getItem(UTEi18n.cache("item.guide.action.main"), Material.STAINED_GLASS_PANE, 9);
            inventory.setItem(0, back);
            inventory.setItem(8, menu);
            CraftGuide.crafts.put(item, inventory);
        }
    }
}
