package HamsterYDS.UntilTheEnd.item.clothes;

import HamsterYDS.UntilTheEnd.cap.san.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class Garland {
	public static int sanityImprove = ItemManager.itemAttributes.getInt("Garland.sanityImprove");

	public Garland() {
		ChangeTasks.clothesChangeSanity.put("Garland", sanityImprove);
	}
}
