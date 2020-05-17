package Commands.Economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class MultiEconomy implements CommandExecutor, TabCompleter {
	private String getEconomyGroup(String ss) {
			for(String s:Loader.config.getConfigurationSection("Options.Economy.MultiEconomy.Types").getKeys(false)) {
			if(s.toLowerCase().equalsIgnoreCase(ss)) {
				return s;
			}
			}
			return null;
		
	}
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(TheAPI.getEconomyAPI().getEconomy()==null) {
			Loader.msg(Loader.s("Prefix")+"&cMissing Vault plugin for economy.",s);
			return true;
		}
		if(API.hasPerm(s, "ServerControl.MultiEconomy")) {
			if(args.length==0) {
				Loader.Help(s, "/MultiEconomy Transfer <player> <target group> <group>", "MultiEconomy.Transfer");
				Loader.Help(s, "/MultiEconomy Money <player> <group>", "MultiEconomy.Money");
				Loader.Help(s, "/MultiEconomy Create <group>", "MultiEconomy.Create");
				Loader.Help(s, "/MultiEconomy Delete <group>", "MultiEconomy.Delete");
				Loader.Help(s, "/MultiEconomy Add <group> <world>", "MultiEconomy.Add");
				Loader.Help(s, "/MultiEconomy Remove <group> <world>", "MultiEconomy.Remove");
				Loader.Help(s, "/MultiEconomy Worlds <group>", "MultiEconomy.Worlds");
				Loader.Help(s, "/MultiEconomy Groups", "MultiEconomy.Groups");
				return true;
			}

			if(args[0].equalsIgnoreCase("Worlds")) {
				if(args.length==1) {
					Loader.Help(s, "/MultiEconomy Worlds <group>", "MultiEconomy.Worlds");
					return true;
				}
				String group = getEconomyGroup(args[1]);
				
				if(group==null) {
					Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.NotExist")
				.replace("%group%", args[1])
				.replace("%economygroup%", args[1])
				.replace("%economy-group%", args[1])
				.replace("%economy%",args[1]),s);
				return true;
				}
				ArrayList<String> worlds = new ArrayList<String>();
				for(String ss:Loader.config.getStringList("Options.Economy.MultiEconomy.Types."+getEconomyGroup(args[1]))) {
					worlds.add(ss);
				}
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.Worlds")
				.replace("%group%", getEconomyGroup(args[1]))
				.replace("%economygroup%", getEconomyGroup(args[1]))
				.replace("%economy-group%", getEconomyGroup(args[1]))
				.replace("%economy%", getEconomyGroup(args[1]))
				.replace("%worlds%", TheAPI.getStringUtils().join(worlds,", ")),s);
				return true;
				}
			if(args[0].equalsIgnoreCase("create")) {
				if(args.length==1) {
					Loader.Help(s, "/MultiEconomy Create <group>", "MultiEconomy.Create");
					return true;	
				}
				if(getEconomyGroup(args[1])!=null) {
					
					Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.AlreadyCreated")
					.replace("%group%", getEconomyGroup(args[1]))
					.replace("%economygroup%", getEconomyGroup(args[1]))
					.replace("%economy-group%", getEconomyGroup(args[1]))
					.replace("%economy%", getEconomyGroup(args[1]))
					, s);
					return true;	
				}
				Loader.config.set("Options.Economy.MultiEconomy.Types."+args[1], "");
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.Created").replace("%group%", getEconomyGroup(args[1]))
						.replace("%economygroup%", getEconomyGroup(args[1]))
						.replace("%economy-group%", getEconomyGroup(args[1]))
						.replace("%economy%", getEconomyGroup(args[1])), s);
				return true;
				
				
			}
			if(args[0].equalsIgnoreCase("delete")) {
				if(args.length==1) {
					Loader.Help(s, "/MultiEconomy Delete <group>", "MultiEconomy.Delete");
					return true;	
				}
				if(getEconomyGroup(args[1])==null) {
					Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.NotExist")
					.replace("%group%", args[1])
					.replace("%economygroup%", args[1])
							.replace("%economy-group%", args[1])
							.replace("%economy%",args[1]), s);
					return true;	
				}
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.Deleted").replace("%group%", getEconomyGroup(args[1]))
						.replace("%economygroup%", getEconomyGroup(args[1]))
						.replace("%economy-group%", getEconomyGroup(args[1]))
						.replace("%economy%", getEconomyGroup(args[1])), s);
				Loader.config.set("Options.Economy.MultiEconomy.Types."+getEconomyGroup(args[1]), null);
				return true;
				
			}
		
		if(args[0].equalsIgnoreCase("add")) {
			if(args.length==1) {
				Loader.Help(s, "/MultiEconomy Add <group> <world>", "MultiEconomy.Add");
				return true;	
			}
			if(args.length==2) {
			if(getEconomyGroup(args[1])==null) {
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.NotExist").replace("%group%", args[1])
						.replace("%economygroup%", args[1])
						.replace("%economy-group%", args[1])
						.replace("%economy%", args[1]), s);
				return true;	
			}if(getEconomyGroup(args[1])!=null) {
				Loader.Help(s, "/MultiEconomy Add <group> <world>", "MultiEconomy.Add");
				return true;	
			}}
			if(Bukkit.getWorld(args[2])==null) {
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.WorldNotExist").replace("%world%", args[2]), s);
				return true;
			}else {
			List<String> list = Loader.config.getStringList("Options.Economy.MultiEconomy.Types."+getEconomyGroup(args[1]));
			if(!list.contains(args[2])) {
			list.add(args[2]);
			Loader.config.set("Options.Economy.MultiEconomy.Types."+getEconomyGroup(args[1]), list);
			Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.WorldAdded").replace("%world%",args[2]).replace("%group%", getEconomyGroup(args[1]))
					.replace("%economygroup%", getEconomyGroup(args[1]))
					.replace("%economy-group%", getEconomyGroup(args[1]))
					.replace("%economy%", getEconomyGroup(args[1])), s);
			return true;
			}
			Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.WorldAlreadyAdded").replace("%world%",args[2]).replace("%group%", getEconomyGroup(args[1]))
					.replace("%economygroup%", getEconomyGroup(args[1]))
					.replace("%economy-group%", getEconomyGroup(args[1]))
					.replace("%economy%", getEconomyGroup(args[1])), s);
			return true;
		}}
		if(args[0].equalsIgnoreCase("remove")) {
			if(args.length==1) {
				Loader.Help(s, "/MultiEconomy Remove <group> <world>", "MultiEconomy.Remove");
				return true;	
			}
			if(args.length==2) {
			if(getEconomyGroup(args[1])==null) {
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.NotExists").replace("%group%", args[1])
						.replace("%economygroup%", args[1])
						.replace("%economy-group%", args[1])
						.replace("%economy%", args[1]), s);
				return true;	
			}if(getEconomyGroup(args[1])!=null) {
				Loader.Help(s, "/MultiEconomy Remove <group> <world>", "MultiEconomy.Remove");
				return true;	
			}}
			if(Bukkit.getWorld(args[2])==null) {
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.WorldNotExist").replace("%world%",args[2]), s);
				return true;
			}else {
			List<String> list = Loader.config.getStringList("Options.Economy.MultiEconomy.Types."+getEconomyGroup(args[1]));
			if(list.contains(args[2])) {
			list.remove(args[2]);
			Loader.config.set("Options.Economy.MultiEconomy.Types."+getEconomyGroup(args[1]), list);
			Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.WorldRemoved").replace("%group%", getEconomyGroup(args[1]))
					.replace("%economygroup%", getEconomyGroup(args[1]))
					.replace("%economy-group%", getEconomyGroup(args[1]))
					.replace("%economy%", getEconomyGroup(args[1])).replace("%world%",args[2]), s);
			return true;
			}
			Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.WorldIsNotInGroup").replace("%group%", getEconomyGroup(args[1]))
					.replace("%economygroup%", getEconomyGroup(args[1]))
					.replace("%economy-group%", getEconomyGroup(args[1]))
					.replace("%economy%", getEconomyGroup(args[1])).replace("%world%",args[2]), s);
			return true;
		}}
			if(args[0].equalsIgnoreCase("money")) {
				if(args.length==1||args.length==2) {
					Loader.Help(s, "/MultiEconomy Money <player> <group>", "MultiEconomy.Money");
					return true;
				}
				if(args.length==3) {
					if(!TheAPI.existsUser(args[1])) {
						Loader.msg(Loader.PlayerNotEx(args[1]),s);
						return true;
					}
					String group = TheAPI.getUser(args[1]).getString("Money."+getEconomyGroup(args[2]));
					if(group==null) {
						if(getEconomyGroup(args[2])!=null)
							Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.NoMoney").replace("%group%", getEconomyGroup(args[2]))
								.replace("%economygroup%", getEconomyGroup(args[2]))
								.replace("%economy-group%", getEconomyGroup(args[2]))
								.replace("%economy%", getEconomyGroup(args[2]))
								.replace("%player%", args[1])
								.replace("%playername%", args[1]), s);
						else
							Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.NotExist").replace("%group%", args[2])
								.replace("%economygroup%", args[2])
								.replace("%economy-group%", args[2])
								.replace("%economy%", args[2]),s);
						return true;
					}
					Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.HaveMoney").replace("%group%", getEconomyGroup(args[2]))
							.replace("%economygroup%", getEconomyGroup(args[2]))
							.replace("%economy-group%", getEconomyGroup(args[2]))
							.replace("%economy%", getEconomyGroup(args[2]))
							.replace("%money%", API.setMoneyFormat(TheAPI.getStringUtils().getDouble(group), true))
							.replace("%player%", args[1])
							.replace("%playername%", args[1]), s);
					return true;
			}
		}
			if(args[0].equalsIgnoreCase("groups")) {
				ArrayList<String> groups = new ArrayList<String>();
				for(String ss:Loader.config.getConfigurationSection("Options.Economy.MultiEconomy.Types").getKeys(false)) {
					groups.add(ss);
				}
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.Groups").replace("%groups%",TheAPI.getStringUtils().join(groups,", ")),s);
				return true;
				}
			if(args[0].equalsIgnoreCase("transfer")) {
				if(args.length==1) {
					Loader.Help(s, "/MultiEconomy Transfer <player> <target group> <group>", "MultiEconomy.Transfer");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if(p==null) {
					Loader.msg(Loader.PlayerNotEx(args[1]),s);
					return true;
				}
				if(p!=null && args.length==2) {
					Loader.Help(s, "/MultiEconomy Transfer <player> <target group> <group>", "MultiEconomy.Transfer");
					return true;
				}
				if(getEconomyGroup(args[2])==null) {
					Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.NotExist").replace("%group%", args[2])
							.replace("%economygroup%", args[2])
							.replace("%economy-group%", args[2])
							.replace("%economy%", args[2]),s);
					return true;
				}
				if(getEconomyGroup(args[2])!=null && args.length==3) {
					Loader.Help(s, "/MultiEconomy Transfer <player> <target group> <group>", "MultiEconomy.Transfer");
					return true;
				}
				if(getEconomyGroup(args[3])==null) {
					Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.NotExists").replace("%group%", args[3])
							.replace("%economygroup%", args[3])
							.replace("%economy-group%", args[3])
							.replace("%economy%", args[3]),s);
					return true;
				}
				double money = TheAPI.getUser(p).getDouble("Money."+getEconomyGroup(args[2]));
				double money2 = TheAPI.getUser(p).getDouble("Money."+getEconomyGroup(args[3]));
				TheAPI.getUser(p).set("Money."+getEconomyGroup(args[3]), money);
				TheAPI.getUser(p).setAndSave("Money."+getEconomyGroup(args[2]), money2);
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiEconomy.Transfer").replace("%group%", getEconomyGroup(args[2]))
						.replace("%economygroup%", getEconomyGroup(args[2]))
						.replace("%economy-group%", getEconomyGroup(args[2]))
						.replace("%economy%", getEconomyGroup(args[2]))
						.replace("%player%", args[1])
						.replace("%playername%", args[1]), s);
				return true;
				}
			}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
    	List<String> c = new ArrayList<>();
		if(s.hasPermission("ServerControl.MultiEconomy")) {
		if(args.length==1) {
	c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Money","Transfer","Create","Delete","Add","Remove","Worlds","Groups"), new ArrayList<>()));
		}
		if(args.length==2) {
			if(args[0].equalsIgnoreCase("Create")) {
	c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("?"), new ArrayList<>()));
			}
			if(args[0].equalsIgnoreCase("Delete")||args[0].equalsIgnoreCase("Add")||args[0].equalsIgnoreCase("Remove")||args[0].equalsIgnoreCase("Worlds")) {
	c.addAll(StringUtil.copyPartialMatches(args[1], Loader.config.getConfigurationSection("Options.Economy.MultiEconomy.Types").getKeys(false), new ArrayList<>()));
			}
			if(args[0].equalsIgnoreCase("Transfer")||args[0].equalsIgnoreCase("Money"))
				return null;
	}
		if(args.length==3) {
			if(args[0].equalsIgnoreCase("Add")||args[0].equalsIgnoreCase("Remove")) {
				for(String world:Loader.config.getConfigurationSection("Options.Economy.MultiEconomy.Types").getKeys(false))
				if(args[1].equalsIgnoreCase(world))
				c.addAll(StringUtil.copyPartialMatches(args[2], worlds(), new ArrayList<>()));
						}
			if(args[0].equalsIgnoreCase("Money")||args[0].equalsIgnoreCase("Transfer")) {
				Player p = TheAPI.getPlayer(args[1]);
				if(p!=null)
					c.addAll(StringUtil.copyPartialMatches(args[2], Loader.config.getConfigurationSection("Options.Economy.MultiEconomy.Types").getKeys(false), new ArrayList<>()));
			}}
		if(args.length==4) {
			if(args[0].equalsIgnoreCase("Transfer")) {
				Player p = TheAPI.getPlayer(args[1]);
				if(p!=null)
					if(Loader.config.getConfigurationSection("Options.Economy.MultiEconomy.Types").getKeys(false).contains(args[2]))
					c.addAll(StringUtil.copyPartialMatches(args[3], Loader.config.getConfigurationSection("Options.Economy.MultiEconomy.Types").getKeys(false), new ArrayList<>()));
			}}
		}
		return c;
	}
	private List<String> worlds(){
		ArrayList<String> worlds = new ArrayList<String>();
		for(World s:Bukkit.getWorlds())worlds.add(s.getName());
		return worlds;
	}

}
