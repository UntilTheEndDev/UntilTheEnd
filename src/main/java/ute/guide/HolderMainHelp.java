package ute.guide;

/**
 * Create at 2020/3/7 23:29
 * Copyright Karlatemp
 * UntilTheEnd $ HamsterYDS.UntilTheEnd.guide
 */
public class HolderMainHelp implements UTEInvHolder {
    public static final HolderMainHelp INSTANCE = new HolderMainHelp();
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
