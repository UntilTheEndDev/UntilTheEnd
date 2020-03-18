package HamsterYDS.UntilTheEnd.item.materials;

import HamsterYDS.UntilTheEnd.cap.san.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

/**
 * @author 南外丶仓鼠
 * @version V5.1.1
 */
public class NightMare{
	public static int sanityReduce=ItemManager.itemAttributes.getInt("NightMare.sanityReduce");
	public NightMare() {
		ChangeTasks.itemsChangeSanity.put("NightMare",sanityReduce);
	}
}
