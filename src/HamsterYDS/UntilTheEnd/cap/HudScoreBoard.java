package HamsterYDS.UntilTheEnd.cap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.api.UntilTheEndApi.PlayerApi;

public class HudScoreBoard extends BukkitRunnable{
	@Override
	public void run() {
		for(World world:Config.enableWorlds)
			for(Player player:world.getPlayers()) {
				Scoreboard board=Bukkit.getServer().getScoreboardManager().getNewScoreboard();
				Objective object=board.registerNewObjective("ute","ute");
			    object.setDisplayName(PlayerApi.getPAPI(player,HudProvider.yaml.getString("scoreboard.title")));
			    object.setDisplaySlot(DisplaySlot.SIDEBAR);
			    for(String line:HudProvider.lines) {
			    	String newLine=PlayerApi.getPAPI(player,line);
			    	object.getScore(newLine).setScore(HudProvider.lines.size()-HudProvider.lines.indexOf(line));
			    }
			    player.setScoreboard(board);
			}
	}
}
