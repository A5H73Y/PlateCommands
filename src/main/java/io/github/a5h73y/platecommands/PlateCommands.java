package io.github.a5h73y.platecommands;

import com.google.gson.GsonBuilder;
import io.github.a5h73y.platecommands.commands.PlateCommandsAutoTabCompleter;
import io.github.a5h73y.platecommands.commands.PlateCommandsCommands;
import io.github.a5h73y.platecommands.configuration.ConfigManager;
import io.github.a5h73y.platecommands.configuration.PlateCommandsConfiguration;
import io.github.a5h73y.platecommands.configuration.impl.DefaultConfig;
import io.github.a5h73y.platecommands.enums.ConfigType;
import io.github.a5h73y.platecommands.listener.PlayerInteractListener;
import io.github.a5h73y.platecommands.other.CommandUsage;
import io.github.a5h73y.platecommands.other.PlateCommandsUpdater;
import io.github.a5h73y.platecommands.plugin.BountifulApi;
import io.github.a5h73y.platecommands.plugin.EconomyApi;
import io.github.a5h73y.platecommands.plugin.PlaceholderApi;
import io.github.a5h73y.platecommands.type.PlateActionManager;
import io.github.a5h73y.platecommands.utility.PluginUtils;
import io.github.a5h73y.platecommands.utility.TranslationUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PlateCommands extends JavaPlugin {

    public static final String PLUGIN_NAME = "platecommands";

    private static final int BSTATS_ID = 10691;
    private static final int SPIGOT_PLUGIN_ID = 90578;
    private static PlateCommands instance;

    private BountifulApi bountifulApi;
    private EconomyApi economyApi;
    private PlaceholderApi placeholderApi;

    private List<CommandUsage> commandUsages;

    private ConfigManager configManager;
    private PlateActionManager plateActionManager;

    /**
     * Get the plugin's instance.
     *
     * @return plateCommands plugin instance.
     */
    public static PlateCommands getInstance() {
        return instance;
    }

    /**
     * Initialise the PlateCommands plugin.
     */
    @Override
    public void onEnable() {
        instance = this;

        registerManagers();
        registerCommands();
        registerEvents();

        setupPlugins();

        getLogger().info("Enabled PlateCommands v" + getDescription().getVersion());
        submitAnalytics();
        checkForUpdates();
    }

    /**
     * Shutdown the plugin.
     */
    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        PluginUtils.log("Disabled PlateCommands v" + getDescription().getVersion());
        instance = null;
    }

    /**
     * Get the Default config.
     * Overrides the default getConfig() method.
     *
     * @return default config
     */
    @Override
    public DefaultConfig getConfig() {
        return (DefaultConfig) this.configManager.get(ConfigType.DEFAULT);
    }

    /**
     * Get the matching {@link PlateCommandsConfiguration} for the given {@link ConfigType}.
     *
     * @param type {@link ConfigType}
     * @return matching {@link PlateCommandsConfiguration}
     */
    public static PlateCommandsConfiguration getConfig(ConfigType type) {
        return instance.configManager.get(type);
    }

    /**
     * Save the Default config.
     * Overrides the default saveConfig() method.
     */
    @Override
    public void saveConfig() {
        getConfig().save();
    }

    /**
     * Get the default config.yml file.
     *
     * @return {@link DefaultConfig}
     */
    public static DefaultConfig getDefaultConfig() {
        return instance.getConfig();
    }

    /**
     * The PlateCommands message prefix.
     *
     * @return platecommands prefix from the config.
     */
    public static String getPrefix() {
        return TranslationUtils.getTranslation("PlateCommands.Prefix", false);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlateActionManager getPlateActionManager() {
        return plateActionManager;
    }

    public BountifulApi getBountifulApi() {
        return bountifulApi;
    }

    public EconomyApi getEconomyApi() {
        return economyApi;
    }

    public PlaceholderApi getPlaceholderApi() {
        return placeholderApi;
    }

    public List<CommandUsage> getCommandUsages() {
        return commandUsages;
    }

    private void setupPlugins() {
        bountifulApi = new BountifulApi();
        economyApi = new EconomyApi();
        placeholderApi = new PlaceholderApi();
    }

    private void registerManagers() {
        configManager = new ConfigManager(this.getDataFolder());
        plateActionManager = new PlateActionManager(this);
    }

    private void registerCommands() {
        getCommand(PLUGIN_NAME).setExecutor(new PlateCommandsCommands(this));

        if (this.getConfig().getBoolean("Other.UseAutoTabCompletion")) {
            getCommand(PLUGIN_NAME).setTabCompleter(new PlateCommandsAutoTabCompleter(this));
        }

        String json = new BufferedReader(new InputStreamReader(getResource("plateCommandsCommands.json"), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        commandUsages = Arrays.asList(new GsonBuilder().create().fromJson(json, CommandUsage[].class));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
    }

    /**
     * Check to see if a newer version exists on Spigot.
     */
    private void checkForUpdates() {
        if (getConfig().getBoolean("Other.CheckForUpdates")) {
            new PlateCommandsUpdater(this, SPIGOT_PLUGIN_ID).checkForUpdateAsync();
        }
    }

    /**
     * Submit bStats analytics.
     * Can be disabled through the bStats config.yml.
     */
    private void submitAnalytics() {
        new Metrics(this, BSTATS_ID);
    }
}
