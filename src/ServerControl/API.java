package ServerControl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import Commands.Kit;
import ServerControlEvents.PluginHookEvent;
import Utils.setting;
import me.DevTec.PluginManagerAPI;
import me.DevTec.TheAPI;
import me.DevTec.Other.StringUtils;
import me.DevTec.Other.User;

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
		List<SPlayer> s = Lists.newArrayList();
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
				TheAPI.msg(Loader.s("Spawn.NoHomesTeleportedToSpawn").replace("%world%", p.getWorld().getName())
						.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), p);
				a = getTeleportLocation(p, TeleportLocation.SPAWN);
			}
		}
			break;
		case SPAWN: {
			World world = Bukkit.getWorlds().get(0);
			Location loc = world.getSpawnLocation();
			if (Loader.config.getString("Spawn") != null
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

	public static void hookAddon(Plugin plugin) {
		PluginHookEvent event = new PluginHookEvent(plugin);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			TheAPI.getConsole().sendMessage(TheAPI
					.colorize(Loader.s("Prefix") + "&aHooked addon &0'&6" + plugin.getName() + "&0' &ato the plugin."));
			event.Hook();
		}
	}

	public static void TeleportBack(Player p) {
		Location loc = getBack(p.getName());
		if (loc != null) {
			TheAPI.getUser(p).set("Back", StringUtils.getLocationAsString(p.getLocation()));
			p.teleport(loc);
			TheAPI.msg(Loader.s("Back.Teleporting").replace("%prefix%", Loader.s("Prefix"))
					.replace("%playername%", p.getDisplayName()).replace("%player%", p.getName()), p);
		} else
			TheAPI.msg(Loader.s("Back.CantGetLocation"), p);
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
		p.sendMessage(TheAPI.colorize(
				Loader.s("NotPermissionsMessage").replace("%player%", p.getName()).replace("%playername%", p.getName())
						.replace("%permission%", StringUtils.join(perms, ", "))));
		return false;
	}

	public static boolean hasPerm(CommandSender s, String permission) {
		if (s.hasPermission(permission)) {
			return true;
		}
		s.sendMessage(TheAPI.colorize(Loader.s("NotPermissionsMessage").replace("%player%", s.getName())
				.replace("%playername%", s.getName()).replace("%permission%", permission)));
		return false;
	}

	public static void setDisplayName(Player p, String name) {
		p.setDisplayName(name);
	}

	public static void setCustomName(Player p, String name) {
		p.setCustomName(name);
	}

	public static List<String> getKits() {
		List<String> list = new ArrayList<String>();
		for (String name : Loader.kit.getConfigurationSection("Kits").getKeys(false)) {
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

	public static String setMoneyFormat(double money, boolean colorized) {
		String a = ""+money;
	    String get = (new DecimalFormat("#,##0.00")).format(new BigDecimal(a)).replaceAll("\\.00", "");
	    String[] s = get.split(",");
	    if (s.length >= 22) {
	      if (get.startsWith("-"))
	        a = "-∞"; 
	      a = "∞";
	    } 
	    if (s.length >= 21 && s.length < 22)
	      return (new DecimalFormat("#,###0.00NOV")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 20 && s.length < 21)
	      return (new DecimalFormat("#,###0.00OCT")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 19 && s.length < 20)
	      return (new DecimalFormat("#,###0.00SEP")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 18 && s.length < 19)
	      return (new DecimalFormat("#,###0.00SED")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 17 && s.length < 18)
	      return (new DecimalFormat("#,###0.00QUI")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 16 && s.length < 17)
	      return (new DecimalFormat("#,###0.00QUA")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 15 && s.length < 16)
	      return (new DecimalFormat("#,###0.00tre")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 14 && s.length < 15)
	      return (new DecimalFormat("#,###0.00duo")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 13 && s.length < 14)
	      return (new DecimalFormat("#,###0.00und")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 12 && s.length < 13)
	      return (new DecimalFormat("#,###0.00dec")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 11 && s.length < 12)
	      return (new DecimalFormat("#,###0.00non")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 10 && s.length < 11)
	      return (new DecimalFormat("#,###0.00oct")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 9 && s.length < 10)
	      return (new DecimalFormat("#,###0.00sep")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 8 && s.length < 9)
	      return (new DecimalFormat("#,###0.00sex")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 7 && s.length < 8)
	      return (new DecimalFormat("#,###0.00qui")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 6 && s.length < 7)
	      return (new DecimalFormat("#,###0.00qua")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 5 && s.length < 6)
	      return (new DecimalFormat("#,###0.00t")).format((new BigDecimal(a)).divide(new BigDecimal("1000000000000"))).replaceAll("\\.00", ""); 
	    if (s.length >= 4 && s.length < 5) 
	      return (new DecimalFormat("#,###0.00b")).format((new BigDecimal(a)).divide(new BigDecimal(1000000000))).replaceAll("\\.00", ""); 
	    if (s.length >= 3 && s.length < 4)
	      return (new DecimalFormat("#,###0.00m")).format((new BigDecimal(a)).divide(new BigDecimal(1000000))).replaceAll("\\.00", ""); 
	    if (s.length >= 2 && s.length < 3)
	      return (new DecimalFormat("#,###0.00k")).format((new BigDecimal(a)).divide(new BigDecimal(1000))).replaceAll("\\.00", ""); 
	    return get;
	  }

	private static HashMap<String, SPlayer> cache = Maps.newHashMap();
	
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
		} catch (IOException ed) {
			ed.printStackTrace();
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
		} catch (IOException ed) {
			ed.printStackTrace();
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
		} catch (IOException ed) {
			ed.printStackTrace();
		}

		if (server_name != null) {
			return server_name;
		}
		return "UKNOWN";
	}

	public static void safeTeleport(Player s, Location location) {
		s.teleport(location);
	}
}
