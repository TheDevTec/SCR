package me.devtec.scr.api;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.devtec.shared.API;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;
import me.devtec.shared.utility.StringUtils.FormatType;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class ScrEconomy extends AbstractEconomy {
	private static Pattern moneyPattern = Pattern.compile("([+-]*[0-9]+.*[0-9]*[E]*[0-9]*)([kmbt]|qu[ia]|se[px]|non|oct|dec|und|duo|tre|sed|nov)", Pattern.CASE_INSENSITIVE);
	
	Config config;
	//Per world economy
	boolean pwe;
	public ScrEconomy(Config config) {
		this.config=config;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return "ScrEconomy";
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return 2;
	}

	@Override
	public String format(double balance) {
		String format = config.getString("format").replace("{0}", currencyNamePlural());
		if(format.contains("{1}"))format=format.replace("{1}", balance+"");
		if(format.contains("{2}")) {
			return format.replace("{2}", StringUtils.formatDouble(FormatType.BASIC, balance));
		}
		if(format.contains("{3}")) {
			return format.replace("{3}", StringUtils.formatDouble(FormatType.NORMAL, balance));
		}
		if(format.contains("{4}")) {
			return format.replace("{4}", StringUtils.formatDouble(FormatType.COMPLEX, balance));
		}
		return format;
	}

	@Override
	public String currencyNamePlural() {
		return config.getString("currencySymbol");
	}

	@Override
	public String currencyNameSingular() {
		return config.getString("currencyName");
	}

	@Override
	public boolean hasAccount(String name) {
		if(pwe)return hasAccount(name, economyGroup(API.getUser(name).getString("disconnectWorld")));
		return API.getUser(name).exists("scr.economy");
	}

	@Override
	public boolean hasAccount(String name, String world) {
		if(!pwe)return hasAccount(name);
		return API.getUser(name).exists("scr.economy."+world);
	}

	@Override
	public double getBalance(String name) {
		if(pwe)return getBalance(name, economyGroup(API.getUser(name).getString("disconnectWorld")));
		return API.getUser(name).getDouble("scr.economy");
	}

	@Override
	public double getBalance(String name, String world) {
		if(!pwe)return getBalance(name);
		return API.getUser(name).getDouble("scr.economy."+world);
	}

	@Override
	public boolean has(String name, double balance) {
		return getBalance(name) >= balance;
	}

	@Override
	public boolean has(String name, String world, double balance) {
		return getBalance(name, world) >= balance;
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, double balance) {
		if(balance < 0)
			return new EconomyResponse(balance, getBalance(name), ResponseType.FAILURE, "Withdraw balance must be higher or equals to 0.");
		if(pwe)return withdrawPlayer(name, economyGroup(API.getUser(name).getString("disconnectWorld")), balance);
		Config user = API.getUser(name);
		double playerBal = user.getDouble("scr.economy") + balance;
		user.set("scr.economy", playerBal);
		return new EconomyResponse(balance, getBalance(name), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, String world, double balance) {
		if(balance < 0)
			return new EconomyResponse(balance, getBalance(name), ResponseType.FAILURE, "Withdraw balance must be higher or equals to 0.");
		if(!pwe)return withdrawPlayer(name, balance);
		Config user = API.getUser(name);
		String group = economyGroup(world);
		double playerBal = user.getDouble("scr.economy."+group) - balance;
		user.set("scr.economy."+group, playerBal);
		return new EconomyResponse(balance, getBalance(name), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(String name, double balance) {
		if(balance < 0)
			return new EconomyResponse(balance, getBalance(name), ResponseType.FAILURE, "Deposit balance must be higher or equals to 0.");
		if(pwe)return depositPlayer(name, API.getUser(name).getString("disconnectWorld"), balance);
		Config user = API.getUser(name);
		double playerBal = user.getDouble("scr.economy") + balance;
		user.set("scr.economy", playerBal);
		return new EconomyResponse(balance, getBalance(name), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(String name, String world, double balance) {
		if(balance < 0)
			return new EconomyResponse(balance, getBalance(name), ResponseType.FAILURE, "Deposit balance must be higher or equals to 0.");
		if(!pwe)return depositPlayer(name, balance);
		Config user = API.getUser(name);
		String group = economyGroup(world);
		double playerBal = user.getDouble("scr.economy."+group) + balance;
		user.set("scr.economy."+group, playerBal);
		return new EconomyResponse(balance, playerBal, ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse createBank(String name, String world) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not implemented.");
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not implemented.");
	}

	@Override
	public EconomyResponse bankBalance(String name) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not implemented.");
	}

	@Override
	public EconomyResponse bankHas(String name, double balance) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not implemented.");
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double balance) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not implemented.");
	}

	@Override
	public EconomyResponse bankDeposit(String name, double balance) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not implemented.");
	}

	@Override
	public EconomyResponse isBankOwner(String name, String world) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not implemented.");
	}

	@Override
	public EconomyResponse isBankMember(String name, String world) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Banks are not implemented.");
	}

	@Override
	public List<String> getBanks() {
		return Collections.emptyList();
	}

	@Override
	public boolean createPlayerAccount(String name) {
		Config user = API.getUser(name);
		if(user.existsKey("scr.economy")) {
			return false;
		}
		user.set("scr.economy", startingMoney(name, null));
		return true;
	}

	@Override
	public boolean createPlayerAccount(String name, String world) {
		if(!pwe)return createPlayerAccount(name);
		Config user = API.getUser(name);
		if(user.existsKey("scr.economy."+world)) {
			return false;
		}
		user.set("scr.economy."+world, startingMoney(name, world));
		return true;
	}

	private double startingMoney(String name, String world) {
		if(pwe && world!=null) {
			return config.getDouble("startingMoney.perWorld."+economyGroup(world));
		}
		return config.getDouble("startingMoney.default");
	}

	private String economyGroup(String world) {
		for(String group : config.getKeys("perWorldEconomy.groups"))
			if(config.getStringList("perWorldEconomy.groups."+group).contains(world))
				return group;
		return "default";
	}
	
	public static double balanceFromString(String text) {
		double has = 0;
		Matcher m = moneyPattern.matcher(text);
		while(m.find())
			has+=StringUtils.getDouble(m.group(1))*getMultiply(m.group(2));
		if(has==0)has=StringUtils.getDouble(text);
		return has;
	}
	
	private static double getMultiply(String name) {
    	switch(name) {
    	case "k":
    		return 1000;
    	case "m":
    		return 1000000;
    	case "b":
    		return 1.0E9;
    	case "t":
    		return 1.0E12;
    	case "qua":
    		return 1.0E15;
    	case "qui":
    		return 1.0E18;
    	case "sex": //No, it's not "sex"...
    		return 1.0E21;
    	case "sep":
    		return 1.0E24;
    	case "oct":
    		return 1.0E27;
    	case "non":
    		return 1.0E30;
    	case "dec":
    		return 1.0E33;
    	case "und":
    		return 1.0E36;
    	case "duo":
    		return 1.0E39;
    	case "tre":
    		return 1.0E42;
    	case "QUA":
    		return 1.0E45;
    	case "QUI":
    		return 1.0E48;
    	case "SED":
    		return 1.0E51;
    	case "SEP":
    		return 1.0E54;
    	case "OCT":
    		return 1.0E57;
    	case "NOV":
    		return 1.0E60;
    	}
    	return 1;
    }
}
