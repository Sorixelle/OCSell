package net.jjstardev.ocsell.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DebugSubCommand implements CommandInterface {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage("Nothing to debug at this time");
		return true;
	}

}