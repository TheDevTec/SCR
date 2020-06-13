package Utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import me.DevTec.ConfigAPI;
import me.DevTec.TheAPI;
import me.DevTec.Other.User;

public class Converter {
	public static void convert() {
		if(new File("plugins/ServerControlReloaded/ChatMe.yml").exists()) {
			ConfigAPI me = TheAPI.getConfig("ServerControlReloaded", "ChatMe");
			if(!me.exist("Players")) {
				Bukkit.getConsoleSender().sendMessage("Players neexistuje!");
				return;
			}
			me.create();
			for(String playername: me.getConfigurationSection("Players").getKeys(false)) {
				User user = TheAPI.getUser(playername);
				ConfigurationSection sec = me.getConfigurationSection("Players."+playername);
				for(String s : sec.getKeys(true))
					user.set(s, sec.get(s));
				user.save();
			}
			me.delete();
		}
		
	}
}
