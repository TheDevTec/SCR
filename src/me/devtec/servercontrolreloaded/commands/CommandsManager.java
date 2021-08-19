package me.devtec.servercontrolreloaded.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.bansystem.Accounts;
import me.devtec.servercontrolreloaded.commands.bansystem.Ban;
import me.devtec.servercontrolreloaded.commands.bansystem.BanIP;
import me.devtec.servercontrolreloaded.commands.bansystem.DelJail;
import me.devtec.servercontrolreloaded.commands.bansystem.Immune;
import me.devtec.servercontrolreloaded.commands.bansystem.Jail;
import me.devtec.servercontrolreloaded.commands.bansystem.Kick;
import me.devtec.servercontrolreloaded.commands.bansystem.Mute;
import me.devtec.servercontrolreloaded.commands.bansystem.SetJail;
import me.devtec.servercontrolreloaded.commands.bansystem.TempBan;
import me.devtec.servercontrolreloaded.commands.bansystem.TempBanIP;
import me.devtec.servercontrolreloaded.commands.bansystem.TempJail;
import me.devtec.servercontrolreloaded.commands.bansystem.TempMute;
import me.devtec.servercontrolreloaded.commands.bansystem.UnBan;
import me.devtec.servercontrolreloaded.commands.bansystem.UnBanIP;
import me.devtec.servercontrolreloaded.commands.bansystem.UnJail;
import me.devtec.servercontrolreloaded.commands.bansystem.UnMute;
import me.devtec.servercontrolreloaded.commands.bansystem.Warn;
import me.devtec.servercontrolreloaded.commands.economy.Balance;
import me.devtec.servercontrolreloaded.commands.economy.Eco;
import me.devtec.servercontrolreloaded.commands.economy.EcoTop;
import me.devtec.servercontrolreloaded.commands.economy.MultiEconomy;
import me.devtec.servercontrolreloaded.commands.economy.Pay;
import me.devtec.servercontrolreloaded.commands.enchantment.EnchantTable;
import me.devtec.servercontrolreloaded.commands.enchantment.EnchantTableRemove;
import me.devtec.servercontrolreloaded.commands.enchantment.EnchantTableRemoveAll;
import me.devtec.servercontrolreloaded.commands.gamemode.Gamemode;
import me.devtec.servercontrolreloaded.commands.gamemode.GamemodeA;
import me.devtec.servercontrolreloaded.commands.gamemode.GamemodeC;
import me.devtec.servercontrolreloaded.commands.gamemode.GamemodeS;
import me.devtec.servercontrolreloaded.commands.gamemode.GamemodeSP;
import me.devtec.servercontrolreloaded.commands.info.Chunks;
import me.devtec.servercontrolreloaded.commands.info.CountryBlocker;
import me.devtec.servercontrolreloaded.commands.info.ListCmd;
import me.devtec.servercontrolreloaded.commands.info.Maintenance;
import me.devtec.servercontrolreloaded.commands.info.Ping;
import me.devtec.servercontrolreloaded.commands.info.RAM;
import me.devtec.servercontrolreloaded.commands.info.SCR;
import me.devtec.servercontrolreloaded.commands.info.Seen;
import me.devtec.servercontrolreloaded.commands.info.Staff;
import me.devtec.servercontrolreloaded.commands.info.TPS;
import me.devtec.servercontrolreloaded.commands.info.WhoIs;
import me.devtec.servercontrolreloaded.commands.inventory.Anvil;
import me.devtec.servercontrolreloaded.commands.inventory.ClearConfirmToggle;
import me.devtec.servercontrolreloaded.commands.inventory.ClearInv;
import me.devtec.servercontrolreloaded.commands.inventory.CloseInventory;
import me.devtec.servercontrolreloaded.commands.inventory.Craft;
import me.devtec.servercontrolreloaded.commands.inventory.EnderChest;
import me.devtec.servercontrolreloaded.commands.inventory.Invsee;
import me.devtec.servercontrolreloaded.commands.kill.Kill;
import me.devtec.servercontrolreloaded.commands.kill.KillAll;
import me.devtec.servercontrolreloaded.commands.kill.Suicide;
import me.devtec.servercontrolreloaded.commands.message.Broadcast;
import me.devtec.servercontrolreloaded.commands.message.ClearChat;
import me.devtec.servercontrolreloaded.commands.message.Helpop;
import me.devtec.servercontrolreloaded.commands.message.Mail;
import me.devtec.servercontrolreloaded.commands.message.PrivateMessage;
import me.devtec.servercontrolreloaded.commands.message.PrivateMessageIgnore;
import me.devtec.servercontrolreloaded.commands.message.ReplyPrivateMes;
import me.devtec.servercontrolreloaded.commands.message.SocialSpy;
import me.devtec.servercontrolreloaded.commands.message.Sudo;
import me.devtec.servercontrolreloaded.commands.nickname.Nick;
import me.devtec.servercontrolreloaded.commands.nickname.NickReset;
import me.devtec.servercontrolreloaded.commands.other.AFK;
import me.devtec.servercontrolreloaded.commands.other.ActionBar;
import me.devtec.servercontrolreloaded.commands.other.BossBar;
import me.devtec.servercontrolreloaded.commands.other.Butcher;
import me.devtec.servercontrolreloaded.commands.other.ChatLock;
import me.devtec.servercontrolreloaded.commands.other.CustomCommand;
import me.devtec.servercontrolreloaded.commands.other.Exp;
import me.devtec.servercontrolreloaded.commands.other.Feed;
import me.devtec.servercontrolreloaded.commands.other.Fly;
import me.devtec.servercontrolreloaded.commands.other.Give;
import me.devtec.servercontrolreloaded.commands.other.God;
import me.devtec.servercontrolreloaded.commands.other.Hat;
import me.devtec.servercontrolreloaded.commands.other.Heal;
import me.devtec.servercontrolreloaded.commands.other.Item;
import me.devtec.servercontrolreloaded.commands.other.Kits;
import me.devtec.servercontrolreloaded.commands.other.MultiWorlds;
import me.devtec.servercontrolreloaded.commands.other.Repair;
import me.devtec.servercontrolreloaded.commands.other.Scoreboard;
import me.devtec.servercontrolreloaded.commands.other.Send;
import me.devtec.servercontrolreloaded.commands.other.Skin;
import me.devtec.servercontrolreloaded.commands.other.Skull;
import me.devtec.servercontrolreloaded.commands.other.Spawner;
import me.devtec.servercontrolreloaded.commands.other.TempFly;
import me.devtec.servercontrolreloaded.commands.other.TempGamemode;
import me.devtec.servercontrolreloaded.commands.other.Thor;
import me.devtec.servercontrolreloaded.commands.other.Top;
import me.devtec.servercontrolreloaded.commands.other.Trash;
import me.devtec.servercontrolreloaded.commands.other.Uuid;
import me.devtec.servercontrolreloaded.commands.other.Vanish;
import me.devtec.servercontrolreloaded.commands.other.chat.ChatNotify;
import me.devtec.servercontrolreloaded.commands.other.guis.GUICreator;
import me.devtec.servercontrolreloaded.commands.other.mirror.MirrorCommand;
import me.devtec.servercontrolreloaded.commands.other.portal.Portal;
import me.devtec.servercontrolreloaded.commands.other.tablist.Tab;
import me.devtec.servercontrolreloaded.commands.server.Reload;
import me.devtec.servercontrolreloaded.commands.server.Restart;
import me.devtec.servercontrolreloaded.commands.server.Stop;
import me.devtec.servercontrolreloaded.commands.speed.FlySpeed;
import me.devtec.servercontrolreloaded.commands.speed.WalkSpeed;
import me.devtec.servercontrolreloaded.commands.time.Day;
import me.devtec.servercontrolreloaded.commands.time.Night;
import me.devtec.servercontrolreloaded.commands.time.PDay;
import me.devtec.servercontrolreloaded.commands.time.PNight;
import me.devtec.servercontrolreloaded.commands.time.PlayTime;
import me.devtec.servercontrolreloaded.commands.tpsystem.Tp;
import me.devtec.servercontrolreloaded.commands.tpsystem.Tpa;
import me.devtec.servercontrolreloaded.commands.tpsystem.TpaBlock;
import me.devtec.servercontrolreloaded.commands.tpsystem.Tpaall;
import me.devtec.servercontrolreloaded.commands.tpsystem.Tpaccept;
import me.devtec.servercontrolreloaded.commands.tpsystem.Tpadeny;
import me.devtec.servercontrolreloaded.commands.tpsystem.Tpahere;
import me.devtec.servercontrolreloaded.commands.tpsystem.Tpall;
import me.devtec.servercontrolreloaded.commands.tpsystem.Tpcancel;
import me.devtec.servercontrolreloaded.commands.tpsystem.Tphere;
import me.devtec.servercontrolreloaded.commands.warps.Back;
import me.devtec.servercontrolreloaded.commands.warps.DelHome;
import me.devtec.servercontrolreloaded.commands.warps.DelWarp;
import me.devtec.servercontrolreloaded.commands.warps.Home;
import me.devtec.servercontrolreloaded.commands.warps.HomeOther;
import me.devtec.servercontrolreloaded.commands.warps.Homes;
import me.devtec.servercontrolreloaded.commands.warps.SetHome;
import me.devtec.servercontrolreloaded.commands.warps.SetSpawn;
import me.devtec.servercontrolreloaded.commands.warps.SetWarp;
import me.devtec.servercontrolreloaded.commands.warps.Spawn;
import me.devtec.servercontrolreloaded.commands.warps.Warp;
import me.devtec.servercontrolreloaded.commands.weather.PRain;
import me.devtec.servercontrolreloaded.commands.weather.PSun;
import me.devtec.servercontrolreloaded.commands.weather.Rain;
import me.devtec.servercontrolreloaded.commands.weather.Sun;
import me.devtec.servercontrolreloaded.commands.weather.Thunder;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.utils.StringUtils;
public class CommandsManager {
	

