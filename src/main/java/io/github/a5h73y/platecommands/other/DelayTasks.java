package io.github.a5h73y.platecommands.other;

import io.github.a5h73y.platecommands.PlateCommands;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Delay sensitive tasks.
 */
public enum DelayTasks {

    INSTANCE;

    private final Map<String, Long> delays = new HashMap<>();

    DelayTasks() {
        initialiseCleanup();
    }

    public static DelayTasks getInstance() {
        return INSTANCE;
    }

    public boolean delayEvent(String detailKey, int secondsDelay) {
        if (!delays.containsKey(detailKey)) {
            delays.put(detailKey, System.currentTimeMillis());
            return true;
        }

        long lastAction = delays.get(detailKey);
        int secondsElapsed = (int) ((System.currentTimeMillis() - lastAction) / 1000);

        if (secondsElapsed >= secondsDelay) {
            delays.put(detailKey, System.currentTimeMillis());
            return true;
        }

        return false;
    }

    /**
     * Delay an event from firing several times.
     *
     * @param player target player
     * @return player able to perform event
     */
    public boolean delayPlayer(Player player, String detailKey, int secondsDelay) {
       return delayEvent(player.getName() + "-" + detailKey, secondsDelay);
    }

    /**
     * Clear the cleanup cache every hour.
     * To keep the size of the map relatively small.
     */
    private void initialiseCleanup() {
        new BukkitRunnable() {
            @Override
            public void run() {
                delays.clear();
            }
        }.runTaskTimer(PlateCommands.getInstance(), 0, 3600000);
    }
}
