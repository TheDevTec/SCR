package me.devtec.servercontrolreloaded.utils.multiworlds;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.worldsapi.WorldsAPI;
import me.devtec.theapi.worldsapi.voidGenerator;
import me.devtec.theapi.worldsapi.voidGenerator_1_8;

public class MWAPI {
	public static final Class<?> cc = Ref.nms("WorldSettings$EnumGamemode")==null?Ref.nmsOrOld("world.level.EnumGamemode","EnumGamemode"):Ref.nms("WorldSettings$EnumGamemode");
	public static final Method getById = Ref.method(cc, "getById", int.class);
	public static final Map<World, Object> gamemodesnms = new HashMap<>();
	public static final Map<World, GameMode> gamemodes = new HashMap<>();
	
	public static void unloadWorld(String w, CommandSender sender) {
		if (Bukkit.getWorld(w) == null)
			return;
		if (WorldsAPI.unloadWorld(w, true)) {
			List<String> worlds = Loader.mw.getStringList("worlds");
			worlds.remove(w);
			Loader.mw.set("worlds", worlds);
			Loader.mw.save();
			Loader.sendMessages(sender, "MultiWorld.Unload", Placeholder.c().add("%world%", w));
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

	public enum Generator {
		NORMAL, FLAT, NETHER, THE_END, THE_VOID
	}

	private static boolean exists(String w) {
		return new File(Bukkit.getWorldContainer().getPath() + "/" + w + "/session.lock").exists();
	}

	public static void importWorld(String w, CommandSender s, String type) {
		if (Bukkit.getWorld(w) != null)
			return;
		if (exists(w)) {
			if (Bukkit.getWorld(w) != null) {
				Loader.sendMessages(s, "MultiWorld.Loaded", Placeholder.c().add("%world%", w));
			} else if (Loader.mw.getStringList("worlds").contains(w)) {
				Loader.sendMessages(s, "MultiWorld.Exists", Placeholder.c().add("%world%", w));
			} else {
				List<String> worlds = Loader.mw.getStringList("worlds");
				boolean create = false;
				switch (type) {
				case "FLAT":
					create = true;
					WorldsAPI.create(w, Environment.NORMAL, WorldType.FLAT, true, 0);
					break;
				case "NETHER":
					create = true;
					WorldsAPI.create(w, Environment.NETHER, WorldType.NORMAL, true, 0);
					break;
				case "NORMAL":
					create = true;
					WorldsAPI.create(w, Environment.NORMAL, WorldType.NORMAL, true, 0);
					break;
				case "THE_END":
					create = true;
					WorldsAPI.create(w, Environment.THE_END, WorldType.NORMAL, true, 0);
					break;
				case "THE_VOID":
					create = true;
					WorldsAPI.create(w, Environment.NORMAL, null, true, 0);
					break;
				}
				if(!create) {
					ChunkGenerator g = null;
					if(type.contains(":")) {
						g=Bukkit.getPluginManager().getPlugin(type.split(":")[0]).getDefaultWorldGenerator(w, type.split(":")[1]);
					}else
						for(Plugin p : Bukkit.getPluginManager().getPlugins())
							g=p.getDefaultWorldGenerator(w, type);
					if(g!=null) {
						WorldsAPI.create(w, Environment.NORMAL, WorldType.NORMAL, g, true, 0);
					}
				}
				worlds.add(w);
				Loader.mw.set("worlds", worlds);
				Loader.mw.save();
				defaultSet(Bukkit.getWorld(w), type.toString());
				Loader.sendMessages(s, "MultiWorld.Import", Placeholder.c().add("%world%", w));
			}
		} else
			Loader.sendMessages(s, "MultiWorld.NotExists", Placeholder.c().add("%world%", w));
	}
	
	public static void defaultSet(World as, String gen) {
		String path = "settings." + as.getName() + ".";
		if(gen==null) { //lookup for gen
			if(as.getGenerator() instanceof voidGenerator || as.getGenerator() instanceof voidGenerator_1_8) {
				gen="THE_VOID";
			}else
				if(as.getEnvironment()==Environment.THE_END)
					gen="THE_END";
			else
				if(as.getEnvironment()==Environment.NETHER)
					gen="NETHER";
			else
				if(as.getEnvironment()==Environment.NORMAL && as.getWorldType()==WorldType.FLAT)
					gen="FLAT";
				else gen="DEFAULT";
		}
		Loader.mw.addDefault(path + "seed", as.getSeed());
		Loader.mw.addDefault(path + "generator", gen);
		Loader.mw.addDefault(path + "gamemode", "SURVIVAL");
		Loader.mw.addDefault(path + "pvp", as.getPVP());
		Loader.mw.addDefault(path + "autoSave", as.isAutoSave());
		Loader.mw.addDefault(path + "difficulty", as.getDifficulty().name());
		try {
			Loader.mw.addDefault(path + "hardCore", as.isHardcore());
		}catch(NoSuchMethodError e) {}
		Loader.mw.addDefault(path + "keepSpawnInMemory", as.getKeepSpawnInMemory());
		Loader.mw.addDefault(path + "spawnLimits.modify", false);
		Loader.mw.addDefault(path + "spawnLimits.ambient", as.getAmbientSpawnLimit());
		Loader.mw.addDefault(path + "spawnLimits.animal", as.getAnimalSpawnLimit());
		Loader.mw.addDefault(path + "spawnLimits.monster", as.getMonsterSpawnLimit());
		Loader.mw.addDefault(path + "spawnLimits.waterAmbient", as.getWaterAmbientSpawnLimit());
		Loader.mw.addDefault(path + "spawnLimits.waterAnimal", as.getWaterAnimalSpawnLimit());
		Loader.mw.addDefault(path + "spawnTicks.modify", false);
		Loader.mw.addDefault(path + "spawnTicks.ambient", as.getTicksPerAmbientSpawns());
		Loader.mw.addDefault(path + "spawnTicks.animal", as.getTicksPerAnimalSpawns());
		Loader.mw.addDefault(path + "spawnTicks.monster", as.getTicksPerMonsterSpawns());
		Loader.mw.addDefault(path + "spawnTicks.waterAmbient", as.getTicksPerWaterAmbientSpawns());
		Loader.mw.addDefault(path + "spawnTicks.waterAnimal", as.getTicksPerWaterSpawns());
		Loader.mw.addDefault(path + "allow.monsters", as.getAllowMonsters());
		Loader.mw.addDefault(path + "allow.animals", as.getAllowAnimals());
		for(String rule : as.getGameRules())
			Loader.mw.addDefault(path+"gamerule."+rule.toLowerCase(), as.getGameRuleValue(rule));
		
		//ADDITIONAL
		Loader.mw.addDefault(path + "portals.create", true);
		Loader.mw.addDefault(path + "portals.teleport", true);
		Loader.mw.addDefault(path + "disableDrops.blocks", false);
		Loader.mw.addDefault(path + "disableDrops.entities", false);
		Loader.mw.addDefault(path + "disableDrops.players", false);
		Loader.mw.addDefault(path + "modifyWorld.placeBlock", true);
		Loader.mw.addDefault(path + "modifyWorld.breakBlock", true);
		Loader.mw.addDefault(path + "modifyWorld.interact.blocks", true);
		Loader.mw.addDefault(path + "modifyWorld.interact.entities", true);
		Loader.mw.addDefault(path + "modifyWorld.pickupItem", true);
		Loader.mw.addDefault(path + "modifyWorld.dropItem", true);
		Loader.mw.addDefault(path + "modifyWorld.decayLeaves", true);
		if(TheAPI.isOlderThan(13)) {
			Loader.mw.addDefault(path + "drownDamage", true);
			Loader.mw.addDefault(path + "fireDamage", true);
			Loader.mw.addDefault(path + "fallDamage", true);
		}
		
		Loader.mw.addDefault(path + "spawn", new Position(as.getSpawnLocation()).toString());
		Loader.mw.save();

		Position spawn = Position.fromString(Loader.mw.getString(path + "spawn"));
		try {
			as.setSpawnLocation(spawn.toLocation());
		}catch(NoSuchMethodError err) {
			as.setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
		}
		
		gamemodesnms.put(as, Ref.invokeStatic(getById,toId(Loader.mw.getString(path + "gamemode"))));
		gamemodes.put(as, GameMode.getByValue(toId(Loader.mw.getString(path + "gamemode"))));
		
		/**
		 * RISKY SETTINGS
		 **/
		if(Loader.mw.getBoolean(path + "spawnLimits.modify")) {
			as.setAmbientSpawnLimit(Loader.mw.getInt(path + "spawnLimits.ambient"));
			as.setAnimalSpawnLimit(Loader.mw.getInt(path + "spawnLimits.animal"));
			as.setMonsterSpawnLimit(Loader.mw.getInt(path + "spawnLimits.monster"));
			as.setWaterAmbientSpawnLimit(Loader.mw.getInt(path + "spawnLimits.waterAmbient"));
			as.setWaterAnimalSpawnLimit(Loader.mw.getInt(path + "spawnLimits.waterAnimal"));
		}
		if(Loader.mw.getBoolean(path + "spawnTicks.modify")) {
			as.setTicksPerAmbientSpawns(Loader.mw.getInt(path + "spawnTicks.ambient"));
			as.setTicksPerAnimalSpawns(Loader.mw.getInt(path + "spawnTicks.animal"));
			as.setTicksPerMonsterSpawns(Loader.mw.getInt(path + "spawnTicks.monster"));
			as.setTicksPerWaterAmbientSpawns(Loader.mw.getInt(path + "spawnTicks.waterAmbient"));
			as.setTicksPerWaterSpawns(Loader.mw.getInt(path + "spawnTicks.waterAnimal"));
		}
		
		try {
			as.setHardcore(Loader.mw.getBoolean(path + "hardCore"));
		}catch(NoSuchMethodError e) {}
		
		as.setAutoSave(Loader.mw.getBoolean(path + "autoSave"));
		as.setDifficulty(Difficulty.valueOf(Loader.mw.getString(path + "difficulty").toUpperCase()));
		as.setKeepSpawnInMemory(Loader.mw.getBoolean(path + "keepSpawnInMemory"));
		as.setPVP(Loader.mw.getBoolean(path + "pvp"));
		as.setSpawnFlags(Loader.mw.getBoolean(path + "allow.monsters"), Loader.mw.getBoolean(path + "allow.animals"));
		for(String rule : as.getGameRules())
			as.setGameRuleValue(rule, Loader.mw.getString(path+"gamerule."+rule.toLowerCase()));
	}

	public static int toId(String string) {
		switch(string.toLowerCase()) {
		case "1":
		case "c":
		case "creative":
			return 1;
		case "2":
		case "a":
		case "adventure":
			return 2;
		case "3":
		case "sp":
		case "spectator":
			return 3;
		}
		return 0;
	}

	public static void loadWorlds() {
		for (String w : Loader.mw.getStringList("worlds"))
			loadWorld(w, null);
		for (World wa : Bukkit.getWorlds())
			defaultSet(wa, null);
	}

	public static void loadWorld(String s, CommandSender sender) {
		if (Bukkit.getWorld(s) != null)
			return;
		String biome = Loader.mw.getString("settings." + s + ".generator");
		if(biome==null)biome="NORMAL";
		long seed = Loader.mw.getLong("settings." + s + ".seed");
		if(!Loader.mw.exists("settings." + s + ".seed"))seed=new Random().nextLong();
		if (biome.equalsIgnoreCase("NETHER")) {
			WorldsAPI.create(s, Environment.NETHER, WorldType.NORMAL, true, seed);
		}else
		if (biome.equalsIgnoreCase("THE_END")||biome.equalsIgnoreCase("end")) {
			WorldsAPI.create(s, Environment.THE_END, WorldType.NORMAL, true, seed);
		}else
		if (biome.equalsIgnoreCase("NORMAL") || biome.equalsIgnoreCase("none")||biome.equalsIgnoreCase("default")) {
			WorldsAPI.create(s, Environment.NORMAL, WorldType.NORMAL, true, seed);
		}else
		if (biome.equalsIgnoreCase("FLAT")) {
			WorldsAPI.create(s, Environment.NORMAL, WorldType.FLAT, true, seed);
		}else
		if (biome.equalsIgnoreCase("THE_VOID")||biome.equalsIgnoreCase("void")) {
			WorldsAPI.create(s, Environment.NORMAL, null, true, seed);
		}else
			WorldsAPI.create(s, Environment.NORMAL, WorldType.NORMAL, true, seed);
		if(Bukkit.getWorld(s)==null)return;
		List<String> worlds = Loader.mw.getStringList("worlds");
		if(!worlds.contains(s))
			worlds.add(s);
		Loader.mw.set("worlds", worlds);
		Loader.mw.save();
		defaultSet(Bukkit.getWorld(s), biome);
		if(sender!=null)
		Loader.sendMessages(sender, "MultiWorld.Load", Placeholder.c().add("%world%", s));
	}

	public static void createWorld(String s, CommandSender sender) {
		String biome = Loader.mw.getString("settings." + s + ".generator");
		if(biome==null)biome="NORMAL";
		List<String> worlds = Loader.mw.getStringList("worlds");
		if (Bukkit.getWorld(s) != null) {
			Loader.sendMessages(sender, "MultiWorld.Loaded", Placeholder.c().add("%world%", s).replace("%generator%", biome));
		}else {
			long seed = Loader.mw.getLong("settings." + s + ".seed");
			if(!Loader.mw.exists("settings." + s + ".seed"))seed=new Random().nextLong();
			if (biome.equalsIgnoreCase("NETHER")) {
				WorldsAPI.create(s, Environment.NETHER, WorldType.NORMAL, true, seed);
			}else
			if (biome.equalsIgnoreCase("THE_END")||biome.equalsIgnoreCase("end")) {
				WorldsAPI.create(s, Environment.THE_END, WorldType.NORMAL, true, seed);
			}else
			if (biome.equalsIgnoreCase("NORMAL") || biome.equalsIgnoreCase("none")||biome.equalsIgnoreCase("default")) {
				WorldsAPI.create(s, Environment.NORMAL, WorldType.NORMAL, true, seed);
			}else
			if (biome.equalsIgnoreCase("FLAT")) {
				WorldsAPI.create(s, Environment.NORMAL, WorldType.FLAT, true, seed);
			}else
			if (biome.equalsIgnoreCase("THE_VOID")||biome.equalsIgnoreCase("void")) {
				WorldsAPI.create(s, Environment.NORMAL, null, true, seed);
			}else
				WorldsAPI.create(s, Environment.NORMAL, WorldType.NORMAL, true, seed);
			if(!worlds.contains(s))
				worlds.add(s);
			Loader.mw.set("worlds", worlds);
			Loader.mw.save();
			defaultSet(Bukkit.getWorld(s), biome);
			Loader.sendMessages(sender, "MultiWorld.Create", Placeholder.c().add("%world%", s).replace("%generator%", biome));
		}
	}

	public static String getFlag(World as, String string) {
		if(string.equalsIgnoreCase("GAMEMODE"))
			return Loader.mw.getString("settings." + as.getName() + ".gamemode");
		if(string.equalsIgnoreCase("DIFFICULTY"))
			return Loader.mw.getString("settings." + as.getName() + ".difficulty");
		if(string.equalsIgnoreCase("KEEP_SPAWN_IN_MEMORY"))
			return Loader.mw.getString("settings." + as.getName() + ".keepSpawnInMemory");
		if(string.equalsIgnoreCase("AUTOSAVE"))
			return Loader.mw.getString("settings." + as.getName() + ".autoSave");
		if(string.equalsIgnoreCase("PVP"))
			return Loader.mw.getString("settings." + as.getName() + ".pvp");
		if(string.equalsIgnoreCase("PORTAL_CREATE"))
			return Loader.mw.getString("settings." + as.getName() + ".portals.create");
		if(string.equalsIgnoreCase("PORTAL_TELEPORT"))
			return Loader.mw.getString("settings." + as.getName() + ".portals.teleport");
		if(string.equalsIgnoreCase("DO_FIRE_DAMAGE"))
			return Loader.mw.getString("settings." + as.getName() + ".fireDamage");
		if(string.equalsIgnoreCase("DO_DROWNING_DAMAGE"))
			return Loader.mw.getString("settings." + as.getName() + ".drownDamage");
		if(string.equalsIgnoreCase("DO_FALL_DAMAGE"))
			return Loader.mw.getString("settings." + as.getName() + ".fallDamage");
		for(String g : as.getGameRules())
			if(g.equalsIgnoreCase(string))
				return Loader.mw.getString("settings." + as.getName() + ".gamerule."+g.toLowerCase());
		return null;
	}

	public static void setFlag(World as, String string, String value) {
		if(string.equalsIgnoreCase("GAMEMODE"))
			Loader.mw.set("settings." + as.getName() + ".gamemode", value);
		if(string.equalsIgnoreCase("DIFFICULTY"))
			Loader.mw.set("settings." + as.getName() + ".difficulty", value);
		if(string.equalsIgnoreCase("KEEP_SPAWN_IN_MEMORY"))
			Loader.mw.set("settings." + as.getName() + ".keepSpawnInMemory", value);
		if(string.equalsIgnoreCase("AUTOSAVE"))
			Loader.mw.set("settings." + as.getName() + ".autoSave", value);
		if(string.equalsIgnoreCase("PVP"))
			Loader.mw.set("settings." + as.getName() + ".pvp", value);
		if(string.equalsIgnoreCase("PORTAL_CREATE"))
			Loader.mw.set("settings." + as.getName() + ".portals.create", value);
		if(string.equalsIgnoreCase("PORTAL_TELEPORT"))
			Loader.mw.set("settings." + as.getName() + ".portals.teleport", value);
		if(string.equalsIgnoreCase("DO_FIRE_DAMAGE"))
			Loader.mw.set("settings." + as.getName() + ".fireDamage", value);
		if(string.equalsIgnoreCase("DO_DROWNING_DAMAGE"))
			Loader.mw.set("settings." + as.getName() + ".drownDamage", value);
		if(string.equalsIgnoreCase("DO_FALL_DAMAGE"))
			Loader.mw.set("settings." + as.getName() + ".fallDamage", value);
		for(String g : as.getGameRules())
			if(g.equalsIgnoreCase(string))
				Loader.mw.set("settings." + as.getName() + ".gamerule."+g.toLowerCase(), value);
	}

	public static GameMode getGamemode(World world) {
		return gamemodes.get(world);
	}

	public static Object getGamemodeNMS(World world) {
		return gamemodesnms.get(world);
	}
}