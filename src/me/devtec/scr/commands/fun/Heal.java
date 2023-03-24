package me.devtec.scr.commands.fun;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.ParseUtils;

public class Heal implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // feed
			if (s instanceof Player) {
				Player p = (Player) s;
				p.setFoodLevel(20);
				p.setFireTicks(-20);
				p.setHealth(((Damageable) p).getMaxHealth());
				for (PotionEffect e : p.getActivePotionEffects())
					p.removePotionEffect(e.getType());

				msgSec(s, "you");
			} else
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).argument("-s", (s, structure, args) -> { // heal -s
			if (s instanceof Player) {
				Player p = (Player) s;
				p.setFoodLevel(20);
				p.setFireTicks(-20);
				p.setHealth(((Damageable) p).getMaxHealth());
				for (PotionEffect e : p.getActivePotionEffects())
					p.removePotionEffect(e.getType());
			} else
				help(s, "usage");
		}).parent() // heal

				.fallback((s, structure, args) -> { // heal {level}
					msg(s, "missing.number", Placeholders.c().replace("number", args[0]));
				}).selector(Selector.NUMBER, (s, structure, args) -> { // heal {level}
					if (s instanceof Player) {
						Player p = (Player) s;
						double level = ParseUtils.getDouble(args[0]);
						if (level < 0)
							level = 0;
						if (level > 20)
							level = 20;
						p.setFoodLevel(20);
						p.setFireTicks(-20);
						p.setHealth(level);
						for (PotionEffect e : p.getActivePotionEffects())
							p.removePotionEffect(e.getType());
						msgSec(s, "you");
					} else
						help(s, "usage");
				}, (s, structure, args) -> {
					List<String> list = new ArrayList<>();
					for (int i = 0; i <= 20; i++)
						list.add("" + i);
					return list;
				}).permission(permission("heal_level")).argument("-s", (s, structure, args) -> { // heal {level} -s
					if (s instanceof Player) {
						Player p = (Player) s;
						double level = ParseUtils.getDouble(args[0]);
						if (level < 0)
							level = 0;
						if (level > 20)
							level = 20;
						p.setFoodLevel(20);
						p.setFireTicks(-20);
						p.setHealth(level);
						for (PotionEffect e : p.getActivePotionEffects())
							p.removePotionEffect(e.getType());
					} else
						help(s, "usage");
				}).parent() // heal {level}
				.parent() // heal

				.fallback((s, structure, args) -> { // heal [player]
					offlinePlayer(s, args[0]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // heal [player]
					for (Player p : playerSelectors(s, args[0])) {
						p.setFoodLevel(20);
						p.setFireTicks(-20);
						p.setHealth(((Damageable) p).getMaxHealth());
						for (PotionEffect e : p.getActivePotionEffects())
							p.removePotionEffect(e.getType());
						msgSec(s, "other.sender", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
						msgSec(p, "other.receiver", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
					}
				}).permission(permission("other")).argument("-s", (s, structure, args) -> { // heal [player] -s
					for (Player p : playerSelectors(s, args[0])) {
						p.setFoodLevel(20);
						p.setFireTicks(-20);
						p.setHealth(((Damageable) p).getMaxHealth());
						for (PotionEffect e : p.getActivePotionEffects())
							p.removePotionEffect(e.getType());
					}
				}).parent() // heal [player]
				.fallback((s, structure, args) -> { // heal [player] {level}
					msg(s, "missing.number", Placeholders.c().replace("number", args[0]));
				}).selector(Selector.NUMBER, (s, structure, args) -> { // heal [player] {level}
					for (Player p : playerSelectors(s, args[0])) {
						double level = ParseUtils.getDouble(args[1]);
						if (level < 0)
							level = 0;
						if (level > 20)
							level = 20;
						p.setFoodLevel(20);
						p.setFireTicks(-20);
						p.setHealth(level);
						for (PotionEffect e : p.getActivePotionEffects())
							p.removePotionEffect(e.getType());
						msgSec(s, "other.sender", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
						msgSec(p, "other.receiver", Placeholders.c().addPlayer("target", p).addPlayer("player", s));
					}
				}, (s, structure, args) -> {
					List<String> list = new ArrayList<>();
					for (int i = 0; i <= 20; i++)
						list.add("" + i);
					return list;
				}).permission(permission("heal_level")).argument("-s", (s, structure, args) -> { // heal [player] {level} -s
					for (Player p : playerSelectors(s, args[0])) {
						double level = ParseUtils.getDouble(args[1]);
						if (level < 0)
							level = 0;
						if (level > 20)
							level = 20;
						p.setFoodLevel(20);
						p.setFireTicks(-20);
						p.setHealth(level);
						for (PotionEffect e : p.getActivePotionEffects())
							p.removePotionEffect(e.getType());
					}
				})

				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "heal";
	}
}
