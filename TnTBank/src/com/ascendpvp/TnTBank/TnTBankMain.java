package com.ascendpvp.TnTBank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.ascendpvp.TnTBank.cmds.TnTBankCommand;
import com.ascendpvp.TnTBank.events.ChestSelect;
import com.ascendpvp.TnTBank.events.FactionDisband;

public class TnTBankMain extends JavaPlugin {
	
	public HashMap<String, Integer> tntbank;
	public ArrayList<String> cMode;
	public FileConfiguration cfg;
	public File f;
	
	public void onEnable() { 
		f = new File("plugins/tntbank", "faction_data.yml");
		cfg = YamlConfiguration.loadConfiguration(f);
		tntbank = new HashMap<String, Integer>();
		cMode = new ArrayList<String>();
		Bukkit.getPluginManager().registerEvents(new ChestSelect(this), this);
		Bukkit.getPluginManager().registerEvents(new FactionDisband(this), this);
		getCommand("tntbank").setExecutor(new TnTBankCommand(this));
		saveDefaultConfig();
		Loadbank();
	}
	
	@Override
	public void onDisable() {
		Savebank();
	}

	public void Savebank(){
		for (String key : tntbank.keySet()) {
			cfg.set("tntbank."+key, tntbank.get(key));
		}
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, Integer> Loadbank(){
		for (String key : cfg.getConfigurationSection("tntbank").getKeys(false)) {
			tntbank.put(key, (Integer) cfg.get("tntbank."+key));
		}
		return tntbank;
	}
	
	public FileConfiguration getSpecialConfig() {
		return cfg;
	}
}

