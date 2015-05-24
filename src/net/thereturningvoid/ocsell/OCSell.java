package net.thereturningvoid.ocsell;

import java.io.File;
import java.util.logging.Logger;

import net.thereturningvoid.ocsell.commands.CommandHandler;
import net.thereturningvoid.ocsell.commands.DebugSubCommand;
import net.thereturningvoid.ocsell.commands.OCSellCommand;
import net.thereturningvoid.ocsell.commands.RegenConfigSubCommand;
import net.thereturningvoid.ocsell.commands.ReloadConfigSubCommand;
import net.thereturningvoid.ocsell.listeners.SignListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class OCSell extends JavaPlugin {
	
	public static OCSell instance;
	private static final Logger log = Logger.getLogger("Minecraft");
	
	public static Economy econ = null;
	public static Permission perm = null;
	
	@Override
	public void onEnable() {
		instance = this;
		createConfig();
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - This plugin requires Vault to work!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		setupPermissions();
		this.registerCommands();
		getServer().getPluginManager().registerEvents(new SignListener(), instance);
		log.info(String.format("[%s] Enabled OCSell version %s", getDescription().getName(), getDescription().getVersion()));
	}
	
	@Override
	public void onDisable() {
		instance = null;
		log.info(String.format("[%s] Disabled OCSell version %s", getDescription().getName(), getDescription().getVersion()));
	}
	
	public void registerCommands() {
		CommandHandler handler = new CommandHandler();
		
		// Register main command
		handler.register("ocsell", new OCSellCommand());
		
		// Register regenConfig subcommand
		handler.register("regenConfig", new RegenConfigSubCommand());
		
		// Register reload subcommand
		handler.register("reload", new ReloadConfigSubCommand());
		
		// Register debug subcommand
		handler.register("debug", new DebugSubCommand());
		getCommand("ocsell").setExecutor(handler);
		
	}
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perm = rsp.getProvider();
		return perm != null;
	}
	
	private void createConfig() {
		try {
			File configFile = new File(getDataFolder(), "config.yml");
			if (!getDataFolder().exists()) {
				getDataFolder().mkdirs();
			}
			if (!configFile.exists()) {
				log.info(String.format("[%s] config.yml not found, creating", getDescription().getName()));
				saveDefaultConfig();
			} else {
				log.info(String.format("[%s] Loading config.yml", getDescription().getName()));
			}
		} catch (Exception e) {
			log.severe(String.format("[%s] Error creating/loading config!", getDescription().getName()));
			e.printStackTrace();
		}
	}
}
