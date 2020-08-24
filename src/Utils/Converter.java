package Utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.ConfigAPI.ConfigAPI;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class Converter {
	public static void convert() {
		if(new File("plugins/ServerControlReloaded/ChatMe.yml").exists()) {
			ConfigAPI me = TheAPI.getConfig("ServerControlReloaded", "ChatMe");
			if(!me.exist("Players")) {
				Bukkit.getConsoleSender().sendMessage("Players dosn't exist!");
				return;
			}
			me.create();
			for(String playername: me.getConfigurationSection("Players").getKeys(false)) {
				User user = TheAPI.getUser(playername);
				ConfigurationSection sec = me.getConfigurationSection("Players."+playername);
				for(String s : sec.getKeys(true)) {
					if(s.startsWith("Homes")) {
						if(!s.endsWith("Homes")) continue;
						for(String home : me.getConfigurationSection("Players."+playername+".Homes").getKeys(false)) {
							ConfigAPI mee = TheAPI.getConfig("ServerControlReloaded", "ChatMe");
							double x = mee.getDouble("Players."+playername+".Homes."+home+".X");
							double y = mee.getDouble("Players."+playername+".Homes."+home+".Y");
							double z = mee.getDouble("Players."+playername+".Homes."+home+".Z");
							float yaw = (float)mee.getLong("Players."+playername+".Homes."+home+".Yaw");
							float pitch = (float)mee.getLong("Players."+playername+".Homes."+home+".Pitch");
							user.set("Homes."+home, new Position(mee.getString("Players."+playername+".Homes."+home+".World"), x, y, z, yaw, pitch).toString());
						}
					continue;
					}else
					user.set(s, sec.get(s));
				}
				user.save();
			}
			me.delete();
		}
		
	}
}
