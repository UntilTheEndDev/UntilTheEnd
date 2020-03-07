package HamsterYDS.UntilTheEnd.entity.neutral;

import org.bukkit.Location;

import HamsterYDS.UntilTheEnd.entity.EntityManager;
import net.minecraft.server.v1_12_R1.EntityPigZombie;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;

public class PigMan{
	public static void spawnPigMan(Location loc) {
		EntityPigZombie instance=new EntityPigZombie(EntityManager.world);
		instance.setCustomName("§6猪人");
		instance.setLocation(loc.getX(),loc.getY(),loc.getZ(),0f,0f);
		instance.goalSelector=new PathfinderGoalSelector(EntityManager.world.methodProfiler);
		instance.targetSelector.a(2,new PathfinderGoalNearestAttackableTarget<EntityPlayer>(instance,EntityPlayer.class,true));
	}
}
