package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.Configs;

public class Immune implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String label, String[] args) {
		if(s.hasPermission("ServerControl.Immune")) {
			if(args.length == 0) {
				if(!(s instanceof Player)) {
				Loader.Help(s, "/Immune <player> <true/false>", "BanSystem.Immune");
				return true;
				}
				Player p = (Player) s;

					boolean im = Loader.me.getBoolean("Players."+s.getName()+".Immune");
				if (im==false) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.Enabled"), p);
					Loader.me.set("Players."+s.getName()+".Immune", true);
					Configs.chatme.save();
					return true;
				}
					Loader.me.set("Players."+s.getName()+".Immune", false);
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.Disabled"), p);
					Configs.chatme.save();
					return true;
			}
			if(args.length==1) {
				if(s.hasPermission("ServerControl.Immune.Other")) {
				Player target = Bukkit.getPlayer(args[0]);
				Player p = (Player) s;
				boolean imt = Loader.me.getBoolean("Players."+args[0]+".Immune");
				SPlayer target2 = new SPlayer((Player)Bukkit.getServer().getPlayer(args[0]));
				if(target2.getPlayer()==null) {
					Loader.msg(Loader.PlayerNotOnline(args[0]), s);
					return true;
				}else
					if(imt==true) {
						Loader.me.set("Players."+target.getName()+".Immune", false);
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.OffOther").replace("%target%", target.getName()), p);
						Configs.chatme.save();
						return true;
					}
					if(imt==false) {
						Loader.me.set("Players."+target.getName()+".Immune", true);
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.OnOther").replace("%target%", target.getName()), p);
						Configs.chatme.save();
						return true;
					}
				}
				
				return true;
			}
		}
		return true;
	}

}
