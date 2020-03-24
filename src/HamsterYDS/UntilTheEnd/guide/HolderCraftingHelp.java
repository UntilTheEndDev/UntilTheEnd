package HamsterYDS.UntilTheEnd.guide;

/**
 * Create at 2020/3/7 23:27
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.guide
 */
public class HolderCraftingHelp implements UTEInvHolder {
    public static final HolderCraftingHelp INSTANCE = new HolderCraftingHelp();
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
