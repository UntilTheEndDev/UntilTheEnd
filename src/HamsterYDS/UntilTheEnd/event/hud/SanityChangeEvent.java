package HamsterYDS.UntilTheEnd.event.hud;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class SanityChangeEvent extends PlayerEvent implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private ChangeCause cause;
	private double change;
	private boolean isCancelled;
	public SanityChangeEvent(Player player,ChangeCause cause,double change) {super(player);this.cause=cause;this.change=change;this.isCancelled=false;} 
	public ChangeCause getCause() {return this.cause;}
	public double getChange() {return this.change;}
	@Override public HandlerList getHandlers() {return handlers;}
	@Override public boolean isCancelled() {return isCancelled;}
	@Override public void setCancelled(boolean isCancelled) {this.isCancelled=isCancelled;}
	
	public enum ChangeCause{
		INVENTORYITEM,INVENTORYCLOTHES,CREATUREAURA,PLAYER,MONSTER,HUMIDITY,
		EVENING,NIGHT,USEWAND,FURROLL,STRAWROLL,SIESTALEANTO,DARKWARN,DARKATTACK;
	}
}
