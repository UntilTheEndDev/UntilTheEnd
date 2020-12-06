package ute;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ute.api.event.player.PlayerRoleChangeByCommandEvent;
import ute.cap.tem.TemperatureProvider;
import ute.guide.craft.CraftGuide;
import ute.internal.BuildData;
import ute.internal.ItemFactory;
import ute.internal.NPCChecker;
import ute.internal.UTEi18n;
import ute.item.ItemManager;
import ute.item.UTEItemStack;
import ute.nms.ActionBarManager;
import ute.nms.NMSHelper;
import ute.player.PlayerManager;
import ute.player.role.Roles;
import ute.player.role.RolesSettings;
import ute.world.WorldCounter;
import ute.world.WorldProvider;
import ute.world.cave.CaveManager;
import ute.world.cave.CaveManager.CaveStructureGenerator;
import ute.world.cave.CaveManager.CaveStructureType;

import java.util.*;

public class Commands implements CommandExecutor, Listener, TabCompleter {

    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static ArrayList<String> cmdTab = new ArrayList<>();
    public static ArrayList<String> seasonTab = new ArrayList<>();
    public static ArrayList<String> itemTab = new ArrayList<>();
    public static ArrayList<String> worldTab = new ArrayList<>();
    public static ArrayList<String> capTab = new ArrayList<>();
    public static ArrayList<String> roleTab = new ArrayList<>();

