package me.devtec.scr.commands.other;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.utils.PlaceholderBuilder;
import me.devtec.theapi.TheAPI;

/**
 * @author StraikerinaCZ
 * 10.1 2022
 **/
public class Uuid extends CommandHolder {
	
	public Uuid(String command) {
		super(command, -1);
	}

	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		if(args.length==0) {
			if(!(s instanceof Player)) {
				help(s, 0);
				return;
			}
			Loader.send(s, "uuid.self", PlaceholderBuilder.make(s, "sender").add("uuid", ((Player)s).getUniqueId().toString()));
			return;
		}
		if(args[0].contains("-") && args[0].length()>16) { //maybe uuid
			try {
				UUID uuid = UUID.fromString(args[0]);
				String name = TheAPI.getCache().lookupNameById(uuid);
				Loader.send(s, "uuid.other", PlaceholderBuilder.make(s, "sender").add("name", name).add("uuid", uuid.toString()));
				return;
			}catch(Exception notUUID) {
				//continue
			}
		}
		String name = TheAPI.getCache().lookupName(args[0]);
		Loader.send(s, "uuid.other", PlaceholderBuilder.make(s, "sender").add("name", name).add("uuid", TheAPI.getCache().lookupId(name).toString()));
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=1?placeholder_FIRST:null;
	}
	
}