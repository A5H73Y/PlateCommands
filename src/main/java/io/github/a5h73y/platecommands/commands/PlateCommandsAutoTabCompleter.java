package io.github.a5h73y.platecommands.commands;

import io.github.a5h73y.platecommands.PlateCommands;
import io.github.a5h73y.platecommands.enums.Permission;
import io.github.a5h73y.platecommands.other.AbstractPluginReceiver;
import io.github.a5h73y.platecommands.utility.PermissionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Tab auto-completion for PlateCommands commands.
 */
public class PlateCommandsAutoTabCompleter extends AbstractPluginReceiver implements TabCompleter {

    private static final List<String> NO_PERMISSION_COMMANDS = Arrays.asList("");

    private static final List<String> ADMIN_ONLY_COMMANDS = Arrays.asList("reload");

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
            allowedCommands = populateMainCommands(player);
        }

        for (String allowedCommand : allowedCommands) {
            if (allowedCommand.startsWith(args[args.length - 1])) {
                filteredCommands.add(allowedCommand);
            }
        }

        return filteredCommands.isEmpty() ? allowedCommands : filteredCommands;
    }

    /**
     * Populate the main command options.
     * @param player player
     * @return allowed commands
     */
    private List<String> populateMainCommands(Player player) {
        List<String> allowedCommands = new ArrayList<>(NO_PERMISSION_COMMANDS);

        // basic commands
        if (PermissionUtils.hasPermission(player, Permission.BASIC_INFO, false)) {
            allowedCommands.add("create");
        }

        if (PermissionUtils.hasPermission(player, Permission.ADMIN_ALL, false)) {
            allowedCommands.addAll(ADMIN_ONLY_COMMANDS);
        }

        return allowedCommands;
    }
}
