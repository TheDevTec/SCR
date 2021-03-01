package me.DevTec.ServerControlReloaded.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.Commands.BanSystem.Accounts;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Ban;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.BanIP;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.DelJail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Immune;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Jail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Kick;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Mute;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.SetJail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.TempBan;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.TempBanIP;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.TempJail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.TempMute;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.UnBan;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.UnBanIP;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.UnJail;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.UnMute;
import me.DevTec.ServerControlReloaded.Commands.BanSystem.Warn;
import me.DevTec.ServerControlReloaded.Commands.Economy.Balance;
import me.DevTec.ServerControlReloaded.Commands.Economy.Eco;
import me.DevTec.ServerControlReloaded.Commands.Economy.EcoTop;
import me.DevTec.ServerControlReloaded.Commands.Economy.MultiEconomy;
import me.DevTec.ServerControlReloaded.Commands.Economy.Pay;
import me.DevTec.ServerControlReloaded.Commands.Enchantment.EnchantTable;
import me.DevTec.ServerControlReloaded.Commands.Enchantment.EnchantTableRemove;
import me.DevTec.ServerControlReloaded.Commands.Enchantment.EnchantTableRemoveAll;
import me.DevTec.ServerControlReloaded.Commands.GameMode.Gamemode;
import me.DevTec.ServerControlReloaded.Commands.GameMode.GamemodeA;
import me.DevTec.ServerControlReloaded.Commands.GameMode.GamemodeC;
import me.DevTec.ServerControlReloaded.Commands.GameMode.GamemodeS;
import me.DevTec.ServerControlReloaded.Commands.GameMode.GamemodeSP;
import me.DevTec.ServerControlReloaded.Commands.Info.ChatFormat;
import me.DevTec.ServerControlReloaded.Commands.Info.Chunks;
import me.DevTec.ServerControlReloaded.Commands.Info.ListCmd;
import me.DevTec.ServerControlReloaded.Commands.Info.Maintenance;
import me.DevTec.ServerControlReloaded.Commands.Info.RAM;
import me.DevTec.ServerControlReloaded.Commands.Info.SCR;
import me.DevTec.ServerControlReloaded.Commands.Info.Seen;
import me.DevTec.ServerControlReloaded.Commands.Info.Staff;
import me.DevTec.ServerControlReloaded.Commands.Info.TPS;
import me.DevTec.ServerControlReloaded.Commands.Info.WhoIs;
import me.DevTec.ServerControlReloaded.Commands.Inventory.ClearConfirmToggle;
import me.DevTec.ServerControlReloaded.Commands.Inventory.ClearInv;
import me.DevTec.ServerControlReloaded.Commands.Inventory.CloseInventory;
import me.DevTec.ServerControlReloaded.Commands.Inventory.Craft;
import me.DevTec.ServerControlReloaded.Commands.Inventory.EnderChest;
import me.DevTec.ServerControlReloaded.Commands.Inventory.Invsee;
import me.DevTec.ServerControlReloaded.Commands.Kill.Kill;
import me.DevTec.ServerControlReloaded.Commands.Kill.KillAll;
import me.DevTec.ServerControlReloaded.Commands.Kill.Suicide;
import me.DevTec.ServerControlReloaded.Commands.Message.Broadcast;
import me.DevTec.ServerControlReloaded.Commands.Message.ClearChat;
import me.DevTec.ServerControlReloaded.Commands.Message.Helpop;
import me.DevTec.ServerControlReloaded.Commands.Message.Mail;
import me.DevTec.ServerControlReloaded.Commands.Message.PrivateMessage;
import me.DevTec.ServerControlReloaded.Commands.Message.PrivateMessageIgnore;
import me.DevTec.ServerControlReloaded.Commands.Message.ReplyPrivateMes;
import me.DevTec.ServerControlReloaded.Commands.Message.SocialSpy;
import me.DevTec.ServerControlReloaded.Commands.Message.Sudo;
import me.DevTec.ServerControlReloaded.Commands.Nickname.Nick;
import me.DevTec.ServerControlReloaded.Commands.Nickname.NickReset;
import me.DevTec.ServerControlReloaded.Commands.Other.AFK;
import me.DevTec.ServerControlReloaded.Commands.Other.ActionBar;
import me.DevTec.ServerControlReloaded.Commands.Other.BossBar;
import me.DevTec.ServerControlReloaded.Commands.Other.Butcher;
import me.DevTec.ServerControlReloaded.Commands.Other.ChatLock;
import me.DevTec.ServerControlReloaded.Commands.Other.ColorsCmd;
import me.DevTec.ServerControlReloaded.Commands.Other.Exp;
import me.DevTec.ServerControlReloaded.Commands.Other.Feed;
import me.DevTec.ServerControlReloaded.Commands.Other.Fly;
import me.DevTec.ServerControlReloaded.Commands.Other.Give;
import me.DevTec.ServerControlReloaded.Commands.Other.God;
import me.DevTec.ServerControlReloaded.Commands.Other.Hat;
import me.DevTec.ServerControlReloaded.Commands.Other.Heal;
import me.DevTec.ServerControlReloaded.Commands.Other.Item;
import me.DevTec.ServerControlReloaded.Commands.Other.Kits;
import me.DevTec.ServerControlReloaded.Commands.Other.MultiWorlds;
import me.DevTec.ServerControlReloaded.Commands.Other.Repair;
import me.DevTec.ServerControlReloaded.Commands.Other.RulesCmd;
import me.DevTec.ServerControlReloaded.Commands.Other.Scoreboard;
import me.DevTec.ServerControlReloaded.Commands.Other.Skin;
import me.DevTec.ServerControlReloaded.Commands.Other.Skull;
import me.DevTec.ServerControlReloaded.Commands.Other.Spawner;
import me.DevTec.ServerControlReloaded.Commands.Other.TempFly;
import me.DevTec.ServerControlReloaded.Commands.Other.Thor;
import me.DevTec.ServerControlReloaded.Commands.Other.Trash;
import me.DevTec.ServerControlReloaded.Commands.Other.Uuid;
import me.DevTec.ServerControlReloaded.Commands.Other.Vanish;
import me.DevTec.ServerControlReloaded.Commands.Other.tablist.Tab;
import me.DevTec.ServerControlReloaded.Commands.Server.Reload;
import me.DevTec.ServerControlReloaded.Commands.Server.Restart;
import me.DevTec.ServerControlReloaded.Commands.Server.Stop;
import me.DevTec.ServerControlReloaded.Commands.Speed.FlySpeed;
import me.DevTec.ServerControlReloaded.Commands.Speed.WalkSpeed;
import me.DevTec.ServerControlReloaded.Commands.Time.Day;
import me.DevTec.ServerControlReloaded.Commands.Time.Night;
import me.DevTec.ServerControlReloaded.Commands.Time.PDay;
import me.DevTec.ServerControlReloaded.Commands.Time.PNight;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tp;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpa;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.TpaBlock;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpaall;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpaccept;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpadeny;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpahere;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpall;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tpcancel;
import me.DevTec.ServerControlReloaded.Commands.TpSystem.Tphere;
import me.DevTec.ServerControlReloaded.Commands.Warps.Back;
import me.DevTec.ServerControlReloaded.Commands.Warps.DelHome;
import me.DevTec.ServerControlReloaded.Commands.Warps.DelWarp;
import me.DevTec.ServerControlReloaded.Commands.Warps.Home;
import me.DevTec.ServerControlReloaded.Commands.Warps.HomeOther;
import me.DevTec.ServerControlReloaded.Commands.Warps.Homes;
import me.DevTec.ServerControlReloaded.Commands.Warps.SetHome;
import me.DevTec.ServerControlReloaded.Commands.Warps.SetSpawn;
import me.DevTec.ServerControlReloaded.Commands.Warps.SetWarp;
import me.DevTec.ServerControlReloaded.Commands.Warps.Spawn;
import me.DevTec.ServerControlReloaded.Commands.Warps.Warp;
import me.DevTec.ServerControlReloaded.Commands.Weather.PRain;
import me.DevTec.ServerControlReloaded.Commands.Weather.PSun;
import me.DevTec.ServerControlReloaded.Commands.Weather.Rain;
import me.DevTec.ServerControlReloaded.Commands.Weather.Sun;
import me.DevTec.ServerControlReloaded.Commands.Weather.Thunder;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;

