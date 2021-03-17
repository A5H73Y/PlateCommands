package io.github.a5h73y.platecommands.type;

import java.util.List;

public class PlateAction {

	private String locationKey;
	private boolean runAsConsole;
	private List<String> commands;
	private String message;
	private String permission;
	private int playerCoolDown;
	private int globalCoolDown;
	private double cost;

	public String getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(String locationKey) {
		this.locationKey = locationKey;
	}

	public boolean isRunAsConsole() {
		return runAsConsole;
	}

	public void setRunAsConsole(boolean runAsConsole) {
		this.runAsConsole = runAsConsole;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public int getPlayerCoolDown() {
		return playerCoolDown;
	}

	public void setPlayerCoolDown(int playerCoolDown) {
		this.playerCoolDown = playerCoolDown;
	}

	public int getGlobalCoolDown() {
		return globalCoolDown;
	}

	public void setGlobalCoolDown(int globalCoolDown) {
		this.globalCoolDown = globalCoolDown;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

}
