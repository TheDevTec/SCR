package me.devtec.scr.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.scr.commands.economy.Balance;
import me.devtec.scr.commands.economy.Economy;
import me.devtec.scr.commands.economy.Pay;
import me.devtec.scr.commands.fun.Thor;
import me.devtec.scr.commands.main.setAir;
import me.devtec.scr.commands.main.setCustomName;
import me.devtec.scr.commands.main.setDisplayName;
import me.devtec.scr.commands.main.setFireTicks;
import me.devtec.scr.commands.main.setHunger;
import me.devtec.scr.commands.other.Repair;
import me.devtec.scr.commands.other.Uuid;
import me.devtec.scr.commands.other.setFly;
import me.devtec.scr.commands.other.control.Reload;
import me.devtec.scr.commands.other.control.Restart;
import me.devtec.scr.commands.other.control.Stop;
import me.devtec.scr.commands.other.speed.setFlySpeed;
import me.devtec.scr.commands.other.speed.setSpeed;
import me.devtec.scr.commands.other.speed.setWalkSpeed;
import me.devtec.scr.commands.punishment.Ban;
import me.devtec.scr.commands.punishment.BanIP;
import me.devtec.scr.commands.punishment.Kick;
import me.devtec.scr.commands.punishment.Mute;
import me.devtec.scr.commands.punishment.MuteIP;
import me.devtec.scr.commands.punishment.Warn;
import me.devtec.scr.commands.teleport.Teleport;
import me.devtec.scr.commands.teleport.homes.Home;
import me.devtec.scr.commands.teleport.homes.delHome;
import me.devtec.scr.commands.teleport.homes.listHomes;
import me.devtec.scr.commands.teleport.homes.otherHome;
import me.devtec.scr.commands.teleport.homes.setHome;
import me.devtec.scr.commands.teleport.requests.Tpa;
import me.devtec.scr.commands.teleport.requests.Tpaccept;
import me.devtec.scr.commands.teleport.requests.Tpahere;
import me.devtec.scr.commands.teleport.requests.Tpcancel;
import me.devtec.scr.commands.teleport.requests.Tpdeny;
import me.devtec.scr.commands.teleport.spawn.Spawn;
import me.devtec.scr.commands.teleport.spawn.setSpawn;
import me.devtec.scr.commands.teleport.warps.Warp;
import me.devtec.scr.commands.teleport.warps.WarpEditor;
import me.devtec.scr.commands.teleport.warps.WarpManager;
import me.devtec.scr.commands.teleport.warps.delWarp;
import me.devtec.scr.commands.teleport.warps.setWarp;
import me.devtec.scr.modules.Module;
import me.devtec.scr.utils.Formatters;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;

public class CommandsManager implements Module {
	private boolean isLoaded;
	private List<PluginCommand> cmds = new ArrayList<>();
	
	public static boolean waitingCooldown(CommandHolder command, CommandSender sender) {
		return false;
	}
	
