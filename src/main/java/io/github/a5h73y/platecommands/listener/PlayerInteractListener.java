package io.github.a5h73y.platecommands.listener;

import io.github.a5h73y.platecommands.PlateCommands;
import io.github.a5h73y.platecommands.enums.Permission;
import io.github.a5h73y.platecommands.other.AbstractPluginReceiver;
import io.github.a5h73y.platecommands.other.DelayTasks;
import io.github.a5h73y.platecommands.type.PlateAction;
import io.github.a5h73y.platecommands.utility.PermissionUtils;
import io.github.a5h73y.platecommands.utility.TranslationUtils;
import org.bukkit.Bukkit;
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
    public void onPlateActionEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock() == null) {
            return;
        }

        Block below = event.getClickedBlock().getRelative(BlockFace.DOWN);

        if (below.getType() != plateCommands.getConfig().getPlateActionBlockMaterial()) {
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
                    plateCommands.getPlateActionManager().executePlateAction(event.getPlayer(), action));
        }
    }

    /**
     * On pressure plate break event.
     * @param event block break event
     */
    @EventHandler
    public void onPlateBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().name().endsWith("PRESSURE_PLATE")
                && !event.getPlayer().isSneaking()
                && plateCommands.getPlateActionManager().getPlateAction(event.getBlock().getLocation()) != null) {
            if (!PermissionUtils.hasPermission(event.getPlayer(), Permission.ADMIN_DELETE)) {
                event.setCancelled(true);

            } else {
                plateCommands.getPlateActionManager().deletePlateAction(event.getBlock().getLocation());
                TranslationUtils.sendTranslation("PlateAction.Deleted", event.getPlayer());
            }
        }
    }
}