	private static final Map<String, Map<String,Long>> cooldownMap = new HashMap<>();
	private static final Map<String, Long> cooldown = new HashMap<>();
	
	//global
	private static final Map<String, Long> waitingCooldown= new HashMap<>();
	private static final Map<String,Boolean> global= new HashMap<>();
	
	public static boolean canUse(String path, CommandSender s) {
		if(!cooldown.containsKey(path))return true;
		if(s instanceof Player) {
			if(s.hasPermission("SCR.Other.Cooldown.Commands"))return true;
			List<String> set = new ArrayList<>();
			set.add(path);
			return canUse(set,path,s);
		}
		return true;
	}
	
	private static boolean canUse(List<String> set, String path, CommandSender s) {
		for(String d : Loader.cmds.getStringList(path+".CooldownCmds")) {
			if(set.contains(d))continue;
			boolean expire = canUse(set,d,s);
			if(!expire)return false;
		}
	if(global.getOrDefault(path,false)) {
		if(waitingCooldown.getOrDefault(path, (long)0)-System.currentTimeMillis()/1000 + cooldown.get(path)<= 0) {
			waitingCooldown.put(path, System.currentTimeMillis()/1000);
			return true;
		}
		return false;
	}
	Map<String, Long> waitingCooldown = cooldownMap.get(path);
	if(waitingCooldown==null)
		cooldownMap.put(path, waitingCooldown=new HashMap<>());
	if(waitingCooldown.getOrDefault(s.getName(), (long)0)-System.currentTimeMillis()/1000 + cooldown.get(path) <= 0) {
		waitingCooldown.put(s.getName(), System.currentTimeMillis()/1000);
		return true;
	}
	return false;
	}