	public Module load() {
		if(isLoaded)return this;
		isLoaded=true;
		Loader.config=Config.loadConfig(Loader.plugin, "files/config.yml", "SCR/config.yml");
		WarpManager.load();
		Spawn.spawn=(ConfigManager.data.getString("spawn")==null||ConfigManager.data.getString("spawn").trim().isEmpty())?Bukkit.getWorlds().get(0).getSpawnLocation():Formatters.locFromString(ConfigManager.data.getString("spawn"));
		for(String val : Loader.config.getStringList("positive"))
			Loader.positive.add(val.toLowerCase());
		for(String val : Loader.config.getStringList("negative"))
			Loader.negative.add(val.toLowerCase());
		//LOAD COMMANDS
		
		//ECONOMY
		if(EconomyAPI.getEconomy()!=null) {
			if(isEnabled("balance"))create(new Balance("balance"));
			if(isEnabled("economy"))create(new Economy("economy"));
			if(isEnabled("pay"))create(new Pay("pay"));
		}
		//SET
		if(isEnabled("setAir"))create(new setAir("setAir"));
		if(isEnabled("setCustomName"))create(new setCustomName("setCustomName"));
		if(isEnabled("setDisplayName"))create(new setDisplayName("setDisplayName"));
		if(isEnabled("setFireTicks"))create(new setFireTicks("setFireTicks"));
		if(isEnabled("setHunger"))create(new setHunger("setHunger"));
		if(isEnabled("setFlySpeed"))create(new setFlySpeed("setFlySpeed"));
		if(isEnabled("setWalkSpeed"))create(new setWalkSpeed("setWalkSpeed"));
		if(isEnabled("setSpeed"))create(new setSpeed("setSpeed"));
		if(isEnabled("setFly"))create(new setFly("setFly"));
		//BAN SYSTEM
		if(isEnabled("ban"))create(new Ban("ban"));
		if(isEnabled("banip"))create(new BanIP("banip"));
		if(isEnabled("kick"))create(new Kick("kick"));
		if(isEnabled("mute"))create(new Mute("mute"));
		if(isEnabled("muteip"))create(new MuteIP("muteip"));
		if(isEnabled("warn"))create(new Warn("warn"));
		//HOMES
		if(isEnabled("delHome"))create(new delHome("delHome"));
		if(isEnabled("setHome"))create(new setHome("setHome"));
		if(isEnabled("otherHome"))create(new otherHome("otherHome"));
		if(isEnabled("home"))create(new Home("home"));
		if(isEnabled("listHomes"))create(new listHomes("listHomes"));
		//TELEPORT
		if(isEnabled("tpa"))create(new Tpa("tpa"));
		if(isEnabled("tpahere"))create(new Tpahere("tpahere"));
		if(isEnabled("tpaccept"))create(new Tpaccept("tpaccept"));
		if(isEnabled("tpdeny"))create(new Tpdeny("tpdeny"));
		if(isEnabled("tpcancel"))create(new Tpcancel("tpcancel"));
		if(isEnabled("teleport"))create(new Teleport("teleport"));
		//SPAWN
		if(isEnabled("setSpawn"))create(new setSpawn("setSpawn"));
		if(isEnabled("spawn"))create(new Spawn("spawn"));
		//WARP
		if(isEnabled("delWarp"))create(new delWarp("delWarp"));
		if(isEnabled("setWarp"))create(new setWarp("setWarp"));
		if(isEnabled("warp"))create(new Warp("warp"));
		if(isEnabled("warpEditor"))create(new WarpEditor("warpEditor"));
		//INFO
		if(isEnabled("uuid"))create(new Uuid("uuid"));
		//SERVER
		if(isEnabled("reload"))create(new Reload("reload"));
		if(isEnabled("restart"))create(new Restart("restart"));
		if(isEnabled("stop"))create(new Stop("stop"));
		//OTHER
		if(isEnabled("repair"))create(new Repair("repair"));
		if(isEnabled("thor"))create(new Thor("thor"));
		return this;
	}
	
	public boolean isEnabled(String name) {
		return !ConfigManager.commands.exists(name+".enabled") || ConfigManager.commands.getBoolean(name+".enabled");
	}
	
	public void create(CommandHolder holder) {
		List<String> aliases = ConfigManager.commands.getStringList(holder.command+".aliases");
		PluginCommand cmd = TheAPI.createCommand(aliases.remove(0), Loader.plugin);
		if(!aliases.isEmpty())
			cmd.setAliases(aliases);
		cmd.setExecutor(holder);
		cmd.setTabCompleter(holder);
		cmds.add(cmd);
	}
	
	public Module unload() {
		if(!isLoaded)return this;
		isLoaded=false;
		WarpManager.unload();
		Loader.positive.clear();
		Loader.negative.clear();
		ConfigManager.data.set("spawn", Formatters.locToString(Spawn.spawn));
		ConfigManager.data.save();
		for(PluginCommand cmd : cmds)TheAPI.unregisterCommand(cmd);
		cmds.clear();
		return this;
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}
}
