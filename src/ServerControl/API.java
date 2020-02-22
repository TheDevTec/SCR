package ServerControl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import Commands.Kit;
import ServerControlEvents.PluginHookEvent;
import Utils.Configs;
import Utils.setting;
import me.Straiker123.TheAPI;

public class API {
	protected static Loader plugin = Loader.getInstance;
	public static String MoneyFormat;
	
	 public static boolean existVaultPlugin() {
		 return TheAPI.getPluginsManagerAPI().getPlugin("Vault") !=null;
	 }
	 public static enum TeleportLocation{
		 HOME,
		 BED,
		 SPAWN
	 }
	 public static void teleportPlayer(Player p, TeleportLocation location) {
		 switch(location) {
		 case BED:{
			 if(p.getBedSpawnLocation()!=null) {
					if(setting.tp_safe) TheAPI.getPlayerAPI(p).safeTeleport(p.getBedSpawnLocation());
					else TheAPI.getPlayerAPI(p).teleport(p.getBedSpawnLocation());
			 }else
				teleportPlayer(p,TeleportLocation.SPAWN);
			 }
			 break;
		 case HOME:{
			 String home = null;
				List<String> homes = new ArrayList<String>();
				if(Loader.me.getString("Players."+p.getName()+".Homes")!=null)
					for(String s :Loader.me.getConfigurationSection("Players."+p.getName()+".Homes").getKeys(false))homes.add(s);
				if(homes.isEmpty()==false) {
					home=homes.get(0);
				}
				if(home != null) {
				World w = Bukkit.getWorld(Loader.me.getString("Players."+p.getName()+".Homes."+home+".World"));
				double x = Loader.me.getDouble("Players."+p.getName()+".Homes."+home+".X");
				double y = Loader.me.getDouble("Players."+p.getName()+".Homes."+home+".Y");
				double z = Loader.me.getDouble("Players."+p.getName()+".Homes."+home+".Z");
				float pitch = Loader.me.getInt("Players."+p.getName()+".Homes."+home+".Pitch");
				float yaw = Loader.me.getInt("Players."+p.getName()+".Homes."+home+".Yaw");
				if(w != null) { 
				Location loc = new Location(w,x,y,z,yaw,pitch);
					Bukkit.getScheduler().scheduleSyncDelayedTask(Loader.getInstance, new Runnable() {
					public void run() {
						if(setting.tp_safe)
							TheAPI.getPlayerAPI(p).safeTeleport(loc);
						else
							TheAPI.getPlayerAPI(p).teleport(loc);
							}}, 1);
				}else {
					teleportPlayer(p,TeleportLocation.SPAWN);
				Loader.msg(Loader.s("Spawn.NoHomesTeleportedToSpawn")
						.replace("%world%", ((Player)p).getWorld().getName())
						.replace("%player%", p.getName())
						.replace("%playername%", ((Player)p).getDisplayName())
						, p);
				}}else {
						teleportPlayer(p,TeleportLocation.SPAWN);
						Loader.msg(Loader.s("Spawn.NoHomesTeleportedToSpawn")
								.replace("%world%", ((Player)p).getWorld().getName())
								.replace("%player%", p.getName())
								.replace("%playername%", ((Player)p).getDisplayName())
								, p);
				}
		 }break;
		 case SPAWN:{
			 World world = Bukkit.getWorlds().get(0);;
				Location loc=Bukkit.getWorlds().get(0).getSpawnLocation();
				if(Loader.config.getString("Spawn")!=null) {
					float x_head = Loader.config.getInt("Spawn.X_Pos_Head");
					float z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
					 world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
					 if(world != null)
					 loc = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y") ,Loader.config.getDouble("Spawn.Z"), x_head, z_head);
				}
				Location l = loc;
				/*Location block = loc.add(0,0,0);
				Location loc1 = loc.add(0,+1,0);
				Location loc2 = loc.add(0,+2,0); 
				Bukkit.broadcast("OriginalLocation: "+loc, "");
				Bukkit.broadcast("Block: "+block, "");
				Bukkit.broadcast("loc1: "+loc1, "");
				Bukkit.broadcast("loc2: "+loc2, "");*/
							Bukkit.getScheduler().scheduleSyncDelayedTask(Loader.getInstance, new Runnable() {
								public void run() {
									
									/*if(setting.tp_safe) {
										TheAPI.getPlayerAPI(p).safeTeleportToSpawn(l,TeleportCause.PLUGIN);
									}else {*/
										TheAPI.getPlayerAPI(p).teleport(l);
								}
							//}
				}, 1);
		 }break;
		 }
	 }
	 
