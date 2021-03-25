package io.github.a5h73y.platecommands.other;

import io.github.a5h73y.platecommands.utility.TranslationUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandUsage {

	private String command;

	private String title;

	private String arguments;

	private String example;

	private String description;

	private String permission;

	/**
	 * Display Command help information.
	 * @param sender command sender
	 */
	public void displayHelpInformation(CommandSender sender) {
		TranslationUtils.sendHeading(title, sender);
		String commandSyntax = arguments != null ? command + " " + arguments : command;
		TranslationUtils.sendValueTranslation("Help.CommandSyntax", commandSyntax, false, sender);
		TranslationUtils.sendValueTranslation("Help.CommandExample", example, false, sender);
		TranslationUtils.sendHeading("Description", sender);
		sender.sendMessage(description);
	}

	/**
	 * Display command usage details.
	 * @param sender command sender
	 */
	public void displayCommandUsage(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/pc " + ChatColor.YELLOW + command
				+ (arguments != null ? ChatColor.YELLOW + " " + arguments : "")
				+ ChatColor.BLACK + " : " + ChatColor.WHITE + title);
	}

	public String getCommand() {
		return command;
	}

	public String getTitle() {
		return title;
	}

	public String getArguments() {
		return arguments;
	}

	public String getExample() {
		return example;
	}

	public String getDescription() {
		return description;
	}

	public String getPermission() {
		return permission;
	}

}
