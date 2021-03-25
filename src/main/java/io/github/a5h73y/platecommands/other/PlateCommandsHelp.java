package io.github.a5h73y.platecommands.other;

import io.github.a5h73y.platecommands.PlateCommands;
import io.github.a5h73y.platecommands.utility.TranslationUtils;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlateCommandsHelp {

    /**
     * Display Help Menu to player.
     * Content is populated based on config and permissions.
     *
     * @param player requesting player
     */
    public static void displayCommands(Player player) {
        TranslationUtils.sendHeading("PlateCommands Commands", player);

        PlateCommands.getInstance().getCommandUsages().stream()
                .filter(commandUsage -> commandUsage.getPermission() == null
                        || player.hasPermission(commandUsage.getPermission()))
                .forEach(commandUsage -> commandUsage.displayCommandUsage(player));
    }

    /**
     * Lookup and display the syntax and description for each PlateCommand command.
     *
     * @param args arguments
     * @param sender command sender
     */
    public static void lookupCommandHelp(String[] args, CommandSender sender) {
        if (args.length == 1) {
            TranslationUtils.sendMessage(sender, "Find helpful information about any PlateCommands command:");
            TranslationUtils.sendMessage(sender, " /plateCommands help &b(command)", false);
            return;
        }

        String command = args[1].toLowerCase();

        Optional<CommandUsage> matching = PlateCommands.getInstance().getCommandUsages().stream()
                .filter(commandUsage -> commandUsage.getCommand().equals(command))
                .findAny();

        if (matching.isPresent()) {
            matching.get().displayHelpInformation(sender);

        } else {
            TranslationUtils.sendMessage(sender,
                    "Unrecognised command. Please find all available commands using '/pc cmds'");
        }
    }
}
