package Commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Warp implements CommandExecutor, TabCompleter {

	public String warp(String[] args) {
		if(Loader.config.getString("Warps")!=null)
		for(String s:Loader.config.getConfigurationSection("Warps").getKeys(false)) {
		if(s.toLowerCase().equalsIgnoreCase(args[0])) {
			return s;
		}
		}
		return null;
	}
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if(API.hasPerm(s, "ServerControl.Warp")) {
			if(Loader.config.getString("Warps")!=null) {
			if(args.length==0) {
				Loader.msg(Loader.s("Prefix")+Loader.s("Warp.List")//c.addAll(StringUtil.copyPartialMatches(args[0], warpss(s), new ArrayList<>()));
								.replace("%warps%", StringUtils.join(warpss(s), ", ")), s);
								//.replace("%warps%", StringUtils.join(Loader.config.getConfigurationSection("Warps").getKeys(false), ", "))
				Loader.msg(Loader.s("Warp.List")
								.replace("%warps%", StringUtils.join(Loader.config.getConfigurationSection("Warps").getKeys(false), ", "))
								.replace("%player%", s.getName())
								.replace("%prefix%", Loader.s("Prefix"))
								, s);
						return true;
					}
			if(args.length==1) {
			if(warp(args)!=null) { 
		 }
		if(s instanceof Player) {
		if(warp(args)!=null) {
			float x_head = Loader.config.getInt("Warps."+warp(args)+".X_Pos_Head");
			float z_head = Loader.config.getInt("Warps."+warp(args)+".Z_Pos_Head");
			 Location loc = new Location(Bukkit.getWorld(Loader.config.getString("Warps."+warp(args)+".World")), Loader.config.getDouble("Warps."+warp(args)+".X"), Loader.config.getDouble("Warps."+warp(args)+".Y") ,Loader.config.getDouble("Warps."+warp(args)+".Z"), x_head, z_head);
			 if(loc == null || loc.getWorld()==null) {
				Loader.msg(Loader.s("Warp.CantGetLocation")
						.replace("%warp%", warp(args))
						.replace("%world%", "-")
						.replace("%player%", s.getName())
						.replace("%playername%", ((Player)s).getDisplayName())
						.replace("%prefix%", Loader.s("Prefix"))
						, s);
				return true;
			}
			 boolean needperm = Loader.config.getBoolean("Warps."+warp(args)+".NeedPermission");
			 if(needperm == true) {
				 if(API.hasPerm(s, "ServerControl.Warp."+warp(args))) {
					 API.setBack(((Player) s));
						TheAPI.getPlayerAPI((Player)s).teleport(loc);
						Loader.msg(Loader.s("Warp.Warping")
									.replace("%warp%",warp(args))
									.replace("%world%", loc.getWorld().getName())
									.replace("%player%", s.getName())
									.replace("%playername%", ((Player)s).getDisplayName())
									.replace("%prefix%", Loader.s("Prefix"))
									, s);
							return true;
				 }
				 return true;
			 }
				API.setBack(((Player) s));
		TheAPI.getPlayerAPI((Player)s).teleport(loc);
		Loader.msg(Loader.s("Warp.Warping")
					.replace("%warp%",warp(args))
					.replace("%world%", loc.getWorld().getName())
					.replace("%player%", s.getName())
					.replace("%playername%", ((Player)s).getDisplayName())
					.replace("%prefix%", Loader.s("Prefix"))
					, s);
			return true;
			}
			Loader.msg(Loader.s("Warp.NotExists")
					.replace("%warp%", args[0])
					.replace("%player%", s.getName())
					.replace("%playername%", ((Player)s).getDisplayName())
					.replace("%prefix%", Loader.s("Prefix"))
					, s);
			return true;}else {
				Loader.msg(Loader.s("ConsoleErrorMessage"),s);
			return true;
			}}
			if(args.length==2) {
				Player p = Bukkit.getPlayer(args[1]);
				if(p==null) {
					Loader.msg(Loader.PlayerNotOnline(args[1]),s);
					return true;
				}else {
						if(warp(args)!=null) {
							float x_head = Loader.config.getInt("Warps."+warp(args)+".X_Pos_Head");
							float z_head = Loader.config.getInt("Warps."+warp(args)+".Z_Pos_Head");
							Location loc = new Location(Bukkit.getWorld(Loader.config.getString("Warps."+warp(args)+".World")), Loader.config.getDouble("Warps."+warp(args)+".X"), Loader.config.getDouble("Warps."+warp(args)+".Y") ,Loader.config.getDouble("Warps."+warp(args)+".Z"), x_head, z_head);
							if(loc == null || loc.getWorld()==null) {
									Loader.msg(Loader.s("Warp.CantGetLocation")
										.replace("%warp%", warp(args))
										.replace("%world%", "-")
										.replace("%player%", s.getName())
										.replace("%playername%", player(s))
										.replace("%prefix%", Loader.s("Prefix"))
										, s);
									return true;
								}
								API.setBack(p);
						TheAPI.getPlayerAPI(p).teleport(loc);
						Loader.msg(Loader.s("Warp.Warping")
								.replace("%warp%",warp(args))
								.replace("%world%", loc.getWorld().getName())
								.replace("%player%", p.getName())
								.replace("%playername%", p.getDisplayName())
								.replace("%prefix%", Loader.s("Prefix"))
								, p);
						Loader.msg(Loader.s("Warp.PlayerWarped")
								.replace("%warp%",warp(args))
								.replace("%world%", loc.getWorld().getName())
								.replace("%player%", p.getName())
								.replace("%playername%", p.getDisplayName())
								.replace("%prefix%", Loader.s("Prefix"))
								, s);
							return true;
							}
					Loader.msg(Loader.s("Warp.NotExists")
									.replace("%warp%", args[0])
									.replace("%player%", s.getName())
									.replace("%playername%", player(s))
									.replace("%prefix%", Loader.s("Prefix"))
									, s);
							return true;
				}}
			Loader.msg(Loader.s("Warp.NoWarps")
						.replace("%prefix%", Loader.s("Prefix")),s);
				return true;
				}}
		Loader.msg(Loader.s("Warp.NoWarps")
					.replace("%prefix%", Loader.s("Prefix")),s);
			return true;}
	public String player(CommandSender s) {
		if(Bukkit.getPlayer(s.getName())!=null)return Bukkit.getPlayer(s.getName()).getDisplayName();
		return s.getName();
	}
	/* String needperm = Loader.config.getString("Warps."+warp(args)+".NeedPermission");
	 if(needperm.equalsIgnoreCase("true")) {*/
	public List<String> warpss(CommandSender s){ 
		List<String> w = new ArrayList<>();
		if(Loader.config.getString("Warps")!=null)
			for(String st : Loader.config.getConfigurationSection("Warps").getKeys(false)) {
				boolean needperm = Loader.config.getBoolean("Warps."+st+".NeedPermission");
				String needperm2 = Loader.config.getString("Warps."+st+".NeedPermission");
				if(s.hasPermission("ServerControl.Warp."+st) && needperm== true) {
					w.add(st);
				}
				if(needperm==false||needperm2==null) {
					w.add(st);
				}}
			return w;
	}
    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
    	List<String> c = new ArrayList<>();
    	if(args.length==1) {
        	if(s.hasPermission("ServerControl.Warp")) {
        		c.addAll(StringUtil.copyPartialMatches(args[0], warpss(s), new ArrayList<>()));
            }
    	}
        return c;
}}