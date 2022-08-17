package me.devtec.scr.commands.info;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.message.Sudo;
import me.devtec.scr.commands.message.Sudo.SudoType;
import me.devtec.scr.listeners.AFKListeners;
import me.devtec.scr.listeners.fun.AntiFallDamage;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;

public class AFK implements ScrCommand {

	// player | start time
	public static HashMap<String, Long> players = new HashMap<>();
	// player | last interaction
	public static HashMap<String, Long> last_interaction = new HashMap<>();
	
	@Override
	public void init(List<String> cmds) {

		Loader.plugin.getLogger().info("[AFK] Using AFK listener");
		Loader.registerListener(new AFKListeners());
		
		startTask();
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { //afk
			if(players.containsKey(s.getName())) { // turn off
				MessageUtils.message(s, configSection()+".end", Placeholders.c().addPlayer("player", s)
						, false, API.getOnlinePlayersThatcanSee(s, true).toArray(new CommandSender[0]));
				players.remove(s.getName());
			}else { //turn on
				players.put(s.getName(), System.currentTimeMillis()/1000);
				MessageUtils.message(s, configSection()+".start", Placeholders.c().addPlayer("player", s)
						, false, API.getOnlinePlayersThatcanSee(s, true).toArray(new CommandSender[0]));
				//TODO - task
			}
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
			.argument("-s", (s, structure, args) -> { // afk -s
				if(players.containsKey(s.getName())) { // turn off
					players.remove(s.getName());
				}else { //turn on
					players.put(s.getName(), System.currentTimeMillis()/1000);
					//TODO - task
				}
			}).priority(1)
		.parent() // afk
		.argument(null, (s, structure, args) -> { // afk [reason]
			if(players.containsKey(s.getName())) { // turn off
				players.remove(s.getName());
				MessageUtils.message(s, configSection()+".end", Placeholders.c().addPlayer("player", s)
						, false, API.getOnlinePlayersThatcanSee(s, true).toArray(new CommandSender[0]));
			}else { //turn on
				players.put(s.getName(), System.currentTimeMillis()/1000);
				MessageUtils.message(s, configSection()+".start_reson",
						Placeholders.c().addPlayer("player", s).add("reason", StringUtils.buildString(args))
						, false, API.getOnlinePlayersThatcanSee(s, true).toArray(new CommandSender[0]));
				//TODO - task
			}
		}).priority(2)
		.first() // afk
		.fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
		})
			.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // afk [player]
				for(Player p : playerSelectors(s, args[0])) {
					if(players.containsKey(p.getName())) { // turn off
						MessageUtils.message(p, configSection()+".end", Placeholders.c().addPlayer("player", p)
								, false, API.getOnlinePlayersThatcanSee(p, true).toArray(new CommandSender[0]));
						players.remove(p.getName());
					}else { //turn on
						players.put(p.getName(), System.currentTimeMillis()/1000);
						MessageUtils.message(p, configSection()+".start", Placeholders.c().addPlayer("player", p)
								, false, API.getOnlinePlayersThatcanSee(p, true).toArray(new CommandSender[0]));
						//TODO - task
					}
				}
			}).permission(permission("other"))
				.argument("-s", (s, structure, args) -> { // afk [player] -s
					for(Player p : playerSelectors(s, args[0])) {
						if(players.containsKey(p.getName())) { // turn off
							players.remove(p.getName());
						}else { //turn on
							players.put(p.getName(), System.currentTimeMillis()/1000);
							//TODO - task
						}
					}
				})
			.parent() // afk [player]
			.argument(null, (s, structure, args) -> {
				for(Player p : playerSelectors(s, args[0])) {
					if(players.containsKey(p.getName())) { // turn off
						MessageUtils.message(p, configSection()+".end", Placeholders.c().addPlayer("player", p)
								, false, API.getOnlinePlayersThatcanSee(p, true).toArray(new CommandSender[0]));
						players.remove(p.getName());
					}else { //turn on
						players.put(p.getName(), System.currentTimeMillis()/1000);
						MessageUtils.message(p, configSection()+".start_reson",
								Placeholders.c().addPlayer("player", p).add("reason", StringUtils.buildString(1, args))
								, false, API.getOnlinePlayersThatcanSee(p, true).toArray(new CommandSender[0]));
						//TODO - task
					}
				}
			})
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "afk";
	}
	public static void afk(CommandSender player, boolean enable) {
		if(enable) { // turn on
			players.put(player.getName(), System.currentTimeMillis()/1000);
			MessageUtils.message(player, "afk.start", Placeholders.c().addPlayer("player", player)
					, false, API.getOnlinePlayersThatcanSee(player, true).toArray(new CommandSender[0]));
		}
		else { // turn off
			MessageUtils.message(player, "afk.end", Placeholders.c().addPlayer("player", player)
					, false, API.getOnlinePlayersThatcanSee(player, true).toArray(new CommandSender[0]));
			players.remove(player.getName());
		}
	}
	public static void update(CommandSender player) {
		if(last_interaction.containsKey(player.getName()))
			last_interaction.remove(player.getName());
		last_interaction.put(player.getName(), System.currentTimeMillis()/1000);
	}
	
	
	private int task;
	
	private void startTask() {
		
		task = new Tasker() {
			
			@Override
			public void run() {
				
				for(String player : players.keySet()) {
					if(Bukkit.getPlayer(player)==null) { // if offline
						players.remove(player);
						continue;
					}
					
					long start = players.get(player);
					//if (expired)
					if((start - System.currentTimeMillis() / 1000 +
							StringUtils.timeFromString( Loader.config.getString("options.afk.times.autoKick")) ) <= 0) {
						for(String cmd : Loader.config.getStringList("options.afk.actions.onKick"))
							Sudo.sudoConsole(SudoType.COMMAND,
									PlaceholderAPISupport.replace(cmd, Bukkit.getPlayer(player), false));
					}
				}
				for(String player : last_interaction.keySet()) {
					if(Bukkit.getPlayer(player)==null) { // if offline
						last_interaction.remove(player);
						continue;
					}
					Player p = Bukkit.getPlayer(player);
					if(players.containsKey(player))
						continue;
					long last = last_interaction.get(player);
					if((last - System.currentTimeMillis() / 1000 +
							StringUtils.timeFromString( Loader.config.getString("options.afk.times.autoAFK")) ) <= 0) {
						//cmds
						for(String cmd : Loader.config.getStringList("options.afk.actions.onStart"))
							Sudo.sudoConsole(SudoType.COMMAND,
									PlaceholderAPISupport.replace(cmd, Bukkit.getPlayer(player), false));
						//AFK on
						players.put(p.getName(), System.currentTimeMillis()/1000);
						MessageUtils.message(p, configSection()+".start", Placeholders.c().addPlayer("player", p)
								, false, API.getOnlinePlayersThatcanSee(p, true).toArray(new CommandSender[0]));
						last_interaction.remove(player); // removing from interactions
					}
				}
				
			}
		}.runRepeating(20, 20);
	}
}
