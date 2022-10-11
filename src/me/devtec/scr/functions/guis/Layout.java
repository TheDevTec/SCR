package me.devtec.scr.functions.guis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.message.Sudo;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.game.ItemMaker;
import me.devtec.theapi.bukkit.gui.GUI.ClickType;
import me.devtec.theapi.bukkit.gui.HolderGUI;

public class Layout {

	List<String> lines;
	Map<Character, Item> builder = new HashMap<>();

	public Layout(Config file) { // Loading and setting up layout of GUI

		for (String key : file.getKeys("items")) { // Loading starting items
			if (key.isEmpty())
				continue;
			builder.put(key.toCharArray()[0], new Item(file, "items." + key));
		}
		lines = file.getStringList("layout"); // Loading layout

	}

	public Item getItem(Player p, Character c) {
		if (!builder.containsKey(c))
			return null;
		Item starting = builder.get(c);
		if (starting != null)
			if (starting.canShowItem(p))
				return getItemForRealLol(p, starting);
		return null;
	}

	private Item getItemForRealLol(Player p, Item starting) { // What do you expect? :D
		if (!starting.config.exists(starting.config_path + ".conditions.item_show.item"))
			return starting;
		Item neww = new Item(starting.config, starting.config_path + ".conditions.item_show");
		if (starting.cond(p))
			// Pokud hráč splnuje starting podmínku, tak
			// se mu to pokusí ukázat ten další item u té podmínky
			return getItemForRealLol(p, neww);
		return starting;
	}

	public class Item {

		private Config config;
		private String config_path;

		// "items."+path
		public Item(Config config, String path) {
			this.config = config;
			config_path = path;
		}

		public boolean canShowItem(Player p) {
			if (config.exists(config_path + ".conditions.item_show.item"))
				return true;
			if (config.exists(config_path + ".conditions.item_show"))
				return ConditionPositive(p, "item_show");
			return true; // in default showing all items
		}

		public boolean cond(Player p) {
			if (config.exists(config_path + ".conditions.item_show"))
				return ConditionPositive(p, "item_show");
			return true; // in default showing all items
		}

		// Conditions check
		// condition only string, without :
		private boolean ConditionPositive(CommandSender s, String condition) {
			// InBuild CONDITIONS
			if (condition.startsWith("havePerm:")) { // just for sender
				String perm = condition.replace("havePerm:", "");
				return s.hasPermission(perm);
			}
			// CUSTOM CONDITION
			String[] con = condition.split(":"); // není potřeba, ale například pro pozdější spojení s CustomCommand condition to
													// tu nechám, pro lepší spojení

			if (config.exists(config_path + ".conditions." + con[0])) {
				String value = PlaceholderAPISupport.replace(config.getString(config_path + ".conditions." + con[0] + ".value"), s, true);
				/*
				 * if (con.length == 2 && con[1] != null && !con[1].isEmpty()) value =
				 * args[StringUtils.getInt(con[1])];
				 */

				for (String positive : config.getStringList(config_path + ".conditions." + condition + ".positive")) {
					// Bukkit.broadcastMessage(value+" ; "+positive);
					if (positive.startsWith(">=") && StringUtils.getDouble(value) >= StringUtils.getDouble(positive.replace(">=", ""))
							|| positive.startsWith("<=") && StringUtils.getDouble(value) <= StringUtils.getDouble(positive.replace("<=", "")))
						return true;
					if (positive.startsWith(">") && !positive.startsWith(">=") && StringUtils.getDouble(value) > StringUtils.getDouble(positive.replace(">", "")))
						return true;
					if (positive.startsWith("<") && !positive.startsWith("<=") && StringUtils.getDouble(value) < StringUtils.getDouble(positive.replace("<", "")))
						return true;
					if (positive.startsWith("=") && StringUtils.getDouble(value) == StringUtils.getDouble(positive.replace("=", ""))
							|| positive.startsWith("==") && StringUtils.getDouble(value) == StringUtils.getDouble(positive.replace("==", "")))
						return true;
					if (value.equalsIgnoreCase(positive))
						return true;
				}
			} else
				Loader.plugin.getLogger().warning("Condition " + condition + " not found in GUI " + config.getFile().getName() + "");
			return false;
		}
		/*
		 * CustomConditions >=50 >50 <=50 <50 ==50 (or =50) <value> -> equalsIgnoreCase
		 */

		public void process(Player player, HolderGUI gui, ClickType click, String action) {
			BukkitLoader.getNmsProvider().postToMainThread(() -> {
				for (String key : config.getStringList(config_path + ".actions." + action))
					if (processAction(player, gui, click, key, true))
						break;
			});
		}

