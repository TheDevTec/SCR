package me.devtec.servercontrolreloaded.scr;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.devtec.servercontrolreloaded.events.functions.JoinQuitEvents;
import me.devtec.servercontrolreloaded.utils.SPlayer;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.blocksapi.BlockIterator;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.nms.NMSAPI;
import net.luckperms.api.LuckPermsProvider;

public class API {
	protected static final Loader plugin = Loader.getInstance;
	private static final Map<String, SPlayer> cache = new HashMap<>();

	public static SPlayer getSPlayer(Player p) {
		if(!cache.containsKey(p.getName()))
			cache.put(p.getName(), new SPlayer(p));
		return cache.get(p.getName());
	}
	public static SPlayer getSPlayer(OfflinePlayer p) {
		return new SPlayer(p.getName());
	}
	public static SPlayer getSPlayer(String p) {
		if(!cache.containsKey(p))
			cache.put(p, new SPlayer(p));
		return cache.get(p);
	}

	public static SPlayer removeSPlayer(Player p) {
		return cache.remove(p.getName());
	}

	public static SPlayer removeSPlayer(String p) {
		return cache.remove(p);
	}
	
	public static List<Player> getPlayers(CommandSender s) { // Players which can be seen by CommandSender
		List<Player> p = TheAPI.getOnlinePlayers();
		if(s instanceof Player) {
			p.removeIf(pd -> pd != s && !canSee(((Player) s), pd.getName()));
		}
		return p;
	}
	