	public static long expire(String path, CommandSender s) {
		if(!cooldown.containsKey(path))return 0;
		if(s instanceof Player) {
			List<String> set = new ArrayList<>();
			set.add(path);
			return expire(set, path, s);
		}
		return 0;
	}
	
	private static long expire(List<String> done, String path, CommandSender s) {
		if(!done.contains(path))
			done.add(path);
		for(String d : Loader.cmds.getStringList(path+".CooldownCmds")) {
			if(done.contains(d))continue;
			long expire = expire(done,d,s);
			if(expire>0)return expire;
		}
		if(global.getOrDefault(path,false)) {
			return waitingCooldown.getOrDefault(path, (long)0)-System.currentTimeMillis()/1000 + cooldown.get(path);
		}
		Map<String, Long> waitingCooldown = cooldownMap.get(path);
		if(waitingCooldown==null) {
			cooldownMap.put(path, waitingCooldown=new HashMap<>());
		}
		return waitingCooldown.getOrDefault(s.getName(), (long)0)-System.currentTimeMillis()/1000 + cooldown.get(path);
	}
	
	private static final Map<String, PluginCommand> commands = new HashMap<>();
	
	public static boolean load(String section, String command, CommandExecutor cs) {
		if(Loader.cmds.getBoolean(section+"."+command+".Enabled")) {
			long time = StringUtils.timeFromString(Loader.cmds.getString(section+"."+command+".Cooldown"));
			if(time>0) {
				global.put(section+'.'+command, Loader.cmds.getBoolean(section+"."+command+".CooldownGlobal"));
				cooldown.put(section+'.'+command, time);
			}
			PluginCommand c = TheAPI.createCommand(Loader.cmds.getString(section+"."+command+".Name"), Loader.getInstance);
			List<String> aliases = new ArrayList<>();
			if(Loader.cmds.exists(section+"."+command+".Aliases")) {
			if(Loader.cmds.get(section+"."+command+".Aliases") instanceof Collection)
				aliases=Loader.cmds.getStringList(section+"."+command+".Aliases");
				else aliases.add(Loader.cmds.getString(section+"."+command+".Aliases"));
			}
			c.setAliases(aliases);
			c.setExecutor(cs);
			c.setPermission(Loader.cmds.getString(section+"."+command+".Permission"));
			TheAPI.registerCommand(c);
			commands.put(section.toLowerCase()+":"+command.toLowerCase(), c);
			return true;
		}else {
			if(commands.containsKey(section.toLowerCase()+":"+command.toLowerCase()))
				unload(section,command);
		}
		return false;
	}
	
