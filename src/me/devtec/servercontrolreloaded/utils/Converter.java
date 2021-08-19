package me.devtec.servercontrolreloaded.utils;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.datakeeper.User;

import java.io.File;

public class Converter {
	public static void convert() {
		if(new File("plugins/ServerControlReloaded/ChatMe.yml").exists() || new File("plugins/ServerControlReloaded/ChatMe.dat").exists()) {
			Config me = new Config(new File("plugins/ServerControlReloaded/ChatMe.yml").exists()?"ServerControlReloaded/ChatMe.yml":"ServerControlReloaded/ChatMe.dat");
			if(!me.exists("Players")) {
				me.getData().getFile().delete();
			}else {
			for(String playername: me.getKeys("Players")) {
				User user = TheAPI.getUser(playername);
				for(String s : me.getKeys("Players."+playername,true)) {
					if(s.startsWith("Homes")) {
						if(!s.endsWith("Homes")) continue;
						for(String home : me.getKeys("Players."+playername+".Homes")) {
							double x = me.getDouble("Players."+playername+".Homes."+home+".X");
							double y = me.getDouble("Players."+playername+".Homes."+home+".Y");
							double z = me.getDouble("Players."+playername+".Homes."+home+".Z");
							float yaw = (float)me.getLong("Players."+playername+".Homes."+home+".Yaw");
							float pitch = (float)me.getLong("Players."+playername+".Homes."+home+".Pitch");
							user.set("Homes."+home, new Position(me.getString("Players."+playername+".Homes."+home+".World"), x, y, z, yaw, pitch).toString());
						}
					}else
					user.set(s, me.get("Players."+playername+"."+s));
				}
				user.save();
			}
			me.getData().getFile().delete();
		}}
		if(new File("plugins/ServerControlReloaded/Config.yml").exists()) {
			if(Loader.config.exists("Spawn") && Loader.config.get("Spawn") == null) { //old format
				Position n = null;
				try {
				n=new Position(Loader.config.getString("Spawn.World"), Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y"), Loader.config.getDouble("Spawn.Z"), Loader.config.getData().getFloat("Spawn.X_Pos_Head"), Loader.config.getData().getFloat("Spawn.Z_Pos_Head"));
				}catch(Exception er) {}
				Loader.config.set("Spawn.World", null);
				Loader.config.set("Spawn.X", null);
				Loader.config.set("Spawn.Y", null);
				Loader.config.set("Spawn.Z", null);
				Loader.config.set("Spawn.X_Pos_Head", null);
				Loader.config.set("Spawn.Z_Pos_Head", null);
				Loader.config.set("Spawn", n!=null?n.toString():null);
				Loader.config.save();
			}
		}
	}
}
