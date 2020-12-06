package ute.guide.craft;

/**
 * Create at 2020/3/7 23:29
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.guide
 */
public class HolderCategoryHelp implements UTEInvHolder {
    public static final HolderCategoryHelp INSTANCE = new HolderCategoryHelp();
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
