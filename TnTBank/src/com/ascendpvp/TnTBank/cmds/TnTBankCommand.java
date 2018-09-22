package com.ascendpvp.TnTBank.cmds;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.ascendpvp.TnTBank.TnTBankMain;
import org.bukkit.command.CommandExecutor;

public class TnTBankCommand implements CommandExecutor {

	TnTBankMain plugin;
	public TnTBankCommand(TnTBankMain plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("tntbank")) return false;
		if (!(sender instanceof Player)) {
			System.out.println(plugin.getConfig().getString("messages.sender_console"));
			return false;
		}
		Player player = (Player)sender;
		MPlayer fplayer = MPlayer.get((Object)player);
		Faction faction = fplayer.getFaction();
		Rel role = fplayer.getRole();
		if (args.length == 0) {
			this.printHelp(player);
			return false;
		}
		//Check if player is in a faction
		if (faction.getName().equals("Wilderness") || fplayer.getFaction().isNone()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.faction_wilderness")));
			return false;
		}
		//Switch for first arg of command
		switch (args[0].toLowerCase()) {
		case "tntfill": {
			if (fplayer.getRole().isAtLeast(Rel.OFFICER)) {
				return new TnTBankFill(plugin).onCommand(sender, cmd, label, args);
			}
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.not_fac_mod")));
			return false;
		}
		case "remove": {
			if (role.isAtLeast(Rel.OFFICER)) {
				return new TnTBankRemove(plugin).onCommand(sender, cmd, label, args);
			}
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.not_fac_mod")));
			return false;
		}
		case "add": {
			return new TnTBankAdd(plugin).onCommand(sender, cmd, label, args);
		}
		case "bal": {
			return new TnTBankBal(plugin).onCommand(sender, cmd, label, args);
		}
		case "chestmode": {
			if (plugin.cMode.contains(player.getName())) {
				plugin.cMode.remove(player.getName());
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.chestmode_exit")));
				return false;
			}
			plugin.cMode.add(player.getName());
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.chestmode_enter")));
			return false;
		}
		case "adminadd": {
			return new TnTBankAdminAdd(plugin).onCommand(sender, cmd, label, args);
		}
		default: 
			printHelp(player);
			break;
		}
		return false;
	}

	private boolean printHelp(Player player) {
		List<String> help = (List<String>)plugin.getConfig().getStringList("help");
		for (String line : help) {
			String formatted = ChatColor.translateAlternateColorCodes('&', line);
			player.sendMessage(formatted);
		}
		return true;
	}
}