		/**
		 * Replace player placeholdes (%player% and %playername%)
		 */
	 public static String replacePlayerName(String where, Player player) {
		 String playername = player.getDisplayName();
		 String playerr = player.getName();
		 return where.replace("%player%", playerr).replace("%playername%", playername);
	 }
		/**
		 * Replace player placeholdes (%player% and %playername%)
		 */
	 public static String replacePlayerName(String where, String player) {
		 String playername = player;
		 if(Bukkit.getPlayer(player)!=null)playername=Bukkit.getPlayer(player).getDisplayName();
		 String playerr = player;
		 return where.replace("%player%", playerr).replace("%playername%", playername);
	 }
		public static double convertMoney(String s) {
			double a = TheAPI.getNumbersAPI(s).getDouble();
			double mille = a*1000;
			double million = mille*1000;
			double billion = million*1000;
			double trillion = billion*1000;
			double quadrillion = trillion*1000;
			if(s.endsWith("k"))a=a*1000;
			if(s.endsWith("m"))a=million;
			if(s.endsWith("b"))a=billion;
			if(s.endsWith("t"))a=trillion;
			if(s.endsWith("q"))a=quadrillion;
			return a;
		}
	 
		/**
		 * Hook addon to /Addons command
		 */
	 public static enum SeenType{
		 Online,
		 Offline;
	 }
	 public static String getSeen(String player, SeenType type) {
		 String a = "0s";
		 switch(type) {
		 case Online:
			 if(Loader.me.getString("Players."+player+".JoinTime")!=null)
			 a=TheAPI.getTimeConventorAPI().setTimeToString((Loader.me.getLong("Players."+player+".JoinTime") - System.currentTimeMillis()/1000)*-1);
			 break;
		 case Offline:
			 if(Loader.me.getString("Players."+player+".LeaveTime")!=null)
			 a=TheAPI.getTimeConventorAPI().setTimeToString((Loader.me.getLong("Players."+player+".LeaveTime") - System.currentTimeMillis()/1000)*-1);
			 break;
		 }
		 return a;
	 }
	 
