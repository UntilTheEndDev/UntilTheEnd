package HamsterYDS.UntilTheEnd.crops;

import java.util.HashMap;
import java.util.List;

public class CropProvider {
	public static HashMap<String,HashMap<String,Double>> seasonCrops=new HashMap<String,HashMap<String,Double>>();
	public static void loadConfig() {
		for(String path:Crops.yaml.getKeys(true)) {
			if(path.equalsIgnoreCase("seasonCrops")) continue;
			if(path.startsWith("seasonCrops")) {
				List<String> seasons=Crops.yaml.getStringList(path+".seasons");
				List<Double> percents=Crops.yaml.getDoubleList(path+".percents");
				HashMap<String,Double> crop=new HashMap<String,Double>();
				int index=0;
				for(String name:seasons) {
					crop.put(name,percents.get(index));
					index++;
				}
				System.out.println("检测到带有季节性作物"+path.replace("seasonCrops.","")+"其数据为："+crop);
				seasonCrops.put(path.replace("seasonCrops.",""),crop);
			}
		}
	}
}
