package com.ascendpvp.TnTBank.events;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.ascendpvp.TnTBank.TnTBankMain;
import com.massivecraft.factions.event.EventFactionsDisband;

public class FactionDisband implements Listener {

	TnTBankMain plugin;
	public FactionDisband(TnTBankMain plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onDisband(EventFactionsDisband e) {
		if(e.getMPlayer() == null) return;
		if(plugin.cfg.get("tntbank." + e.getFactionId()) == null ) return;
		
		plugin.cfg.set("tntbank." + e.getMPlayer().getFactionId(), null);
		plugin.tntbank.remove(e.getMPlayer().getFactionId());
		try {
			plugin.cfg.save(plugin.f);
		} catch (IOException exeption) {
			exeption.printStackTrace();
		}
	}
}