	 public static void hookAddon(Plugin plugin) {
		PluginHookEvent event = new PluginHookEvent(plugin);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			TheAPI.getConsole().sendMessage(TheAPI.colorize(Loader.s("Prefix")+"&aHooked addon &0'&6"+plugin.getName()+"&0' &ato the plugin."));
			event.Hook();
		}
	 }
	 public static void TeleportBack(Player p) {
				Location loc = getBack(p.getName());
				if(loc != null) {
				setBack(p);
				p.teleport(loc);
				Loader.msg(Loader.s("Back.Teleporting")
						.replace("%prefix%", Loader.s("Prefix"))
						.replace("%playername%", p.getDisplayName())
						.replace("%player%", p.getName()),p);
				}else
				Loader.msg(Loader.s("Back.CantGetLocation"),p);
	 }

	 public static void setBack(Player p) {
		 setBack(p.getName(),p.getLocation());
	 }
	 public static void setBack(String p, Location l) {
		 if(l != null) {
		 Loader.me.set("Players."+p+".Back.World",l.getWorld().getName());
		 Loader.me.set("Players."+p+".Back.X",l.getX());
		 Loader.me.set("Players."+p+".Back.Y",l.getY());
		 Loader.me.set("Players."+p+".Back.Z",l.getZ());
		 Loader.me.set("Players."+p+".Back.Yaw",l.getPitch());
		 Loader.me.set("Players."+p+".Back.Pitch",l.getYaw());
		 Configs.chatme.save();
		 }
	 }
	 
	 public static Location getBack(String p) {
		 if(Bukkit.getWorld(Loader.me.getString("Players."+p+".Back.World")) != null) {
			 return new Location(Bukkit.getWorld(Loader.me.getString("Players."+p+".Back.World")),
					 Loader.me.getDouble("Players."+p+".Back.X"),Loader.me.getDouble("Players."+p+".Back.Y"),Loader.me.getDouble("Players."+p+".Back.Z")
					 ,Loader.me.getLong("Players."+p+".Back.Pitch"),Loader.me.getLong("Players."+p+".Back.Yaw"));
		 }
		 return null;
	 }
	 private static boolean has(CommandSender p, List<String> perms) {
		 int i = 0;
		 for(String s:perms)if(p.hasPermission(s))++i;
		 return i==perms.size()-1;
	
	 }
	 public static boolean hasPerms(CommandSender p, List<String> perms) {
		 if(has(p,perms)) {return true;}
			p.sendMessage(TheAPI.colorize(Loader.s("NotPermissionsMessage")
					.replace("%player%", p.getName())
					.replace("%playername%", p.getName())
					.replace("%permission%", StringUtils.join(perms,", "))));
			return false;
	 }
	 public static boolean hasPerm(CommandSender s, String permission) {
		 if(s.hasPermission(permission)) {return true;}
			s.sendMessage(TheAPI.colorize(Loader.s("NotPermissionsMessage")
					.replace("%player%", s.getName())
					.replace("%playername%", s.getName())
					.replace("%permission%", permission)));
			return false;
	 }
	 public static void setDisplayName(Player p, String name) {
		 p.setDisplayName(TheAPI.colorize(name));
	 }
	 
	 public static void setCustomName(Player p, String name) {
		p.setCustomName(TheAPI.colorize(name));
	 }
	 
	 public static String getIPAdress(String player) {
		 return Loader.me.getString("Players."+player+".IPAdress").replace('_', '.');
	 }
	 public static ArrayList<String> getKits() {
		 ArrayList<String> list = new ArrayList<String>();
		for(String name: Loader.kit.getConfigurationSection("Kits").getKeys(false)) {
			list.add(name);
		}
		return list;
	 }
	 public static void giveKit(String player, String KitName) {
		 if(getKitFromString(KitName)!=null)
		Kit.giveKit(player, KitName, false, false);
	 }
	 public static void giveKit(String player, String KitName, boolean cooldown, boolean economy) {
		 if(getKitFromString(KitName)!=null)
		Kit.giveKit(player, KitName, cooldown, economy);
	 }

		 public static String getKitFromString(String string) {
					return Kit.getKitName(string);
			 }
		 public static boolean isKit(String string) {
			 if(getKitFromString(string)!=null)
				return true;
			 return false;
			 }

		 /**
		  * API where you can set ban/mute.. and get information about ban/mute..
		  * @return BanSystemAPI
		  */
		 public static BanSystemAPI getBanSystemAPI() {
			 return new BanSystemAPI();
		 }
		 
	public static String setMoneyFormat(double money, boolean colorized) {
		String a = "0";
		 if(MoneyFormat==null) {
			 if(existVaultPlugin()) {
			 if(Loader.econ!=null) {
				 DecimalFormat df = null;
				 String get = null;
				 double mille = money/1000;
				 double million = mille/1000;
				 double billion = million/1000;
				 double trillion = billion/1000;
				 double quadrillion = trillion/1000;
				 
				 df= new DecimalFormat("#,##0.00");
				 get = df.format(money);
					if(get.length()>=8 && get.length()<12) {
						 df = new DecimalFormat("#,###0.00k");
						 df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
							return df.format(mille).replaceAll("\\.00", "");
					}
					if(get.length()>=12 && get.length()<16) {
						 df = new DecimalFormat("#,###0.00m");
						 df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
							return df.format(million).replaceAll("\\.00", "");
					}
					if(get.length()>=16 && get.length()<20) {
						 df = new DecimalFormat("#,###0.00b");
						 df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
							return df.format(billion).replaceAll("\\.00", "");
					}
					if(get.length()>=20 && get.length()<24) {
						 df = new DecimalFormat("#,###0.00t");
						 df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
							return df.format(trillion).replaceAll("\\.00", "");
					}
					if(get.length()>=24 && get.length()<28) {
						 df = new DecimalFormat("#,###0.00q");
						 df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
							return df.format(quadrillion).replaceAll("\\.00", "");
					}
					if(get.length()>=28) {
						if(String.valueOf(quadrillion).startsWith("-"))
							return "-∞";
						return "∞";
					}
				 df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
				a= df.format(money).replaceAll("\\.00", "");
			 }
		 }
		 }
		 if(MoneyFormat!=null) {
			 if(Loader.econ!=null) {
				a= MoneyFormat;
			 }
		 }
		 if(colorized) {
			 if(a.startsWith("-"))return ChatColor.RED+a;
				if(!a.startsWith("-")&&a.equals("0"))return ChatColor.YELLOW+a;
				return ChatColor.GREEN+a;
		 }else
		 return a;
	 }

	 public static boolean isAFK(Player p) {
		 return new SPlayer(p).isAFK();
	 }
	 public static String getMoneyFormat() {
		 if(MoneyFormat!=null) {
				return MoneyFormat;
		 }
		 return null;
	 }
	 public static boolean getVulgarWord(String string) {
		 List<String> words = Loader.config.getStringList("SwearWords");
		 for(String word:words)
		 if(string.toLowerCase().contains(word.toLowerCase())) {
				return true;
		 }
		 return false;
	 }
	 public static String getValueOfVulgarWord(String string) {
		 List<String> words = Loader.config.getStringList("SwearWords");
		 for(String word:words)
		 if(string.toLowerCase().contains(word.toLowerCase())) {
				return String.valueOf(word);
		 }
		 return "";
	 }
	 public static boolean getSpamWord(String string) {
		 List<String> words = Loader.config.getStringList("SpamWords.Words");
		 for(String word:words)
		 if(string.toLowerCase().contains(word.toLowerCase())) {
				return true;
		 }
		 return false;
	 }
	 public static String getValueOfSpamWord(String string) {
		 List<String> words = Loader.config.getStringList("SpamWords.Words");
		 for(String word:words)
		 if(string.toLowerCase().contains(word.toLowerCase())) {
				return String.valueOf(word);
		 }
		 return "";
	 }
	 private static boolean checkForDomain(String str) {
	    	for(String w:Loader.config.getStringList("AntiAD.WhiteList")) {
		    	str = str.replace(w, "").replaceAll("[0-9]+", "").replace(" ",".");
		    }
	        Matcher m = Pattern.compile("[-a-zA-Z0-9@:%_\\+~#?&//=]{5,256}\\.(com|ru|net|org|jp|uk|br|fr|nl|cn|ir|es|cz|biz|ca|kr|eu|ua|gr|ro|tw|vn|mx|ch|tr|hu|tv|ar|us|sk|fi|id|cl|nz|pt)\\b(\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?").matcher(str.toLowerCase());
	        return m.find();
	}
	    private static boolean checkForIp(String str) {
	    	for(String w:Loader.config.getStringList("AntiAD.WhiteList")) {
		    	str = str.replace(w, "").replaceAll("[A-Za-z]+", "").replace(" ",".");
		    }
	        Matcher m = Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b").matcher(str.toLowerCase());
	   	     return m.find();
	    	}
	    
	    public static List<String> getAdvertisementMatches(String where){
	    	List<String> matches = new ArrayList<String>();
	    	if(getAdvertisement(where)) {
		        Matcher m = Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b").matcher(where.toLowerCase());
		        while(m.find()) {
		        	for(int i = 0; i < m.groupCount(); ++i) {
         		    String match = m.group(i);
         		    matches.add(match);
		        	}
    		}
		        Matcher ma = Pattern.compile("[-a-zA-Z0-9@:%_\\+~#?&//=]{5,256}\\.(com|ru|net|org|jp|uk|br|fr|nl|cn|ir|es|cz|biz|ca|kr|eu|ua|gr|ro|tw|vn|mx|ch|tr|hu|tv|ar|us|sk|fi|id|cl|nz|pt)\\b(\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?").matcher(where.toLowerCase());
		        while(ma.find()) {
		        	for(int i = 0; i < ma.groupCount(); ++i) {
         		    String match = ma.group(i);
         		    matches.add(match);
		        	}
    		}
	    	}
	    	return matches;
	    	}
	    

	 public static boolean getAdvertisement(String string) {
		 if(checkForIp(string)||checkForDomain(string)) {
				return true;
		 }
		 return false;
	 }
	 public static boolean getBlockedCommand(String string) {
		 List<String> words = Loader.config.getStringList("Options.CommandsBlocker.List");
		 for(String word:words) 
		 if(string.toLowerCase().contains(word.toLowerCase())) {
				return true;
		 }
		 return false;
	 }
	 public static String getServerIP() {
		 String ip= null;
		    try {
		      BufferedReader is = new BufferedReader(new FileReader("server.properties"));
		      Properties props = new Properties();
		      props.load(is);
		      is.close();
		      ip = props.getProperty("server-ip");
		    } catch (IOException ed) {
		      ed.printStackTrace();
		    }

		    if (ip != null) {
		    	return ip;
		    }
		    return "UKNOWN";
		 }
	 public static String getServerPort() {
		 String port= null;
		    try {
		      BufferedReader is = new BufferedReader(new FileReader("server.properties"));
		      Properties props = new Properties();
		      props.load(is);
		      is.close();
		      port = props.getProperty("server-port");
		    } catch (IOException ed) {
		      ed.printStackTrace();
		    }

		    if (port != null) {
		    	return port;
		    }
		    return "UKNOWN";
		 }
	 public static String getServerName() {
		 String server_name= null;
		    try {
		      BufferedReader is = new BufferedReader(new FileReader("server.properties"));
		      Properties props = new Properties();
		      props.load(is);
		      is.close();
		      server_name = props.getProperty("server-name");
		    } catch (IOException ed) {
		      ed.printStackTrace();
		    }

		    if (server_name!= null) {
		    	return server_name;
		    }
		    return "UKNOWN";
		 }}
