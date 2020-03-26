package HamsterYDS.UntilTheEnd.api;

import java.util.Collection;

import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.item.UTEItemStack;

public class ItemApi {
    public static Collection<UTEItemStack> getItems() {
        return ItemManager.items.values();
    }

    public static UTEItemStack getItem(String id) {
        return ItemManager.items.get(id);
    }
}