	public static void unload(String section, String command) {
		TheAPI.unregisterCommand(commands.remove(section.toLowerCase()+":"+command.toLowerCase()));
	}
	
	public static void load() {
		//Server
		load("Server", "Stop" , new Stop());
		load("Server", "Reload", new Reload());
		load("Server", "Restart", new Restart());
		
		//Kill
		load("Kill", "Kill", new Kill());
		load("Kill", "KillAll", new KillAll());
		load("Kill", "Suicide", new Suicide());
		
		//Info
		load("Info", "Memory", new RAM());
		load("Info", "Chunks", new Chunks());
		load("Info", "SCR", new SCR());
		load("Info", "Seen", new Seen());
		load("Info", "List", new ListCmd());
		load("Info", "Staff", new Staff());
		load("Info", "TPS", new TPS());
		load("Info", "WhoIs", new WhoIs()); 
		load("Info", "Maintenance", new Maintenance());
		load("Info", "Ping", new Ping());
		if(Loader.config.getBoolean("CountryBlocker.Enabled"))
		load("Info", "CountryBlocker", new CountryBlocker());

		//Speed
		load("Speed", "FlySpeed", new FlySpeed());
		load("Speed", "WalkSpeed", new WalkSpeed());
		
		//Warps
		load("Warps", "SetSpawn", new SetSpawn());
		load("Warps", "Spawn", new Spawn());
		load("Warps", "SetWarp", new SetWarp());
		load("Warps", "DelWarp", new DelWarp());
		load("Warps", "Warp", new Warp());
		load("Warps", "Home", new Home());
		load("Warps", "HomeOther", new HomeOther());
		load("Warps", "SetHome", new SetHome());
		load("Warps", "DelHome", new DelHome());
		load("Warps", "Homes", new Homes());
		load("Warps", "Back", new Back());
		
		//Economy
		if(PluginManagerAPI.getPlugin("Vault")!=null) {
			load("Economy", "BalanceTop", new EcoTop());
			load("Economy", "Balance", new Balance());
			load("Economy", "Economy", new Eco());
			load("Economy", "Pay", new Pay());
			if(setting.eco_multi)
			load("Economy", "MultiEconomy", new MultiEconomy());
		}
		
		//Weather
		load("Weather", "Sun", new Sun());
		load("Weather", "Thunder", new Thunder());
		load("Weather", "Rain", new Rain());
		load("Weather", "PlayerSun", new PSun());
		load("Weather", "PlayerRain", new PRain());
		
		//Time
		load("Time", "Day", new Day());
		load("Time", "Night", new Night());
		load("Time", "PDay", new PDay());
		load("Time", "PNight", new PNight());
		load("Time", "PlayTime", new PlayTime());
		
		//Message
		load("Message", "SocialSpy", new SocialSpy());
		load("Message", "Mail", new Mail());
		load("Message", "Sudo", new Sudo());
		load("Message", "Broadcast", new Broadcast());
		load("Message", "PrivateMessage", new PrivateMessage());
		load("Message", "ClearChat", new ClearChat());
		load("Message", "Helpop", new Helpop());
		load("Message", "Reply", new ReplyPrivateMes());
		load("Message", "Ignore", new PrivateMessageIgnore());
		
		//Gamemode
		load("GameMode", "TempGamemode", new TempGamemode());
		load("GameMode", "GameMode", new Gamemode());
		load("GameMode", "GameModeSurvival", new GamemodeS());
		load("GameMode", "GameModeCreative", new GamemodeC());
		load("GameMode", "GameModeAdventure", new GamemodeA());
		if(TheAPI.isNewerThan(13))
			load("GameMode", "GameModeSpectator", new GamemodeSP());
			
		//BanSystem	
		load("BanSystem", "Kick", new Kick());
		load("BanSystem", "Ban", new Ban());
		load("BanSystem", "Immune", new Immune());
		load("BanSystem", "TempBan", new TempBan());
		load("BanSystem", "Jail", new Jail());
		load("BanSystem", "TempBanIP", new TempBanIP());
		load("BanSystem", "UnJail", new UnJail());
		load("BanSystem", "SetJail", new SetJail());
		load("BanSystem", "DelJail", new DelJail());
		load("BanSystem", "TempJail", new TempJail());
		load("BanSystem", "BanIP", new BanIP());
		load("BanSystem", "UnBanIP", new UnBanIP());
		load("BanSystem", "UnBan", new UnBan());
		load("BanSystem", "TempMute", new TempMute());
		load("BanSystem", "Mute", new Mute());
		load("BanSystem", "UnMute", new UnMute());
		load("BanSystem", "Warn", new Warn());
		load("BanSystem", "Accounts", new Accounts());
		
		//Inventory
		load("Inventory", "EnderChest", new EnderChest());
		load("Inventory", "CloseInventory", new CloseInventory());
		load("Inventory", "ClearInventory",new ClearInv());
		load("Inventory", "ClearInventoryToggle", new ClearConfirmToggle());
		load("Inventory", "Invsee", new Invsee());
		load("Inventory", "Workbench", new Craft());
		load("Inventory", "Anvil", new Anvil());
		
		//Enchantment
		load("Enchantment", "Enchant", new EnchantTable());
		load("Enchantment", "EnchantRemove", new EnchantTableRemove());
		load("Enchantment", "EnchantRemoveAll", new EnchantTableRemoveAll());
		
		//TpSystem
		load("TpSystem", "Tp", new Tp());
		load("TpSystem", "TpaCancel", new Tpcancel());
		load("TpSystem", "Tpa", new Tpa());
		load("TpSystem", "TpaHere", new Tpahere());
		load("TpSystem", "TpHere", new Tphere());
		load("TpSystem", "TpToggle", new TpaBlock());
		load("TpSystem", "TpaAll", new Tpaall());
		load("TpSystem", "TpAll", new Tpall());
		load("TpSystem", "TpaAccept", new Tpaccept());
		load("TpSystem", "TpaDeny", new Tpadeny());
		
		//Modules -> package Modules
		load("Other", "Mirror", new MirrorCommand());
		
		//Other
		if(Loader.hasBungee)
		load("Other", "Send",new Send()); //requres spigot
		
		load("Other", "Top",new Top());
		load("Other", "Portal",new Portal());
		load("Other", "ChatNotify",new ChatNotify());
		load("Other", "ChatLock",new ChatLock());
		load("Other", "Repair", new Repair());
		load("Other", "Feed", new Feed());
		load("Other", "Item", new Item());
		load("Other", "TempFly", new TempFly());
		if(setting.sb)
		load("Other", "ScoreBoard", new Scoreboard());
		if(Loader.ac.getBoolean("Enabled"))
		load("Other", "ActionBar", new ActionBar());
		if(Loader.bb.getBoolean("Enabled"))
		load("Other", "BossBar", new BossBar());
		load("Other", "Trash", new Trash());
		load("Other", "Thor", new Thor());
		load("Other", "Give",new Give());
		load("Other", "Kits",new Kits());
		load("Other", "Skull",new Skull());
		load("Other", "God",new God());
		load("Other", "Heal", new Heal());
		load("Other", "Fly",new Fly());
		load("Other", "Vanish",new Vanish());
		load("Other", "Butcher",new Butcher());
		load("Other", "AFK",new AFK());
		load("Other", "MultiWorlds",new MultiWorlds());
		if(setting.tab)
		load("Other", "Tablist",new Tab());
		load("Other", "Hat",new Hat());
		load("Other", "Skin",new Skin());
		load("Other", "Experiences", new Exp());
		load("Other", "Spawner", new Spawner());
		load("Other", "Uuid", new Uuid());
		//Utility
		if(Loader.guicreator.exists("Commands")){
			for(String a : Loader.guicreator.getKeys("Commands")){
				if(commands.containsKey(a))return;
				PluginCommand c = TheAPI.createCommand(a,Loader.getInstance);
				c.setExecutor(new GUICreator(StringUtils.timeFromString(Loader.guicreator.getString("Commands."+a+".cooldown")), Loader.guicreator.getBoolean("Commands."+a+".cooldownGlobal"),a,Loader.guicreator.getString("Commands."+a+".gui")));
				c.setAliases(Loader.guicreator.getStringList("Commands."+a+".aliases"));
				c.setPermission(Loader.guicreator.getString("Commands."+a+".permission"));
				commands.put("other:"+a,c);
				TheAPI.registerCommand(c);
			}
		}
		//Nickname
		load("Nickname", "Nickname", new Nick());
		load("Nickname", "NicknameReset", new NickReset());
		
		loadCustomCommands();
		
		if(TheAPI.isNewerThan(12))
		for(Player p : TheAPI.getOnlinePlayers())
			p.updateCommands();
		}
	
