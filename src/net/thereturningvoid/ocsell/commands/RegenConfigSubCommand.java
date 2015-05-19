package net.thereturningvoid.ocsell.commands;

import java.io.File;

import net.thereturningvoid.ocsell.OCSell;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class RegenConfigSubCommand implements CommandInterface {

	private RegenConfigConversation conv = new RegenConfigConversation();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 1)
			return false;

		Player p = (Player) sender;
		OCSell plugin = OCSell.instance;

		if (sender instanceof Player) {
			if (plugin.getConfig() != null) {
				if (sender instanceof Conversable) {
					if (p.hasPermission("ocsell.regenConf")) {
						p.sendMessage(ChatColor.GREEN + "Are you sure you want to do this?");
						p.sendMessage(ChatColor.GREEN + "Doing this will completely reset the config and destroy\ncurrent sign group prices FOREVER.");

						conv.conv.buildConversation((Conversable) p).begin();

						return true;
					} else {
						p.sendMessage(ChatColor.DARK_RED + "For some reason the config doesn't exist :/ Try reloading the plugin/server.");
						return false;
					}
				} else {
					p.sendMessage(ChatColor.RED + "You don't have the required permissions!");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Only run this command from ingame.");
				return false;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only run this command from ingame.");
			return false;
		}
	}

	private class RegenConfigConversation implements ConversationAbandonedListener {

		public ConversationFactory conv;

		public RegenConfigConversation() {
			this.conv = new ConversationFactory(OCSell.instance).withModality(true).withFirstPrompt(new DoRegenPrompt()).withEscapeSequence(".out").addConversationAbandonedListener(this);

		}

		public void conversationAbandoned(ConversationAbandonedEvent e) {
			if (e.gracefulExit()) {
				return;
			} else {
				e.getContext().getForWhom().sendRawMessage("Config not regenerated.");
			}
		}

		private class DoRegenPrompt extends FixedSetPrompt {

			public DoRegenPrompt() {
				super("yes", "no");
			}

			@Override
			public String getPromptText(ConversationContext context) {
				return ChatColor.GREEN + "If you want to regenerate the config, type \"" + ChatColor.WHITE + "yes" + ChatColor.GREEN + "\" to\ncontinue.";
			}

			@Override
			protected Prompt acceptValidatedInput(ConversationContext context, String s) {
				if (s.equals("yes")) {
					return (Prompt) new RegenConfig();
				} else {
					return (Prompt) new NotRegenConfig();
				}

			}

			private class NotRegenConfig extends MessagePrompt {

				@Override
				public String getPromptText(ConversationContext arg0) {
					return "Config not regenerated.";
				}

				@Override
				protected Prompt getNextPrompt(ConversationContext arg0) {
					return Prompt.END_OF_CONVERSATION;
				}

			}

			private class RegenConfig extends MessagePrompt {

				@Override
				public String getPromptText(ConversationContext arg0) {
					OCSell plugin = OCSell.instance;
					File configFile = new File(plugin.getDataFolder(), "config.yml");
					configFile.delete();
					plugin.saveDefaultConfig();
					plugin.reloadConfig();

					return "Config regenerated.";
				}

				@Override
				protected Prompt getNextPrompt(ConversationContext arg0) {
					return Prompt.END_OF_CONVERSATION;
				}

			}
		}

	}
}
