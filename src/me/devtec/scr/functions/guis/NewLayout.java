package me.devtec.scr.functions.guis;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.message.Sudo;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.game.ItemMaker;
import me.devtec.theapi.bukkit.gui.GUI.ClickType;
import me.devtec.theapi.bukkit.gui.HolderGUI;
import me.devtec.theapi.bukkit.nms.GameProfileHandler;
import me.devtec.theapi.bukkit.nms.GameProfileHandler.PropertyHandler;
import me.devtec.theapi.bukkit.xseries.XMaterial;

public class NewLayout {

	List<String> lines;
	Map<Character, Item> builder = new HashMap<>();

	public NewLayout(Config file) { // Loading and setting up layout of GUI

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
		private boolean dynamic;

		// "items."+path
		public Item(Config config, String path) {
			this.config = config;
			config_path = path;

			if (config.exists("dynamic"))
				dynamic = config.getBoolean("dynamic");
			else
				dynamic = false;
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

			if (config.getString(path + ".type") == null)
				return null; // missing type

			String[] typeSplit = PlaceholderAPISupport.replace(config.getString(path + ".type"), player, true).split(":");
			XMaterial type;
			if (StringUtils.isInt(typeSplit[0]))
				type = XMaterial.matchXMaterial(StringUtils.getInt(typeSplit[0]), typeSplit.length >= 2 ? StringUtils.getByte(typeSplit[1]) : 0).get();
			else
				type = XMaterial.matchXMaterial(typeSplit[0].toUpperCase()).get();

			ItemStack stack = type.parseItem();

			if (config.getString(path + ".head.type", "PLAYER").toUpperCase().equalsIgnoreCase("HDB")) // HEADDATABASE plugin
				if (new HeadDatabaseAPI().isHead(config.getString(path + ".head.owner")))
					stack = new HeadDatabaseAPI().getItemHead(config.getString(path + ".head.owner"));

			String nbt = config.getString(path + ".nbt"); // additional nbt
			if (nbt != null)
				stack = BukkitLoader.getNmsProvider().setNBT(stack, BukkitLoader.getNmsProvider().parseNBT(PlaceholderAPISupport.replace(nbt, player)));

			stack.setAmount(StringUtils.getInt(PlaceholderAPISupport.replace(config.getInt(path + ".amount", 1) + "", player, true)));
			short damage = config.getShort(path + ".damage", config.getShort(path + ".durability"));
			if (damage != 0)
				stack.setDurability(damage);

			ItemMeta meta = stack.getItemMeta();

			String displayName = config.getString(path + ".displayName", config.getString(path + ".display-name"));
			if (displayName != null)
				meta.setDisplayName(StringUtils.colorize(PlaceholderAPISupport.replace(displayName, player)));
			List<String> lore = config.getStringList(path + ".lore");
			if (!lore.isEmpty())
				meta.setLore(StringUtils.colorize(PlaceholderAPISupport.replace(lore, player)));
			if (config.getBoolean(path + ".unbreakable"))
				if (Ref.isNewerThan(10)) // 1.11+
					meta.setUnbreakable(true);
				else
					try {
						Ref.invoke(Ref.invoke(meta, "spigot"), "setUnbreakable", true);
					} catch (NoSuchFieldError | Exception e2) {
						// unsupported
					}
			if (Ref.isNewerThan(7)) // 1.8+
				for (String flag : config.getStringList(path + ".itemFlags"))
					meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));

			int modelData = config.getInt(path + ".modelData");
			if (Ref.isNewerThan(13) && modelData != 0) // 1.14+
				meta.setCustomModelData(modelData);

