package me.devtec.servercontrolreloaded.utils;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.SoundAPI;
import me.devtec.theapi.configapi.Config;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class setting {
	public static enum DeathTp {
		HOME, BED, SPAWN
	}

	public static SimpleDateFormat format_date_time, format_time, format_date;
	public static boolean warn_reload, warn_stop, warn_restart, singeplayersleep, color_chat, color_sign, sound,
			cmdblock, disable_item, vip, vip_kick, vip_add, vip_join, motd, motd_maintenance, lock_chat, lock_server,
			afk_auto, afk_kick, code, sb, am, am_random, timezone, save, tab_header, tab_footer, tab_nametag,
			tab_sort, tab, eco, eco_other, eco_log, eco_multi, ad_book, ad_chat, ad_cmd, ad_sign, ad_anvil, ad_itemdrop,
			ad_itempick, cool_cmd, cool_chat, cool_percmd, color_chat_perm,
			color_sign_perm, tp_safe, tp_onreqloc, ram, spam_double, caps_chat, caps_cmd, deathspawnbol
			,list, staff_hide, staff_replace, farming, farming_packet, tab_vanish, tab_move, vanish_action,
			pm, helpop,tab_name;
	public static String prefix;
	public static DeathTp deathspawn;

	/**
	 * public static boolean offlineinvsee; ///invsee <offline player>, /esee
	 * <offline player> (Req. SCR_PerWorldInventory)
	 **/
	public static void load(boolean message) {
		Config f = Loader.config;
		prefix = Loader.getTranslation("Prefix")+"";
		if(message)
		TheAPI.msg(setting.prefix + " &7Loading settings..", TheAPI.getConsole());
		format_date_time = new SimpleDateFormat(f.getString("Format.DateWithTime"));
		format_time = new SimpleDateFormat(f.getString("Format.Time"));
		format_date = new SimpleDateFormat(f.getString("Format.Date"));
		list=f.getBoolean("Options.List-RemoveEmptyLine");
		staff_hide=f.getBoolean("Options.Staff.HideAllLines");
		staff_replace=f.getBoolean("Options.Staff.UseReplace");
		helpop=f.getBoolean("Options.LockChat.Helpop");
		pm=f.getBoolean("Options.LockChat.PrivateMessage");
		farming = f.getBoolean("Options.FarmingSystem.Use");
		vanish_action = f.getBoolean("Options.Vanish-ActionBar");
		singeplayersleep = f.getBoolean("Options.SinglePlayerSleep");
		timezone = f.getBoolean("Options.TimeZone.Use");
		ram = f.getBoolean("Options.RAM-Percentage");
		warn_restart = f.getBoolean("Options.WarningSystem.Restart.Use");
		warn_reload = f.getBoolean("Options.WarningSystem.Reload.Use");
		warn_stop = f.getBoolean("Options.WarningSystem.Stop.Use");
		cmdblock = f.getBoolean("Options.CommandsBlocker.Use");
		code = f.getBoolean("Options.Codes.Use");
		sound = f.getBoolean("Options.Sounds.Use");
		color_chat = f.getBoolean("Options.Colors.Chat.Use");
		color_chat_perm = f.getBoolean("Options.Colors.Chat.Required-Permission");
		color_sign = f.getBoolean("Options.Colors.Sign.Use");
		color_sign_perm = f.getBoolean("Options.Colors.Sign.Required-Permission");
		disable_item = f.getBoolean("Options.Disable-Items.Use");
		deathspawnbol = f.getBoolean("Options.Use-RespawnTeleport");
		if(deathspawnbol)
		try {
			deathspawn = DeathTp.valueOf(f.getString("Options.RespawnTeleport").toUpperCase());
		} catch (Exception er) {
			deathspawn = DeathTp.SPAWN;
		}
		tp_safe = f.getBoolean("Options.Teleport.SafeLocation");
		tp_onreqloc = f.getBoolean("Options.Teleport.CommandSendLocation");
		cool_cmd = f.getBoolean("Options.Cooldowns.Commands.Use");
		cool_percmd = f.getBoolean("Options.Cooldowns.Commands.PerCommand.Use");
		cool_chat = f.getBoolean("Options.Cooldowns.Chat.Use");
		spam_double = f.getBoolean("SpamWords.DoubledLetters.Use");
		caps_chat = f.getBoolean("Options.Security.AntiCaps.Chat");
		caps_cmd = f.getBoolean("Options.Security.AntiCaps.Commands");
		ad_book = f.getBoolean("Options.Security.AntiAD.Book");
		ad_chat = f.getBoolean("Options.Security.AntiAD.Chat");
		ad_cmd = f.getBoolean("Options.Security.AntiAD.Commands");
		ad_sign = f.getBoolean("Options.Security.AntiAD.Sign");
		ad_anvil = f.getBoolean("Options.Security.AntiAD.Anvil");
		ad_itemdrop = f.getBoolean("Options.Security.AntiAD.ItemDrop");
		ad_itempick = f.getBoolean("Options.Security.AntiAD.ItemPickup");
		eco_multi = f.getBoolean("Options.Economy.MultiEconomy.Use");
		eco_log = f.getBoolean("Options.Economy.Log");
		eco = f.getBoolean("Options.Economy.DisablePluginEconomy");
		eco_other = f.getBoolean("Options.Economy.CanUseOtherEconomy");
		tab = Loader.tab.getBoolean("Options.Enabled");
		if(tab) {
			tab_header = Loader.tab.getBoolean("Options.Modify.Header");
			tab_footer = Loader.tab.getBoolean("Options.Modify.Footer");
			tab_nametag = Loader.tab.getBoolean("Options.Modify.NameTags");
			tab_name = Loader.tab.getBoolean("Options.Modify.TabListName");
			tab_sort = Loader.tab.getBoolean("Options.Sorting");
			if(TheAPI.isNewerThan(7)) {
				tab_vanish=Loader.tab.getBoolean("Options.VanishToSpectator");
				tab_move=Loader.tab.getBoolean("Options.MoveSpectator");
			}
		}else {
			tab_header = false;
			tab_footer = false;
			tab_name=false;
			tab_nametag = false;
			tab_sort = false;
			tab_vanish = false;
			tab_move = false;
		}
		sb = Loader.sb.getBoolean("Options.Enabled");
		save = Loader.mw.getBoolean("SavingTask.Enabled");
		vip = f.getBoolean("Options.VIPSlots.Use");
		vip_add = f.getBoolean("Options.VIPSlots.AddSlots");
		vip_kick = f.getBoolean("Options.VIPSlots.KickWhenFullServer");
		vip_join = f.getBoolean("Options.VIPSlots.VIPJoinBroadcast");
		motd = f.getBoolean("Options.ServerList.MOTD.Use");
		motd_maintenance = f.getBoolean("Options.ServerList.MOTD.Maintenance");
		afk_auto = f.getBoolean("Options.AFK.AutoAFK");
		afk_kick = f.getBoolean("Options.AFK.AutoKick");
		lock_chat = f.getBoolean("Options.ChatLock");
		lock_server = f.getBoolean("Options.Maintenance.Enabled");
		am = f.getBoolean("Options.AutoMessage.Use");
		am_random = f.getBoolean("Options.AutoMessage.Random");
		if(message)
		if (sound && !SoundAPI.existSound(Loader.config.getString("Options.Sounds.Sound")))
			TheAPI.msg(prefix + " &4ERROR: &7Sound &e"+Loader.config.getString("Options.Sounds.Sound")+" &7doesn't exist", TheAPI.getConsole());
		try {
			if (timezone)
				TimeZone.setDefault(TimeZone.getTimeZone(f.getString("Options.TimeZone.Zone")));
		} catch (Exception e) {
			if(message) {
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &4ERROR: &7Invalid time zone: &c" + f.getString("Options.TimeZone.Zone"), TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &4ERROR: &7List of available time zones:", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &4ERROR:  &ehttps://greenwichmeantime.com/time-zone/", TheAPI.getConsole());
			TheAPI.msg(setting.prefix + " &8*********************************************", TheAPI.getConsole());
		}}
		if(message)
		TheAPI.msg(setting.prefix + " &7Settings loaded", TheAPI.getConsole());
	}
}
