package me.devtec.scr.utils;

import me.devtec.shared.dataholder.Config;
import net.milkbowl.vault.economy.Economy;

public interface ISuser {

	boolean isAutorized(String permission); // true/false

	boolean checkPerm(String permissin); // true/false + Also sends noPerm message

	// cooldowns
	boolean cooldownExpired(String cooldownpath, String cooldowntime); // true/false
	long expires(String cooldownpath, String cooldowntime);
	void newCooldown(String cooldownpath);

	boolean isConsole();

	Config getUserConfig();
	
	Economy getEconomy();

	//NICKNAME
	String getName();
	String getRealName();
	boolean haveNickname();
	void resetNickname();
	void setNickname(String nick);
	
	//IGNORE
	boolean isIgnoring(String target);
	public void addIgnore(String target);
	public void removeIgnore(String target);
	
	
}
