package io.github.a5h73y.platecommands.commands;

import io.github.a5h73y.platecommands.PlateCommands;
import io.github.a5h73y.platecommands.enums.Permission;
import io.github.a5h73y.platecommands.other.AbstractPluginReceiver;
import io.github.a5h73y.platecommands.other.PlateCommandsHelp;
import io.github.a5h73y.platecommands.utility.PermissionUtils;
import io.github.a5h73y.platecommands.utility.PluginUtils;
import io.github.a5h73y.platecommands.utility.TranslationUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Player related PlateCommands commands handling.
 */
public class PlateCommandsCommands extends AbstractPluginReceiver implements CommandExecutor {

    public PlateCommandsCommands(final PlateCommands plateCommands) {
        super(plateCommands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String... args) {
        if (!(sender instanceof Player)) {
            TranslationUtils.sendMessage(sender, "'/plateCommands' is only available in game.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            TranslationUtils.sendMessage(player, "Plugin proudly created by &bA5H73Y");
            TranslationUtils.sendTranslation("Help.Commands", player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (!PermissionUtils.hasPermission(player, Permission.BASIC_INFO)) {
                    return false;
                }
                plateCommands.getPlateActionManager().createPlateAction(player, args);
                break;

            case "info":
                if (!PermissionUtils.hasPermission(player, Permission.BASIC_INFO)) {
                    return false;
                }
                plateCommands.getPlateActionManager().lookupPlateActionDetails(player);
                break;

            case "reload":
                if (!PermissionUtils.hasPermission(player, Permission.ADMIN_ALL)) {
                    return false;
                }

                plateCommands.getConfigManager().reloadConfigs();
                plateCommands.getPlateActionManager().populatePlateActions();
                TranslationUtils.sendTranslation("PlateCommands.ConfigReloaded", player);
                PluginUtils.logToFile(player.getName() + " reloaded the config");
                break;

            case "help":
                PlateCommandsHelp.lookupCommandHelp(args, player);
                break;

            case "about":
            case "ver":
            case "version":
                player.sendMessage(PlateCommands.getPrefix() + "Server is running PlateCommands " + ChatColor.GRAY
                        + plateCommands.getDescription().getVersion());
                player.sendMessage("This plugin was developed by " + ChatColor.GOLD + "A5H73Y");
                break;

            case "cmds":
            case "commands":
                PlateCommandsHelp.displayCommands(player);
                break;

            default:
                TranslationUtils.sendTranslation("Error.UnknownCommand", player);
                TranslationUtils.sendTranslation("Help.Commands", player);
                break;
        }
        return true;
    }
}
