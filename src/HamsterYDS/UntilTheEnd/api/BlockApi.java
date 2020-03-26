package HamsterYDS.UntilTheEnd.api;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import HamsterYDS.UntilTheEnd.item.BlockManager;

public class BlockApi {
    public static String getSpecialBlock(Location loc) {
        String toString = locToStr(loc);
        return BlockManager.blocks.containsKey(toString) ? BlockManager.blocks.get(toString) : " ";
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