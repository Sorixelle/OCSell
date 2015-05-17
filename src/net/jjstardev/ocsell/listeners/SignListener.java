package net.jjstardev.ocsell.listeners;

import java.util.ArrayList;
import java.util.List;

import net.jjstardev.ocsell.OCSell;
import net.jjstardev.ocsell.util.InventoryUtil;
import net.jjstardev.ocsell.util.MiscUtil;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SignListener implements Listener {

	private Economy econ = OCSell.econ;
	private OCSell mainClass = OCSell.instance;
	private String prefix = ChatColor.GOLD + "[" + ChatColor.BLUE + "OCSell" + ChatColor.GOLD + "] ";

	private static final MiscUtil mutil = new MiscUtil();
	private static final InventoryUtil iutil = new InventoryUtil();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block blockClicked = e.getClickedBlock();

			if (blockClicked.getType() == Material.WALL_SIGN || blockClicked.getType() == Material.SIGN_POST) {
				Sign s = (Sign) blockClicked.getState();

				String sLine2 = mutil.removeMCFormatting(s.getLine(1));
				if (mutil.removeMCFormatting(s.getLine(0)).equals("[SellAll]")) {
					if (!sLine2.equals("")) {

						Inventory pInv = p.getInventory();
						double creditToBalance = 0;

						List<Material> materials = new ArrayList<Material>();

						for (int i = 1; i <= mainClass.getConfig().getConfigurationSection("groups." + sLine2).getKeys(false).size(); i++) {
							ConfigurationSection section = mainClass.getConfig().getConfigurationSection("groups." + sLine2 + ".item" + i);
							int type = section.getInt("type");
							int amount = section.getInt("amount");
							ItemStack sellableItems = new ItemStack(type);
							Material material = sellableItems.getType();
							Integer[] amounts = iutil.getItemStackAmountAsArray(p, material);
							if (amount != 0) {
								materials.add(material);
								for (Integer amt : amounts) {
									creditToBalance = creditToBalance + (amount * amt);
								}
							}
						}
						if (creditToBalance > 0) {
							EconomyResponse r = econ.depositPlayer(p, creditToBalance);
							if (r.transactionSuccess()) {
								mutil.clearChatLines(5, p);
								p.sendMessage(ChatColor.GOLD + "=====================" + ChatColor.DARK_BLUE + "[" + ChatColor.GREEN + "Sold Items" + ChatColor.DARK_BLUE + "]" + ChatColor.GOLD + "======================");
								for (int i = 1; i <= mainClass.getConfig().getConfigurationSection("groups." + sLine2).getKeys(false).size(); i++) {
									ConfigurationSection section = mainClass.getConfig().getConfigurationSection("groups." + sLine2 + ".item" + i);
									int type = section.getInt("type");
									int amount = section.getInt("amount");
									ItemStack sellableItems = new ItemStack(type);
									Material material = sellableItems.getType();
									int amounts = iutil.getItemStackAmount(p, material);
									p.sendMessage("");
								   p.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "    $" + amounts * amount + ChatColor.RESET + ChatColor.DARK_AQUA + " for " + ChatColor.RESET + ChatColor.BLUE + ChatColor.BOLD + amounts + "x " + material.toString() );
								}
								p.sendMessage("");
								p.sendMessage(ChatColor.GREEN + "     Total money added to balance: " + ChatColor.BOLD + "$" + creditToBalance);
								p.sendMessage("");
								p.sendMessage(ChatColor.GOLD + "=====================================================");
								for (Material material : materials) {
									for (int j = 1; j <= 64; j++) {
										pInv.remove(new ItemStack(material, j));
										p.updateInventory();
									}
								}

							} else {
								p.sendMessage(String.format(prefix + ChatColor.RED + "An error occured: %s", r.errorMessage));
							}
						} else {
							p.sendMessage(ChatColor.RED + "You do not have the required items to sell!");
						}
					}
				}
			}
		}

		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			Block punchedBlock = e.getClickedBlock();

			if (punchedBlock.getType() == Material.WALL_SIGN || punchedBlock.getType() == Material.SIGN_POST) {
				Sign s = (Sign) punchedBlock.getState();
				String sLine2 = mutil.removeMCFormatting(s.getLine(1));
				if (mutil.removeMCFormatting(s.getLine(0)).equals("[SellAll]")) {
					if (!sLine2.equals("")) {
						FileConfiguration config = OCSell.instance.getConfig();
						mutil.clearChatLines(5, p);
						p.sendMessage(ChatColor.GOLD + "=====================" + ChatColor.DARK_BLUE + "[" + ChatColor.GREEN + "Sell Prices" + ChatColor.DARK_BLUE + "]" + ChatColor.GOLD + "======================");
						for (int i = 1; i <= config.getConfigurationSection("groups." + sLine2).getKeys(false).size(); i++) {
							ConfigurationSection section = config.getConfigurationSection("groups." + sLine2 + ".item" + i);
							int type = section.getInt("type");
							int price = section.getInt("amount") * 64;
							ItemStack itemsAsStack = new ItemStack(type);
							Material itemsAsMaterial = itemsAsStack.getType();
							String itemName = itemsAsMaterial.toString();
							p.sendMessage("");
							p.sendMessage("" + ChatColor.BLUE + ChatColor.BOLD + "     $" + price + "" + ChatColor.RESET + ChatColor.DARK_AQUA + " for one stack of: " + ChatColor.BLUE + ChatColor.BOLD + "" + itemName);
						}
						p.sendMessage("");
						p.sendMessage(ChatColor.GOLD + "=====================================================");
					}
				}
			}
		}
	}

	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		Player p = e.getPlayer();
		Block placedSign = e.getBlock();
		if (placedSign.getType().equals(Material.SIGN_POST) || placedSign.getType().equals(Material.WALL_SIGN)) {
			if (mutil.removeMCFormatting(e.getLine(0)).endsWith("[SellAll]")) {
				if (p.hasPermission("ocsell.create")) {
					String sLine2 = mutil.removeMCFormatting(e.getLine(1));
					if (mainClass.getConfig().getString("groups." + sLine2 + ".item1.type") != null) {
						if (mainClass.getConfig().getString("groups." + sLine2 + ".item1.amount") != null) {
							p.sendMessage(prefix + "OCSell sign created!");
						} else {
							p.sendMessage(prefix + ChatColor.RED + "Invalid group specified! Check the config!");
							placedSign.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
							e.setCancelled(true);
						}
					} else {
						p.sendMessage(prefix + ChatColor.RED + "Invalid group specified! Check the config!");
						placedSign.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
						e.setCancelled(true);
					}
				} else {
					p.sendMessage(prefix + ChatColor.RED + "You do not have required permissions!");
					placedSign.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onSignBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block brokenSign = e.getBlock();
		if (brokenSign.getType().equals(Material.SIGN_POST) || brokenSign.getType().equals(Material.WALL_SIGN)) {
			Sign s = (Sign) brokenSign.getState();
			if (mutil.removeMCFormatting(s.getLine(0)).equals("[SellAll]")) {
				if (p.isSneaking()) {
					p.sendMessage(prefix + ChatColor.GREEN + "You broke a sell sign!");
					brokenSign.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
				} else {
					p.sendMessage(prefix + ChatColor.RED + "You must be sneaking to break SellAll signs!");
					e.setCancelled(true);
				}
			}
		}
	}

}
