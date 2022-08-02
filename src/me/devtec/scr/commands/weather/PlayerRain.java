package me.devtec.scr.commands.weather;

import java.util.List;

import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class PlayerRain implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			apply((Player) s);
			msg(s, "weather.pRain.self");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
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
						msg(s, "weather.pRain.sender", Placeholders.c().addPlayer("target", player));
						msg(s, "weather.pRain.target", Placeholders.c().addPlayer("player", s));
					}
				}).permission(permission("other"))
				// psun [target] -s
				.argument("-s", (s, structure, args) -> {
					for (Player player : playerSelectors(s, args[0]))
						apply(player);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure();
	}

	public void apply(Player player) {
		player.setPlayerWeather(WeatherType.DOWNFALL);
	}

	@Override
	public String configSection() {
		return "prain";
	}

}
