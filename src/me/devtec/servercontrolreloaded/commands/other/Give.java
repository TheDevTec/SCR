package me.devtec.servercontrolreloaded.commands.other;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.XMaterial;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;

public class Give implements CommandExecutor, TabCompleter {
	List<String> list = new ArrayList<String>();
	
	public boolean isAir(Material a) {
		if (!(a.name().equals("AIR") || a.name().equals("VOID_AIR")
				|| a.name().equals("STRUCTURE_VOID"))) {
			return false;
		}
		return true;
	}

	public boolean isItem(Material a, boolean canBeLegacy) {
		String s = a.name();
		if ((s.contains("WALL_") || isAir(a) || s.contains("_STEM") || s.contains("POTTED_")
				|| !canBeLegacy && s.contains("LEGACY_") || s.equals("END_PORTAL") || s.equals("END_GATEWAY")
				|| s.equals("NETHER_PORTAL")) && !(a.isBlock() && a.isOccluding())) {
			return false;
		}
		return true;
	}
	
	public Give() {
		for (XMaterial ss : XMaterial.values())
			if (new ItemCreatorAPI(ss.getMaterial()).isItem(false))
				list.add(ss.name());
		for (String add : Arrays.asList("POTION_OF_", "SPLASH_POTION_OF_", "LINGERING_POTION_OF_")) {
			list.add(add + "MINING_FATIQUE");
			list.add(add + "GLOWING");

			list.add(add + "HASTE");
			list.add(add + "HASTE2");
			list.add(add + "HASTE3");

			list.add(add + "INVISIBILITY");
			list.add(add + "INVISIBILITY2");

			list.add(add + "NIGHT_VISION");
			list.add(add + "NIGHT_VISION2");

			list.add(add + "LEAPING");
			list.add(add + "LEAPING2");
			list.add(add + "LEAPING3");

			list.add(add + "FIRE_RESISTANCE");
			list.add(add + "FIRE_RESISTANCE2");

			list.add(add + "SPEED");
			list.add(add + "SPEED2");
			list.add(add + "SPEED3");

			list.add(add + "SLOWNESS");
			list.add(add + "SLOWNESS2");
			list.add(add + "SLOWNESS3");

			list.add(add + "WATER_BREATHING");
			list.add(add + "WATER_BREATHING2");

			list.add(add + "HEALING");
			list.add(add + "HEALING2");

			list.add(add + "HARMING");
			list.add(add + "HARMING2");

			list.add(add + "POISON");
			list.add(add + "POISON2");
			list.add(add + "POISON3");

			list.add(add + "REGENERATION");
			list.add(add + "REGENERATION2");
			list.add(add + "REGENERATION3");

			list.add(add + "STRENGHT");
			list.add(add + "STRENGHT2");
			list.add(add + "STRENGHT3");

			list.add(add + "WEAKNESS");
			list.add(add + "WEAKNESS2");

			list.add(add + "LUCK");

			list.add(add + "SWIFTNESS");
			list.add(add + "SWIFTNESS2");
			list.add(add + "SWIFTNESS3");
			if (TheAPI.isNewVersion()) {
				list.add(add + "DOLPHINS_GRACE");
				list.add(add + "CONDUIT_POWER");
				list.add(add + "BAD_OMEN");
				list.add(add + "HERO_OF_VILLAGE");
				list.add(add + "TURTLE_MASTER");
				list.add(add + "TURTLE_MASTER2");
				list.add(add + "TURTLE_MASTER3");
				list.add(add + "SLOW_FALLING");
				list.add(add + "SLOW_FALLING2");
			}
		}
	}

