package io.github.a5h73y.platecommands.type;

import io.github.a5h73y.platecommands.PlateCommands;
import io.github.a5h73y.platecommands.configuration.impl.PlatesConfig;
import io.github.a5h73y.platecommands.enums.ConfigType;
import io.github.a5h73y.platecommands.other.AbstractPluginReceiver;
import io.github.a5h73y.platecommands.other.DelayTasks;
import io.github.a5h73y.platecommands.utility.MaterialUtils;
import io.github.a5h73y.platecommands.utility.PluginUtils;
import io.github.a5h73y.platecommands.utility.StringUtils;
import io.github.a5h73y.platecommands.utility.TranslationUtils;
import io.github.a5h73y.platecommands.utility.ValidationUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlateActionManager extends AbstractPluginReceiver {

	public static final String PLAYER_COMMAND_PREFIX = "player:";
	public static final String PLAYER_PLACEHOLDER= "%player%";

	private final Map<String, PlateAction> plateActions = new HashMap<>();

	public PlateActionManager(PlateCommands plateCommands) {
		super(plateCommands);
		populatePlateActions();
	}

	public PlateAction getPlateAction(Location location) {
		return getPlateAction(generateLocationKey(location));
	}

	public PlateAction getPlateAction(String key) {
		return plateActions.get(key);
	}

	/**
	 * Execute PlateAction.
	 * @param player player
	 * @param action plate action
	 */
	public void executePlateAction(Player player, PlateAction action) {
		if (action.getGlobalCoolDown() > 0
				&& !DelayTasks.getInstance().delayEvent(action.getLocationKey(), action.getGlobalCoolDown())) {
			return;
		}

		if (action.getPlayerCoolDown() > 0
				&& !DelayTasks.getInstance().delayPlayer(
				player, action.getLocationKey(), action.getPlayerCoolDown())) {
			return;
		}

		if (ValidationUtils.isStringValid(action.getPermission())
				&& !player.hasPermission(action.getPermission())) {
			return;
		}

		if (action.getCost() > 0 && !plateCommands.getEconomyApi().chargePlayer(player, action.getCost())) {
			TranslationUtils.sendValueTranslation("Economy.Insufficient",
					action.getCost() + plateCommands.getEconomyApi().getCurrencyName(), player);
			return;
		}

		if (ValidationUtils.isStringValid(action.getMessage())) {
			String message = StringUtils.colour(parsePlaceholders(player, action.getMessage()));
			plateCommands.getBountifulApi().sendSubTitle(player, message,
					plateCommands.getConfig().getDisplayMessageAsTitle());
		}

		for (String command : action.getCommands()) {
			dispatchCommand(player, parsePlaceholders(player, command));
		}
	}

	private String parsePlaceholders(Player player, String command) {
		command = command.replace(PLAYER_PLACEHOLDER, player.getName());
		return plateCommands.getPlaceholderApi().evaluatePlaceholders(player, command);
	}

	/**
	 * Dispatch command for Player.
	 * If the commands begins with "player:" it will be player executed, otherwise server executed.
	 * @param player player
	 * @param command command to run
	 */
	private void dispatchCommand(Player player, String command) {
		if (command.startsWith(PLAYER_COMMAND_PREFIX)) {
			player.performCommand(command.split(PLAYER_COMMAND_PREFIX)[1]);

		} else {
			plateCommands.getServer().dispatchCommand(plateCommands.getServer().getConsoleSender(), command);
		}
	}

	/**
	 * Create PlateAction at Player's current location.
	 * @param player player
	 * @param args arguments
	 */
	public void createPlateAction(Player player, String... args) {
		String locationKey = generateLocationKey(player.getLocation());

		if (!ValidationUtils.canCreatePlateAction(player, locationKey)) {
			return;
		}

		PlatesConfig platesConfig = (PlatesConfig) plateCommands.getConfigManager().get(ConfigType.PLATES);

		Block block = player.getLocation().getBlock();
		block.setType(plateCommands.getConfig().getPlateActionPlateMaterial());
		Block blockUnder = block.getRelative(BlockFace.DOWN);
		blockUnder.setType(plateCommands.getConfig().getPlateActionBlockMaterial());

		String combinedArgs = StringUtils.extractMessageFromArgs(args, 1);
		// remove the spacing from either side of the command
		List<String> commands = Arrays.stream(combinedArgs.split(";")).map(String::trim).collect(Collectors.toList());

		platesConfig.set(locationKey + ".Commands", commands);
		platesConfig.set(locationKey + ".PlayerCoolDown", 0);
		platesConfig.set(locationKey + ".GlobalCoolDown", 0);
		platesConfig.set(locationKey + ".Message", "&fActivated!");
		platesConfig.set(locationKey + ".Cost", 0);
		platesConfig.set(locationKey + ".Permission", "");
		platesConfig.set(locationKey + ".Creator", player.getName());
		platesConfig.save();
		platesConfig.reload();

		populatePlateActions();
		PluginUtils.logToFile(player.getName() + " created PlateAction at " + locationKey);
	}

	/**
	 * Populate the PlateActions cache with the config entries.
	 */
	public void populatePlateActions() {
		PlatesConfig platesConfig = (PlatesConfig) plateCommands.getConfigManager().get(ConfigType.PLATES);
		ConfigurationSection section = platesConfig.getConfigurationSection("");
		plateActions.clear();

		if (section != null) {
			Set<String> entries = section.getKeys(false);
			for (String entry : entries) {
				plateActions.put(entry, platesConfig.getPlateAction(entry));
			}
		}
	}

	private String generateLocationKey(Location location) {
		String coordinates = location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ();

		if (plateCommands.getConfig().getIncludeWorldName() && location.getWorld() != null) {
			coordinates += "/" + location.getWorld().getName();
		}
		return coordinates;
	}

	/**
	 * Delete PlateAction at block location.
	 * @param location location
	 */
	public void deletePlateAction(Location location) {
		PlateAction action = getPlateAction(location);

		if (action != null) {
			PlatesConfig platesConfig = (PlatesConfig) plateCommands.getConfigManager().get(ConfigType.PLATES);
			platesConfig.deletePlateAction(action);
			populatePlateActions();
		}
	}

	/**
	 * Lookup and display PlateAction details.
	 * @param player player
	 */
	public void lookupPlateActionDetails(Player player) {
		Block block = MaterialUtils.getTargetBlock(player, 10);
		if (block.getType().name().endsWith("PRESSURE_PLATE")) {
			PlateAction plateAction = getPlateAction(block.getLocation());

			if (plateAction != null) {
				plateAction.displaySummary(player);
				return;
			}
		}
		TranslationUtils.sendTranslation("Error.UnknownPlateAction", player);
	}
}
