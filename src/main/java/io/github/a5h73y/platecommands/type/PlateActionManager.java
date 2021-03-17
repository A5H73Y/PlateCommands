package io.github.a5h73y.platecommands.type;

import io.github.a5h73y.platecommands.PlateCommands;
import io.github.a5h73y.platecommands.configuration.impl.PlatesConfig;
import io.github.a5h73y.platecommands.enums.ConfigType;
import io.github.a5h73y.platecommands.other.AbstractPluginReceiver;
import io.github.a5h73y.platecommands.other.DelayTasks;
import io.github.a5h73y.platecommands.utility.StringUtils;
import io.github.a5h73y.platecommands.utility.ValidationUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlateActionManager extends AbstractPluginReceiver {

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

	public void execute(Player player, PlateAction action) {
		if (action.getGlobalCoolDown() > 0
				&& !DelayTasks.getInstance().delayEvent(action.getLocationKey(), action.getGlobalCoolDown())) {
			return;
		}

		if (action.getPlayerCoolDown() > 0
				&& !DelayTasks.getInstance().delayPlayer(player, action.getLocationKey(), action.getPlayerCoolDown())) {
			return;
		}

		if (ValidationUtils.isStringValid(action.getPermission())
				&& !player.hasPermission(action.getPermission())) {
			return;
		}

		if (action.getCost() > 0 && !plateCommands.getEconomyApi().chargePlayer(player, action.getCost())) {
			return;
		}

		if (ValidationUtils.isStringValid(action.getMessage())) {
			plateCommands.getBountifulApi().sendSubTitle(player, StringUtils.colour(action.getMessage()), true);
		}

		for (String command : action.getCommands()) {
			command = command.replace("%player%", player.getName());

			if (action.isRunAsConsole()) {
				plateCommands.getServer().dispatchCommand(plateCommands.getServer().getConsoleSender(), command);
			} else {
				player.performCommand(command);
			}
		}
	}

	public void create(Player player, String... args) {
		String locationKey = generateLocationKey(player.getLocation());

		if (!ValidationUtils.canCreatePlateAction(player, locationKey, args)) {
			return;
		}

		PlatesConfig platesConfig = (PlatesConfig) plateCommands.getConfigManager().get(ConfigType.PLATES);

		Block block = player.getLocation().getBlock();
		block.setType(Material.STONE_PRESSURE_PLATE);
		Block blockUnder = block.getRelative(BlockFace.DOWN);
		blockUnder.setType(plateCommands.getConfig().getPlateActionMaterial());

		String combinedArgs = StringUtils.extractMessageFromArgs(args, 1);

		platesConfig.set(locationKey + ".Commands", combinedArgs.split(";"));
		platesConfig.set(locationKey + ".RunAsConsole", true);
		platesConfig.set(locationKey + ".PlayerCoolDown", 0);
		platesConfig.set(locationKey + ".GlobalCoolDown", 0);
		platesConfig.set(locationKey + ".Message", "&fActivated!");
		platesConfig.set(locationKey + ".Cost", 0);
		platesConfig.set(locationKey + ".Permission", "");
		platesConfig.set(locationKey + ".Creator", player.getName());
		platesConfig.save();

		populatePlateActions();
	}

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

	public void deletePlateAction(Location location) {
		PlateAction action = getPlateAction(location);

		if (action != null) {
			PlatesConfig platesConfig = (PlatesConfig) plateCommands.getConfigManager().get(ConfigType.PLATES);
			platesConfig.deletePlateAction(action);
			populatePlateActions();
		}
	}
}
