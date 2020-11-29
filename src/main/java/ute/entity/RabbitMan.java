package ute.entity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import ute.UntilTheEnd;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.Navigation;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStrollLand;

public class RabbitMan extends EntityZombie {
	private static MinecraftKey minecraftKey;
	static {
		minecraftKey = new MinecraftKey("ute_rabbitman"); // minecraft:ute_rabbitman
		register();
	}

	public static void summonRabbitMan(Location location, SpawnReason reason) {
		RabbitMan entity = new RabbitMan(location);
		CraftWorld craftWorld = (CraftWorld) location.getWorld();
		craftWorld.addEntity(entity, reason);
	}

	public RabbitMan(Location location) {
		super(((CraftWorld) location.getWorld()).getHandle());
		this.setCustomName("§6兔人");
		this.setCustomNameVisible(true);
		this.setPosition(location.getX(), location.getY(), location.getZ());
	}

	// 实体注册
	public static void register() {
		EntityTypes.d.add(minecraftKey);
		EntityTypes.b.a(10501, minecraftKey, RabbitMan.class);
	}

	// 取消注册
	public static void unregister() {
		EntityTypes.d.remove(minecraftKey);
		MinecraftKey oldKey = EntityTypes.b.b(EntityZombie.class);
		EntityTypes.b.a(10501, oldKey, EntityZombie.class);
	}

	// AI TODO
	@Override
	protected void r() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(4, new PathfinderGoalWalkToFood(this, 1.0D));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	}
	
	//跟从拿着胡萝卜的玩家 & 素食主义者I-攻击携带肉类的玩家 TODO
	public class PathfinderGoalAttackPlayer extends PathfinderGoal {
		private double speed;
		private EntityInsentient entity;
		private Location loc;
		private Navigation navigation;

		public PathfinderGoalAttackPlayer(EntityInsentient entity, double speed) {
			this.entity = entity;
			this.navigation = (Navigation) this.entity.getNavigation();
			this.speed = speed;
			this.loc = new Location(entity.getWorld().getWorld(), entity.locX, entity.locY, entity.locZ);
		}

		@Override
		public boolean a() {
			return true;
		}

		@Override
		public boolean b() {
			return false;
		}
		
		
	}
	//自动找吃的 & 素食主义者II-销毁一切肉类
	public class PathfinderGoalWalkToFood extends PathfinderGoal {
		private double speed;
		private EntityInsentient entity;
		private Location loc;
		private Navigation navigation;

		public PathfinderGoalWalkToFood(EntityInsentient entity, double speed) {
			this.entity = entity;
			this.navigation = (Navigation) this.entity.getNavigation();
			this.speed = speed;
			this.loc = new Location(entity.getWorld().getWorld(), entity.locX, entity.locY, entity.locZ);
		}

		@Override
		public boolean a() {
			return true;
		}

		@Override
		public boolean b() {
			return false;
		}

		@Override
		public void c() {
			for (Entity nearbyEntity : loc.getWorld().getNearbyEntities(loc, 10.0, 5.0, 10.0)) {
				if (nearbyEntity instanceof Item) {
					Item item = (Item) nearbyEntity;
					ItemStack stack = item.getItemStack();
					if (stack.getType().isEdible()) {
						Location itemLoc = item.getLocation();
						this.navigation.a(itemLoc.getX(), itemLoc.getY(), itemLoc.getZ(), speed);
						boolean isMeat = stack.getType().toString().contains("PORK")
								|| stack.getType().toString().contains("BEEF")
								|| stack.getType().toString().contains("RABBIT");
						if (isMeat)
							this.entity.setCustomName("§6§l可恶的肉食！");
						else
							this.entity.setCustomName("§6§l那是什么？");

						new BukkitRunnable() {
							@Override
							public void run() {
								if (item.isDead()) {
									cancel();
									return;
								}
								if (item.getLocation().distance(new Location(entity.getWorld().getWorld(), entity.locX,
										entity.locY, entity.locZ)) <= 1) {
									if (isMeat)
										entity.setCustomName("§6§l恶心的味道！");
									else
										entity.setCustomName("§6§l真好吃！");

									new BukkitRunnable() {

										@Override
										public void run() {
											entity.setCustomName("§6兔人");
										}

									}.runTaskLater(UntilTheEnd.getInstance(), 20L);
									item.remove();
									cancel();
									return;
								}
							}
						}.runTaskTimer(UntilTheEnd.getInstance(), 0L, 5L);
					}
				}
			}
		}
	}
}