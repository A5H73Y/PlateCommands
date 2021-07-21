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

    /**
     * Delay the event based on detail key.
     * If the detail already exists in the cooldown, check if the required seconds have passed.
     * This allows each pressure plate / player to have a different cooldown amount.
     * @param detailKey detail key
     * @param secondsDelay seconds delay
     * @return event should be delayed
     */
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
        }.runTaskTimer(PlateCommands.getInstance(), 0, 3_600_000);
    }
}
