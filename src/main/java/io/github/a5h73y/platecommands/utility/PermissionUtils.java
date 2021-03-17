package io.github.a5h73y.platecommands.utility;

import io.github.a5h73y.platecommands.enums.Permission;
import org.bukkit.command.CommandSender;

/**
 * Player Permission related utility methods.
 */
public class PermissionUtils {

	private static final String WILDCARD = "*";

	/**
	 * Check if the player has the specified permission.
	 * The player will be sent a message if they don't have the permission.
	 *
	 * @param sender target command sender
	 * @param permission required {@link Permission}
	 * @return player has permission
	 */
	public static boolean hasPermission(CommandSender sender, Permission permission) {
		return hasPermission(sender, permission, true);
	}

	/**
	 * Check if the player has the specified permission.
	 *
	 * @param sender target command sender
	 * @param permission the required {@link Permission}
	 * @param displayMessage display failure message
	 * @return player has permission
	 */
	public static boolean hasPermission(CommandSender sender, Permission permission, boolean displayMessage) {
		if (sender.isOp()
				|| sender.hasPermission(Permission.PLATE_COMMANDS_ALL.getPermission())
				|| sender.hasPermission(permission.getPermissionRoot() + "." + WILDCARD)
				|| sender.hasPermission(permission.getPermission())) {
			return true;
		}

		if (displayMessage) {
			TranslationUtils.sendValueTranslation("Error.NoPermission",
					permission.getPermission(), sender);
		}
		return false;
	}

	/**
	 * Check if the player has a specific permission.
	 *
	 * @param sender target command sender
	 * @param permission specified permission
	 * @param displayMessage display failure message
	 * @return player has permission
	 */
	public static boolean hasSpecificPermission(CommandSender sender, Permission permission, String permissionNode,
	                                            boolean displayMessage) {
		if (sender.isOp()
				|| sender.hasPermission(Permission.PLATE_COMMANDS_ALL.getPermission())
				|| sender.hasPermission(permission.getPermissionRoot() + "." + permissionNode)) {
			return true;
		}

		if (displayMessage) {
			TranslationUtils.sendValueTranslation("Error.NoPermission",
					permission.getPermissionRoot() + "." + permissionNode, sender);
		}
		return false;
	}
}
