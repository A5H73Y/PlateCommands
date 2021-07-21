package io.github.a5h73y.platecommands.plugin;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

/**
 * {@link me.clip.placeholderapi.PlaceholderAPI} integration.
 * Allow for usage of Parkour placeholders.
 */
public class PlaceholderApi extends PluginWrapper {

	@Override
	public String getPluginName() {
		return "PlaceholderAPI";
	}

	public String evaluatePlaceholders(Player player, String input) {
		if (isEnabled()) {
			return PlaceholderAPI.setPlaceholders(player, input);
		} else {
			return input;
		}
	}
}
