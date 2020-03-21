package HamsterYDS.UntilTheEnd.guide;

import org.bukkit.Nameable;

/**
 * Create at 2020/3/7 23:29
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.guide
 */
public class HolderMainGuide implements UTEInvHolder {
    public static final HolderMainGuide INSTANCE = new HolderMainGuide();
    private String name;

    @Override
    public String getCustomName() {
        return name;
    }

    @Override
    public void setCustomName(String name) {
        this.name = name;
    }
}
