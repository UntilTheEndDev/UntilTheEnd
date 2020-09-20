package HamsterYDS.UntilTheEnd.guide;

import HamsterYDS.UntilTheEnd.Logging;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.BlockApi;
import HamsterYDS.UntilTheEnd.api.GuideApi;
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
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author 鍗楀涓朵粨榧�
 * @version V5.1.1
 */
public class CraftGuide implements Listener {
    public static UntilTheEnd plugin = UntilTheEnd.getInstance();
    public static int tot = 10;
    public static HashMap<String, ArrayList<Inventory>> helps = new HashMap<>();
    public static HashMap<ItemStack, Inventory> crafts = new HashMap<>();
    public static HashMap<String, ArrayList<Inventory>> playerInvs = new HashMap<>();
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
        GuideApi.addCategory("搂6鍩虹", Material.LEASH, (short) 0);
        GuideApi.addCategory("搂6琛ｇ墿", Material.GOLD_HELMET, (short) 0);
        GuideApi.addCategory("搂6鐢熷瓨", Material.IRON_PICKAXE, (short) 0);
        GuideApi.addCategory("搂6鎴樻枟", Material.GOLD_SWORD, (short) 0);
        GuideApi.addCategory("搂6榄旀硶", Material.SPLASH_POTION, (short) 0);
        GuideApi.addCategory("搂6绉戝", Material.REDSTONE_COMPARATOR, (short) 0);
    }

    public static ArrayList<UUID> openers = new ArrayList<>();

    @EventHandler()
    public void onOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getHolder() instanceof HolderCraftingHelp)
            openers.add(event.getPlayer().getUniqueId());
    }

    @EventHandler()
    public void onClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getHolder() instanceof HolderCraftingHelp) {
            ArrayList<Inventory> invs = playerInvs.get(event.getPlayer().getName());
            invs.add(inv);
            openers.remove(event.getPlayer().getUniqueId());
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
        playerInvs.put(player.getName(), new ArrayList<>());
    }

    @EventHandler()
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerInvs.remove(player.getName());
    }

    @EventHandler()
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        if (inv == null)
            return;
        if (openers.contains(player.getUniqueId())) {
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
        if (cheating.contains(player.getUniqueId())) {
            player.getInventory().addItem(event.getCurrentItem().clone());
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
        // //涓嬩竴椤�
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
                    //noinspection deprecation
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
        HashMap<Integer, ItemStack> slots = new HashMap<>();
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
        return playerLevel >= level;
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

    // 鑾峰彇涓�涓墿鍝�
    public static ItemStack getItem(String name, Material material, int data) {
        ItemStack item = new ItemStack(material);
        item.setDurability((short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    // 鑾峰彇涓�涓柊鐨勭被鍨婫UI锛堝鐗╁搧锛�
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

    // 鑾峰彇涓�鏁翠釜濂藉GUI
    public static ArrayList<Inventory> getTypeInventory() {
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(loadNew());
        return invs;
    }

    // 鍔犺浇涓�涓柊鐨勭墿鍝佸悎鎴愬睍绀篏UI
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
        ItemStack craft = getItem("鐐规垜鍚堟垚", Material.STAINED_GLASS_PANE, 9);
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
                    if (meta == null) {
                        // ???????
                        Logging.getLogger().warning(() -> "Your server core version seems too old, it will affect the normal operation of UTE");
                        Logging.getLogger().warning(() -> "浣犵殑鏈嶅姟鍣ㄦ牳蹇冪増鏈技涔庤繃鏃�, 灏嗕細褰卞搷UTE鐨勬甯歌繍琛�");
                        continue;
                    }
                    List<String> lores = meta.hasLore() ? meta.getLore() : new ArrayList<>();

                    lores.removeIf(line -> line.contains("缂哄皯鏈哄櫒"));

                    if (playerLevel + 1 == level) {
                        item.setDurability((short) 14);
                        lores.add("搂4缂哄皯鏈哄櫒: 搂d搂l" + ItemManager.items.get(ItemManager.machines.get(level)).displayName);
                    }
                    if (playerLevel + 1 < level) {
                        item.setDurability((short) 7);
                        lores.add("搂4缂哄皯鏈哄櫒: 搂c搂l鏈煡");
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
