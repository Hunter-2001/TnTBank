package com.ascendpvp.TnTBank.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

import com.ascendpvp.TnTBank.TnTBankMain;
import com.ascendpvp.TnTBank.utils.Helpers;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

public class ChestSelect implements Listener {

	TnTBankMain plugin;
	public ChestSelect(TnTBankMain plugin) {
		this.plugin = plugin;
	}
	Helpers help = new Helpers();

	@EventHandler
	public void playerInteract(PlayerInteractEvent e) {

		String eName = e.getPlayer().getName();
		Player p = e.getPlayer();
		MPlayer fplayer = MPlayer.get(p);
		Faction faction = fplayer.getFaction();
		Block blockClicked = e.getClickedBlock();

		//Basic checks
		if(!plugin.cMode.contains(eName)) return;
		if(e.getAction() != Action.LEFT_CLICK_BLOCK) return;
		if(blockClicked == null) return;
		if(!(blockClicked.getState() instanceof Chest)) return;

		//Basic calculations to determine contents of chests
		Chest chestToCheck = (Chest) blockClicked.getState();
		int amountToAdd = 0;
		ItemStack[] arrayOfItemStack;
		int i = (arrayOfItemStack = chestToCheck.getInventory().getContents()).length;
		for (int y = 0; y < i; y++) {
			ItemStack z = arrayOfItemStack[y];
			if (z == null) continue;
			if (!z.getType().equals(Material.TNT)) continue;
			amountToAdd += z.getAmount();
			chestToCheck.getInventory().remove(new ItemStack(Material.TNT, z.getAmount()));
		}

		//Null checks to determine whether bank exists in memory
		String amountToAddMsg = String.valueOf(amountToAdd);
		if(plugin.tntbank.get(faction.getId()) == null) {
			plugin.tntbank.put(faction.getId(), amountToAdd);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.tnt_chestmode_added").replaceAll("#amountToAdd#", amountToAddMsg)));
			return;
		}
		int currentAmount = plugin.tntbank.get(faction.getId());
		plugin.tntbank.put(faction.getId(), currentAmount + amountToAdd);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.tnt_chestmode_added").replaceAll("#amountToAdd#", amountToAddMsg)));

	}
}
