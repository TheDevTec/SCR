package Utils;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.jar.JarEntry;

import org.bukkit.plugin.java.JavaPlugin;

import ServerControl.Loader;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Utils.TheAPIUtils.Validator;
import me.DevTec.TheAPI.Utils.ZIP.JarReader;

public class Configs {
	
	public static void load() {
		copyDefauts(Loader.getInstance, "Commands.yml");
		copyDefauts(Loader.getInstance, "Config.yml");
		copyDefauts(Loader.getInstance, "Events.yml");
		copyDefauts(Loader.getInstance, "Translations");
		Loader.config = new Config("ServerControlReloaded/Config.yml");
		String lang = Loader.config.getString("Options.Language");
		if(lang!=null)
		if(!new File("ServerControlReloaded/translation-"+lang+".yml").exists()) {
			Validator.send("File translation-"+lang+".yml doesn't exist", new NoSuchFileException("File translation-"+lang+".yml doesn't exist"));
			lang="en";
		}
		Loader.trans = new Config("ServerControlReloaded/translation-"+lang+".yml");
		MultiWorldLoading();
		ScoreboardLoading();
		TabLoading();
		KitLoading();
	}

	private static void ScoreboardLoading() {
		Loader.sb=new Config("ServerControlReloaded/Scoreboard.yml");
		Loader.sb.addDefault("Enabled", true);
		Loader.sb.addDefault("PerWorld", false);
		Loader.sb.addDefault("RefleshTick", 20);
		Loader.sb.addDefault("Name", "&bStatus");
		Loader.sb.addDefault("Lines", Arrays.asList("&r&lMoney: &a%money%$", "&r&lOnline:  &a%online%"));
		if(!Loader.sb.exists("PerWorld")) {
		Loader.sb.addDefault("PerWorld.pvp_world.Name", "&9PvP");
		Loader.sb.addDefault("PerWorld.pvp_world.Lines", Arrays.asList("&r&lKills: &a%kills%$", "&r&lHealth:  &a%health%"));

		Loader.sb.addDefault("PerWorld.skyblock.Name", "&eSkyBlock");
		Loader.sb.addDefault("PerWorld.skyblock.Lines",
				Arrays.asList("&r&lMoney: &a%money%$", "&r&lHealth:  &a%health%", "&r&lFood:  &a%food%"));
		Loader.sb.save();
		}
	}

	private static void copyDefauts(JavaPlugin parent, String file) {
	    for(JarEntry entry : new JarReader(new File(parent.getDataFolder(), file)).getEntries()) {
	        String name = entry.getName();
	        if (!name.startsWith(file + "/") || entry.isDirectory()) {
	            if(entry.isDirectory())
	                copyDefauts(parent, file+"/"+name);
	            continue;
	        }
	        parent.saveResource(name, false);
	    }
	}
	
	private static void TabLoading() {
		Loader.tab=new Config("ServerControlReloaded/TabList.yml");
		Loader.tab.addDefault("Tab-Enabled", true);
		Loader.tab.addDefault("Header-Enabled", true);
		Loader.tab.addDefault("Footer-Enabled", true);
		Loader.tab.addDefault("Colored-Ping", true);
		Loader.tab.addDefault("ModifyNameTags", true);
		Loader.tab.addDefault("SortTabList", true);
		Loader.tab.addDefault("Header",
				Arrays.asList("&bWelcome back &a%playername%&b!",
						"&bTPS: &a%tps%   &bFree Ram: &a%ram_free_percentage%",
						"&bMoney: &9%money%$   &bPing: &9%ping%", "&7--------------------------------"));
		Loader.tab.addDefault("Footer", Arrays.asList("&7--------------------------------",
				"&bTime: &a%time%   &bOnline: &a%online% &0/ &a%max_players%"));
		Loader.tab.addDefault("RefleshTick", 20);
		Loader.tab.addDefault("NameTag-RefleshTick", 20);
		Loader.tab.addDefault("AFK.IsAFK", " &4&l*AFK*");
		Loader.tab.addDefault("AFK.IsNotAFK", "");
		if (!Loader.tab.exists("Groups.default")) {
			Loader.tab.addDefault("Groups.default.TabList.Prefix", "&7Player &r");
			Loader.tab.addDefault("Groups.default.TabList.Suffix", "%afk%");
			Loader.tab.addDefault("Groups.default.NameTag.Prefix", "&7Player &r");
			Loader.tab.addDefault("Groups.default.Name", "%prefix% %playername% %suffix%");
			Loader.tab.addDefault("Groups.default.Priorite", "z");
			Loader.tab.addDefault("PerPlayerTabList.timtower.Footer",
					Arrays.asList("&7--------------------------------", "&6Money: &a%money%$   &6Rank: &a%group%"));
			Loader.tab.addDefault("PerPlayerTabList.timtower.Header",
					Arrays.asList("&4Welcome back %playername% !", "&7--------------------------------"));
			Loader.tab.addDefault("PerWorldTabList.world1.Footer",
					Arrays.asList("&7--------------------------------", "&6Online: &a%online%$   &6Rank: &a%group%"));
			Loader.tab.addDefault("PerWorldTabList.world1.Header", Arrays.asList("&2TabList in world %world%",
					"&6Health: &a%hp%$   &6Food: &a%food%", "&7--------------------------------"));
			Loader.tab.save();
		}
	}

	private static void MultiWorldLoading() {
		Loader.mw=new Config("ServerControlReloaded/MultiWorlds.yml");
		Loader.mw.addDefault("ModifyMobsSpawnRates", false);
		Loader.mw.addDefault("SavingTask.Enabled", true);
		Loader.mw.addDefault("SavingTask.Delay", 3600);
		Loader.mw.save();
	}

	private static void KitLoading() {
		Loader.kit=new Config("ServerControlReloaded/Kits.yml");
		if (!Loader.kit.exists("Kits")) {
			Loader.kit.addDefault("Kits.Default.Items.Stone.Amount", 16);
			Loader.kit.addDefault("Kits.Default.Items.Stone_Pickaxe.Amount", 1);
			Loader.kit.addDefault("Kits.Default.Items.Stone_Pickaxe.CustomName", "&8Normal pickaxe");
			Loader.kit.addDefault("Kits.Default.Price", 25);
			Loader.kit.addDefault("Kits.Default.Cooldown", 3600);
			Loader.kit.addDefault("Kits.VIP.Items.Diamond.Amount", 3);
			Loader.kit.addDefault("Kits.VIP.Items.Diamond.CustomName", "&bShiny Diamond");
			Loader.kit.addDefault("Kits.VIP.Items.Iron_Pickaxe.Amount", 1);
			Loader.kit.addDefault("Kits.VIP.Items.Iron_Pickaxe.CustomName", "&eSuper pickaxe");
			Loader.kit.addDefault("Kits.VIP.Items.Iron_Pickaxe.Lore", Arrays.asList("&4The best of best"));
			Loader.kit.addDefault("Kits.VIP.Items.Iron_Pickaxe.Enchantments", Arrays.asList("SHARPNESS:4", "UNBREAKING:2"));
			Loader.kit.addDefault("Kits.VIP.Price", 60);
			Loader.kit.addDefault("Kits.VIP.Cooldown", 3600);
			Loader.kit.save();
		}
	}
}
