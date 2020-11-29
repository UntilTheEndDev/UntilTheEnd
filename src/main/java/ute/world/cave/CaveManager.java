package ute.world.cave;

import ute.UntilTheEnd;
import ute.internal.DisableManager;
import ute.world.cave.structure.RabbitHouse;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.HashMap;
import java.util.Random;

public class CaveManager implements Listener {
	public static World cave;

	public static void initialize() {
		if (DisableManager.root.getBoolean("disable_cave_world", true))
			return;
		Bukkit.getPluginManager().registerEvents(new CaveManager(), UntilTheEnd.getInstance());
		Bukkit.getPluginManager().registerEvents(new CaveListener(), UntilTheEnd.getInstance());
		cave = Bukkit.createWorld(new WorldCreator("UTECave").generator(new CaveChunkGenerator()));
		cave.setDifficulty(Difficulty.HARD);
		cave.setGameRuleValue("KeepInventory", "true");

		new CaveEvents();
	}

	public static class CaveListener implements Listener {
		@EventHandler
		public void onWorldInit(WorldInitEvent event) {
			if (event.getWorld().getName().equals("UTECave")) {
				event.getWorld().getPopulators().add(new CaveTreePopulator());
				event.getWorld().getPopulators().add(new CaveStructureGenerator());
			}
		}
	}

	static class CaveTreePopulator extends BlockPopulator {
		@Override
		public void populate(World world, Random random, Chunk chunk) {
			final int maxn = 16;
			for (int i = 0; i < 3; i++) {
				int x = random.nextInt(maxn), z = random.nextInt(maxn);
				for (int y = 0; y <= 30; y++) {
					if (chunk.getBlock(x, y, z).getType() == Material.DIRT
							&& chunk.getBlock(x, y + 1, z).getType() == Material.AIR) {
						if (Math.random() <= 0.5)
							world.generateTree(chunk.getBlock(x, y + 1, z).getLocation(), TreeType.RED_MUSHROOM);
						else
							world.generateTree(chunk.getBlock(x, y + 1, z).getLocation(), TreeType.BROWN_MUSHROOM);
						for (int k = y; k <= y * 10; k++) {
							if (chunk.getBlock(x, k, z).getType().toString().contains("HUGE_MUSHROOM_1")
									&& chunk.getBlock(x, k + 1, z).getType() == Material.AIR)
								chunk.getBlock(x, k + 1, z).setType(Material.GLOWSTONE);
							if (chunk.getBlock(x, k, z).getType().toString().contains("HUGE_MUSHROOM_2")
									&& chunk.getBlock(x, k + 1, z).getType() == Material.AIR)
								chunk.getBlock(x, k + 1, z).setType(Material.SEA_LANTERN);
							// LIGHT API TODO
						}
						break;
					}
				}
			}
		}
	}

	static HashMap<Chunk, CaveStructureType> hasStructures = new HashMap<Chunk, CaveStructureType>();

	public enum CaveStructureType {
		RABBITHOUSE, LAKE, MUSHSPACE, HUGETREE, LIGHTSPACE, CAMP, PICNIC, NULL;
	}

	public static class CaveStructureGenerator extends BlockPopulator {

		@Override
		public void populate(World world, Random random, Chunk chunk) {
			CaveStructureType structure = CaveStructureType.NULL;
			CaveStructureType newStructure = CaveStructureType.NULL;
			for (Chunk other : ((HashMap<Chunk, CaveStructureType>)(hasStructures.clone())).keySet()) {
				if ((other.getX() - chunk.getX() <= 2) && (other.getZ() - chunk.getZ() <= 2) && Math.random() <= 0.2) {
					// 结构扩张
					hasStructures.put(chunk, hasStructures.get(other));
					spawnStructure(chunk, hasStructures.get(other));
				}else break;
			}
			if (Math.random() <= 0.02) {
				// 结构起始
				newStructure = CaveStructureType
						.values()[(int) Math.floor(CaveStructureType.values().length * Math.random())];
				hasStructures.put(chunk, newStructure);
				spawnStructure(chunk, newStructure);
			}
		}

		public static void spawnStructure(Chunk chunk, CaveStructureType caveStructureType) {
			switch(caveStructureType) {
			case CAMP:
				break;
			case HUGETREE:
				break;
			case LAKE:
				break;
			case LIGHTSPACE:
				break;
			case MUSHSPACE:
				break;
			case PICNIC:
				break;
			case RABBITHOUSE:
				RabbitHouse.generate(chunk);
				break;
			case NULL:
				break;
			}
		}

	}

	static class CaveChunkGenerator extends ChunkGenerator {
		@Override
		public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
			ChunkData chunkData = createChunkData(world);

			chunkData.setRegion(0, 0, 0, 16, 2, 16, Material.BEDROCK);
			chunkData.setRegion(0, 63, 0, 16, 65, 16, Material.BEDROCK);

			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					for (int tmp = 2; tmp <= 2 + Math.random() * (20 * Math.random()); tmp++) {
						Material block = getOre();
						if (block != Material.STONE) {
							for (int k = 0; k <= Math.random() * 5; k++)
								chunkData.setBlock((int) (i + 2 * Math.random()), (int) (tmp + 2 * Math.random()),
										(int) (j + 2 * Math.random()), block);
						}
						chunkData.setBlock(i, tmp, j, block);
					}
					for (int tmp = 63; tmp >= 63 - Math.random() * (20 * Math.random()); tmp--) {
						Material block = getOre();
						if (block != Material.STONE) {
							for (int k = 0; k <= Math.random() * 5; k++)
								chunkData.setBlock((int) (i + 2 * Math.random()), (int) (tmp + 2 * Math.random()),
										(int) (j + 2 * Math.random()), block);
						}
						chunkData.setBlock(i, tmp, j, block);
					}
					biome.setBiome(i, j, Biome.FROZEN_RIVER);
				}
			}
			return chunkData;
		}

		private Material getOre() {
			double ran = Math.random();
			if (ran <= 0.0005)
				return Material.EMERALD_ORE;
			ran = Math.random();
			if (ran <= 0.0007)
				return Material.DIAMOND_ORE;
			ran = Math.random();
			if (ran <= 0.001)
				return Material.GOLD_ORE;
			ran = Math.random();
			if (ran <= 0.003)
				return Material.IRON_ORE;
			ran = Math.random();
			if (ran <= 0.003)
				return Material.LAPIS_ORE;
			ran = Math.random();
			if (ran <= 0.005)
				return Material.QUARTZ_ORE;
			ran = Math.random();
			if (ran <= 0.005)
				return Material.GLOWSTONE;
			ran = Math.random();
			if (ran <= 0.003)
				return Material.REDSTONE_ORE;
			ran = Math.random();
			if (ran <= 0.007)
				return Material.COAL_ORE;
			ran = Math.random();
			if (ran <= 0.01)
				return Material.COBBLESTONE;
			ran = Math.random();
			if (ran <= 0.01)
				return Material.MOSSY_COBBLESTONE;
			ran = Math.random();
			if (ran <= 0.03)
				return Material.DIRT;
			ran = Math.random();
			if (ran <= 0.04)
				return Material.GRAVEL;
			return Material.STONE;
		}
	}
}
