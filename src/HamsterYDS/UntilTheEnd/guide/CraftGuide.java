package HamsterYDS.UntilTheEnd.guide;

import java.util.*;

import HamsterYDS.UntilTheEnd.internal.UTEi18n;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.item.UTEItemStack;
import HamsterYDS.UntilTheEnd.player.PlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.api.GuideApi;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class CraftGuide implements Listener {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static int tot = 10;
    public static HashMap<String, ArrayList<Inventory>> helps = new HashMap<String, ArrayList<Inventory>>();
    public static HashMap<ItemStack, Inventory> crafts = new HashMap<ItemStack, Inventory>();
    public static HashMap<String, ArrayList<Inventory>> playerInvs = new HashMap<String, ArrayList<Inventory>>();
    public static ArrayList<UUID> cheating = new ArrayList<>();

    public static Inventory inv = Bukkit.createInventory(HolderCraftingHelp.INSTANCE, 45,
            UTEi18n.cache("item.guide.help.crafting.main"));

    public CraftGuide(UntilTheEnd plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        for (Player player : Bukkit.getOnlinePlayers())
            playerInvs.put(player.getName(), new ArrayList<Inventory>());
        new CraftGuide();
    }

    public CraftGuide() {
        loadNew();
        loadTypes();
        HolderCraftingHelp.INSTANCE.setCustomName(UTEi18n.cache("item.guide.help.crafting.main"));
    }

    public static void loadTypes() {
        ItemStack frame = getItem(UTEi18n.cache("item.guide.border"), Material.STAINED_GLASS_PANE, 15);
        for (int i = 0; i < 9; i++)
            inv.setItem(i, frame);
        inv.setItem(9, frame);
        inv.setItem(17, frame);
        inv.setItem(18, frame);
        inv.setItem(26, frame);
        inv.setItem(27, frame);
        inv.setItem(35, frame);
        for (int i = 36; i < 45; i++)
            inv.setItem(i, frame);
        GuideApi.addCategory("§6基础", Material.LEASH, (short) 0);
        GuideApi.addCategory("§6衣物", Material.GOLD_HELMET, (short) 0);
        GuideApi.addCategory("§6生存", Material.IRON_PICKAXE, (short) 0);
        GuideApi.addCategory("§6战斗", Material.GOLD_SWORD, (short) 0);
        GuideApi.addCategory("§6魔法", Material.SPLASH_POTION, (short) 0);
        GuideApi.addCategory("§6科学", Material.REDSTONE_COMPARATOR, (short) 0);
    }

    public static ArrayList<String> openers = new ArrayList<String>();

    @EventHandler()
    public void onOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getHolder() instanceof HolderCraftingHelp)
            openers.add(event.getPlayer().getName());
    }

    @EventHandler()
    public void onClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getHolder() instanceof HolderCraftingHelp) {
            ArrayList<Inventory> invs = playerInvs.get(event.getPlayer().getName());
            invs.add(inv);
            openers.remove(event.getPlayer().getName());
        }
    }

    @EventHandler()
    public void onDrag(InventoryDragEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getHolder() instanceof HolderCraftingHelp)
            event.setCancelled(true);
    }

    @EventHandler()
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerInvs.put(player.getName(), new ArrayList<Inventory>());
    }

    @EventHandler()
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerInvs.remove(player.getName());
    }

    @EventHandler()
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getClickedInventory();
        if (inv == null)
            return;
        if (openers.contains(player.getName())) {
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
                return;
            }
        }
        if (!(inv.getHolder() instanceof HolderCraftingHelp))
            return;
        event.setCancelled(true);
        if (event.getCurrentItem() == null)
            return;
        ItemStack item = event.getCurrentItem().clone();
        if (item.getItemMeta() == null)
            return;
        item.setAmount(1);
        if (cheating.contains(player.getUniqueId()))
            if (event.getClick() == ClickType.MIDDLE) {
                event.setCursor(event.getCurrentItem());
                return;
            }
        if (event.getSlot() == 8)
            player.openInventory(CraftGuide.inv);
        if (event.getSlot() == 0) {
            ArrayList<Inventory> invs = playerInvs.get(player.getName());
            if (invs.size() != 0) {
                player.openInventory(adaptInventory(invs.get(invs.size() - 1), player));
                invs.remove(invs.get(invs.size() - 1));
                playerInvs.remove(player.getName());
                playerInvs.put(player.getName(), invs);
            }
        }
        if (event.getSlot() == 20)
            return;
        if (event.getSlot() == 40) {
            String id = ItemManager.getUTEItemId(inv.getItem(20));
            if (!id.equalsIgnoreCase("")) {
                goCraft(player, id);
                return;
            }
        }
        // if(event.getSlot()==36) {
        // int index=helps.get(key);
        // }
        // if(event.getSlot()==45) {
        // //下一页
        // }
        if (item.getDurability() == 14 || item.getDurability() == 7) {
            player.sendMessage(UTEi18n.cacheWithPrefix("item.system.no-machine"));
            return;
        }
        Inventory find = find(item);
        if (find != null) {
            if (cheating.contains(player.getUniqueId()))
                if (find.getSize() == 27) {
                    event.setCancelled(true);
                    event.setCursor(item);
                    return;
                }
            ArrayList<Inventory> invs = playerInvs.get(player.getName());
            invs.add(inv);
            playerInvs.remove(player.getName());
            playerInvs.put(player.getName(), invs);
            player.openInventory(adaptInventory(find, player));
        }
    }

    public void goCraft(Player player, String id) {
        if (!hasCraftItems(player, id)) {
            player.sendMessage(UTEi18n.cacheWithPrefix("item.system.no-craft-items"));
            return;
        }
        if (!hasMachine(player, id)) {
            player.sendMessage(UTEi18n.cacheWithPrefix("item.system.no-machine"));
            return;
        }
        if (!PlayerManager.checkUnLockedRecipes(player).contains(id))
            PlayerManager.addUnLockedRecipes(player, id);
        UTEItemStack uteitem = ItemManager.items.get(id);
        PlayerInventory inv = player.getInventory();
        for (ItemStack material : uteitem.craft.keySet()) {
            int needAmount = uteitem.craft.get(material);

            HashMap<Integer, ItemStack> slots = getSimilarSlots(inv, material);

            for (int slot : slots.keySet()) {
                if (needAmount <= 0)
                    break;
                ItemStack item = slots.get(slot);
                int amount = item.getAmount();
                if (amount > needAmount) {
                    item.setAmount(amount - needAmount);
                    break;
                } else {
                    item.setAmount(0);
                    needAmount -= amount;
                }
                inv.setItem(slot, item);
                player.updateInventory();
            }
        }
        inv.addItem(uteitem.item);
        player.updateInventory();
    }

    private HashMap<Integer, ItemStack> getSimilarSlots(PlayerInventory inv, ItemStack material) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        for (int slot = 0; slot < inv.getSize(); slot++)
            if (ItemManager.isSimilar(inv.getItem(slot), material)) {
                slots.put(slot, inv.getItem(slot));
            }
        return slots;
    }

    private boolean hasCraftItems(Player player, String id) {
        if (player.getGameMode() == GameMode.CREATIVE) return true;
        UTEItemStack item = ItemManager.items.get(id);
        PlayerInventory inv = player.getInventory();
        for (ItemStack material : item.craft.keySet()) {
            int needAmount = item.craft.get(material);
            HashMap<Integer, ItemStack> slots = getSimilarSlots(inv, material);
            int amount = 0;
            for (int slot : slots.keySet())
                amount += slots.get(slot).getAmount();
            if (amount < needAmount)
                return false;
        }
        return true;
    }

    private boolean hasMachine(Player player, String id) {
        int level = ItemManager.items.get(id).needLevel;
        if (level == 0)
            return true;
        if (PlayerManager.checkUnLockedRecipes(player).contains(id))
            return true;
        int playerLevel = getNearbyMachine(player);
        if (playerLevel >= level) return true;
        return false;
    }

    private static int getNearbyMachine(Player player) {
        int maxLevel = 0;
        for (int i = -5; i <= 5; i++)
            for (int j = -5; j <= 5; j++)
                for (int k = -5; k <= 5; k++) {
                    Location newLoc = new Location(player.getWorld(), player.getLocation().getX() + i,
                            player.getLocation().getY() + j, player.getLocation().getZ() + k);
                    newLoc = newLoc.getBlock().getLocation();
                    for (int level : ItemManager.machines.keySet()) {
                        String machineId = ItemManager.machines.get(level);
                        if (machineId.equalsIgnoreCase(BlockApi.getSpecialBlock(newLoc))) {
                            maxLevel = Math.max(maxLevel, level);
                        }
                    }
                }
        return maxLevel;
    }

    private Inventory find(ItemStack stack) {
        final ItemMeta meta = stack.getItemMeta();
        for (Map.Entry<ItemStack, Inventory> ivs : crafts.entrySet()) {
            final ItemStack key = ivs.getKey();
            if (meta != null) {
                if (key.hasItemMeta()) {
                    final ItemMeta meta0 = key.getItemMeta();
                    if (Objects.equals(meta0.getDisplayName(), meta.getDisplayName())) {
                        return ivs.getValue();
                    }
                }
            } else {
                return ivs.getValue();
            }
        }
        return null;
    }

    public static void addItem(String string, ItemStack item) {
        ArrayList<Inventory> invs = helps.get(string);
        Inventory cinv = invs.get(invs.size() - 1);
        int index = 0;
        for (int slot = 0; slot < cinv.getSize(); slot++)
            if (cinv.getItem(slot) == null) {
                index = slot;
                break;
            }
        cinv.setItem(index, item);
        invs.set(invs.size() - 1, cinv);
        helps.remove(string);
        helps.put(string, invs);
    }

    // 获取一个物品
    public static ItemStack getItem(String name, Material material, int data) {
        ItemStack item = new ItemStack(material);
        item.setDurability((short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    // 获取一个新的类型GUI（多物品）
    public static Inventory loadNew() {
        ItemStack frame = getItem(UTEi18n.cache("item.guide.border"), Material.STAINED_GLASS_PANE, 15);
        Inventory inv = Bukkit.createInventory(HolderCraftingHelp.INSTANCE, 45,
                UTEi18n.cache("item.guide.help.crafting.main"));
        /**/
        for (int i = 0; i < 9; i++)
            inv.setItem(i, frame);
        inv.setItem(9, frame);
        inv.setItem(17, frame);
        inv.setItem(18, frame);
        inv.setItem(26, frame);
        inv.setItem(27, frame);
        inv.setItem(35, frame);
        for (int i = 36; i < 45; i++)
            inv.setItem(i, frame);
        /**/
        ItemStack last = getItem(UTEi18n.cache("item.guide.action.previous-page"), Material.STAINED_GLASS_PANE, 4);
        ItemStack next = getItem(UTEi18n.cache("item.guide.action.next-page"), Material.STAINED_GLASS_PANE, 11);
        inv.setItem(36, last);
        inv.setItem(44, next);
        ItemStack back = getItem(UTEi18n.cache("item.guide.action.previous"), Material.STAINED_GLASS_PANE, 6);
        ItemStack menu = getItem(UTEi18n.cache("item.guide.action.main"), Material.STAINED_GLASS_PANE, 9);
        inv.setItem(0, back);
        inv.setItem(8, menu);
        return inv;
    }

    // 获取一整个好多GUI
    public static ArrayList<Inventory> getTypeInventory() {
        ArrayList<Inventory> invs = new ArrayList<Inventory>();
        invs.add(loadNew());
        return invs;
    }

    // 加载一个新的物品合成展示GUI
    public static Inventory getCraftInventory() {
        ItemStack frame = getItem(UTEi18n.cache("item.guide.border"), Material.STAINED_GLASS_PANE, 15);
        Inventory craftInv = Bukkit.createInventory(HolderCraftingHelp.INSTANCE, 45,
                UTEi18n.cache("item.guide.help.crafting.main"));
        for (int i = 0; i < 9; i++)
            craftInv.setItem(i, frame);
        craftInv.setItem(9, frame);
        craftInv.setItem(17, frame);
        craftInv.setItem(18, frame);
        craftInv.setItem(26, frame);
        craftInv.setItem(27, frame);
        craftInv.setItem(35, frame);
        for (int i = 36; i < 45; i++)
            craftInv.setItem(i, frame);
        ItemStack craft = getItem("点我合成", Material.STAINED_GLASS_PANE, 9);
        craftInv.setItem(40, craft);
        return craftInv;
    }

    public static Inventory adaptInventory(Inventory oldInv, Player player) {
        Inventory inv = Bukkit.createInventory(oldInv.getHolder(), oldInv.getSize(), findTitle(oldInv));
        for (int slot = 0; slot < oldInv.getSize(); slot++) {
            if (oldInv.getItem(slot) == null) continue;
            ItemStack item = oldInv.getItem(slot).clone();
            String id = ItemManager.getUTEItemId(item);
            if (cheating.contains(player.getUniqueId()) || id.equalsIgnoreCase("")) {
                inv.setItem(slot, item);
                continue;
            }
            UTEItemStack uteitem = ItemManager.items.get(id);
            boolean unlocked = PlayerManager.checkUnLockedRecipes(player).contains(id);
            if (!unlocked) {
                int level = uteitem.needLevel;
                int playerLevel = getNearbyMachine(player);
                item.setType(Material.STAINED_GLASS_PANE);
                if (playerLevel >= level)
                    item.setDurability((short) 13);
                else {
                    ItemMeta meta = item.getItemMeta();
                    List<String> lores = new ArrayList<String>();
                    if (meta.hasLore()) lores = meta.getLore();

                    boolean flag = true;
                    lores.removeIf(line -> line.contains("缺少机器"));

                    if (playerLevel + 1 == level) {
                        item.setDurability((short) 14);
                        if (flag)
                            lores.add("§4缺少机器: §d§l" + ItemManager.items.get(ItemManager.machines.get(level)).displayName);
                    }
                    if (playerLevel + 1 < level) {
                        item.setDurability((short) 7);
                        if (flag)
                            lores.add("§4缺少机器: §c§l未知");
                    }
                    meta.setLore(lores);
                    item.setItemMeta(meta);
                }
            }
            inv.setItem(slot, item);
        }
        return inv;
    }

    private static String findTitle(Inventory oldInv) {
        try {
            return oldInv.getTitle();
        } catch (NoSuchMethodError ignore) {
            final InventoryHolder holder = oldInv.getHolder();
            if (holder instanceof UTEInvHolder) return ((UTEInvHolder) holder).getCustomName();
            return "<unknown title>";
        }
    }
}
