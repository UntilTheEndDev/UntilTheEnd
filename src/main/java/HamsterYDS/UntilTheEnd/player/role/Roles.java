package HamsterYDS.UntilTheEnd.player.role;

import HamsterYDS.UntilTheEnd.Config;
import HamsterYDS.UntilTheEnd.Logging;
import HamsterYDS.UntilTheEnd.UntilTheEnd;
import HamsterYDS.UntilTheEnd.internal.DisableManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Set;
import java.util.stream.Collectors;

public enum Roles {
    DEFAULT("未选择", 0, 200, 20, 1.0),
    WILSON("威尔逊", 0, 200, 20, 1.0),
    WILLOW("薇洛", 0, 120, 20, 1.0),
    WOLFGANG("沃尔夫冈", 0, 200, 30, 1.0),
    WENDY("温蒂", 0, 250, 20, 0.75),
    WX78("AI->WX-78", 0, 100, 10, 1.0),
    WICKERBOTTOM("维克波顿", 0, 250, 20, 1.0),
    WOODIE("伍迪", 0, 200, 20, 1.0),
    WES("维斯", 0, 150, 15, 0.75),
    WIGFRID("威戈芙瑞德", 0, 120, 25, 1.25),
    WEBBER("韦伯", 0, 100, 22, 1.0),
    MAXWELL("麦克斯韦", 0, 200, 10, 1.0);
    public String name;
    public int originLevel;
    public int originSanMax;
    public int originHealthMax;
    public double originDamageLevel;
    public static boolean isEnable;
    public boolean allow = true;

    Roles(String name, int originLevel, int originSanMax, int originHealthMax, double originDamageLevel) {
        this.name = name;
        this.originLevel = originLevel;
        this.originSanMax = originSanMax;
        this.originHealthMax = originHealthMax;
        this.originDamageLevel = originDamageLevel;
    }

    static {
        final UntilTheEnd instance = UntilTheEnd.getInstance();
        Logging.getLogger().fine("[Role] Loading rule configuration......");
        final YamlConfiguration configs = Config.autoUpdateConfigs("roles.yml");
        isEnable = configs.getBoolean("enabled", true);
        Logging.getLogger().fine("[Role] Was Role enabled? > " + isEnable);
        final Set<String> roles = DisableManager.root.getStringList("roles").stream().map(String::toLowerCase).collect(Collectors.toSet());
        for (Roles r : values()) {
            Logging.getLogger().fine("[Role] Loading settings for " + r.name());
            if (roles.contains(r.name())) {
                instance.getLogger().fine("[Role] \tRole [" + r.name() + "] was disabled in disable.yml....");
                r.allow = false;
            }
            final ConfigurationSection section = configs.getConfigurationSection(r.name().toLowerCase());
            if (section == null) {
                Logging.getLogger().fine("[Role] \tSettings for " + r.name() + " not found. use default values.");
            } else {
                r.name = section.getString("name", r.name);
                r.originLevel = section.getInt("originLevel", r.originLevel);
                r.originSanMax = section.getInt("originSanMax", r.originSanMax);
                r.originHealthMax = section.getInt("originHealthMax", r.originHealthMax);
                r.originDamageLevel = section.getDouble("originDamageLevel", r.originDamageLevel);
                Logging.getLogger().fine("[Role] \t\tName     : " + r.name);
                Logging.getLogger().fine("[Role] \t\tLevel    : " + r.originLevel);
                Logging.getLogger().fine("[Role] \t\tMaxHealth: " + r.originHealthMax);
                Logging.getLogger().fine("[Role] \t\tMaxSanity: " + r.originSanMax);
                Logging.getLogger().fine("[Role] \t\tDamageLv : " + r.originDamageLevel);
            }
        }
        RolesSettings.initialize(configs);
    }
}
