package me.DevTec.ServerControlReloaded.SCR;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.utils.BlockMathIterator;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.TheMaterial;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;
import net.luckperms.api.LuckPermsProvider;

public class API {
	protected static Loader plugin = Loader.getInstance;
	private static Map<String, SPlayer> cache = new HashMap<>();

	public static SPlayer getSPlayer(Player p) {
		if(!cache.containsKey(p.getName()))
			cache.put(p.getName(), new SPlayer(p));
		return cache.get(p.getName());
	}

	public static SPlayer getSPlayer(String p) {
		if(!cache.containsKey(p))
			cache.put(p, new SPlayer(p));
		return cache.get(p);
	}
	
	public static List<Player> getPlayers(CommandSender s){ // Players which can be seen by CommandSender
		List<Player> p = TheAPI.getOnlinePlayers();
		if(s instanceof Player) {
			Iterator<Player> it = p.iterator();
			while(it.hasNext()) {
				Player pd = it.next();
				if( pd!=s && !canSee( ( (Player)s), pd.getName()) ) {
	           		it.remove();
	           	}
			}
		}
		return p;
	}
	
	public static List<Player> getPlayersThatCanSee(CommandSender s){ // Player which can see CommandSender
		List<Player> p = TheAPI.getOnlinePlayers();
		if(s instanceof Player) {
			Iterator<Player> it = p.iterator();
			while(it.hasNext()) {
				Player pd = it.next();
				if( pd!=s && !canSee(pd, s.getName()) ) {
	           		it.remove();
	           	}
			}
		}
		return p;
	}
	
	public static List<String> getPlayerNames(CommandSender s){
		List<Player> p = getPlayers(s);
		List<String> names = new ArrayList<>(p.size());
		p.forEach(a -> names.add(a.getName()));
		return names;
	}
	
	public static String getGroup(String player) {
		if(PluginManagerAPI.isEnabledPlugin("LuckPerms"))
			return LuckPermsProvider.get().getUserManager().getUser(player).getPrimaryGroup();
		if(Loader.perms!=null)
			return Loader.perms.getPrimaryGroup("world", player);
		if(Loader.vault!=null)
			return Loader.vault.getPrimaryGroup("world", player);
		return "default";
	}
	
	public static String getGroup(Player player) {
		if(PluginManagerAPI.isEnabledPlugin("LuckPerms"))
			return LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
		if(Loader.perms!=null && Loader.perms.hasGroupSupport())
			return Loader.perms.getPrimaryGroup(player);
		if(Loader.vault!=null)
			return Loader.vault.getPrimaryGroup(player);
		return "default";
	}
	
    public static void setVanish(User i, String permission, boolean value) {
        Player s = TheAPI.getPlayerOrNull(i.getName());
        if (s != null)
            applyVanish(i,s, permission, value);
        else {
            if(value) {
            	i.set("vanish", value);
            	i.set("vanish.perm", permission);
            }else {
            	i.remove("vanish");
            }
        }
    }
	
    public static void setVanish(Player playerName, String permission, boolean value) {
        if (playerName != null) {
        	User s = TheAPI.getUser(playerName);
            applyVanish(s, playerName, permission, value);
            s.save();
        }
    }
 
    private static void applyVanish(User i, Player s, String perm, boolean var) {
            if (var) {
                i.set("vanish", var);
                i.set("vanish.perm", perm);
                List<Player> l = TheAPI.getOnlinePlayers();
                l.remove(s);
                for(Player d : l)
                    if(!d.hasPermission(perm))
                        d.hidePlayer(s);
                return;
            }
            i.remove("vanish");
            List<Player> l = TheAPI.getOnlinePlayers();
            l.remove(s);
            for (Player d : l)
            	d.showPlayer(s);
    }
 
    public static boolean hasVanish(String playerName) {
        if(playerName==null)return false;
        return TheAPI.getUser(playerName).getBoolean("vanish");
    }
 
    public static boolean hasVanish(Player player) {
        if(player==null)return false;
        return TheAPI.getUser(player).getBoolean("vanish");
    }
 
    public static String getVanishPermission(String playerName) {
        return TheAPI.getUser(playerName).getString("vanish.perm");
    }
 
