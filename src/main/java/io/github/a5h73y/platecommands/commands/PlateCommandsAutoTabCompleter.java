package io.github.a5h73y.platecommands.commands;

import io.github.a5h73y.platecommands.PlateCommands;
import io.github.a5h73y.platecommands.other.AbstractPluginReceiver;
import io.github.a5h73y.platecommands.other.CommandUsage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Tab auto-completion for PlateCommands commands.
 */
public class PlateCommandsAutoTabCompleter extends AbstractPluginReceiver implements TabCompleter {

    public PlateCommandsAutoTabCompleter(final PlateCommands plateCommands) {
        super(plateCommands);
    }

    /**
     * List of tab-able commands will be built based on the configuration and player permissions.
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command cmd,
                                      @NotNull String alias,
                                      @NotNull String... args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }

        final Player player = (Player) sender;
        List<String> allowedCommands = new ArrayList<>();
        List<String> filteredCommands = new ArrayList<>();

        if (args.length == 1) {
            allowedCommands = plateCommands.getCommandUsages().stream()
                    .filter(commandUsage -> commandUsage.getPermission() == null
                            || player.hasPermission(commandUsage.getPermission()))
                    .map(CommandUsage::getCommand)
                    .collect(Collectors.toList());
        }

        for (String allowedCommand : allowedCommands) {
            if (allowedCommand.startsWith(args[args.length - 1])) {
                filteredCommands.add(allowedCommand);
            }
        }

        return filteredCommands.isEmpty() ? allowedCommands : filteredCommands;
    }
}
