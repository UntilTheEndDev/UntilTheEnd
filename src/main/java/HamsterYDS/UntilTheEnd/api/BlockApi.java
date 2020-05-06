package HamsterYDS.UntilTheEnd.api;

import HamsterYDS.UntilTheEnd.item.BlockManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockApi {
    public static String getSpecialBlock(Location loc) {
        String toString = locToStr(loc);
        return BlockManager.blocks.getOrDefault(toString, " ");
    }

    public static String getSpecialBlock(String toString) {
        return BlockManager.blocks.getOrDefault(toString, " ");
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
        HashMap<String, String> map = new HashMap<>();
        for (String str : BlockManager.blocks.keySet()) {
            String name = BlockManager.blocks.get(str);
            map.put(str, name);
        }
        return map;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static Location binToLoc(byte[] data) {
        final ByteArrayDataInput input = ByteStreams.newDataInput(data);
        String worldName = input.readUTF();
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new NoSuchElementException("World not found `" + worldName + "`");
        }
        return new Location(
                world,
                input.readInt(),
                input.readInt(),
                input.readInt()
        );
    }

    private static final Pattern loc3d = Pattern.compile(
            // World-X-Y-Z
            "(^.*)?-([0-9]+)-((-|)[0-9]+)-((-|)[0-9]+)$"
    );

    public static Location strToLoc(@NotNull String location) {
        try {
            return strToLoc0(location);
        } catch (Throwable any) {
            any.addSuppressed(new Exception("Try parsing `" + location + "`"));
            throw any;
        }
    }

    public static Location strToLoc0(@NotNull String location) {
        base64:
        {
            byte[] base64;
            try {
                base64 = Base64.getDecoder().decode(location);
            } catch (Exception ignored) {
                break base64;
            }
            return binToLoc(base64);
        }
        final Matcher matcher = loc3d.matcher(location);
        if (!matcher.find()) {
            return null;
        }
        // 1: World
        // 2: X
        // 3: Y
        // 5: Z
        String world = matcher.group(1);
        String x = matcher.group(2);
        String y = matcher.group(3);
        String z = matcher.group(5);
        if (world.isEmpty()) throw new ArrayIndexOutOfBoundsException("Empty world name");
        if (world.charAt(world.length() - 1) == '-') {
            world = world.substring(0, world.length() - 1);
            x = '-' + x;
        }
        return new Location(
                Objects.requireNonNull(Bukkit.getWorld(world), "No world `" + world + "` found."),
                Integer.parseInt(x),
                Integer.parseInt(y),
                Integer.parseInt(z)
        );
    }

    @SuppressWarnings("UnstableApiUsage")
    public static String locToStr(Location loc) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        World world = loc.getWorld();
        output.writeUTF(world.getName());
        output.writeInt(loc.getBlockX());
        output.writeInt(loc.getBlockY());
        output.writeInt(loc.getBlockZ());
        return Base64.getEncoder().encodeToString(output.toByteArray());
    }
}