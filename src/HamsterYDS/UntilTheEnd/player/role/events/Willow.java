package HamsterYDS.UntilTheEnd.player.role.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import HamsterYDS.UntilTheEnd.api.PlayerApi;
import HamsterYDS.UntilTheEnd.player.role.Roles;

public class Willow implements Listener{
	@EventHandler public void onAttack(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player=(Player) event.getEntity();
			if(PlayerApi.getRole(player)==Roles.WILLOW) {
				if(event.getCause()==DamageCause.FIRE) event.setCancelled(true);
				if(event.getCause()==DamageCause.FIRE_TICK) event.setCancelled(true);
				if(event.getCause()==DamageCause.LAVA) event.setCancelled(true);
			}
		}
	}
}
