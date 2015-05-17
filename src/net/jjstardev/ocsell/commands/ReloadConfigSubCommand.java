package net.jjstardev.ocsell.commands;

import net.jjstardev.ocsell.OCSell;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadConfigSubCommand implements CommandInterface {
	
	private OCSell plugin = OCSell.instance;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (sender instanceof Player) {
			if (p.hasPermission("ocsell.reload")) {
				plugin.reloadConfig();
				p.sendMessage("Config reloaded!");
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Command can only be run from ingame.");
			return false;
		}
		return false;
	}

}
