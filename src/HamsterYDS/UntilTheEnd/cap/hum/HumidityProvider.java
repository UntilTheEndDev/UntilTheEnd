package HamsterYDS.UntilTheEnd.cap.hum;

import java.util.HashMap;

import org.bukkit.Material;

public class HumidityProvider {
	public static HashMap<Material,Material> moistness=new HashMap<Material,Material>();
	public static HashMap<Material,Material> driness=new HashMap<Material,Material>();
	public static void loadConfig() {
		for(String path:Humidity.yaml.getKeys(true)) {
			if(path.equalsIgnoreCase("wetBlocks.")) continue;
			if(path.startsWith("wetBlocks.")) {
				Material to=Material.valueOf(Humidity.yaml.getString(path));
				path=path.replace("wetBlocks.","");
				Material from=Material.valueOf(path);
				System.out.println("检测到带有潮湿方块变化"+to.toString()+"->"+from);
				moistness.put(from,to);
				driness.put(to,from);
			}
		}
	}
}
