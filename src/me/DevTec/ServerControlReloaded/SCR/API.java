package me.DevTec.ServerControlReloaded.SCR;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.blocksapi.BlockIterator;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;

public class API {
	protected static Loader plugin = Loader.getInstance;
	private static HashMap<String, SPlayer> cache = new HashMap<>();

	public static SPlayer getSPlayer(Player p) {
		if(!cache.containsKey(p.getName())) {
			cache.put(p.getName(), new SPlayer(p));
		}
		return cache.get(p.getName());
	}

	public static SPlayer getSPlayer(String p) {
		if(!cache.containsKey(p)) {
			cache.put(p, new SPlayer(p));
		}
		return cache.get(p);
	}

	public static List<SPlayer> getSPlayers() {
		List<SPlayer> s = new ArrayList<>();
		for(Player p : TheAPI.getOnlinePlayers())s.add(getSPlayer(p));
		return s;
	}

	public enum TeleportLocation {
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
			if (Loader.config.exists("Spawn")) {
				loc=Position.fromString(Loader.config.getString("Spawn")).toLocation();
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
	
	private static Pattern moneyPattern = Pattern.compile("([+-]*[0-9]+.*[0-9]*[E]*[0-9]*)([kmbt]|qu[ia]|se[px]|non|oct|dec|und|duo|tre|sed|nov)", Pattern.CASE_INSENSITIVE);
    public static double convertMoney(String s) {
		double has = 0;
		Matcher m = moneyPattern.matcher(s);
		while(m.find())
			has+=StringUtils.getDouble(m.group(1))*getMultiply(m.group(2));
		if(has==0)has=StringUtils.getDouble(s);
		return has;
	}
    
    public static long getMultiply(String name) {
    	switch(name) {
    	case "k":
    		return 1000;
    	case "m":
    		return 1000000;
    	case "b":
    		return 1000000000;
    	case "t":
    		return new BigDecimal("1000000000000").longValue();
    	case "qua":
    		return new BigDecimal("1000000000000000").longValue();
    	case "qui":
    		return new BigDecimal("1000000000000000000").longValue();
    	case "sex":
    		return new BigDecimal("1000000000000000000000").longValue();
    	case "sep":
    		return new BigDecimal("1000000000000000000000000").longValue();
    	case "oct":
    		return new BigDecimal("1000000000000000000000000000").longValue();
    	case "non":
    		return new BigDecimal("1000000000000000000000000000000").longValue();
    	case "dec":
    		return new BigDecimal("1000000000000000000000000000000000").longValue();
    	case "und":
    		return new BigDecimal("1000000000000000000000000000000000000").longValue();
    	case "duo":
    		return new BigDecimal("1000000000000000000000000000000000000000").longValue();
    	case "tre":
    		return new BigDecimal("1000000000000000000000000000000000000000000").longValue();
    	case "QUA":
    		return new BigDecimal("1000000000000000000000000000000000000000000000").longValue();
    	case "QUI":
    		return new BigDecimal("1000000000000000000000000000000000000000000000000").longValue();
    	case "SED":
    		return new BigDecimal("1000000000000000000000000000000000000000000000000000").longValue();
    	case "OCT":
    		return new BigDecimal("1000000000000000000000000000000000000000000000000000000000").longValue();
    	case "NOV":
    		return new BigDecimal("1000000000000000000000000000000000000000000000000000000000000").longValue();
    	}
    	return 1;
    }
    
	/**
	 * Hook addon to /Addons command
	 */
	public static enum SeenType {
		Online, Offline;
	}

	public static String getSeen(String player, SeenType type) {
		User s = TheAPI.getUser(player);
		String a = "0s";
		switch (type) {
		case Online:
			if (s.exist("JoinTime"))
				a = StringUtils.setTimeToString(System.currentTimeMillis() / 1000 - s.getLong("JoinTime"));
			break;
		case Offline:
			a = StringUtils.setTimeToString(System.currentTimeMillis() / 1000 - Bukkit.getOfflinePlayer(player).getLastPlayed() / 1000);
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
	
	private static DecimalFormat a(String c) {
		DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance(Locale.ENGLISH);
		decimalFormat.applyPattern("###,###.##"+c);
		return decimalFormat;
	}

	public static String setMoneyFormat(BigDecimal money, boolean colorized) {
		String a = a("").format(money);
	    String[] s = a.toLowerCase(Locale.ENGLISH).split(",");
	    if (s.length >= 22) {
	      if (a.startsWith("-"))
	        a="-∞";
	      else
	      a="∞";
	    }else
	    if (s.length >= 21 && s.length < 22)
	      a= a("NOV").format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000000000"))); 
	    else
	    if (s.length >= 20 && s.length < 21)
	    	a= (a("OCT")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000000"))); 
	    	else
	    if (s.length >= 19 && s.length < 20)
	    	a= (a("SEP")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000"))); 
		    else
		if (s.length >= 18 && s.length < 19)
	    	a= (a("SED")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 17 && s.length < 18)
	    	a= (a("QUI")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 16 && s.length < 17)
	    	a= (a("QUA")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 15 && s.length < 16)
	    	a= (a("tre")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 14 && s.length < 15)
	    	a= (a("duo")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 13 && s.length < 14)
	    	a= (a("und")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000"))); 
			else
		if (s.length >= 12 && s.length < 13)
	    	a= (a("dec")).format(money.divide(new BigDecimal("1000000000000000000000000000000000"))); 
			else
		if (s.length >= 11 && s.length < 12)
	    	a= (a("non")).format(money.divide(new BigDecimal("1000000000000000000000000000000"))); 
			else
		if (s.length >= 10 && s.length < 11)
	    	a= (a("oct")).format(money.divide(new BigDecimal("1000000000000000000000000000"))); 
			else
		if (s.length >= 9 && s.length < 10)
	    	a= (a("sep")).format(money.divide(new BigDecimal("1000000000000000000000000"))); 
			else
		if (s.length >= 8 && s.length < 9)
	    	a= (a("sex")).format(money.divide(new BigDecimal("1000000000000000000000"))); 
			else
		if (s.length >= 7 && s.length < 8)
	    	a= (a("qui")).format(money.divide(new BigDecimal("1000000000000000000"))); 
			else
		if (s.length >= 6 && s.length < 7)
	    	a= (a("qua")).format(money.divide(new BigDecimal("1000000000000000"))); 
			else
		if (s.length >= 5 && s.length < 6)
	    	a= (a("t")).format(money.divide(new BigDecimal("1000000000000"))); 
			else
		if (s.length >= 4 && s.length < 5) 
	    	a= (a("b")).format(money.divide(new BigDecimal(1000000000))); 
			else
		if (s.length >= 3 && s.length < 4)
	    	a= (a("m")).format(money.divide(new BigDecimal(1000000))); 
			else
		if (s.length >= 2 && s.length < 3)
	    	a= (a("k")).format(money.divide(new BigDecimal(1000))); 
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
	
	public static boolean isAFK(Player p) {
		return getSPlayer(p).isAFK();
	}

	public static boolean getBlockedCommand(String string) {
		List<String> words = Loader.config.getStringList("Options.CommandsBlocker.List");
		for (String word : words)
			if (string.toLowerCase().contains(word.toLowerCase())) {
				return true;
			}
		return false;
	}
	
	//Can be teleport cancelled if plugin not find any safe location!
	public static void safeTeleport(Player s, Location location) {
		if(location==null) {
			Loader.sendMessages(s, "TpSystem.NotSafe");
			return;
		}
		if(!isSafe(location)) {
			Location safe = findSafeLocation(location);
			if(safe!=null) {
				s.setNoDamageTicks(40);
				s.teleport(safe);
			}else {
				Loader.sendMessages(s, "TpSystem.NotSafe");
			}
		}else {
			s.setNoDamageTicks(40);
			s.teleport(location);
		}
	}
	
	public static boolean isSafe(Location loc) {
		if(loc==null)return false;
		Material under = loc.clone().add(0,-1,0).getBlock().getType();
		Material current = loc.getBlock().getType();
		Material above = loc.clone().add(0,1,0).getBlock().getType();
		return c(1, under.name()) && c(2, current.name()) && c(2, above.name());
	}
	
	public static Location findSafeLocation(Location start) {
		Location f = null;
			BlockIterator g = new BlockIterator(new Position(start.clone().add(20,20,20)), new Position(start.clone().add(-20,-20,-20)));
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
