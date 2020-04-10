package HamsterYDS.UntilTheEnd.cap.tem;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.logging.Level;

import HamsterYDS.UntilTheEnd.internal.ItemFactory;
import HamsterYDS.UntilTheEnd.internal.ScriptProvider;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.WorldApi;
import HamsterYDS.UntilTheEnd.world.WorldProvider.Season;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import javax.script.CompiledScript;

public class TemperatureProvider {
    public interface TemperatureAllocator {
        double apply(int day, World world);
    }

    public static UntilTheEnd plugin;
    public static HashMap<World, AtomicDouble> worldTemperatures = new HashMap<>();
    public static HashMap<Material, Integer> blockTemperatures = new HashMap<>();
    public static HashMap<Material, FMBlock> fmBlocks = new HashMap<>();
    private static final BiFunction<World, AtomicDouble, AtomicDouble> merger = (w, v) -> v == null ? new AtomicDouble() : v;
    private static final Map<Season, TemperatureAllocator> rules = new HashMap<>();

    private static TemperatureAllocator fromScript(String script) {
        File sf = new File(UntilTheEnd.getInstance().getDataFolder(), "scripts/" + script);
        if (sf.isFile()) {
            try (Reader reader = new InputStreamReader(new FileInputStream(sf), StandardCharsets.UTF_8)) {
                final CompiledScript compile = ScriptProvider.compile(reader);
                return (day, world) -> {
                    Number num = (Number)
                            ScriptProvider.of(compile)
                                    .name(script)
                                    .append("day", day)
                                    .append("world", world)
                                    .invoke();
                    if (num == null) return 0D;
                    return num.doubleValue();
                };
            } catch (Exception e) {
                UntilTheEnd.getInstance().getLogger().log(Level.SEVERE, "Failed to load script " + script, e);
            }
        }
        return null;
    }

    private static void make(YamlConfiguration configuration, Season s, TemperatureAllocator default_) {
        String path = configuration.getString(s.name().toLowerCase(), "default");
        if (path.equals("default")) {
            rules.put(s, default_);
        } else {
            final TemperatureAllocator script = fromScript(path);
            rules.put(Season.SPRING, script == null ? default_ : script);
        }
    }

    static {
        final YamlConfiguration configuration = Config.autoUpdateConfigs("season-tmp.yml");
        make(configuration, Season.SPRING, (day, world) -> (Math.random() * (-5) + Math.random() * 5 + 1.5 * day - Math.sqrt(Math.sqrt(Math.sqrt(world.getTime())))));
        make(configuration, Season.SUMMER, (day, world) -> (Math.random() * (-5) + Math.random() * 15 - 0.2 * day * day + 4.6 * day + 35.5 - Math.sqrt(Math.sqrt(Math.sqrt(world.getTime())))));
        make(configuration, Season.AUTUMN, (day, world) -> (Math.random() * (-5) + Math.random() * 5 + 50 - 1.5 * day - Math.sqrt(Math.sqrt(Math.sqrt(world.getTime())))));
        make(configuration, Season.WINTER, (day, world) -> (0.15 * day * day - 3.5 * day + 3.3 - Math.random() * (-5) + Math.random() * 5 - Math.sqrt(Math.sqrt(Math.sqrt(world.getTime())))));
    }

    public static void updateWorldTemperature(World world, double value) {
        worldTemperatures.compute(world, merger).set(value);
    }

