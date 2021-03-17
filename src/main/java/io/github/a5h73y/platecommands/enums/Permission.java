package io.github.a5h73y.platecommands.enums;

/**
 * All PlateCommands related permissions.
 */
public enum Permission {

	// All Permissions
	PLATE_COMMANDS_ALL("PlateCommands", "*"),

	// PlateCommands Basic
	BASIC_ALL(Constants.PLATE_COMMANDS_BASIC, "*"),
	BASIC_CREATE(Constants.PLATE_COMMANDS_BASIC, "Create"),
	BASIC_INFO(Constants.PLATE_COMMANDS_BASIC, "Info"),

	// PlateCommands Admins
	ADMIN_ALL(Constants.PLATE_COMMANDS_ADMIN, "*"),
	ADMIN_DELETE(Constants.PLATE_COMMANDS_ADMIN, "Delete");

	private final String permissionNode;
	private final String permissionRoot;

	Permission(String permissionRoot, String permissionNode) {
		this.permissionRoot = permissionRoot;
		this.permissionNode = permissionNode;
	}

	public String getPermissionRoot() {
		return permissionRoot;
	}

	public String getPermissionNode() {
		return permissionNode;
	}

	public String getPermission() {
		return permissionRoot + "." + permissionNode;
	}

	private static class Constants {
		private static final String PLATE_COMMANDS_BASIC = "PlateCommands.Basic";
		private static final String PLATE_COMMANDS_ADMIN = "PlateCommands.Admin";
	}
}

