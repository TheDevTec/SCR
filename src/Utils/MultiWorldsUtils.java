package Utils;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import me.DevTec.TheAPI;

public class MultiWorldsUtils {
	public static void UnloadWorld(String w, CommandSender sender) {
		if (Bukkit.getWorld(w) == null)
			return;
		if (!TheAPI.getWorldsManager().unloadWorld(w, true)) {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("MultiWorld.DoNotUnloaded").replace("%world%", w), sender);
			return;
		} else {
			List<String> worlds = Loader.mw.getStringList("Worlds");
			List<String> ww = Loader.mw.getStringList("Unloaded-Worlds");
			worlds.remove(w);
			ww.add(w);
			Loader.mw.set("Worlds", worlds);
			Loader.mw.set("Unloaded-Worlds", ww);
			Loader.mw.save();
			TheAPI.msg(Loader.s("Prefix") + Loader.s("MultiWorld.Unloaded").replace("%world%", w), sender);
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
		TheAPI.msg(Loader.s("Prefix") + Loader.s("Chunks.Unloaded").replace("%chunks%", String.valueOf(chunk)), s);
	}

	public static void EnableWorldCheck() {
		for (Player on : TheAPI.getOnlinePlayers()) {
			if (Bukkit.getWorld(on.getWorld().getName()) != null) {
				String[] f = { "SURVIVAL", "CREATIVE", "ADVENTURE", "SPECTATOR" };
				for (String waww : f) {
					if(Loader.mw.exist("WorldsSettings." + on.getWorld().getName() + ".GameMode"))
					if (Loader.mw.getString("WorldsSettings." + on.getWorld().getName() + ".GameMode")
							.equalsIgnoreCase(waww)) {
						on.setGameMode(GameMode.valueOf(
								Loader.mw.getString("WorldsSettings." + on.getWorld().getName() + ".GameMode")));
					}
				}
			}
			if (Bukkit.getWorld(on.getWorld().getName()) == null) {
				on.kickPlayer("Unable to find world");
			}
		}
	}

	public static boolean checkWorld(String world) {
		if (Bukkit.getWorld(world) != null) {
			return true;
		}
		return false;
	}

	public static enum Generator {
		NORMAL, FLAT, NETHER, THE_END, THE_VOID;
	}

	private static boolean exist(String w) {
		return new File(Bukkit.getWorldContainer().getPath() + "/" + w + "/session.lock").exists();
	}

