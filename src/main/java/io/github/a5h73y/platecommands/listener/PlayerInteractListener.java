package io.github.a5h73y.platecommands.listener;

import io.github.a5h73y.platecommands.PlateCommands;
import io.github.a5h73y.platecommands.enums.Permission;
import io.github.a5h73y.platecommands.other.AbstractPluginReceiver;
import io.github.a5h73y.platecommands.other.DelayTasks;
import io.github.a5h73y.platecommands.type.PlateAction;
import io.github.a5h73y.platecommands.utility.PermissionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener extends AbstractPluginReceiver implements Listener {

    public PlayerInteractListener(final PlateCommands plateCommands) {
        super(plateCommands);
    }

    /**
     * Handle Player Interaction Event.
     * Used to handle the pressure plate interaction while NOT on a Course.
     * This is used to identify if the plate matches an AutoStart location.
     *
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onAutoStartEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock() == null) {
            return;
        }

        Block below = event.getClickedBlock().getRelative(BlockFace.DOWN);

        if (below.getType() != plateCommands.getConfig().getPlateActionMaterial()) {
            return;
        }

        if (plateCommands.getConfig().getPreventPlateStick()) {
            event.setCancelled(true);
        }

        if (!DelayTasks.getInstance().delayPlayer(event.getPlayer(), "Interact", 1)) {
            return;
        }

        PlateAction action = plateCommands.getPlateActionManager().getPlateAction(event.getClickedBlock().getLocation());

        if (action != null) {
            Bukkit.getScheduler().runTask(plateCommands, () ->
                    plateCommands.getPlateActionManager().execute(event.getPlayer(), action));
        }
    }

    @EventHandler
    public void onPlateBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.STONE_PRESSURE_PLATE
                && plateCommands.getPlateActionManager().getPlateAction(event.getBlock().getLocation()) != null) {
            if (!PermissionUtils.hasPermission(event.getPlayer(), Permission.ADMIN_DELETE)) {
                event.setCancelled(true);
            } else {
                event.getPlayer().sendMessage("deleted");
                plateCommands.getPlateActionManager().deletePlateAction(event.getBlock().getLocation());
            }
        }
    }
}