    public static boolean canSee(Player player, String target) {
    	if(TheAPI.getPlayerOrNull(target)!=null && (hasVanish(target) ? player.hasPermission(getVanishPermission(target)) : true))return player.canSee(TheAPI.getPlayerOrNull(target));
        return hasVanish(target) ? player.hasPermission(getVanishPermission(target)) : true;
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
				safeTeleport(p,false,new Position(a));
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
    		return 1000000000000L;
    	case "qua":
    		return 1000000000000000L;
    	case "qui":
    		return 1000000000000000000L;
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
	    	a= (a("qui")).format(money.divide(new BigDecimal(1000000000000000000L))); 
			else
		if (s.length >= 6 && s.length < 7)
	    	a= (a("qua")).format(money.divide(new BigDecimal(1000000000000000L))); 
			else
		if (s.length >= 5 && s.length < 6)
	    	a= (a("t")).format(money.divide(new BigDecimal(1000000000000L))); 
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
	public static void safeTeleport(Player s, boolean air, Position location) {
		if(location==null) {
			Loader.sendMessages(s, "TpSystem.NotSafe");
			return;
		}
		if(!isSafe(air, location)) {
			new Thread(new Runnable() {
				public void run() {
					s.setNoDamageTicks(60);
					Position safe = findSafeLocation(air,location);
					if(safe!=null)
						NMSAPI.postToMainThread(new Runnable() {
							@Override
							public void run() {
								s.teleport(safe.toLocation());
							}
						});
					else
						Loader.sendMessages(s, "TpSystem.NotSafe");
				}
			}).start();
		}else {
			s.setNoDamageTicks(60);
			s.teleport(location.toLocation());
		}
	}
	
	public static boolean isSafe(Map<Long, Object> loaded, Position loc) {
		return isSafe(false,loaded,loc);
	}
	
	public static boolean isSafe(boolean air, Map<Long, Object> loaded, Position loc) {
		Object chunk = loaded.get(loc.getChunkKey());
		if(chunk==null)
			loaded.put(loc.getChunkKey(), chunk=loc.getNMSChunk());
		Material under = getType(loc.add(0,-1,0),chunk).getType();
		Material above = getType(loc.add(0,2,0),chunk).getType();
		Material current = getType(loc.add(0,-1,0),chunk).getType();
		return c(air,1, under.name(),under) && c(air,2, current.name(),current) && c(air,2, above.name(),above);
	}
	
	public static boolean isSafe(Position loc) {
		return isSafe(false,loc);
	}
	
	public static boolean isSafe(boolean air, Position loc) {
		Object chunk = loc.getNMSChunk();
		Material under = getType(loc.add(0,-1,0),chunk).getType();
		Material above = getType(loc.add(0,2,0),chunk).getType();
		Material current = getType(loc.add(0,-1,0),chunk).getType();
		return c(air,1, under.name(),under) && c(air,2, current.name(),current) && c(air,2, above.name(),above);
	}
	
	private static Method getter = (Method) Ref.getStatic(Position.class, "getter"), getdata = (Method) Ref.getStatic(Position.class, "getdata");
	
	public static TheMaterial getType(Position p, Object chunk) {
		if (TheAPI.isOlderThan((int) 8)) {
			return TheMaterial.fromData(
					Ref.invoke(chunk, getter,
							(int) p.getX() & 15, (int) p.getY() & 15, (int) p.getZ() & 15),
					(int) ((byte) Ref.invoke(chunk, getdata,
							(int) p.getX() & 15, (int) p.getY() & 15, (int) p.getZ() & 15)));
		}
		return TheMaterial.fromData(Ref.invoke(chunk, getter, p.getBlockPosition()));
	}
	
	public Object getIBlockData(Object chunk, Position p) {
		if (TheAPI.isOlderThan((int) 8)) {
			return Ref.invoke((Object) chunk, getter, (int) p.getX() & 15, (int) p.getY() & 15, (int) p.getZ() & 15);
		}
		return Ref.invoke((Object) chunk, getter, p.getBlockPosition());
	}
	
	public static Position findSafeLocation(Position start) {
		return findSafeLocation(false,start);
	}
	
	public static Position findSafeLocation(boolean canBeAir, Position start) {
		Position f = start.clone();
		double oldDistance = 100;
		BlockMathIterator g = new BlockMathIterator(start.getBlockX()+10,start.getBlockY()+10,start.getBlockZ()+10,
				start.getBlockX()-10,start.getBlockY()-10,start.getBlockZ()-10);
		Map<Long,Object> loaded = new HashMap<>();
		while(g.has()) {
			int[] a = g.get();
			Position a1 = new Position(start.getWorld(), a[0],a[1],a[2]);
			if(isSafe(canBeAir,loaded,a1)) {
				if(a1.distance(start) <= oldDistance) {
					oldDistance=a1.distance(start);
					f.setX(a[0]);
					f.setY(a[1]);
					f.setZ(a[2]);
				}
			}
		}
		return oldDistance!=100?f.add(StringUtils.getDouble("0."+(start.getX()+"").split("\\.")[1])==0?0.5:StringUtils.getDouble("0."+(start.getX()+"").split("\\.")[1]),0,StringUtils.getDouble("0."+(start.getZ()+"").split("\\.")[1])==0?0.5:StringUtils.getDouble("0."+(start.getZ()+"").split("\\.")[1])):null;
	}
	
    private static boolean c(boolean air, int i, String c, Material d) {
    	if(air && i == 2)return !c.contains("LAVA");
    	switch (i) {
        case 1:
            if (air?true:!c(air,2, c,d) && !c.contains("LAVA") && d.isBlock())
                return true;
            break;
        case 2:
        	if (c.contains("AIR") || c.equals("SEAGRASS") || c.equals("LONG_GRASS") || c.equals("FLOWER") || c.contains("BUTTON") || c.contains("DOOR") || c.contains("SIGN")
                        || c.contains("TORCH") || c.equals("MUSHROOM_STEM") ||c.contains("RED_MUSHROOM") || c.contains("BROWN_MUSHROOM"))
                    return true;
            break;
        }
        return false;
    }
	
}
