package ute;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.NotNull;
import ute.internal.YamlUpdater;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Config {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static List<World> enableWorlds = new ArrayList<>();
    private static Set<String> disables = new HashSet<>();

    public static void registerWorld(World world) {
        final String wn = world.getName();
        if (disables.stream().noneMatch(name -> name.equalsIgnoreCase(wn))) {
            enableWorlds.add(world);
        } 
    }

    public static void initialize() {
        disables.addAll(plugin.getConfig().getStringList("disableWorlds"));
        autoUpdateConfigs("config.yml");
        Bukkit.getWorlds().forEach(Config::registerWorld);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler()
            void onWorldLoad(WorldLoadEvent event) {
                registerWorld(event.getWorld());
            }

            @EventHandler()
            void onWorldUnload(WorldUnloadEvent event) {
                enableWorlds.remove(event.getWorld());
            }
        }, plugin);
    }

    public static YamlConfiguration autoUpdateConfigs(String name) {
        File file = new File(plugin.getDataFolder(), name);
        final InputStream resource = plugin.getResource(name);
        if (resource != null) {
            if (!file.isFile()) {
                plugin.saveResource(name, true);
                return YamlConfiguration.loadConfiguration(file);
            }
            try (InputStreamReader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
                try (BufferedReader buffer = new BufferedReader(reader)) {
                    final ArrayDeque<String> strings = buffer.lines().collect(Collectors.toCollection(ArrayDeque::new));
                    Map<String, List<String>> commits = new LinkedHashMap<>();
                    Map<String, List<String>> area = new LinkedHashMap<>();
                    Collection<String> copyright = new ArrayList<>();
                    YamlUpdater.parse(strings, commits, area, copyright);
                    try (RandomAccessFile data = new RandomAccessFile(file, "rw")) {
                        try (InputStreamReader fReader = new InputStreamReader(new InputStream() {
                            @Override
                            public int read() throws IOException {
                                return data.read();
                            }

                            @Override
                            public int read(@NotNull byte[] b) throws IOException {
                                return data.read(b);
                            }

                            @Override
                            public int read(@NotNull byte[] b, int off, int len) throws IOException {
                                return data.read(b, off, len);
                            }
                        }, StandardCharsets.UTF_8)) {
                            try (BufferedReader fBuffer = new BufferedReader(fReader)) {
                                Map<String, List<String>> output_commits = new LinkedHashMap<>();
                                Map<String, List<String>> output_areas = new LinkedHashMap<>();
                                YamlUpdater.parse(fBuffer.lines().collect(Collectors.toCollection(ArrayDeque::new)),
                                        output_commits, output_areas, null);
                                YamlUpdater.merge(commits, area, output_commits, output_areas);
                                commits = output_commits;
                                area = output_areas;
                            }
                        }
                        data.seek(0);
                        try (OutputStreamWriter writer = new OutputStreamWriter(new OutputStream() {
                            @Override
                            public void write(int b) throws IOException {
                                data.write(b);
                            }

                            @Override
                            public void write(@NotNull byte[] b, int off, int len) throws IOException {
                                data.write(b, off, len);
                            }

                            @Override
                            public void write(@NotNull byte[] b) throws IOException {
                                data.write(b);
                            }
                        }, StandardCharsets.UTF_8)) {
                            YamlUpdater.store(commits, area, copyright, writer);
                        }
                        data.setLength(data.getFilePointer());
                    }
                }
            } catch (IOException ioe) {
                Logging.getLogger().log(Level.SEVERE, "Failed in updating configuration " + name, ioe);
            }
        }
        /*
        final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        plugin.saveResource(name, true);
        YamlConfiguration newYaml = YamlConfiguration.loadConfiguration(file);
        for (String path : yaml.getKeys(true)) {
            Object obj = yaml.get(path);
            newYaml.set(path, obj);
        }
        try {
            newYaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        return YamlConfiguration.loadConfiguration(file);
    }
}
