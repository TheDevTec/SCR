package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import me.Straiker123.TheAPI;

public class Give implements CommandExecutor, TabCompleter {
	public List<String> items(){
		ArrayList<String> list = new ArrayList<String>();
		for(Material ss:Material.values()) {
			if(ss.isOccluding() && ss != Material.AIR)
			list.add(ss.name());
		}
		return list;
	}
	public String getItem(String s) {
		if(Material.matchMaterial(s) != null && Material.matchMaterial(s).isOccluding() && Material.matchMaterial(s) != Material.AIR)
		return Material.matchMaterial(s).name();
		return null;
	}
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Give")) {
			if(args.length==0) {
				Loader.Help(s, "/Give <player> <item> <amount>", "Give");
				return true;
			}
				if(args.length==1) {
					Player ps = Bukkit.getPlayer(args[0]);
					if(ps==null) {

						if(s instanceof Player==false) {
							Loader.Help(s, "/Give <player> <item> <amount>", "Give");
							return true;
						}
					if(getItem(args[0])!=null) {
						if(API.hasPerm(s, "ServerControl."+getItem(args[0]))||API.hasPerm(s, "ServerControl.Give.*")) {
							Player p = (Player)s;
							try {
							TheAPI.giveItem(p, Material.matchMaterial(args[0]),1);
							Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Give.Given"),p).replace("%amount%", "1").replace("%item%", getItem(args[0])), s);
						return true;
							}catch(Exception e) {
								Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[0]), s);
								return true;
							}
						}return true;
					}
					Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[0]), s);
						return true;
					}
						Loader.Help(s, "/Give <player> <item> <amount>", "Give");
					return true;
					}
				if(args.length==2) {
					Player ps = Bukkit.getPlayer(args[0]);
					if(ps==null) {
						if(args[0].equals("*")) {
							Repeat.a(s,"give * "+args[1]);
							return true;
						}
						if(getItem(args[0])!=null) {
							ps=(Player)s;
							try {
							TheAPI.giveItem(ps, new ItemStack(Material.matchMaterial(args[0]),TheAPI.getNumbersAPI(args[1]).getInt()));
							Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Give.Given"),ps).replace("%amount%",TheAPI.getNumbersAPI(args[1]).getInt()+"").replace("%item%", getItem(args[0])), s)
							;return true;
							
						}catch(Exception e) {
							Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[0]), s);
							return true;
						}
						}
						if(s instanceof Player ==false)
							Loader.msg(Loader.PlayerNotOnline(args[1]), s);
						else
							Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[0]), s);
							return true;
						
					}
					if(getItem(args[1])!=null) {
						TheAPI.giveItem(ps, new ItemStack(Material.matchMaterial(args[1])));
						Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Give.Given"),ps).replace("%item%", getItem(args[1])).replace("%amount%", "1"), s);
						return true;
						}
						Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[1]), s);
						return true;
				}
				if(args.length==3) {
					Player ps = Bukkit.getPlayer(args[0]);
					if(ps!=null) {
						if(args[0].equals("*")) {
							Repeat.a(s,"give * "+args[1]+" "+args[2]);
							return true;
						}
						if(getItem(args[1])!=null) {
							TheAPI.giveItem(ps, new ItemStack(Material.matchMaterial(args[1]),TheAPI.getNumbersAPI(args[2]).getInt()));
							Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Give.Given"),ps).replace("%item%", getItem(args[1])).replace("%amount%", TheAPI.getNumbersAPI(args[2]).getInt()+""), s);
							return true;
							}
							Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[1]), s);
							return true;			
					}
					Loader.msg(Loader.PlayerNotOnline(args[1]), s);
					return true;
				}
				
	}return true;
	}
	  @Override
	  public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
	  	List<String> c = new ArrayList<>();
	      	if(s.hasPermission("ServerControl.Give")) {
	    	  	if(args.length==1)return null;
	    	  	if(args.length==2) {
		      		c.addAll(StringUtil.copyPartialMatches(args[1], items(), new ArrayList<>()));
	    	  		}
	    	  	if(args.length==3) {
		      		c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("?"), new ArrayList<>()));
	    	  	}
	    	  	}
	      return c;}
}
