package ServerControl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Commands.Other.Kit;
import ServerControl.Loader.Placeholder;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.PluginManagerAPI;
import me.DevTec.TheAPI.BlocksAPI.BlockGetter;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.User;
import me.DevTec.TheAPI.Utils.NMS.NMSAPI;

public class API {
	protected static Loader plugin = Loader.getInstance;

	public static boolean existVaultPlugin() {
		return PluginManagerAPI.getPlugin("Vault") != null;
	}

	public static SPlayer getSPlayer(Player p) {
		if(!cache.containsKey(p.getName())) {
			cache.put(p.getName(), new SPlayer(p));
		}
		return cache.get(p.getName());
	}

	public static List<SPlayer> getSPlayers() {
		List<SPlayer> s = new ArrayList<>();
		for(Player p : TheAPI.getOnlinePlayers())s.add(getSPlayer(p));
		return s;
	}

	public static enum TeleportLocation {
		HOME, BED, SPAWN
	}

	public static Location getTeleportLocation(Player p, TeleportLocation l) {
		Location a = null;
		switch (l) {
		case BED: {
			if (p.getBedSpawnLocation() != null) {
				a = p.getBedSpawnLocation();
			} else
				a = getTeleportLocation(p, TeleportLocation.HOME);
		}
			break;
		case HOME: {
			String home = null;
			User d = TheAPI.getUser(p);
			List<String> homes = new ArrayList<String>();
			for (String s : d.getKeys("Homes"))
				homes.add(s);
			if (homes.isEmpty() == false) {
				home = homes.get(0);
			}
			if (home != null) {
				return StringUtils.getLocationFromString(d.getString("Homes." + home));
			} else {
				Loader.sendMessages(p, "Home.TpSpawn");
				a = getTeleportLocation(p, TeleportLocation.SPAWN);
			}
		}
			break;
		case SPAWN: {
			World world = Bukkit.getWorlds().get(0);
			Location loc = world.getSpawnLocation();
			if (Loader.config.exists("Spawn")
					&& Bukkit.getWorld(Loader.config.getString("Spawn.World")) != null) {
				float x_head = Loader.config.getInt("Spawn.X_Pos_Head");
				float z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
				world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
				loc = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y"),
						Loader.config.getDouble("Spawn.Z"), x_head, z_head);
			}
			a = loc;
		}
			break;
		}
		return a;
	}

	public static void teleportPlayer(Player p, TeleportLocation location) {
		Location a = getTeleportLocation(p, location);
		if (a != null)
			if (setting.tp_safe)
				safeTeleport(p,a);
			else
				p.teleport(a);

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
		if (TheAPI.getPlayer(player) != null)
			playername = TheAPI.getPlayer(player).getDisplayName();
		String playerr = player;
		return where.replace("%player%", playerr).replace("%playername%", playername);
	}

	public static double convertMoney(String s) {
		double a = StringUtils.getDouble(s);
		double mille = a * 1000;
		double million = mille * 1000;
		double billion = million * 1000;
		double trillion = billion * 1000;
		double quadrillion = trillion * 1000;
		if (s.endsWith("k"))
			a = a * 1000;
		if (s.endsWith("m"))
			a = million;
		if (s.endsWith("b"))
			a = billion;
		if (s.endsWith("t"))
			a = trillion;
		if (s.endsWith("q"))
			a = quadrillion;
		return a;
	}

	/**
	 * Hook addon to /Addons command
	 */
	public static enum SeenType {
		Online, Offline;
	}

	@SuppressWarnings("deprecation")
	public static String getSeen(String player, SeenType type) {
		User s = TheAPI.getUser(player);
		String a = "0s";
		switch (type) {
		case Online:
			if (s.exist("JoinTime"))
				a = StringUtils.setTimeToString(System.currentTimeMillis() / 1000 - s.getLong("JoinTime"));
			break;
		case Offline:
			a = StringUtils.setTimeToString(
					System.currentTimeMillis() / 1000 - Bukkit.getOfflinePlayer(player).getLastPlayed() / 1000);
			break;
		}
		return a;
	}

	public static void TeleportBack(Player p) {
		Location loc = getBack(p.getName());
		if (loc != null) {
			TheAPI.getUser(p).set("Back", StringUtils.getLocationAsString(p.getLocation()));
			p.teleport(loc);

			Loader.sendMessages(p, "Back.Teleport.You");
		} else
			Loader.sendMessages(p, "Back.WrongLocation");
	}

	public static void setBack(Player p) {
		TheAPI.getUser(p).set("Back", StringUtils.getLocationAsString(p.getLocation()));
	}

	public static void setBack(String p, Location l) {
		TheAPI.getUser(p).set("Back", StringUtils.getLocationAsString(l));
	}

	public static Location getBack(String p) {
		return StringUtils.getLocationFromString(TheAPI.getUser(p).getString("Back"));
	}

	private static boolean has(CommandSender p, List<String> perms) {
		int i = 0;
		for (String s : perms)
			if (p.hasPermission(s))
				++i;
		return i == perms.size() - 1;

	}

	public static boolean hasPerms(CommandSender p, List<String> perms) {
		if (has(p, perms)) {
			return true;
		}
		Loader.sendMessages(p, "NoPerms", Placeholder.c().add("%permission%", StringUtils.join(perms, ", ")));
		return false;
	}

	public static boolean hasPerm(CommandSender s, String permission) {
		if (s.hasPermission(permission)) {
			return true;
		}
		Loader.sendMessages(s, "NoPerms", Placeholder.c().add("%permission%", permission));
		return false;
	}

	public static void setDisplayName(Player p, String name) {
		NMSAPI.getNMSPlayerAPI(p).setDisplayName(name);
	}

	public static void setCustomName(Player p, String name) {
		NMSAPI.getNMSPlayerAPI(p).setDisplayName(name);
	}

	public static List<String> getKits() {
		List<String> list = new ArrayList<String>();
		for (String name : Loader.kit.getKeys("Kits")) {
			list.add(name);
		}
		return list;
	}

	public static void giveKit(String player, String KitName) {
		if (getKitFromString(KitName) != null)
			Kit.giveKit(player, KitName, false, false);
	}

	public static void giveKit(String player, String KitName, boolean cooldown, boolean economy) {
		if (getKitFromString(KitName) != null)
			Kit.giveKit(player, KitName, cooldown, economy);
	}

	public static String getKitFromString(String string) {
		return Kit.getKitName(string);
	}

	public static boolean isKit(String string) {
		if (getKitFromString(string) != null)
			return true;
		return false;
	}

	public static String setMoneyFormat(BigDecimal money, boolean colorized) {
	    String a = (new DecimalFormat("#,##0.00")).format(money).replaceAll("\\.00", "");
	    String[] s = a.split(",");
	    if (s.length >= 22) {
	      if (a.startsWith("-"))
	        a="-∞"; 
	      a="∞";
	    }else
	    if (s.length >= 21 && s.length < 22)
	      a= (new DecimalFormat("#,###0.00NOV")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    else
	    if (s.length >= 20 && s.length < 21)
	    	a= (new DecimalFormat("#,###0.00OCT")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    	else
	    if (s.length >= 19 && s.length < 20)
	    	a= (new DecimalFormat("#,###0.00SEP")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
		    else
		if (s.length >= 18 && s.length < 19)
	    	a= (new DecimalFormat("#,###0.00SED")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 17 && s.length < 18)
	    	a= (new DecimalFormat("#,###0.00QUI")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 16 && s.length < 17)
	    	a= (new DecimalFormat("#,###0.00QUA")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 15 && s.length < 16)
	    	a= (new DecimalFormat("#,###0.00tre")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 14 && s.length < 15)
	    	a= (new DecimalFormat("#,###0.00duo")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 13 && s.length < 14)
	    	a= (new DecimalFormat("#,###0.00und")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 12 && s.length < 13)
	    	a= (new DecimalFormat("#,###0.00dec")).format(money.divide(new BigDecimal("1000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 11 && s.length < 12)
	    	a= (new DecimalFormat("#,###0.00non")).format(money.divide(new BigDecimal("1000000000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 10 && s.length < 11)
	    	a= (new DecimalFormat("#,###0.00oct")).format(money.divide(new BigDecimal("1000000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 9 && s.length < 10)
	    	a= (new DecimalFormat("#,###0.00sep")).format(money.divide(new BigDecimal("1000000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 8 && s.length < 9)
	    	a= (new DecimalFormat("#,###0.00sex")).format(money.divide(new BigDecimal("1000000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 7 && s.length < 8)
	    	a= (new DecimalFormat("#,###0.00qui")).format(money.divide(new BigDecimal("1000000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 6 && s.length < 7)
	    	a= (new DecimalFormat("#,###0.00qua")).format(money.divide(new BigDecimal("1000000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 5 && s.length < 6)
	    	a= (new DecimalFormat("#,###0.00t")).format(money.divide(new BigDecimal("1000000000000"))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 4 && s.length < 5) 
	    	a= (new DecimalFormat("#,###0.00b")).format(money.divide(new BigDecimal(1000000000))).replaceAll("\\.00", ""); 
			else
		if (s.length >= 3 && s.length < 4)
	    	a= (new DecimalFormat("#,###0.00m")).format(money.divide(new BigDecimal(1000000))).replaceAll("\\.00", ""); 
			else
		if (s.length > 2 && s.length < 3)
	    	a= (new DecimalFormat("#,###0.00k")).format(money.divide(new BigDecimal(1000))).replaceAll("\\.00", ""); 
	    if(colorized) {
	    	if(a.equals("0"))a="&e0";
	    	else {
	    	if(a.startsWith("-"))a="&c"+a;
	    	else a="&a"+a;
	    }}
	    return a;
	}

	public static String setMoneyFormat(double money, boolean colorized) {
		return setMoneyFormat(new BigDecimal(money), colorized);
	}

	private static HashMap<String, SPlayer> cache = new HashMap<>();
	
	public static boolean isAFK(Player p) {
		return getSPlayer(p).isAFK();
	}

	public static boolean getVulgarWord(String string) {
		List<String> words = Loader.config.getStringList("SwearWords");
		for (String word : words)
			if (string.toLowerCase().contains(word.toLowerCase())) {
				return true;
			}
		return false;
	}

	public static String getValueOfVulgarWord(String string) {
		List<String> words = Loader.config.getStringList("SwearWords");
		for (String word : words)
			if (string.toLowerCase().contains(word.toLowerCase())) {
				return String.valueOf(word);
			}
		return "";
	}

	public static boolean getSpamWord(String string) {
		List<String> words = Loader.config.getStringList("SpamWords.Words");
		for (String word : words)
			if (string.toLowerCase().contains(word.toLowerCase())) {
				return true;
			}
		return false;
	}

	public static String getValueOfSpamWord(String string) {
		List<String> words = Loader.config.getStringList("SpamWords.Words");
		for (String word : words)
			if (string.toLowerCase().contains(word.toLowerCase())) {
				return String.valueOf(word);
			}
		return "";
	}

	private static boolean checkForDomain(String str) {
		str = str.replaceAll("[0-9]+", "").replace(" ", ".");
		for (String w : Loader.config.getStringList("AntiAD.WhiteList")) {
			str = str.replace(w, "");
		}
		Matcher m = Pattern
				.compile("[-a-zA-Z0-9@:%_\\+~#?&//=]{3,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+~#?&//=]*)?")
				.matcher(str.toLowerCase());
		return m.find();
	}

	private static boolean checkForIp(String str) {
		str = str.replaceAll("[A-Za-z]+", "").replace(" ", ".");
		for (String w : Loader.config.getStringList("AntiAD.WhiteList")) {
			str = str.replace(w, "");
		}
		Matcher m = Pattern.compile("(?:\\d{1,3}[.,\\-:;\\/()=?}+ ]{1,4}){3}\\d{1,3}").matcher(str.toLowerCase());
		return m.find();
	}

	public static List<String> getAdvertisementMatches(String where) {
		List<String> matches = new ArrayList<String>();
		if (getAdvertisement(where)) {
			Matcher m = Pattern.compile("(?:\\d{1,3}[.,\\-:;\\/()=?}+ ]{1,4}){3}\\d{1,3}").matcher(where.toLowerCase());
			while (m.find()) {
				matches.add(m.group());
			}
			m = Pattern.compile("[-a-zA-Z0-9@:%_\\+~#?&//=]{3,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+~#?&//=]*)?")
					.matcher(where.toLowerCase());
			while (m.find()) {
				matches.add(m.group());
			}
		}
		return matches;
	}

	public static boolean getAdvertisement(String string) {
		return checkForIp(string) || checkForDomain(string);
	}

	public static boolean getBlockedCommand(String string) {
		List<String> words = Loader.config.getStringList("Options.CommandsBlocker.List");
		for (String word : words)
			if (string.toLowerCase().contains(word.toLowerCase())) {
				return true;
			}
		return false;
	}

	public static String getServerIP() {
		String ip = null;
		try {
			BufferedReader is = new BufferedReader(new FileReader("server.properties"));
			Properties props = new Properties();
			props.load(is);
			is.close();
			ip = props.getProperty("server-ip");
		} catch (Exception ed) {
		}

		if (ip != null) {
			return ip;
		}
		return "UKNOWN";
	}

	public static String getServerPort() {
		String port = null;
		try {
			BufferedReader is = new BufferedReader(new FileReader("server.properties"));
			Properties props = new Properties();
			props.load(is);
			is.close();
			port = props.getProperty("server-port");
		} catch (Exception ed) {
		}

		if (port != null) {
			return port;
		}
		return "UKNOWN";
	}

	public static String getServerName() {
		String server_name = null;
		try {
			BufferedReader is = new BufferedReader(new FileReader("server.properties"));
			Properties props = new Properties();
			props.load(is);
			is.close();
			server_name = props.getProperty("server-name");
		} catch (Exception ed) {
		}

		if (server_name != null) {
			return server_name;
		}
		return "UKNOWN";
	}
	
	//Can be teleport cancelled if plugin not find any safe location!
	public static void safeTeleport(Player s, Location location) {
		if(!isSafe(location)) {
			Location safe = findSafeLocation(location);
			if(safe!=null) {
				s.setNoDamageTicks(40);
				s.teleport(safe);
			}
		}else {
			s.setNoDamageTicks(40);
			s.teleport(location);
		}
	}
	
	public static boolean isSafe(Location loc) {
		Material under = loc.clone().add(0,-1,0).getBlock().getType();
		Material current = loc.getBlock().getType();
		Material above = loc.clone().add(0,1,0).getBlock().getType();
		return c(1, under.name()) && c(2, current.name()) && c(2, above.name());
	}
	
	public static Location findSafeLocation(Location start) {
		Location f = null;
		BlockGetter g = new BlockGetter(new Position(start.clone().add(0,-6,0)), new Position(start.clone().add(0,6,0)));
		while(g.has()) {
			Position a1 = g.get();
			a1.setX(start.getX());
			a1.setZ(start.getZ());
			if(isSafe(a1.toLocation())) {
				if(f==null) {
					f=start;
					f.setY(a1.getY());
				}
				else {
					if(f.distance(start) > a1.toLocation().distance(start))
						f.setY(a1.getY());
				}
				f=start;
				f.setY(a1.getY());
		}}
		if(f==null) {
		g = new BlockGetter(new Position(start.clone().add(4,4,4)), new Position(start.clone().add(-4,-4,-4)));
		while(g.has()) {
			Position a1 = g.get();
			a1.setX(a1.getX()+0.5);
			a1.setZ(a1.getZ()+0.5);
			if(isSafe(a1.toLocation())) {
				Location safef=a1.toLocation();
				safef.setPitch(start.getPitch());
				safef.setYaw(start.getYaw());
				if(f==null)f=safef;
				else {
					if(f.distance(start) > safef.distance(start))
						f=safef;
				}
		}}
		}
		return f;
	}
	
    private static boolean c(int i, String c) {
        boolean d = false;
        switch (i) {
        case 1:
            if (!c(2, c) && !c.contains("LAVA") && Material.matchMaterial(c).isBlock())
                d = true;
            break;
        case 2:
            if (c.contains("AIR") || c.equals("SEAGRASS") || c.equals("LONG_GRASS") || c.equals("FLOWER") || c.contains("BUTTON") || c.contains("DOOR") || c.contains("SIGN")
                    || c.contains("TORCH") || c.equals("MUSHROOM_STEM") || c.contains("WATER")||c.contains("RED_MUSHROOM") || c.contains("BROWN_MUSHROOM"))
                d = true;
            break;
        }
        return d;
    }
	
}
