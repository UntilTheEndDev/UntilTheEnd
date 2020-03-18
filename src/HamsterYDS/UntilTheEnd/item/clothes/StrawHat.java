package HamsterYDS.UntilTheEnd.item.clothes;

import HamsterYDS.UntilTheEnd.cap.tem.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class StrawHat {
	public static double percent = ItemManager.itemAttributes.getDouble("StrawHat.percent");

	public StrawHat() {
		ChangeTasks.clothesChangeTemperature.put("§6草帽", percent);
	}
}
