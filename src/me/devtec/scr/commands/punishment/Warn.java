package me.devtec.scr.commands.punishment;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

/**
 * @author StraikerinaCZ
 * 27.1 2022
 **/
public class Warn extends CommandHolder {
	
	public Warn(String command) {
		super(command, 0);
		DEFAULT_REASON = ConfigManager.punishment.getString("warn.reason");
		FORMAT = ConfigManager.punishment.get("warn.format") instanceof Collection ? StringUtils.join(ConfigManager.punishment.getStringList("warn.format"), "\n") : ConfigManager.punishment.getString("warn.format");
	}

	public static String DEFAULT_REASON, FORMAT;
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		String reason = DEFAULT_REASON;
		
		if(args.length==1) { //warn NICK
			Loader.send(s, "punishment.warn", PlaceholderBuilder.make(s, "sender").add("name", args[0]).add("reason", reason));
			TheAPI.getPunishmentAPI().warn(args[0], FORMAT.replace("{reason}", reason).replace("{admin}", s.getName()));
			return;
		}
		reason=StringUtils.buildString(1, args);
		Loader.send(s, "punishment.warn", PlaceholderBuilder.make(s, "sender").add("name", args[0]).add("reason", reason));
		TheAPI.getPunishmentAPI().warn(args[0], FORMAT.replace("{reason}", reason).replace("{admin}", s.getName()));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}