	private static void loadCustomCommands() {
		for(String command : Loader.customCmds.getKeys()) {
			load(command, new CustomCommand(command));
		}
	}
	
	private static void load(String command, CommandExecutor cs) {
		if(Loader.customCmds.getBoolean(command+".Enabled")) {
			long time = StringUtils.timeFromString(Loader.customCmds.getString(command+".Cooldown"));
			if(time>0) {
				global.put("CustomCommand."+command, Loader.customCmds.getBoolean(command+".CooldownGlobal"));
				cooldown.put("CustomCommand."+command, time);
			}
			PluginCommand c = TheAPI.createCommand(Loader.customCmds.getString(command+".Name"), Loader.getInstance);
			List<String> aliases = new ArrayList<>();
			if(Loader.customCmds.exists(command+".Aliases")) {
			if(Loader.customCmds.get(command+".Aliases") instanceof Collection)
				aliases=Loader.customCmds.getStringList(command+".Aliases");
				else aliases.add(Loader.customCmds.getString(command+".Aliases"));
			}
			c.setAliases(aliases);
			c.setExecutor(cs);
			c.setPermission(Loader.customCmds.getString(command+".Permission"));
			TheAPI.registerCommand(c);
			commands.put("scr:customcmd:"+command.toLowerCase(), c);
		}else {
			if(commands.containsKey("scr:customcmd:"+command.toLowerCase())){
				TheAPI.unregisterCommand(commands.remove("scr:customcmd:"+command.toLowerCase()));
			}
		}
	}

	public static void unload() {
		cooldown.clear();
		cooldownMap.clear();
		waitingCooldown.clear();
		global.clear();
		for(PluginCommand s : commands.values())
			TheAPI.unregisterCommand(s);
		commands.clear();
		if(TheAPI.isNewerThan(12))
			for(Player p : TheAPI.getOnlinePlayers())
				p.updateCommands();
	}

	public static boolean isLoaded(String section, String command) {
		return commands.containsKey(section.toLowerCase()+":"+command.toLowerCase());
	}

	public static PluginCommand get(String s) {
		return commands.get(s.toLowerCase());
	}
}
