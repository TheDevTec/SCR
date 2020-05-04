package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class UnJail implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.unJail")) {
			if(args.length==0) { 
				Loader.Help(s, "/unJail <player>", "BanSystem.unJail");
				return true;
			}
			if(args.length==1) {
				if(TheAPI.existsUser(args[0])) {
					TheAPI.getUser(args[0]).set("Jail", null);
					TheAPI.getUser(args[0]).setAndSave("TempJail", null);
					if(TheAPI.getPlayer(args[0])!=null)
						API.teleportPlayer(TheAPI.getPlayer(args[0]), TeleportLocation.SPAWN);
					Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.unJailed")
					.replace("%playername%", BanSystem.getName(args[0]))
					.replace("%player%", args[0]), s);
					TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.UnJail")
							.replace("%operator%", s.getName()).replace("%player%", args[0])
							.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.UnJail");
					
					return true;
				}
				BanSystem.notExist(s, args);
				return true;
			}}return true;}}
