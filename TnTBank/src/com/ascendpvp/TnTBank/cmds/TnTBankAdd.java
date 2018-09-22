package com.ascendpvp.TnTBank.cmds;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.ascendpvp.TnTBank.TnTBankMain;
import com.ascendpvp.TnTBank.utils.Helpers;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;


public class TnTBankAdd implements CommandExecutor {

	TnTBankMain plugin;
	public TnTBankAdd(TnTBankMain plugin) {
		this.plugin = plugin;
	}
	Helpers help = new Helpers();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;
		if(args.length == 2) {
			printHelp(p);
		}
		MPlayer fplayer = MPlayer.get(p);
		Faction faction = fplayer.getFaction();
		
		/*
		 * 
		 * If input is an Integer
		 * 
		 */
		if(help.isInt(args[1])) {
			int amount = Integer.parseInt(args[1]);
			if(amount < 1) {
				p.sendMessage(help.cc(plugin.getConfig().getString("messages.add_lessthan_one")));
				return false;
			}
			if(!(amount > 2304)) {
				p.sendMessage(help.cc(this.plugin.getConfig().getString("messages.max_inv_slots")));
				return false;
			}

			//Basic calculations to determine amount of tnt in selected inventory
			int tntInInv = 0;
			for(ItemStack tnt : p.getInventory().getContents()) {
				if(tnt == null) continue;
				if(tnt.getType() != Material.TNT) continue;
				if(tnt.hasItemMeta()) {
					p.sendMessage(help.cc(plugin.getConfig().getString("messages.custom_tnt")));
					return false;
				}
				tntInInv += tnt.getAmount();
			}
			
			//Check if tnt in inventory is greater than amount input
			if(!(tntInInv >= amount)) {
				p.sendMessage(help.cc(this.plugin.getConfig().getString("messages.not_enough_tnt_in_inv")));
				return false;
			}

			//Put desired amount into HashMap
			p.getInventory().removeItem(new ItemStack(Material.TNT, amount));
			if(plugin.tntbank.get(faction.getId()) == null) {
				plugin.tntbank.put(faction.getId(), amount);
				String am = args[1];
				p.sendMessage(help.cc(plugin.getConfig().getString("messages.added").replaceAll("#amountAdded#", am).replaceAll("#totalAmount#", plugin.tntbank.get(faction.getId()).toString())));
				return false;
			}
			plugin.tntbank.put(faction.getId(), plugin.tntbank.get(faction.getId()) + amount);
			String am = args[1];
			p.sendMessage(help.cc(plugin.getConfig().getString("messages.added").replaceAll("#amountAdded#", am).replaceAll("#totalAmount#", plugin.tntbank.get(faction.getId()).toString())));
			//
			
			
			/*
			 * 
			 * If input is "ALL"
			 * 
			 */
		} else if(args[1].equalsIgnoreCase("all")) {

			//Basic calculations to determine amount of tnt in selected inventory
			Inventory inventoryToCheck = p.getInventory();
			int allTntAdd = 0;
			for(ItemStack is : inventoryToCheck.getContents()) {
				if(is == null) continue;
				if(is.getType() != Material.TNT) continue;
				if(!(is.hasItemMeta())) continue;
				int allTntAmount = is.getAmount();
				allTntAdd += allTntAmount;
				inventoryToCheck.remove(is);
			}
			
			//Check to see if player already has a balance or not
			if(plugin.tntbank.get(faction.getId()) == null) {
				plugin.tntbank.put(faction.getId(), allTntAdd);
				String allTntAddString = String.valueOf(allTntAdd);
				p.sendMessage(help.cc(plugin.getConfig().getString("messages.all_added").replaceAll("#amountAdded#", allTntAddString).replaceAll("#totalAmount#", plugin.tntbank.get(faction.getId()).toString())));
				return false;
			}
			plugin.tntbank.put(faction.getId(), plugin.tntbank.get(faction.getId()) + allTntAdd);
			String allTntAddString = String.valueOf(allTntAdd);
			p.sendMessage(help.cc(plugin.getConfig().getString("messages.all_added").replaceAll("#amountAdded#", allTntAddString).replaceAll("#totalAmount#", plugin.tntbank.get(faction.getId()).toString())));
		}else {
			p.sendMessage(help.cc(plugin.getConfig().getString("messages.arg_not_number")));
		}
		//
		return false;
	}

	private boolean printHelp(Player player) {
		List<String> helper = (List<String>)this.plugin.getConfig().getStringList("help");
		for (String line : helper) {
			String formatted = help.cc(line);
			player.sendMessage(formatted);
		}
		return true;
	}
}