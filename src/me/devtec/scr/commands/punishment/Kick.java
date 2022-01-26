package me.devtec.scr.commands.punishment;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.utils.StringUtils;

/**
 * @author StraikerinaCZ
 * 27.1 2022
 **/
public class Kick extends CommandHolder {
	
	public Kick(String command) {
		super(command, 0);
		DEFAULT_REASON = ConfigManager.punishment.getString("kick.reason");
		FORMAT = ConfigManager.punishment.get("kick.format") instanceof Collection ? StringUtils.join(ConfigManager.punishment.getStringList("kick.format"), "\n") : ConfigManager.punishment.getString("kick.format");
	}

	public static String DEFAULT_REASON, FORMAT;
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		String reason = DEFAULT_REASON;
		
		Player target;
		if((target=requireOnline(s, args[0]))==null)return;
		
		if(args.length==1) { //kick NICK
			Loader.send(s, "punishment.kick", PlaceholderBuilder.make(s, "sender").add("name", args[0]).add("reason", reason));
			target.kickPlayer(FORMAT.replace("{reason}", reason).replace("{admin}", s.getName()));
			return;
		}
		reason=StringUtils.buildString(1, args);
		Loader.send(s, "punishment.kick", PlaceholderBuilder.make(s, "sender").add("name", args[0]).add("reason", reason));
		target.kickPlayer(FORMAT.replace("{reason}", reason).replace("{admin}", s.getName()));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}