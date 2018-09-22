package com.ascendpvp.TnTBank.cmds;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.ascendpvp.TnTBank.TnTBankMain;
import com.ascendpvp.TnTBank.utils.Helpers;

import org.bukkit.command.CommandExecutor;

public class TnTBankBal implements CommandExecutor {

	TnTBankMain plugin;    
	public TnTBankBal(TnTBankMain plugin) {
		this.plugin = plugin;
	}
	Helpers help = new Helpers();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player player = (Player)sender;
		MPlayer fplayer = MPlayer.get((Object)player);
		Faction faction = fplayer.getFaction();
		/*
		 * 
		 * If player checks own tnt balance
		 * 
		 */
		if (args.length == 1) {
			//Null checks to determine whether bank exists in memory
			if (plugin.tntbank.get(faction.getId()) == null) {
				player.sendMessage(help.cc(plugin.getConfig().getString("messages.player_has_no_tntbank")));
				return false;
			}
			String amount = plugin.tntbank.get(faction.getId()).toString();
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.tnt_bal").replaceAll("#tntAmount#", amount)));

			/*
			 * 
			 * If player checks other faction tnt balance
			 * 
			 */
		}else if (args.length == 2) {
			String otherFaction = args[1];
			Faction otherfac = FactionColl.get().getByName(otherFaction);
			//Determine if faction input is a real faction
			if (otherfac == null) {
				player.sendMessage(help.cc(plugin.getConfig().getString("messages.fac_not_exist")));
				return false;
			}
			//Null checks to determine whether bank exists in memory
			if (plugin.tntbank.get(otherfac.getId()) == null) {
				player.sendMessage(help.cc(plugin.getConfig().getString("messages.fac_no_tntbank")));
				return false;
			}
			String amountOfOtherFac = plugin.tntbank.get(otherfac.getId()).toString();
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.otherfac_tnt_count").replaceAll("#otherFacName#", otherFaction).replaceAll("#amountOfOtherFac#", amountOfOtherFac)));
		}
		return false;
	}
}
