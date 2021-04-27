package me.DevTec.ServerControlReloaded.SCR;

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
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.blocksapi.BlockIterator;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.nms.NMSAPI;
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
			List<String> homes = new ArrayList<>();
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
			new Thread(new Runnable() {
				public void run() {
					Position safe = findSafeLocation(air,location);
					if(safe!=null) {
						s.setNoDamageTicks(60);
						NMSAPI.postToMainThread(new Runnable() {
							@Override
							public void run() {
								s.teleport(safe.toLocation());
							}
						});
					}
					else
						Loader.sendMessages(s, "TpSystem.NotSafe");
				}
			}).start();
	}
	
	public static boolean isSafe(Position loc) {
		return isSafe(false,loc);
	}
	
	public static Position findSafeLocation(Position start) {
		return findSafeLocation(false,start);
	}
	
	public static Position findSafeLocation(boolean canBeAir, Position start) {
		Position f = start.clone();
		double oldDistance = 100;
		BlockIterator g = new BlockIterator(start.clone().add(10,10,10),start.clone().add(-10,-10,-10));
		while(g.has()) {
			Position a1 = g.get().clone();
			double distance = start.distanceSquared(a1);
			if(distance <= oldDistance) {
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
		return oldDistance!=100?Position.fromString(("[Position:" + start.getWorldName() + 
				"/" + (f.getX()+"").split("\\.")[0]+"."+(start.getX()+"").split("\\.")[1] + "/" + 
				f.getY()+ "/" 
				+ (f.getZ()+"").split("\\.")[0]+"."+(start.getZ()+"").split("\\.")[1] + "/"
				+ start.getYaw() + "/" + start.getPitch() + "]").replace(".", ":")):null;
	}

	public static boolean isSafe(boolean air, Position loc) {
		Position down = loc.clone().add(0, -1, 0);
		int i = toInt(down);
		Position middle = loc.clone();
		int i1 = toInt(middle);
		Position top = loc.clone().add(0, 1, 0);
		int i2 = toInt(top);
		if(i1==3) {
			String c = middle.getBukkitType().name();
			if(c.equals("SNOW")) {
			int stage = getSnowLevel(middle);
			if(stage<=2) { //lowest
				c = top.getBukkitType().name();
				if(c.contains("SLAB")||c.contains("STEP"))
					return getSlabLevel(down)==0; //only top
				return false;
			}
			}
			return false;
		}
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
			if(!TheAPI.isNewVersion())return false;
			String c = down.getBukkitType().name();
			if(c.contains("SLAB")||c.contains("STEP")) {
				if(i1!=0)return false; //must be empty block
				if(getSlabLevel(down)==1) //button
					return true; //can be everything
				if(getSlabLevel(down)==0) { //top
					if(i2==1)return false; //solid block
					c = top.getBukkitType().name();
					if(c.contains("SLAB")||c.contains("STEP"))
						return getSlabLevel(top)==0;
					return false;
				}
			}else { //snow
				int stage = getSnowLevel(middle);
				if(stage<=2) { //lowest
					c = top.getBukkitType().name();
					if(c.contains("SLAB")||c.contains("STEP"))
						return getSlabLevel(down)==0; //only top
					return false;
				}
				if(stage<=5) { //low
					if(i1!=0)return false; //must be empty block
					return true;
				}
				if(i1!=0)return false; //must be empty block
				c = top.getBukkitType().name();
				if(c.contains("SLAB")||c.contains("STEP"))
					return getSlabLevel(down)==0; //only top
				return false;
			}
		}
		return air?i!=2:i==1 && i1==0 && i2==0;
	}
	
	private static int toInt(Position loc) {
		Material a = loc.getBukkitType();
		String c = a.name();
		if(c.contains("LAVA"))return 2;
		if(!TheAPI.isNewVersion()) {
			MaterialData d = loc.getType().toItemStack().getData();
			if(d instanceof Crops||d instanceof org.bukkit.material.Sapling||d instanceof org.bukkit.material.NetherWarts)return 1;
		}else {
			BlockData d = loc.getBlock().getBlockData();
			if(d instanceof Ageable)return 1;
		}
		if(c.contains("AIR")||c.contains("WATER")||c.contains("BANNER")||c.equals("SEAGRASS") || c.equals("LONG_GRASS") || c.equals("FLOWER")
				|| c.equals("CARPET") || c.contains("BUTTON") || c.contains("DOOR") || c.contains("SIGN")
                || c.contains("TORCH") || isFlower(c) ||c.contains("RED_MUSHROOM") || c.contains("BROWN_MUSHROOM")|| c.contains("PLATE")|| c.contains("GATE") && isOpen(loc))return 0;
		return ((c.contains("SLAB")||c.contains("STEP")) && getSlabLevel(loc)!=2 || a==Material.SNOW&& getSnowLevel(loc)!=8)?3:1;
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

	private static int getSlabLevel(Position pos) {
		Material a = pos.getBukkitType();
		String c = a.name();
		if(c.contains("SLAB")) {
		if(TheAPI.isNewVersion())
			return ((Slab)pos.getBlock().getBlockData()).getType().ordinal();
		if(pos.getBukkitType().name().contains("DOUBLE_SLAB")||pos.getBukkitType().name().contains("DOUBLE_STEP"))
			return 2;
		return pos.getData();
		}
		return 0;
	}

	private static int getSnowLevel(Position pos) {
		return TheAPI.isNewVersion()?(((Snow)pos.getBlock().getBlockData()).getLayers()):pos.getData();
	}

	public static void send(Player p, String server) {
		ByteArrayDataOutput d = ByteStreams.newDataOutput();
		d.writeUTF("Connect");
		d.writeUTF(server);
		p.sendPluginMessage(plugin, "BungeeCord", d.toByteArray());
	}
}
