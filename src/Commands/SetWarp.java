package Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

public class SetWarp implements CommandExecutor {

	public void warp(CommandSender s) {
        Loader.Help(s,"/SetWarp <warp> <true/false>","Warp.SetWarp");
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if(API.hasPerm(s, "ServerControl.SetWarp")) {
			if(args.length==0) {
				if(s instanceof Player) {
					warp(s);
					
					return true;
				}else {
					Loader.msg(Loader.s("ConsoleErrorMessage"),s);
				return true;
				}
			}
			if(args[0]!=null && args[1]==null) {
			if(s instanceof Player) {
				if(Loader.config.getString("Warps."+args[0])==null) {
				Player p = (Player)s;
				Location local = p.getLocation();
		
		Loader.config.set("Warps."+args[0]+".World", ((Player)s).getWorld().getName());
		Loader.config.set("Warps."+args[0]+".X", local.getX());
		Loader.config.set("Warps."+args[0]+".Y", local.getY());
		Loader.config.set("Warps."+args[0]+".Z", local.getZ());
		Loader.config.set("Warps."+args[0]+".X_Pos_Head", local.getYaw());
		Loader.config.set("Warps."+args[0]+".Z_Pos_Head", local.getPitch());
		Loader.config.set("Warps."+args[0]+".NeedPermission", false);
		Configs.config.save();
		Loader.msg(Loader.s("Warp.Created")
					.replace("%warp%", args[0])
					.replace("%world%", Loader.config.getString("Warps."+args[0]+".World"))
					.replace("%player%", s.getName())
					.replace("%prefix%", Loader.s("Prefix"))
					.replace("%playername%", ((Player)s).getDisplayName()), s);
			return true;
			}else {
				Loader.msg(Loader.s("Warp.AlreadyExists")
						.replace("%player%", s.getName())
						.replace("%playername%", ((Player)s).getDisplayName())
						.replace("%warp%", args[0]), s);
				return true;
			}
				}else {
					Loader.msg(Loader.s("ConsoleErrorMessage"),s);
				return true;}
			}
			if(args[0]!=null && args[1].equalsIgnoreCase("false")) {
				if(s instanceof Player) {
					if(Loader.config.getString("Warps."+args[0])==null) {
					Player p = (Player)s;
					Location local = p.getLocation();
			
			Loader.config.set("Warps."+args[0]+".World", ((Player)s).getWorld().getName());
			Loader.config.set("Warps."+args[0]+".X", local.getX());
			Loader.config.set("Warps."+args[0]+".Y", local.getY());
			Loader.config.set("Warps."+args[0]+".Z", local.getZ());
			Loader.config.set("Warps."+args[0]+".X_Pos_Head", local.getYaw());
			Loader.config.set("Warps."+args[0]+".Z_Pos_Head", local.getPitch());
			Loader.config.set("Warps."+args[0]+".NeedPermission", false);
			Configs.config.save();
			Loader.msg(Loader.s("Prefix")+Loader.s("Warp.Created")
						.replace("%warp%", args[0])
						.replace("%world%", Loader.config.getString("Warps."+args[0]+".World"))
						.replace("%player%", s.getName())
						.replace("%prefix%", Loader.s("Prefix"))
						.replace("%playername%", ((Player)s).getDisplayName())
						.replace("%permission%", "ServerControl.Warp."+args[0]), s);
				return true;
				}else {
					
					Loader.msg(Loader.s("Prefix")+Loader.s("Warp.AlreadyExists")
							.replace("%player%", s.getName())
							.replace("%playername%", ((Player)s).getDisplayName())
							.replace("%warp%", args[0]), s);
					return true;
				}
					}else {
						Loader.msg(Loader.s("Prefix")+Loader.s("ConsoleErrorMessage"),s);
					return true;}
			}
			if(args[0]!=null && args[1].equalsIgnoreCase("true")) {
				if(s instanceof Player) {
					if(Loader.config.getString("Warps."+args[0])==null) {
					Player p = (Player)s;
					Location local = p.getLocation();
			
			Loader.config.set("Warps."+args[0]+".World", ((Player)s).getWorld().getName());
			Loader.config.set("Warps."+args[0]+".X", local.getX());
			Loader.config.set("Warps."+args[0]+".Y", local.getY());
			Loader.config.set("Warps."+args[0]+".Z", local.getZ());
			Loader.config.set("Warps."+args[0]+".X_Pos_Head", local.getYaw());
			Loader.config.set("Warps."+args[0]+".Z_Pos_Head", local.getPitch());
			Loader.config.set("Warps."+args[0]+".NeedPermission", true);
			Configs.config.save();
			Loader.msg(Loader.s(Loader.s("Prefix")+"Warp.CreatedWithPerm")
						.replace("%warp%", args[0])
						.replace("%world%", Loader.config.getString("Warps."+args[0]+".World"))
						.replace("%player%", s.getName())
						.replace("%prefix%", Loader.s("Prefix"))
						.replace("%playername%", ((Player)s).getDisplayName())
						.replace("%permission%", "ServerControl.Warp."+args[0]), s);
				return true;
				}else {
					Loader.msg(Loader.s("Prefix")+Loader.s("Warp.AlreadyExists")
							.replace("%player%", s.getName())
							.replace("%playername%", ((Player)s).getDisplayName())
							.replace("%warp%", args[0]), s);
					return true;
				}
					}else {
						Loader.msg(Loader.s("Prefix")+Loader.s("ConsoleErrorMessage"),s);
					return true;}
			}
		}
		return true;
	}
}