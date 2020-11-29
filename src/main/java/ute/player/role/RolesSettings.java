package ute.player.role;

import ute.Logging;
import ute.internal.ScriptProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import javax.script.CompiledScript;
import java.util.function.DoubleFunction;
import java.util.function.ToDoubleBiFunction;
import java.util.logging.Level;

public class RolesSettings {
    public static boolean resetRoleOnPlayerDeath;
    public static boolean isVaultSupported;

    // 测试的同时会尝试移除对应的金钱
    public static ToDoubleBiFunction<Player, Roles> roleCoins;
    public static DoubleFunction<String> formatter;

    public interface EcoTester {
        boolean withdraw(Player p, double amount);
    }

    public static EcoTester ecoTester;

    static void initialize(@NotNull YamlConfiguration configs) {
//        ScriptProvider
        resetRoleOnPlayerDeath = configs.getBoolean("reset-on-death", true);
        final ConfigurationSection economy = configs.getConfigurationSection("economy");
        isVaultSupported = true;
        if (economy != null) {
            if (isVaultSupported = economy.getBoolean("enable")) {
                final RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(Economy.class);
                if (provider == null) {
                    throw new ExceptionInInitializerError("Roles used vault. But Vault Economy NOT SET.");
                }
                final Economy eco = provider.getProvider();
                if (eco == null) {
                    throw new AssertionError("Roles used vault. But Vault Economy IS NULL!");
                }
                Logging.getLogger().log(Level.INFO, "[Role] Using economy with vault. Power by " + provider.getPlugin() + ", " + eco.getName());
                formatter = eco::format;
                ecoTester = (p, a) -> {
                    if (eco.has(p, a))
                        return eco.withdrawPlayer(p, a).transactionSuccess();
                    return false;
                };
                if (economy.getBoolean("use-js")) {
                    String script = economy.getString("script", "role-script.js");
                    final CompiledScript compiledScript = ScriptProvider.of(script);
                    if (compiledScript == null)
                        throw new ExceptionInInitializerError("No Eco Script Found! [" + script + "]");
                    roleCoins = (p, r) -> ((Number) ScriptProvider.of(compiledScript)
                            .name("script/" + script)
                            .append("player", p)
                            .append("role", r)
                            .invoke()).doubleValue();

                } else {
                    double coins = economy.getDouble("coins");
                    roleCoins = (p, r) -> coins;
                }
            }
        }
        if (roleCoins == null)
            roleCoins = (p, r) -> 0;
        if (ecoTester == null)
            ecoTester = (p, a) -> true;
        if (formatter == null)
            formatter = String::valueOf;
    }
}