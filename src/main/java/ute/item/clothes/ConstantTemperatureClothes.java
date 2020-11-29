package ute.item.clothes;

import ute.cap.tem.ChangeTasks;
import ute.item.ItemManager;

public class ConstantTemperatureClothes {
    public ConstantTemperatureClothes() {
        ChangeTasks.clothesChangeTemperature.put(ItemManager.items.get("ConstantTemperatureClothes").displayName, 2.0);
    }
}
