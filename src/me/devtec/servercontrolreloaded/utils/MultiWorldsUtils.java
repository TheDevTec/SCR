package me.devtec.servercontrolreloaded.utils;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.worldsapi.WorldsAPI;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Random;

public class MultiWorldsUtils {
	public static void unloadWorld(String w, CommandSender sender) {
		if (Bukkit.getWorld(w) == null)
			return;
		if (WorldsAPI.unloadWorld(w, true)) {
			List<String> worlds = Loader.mw.getStringList("Worlds");
			List<String> ww = Loader.mw.getStringList("Unloaded-Worlds");
			worlds.remove(w);
			ww.add(w);
			Loader.mw.set("Worlds", worlds);
			Loader.mw.set("Unloaded-Worlds", ww);
			Loader.mw.save();
			Loader.sendMessages(sender, "MultiWorld.Unload", Placeholder.c().add("%world%", w));
			return;
		}
	}

	public static void unloadWorlds(CommandSender s) {
		int chunk = 0;
		for (World world : Bukkit.getWorlds()) {
			chunk = chunk + world.getLoadedChunks().length;
			for (Chunk c : world.getLoadedChunks()) {
				c.unload();
			}
		}
		for (World world : Bukkit.getWorlds())
			chunk = chunk - world.getLoadedChunks().length;
		Loader.sendMessages(s, "Chunks.Unload", Placeholder.c().add("%chunks%", chunk+""));
	}

	public static void gamemodeWorldCheck() {
		for (Player on : TheAPI.getOnlinePlayers())
			API.getSPlayer(on).setGamamode();
	}

	public static enum Generator {
		NORMAL, FLAT, NETHER, THE_END, THE_VOID;
	}

	private static boolean exists(String w) {
		return new File(Bukkit.getWorldContainer().getPath() + "/" + w + "/session.lock").exists();
	}

	public static void importWorld(String w, CommandSender s, Generator type) {
		if (Bukkit.getWorld(w) != null)
			return;
		if (exists(w)) {
			if (Bukkit.getWorld(w) != null) {
				Loader.sendMessages(s, "MultiWorld.Loaded", Placeholder.c().add("%world%", w));
			} else if (Loader.mw.getStringList("Unloaded-Worlds").contains(w)
					|| Loader.mw.getStringList("Worlds").contains(w)) {
				Loader.sendMessages(s, "MultiWorld.Exists", Placeholder.c().add("%world%", w));
			} else {
				List<String> wws = Loader.mw.getStringList("Deleted-Worlds");
				List<String> worlds = Loader.mw.getStringList("Worlds");
				switch (type) {
				case FLAT:
					WorldsAPI.create(w, Environment.NORMAL, WorldType.FLAT, true, 0);
					break;
				case NETHER:
					WorldsAPI.create(w, Environment.NETHER, WorldType.NORMAL, true, 0);
					break;
				case NORMAL:
					WorldsAPI.create(w, Environment.NORMAL, WorldType.NORMAL, true, 0);
					break;
				case THE_END:
					WorldsAPI.create(w, Environment.THE_END, WorldType.NORMAL, true, 0);
					break;
				case THE_VOID:
					WorldsAPI.create(w, Environment.NORMAL, null, true, 0);
					break;
				}
				wws.remove(w);
				worlds.add(w);
				Loader.mw.set("Deleted-Worlds", wws);
				Loader.mw.set("Worlds", worlds);
				Loader.mw.save();
				defaultSet(Bukkit.getWorld(w), type.toString());
				Loader.sendMessages(s, "MultiWorld.Import", Placeholder.c().add("%world%", w));
			}
		} else
			Loader.sendMessages(s, "MultiWorld.NotExists", Placeholder.c().add("%world%", w));
	}
	
