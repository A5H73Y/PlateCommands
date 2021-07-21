package io.github.a5h73y.platecommands.type;

import io.github.a5h73y.platecommands.utility.TranslationUtils;
import java.util.List;
import org.bukkit.entity.Player;

public class PlateAction {

	private String locationKey;
	private List<String> commands;
	private String message;
	private String permission;
	private int playerCoolDown;
	private int globalCoolDown;
	private double cost;

	/**
	 * Display a basic summary of the PlateAction's details.
	 */
	public void displaySummary(Player player) {
		TranslationUtils.sendHeading("PlateAction Details", player);
		TranslationUtils.sendValue(player, "Location", locationKey);
		TranslationUtils.sendConditionalValue(player, "Message", message);
		TranslationUtils.sendConditionalValue(player, "Permission", permission);
		TranslationUtils.sendConditionalValue(player, "Player Cool-down", playerCoolDown);
		TranslationUtils.sendConditionalValue(player, "Global Cool-down", globalCoolDown);
		TranslationUtils.sendConditionalValue(player, "Economy Cost", cost);
		TranslationUtils.sendValue(player, "Commands", "\n " + String.join("\n ", commands));
	}

	public String getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(String locationKey) {
		this.locationKey = locationKey;
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
