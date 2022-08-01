package me.devtec.scr.commands.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.listeners.fun.AntiDamage;
import me.devtec.shared.Ref;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class God implements ScrCommand {

	public static List<UUID> antiDamage;
	boolean legacy = Ref.isOlderThan(10);
	
	@Override
	public void init(int cd, List<String> cmds) {
		if(legacy) { //We have to use listener
			antiDamage = new ArrayList<>();
	        Loader.plugin.getLogger().info("[God] Using AntiDamage listener");
			Loader.registerListener(new AntiDamage());
		}
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if(s instanceof Player) {
				Player p = (Player)s;
				if(legacy) {
					applyLegacy(p, isInvulnerable(p));
				}else {
					applyModern(p, isInvulnerable(p));
				}
				
				String status = isInvulnerable(p) ? "enabled" : "disabled";
				msgSec(s, ""+status);
			}else {
				help(s, "admin_usage");
			}
		}).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
			}).permission(permission(""))
			.argument("-s", (s, structure, args) -> { // cmd -s
				if(s instanceof Player) {
					Player p = (Player)s;
					if(legacy) {
						applyLegacy(p, isInvulnerable(p));
					}else {
						applyModern(p, isInvulnerable(p));
					}
				}else {
					help(s, "admin_usage");
				}
			})
			.parent() // cmd 
			.selector(Selector.BOOLEAN, (s, structure, args) -> { // cmd [boolean]
				if(s instanceof Player) {
					Player p = (Player)s;
					if(legacy) {
						applyLegacy(p, !Boolean.parseBoolean(args[0]));
					}else {
						applyModern(p, !Boolean.parseBoolean(args[0]));
					}
					
					String status = Boolean.parseBoolean(args[0]) ? "enabled" : "disabled";
					msgSec(s, ""+status);
				}else {
					help(s, "admin_usage");
				}
				})
				.argument("-s", (s, structure, args) -> { // cmd [boolean] -s
					if(s instanceof Player) {
						Player p = (Player)s;
						if(legacy) {
							applyLegacy(p, !Boolean.parseBoolean(args[0]));
						}else {
							applyModern(p, !Boolean.parseBoolean(args[0]));
						}
					}else {
						help(s, "admin_usage");
					}
				})
				.parent() // cmd [boolean]
			.parent() // cmd
			.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [entity_selector]
				for(Player p : playerSelectors(s, args[0])) {
					if(legacy) {
						applyLegacy(p, isInvulnerable(p));
					}else {
						applyModern(p, isInvulnerable(p));
					}
					
					String status = isInvulnerable(p) ? "enabled" : "disabled";
					msgSec(s, "other."+status+".sender", Placeholders.c().replace("target", p.getName()));
					msgSec(p, "other."+status+".target", Placeholders.c().replace("target", s.getName()));
				}
			}).permission(permission("other"))
			.fallback((s, structure, args) -> {
				help(s, "admin_usage");
				})
				.argument("-s", (s, structure, args) -> { // cmd [entity_selector] -s
					for(Player p : playerSelectors(s, args[0])) {
						if(legacy) {
							applyLegacy(p, isInvulnerable(p));
						}else {
							applyModern(p, isInvulnerable(p));
						}
					}
				})
				.parent() // cmd [entity_selector]
				.selector(Selector.BOOLEAN, (s, structure, args) -> { // cmd [entity_selector] [boolean]
					for(Player p : playerSelectors(s, args[0])) {
						if(legacy) {
							applyLegacy(p, !Boolean.parseBoolean(args[1]));
						}else {
							applyModern(p, !Boolean.parseBoolean(args[1]));
						}
						
						String status = Boolean.parseBoolean(args[1]) ? "enabled" : "disabled";
						msgSec(s, "other."+status+".sender", Placeholders.c().replace("target", p.getName()));
						msgSec(p, "other."+status+".target", Placeholders.c().replace("target", s.getName()));
					}
					})
					.argument("-s", (s, structure, args) -> { // cmd [entity_selector] [boolean] -s
						for(Player p : playerSelectors(s, args[0])) {
							if(legacy) {
								applyLegacy(p, !Boolean.parseBoolean(args[1]));
							}else {
								applyModern(p, !Boolean.parseBoolean(args[1]));
							}
						}
					}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}
	
	public boolean isInvulnerable(Player p) {
		if(!legacy) {
			return p.isInvulnerable();
		}
		return antiDamage.contains(p.getUniqueId());
	}
	
	public void applyLegacy(Player p, boolean status) {
		if(status) {
			antiDamage.remove(p.getUniqueId());
		}else {
			antiDamage.add(p.getUniqueId());
		}
	}
	
	public void applyModern(Player p, boolean status) {
		if(status) {
			p.setInvulnerable(false);
		}else {
			p.setInvulnerable(true);
		}
	}

	@Override
	public String configSection() {
		return "god";
	}
	
}
