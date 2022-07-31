package me.devtec.scr.utils;

import me.devtec.shared.dataholder.Config;

public interface sUser {
	
	boolean isAutorized(String permission); // true/false
	
	boolean checkPerm(String permissin); // true/false + Also sends noPerm message
	
	//cooldowns
	boolean cooldownExpired(String cooldownpath, String cooldowntime); // true/false
	
	long expires(String cooldownpath, String cooldowntime);
	
	void newCooldown(String cooldownpath);
	
	
	boolean isConsole();
	
	Config getUserConfig();

}
