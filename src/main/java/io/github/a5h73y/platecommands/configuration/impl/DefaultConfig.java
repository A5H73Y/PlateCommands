package io.github.a5h73y.platecommands.configuration.impl;

import io.github.a5h73y.platecommands.PlateCommands;
import io.github.a5h73y.platecommands.configuration.PlateCommandsConfiguration;
import org.bukkit.Material;

public class DefaultConfig extends PlateCommandsConfiguration {

	@Override
	protected String getFileName() {
		return "config.yml";
	}

	/**
	 * Initialise the config.yml on startup.
	 * Values will be defaulted if not set.
	 */
	@Override
	protected void initializeConfig() {
		this.options().header("==== PlateCommands Config ==== #");

		this.addDefault("PlateAction.DefaultPlateMaterial", "STONE_PRESSURE_PLATE");
		this.addDefault("PlateAction.BlockMaterial", "GOLD_BLOCK");
		this.addDefault("PlateAction.PreventPlateStick", false);
		this.addDefault("PlateAction.IncludeWorldName", true);
		this.addDefault("PlateAction.DisplayMessageAsTitle", true);

		this.addDefault("Other.CheckForUpdates", true);
		this.addDefault("Other.LogToFile", true);
		this.addDefault("Other.UseAutoTabCompletion", true);

		this.addDefault("Plugin.BountifulAPI.Enabled", true);
		this.addDefault("Plugin.Vault.Enabled", true);
		this.addDefault("Plugin.PlaceholderAPI.Enabled", true);

		this.addDefault("Version", PlateCommands.getInstance().getDescription().getVersion());

		this.options().copyDefaults(true);
	}

	public Material getPlateActionPlateMaterial() {
		return Material.valueOf(this.getString("PlateAction.DefaultPlateMaterial"));
	}

	public Material getPlateActionBlockMaterial() {
		return Material.valueOf(this.getString("PlateAction.BlockMaterial"));
	}

	public boolean getPreventPlateStick() {
		return this.getBoolean("PlateAction.PreventPlateStick");
	}

	public boolean getIncludeWorldName() {
		return this.getBoolean("PlateAction.IncludeWorldName");
	}

	public boolean getDisplayMessageAsTitle() {
		return this.getBoolean("PlateAction.DisplayMessageAsTitle");
	}
}
