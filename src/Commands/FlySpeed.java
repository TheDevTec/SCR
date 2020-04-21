package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class FlySpeed implements CommandExecutor {
	public void speed(CommandSender s) {
		if(s instanceof Player) {
		Loader.Help(s, "/FlySpeed <number>", "FlySpeed");
		}
		if(s instanceof Player == false) {
			Loader.Help(s, "/FlySpeed <player> <number>", "FlySpeed");
		}
	}
	public boolean onCommand(CommandSender s, Command cmd, String label,String[] args) {
	if(args.length==0) {
		if(API.hasPerm(s, "ServerControl.FlySpeed")) {
			speed(s);
			return true;
		}return true;}
	if(args.length==1) {
		if(s instanceof Player == false) {speed(s);return true;}else {
			if(API.hasPerm(s, "ServerControl.FlySpeed")) {
		double flightmodifier=TheAPI.getStringUtils().getDouble(args[0]);
		if(flightmodifier>10.0)flightmodifier = 10.0;
		if(flightmodifier<-10.0)flightmodifier = -10.0;
		((Player) s).setFlySpeed((float)flightmodifier/10);
		Loader.me.set("Players."+((Player) s).getName()+".FlySpeed", flightmodifier/10);
		Loader.msg(Loader.s("Prefix")+Loader.s("Fly.FlySpeed")
		.replace("%player%", s.getName())
		.replace("%playername%", ((Player) s).getDisplayName())
		.replace("%speed%", String.valueOf(flightmodifier)),s);
		return true;
	}return true;}}
	if(args.length==2) {
		if(API.hasPerm(s, "ServerControl.FlySpeed")) {
		Player target = TheAPI.getPlayer(args[0]);
		if(target!=null) {
			double flightmodifier=TheAPI.getStringUtils().getDouble(args[1]);
			if(flightmodifier>10.0)flightmodifier = 10.0;
			if(flightmodifier<-10.0)flightmodifier = -10.0;
			target.setFlySpeed((float)flightmodifier/10);
		Loader.me.set("Players."+target.getName()+".FlySpeed", flightmodifier/10);
		Loader.msg(Loader.s("Prefix")+Loader.s("Fly.FlySpeedPlayer")
				.replace("%player%", target.getName())
				.replace("%playername%", target.getDisplayName())
				.replace("%speed%", String.valueOf(flightmodifier)),s);
		Loader.msg(Loader.s("Prefix")+Loader.s("Fly.FlySpeed")
		.replace("%player%", target.getName())
		.replace("%playername%", target.getDisplayName())
		.replace("%speed%", String.valueOf(flightmodifier)),target);
		return true;
	}
		Loader.msg(Loader.PlayerNotOnline(args[0]),s);
		return true;
		}return true;}return false;}}