			if (type.name().contains("BANNER")) {
				BannerMeta banner = (BannerMeta) meta;
				// Example: RED:STRIPE_TOP
				for (String pattern : config.getStringList(path + ".banner.patterns")) {
					String[] split = pattern.split(":");
					banner.addPattern(new Pattern(DyeColor.valueOf(split[0].toUpperCase()), PatternType.valueOf(split[1].toUpperCase())));
				}
			}
			if (type == XMaterial.PLAYER_HEAD) { // HEAD
				SkullMeta skull = (SkullMeta) meta;
				String headOwner = config.getString(path + ".head.owner");
				if (headOwner != null) {
					/*
					 * PLAYER VALUES URL
					 */
					String headType = config.getString(path + ".head.type", "PLAYER").toUpperCase();
					if (headType.equals("PLAYER"))
						skull.setOwner(PlaceholderAPISupport.replace(headOwner, player));
					if (headType.equals("VALUES") || headType.equals("URL")) {
						if (headType.equals("URL"))
							headOwner = ItemMaker.fromUrl(headOwner);
						Ref.set(skull, profileField, BukkitLoader.getNmsProvider().toGameProfile(GameProfileHandler.of("SCR", UUID.randomUUID(), PropertyHandler.of("textures", headOwner))));
					}
				}
			}
			if (type.name().contains("LEATHER_") && config.getString(path + ".leather.color") != null) {
				LeatherArmorMeta armor = (LeatherArmorMeta) meta;
				armor.setColor(Color.fromRGB(Integer.decode(config.getString(path + ".leather.color"))));
			}
			if (type.name().contains("POTION")) {
				PotionMeta potion = (PotionMeta) meta;
				if (Ref.isNewerThan(9) && config.getString(path + ".potion.type") != null)
					potion.setBasePotionData(new PotionData(PotionType.valueOf(config.getString(path + ".potion.type").toUpperCase())));
				for (String pattern : config.getStringList(path + ".potion.effects")) {
					String[] split = pattern.split(":");
					// PotionEffectType type, int duration, int amplifier, boolean ambient, boolean
					// particles
					potion.addCustomEffect(new PotionEffect(PotionEffectType.getByName(split[0].toUpperCase()), StringUtils.getInt(split[1]), StringUtils.getInt(split[2]),
							split.length >= 4 ? StringUtils.getBoolean(split[3]) : true, split.length >= 5 ? StringUtils.getBoolean(split[4]) : true), true);
				}
				if (Ref.isNewerThan(10) && config.getString(path + ".potion.color") != null) // 1.11+
					potion.setColor(Color.fromRGB(Integer.decode(config.getString(path + ".potion.color"))));
			}
			if (type == XMaterial.ENCHANTED_BOOK) {
				EnchantmentStorageMeta book = (EnchantmentStorageMeta) meta;
				for (String enchant : config.getStringList(path + ".enchants")) {
					String[] split = enchant.split(":");
					book.addStoredEnchant(Enchantment.getByName(split[0].toUpperCase()), split.length >= 2 ? StringUtils.getInt(split[1]) : 1, true);
				}
			} else
				for (String enchant : config.getStringList(path + ".enchants")) {
					String[] split = enchant.split(":");
					meta.addEnchant(Enchantment.getByName(split[0].toUpperCase()), split.length >= 2 ? StringUtils.getInt(split[1]) : 1, true);
				}
			if (type == XMaterial.WRITTEN_BOOK || type == XMaterial.WRITABLE_BOOK) {
				BookMeta book = (BookMeta) meta;
				if (config.getString(path + ".book.author") != null)
					book.setAuthor(StringUtils.colorize(PlaceholderAPISupport.replace(config.getString(path + ".book.author"), player)));
				if (Ref.isNewerThan(9) && config.getString(path + ".book.generation") != null) // 1.10+
					book.setGeneration(Generation.valueOf(config.getString(path + ".book.generation").toUpperCase()));
				if (config.getString(path + ".book.title") != null)
					book.setTitle(StringUtils.colorize(PlaceholderAPISupport.replace(config.getString(path + ".book.title"), player)));
				book.setPages(StringUtils.colorize(PlaceholderAPISupport.replace(config.getStringList(path + ".book.pages"), player)));
			}
			stack.setItemMeta(meta);

			notupdated = stack;
			lastUpdate = System.currentTimeMillis() / 1000;
			return stack;
		}

		private Field profileField = Ref.field(Ref.craft("inventory.CraftMetaSkull"), "profile");

	}
}
