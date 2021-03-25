package io.github.a5h73y.platecommands.plugin;

import static org.bukkit.Bukkit.getServer;

import io.github.a5h73y.platecommands.utility.PluginUtils;
import io.github.a5h73y.platecommands.utility.TranslationUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Vault Economy integration.
 * When the EconomyAPI class is initialised, an attempt is made to connect to Vault / Economy.
 * If the outcome succeeds and a provider is found, economy will be enabled.
 * If PlateCommands does not link to an Economy plugin, all attempted purchases will be successful.
 */
public class EconomyApi extends PluginWrapper {

	private Economy economy;

	@Override
	public String getPluginName() {
		return "Vault";
	}

	@Override
	protected void initialise() {
		super.initialise();

		if (isEnabled()) {
			RegisteredServiceProvider<Economy> economyProvider =
					getServer().getServicesManager().getRegistration(Economy.class);

			if (economyProvider == null) {
				PluginUtils.log("[Economy] Failed to connect to Vault's Economy service. Disabling Economy.", 2);
				setEnabled(false);
				return;
			}

			economy = economyProvider.getProvider();
		}
	}

	/**
	 * Charge the Player an amount.
	 *
	 * @param player target player
	 * @param amount amount to charge
	 * @return transaction success
	 */
	public boolean chargePlayer(Player player, double amount) {
		return this.economy.withdrawPlayer(player, amount).transactionSuccess();
	}

	/**
	 * Get the Economy Currency name.
	 *
	 * @return currency name
	 */
	public String getCurrencyName() {
		return economy.currencyNamePlural() == null ? "" : " " + economy.currencyNamePlural();
	}

	/**
	 * Display the Economy information.
	 *
	 * @param sender requesting sender
	 */
	public void displayEconomyInformation(CommandSender sender) {
		TranslationUtils.sendHeading("Economy Details", sender);
		TranslationUtils.sendValue(sender, "Enabled", Boolean.toString(isEnabled()));

		if (isEnabled()) {
			TranslationUtils.sendValue(sender, "Economy", economy.getName());
			TranslationUtils.sendValue(sender, "Currency", getCurrencyName());
		}
	}
}