	private ItemStack getPotion(String s) {
		Material args1 = XMaterial.POTION.getMaterial(); // type
		String args2 = null; // eff
		int args3 = 0; // level
		int args4 = 0; // time
		boolean multi = false;
		HashMap<PotionEffectType, String> a = new HashMap<>();
		if (s.toUpperCase().startsWith("POTION_OF_")) {
			args1 = XMaterial.POTION.getMaterial();
			args2 = s.toUpperCase().replaceFirst("POTION_OF_", "").replaceAll("[0-9]", "");
			args3 = StringUtils.getInt(s);
		}
		if (s.toUpperCase().startsWith("SPLASH_POTION_OF_")) {
			args1 = XMaterial.SPLASH_POTION.getMaterial();
			args2 = s.toUpperCase().replaceFirst("SPLASH_POTION_OF_", "").replaceAll("[0-9]", "");
			args3 = StringUtils.getInt(s);
		}
		if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
			args1 = XMaterial.LINGERING_POTION.getMaterial();
			args2 = s.toUpperCase().replaceFirst("LINGERING_POTION_OF_", "").replaceAll("[0-9]", "");
			args3 = StringUtils.getInt(s);

		}
		if (args1 == null)
			return null;
		switch (s.toUpperCase().replaceFirst("LINGERING_", "").replaceFirst("SPLASH_", "").replaceFirst("POTION_OF_",
				"")) {

		case "INVISIBILITY":
			args3 = 1;
			args4 = 180;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 45;
			}
			break;
		case "INVISIBILITY2":
			args3 = 1;
			args4 = 480;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 120;
			}
			break;
		case "NIGHT_VISION":
			args3 = 1;
			args4 = 180;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 45;
			}
			break;
		case "NIGHT_VISION2":
			args3 = 1;
			args4 = 480;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 120;
			}
			break;

		case "FIRE_RESISTANCE":
			args3 = 1;
			args4 = 180;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 45;
			}
			break;
		case "FIRE_RESISTANCE2":
			args3 = 1;
			args4 = 480;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 120;
			}
			break;
		case "SWIFTNESS":
		case "SPEED":
			args3 = 1;
			args4 = 180;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 45;
			}
			break;
		case "SWIFTNESS2":
		case "SPEED2":
			args3 = 1;
			args4 = 480;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 120;
			}
			break;
		case "SWIFTNESS3":
		case "SPEED3":
			args3 = 2;
			args4 = 90;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 22;
			}
			break;

		case "WATER_BREATHING":
			args3 = 1;
			args4 = 180;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 45;
			}
			break;
		case "WATER_BREATHING2":
			args3 = 1;
			args4 = 480;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 120;
			}
			break;

		case "HEALING":
			args3 = 1;
			args4 = 0;
			break;
		case "HEALING2":
			args3 = 2;
			args4 = 0;
			break;

		case "HARMING":
			args3 = 1;
			args4 = 0;
			break;
		case "HARMING2":
			args3 = 2;
			args4 = 0;
			break;

		case "POISON":
			args3 = 1;
			args4 = 45;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 11;
			}
			break;
		case "POISON2":
			args3 = 1;
			args4 = 90;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 22;
			}
			break;
		case "POISON3":
			args3 = 2;
			args4 = 21;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 5;
			}
			break;

		case "REGENERATION":
			args3 = 1;
			args4 = 45;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 11;
			}
			break;
		case "REGENERATION2":
			args3 = 1;
			args4 = 90;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 22;
			}
			break;
		case "REGENERATION3":
			args3 = 2;
			args4 = 22;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 5;
			}
			break;

		case "HASTE":
			args3 = 1;
			args4 = 180;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 45;
			}
			break;
		case "HASTE2":
			args3 = 1;
			args4 = 480;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 120;
			}
			break;
		case "HASTE3":
			args3 = 2;
			args4 = 90;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 22;
			}
			break;

		case "STRENGHT":
			args3 = 1;
			args4 = 180;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 45;
			}
			break;
		case "STRENGHT2":
			args3 = 1;
			args4 = 480;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 120;
			}
			break;
		case "STRENGHT3":
			args3 = 2;
			args4 = 90;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 22;
			}
			break;

		case "WEAKNESS":
			args3 = 1;
			args4 = 180;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 22;
			}
			break;
		case "WEAKNESS2":
			args3 = 1;
			args4 = 480;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 60;
			}
			break;
		case "LUCK":
			args3 = 1;
			args4 = 480;
			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 75;
			}
			break;
		case "TURTLE_MASTER":
			multi = true;
			a.put(PotionEffectType.SLOW, 4+":"+20);
			a.put(PotionEffectType.DAMAGE_RESISTANCE, 3+":"+20);
			break;
		case "TURTLE_MASTER2":
			multi = true;
			a.put(PotionEffectType.SLOW, 4+":"+40);
			a.put(PotionEffectType.DAMAGE_RESISTANCE, 3+":"+40);
			break;
		case "TURTLE_MASTER3":
			multi = true;
			a.put(PotionEffectType.SLOW, 4+":"+20);
			a.put(PotionEffectType.DAMAGE_RESISTANCE, 4+":"+20);
			break;

		case "CONDUIT_POWER":
		case "DOLPHINS_GRACE":
		case "BAD_OMEN":
		case "HERO_OF_VILLAGE":
		case "GLOWING":
		case "SLOW_FALLING":
		case "MINING_FATIQUE":
			args3 = 1;
			args4 = 90;

			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 22;
			}
			break;
		case "SLOW_FALLING2":
			args3 = 1;
			args4 = 240;

			if (s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4 = 60;
			}
			break;

		}
		args2 = args2.replace("STRENGHT", "INCREASE_DAMAGE").replace("HARMING", "HARM").replace("HEALING", "HEAL")
				.replace("SWIFTNESS", "SPEED");

		ItemStack h = new ItemStack(args1);
		PotionMeta g = (PotionMeta) h.getItemMeta();
		if (multi) {
			for (PotionEffectType f : a.keySet()) { // effect
				String[] o = a.get(f).split(":");// values
				int i = StringUtils.getInt(o[0].toString());
				int ib = StringUtils.getInt(o[1].toString());
				g.addCustomEffect(new PotionEffect(f, i * 20, (ib == 0 ? 1 : ib)), true);
			}
		} else
			g.addCustomEffect(new PotionEffect(PotionEffectType.getByName(args2), args4 * 20, (args3 == 0 ? 1 : args3)),
					true);

		if (s.toUpperCase().startsWith("LINGERING_POTION_OF_"))
			g.addCustomEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 20, 1), true);
		h.setItemMeta(g);
		return h;
	}

	public boolean existsItem(String s) {
		return XMaterial.matchXMaterial(s.toUpperCase())!=null;
	}

	private void giveItem(CommandSender sender, Player target, String type, int amount, String nbt) {
		type = type.toLowerCase().replaceFirst("minecraft:", "").toUpperCase();
		ItemStack stack;
		try {
			if (!type.startsWith("LINGERING_POTION_OF_") && !type.startsWith("SPLASH_POTION_OF_")
					&& !type.startsWith("POTION_OF_")) {
				stack = XMaterial.matchXMaterial(type).parseItem(amount);
			}else {
				stack = getPotion(type);
				stack.setAmount(amount);
			}
		}catch(Exception err) {
			Loader.sendMessages(sender, "Missing.Material", Placeholder.c().add("%material%", type));
			return;
		}
		if(nbt!=null && nbt.startsWith("{") && nbt.endsWith("}")) {
			stack=NMSAPI.setNBT(stack, nbt);
		}
		TheAPI.giveItem(target, stack);
		if(target==null||sender==target)
			Loader.sendMessages(sender, "Give.Item.You", Placeholder.c().add("%item%", type.toUpperCase()).add("%amount%", amount+"").add("%nbt%", nbt==null?"":nbt));
		else {
			Loader.sendMessages(sender, "Give.Item.Other.Sender", Placeholder.c().add("%item%", type.toUpperCase()).add("%amount%", amount+"")
					.add("%nbt%", nbt==null?"":nbt)
					.add("%player%", target.getName()).replace("%playername%", target.getDisplayName()));
			Loader.sendMessages(target, "Give.Item.Other.Receiver", Placeholder.c().add("%item%", type.toUpperCase()).add("%amount%", amount+"")
					.add("%nbt%", nbt==null?"":nbt)
					.add("%player%", sender.getName()).replace("%playername%", sender.getName()));
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Give", "Other")) {
			if(!CommandsManager.canUse("Other.Give", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Give", s))));
				return true;
			}
			switch(args.length) {
			case 0:
				Loader.Help(s, "Give", "Other");
				break;
			case 1:
				giveItem(s, (Player)s, args[0], 1, null);
				break;
			case 2:
				if(TheAPI.getPlayer(args[0])!=null)
					giveItem(s, TheAPI.getPlayer(args[0]), args[1], 1, null);
				else {
					if(args[1].startsWith("{"))
						giveItem(s, (Player)s, args[0], 1, args[1]);
					else
						giveItem(s, (Player)s, args[0], StringUtils.getInt(args[1]), null);
				}
				break;
			case 3://give STONE 1 NBT
				//give PLAYER STONE NBT/1
				if(TheAPI.getPlayer(args[0])!=null) {
					if(args[2].startsWith("{"))
						giveItem(s, TheAPI.getPlayer(args[0]), args[1], 1, args[2]);
					else
						giveItem(s, TheAPI.getPlayer(args[0]), args[1], StringUtils.getInt(args[2]), null);
				}else {
					if(args[2].startsWith("{")) {
						giveItem(s, (Player)s, args[0], StringUtils.getInt(args[1]), args[2]);
					}else
						Loader.notOnline(s, args[0]);
				}
				break;
			default://give PLAYER STONE 1 NBT
				if(TheAPI.getPlayer(args[0])!=null)
					giveItem(s, TheAPI.getPlayer(args[0]), args[1], StringUtils.getInt(args[2]), StringUtils.buildString(3,args));
				else
					Loader.notOnline(s, args[0]);
				break;
			}
			return true;
		}
		Loader.noPerms(s, "Give", "Other");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (Loader.has(s, "Give", "Other")) {
			if (args.length == 1)
				return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
			if (args.length == 2) {
				return StringUtils.copyPartialMatches(args[1], list);
			}
			if (args.length == 3) {
				return StringUtils.copyPartialMatches(args[2], Arrays.asList("1", "3", "5", "12", "16", "32","64"));
			}
		}
		return c;
	}
}
