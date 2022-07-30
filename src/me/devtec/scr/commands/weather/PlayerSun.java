package me.devtec.scr.commands.weather;

import java.util.List;

import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class PlayerSun implements ScrCommand {

	@Override
	public void init(int cd, List<String> cmds) {
		cooldownMap.put(CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			apply((Player) s);
			msg(s, "weather.pSun.self");
		}).cooldownDetection(COOLDOWN).permission(permission("cmd")).fallback((s, structure, args) -> {
			help(s, "usage");
		})
				// psun -s
				.argument("-s", (s, structure, args) -> {
					if (!(s instanceof Player)) {
						help(s, "usage");
						return;
					}
					apply((Player) s);
				}).first() // psun
				// psun [target]
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> {
					for (Player player : playerSelectors(s, args[0])) {
						apply(player);
						msg(s, "weather.pSun.sender", Placeholders.c().add("target", player.getName()));
						msg(s, "weather.pSun.target", Placeholders.c().add("player", s.getName()));
					}
				}).permission(permission("other"))
				// psun [target] -s
				.argument("-s", (s, structure, args) -> {
					for (Player player : playerSelectors(s, args[0]))
						apply(player);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure(), new CooldownHolder(this, cd));
	}

	public void apply(Player player) {
		player.setPlayerWeather(WeatherType.CLEAR);
	}

	@Override
	public String configSection() {
		return "psun";
	}

}