	public static void importWorld(String w, CommandSender s, Generator type) {
		if (Bukkit.getWorld(w) != null)
			return;
		if (exist(w)) {
			if (Bukkit.getWorld(w) != null) {
				TheAPI.msg(Loader.s("Prefix") + Loader.s("MultiWorld.AlreadyExists").replace("%world%", w), s);
			} else if (Loader.mw.getStringList("Unloaded-Worlds").contains(w)
					|| Loader.mw.getStringList("Worlds").contains(w)) {
				TheAPI.msg(Loader.s("Prefix") + Loader.s("MultiWorld.AlreadyExists").replace("%world%", w), s);
			} else {
				List<String> wws = Loader.mw.getStringList("Deleted-Worlds");
				List<String> worlds = Loader.mw.getStringList("Worlds");
				switch (type) {
				case FLAT:
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, WorldType.FLAT, true, 0);
					break;
				case NETHER:
					TheAPI.getWorldsManager().create(w, Environment.NETHER, WorldType.NORMAL, true, 0);
					break;
				case NORMAL:
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, WorldType.NORMAL, true, 0);
					break;
				case THE_END:
					TheAPI.getWorldsManager().create(w, Environment.THE_END, WorldType.NORMAL, true, 0);
					break;
				case THE_VOID:
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, null, true, 0);
					break;
				}
				wws.remove(w);
				worlds.add(w);
				Loader.mw.set("Deleted-Worlds", wws);
				Loader.mw.set("Worlds", worlds);
				Loader.mw.save();
				DefaultSet(Bukkit.getWorld(w), type.toString());
				TheAPI.msg(Loader.s("Prefix") + Loader.s("MultiWorld.WorldImported").replace("%world%", w), s);
			}
		} else
			TheAPI.msg(Loader.s("Prefix") + Loader.s("MultiWorld.NotFoundImport").replace("%world%", w), s);
	}

	@SuppressWarnings("deprecation")
	public static void DefaultSet(World as, String gen) {
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".GameMode", "SURVIVAL");
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Difficulty", as.getDifficulty().name());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".KeepSpawnInMemory", as.getKeepSpawnInMemory());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".AutoSave", as.isAutoSave());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".PvP", as.getPVP());
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".CreatePortal", true);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".PortalTeleport", true);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".DoFireDamage", true);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".DoDrownDamage", true);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".DoFallDamage", true);
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.COMMAND_BLOCK_OUTPUT",
				as.getGameRuleValue("COMMAND_BLOCK_OUTPUT")); // 22 rules
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.DISABLE_ELYTRA_MOVEMENT_CHECK",
				as.getGameRuleValue("DISABLE_ELYTRA_MOVEMENT_CHECK"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.DISABLE_RAIDS",
				as.getGameRuleValue("DISABLE_RAIDS"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.DO_DAYLIGHT_CYCLE",
				as.getGameRuleValue("DO_DAYLIGHT_CYCLE"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.DO_ENTITY_DROPS",
				as.getGameRuleValue("DO_ENTITY_DROPS"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.DO_FIRE_TICK",
				as.getGameRuleValue("DO_FIRE_TICK"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.DO_LIMITED_CRAFTING",
				as.getGameRuleValue("DO_LIMITED_CRAFTING"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.DO_MOB_LOOT",
				as.getGameRuleValue("DO_MOB_LOOT"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.DO_TILE_DROPS",
				as.getGameRuleValue("DO_TILE_DROPS"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.DO_WEATHER_CYCLE",
				as.getGameRuleValue("DO_WEATHER_CYCLE"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.KEEP_INVENTORY",
				as.getGameRuleValue("KEEP_INVENTORY"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.LOG_ADMIN_COMMANDS",
				as.getGameRuleValue("LOG_ADMIN_COMMANDS"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.MAX_COMMAND_CHAIN_LENGTH",
				as.getGameRuleValue("MAX_COMMAND_CHAIN_LENGTH"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.MAX_ENTITY_CRAMMING",
				as.getGameRuleValue("MAX_ENTITY_CRAMMING"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.MOB_GRIEFING",
				as.getGameRuleValue("MOB_GRIEFING"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.RANDOM_TICK_SPEED",
				as.getGameRuleValue("RANDOM_TICK_SPEED"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.REDUCED_DEBUG_INFO",
				as.getGameRuleValue("REDUCED_DEBUG_INFO"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.SEND_COMMAND_FEEDBACK",
				as.getGameRuleValue("SEND_COMMAND_FEEDBACK"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.SHOW_DEATH_MESSAGES",
				as.getGameRuleValue("SHOW_DEATH_MESSAGES"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.SPAWN_RADIUS",
				as.getGameRuleValue("SPAWN_RADIUS"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.SPECTATORS_GENERATE_CHUNKS",
				as.getGameRuleValue("SPECTATORS_GENERATE_CHUNKS"));
		Loader.mw.addDefault("WorldsSettings." + as.getName() + ".Gamerule.ANNOUNCE_ADVANCEMENTS",
				as.getGameRuleValue("ANNOUNCE_ADVANCEMENTS"));
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
		if (Loader.mw.getString("WorldsSettings." + as.getName()) != null) {
			for (String s : Loader.mw.getConfigurationSection("").getKeys(false)) {

				if (s.equalsIgnoreCase("MAX_COMMAND_CHAIN_LENGTH") || s.equalsIgnoreCase("RANDOM_TICK_SPEED")
						|| s.equalsIgnoreCase("MAX_ENTITY_CRAMMING") || s.equalsIgnoreCase("RANDOM_TICK_SPEED")) {
					as.setGameRuleValue(s, "" + Loader.mw.getInt("WorldsSettings." + as.getName() + ".GameRule." + s));
				} else
					as.setGameRuleValue(s,
							"" + Loader.mw.getDouble("WorldsSettings." + as.getName() + ".GameRule." + s));

			}
			String dif = null;
			dif = Loader.mw.getString("WorldsSettings." + as.getName() + ".Difficulty").toUpperCase();
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
			String biome = Loader.mw.getString("WorldsSettings." + as.getName() + ".Generator");

			if (!dif.equals("EASY") && !dif.equals("NORMAL") && !dif.equals("HARD") && !dif.equals("PEACEFUL")) {
				Loader.getInstance.getLogger()
						.warning("Setting Difficulty of world '" + as.getName() + "' is unknown !");
				Loader.getInstance.getLogger().warning("Setting the default difficulty..");
				as.setDifficulty(Difficulty.EASY);
			}
			if (dif.equals("EASY") || dif.equals("NORMAL") || dif.equals("HARD") || dif.equals("PEACEFUL")) {
				as.setDifficulty(Difficulty.valueOf(dif));

			}
			if (keepspawn == true || keepspawn == false) {
				as.setKeepSpawnInMemory(keepspawn);
			}
			if (autosave == true || autosave == false) {
				as.setAutoSave(autosave);
			}
			if (pvp == true || pvp == false) {
				as.setPVP(pvp);
			}
			as.setTicksPerAnimalSpawns(an);
			as.setTicksPerMonsterSpawns(mo);
			if (biome.equalsIgnoreCase("FLAT")) {
				as.setBiome(0, 15, Biome.PLAINS);
			}
			if (Loader.mw.getBoolean("ModifyMobsSpawnRates")) {
				as.setMonsterSpawnLimit(monster); // max spawning of monsters (zombies..) is defaulty 300
				as.setAnimalSpawnLimit(animal); // max spawning of animals (cows..) is defaulty 150
				as.setWaterAnimalSpawnLimit(animal / 2); // max spawning of water animals (squids..) is defaulty 75
				as.setAmbientSpawnLimit((animal / 2) / 2); // max spawning of ambient mobs (bats..) is defaulty 37.5
			}
		}
	}

	public static void LoadWorlds() {
		List<String> worlds = Loader.mw.getStringList("Worlds");
		if (worlds.isEmpty() == false)
			for (String w : worlds) {
				if (Bukkit.getWorld(w) != null)
					continue;
				String biome = Loader.mw.getString("WorldsSettings." + w + ".Generator");
				if (biome.equalsIgnoreCase("NETHER")) {
					TheAPI.getWorldsManager().create(w, Environment.NETHER, WorldType.NORMAL, true, 0);
				}
				if (biome.equalsIgnoreCase("THE_END")) {
					TheAPI.getWorldsManager().create(w, Environment.THE_END, WorldType.NORMAL, true, 0);
				}
				if (biome.equalsIgnoreCase("NORMAL") || biome.equalsIgnoreCase("none")) {
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, WorldType.NORMAL, true, 0);
				}
				if (biome.equalsIgnoreCase("FLAT")) {
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, WorldType.FLAT, true, 0);
				}
				if (biome.equalsIgnoreCase("THE_VOID")) {
					TheAPI.getWorldsManager().create(w, Environment.NORMAL, null, true, 0);
				}
			}
		for (World wa : Bukkit.getWorlds()) {
			DefaultSet(wa, Loader.mw.getString("WorldsSettings." + wa.getName() + ".Generator"));
		}
	}

	public static void LoadWorld(String s, CommandSender sender) {
		if (Bukkit.getWorld(s) != null)
			return;
		List<String> worlds = Loader.mw.getStringList("Worlds");
		List<String> ww = Loader.mw.getStringList("Unloaded-Worlds");
		String biome = Loader.mw.getString("WorldsSettings." + s + ".Generator");
		if (biome.equalsIgnoreCase("NETHER")) {
			TheAPI.getWorldsManager().create(s, Environment.NETHER, WorldType.NORMAL, true, 0);
		}
		if (biome.equalsIgnoreCase("THE_END")) {
			TheAPI.getWorldsManager().create(s, Environment.THE_END, WorldType.NORMAL, true, 0);
		}
		if (biome.equalsIgnoreCase("NORMAL") || biome.equalsIgnoreCase("none")) {
			TheAPI.getWorldsManager().create(s, Environment.NORMAL, WorldType.NORMAL, true, 0);
		}
		if (biome.equalsIgnoreCase("FLAT")) {
			TheAPI.getWorldsManager().create(s, Environment.NORMAL, WorldType.FLAT, true, 0);
		}
		if (biome.equalsIgnoreCase("THE_VOID")) {
			TheAPI.getWorldsManager().create(s, Environment.NORMAL, null, true, 0);
		}
		worlds.add(s);
		ww.remove(s);
		Loader.mw.set("Worlds", worlds);
		Loader.mw.set("Unloaded-Worlds", ww);
		Loader.mw.save();
		DefaultSet(Bukkit.getWorld(s), biome);
		TheAPI.msg(Loader.s("Prefix") + Loader.s("MultiWorld.Loaded").replace("%world%", s), sender);
	}

	public static void CreateWorld(String s, CommandSender sender) {
		String biome = Loader.mw.getString("WorldsSettings." + s + ".Generator");
		List<String> wws = Loader.mw.getStringList("Deleted-Worlds");
		List<String> worlds = Loader.mw.getStringList("Worlds");
		if (Bukkit.getWorld(s) != null) {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("MultiWorld.AlreadyCreated").replace("%world%", s), sender);

		}
		if (Bukkit.getWorld(s) == null) {
			if (biome.equalsIgnoreCase("NETHER")) {
				TheAPI.getWorldsManager().create(s, Environment.NETHER, WorldType.NORMAL, true, 0);
			}
			if (biome.equalsIgnoreCase("THE_END")) {
				TheAPI.getWorldsManager().create(s, Environment.THE_END, WorldType.NORMAL, true, 0);
			}
			if (biome.equalsIgnoreCase("NORMAL") || biome.equalsIgnoreCase("none")) {
				TheAPI.getWorldsManager().create(s, Environment.NORMAL, WorldType.NORMAL, true, 0);
			}
			if (biome.equalsIgnoreCase("FLAT")) {
				TheAPI.getWorldsManager().create(s, Environment.NORMAL, WorldType.FLAT, true, 0);
			}
			if (biome.equalsIgnoreCase("THE_VOID")) {
				TheAPI.getWorldsManager().create(s, Environment.NORMAL, null, true, 0);
			}
			wws.remove(s);
			worlds.add(s);
			Loader.mw.set("Deleted-Worlds", wws);
			Loader.mw.set("Worlds", worlds);
			Loader.mw.save();
			DefaultSet(Bukkit.getWorld(s), biome);
			TheAPI.msg(Loader.s("Prefix") + Loader.s("MultiWorld.Created").replace("%world%", s), sender);
		}
	}
}
