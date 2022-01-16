package me.devtec.scr.modules;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.datakeeper.Data;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class SEconomy implements Economy {
	public static String getEconomyGroup(String p) {
		String world = null;
		Player online = TheAPI.getPlayer(p);
		if (online != null)
			world = online.getWorld().getName();
		else {
			world = Position.fromString(TheAPI.getUser(p).getString("position")).getWorldName();
			if (world == null)world = Bukkit.getWorlds().get(0).getName();
		}
		return getEconomyGroupByWorld(world);
	}

	public static String getEconomyGroupByWorld(String world) {
		for (String f : Loader.config.getKeys("economy.per-world")) {
			if(Loader.config.getStringList("economy.per-world."+f+".list").contains(world))
				return f;
		}
		return "default";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return "SCR_Economy";
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return 0;
	}
	
	@Override
	public String format(double money) {
		String a = a("").format(money);
	    String[] s = a.toLowerCase(Locale.ENGLISH).split(",");
	    if (s.length >= 22) { //Why?...
	      if (a.startsWith("-"))
	        a="-∞";
	      else
	      a="∞";
	    }else
	    if (s.length >= 21)
	      a= a("NOV").format(money/=1.0E60);
	    else
	    if (s.length >= 20)
	    	a= (a("OCT")).format(money/=1.0E57);
	    	else
	    if (s.length >= 19)
	    	a= (a("SEP")).format(money/=1.0E54);
		    else
		if (s.length >= 18)
	    	a= (a("SED")).format(money/=1.0E51);
			else
		if (s.length >= 17)
	    	a= (a("QUI")).format(money/=1.0E48);
			else
		if (s.length >= 16)
	    	a= (a("QUA")).format(money/=1.0E45);
			else
		if (s.length >= 15)
	    	a= (a("tre")).format(money/=1.0E42);
			else
		if (s.length >= 14)
	    	a= (a("duo")).format(money/=1.0E39);
			else
		if (s.length >= 13)
	    	a= (a("und")).format(money/=1.0E36);
			else
		if (s.length >= 12)
	    	a= (a("dec")).format(money/=1.0E33);
			else
		if (s.length >= 11)
	    	a= (a("non")).format(money/=1.0E30);
			else
		if (s.length >= 10)
	    	a= (a("oct")).format(money/=1.0E27);
			else
		if (s.length >= 9)
	    	a= (a("sep")).format(money/=1.0E24);
			else
		if (s.length >= 8) //No, it's not "sex"...
	    	a= (a("sex")).format(money/=1.0E21);
			else
		if (s.length >= 7)
	    	a= (a("qui")).format(money/=1.0E18);
			else
		if (s.length >= 6)
	    	a= (a("qua")).format(money/=1.0E15);
			else
		if (s.length >= 5)
	    	a= (a("t")).format(money/=1.0E12);
			else
		if (s.length >= 4)
	    	a= (a("b")).format(money/=1.0E9);
			else
		if (s.length >= 3)
	    	a= (a("m")).format(money/=1000000);
			else
		if (s.length >= 2)
	    	a= (a("k")).format(money/=1000);
	    return a;
	}
	
	private static Map<String, DecimalFormat> formats = new HashMap<>();
	
	private static DecimalFormat a(String c) { //Let's save some time!
		DecimalFormat decimalFormat = formats.get(c);
		if(decimalFormat==null) {
			formats.put(c, decimalFormat=(DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH));
			decimalFormat.applyPattern("###,###.##"+c);
		}
		return decimalFormat;
	}

	@Override
	public String currencyNamePlural() {
		return "SCR_Money";
	}

	@Override
	public String currencyNameSingular() {
		return "$";
	}

	private String getPaths(String player) {
		String path = "Money";
		if (Loader.config.getBoolean("Options.Economy.MultiEconomy.Use"))
			return path + "." + getEconomyGroup(player);
		return path;
	}
	
	private String get(String player, String world) {
		String path = "Money";
		if (Loader.config.getBoolean("Options.Economy.MultiEconomy.Use"))
			return path + "." + getEconomyGroupByWorld(world);
		return path;
	}

	private boolean existPath(String player) {
		return TheAPI.getUser(player).exists(getPaths(player));
	}

	private boolean existPath(String player, String world) {
		String path = "Money";
		if (Loader.config.getBoolean("Options.Economy.MultiEconomy.Use"))
			path=path + "." + getEconomyGroupByWorld(world);
		return TheAPI.getUser(player).exists(path);
	}

	@Override
	public boolean hasAccount(String s) {
		if (s == null)
			return false;
		return existPath(s);
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer) {
		return hasAccount(offlinePlayer.getName());
	}

	@Override
	public boolean hasAccount(String s, String world) {
		if (s == null)
			return false;
		return !existPath(s, world);
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer, String world) {
		return hasAccount(offlinePlayer.getName(), world);
	}

	@Override
	public double getBalance(String s) {
		if (s == null)
			return 0.0;
		return TheAPI.getUser(s).getDouble(getPaths(s));
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer) {
		return getBalance(offlinePlayer.getName());
	}

	public double getBalanceWithoutCache(String s, String world) {
		if (s == null||world==null)
			return 0.0;
		return new Data("plugins/TheAPI/User/"+TheAPI.getCache().lookupId(s)+".yml").getDouble(get(s, world));
	}

	@Override
	public double getBalance(String s, String world) {
		if (s == null||world==null)
			return 0.0;
		return TheAPI.getUser(s).getDouble(get(s, world));
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer, String world) {
		return getBalance(offlinePlayer.getName(), world);
	}

	@Override
	public boolean has(String s, double v) {
		if (s == null)
			return false;
		double balance = TheAPI.getUser(s).getDouble(getPaths(s));
        return balance >= v;
    }

	@Override
	public boolean has(OfflinePlayer offlinePlayer, double v) {
		return has(offlinePlayer.getName(), v);
	}

	@Override
	public boolean has(String s, String world, double v) {
		if (s == null)
			return false;
		double balance = TheAPI.getUser(s).getDouble(get(s, world));
        return balance >= v;
    }

	@Override
	public boolean has(OfflinePlayer offlinePlayer, String world, double v) {
		return has(offlinePlayer.getName(), world, v);
	}

	@Override
	public EconomyResponse withdrawPlayer(String s, double v) {
		if (s == null)
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + ", player is null");
		if (v < 0) {
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
		} else {
			TheAPI.getUser(s).setAndSave(getPaths(s), getBalance(s) - v);
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.SUCCESS,
					"Succefully withdrawed $" + v + " from player " + s);
		}
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer f, double v) {
		return withdrawPlayer(f.getName(), v);
	}

	@Override
	public EconomyResponse withdrawPlayer(String s, String world, double v) {
		if (s == null)
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + ", player is null");
		if (v < 0) {
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
		} else {
			TheAPI.getUser(s).setAndSave(get(s, world), getBalance(s,world) - v);
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.SUCCESS,
					"Succefully withdrawed $" + v + " from player " + s);
		}
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer f, String world, double v) {
		return withdrawPlayer(f.getName(), world, v);
	}

	@Override
	public EconomyResponse depositPlayer(String s, double v) {
		if (s == null)
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed deposited $" + v + ", player is null");
		if (v < 0) {
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
		} else {
			TheAPI.getUser(s).setAndSave(getPaths(s), getBalance(s) + v);
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.SUCCESS,
					"Succefully deposited $" + v + " to player " + s);
		}
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer f, double v) {
		return depositPlayer(f.getName(), v);
	}
	@Override
	public EconomyResponse depositPlayer(String s, String w, double v) {
		if (s == null)
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed deposited $" + v + ", player is null");
		if (v < 0) {
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
		} else {
			TheAPI.getUser(s).setAndSave(get(s,w), getBalance(s, w) + v);
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.SUCCESS,
					"Succefully deposited $" + v + " to player " + s);
		}
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer f, String world, double v) {
		return depositPlayer(f.getName(), world, v);
	}

	@Override
	public EconomyResponse createBank(String bank, String player) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse createBank(String bank, OfflinePlayer offlinePlayer) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse deleteBank(String bank) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse bankBalance(String bank) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse bankHas(String bank, double money) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse bankWithdraw(String bank, double money) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse bankDeposit(String bank, double money) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse isBankOwner(String bank, String player) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse isBankOwner(String bank, OfflinePlayer offlinePlayer) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse isBankMember(String bank, String player) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public EconomyResponse isBankMember(String bank, OfflinePlayer offlinePlayer) {
		return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported !");
	}

	@Override
	public List<String> getBanks() {
		return null;
	}

	@Override
	public boolean createPlayerAccount(String s) {
		if (s == null)
			return false;
		if (!existPath(s)) {
			TheAPI.getUser(s).setAndSave(getPaths(s), Loader.config.getDouble("economy.start-money"));
			return true;
		}
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer f) {
		return createPlayerAccount(f.getName());
	}

	@Override
	public boolean createPlayerAccount(String s, String world) {
		if (!existPath(s, world)) {
			TheAPI.getUser(s).setAndSave(get(s, world), Loader.config.getDouble("economy.start-money"));
			return true;
		}
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer f, String world) {
		return createPlayerAccount(f.getName(), world);
	}
}