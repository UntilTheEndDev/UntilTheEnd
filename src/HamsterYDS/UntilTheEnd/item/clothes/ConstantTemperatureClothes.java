package HamsterYDS.UntilTheEnd.item.clothes;

import HamsterYDS.UntilTheEnd.cap.tem.ChangeTasks;
import HamsterYDS.UntilTheEnd.item.ItemManager;

public class ConstantTemperatureClothes {
    public ConstantTemperatureClothes() {
        ChangeTasks.clothesChangeTemperature.put(ItemManager.items.get("ConstantTemperatureClothes").displayName,2.0);
    }
}
