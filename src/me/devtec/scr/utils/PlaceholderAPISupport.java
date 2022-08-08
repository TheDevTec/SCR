package me.devtec.scr.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.commands.info.Ping;
import me.devtec.scr.commands.server_managment.Memory;
import me.devtec.scr.commands.server_managment.TPS;
import me.devtec.scr.commands.server_managment.TPS.TPSType;
import me.devtec.shared.utility.StringUtils;
import me.devtec.shared.utility.StringUtils.FormatType;
import net.milkbowl.vault.economy.Economy;

public class PlaceholderAPISupport {

	@SuppressWarnings({ "static-access" })
	public static String replace(String text, CommandSender s, boolean placeholderAPI) {
		Placeholders placeholders = new Placeholders().c();
		
		if(s!=null && s instanceof Player) {
			Player p = (Player) s;
			placeholders.addPlayer("player", p);
			/* %money_symbol% - money symbol
			 * %money% - vault format (probably with currency symbol?)
			 * %money_raw% - is raw balance (1102100.540000614)
			 * %money_raw_formatted% - is raw balance (1102100.54)
			 * %money_formatted1% - is formatted balance v1 (1,102,100.54)
			 * %money_formatted2% - is formatted balance v2(1.1M)
			 */
			if(text.contains("%money%"))
				placeholders.replace("money", ((Economy)Loader.economy).format(((Economy)Loader.economy).getBalance(p)) );
				//text = text.replace("%money%", ((Economy)Loader.economy).format(((Economy)Loader.economy).getBalance(p)) );
			if(text.contains("%money_symbol%"))
				placeholders.replace("money_symbol", ((Economy)Loader.economy).currencyNamePlural() );
			if(text.contains("%money_raw%")|| text.contains("%money_raw_formatted%")||
					text.contains("%money_formatted1%") ||	text.contains("%money_formatted2%"))
				text = ScrEconomy.publicFormat( ((Economy)Loader.economy).getBalance(p) , text);
			/*
			 * %world%
			 * %x%
			 * %y%
			 * %z%
			 * %yaw%
			 * %pitch%
			 */
			if(text.contains("%world%"))
				 placeholders.replace("world", p.getWorld().getName());
			if(text.contains("%x%"))
				placeholders.replace("x", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getX()) );
			if(text.contains("%y%"))
				placeholders.replace("y", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getY()) );
			if(text.contains("%z%"))
				placeholders.replace("z", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getZ()) );
			if(text.contains("%yaw%"))
				placeholders.replace("yaw", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getYaw()) );
			if(text.contains("%pitch%"))
				placeholders.replace("pitch", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getPitch()) );

			/*
			 * %ping%
			 * %hp%
			 * %health%
			 * %food%
			 * %kills%
			 * %deaths%
			 * %xp%
			 * %exp%
			 * %level%
			 */
			if(text.contains("%ping%"))
				placeholders.replace("ping", Ping.pingPlayer(p));

			/*
			 * %afk%
			 * %vanish%
			 * %god%
			 * %fly%
			 */
			
			/*
			 * %online%
			 * %online_max% 
			 * %time%
			 * %date%
			 */
			
			/*
			 * %ram_free%
			 * %ram_max%
			 * %ram_used%
			 * %tps%
			 * %tps_1%
			 * %tps_5%
			 * %tps_15%
			 */
			if(text.contains("%ram_free%"))
				placeholders.add("ram_free", Memory.getFreeMemory(Loader.config.getBoolean("options.ram-percentage")));
			if(text.contains("%ram_max%"))
				placeholders.add("ram_max", Memory.getMaxMemory());
			if(text.contains("%ram_used%"))
				placeholders.add("ram_used", Memory.getUsedMemory(Loader.config.getBoolean("options.ram-percentage")));
			
			if(text.contains("%tps%"))
				placeholders.add("tps", TPS.getServerTPS());
			if(text.contains("%tps_1%"))
				placeholders.add("tps_1", TPS.getServerTPS(TPSType.ONE_MINUTE));
			if(text.contains("%tps_5%"))
				placeholders.add("tps_5", TPS.getServerTPS(TPSType.FIVE_MINUTES));
			if(text.contains("%tps_15%"))
				placeholders.add("tps_15", TPS.getServerTPS(TPSType.FIFTEEN_MINUTES));
			if(placeholderAPI)
				text = me.devtec.shared.placeholders.PlaceholderAPI.apply(
						MessageUtils.placeholder(p, text, placeholders), s instanceof Player ? ((Player) s).getUniqueId() : null);
			else
				text = MessageUtils.placeholder(p, text, placeholders);
		}
		return text;
	}

}
