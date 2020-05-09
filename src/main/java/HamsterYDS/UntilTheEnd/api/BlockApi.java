package HamsterYDS.UntilTheEnd.api;

import HamsterYDS.UntilTheEnd.item.BlockManager;
import com.google.common.annotations.Beta;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
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
    private static Loc3D binToLoc(byte[] data) {
        final ByteArrayDataInput input = ByteStreams.newDataInput(data);
        String worldName = input.readUTF();
        return new Loc3D(
                worldName,
                input.readInt(),
                input.readInt(),
                input.readInt()
        );
    }

    @Beta
    public static final Pattern loc3d = Pattern.compile(
            // World-X-Y-Z
            "(^.*)?((-|)[0-9]+)-((-|)[0-9]+)-((-|)[0-9]+)$"
    );

    public static Loc3D strToLoc3d(@NotNull String location) {
        try {
            return strToLoc0(location);
        } catch (Throwable any) {
            any.addSuppressed(new Exception("Try parsing `" + location + "`"));
            throw any;
        }
    }

    public static Location strToLoc(@NotNull String location) {
        final Loc3D loc3d = strToLoc3d(location);
        if (loc3d == null) return null;
        try {
            return loc3d.toUsableLocation();
        } catch (Throwable any) {
            any.addSuppressed(new Exception("Try parsing `" + location + "`"));
            throw any;
        }
    }

    public static Loc3D strToLoc0(@NotNull String location) {
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
        // region Legacy

        // region 正则匹配信息
        // 1: World
        // 2: X
        // 4: Y
        // 6: Z
        // endregion
        String world = matcher.group(1);
        String x = matcher.group(2);
        String y = matcher.group(4);
        String z = matcher.group(6);
        if (world.isEmpty()) throw new ArrayIndexOutOfBoundsException("Empty world name");
        if (x.charAt(0) != '-') { // 应该永远都是 true?
            int cut = world.length();
            while (cut > 1) {
                int pre = cut - 1;
                char prev = world.charAt(pre);
                if (prev == '-') {
                    cut = pre;
                    break;
                } else {
                    if (prev >= '0' && prev <= '9') {
                        cut = pre;
                    } else break;
                }
            }
            String fullWorld = world;
            world = fullWorld.substring(0, cut);
            x = fullWorld.substring(cut) + x;
        }
        return new Loc3D(
                world,
                Integer.parseInt(x),
                Integer.parseInt(y),
                Integer.parseInt(z)
        );
        // endregion
    }

    @SuppressWarnings("UnstableApiUsage")
    public static @NotNull String locToStr(@NotNull Loc3D loc) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(loc.world);
        output.writeInt(loc.x);
        output.writeInt(loc.y);
        output.writeInt(loc.z);
        return Base64.getEncoder().encodeToString(output.toByteArray());
    }

    public static @NotNull String locToStr(@NotNull Location loc) {
        return locToStr(Loc3D.from(loc));
    }
}