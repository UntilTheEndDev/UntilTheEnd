package HamsterYDS.UntilTheEnd.papi;

import org.bukkit.World;
import org.bukkit.entity.Player;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi;
import me.clip.placeholderapi.external.EZPlaceholderHook;

public class UTEPapi extends EZPlaceholderHook {
	public UTEPapi(UntilTheEnd ute) {
        super(ute,"ute");
	}
    @Override
    public String onPlaceholderRequest(Player player,String var) {
    	if (player==null) return "";
        if (!Config.enableWorlds.contains(player.getWorld())) return "本世界不启用该功能";
    	if (var.equals("san")) return String.valueOf(UntilTheEndApi.PlayerApi.getValue(player,"san"));
        if (var.equals("hum")) return String.valueOf(UntilTheEndApi.PlayerApi.getValue(player,"hum"));
        if (var.equals("tem")) return String.valueOf(UntilTheEndApi.PlayerApi.getValue(player,"tem"));
        if (var.equals("season")) {
        	World world=player.getWorld();
        	return UntilTheEndApi.WorldApi.getSeason(world).name();
        }
        if (var.equals("day")) {
        	World world=player.getWorld();
        	return String.valueOf(UntilTheEndApi.WorldApi.getDay(world));
        }
        return "";
    }
}