    public TemperatureProvider(UntilTheEnd plugin) {
        TemperatureProvider.plugin = plugin;
        loadWorldTemperatures();
        loadBlockTemperatures();
        loadFMBlocks();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler()
            void onWorldLoad(WorldLoadEvent event) {
                loadWorldTemperature(event.getWorld());
            }

            @EventHandler()
            void onWorldUnload(WorldUnloadEvent event) {
                worldTemperatures.remove(event.getWorld());
            }
        }, plugin);
    }

    public static void loadFMBlocks() {
        for (String path : Temperature.yaml.getKeys(true)) {
            if (path.startsWith("fmBlocks.")) {
                if (path.replace("fmBlocks.", "").contains(".")) continue;
                Material currentMaterial = ItemFactory.valueOf(path.replace("fmBlocks.", ""));
                Material newMaterial = ItemFactory.valueOf(Temperature.yaml.getString(path + ".newMaterial"));
                boolean isIncrease = Temperature.yaml.getBoolean(path + ".increase");
                int temperature = Temperature.yaml.getInt(path + ".temperature");
                fmBlocks.put(currentMaterial, new FMBlock(newMaterial, temperature, isIncrease));
                UntilTheEnd.getInstance().getLogger().info(UTEi18n.parse("cap.tem.provider.fmb.rule", path.replace("fmBlocks.", ""), String.valueOf(newMaterial), String.valueOf(temperature)));
            }
        }
    }

    public static void loadWorldTemperatures() {
        for (World world : Bukkit.getWorlds()) {
            loadWorldTemperature(world);
        }
    }

    public static void loadWorldTemperature(World world) {
        if (Config.enableWorlds.contains(world)) updateWorldTemperature(world, getWorldTemperature(world));
        else updateWorldTemperature(world, 37);
    }

    public static void loadBlockTemperatures() {
        for (String path : Temperature.yaml.getKeys(true)) {
            if (path.startsWith("blockTemperature.")) {
                int tem = Temperature.yaml.getInt(path);
                path = path.replace("blockTemperature.", "");
                Material material = ItemFactory.valueOf(path);
                UntilTheEnd.getInstance().getLogger().info(UTEi18n.parse("cap.tem.provider.block.tem.rule", String.valueOf(material), String.valueOf(tem)));
                blockTemperatures.put(material, tem);
            }
        }
    }

    public static double getWorldTemperature(World world) {
        Season season = WorldApi.getSeason(world);
        int day = WorldApi.getDay(world);
        final TemperatureAllocator allocator = rules.get(season);
        if (allocator != null) return allocator.apply(day, world);
        return 37;
    }

    public static double getBlockTemperature(Location loc) {
        if (!Config.enableWorlds.contains(loc.getWorld())) return 37;
        if (loc.getBlock() == null) return 37;
        World world = loc.getWorld();
        // 我们需要你的帮助来优化此温度算法! 谢谢!
        // We need the help for update this Temperature algorithm. Thank you very much!
        final Integer value = blockTemperatures.get(ItemFactory.getType(loc.getBlock()));
        if (value != null) return value;

        double season = TemperatureProvider.worldTemperatures.get(world).doubleValue();
        Location l = loc.clone();
        final int d = 4;
        double tem0 = season;
        double temchange = 0.0;
        double tot = 1;
        for (int x = -d; x <= d; x++) {
            l.setX(loc.getX() + x);
            for (int z = -d; z <= d; z++) {
                l.setZ(loc.getZ() + z);
                for (int y = -d; y <= d; y++) {
                    l.setY(loc.getY() + y);
                    Block b = l.getBlock();
                    if (b == null) continue;
                    Material mt = ItemFactory.getType(b);
                    final Integer tmp = blockTemperatures.get(mt);
                    if (tmp != null) {
                        double dg = l.distance(loc);
                        double weight = 1- dg / (4 * Math.sqrt(2));
                        tot += weight;
                        temchange += weight * (tmp - season);
                    }
                }
            }
        }
        double answer=tem0+(temchange / tot);
        return answer+(loc.getBlockY()>=90?(loc.getBlockY()-90)*0.6:0);
    }

    public static class FMBlock {
        Material newMaterial;
        int temperature;
        boolean upOrDown;

        public FMBlock(Material newBlock, int temperature, boolean upOrDown) {
            this.newMaterial = newBlock;
            this.temperature = temperature;
            this.upOrDown = upOrDown;
        }
    }
}
