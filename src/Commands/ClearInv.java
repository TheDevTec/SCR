package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;
import me.Straiker123.TheAPI;

public class ClearInv implements CommandExecutor, TabCompleter {

	public String f(String string) {
		return Loader.s(string);
	}
	public String value(int i) {
	return String.valueOf(i);
}
	public HashMap<CommandSender, ItemStack[]> undo = new HashMap<CommandSender, ItemStack[]>();
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		String fs = f("Prefix");

		if(s instanceof Player == false) {
			if(args.length==0) {
				Loader.msg(fs+"&e----------------- &bClear &e-----------------",s);

				Loader.msg("",s);
				Loader.Help(s, "/ClearInv <player>", "ClearInv");
	    	    return true;
			}
	    		if(args.length==1) {
	    		Player target = Bukkit.getServer().getPlayer(args[0]);
	            		if(target == null) {
	            			Loader.msg(fs+f("ClearInventory.PlayerNotOnline").replace("%player%", args[0]),s);
	    	    	    return true;
	        		}
	            		Loader.msg(fs+f("ClearInventory.PlayerInvCleared").replace("%player%", target.getName()),s);
		        			undo.put(target, target.getInventory().getContents());
		        			Loader.msg(fs+f("ClearInventory.InvCleared"),target);
	            			target.getInventory().clear();
		    	    	    return true;
			}
        		Loader.msg(Loader.s("Prefix")+Loader.s("UknownCommand"),s);
	     		return true;
		}else {
			Player p = (Player)s;
    		int take = Loader.config.getInt("Options.Cost-ClearInvUndo");
    		
    		if(args.length==0) {
    			if(API.hasPerm(s, "ServerControl.ClearInv.Clear")) {
    			if(Loader.me.getBoolean("Players."+s.getName()+".ClearInvConfirm")==false) {
    				Loader.me.set("Players."+p.getName()+".ClearInvCooldown",System.currentTimeMillis()/1000);
    				Configs.chatme.save();
    				Loader.msg(fs+"&e----------------- &bClear &e-----------------",s);
    				Loader.msg("",s);
    				Loader.msg(fs +f("ClearInventory.ConfirmClearInv"),s);
	    	    return true;
			}
    			if(Loader.me.getBoolean("Players."+s.getName()+".ClearInvConfirm")==true) {
    				if(Loader.me.getString("Players."+p.getName()+".ClearInvCooldown")!=null)
    					Loader.me.set("Players."+p.getName()+".ClearInvCooldown",null);
    				Configs.chatme.save();undo.put(p, p.getInventory().getContents());
    				Loader.msg(fs+"&e----------------- &bClear &e-----------------",s);
    	        		Loader.msg("",s);
    	        		Loader.msg(fs+f("ClearInventory.InvCleared"),s);
			p.getInventory().clear();
            return true;
    		}}return true;}
        	if(args[0].equalsIgnoreCase("Other")) {
    			if(API.hasPerm(s, "ServerControl.ClearInv.Other")) {
    				if(args.length ==1) {
        				Loader.msg(fs+"&e----------------- &bClear &e-----------------",s);
    					Loader.msg("",s);
    					Loader.msg(fs + f("ClearInventory.Usage"),s);
    		    	    return true;
    				}

    				if(args.length ==2) {
    	    		Player target = Bukkit.getServer().getPlayer(args[1]);
    	            		if(target == null) {
    	            			Loader.msg(Loader.PlayerNotOnline(args[1]),s);
    	    	    	    return true;
    	        		}
    	            	if(target != null) {
    	            		Loader.msg(fs+f("ClearInventory.PlayerInvCleared").replace("%player%", target.getName()),s);
    		        			undo.put(target, target.getInventory().getContents());
    		        			Loader.msg(fs+f("ClearInventory.InvCleared"),s);
    	            			target.getInventory().clear();
    		    	    	    return true;
    	            	}}}return true;}
			if(Loader.me.getBoolean("Players."+s.getName()+".ClearInvConfirm")==false) {
    		if(args[0].equalsIgnoreCase("Confirm")) {
    				
    			if(API.hasPerm(s, "ServerControl.ClearInv.Clear")) {
	        		long reset = Loader.me.getLong("Players."+s.getName()+".ClearInvCooldown")-System.currentTimeMillis()/1000;
	        		reset = reset*-1;
	        		if(reset <60) {
	        			undo.put(p, p.getInventory().getContents());
	    				Loader.msg(fs+"&e----------------- &bConfirm &e-----------------",s);

    	        		Loader.msg("",s);
    	        		Loader.msg(fs+f("ClearInventory.InvCleared"),s);
    				Loader.me.set("Players."+p.getName()+".ClearInvCooldown",null);
    				Configs.chatme.save();p.getInventory().clear();
    	    	    return true;
    			}
    				Loader.msg(fs+"&e----------------- &bConfirm &e-----------------",s);
	        		Loader.msg("",s);
	        		Loader.msg(fs +f("ClearInventory.NoConfirm"),s);
        	   	    return true;
    			}return true;}}
        	if(args[0].equalsIgnoreCase("Clear")) {
        		if(API.hasPerm(s, "ServerControl.ClearInv.Clear")) {
        			if(Loader.me.getBoolean("Players."+s.getName()+".ClearInvConfirm")==false) {
        				Loader.me.set("Players."+p.getName()+".ClearInvCooldown",System.currentTimeMillis()/1000);
        				Configs.chatme.save();
        				Loader.msg(fs+"&e----------------- &bClear &e-----------------",s);
        				Loader.msg("",s);
        				Loader.msg(fs +f("ClearInventory.ConfirmClearInv"),s);
    	    	    return true;
    			}
        			if(Loader.me.getBoolean("Players."+s.getName()+".ClearInvConfirm")==true) {
        				if(Loader.me.getString("Players."+p.getName()+".ClearInvCooldown")!=null)
        					Loader.me.set("Players."+p.getName()+".ClearInvCooldown",null);
        				Configs.chatme.save();undo.put(p, p.getInventory().getContents());
        				Loader.msg(fs+"&e----------------- &bClear &e-----------------",s);
        	        		Loader.msg("",s);
        	        		Loader.msg(fs+f("ClearInventory.InvCleared"),s);
    			p.getInventory().clear();
                return true;
        		}}}
    	        	
    	        	if(args[0].equalsIgnoreCase("Undo")) {
	        			if(API.hasPerm(s, "ServerControl.ClearInv.Undo")) {
	    	        	if(undo.containsKey(p)) {
	    	        		
						if(take == 0||Loader.econ==null) {
							for(ItemStack item : undo.get(p)) {
						        TheAPI.giveItem(p, item);}
							undo.remove(p);
	        				Loader.msg(fs+"&e----------------- &bUndo &e-----------------",s);
							Loader.msg("",s);
							Loader.msg(fs +f("ClearInventory.InventoryRetrievedForFree"),s);
							
							return true;
						}else
						if(take != 0&&Loader.econ!=null) {
							
						if(Loader.econ.has(p,take)) {
								for(ItemStack item : undo.get(p)) {
							        TheAPI.giveItem(p, item);}
								Loader.econ.withdrawPlayer(p, take);
								undo.remove(p);
		        				Loader.msg(fs+"&e----------------- &bUndo &e-----------------",s);
								Loader.msg("",s);
								Loader.msg(fs +f("ClearInventory.InventoryRetrievedForMoney")
								.replace("%money%", value(take)),s);
								return true;
						}else
							if(!Loader.econ.has(p,take)) {
		        				Loader.msg(fs+"&e----------------- &bUndo &e-----------------",s);
								Loader.msg("",s);
								Loader.msg(fs +f("ClearInventory.NoMoney").replace("%money%", value(take)),s);
							    return true;
								
							}}
						
	    	        	}else
			    	        	if(!undo.containsKey(p)) {
			        				Loader.msg(fs+"&e----------------- &bUndo &e-----------------",s);
			    	        		Loader.msg("",s);
			    	        		Loader.msg(fs +f("ClearInventory.NoInventoryRetrieved").replace("%money%", value(take)),s);
									return true;
			    	        		
			    	        	}}return true;}
    	        		if(args[0].equalsIgnoreCase("Help")) {
    	        			if(API.hasPerm(s, "ServerControl.ClearInv.Help")) {
    	        				Loader.msg(fs + "&e----------------- &bHelp &e-----------------",s);
    	        				Loader.msg("",s);
    	        				Loader.Help(s, "/Clear","ClearInv.Clear");
    	        				Loader.Help(s, "/Clear Clear","ClearInv.Clear");
    	        				Loader.Help(s, "/Clear Help","ClearInv.Help");
    	        				Loader.Help(s, "/Clear Other <player>","ClearInv.Other");
    	        				if(Loader.me.getBoolean("Players."+s.getName()+".ClearInvConfirm")==false) {
    	        				Loader.Help(s, "/Clear Confirm","ClearInv.Confirm");
    	        				}
    	        				Loader.Help(s, "/Clear Undo","ClearInv.Undo");
	    	    	    return true;
    	        			}return true;}
    	        		Loader.msg(Loader.s("Prefix")+Loader.s("UknownCommand"),s);
    	    	     		return true;
	}
	}
	  @Override
	    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
	    	List<String> c = new ArrayList<>();
	        	if(s.hasPermission("ServerControl.ClearInv")) {

	    			if(Loader.me.getBoolean("Players."+s.getName()+".ClearInvConfirm")==false) {
	                c.addAll(StringUtil.copyPartialMatches(args[0],  Arrays.asList("Clear", "Confirm"), new ArrayList<>()));
	    			}
	                c.addAll(StringUtil.copyPartialMatches(args[0],  Arrays.asList("Clear"), new ArrayList<>()));
	    			
	            }
	        	if(s.hasPermission("ServerControl.Help")) {
	        		c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Help"), new ArrayList<>()));
	            }
	        	if(s.hasPermission("ServerControl.Undo")) {
	        		c.addAll(StringUtil.copyPartialMatches(args[0],  Arrays.asList("Undo"), new ArrayList<>()));
	            }
	        	if(s.hasPermission("ServerControl.Other")) {
	        		c.addAll(StringUtil.copyPartialMatches(args[0],  Arrays.asList("Other"), new ArrayList<>()));
	            }
	    	if(args[0].equalsIgnoreCase("Other") && args.length==2) {
	    		return null;
	    	}
	        return c;
	    }
}