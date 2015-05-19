package net.thereturningvoid.ocsell.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {

	private static HashMap<String, CommandInterface> commands = new HashMap<String, CommandInterface>();
	
	public void register(String name, CommandInterface cmd) {
		commands.put(name, cmd);
	}
	
	public boolean exists(String name) {
		return commands.containsKey(name);
	}
	
	public CommandInterface getExcecutor(String name) {
		return commands.get(name);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			getExcecutor("ocsell").onCommand(sender, cmd, label, args);
			return true;
		}
		
		if (args.length > 0) {
			if (exists(args[0])) {
				getExcecutor(args[0]).onCommand(sender, cmd, label, args);
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Nooooooope. Try /ocsell for a list of commands.");
			return true;
		}
		return false;
	}
}
