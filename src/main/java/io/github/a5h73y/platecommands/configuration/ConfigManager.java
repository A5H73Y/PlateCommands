package io.github.a5h73y.platecommands.configuration;

import io.github.a5h73y.platecommands.configuration.impl.DefaultConfig;
import io.github.a5h73y.platecommands.configuration.impl.PlatesConfig;
import io.github.a5h73y.platecommands.configuration.impl.StringsConfig;
import io.github.a5h73y.platecommands.enums.ConfigType;
import java.io.File;
import java.util.EnumMap;

/**
 * PlateCommands Configuration Manager.
 * Manages and stores references to each of the available Config files.
 */
public class ConfigManager {

	private final File dataFolder;

	private final EnumMap<ConfigType, PlateCommandsConfiguration> plateCommandsConfigs = new EnumMap<>(ConfigType.class);

	/**
	 * Initialise the Config Manager.
	 * Will invoke setup for each available config type.
	 *
	 * @param dataFolder where to store the configs
	 */
	public ConfigManager(File dataFolder) {
		this.dataFolder = dataFolder;
		createPlateCommandsFolder();

		plateCommandsConfigs.put(ConfigType.DEFAULT, new DefaultConfig());
		plateCommandsConfigs.put(ConfigType.STRINGS, new StringsConfig());
		plateCommandsConfigs.put(ConfigType.PLATES, new PlatesConfig());

		for (PlateCommandsConfiguration plateCommandsConfig: plateCommandsConfigs.values()) {
			plateCommandsConfig.setupFile(this.dataFolder);
		}
	}

	/**
	 * Get matching PlateCommandsConfiguration for the ConfigType.
	 *
	 * @param type requested config type
	 * @return matching PlateCommandsConfiguration
	 */
	public PlateCommandsConfiguration get(ConfigType type) {
		return plateCommandsConfigs.get(type);
	}

	/**
	 * Reload each of the configuration files.
	 */
	public void reloadConfigs() {
		for (PlateCommandsConfiguration plateCommandsConfig : plateCommandsConfigs.values()) {
			plateCommandsConfig.reload();
		}
	}

	private void createPlateCommandsFolder() {
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
	}
}
