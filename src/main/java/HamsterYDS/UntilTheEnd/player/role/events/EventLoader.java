package HamsterYDS.UntilTheEnd.player.role.events;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class EventLoader {
    public EventLoader(UntilTheEnd plugin) {
        plugin.getServer().getPluginManager().registerEvents(new GeneralEvents(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new Willow(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new WX78(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new Wendy(), plugin);
    }
}
