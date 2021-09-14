package me.devtec.servercontrolreloaded.utils;

import java.io.File;

import org.bukkit.Bukkit;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.datakeeper.User;

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
					if(s.startsWith("Homes.")) {
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
				user.setAutoUnload(true);
			}
			me.getData().getFile().delete();
		}}
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
		boolean saveMw = false;
		if(Loader.mw.get("Worlds")!=null) { //old format
			saveMw = true;
			Loader.mw.set("worlds", Loader.mw.getStringList("Worlds"));
			Loader.mw.set("Worlds", null);
		}
		if(Loader.mw.exists("Deleted-Worlds")) { //old format
			saveMw = true;
			Loader.mw.remove("Deleted-Worlds");
		}
		if(Loader.mw.exists("Unloaded-Worlds")) { //old format
			saveMw = true;
			Loader.mw.remove("Unloaded-Worlds");
		}
		if(Loader.mw.exists("WorldsSettings")) { //old format
			saveMw = true;
			for(String key : Loader.mw.getKeys("WorldsSettings")) {
				String path = "settings."+key+".", path2 = "WorldsSettings."+key+".";
				Loader.mw.addDefault(path + "seed", Loader.mw.getLong(path2 + "Seed"));
				Loader.mw.addDefault(path + "generator", Loader.mw.getString(path2 + "Generator"));
				Loader.mw.addDefault(path + "autoSave", Loader.mw.getBoolean(path2 + "AutoSave"));
				Loader.mw.addDefault(path + "gamemode", Loader.mw.getString(path2 + "GameMode"));
				Loader.mw.addDefault(path + "difficulty", Loader.mw.getString(path2 + "Difficulty"));
				Loader.mw.addDefault(path + "pvp", Loader.mw.getBoolean(path2 + "PvP"));
				if(Loader.mw.getBoolean(path2 + "Hardcore"))
					Loader.mw.addDefault(path + "hardCore", Loader.mw.getBoolean(path2 + "Hardcore"));
				Loader.mw.addDefault(path + "keepSpawnInMemory", Loader.mw.getBoolean(path2 + "KeepSpawnInMemory"));
				Loader.mw.addDefault(path + "portals.create", Loader.mw.getBoolean(path2 + "CreatePortal"));
				Loader.mw.addDefault(path + "portals.teleport", Loader.mw.getBoolean(path2 + "PortalTeleport"));
				if(TheAPI.isOlderThan(13)) {
					Loader.mw.addDefault(path + "drownDamage", Loader.mw.getBoolean(path2 + "DoDrownDamage"));
					Loader.mw.addDefault(path + "fireDamage", Loader.mw.getBoolean(path2 + "DoFireDamage"));
					Loader.mw.addDefault(path + "fallDamage", Loader.mw.getBoolean(path2 + "DoFallDamage"));
				}
				for(String rule : Bukkit.getWorlds().get(0).getGameRules())
					if(Loader.mw.get(path2+"Gamerule."+rule)!=null)
						Loader.mw.addDefault(path+"gamerule."+rule, Loader.mw.get(path2+"Gamerule."+rule));
				Loader.mw.addDefault(path + "spawn", new Position(key, Loader.mw.getDouble(path2+"Spawn.X"), Loader.mw.getDouble(path2+"Spawn.Y"), Loader.mw.getDouble(path2+"Spawn.Z"), Loader.mw.getFloat(path2+"Spawn.X_Pos_Head"), Loader.mw.getFloat(path2+"Spawn.Z_Pos_Head")).toString());
			}
			Loader.mw.remove("WorldsSettings");
		}
		if(saveMw)
			Loader.mw.save();
	}
}
