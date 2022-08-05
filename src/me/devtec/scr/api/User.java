package me.devtec.scr.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.utils.ISuser;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;
import net.milkbowl.vault.economy.Economy;

public class User implements ISuser {

	public User(Player player) {
		this.player=player;
		if(haveNickname())
			name = getName();
		else
			name = player.getName();
	}
	public User(CommandSender player) {
		this.player=Bukkit.getPlayer(player.getName());
		if(haveNickname())
			name = getName();
		else
			name = player.getName();
	}
	public User(String player) {
		//nicknames:
		//  <nickname>: real_player_name
		if(Loader.data.exists("nicknames."+player))
			name = Loader.data.getString("nicknames."+player);
		else
			name = player;
		if(player.equalsIgnoreCase("console"))
			this.player=null;
		else
			this.player= Bukkit.getPlayer(name)!=null ? Bukkit.getPlayer(name) : null;
	}

	
	public Player player; //Always real player
	private String name; //Can be Nickname -> use getName()
	

	@Override
	public boolean isConsole() {
		if(name.equalsIgnoreCase("console"))
			return true;
		return false;
	}
	
	@Override
	public boolean checkPerm(String permission) {
		if(isConsole() || player.hasPermission(permission)  || permission==null) //If permission==null -> missing perm in config probably
			return true;
		else {
			MessageUtils.noPerm(player, permission);
			//Messages.message(player, "NoPermission", Placeholder.c().replace("%permission%", permission));
			return false;
		}
	}
	
	@Override
	public boolean isAutorized(String permission) {
		if(isConsole()|| player.hasPermission(permission) || permission==null)
			return true;
		else
			return false;
	}

	@Override
	public Config getUserConfig() {
		if(me.devtec.shared.API.getUser(player.getName())!=null)
			return me.devtec.shared.API.getUser(player.getName());
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
	
	//NICKNAME
	@Override
	public String getName() {
		if(haveNickname()) {
			return getUserConfig().getString("nickname");
		}
		return this.name;
	}
	@Override
	public String getRealName() {
		if(haveNickname()) {
			return Loader.data.getString("nicknames."+getName());
		}
		return this.name;
	}
	@Override
	public boolean haveNickname() {
		return getUserConfig().exists("nickname");
	}
	//nicknames:
	//  <nickname>: real_player_name
	@Override
	public void resetNickname() {
		Config c = getUserConfig();
		if(haveNickname()) {
			Loader.data.remove("nicknames."+getName());
			Loader.data.save();
			c.remove("nickname");
			c.save();
		}
	}
	@Override
	public void setNickname(String nick) {
		if(haveNickname()) { // reset from global config
			resetNickname();
		}
		 // setting nickname
		Loader.data.set("nicknames."+nick, player.getName());
		Loader.data.save();
		Config c = getUserConfig();
		c.set("nickname", nick);
		c.save();
	}
	
	//IGNORE
	@Override
	public boolean isIgnoring(String target) {
		if( !getUserConfig().exists("privateMessage.ignorelist."+target) ) return false;
		return getUserConfig().getBoolean("privateMessage.ignorelist."+target);
	}
	@Override
	public void addIgnore(String target) {
		Config c = getUserConfig();
		c.set("privateMessage.ignorelist."+target, true);
		c.save();
	}
	@Override
	public void removeIgnore(String target) {
		Config c = getUserConfig();
		c.remove("privateMessage.ignorelist."+target);
		c.save();
	}

	//JOIN & LEAVE time
	@Override
	public void leaveTime() {
		Config c = getUserConfig();
		c.set("lastLeave", System.currentTimeMillis()/1000);
		c.save();
	}
	@Override
	public void joinTime() {
		Config c = getUserConfig();
		c.set("joinTime", System.currentTimeMillis()/1000);
		c.save();
	}
	public enum SeenType{
		ONLINE, OFFLINE;
	}
	@Override
	public long getSeen(SeenType type) {
		Config s = getUserConfig();
		long a = 0;
		switch (type) {
		case ONLINE:
			if (s.exists("joinTime"))
				a = System.currentTimeMillis() / 1000 - s.getLong("joinTime");
			break;
		case OFFLINE:
			if (s.exists("lastLeave"))
				a = System.currentTimeMillis() / 1000 - s.getLong("lastLeave");
			break;
		}
		return a;
	}

}
