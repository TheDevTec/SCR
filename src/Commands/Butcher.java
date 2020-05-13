package Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Butcher implements CommandExecutor {
		
	
	public static int butcher(World a,Location w, int radius) {
		if(radius == 0) {
			int killed = 0;
			for(Entity e: a.getEntities()) {
				if(e instanceof Player == false) {
					++killed;
					e.remove();
				}}
			return killed;
		}else {
			int killed = 0;
			if(radius > 1000000)radius = 1000000;
			for(Entity e: TheAPI.getBlocksAPI().getNearbyEntities(w, radius)) {
				if(e instanceof Player == false) {
					++killed;
					e.remove();
				}}
			return killed;
		}
	}
	
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Butcher")) {
		if(args.length==0) {
			if(s instanceof Player)
			Loader.Help(s, "/Butcher <radius>", "Butcher");
			else
				Loader.Help(s, "/Butcher <world>", "Butcher");
			return true;
		}
		if(args.length==1) {
			if(s instanceof Player == false) {
				World w=Bukkit.getWorld(args[0]);
			if(w==null) {
				Loader.msg(Loader.s("Butcher.WorldIsInvalid").replace("%world%", args[0]), s);
				return true;
			}
			Loader.msg(Loader.s("Butcher.Killed").replace("%amount%", butcher(w,null,0)+""), s);
			return true;
			}
			Loader.msg(Loader.s("Butcher.Killed").replace("%amount%", butcher(((Player)s).getWorld(),((Player)s).getLocation(),TheAPI.getStringUtils().getInt(args[0]))+""), s);
			return true;
		}}
		return true;
	}
}