public class CommandsManager {
	private static Map<String, PluginCommand> commands = new HashMap<>();
	
	public static void load(String section, String command, CommandExecutor cs) {
		if(Loader.cmds.getBoolean(section+"."+command+".Enabled")) {
			PluginCommand c = TheAPI.createCommand(Loader.cmds.getString(section+"."+command+".Name"), Loader.getInstance);
			List<String> aliases = new ArrayList<>();
			if(Loader.cmds.exists(section+"."+command+".Aliases")) {
			if(Loader.cmds.get(section+"."+command+".Aliases") instanceof List)
				aliases=Loader.cmds.getStringList(section+"."+command+".Aliases");
			else aliases.add(Loader.cmds.getString(section+"."+command+".Aliases"));
			}
			c.setAliases(aliases);
			c.setExecutor(cs);
			c.setPermission(Loader.cmds.getString(section+"."+command+".Permission"));
			TheAPI.registerCommand(c);
			commands.put(section+":"+command, c);
		}else {
			if(commands.containsKey(section+":"+command)) {
				unload(section,command);
			}
		}
	}
	
	public static void unload(String section, String command) {
		TheAPI.unregisterCommand(commands.get(section+":"+command));
		commands.remove(section+":"+command);
	}
	
	public static void load() {
		//Server
		load("Server", "Stop" , new Stop());
		load("Server", "Reload", new Reload());
		load("Server", "Restart", new Restart());
		
		//Kill
		load("Kill", "Kill",new Kill());
		load("Kill", "KillAll",new KillAll());
		load("Kill", "Suicide",new Suicide());
		
		//Info
		load("Info", "Memory",new RAM());
		load("Info", "Chunks",new Chunks());
		load("Info", "SCR",new SCR());
		load("Info","Seen", new Seen());
		load("Info","ChatFormat", new ChatFormat());
		load("Info","List", new ListCmd());
		load("Info","Staff", new Staff());
		load("Info", "TPS",new TPS());
		load("Info","WhoIs", new WhoIs()); 
		load("Info", "Maintenance",new Maintenance());
		
		//Speed
		load("Speed", "FlySpeed",new FlySpeed());
		load("Speed", "WalkSpeed",new WalkSpeed());
		
		//Warps
		load("Warps", "SetSpawn",new SetSpawn());
		load("Warps", "Spawn",new Spawn());
		load("Warps", "SetWarp",new SetWarp());
		load("Warps", "DelWarp",new DelWarp());
		load("Warps", "Warp",new Warp());
		load("Warps", "Home",new Home());
		load("Warps", "HomeOther", new HomeOther());
		load("Warps", "SetHome",new SetHome());
		load("Warps", "DelHome",new DelHome());
		load("Warps", "Homes",new Homes());
		load("Warps", "Back",new Back());
		
		//Economy
		load("Economy", "BalanceTop",new EcoTop());
		load("Economy", "Balance",new Balance());
		load("Economy", "Economy",new Eco());
		load("Economy", "Pay",new Pay());
		load("Economy", "MultiEconomy", new MultiEconomy());
		
		//Weather
		load("Weather", "Sun",new Sun());
		load("Weather", "Thunder",new Thunder());
		load("Weather", "Rain",new Rain());
		load("Weather", "PlayerSun",new PSun());
		load("Weather", "PlayerRain",new PRain());
		
		//Time
		load("Time", "Day",new Day());
		load("Time", "Night",new Night());
		load("Time", "PDay",new PDay());
		load("Time", "PNight",new PNight());
		
		//Message
		load("Message","SocialSpy", new SocialSpy());
		load("Message","Mail", new Mail());
		load("Message","Sudo", new Sudo());
		load("Message","Broadcast", new Broadcast());
		load("Message", "PrivateMessage", new PrivateMessage());
		load("Message", "ClearChat",new ClearChat());
		load("Message","Helpop", new Helpop());
		load("Message","Reply", new ReplyPrivateMes());
		load("Message","Ignore", new PrivateMessageIgnore());
		
		//Gamemode
		load("GameMode", "GameMode",new Gamemode());
		load("GameMode", "GameModeSurvival",new GamemodeS());
		load("GameMode", "GameModeCreative",new GamemodeC());
		load("GameMode", "GameModeAdventure",new GamemodeA());
		if(TheAPI.isNewerThan(7))
			load("GameMode", "GameModeSpectator",new GamemodeSP());
			
		//BanSystem	
		load("BanSystem", "Kick", new Kick());
		load("BanSystem", "Ban", new Ban());
		load("BanSystem", "Immune", new Immune());
		load("BanSystem", "TempBan", new TempBan());
		load("BanSystem", "Jail",new Jail());
		load("BanSystem", "TempBanIP",new TempBanIP());
		load("BanSystem", "UnJail",new UnJail());
		load("BanSystem", "SetJail",new SetJail());
		load("BanSystem", "DelJail",new DelJail());
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
		load("Inventory", "ClearConfirmToggle", new ClearConfirmToggle());
		load("Inventory", "Inventory", new Invsee());
		load("Inventory", "Workbench", new Craft());
		
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
		
		//Other
		load("Other", "ChatLock",new ChatLock());
		load("Other", "Repair", new Repair());
		load("Other", "Feed", new Feed());
		load("Other", "Item", new Item());
		load("Other", "TempFly", new TempFly());
		load("Other", "ScoreBoard", new Scoreboard());
		load("Other", "ActionBar", new ActionBar());
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
		load("Other", "TabList",new Tab());
		load("Other", "Hat",new Hat());
		load("Other", "Skin",new Skin());
		load("Other", "Experiences", new Exp());
		load("Other", "Spawner", new Spawner());
		load("Other", "Uuid", new Uuid());
		//Utility
		load("Other", "Colors", new ColorsCmd());
		load("Other", "Rules", new RulesCmd());
		//Nickname
		load("Nickname", "Nickname", new Nick());
		load("Nickname", "NicknameReset", new NickReset());
		for(Player p : TheAPI.getOnlinePlayers())p.updateCommands();
	}
	
	public static void unload() {
		for(PluginCommand s : commands.values())
			TheAPI.unregisterCommand(s);
		commands.clear();
		for(Player p : TheAPI.getOnlinePlayers())p.updateCommands();
	}

	public static boolean isLoaded(String section, String command) {
		return commands.containsKey(section+":"+command);
	}
}
