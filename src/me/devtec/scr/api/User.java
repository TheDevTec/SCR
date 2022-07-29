package me.devtec.scr.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.utils.ISuser;
import me.devtec.scr.utils.Messages;
import me.devtec.shared.API;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;

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
		if(player.hasPermission(permission) || isConsole())
			return true;
		else {
			Messages.noPerm(player, permission);
			//Messages.message(player, "NoPermission", Placeholder.c().replace("%permission%", permission));
			return false;
		}
	}
	
	@Override
	public boolean isAutorized(String permission) {
		if(player.hasPermission(permission) || isConsole())
			return true;
		else
			return false;
	}

	@Override
	public Config getUserConfig() {
		if(API.getUser(name)!=null)
			return API.getUser(name);
		return null;
	}
	
	//COOLDOWNS
	@Override
	//cooldownpath - in user config
	//expires - cooldown time
	public boolean cooldownExpired(String cooldownpath, String cooldowntime) {
		if(isAutorized("SCR.Other.Cooldowns.IgnoresAll"))
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

}