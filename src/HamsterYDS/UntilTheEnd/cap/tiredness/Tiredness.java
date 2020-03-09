package HamsterYDS.UntilTheEnd.cap.tiredness;

import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class Tiredness {
	public static UntilTheEnd plugin;
	public Tiredness(UntilTheEnd plugin) {
		this.plugin=plugin;
		new ChangeTasks(plugin);
		plugin.getServer().getPluginManager().registerEvents(new ChangeEvents(), plugin);
	}
}
