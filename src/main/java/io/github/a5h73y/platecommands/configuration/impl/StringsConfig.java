package io.github.a5h73y.platecommands.configuration.impl;

import io.github.a5h73y.platecommands.configuration.PlateCommandsConfiguration;

public class StringsConfig extends PlateCommandsConfiguration {

	@Override
	protected String getFileName() {
		return "strings.yml";
	}

	@Override
	protected void initializeConfig() {
		this.addDefault("PlateCommands.Prefix", "&0[&6Plate&eCommands&0] &f");
		this.addDefault("PlateCommands.ConfigReloaded", "The config has been reloaded.");
		this.addDefault("PlateCommands.Heading", "-- &9&l%VALUE% &r--");

		this.addDefault("Error.TooMany", "Too many arguments! (%VALUE%)");
		this.addDefault("Error.TooLittle", "Not enough arguments! (%VALUE%)");
		this.addDefault("Error.Syntax", "&cInvalid Syntax: &f/pc &8%COMMAND% &7%ARGUMENTS%");
		this.addDefault("Error.UnknownCommand", "Unknown command!");
		this.addDefault("Error.UnknownMaterial", "Unknown Material: &4%VALUE%");
		this.addDefault("Error.PluginNotLinked", "&b%VALUE% &fhas not been linked.");
		this.addDefault("Error.NoPermission", "You do not have Permission: &b%VALUE%");
		this.addDefault("Error.InvalidAmount", "Amount needs to be numeric.");

		this.addDefault("Help.Command", "&7/pc help &9%VALUE% &0: &7To learn more about the command.");
		this.addDefault("Help.Commands", "To display the commands menu, enter &b/pc cmds");
		this.addDefault("Help.CommandSyntax", "&7Syntax: &f/pa %VALUE%");
		this.addDefault("Help.ConsoleCommandSyntax", "&7Syntax: &f%VALUE%");
		this.addDefault("Help.CommandExample", "&7Example: &f%VALUE%");
		this.addDefault("Help.CommandUsage", "&3/pc &b%COMMAND%&e%ARGUMENTS% &0: &f%TITLE%");

		this.addDefault("Other.PropertySet", "The &3%PROPERTY% &ffor &3%COURSE% &fwas set to &b%VALUE%&f!");

		this.addDefault("Economy.Insufficient", "You require at least &b%AMOUNT% &fbefore joining &b%COURSE%");
		this.addDefault("Economy.Fee", "&b%AMOUNT% &fhas been deducted from your balance for joining &b%COURSE%");
		this.addDefault("Economy.Reward", "You earned &b%AMOUNT% &ffor completing &b%COURSE%&f!");

		this.options().copyDefaults(true);
	}
}
