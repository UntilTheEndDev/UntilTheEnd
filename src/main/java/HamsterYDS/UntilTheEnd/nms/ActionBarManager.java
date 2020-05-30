package HamsterYDS.UntilTheEnd.nms;

import org.bukkit.entity.Player;

public abstract class ActionBarManager {
    public static final ActionBarManager implement;

    public static void initialize() {
        ActionBarManager.class.getClassLoader();
    }

    static {
        implement = ReflectionImplAllocator.allocate("ActionBarManagerImpl", ActionBarManager.class);
    }

    protected abstract void sendActionBar0(Player player, String line);

    public static void sendActionBar(Player player, String line) {
        implement.sendActionBar0(player, line);
    }
}
