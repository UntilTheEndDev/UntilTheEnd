package HamsterYDS.UntilTheEnd.guide;

import org.jetbrains.annotations.Nullable;

/**
 * Create at 2020/3/7 23:30
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.guide
 */
public class HolderMechanismGuide implements UTEInvHolder {
    public static final HolderMechanismGuide INSTANCE = new HolderMechanismGuide();
    private String name;

    @Override
    public @Nullable String getCustomName() {
        return name;
    }

    @Override
    public void setCustomName(@Nullable String name) {
        this.name = name;
    }
}
