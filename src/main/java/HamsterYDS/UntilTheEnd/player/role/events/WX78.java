package HamsterYDS.UntilTheEnd.player.role.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.internal.DisableManager;
import HamsterYDS.UntilTheEnd.internal.EventHelper;
import HamsterYDS.UntilTheEnd.item.ItemManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager;
import HamsterYDS.UntilTheEnd.player.PlayerManager.CheckType;
import HamsterYDS.UntilTheEnd.player.role.Roles;

public class WX78 implements Listener {
	//抗雷
	@EventHandler
	public void onLightned(EntityDamageEvent event) {
		Entity entity=event.getEntity();
		if(entity instanceof Player) {
			Player player=(Player) entity;
			if(PlayerManager.checkRole(player)==Roles.WX78) {
				event.setDamage(0.0);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,3600,2));
				player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,3600,2));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
    public void onRight(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(PlayerManager.checkRole(player)!=Roles.WX78) return;
        if (!Config.enableWorlds.contains(player.getWorld())) return;
        if (event.isCancelled() && !DisableManager.bypass_right_action_cancelled) return;
        if (!event.hasItem()) return;
        if (!EventHelper.isRight(event.getAction())) return;
        ItemStack item = event.getItem();
        if (ItemManager.isSimilar(item, ItemManager.items.get("Gear").item)) {
            event.setCancelled(true);
            //常数放进Config可设置
            /*
             * 0.1：升级概率
             * 5：最高等级
             * 语言
            */
            if(PlayerManager.check(player,CheckType.LEVEL)<=5) {
            	player.sendMessage("§6[§cUntilTheEnd§6]§r 已经达到最高等级！");
            	event.setCancelled(false);
            	return;
        	}
            if(Math.random()<=0.1) {
	            PlayerManager.change(player,CheckType.LEVEL,
	            		PlayerManager.check(player,CheckType.LEVEL)+1);
	            PlayerManager.change(player,CheckType.DAMAGELEVEL,
	            		PlayerManager.check(player,CheckType.DAMAGELEVEL)+0.2);
	            player.sendMessage("§6[§cUntilTheEnd§6]§r 升级成功，您现在的等级：§l"+PlayerManager.check(player,CheckType.LEVEL));
            }
        }
    }
}
