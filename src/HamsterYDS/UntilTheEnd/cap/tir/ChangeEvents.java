package HamsterYDS.UntilTheEnd.cap.tir;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;

public class ChangeEvents implements Listener {
	public UntilTheEnd plugin;

	public ChangeEvents(UntilTheEnd plugin) {
		this.plugin = plugin;
	}

	public static ArrayList<UUID> movingPlayers = new ArrayList<UUID>();

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		movingPlayers.add(player.getUniqueId());
		new BukkitRunnable() {

			@Override
			public void run() {
				movingPlayers.remove(movingPlayers.lastIndexOf(player.getUniqueId()));
				cancel();
			}

		}.runTaskLaterAsynchronously(plugin, 60L);
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.teleport"));
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player == null)
			return;
		PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.break"));
	}

	@EventHandler
	public void onTalk(PlayerChatEvent event) {
		Player player = event.getPlayer();
		if (player == null)
			return;
		PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.talk"));
	}

	@EventHandler
	public void onTab(PlayerChatTabCompleteEvent event) {
		Player player = event.getPlayer();
		if (player == null)
			return;
		PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.tab"));
	}

	@EventHandler 
	public void onAttack(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player){
			Player player=(Player) event.getDamager();
			PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.damage"));
		}
		if(event.getEntity() instanceof Player) {
			if(!(event.getDamager() instanceof LivingEntity)) return;
			Player player=(Player) event.getEntity();
			PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.beDamaged"));
		}
	}
	
	@EventHandler
	public void onPick(EnchantItemEvent event) {
		Player player=event.getEnchanter();
		PlayerManager.change(player, CheckType.TIREDNESS, Tiredness.yaml.getInt("change.event.enchant"));
	}
}
