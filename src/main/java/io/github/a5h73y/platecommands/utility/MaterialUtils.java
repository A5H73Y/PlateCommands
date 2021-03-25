package io.github.a5h73y.platecommands.utility;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class MaterialUtils {

	/**
	 * Get Player's Target block.
	 * Find the first block in the player's view.
	 * @param player player
	 * @param range how many blocks ahead
	 * @return target block
	 */
	public static Block getTargetBlock(Player player, int range) {
		BlockIterator iter = new BlockIterator(player, range);
		Block lastBlock = iter.next();
		while (iter.hasNext()) {
			lastBlock = iter.next();
			if (lastBlock.getType() != Material.AIR) {
				break;
			}
		}
		return lastBlock;
	}

}
