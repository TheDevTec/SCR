package me.devtec.scr.commands.economy;

import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import net.milkbowl.vault.economy.Economy;

public class Pay implements ScrCommand {
	
	@Override
	public void init(int cd, List<String> cmds) {
		if(Loader.economy == null)return;
		// /pay [player] [amount]
		CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, "usage");
		}).permission(permission("cmd")).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
			})
			.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [entity_selector]
				help(s, "usage");
			}).fallback((s, structure, args) -> {
				help(s, "usage");
				})
				.argument(null, (s, structure, args) -> { // cmd [entity_selector] [any string]
					double money = ScrEconomy.balanceFromString(args[1]);
					String bal = ((Economy)Loader.economy).format(money);
					for(Player p : playerSelectors(s, args[0])) {
						pay(s, p, money);
						
						msgSec(s, "sender", Placeholders.c().replace("target", p.getName()).replace("money", bal));
						msgSec(p, "target", Placeholders.c().replace("player", s.getName()).replace("money", bal));
					}
					})
					.argument("-s", (s, structure, args) -> { // cmd [entity_selector] [any string] -s
						double money = ScrEconomy.balanceFromString(args[1]);
						for(Player p : playerSelectors(s, args[0])) {
							pay(s, p, money);
						}
					}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}
	
	private void pay(Player sender, Player target, double money) {
		Economy ec = (Economy)Loader.economy;
		ec.withdrawPlayer(sender, money);
		ec.depositPlayer(target, money);
	}

	@Override
	public String configSection() {
		return "pay";
	}
	
}
