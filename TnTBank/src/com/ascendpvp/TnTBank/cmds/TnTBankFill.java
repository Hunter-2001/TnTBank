package com.ascendpvp.TnTBank.cmds;

import java.util.List;
import org.bukkit.block.Block;
import com.massivecraft.factions.entity.Faction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import java.util.ArrayList;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.ascendpvp.TnTBank.TnTBankMain;
import com.ascendpvp.TnTBank.utils.Helpers;

import org.bukkit.command.CommandExecutor;

public class TnTBankFill implements CommandExecutor {

	TnTBankMain plugin;
	public TnTBankFill(TnTBankMain plugin) {
		this.plugin = plugin;
	}
	Helpers help = new Helpers();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player player = (Player)sender;
		MPlayer fPlayer = MPlayer.get((Object)player);
		Faction pFaction = fPlayer.getFaction();
		Faction fpLoc = BoardColl.get().getFactionAt(PS.valueOf(player.getPlayer().getLocation()));

		//All null checks/general checks
		if (!player.hasPermission("tntbank.tntfill")) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.no_permission_tntfill")));
			return false;
		}
		if (args.length != 3) {
			this.printHelp(player);
			return false;
		}
		if (plugin.tntbank.get(pFaction.getId()) == null) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.player_has_no_tntbank")));
			return false;
		}
		if (!fpLoc.getMPlayers().contains(fPlayer)) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.tntfill_not_own_territory")));
			return false;
		}
		if (!help.isInt(args[1])) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.arg_not_number")));
			return false;
		}
		if (help.isInt(args[2])) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.radius_not_int")));
			return false;
		}
		int fillAmount = Integer.parseInt(args[1]);
		if (fillAmount <= 0) {
			this.printHelp(player);
			return false;
		}
		int radius = Integer.parseInt(args[2]);
		if (radius <= 0) {
			this.printHelp(player);
			return false;
		}
		int maxradius = Integer.parseInt(plugin.getConfig().getString("max_radius"));
		if (maxradius >= radius) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.max_radius").replaceAll("#maxRadius#", plugin.getConfig().getString("max_radius"))));
			return false;
		}

		//Calculations to determine amount of dispensers in a 3D cubical shape
		ArrayList<Dispenser> dispensers = new ArrayList<Dispenser>();
		for (int x = -radius; x <= radius; ++x) {
			for (int y = -radius; y <= radius; ++y) {
				for (int z = -radius; z <= radius; ++z) {
					Block block = player.getWorld().getBlockAt(player.getLocation()).getRelative(x, y, z);
					if (block.getType() != Material.DISPENSER) continue;
					dispensers.add((Dispenser)block.getState());
				}
			}
		}

		if ((fillAmount * dispensers.size() <= plugin.tntbank.get(pFaction.getId()))) {
			player.sendMessage(help.cc(plugin.getConfig().getString("messages.no_tnt_to_fill")));
			return false;
		}
		
		//Remove tnt amount from HashMap and add tnt to dispensers in ArrayList
		plugin.tntbank.put(pFaction.getId(), plugin.tntbank.get(pFaction.getId()) - fillAmount * dispensers.size());
		for (Dispenser dispenser : dispensers) {
			dispenser.getInventory().addItem(new ItemStack[] { new ItemStack(Material.TNT, fillAmount) });
		}
		String dispensersSize = String.valueOf(dispensers.size());
		String tntAmount = String.valueOf(fillAmount);
		player.sendMessage(help.cc(plugin.getConfig().getString("messages.tntfill_successful").replaceAll("#dispenserCount#", dispensersSize).replaceAll("#tntAmount#", tntAmount)));
		return false;
	}
	
	private boolean printHelp(Player player) {
		List<String> helper = (List<String>)plugin.getConfig().getStringList("help");
		for (String line : helper) {
			String formatted = help.cc(line);
			player.sendMessage(formatted);
		}
		return true;
	}
}
