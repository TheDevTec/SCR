package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI;

public class Spawner implements CommandExecutor, TabCompleter {

	public static List<String> list() {
		ArrayList<String> w = new ArrayList<String>();
		String[] d = { "FISHING_HOOK", "DROPPED_ITEM", "LEASH_HITCH", "LIGHTNING", "PLAYER", "MINECART_MOB_SPAWNER",
				"UKNOWN", "FIREWORK", "PRIMED_TNT", "AREA_EFFECT_CLOUD", "ENDER_SIGNAL", "UNKNOWN" };
		for (EntityType t : EntityType.values())
			w.add(t.name());
		for (String s : d)
			w.remove(s);
		return w;
	}

	public final Block getTargetBlock(Player player, int range) {
		BlockIterator iter = new BlockIterator(player, range);
		Block lastBlock = iter.next();
		while (iter.hasNext()) {
			lastBlock = iter.next();
			if (lastBlock.getType() == Material.AIR) {
				continue;
			}
			break;
		}
		return lastBlock;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Spawner")) {
			if (s instanceof Player) {
				Player p = (Player) s;
				if (args.length == 0) {
					Loader.Help(s, "/Spawner setMob <mob>", "Spawner-Mob");
					Loader.Help(s, "/Spawner setTime <time>", "Spawner-SpawnTime");
					if (TheAPI.isNewVersion()) {
						Loader.Help(s, "/Spawner setAmount <amount>", "Spawner-SpawnAmount");
						Loader.Help(s, "/Spawner setRangePlayer <range>", "Spawner-SpawnRangePlayer");
					}
					return true;
				}
				if (TheAPI.isNewVersion()) {
					if (args[0].equalsIgnoreCase("setAmount")) {
						if (args.length == 1) {
							Loader.Help(s, "/Spawner setAmount <amount>", "Spawner-SpawnAmount");
							return true;
						}
						Block b = getTargetBlock(p, 10);
						if (b.getType().name() == "SPAWNER") {
							CreatureSpawner ss = (CreatureSpawner) b.getState();
							ss.setSpawnCount(TheAPI.getStringUtils().getInt(args[1]));
							ss.update();
						}
						TheAPI.msg(Loader.s("Spawner.AmountSet")
								.replace("%amount%", "" + TheAPI.getStringUtils().getInt(args[1]))
								.replace("%world%", b.getWorld().getName()).replace("%x%", String.valueOf(b.getX()))
								.replace("%z%", String.valueOf(b.getZ())).replace("%y%", String.valueOf(b.getY())), s);
						return true;
					}

					if (args[0].equalsIgnoreCase("setRangePlayer")) {
						if (args.length == 1) {
							Loader.Help(s, "/Spawner setRangePlayer <range>", "Spawner-SpawnRangePlayer");
							return true;
						}
						Block b = getTargetBlock(p, 10);
						if (b.getType().name() == "SPAWNER") {
							CreatureSpawner ss = (CreatureSpawner) b.getState();
							ss.setRequiredPlayerRange(TheAPI.getStringUtils().getInt(args[1]));
							ss.update();
						}
						TheAPI.msg(Loader.s("Spawner.RangePlayerSet")
								.replace("%range%", "" + TheAPI.getStringUtils().getInt(args[1]))
								.replace("%world%", b.getWorld().getName()).replace("%x%", String.valueOf(b.getX()))
								.replace("%z%", String.valueOf(b.getZ())).replace("%y%", String.valueOf(b.getY())), s);
						return true;
					}
				} else {
					return true;
				}
				if (args[0].equalsIgnoreCase("setTime")) {
					if (args.length == 1) {
						Loader.Help(s, "/Spawner setTime <time>", "Spawner-SpawnTime");
						return true;
					}
					Block b = getTargetBlock(p, 10);
					if (b.getType().name() == "SPAWNER") {
						CreatureSpawner ss = (CreatureSpawner) b.getState();
						ss.setDelay(TheAPI.getStringUtils().getInt(args[1]));
						ss.update();
					}
					TheAPI.msg(
							Loader.s("Spawner.TimeSet").replace("%time%", "" + TheAPI.getStringUtils().getInt(args[1]))
									.replace("%world%", b.getWorld().getName()).replace("%x%", String.valueOf(b.getX()))
									.replace("%z%", String.valueOf(b.getZ())).replace("%y%", String.valueOf(b.getY())),
							s);
					return true;
				}
				if (args[0].equalsIgnoreCase("setMob")) {
					if (args.length == 1) {
						Loader.Help(s, "/Spawner setMob <mob>", "Spawner-Mob");
						return true;
					}
					Block b = getTargetBlock(p, 10);
					if (b.getType().name() == "SPAWNER") {
						@SuppressWarnings("deprecation")
						EntityType type = EntityType.fromName(args[1]);
						if (type != null) {
							CreatureSpawner ss = (CreatureSpawner) b.getState();
							ss.setSpawnedType(type);
							ss.update();
							TheAPI.msg(Loader.s("Spawner.Set").replace("%mob%", type.name())
									.replace("%world%", b.getWorld().getName()).replace("%x%", String.valueOf(b.getX()))
									.replace("%z%", String.valueOf(b.getZ())).replace("%y%", String.valueOf(b.getY())),
									s);
							return true;
						} else {
							TheAPI.msg(Loader.s("Spawner.InvalidMob").replace("%mob%", args[1])
									.replace("%world%", b.getWorld().getName()).replace("%x%", String.valueOf(b.getX()))
									.replace("%z%", String.valueOf(b.getZ())).replace("%y%", String.valueOf(b.getY())),
									s);
							return true;
						}
					}
					TheAPI.msg(
							Loader.s("Spawner.BlockIsNotSpawner").replace("%block%", b.getType().name())
									.replace("%world%", b.getWorld().getName()).replace("%x%", String.valueOf(b.getX()))
									.replace("%z%", String.valueOf(b.getZ())).replace("%y%", String.valueOf(b.getY())),
							s);
					return true;
				}
				Loader.Help(s, "/Spawner setMob <mob>", "Spawner-Mob");
				Loader.Help(s, "/Spawner setTime <time>", "Spawner-SpawnTime");
				if (TheAPI.isNewVersion()) {
					Loader.Help(s, "/Spawner setAmount <amount>", "Spawner-SpawnAmount");
					Loader.Help(s, "/Spawner setRangePlayer <range>", "Spawner-SpawnRangePlayer");
				}
				return true;
			}
			TheAPI.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (s.hasPermission("servercontrol.spawner")) {
			if (args.length == 1) {
				List<String> list = new ArrayList<String>();
				if (TheAPI.isNewVersion()) {
					list = Arrays.asList("setMob", "setRangePlayer", "setTime", "setAmount");
				} else
					list = Arrays.asList("setMob", "setTime");
				c.addAll(StringUtil.copyPartialMatches(args[0], list, new ArrayList<>()));
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("setMob"))
					c.addAll(StringUtil.copyPartialMatches(args[1], list(), new ArrayList<>()));

				if (TheAPI.isNewVersion()) {
					if (args[0].equalsIgnoreCase("setRangePlayer") || args[0].equalsIgnoreCase("setAmount"))
						c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("?"), new ArrayList<>()));
				}
				if (args[0].equalsIgnoreCase("setTime"))
					c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("?"), new ArrayList<>()));
			}
		}
		return c;
	}
}
