package HamsterYDS.UntilTheEnd.cap.san;

import java.util.HashMap;

import org.bukkit.entity.EntityType;

public class SanityProvider {
	public static HashMap<EntityType,Integer> creatureAura=new HashMap<EntityType,Integer>();
	public static void loadAura() {
		for(String path:Sanity.yaml.getKeys(true)) {
			if(path.equalsIgnoreCase("creatureAura")) continue;
			if(path.startsWith("creatureAura")) {
				int san=Sanity.yaml.getInt(path);
				path=path.replace("creatureAura.","");
				EntityType type=EntityType.valueOf(path);
				System.out.println("检测到带有理智光环的生物"+type.toString()+"影响理智值为："+san);
				creatureAura.put(type,san);
			}
		}
	}
}
