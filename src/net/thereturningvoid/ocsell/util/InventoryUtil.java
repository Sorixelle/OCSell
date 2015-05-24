package net.thereturningvoid.ocsell.util;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
	
	public Integer[] getItemStackAmountAsArray(Player player, Material item) {
		List<Integer> list = new ArrayList<Integer>();
		@SuppressWarnings("unchecked")
		Collection<ItemStack> values = (Collection<ItemStack>) player.getInventory().all(item).values();
		for (ItemStack is : values) {
			list.add(is.getAmount());
		}
		Integer[] array = list.toArray(new Integer[list.size()]);
		return array;
	}
	
	public int getItemStackAmount(Player player, Material item) {
		int amount = 0;
		for (ItemStack is : player.getInventory().all(item).values()) {
			amount = amount + is.getAmount();
		}
		return amount;
	}
	
}
