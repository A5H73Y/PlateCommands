package io.github.a5h73y.platecommands.other;

import io.github.a5h73y.platecommands.PlateCommands;

/**
 * Ensure the concrete class receives an instance of the PlateCommands plugin.
 */
public abstract class AbstractPluginReceiver {

	protected final PlateCommands plateCommands;

	protected AbstractPluginReceiver(final PlateCommands plateCommands) {
		this.plateCommands = plateCommands;
	}

	public PlateCommands getPlateCommands() {
		return this.plateCommands;
	}

}
