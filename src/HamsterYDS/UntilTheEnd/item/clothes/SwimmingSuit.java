package HamsterYDS.UntilTheEnd.item.clothes;

import HamsterYDS.UntilTheEnd.cap.hum.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class SwimmingSuit {
	public SwimmingSuit() {
        ChangeTasks.waterProofSuits.add(ItemManager.items.get("SwimmingSuit").displayName);
	}
}