		java.util.regex.Pattern reqVal = java.util.regex.Pattern.compile("(any|shift_right|shift_left|middle|left|right) (cmd|msg|connect|open) (.*)"),
				aditVal = java.util.regex.Pattern.compile("(any|shift_right|shift_left|middle|left|right) (con:.*?|perm:.*?) (.*)"),
				nonVal = java.util.regex.Pattern.compile("(any|shift_right|shift_left|middle|left|right) (close|anim:.*)");

		public boolean processAction(Player player, HolderGUI gui, ClickType click, String action, boolean subActions) {
			Matcher matcher = nonVal.matcher(action);
			if (matcher.find()) {
				String key = convertToString(click);
				if (matcher.group(1).equals("any") || matcher.group(1).equals(key))
					if (matcher.group(2).equals("close"))
						gui.close(player);
				return false;
			}

			matcher = aditVal.matcher(action);
			if (matcher.find()) {
				String key = convertToString(click);
				if (matcher.group(1).equals("any") || matcher.group(1).equals(key)) {
					if (matcher.group(2).startsWith("con:")) {
						String con = matcher.group(2).substring(4);
						if (matcher.group(3) != null) {
							String actions = matcher.group(3);
							boolean result = !ConditionPositive(player, con);
							if (result) { // If condition is not true
								for (String keys : config.getStringList(config_path + ".actions." + actions))
									if (processAction(player, gui, click, keys, true))
										break;
								return true;
							}
							return false;
						}
						// res is con
						return !ConditionPositive(player, con);
					}
					String perm = matcher.group(2).substring(5);
					if (matcher.group(3) != null) {
						String actions = matcher.group(3);
						boolean result = !player.hasPermission(perm);
						if (result) { // If player do not have perm
							for (String keys : config.getStringList(config_path + ".actions." + actions))
								if (processAction(player, gui, click, keys, true))
									break;
							return true;
						}
						return false;
					}
					// res is perm
					return !player.hasPermission(perm);
				}
				return false;
			}
			matcher = reqVal.matcher(action);
			if (matcher.find()) {
				String key = convertToString(click);
				if (matcher.group(1).equals("any") || matcher.group(1).equals(key)) {
					/*
					 * if(matcher.group(2).startsWith("connect")) { //TODO - bungee support String
					 * res = matcher.group(3); API.send(player,res); return false; }else {
					 */
					if (matcher.group(2).startsWith("open")) {
						String res = matcher.group(3);
						GUIManager.open(player, res);
						return false;
					}
					if (matcher.group(2).startsWith("cmd")) {
						String res = matcher.group(3);
						Sudo.sudoConsole(PlaceholderAPISupport.replace(res, player, true));
						return false;
					}
					String res = matcher.group(3);
					msg(player, res, Placeholders.c().addPlayer("player", player));
				}
			}
			return false;
		}

		private String convertToString(ClickType click) {
			switch (click) {
			case LEFT_DROP:
			case LEFT_PICKUP:
				return "left";
			case RIGHT_DROP:
			case RIGHT_PICKUP:
				return "right";
			case SHIFT_LEFT_DROP:
			case SHIFT_LEFT_PICKUP:
				return "shift_left";
			case SHIFT_RIGHT_DROP:
			case SHIFT_RIGHT_PICKUP:
				return "shift_right";
			default:
				return "middle";
			}
		}

		private void msg(CommandSender s, String message, Placeholders placeholders) {
			String text = message;
			text = StringUtils.colorize(MessageUtils.placeholder(s, text, placeholders));
			String lastcolor = null;
			for (String line : text.replace("\\n", "\n").split("\n")) {
				if (lastcolor != null && lastcolor.length() == 1)
					lastcolor = "&" + lastcolor;
				if (lastcolor != null && lastcolor.length() == 7) {
					lastcolor = "&" + lastcolor;
					lastcolor = lastcolor.replace("&x", "#");
				}
				s.sendMessage(StringUtils.colorize(PlaceholderAPISupport.replace(lastcolor == null ? line : lastcolor + "" + line, s, true, null)));
				lastcolor = StringUtils.getLastColors(StringUtils.colorize(line));
			}
		}

		public ItemStack notupdated;
		public long lastUpdate;

		public ItemStack build(Player player) {

			if (player == null)
				return null;

			String path = config_path + ".item";

			// If dynamci is false, we will load ItemStack once and then update it after
			// soem time... (loading new placeholders)
			if (notupdated != null && config.exists(path + ".update")) {
				if (config.getString(path + ".update").equalsIgnoreCase("NEVER"))
					return notupdated;
				long updateAfter = 900;
				if (config.exists("update"))
					updateAfter = StringUtils.timeFromString(config.getString(path + ".update"));
				if (lastUpdate - System.currentTimeMillis() / 1000 + updateAfter >= 0)
					return notupdated;
			}

			notupdated = ItemMaker.loadFromConfig(config, path);
			lastUpdate = System.currentTimeMillis() / 1000;
			return notupdated;
		}

	}
}
