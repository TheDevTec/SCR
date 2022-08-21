package me.devtec.scr.commands.kits;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.message.Sudo;
import me.devtec.scr.commands.message.Sudo.SudoType;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;
import net.milkbowl.vault.economy.Economy;

public class Kit implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		KitUtils.loadKits();

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if (hasPermission(s, "other") || !(s instanceof Player))
				help(s, "admin");
			else
				help(s, "help");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
			msgSec(s, "notFound", Placeholders.c().add("kit", args[0]));
		}).callableArgument((s, structure, args) -> KitUtils.getKitsFor(s), (s, structure, args) -> { // kit [kit]
			if (!(s instanceof Player)) {
				help(s, "admin");
				return;
			}
			kit(s, s, args[0], true, true, true, false);
			/*
			 * me.devtec.scr.commands.kits.KitUtils.Kit kit =
			 * KitUtils.loaded_kits.get(args[0]); if(s.hasPermission(kit.permission())) { //
			 * PERMISSION User u = API.getUser(s);
			 * if(u.cooldownExpired("kits."+kit.getName(), kit.getCooldownTime())) { //
			 * COOLDOWN if(kit.config.exists("cost") && Loader.economy != null) { // MONEY
			 * double money = ScrEconomy.balanceFromString(kit.getCost()); String bal =
			 * ((Economy) Loader.economy).format(money);
			 * 
			 * Economy ec = (Economy) Loader.economy; if (!ec.has(s.getName(), money)) {
			 * msgSec(s, "notEnoughMoney", Placeholders.c().replace("money", bal)); return;
			 * } if(ec.has(s.getName(), money)) ec.withdrawPlayer((Player) s, money); }
			 * kit.giveItems(s); // giving items to player //COMMAND
			 * if(kit.config.exists("commands")) for(String cmd :
			 * kit.config.getStringList("commands")) Sudo.sudoConsole(SudoType.COMMAND,
			 * PlaceholderAPISupport.replace(cmd, s, true,
			 * Placeholders.c().addPlayer("player", s))); //MESSAGE
			 * if(kit.config.exists("messages")) MessageUtils.msgConfig(s, "messages",
			 * kit.config, Placeholders.c() .add("cooldown",
			 * StringUtils.timeFromString(kit.getCooldownTime())) .add("kit",
			 * kit.displayName()).add("kit_name", kit.getName()) .add("cost", ((Economy)
			 * Loader.economy).format( ScrEconomy.balanceFromString(kit.getCost()))) ); else
			 * msgSec(s, "used", Placeholders.c() .add("cooldown",
			 * StringUtils.timeFromString(kit.getCooldownTime())) .add("kit",
			 * kit.displayName()).add("kit_name", kit.getName()) .add("cost", ((Economy)
			 * Loader.economy).format( ScrEconomy.balanceFromString(kit.getCost()))) ); }
			 * else { // cooldown msg msgSec(s, "cooldown", Placeholders.c() .add("time",
			 * StringUtils.timeToString(u.expires("kits."+kit.getName(),
			 * kit.getCooldownTime()))) .add("kit", kit.displayName()).add("kit_name",
			 * kit.getName())); return; } } else { // no perms MessageUtils.noPerm(s,
			 * kit.permission()); return; }
			 */
		}).argument("-s", (s, structure, args) -> { // kit [kit] -s
			if (!(s instanceof Player)) {
				help(s, "admin");
				return;
			}
			kit(s, s, args[0], true, true, true, true);
		}).parent() // kit [kit]
				.fallback((s, structure, args) -> { // kit [kit] [player]
					offlinePlayer(s, args[1]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // kit [kit] [player]
					for (Player target : playerSelectors(s, args[1]))
						kit(s, target, args[0], true, true, true, false);
				}).permission(permission("other"))
				// -c without cooldown
				// -p without perm
				// -e without economy check
				.argument(null, (s, structure, args) -> { // kit [kit] [player] [aditionals]
					String aditional = args[2];
					if (aditional.startsWith("-")) {
						boolean cooldown = false, perm = false, economy = true, silent = false;
						aditional = aditional.replace("-", "");
						for (String ad : aditional.split("")) {
							if (ad.equalsIgnoreCase("c"))
								cooldown = false;
							if (ad.equalsIgnoreCase("p"))
								perm = false;
							if (ad.equalsIgnoreCase("e"))
								economy = false;
							if (ad.equalsIgnoreCase("s"))
								silent = true;
						}
						for (Player target : playerSelectors(s, args[1]))
							kit(s, target, args[0], perm, cooldown, economy, silent);
					} else
						help(s, "admin");
				})

				.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "kit";
	}

	// permission true == yes check perm
	// cooldown true == yes check cooldown
	// economy true == yes check economy
	// silent true == without messages
	public void kit(CommandSender sender, CommandSender target, String kit_name, boolean permission, boolean cooldown, boolean economy, boolean silent) {
		me.devtec.scr.commands.kits.KitUtils.Kit kit = KitUtils.loaded_kits.get(kit_name);
		if (!permission || sender.hasPermission(kit.permission())) { // PERMISSION
			User u = API.getUser(target);
			// COOLDOWN
			if (!cooldown || u.cooldownExpired("kits." + kit.getName(), kit.getCooldownTime())) {
				if (economy && kit.config.exists("cost") && Loader.economy != null) { // MONEY
					double money = ScrEconomy.balanceFromString(kit.getCost());
					String bal = ((Economy) Loader.economy).format(money);

					Economy ec = (Economy) Loader.economy;
					if (!ec.has(target.getName(), money)) {
						msgSec(target, "notEnoughMoney", Placeholders.c().replace("money", bal));
						return;
					}
					if (ec.has(target.getName(), money))
						ec.withdrawPlayer((Player) target, money);
				}
				kit.giveItems(target); // giving items to player
				// COMMAND
				if (kit.config.exists("commands"))
					for (String cmd : kit.config.getStringList("commands"))
						Sudo.sudoConsole(SudoType.COMMAND, PlaceholderAPISupport.replace(cmd, target, Placeholders.c().addPlayer("player", target)));
				// MESSAGE
				if (!silent && kit.config.exists("messages"))
					MessageUtils.msgConfig(target, "messages", kit.config, Placeholders.c().add("cooldown", StringUtils.timeFromString(kit.getCooldownTime())).add("kit", kit.displayName())
							.add("kit_name", kit.getName()).add("cost", ((Economy) Loader.economy).format(ScrEconomy.balanceFromString(kit.getCost()))));
				else if (!silent)
					msgSec(target, "give.receiver", Placeholders.c().addPlayer("target", target).add("cooldown", StringUtils.timeFromString(kit.getCooldownTime())).add("kit", kit.displayName())
							.add("kit_name", kit.getName()).add("cost", ((Economy) Loader.economy).format(ScrEconomy.balanceFromString(kit.getCost()))));
				if (!sender.getName().equalsIgnoreCase(target.getName()))
					msgSec(sender, "give.sender", Placeholders.c().addPlayer("target", target).add("cooldown", StringUtils.timeFromString(kit.getCooldownTime())).add("kit", kit.displayName())
							.add("kit_name", kit.getName()).add("cost", ((Economy) Loader.economy).format(ScrEconomy.balanceFromString(kit.getCost()))));
			} else
				msgSec(target, "cooldown",
						Placeholders.c().add("time", StringUtils.timeToString(u.expires("kits." + kit.getName(), kit.getCooldownTime()))).add("kit", kit.displayName()).add("kit_name", kit.getName()));
		} else
			MessageUtils.noPerm(target, kit.permission());

	}

}
