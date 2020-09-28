package Utils;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;

import ServerControl.Loader;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Utils.TheAPIUtils.Validator;

public class Configs {
	
	public static void load() {
		configLoading();
		String lang = Loader.config.getString("Options.Language");
		if(!new File("ServerControlReloaded/translation-"+lang+".yml").exists()) {
			Validator.send("File translation-"+lang+".yml doesn't exist", new NoSuchFileException("File translation-"+lang+".yml doesn't exist"));
			lang="en";
		}
		Loader.trans = new Config("ServerControlReloaded/translation-"+lang+".yml");
		MultiWorldLoading();
		ScoreboardLoading();
		TabLoading();
		KitLoading();
	}

	private static void configLoading() {
		Loader.config= new Config("ServerControlReloaded/Config.yml");
		Loader.config.addDefault("Options.Maintenance.Enabled", false);
		Loader.config.addDefault("Options.Language", "en");
		Loader.config.addDefault("Options.Maintenance.KickMessages",
				Arrays.asList("&8=-=-=-=-=-= &eMaintenance Mode &8=-=-=-=-=-=",
						"&cWe are sorry %playername%, the server is currently under maintenance mode",
						"&cMore informations can found on our web: &cwww.example.com",
						"&8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
		Loader.config.addDefault("Options.Codes.Use", true);
		Loader.config.addDefault("Options.Codes.Message", "&eCongratulation, you found secret code: &c%code%");
		Loader.config.addDefault("Options.Codes.List",
				Arrays.asList("ServerControlReloaded", "TheAPI", "Straiker123", "Houska02", "2020", "Secret", "Codes"));
		Loader.config.addDefault("Options.Codes.Random-Command", Arrays.asList("eco give %player% 50", "eco give %player% 150",
				"give %player% diamond 2", "give %player% iron_block 1", "xp give %player% 150"));
		Loader.config.addDefault("Options.Codes.Commands", Arrays.asList("eco give %player% 25"));
		if (!Loader.config.exists("Options.Disable-Items")) {
			Loader.config.addDefault("Options.Disable-Items.Worlds.Creative", Arrays.asList("Bedrock", "TNT", "TNT_Minecart"));
			Loader.config.addDefault("Options.Disable-Items.Worlds.world", Arrays.asList("Bedrock"));
			Loader.config.addDefault("Options.Disable-Items.Worlds.world_nether", Arrays.asList("Bedrock"));
			Loader.config.addDefault("Options.Disable-Items.Worlds.world_the_end", Arrays.asList("Bedrock"));
		}
		Loader.config.addDefault("Options.ChatLock", false);
		Loader.config.addDefault("Options.FarmingSystem", true);
		Loader.config.addDefault("Options.SinglePlayerSleep", true);
		Loader.config.addDefault("Options.Disable-Items.Use", true);
		Loader.config.addDefault("Options.TimeZone.Use", false);
		Loader.config.addDefault("Options.TimeZone.Zone", "Europe/Prague");
		
		Loader.config.addDefault("Options.Colors.Chat.Use", true);
		Loader.config.addDefault("Options.Colors.Chat.Required-Permission", true);
		Loader.config.addDefault("Options.Colors.Chat.Permission.Color", "ServerControl.ChatColor");
		Loader.config.addDefault("Options.Colors.Chat.Permission.Format", "ServerControl.ChatColor.Format");
		Loader.config.addDefault("Options.Colors.Chat.Permission.Magic", "ServerControl.ChatColor.Magic");
		Loader.config.addDefault("Options.Colors.Chat.Permission.Hex", "ServerControl.ChatColor.Hex");
		Loader.config.addDefault("Options.Colors.Sign.Use", true);
		Loader.config.addDefault("Options.Colors.Sign.Required-Permission", true);
		Loader.config.addDefault("Options.Colors.Sign.Permission.Color", "ServerControl.SignColor");
		Loader.config.addDefault("Options.Colors.Sign.Permission.Format", "ServerControl.SignColor.Format");
		Loader.config.addDefault("Options.Colors.Sign.Permission.Magic", "ServerControl.SignColor.Magic");
		Loader.config.addDefault("Options.Colors.Sign.Permission.Hex", "ServerControl.SignColor.Hex");
		
		Loader.config.addDefault("Options.Sounds.Use", true);
		Loader.config.addDefault("Options.Sounds.Sound", "ENTITY_PLAYER_LEVELUP");
		Loader.config.addDefault("Options.Teleport.SafeLocation", true);
		Loader.config.addDefault("Options.Teleport.RequestTime", "1min");
		Loader.config.addDefault("Options.Teleport.CommandSendLocation", true);
		Loader.config.addDefault("Options.RespawnTeleport", "home"); // spawn, home, bed
		Loader.config.addDefault("Options.Use-RespawnTeleport", true);
		Loader.config.addDefault("Options.AFK.AutoAFK", true);
		Loader.config.addDefault("Options.AFK.TimeToAFK", "5min");
		Loader.config.addDefault("Options.AFK.AutoKick", true);
		Loader.config.addDefault("Options.AFK.TimeToKick", "10min");
		Loader.config.addDefault("Options.AFK.KickMessage", "&cYou were kicked for AFK longer than 10 minutes.");
		Loader.config.addDefault("Options.Economy.Money", 100);
		Loader.config.addDefault("Options.Economy.DisablePluginEconomy", false);
		Loader.config.addDefault("Options.Economy.CanUseOtherEconomy", true);
		Loader.config.addDefault("Options.Economy.BalanceTop", "&6%position%. &c%playername% &a&o(%money%&a&o)");
		Loader.config.addDefault("Options.Economy.Log", false);
		Loader.config.addDefault("Options.Economy.MultiEconomy.Use", false);
		if (!Loader.config.exists("Options.Economy.MultiEconomy.Types")) {
			Loader.config.addDefault("Options.Economy.MultiEconomy.Types.default",
					Arrays.asList("world", "world_nether", "world_the_end"));
			Loader.config.addDefault("Options.Economy.MultiEconomy.Types.SkyBlock",
					Arrays.asList("SkyBlock_Normal", "SkyBlock_Nether", "SkyBlock_The_End", "SkyBlock_Shop"));
		}
		Loader.config.addDefault("Options.WarningSystem.Reload.Use", true);
		Loader.config.addDefault("Options.WarningSystem.Reload.PauseTime", 5);
		Loader.config.addDefault("Options.WarningSystem.Reload.Messages",
				Arrays.asList("&8****** &4Warning &8******", "&cReload of server in %time%",
						"&cPlease login using /login <password>", "&8****** &4Warning &8******"));
		Loader.config.addDefault("Options.WarningSystem.Restart.Use", true);
		Loader.config.addDefault("Options.WarningSystem.Restart.PauseTime", 15);
		Loader.config.addDefault("Options.WarningSystem.Restart.Messages", Arrays.asList("&8****** &4Warning &8******",
				"&cRestart of server in %time%", "&8****** &4Warning &8******"));
		Loader.config.addDefault("Options.WarningSystem.Stop.Use", true);
		Loader.config.addDefault("Options.WarningSystem.Stop.PauseTime", 15);
		Loader.config.addDefault("Options.WarningSystem.Stop.Messages", Arrays.asList("&8****** &4Warning &8******",
				"&cStopping of server in %time%", "&8****** &4Warning &8******"));
		Loader.config.addDefault("Options.AutoMessage.Use", true);
		Loader.config.addDefault("Options.AutoMessage.MinimalPlayers", 1);
		Loader.config.addDefault("Options.AutoMessage.Interval", "5min");
		Loader.config.addDefault("Options.AutoMessage.Random", true);
		Loader.config.addDefault("Options.AutoMessage.Messages", Arrays.asList(
				"&0[&a&lINFO&0] &cServerControlReloaded created by &nDevTec", "&0[&a&lINFO&0] &c20% Bugs free",
				"&0[&a&lINFO&0] &cOnline players &a%online% &7/ &a%max_players%",
				"&0[&a&lINFO&0] &cYou found a bug ? Report it to https://github.com/TheDevTec/ServerControlReloaded",
				"&0[&a&lINFO&0] &cDo you like our plugin? Write a comment on the https://www.spigotmc.org/resources/server-control-reloaded.71147"));
		Loader.config.addDefault("Options.Cost-ClearInvUndo", 50);
		Loader.config.addDefault("Options.RAM-Percentage", true);
		Loader.config.addDefault("Options.ServerList.MOTD.Use", true);
		Loader.config.addDefault("Options.ServerList.MOTD.Maintenance", false);
		Loader.config.addDefault("Options.ServerList.MOTD.Text.Normal",
				"&8-------< &eMinecraft Server &8>-------%line%&aServer is online");
		Loader.config.addDefault("Options.ServerList.MOTD.Text.Maintenance",
				"&8-------< &eMinecraft Server &8>-------%line%&cServer is under Maintenance Mode");
		Loader.config.addDefault("Options.VIPSlots.Use", true);
		Loader.config.addDefault("Options.VIPSlots.AddSlots", false);
		Loader.config.addDefault("Options.VIPSlots.SlotsToAdd", 4);
		Loader.config.addDefault("Options.VIPSlots.KickWhenFullServer", true);
		Loader.config.addDefault("Options.VIPSlots.VIPJoinBroadcast", false);
		Loader.config.addDefault("Options.VIPSlots.Text.Kick", "&eBuy &nVIP&r&e on our site and get access to this &nreserved slot");
		Loader.config.addDefault("Options.VIPSlots.Text.BroadcastVIPJoin", "&eVIP player joined to the server");
		Loader.config.addDefault("Options.VIPSlots.Text.FullServer", "&cAll &nVIP Slots&r &care in use");
		Loader.config.addDefault("Options.Join.TeleportToSpawn", false);
		Loader.config.addDefault("Options.Join.CustomJoinMessage", true);
		Loader.config.addDefault("Options.Join.MOTD", true);
		Loader.config.addDefault("Options.Join.FirstJoin.Use", true);
		Loader.config.addDefault("Options.Join.FirstJoin.GiveKit", true);
		Loader.config.addDefault("Options.Join.FirstJoin.Kit", "Default");
		Loader.config.addDefault("Options.Join.FirstJoin.Wait", 5); // only for give kit, commands
		Loader.config.addDefault("Options.Join.FirstJoin.PerformCommands.Use", false);
		Loader.config.addDefault("Options.Join.FirstJoin.PerformCommands.List",
				Arrays.asList("give %player% stone_sword 1", "eco give %player% 10"));
		Loader.config.addDefault("Options.Leave.CustomLeaveMessage", true);
		Loader.config.addDefault("Options.Security.AntiAD.WhiteList", Arrays.asList("minecraft.net", "bukkit.org", "spigot.org"));
		Loader.config.addDefault("Options.Security.AntiAD.Book", true);
		Loader.config.addDefault("Options.Security.AntiAD.Sign", true);
		Loader.config.addDefault("Options.Security.AntiAD.Chat", true);
		Loader.config.addDefault("Options.Security.AntiAD.Commands", true);
		Loader.config.addDefault("Options.Security.AntiAD.Anvil", true);
		Loader.config.addDefault("Options.Security.AntiAD.ItemDrop", true);
		Loader.config.addDefault("Options.Security.AntiAD.ItemPickup", true);
		Loader.config.addDefault("Options.Security.AntiSwear.Chat", true);
		Loader.config.addDefault("Options.Security.AntiSwear.Commands", false);
		Loader.config.addDefault("Options.Security.AntiSpam.Chat", true);
		Loader.config.addDefault("Options.Security.AntiSpam.Commands", false);
		Loader.config.addDefault("Options.Security.AntiCaps.Chat", true);
		Loader.config.addDefault("Options.Security.AntiCaps.Commands", false);
		Loader.config.addDefault("Options.Cooldowns.Chat.Use", true);
		Loader.config.addDefault("Options.Cooldowns.Chat.Time", 2);
		Loader.config.addDefault("Options.Cooldowns.Commands.Use", true);
		Loader.config.addDefault("Options.Cooldowns.Commands.Time", 1);
		Loader.config.addDefault("Options.Cooldowns.Commands.PerCommand.Use", true); // <command>:<cooldown>
		Loader.config.addDefault("Options.Cooldowns.Commands.PerCommand.List",
				Arrays.asList("Heal:300", "Tpa:30", "TpaHere:30", "TpaAll:30", "Suicide:60", "Feed:150"));
		Loader.config.addDefault("Options.CommandsBlocker.Use", true);
		Loader.config.addDefault("Options.CommandsBlocker.List",
				Arrays.asList("pl", "plugins", "version", "ihasbukkit", "spigot", "reload", "stop", "restart"));
		Loader.config.addDefault("StaffList", Arrays.asList("owner", "admin", "operator", "helper", "builder", "developer"));
		Loader.config.addDefault("BanSystem.TempMute.Text", "&cYou are temp-muted for &6%reason%&c on &6%time%");
		Loader.config.addDefault("BanSystem.TempMute.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.TempMute.Time", "1h");
		Loader.config.addDefault("BanSystem.Kick.Text", "&cYou are kicked for &6%reason%\n&7www.example.com");
		Loader.config.addDefault("BanSystem.Kick.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.Ban.Text", "&cYou are banned for &6%reason%\n&7www.example.com");
		Loader.config.addDefault("BanSystem.Ban.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.TempBanIP.Text", "&cYou are temp-ipbanned for &6%reason%&c on &6%time%\n&7www.example.com");
		Loader.config.addDefault("BanSystem.TempBanIP.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.TempBanIP.Time", "7d");
		Loader.config.addDefault("BanSystem.TempBan.Text", "&cYou are temp-banned for &6%reason%&c on &6%time%\n&7www.example.com");
		Loader.config.addDefault("BanSystem.TempBan.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.TempBan.Time", "1h");
		Loader.config.addDefault("BanSystem.BanIP.Text", "&cYou are ipbanned for &6%reason%\n&7www.example.com");
		Loader.config.addDefault("BanSystem.BanIP.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.Mute.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.Mute.Text", "&cYou are muted for &6%reason%");
		Loader.config.addDefault("BanSystem.Jail.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.Jail.Text", "&cYou are arrested for &6%reason%");
		Loader.config.addDefault("BanSystem.TempJail.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.TempJail.Time", "1h");
		Loader.config.addDefault("BanSystem.TempJail.Text", "&cYou are temp-arrested for &6%reason%&c on &6%time%");

		Loader.config.addDefault("BanSystem.Warn.Text", "&7----------\n&cWarning\n&cReason: &6%reason%\n&7----------");
		Loader.config.addDefault("BanSystem.Warn.Reason", "Unknown");
		if (!Loader.config.exists("BanSystem.Warn.Operations")) {
			Loader.config.addDefault("BanSystem.Warn.Operations.1.Commands", Arrays.asList("eco take %player% 20"));
			Loader.config.addDefault("BanSystem.Warn.Operations.1.Messages", Arrays.asList("&0[&4Warning&0] &cYou have last 20 warnings"));
			Loader.config.addDefault("BanSystem.Warn.Operations.5.Commands",
					Arrays.asList("eco take %player% 50", "kick %player% &0[&4Warning&0] &cYou have 5 warnings"));
			Loader.config.addDefault("BanSystem.Warn.Operations.20.Commands", Arrays.asList("eco take %player% 100"));
			Loader.config.addDefault("BanSystem.Warn.Operations.20.Messages", Arrays.asList("&0[&4Warning&0] &cThis is last warning"));
			Loader.config.addDefault("BanSystem.Warn.Operations.21.Commands",
					Arrays.asList("tempban %player% 2d &0[&4Info&0] &cYour life is lie!"));
		}
		Loader.config.addDefault("BanSystem.Mute.Reason", "Unknown");
		Loader.config.addDefault("BanSystem.Mute.DisableChat", true);
		Loader.config.addDefault("BanSystem.Mute.DisableCmds", true);
		Loader.config.addDefault("BanSystem.Mute.DisabledCmds", Arrays.asList("m", "r", "tell", "msg", "message", "pm", "w", "say"));
		Loader.config.addDefault("Chat-Groups-Enabled", true);
		if (!Loader.config.exists("Chat-Groups.DefaultFormat")) {
			Loader.config.addDefault("Chat-Groups.DefaultFormat.Chat", "%player% &8> &f%message%");
			Loader.config.addDefault("Chat-Groups.DefaultFormat.Name", "&7%vault-group% &f%player%");
			Loader.config.addDefault("Chat-Groups.default.Chat", "%player% &8> &f%message%");
			Loader.config.addDefault("Chat-Groups.default.Name", "&7%player%");
			Loader.config.addDefault("Chat-Groups.vip.Chat", "%player% &8> &a%message%");
			Loader.config.addDefault("Chat-Groups.vip.Name", "&aVIP &e%player%");
			Loader.config.addDefault("Chat-Groups.admin.Chat", "%player% &8> &3%message%");
			Loader.config.addDefault("Chat-Groups.admin.Name", "&4Admin &3%player%");
			Loader.config.addDefault("Chat-Groups.owner.Chat", "%player% &8> &3%message%");
			Loader.config.addDefault("Chat-Groups.owner.Name", "&3&lOwner &f%player%");
		}
		if (!Loader.config.exists("Homes.default")) {
			Loader.config.addDefault("Homes.default", 3);
			Loader.config.addDefault("Homes.vip", 3);
			Loader.config.addDefault("Homes.helper", 4);
			Loader.config.addDefault("Homes.operator", 5);
			Loader.config.addDefault("Homes.admin", 5);
			Loader.config.addDefault("Homes.builder", 7);
			Loader.config.addDefault("Homes.owner", 10);
		}
		Loader.config.addDefault("List.LoadedFormat",
				"&6World: &e%world%&6, Chunks: &e%chunks%&6, Mobs: &e%mobs%&6, Players: &e%players%");
		Loader.config.addDefault("List.UnloadedFormat", "&6World: &e%world% &7(Unloaded)");
		Loader.config.addDefault("HelpFormat", "%prefix%&e%command% &7- &5%help%");
		Loader.config.addDefault("Format.Time", "HH:mm:ss");
		Loader.config.addDefault("Format.Date", "dd.MM.yyyy");
		Loader.config.addDefault("Format.DateWithTime", "dd.MM.yyyy HH:mm:ss");
		Loader.config.addDefault("Format.Broadcast", "&0[&4Broadcast&0] &a%message%");
		Loader.config.addDefault("Format.PrivateMessageTo", "&8(&eYou -> %to%&8): &f%message%");
		Loader.config.addDefault("Format.PrivateMessageFrom", "&8(&e%from% -> You&8): &f%message%");
		Loader.config.addDefault("Format.Kick", "&6You was kicked for &c%reason%");
		Loader.config.addDefault("Format.Ban", "&6You was banned for &c%reason%");
		Loader.config.addDefault("Format.TempBan", "&6You was temp-banned for &c%reason% &6on &c%time%");
		Loader.config.addDefault("Format.BanIP", "&6You was ip banned for &c%reason%");
		Loader.config.addDefault("Format.Helpop", "&0[&4HelpOp&0] &6%playername%&f: &6%message%");
		Loader.config.addDefault("Task.Spam.Use", true);
		Loader.config.addDefault("Task.Spam.Commands", Arrays.asList("msg %player% &0[&4AntiSpam&0] &cPlease, stop spamming"));
		Loader.config.addDefault("Task.Swear.Use", true);
		Loader.config.addDefault("Task.Swear.Commands",
				Arrays.asList("msg %player% &0[&4AntiSwear&0] &cPlease, stop being vulgar"));
		Loader.config.addDefault("Task.Ad.Use", true);
		Loader.config.addDefault("Task.Ad.Commands", Arrays
				.asList("msg %player% &0[&4AntiAD&0] &cPlease, stop sending advertisements", "eco take %player% 150"));
		Loader.config.addDefault("WritingFormat.Spam", "%player%: %message% - Spam (%spam%)");
		Loader.config.addDefault("WritingFormat.Swear", "%player%: %message% - Swear (%vulgarword%)");
		Loader.config.addDefault("WritingFormat.Advertisement", "%player%: %message% - Advertisement");
		Loader.config.addDefault("WritingFormat.Blocked-Command", "%player%: %message%");
		Loader.config.addDefault("AutoKickLimit.Swear.Use", true);
		Loader.config.addDefault("AutoKickLimit.Swear.Number", 3);
		Loader.config.addDefault("AutoKickLimit.Swear.Message.Use", true);
		Loader.config.addDefault("AutoKickLimit.Swear.Message.List",
				Arrays.asList("&6=-=-=-=-=-=-=-=-=-=-=-=-=-=",
						"&cYou have reached the maximum number of Vulgar Words! &o(%number%)",
						"&6=-=-=-=-=-=-=-=-=-=-=-=-=-="));
		Loader.config.addDefault("AutoKickLimit.Swear.Commands.Use", true);
		Loader.config.addDefault("AutoKickLimit.Swear.Commands.List",
				Arrays.asList("kick %player% &cYou have reached maximum warnings for &nswearing&r&c &o(%number%)",
						"eco take %player% 350"));
		Loader.config.addDefault("AutoKickLimit.Spam.Use", true);
		Loader.config.addDefault("AutoKickLimit.Spam.Number", 5);
		Loader.config.addDefault("AutoKickLimit.Spam.Message.Use", true);
		Loader.config.addDefault("AutoKickLimit.Spam.Message.List", Arrays.asList("&6=-=-=-=-=-=-=-=-=-=-=-=-=-=",
				"&cYou have reached the maximum number of Spams! &o(%number%)", "&6=-=-=-=-=-=-=-=-=-=-=-=-=-="));
		Loader.config.addDefault("AutoKickLimit.Spam.Commands.Use", true);
		Loader.config.addDefault("AutoKickLimit.Spam.Commands.List",
				Arrays.asList("kick %player% &cYou have reached maximum warnings for &nspaming&r&c &o(%number%)"));
		Loader.config.addDefault("AutoKickLimit.Kick.Use", true);
		Loader.config.addDefault("AutoKickLimit.Kick.Number", 5);
		Loader.config.addDefault("AutoKickLimit.Kick.Message.Use", true);
		Loader.config.addDefault("AutoKickLimit.Kick.Message.List", Arrays.asList("&6=-=-=-=-=-=-=-=-=-=-=-=-=-=",
				"&4&lYou have reached the maximum number of Kicks! &o(%number%)", "&6=-=-=-=-=-=-=-=-=-=-=-=-=-="));
		Loader.config.addDefault("AutoKickLimit.Kick.Commands.Use", true);
		Loader.config.addDefault("AutoKickLimit.Kick.Commands.List",
				Arrays.asList("tempban %player% 7d &4&lYou have reached maximum number of &nkicks&r&4&l &o(%number%)",
						"eco take %player% 5000"));
		Loader.config.addDefault("SwearWords", Arrays.asList("fuck", "idiot", "kurv", "kurw"));
		Loader.config.addDefault("SpamWords.SimiliarMessage", true);
		Loader.config.addDefault("SpamWords.DoubledLetters.Use", true);
		Loader.config.addDefault("SpamWords.Words", Arrays.asList("?????", "!!!!!", "....."));
		Loader.config.getData().setHeader(Arrays.asList("+-------------------------------------------------------------------+ #"
				, "| Info: https://dev.bukkit.org/projects/server-control-reloaded     | #"
				, "+-------------------------------------------------------------------+ #"
				, "Options for RespawnTeleport are: Home, Bed, Spawn" + "PlaceHolders for AutoMessage:"
				, "  %used_ram% - used memory\n" + "  %free_ram% -  free memory\n" + "  %max_ram% - maximum memory"
				, "  %online% - online players\n" + "  %max_players% - maximum players on server"
				, "  %time% - server time\n" + "  %date% - server date"
				, "+-------------------------------------------+ #"
				, "| INFO: TimeZones! List of time zones:      | #"
				, "+- https://greenwichmeantime.com/time-zone -+ #"));
	}

	private static void ScoreboardLoading() {
		Loader.sb=new Config("ServerControlReloaded/Scoreboard.yml");
		Loader.sb.addDefault("Enabled", true);
		Loader.sb.addDefault("PerWorld", false);
		Loader.sb.addDefault("RefleshTick", 20);
		Loader.sb.addDefault("Name", "&bStatus");
		Loader.sb.addDefault("Lines", Arrays.asList("&r&lMoney: &a%money%$", "&r&lOnline:  &a%online%"));
		if(!Loader.sb.exists("PerWorld")) {
		Loader.sb.addDefault("PerWorld.pvp_world.Name", "&9PvP");
		Loader.sb.addDefault("PerWorld.pvp_world.Lines", Arrays.asList("&r&lKills: &a%kills%$", "&r&lHealth:  &a%health%"));

		Loader.sb.addDefault("PerWorld.skyblock.Name", "&eSkyBlock");
		Loader.sb.addDefault("PerWorld.skyblock.Lines",
				Arrays.asList("&r&lMoney: &a%money%$", "&r&lHealth:  &a%health%", "&r&lFood:  &a%food%"));
		}
	}

	private static void TabLoading() {
		Loader.tab=new Config("ServerControlReloaded/TabList.yml");
		Loader.tab.addDefault("Tab-Enabled", true);
		Loader.tab.addDefault("Header-Enabled", true);
		Loader.tab.addDefault("Footer-Enabled", true);
		Loader.tab.addDefault("Colored-Ping", true);
		Loader.tab.addDefault("ModifyNameTags", true);
		Loader.tab.addDefault("SortTabList", true);
		Loader.tab.addDefault("Header",
				Arrays.asList("&bWelcome back &a%playername%&b!",
						"&bTPS: &a%tps%   &bFree Ram: &a%ram_free_percentage%",
						"&bMoney: &9%money%$   &bPing: &9%ping%", "&7--------------------------------"));
		Loader.tab.addDefault("Footer", Arrays.asList("&7--------------------------------",
				"&bTime: &a%time%   &bOnline: &a%online% &0/ &a%max_players%"));
		Loader.tab.addDefault("RefleshTick", 20);
		Loader.tab.addDefault("NameTag-RefleshTick", 20);
		Loader.tab.addDefault("AFK.IsAFK", " &4&l*AFK*");
		Loader.tab.addDefault("AFK.IsNotAFK", "");
		if (!Loader.tab.exists("Groups.default")) {
			Loader.tab.addDefault("Groups.default.TabList.Prefix", "&7Player &r");
			Loader.tab.addDefault("Groups.default.TabList.Suffix", "%afk%");
			Loader.tab.addDefault("Groups.default.NameTag.Prefix", "&7Player &r");
			Loader.tab.addDefault("Groups.default.Name", "%prefix% %playername% %suffix%");
			Loader.tab.addDefault("Groups.default.Priorite", "z");
			Loader.tab.addDefault("PerPlayerTabList.timtower.Footer",
					Arrays.asList("&7--------------------------------", "&6Money: &a%money%$   &6Rank: &a%group%"));
			Loader.tab.addDefault("PerPlayerTabList.timtower.Header",
					Arrays.asList("&4Welcome back %playername% !", "&7--------------------------------"));
			Loader.tab.addDefault("PerWorldTabList.world1.Footer",
					Arrays.asList("&7--------------------------------", "&6Online: &a%online%$   &6Rank: &a%group%"));
			Loader.tab.addDefault("PerWorldTabList.world1.Header", Arrays.asList("&2TabList in world %world%",
					"&6Health: &a%hp%$   &6Food: &a%food%", "&7--------------------------------"));
		}
	}

	private static void MultiWorldLoading() {
		Loader.mw=new Config("ServerControlReloaded/MultiWorlds.yml");
		Loader.mw.addDefault("ModifyMobsSpawnRates", false);
		Loader.mw.addDefault("SavingTask.Enabled", true);
		Loader.mw.addDefault("SavingTask.Delay", 3600);
	}

	private static void KitLoading() {
		Loader.kit=new Config("ServerControlReloaded/Kits.yml");
		if (!Loader.kit.exists("Kits")) {
			Loader.kit.addDefault("Kits.Default.Items.Stone.Amount", 16);
			Loader.kit.addDefault("Kits.Default.Items.Stone_Pickaxe.Amount", 1);
			Loader.kit.addDefault("Kits.Default.Items.Stone_Pickaxe.CustomName", "&8Normal pickaxe");
			Loader.kit.addDefault("Kits.Default.Price", 25);
			Loader.kit.addDefault("Kits.Default.Cooldown", 3600);
			Loader.kit.addDefault("Kits.VIP.Items.Diamond.Amount", 3);
			Loader.kit.addDefault("Kits.VIP.Items.Diamond.CustomName", "&bShiny Diamond");
			Loader.kit.addDefault("Kits.VIP.Items.Iron_Pickaxe.Amount", 1);
			Loader.kit.addDefault("Kits.VIP.Items.Iron_Pickaxe.CustomName", "&eSuper pickaxe");
			Loader.kit.addDefault("Kits.VIP.Items.Iron_Pickaxe.Lore", Arrays.asList("&4The best of best"));
			Loader.kit.addDefault("Kits.VIP.Items.Iron_Pickaxe.Enchantments", Arrays.asList("SHARPNESS:4", "UNBREAKING:2"));
			Loader.kit.addDefault("Kits.VIP.Price", 60);
			Loader.kit.addDefault("Kits.VIP.Cooldown", 3600);
		}
	}
}
