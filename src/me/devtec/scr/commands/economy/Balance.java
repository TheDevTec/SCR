package me.devtec.scr.commands.economy;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import net.milkbowl.vault.economy.Economy;

public class Balance implements ScrCommand {
	
	@Override
	public void init(int cd, List<String> cmds) {
		if(Loader.economy == null)return;
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if(s instanceof Player)
			//msgSec(s, "balance", ((Economy)Loader.economy).format(((Economy)Loader.economy).getBalance((Player)s)) );
			msgSec(s, "balance", Placeholders.c()
					.replace("money", ((Economy)Loader.economy).format(((Economy)Loader.economy).getBalance((Player)s))) );
			else help(s, "usage");
		}).permission("scr."+configSection()).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
			})
			.selector(Selector.PLAYER, (s, structure, args) -> { // cmd [player]
				Player p = Bukkit.getPlayer(args[0]);
				//msgSec(s, "balance-other", p.getName(), ((Economy)Loader.economy).format(((Economy)Loader.economy).getBalance(p)));
				msgSec(s, "balance-other", Placeholders.c()
						.replace("target", p.getName()).replace("money", ((Economy)Loader.economy).format(((Economy)Loader.economy).getBalance(p))) );
			}).permission("scr."+configSection()+".other").build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "balance";
	}
	
}
