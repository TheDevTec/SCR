package me.devtec.scr.listeners.additional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.fun.Fly;
import me.devtec.scr.commands.fun.God;
import me.devtec.scr.commands.server_managment.Vanish;
import me.devtec.scr.commands.server_managment.Vanish.VanishStatus;
import me.devtec.scr.commands.teleport.spawn.Spawn;
import me.devtec.scr.commands.tpsystem.TpSystem;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.ColorUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class PlayerJoin implements Listener {
	public static Config config;

	public PlayerJoin(Config config) {
		PlayerJoin.config = config;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void login(PlayerLoginEvent e) {
		if (e.getResult() == Result.ALLOWED) {
			Player p = e.getPlayer();
			if (Loader.data.getBoolean("maintenance") && !p.hasPermission(Loader.commands.getString("maintenance.permission.bypass"))) {
				StringBuilder kick = new StringBuilder();
				for (String s : Loader.translations.getStringList("maintenance.kickMessages"))
					if (s.endsWith("\n"))
						kick.append(s);
					else
						kick.append(s).append("\n");
				e.disallow(Result.KICK_WHITELIST, ColorUtils.colorize(PlaceholderAPISupport.replace(kick.toString(), p)));
				return;
			}
			API.getUser(p); // Load user async
		}
	}

	/**
	 * Teleport to the spawn on first join or respawinng
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		TpSystem.setBack(e.getEntity());
	}

	/**
	 * Teleport to the spawn on first join or respawinng
	 */
	@EventHandler
	public void onSpawnLocation(PlayerSpawnLocationEvent e) {
		Player p = e.getPlayer();
		if (Spawn.spawn != null && !p.hasPlayedBefore())
			e.setSpawnLocation(Spawn.spawn.toLocation());
	}

	/**
	 * Teleport to the spawn on first join or respawinng
	 */
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (Spawn.spawn != null && (p.getBedSpawnLocation() == null || !compile(p.getBedSpawnLocation(), e.getRespawnLocation())))
			e.setRespawnLocation(Spawn.spawn.toLocation());
	}

	private boolean compile(Location bedSpawnLocation, Location respawnLocation) {
		return bedSpawnLocation.getWorld().getUID().equals(respawnLocation.getWorld().getUID()) && bedSpawnLocation.getX() == respawnLocation.getX()
				&& bedSpawnLocation.getY() == respawnLocation.getY() && bedSpawnLocation.getZ() == respawnLocation.getZ();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();
		User user = API.getUser(player);
		user.notifyJoin(player, true);
		boolean vanish = user.isVanished();

		if (Vanish.ENABLED)
			if (vanish) { // Apply vanish
				MessageUtils.message(player, "vanish.stillenabled", null);
				if (Ref.getClass("org.spigotmc.SpigotConfig") != null) {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF(player.getName());
					out.writeInt(VanishStatus.ENABLED_ON_JOIN.getId());
					player.sendPluginMessage(Loader.plugin, "scr:vanish", out.toByteArray());
				}
				for (Player target : BukkitLoader.getOnlinePlayers())
					if (!target.hasPermission(Loader.commands.getString("vanish.permission.cmd")))
						target.hidePlayer(player);
			} else if (Ref.getClass("org.spigotmc.SpigotConfig") != null) {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF(player.getName());
				out.writeInt(VanishStatus.NOT_SET.getId());
				player.sendPluginMessage(Loader.plugin, "scr:vanish", out.toByteArray());
			}

		if (config.getBoolean("enabled"))
			if (!player.hasPlayedBefore()) {
				for (String command : config.getStringList("firstTime.commands"))
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
				new Tasker() {
					@Override
					public void run() {
						if (!vanish)
							MessageUtils.msgConfig(player, "firstTime.broadcast", config, Placeholders.c().addPlayer("player", player), BukkitLoader.getOnlinePlayers().toArray(new CommandSender[0]));
						MessageUtils.msgConfig(player, "firstTime.messages", config, Placeholders.c().addPlayer("player", player), player);
					}
				}.runTask();
			} else {
				for (String command : config.getStringList("commands"))
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
				new Tasker() {
					@Override
					public void run() {
						if (!vanish)
							MessageUtils.msgConfig(player, "broadcast", config, Placeholders.c().addPlayer("player", player), BukkitLoader.getOnlinePlayers().toArray(new Player[0]));
						MessageUtils.msgConfig(player, "messages", config, Placeholders.c().addPlayer("player", player), player);
					}
				}.runTask();
			}
		// On join with turned fly on
		if (Fly.isEnabled() && user.fly()) {
			Fly.apply(player, false); // false - turning on
			MessageUtils.message(player, "fly.stillenabled", null);
		}
		if (God.isEnabled() && user.god())
			MessageUtils.message(player, "god.stillenabled", null);
	}
}
