package net.thereturningvoid.ocsell.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class OCSellCommand implements CommandInterface {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("ocsell.help")) {
			sender.sendMessage(ChatColor.GOLD + "=====================[" + ChatColor.BLUE + "OCSell Help" + ChatColor.GOLD + "]=====================");
			if (sender.hasPermission("ocsell.reload")) {
				sender.sendMessage(ChatColor.GOLD + "/ocsell reload" + ChatColor.WHITE + ": Reload the plugin configuration.");
			}
			if (sender.hasPermission("ocsell.regenConf")) {
				sender.sendMessage(ChatColor.GOLD + "/ocsell regenConfig" + ChatColor.WHITE + ": Regenerate plugin configuration (DANGEROUS)");
			}
			if (sender.hasPermission("ocsell.create")) {
				sender.sendMessage("You can also create OCSell signs.");
			}
			sender.sendMessage(ChatColor.GOLD + "=====================================================");
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have the required permissions!");
			return false;
		}
	}

}
