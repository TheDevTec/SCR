package Commands;


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

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;
import me.Straiker123.TheAPI;

public class Mail implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String label, String[] args) {
		
			if(args.length == 0) {
				if(s.hasPermission("ServerControl.Mail.Send")) 
					Loader.Help(s, "/Mail send <player> <text>", "Mail.Send");
				if(s.hasPermission("ServerControl.Mail.Read")) { 
				Loader.Help(s, "/Mail clear", "Mail.Clear");
				Loader.Help(s, "/Mail read", "Mail.Read");}
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("Send")&&API.hasPerm(s, "ServerControl.Mail.Send")) {
					Loader.Help(s, "/Mail send <player> <text>", "Mail.Send");
					return true;
					}
				if(args[0].equalsIgnoreCase("Read")&&API.hasPerm(s, "ServerControl.Mail.Read")) {
					if(getMails(s.getName()).isEmpty()) {
						TheAPI.sendActionBar((Player) s, "&cYou do not have any mail!");
						return true;
					}
					for (String mail : getMails(s.getName())) {
                    	msg(mail,s); }
					Configs.chatme.save();
					return true;
					}
				if(args[0].equalsIgnoreCase("Clear")&&API.hasPerm(s, "ServerControl.Mail.Read")) {
					if(getMails(s.getName()).isEmpty()) {
						TheAPI.sendActionBar((Player) s, "&cYou do not have any mail!");
						return true;
					}
					removeALL(s);
					TheAPI.sendActionBar((Player) s, "&aYou cleared all mails...");
					Configs.chatme.save();
					return true;
				}
				
				
				
			}
			if(args.length > 2 && args[0].equalsIgnoreCase("Send")&&API.hasPerm(s, "ServerControl.Mail.Send")) {
				if(Loader.me.getString("Players."+args[1])==null) {
            		Loader.msg(Loader.PlayerNotEx(args[1]),s);
            		return true;
            	}
				String msg = "";
				for (int i = 2; i < args.length; ++i) {
                msg = String.valueOf(msg) + args[i] + " ";
				}
				msg=msg.substring(0,msg.length()-1);
				add(s, "&8"+s.getName()+": &8"+msg, args[1]);
				TheAPI.sendActionBar((Player) s, "&6You sended new mail to player &a"+args[1]+" &6...");
				
				if(Bukkit.getPlayer(args[1])!=null) {
					Player p = Bukkit.getPlayer(args[1]);
					int number = Loader.me.getStringList("Players."+p.getName()+".Mails").size();
					Loader.msg(Loader.s("Prefix")+Loader.s("Mail.Notification")
							.replace("%number%", ""+number), p);
				}
				return true;
			}
		return true;
	}
	 public static void add(CommandSender s, String message, String p) {
	    		List<String> a = getMails(p);
	    		a.add(message);
	    		Loader.me.set("Players."+p+".Mails", a);
	    		Configs.chatme.save();
	 }
	public static List<String> getMails(String p){
    	return Loader.me.getStringList("Players."+p+".Mails");
    }
	public static List<String> getMails(CommandSender p){
    	return Loader.me.getStringList("Players."+p+".Mails");
    }
	public static void msg(String mail, CommandSender s){
		Loader.msg(""+mail,s);
		}
	 public static void removeALL(CommandSender p) {
		 Loader.me.set("Players."+p.getName()+".Mails", null);
		 Configs.chatme.save();
	 }

		@Override
		public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
			List<String> c = new ArrayList<>();
			if(s.hasPermission("ServerControl.Mail.Read")) { // pokud má permise ...
				if(args.length==1) {
					c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Read", "Clear"), new ArrayList<>())); //pøidá tam Read a Clear
				}}
			if(s.hasPermission("ServerControl.Mail.Send")) {
				if(args.length==1) {
					c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Send"), new ArrayList<>())); // Pøidá Send
				}
				if(args[0].equalsIgnoreCase("Send") && args.length==2)
					return null; //returne online hráèe
				if(args.length>=3)return Arrays.asList("?");
			}
			return c; //returne celej ten "list" co se ti bude zobrazovat
		}
}
