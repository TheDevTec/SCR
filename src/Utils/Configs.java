package Utils;

import java.util.Arrays;

import ServerControl.Loader;

public class Configs {

	public static void TranslationsLoading() {
		Loader.trans.addDefault("Prefix", "&4SCR &e> ");
		Loader.trans.addDefault("Skull-GivenToPlayer", "&6Gave player-head %head% to %playername%");
		Loader.trans.addDefault("Skull-Given", "&6Gave player-head ''%head%'' to you");
		Loader.trans.addDefault("Seen.Online", "&6Player %playername% &6is &aonline &6for &a%online%");
		Loader.trans.addDefault("Seen.Offline", "&6Player %playername% &6is &coffline &6for &c%offline%");
		Loader.trans.addDefault("Seen.SimiliarNames", "&6Did you mean this name: %names%");
		Loader.trans.addDefault("PlayerList.Staff", Arrays.asList("&7=-=-=- %online% / %max_players% -=-=-=", "&8Online staff: &a%staff%",
				"&7=-=-=-=-=-=-=-=-="));
		Loader.trans.addDefault("PlayerList.Normal", Arrays.asList("&7=-=-=- %online% / %max_players% -=-=-=", "&8Staff: &a%staff%",
				"&8VIP: &a%VIP%", "&8Players: &a%players%"));
		Loader.trans.addDefault("Give.UknownItem", "&6Item &c'%item%' &6is invalid");
		Loader.trans.addDefault("Give.Given", "&6Gave %amount%x %item% to player %playername%");
		Loader.trans.addDefault("Kill.Killed", "&6Killed %playername%");
		Loader.trans.addDefault("Kill.Suicide", "&6%playername% has commited suicide. Farewell cruel world!");
		Loader.trans.addDefault("Kill.KilledAll", "&6Killed &a%amount% &6players &a(%players%)");
		Loader.trans.addDefault("Butcher.WorldIsInvalid", "&6World &c&%world% &6doesn't exist");
		Loader.trans.addDefault("Butcher.EntityIsInvalid", "&6Entity with name &c&%entity% &6doesn't exist");
		Loader.trans.addDefault("Butcher.Killed", "&6Killed &a%amount% &6entities");
		Loader.trans.addDefault("Butcher.KilledSpecified", "&6Killed &a%amount% &6entities type &a%entity%");
		Loader.trans.addDefault("RAM.Info.Normal", Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-", "&aFree Memory: &6%free_ram% MB",
				"&aUsed Memory: &6%used_ram% MB", "&aMax Memory: &6%max_ram% MB", "&6-=-=-=-=-=-=-=-=-=-=-=-"));
		Loader.trans.addDefault("RAM.Info.Percent",
				Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-",
						"&aFree: &6%free_ram%% &8/ &aUsed: &6%used_ram%% &8/ &aMax: &6%max_ram% MB",
						"&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		Loader.trans.addDefault("Xp.Balance", "&6%playername% &6currently have &a%amount%exps");
		Loader.trans.addDefault("Xp.Given", "&6Given &a%amount%exps &6to %playername%");
		Loader.trans.addDefault("Xp.Taken", "&6Taken &c%amount%exps &6from %playername%");
		Loader.trans.addDefault("Xp.Set", "&6%playername%'s experiences set to &c%amount%");
		Loader.trans.addDefault("RAM.Clearing", "&6Clearing memory cache, please wait..");
		Loader.trans.addDefault("RAM.Cleared", "&6Cleared &a%cleared% MB");
		Loader.trans.addDefault("RAM.AlreadyClearing", "&cMemory is clearing, please wait..");
		Loader.trans.addDefault("PlayerNotOnline", "&cPlayer &4%player% &cisn't online");
		Loader.trans.addDefault("PlayerNotExists", "&cPlayer &4%player% &cdoesn't exist");
		Loader.trans.addDefault("Item.SetName", "&6Set custom name &a'%name%&a' &6on item &a%item%");
		Loader.trans.addDefault("Item.SetLore.Add", "&6Added lore &a'%lore%&a' &6on item &a%item%");
		Loader.trans.addDefault("Item.SetLore.Remove", "&6Removed lore line &a%line% &6from item &a%item%");
		Loader.trans.addDefault("Item.SetLore.RemoveError", "&6On item &c%item% &6isn't line &c%line%");
		Loader.trans.addDefault("Item.SetLore.Lines-List", "&6List of lore lines on item &a%item%&6:");
		Loader.trans.addDefault("Item.SetLore.Lines-Format", "&6%position%. line: &5%lore%");
		Loader.trans.addDefault("Item.Unbreakable.true", "&6Unbreakable on item &a%item% &aenabled");
		Loader.trans.addDefault("Item.Unbreakable.false", "&6Unbreakable on item &a%item% &cdisabled");
		Loader.trans.addDefault("Item.HideEnchants.true", "&6Enchants on item &a%item% &6are no longer visible");
		Loader.trans.addDefault("Item.HideEnchants.false", "&6Enchants on item &a%item% &6are visible");
		Loader.trans.addDefault("Item.HandIsEmpty", "&cFirst take item to your &nhand");
		Loader.trans.addDefault("Hat.HandIsEmpty", "&cFirst take item to your &nhand");
		Loader.trans.addDefault("Hat.Equiped", "&aItem/Block from your hand equiped to your head!");
		Loader.trans.addDefault("Hat.EquipedToOther", "&aItem/Block from your hand equiped to %target%'s head!");
		Loader.trans.addDefault("MultiEconomy.AlreadyCreated", "&6Economy group %economy-group% is already exists");
		Loader.trans.addDefault("MultiEconomy.Created", "&6Economy group %economy-group% created");
		Loader.trans.addDefault("MultiEconomy.NotExist", "&6Economy group %economy-group% doesn't exist");
		Loader.trans.addDefault("MultiEconomy.Groups", "&6Economy groups: &a%groups%");
		Loader.trans.addDefault("MultiEconomy.Worlds", "&6Worlds in economy group %economy-group%: &a%worlds%");
		Loader.trans.addDefault("MultiEconomy.Deleted", "&6Economy group %economy-group% deleted");
		Loader.trans.addDefault("MultiEconomy.WorldAdded", "&aWorld %world% added to economy group %economy-group%");
		Loader.trans.addDefault("MultiEconomy.WorldAlreadyAdded", "&a6World %world% is already in economy group %economy-group%");
		Loader.trans.addDefault("MultiEconomy.WorldIsNotInGroup", "&6World %world% isn't in economy group %economy-group%");
		Loader.trans.addDefault("MultiEconomy.WorldRemoved", "&6World %world% &aremoved &6from economy group &a%economy-group%");
		Loader.trans.addDefault("MultiEconomy.NoMoney",
				"&6Player %playername% &6doesn't have any money in economy group %economy-group%");
		Loader.trans.addDefault("MultiEconomy.HaveMoney",
				"&6Player %playername% &6currently have %money%$ in economy group %economy-group%");
		Loader.trans.addDefault("MultiEconomy.Transfer",
				"&6Transferred economy of player %playername% &6to economy group %economy-group%");
		Loader.trans.addDefault("MultiEconomy.WorldNotExist", "&cWorld &4'%world%' &cdoesn't exist");
		Loader.trans.addDefault("NicknameChanged", "&6Your nickname changed to &r%nickname%");
		Loader.trans.addDefault("NicknameReseted", "&6Your nickname has been reset");
		Loader.trans.addDefault("NicknameResetedOther", "&6Nickname of player %player% has been reset");
		Loader.trans.addDefault("AFK.IsAFK", "%playername% &cis AFK");
		Loader.trans.addDefault("AFK.NoLongerAFK", "%playername% &7is no longer AFK");
		Loader.trans.addDefault("PrivateMessage.NoPlayerToReply", "&6You have nobody to reply to");
		Loader.trans.addDefault("BanSystem.UnMute", "&6Player %playername% &6has been unmuted");
		Loader.trans.addDefault("BanSystem.UnMuted", "&6You are no longer muted");
		Loader.trans.addDefault("BanSystem.Warn", "&6Player %playername% &6was warned for &c%reason%");
		Loader.trans.addDefault("BanSystem.Muted", "&cYou are muted for &c%reason%");
		Loader.trans.addDefault("BanSystem.Kick", "&6Player %playername% &6has been kicked for %reason%");
		Loader.trans.addDefault("BanSystem.CantKickYourself", "&6Hey %playername%&6, you can't kick yourself..");
		Loader.trans.addDefault("BanSystem.CantBanYourself", "&6Hey %playername%&6, you can't ban yourself..");
		Loader.trans.addDefault("BanSystem.CantMuteYourself", "&6Hey %playername%&6, you can't mute yourself..");
		Loader.trans.addDefault("BanSystem.CantJailYourself", "&6Hey %playername%&6, you can't jail yourself..");
		Loader.trans.addDefault("BanSystem.MissingJail", "&6Missing jail, first set jail using command /setJail <name>");
		Loader.trans.addDefault("BanSystem.TempMute", "&6Player %playername% &6has been muted for &c%reason% &6on &c%time%");
		Loader.trans.addDefault("BanSystem.TempBanIP", "&6Player %playername% &6has been tempbannedIP for &c%reason% &6on &c%time%");
		Loader.trans.addDefault("BanSystem.Mute", "&6Player %playername% &6has been muted for &c%reason%");
		Loader.trans.addDefault("BanSystem.Ban", "&6Player %playername% &6has been banned for &c%reason%");
		Loader.trans.addDefault("BanSystem.Jail", "&6Player %playername% &6has been arrested for &c%reason%");
		Loader.trans.addDefault("BanSystem.TempJail", "&6Player %playername% &6has been temp-arrested for &c%reason% &6on time &c%time%");
		Loader.trans.addDefault("BanSystem.UnBan", "&6Player %playername% &6has been unbanned");
		Loader.trans.addDefault("BanSystem.UnBanIP", "&6Player %playername% &6has been unIPbanned");
		Loader.trans.addDefault("BanSystem.TempBan", "&6Player %playername% &6has been temp-banned for &c%reason% &6on &c%time%");
		Loader.trans.addDefault("BanSystem.BanIP", "&6Player %playername% &6has been ip banned for &c%reason%");
		Loader.trans.addDefault("BanSystem.PlayerHasNotBan", "&6Player %playername% hasn''t been banned");
		Loader.trans.addDefault("BanSystem.PlayerNotMuted", "&6Player %player% isn't muted");
		Loader.trans.addDefault("BanSystem.PlayerNotWarned", "&6Player %player% isn't warned");
		Loader.trans.addDefault("BanSystem.unJailed", "&6Player %player% was unjailed");
		Loader.trans.addDefault("BanSystem.JailAlreadyExist", "&6Jail %jail% already exists");
		Loader.trans.addDefault("BanSystem.CreatedJail", "&6Create new jail %jail% at your location");
		Loader.trans.addDefault("BanSystem.JailNotExist", "&6Jail %jail% doesn't exist");
		Loader.trans.addDefault("BanSystem.DeletedJail", "&6Deleted jail %jail%");
		Loader.trans.addDefault("BanSystem.Broadcast.Ban", "&6Operator %operator% banned player %player% for %reason%");
		Loader.trans.addDefault("BanSystem.Broadcast.TempBanIP", "&6Operator %operator% &6temp-bannedIP player %player% for &c%reason% &6on &c%time%");
		Loader.trans.addDefault("BanSystem.Broadcast.BanIP", "&6Operator %operator% bannedIP player %player% for %reason%");
		Loader.trans.addDefault("BanSystem.Broadcast.TempMute", "&6Operator %operator% temp-muted player %player% for %reason% on %time%");
		Loader.trans.addDefault("BanSystem.Broadcast.Mute", "&6Operator %operator% muted player %player% for %reason%");
		Loader.trans.addDefault("BanSystem.Broadcast.Kick", "&6Operator %operator% kicked player %player% for %reason%");
		Loader.trans.addDefault("BanSystem.Broadcast.Jail", "&6Operator %operator% arrested player %player% for %reason%");
		Loader.trans.addDefault("BanSystem.Broadcast.TempJail", "&6Operator %operator% temp-arrested player %player% for %reason% on %time%");
		Loader.trans.addDefault("BanSystem.Broadcast.Warn", "&6Operator %operator% warned player %player% for %reason%");
		Loader.trans.addDefault("BanSystem.Broadcast.UnBan", "&6Operator %operator% unbanned player %player%");
		Loader.trans.addDefault("BanSystem.Broadcast.UnBanIP", "&6Operator %operator% unbannedIP player %player%");
		Loader.trans.addDefault("BanSystem.Broadcast.UnJail", "&6Operator %operator% unjailed player %player% for %reason% on %time%");
		Loader.trans.addDefault("BanSystem.Broadcast.UnMute", "&6Operator %operator% unmuted player %player% for %reason% on %time%");

		Loader.trans.addDefault("ClearInventory.PlayerInvCleared", "&6Inventory of &c%player% &6has been cleared");
		Loader.trans.addDefault("ClearInventory.NoMoney", "&cYou must have &6$%money% &cto purchase your inventory back");
		Loader.trans.addDefault("ClearInventory.NoConfirm", "&6You have no confirm request");
		Loader.trans.addDefault("ClearInventory.InvCleared", "&6Your inventory has been cleared");
		Loader.trans.addDefault("ClearInventory.ConfirmClearInv", "&cPlease confirm your request to clear your inventory");
		Loader.trans.addDefault("ClearInventory.InstallEconomyPlugin", "&cPlease install economy plugin");
		Loader.trans.addDefault("ClearInventory.NoInventoryRetrieved", "&6There is no inventory to be retrieved");
		Loader.trans.addDefault("ClearInventory.InventoryRetrievedForFree", "&aYour inventory has been retrieved for &6Free&a!");
		Loader.trans.addDefault("ClearInventory.InventoryRetrievedForMoney", "&a&aYour inventory has been retrieved for &6$%money%");
		Loader.trans.addDefault("ClearInventory.ConfirmEnabled", "&6Request for clear inventory has been enabled");
		Loader.trans.addDefault("ClearInventory.ConfirmDisabled", "&6Request for clear inventory has been disabled");
		Loader.trans.addDefault("Economy.Balance", "&6You currently have &a$%money%");
		Loader.trans.addDefault("Economy.BalanceOther", "&6Player &a%player% &6currently has &a$%currently%");
		Loader.trans.addDefault("Economy.Given", "&6$%money%&6 &ahas been added to your account, currently you have &6$%currently%");
		Loader.trans.addDefault("Economy.GivenToPlayer",
				"&6You gave &a$%money% &6to &a%playername%&6, currently &a%player% &6has &a$%currently%");
		Loader.trans.addDefault("Economy.Taken", "&c$%money% has been taken from your account, you now have &a$%currently%");
		Loader.trans.addDefault("Economy.TakenFromPlayer",
				"&6You took &c$%money% &6from &c%playername%&6, currently &c%player% &6has &c$%currently%");
		Loader.trans.addDefault("Economy.Reseted", "&6Your account has been reset, you now have &a$%currently%");
		Loader.trans.addDefault("Economy.ResetedPlayer", "&6You &creset &6account of player &c%playername%");
		Loader.trans.addDefault("Economy.PaidTo", "&6You paid &e$%money% &6to player &e%playername%");
		Loader.trans.addDefault("Economy.PaidFrom", "&6%playername% &6paid you &e$%money%&6, you now have &a$%currently%");
		Loader.trans.addDefault("Economy.NoMoney", "&cYou don't have enough money. You only have &6$%money%");
		Loader.trans.addDefault("Economy.SetPlayer", "&6Balance of player &a%playername% &6has been changed to &a$%money%");
		Loader.trans.addDefault("Economy.Set", "&6Your balance has been changed to &a$%money%");
		Loader.trans.addDefault("Economy.NoPlayers", "&6There is no player economies");
		Loader.trans.addDefault("Homes.Created", "&6Home &a'%home%' &6created");
		Loader.trans.addDefault("Homes.Deleted", "&6Home &c'%home%' &6deleted");
		Loader.trans.addDefault("Homes.Teleporting", "&6Teleporting to home &a'%home%'");
		Loader.trans.addDefault("Homes.TeleportingToOther", "&6Teleporting to home &a'%home%' &6of player &c%target%");
		Loader.trans.addDefault("Homes.TeleportingOtherToOther",
				"&6Teleporting player %player% to home &a'%home%' &6of player &c%target%");
		Loader.trans.addDefault("Homes.NotExists", "&6Home &c'%home%' &6doesn't exist");
		Loader.trans.addDefault("Homes.NotExistsOther", "&6Home &c'%home%' &6of player &c%target% &6doesn't exist");
		Loader.trans.addDefault("Homes.List", "&6Homes: &a%list%");
		Loader.trans.addDefault("Homes.ListOther", "&6Homes of player %target%: &a%list%");
		Loader.trans.addDefault("Homes.ListEmpty", "&6There are no homes");
		Loader.trans.addDefault("Homes.LimitReached", "&cYou reached the maximum home limit! &o(%limit%)");
		Loader.trans.addDefault("Homes.ListOtherEmpty", "&6Player %target% has no homes");
		Loader.trans.addDefault("Chunks.Loaded", "&aLoaded &6%chunks% &achunks in world &6'%world%'");
		Loader.trans.addDefault("Chunks.TotalLoaded", "&aTotal loaded &6%chunks% &achunks in &6%worlds% &aworlds");
		Loader.trans.addDefault("Chunks.Unloaded", "&aUnloaded &6'%chunks%' &achunks");
		Loader.trans.addDefault("Gamemode.Invalid", "&6Gamemode &c%gamemode% &6is invalid !");
		Loader.trans.addDefault("Gamemode.Changed", "&6Your gamemode changed to &a%gamemode%");
		Loader.trans.addDefault("Gamemode.ChangedOther", "&6Gamemode of player &a%player% &6changed to &a%gamemode%");
		Loader.trans.addDefault("TPS", "&aCurrent server TPS: &6%tps%");
		Loader.trans.addDefault("Enchant.Enchanted", "&5Item &d%item% &5enchanted with enchant &d%enchant% &5to level &d%level%");
		Loader.trans.addDefault("Enchant.EnchantRemoved",
				"&5From item &d%item% &5has been removed enchant &d%enchant% &5that has level &d%level%");
		Loader.trans.addDefault("Enchant.EnchantsRemoved", "&5From item &d%item% &5has been removed all enchants, list: &d%enchants%");
		Loader.trans.addDefault("Enchant.HandIsEmpty", "&cFirst take item to your &nhand");
		Loader.trans.addDefault("Enchant.EnchantNotExist", "&cEnchant &4'%enchant%' &cdoesn't exist");
		Loader.trans.addDefault("Enchant.NoEnchant", "&cThere isn't enchant &4%enchant% on item &4%item% &cto remove");
		Loader.trans.addDefault("Enchant.NoEnchants", "&cThere is no enchant on item &4%item% &cto remove");
		Loader.trans.addDefault("TrashTitle", "&8Trash");
		Loader.trans.addDefault("TpaSystem.TpahereSender", "&6Request for teleport to player %playername% sent");
		Loader.trans.addDefault("TpaSystem.TpahereTarget", "&6Player %playername% &6wants you to teleport to him");
		Loader.trans.addDefault("TpaSystem.TpaSender", "&6Request to teleport you to player %playername% sent");
		Loader.trans.addDefault("TpaSystem.NoRequest", "&6You don't have any request to accept or deny");
		Loader.trans.addDefault("TpaSystem.CantSendRequestToSelf", "&6You can't send request to your self");
		Loader.trans.addDefault("TpaSystem.CantBlockSelf", "&6You can't block yourself");
		Loader.trans.addDefault("TpaSystem.TpaTarget", "&6Player %playername% &6wants to teleport to you");
		Loader.trans.addDefault("TpaSystem.Tpaall", "&6Request to teleport players to you sent to players: %players%");
		Loader.trans.addDefault("TpaSystem.Tpall", "&6Teleported players to you: %players%");
		Loader.trans.addDefault("TpaSystem.TpLocationPlayer",
				"&6Player %playername% teleported to location in world %world% on X %x% Y %y% Z %z%");
		Loader.trans.addDefault("TpaSystem.TpLocation", "&6Teleporting you to location in world %world% on X %x% Y %y% Z %z%");
		Loader.trans.addDefault("TpaSystem.TpPlayerToPlayer", "&6Player %firstplayername% teleported to player %lastplayername%");
		Loader.trans.addDefault("TpaSystem.AlreadyHaveRequest", "&6Player %playername% already have request from you");
		Loader.trans.addDefault("TpaSystem.Tpaccept", "&6Request of player %playername% &6to teleport him to you accepted");
		Loader.trans.addDefault("TpaSystem.TpaAccepted", "&6Player %playername% &6accepted your request to teleport you to him");
		Loader.trans.addDefault("TpaSystem.Tpahereccept", "&6Request of player %playername% &6to teleport you to him accepted");
		Loader.trans.addDefault("TpaSystem.TpahereAccepted", "&6Player %playername% &6accepted your request to teleport him to you");
		Loader.trans.addDefault("TpaSystem.Tpadeny", "&6Request of player %playername% &6to teleport you to him denied");
		Loader.trans.addDefault("TpaSystem.TpaDenied", "&6Player %playername% &6denied your request to teleport you to him");
		Loader.trans.addDefault("TpaSystem.Tpaheredeny", "&6Request of player %playername% &6to teleport you to him denied");
		Loader.trans.addDefault("TpaSystem.TpahereDenied", "&6Player %playername% &6denied your request to teleport him to you");
		Loader.trans.addDefault("TpaSystem.Teleportedhere", "&6Player %playername% &6teleported to your location");
		Loader.trans.addDefault("TpaSystem.Teleported", "&6Teleporting you to player %playername%");
		Loader.trans.addDefault("TpaSystem.TeleportedHere", "&6Teleporting player %playername% &6to you");
		Loader.trans.addDefault("TpaSystem.TpaBlock.Blocked", "&6You are blocking teleport requests of player %playername%");
		Loader.trans.addDefault("TpaSystem.TpaBlock.UnBlocked", "&6You are no longer blocking teleport requests of player %playername%");
		Loader.trans.addDefault("TpaSystem.TpaBlock.Blocked-Global", "&6You are blocking teleport requests of all players");
		Loader.trans.addDefault("TpaSystem.TpaBlock.UnBlocked-Global", "&6You are no longer blocking teleport requests of all players");
		Loader.trans.addDefault("TpaSystem.TpBlocked", "&6Player %playername% is blocking your teleport");
		Loader.trans.addDefault("TpaSystem.TpaBlocked", "&6Player %playername% is blocking your requests");
		Loader.trans.addDefault("TpaSystem.Cancelled", "&6Player %playername% has cancelled teleport requests");
		Loader.trans.addDefault("TpaSystem.TpaCancel", "&6You cancelled teleport request");
		Loader.trans.addDefault("Sudo.SendMessage", "&6Sent message &a'%message%' &6as player &a%playername%");
		Loader.trans.addDefault("Sudo.SendCommand", "&6Sent command &a'%command%' &6as player &a%playername%");
		Loader.trans.addDefault("Repair.HandIsEmpty", "&cFirst take item to your &nhand");
		Loader.trans.addDefault("Repair.Repaired", "&aItem in your hand reapired");
		Loader.trans.addDefault("Repair.RepairedAll", "&aAll items in your inventory reapired");
		Loader.trans.addDefault("Time.Day", "&6Time in world &a'%world%' &6changed to &aday");
		Loader.trans.addDefault("Time.Night", "&6Time of world &e'%world%' &6changed to &enight");
		Loader.trans.addDefault("Time.WorldNotExists", "&6World &c'%world%' &6doesn't exist");
		Loader.trans.addDefault("Weather.Sun", "&6Weather of world &e'%world%' &6changed to &asunny");
		Loader.trans.addDefault("Weather.Rain", "&6Weather of world &e'%world%' &6changed to &erainy");
		Loader.trans.addDefault("Weather.Thunder", "&6Weather of world &e'%world%' &6changed to &cstorm");
		Loader.trans.addDefault("Weather.WorldNotExists", "&6World &c'%world%' &6doesn't exist");
		Loader.trans.addDefault("MultiWorld.AlreadyExists", "&cWorld &6'%world%' &calready exists");
		Loader.trans.addDefault("MultiWorld.WorldImported", "&6World &a'%world%' &6imported & loaded");
		Loader.trans.addDefault("MultiWorld.AlreadyLoaded", "&cWorld &6'%world%' &cis already loaded");
		Loader.trans.addDefault("MultiWorld.TeleportedWorld", "&6You has been teleported to world &c'%world%'");
		Loader.trans.addDefault("MultiWorld.SpawnSet", "&aSpawn of world &6'%world%'&a has been set");
		Loader.trans.addDefault("MultiWorld.CantBeDeleted", "&cWorld &6'%world%' &ccan't been deleted");
		Loader.trans.addDefault("MultiWorld.Deleted", "&aWorld &6'%world%' &ahas been deleted");
		Loader.trans.addDefault("MultiWorld.Created", "&aWorld &6'%world%' &ahas been created");
		Loader.trans.addDefault("MultiWorld.NotFoundImport", "&6We not found world folder with name &c'%world%'");
		Loader.trans.addDefault("MultiWorld.Loaded", "&aWorld &6'%world%' &ahas been loaded");
		Loader.trans.addDefault("MultiWorld.Unloaded", "&aWorld &6'%world%' &ahas been unloaded and saved");
		Loader.trans.addDefault("MultiWorld.DoNotUnloaded", "&cWorld &6'%world%' &cdoesn't been unloaded");
		Loader.trans.addDefault("MultiWorld.AlreadyCreated", "&6World &c'%world%' &6is already created");
		Loader.trans.addDefault("MultiWorld.PlayerTeleportedWorld", "&aPlayer &6%player% &ahas been teleported to world &6'%world%'");

		Loader.trans.addDefault("Mail.Notification", "&6You have &a%number% &6new mail('s)");

		Loader.trans.addDefault("WalkSpeed.WalkSpeed", "&6Your walk speed has been set to &a%speed%");
		Loader.trans.addDefault("WalkSpeed.WalkSpeedPlayer", "&6Walk speed of player &a%playername% &6has been set to &a%speed%");
		Loader.trans.addDefault("Fly.Enabled", "&6Fly &aenabled");
		Loader.trans.addDefault("Fly.Disabled", "&6Fly &cdisabled");
		Loader.trans.addDefault("Fly.FlySpeed", "&6Your fly speed has been set to &a%speed%");
		Loader.trans.addDefault("Fly.FlySpeedPlayer", "&6Fly speed of player &a%playername% &6has been set to &a%speed%");
		Loader.trans.addDefault("Fly.SpecifiedPlayerFlyEnabled", "&6Fly of player %playername%&6 has been &aenabled");
		Loader.trans.addDefault("Fly.SpecifiedPlayerFlyDisabled", "&6Fly of player %playername%&6 has been &cdisabled");
		Loader.trans.addDefault("TempFly.Enabled", "&6TempFly &aenabled &6for &a%time%");
		Loader.trans.addDefault("TempFly.EnabledOther", "&6TempFly of player %playername% has been &aenabled &6for &a%time%");
		Loader.trans.addDefault("ConsoleErrorMessage", "&4This command can be used only ingame!");
		Loader.trans.addDefault("SoundErrorMessage", "&4Invalid song!");
		Loader.trans.addDefault("Spawn.TeleportedToSpawn", "&aYou has been teleported to the spawn");
		Loader.trans.addDefault("Spawn.NoHomesTeleportedToSpawn", "&aYou have no home! You have been teleported to the spawn");
		Loader.trans.addDefault("Spawn.PlayerTeleportedToSpawn", "&aPlayer %playername% &ahas been teleported to the spawn");
		Loader.trans.addDefault("Spawn.SpawnSet", "&6Global spawn set");
		Loader.trans.addDefault("Warp.Created", "&aWarp &6'%warp%' &ahas been created");
		Loader.trans.addDefault("Warp.CreatedWithPerm", "&aWarp &6'%warp%' &awith permission &7%permission% &ahas been created");
		Loader.trans.addDefault("Warp.Deleted", "&6Warp &c'%warp%' &6has been deleted");
		Loader.trans.addDefault("Warp.Warping", "&6Warping to warp &c'%warp%'");
		Loader.trans.addDefault("Warp.PlayerWarped", "&6Player %playername% warped to warp &c'%warp%'");
		Loader.trans.addDefault("Warp.List", "&6Warps: &c%warps%");
		Loader.trans.addDefault("Warp.AlreadyExists", "&cWarp &6'%warp%' &calready exists");
		Loader.trans.addDefault("Warp.NotExists", "&cWarp &6'%warp%' &cdoesn't exist");
		Loader.trans.addDefault("Warp.NoWarps", "&6There are no warps");
		Loader.trans.addDefault("Warp.CantGetLocation", "&cWarp &6'%warp%' &chave wrong location");
		Loader.trans.addDefault("OnJoin.Messages",
				Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-",
						"&aWelcome back &6%player% &aon &6&l%server_name% !", "&aDon't forget to vote! /Vote",
						"&aServer IP: &6%server_ip%", "&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		Loader.trans.addDefault("OnJoin.Join", "%prefix%&a%playername% &ajoined to the game");
		Loader.trans.addDefault("OnJoin.FirstJoin.Messages",
				Arrays.asList("&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-",
						"&aWelcome &6%player% &aon &6&l%server_name% !",
						"&aInvite more of your friends and create a great community of people together!",
						"&aServer IP: &6%server_ip%", "&6-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		Loader.trans.addDefault("OnJoin.FirstJoin.BroadCast", "%prefix%&aWelcome new player &r%player%&a on our server!");
		Loader.trans.addDefault("OnLeave.Leave", "%prefix%&r%player% &9left the game");
		Loader.trans.addDefault("Inventory.OpeningEnderChest", "&5Opening EnderChest..");
		Loader.trans.addDefault("Inventory.OpeningEnderChestOther", "&5Opening EnderChest of player %playername%&5..");
		Loader.trans.addDefault("Inventory.OpeningEnderChestForTarget",
				"&5Opening EnderChest of player %playername%&5 for player %target%..");
		Loader.trans.addDefault("Inventory.OpeningCraftTable", "&5Opening Crafting Table..");
		Loader.trans.addDefault("Inventory.OpeningCraftTableForTarget", "&5Opening Crafting Table for player %target%..");
		Loader.trans.addDefault("Inventory.OpeningInvsee", "&5Opening inventory of player %playername%&5..");
		Loader.trans.addDefault("Inventory.OpeningInvseeForTarget",
				"&5Opening inventory of player %playername% &5 for player %target%&5..");
		Loader.trans.addDefault("Inventory.OpeningTrash", "&5Opening Trash..");
		Loader.trans.addDefault("Inventory.ClosePlayersInventory", "&5You closed opened inventory of player %playername%&5..");
		Loader.trans.addDefault("Vanish.Enabled", "&6Your vanish has been enabled");
		Loader.trans.addDefault("Vanish.Disabled", "&6Your vanish has been disabled");
		Loader.trans.addDefault("Vanish.EnabledPlayer", "Vanish of player %player% has been enabled");
		Loader.trans.addDefault("Vanish.DisabledPlayer", "Vanish of player %player% has been disabled");
		Loader.trans.addDefault("MaintenanceMode.TurnOn", "&6Maintenance mode turned &aon.");
		Loader.trans.addDefault("MaintenanceMode.TurnOff", "&6Maintenance mode turned &aoff.");
		Loader.trans.addDefault("Spawner.Set", "&6Spawner set to mob %mob%");
		Loader.trans.addDefault("Spawner.AmountSet", "&6Spawner amount limit of spawning set to %amount%");
		Loader.trans.addDefault("Spawner.RangePlayerSet", "&6Spawner required player range set to %range%");
		Loader.trans.addDefault("Spawner.TimeSet", "&6Spawner spawning time set to %time%");
		Loader.trans.addDefault("Spawner.Set", "&6Spawner set to mob %mob%");
		Loader.trans.addDefault("Spawner.InvalidMob", "&cMob with name &4'%mob%' &cis invalid");
		Loader.trans.addDefault("Spawner.BlockIsNotSpawner", "&6Block you're looking at isn't a spawner");
		Loader.trans.addDefault("NotPermissionsMessage", "&cYou do not have permission %permission% to do that!");
		Loader.trans.addDefault("ClearChat.ByConsole", "&aChat Cleared by &cconsole");
		Loader.trans.addDefault("ClearChat.ByPlayer", "&aChat Cleared by %playername% &7(%player%)");
		Loader.trans.addDefault("ClearChat.PlayerNotOnline", "&cPlayer %player% is not online");
		Loader.trans.addDefault("ClearChat.SpecifedChatCleared", "&6Chat of player &c%player% &6has been cleared");
		Loader.trans.addDefault("ClearChat.NoClearOwnChat", "&6You can't clear your own chat");
		Loader.trans.addDefault("ClearChat.SpecifedChatHaveBypass",
				"&6You can't clear chat of player %player%, because they has bypass permission");
		Loader.trans.addDefault("ConfigReloaded", "&aConfiguration reloaded !");
		Loader.trans.addDefault("Back.CantGetLocation", "&cCan't get location to teleport back");
		Loader.trans.addDefault("Back.Teleporting", "&6Teleporting back to latest position..");
		Loader.trans.addDefault("Back.PlayerTeleported", "&6Player %playername% &6has been teleported back to last position..");
		Loader.trans.addDefault("Help.NoHelpForCommand", "&cThere is no help for command: %command%");
		Loader.trans.addDefault("Help.Fly", "&5Enable/disable player fly");
		Loader.trans.addDefault("Help.FlySpeed", "&5Set your or specified player fly speed");
		Loader.trans.addDefault("Help.WalkSpeed", "&5Set your or specified player walk speed");
		Loader.trans.addDefault("Help.ClearChat", "&5Deletes all players chat");
		Loader.trans.addDefault("Help.Reload", "&5Reload plugin configuration");
		Loader.trans.addDefault("Help.Help", "&5Shows this help list");
		Loader.trans.addDefault("Help.Give", "&5Give player item/block");
		Loader.trans.addDefault("Help.Butcher", "&5Kill specified entities in specified world");
		Loader.trans.addDefault("Help.WhoIs", "&5Information abouts specified player");
		Loader.trans.addDefault("Help.Kill", "&5Kill player");
		Loader.trans.addDefault("Help.Broadcast", "&5Send message for all players");
		Loader.trans.addDefault("Help.Item.SetName", "&5Set custom name on item in your hand");
		Loader.trans.addDefault("Help.Item.SetLore.Add", "&5Add lore line on item in your hand");
		Loader.trans.addDefault("Help.Item.SetLore.Remove", "&5Remove lore line from item in your hand");
		Loader.trans.addDefault("Help.Item.SetLore.Lines", "&5Show you list of lore lines on item in your hand");
		Loader.trans.addDefault("Help.Item.Unbreakable", "&5Set your item in your hand unbreakable");
		Loader.trans.addDefault("Help.Item.HideEnchants", "&5Hide all enchants on your item in your hand");
		Loader.trans.addDefault("Help.Seen", "&5Sends you information about the player how long is player online/offline");
		Loader.trans.addDefault("Help.Xp.Give", "&5Gave player experiences");
		Loader.trans.addDefault("Help.Xp.Take", "&5Take from player experiences");
		Loader.trans.addDefault("Help.Xp.Set", "&5Set player experiences to new value");
		Loader.trans.addDefault("Help.Xp.Balance", "&5Balance of player experiences");
		Loader.trans.addDefault("Help.MultiWorld.Tp", "&5Teleport player to world");
		Loader.trans.addDefault("Help.BanSystem.TempBan", "&5Temp-Ban specified player with reason for time");
		Loader.trans.addDefault("Help.BanSystem.Ban", "&5Ban specified player with reason");
		Loader.trans.addDefault("Help.BanSystem.BanIP", "&5Ban IP of specified player with reason");
		Loader.trans.addDefault("Help.BanSystem.UnBan", "&5UnBan specified player");
		Loader.trans.addDefault("Help.BanSystem.UnBanIP", "&5UnBan-IP of specified player");
		Loader.trans.addDefault("Help.BanSystem.UnMute", "&5UnMute specified player");
		Loader.trans.addDefault("Help.BanSystem.TempMute", "&5Temp-Mute specified player with reason for time");
		Loader.trans.addDefault("Help.BanSystem.Mute", "&5Mute specified player with reason");
		Loader.trans.addDefault("Help.BanSystem.unWarn", "&5UnWarn specified player");
		Loader.trans.addDefault("Help.BanSystem.unJail", "&5UnJail specified player");
		Loader.trans.addDefault("Help.BanSystem.Jail", "&5Jail specified player with reason");
		Loader.trans.addDefault("Help.BanSystem.TempJail", "&5Jail specified player with reason for time");
		Loader.trans.addDefault("Help.BanSystem.setJail", "&5Set new jail on your location");
		Loader.trans.addDefault("Help.BanSystem.delJail", "&5Remove specified jail");
		Loader.trans.addDefault("Help.BanSystem.Immune", "&7You can enable or disable immunity for others players");
		Loader.trans.addDefault("Help.BanSystem.Kick", "&5Kick specified player from server with reason");
		Loader.trans.addDefault("Help.BanSystem.Warn", "&5Send warning to specified player with reason");
		Loader.trans.addDefault("Help.TabList.Prefix", "&5Set prefix of group in tablist and above player");
		Loader.trans.addDefault("Help.TabList.Suffix", "&5Set suffix of group in tablist and above player");
		Loader.trans.addDefault("Help.TabList.Priorite", "&5Set priorite of group in tablist");
		Loader.trans.addDefault("Help.TabList.Reload", "&5Reload tablist config and tablist of all players");
		Loader.trans.addDefault("Help.TabList.Create", "&5Create new tablist group");
		Loader.trans.addDefault("Help.TabList.Delete", "&5Delete existing tablist group");
		Loader.trans.addDefault("Help.MultiEconomy.Transfer", "&5Transfer player economy to specified economy group");
		Loader.trans.addDefault("Help.MultiEconomy.Money", "&5Show you money of player specified economy group");
		Loader.trans.addDefault("Help.MultiEconomy.Create", "&5Show you money of player specified economy group");
		Loader.trans.addDefault("Help.MultiEconomy.Delete", "&5Delete existing economy group");
		Loader.trans.addDefault("Help.MultiEconomy.Add", "&5Add world to economy group");
		Loader.trans.addDefault("Help.MultiEconomy.Remove", "&5Remove world from economy group");
		Loader.trans.addDefault("Help.MultiEconomy.Groups", "&5Show you list of economies groups");
		Loader.trans.addDefault("Help.MultiEconomy.Worlds", "&5Show you list of worlds in economy group");
		Loader.trans.addDefault("Help.TpaSystem.Tpa", "&5Send request for teleport you to player");
		Loader.trans.addDefault("Help.TpaSystem.Tp", "&5Teleport you to player or player to coordinates");
		Loader.trans.addDefault("Help.TpaSystem.Tpahere", "&5Send request for teleport player to you");
		Loader.trans.addDefault("Help.TpaSystem.Tphere", "&5Teleport player to you");
		Loader.trans.addDefault("Help.Info", "&5Shows informations about plugin");
		Loader.trans.addDefault("Help.Addons.Disable", "&5Disable enabled addon");
		Loader.trans.addDefault("Help.Addons.Enable", "&5Enable disabled addon");
		Loader.trans.addDefault("Help.General", "&5Shows general informations about chat");
		Loader.trans.addDefault("Help.Vanish", "&5Hide / Show you for all players");
		Loader.trans.addDefault("Help.Nick", "&5Change your nickname in chat");
		Loader.trans.addDefault("Help.NickReset", "&5Reset nickname of player in chat");
		Loader.trans.addDefault("Help.Sudo", "&5Sent message/command as specified player");
		Loader.trans.addDefault("Help.Me", "&5Shows informations about you");
		Loader.trans.addDefault("Help.Version", "&5Shows plugin version");
		Loader.trans.addDefault("Help.Reset", "&5Reset general informations");
		Loader.trans.addDefault("Help.Invsee", "&5Open inventory of player");
		Loader.trans.addDefault("Help.Kit-Give", "&5Give kit to specified player");
		Loader.trans.addDefault("Help.Weather", "&5Set specified wheather in specified world");
		Loader.trans.addDefault("Help.Time", "&5Set specified time in specified world");
		Loader.trans.addDefault("Help.Skull", "&5Gave you a player head");
		Loader.trans.addDefault("Help.SkullOther", "&5Gave specified player a player head");
		Loader.trans.addDefault("Help.Enchant", "&5Enchant item in your hand with specified enchant and level");
		Loader.trans.addDefault("Help.EnchantRemove", "&5Remove specified enchant from item in your hand");
		Loader.trans.addDefault("Help.Gamemode", "&5Change specified player gamemode");
		Loader.trans.addDefault("Help.Heal", "&5Heal & Feed specified player");
		Loader.trans.addDefault("Help.ChatLock", "&5Lock or Unlock chat");
		Loader.trans.addDefault("Help.Spawner-Mob", "&5Sets spawner on specified entity");
		Loader.trans.addDefault("Help.Spawner-SpawnTime", "&5Sets entity spawn delay");
		Loader.trans.addDefault("Help.Spawner-SpawnAmount", "&5Sets entity spawn amount");
		Loader.trans.addDefault("Help.Spawner-SpawnRangePlayer", "&5Sets required range of players to spawn entity");
		Loader.trans.addDefault("Help.Helpop", "&5Send message to online admins");
		Loader.trans.addDefault("Help.List", "&5Shows list with swear words a spam words");
		Loader.trans.addDefault("Help.Chunks", "&5Shows chunks used and loaded worlds");
		Loader.trans.addDefault("Help.AFK-Other", "&5Enable or Disable AFK of specified player");
		Loader.trans.addDefault("Help.ClearInv.Clear", "&5Send request to clear your inventory");
		Loader.trans.addDefault("Help.ClearInv.Undo", "&5Undo latest clear inventory command (If it was confirmed)");
		Loader.trans.addDefault("Help.ClearInv.Confirm", "&5Confirm request for clear inventory");
		Loader.trans.addDefault("Help.ClearInv.Other", "&5Clear specified player inventory");
		Loader.trans.addDefault("Help.Warp.SetWarp", "&5Create new warp");
		Loader.trans.addDefault("Help.Warp.DelWarp", "&5Delete existing warp");
		Loader.trans.addDefault("Help.Warp.Warp", "&5Teleport you to specified warp");
		Loader.trans.addDefault("Help.DelHome", "&5Delete specified home");
		Loader.trans.addDefault("Help.Thor", "&5Strike player with lighting bolt");
		Loader.trans.addDefault("Help.ThorOnBlock", "&5Summon lightning bolt on block you are looking!");
		Loader.trans.addDefault("Help.TempFly", "&5Enable FLY to specific player for time");
		Loader.trans.addDefault("Help.Back", "&5Teleport player back to latest position");
		Loader.trans.addDefault("Help.Spawn", "&5Teleport player to spawn");
		Loader.trans.addDefault("Help.PrivateMessage", "&5Send player private message");
		Loader.trans.addDefault("Help.God", "&5Enable or disable god mode of specified player");
		Loader.trans.addDefault("Help.ReplyPrivateMessage", "&5Reply to latest message");
		Loader.trans.addDefault("Help.Economy.Give", "&5Gave money to player");
		Loader.trans.addDefault("Help.Economy.Take", "&5Take money from player");
		Loader.trans.addDefault("Help.Economy.Reset", "&5Reset money of player");
		Loader.trans.addDefault("Help.Economy.Pay", "&5Send money to player");
		Loader.trans.addDefault("Help.Economy.Set", "&5Sets the player money value");
		Loader.trans.addDefault("Help.Economy.Balance", "&5Show you balance of specified player");
		Loader.trans.addDefault("Help.Inventory.CloseInventory", "&5Close opened inventory of specific player");
		Loader.trans.addDefault("Help.Homes.HomeOther", "&5Teleport you to player home");

		Loader.trans.addDefault("Help.Mail.Send", "&5Send some player new mail...");
		Loader.trans.addDefault("Help.Mail.Read", "&5Read all your mails...");
		Loader.trans.addDefault("Help.Mail.Clear", "&5Delete all your mails...");
		Loader.trans.addDefault("Heal.CooldownMessage", "&6You have to wait &c%time% seconds &6before you can heal yourselves");
		Loader.trans.addDefault("Heal.Healed", "&aYou has been healed");
		Loader.trans.addDefault("Heal.Feed", "&aYou has been feeded");
		Loader.trans.addDefault("Heal.PlayerFeed", "&aPlayer %playername% has been feeded");
		Loader.trans.addDefault("Heal.SpecifyPlayerHealed", "&aPlayer %playername% has been healed");
		Loader.trans.addDefault("TabList.PrefixSet", "&6Prefix of group '&a%group%&6' set to '&a%prefix%&6'");
		Loader.trans.addDefault("TabList.SuffixSet", "&6Suffix of group '&a%group%&6' set to '&a%suffix%&6'");
		Loader.trans.addDefault("TabList.PrioriteSet", "&6Priorite of group '&a%group%&6' set to '&a%priorite%&6'");
		Loader.trans.addDefault("TabList.AlreadyExist", "&6Group '&c%group%&6' already exists");
		Loader.trans.addDefault("TabList.DoNotExist", "&6Group '&c%group%&6' doesn't exist");
		Loader.trans.addDefault("TabList.GroupCreated", "&6Group '&c%group%&6' created");
		Loader.trans.addDefault("TabList.GroupDeleted", "&6Group '&c%group%&6' deleted");
		Loader.trans.addDefault("Kit.List", "&6Kits: &a%kits%");
		Loader.trans.addDefault("Kit.Used", "&6Kit &a'%kit%' &6used.");
		Loader.trans.addDefault("Kit.Cooldown", "&6You must wait &a%cooldown% &6to use kit &a'%kit%'");
		Loader.trans.addDefault("Kit.NotExists", "&6Kit &c'%kit%' &6doesn't exists");
		Loader.trans.addDefault("Kit.Got", "&6You got kit &a'%kit%'");
		Loader.trans.addDefault("Kit.Given", "&6You gave kit &a'%kit%' &6to player %playername%");
		Loader.trans.addDefault("God.Enabled", "&6God mode &aEnabled");
		Loader.trans.addDefault("God.Disabled", "&6God mode &cDisabled");
		Loader.trans.addDefault("God.SpecifiedPlayerGodEnabled", "&6God mode of player %playername%&6 has been &aEnabled");
		Loader.trans.addDefault("God.SpecifiedPlayerGodDisabled", "&6God mode of player %playername%&6 has been &cDisabled");
		Loader.trans.addDefault("General.Reset", "&2Chat General was reseted");
		Loader.trans.addDefault("General.Confirm",
				"&cConfirm reset all general informations using '/ServerControl Reset Confirm' command. &o(You have 10 seconds to do this)");
		Loader.trans.addDefault("General.AnyConfirm", "&cYou have no request to confirm");
		Loader.trans.addDefault("General.PleaseConfirm", "&cPlease confirm your request");
		Loader.trans.addDefault("AboutYou", Arrays.asList("&a-=-=-=-=-=-< &6%playername% &a>-=-=-=-=-=-", "&aNickname: &6%player%",
				"&aJoins: &6%joins%", "&aLeaves: &6%leaves%", "&aFirst Join: &6%firstjoin%",
				"&aVulgarWords: &6%vulgarwords%", "&aSpams: &6%spams%", "&aKicks: &6%kicks%", "&aDeaths: &6%deaths%",
				"&aMoney: &6%vault-money%$", "&aGroup: &6%vault-group%", "&a-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
		Loader.trans.addDefault("Thor", "&ePlayer %playername% striked");
		Loader.trans.addDefault("ThorOnBlock", "&eSummoned lightning bolt!");
		Loader.trans.addDefault("Cooldown.ToSendMessage", "&6You have to wait &c%timer% seconds &6before you can send a message");
		Loader.trans.addDefault("Cooldown.ToSendCommand", "&6You have to wait &c%timer% seconds &6before you can send a command");
		Loader.trans.addDefault("Security.TriedSendSimiliarMessage", "&4%player% &ctried to send a &4similiar message");
		Loader.trans.addDefault("Security.TryingSendVulgarWord", "&4%player% &ctried to write a &4swear word");
		Loader.trans.addDefault("Security.TryingSendSpam", "&4%player% &ctried to send a &4spam");
		Loader.trans.addDefault("Security.TryingSendAdvertisement", "&4%player% &ctried to send an &4advertisement");
		Loader.trans.addDefault("Security.TryingSendCaps", "&ePlayer &6%player% &etried send a Caps &8( &6%message% &8)&e.");
		Loader.trans.addDefault("Security.TryingSendBlockedCommand",
				"&ePlayer &6%playername% &etried send blocked command &8( &6%command% &8)&e.");
		Loader.trans.addDefault("ChatLock.ChatIsLocked", "&4Chat is Locked");
		Loader.trans.addDefault("ChatLock.ChatIsUnlocked", "&2Chat is Unlocked!");
		Loader.trans.addDefault("ChatLock.ChatIsLockedErrorPlayerMessage",
				"&4We are sorry but it is currently locked chat, try it later.");
		Loader.trans.addDefault("ChatLock.BroadCastMessageChatLock", "&ePlayer &6%player% &ewrote: &6%message%&e, but chat is locked.");
		Loader.trans.addDefault("Caps", "&cPlease turn off caps lock, because you sending too many large block letters");
		Loader.trans.addDefault("UknownCommand", "&4Uknown command");
		Loader.trans.addDefault("BroadCastMessageVulgarWord",
				"&ePlayer &6%player% &ewrote a swear word &8( &6%message% &7<--- &6%word% &8)&e.");
		Loader.trans.addDefault("BroadCastMessageAdvertisement", "&ePlayer &6%player% &ewrote an advertisement &8( &6%message% &8)&e.");
		Loader.trans.addDefault("BroadCastMessageAdvertisementPickupItem",
				"&ePlayer &6%player% &etried pickup item with an advertisement &8( &6%message% &8)&e.");
		Loader.trans.addDefault("BroadCastMessageAdvertisementDropItem",
				"&ePlayer &6%player% &etried drop item with an advertisement &8( &6%message% &8)&e.");
		Loader.trans.addDefault("BroadCastMessageSpam", "&ePlayer &6%player% &ehas attempted spam &8(&6 %message% &7<--- &6%word%&8)&e.");
		Loader.trans.addDefault("VulgarWordsList-PlayerMessage",
				"&cHey, &6%player% &cword &6'%word%! &cis not allowed, please do not repeat it");
		Loader.trans.addDefault("Immune.Enabled", "&6You &aenabled &6your immunity");
		Loader.trans.addDefault("Immune.Disabled", "&6You &cdisabled &6your immunity");
		Loader.trans.addDefault("Immune.OnOther", "&6You &aenabled &6immunity of player &a%target%");
		Loader.trans.addDefault("Immune.OffOther", "&6You &cdisabled &6immunity of player &c%target%");
		Loader.trans.addDefault("Immune.NoPunish", "&6You can't &c%punishment% %target%&6, his immunity is enabled.");
		Loader.trans.setHeader("*************************\n" + "*** Created by DevTec ***\n" + "*************************\n");
		Loader.trans.create();
	}

	public static void configLoading() {
		Loader.config.addDefault("Options.Maintenance.Enabled", false);
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
		if (!Loader.config.existPath("Options.Disable-Items")) {
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
		if (!Loader.config.existPath("Options.Economy.MultiEconomy.Types")) {
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
		if (!Loader.config.existPath("BanSystem.Warn.Operations")) {
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
		if (!Loader.config.existPath("Chat-Groups.DefaultFormat")) {
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
		if (!Loader.config.existPath("Homes.default")) {
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
		Loader.config.addDefault("TasksOnSend.BlockedCommand-Broadcast", false);
		Loader.config.addDefault("TasksOnSend.Spam.Use-Commands", true);
		Loader.config.addDefault("TasksOnSend.Spam.Broadcast", false);
		Loader.config.addDefault("TasksOnSend.Spam.Commands", Arrays.asList("msg %player% &0[&4AntiSpam&0] &cPlease, stop spamming"));
		Loader.config.addDefault("TasksOnSend.Swear.Use-Commands", true);
		Loader.config.addDefault("TasksOnSend.Swear.Broadcast", false);
		Loader.config.addDefault("TasksOnSend.Swear.Commands",
				Arrays.asList("msg %player% &0[&4AntiSwear&0] &cPlease, stop being vulgar"));
		Loader.config.addDefault("TasksOnSend.Advertisement.Broadcast", false);
		Loader.config.addDefault("TasksOnSend.Advertisement.Use-Commands", true);
		Loader.config.addDefault("TasksOnSend.Advertisement.Commands", Arrays
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
		Loader.config.setHeader("+-------------------------------------------------------------------+ #\r\n"
				+ "| Info: https://dev.bukkit.org/projects/server-control-reloaded     | #\r\n"
				+ "+-------------------------------------------------------------------+ #\r\n"
				+ "Options for RespawnTeleport are: Home, Bed, Spawn" + "PlaceHolders for AutoMessage:\n"
				+ "  %used_ram% - used memory\n" + "  %free_ram% -  free memory\n" + "  %max_ram% - maximum memory\n"
				+ "  %online% - online players\n" + "  %max_players% - maximum players on server\n"
				+ "  %time% - server time\n" + "  %date% - server date\n"
				+ "+-------------------------------------------+ #\r\n"
				+ "| INFO: TimeZones! List of time zones:      | #\r\n"
				+ "+- https://greenwichmeantime.com/time-zone -+ #");
		Loader.config.create();
	}

	public static void ScoreboardLoading() {
		Loader.sb.addDefault("Scoreboard-Enabled", true);
		Loader.sb.addDefault("Scoreboard-PerWorld", false);
		Loader.sb.addDefault("RefleshTick", 20);
		Loader.sb.addDefault("Name", "&bStatus");
		Loader.sb.addDefault("Lines", Arrays.asList("&r&lMoney: &a%money%$", "&r&lOnline:  &a%online%"));
		if(!Loader.sb.exist("PerWorld")) {
		Loader.sb.addDefault("PerWorld.pvp_world.Name", "&9PvP");
		Loader.sb.addDefault("PerWorld.pvp_world.Lines", Arrays.asList("&r&lKills: &a%kills%$", "&r&lHealth:  &a%health%"));

		Loader.sb.addDefault("PerWorld.skyblock.Name", "&eSkyBlock");
		Loader.sb.addDefault("PerWorld.skyblock.Lines",
				Arrays.asList("&r&lMoney: &a%money%$", "&r&lHealth:  &a%health%", "&r&lFood:  &a%food%"));
		}
		Loader.sb.setHeader("%money%   player money balance\n" + "%online%   online players on server\n"
				+ "%max_players%   maximum players on server\n" + "%time%   server time\n" + "%date%   server date\n"
				+ "%world%   world name of player\n" + "%health%, %hp%   player healths\n"
				+ "%food%   player food level\n" + "%group%   player vault group\n" + "%ping%   player server ping\n"
				+ "%tps%   current server tps\n" + "%ram_free%   free memory of server\n"
				+ "%ram_usage%   used memory of server\n" + "%ram_max%   maximum memory of server\n"
				+ "%ram_free_percentage%   free memory of server in percentage\n"
				+ "%ram_usage_percentage%   used memory of server in percentage\n" + "%kills%   killed players\n"
				+ "%afk%   afk placeholder");
		Loader.sb.create();
	}

	public static void TabLoading() {
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
		Loader.tab.addDefault("NameTag-RefleshTick", 80);
		Loader.tab.addDefault("AFK.IsAFK", " &4&l*AFK*");
		Loader.tab.addDefault("AFK.IsNotAFK", "");
		if (!Loader.tab.existPath("Groups.default")) {
			Loader.tab.addDefault("Groups.default.TabList.Prefix", "&7Player &r");
			Loader.tab.addDefault("Groups.default.TabList.Suffix", "%afk%");
			Loader.tab.addDefault("Groups.default.NameTag.Prefix", "&7Player &r");
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
		Loader.tab.setHeader("%money%   player money balance\n" + "%online%   online players on server\n"
				+ "%max_players%   maximum players on server\n" + "%time%   server time\n" + "%date%   server date\n"
				+ "%world%   world name of player\n" + "%health%, %hp%   player healths\n"
				+ "%food%   player food level\n" + "%group%   player vault group\n" + "%ping%   player server ping\n"
				+ "%tps%   current server tps\n" + "%ram_free%   free memory of server\n"
				+ "%ram_usage%   used memory of server\n" + "%ram_max%   maximum memory of server\n"
				+ "%ram_free_percentage%   free memory of server in percentage\n"
				+ "%ram_usage_percentage%   used memory of server in percentage\n" + "%kills%   killed players\n"
				+ "%afk%   afk placeholder");
		Loader.tab.create();
	}

	public static void MultiWorldLoading() {
		Loader.mw.addDefault("ModifyMobsSpawnRates", false);
		Loader.mw.addDefault("SavingTask.Enabled", true);
		Loader.mw.addDefault("SavingTask.Delay", 3600);
		Loader.mw.create();
	}

	public static void KitLoading() {
		if (!Loader.kit.existPath("Kits")) {
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
		Loader.kit.create();
	}

	public static void load() {
		MultiWorldLoading();
		configLoading();
		TranslationsLoading();
		ScoreboardLoading();
		TabLoading();
		KitLoading();
	}

	public static void reload() {
		Loader.trans.reload();
		Loader.config.reload();
		Loader.kit.reload();
		Loader.mw.reload();
		Loader.sb.reload();
		Loader.tab.reload();
	}
}
