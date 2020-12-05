package ute.guide;

import ute.UntilTheEnd;
import ute.api.GuideApi;
import ute.api.ItemApi;
import ute.guide.event.OperateLimiter;
import ute.internal.UTEi18n;
import ute.item.ItemManager;
import ute.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CraftGuide implements Listener {
    public static Inventory guide = Bukkit.createInventory(new HolderMainHelp(), 45, "");
    public static HashMap<ItemStack, Inventory> button_to_gui = new HashMap<ItemStack, Inventory>();
    public static ArrayList<UUID> cheaters = new ArrayList<UUID>();

    public static ItemStack open_item = GuideApi.item_creater("§6§lUntilTheEnd指导", Material.ENCHANTED_BOOK, (short) 0);
    public static ItemStack guide_frame = GuideApi.item_creater("§8FRAME", Material.STAINED_GLASS_PANE, (short) 15);
    public static ItemStack back_button = GuideApi.item_creater("§e返回上一层", Material.STAINED_GLASS_PANE, (short) 6);
    public static ItemStack main_guide_button = GuideApi.item_creater("§e返回菜单", Material.STAINED_GLASS_PANE, (short) 9);
    public static ItemStack craft_button = GuideApi.item_creater("点我合成该物品", Material.STAINED_GLASS_PANE, (short) 9);


    public static void init() {
        guide = init_frame(guide, guide_frame);
        guide.setItem(0, back_button);
        for (GuideApi.AvaliableCategories category : GuideApi.AvaliableCategories.values()) {
            guide.setItem(category.slot_id, category.button);
            button_to_gui.put(category.button, get_simple_category_guide(category.button.getItemMeta().getDisplayName()));
        }
        button_to_gui.put(main_guide_button, guide);
        Bukkit.getPluginManager().registerEvents(new CraftGuide(), UntilTheEnd.getInstance());
        Bukkit.getPluginManager().registerEvents(new OperateLimiter(), UntilTheEnd.getInstance());
    }

    @EventHandler
    public void on_open_guide(PlayerInteractEvent event) {
        if (event.hasItem()) {
            ItemStack item = event.getItem();
            if (item.equals(open_item)) {
                Player player = event.getPlayer();
                player.openInventory(guide);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on_open_guide(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof HolderCategoryHelp
                || event.getInventory().getHolder() instanceof HolderItemCraftingHelp
                || event.getInventory().getHolder() instanceof HolderMainHelp) {
            OperateLimiter.openers.add(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void on_close_guide(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof HolderCategoryHelp
                || event.getInventory().getHolder() instanceof HolderItemCraftingHelp
                || event.getInventory().getHolder() instanceof HolderMainHelp) {
            OperateLimiter.openers.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void on_click(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (event.getCurrentItem() == null) return;
        if (event.getInventory().getHolder() instanceof HolderCategoryHelp
                || event.getInventory().getHolder() instanceof HolderItemCraftingHelp
                || event.getInventory().getHolder() instanceof HolderMainHelp) {
            Player player = (Player) event.getWhoClicked();
            if (cheaters.contains(player.getUniqueId())) {
                if (!ItemManager.getUTEItemId(event.getCurrentItem()).equalsIgnoreCase("")) {
                    player.getInventory().addItem(event.getCurrentItem());
                    player.updateInventory();
                    return;
                }
            }
            if (event.getSlot() == 8){
                player.openInventory(guide);
                return;
            }
            if (event.getSlot() == 0) {
                ItemStack item = inv.getItem(event.getSlot());
                if (item.getItemMeta().hasLore()) {

                    Inventory adapted_inv;
                    ArrayList<String> lores = (ArrayList<String>) item.getItemMeta().getLore();
                    String lore = lores.get(lores.size() - 1);
                    if (ItemManager.items.containsKey(lore)) {
                        adapted_inv = button_to_gui.get(ItemManager.items.get(lore).item);
                    } else if (lore.equalsIgnoreCase("main")) {
                        adapted_inv = guide;
                    } else
                        adapted_inv = button_to_gui.get(GuideApi.AvaliableCategories.valueOf(lore).button);
                    ItemStack back_button = adapted_inv.getItem(0);
                    ItemMeta back_meta = back_button.getItemMeta();
                    lores.remove(lores.size() - 1);
                    back_meta.setLore(lores);
                    back_button.setItemMeta(back_meta);
                    adapted_inv.setItem(0, back_button);

                    player.openInventory(adapted_inv);
                }
                return;
            }
            ItemStack one_item = event.getCurrentItem().clone();
            one_item.setAmount(1);
            if (button_to_gui.containsKey(one_item)) {
                Inventory adapted_inv = GuideApi.copy_inventory(button_to_gui.get(one_item));

                ItemStack item = adapted_inv.getItem(0);
                ItemMeta meta = item.getItemMeta();
                ArrayList<String> lores;
                if (inv.getItem(0).getItemMeta().hasLore())
                    lores = (ArrayList<String>) inv.getItem(0).getItemMeta().getLore();
                else
                    lores = new ArrayList<String>();

                if (inv.getHolder() instanceof HolderMainHelp)
                    lores.add("main");
                if (inv.getHolder() instanceof HolderCategoryHelp)
                    lores.add(inv.getTitle());
                if (inv.getHolder() instanceof HolderItemCraftingHelp)
                    lores.add(ItemManager.getUTEItemId(inv.getItem(19)));
                meta.setLore(lores);
                item.setItemMeta(meta);
                adapted_inv.setItem(0, item);

                player.openInventory(adapted_inv);
                return;
            }
            if (inv.getHolder() instanceof HolderItemCraftingHelp && event.getSlot() == 40) {
                ItemStack result = inv.getItem(19);
                String result_id = ItemManager.getUTEItemId(result);
                switch (ItemApi.can_craft(player, result_id)) {
                    case 0: {
                        if (!PlayerManager.checkUnLockedRecipes(player).contains(result_id))
                            PlayerManager.addUnLockedRecipes(player, result_id);
                        ItemApi.go_craft(player, result_id);
                        break;
                    }
                    case 1:
                        player.sendMessage(UTEi18n.cacheWithPrefix("item.system.no-craft-items"));
                        break;
                    case 2:
                        player.sendMessage(UTEi18n.cacheWithPrefix("item.system.no-machine"));
                        break;
                }
            }
        }
    }

    public static Inventory get_simple_category_guide(String title) {
        Inventory guide = Bukkit.createInventory(new HolderCategoryHelp(), 45, title);
        guide = init_frame(guide, guide_frame);
        guide.setItem(0, back_button);
        guide.setItem(8, main_guide_button);
        return guide;
    }

    public static Inventory get_simple_craft_guide(String title) {
        Inventory guide = Bukkit.createInventory(new HolderItemCraftingHelp(), 45, title);
        guide = init_frame(guide, guide_frame);
        guide.setItem(13, guide_frame);
        guide.setItem(22, guide_frame);
        guide.setItem(31, guide_frame);
        guide.setItem(0, back_button);
        guide.setItem(40, craft_button);
        return guide;
    }

    public static Inventory init_frame(Inventory inv, ItemStack frame) {
        int size = inv.getSize();
        for (int slot_id = 0; slot_id < size; slot_id++) {
            if (slot_id <= 8 || slot_id >= size - 9 || slot_id % 9 == 0 || slot_id % 9 == 8) {
                inv.setItem(slot_id, frame);
            }
        }
        return inv;
    }
}