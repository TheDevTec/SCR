package Utils;

import org.bukkit.entity.Player;

import ServerControl.Loader;

public class setting {
	public static enum DeathTp{
		Home,
		Bed,
		Spawn
	}
	
	
	public static boolean
	warn_reload, warn_stop, warn_restart,
	color_chat, color_sign,
	sound, cmdblock, disable_item,
	vip, vip_kick, vip_add, vip_join,
	motd, motd_maintenance,
	lock_chat, lock_server,
 	afk_auto, afk_kick, code,
	sb, sb_world, am, am_random, timezone, save,
	tab_header, tab_footer, tab_nametag, tab_sort, tab, 
	eco, eco_other, eco_log, eco_multi,
	ad_book, ad_chat, ad_cmd, ad_sign, ad_anvil, ad_itemdrop, ad_itempick,
	spam_chat, spam_cmd, swear_chat, swear_cmd,
	join_spawn, join_msg, join_motd, join_first, join_first_give, join_first_percmd, leave,
	cool_cmd, cool_chat, cool_percmd, color_chat_perm,  color_sign_perm, 
	tp_safe,tp_onreqloc, ram, spam_double,caps_chat, caps_cmd;
	public static DeathTp deathspawn;
	
/**	public static boolean offlineinvsee; ///invsee <offline player>, /esee <offline player> (Req. SCR_PerWorldInventory)
	public static boolean clearmem; //clear memory every X minutes
	public static boolean criticalclearmem; //clear memory when memory usage is ~90%
	public static boolean clearmemKillmobs; //kill mobs in chunks with 100+ mobs
	public static boolean findchunks; //every X minutes, this feature will search for chunks with 100+ mobs and send to the console a warning about these chunks
**/
	public static void load() {
		timezone = Loader.config.getBoolean("Options.TimeZone.Use");
		ram = Loader.config.getBoolean("Options.RAM-Percentage");
		warn_restart = Loader.config.getBoolean("Options.WarningSystem.Restart.Use");
		warn_reload = Loader.config.getBoolean("Options.WarningSystem.Reload.Use");
		warn_stop = Loader.config.getBoolean("Options.WarningSystem.Stop.Use");
		cmdblock = Loader.config.getBoolean("Options.CommandsBlocker.Use");
		code = Loader.config.getBoolean("Options.Codes.Use");
		sound = Loader.config.getBoolean("Options.Sounds.Use");
		color_chat = Loader.config.getBoolean("Options.Colors.Chat.Use");
		color_chat_perm= Loader.config.getBoolean("Options.Colors.Chat.RequiredPermission");
		color_sign = Loader.config.getBoolean("Options.Colors.Sign.Use");
		color_sign_perm= Loader.config.getBoolean("Options.Colors.Sign.RequiredPermission");
		disable_item=Loader.config.getBoolean("Options.Disable-Items.Use");
		try {
		deathspawn = DeathTp.valueOf(Loader.config.getString("Options.RespawnTeleport"));
		}catch(Exception er) {
			deathspawn = DeathTp.Spawn;
		}
		tp_safe = Loader.config.getBoolean("Options.Teleport.SafeLocation");
		tp_onreqloc = Loader.config.getBoolean("Options.Teleport.CommandSendLocation");
		join_spawn = Loader.config.getBoolean("Options.Join.TeleportToSpawn");
		join_msg = Loader.config.getBoolean("Options.Join.CustomJoinMessage");
		join_motd = Loader.config.getBoolean("Options.Join.MOTD");
		join_first_give = Loader.config.getBoolean("Options.Join.FirstJoin.GiveKit");
		join_first_percmd = Loader.config.getBoolean("Options.Join.PerformCommands.Use");
		join_first = Loader.config.getBoolean("Options.Join.FirstJoin.Use");
		leave = Loader.config.getBoolean("Options.Leave.CustomLeaveMessage");
		cool_cmd = Loader.config.getBoolean("Options.Cooldowns.Commands.Use");
		cool_percmd = Loader.config.getBoolean("Options.Cooldowns.Commands.PerCommand.Use");
		cool_chat = Loader.config.getBoolean("Options.Cooldowns.Chat.Use");
		spam_chat = Loader.config.getBoolean("Options.Security.AntiSpam.Chat");
		caps_cmd = Loader.config.getBoolean("Options.Security.AntiCaps.Commands");
		caps_chat = Loader.config.getBoolean("Options.Security.AntiCaps.Chat");
		spam_double = Loader.config.getBoolean("SpamWords.DoubledLetters.Use");
		spam_cmd = Loader.config.getBoolean("Options.Security.AntiSpam.Commands");
		swear_chat = Loader.config.getBoolean("Options.Security.AntiSwear.Chat");
		swear_cmd = Loader.config.getBoolean("Options.Security.AntiSwear.Commands");
		ad_book = Loader.config.getBoolean("Options.Security.AntiAD.Book");
		ad_chat = Loader.config.getBoolean("Options.Security.AntiAD.Chat");
		ad_cmd = Loader.config.getBoolean("Options.Security.AntiAD.Commands");
		ad_sign = Loader.config.getBoolean("Options.Security.AntiAD.Sign");
		ad_anvil = Loader.config.getBoolean("Options.Security.AntiAD.Anvil");
		ad_itemdrop = Loader.config.getBoolean("Options.Security.AntiAD.ItemDrop");
		ad_itempick = Loader.config.getBoolean("Options.Security.AntiAD.ItemPickup");
		eco_multi = Loader.config.getBoolean("Options.Economy.MultiEconomy.Use");
		eco_log = Loader.config.getBoolean("Options.Economy.Log");
		eco = Loader.config.getBoolean("Options.Economy.DisablePluginEconomy");
		eco_other = Loader.config.getBoolean("Options.Economy.CanUseOtherEconomy");
		tab = Loader.tab.getBoolean("Tab-Enabled");
		tab_header = Loader.tab.getBoolean("Header-Enabled");
		tab_footer = Loader.tab.getBoolean("Footer-Enabled");
		tab_nametag = Loader.tab.getBoolean("ModifyNameTags");
		tab_sort = Loader.tab.getBoolean("SortTabList");
		sb = Loader.scFile.getBoolean("Scoreboard-Enabled");
		sb_world = Loader.scFile.getBoolean("Scoreboard-PerWorld");
		save = Loader.mw.getBoolean("SavingTask.Enabled");
		vip = Loader.config.getBoolean("Options.VIPSlots.Use");
		vip_add = Loader.config.getBoolean("Options.VIPSlots.AddSlots");
		vip_kick = Loader.config.getBoolean("Options.VIPSlots.KickWhenFullServer");
		vip_join = Loader.config.getBoolean("Options.VIPSlots.VIPJoinBroadcast");
		motd = Loader.config.getBoolean("Options.ServerList.MOTD.Use");
		motd_maintenance = Loader.config.getBoolean("Options.ServerList.Maintenance");
		afk_auto = Loader.config.getBoolean("Options.AFK.AutoAFK");
		afk_kick = Loader.config.getBoolean("Options.AFK.AutoKick");
		lock_chat = Loader.config.getBoolean("Options.ChatLock");
		lock_server = Loader.config.getBoolean("Options.Maintenance.Enabled");
		am = Loader.config.getBoolean("Options.AutoMessage.Use");
		am_random = Loader.config.getBoolean("Options.AutoMessage.Random");
	}

public static void getManager(Player s) {
	
}
}
