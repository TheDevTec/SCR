package Commands.Economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import Commands.BanSystem.BanSystem;
import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import me.Straiker123.TheAPI;

public class Eco implements CommandExecutor, TabCompleter {

	public void give(CommandSender s) {
		Loader.Help(s,"/Eco give <player> <money>","Economy.Give");
	}
	public void take(CommandSender s) {
		Loader.Help(s,"/Eco take <player> <money>","Economy.Take");
	}
	public void reset(CommandSender s) {
		Loader.Help(s,"/Eco reset <player>","Economy.Reset");
	}
	public void pay(CommandSender s) {
		Loader.Help(s,"/Eco pay <player> <money>","Economy.Pay");
	}
	public void bal(CommandSender s) {
		Loader.Help(s,"/Eco <player>","Economy.Balance");
	}
	public void set(CommandSender s) {
		Loader.Help(s,"/Eco set <player> <money>","Economy.Set");
	}

	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.econ==null) {
			Loader.msg(Loader.s("Prefix")+"&cMissing Vault plugin for economy.",s);
			return true;
		}

		if(args.length==0) {
			if(s instanceof Player) {
			if(API.hasPerm(s, "ServerControl.Balance")) {
				Player p = (Player)s;
				Loader.msg(Loader.s("Economy.Balance")
						.replace("%money%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p.getName()), true))
						.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p.getName()), true))
						.replace("%prefix%", Loader.s("Prefix"))
						.replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName()), s);
				return true;
			}return true;
			}
			Loader.msg(Loader.s("Prefix")+"&e----------------- &bHelp &e-----------------",s);
			Loader.msg("",s);
			bal(s);
			give(s);
			take(s);
			set(s);
			reset(s);
			return true;
			}

		if(args[0].equalsIgnoreCase("Help")) {
			Loader.msg(Loader.s("Prefix")+"&e----------------- &bHelp &e-----------------",s);
			Loader.msg("",s);
		if(s instanceof Player)
			if(s.hasPermission("ServerControl.Pay"))
		pay(s);
		if(s.hasPermission("ServerControl.Balance.Other"))
		bal(s);
			
			if(s.hasPermission("ServerControl.Economy.Give")) {
				give(s);
			}
			if(s.hasPermission("ServerControl.Economy.Take")) {
				take(s);
			}
			if(s.hasPermission("ServerControl.Economy.Reset")) {
				reset(s);
			}
			if(s.hasPermission("ServerControl.Economy.Set")) {
				set(s);
			}return true;}
		if(Loader.me.getString("Players."+args[0])!=null) {
			if(API.hasPerm(s, "ServerControl.Balance.Other")) {

				String world = Bukkit.getWorlds().get(0).getName();
				if(Bukkit.getPlayer(s.getName())!=null)world=((Player) s).getWorld().getName();
				
				Loader.msg(Loader.s("Economy.BalanceOther")
						.replace("%money%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[0],world), true))
						.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[0],world), true))
						.replace("%prefix%", Loader.s("Prefix"))
						.replace("%player%", args[0])
						.replace("%playername%", BanSystem.getName(args[0])), s);
				return true;
			}return true;}
		if(args[0].equalsIgnoreCase("Give")) {
			if(API.hasPerm(s, "ServerControl.Economy.Give")) {
				if(args.length==1||args.length==2) {
					give(s);
					return true;
				}
				if(args.length==3) {
					if(args[1].contains("*")) {
						Repeat.a(s,"eco give * "+API.convertMoney(args[2]));
						return true;
					}
					String t = Loader.me.getString("Players."+args[1]);
					if(t!=null) {
					double given = API.convertMoney(args[2]);
					TheAPI.getEconomyAPI().depositPlayer(args[1], given);
					Loader.msg(Loader.s("Economy.GivenToPlayer")
							.replace("%money%",API.setMoneyFormat(given, true))
							.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", args[1])
							.replace("%playername%", BanSystem.getName(args[1])), s);
					if(Bukkit.getPlayer(args[1])!=null) {
						Loader.msg(Loader.s("Economy.Given")
							.replace("%money%",API.setMoneyFormat(given, true))
							.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", args[1])
							.replace("%playername%", BanSystem.getName(args[1])), get(args[1]));
					return true;
				}return true;}

					Loader.msg(Loader.PlayerNotEx(args[1]), s);
					return true;
					
					}}return true;}
		if(args[0].equalsIgnoreCase("Take")) {
			if(API.hasPerm(s, "ServerControl.Economy.Take")) {
				if(args.length==1||args.length==2) {
					take(s);
					return true;
				}
				if(args.length==3) {
					if(args[1].contains("*")) {
						Repeat.a(s,"eco take * "+API.convertMoney(args[2]));
						return true;
					}
					String t = Loader.me.getString("Players."+args[1]);
					if(t!=null) {
					double taken = API.convertMoney(args[2]);
					TheAPI.getEconomyAPI().withdrawPlayer(args[1], taken);
					Loader.msg(Loader.s("Economy.TakenFromPlayer")
							.replace("%money%",API.setMoneyFormat(taken,true))
							.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", args[1])
							.replace("%playername%", BanSystem.getName(args[1])), s);

						if(Bukkit.getPlayer(args[1])!=null) {
							Loader.msg(Loader.s("Economy.Taken")
							.replace("%money%",API.setMoneyFormat(taken,true))
							.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", args[1])
							.replace("%playername%", BanSystem.getName(args[1])), get(args[1]));
					return true;
				}return true;}
					Loader.msg(Loader.PlayerNotEx(args[1]), s);
					return true;
					}}return true;}
		if(args[0].equalsIgnoreCase("Pay")) {
			if(API.hasPerm(s, "ServerControl.Pay")) {
				if(s instanceof Player) {
					Player p = (Player)s;
					if(args.length==1||args.length==2) {
					pay(s);
					return true;
				}
				if(args.length==3) {
					String t = Loader.me.getString("Players."+args[1]);
					if(t!=null) {
						String moneyfromargs = args[2];
						if(moneyfromargs.startsWith("-"))moneyfromargs="0.0";
						double money = API.convertMoney(args[2]);
						if(TheAPI.getEconomyAPI().has(p.getName(), money)||s.hasPermission("ServerControl.Economy.InMinus")){
							TheAPI.getEconomyAPI().withdrawPlayer(p.getName(), money);
							TheAPI.getEconomyAPI().depositPlayer(args[1], money);
							Loader.msg(Loader.s("Economy.PaidTo")
							.replace("%money%",API.setMoneyFormat(money,true))
							.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(s.getName()), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", args[1])
							.replace("%playername%", BanSystem.getName(args[1])), s);
					if(get(args[1])!=null && get(args[1]).getWorld().getName().equals(Bukkit.getPlayer(s.getName()).getWorld().getName())) {
						Loader.msg(Loader.s("Economy.PaidFrom")
							.replace("%money%",API.setMoneyFormat(money,true))
							.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(s.getName()), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", s.getName())
							.replace("%playername%", ((Player) s).getDisplayName()), get(args[1]));
					}return true;
				}
						Loader.msg(Loader.s("Economy.NoMoney")
								.replace("%money%",API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(s.getName()), true))
								.replace("%currently%",API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(s.getName()), true))
								.replace("%player%", args[1])
								.replace("%playername%", args[1]), s);
						return true;}
					Loader.msg(Loader.PlayerNotEx(args[1]), s);
					return true;
					}}
				Loader.msg(Loader.s("Prefix")+Loader.s("UknownCommand"),s);
				return true;
			}return true;}
		if(args[0].equalsIgnoreCase("Reset")) {
			if(API.hasPerm(s, "ServerControl.Economy.Reset")) {
				if(args.length==1) {
					reset(s);
					return true;
				}
				if(args.length==2) {
					String t = Loader.me.getString("Players."+args[1]);
					if(t!=null) {
						reset(args[1], Loader.config.getDouble("Economy.DefaultMoney"));
						Loader.msg(Loader.s("Economy.ResetedPlayer")
							.replace("%money%",  API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%currently%",API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", args[1])
							.replace("%playername%",BanSystem.getName(args[1])), s);
					if(Bukkit.getPlayer(args[1])!=null) {
						Loader.msg(Loader.s("Economy.Reseted")
							.replace("%money%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", args[1])
							.replace("%playername%", BanSystem.getName(args[1])), get(args[1]));
					return true;
				}return true;}
					Loader.msg(Loader.PlayerNotEx(args[1]), s);
					return true;
					}}return true;}
		if(args[0].equalsIgnoreCase("Set")) {
			if(API.hasPerm(s, "ServerControl.Economy.Set")) {
				if(args.length==1||args.length==2) {
					set(s);
					return true;
				}
				if(args.length==3) {
					String t = Loader.me.getString("Players."+args[1]);
					if(t!=null) {
						reset(args[1], API.convertMoney(args[2]));
						Loader.msg(Loader.s("Economy.SetPlayer")
							.replace("%money%",  API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%currently%",API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", args[1])
							.replace("%playername%", BanSystem.getName(args[1])), s);
					if(Bukkit.getPlayer(args[1])!=null) {
						Loader.msg(Loader.s("Economy.Set")
							.replace("%money%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[1]), true))
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%player%", args[1])
							.replace("%playername%", BanSystem.getName(args[1])), get(args[1]));
					return true;
				}return true;}
					Loader.msg(Loader.PlayerNotEx(args[1]), s);
					return true;
					}}return true;}
		if(Loader.me.getString("Players."+args[0])==null
				&& !args[0].equalsIgnoreCase("Reset")
				&& !args[0].equalsIgnoreCase("Take")
				&& !args[0].equalsIgnoreCase("Pay")
				&& !args[0].equalsIgnoreCase("Set")
			&& !args[0].equalsIgnoreCase("Give")) {
			Loader.msg(Loader.s("Prefix")+Loader.s("UknownCommand"),s);
			return true;
		}
		return false;
	}
	@SuppressWarnings("deprecation")
	private void reset(String i, double money) {
		TheAPI.getEconomyAPI().withdrawPlayer(i, Loader.econ.getBalance(i));
		TheAPI.getEconomyAPI().depositPlayer(i, money);
	}

	private Player get(String args) {
		if(Bukkit.getPlayer(args) != null) {
			return Bukkit.getPlayer(args);
		}
		return null;
	}
	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
    	List<String> c = new ArrayList<>();
    	List<String> Pay = Arrays.asList("Pay");
    	List<String> Give = Arrays.asList("Give");
    	List<String> Take = Arrays.asList("Take");
    	List<String> Reset = Arrays.asList("Reset");
    	List<String> Set = Arrays.asList("Set");
    	List<String> numbers = Arrays.asList("?");
        	if(args.length == 1) {
            if(sender.hasPermission("ServerControl.Pay")) {
            	c.addAll(StringUtil.copyPartialMatches(args[0], Pay, new ArrayList<>()));
        	          
                  }
            if(sender.hasPermission("ServerControl.Economy.Take")) {
            	c.addAll(StringUtil.copyPartialMatches(args[0], Take, new ArrayList<>()));
        	          
                  }
            if(sender.hasPermission("ServerControl.Economy.Reset")) {
            	c.addAll(StringUtil.copyPartialMatches(args[0], Reset, new ArrayList<>()));
        	          
                  }
            if(sender.hasPermission("ServerControl.Economy.Give")) {
            	c.addAll(StringUtil.copyPartialMatches(args[0], Give, new ArrayList<>()));
        	          
                  }
            if(sender.hasPermission("ServerControl.Economy.Set")) {
            	c.addAll(StringUtil.copyPartialMatches(args[0], Set, new ArrayList<>()));
        	          
                  }}
            if(args[0].equalsIgnoreCase("Pay")) {
                if(sender.hasPermission("ServerControl.Pay")) {
                	if(args.length==2)
                	return null;
                	if(args.length==3)
                    	c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
                }
            }
            if(args[0].equalsIgnoreCase("Take")) {
                if(sender.hasPermission("ServerControl.Economy.Take")) {
                	if(args.length==2)
                	return null;
                	if(args.length==3)
                    	c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
                }
            }
            if(args[0].equalsIgnoreCase("Reset")) {
                if(sender.hasPermission("ServerControl.Economy.Reset")) {
                	if(args.length==2)
                	return null;
                	if(args.length==3)
                    	c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
                }
            }
            if(args[0].equalsIgnoreCase("Set")) {
                if(sender.hasPermission("ServerControl.Economy.Set")) {
                	if(args.length==2)
                	return null;
                	if(args.length==3)
                    	c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
                }
            }
            if(args[0].equalsIgnoreCase("Give")) {
            if(sender.hasPermission("ServerControl.Economy.Give")) {
                	if(args.length==2)
                	return null;
                	if(args.length==3)
                    	c.addAll(StringUtil.copyPartialMatches(args[2], numbers, new ArrayList<>()));
                }
            }
        return c;
    }

}
