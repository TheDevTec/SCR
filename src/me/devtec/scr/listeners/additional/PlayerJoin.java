package me.devtec.scr.listeners.additional;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.fun.Fly;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class PlayerJoin implements Listener {
	Config joinconfig;

	public PlayerJoin(Config config) {
		joinconfig = config;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void login(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (Loader.data.getBoolean("maintenance") && !p.hasPermission(Loader.commands.getString("maintenance.permission.bypass"))) {
			StringBuilder kick = new StringBuilder();
			for (String s : Loader.translations.getStringList("maintenance.kickMessages"))
				if (s.endsWith("\n"))
					kick.append(s);
				else
					kick.append(s).append("\n");
			e.disallow(Result.KICK_WHITELIST, StringUtils.colorize(PlaceholderAPISupport.replace(kick.toString(), p)));
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage("a \nb \nc \nd ");
		event.setJoinMessage(null);
		if (joinconfig.getBoolean("enabled"))
			if (!event.getPlayer().hasPlayedBefore()) {
				for (String command : joinconfig.getStringList("firstTime.commands"))
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", event.getPlayer().getName()));
				new Tasker() {
					@Override
					public void run() {
						MessageUtils.msgConfig(event.getPlayer(), "firstTime.broadcast", joinconfig, Placeholders.c().addPlayer("player", event.getPlayer()),
								BukkitLoader.getOnlinePlayers().toArray(new CommandSender[0]));
						MessageUtils.msgConfig(event.getPlayer(), "firstTime.messages", joinconfig, Placeholders.c().addPlayer("player", event.getPlayer()), event.getPlayer());
					}
				}.runTask();
			} else {
				for (String command : joinconfig.getStringList("commands"))
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", event.getPlayer().getName()));
				new Tasker() {
					@Override
					public void run() {
						MessageUtils.msgConfig(event.getPlayer(), "broadcast", joinconfig, Placeholders.c().addPlayer("player", event.getPlayer()),
								BukkitLoader.getOnlinePlayers().toArray(new Player[0]));
						MessageUtils.msgConfig(event.getPlayer(), "messages", joinconfig, Placeholders.c().addPlayer("player", event.getPlayer()), event.getPlayer());
					}
				}.runTask();
			}
		Player player = event.getPlayer();
		User user = API.getUser(player);
		user.joinTime();
		// On join with turned fly on
		if (user.haveFly()) {
			Fly.apply(player, false); // false - turning on
			MessageUtils.message(player, "fly.stillenabled", null);
		}
		if (user.haveGod())
			MessageUtils.message(player, "god.stillenabled", null);
	}
}