	public static void defaultSet(World as, String gen) {
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".GameMode", "SURVIVAL");
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Seed", as.getSeed());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Difficulty", as.getDifficulty().name());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".KeepSpawnInMemory", as.getKeepSpawnInMemory());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".AutoSave", as.isAutoSave());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".PvP", as.getPVP());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".CreatePortal", true);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".NoMobs", false);
		try {
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Hardcore", as.isHardcore());
		}catch(NoSuchMethodError e) {}
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".PortalTeleport", true);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".DoFireDamage", true);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".DoDrownDamage", true);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".DoFallDamage", true);
		for(String g : as.getGameRules())
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule."+g, as.getGameRuleValue(g));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Spawn.World", as.getName());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Generator", gen != null ? gen : "none");
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".MonstersSpawnLimit", as.getMonsterSpawnLimit());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".AnimalsSpawnLimit", as.getAnimalSpawnLimit());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".TicksPerAnimalSpawn", as.getTicksPerAnimalSpawns());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".TicksPerMonsterSpawn", as.getTicksPerMonsterSpawns());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Spawn.X", as.getSpawnLocation().getBlockX());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Spawn.Y", as.getSpawnLocation().getBlockY());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Spawn.Z", as.getSpawnLocation().getBlockZ());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Spawn.X_Pos_Head", 90);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Spawn.Z_Pos_Head", 0);
		Loader.mw.save();
		for(String g : as.getGameRules())
			if(Loader.mw.getString("WorldsSettings." + as.getName()+".Gamerule."+g)!=null && !Loader.mw.getString("WorldsSettings." + as.getName() + ".Gamerule." + g).trim().equals(""))
				as.setGameRuleValue(g, Loader.mw.getString("WorldsSettings." + as.getName() + ".Gamerule." + g));
			String dif = Loader.mw.getString("WorldsSettings." + as.getName() + ".Difficulty");
			if(dif!=null)dif=dif.toUpperCase();
			boolean keepspawn = Loader.mw.getBoolean("WorldsSettings." + as.getName() + ".KeepSpawnInMemory");
			boolean autosave = Loader.mw.getBoolean("WorldsSettings." + as.getName() + ".AutoSave");
			boolean pvp = Loader.mw.getBoolean("WorldsSettings." + as.getName() + ".PvP");
			int monster = 0;
			int animal = 0;
			int an = 0;
			int mo = 0;
			if (Loader.mw.getBoolean("ModifyMobsSpawnRates")) {
				monster = Loader.mw.getInt("WorldsSettings." + as.getName() + ".MonstersSpawnLimit");
				animal = Loader.mw.getInt("WorldsSettings." + as.getName() + ".AnimalsSpawnLimit");
				an = Loader.mw.getInt("WorldsSettings." + as.getName() + ".TicksPerAnimalSpawn");
				mo = Loader.mw.getInt("WorldsSettings." + as.getName() + ".TicksPerMonsterSpawn");
			}
			if(dif!=null)
			if (dif.equalsIgnoreCase("EASY") || dif.equalsIgnoreCase("NORMAL") || dif.equalsIgnoreCase("HARD") || dif.equalsIgnoreCase("PEACEFUL")) {
				as.setDifficulty(Difficulty.valueOf(dif.toUpperCase()));
			}else {
				Loader.getInstance.getLogger()
				.warning("Setting Difficulty of world '" + as.getName() + "' is unknown !");
				Loader.getInstance.getLogger().warning("Setting the default difficulty..");
				as.setDifficulty(Difficulty.EASY);
			}
			as.setKeepSpawnInMemory(keepspawn);
			try {
				boolean hard = Loader.mw.getBoolean("WorldsSettings." + as.getName() + ".Hardcore");
				as.setHardcore(hard);
			}catch(NoSuchMethodError e) {}
			as.setAutoSave(autosave);
			as.setPVP(pvp);
			if (Loader.mw.getBoolean("ModifyMobsSpawnRates")) {
				as.setMonsterSpawnLimit(monster); // max spawning of monsters (zombies..) is defaulty 300
				as.setAnimalSpawnLimit(animal); // max spawning of animals (cows..) is defaulty 150
				as.setWaterAnimalSpawnLimit(animal / 2); // max spawning of water animals (squids..) is defaulty 75
				as.setAmbientSpawnLimit((animal / 2) / 2); // max spawning of ambient mobs (bats..) is defaulty 37.5
				as.setTicksPerAnimalSpawns(an);
				as.setTicksPerMonsterSpawns(mo);
			}
	}

	public static void loadWorlds() {
		for (String w : Loader.mw.getStringList("Worlds"))
			loadWorld(w, null);
		for (World wa : Bukkit.getWorlds())
			defaultSet(wa, Loader.mw.getString("WorldsSettings." + wa.getName() + ".Generator"));
	}

	public static void loadWorld(String s, CommandSender sender) {
		if (Bukkit.getWorld(s) != null)
			return;
		List<String> worlds = Loader.mw.getStringList("Worlds");
		List<String> ww = Loader.mw.getStringList("Unloaded-Worlds");
		String biome = Loader.mw.getString("WorldsSettings." + s + ".Generator");
		if(biome==null)biome="NORMAL";
		long seed = Loader.mw.getLong("WorldsSettings." + s + ".Seed");
		if(!Loader.mw.exists("WorldsSettings." + s + ".Seed"))seed=new Random().nextLong();
		if (biome.equalsIgnoreCase("NETHER")) {
			WorldsAPI.create(s, Environment.NETHER, WorldType.NORMAL, true, seed);
		}
		if (biome.equalsIgnoreCase("THE_END")||biome.equalsIgnoreCase("end")) {
			WorldsAPI.create(s, Environment.THE_END, WorldType.NORMAL, true, seed);
		}
		if (biome.equalsIgnoreCase("NORMAL") || biome.equalsIgnoreCase("none")||biome.equalsIgnoreCase("default")) {
			WorldsAPI.create(s, Environment.NORMAL, WorldType.NORMAL, true, seed);
		}
		if (biome.equalsIgnoreCase("FLAT")) {
			WorldsAPI.create(s, Environment.NORMAL, WorldType.FLAT, true, seed);
		}
		if (biome.equalsIgnoreCase("THE_VOID")||biome.equalsIgnoreCase("void")) {
			WorldsAPI.create(s, Environment.NORMAL, null, true, seed);
		}
		if(!worlds.contains(s))
		worlds.add(s);
		ww.remove(s);
		Loader.mw.set("Worlds", worlds);
		Loader.mw.set("Unloaded-Worlds", ww);
		Loader.mw.save();
		defaultSet(Bukkit.getWorld(s), biome);
		if(sender!=null)
		Loader.sendMessages(sender, "MultiWorld.Load", Placeholder.c().add("%world%", s));
	}

	public static void createWorld(String s, CommandSender sender) {
		String biome = Loader.mw.getString("WorldsSettings." + s + ".Generator");
		if(biome==null)biome="NORMAL";
		List<String> wws = Loader.mw.getStringList("Deleted-Worlds");
		List<String> worlds = Loader.mw.getStringList("Worlds");
		if (Bukkit.getWorld(s) != null) {
			Loader.sendMessages(sender, "MultiWorld.Loaded", Placeholder.c().add("%world%", s).replace("%generator%", biome));
			return;
		}else {
			long seed = Loader.mw.getLong("WorldsSettings." + s + ".Seed");
			if(!Loader.mw.exists("WorldsSettings." + s + ".Seed"))seed=new Random().nextLong();
			if (biome.equalsIgnoreCase("NETHER")) {
				WorldsAPI.create(s, Environment.NETHER, WorldType.NORMAL, true, seed);
			}
			if (biome.equalsIgnoreCase("THE_END")||biome.equalsIgnoreCase("end")) {
				WorldsAPI.create(s, Environment.THE_END, WorldType.NORMAL, true, seed);
			}
			if (biome.equalsIgnoreCase("NORMAL") || biome.equalsIgnoreCase("none")||biome.equalsIgnoreCase("default")) {
				WorldsAPI.create(s, Environment.NORMAL, WorldType.NORMAL, true, seed);
			}
			if (biome.equalsIgnoreCase("FLAT")) {
				WorldsAPI.create(s, Environment.NORMAL, WorldType.FLAT, true, seed);
			}
			if (biome.equalsIgnoreCase("THE_VOID")||biome.equalsIgnoreCase("void")) {
				WorldsAPI.create(s, Environment.NORMAL, null, true, seed);
			}
			wws.remove(s);
			worlds.add(s);
			Loader.mw.set("Deleted-Worlds", wws);
			Loader.mw.set("Worlds", worlds);
			Loader.mw.save();
			defaultSet(Bukkit.getWorld(s), biome);
			Loader.sendMessages(sender, "MultiWorld.Create", Placeholder.c().add("%world%", s).replace("%generator%", biome));
		}
	}

	public static String getFlag(World as, String string) {
		if(string.equalsIgnoreCase("GAMEMODE"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".GameMode");
		if(string.equalsIgnoreCase("DIFFICULTY"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".Difficulty");
		if(string.equalsIgnoreCase("KEEP_SPAWN_IN_MEMORY"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".KeepSpawnInMemory");
		if(string.equalsIgnoreCase("AUTOSAVE"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".AutoSave");
		if(string.equalsIgnoreCase("PVP"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".PvP");
		if(string.equalsIgnoreCase("PORTAL_CREATE"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".CreatePortal");
		if(string.equalsIgnoreCase("NO_MOBS"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".NoMobs");
		if(string.equalsIgnoreCase("PORTAL_TELEPORT"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".PortalTeleport");
		if(string.equalsIgnoreCase("DO_FIRE_DAMAGE"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".DoFireDamage");
		if(string.equalsIgnoreCase("DO_DROWNING_DAMAGE"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".DoDrownDamage");
		if(string.equalsIgnoreCase("DO_FALL_DAMAGE"))
			return Loader.mw.getString("WorldsSettings." + as.getName() + ".DoFallDamage");
		for(String g : as.getGameRules())
			if(g.equalsIgnoreCase(string))
				return Loader.mw.getString("WorldsSettings." + as.getName() + ".Gamerule."+g);
		return null;
	}

	public static void setFlag(World as, String string, String value) {
		if(string.equalsIgnoreCase("GAMEMODE"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".GameMode", value);
		if(string.equalsIgnoreCase("DIFFICULTY"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".Difficulty", value);
		if(string.equalsIgnoreCase("KEEP_SPAWN_IN_MEMORY"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".KeepSpawnInMemory", value);
		if(string.equalsIgnoreCase("AUTOSAVE"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".AutoSave", value);
		if(string.equalsIgnoreCase("PVP"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".PvP", value);
		if(string.equalsIgnoreCase("PORTAL_CREATE"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".CreatePortal", value);
		if(string.equalsIgnoreCase("NO_MOBS"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".NoMobs", value);
		if(string.equalsIgnoreCase("PORTAL_TELEPORT"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".PortalTeleport", value);
		if(string.equalsIgnoreCase("DO_FIRE_DAMAGE"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".DoFireDamage", value);
		if(string.equalsIgnoreCase("DO_DROWNING_DAMAGE"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".DoDrownDamage", value);
		if(string.equalsIgnoreCase("DO_FALL_DAMAGE"))
			Loader.mw.set("WorldsSettings." + as.getName() + ".DoFallDamage", value);
		for(String g : as.getGameRules())
			if(g.equalsIgnoreCase(string))
				Loader.mw.set("WorldsSettings." + as.getName() + ".Gamerule."+g, value);
	}
}
