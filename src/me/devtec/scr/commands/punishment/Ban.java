package me.devtec.scr.commands.punishment;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.utils.PlaceholderBuilder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

/**
 * @author StraikerinaCZ
 * 25.1 2022
 **/
public class Ban extends CommandHolder {
	
	public Ban(String command) {
		super(command, 0);
		DEFAULT_REASON = ConfigManager.punishment.getString("ban.reason");
		FORMAT_PERM = ConfigManager.punishment.get("ban.format.perm") instanceof Collection ? StringUtils.join(ConfigManager.punishment.getStringList("ban.format.perm"), "\n") : ConfigManager.punishment.getString("ban.format.perm");
		FORMAT_TEMP = ConfigManager.punishment.get("ban.format.temp") instanceof Collection ? StringUtils.join(ConfigManager.punishment.getStringList("ban.format.temp"), "\n") : ConfigManager.punishment.getString("ban.format.temp");
	}

	public static String DEFAULT_REASON, FORMAT_PERM, FORMAT_TEMP;
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		long time = 0;
		String reason = DEFAULT_REASON;
		
		if(args.length==1) { //ban NICK
			Loader.send(s, "punishment.ban", PlaceholderBuilder.make(s, "sender").add("name", args[0]).add("time", StringUtils.timeToString(time)).add("reason", reason));
			TheAPI.getPunishmentAPI().ban(args[0], time, FORMAT_PERM.replace("{reason}", reason).replace("{admin}", s.getName()));
			return;
		}

		long maybeTime = StringUtils.timeFromString(args[1]);
		if(args.length==2) { //ban NICK REASON/TIME
			if(maybeTime > 0) {
				time=maybeTime;
			}else {
				reason=args[1];
			}
			Loader.send(s, "punishment.ban", PlaceholderBuilder.make(s, "sender").add("name", args[0]).add("time", StringUtils.timeToString(time)).add("reason", reason));
			TheAPI.getPunishmentAPI().ban(args[0], time, (time != 0 ? FORMAT_TEMP.replace("{time}", StringUtils.timeToString(time)) : FORMAT_PERM).replace("{reason}", reason).replace("{admin}", s.getName()));
			return;
		}
		if(maybeTime > 0) {
			time=maybeTime;
			reason=StringUtils.buildString(2, args);
		}else {
			reason=StringUtils.buildString(1, args);
		}
		Loader.send(s, "punishment.ban", PlaceholderBuilder.make(s, "sender").add("name", args[0]).add("time", StringUtils.timeToString(time)).add("reason", reason));
		TheAPI.getPunishmentAPI().ban(args[0], time, (time != 0 ? FORMAT_TEMP.replace("{time}", StringUtils.timeToString(time)) : FORMAT_PERM).replace("{reason}", reason).replace("{admin}", s.getName()));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}