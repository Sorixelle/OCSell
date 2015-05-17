package net.jjstardev.ocsell.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MiscUtil {

	public String removeMCFormatting(String s) {
		String ns = s.replaceAll("\u00a7[0-9a-fA-F]", "");
		return ns;
	}
	
	public void clearChatLines(int lines, Player p) {
		for (int i = 0; i < lines; i++) {
			p.sendMessage("");
		}
	}
	
	public void clearChatLines(int lines, CommandSender sender) {
		for (int i = 0; i < lines; i++) {
			sender.sendMessage("");
		}
	}
}
