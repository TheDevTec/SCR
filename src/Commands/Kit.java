package Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import Utils.XMaterial;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.EnchantmentAPI;
import me.DevTec.TheAPI.APIs.ItemCreatorAPI;
import me.DevTec.TheAPI.CooldownAPI.CooldownAPI;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Kit implements CommandExecutor, TabCompleter {
	public List<String> kits(CommandSender p) {
		List<String> list = new ArrayList<String>();
		if (Kits().isEmpty() == false) {
			if (!p.hasPermission("servercontrol.kit.*")) {
				for (String name : Kits()) {
					if (p.hasPermission("ServerControl.Kit." + name))
						list.add(name);
				}
			} else {
				list = Kits();
			}
		}
		return list;
	}

	public static List<String> Kits() {
		List<String> list = new ArrayList<String>();
		if (Loader.kit.getString("Kits") != null)
			for (String s : Loader.kit.getConfigurationSection("Kits").getKeys(false))
				list.add(s);
		return list;
	}

	public static String getKitName(String original) {
		if (Loader.kit.getString("Kits") != null)
			for (String s : Loader.kit.getConfigurationSection("Kits").getKeys(false)) {
				if (s.toLowerCase().equalsIgnoreCase(original)) {
					return s;
				}
			}
		return null;
	}

	public static void giveKit(String s, String KitName, boolean cooldown, boolean economy) {
		Player p = TheAPI.getPlayer(s);
		if (p != null) {
			if (!cooldown) {
				if (!economy) {
					setupKit(p, KitName);
					return;
				} else {
					if (EconomyAPI.has(s, Loader.kit.getDouble("Kits." + KitName + ".Price"))) {
						setupKit(p, KitName);
						TheAPI.msg(
								Loader.s("Prefix") + Loader.s("Kit.Used").replace("%kit%", getKitName(KitName))
										.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()),
								p);
						EconomyAPI.withdrawPlayer(s, Loader.kit.getDouble("Kits." + KitName + ".Price"));
						return;
					}
					TheAPI.msg(Loader.s("Economy.NoMoney")
							.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(s), true))
							.replace("%currently%", API.setMoneyFormat(EconomyAPI.getBalance(s), true))
							.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), p);
					return;
				}
			} else {
				if (economy) {
					if (EconomyAPI.has(s, Loader.kit.getDouble("Kits." + KitName + ".Price"))) {
						CooldownAPI a = TheAPI.getCooldownAPI(s);
						if (!a.expired("Kit." + KitName)) {
							TheAPI.msg(Loader.s("Prefix") + Loader.s("Kit.Cooldown")
									.replace("%cooldown%",
											StringUtils.setTimeToString(a.getTimeToExpire("Kit." + KitName)))
									.replace("%kit%", getKitName(KitName)).replace("%player%", p.getName())
									.replace("%playername%", p.getDisplayName()), p);
							return;
						}
						setupKit(p, KitName);
						TheAPI.msg(
								Loader.s("Prefix") + Loader.s("Kit.Used").replace("%kit%", getKitName(KitName))
										.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()),
								p);
						a.createCooldown("Kit." + KitName, Loader.kit.getDouble("Kits." + KitName + ".Cooldown"));
						EconomyAPI.withdrawPlayer(s,
								Loader.kit.getDouble("Kits." + getKitName(KitName) + ".Price"));
						return;
					}
					TheAPI.msg(Loader.s("Economy.NoMoney")
							.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(s), true))
							.replace("%currently%", API.setMoneyFormat(EconomyAPI.getBalance(s), true))
							.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), p);
					return;
				} else {
					CooldownAPI a = TheAPI.getCooldownAPI(s);
					if (!a.expired("Kit." + KitName)) {
						TheAPI.msg(Loader.s("Prefix") + Loader.s("Kit.Cooldown")
								.replace("%cooldown%", StringUtils.setTimeToString(a.getTimeToExpire("Kit." + KitName)))
								.replace("%kit%", getKitName(KitName)).replace("%player%", p.getName())
								.replace("%playername%", p.getDisplayName()), p);
						return;
					}
					setupKit(p, KitName);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Kit.Used").replace("%kit%", getKitName(KitName))
							.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), p);
					a.createCooldown("Kit." + KitName, Loader.kit.getDouble("Kits." + KitName + ".Cooldown"));
					return;
				}
			}
		}
	}

	private static void setupKit(Player p, String kitName) {
		if (getKitName(kitName) != null) {
			kitName = getKitName(kitName);
			for (String def : Loader.kit.getConfigurationSection("Kits." + kitName + ".Items").getKeys(false)) {
				Material m = null;
				try{
					m=XMaterial.matchXMaterial(def.toUpperCase()).get().parseMaterial();
				}catch(Exception e) {}
				if (m == null) {
					Loader.warn("Error when giving kit '" + kitName + "', material '" + def + "' is invalid !");
					return;
				}

				ItemCreatorAPI a = new ItemCreatorAPI(m);
				int numb = 1;
				if (Loader.kit.getInt("Kits." + kitName + ".Items." + def + ".Amount") != 0)
					numb = Loader.kit.getInt("Kits." + kitName + ".Items." + def + ".Amount");
				a.setAmount(numb);
				a.setLore(Loader.kit.getStringList("Kits." + kitName + ".Items." + def + ".Lore"));
				if (Loader.kit.getBoolean("Kits." + kitName + ".Items." + def + ".HideEnchants"))
					a.addItemFlag(ItemFlag.HIDE_ENCHANTS);
				if (Loader.kit.getBoolean("Kits." + kitName + ".Items." + def + ".HideAttributes"))
					a.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
				if (Loader.kit.getBoolean("Kits." + kitName + ".Items." + def + ".HideUnbreakable"))
					a.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
				if (Loader.kit.getBoolean("Kits." + kitName + ".Items." + def + ".Unbreakable"))
					a.setUnbreakable(true);
				a.setDisplayName(Loader.kit.getString("Kits." + kitName + ".Items." + def + ".CustomName"));
				if (Loader.kit.getString("Kits." + kitName + ".Items." + def + ".Enchantments") != null)
					for (String enchs : Loader.kit
							.getStringList("Kits." + kitName + ".Items." + def + ".Enchantments")) {
						String nonum = enchs.replace(":", "").replaceAll("[0-9 ]+", "").toUpperCase();
						if (EnchantmentAPI.byName(nonum)==null) {
							Loader.warn(
									"Error when giving kit '" + kitName + "', enchant '" + nonum + "' is invalid !");
						} else {
							a.addEnchantment(nonum, StringUtils
									.getInt(enchs.replace(":", "").replace("_", "").replace(" ", "")));
						}
					}
				TheAPI.giveItem(p, a.create());
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("Kit.List").replace("%player%", s.getName())
					.replace("%kits%", StringUtils.join(kits(s), ", "))
					.replace("%playername%", s.getName()), s);
			return true;
		}
		if (args.length == 1) {
			String kit = getKitName(args[0]);
			if (kit != null) {
				if (s.hasPermission("servercontrol.kit." + kit) || s.hasPermission("ServerControl.Kit.*")) {
					if (s instanceof Player) {
						giveKit(s.getName(), kit, true, true);
						return true;
					}

					Loader.Help(s, "/Kit " + kit + " <player>", "Kit-Give");
					return true;
				}
				TheAPI.msg(Loader.s("NotPermissionsMessage").replace("%player%", s.getName())
						.replace("%playername%", s.getName()).replace("%permission%", "servercontrol.kit." + kit), s);
				return true;
			}

			TheAPI.msg(Loader.s("Prefix") + Loader.s("Kit.NotExists").replace("%kit%", args[0])
					.replace("%player%", s.getName()).replace("%playername%", s.getName()), s);
			return true;
		}
		if (args.length == 2) {
			String kit = getKitName(args[0]);
			if (kit == null) {
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Kit.NotExists").replace("%kit%", args[0])
						.replace("%player%", s.getName()).replace("%playername%", s.getName()), s);
				return true;
			}
			Player t = TheAPI.getPlayer(args[1]);
			if (t != null) {
				if (t != s) {
					if (API.hasPerm(s, "servercontrol.kit.give")) {
						TheAPI.msg(
								Loader.s("Prefix") + Loader.s("Kit.Got").replace("%kit%", getKitName(args[0]))
										.replace("%player%", t.getName()).replace("%playername%", t.getDisplayName()),
								t);
						TheAPI.msg(
								Loader.s("Prefix") + Loader.s("Kit.Given").replace("%kit%", getKitName(args[0]))
										.replace("%player%", t.getName()).replace("%playername%", t.getDisplayName()),
								s);
						giveKit(t.getName(), kit, false, false);
						return true;
					}
					return true;
				} else {
					giveKit(t.getName(), kit, true, true);
					return true;
				}
			}
			if (args[0].equals("*")) {
				Repeat.a(s, "kit " + kit + " *");
				return true;
			}
			TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
			return true;
		}
		return false;
	}

	public List<String> kitss(CommandSender s) {
		List<String> w = new ArrayList<String>();
		if (Loader.kit.getString("Kits") != null)
			for (String d : Loader.kit.getConfigurationSection("Kits").getKeys(false)) {
				if (s.hasPermission("ServerControl.Kit." + d)) {
					w.add(d);
				}
			}
		return w;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length == 1) {
			c.addAll(StringUtil.copyPartialMatches(args[0], kitss(s), new ArrayList<>()));
		}
		if (args.length == 2)
			return null;
		return c;
	}

}
