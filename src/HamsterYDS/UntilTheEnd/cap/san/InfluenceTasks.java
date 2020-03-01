package HamsterYDS.UntilTheEnd.cap.san;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

public class InfluenceTasks {
	public UntilTheEnd plugin;
	public static double disguiseRangeX=Sanity.yaml.getDouble("disguiseRangeX");
	public static double disguiseRangeY=Sanity.yaml.getDouble("disguiseRangeY");
	public static double disguiseRangeZ=Sanity.yaml.getDouble("disguiseRangeZ"); 
	public static int disguiseSanity=Sanity.yaml.getInt("disguiseSanity");
	public static int confusionSanity=Sanity.yaml.getInt("confusionSanity"); 
	public InfluenceTasks(UntilTheEnd plugin) {
		this.plugin=plugin;
		new CreatureDisguise().runTaskTimer(plugin,0L,1000L);
		new Confusion().runTaskTimer(plugin,0L,80L);
	}
	public class CreatureDisguise extends BukkitRunnable{
		public ArrayList<UUID> mobs=new ArrayList<UUID>();
		@Override
		public void run() {
			for(UUID uuid:mobs) {
				Entity entity=Bukkit.getEntity(uuid);
				if(entity==null) return;
				DisguiseAPI.undisguiseToAll(entity);
			} 
			for(World world:Config.enableWorlds) 
				for(Player player:world.getPlayers()) 
					if(PlayerManager.check(player.getName(),"san")<=disguiseSanity) 
						for(Entity entity:player.getNearbyEntities(disguiseRangeX,disguiseRangeY,disguiseRangeZ)) {
							DisguiseType type=DisguiseType.values()[(int) (DisguiseType.values().length*Math.random()-1)];
							MobDisguise disguise=new MobDisguise(type);
							DisguiseAPI.disguiseToPlayers(entity,disguise,player.getName());
							disguise.setEntity(entity);
							disguise.startDisguise();
							mobs.add(entity.getUniqueId());
						}
		}
	}
	public class Confusion extends BukkitRunnable {
		@Override
		public void run() {
			for(World world:Config.enableWorlds) 
				for(Player player:world.getPlayers()) {
					if(PlayerManager.check(player.getName(),"san")<=confusionSanity) {
						player.removePotionEffect(PotionEffectType.CONFUSION);
						player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,100,0));
					}
				}
		}
	}
//	if(san>120) continue;
//	if(san<=120) {
//		
//	}
}