	public static List<Player> getPlayersThatCanSee(CommandSender s) { // Player which can see CommandSender
		List<Player> p = TheAPI.getOnlinePlayers();
		if(s instanceof Player) {
			p.removeIf(pd -> pd != s && !canSee(pd, s.getName()));
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
            	i.set("vanish", true);
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
            
            if(Loader.events.exists("onVanish")) {
            	if(value) {
					Object o = Loader.events.get("onVanish.Enable.Broadcast");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.broadcastMessage(JoinQuitEvents.replaceAll(fa+"",playerName));
						}}else
							if(!(""+o).isEmpty())
								TheAPI.broadcastMessage(JoinQuitEvents.replaceAll(""+o, playerName));
					}
            	}
            	if(!value) {
					Object o = Loader.events.get("onVanish.Disable.Broadcast");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.broadcastMessage(JoinQuitEvents.replaceAll(fa+"",playerName));
						}}else
							if(!(""+o).isEmpty())
								TheAPI.broadcastMessage(JoinQuitEvents.replaceAll(""+o, playerName));
					}
            	
            		
            	}
            }
        }
    }
 
    private static void applyVanish(User i, Player s, String perm, boolean var) {
            if (var) {
                i.set("vanish", true);
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
 
    public static String getVanishPermission(Player playerName) {
        return TheAPI.getUser(playerName).getString("vanish.perm");
    }
    
    // Player can see target
    public static boolean canSee(Player player, String target) { 
    	if(TheAPI.getPlayerOrNull(target)!=null && (!hasVanish(target) || player.hasPermission(getVanishPermission(target))))return player.canSee(TheAPI.getPlayerOrNull(target));
        return !hasVanish(target) || player.hasPermission(getVanishPermission(target));
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
		switch (l) {
		case BED: {
			if (p.getBedSpawnLocation() != null) {
				return p.getBedSpawnLocation();
			} else
				return getTeleportLocation(p, TeleportLocation.HOME);
		}
		case HOME: {
			String home = null;
			User d = TheAPI.getUser(p);
			List<String> homes = new ArrayList<>(d.getKeys("Homes"));
			if (!homes.isEmpty())
				home = homes.get(0);
			if (home != null) {
				return Position.fromString(d.getString("Homes." + home)).toLocation();
			} else {
				Loader.sendMessages(p, "Home.TpSpawn");
				return getTeleportLocation(p, TeleportLocation.SPAWN);
			}
		}
		case SPAWN: {
			World world = Bukkit.getWorlds().get(0);
			Location loc = world.getSpawnLocation();
			if (Loader.config.exists("Spawn")) {
				return Position.fromString(Loader.config.getString("Spawn")).toLocation();
			}
			return loc;
		}
		}
		return null;
	}

	public static void teleportPlayer(Player p, TeleportLocation location) {
		Location a = getTeleportLocation(p, location);
		if (a != null)
			if (setting.tp_safe)
				safeTeleport(p,false,new Position(a));
			else
				API.teleport(p, a);

	}
	
	private static final Pattern moneyPattern = Pattern.compile("([+-]*[0-9]+.*[0-9]*[E]*[0-9]*)([kmbt]|qu[ia]|se[px]|non|oct|dec|und|duo|tre|sed|nov)", Pattern.CASE_INSENSITIVE);
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
	public enum SeenType {
		Online, Offline
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
			a = StringUtils.setTimeToString(System.currentTimeMillis() / 1000 - Bukkit.getOfflinePlayer(s.getName()).getLastPlayed() / 1000);
			break;
		}
		return a;
	}

	public static void TeleportBack(Player p) {
		Position loc = getBack(p.getName());
		if (loc != null) {
			TheAPI.getUser(p).set("Back", new Position(p.getLocation()).toString());
			API.teleport(p, loc);
			Loader.sendMessages(p, "Back.Teleport.You");
		} else
			Loader.sendMessages(p, "Back.WrongLocation");
	}

	public static void setBack(Player p) {
		TheAPI.getUser(p).set("Back", new Position(p.getLocation()).toString());
	}

	public static void setBack(String p, Location l) {
		TheAPI.getUser(p).set("Back", new Position(l).toString());
	}

	public static Position getBack(String p) {
		return Position.fromString(TheAPI.getUser(p).getString("Back"));
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
	    if (s.length >= 21)
	      a= a("NOV").format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000000000"))); 
	    else
	    if (s.length >= 20)
	    	a= (a("OCT")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000000"))); 
	    	else
	    if (s.length >= 19)
	    	a= (a("SEP")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000000"))); 
		    else
		if (s.length >= 18)
	    	a= (a("SED")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 17)
	    	a= (a("QUI")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 16)
	    	a= (a("QUA")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 15)
	    	a= (a("tre")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 14)
	    	a= (a("duo")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000000"))); 
			else
		if (s.length >= 13)
	    	a= (a("und")).format(money.divide(new BigDecimal("1000000000000000000000000000000000000"))); 
			else
		if (s.length >= 12)
	    	a= (a("dec")).format(money.divide(new BigDecimal("1000000000000000000000000000000000"))); 
			else
		if (s.length >= 11)
	    	a= (a("non")).format(money.divide(new BigDecimal("1000000000000000000000000000000"))); 
			else
		if (s.length >= 10)
	    	a= (a("oct")).format(money.divide(new BigDecimal("1000000000000000000000000000"))); 
			else
		if (s.length >= 9)
	    	a= (a("sep")).format(money.divide(new BigDecimal("1000000000000000000000000"))); 
			else
		if (s.length >= 8)
	    	a= (a("sex")).format(money.divide(new BigDecimal("1000000000000000000000"))); 
			else
		if (s.length >= 7)
	    	a= (a("qui")).format(money.divide(new BigDecimal(1000000000000000000L))); 
			else
		if (s.length >= 6)
	    	a= (a("qua")).format(money.divide(new BigDecimal(1000000000000000L))); 
			else
		if (s.length >= 5)
	    	a= (a("t")).format(money.divide(new BigDecimal(1000000000000L))); 
			else
		if (s.length >= 4)
	    	a= (a("b")).format(money.divide(new BigDecimal(1000000000))); 
			else
		if (s.length >= 3)
	    	a= (a("m")).format(money.divide(new BigDecimal(1000000))); 
			else
		if (s.length >= 2)
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
	
	public static void teleport(Player s, Position loc) {
		s.teleport(loc.toLocation());
	}

	public static void teleport(Player s, Location loc) {
		s.teleport(loc);
	}
	
	//Can be teleport cancelled if plugin not find any safe location!
	public static void safeTeleport(Player s, boolean air, Position location) {
		if(location==null) {
			Loader.sendMessages(s, "TpSystem.NotSafe");
			return;
		}
		new Thread(() -> {
			Position safe = findSafeLocation(air,location);
			if(safe!=null) {
				s.setNoDamageTicks(60);
				NMSAPI.postToMainThread(() -> teleport(s, safe));
			}
			else
				Loader.sendMessages(s, "TpSystem.NotSafe");
		}).start();
	}
	
	public static boolean isSafe(Position loc) {
		return isSafe(false,loc);
	}
	
	public static Position findSafeLocation(Position start) {
		return findSafeLocation(false,start);
	}
	
	public static Position findSafeLocation(boolean canBeAir, Position start) {
		if(toInt(start.clone().add(0, -1, 0))==0)return start;
		Position f = start.clone();
		double oldDistance = -1;
		BlockIterator g = new BlockIterator(start.clone().add(6,3,6),start.clone().add(-6,-3,-6));
		while(g.has()) {
			Position a1 = g.get().clone();
			double distance = start.distanceSquared(a1);
			if(distance <= oldDistance || oldDistance < 0) {
				if(isSafe(canBeAir,a1)) {
					oldDistance=distance;
					f=a1;
				}else {
					if(isSafe(canBeAir,a1.add(0,1,0))) {
						oldDistance=distance;
						f=a1;
					}
				}
			}
		}
		return oldDistance!=-1?Position.fromString(("[Position:" + start.getWorldName() + 
				'/' + (f.getX()+"").split("\\.")[0]+'.'+(start.getX()+"").split("\\.")[1] + '/' + 
				f.getY()+ '/' 
				+ (f.getZ()+"").split("\\.")[0]+'.'+(start.getZ()+"").split("\\.")[1] + '/'
				+ start.getYaw() + '/' + start.getPitch() + "]").replace(".", ":")):null;
	}

	public static boolean isSafe(boolean air, Position loc) {
		Position down = loc.clone().add(0, -1, 0);
		int i = toInt(down);
		Position middle = loc.clone();
		int i1 = toInt(middle);
		Position top = loc.clone().add(0, 1, 0);
		int i2 = toInt(top);
		if(i==3) {
			if(i2==0) {
				loc.add(0,-1,0);
				return true; //empty block
			}
			if(i1==0) {
				loc.add(0,-1,0);
				return true; //empty block
			} //can be empty block
			if(i2==2)return false; //lavaaa!
		}
		return air?i!=2:i==1 && i1==0 && i2==0;
	}
	
	private static int toInt(Position loc) {
		Material a = loc.getBukkitType();
		String c = a.name();
		if(c.contains("LAVA"))return 2;
		if(!TheAPI.isNewVersion()) {
			MaterialData d = loc.getType().toItemStack().getData();
			try {
				if(d instanceof org.bukkit.material.Crops)return 1;
			}catch(Exception | NoClassDefFoundError e) {}
			try {
				if(d instanceof org.bukkit.material.Sapling)return 1;
			}catch(Exception | NoClassDefFoundError e) {}
			try {
				if(d instanceof org.bukkit.material.NetherWarts)return 1;
			}catch(Exception | NoClassDefFoundError e) {}
		}else {
			BlockData d = loc.getBlock().getBlockData();
			if(d instanceof Ageable)return 1;
		}
		return (c.equals("AIR")||c.equals("CAVE_AIR")||c.equals("STRUCTURE_AIR")||c.contains("WATER")||c.contains("BANNER")||c.equals("SEAGRASS") || c.equals("LONG_GRASS") || c.equals("FLOWER")
				|| c.equals("CARPET") || c.contains("BUTTON") || c.contains("DOOR") || c.contains("SIGN")
                || c.contains("TORCH") || isFlower(c) ||c.contains("RED_MUSHROOM") || c.contains("BROWN_MUSHROOM")|| c.contains("PLATE")|| c.contains("GATE") && isOpen(loc))?0:1;
	}
	
	private static boolean isFlower(String c) {
		if(!c.contains("POTTED"))
		return c.equals("PEONY")||c.equals("SUNFLOWER")||c.contains("TULIP")
				||c.equals("DANDELION")||c.equals("DEAD_BUSH")||c.contains("SEAGRASS")||c.equals("GRASS")
				||c.equals("TALL_GRASS")||c.contains("FERN")||c.equals("POPPY")||c.equals("BLUE_ORCHID")
				||c.equals("ALLIUM")||c.equals("AZURE_BLUET")||c.equals("OXEYE_DAISY")||c.equals("CORNFLOWER")
				||c.equals("LILY_OF_THE_VALLEY")||c.equals("WITHER_ROSE")||c.contains("FUNGUS")||c.endsWith("ROOTS")
				||c.equals("SUGAR_CANE")||c.equals("DOUBLE_PLANT")||c.contains("YELLOW_FLOWER")||c.equals("RED_ROSE")
				||c.contains("CORAL") && !c.contains("BLOCK")||c.equals("LILAC")||c.equals("ROSE_BUSH");
		return false;
	}

	private static boolean isOpen(Position loc) {
		return TheAPI.isNewerThan(14)?((org.bukkit.block.data.Openable)loc.getBlock().getBlockData()).isOpen():((Openable)loc.getBukkitType().getNewData((byte)loc.getData())).isOpen();
	}

	public static void send(Player p, String server) {
		ByteArrayDataOutput d = ByteStreams.newDataOutput();
		d.writeUTF("Connect");
		d.writeUTF(server);
		p.sendPluginMessage(plugin, "BungeeCord", d.toByteArray());
	}
}
