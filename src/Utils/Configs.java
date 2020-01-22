package Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ServerControl.Loader;
import me.Straiker123.ConfigAPI;
import me.Straiker123.TheAPI;

public class Configs {
	public static ConfigAPI trans;
	public static ConfigAPI config;
	public static ConfigAPI chatme;
	public static ConfigAPI bans;
	public static ConfigAPI sb;
	public static ConfigAPI tab;
	public static ConfigAPI chat;
	public static ConfigAPI mw;
	public static ConfigAPI kit;
	public static void TranslationsLoading() {
		Map<String, Object> c = new HashMap<String, Object>();
			c.put("Prefix", "&4SCR &e> ");
			c.put("Skull-GivenToPlayer", "&6Given player head '%head%' to player %playername%");
			c.put("Skull-Given", "&6Given player head '%head%' to you");
			c.put("Seen.Online", "&6Player %playername% &6is &aonline &6for &a%online%");	
			c.put("Seen.Offline", "&6Player %playername% &6is &coffline &6for &c%offline%");	
			c.put("PlayerList.Staff", Arrays.asList("&7=-=-=- %online% / %max_players% -=-=-=","&8Online staff: &a%staff%","&7=-=-=-=-=-=-=-=-="));	
			c.put("PlayerList.Normal", Arrays.asList("&7=-=-=- %online% / %max_players% -=-=-=","&8AT: &a%staff%","&8Players: &a%players%","&8VIP: &a%VIP% %SuperVIP%","&8All: &a%all%"));	
			c.put("Give.UknownItem", "&6Item/Block with name &c'%item%' &6is invalid");
			c.put("Give.Given", "&6Given %amount%x %item% to player %playername%");
			c.put("Kill.Killed", "&6Killed %playername%");
			c.put("Kill.Suicide", "&6You killed yourself");
			c.put("Kill.KilledAll", "&6Killed &a%amount% &6players &a(%players%)");
			c.put("Butcher.WorldIsInvalid", "&6World &c&%world% &6doesn't exists");
			c.put("Butcher.EntityIsInvalid", "&6Entity with name &c&%entity% &6doesn't exists");
			c.put("Butcher.Killed", "&6Killed &a%amount% &6entities");
			c.put("Butcher.KilledSpecified", "&6Killed &a%amount% &6entities type &a%entity%");
			c.put("RAM.Info.Normal", Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-","&aUsed Memory: &6%used_ram% MB","&aMax Memory: &6%max_ram% MB","&6-=-=-=-=-=-=-=-=-=-=-=-"));	
			c.put("RAM.Info.Percent", Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"
					,"&aFree: &6%free_ram%% &8/ &aUsed: &6%used_ram%% &8/ &aMax: &6%max_ram% MB","&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
			c.put("Xp.Balance", "&6%playername% &6currently have &a%amount%exps");
			c.put("Xp.Given", "&6Given &a%amount%exps &6to %playername%");
			c.put("Xp.Taken", "&6Taken &c%amount%exps &6from %playername%");
			c.put("RAM.Clearing", "&6Clearing memory cache, please wait..");
			c.put("RAM.Cleared", "&6Cleared &a%cleared% MB");
			c.put("RAM.AlreadyClearing", "&cMemory is clearing, please wait..");
			c.put("PlayerNotOnline", "&cPlayer &4%player% &cis not online");
			c.put("PlayerNotExists", "&cPlayer &4%player% &cnot exists");
			c.put("Item.SetName", "&6Set custom name &a'%name%&a' &6on item &a%item%");
			c.put("Item.SetLore.Add", "&6Added lore &a'%lore%&a' &6on item &a%item%");
			c.put("Item.SetLore.Remove", "&6Removed lore line &a%line% &6from item &a%item%");
			c.put("Item.SetLore.RemoveError", "&6On item &c%item% &6isn't line &c%line%");
			c.put("Item.SetLore.Lines-List", "&6List of lore lines on item &a%item%&6:");
			c.put("Item.SetLore.Lines-Format", "&6%position%. line: &5%lore%");
			c.put("Item.Unbreakable.true", "&6Unbreakable on item &a%item% &aenabled");
			c.put("Item.Unbreakable.false", "&6Unbreakable on item &a%item% &cdisabled");
			c.put("Item.HideEnchants.true", "&6Enchants on item &a%item% &6are now invisible");
			c.put("Item.HideEnchants.false", "&6Enchants on item &a%item% &6are no longer invisible");
			c.put("Hat.HandIsEmpty", "&cFirst take item to your &nhand");
			c.put("Hat.Equiped", "&aItem/Block from your hand equiped to your head!");
			c.put("Hat.EquipedToOther", "&aItem/Block from your hand equiped to %target%'s head!");
			c.put("MultiEconomy.AlreadyCreated", "&6Economy group %economy-group% is already exists");
			c.put("MultiEconomy.Created", "&6Economy group %economy-group% created");
			c.put("MultiEconomy.NotExists", "&6Economy group %economy-group% doesn't exists");
			c.put("MultiEconomy.Groups", "&6Economy groups: &a%groups%");
			c.put("MultiEconomy.Worlds", "&6Worlds in economy group %economy-group%: &a%worlds%");
			c.put("MultiEconomy.Deleted", "&6Economy group %economy-group% deleted");
			c.put("MultiEconomy.WorldAdded", "&aWorld %world% added to economy group %economy-group%");
			c.put("MultiEconomy.WorldAlreadyAdded", "&a6World %world% is already in economy group %economy-group%");
			c.put("MultiEconomy.WorldIsNotInGroup", "&6World %world% isn't in economy group %economy-group%");
			c.put("MultiEconomy.WorldRemoved", "&6World %world% &aremoved &6from economy group &a%economy-group%");
			c.put("MultiEconomy.NoMoney", "&6Player %playername% &6doesn't have any money in economy group %economy-group%");
			c.put("MultiEconomy.HaveMoney", "&6Player %playername% &6currently have %money%$ in economy group %economy-group%");
			c.put("MultiEconomy.Transfer", "&6Transferred economy of player %playername% &6to economy group %economy-group%");
			c.put("MultiEconomy.WorldNotExists", "&cWorld &4'%world%' &cdoesn't exists");
			c.put("MultiEconomy.WorldNotExists", "&cWorld &4'%world%' &cdoesn't exists");
			c.put("MultiEconomy.Deleted", "&6Economy group %economy-group% deleted");
			c.put("NicknameChanged", "&6Your nickname changed to &r%nickname%");
			c.put("NicknameReseted", "&6Your nickname reseted");
			c.put("NicknameResetedOther", "&6Nickname of player %player% reseted");
			c.put("AFK.IsAFK", "%playername% &cis AFK");
			c.put("AFK.NoLongerAFK", "%playername% &7is no longer AFK");
			c.put("PrivateMessage.NoPlayerToReply", "&6You have never written a private message to anyone to reply");
			c.put("BanSystem.Broadcast.Ban", "&6Operator &c%operator% &6banned &c%playername%&r &6for &c%reason%");
			c.put("BanSystem.Broadcast.TempBan", "&6Operator &c%operator% &6temp-banned &c%playername%&r &6for &c%reason%&r &6on &c%time%");
			c.put("BanSystem.Broadcast.TempBanIP", "&6Operator &c%operator% &6temp-ip banned &c%playername%&r &6for &c%reason%&r &6on &c%time%");
			c.put("BanSystem.Broadcast.BanIP", "&6Operator &c%operator% &6ip banned &c%playername%&r &6for &c%reason%");
			c.put("BanSystem.Broadcast.Mute", "&6Operator &c%operator% &6muted &c%playername%&r &6for &c%reason%");
			c.put("BanSystem.Broadcast.TempMute", "&6Operator &c%operator% &6temp-muted &c%playername%&r &6for &c%reason%&r &6on &c%time%");
			c.put("BanSystem.Broadcast.Warn", "&6Operator &c%operator% &6warned &c%playername%&r &6for &c%reason%");
			c.put("BanSystem.Broadcast.Kick", "&6Operator &c%operator% &6kicked &c%playername%&r &6for &c%reason%");
			c.put("BanSystem.Broadcast.Jail", "&6Operator &c%operator% &6arrested &c%playername%&r &6for &c%reason%");
			c.put("BanSystem.Broadcast.UnBan", "&6Operator &c%operator% &6unbanned &c%playername%");
			c.put("BanSystem.Broadcast.UnBanIP", "&6Operator &c%operator% &6unbanned ip of &c%playername%");
			c.put("BanSystem.Broadcast.UnMute", "&6Operator &c%operator% &6unmuted &c%playername%");
			c.put("BanSystem.Broadcast.UnJail", "&6Operator &c%operator% &6unjailed &c%playername%");
			c.put("BanSystem.Broadcast.UnWarn", "&6Operator &c%operator% &6unwarned &c%playername%");
			c.put("BanSystem.UnMute", "&6Player %playername% &6has been unmuted");
			c.put("BanSystem.UnMuted", "&6You're no longer muted");
			c.put("BanSystem.Warn", "&6Player %playername% &6warned for &c%reason%");
			c.put("BanSystem.Warned", "&6You was warned for &c%reason% &6by operator &c%warnedby%");
			c.put("BanSystem.WarnLater", "&6You was warned for &c%reason% &6by operator &c%warnedby% &6at &c%time%");
			c.put("BanSystem.Muted", "&cYou're muted for &c%reason%");
			c.put("BanSystem.Kick", "&6Player %playername% &6has been kicked for %reason%");
			c.put("BanSystem.NotWarned", "&6Player %playername% &6wasn't been any time warned");
			c.put("BanSystem.unWarned", "&6Player %playername% &6was unwarned");
			c.put("BanSystem.CantKickYourself", "&6Hey %playername%&6, you can't kick yourself..");
			c.put("BanSystem.CantBanYourself", "&6Hey %playername%&6, you can't ban yourself..");
			c.put("BanSystem.CantMuteYourself", "&6Hey %playername%&6, you can't mute yourself..");
			c.put("BanSystem.CantJailYourself", "&6Hey %playername%&6, you can't jail yourself..");
			c.put("BanSystem.MissingJail", "&6Missing jail, first set jail using command /setJail <name>");
			c.put("BanSystem.TempMute", "&6Player %playername% &6has been muted for &c%reason% &6on &c%time%");
			c.put("BanSystem.TempMuted", "&cYou're muted for &c%reason% &6on &c%time%");
			c.put("BanSystem.Mute", "&6Player %playername% &6has been muted for &c%reason%");
			c.put("BanSystem.Ban", "&6Player %playername% &6has been banned for &c%reason%");
			c.put("BanSystem.Jail", "&6Player %playername% &6has been jailed for &c%reason%");
			c.put("BanSystem.Arrested", "&6You're arrested for &c%reason%");
			c.put("BanSystem.UnBan", "&6Player %playername% &6has been unbanned");
			c.put("BanSystem.UnBanIP", "&6Player %playername% &6has been unIPbanned");
			c.put("BanSystem.TempBan", "&6Player %playername% &6has been temp-banned for &c%reason% &6on &c%time%");
			c.put("BanSystem.BanIP", "&6Player %playername% &6has been ip banned for &c%reason%");
			c.put("BanSystem.PlayerHaveNotBan", "&6Player %playername% haven't ban");
			c.put("BanSystem.PlayerNotMuted", "&6Player %player% isn't muted");
			c.put("BanSystem.unJailed", "&6Player %player% was unjailed");
			c.put("BanSystem.JailAlreadyExist", "&6Jail %jail% already exists");
			c.put("BanSystem.CreatedJail", "&6Create new jail %jail% on your location");
			c.put("BanSystem.JailNotExist", "&6Jail %jail% doesn't exists");
			c.put("BanSystem.DeletedJail", "&6Deleted jail %jail%");
			c.put("ClearInventory.Usage", "&6/Clear <player> &7- &5To clear inventory selected player");
			c.put("ClearInventory.PlayerInvCleared", "&6Inventory of player &c%player% &6has been cleared !");
		c.put("ClearInventory.NoMoney", "&cYou must have &6$%money% &cto buy undo inventory !");
		c.put("ClearInventory.NoConfirm", "&6You have no confirm request");
		c.put("ClearInventory.InvCleared", "&6Your inventory has been cleared");
		c.put("ClearInventory.ConfirmClearInv", "&cPlease confirm your request to clear your inventory !");
		c.put("ClearInventory.InstallEconomyPlugin", "&cPlease install economy plugin");
		c.put("ClearInventory.NoInventoryRetrieved", "&6There is no your inventory to retrived");
		c.put("ClearInventory.InventoryRetrievedForFree", "&aYour inventory has been retrieved for &6Free &a!");
		c.put("ClearInventory.InventoryRetrievedForMoney", "&aYour inventory has been retrieved for &6$%money% &a!");
		c.put("ClearInventory.ConfirmToggled", "&6Request for clear inventory has been %confirm%");
		c.put("Economy.Balance", "&6You currently have &a%money%$");
		c.put("Economy.BalanceOther", "&6Player &a%player% &6currently have &a%currently%$");
		c.put("Economy.Given", "&6To your account has been added &a%money%$&6, currently you have &a%currently%$");
		c.put("Economy.GivenToPlayer", "&6You gave &a%money%$ &6to player &a%playername%&6, currently &a%player% &6have &a%currently%$");
		c.put("Economy.Taken", "&6From your account has been taken &c%money%$, currently you have &a%currently%$");
		c.put("Economy.TakenFromPlayer", "&6You taken &c%money%$ &6from player &c%playername%&6, currently &c%player% &6have &c%currently%$");
		c.put("Economy.Reseted", "&6Your account has been reseted, currently you have &a%currently%$");
	 	c.put("Economy.ResetedPlayer", "&6You &creseted &6money account of player &c%playername%");
		c.put("Economy.PaidTo", "&6You paid &e%money%$ &6to player &e%playername%");
		c.put("Economy.PaidFrom", "&6%playername% &6paid you &e%money%$&6, currently you have &a%currently%$");
		c.put("Economy.NoMoney", "&cYou don't have enough money you currently have &6%money%$");
		c.put("Economy.SetPlayer", "&6Money of player &a%playername% &6has been changed to &a%money%$");
		c.put("Economy.Set", "&6Your money has been changed to &a%money%$");
		c.put("Economy.NoPlayers", "&6There is no players economies");
		c.put("Homes.Created", "&6Home &a'%home%' &6created");
		c.put("Homes.Deleted", "&6Home &c'%home%' &6deleted");
		c.put("Homes.Teleporting", "&6Teleporting to home &a'%home%'");
		c.put("Homes.TeleportingToOther", "&6Teleporting to home &a'%home%' &6of player &c%target%");
		c.put("Homes.TeleportingOtherToOther", "&6Teleporting player %player% to home &a'%home%' &6of player &c%target%");
		c.put("Homes.NotExists", "&6Home &c'%home%' &6doesn't exists");
		c.put("Homes.NotExistsOther", "&6Home &c'%home%' &6of player &c%target% &6doesn't exists");
		c.put("Homes.List", "&6Homes: &a%list%");
		c.put("Homes.ListOther", "&6Homes of Player %target%: &a%list%");
		c.put("Homes.ListEmpty", "&6There is no homes");
		c.put("Homes.LimitReached", "&cYou reached limit of maximum homes ! &o(%limit%)");
		c.put("Homes.ListOtherEmpty", "&6Player %target% have no homes");
		c.put("Chunks.Loaded", "&aLoaded &6%chunks% &achunks in world &6'%world%'");
		c.put("Chunks.TotalLoaded", "&aTotal loaded &6%chunks% &achunks in &6%worlds% &aworlds");
		c.put("Chunks.Unloaded", "&aUnloaded &6'%chunks%' &achunks");
		c.put("Gamemode.Invalid", "&6Gamemode &c%gamemode% &6is invalid !");
		c.put("Gamemode.Changed", "&6Your gamemode changed to &a%gamemode%");
		c.put("Gamemode.ChangedOther", "&6Gamemode of player &a%player% &6changed to &a%gamemode%");
		c.put("TPS", "&aCurrent server TPS: &6%tps%");
		c.put("Enchant.Enchanted", "&5Item &d%item% &5enchanted with enchant &d%enchant% &5to level &d%level%");
		c.put("Enchant.EnchantRemoved", "&5From item &d%item% &5has been removed enchant &d%enchant% &5that has level &d%level%");
		c.put("Enchant.EnchantsRemoved", "&5From item &d%item% &5has been removed all enchants, list: &d%enchants%");
		c.put("Enchant.HandIsEmpty", "&cFirst take item to your &nhand");
		c.put("Enchant.EnchantNotExist", "&cEnchant &4'%enchant%' &cdon't exists");
		c.put("Enchant.NoEnchant", "&cOn item &4%item% &cisn't enchant &4%enchant% &cto remove");
		c.put("Enchant.NoEnchants", "&cOn item &4%item% &cisn't any enchant to remove");
		c.put("TrashTitle", "&8Trash");
		c.put("TpaSystem.TpahereSender", "&6Request for teleport player %playername% &6to you sent");
		c.put("TpaSystem.TpahereTarget", "&6Player %playername% &6want to teleport you to him");
		c.put("TpaSystem.TpaSender", "&6Request for teleport player you to %playername% &6sent");
		c.put("TpaSystem.NoRequest", "&6You have any request to accept or deny");
		c.put("TpaSystem.CantSendRequestToSelf", "&6You can't send request to your self");
		c.put("TpaSystem.CantBlockSelf", "&6You can't block your self");
		c.put("TpaSystem.TpaTarget", "&6Player %playername% &6want to teleport to you");
		c.put("TpaSystem.Tpaall", "&6Request for teleport players to you sended to players: %players%");
		c.put("TpaSystem.Tpall", "&6Teleported to you players: %players%");
		c.put("TpaSystem.TpLocationPlayer", "&6Player %playername% teleported to location in world %world% on X %x% Y %y% Z %z%");
		c.put("TpaSystem.TpLocation", "&6Teleported to you players: %players%");
		c.put("TpaSystem.TpPlayerToPlayer", "&6Player %firstplayername% teleported to player %lastplayername%");
		c.put("TpaSystem.AlreadyHaveRequest", "&6Player %playername% already have request from you");
		c.put("TpaSystem.Tpaccept", "&6Request of player %playername% &6for teleport him to you accepted");
		c.put("TpaSystem.TpaAccepted", "&6Player %playername% &6accepted your request for teleport you to him");
		c.put("TpaSystem.Tpahereccept", "&6Request of player %playername% &6for teleport you to him accepted");
		c.put("TpaSystem.TpahereAccepted", "&6Player %playername% &6accepted your request for teleport him to you");
		c.put("TpaSystem.Tpadeny", "&6Request of player %playername% &6for teleport you to him denied");
		c.put("TpaSystem.TpaDenied", "&6Player %playername% &6denied your request for teleport you to him");
		c.put("TpaSystem.Tpaheredeny", "&6Request of player %playername% &6for teleport you to him denied");
		c.put("TpaSystem.TpahereDenied", "&6Player %playername% &6denied your request for teleport him to you");
		c.put("TpaSystem.Teleportedhere", "&6Player %playername% &6teleported to your location");
		c.put("TpaSystem.Teleported", "&6Teleporting you to player %playername%");
		c.put("TpaSystem.TeleportedHere", "&6Teleporting player %playername% &6to you");
		c.put("TpaSystem.TpaBlock.Blocked", "&6You are blocking teleport requests of player %playername%");
		c.put("TpaSystem.TpaBlock.UnBlocked", "&6You are no longer blocking teleport requests of player %playername%");
		c.put("TpaSystem.TpaBlock.Blocked-Global", "&6You are blocking teleport requests of all players");
		c.put("TpaSystem.TpaBlock.UnBlocked-Global", "&6You are no longer blocking teleport requests of all players");
		c.put("TpaSystem.TpBlocked", "&6Player %playername% is blocking your teleport");
		c.put("TpaSystem.TpaBlocked", "&6Player %playername% is blocking your requests");
		c.put("TpaSystem.Cancelled", "&6Player %playername% has cancelled teleport requests");
		c.put("TpaSystem.TpaCancel", "&6You cancelled teleport request");
		c.put("Sudo.SendMessage", "&6Sent message &a'%message%' &6as player &a%playername%");
		c.put("Sudo.SendCommand", "&6Sent command &a'%command%' &6as player &a%playername%");
		c.put("Repair.HandIsEmpty", "&cFirst take item to your &nhand");
		c.put("Repair.Repaired", "&aItem in your hand reapired");
		c.put("Repair.RepairedAll", "&aAll items in your inventory reapired");
		c.put("Time.Day", "&6Time in world &a'%world%' &6changed to &aday");
		c.put("Time.Night", "&6Time of world &e'%world%' &6changed to &enight");
		c.put("Time.WorldNotExists", "&6World &c'%world%' &6doesn't exists");
		c.put("Weather.Sun", "&6Weather of world &e'%world%' &6changed to &asunny");
		c.put("Weather.Rain", "&6Weather of world &e'%world%' &6changed to &erainy");
		c.put("Weather.Thunder", "&6Weather of world &e'%world%' &6changed to &cstorm");
		c.put("Weather.WorldNotExists", "&6World &c'%world%' &6doesn't exists");
		c.put("MultiWorld.AlreadyExists", "&cWorld &6'%world%' &calready exists");
		c.put("MultiWorld.WorldImported", "&6World &a'%world%' &6imported & loaded");
		c.put("MultiWorld.AlreadyLoaded", "&cWorld &6'%world%' &cis already loaded");
		c.put("MultiWorld.TeleportedWorld", "&6You has been teleported to world &c'%world%'");
		c.put("MultiWorld.SpawnSet", "&aSpawn of world &6'%world%'&a has been set");
		c.put("MultiWorld.CantBeDeleted", "&cWorld &6'%world%' &ccan't been deleted !");
		c.put("MultiWorld.Deleted", "&aWorld &6'%world%' &ahas been deleted");
		c.put("MultiWorld.Created", "&aWorld &6'%world%' &ahas been created");
		c.put("MultiWorld.NotFoundImport", "&6We not found world folder with name &c'%world%'");
		c.put("MultiWorld.Loaded", "&aWorld &6'%world%' &ahas been loaded");
		c.put("MultiWorld.Unloaded", "&aWorld &6'%world%' &ahas been unloaded and saved");
		c.put("MultiWorld.DoNotUnloaded", "&cWorld &6'%world%' &cdoesn't been unloaded");
		c.put("MultiWorld.AlreadyCreated", "&6World &c'%world%' &6is already created");
		c.put("MultiWorld.PlayerTeleportedWorld", "&aPlayer &6%player% &ahas been teleported to world &6'%world%'");
		c.put("WalkSpeed.WalkSpeed", "&6Your walk speed has been set to &a%speed%");
		c.put("WalkSpeed.WalkSpeedPlayer", "&6Walk speed of player &a%playername% &6has been set to &a%speed%");
		c.put("Fly.Enabled", "&6Fly &aEnabled");
		c.put("Fly.Disabled", "&6Fly &cDisabled");
		c.put("Fly.FlySpeed", "&6Your fly speed has been set to &a%speed%");
		c.put("Fly.FlySpeedPlayer", "&6Fly speed of player &a%playername% &6has been set to &a%speed%");
		c.put("Fly.Usage", "&e/Fly <player> &7- &5Enable or Disable fly specified player");
		c.put("Fly.SpecifiedPlayerFlyEnabled", "&6Fly of player %playername%&6 has been &aEnabled");
		c.put("Fly.SpecifiedPlayerFlyDisabled", "&6Fly of player %playername%&6 has been &cDisabled");
		c.put("TempFly.Enabled", "&6TempFly &aEnabled &6for &a%time%");
		c.put("TempFly.EnabledOther", "&6TempFly of player %playername% has been &aEnabled &6for &a%time%");
		c.put("ConsoleErrorMessage", "&4This command can be used only ingame!");
		c.put("SoundErrorMessage", "&4Invalid song, please select a song from 1 to 5 or false to disable !");
		c.put("Spawn.TeleportedToSpawn", "&aYou has been teleported to the spawn");
		c.put("Spawn.PlayerTeleportedToSpawn", "&aPlayer %playername% &ahas been teleported to the spawn");
		c.put("Spawn.SpawnSet", "&6Global spawn set");
		c.put("Warp.Created", "&aWarp &6'%warp%' &ahas been created");
		c.put("Warp.CreatedWithPerm", "&aWarp &6'%warp%' &awith permission &7%permission% &ahas been created");
		c.put("Warp.Deleted", "&6Warp &c'%warp%' &6has been deleted");
		c.put("Warp.Warping", "&6Warping to warp &c'%warp%'");
		c.put("Warp.PlayerWarped", "&6Player %playername% warped to warp &c'%warp%'");
		c.put("Warp.List", "&6Warps: &c%warps%");
		c.put("Warp.AlreadyExists", "&cWarp &6'%warp%' &calready exists");
		c.put("Warp.NotExists", "&cWarp &6'%warp%' &cdoesn't exists");
		c.put("Warp.NoWarps", "&6There is no warps");
		c.put("Warp.CantGetLocation", "&cWarp &6'%warp%' &chave wrong location");
		c.put("OnJoin.Messages", Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-","&aWelcome back %player% &a!"
				,"&aDon't forget to vote for the server ! /Vote","&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		c.put("OnJoin.Join", "%prefix%&r%player% &ajoined the game");
		c.put("OnJoin.FirstJoin.Messages", Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-","&aWelcome &6%player% &aon server &6%server_name% !"
				,"&aInvite more of your friends and create a great community of people together!"
				,"&aIP: &6%server_ip%","&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		c.put("OnJoin.FirstJoin.BroadCast", "%prefix% &aWelcome new player &r%player%&a on our server! ");
		c.put("OnLeave.Leave", "%prefix%&r%player% &9left the game");
		c.put("Inventory.OpeningEnderChest", "&5Opening EnderChest..");
		c.put("Inventory.OpeningEnderChestOther", "&5Opening EnderChest of player %playername%&5..");
		c.put("Inventory.OpeningEnderChestForTarget", "&5Opening EnderChest of player %playername%&5 for target %target%..");
		c.put("Inventory.OpeningCraftTable", "&5Opening Crafting Table..");
		c.put("Inventory.OpeningCraftTableForTarget", "&5Opening Crafting Table for player %target%..");
		c.put("Inventory.OpeningInvsee", "&5Opening inventory of player %playername%&5..");
		c.put("Inventory.OpeningInvseeForTarget", "&5Opening inventory of player %playername% &5 for target %target%&5..");
		c.put("Inventory.OpeningTrash", "&5Opening Trash..");
		c.put("Inventory.ClosePlayersInventory", "&5You closed opened inventory of player %playername%&5..");
		c.put("Words.Usage", "&4Usage");
		c.put("Words.Help", "&2Help");
		c.put("Words.HelpFor", "&2Help For:");
		c.put("Words.Version", "&aVersion");
		c.put("Words.ServerVersion", "&aServer Version");
		c.put("Words.Vulgar", "&4Vulgar Words");
		c.put("Words.Spams", "&4Spams");
		c.put("Words.List", "&2Lists:");
		c.put("Words.VulgarWordsList", "&cVulgar Words: ");
		c.put("Words.SpamWordsList", "&cSpam Words: ");
		c.put("Vanish.Toggled", "&6Your Vanish has been %toggled%");
		c.put("Vanish.ToggledPlayer", "Vanish of player %player% has been %toggled%");
		c.put("Chat.TurnOn", "&aChat Security turned on.");
		c.put("Chat.AlreadyTurnOn", "&6Chat Security is already turned on.");
		c.put("Chat.TurnOff", "&cChat Security turned off !");
		c.put("Chat.AlreadyTurnOff", "&6Chat Security is already turned off.");
		c.put("MaintenanceMode.TurnOn", "&6Maintenance mode turned &aon.");
		c.put("MaintenanceMode.TurnOff", "&6Maintenance mode turned &aoff.");
		c.put("Spawner.Set", "&6Spawner set to mob %mob%");
		c.put("Spawner.AmountSet", "&6Spawner amount limit of spawning set to %amount%");
		c.put("Spawner.RangePlayerSet", "&6Spawner required player range set to %range%");
		c.put("Spawner.TimeSet", "&6Spawner spawning time set to %time%");
		c.put("Spawner.Set", "&6Spawner set to mob %mob%");
		c.put("Spawner.InvalidMob", "&cMob with name &4'%mob%' &cis invalid");
		c.put("Spawner.BlockIsNotSpawner", "&6Block you're looking at isn't a spawner");
		c.put("Warning.Reload", Arrays.asList("&c***** &4Warning &c*****","&4Reload, please login.","&c***** &4Warning &c*****"));
		c.put("Warning.Stop", Arrays.asList("&c***** &4Warning &c*****","&4Warning: After %time%s server shutdown.","&c***** &4Warning &c*****"));
		c.put("Warning.Restart", Arrays.asList("&c***** &4Warning &c*****","&4Warning: After %time%s server restart.","&c***** &4Warning &c*****"));
		c.put("PluginName", "&aPlugin name");
		c.put("PluginCreatedBy", "&aPlugin created by");
		c.put("ServerControlThema", "&aThis plugin is used to manage server.");
		c.put("NotPermissionsMessage", "&cYou do not have permissions !");
		c.put("ClearChat.ByConsole", "&aChat Cleared by &cconsole");
		c.put("ClearChat.ByPlayer", "&aChat Cleared by %playername% &7(%player%)");
		c.put("ClearChat.PlayerNotOnline", "&cPlayer %player% is not online !");
		c.put("ClearChat.SpecifedChatCleared", "&6Chat of player &c%player% &6has been cleared");
		c.put("ClearChat.NoClearOwnChat", "&6You can't clear your own chat !");
		c.put("ClearChat.SpecifedChatHaveBypass", "&6You can't clear chat of player %player%, because they has bypass permission !");
		c.put("ConfigReloaded", "&aConfiguration reloaded !");
		c.put("Back.CantGetLocation", "&cCan't get position to teleport back");
		c.put("Back.Teleporting", "&6Teleporting back to latest position..");
		c.put("Back.PlayerTeleported", "&6Player %playername% &6has been teleported back to latest position..");
		c.put("Help.NoHelpForCommand", "&cThere is no help for command: %command%");
		c.put("Help.FlySpeed", "&5Set your or specified player fly speed");
		c.put("Help.WalkSpeed", "&5Set your or specified player walk speed");
		c.put("Help.ClearChat", "&5Deletes all players chat");
		c.put("Help.Reload", "&5Reload plugin configuration");
		c.put("Help.Help", "&5Shows this help list");
		c.put("Help.Give", "&5Give player item/block");
		c.put("Help.Butcher", "&5Kill specified entities in specified world");
		c.put("Help.WhoIs", "&5Information abouts specified player");
		c.put("Help.Kill", "&5Kill player");
		c.put("Help.Broadcast", "&5Send message for all players");
		c.put("Help.Item.SetName", "&5Set custom name on item in your hand");
		c.put("Help.Item.SetLore.Add", "&5Add lore line on item in your hand");
		c.put("Help.Item.SetLore.Remove", "&5Remove lore line from item in your hand");
		c.put("Help.Item.SetLore.Lines", "&5Show you list of lore lines on item in your hand");
		c.put("Help.Item.Unbreakable", "&5Set your item in your hand unbreakable");
		c.put("Help.Item.HideEnchants", "&5Hide all enchants on your item in your hand");
		c.put("Help.Manager", "&5Open manager of AntiSpam, AntiSwear and AntiCaps");
		c.put("Help.Seen", "&5Sends you information about the player how long is player online/offline");
		c.put("Help.Xp.Give", "&5Give player experiences");
		c.put("Help.Xp.Take", "&5Take from player experiences");
		c.put("Help.Xp.Balance", "&5Balance of player experiences");
		c.put("Help.BanSystem.TempBan", "&5Temp-Ban specified player with reason for time");
		c.put("Help.BanSystem.Ban", "&5Ban specified player with reason");
		c.put("Help.BanSystem.BanIP", "&5Ban IP of specified player with reason");
		c.put("Help.BanSystem.UnBan", "&5UnBan specified player");
		c.put("Help.BanSystem.UnBanIP", "&5UnBan-IP of specified player");
		c.put("Help.BanSystem.UnMute", "&5UnMute specified player");
		c.put("Help.BanSystem.TempMute", "&5Temp-Mute specified player with reason for time");
		c.put("Help.BanSystem.Mute", "&5Mute specified player with reason");
		c.put("Help.BanSystem.unWarn", "&5UnWarn specified player");
		c.put("Help.BanSystem.unJail", "&5UnJail specified player");
		c.put("Help.BanSystem.Jail", "&5Jail specified player with reason");
		c.put("Help.BanSystem.setJail", "&5Set new jail on your location");
		c.put("Help.BanSystem.delJail", "&5Remove specified jail");
		c.put("Help.BanSystem.Immune", "&7You can enable or disable immunity for others players");
		c.put("Help.BanSystem.Kick", "&5Kick specified player from server with reason");
		c.put("Help.BanSystem.Warn", "&5Send warning to specified player with reason");
		c.put("Help.TabList.Prefix", "&5Set prefix of group in tablist and above player");
		c.put("Help.TabList.Suffix", "&5Set suffix of group in tablist and above player");
		c.put("Help.TabList.Priorite", "&5Set priorite of group in tablist");
		c.put("Help.TabList.Reload", "&5Reload tablist config and tablist of all players");
		c.put("Help.TabList.Create", "&5Create new tablist group");
		c.put("Help.TabList.Delete", "&5Delete existing tablist group");
		c.put("Help.MultiEconomy.Transfer", "&5Transfer player economy to specified economy group");
	    c.put("Help.MultiEconomy.Money", "&5Show you money of player specified economy group");
	    c.put("Help.MultiEconomy.Create", "&5Show you money of player specified economy group");
	    c.put("Help.MultiEconomy.Delete", "&5Delete existing economy group");
	    c.put("Help.MultiEconomy.Add", "&5Add world to economy group");
	    c.put("Help.MultiEconomy.Remove", "&5Remove world from economy group");
	    c.put("Help.MultiEconomy.Groups", "&5Show you list of economies groups");
	    c.put("Help.MultiEconomy.Worlds", "&5Show you list of worlds in economy group");
	    c.put("Help.TpaSystem.Tpa", "&5Send request for teleport you to player");
		c.put("Help.TpaSystem.Tp", "&5Teleport you to player or player to coordinates");
		c.put("Help.TpaSystem.Tpahere", "&5Send request for teleport player to you");
		c.put("Help.TpaSystem.Tphere", "&5Teleport player to you");
		c.put("Help.Info", "&5Shows informations about plugin");
		c.put("Help.Addons.Disable", "&5Disable enabled addon");
		c.put("Help.Addons.Enable", "&5Enable disabled addon");
		c.put("Help.General", "&5Shows general informations about chat");
		c.put("Help.Vanish", "&5Hide / Show you for all players");
		c.put("Help.Nick", "&5Change your nickname in chat");
		c.put("Help.NickReset", "&5Reset nickname of player in chat");
		c.put("Help.Sudo", "&5Sent message/command as specified player");
		c.put("Help.Me", "&5Shows informations about you");
		c.put("Help.Version", "&5Shows plugin version");
		c.put("Help.Reset", "&5Reset general informations");
		c.put("Help.Invsee", "&5Open inventory of player");
		c.put("Help.Kit-Give", "&5Give kit to specified player");
		c.put("Help.Weather", "&5Set specified wheather in specified world");
		c.put("Help.Time", "&5Set specified time in specified world");
		c.put("Help.Skull", "&5Gave you a player head");
		c.put("Help.SkullOther", "&5Gave specified player a player head");
		c.put("Help.Enchant", "&5Enchant item in your hand with specified enchant and level");
		c.put("Help.EnchantRemove", "&5Remove specified enchant from item in your hand");//export?
		c.put("Help.Gamemode", "&5Change specified player gamemode"); //export, ale ještì nìco tzapni google 
		c.put("Help.ChatOn", "&5Turn on Chat Security (Vulgar Words and Spams)");
		c.put("Help.ChatOff", "&5Turn off Chat Security (Vulgar Words and Spams)");
		c.put("Help.ChatLock", "&5Lock or Unlock chat");
		c.put("Help.Spawner-Mob", "&5Sets spawner on specified entity");
		c.put("Help.Spawner-SpawnTime", "&5Sets entity spawn delay");
		c.put("Help.Spawner-SpawnAmount", "&5Sets entity spawn amount");
		c.put("Help.Spawner-SpawnRangePlayer", "&5Sets required range of players to spawn entity");
		c.put("Help.Helpop", "&5Send message to online admins");
		c.put("Help.Maintenance", "&5Turn on / off server maintenance mode");
		c.put("Help.List", "&5Shows list with vulgar words a spawn words");
		c.put("Help.Chunks", "&5Shows chunks used and loaded worlds");
		c.put("Help.WordAdd", "&5Add selected word to selected path");
		c.put("Help.WordDel", "&5Delete selected word from selected path");
		c.put("Help.AFK-Other", "&5Enable or Disable AFK of specified player");
		c.put("Help.ClearInv.Help", "&5Shows this help list");
		c.put("Help.ClearInv.Clear", "&5Send request to clear your inventory");
		c.put("Help.ClearInv.Undo", "&5Undo latest clear inventory command (If it was confirmed)");
		c.put("Help.ClearInv.Confirm", "&5Confirm request for clear inventory");
		c.put("Help.ClearInv.Other", "&5Clear specified player inventory");
		c.put("Help.Warp.SetWarp", "&5Create new warp");
		c.put("Help.Warp.DelWarp", "&5Delete existing warp");
		c.put("Help.Warp.Warp", "&5Teleport you to specified warp");
		c.put("Help.DelHome", "&5Delete specified home");
		c.put("Help.Thor", "&5Strike player with lighting bolt");
		c.put("Help.TempFly", "&5Enable FLY to specific player for time");
		c.put("Help.Back", "&5Teleport player back to latest position");
		c.put("Help.Spawn", "&5Teleport player to spawn");
		c.put("Help.PrivateMessage", "&5Send player private message");
		c.put("Help.ReplyPrivateMessage", "&5Reply to latest message");
		c.put("Help.Economy.Give", "&5Give money to player");
		c.put("Help.Economy.Take", "&5Take money from player");
		c.put("Help.Economy.Reset", "&5Reset money of player");
		c.put("Help.Economy.Pay", "&5Send money to player");
		c.put("Help.Economy.Set", "&5Sets the player money value");
		c.put("Help.Economy.Balance", "&5Show you balance of specified player");
		c.put("Help.Inventory.CloseInventory", "&5Close opened inventory of specific player");
		c.put("Help.Homes.HomeOther", "&5Teleport you to player home");
		c.put("Heal.CooldownMessage", "&6You have to wait &c%time% seconds &6before you can heal yourselves");
		c.put("Heal.Healed", "&aYou has been healed");
		c.put("Heal.Feed", "&aYou has been feeded");
		c.put("Heal.PlayerFeed", "&aPlayer %playername% has been feeded");
		c.put("Heal.SpecifyPlayerHealed", "&aPlayer %playername% has been healed");
		c.put("Heal.SpecifyPlayer", "&e/Heal <player> &7- &5Heal specified player");
		c.put("TabList.PrefixSet", "&6Prefix of group '&a%group%&6' set to '&a%prefix%&6'");
		c.put("TabList.SuffixSet", "&6Suffix of group '&a%group%&6' set to '&a%suffix%&6'");
		c.put("TabList.PrioriteSet", "&6Priorite of group '&a%group%&6' set to '&a%priorite%&6'");
		c.put("TabList.AlreadyExist", "&6Group '&c%group%&6' already exist");
		c.put("TabList.DoNotExist", "&6Group '&c%group%&6' doesn't exist");
		c.put("TabList.GroupCreated", "&6Group '&c%group%&6' created");
		c.put("TabList.GroupDeleted", "&6Group '&c%group%&6' deleted");
		c.put("Kit.List", "&6Kits: &a%kits%");
		c.put("Kit.Used", "&6Kit &a'%kit%' &6used.");
		c.put("Kit.Cooldown", "&6You must wait &a%cooldown% &6to use kit &a'%kit%'");
		c.put("Kit.NotExists", "&6Kit &c'%kit%' &6doesn't exists");
		c.put("Kit.Got", "&6You got kit &a'%kit%'");
		c.put("Kit.Given", "&6You give kit &a'%kit%' &6to player %playername%");
		c.put("God.Usage", "&e/God <player> &7- &5Enable or Disable god mode specified player");
		c.put("God.Enabled", "&6God mode &aEnabled");
		c.put("God.Disabled", "&6God mode &cDisabled");
		c.put("God.SpecifiedPlayerGodEnabled", "&6God mode of player %playername%&6 has been &aEnabled");
		c.put("God.SpecifiedPlayerGodDisabled", "&6God mode of player %playername%&6 has been &cDisabled");
		c.put("General.Reset", "&2Chat General was reseted");
		c.put("General.Confirm", "&cConfirm reset all general informations using /ServerControl Reset Confirm. &o(You have 10 seconds to do this)");
		c.put("General.AnyConfirm", "&cYou have no request to confirm");
		c.put("General.PleaseConfirm", "&cPlease confirm your request");
		c.put("AboutYou", Arrays.asList("&a-=-=-=-=-=-< &6%playername% &a>-=-=-=-=-=-","&aNickname: &6%player%","&aJoins: &6%joins%"
				,"&aLeaves: &6%leaves%","&aFirst Join: &6%firstjoin%","&aVulgarWords: &6%vulgarwords%"
				,"&aSpams: &6%spams%","&aKicks: &6%kicks%","&aDeaths: &6%deaths%","&aMoney: &6%vault-money%$"
				,"&aGroup: &6%vault-group%","&a-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		c.put("Thor", "Player %playername% striked");
		c.put("Cooldown.ToSendMessage", "&6You have to wait &c%timer% seconds &6before you can send a message");
		c.put("Cooldown.ToSendCommand", "&6You have to wait &c%timer% seconds &6before you can send a command");
		c.put("Security.TryingSendVulgarWord", Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-","&4%player% &ctried to write a &4vulgar word",
				"&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		c.put("Security.TryingSendSpam", Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-","&4%player% &ctried to send a &4spam","&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		c.put("Security.TryingSendAdvertisement", Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-","&4%player% &ctried to send a &4advertisement",
				"&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		c.put("Security.TryingSendCaps", "&ePlayer &6%player% &etried send a Caps &8( &6%message% &8)&e.");
		c.put("Security.TryingSendBlockedCommand", "&ePlayer &6%playername% &etried send blocked command &8( &6%command% &8)&e.");
		c.put("ChatLock.ChatIsLocked", "&4Chat is Locked");
		c.put("ChatLock.ChatIsUnlocked", "&2Chat is Unlocked!");
		c.put("ChatLock.ChatIsLockedErrorPlayerMessage", "&4We are sorry but it is currently locked chat, try it later.");
		c.put("ChatLock.BroadCastMessageChatLock", "&ePlayer &6%player% &ewrote: &6%message%&e, but chat is locked.");
		c.put("Caps", "&cPlease turn off caps lock, because you sending too many large block letters");
		c.put("UknownCommand", "&4Uknown command");
		c.put("BroadCastMessageVulgarWord", "&ePlayer &6%player% &ewrote a vulgar word &8( &6%message% &7<--- &6%word% &8)&e.");
		c.put("BroadCastMessageAdvertisement", "&ePlayer &6%player% &ewrote an advertisement &8( &6%message% &8)&e.");
		c.put("BroadCastMessageAdvertisementPickupItem", "&ePlayer &6%player% &etried pickup item with an advertisement &8( &6%message% &8)&e.");
		c.put("BroadCastMessageAdvertisementDropItem", "&ePlayer &6%player% &etried drop item with an advertisement &8( &6%message% &8)&e.");
		c.put("BroadCastMessageSpam", "&ePlayer &6%player% &ehas attempted spam &8(&6 %message% &7<--- &6%word%&8)&e.");
		c.put("VulgarWordsList-PlayerMessage", "&cHey, &6%player% &cword &6'%word%! &cis not allowed, please do not repeat it");
		c.put("AddWord.Added", "&2Word %word% successfully added!");
		c.put("AddWord.UsageCommand", "&4Usage /WordAdd <path> <word> to add a word to the selected path.");
		c.put("AddWord.WrongPath", "&4Sorry, we did not find a path to: %path%");
		c.put("AddWord.Path", "&cPaths are: %paths%");
		c.put("AddWord.AlreadyInConfig", "&cThis word %word% is already in config, usage /ServerControl List for list words in config.");
		c.put("DelWord.WordDeleted", "&2Word %word% successfully deleted!");
		c.put("DelWord.WordIsNotInConfig", "&cWord %word% is not in config, usage /ServerControl List for list words in config.");
		c.put("DelWord.UsageCommand", "&4Usage /WordDel <path> <word> to delete a word to the selected path.");
		c.put("DelWord.WrongPath", "&4Sorry, we did not find a path to: %path%");
		c.put("DelWord.Path", "&cPaths are: %paths%");
		
		c.put("Immune.Enabled", "&6You &aenabled &6your immunity");
		c.put("Immune.Disabled", "&6You &cdisabled &6your immunity");
		c.put("Immune.OnOther", "&6You &aenabled &6immunity of player &a%target%");
		c.put("Immune.OffOther", "&6You &cdisabled &6immunity of player &c%target%");
		c.put("Immune.NoPunish", "&6You can't &c%punishment% %target%&6, his immunity is enabled.");
		trans = TheAPI.getConfig("ServerControlReloaded", "Translations");
		trans.setHeader("******************************\n*** Created by Straiker123 ***\n******************************");
		trans.addDefaults(c);
		trans.create();
		Loader.TranslationsFile=trans.getConfig();
		}
	public static void configLoading() {
		config = TheAPI.getConfig("ServerControlReloaded", "Config");
		Map<String, Object> c = new HashMap<String, Object>();
		c.put("Options.Maintenance.Enabled", false);
		c.put("Options.Maintenance.KickMessages", Arrays.asList(
				"&8=-=-=-=-=-= &eMaintenance Mode &8=-=-=-=-=-=",
				"&cWe are sorry %playername%, the server is currently under maintenance mode",
				"&cMore informations can found on our web: &cwww.example.com",
				"&8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
		c.put("Options.Codes.Use", true);
		c.put("Options.Codes.Message", "&eCongratulation, you found secret code: &c%code%");
		c.put("Options.Codes.List", Arrays.asList("ServerControlReloaded","TheAPI","Straiker123","Houska02","2020","Secret","Codes"));
		c.put("Options.Codes.Random-Command", Arrays.asList(
				"eco give %player% 50",
				"eco give %player% 150",
				"give %player% diamond 2",
				"give %player% iron_block 1",
				"xp give %player% 150"));
		c.put("Options.Codes.Commands", Arrays.asList(
				"eco give %player% 25"));
		if(!config.existPath("Options.Disable-Items")) {
		c.put("Options.Disable-Items.Worlds.Creative", Arrays.asList("Bedrock","TNT", "TNT_Minecart"));
		c.put("Options.Disable-Items.Worlds.world", Arrays.asList("Bedrock"));
		c.put("Options.Disable-Items.Worlds.world_nether", Arrays.asList("Bedrock"));
		c.put("Options.Disable-Items.Worlds.world_the_end", Arrays.asList("Bedrock"));
		}
		c.put("Options.ChatLock", false);
		c.put("Options.Disable-Items.Use", true);
		c.put("Options.TimeZone.Use", false);
		c.put("Options.TimeZone.Zone", "Europe/Prague");
		c.put("Options.Colors.Chat.Use", true);
		c.put("Options.Colors.Chat.Required-Permission", true);
		c.put("Options.Colors.Chat.Permission", "ServerControl.ChatColor");
		c.put("Options.Colors.Sign.Use", true);
		c.put("Options.Colors.Sign.Required-Permission", true);
		c.put("Options.Colors.Sign.Permission", "ServerControl.SignColor");
		c.put("Options.Sounds.Use", true);
		c.put("Options.Sounds.Sound", "ENTITY_PLAYER_LEVELUP");
		c.put("Options.Teleport.SafeLocation", true);
		c.put("Options.Teleport.RequestTime", "1min");
		c.put("Options.Teleport.CommandSendLocation", true);
		c.put("Options.RespawnTeleport", "home"); //spawn, home, bed
		c.put("Options.AFK.AutoAFK", true);
		c.put("Options.AFK.TimeToAFK", "5min");
		c.put("Options.AFK.AutoKick", true);
		c.put("Options.AFK.TimeToKick", "10min");
		c.put("Options.AFK.KickMessage", "&cYou were kicked for AFK longer than 10 minutes.");
		c.put("Options.Economy.Money", 100);
		c.put("Options.Economy.DisablePluginEconomy", false);
		c.put("Options.Economy.CanUseOtherEconomy", true);
		c.put("Options.Economy.BalanceTop", "&6%position%. &c%playername% &a&o(%money%&a&o)");
		c.put("Options.Economy.Log", false);
		c.put("Options.Economy.MultiEconomy.Use", false);
		if(!config.existPath("Options.Economy.MultiEconomy.Types")) {
			c.put("Options.Economy.MultiEconomy.Types.default",Arrays.asList("world","world_nether","world_the_end"));
			c.put("Options.Economy.MultiEconomy.Types.SkyBlock",Arrays.asList("SkyBlock_Normal", "SkyBlock_Nether", "SkyBlock_The_End", "SkyBlock_Shop"));
		}
		c.put("Options.WarningSystem.Reload.Use", true);
		c.put("Options.WarningSystem.Reload.PauseTime", 5);
		c.put("Options.WarningSystem.Reload.Messages", 
				Arrays.asList(
						"&8****** &4Warning &8******"
						,"&cReload of server in %time%"
						,"&cPlease login using /login <password>"
						,"&8****** &4Warning &8******"));
		c.put("Options.WarningSystem.Restart.Use", true);
		c.put("Options.WarningSystem.Restart.PauseTime", 15);
		c.put("Options.WarningSystem.Restart.Messages", 
				Arrays.asList(
						"&8****** &4Warning &8******"
						,"&cRestart of server in %time%"
						,"&8****** &4Warning &8******"));
		c.put("Options.WarningSystem.Stop.Use", true);
		c.put("Options.WarningSystem.Stop.PauseTime", 15);
		c.put("Options.WarningSystem.Stop.Messages", 
				Arrays.asList(
						"&8****** &4Warning &8******"
						,"&cStopping of server in %time%"
						,"&8****** &4Warning &8******"));
		c.put("Options.AutoMessage.Use", true);
		c.put("Options.AutoMessage.MinimalPlayers", 1);
		c.put("Options.AutoMessage.Interval", "5min");
		c.put("Options.AutoMessage.Random", false);
		c.put("Options.AutoMessage.Messages", Arrays.asList("&0[&a&lINFO&0] &cServerControlReloaded created by &nDevTec","&0[&a&lINFO&0] &c20% Bugs free"
				,"&0[&a&lINFO&0] &cOnline players &a%online% &7/ &a%max_players%","&0[&a&lINFO&0] &cYou found a bug ? Report it to https://github.com/TheDevTec/ServerControlReloaded"
				,"&0[&a&lINFO&0] &cDo you like our plugin? Write a comment on the https://www.spigotmc.org/resources/server-control-reloaded.71147"));
		c.put("Options.Cost-ClearInvUndo", 50);
		c.put("Options.RAM-Percentage", true);
		c.put("Options.ServerList.MOTD.Use", true);
		c.put("Options.ServerList.MOTD.Maintenance", false);
		c.put("Options.ServerList.MOTD.Text.Normal", "&8-------< &eMinecraft Server &8>-------%line%&aServer is online");
		c.put("Options.ServerList.MOTD.Text.Maintenance", "&8-------< &eMinecraft Server &8>-------%line%&cServer is under Maintenance Mode");
		c.put("Options.VIPSlots.Use", true);
		c.put("Options.VIPSlots.AddSlots", false);
		c.put("Options.VIPSlots.SlotsToAdd", 4);
		c.put("Options.VIPSlots.KickWhenFullServer", true);
		c.put("Options.VIPSlots.VIPJoinBroadcast", false);
		c.put("Options.VIPSlots.Text.Kick", "&eBuy &nVIP&r&e on our site and get access to this &nreserved slot");
		c.put("Options.VIPSlots.Text.BroadcastVIPJoin", "&eVIP player joined to the server");
		c.put("Options.VIPSlots.Text.FullServer", "&cAll &nVIP Slots&r &care in use");
		c.put("Options.Join.TeleportToSpawn", false);
		c.put("Options.Join.CustomJoinMessage", true);
		c.put("Options.Join.MOTD", true);
		c.put("Options.Join.FirstJoin.Use", true);
		c.put("Options.Join.FirstJoin.GiveKit", true);
		c.put("Options.Join.FirstJoin.Kit", "Default");
		c.put("Options.Join.FirstJoin.PerformCommands.Use", false);
		c.put("Options.Join.FirstJoin.PerformCommands.List", Arrays.asList("give %player% stone_sword 1","eco give %player% 10"));
		c.put("Options.Leave.CustomLeaveMessage", true);
		c.put("Options.Security.AntiAD.WhiteList", Arrays.asList("minecraft.net","bukkit.org","spigot.org"));
		c.put("Options.Security.AntiAD.Book", true);
		c.put("Options.Security.AntiAD.Sign", true);
		c.put("Options.Security.AntiAD.Chat", true);
		c.put("Options.Security.AntiAD.Commands", true);
		c.put("Options.Security.AntiAD.Anvil", true);
		c.put("Options.Security.AntiAD.ItemDrop", true);
		c.put("Options.Security.AntiAD.ItemPickup", true);
		c.put("Options.Security.AntiSwear.Chat", true);
		c.put("Options.Security.AntiSwear.Commands", false);
		c.put("Options.Security.AntiSpam.Chat", true);
		c.put("Options.Security.AntiSpam.Commands", false);
		c.put("Options.Security.AntiCaps.Chat", true);
		c.put("Options.Security.AntiCaps.Commands", false);
		c.put("Options.Cooldowns.Chat.Use", true);
		c.put("Options.Cooldowns.Chat.Time", 2);
		c.put("Options.Cooldowns.Commands.Use", true);
		c.put("Options.Cooldowns.Commands.Time", 1);
		c.put("Options.Cooldowns.Commands.PerCommand.Use", true); //<command>:<cooldown>
		c.put("Options.Cooldowns.Commands.PerCommand.List", Arrays.asList("Heal:300","Tpa:30","TpaHere:30","TpaAll:30","Suicide:60","Feed:150"));
		c.put("Options.CommandsBlocker.Use", true);
		c.put("Options.CommandsBlocker.List", Arrays.asList("pl","plugins","version","ihasbukkit","spigot","reload","stop","restart"));
		c.put("StaffList", Arrays.asList("owner","admin","operator","helper","builder","developer"));
		c.put("BanSystem.TempMute.Reason", "Unknown");
		c.put("BanSystem.TempMute.Time", "1h");
		c.put("BanSystem.Kick", "Unknown");
		c.put("BanSystem.Ban", "Unknown");
		c.put("BanSystem.TempBanIP.Reason", "Unknown");
		c.put("BanSystem.TempBanIP.Time", "7d");
		c.put("BanSystem.TempBan.Reason", "Unknown");
		c.put("BanSystem.TempBan.Time", "1h");
		c.put("BanSystem.BanIP", "Unknown");
		c.put("BanSystem.Mute.Reason", "Unknown");
		c.put("BanSystem.Jail.Reason", "Unknown");
		c.put("BanSystem.Warn.Reason", "Unknown");
			if(!config.existPath("BanSystem.Warn.Operations")) {
			c.put("BanSystem.Warn.Operations.1.Commands", Arrays.asList("eco take %player% 20"));
			c.put("BanSystem.Warn.Operations.1.Messages",  Arrays.asList("&0[&4Warning&0] &cYou have last 20 warnings"));
			c.put("BanSystem.Warn.Operations.5.Commands", Arrays.asList("eco take %player% 50", "kick %player% &0[&4Warning&0] &cYou have 5 warnings"));
			c.put("BanSystem.Warn.Operations.20.Commands", Arrays.asList("eco take %player% 100"));
			c.put("BanSystem.Warn.Operations.20.Messages", Arrays.asList("&0[&4Warning&0] &cThis is last warning"));
			c.put("BanSystem.Warn.Operations.21.Commands", Arrays.asList("tempban %player% 2d &0[&4Info&0] &cYour life is lie!"));
		}
		c.put("BanSystem.Mute.Reason", "Unknown");
		c.put("BanSystem.Mute.DisableChat", true);
		c.put("BanSystem.Mute.DisableCmds", true);
		c.put("BanSystem.Mute.DisabledCmds", Arrays.asList("m","r","tell","msg","message","pm","w","say"));
		c.put("Chat-Groups-Enabled", true);
		if(!config.existPath("Chat-Groups.DefaultFormat")) {
		c.put("Chat-Groups.DefaultFormat.Chat", "%player% &8> &f%message%");
		c.put("Chat-Groups.DefaultFormat.Name", "&7%vault-group% &f%player%");
		c.put("Chat-Groups.default.Chat", "%player% &8> &f%message%");
		c.put("Chat-Groups.default.Name", "&7%player%");
		c.put("Chat-Groups.vip.Chat", "%player% &8> &a%message%");
		c.put("Chat-Groups.vip.Name", "&aVIP &e%player%");
		c.put("Chat-Groups.admin.Chat", "%player% &8> &3%message%");
		c.put("Chat-Groups.admin.Name", "&4Admin &3%player%");
		c.put("Chat-Groups.owner.Chat", "%player% &8> &3%message%");
		c.put("Chat-Groups.owner.Name", "&3&lOwner &f%player%");
		}
		if(!config.existPath("Homes.default")) {
			c.put("Homes.default", 3);
			c.put("Homes.vip", 3);
			c.put("Homes.helper", 4);
			c.put("Homes.operator", 5);
			c.put("Homes.admin", 5);
			c.put("Homes.builder", 7);
			c.put("Homes.owner", 10);
		}
		c.put("List.LoadedFormat", "&6World: &e%world%&6, Chunks: &e%chunks%&6, Mobs: &e%mobs%&6, Players: &e%players%");
		c.put("List.UnloadedFormat", "&6World: &e%world% &7(Unloaded)");
		c.put("HelpFormat", "%prefix%&e%command%& &7- &5%help%");
		c.put("Format.Time", "HH:mm:ss");
		c.put("Format.Date", "dd.MM.yyyy");
		c.put("Format.DateWithTime", "dd.MM.yyyy HH:mm:ss");
		c.put("Format.Broadcast", "&0[&4Broadcast&0] &a%message%");
		c.put("Format.PrivateMessageTo", "&8(&eYou -> %to%&8): &f%message%");
		c.put("Format.PrivateMessageFrom", "&8(&e%from% -> You&8): &f%message%");
		c.put("Format.Kick", "&6You was kicked for &c%reason%");
		c.put("Format.Ban", "&6You was banned for &c%reason%");
		c.put("Format.TempBan", "&6You was temp-banned for &c%reason% &6on &c%time%");
		c.put("Format.BanIP", "&6You was ip banned for &c%reason%");
		c.put("Format.Helpop", "&0[&4HelpOp&0] &6%playername%&f: &6%message%");
		c.put("TasksOnSend.BlockedCommand-Broadcast", false);
		c.put("TasksOnSend.Spam.Use-Commands", true);
		c.put("TasksOnSend.Spam.Broadcast", false);
		c.put("TasksOnSend.Spam.Commands", Arrays.asList("msg %player% &0[&4AntiSpam&0] &cPlease, stop spamming"));
		c.put("TasksOnSend.Swear.Use-Commands", true);
		c.put("TasksOnSend.Swear.Broadcast", false);
		c.put("TasksOnSend.Swear.Commands", Arrays.asList("msg %player% &0[&4AntiSwear&0] &cPlease, stop being vulgar"));
		c.put("TasksOnSend.Advertisement.Broadcast", false);
		c.put("TasksOnSend.Advertisement.Use-Commands", true);
		c.put("TasksOnSend.Advertisement.Commands", Arrays.asList("msg %player% &0[&4AntiAD&0] &cPlease, stop sending advertisements"
				,"eco take %player% 150"));
		c.put("WritingFormat.Spam", "%player%: %message% - Spam (%spam%)");
		c.put("WritingFormat.Swear", "%player%: %message% - Swear (%vulgarword%)");
		c.put("WritingFormat.Advertisement", "%player%: %message% - Advertisement");
		c.put("WritingFormat.Blocked-Command", "%player%: %message%");
		c.put("AutoKickLimit.Swear.Use", true);
		c.put("AutoKickLimit.Swear.Number", 3);
		c.put("AutoKickLimit.Swear.Message.Use", true);
		c.put("AutoKickLimit.Swear.Message.List", Arrays.asList("&6=-=-=-=-=-=-=-=-=-=-=-=-=-=",
				"&cYou have reached the maximum number of Vulgar Words! &o(%number%)","&6=-=-=-=-=-=-=-=-=-=-=-=-=-="));
		c.put("AutoKickLimit.Swear.Commands.Use", true);
		c.put("AutoKickLimit.Swear.Commands.List", Arrays.asList("kick %player% &cYou have reached maximum warnings for &nswearing&r&c &o(%number%)","eco take %player% 350"));
		c.put("AutoKickLimit.Spam.Use", true);
		c.put("AutoKickLimit.Spam.Number", 5);
		c.put("AutoKickLimit.Spam.Message.Use", true);
		c.put("AutoKickLimit.Spam.Message.List", Arrays.asList("&6=-=-=-=-=-=-=-=-=-=-=-=-=-=",
				"&cYou have reached the maximum number of Spams! &o(%number%)","&6=-=-=-=-=-=-=-=-=-=-=-=-=-="));
		c.put("AutoKickLimit.Spam.Commands.Use", true);
		c.put("AutoKickLimit.Spam.Commands.List", Arrays.asList("kick %player% &cYou have reached maximum warnings for &nspaming&r&c &o(%number%)"));
		c.put("AutoKickLimit.Kick.Use", true);
		c.put("AutoKickLimit.Kick.Number", 5);
		c.put("AutoKickLimit.Kick.Message.Use", true);
		c.put("AutoKickLimit.Kick.Message.List", Arrays.asList("&6=-=-=-=-=-=-=-=-=-=-=-=-=-=",
				"&4&lYou have reached the maximum number of Kicks! &o(%number%)","&6=-=-=-=-=-=-=-=-=-=-=-=-=-="));
		c.put("AutoKickLimit.Kick.Commands.Use", true);
		c.put("AutoKickLimit.Kick.Commands.List", Arrays.asList("tempban %player% 7d &4&lYou have reached maximum number of &nkicks&r&4&l &o(%number%)","eco take %player% 5000"));
		c.put("SwearWords", Arrays.asList("fuck","idiot","kurv","kurw"));
		c.put("SpamWords.SimiliarMessage", true);
		c.put("SpamWords.DoubledLetters.MaxAmount", 4);
		c.put("SpamWords.DoubledLetters.ReduceLetters", true);
		c.put("SpamWords.DoubledLetters.Ignore", Arrays.asList("?","!"));
		c.put("SpamWords.DoubledLetters.Use", true);
		c.put("SpamWords.Words",Arrays.asList("pls op","please op", "op pls", "op please"));
		config.setHeader("+-------------------------------------------------------------------+ #\r\n" + 
				"| Info: https://dev.bukkit.org/projects/server-control-reloaded     | #\r\n" + 
				"+-------------------------------------------------------------------+ #\r\n" + 
				"Options for RespawnTeleport are: Home, Bed, Spawn"+
				"PlaceHolders for AutoMessage:\n" +
				"  %used_ram% - used memory\n"+
				"  %free_ram% -  free memory\n"+
				"  %max_ram% - maximum memory\n"+
				"  %online% - online players\n"+
				"  %max_players% - maximum players on server\n"+
				"  %time% - server time\n"+
				"  %date% - server date\n"+
				"+-------------------------------------------+ #\r\n" + 
				"| INFO: TimeZones! List of time zones:      | #\r\n" + 
				"+- https://greenwichmeantime.com/time-zone -+ #");
		config.addDefaults(c);
		config.create();
		Loader.config=config.getConfig();
	}
	public static void BansLoading() {
			bans= TheAPI.getConfig("ServerControlReloaded", "Bans");
			bans.addDefault("Ban","");
			bans.addDefault("Mute","");
			bans.create();
			Loader.ban=bans.getConfig();
			}
	public static void ScoreboardLoading() {
			Map<String, Object> c = new HashMap<String, Object>();
			c.put("Scoreboard-Enabled", true);
			c.put("Scoreboard-PerWorld", false);
			c.put("RefleshTick", 20);
			c.put("Name", "&bStatus");
			c.put("Lines", Arrays.asList("&r&lMoney: &a%money%$","&r&lOnline:  &a%online%"));
			

			c.put("PerWorld.pvp_world.Name", "&9PvP");
			c.put("PerWorld.pvp_world.Lines", Arrays.asList("&r&lKills: &a%kills%$","&r&lHealth:  &a%health%"));
			
			c.put("PerWorld.skyblock.Name", "&eSkyBlock");
			c.put("PerWorld.skyblock.Lines", Arrays.asList("&r&lMoney: &a%money%$","&r&lHealth:  &a%health%","&r&lFood:  &a%food%"));
			
			sb= TheAPI.getConfig("ServerControlReloaded", "Scoreboard");
			sb.setHeader("%money%   player money balance\n"
					+"%online%   online players on server\n"
					+"%max_players%   maximum players on server\n"
					+"%time%   server time\n"
					+"%date%   server date\n"
					+"%world%   world name of player\n"
					+"%health%, %hp%   player healths\n"
					+"%food%   player food level\n"
					+"%group%   player vault group\n"
					+"%ping%   player server ping\n"
					+"%tps%   current server tps\n"
					+"%ram_free%   free memory of server\n"
					+"%ram_usage%   used memory of server\n"
					+"%ram_max%   maximum memory of server\n"
					+"%ram_free_percentage%   free memory of server in percentage\n"
					+"%ram_usage_percentage%   used memory of server in percentage\n"
					+"%kills%   killed players\n"
					+"%afk%   afk placeholder");
			sb.addDefaults(c);
			sb.create();
			Loader.scFile=sb.getConfig();
			}
	public static void ChatLogLoading() {
				chat= TheAPI.getConfig("ServerControlReloaded", "ChatLog");
				chat.create();
			}
	public static void ChatMeLoading() {
				chatme= TheAPI.getConfig("ServerControlReloaded", "ChatMe");
				chatme.create();
				Loader.me=chatme.getConfig();
				}
	public static void TabLoading() {
			tab= TheAPI.getConfig("ServerControlReloaded", "TabList");
			Map<String, Object> c = new HashMap<String, Object>();
			c.put("Tab-Enabled", true);
			c.put("Header-Enabled", true);
			c.put("Footer-Enabled", true);
			c.put("Colored-Ping", true);
			c.put("ModifyNameTags", true);
			c.put("SortTabList", true);
			c.put("Header", Arrays.asList("&bWelcome back &a%playername%&b!","&bTPS: &a%tps%   &bFree Ram: &a%ram_free_percentage%"
					,"&bMoney: &9%money%$   &bPing: &9%ping%","&7--------------------------------"));
			c.put("Footer", Arrays.asList("&7--------------------------------","&bTime: &a%time%   &bOnline: &a%online% &0/ &a%max_players%"));
			c.put("RefleshTick", 20);
			c.put("NameTag-RefleshTick", 3);
			c.put("AFK.IsAFK", " &4&l*AFK*");
			c.put("AFK.IsNotAFK", "");
			if(!tab.existPath("Groups.default")) {
				c.put("Groups.default.Prefix", "&7Default ");
				c.put("Groups.default.Suffix", "%afk%");
				c.put("Groups.default.Priorite", "z");
				c.put("PerPlayerTabList.timtower.Footer", Arrays.asList("&7--------------------------------","&6Money: &a%money%$   &6Rank: &a%group%"));
				c.put("PerPlayerTabList.timtower.Header", Arrays.asList("&4Welcome back %playername% !","&7--------------------------------"));
				c.put("PerWorldTabList.world1.Footer", Arrays.asList("&7--------------------------------","&6Online: &a%online%$   &6Rank: &a%group%"));
				c.put("PerWorldTabList.world1.Header", Arrays.asList("&2TabList in world %world%","&6Health: &a%hp%$   &6Food: &a%food%"
					,"&7--------------------------------"));
			}
			tab.addDefaults(c);
			tab.setHeader("%money%   player money balance\n"
					+"%online%   online players on server\n"
					+"%max_players%   maximum players on server\n"
					+"%time%   server time\n"
					+"%date%   server date\n"
					+"%world%   world name of player\n"
					+"%health%, %hp%   player healths\n"
					+"%food%   player food level\n"
					+"%group%   player vault group\n"
					+"%ping%   player server ping\n"
					+"%tps%   current server tps\n"
					+"%ram_free%   free memory of server\n"
					+"%ram_usage%   used memory of server\n"
					+"%ram_max%   maximum memory of server\n"
					+"%ram_free_percentage%   free memory of server in percentage\n"
					+"%ram_usage_percentage%   used memory of server in percentage\n"
					+"%kills%   killed players\n"
					+"%afk%   afk placeholder");
			tab.create();
			Loader.tab=tab.getConfig();
			}
	public static void MultiWorldLoading() {
		Map<String, Object> c = new HashMap<String, Object>();
		c.put("ModifyMobsSpawnRates", false);
		c.put("SavingTask.Enabled", true);
		c.put("SavingTask.Delay", 3600);
		mw=TheAPI.getConfig("ServerControlReloaded", "MultiWorlds");
		mw.addDefaults(c);
		mw.create();
		Loader.mw=mw.getConfig();
		}
	public static void KitLoading() {
	kit=TheAPI.getConfig("ServerControlReloaded", "Kits");
	Map<String, Object> c = new HashMap<String, Object>();
	if(!kit.existPath("Kits")) {
	c.put("Kits.Default.Items.Stone.Amount", 16);
	c.put("Kits.Default.Items.Stone_Pickaxe.Amount", 1);
	c.put("Kits.Default.Items.Stone_Pickaxe.CustomName", "&8Normal pickaxe");
	c.put("Kits.Default.Price", 25);
	c.put("Kits.Default.Cooldown", 3600);
	c.put("Kits.VIP.Items.Diamond.Amount", 3);
	c.put("Kits.VIP.Items.Diamond.CustomName", "&bShiny Diamond");
	c.put("Kits.VIP.Items.Iron_Pickaxe.Amount", 1);	
	c.put("Kits.VIP.Items.Iron_Pickaxe.CustomName", "&eSuper pickaxe");
	c.put("Kits.VIP.Items.Iron_Pickaxe.Lore", Arrays.asList("&4The best of best"));
	c.put("Kits.VIP.Items.Iron_Pickaxe.Enchantments", Arrays.asList("SHARPNESS:4","UNBREAKING:2"));
	c.put("Kits.VIP.Price", 60);
	c.put("Kits.VIP.Cooldown", 3600);
	}
	kit.addDefaults(c);
	kit.create();
	Loader.kit=kit.getConfig();
	}
	public static void LoadConfigs() {
		ChatMeLoading();
		MultiWorldLoading();
		configLoading();
		TranslationsLoading();
		ScoreboardLoading();
		TabLoading();
		ChatLogLoading();
		KitLoading();
		BansLoading();
	}
}
