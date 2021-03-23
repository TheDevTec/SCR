package me.DevTec.ServerControlReloaded.Modules.Mirror;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;

public class MirrorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		if(args.length==0) {
			Loader.Help(s, "Mirror", "Other");
			Loader.sendMessages(s, "Mirror.Types", Placeholder.c().add("%types%", "AxisX, AxisZ, Center"));
			return true;
		}
		if(args.length==1) {
			if(args[0].equalsIgnoreCase("none")) {
				MirrorManager.remove( ((Player)s) );
				TheAPI.msg("Debug: Vyresetováno ", s);
				return true;
			}
			MirrorManager.add( ((Player)s), args[0]);
			TheAPI.msg("Debug: Nastaveno na "+args[0], s);
			return true;
		}
		return true;
	}
}
