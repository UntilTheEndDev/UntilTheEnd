package ute.world.cave.structure;

import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class RabbitHouse {
	public RabbitHouse() {
		UUID whichRabbitOwn;
		int updatePeriod;
	}

	public static void generate(Chunk chunk) {
		int centreX = (int) (Math.random() * 5 + 5);
		int centreZ = (int) (Math.random() * 5 + 5);
		int radius = 5;
		int height = (int) (Math.random() * 10) + 10;
		int standard = getLowestY(chunk, centreX, centreZ);

		// 地板及中空
		for (int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++) {
				Block block = chunk.getBlock(x, standard, z);
				if (block.getLocation().distance(chunk.getBlock(centreX, standard, centreZ).getLocation()) <= 4
						* Math.sqrt(2)) {
					block.setType(Material.WOOD);
					block.setData((byte) Math.floor(Math.random() * 6));
				} 
				if (block.getLocation().distance(chunk.getBlock(centreX, standard, centreZ).getLocation()) <= 5
						* Math.sqrt(2)) {
					for (int y = standard + 1; y <= standard + height; y++) {
						Block block_up = chunk.getBlock(x, y, z);
						block_up.setType(Material.AIR);
					}
				}
			}

		// 最外边 3*4格
		setWallBlock(chunk, centreX + radius, centreZ, standard, height);
		setWallBlock(chunk, centreX + radius, centreZ + 1, standard, height);
		setWallBlock(chunk, centreX + radius, centreZ - 1, standard, height);

		setWallBlock(chunk, centreX - radius, centreZ, standard, height);
		setWallBlock(chunk, centreX - radius, centreZ + 1, standard, height);
		setWallBlock(chunk, centreX - radius, centreZ - 1, standard, height);

		setWallBlock(chunk, centreX, centreZ + radius, standard, height);
		setWallBlock(chunk, centreX + 1, centreZ + radius, standard, height);
		setWallBlock(chunk, centreX - 1, centreZ + radius, standard, height);

		setWallBlock(chunk, centreX, centreZ - radius, standard, height);
		setWallBlock(chunk, centreX + 1, centreZ - radius, standard, height);
		setWallBlock(chunk, centreX - 1, centreZ - radius, standard, height);

		// 内边 8*2格
		setWallBlock(chunk, centreX + radius - 1, centreZ + 2, standard, height);
		setWallBlock(chunk, centreX + radius - 1, centreZ + 3, standard, height);
		setWallBlock(chunk, centreX + radius - 1, centreZ - 2, standard, height);
		setWallBlock(chunk, centreX + radius - 1, centreZ - 3, standard, height);

		setWallBlock(chunk, centreX + 2, centreZ + radius - 1, standard, height);
		setWallBlock(chunk, centreX + 3, centreZ + radius - 1, standard, height);
		setWallBlock(chunk, centreX + 2, centreZ - radius + 1, standard, height);
		setWallBlock(chunk, centreX + 3, centreZ - radius + 1, standard, height);

		setWallBlock(chunk, centreX - radius + 1, centreZ + 2, standard, height);
		setWallBlock(chunk, centreX - radius + 1, centreZ + 3, standard, height);
		setWallBlock(chunk, centreX - radius + 1, centreZ - 2, standard, height);
		setWallBlock(chunk, centreX - radius + 1, centreZ - 3, standard, height);

		setWallBlock(chunk, centreX - 2, centreZ + radius - 1, standard, height);
		setWallBlock(chunk, centreX - 3, centreZ + radius - 1, standard, height);
		setWallBlock(chunk, centreX - 2, centreZ - radius + 1, standard, height);
		setWallBlock(chunk, centreX - 3, centreZ - radius + 1, standard, height);
	}

	private static Material[] wallMaterials = new Material[] { Material.PUMPKIN, Material.BRICK, Material.BRICK,
			Material.BRICK, Material.MOSSY_COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.HAY_BLOCK,
			Material.NETHERRACK, Material.NETHERRACK, Material.NETHER_BRICK, Material.NETHER_BRICK, Material.MYCEL };

	private static void setWallBlock(Chunk chunk, int x, int z, int standard_basic, int height) {
		for (int y = 3; y <= standard_basic + height; y++) {
			Block block = chunk.getBlock(x, y, z);
			if (block.getType() == Material.AIR)
				block.setType(wallMaterials[(int) Math.floor(wallMaterials.length * Math.random())]);
		}
	}

	private static int getLowestY(Chunk chunk, int x, int z) {
		int y = 3;
		for (; y <= 20 && chunk.getBlock(x, y, z).getType() == Material.AIR; y++)
			;
		return y;
	}
}
