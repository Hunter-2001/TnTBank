package com.ascendpvp.TnTBank.cmds;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ascendpvp.TnTBank.TnTBankMain;
import com.ascendpvp.TnTBank.utils.Helpers;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

public class TnTBankRemove implements CommandExecutor {

	TnTBankMain plugin;
	public TnTBankRemove(TnTBankMain plugin){
		this.plugin = plugin;
	}
	Helpers help = new Helpers();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		MPlayer fplayer = MPlayer.get(player);
		Faction faction = fplayer.getFaction();

		//All null checks/general checks
		if(args.length != 2) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.tntbank_incorrect_syntax")));
			return false;
		}
		if(!help.isInt(args[1])) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.arg_not_number")));
			return false;
		}
		int amount = Integer.parseInt(args[1]);
		String printAmount = args[1];
		if(amount < 1) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.add_lessthan_one")));
			return false;
		}
		if(amount >= plugin.tntbank.get(faction.getId())) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.not_enough_tnt_in_bank").replaceAll("#tntAmount#", printAmount)));
			return false;
		}
		if(plugin.tntbank.get(faction.getId()) == null) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.not_enough_tnt_in_bank").replaceAll("#tntAmount#", printAmount)));
			return false;
		}
		//Basic calculations to determine availible inventory space
		int space = 0;
		for(ItemStack item : player.getInventory().getContents()){	
			if(item == null) {
				space += 64;
			}else if(item.getType() == Material.TNT ) {
				space += 64 - item.getAmount();
			}
		}	
		String printSpace = String.valueOf(space);
		if((space >= amount)) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.no_inv_space").replaceAll("#tntAmount#", printAmount).replaceAll("#invSpaceLeft#", printSpace)));
			return false;
		}
		//Final tnt amount given
		plugin.tntbank.put(faction.getId(), plugin.tntbank.get(faction.getId()) - amount);
		player.getInventory().addItem(new ItemStack(Material.TNT, amount));
		player.sendMessage(help.cc(plugin.getConfig().getString("messages.add_tnt_to_inv").replaceAll("#tntAddedToInv#", printAmount)));

		return false;
	}

}
