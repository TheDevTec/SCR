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

	@SuppressWarnings("deprecation")
	@Override
	public void init(List<String> cmds) {
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if(hasPermission(s, "other") || !(s instanceof Player))
				help(s, "admin");
			else
				help(s, "help");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
			.fallback((s, structure, args) -> {
				msgSec(s, "notFound", Placeholders.c().add("kit", args[0]));
			})
			.callableArgument((s, structure, args) -> KitUtils.getKitsFor(s), (s, structure, args) -> { // kit [kit]
				if( !(s instanceof Player)) {
					help(s, "admin");
					return;
				}
				me.devtec.scr.commands.kits.KitUtils.Kit kit = KitUtils.loaded_kits.get(args[0]);
				if(s.hasPermission(kit.permission())) { // PERMISSION
					User u = API.getUser(s);
					if(u.cooldownExpired("kits."+kit.getName(), kit.getCooldownTime())) { // COOLDOWN
						if(kit.config.exists("cost") && Loader.economy != null) { // MONEY
							double money = ScrEconomy.balanceFromString(kit.getCost());
							String bal = ((Economy) Loader.economy).format(money);

							Economy ec = (Economy) Loader.economy;
							if (!ec.has(s.getName(), money)) {
								msgSec(s, "notEnoughMoney", Placeholders.c().replace("money", bal));	
								return;
							}
							if(ec.has(s.getName(), money))
								ec.withdrawPlayer((Player) s, money);
						}
						kit.giveItems(s); // giving items to player
						//COMMAND
						if(kit.config.exists("commands"))
							for(String cmd : kit.config.getStringList("commands"))
								Sudo.sudoConsole(SudoType.COMMAND, 
										PlaceholderAPISupport.replace(cmd, s, true, Placeholders.c().addPlayer("player", s)));
						//MESSAGE
						if(kit.config.exists("messages"))
							MessageUtils.msgConfig(s, "messages", kit.config, 
									Placeholders.c()
									.add("cooldown", StringUtils.timeFromString(kit.getCooldownTime()))
									.add("kit", kit.displayName()).add("kit_name", kit.getName())
									.add("cost", ((Economy) Loader.economy).format(
											ScrEconomy.balanceFromString(kit.getCost())))
									);
						else
							msgSec(s, "used", Placeholders.c()
									.add("cooldown", StringUtils.timeFromString(kit.getCooldownTime()))
									.add("kit", kit.displayName()).add("kit_name", kit.getName())
									.add("cost", ((Economy) Loader.economy).format(
											ScrEconomy.balanceFromString(kit.getCost())))
									);
					} else { // cooldown msg
						msgSec(s, "cooldown", Placeholders.c()
								.add("time", StringUtils.timeToString(u.expires("kits."+kit.getName(), kit.getCooldownTime())))
								.add("kit", kit.displayName()).add("kit_name", kit.getName()));
					return;
					}
				} else { // no perms
					MessageUtils.noPerm(s, kit.permission());
					return;
				}
			})
				.fallback((s, structure, args) -> { // kit [kit] [player]
					offlinePlayer(s, args[1]);
				})
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // kit [kit] [player]
					for(Player target : playerSelectors(s, args[1])) {
						me.devtec.scr.commands.kits.KitUtils.Kit kit = KitUtils.loaded_kits.get(args[0]);
						if(s.hasPermission(kit.permission())) { // PERMISSION
							User u = API.getUser(s);
							if(u.cooldownExpired("kits."+kit.getName(), kit.getCooldownTime())) { // COOLDOWN
								if(kit.config.exists("cost") && Loader.economy != null) { // MONEY
									double money = ScrEconomy.balanceFromString(kit.getCost());
									String bal = ((Economy) Loader.economy).format(money);

									Economy ec = (Economy) Loader.economy;
									if (!ec.has(s.getName(), money)) {
										msgSec(s, "notEnoughMoney", Placeholders.c().replace("money", bal));	
										return;
									}
									if(ec.has(s.getName(), money))
										ec.withdrawPlayer((Player) s, money);
								}
								kit.giveItems(target); // giving items to player
								//COMMAND
								if(kit.config.exists("commands"))
									for(String cmd : kit.config.getStringList("commands"))
										Sudo.sudoConsole(SudoType.COMMAND, 
												PlaceholderAPISupport.replace(cmd, s, true, Placeholders.c().addPlayer("player", target)));
								//MESSAGE
								if(kit.config.exists("messages"))
									MessageUtils.msgConfig(target, "messages", kit.config, 
											Placeholders.c()
											.add("cooldown", StringUtils.timeFromString(kit.getCooldownTime()))
											.add("kit", kit.displayName()).add("kit_name", kit.getName())
											.add("cost", ((Economy) Loader.economy).format(
													ScrEconomy.balanceFromString(kit.getCost())))
											);
								else
									msgSec(target, "give.receiver", Placeholders.c()
											.addPlayer("target", target)
											.add("cooldown", StringUtils.timeFromString(kit.getCooldownTime()))
											.add("kit", kit.displayName()).add("kit_name", kit.getName())
											.add("cost", ((Economy) Loader.economy).format(
													ScrEconomy.balanceFromString(kit.getCost())))
											);
								msgSec(s, "give.sender", Placeholders.c()
										.addPlayer("target", target)
										.add("cooldown", StringUtils.timeFromString(kit.getCooldownTime()))
										.add("kit", kit.displayName()).add("kit_name", kit.getName())
										.add("cost", ((Economy) Loader.economy).format(
												ScrEconomy.balanceFromString(kit.getCost())))
											);
							} else { // cooldown msg
								msgSec(s, "cooldown", Placeholders.c()
										.add("time", StringUtils.timeToString(u.expires("kits."+kit.getName(), kit.getCooldownTime())))
										.add("kit", kit.displayName()).add("kit_name", kit.getName()));
							return;
							}
						} else { // no perms
							MessageUtils.noPerm(s, kit.permission());
							return;
						}
					}
				}).permission(permission("other"))
			
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "kit";
	}

}
