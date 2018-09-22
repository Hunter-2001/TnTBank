package com.ascendpvp.TnTBank.cmds;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.ascendpvp.TnTBank.TnTBankMain;
import com.ascendpvp.TnTBank.utils.Helpers;

import org.bukkit.command.CommandExecutor;

public class TnTBankAdminAdd implements CommandExecutor {

	TnTBankMain plugin;
	public TnTBankAdminAdd(TnTBankMain plugin) {
		this.plugin = plugin;
	}
	Helpers help = new Helpers();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Basic checks
		if (args.length != 3) return false;
		if (Bukkit.getPlayer(args[1]) == null) return false;
		if (!help.isInt(args[2])) return false;
		if (!sender.hasPermission("tntbank.adminadd")) return false;
		MPlayer fplayer = MPlayer.get((Object)Bukkit.getPlayer(args[1]));
		Faction faction = fplayer.getFaction();
		int amount = Integer.parseInt(args[2]);

		//Null checks to determine whether bank exists in memory
		if (plugin.tntbank.get(faction.getId()) == null) {
			plugin.tntbank.put(faction.getId(), amount);
			sender.sendMessage(help.cc("&b&lAdminAdd &7>&b " + amount + " &7added to current faction. New amount: &b" + plugin.tntbank.get(faction.getId())));
			return false;
		}
		plugin.tntbank.put(faction.getId(), plugin.tntbank.get(faction.getId()) + amount);
		sender.sendMessage(help.cc("&b&lAdminAdd &7>&b " + amount + " &7added to current faction. New amount: &b" + plugin.tntbank.get(faction.getId())));
		return false;
	}
}