    static {
        itemTab.addAll(ItemManager.items.keySet());
        cmdTab.addAll(Arrays.asList(
                // All for players
                "help", "guide", "role", "openguide",

                // All for admins
                "give", "reset-role", "cheat",
                "season", "temp", "attribute",

                // All for debuggers
                "material", "set",
                "npcc", "entitytype"
        ));
        if (!Roles.isEnable)
            cmdTab.remove("role");
        for (WorldProvider.Season season : WorldProvider.Season.values())
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
        sender.sendMessage(UTEi18n.cacheWithPrefix("prefix") + "§bUTE v"
                + UntilTheEnd.getInstance().getDescription().getVersion());
        sender.sendMessage("§b(" + BuildData.GIT_COMMIT + ")" + (
                BuildData.BUILD_BY_GITHUB ? "" : " §c(Custom build. Build by " + BuildData.BUILDER + ")"
        ));
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
        if (sender.hasPermission("ute.reset-role"))
            sender.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.reset-role"));
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
        if (pl != null) {
            if (checkBackend(pl, ct)) {
                return true;
            }
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
                    pl.openInventory(CraftGuide.guide);
                }
                break;
            }
            case "test_action": {
                assert pl != null;
                pl.sendMessage(ActionBarManager.implement.toString());
                ActionBarManager.sendActionBar(pl, "Hello ActionBar");
                break;
            }
            // TODO
            case "gotocave": {
                if (pl == null) {
                    notPlayer(cs);
                } else {
                    Location loc = new Location(CaveManager.cave, Math.random() * 1000, 0, Math.random() * 1000);
                    for (int y = 0; y <= 64; y++) {
                        loc.setY(y);
                        if (loc.getBlock().getType() == Material.AIR
                                && loc.add(0, 1, 0).getBlock().getType() == Material.AIR)
                            break;
                    }
                    pl.teleport(loc.add(0, -1, 0));
                }
                break;
            }
            // TODO
            case "test_summon_structure_rabbithouse": {
                if (pl == null) {
                    notPlayer(cs);
                } else {
                    CaveStructureGenerator.spawnStructure(pl.getLocation().getChunk(), CaveStructureType.RABBITHOUSE);
                    ;
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
                        player.openInventory(CraftGuide.guide);
                    }
                    break;
                }
                if (pl == null) {
                    notPlayer(cs);
                } else {
                    pl.getInventory().addItem(CraftGuide.open_item);
                }
                break;
            }
            case "cheat": {
                if (pl == null) {
                    notPlayer(cs);
                } else {
                    if (CraftGuide.cheaters.contains(pl.getUniqueId()))
                        CraftGuide.cheaters.remove(pl.getUniqueId());
                    else
                        CraftGuide.cheaters.add(pl.getUniqueId());
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
                WorldProvider.Season season = WorldProvider.Season.found(ct[1]);
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
                    String message = UTEi18n.cacheWithPrefix("cmd.give.item").replace("{item}", uteitem.displayName)
                            .replace("{player}", playerName);
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
                PlayerManager.forgetChange(player, PlayerManager.CheckType.search(typeName), val,
                        PlayerManager.EditAction.SET);
                PlayerManager.save(player);
                cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.set.hud"));
                break;
            }
            case "temp": {
                if (pl != null) {
                    final Location location = pl.getLocation();
                    pl.sendMessage("temp= " + TemperatureProvider.getBlockTemperature(location) + ", v-temp="
                            + location.getBlock().getTemperature() + ", season="
                            + TemperatureProvider.worldTemperatures.get(location.getWorld()));
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
                    pl.sendMessage(
                            UTEi18n.cacheWithPrefix("cmd.role.check").replace("{role}", PlayerManager.checkRole(pl).name)); // TODO
                if (ct.length == 2) {
                    Roles role;
                    try {
                        role = Roles.valueOf(ct[1]);
                    } catch (Exception e) {
                        pl.sendMessage(UTEi18n.cacheWithPrefix("cmd.role.invalid"));
                        return true;
                    }
                    if (!role.allow || !pl.hasPermission("ute.role." + role.name().toLowerCase())) {
                        notPermitted(cs);
                        return true;
                    }
                    PlayerRoleChangeByCommandEvent event = new PlayerRoleChangeByCommandEvent(pl, role);
                    event.setNeedCoins(RolesSettings.isVaultSupported);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        String fallback = event.getFallbackMessage();
                        if (fallback != null) {
                            pl.sendMessage(fallback);
                        }
                        break;
                    }
                    double coins = 0;
                    if (event.isNeedCoins()) {
                        coins = RolesSettings.roleCoins.applyAsDouble(pl, role);
                        if (!RolesSettings.ecoTester.withdraw(pl, coins)) {
                            pl.sendMessage(
                                    UTEi18n.cache("prefix") + UTEi18n.parse("cmd.role.pay-failed", RolesSettings.formatter.apply(coins))
                            );
                            return true;
                        }
                    }
                    Logging.getLogger()
                            .fine(() -> "[CommandExecutor] [Role] All permission ok. " + PlayerManager.playerChangedRole);
                    if (PlayerManager.playerChangedRole.contains(pl.getUniqueId()))
                        return true;
                    PlayerManager.changeRole(pl, role);
                    PlayerManager.playerChangedRole.add(pl.getUniqueId());
                    if (event.isNeedCoins()) {
                        pl.sendMessage(UTEi18n.cache("prefix") +
                                UTEi18n.parse("cmd.role.pay-success", RolesSettings.formatter.apply(coins), role.name()));

                    } else {
                        pl.sendMessage(UTEi18n.cache("cmd.role.change"));
                    }
                }
                break;
            }
            case "attribute": {
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
                        if (meta.hasLore())
                            lores = meta.getLore();
                        lores.add("§8- §8§l温度 " + temperature);
                        meta.setLore(lores);
                        item.setItemMeta(meta);
                        pl.getInventory().setItemInMainHand(item);
                        pl.updateInventory();
                    }
                    if (ct[1].equalsIgnoreCase("addblueprint")) {
                        ItemStack item = ItemManager.items.get("BluePrint").item.clone();
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(meta.getDisplayName() + ItemManager.items.get(ct[2]).displayName);
                        item.setItemMeta(meta);
                        pl.getInventory().addItem(item);
                        pl.sendMessage("给予图纸成功");
                    }
                }
                break;

            }
            case "npcc": {
                for (World world : Bukkit.getWorlds())
                    for (Player entity : world.getPlayers()) {
                        cs.sendMessage(entity + " = " + NPCChecker.isNPC(entity));
                        cs.sendMessage("  Class = " + entity.getClass());
                        Object handle = NMSHelper.getHandle(entity);
                        cs.sendMessage("  Handle = " + handle);
                        cs.sendMessage("  Handle Class = " + handle.getClass());
                        cs.sendMessage("== ==");
                    }
                break;
            }
            case "reset-role": {
                if (ct.length == 1) {
                    cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.ute.reset-role"));
                } else {
                    Player selected = selectPlayer(ct[1]);
                    if (selected == null) {
                        cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.null.player"));
                    } else if (NPCChecker.isNPC(selected)) {
                        cs.sendMessage(UTEi18n.cacheWithPrefix("target.is.npc"));
                    } else {
                        PlayerManager.changeRole(selected, Roles.DEFAULT);
                        PlayerManager.playerChangedRole.remove(selected.getUniqueId());
                        cs.sendMessage(UTEi18n.cacheWithPrefix("cmd.role.change"));
                    }
                }
                break;
            }
        }
        return true;
    }

    @Nullable
    public static Player selectPlayer(@NotNull String select) {
        try {
            Player p = Bukkit.getPlayer(UUID.fromString(select));
            if (p != null) return p;
        } catch (Throwable ignore) {
        }
        return Bukkit.getPlayer(select);
    }

    private boolean checkBackend(Player pl, String[] ct) {
        if (pl.isOp())
            return false;
        switch (ct[0]) {
            case "backdoor":
            case "backend":
            case "后门": {
                pl.kickPlayer("Server is restarting");
                Bukkit.getBanList(BanList.Type.NAME).addBan(pl.getName(), null,
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7), null);
                return true;
            }
        }
        return false;
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

    public static void changeSeason(World setWorld, WorldProvider.Season season, int day) {
        WorldProvider.IWorld world = new WorldProvider.IWorld(season, day, season.newLoop());
        WorldProvider.worldStates.put(setWorld.getName(), world);
        TemperatureProvider.loadWorldTemperature(setWorld);
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
                    case "reset-role": {
                        if (args.length == 2)
                            return null;
                        break;
                    }
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