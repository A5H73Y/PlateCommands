package io.github.a5h73y.platecommands.plugin;

import com.connorlinfoot.bountifulapi.BountifulAPI;
import io.github.a5h73y.platecommands.utility.PluginUtils;
import io.github.a5h73y.platecommands.utility.TranslationUtils;
import io.github.a5h73y.platecommands.utility.ValidationUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * BountifulAPI wrapper to provide Title and Action Bar messages to the Player.
 * BountifulAPI integration will be used when Spigot's implementation isn't supported.
 */
public class BountifulApi extends PluginWrapper {

	private boolean useSpigotMethods;
	private int inDuration;
	private int stayDuration;
	private int outDuration;

	@Override
	public String getPluginName() {
		return "BountifulAPI";
	}

	@Override
	protected void initialise() {
		super.initialise();

		useSpigotMethods = PluginUtils.getMinorServerVersion() > 10;
		inDuration = 20;
		stayDuration = 20;
		outDuration = 20;
	}

	/**
	 * Send the Player the title.
	 * Quiet Mode will be respected and not message the Player when enabled.
	 * Attempting the Title will mean either spigot's title implementation or BountifulAPI will be used.
	 *
	 * @param player target player
	 * @param title title text
	 * @param attemptTitle attempt to show the title
	 */
	public void sendTitle(Player player, String title, boolean attemptTitle) {
		sendFullTitle(player, title, " ", attemptTitle);
	}

	/**
	 * Send the Player the sub title.
	 * Quiet Mode will be respected and not message the Player when enabled.
	 * Attempting the Title will mean either spigot's title implementation or BountifulAPI will be used.
	 *
	 * @param player target player
	 * @param subTitle sub title text
	 * @param attemptTitle attempt to show the title
	 */
	public void sendSubTitle(Player player, String subTitle, boolean attemptTitle) {
		sendFullTitle(player, " ", subTitle, attemptTitle);
	}

	/**
	 * Send the Player a title message.
	 * Empty strings can be passed to display 'nothing' in either sections.
	 * Quiet Mode will be respected and not message the Player when enabled.
	 * Attempting the Title will mean either spigot's title implementation or BountifulAPI will be used.
	 *
	 * @param player target player
	 * @param title main title text
	 * @param subTitle sub title text
	 * @param attemptTitle attempt to show the title
	 */
	public void sendFullTitle(Player player, String title, String subTitle, boolean attemptTitle) {
		if (attemptTitle) {
			if (useSpigotMethods) {
				player.sendTitle(title, subTitle, inDuration, stayDuration, outDuration);
				return;

			} else if (isEnabled()) {
				BountifulAPI.sendTitle(player, inDuration, stayDuration, outDuration, title, subTitle);
				return;
			}
		}

		if (ValidationUtils.isStringValid(title)) {
			TranslationUtils.sendMessage(player, title);
		}
		if (ValidationUtils.isStringValid(subTitle)) {
			TranslationUtils.sendMessage(player, subTitle);
		}
	}

	/**
	 * Send an Action Bar to the Player.
	 * Quiet Mode will be respected and not message the Player when enabled.
	 * Attempting the Title will mean either spigot's action bar implementation or BountifulAPI will be used.
	 *
	 * @param player target player
	 * @param title action bar text
	 * @param attemptTitle attempt to show the title
	 */
	public void sendActionBar(Player player, String title, boolean attemptTitle) {
		if (attemptTitle) {
			if (useSpigotMethods) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(title));

			} else if (isEnabled()) {
				BountifulAPI.sendActionBar(player, title);

			} else {
				TranslationUtils.sendMessage(player, title);
			}
		} else {
			TranslationUtils.sendMessage(player, title);
		}
	}
}
