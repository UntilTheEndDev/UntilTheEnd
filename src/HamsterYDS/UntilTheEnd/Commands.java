package HamsterYDS.UntilTheEnd;

import java.util.*;

import HamsterYDS.UntilTheEnd.internal.ItemFactory;
import HamsterYDS.UntilTheEnd.internal.UTEi18n;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import HamsterYDS.UntilTheEnd.cap.tem.TemperatureProvider;
import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import HamsterYDS.UntilTheEnd.guide.Guide;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.item.UTEItemStack;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.role.Roles;
import HamsterYDS.UntilTheEnd.world.WorldCounter;
import HamsterYDS.UntilTheEnd.world.WorldProvider;
import HamsterYDS.UntilTheEnd.world.WorldProvider.IWorld;
import HamsterYDS.UntilTheEnd.world.WorldProvider.Season;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Commands implements CommandExecutor, Listener, TabCompleter {

    public static UntilTheEnd plugin;
    public static ArrayList<String> cmdTab = new ArrayList<String>();
    public static ArrayList<String> seasonTab = new ArrayList<String>();
    public static ArrayList<String> itemTab = new ArrayList<String>();
    public static ArrayList<String> worldTab = new ArrayList<String>();
    public static ArrayList<String> capTab = new ArrayList<String>();
    public static ArrayList<String> roleTab = new ArrayList<String>();

    static {
        itemTab.addAll(ItemManager.items.keySet());
        cmdTab.addAll(Arrays.asList("cheat", "give", "guide", "help", "material", "entitytype", "set", "season", "temp",
                "role", "openguide"));
        if (!Roles.isEnable) cmdTab.remove("role");
        for (Season season : Season.values())
            seasonTab.add(season.name().toLowerCase());
        for (PlayerManager.CheckType type : PlayerManager.CheckType.values())
            capTab.add(type.getShortName());
        for (Roles role : Roles.values())
            if (role.allow)
                roleTab.add(role.toString());
        Collections.sort(itemTab);
        Collections.sort(cmdTab);
    }

    public Commands(UntilTheEnd plugin) {
        final PluginCommand ute = plugin.getCommand("ute");
        ute.setExecutor(this);
        ute.setTabCompleter(this);
    }

    public static void sendHelp(CommandSender sender) {
        sender.sendMessage(
                UTEi18n.cacheWithPrefix("prefix") + "§bUTE v" + UntilTheEnd.getInstance().getDescription().getVersion());
        sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.label.1"));
        if (sender.hasPermission("ute.cheat"))
            sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.cheat"));
        if (sender.hasPermission("ute.guide"))
            sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.guide"));
        if (sender.hasPermission("ute.season"))
            sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.season"));
        if (sender.hasPermission("ute.give"))
            sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.give"));
        if (sender.hasPermission("ute.set"))
            sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.set"));
        if (sender.hasPermission("ute.role"))
            sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.role"));
        sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.label.2"));
        if (sender.hasPermission("ute.material"))
            sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.material"));
        if (sender.hasPermission("ute.entitytype"))
            sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.entitytype"));
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String lb, String[] ct) {
        // 不需要判断cmd name. 使用独立的执行器的话
        Player pl;
        if (!(cs instanceof Player)) {
            pl = null;
        } else {
            pl = (Player) cs;
        }
        if (ct.length == 0) {
            sendHelp(cs);
            return true;
        }
        String sub = ct[0];
        if (sub.equalsIgnoreCase("help")) {
            sendHelp(cs);
            return true;
        }
        if (!cs.hasPermission("ute." + sub.toLowerCase())) { // Permission Check.
            notPermitted(cs);
            return true;
        }
        switch (sub.toLowerCase()) {
            case "openguide": {
                if (pl == null) {
                    notPlayer(cs);
                } else {
                    pl.openInventory(Guide.inv);
                }
                break;
            }
            case "guide": {
                if (ct.length > 1) {// Open for other
                    if (!cs.hasPermission("ute.guide.other")) {
                        notPermitted(cs);
                        return true;
                    }
                    final Player player = Bukkit.getPlayer(ct[1]);
                    if (player == null) {
                        cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.null.player"));
                    } else {
                        player.openInventory(Guide.inv);
                    }
                    break;
                }
                if (pl == null) {
                    notPlayer(cs);
                } else {
                    pl.getInventory().addItem(Guide.item);
                }
                break;
            }
            case "cheat": {
                if (pl == null) {
                    notPlayer(cs);
                } else {
                    if (CraftGuide.cheating.contains(pl.getName()))
                        CraftGuide.cheating.remove(pl.getName());
                    else
                        CraftGuide.cheating.add(pl.getName());
                    changeCheatingMode(pl);
                }
                break;
            }
            case "material": {
                if (pl == null) {
                    notPlayer(cs);
                } else {
                    pl.sendMessage(String.valueOf(ItemFactory.getType(pl.getLocation().add(0, -1, 0).getBlock())));
                }
                break;
            }
            case "entitytype": {
                if (pl == null) {
                    notPlayer(cs);
                } else {
                    for (Entity entity : pl.getNearbyEntities(1.0, 1.0, 1.0))
                        pl.sendMessage(entity.getType().toString());
                }
                break;
            }
            case "season": {
                if (ct.length < 3) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.season")); // TODO
                    return true;
                }
                Season season = Season.found(ct[1]);
                if (season == null) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.season"));
                    return true;
                }
                int day;
                try {
                    day = Integer.parseInt(ct[2]);
                } catch (NumberFormatException format) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.number"));
                    return true;
                }
                World world = null;
                if (ct.length == 3) {
                    if (pl == null) {
                        world = Bukkit.getWorlds().get(0);
                    } else {
                        world = pl.getWorld();
                    }
                } else {
                    String tok = ct[3];
                    try {
                        world = Bukkit.getWorld(UUID.fromString(tok));
                    } catch (Throwable ignore) {
                    }
                    if (world == null)
                        world = Bukkit.getWorld(tok);
                }
                if (world == null) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.world"));
                    return true;
                }
                changeSeason(world, season, day);
                WorldCounter.tellPlayers(world);
                cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.set.season"));
                break;
            }
            case "give": {
                if (ct.length < 3) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.give"));
                } else {
                    String playerName = ct[1];
                    String itemName = ct[2];
                    int amount = 1;
                    if (ct.length == 4)
                        try {
                            amount = Integer.parseInt(ct[3]);
                        } catch (Exception e) {
                            cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.number"));
                            return true;
                        }
                    if (amount <= 0) {
                        cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.positive"));
                        return true;
                    }
                    if (!ItemManager.items.containsKey(itemName)) {
                        cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.item"));
                        return true;
                    }
                    Player givee = Bukkit.getPlayer(playerName);
                    UTEItemStack uteitem = ItemManager.items.get(itemName);
                    if (givee == null) {
                        cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.player"));
                        return true;
                    }
                    ItemStack item = uteitem.item.clone();
                    item.setAmount(amount);
                    givee.getInventory().addItem(item);
                    String message = UTEi18n.cacheWithPrefix("cmd.give.item").replace("{item}", uteitem.displayName).replace("{player}", playerName);
                    cs.sendMessage(message);
                }
                break;
            }
            case "set": {
                if (ct.length < 4) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.set"));
                    return true;
                }
                String playerName = ct[1];
                Player player = Bukkit.getPlayer(playerName);
                if (player == null) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.null.player"));
                    return true;
                }
                if (!player.isOnline()) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.null.player"));
                    return true;
                }
                String typeName = ct[2];
                double val;
                try {
                    val = Double.parseDouble(ct[3]);
                } catch (Exception e) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.number"));
                    return true;
                }
                PlayerManager.forgetChange(player, PlayerManager.CheckType.search(typeName),
                        val, PlayerManager.EditAction.SET);
                PlayerManager.save(player);
                cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.set.hud"));
                break;
            }
            case "temp": {
                if (pl != null) {
                    final Location location = pl.getLocation();
                    pl.sendMessage(TemperatureProvider.getBlockTemperature(location) + ", "
                            + location.getBlock().getTemperature());
                }
                break;
            }
            case "role": {
                if (pl == null) {
                    notPlayer(cs);
                    break;
                }
                if (!Roles.isEnable) {
                    notPermitted(cs);
                    break;
                }
                if (ct.length == 1)
                    pl.sendMessage(UTEi18n.cacheWithPrefix("cmd.role.check").replace("{role}", PlayerManager.checkRole(pl).name)); // TODO
                if (ct.length == 2) {
                    Roles role;
                    try {
                        role = Roles.valueOf(ct[1]);
                    } catch (Exception e) {
                        pl.sendMessage(UTEi18n.cacheWithPrefix("cmd.role.invalid"));
                        return true;
                    }
                    if (!role.allow || !pl.hasPermission("ute.role." + role.name))
                        notPermitted(cs);
                    if (PlayerManager.playerChangedRole.contains(pl.getUniqueId()))
                        return true;
                    PlayerManager.changeRole(pl, role);
                    PlayerManager.playerChangedRole.add(pl.getUniqueId());
                    pl.sendMessage(UTEi18n.cacheWithPrefix("cmd.role.change"));
                }
            }
            case "lore": {
                if (pl == null) {
                    notPlayer(cs);
                    break;
                }
                if (ct.length == 3) {
                    if (ct[1].equalsIgnoreCase("addtem")) {
                        int temperature;
                        try {
                            temperature = Integer.parseInt(ct[2]);
                        } catch (Exception e) {
                            cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.number"));
                            return true;
                        }
                        ItemStack item = pl.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();
                        List<String> lores = new ArrayList<String>();
                        if (meta.hasLore()) lores = meta.getLore();
                        lores.add("§8- §8§l温度 " + temperature);
                        meta.setLore(lores);
                        item.setItemMeta(meta);
                        pl.getInventory().setItemInMainHand(item);
                        pl.updateInventory();
                    }
                }
                break;
            }
        }
        return true;
    }

    public static void changeCheatingMode(Player player) {
        player.sendMessage(UTEi18n.cacheWithPrefix("cmd.cheat"));
    }

    public static void notPlayer(CommandSender CommandSender) {
        CommandSender.sendMessage(UTEi18n.cacheWithPrefix("cmd.not.player"));
    }

    public static void notPermitted(CommandSender CommandSender) {
        CommandSender.sendMessage(UTEi18n.cacheWithPrefix("cmd.no.permission"));
    }

    public static void changeSeason(World setWorld, Season season, int day) {
        IWorld world = new IWorld(season, day, season.newLoop());
        WorldProvider.worldStates.remove(setWorld.getName());
        WorldProvider.worldStates.put(setWorld.getName(), world);
        TemperatureProvider.loadWorldTemperatures();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String latest = null;
        List<String> list = new ArrayList<>();
        if (args.length != 0) {
            latest = args[args.length - 1];
        }
        switch (args.length) {
            case 0:
            case 1: {
                for (String sub : cmdTab) {
                    if (sender.hasPermission("ute." + sub))
                        list.add(sub);
                }
                break;
            }
            default: {
                switch (args[0].toLowerCase()) {
                    case "give": {
                        if (sender.hasPermission("ute.give")) {
                            switch (args.length) {
                                case 2:
                                    return null;
                                case 3: {
                                    list.addAll(itemTab);
                                    break;
                                }
                                default:
                                    return list;
                            }
                        }
                        break;
                    }
                    case "set": {
                        if (sender.hasPermission("ute.set")) {
                            switch (args.length) {
                                case 2:
                                    return null;
                                case 3: {
                                    list.addAll(capTab);
                                    break;
                                }
                                default:
                                    return list;
                            }
                        }
                        break;
                    }
                    case "season": {
                        if (sender.hasPermission("ute.season")) {
                            switch (args.length) {
                                case 2: {
                                    list.addAll(seasonTab);
                                    break;
                                }
                                case 4: {
                                    for (World w : Bukkit.getWorlds()) {
                                        list.add(w.getName());
                                    }
                                    break;
                                }
                                default:
                                    return list;
                            }
                        }
                        break;
                    }
                    case "role": {
                        if (Roles.isEnable)
                            if (sender.hasPermission("ute.role")) {
                                list.addAll(roleTab);
                            }
                        break;
                    }
                    case "guide": {
                        if (args.length == 2)
                            if (sender.hasPermission("ute.guide"))
                                if (sender.hasPermission("ute.guide.other")) {
                                    return null;
                                }
                        break;
                    }
                }
                break;
            }
        }
        filter(list, latest);
        return list;
    }

    private void filter(List<String> list, String latest) {
        if (list.isEmpty() || latest == null)
            return;
        String ll = latest.toLowerCase();
        list.removeIf(k -> !k.toLowerCase().startsWith(ll));
    }

}