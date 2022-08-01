package me.devtec.scr.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils;
import me.devtec.scr.utils.ISuser;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;
import net.milkbowl.vault.economy.Economy;

public class User implements ISuser {

	public User(Player player) {
		this.player=player;
		name = player.getName();
	}
	public User(CommandSender player) {
		this.player=Bukkit.getPlayer(player.getName());
		name = player.getName();
	}
	public User(String player) {
		name = player;
		if(player.equalsIgnoreCase("console"))
			this.player=null;
		this.player= Bukkit.getPlayer(player)!=null ? Bukkit.getPlayer(player) : null;
	}

	
	public Player player;
	public String name;
	

	@Override
	public boolean isConsole() {
		if(name.equalsIgnoreCase("console"))
			return true;
		return false;
	}
	
	@Override
	public boolean checkPerm(String permission) {
		if(player.hasPermission(permission) || isConsole() || permission==null) //If permission==null -> missing perm in config probably
			return true;
		else {
			MessageUtils.noPerm(player, permission);
			//Messages.message(player, "NoPermission", Placeholder.c().replace("%permission%", permission));
			return false;
		}
	}
	
	@Override
	public boolean isAutorized(String permission) {
		if(player.hasPermission(permission) || isConsole() || permission==null)
			return true;
		else
			return false;
	}

	@Override
	public Config getUserConfig() {
		if(API.getUser(name)!=null)
			return me.devtec.shared.API.getUser(name);
		return null;
	}
	
	//COOLDOWNS
	//cooldownpath - in user config
	//expires - cooldown time
	@Override
	public boolean cooldownExpired(String cooldownpath, String cooldowntime) {
		if(isAutorized("scr.bypass.cooldowns"))
			return true;
		if(getUserConfig().getLong(cooldownpath) - System.currentTimeMillis()/1000+StringUtils.timeFromString(cooldowntime)<=0) {
			return true;
		}
		return false;
	}
	@Override
	public long expires(String cooldownpath, String cooldowntime) {
		return getUserConfig().getLong(cooldownpath) - System.currentTimeMillis()/1000+StringUtils.timeFromString(cooldowntime);
	}
	@Override
	public void newCooldown(String cooldownpath) {
		Config c = getUserConfig();
		c.set(cooldownpath, System.currentTimeMillis()/1000);
		c.save();
	}
	
	@Override
	public Economy getEconomy() {
		return Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
	}

}
