package me.devtec.servercontrolreloaded.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Eco implements Economy {
	public static String getEconomyGroup(String p) {
		String world = TheAPI.getUser(p).getString("DisconnectWorld");
		Player online = TheAPI.getPlayer(p);
		if (online != null)
			world = online.getWorld().getName();
		if (world == null)
			world = Bukkit.getWorlds().get(0).getName();
		return getEconomyGroupByWorld(world);
	}

	public static String getEconomyGroupByWorld(String world) {
		for (String f : Loader.config.getKeys("Options.Economy.MultiEconomy.Types")) {
			if(Loader.config.getStringList("Options.Economy.MultiEconomy.Types."+f).contains(world))
				return f;
		}
		return "default";
	}

	@Override
	public boolean isEnabled() {
		return Loader.getInstance.isEnabled() && Loader.econ != null;
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
	public String format(double v) {
		return API.setMoneyFormat(v, false);
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

	private String get(String player) {
		return getPaths(player);
	}

	private String get(String player, String world) {
		String path = "Money";
		if (Loader.config.getBoolean("Options.Economy.MultiEconomy.Use"))
			return path + "." + getEconomyGroupByWorld(world);
		return path;
	}

	private boolean existPath(String player) {
		return TheAPI.getUser(player).exist(get(player, getEconomyGroup(player)));
	}

	private boolean existPath(String player, String world) {
		return TheAPI.getUser(player).exist(get(player, getEconomyGroupByWorld(world)));
	}

	@Override
	public boolean hasAccount(String s) {
		if (s == null)
			return false;
		return !existPath(s);
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
		return TheAPI.getUser(s).getDouble(get(s));
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer) {
		return getBalance(offlinePlayer.getName());
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
		double balance = TheAPI.getUser(s).getDouble(get(s));
		if (balance >= v)
			return true;
		return false;
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
		if (balance >= v)
			return true;
		return false;
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
			Loader.EconomyLog("Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
		} else {
			TheAPI.getUser(s).setAndSave(get(s), getBalance(s) - v);
			
			Loader.EconomyLog("Succefully withdrawed $" + v + " from player " + s);
			
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
			Loader.EconomyLog("Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
		} else {
			TheAPI.getUser(s).setAndSave(get(s, world), getBalance(s,world) - v);
			Loader.EconomyLog("Succefully withdrawed $" + v + " from player " + s);
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
			Loader.EconomyLog("Failed deposited $" + v + " to player " + s + ", you can't deposite negative amount");
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
		} else {
			TheAPI.getUser(s).setAndSave(get(s), getBalance(s) + v);
			Loader.EconomyLog("Succefully deposited $" + v + " from player " + s);
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
			Loader.EconomyLog("Failed deposited $" + v + " to player " + s + ", you can't deposite negative amount");
			return new EconomyResponse(v, v, EconomyResponse.ResponseType.FAILURE,
					"Failed withdrawed $" + v + " from player " + s + ", you can't withdraw negative amount");
		} else {
			TheAPI.getUser(s).setAndSave(get(s,w), getBalance(s, w) + v);
			Loader.EconomyLog("Succefully deposited $" + v + " from player " + s);
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
			TheAPI.getUser(s).setAndSave(get(s), Loader.config.getDouble("Options.Economy.Money"));
			Loader.EconomyLog("Creating economy account for player " + s);
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
			TheAPI.getUser(s).setAndSave(get(s, world), Loader.config.getDouble("Options.Economy.Money"));
			Loader.EconomyLog("Creating economy account for player " + s);
			return true;
		}
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer f, String world) {
		return createPlayerAccount(f.getName(), world);
	}
}