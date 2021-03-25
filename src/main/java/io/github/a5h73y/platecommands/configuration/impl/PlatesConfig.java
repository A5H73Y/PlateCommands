package io.github.a5h73y.platecommands.configuration.impl;

import io.github.a5h73y.platecommands.configuration.PlateCommandsConfiguration;
import io.github.a5h73y.platecommands.type.PlateAction;

public class PlatesConfig extends PlateCommandsConfiguration {

	@Override
	protected String getFileName() {
		return "plates.yml";
	}

	@Override
	protected void initializeConfig() {
		// config is empty by default
	}

	/**
	 * Get PlateAction by location key from config.
	 * @param locationKey location key
	 * @return populated {@link PlateAction}
	 */
	public PlateAction getPlateAction(String locationKey) {
		PlateAction action = new PlateAction();
		action.setLocationKey(locationKey);
		action.setCommands(this.getStringList(locationKey + ".Commands"));
		action.setRunAsConsole(this.getBoolean(locationKey + ".RunAsConsole"));
		action.setPlayerCoolDown(this.getInt(locationKey + ".PlayerCoolDown"));
		action.setGlobalCoolDown(this.getInt(locationKey + ".GlobalCoolDown"));
		action.setMessage(this.getString(locationKey + ".Message"));
		action.setCost(this.getDouble(locationKey + ".Cost"));
		action.setPermission(this.getString(locationKey + ".Permission"));
		return action;
	}

	public void deletePlateAction(PlateAction action) {
		this.set(action.getLocationKey(), null);
		this.save();
	}
}
