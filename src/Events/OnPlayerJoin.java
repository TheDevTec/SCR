
package Events;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mojang.authlib.yggdrasil.response.User;

import Commands.Message.Mail;
import Commands.Other.KitCmd;
import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import ServerControl.SPlayer;
import Utils.Tasks;
import Utils.setting;
import me.DevTec.TheAPI.APIs.SoundAPI;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.PlaceholderAPI.PlaceholderAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.TheAPI.SudoType;

public class OnPlayerJoin implements Listener {

	public static String replaceAll(String s, Player p) {
		String name = p.getDisplayName();
		return PlaceholderAPI.setPlaceholders(p, s.replace("%players_max%", TheAPI.getMaxPlayers() + "")
				.replace("%online%", TheAPI.getOnlinePlayers().size() - 1 + "").replace("%player%", name)
				.replace("%playername%", name)
				.replace("%customname%", p.getCustomName() != null ? p.getCustomName() : name)
				.replace("%prefix%", setting.prefix).replace("%time%", setting.format_time.format(new Date()))
				.replace("%date%", setting.format_date.format(new Date()))
				.replace("%date-time%", setting.format_date_time.format(new Date()))
				.replace("%version%", "V" + Loader.getInstance.getDescription().getVersion())
				.replace("%server_time%", setting.format_time.format(new Date()))
				.replace("%server_name%", API.getServerName())
				.replace("%server_ip%", p.getServer().getIp() + ":" + p.getServer().getPort()));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void playerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Loader.setupChatFormat(p);
		Tasks.regPlayer(p);
		User d = TheAPI.getUser(p);
		if (setting.join_msg) {
			if (TheAPI.isVanished(p))
				e.setJoinMessage("");
			else
				e.setJoinMessage(TheAPI.colorize(replaceAll(Loader.getTranslation("Join.Text").toString(), p)));
		}
		Config f = Loader.config;
		if (!Mail.getMails(p.getName()).isEmpty())
			Loader.sendMessages(p,"Mail.Notification", Placeholder.c().add("%amount%", "" + d.getStringList("Mails").size()));
		if (setting.sound)
			SoundAPI.playSound(p, f.getString("Options.Sounds.Sound"));
		if (PunishmentAPI.getBanList(p.getName()).isJailed()
				|| PunishmentAPI.getBanList(p.getName()).isTempJailed()) {
			if (setting.tp_safe)
				API.safeTeleport(p,StringUtils.getLocationFromString(f.getString("Jails." + d.getString("Jail.Location"))));
			else
				p.teleport(StringUtils.getLocationFromString(f.getString("Jails." + d.getString("Jail.Location"))));
		} else if (f.getBoolean("OnJoin.SpawnTeleport"))
			API.teleportPlayer(p, TeleportLocation.SPAWN);

		d.set("JoinTime", System.currentTimeMillis() / 1000);
		if (!d.exist("FirstJoin"))
			d.set("FirstJoin", setting.format_date_time.format(new Date()));

		if (!p.hasPlayedBefore() && setting.join_first) {
			for (String ss : Loader.trans.getStringList("OnJoin.FirstJoin.Messages")) {
				TheAPI.msg(replaceAll(ss, p), p);
			}
			if (!TheAPI.isVanished(p))
				Loader.sendBroadcasts(p,"Join.FirstJoin.Text");
			new Tasker() {
				@Override
				public void run() {
					if (setting.join_first_percmd)
						for (String cmds : f.getStringList("Options.Join.FirstJoin.PerformCommands.List"))
							TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(replaceAll(cmds, p)));
					if (setting.join_first_give && f.getString("Options.Join.FirstJoin.Kit") != null)
						KitCmd.giveKit(p, Loader.getKit(f.getString("Options.Join.FirstJoin.Kit")), false, false, false);
				}
			}.runLater(f.getInt("Options.Join.FirstJoin.Wait") > 0 ? 20 * f.getInt("Options.Join.FirstJoin.Wait") : 1);
			API.teleportPlayer(p, TeleportLocation.SPAWN);
		} else {
			if (setting.join_motd) {
				for (String ss : Loader.trans.getStringList("OnJoin.Messages")) {
					TheAPI.msg(replaceAll(ss, p), p);
				}
			}
		}
		if (!EconomyAPI.hasAccount(p))
			EconomyAPI.createAccount(p);
		SPlayer s = API.getSPlayer(p);
		if (s.hasPermission("ServerControl.FlySpeedOnJoin"))
			s.setFlySpeed();
		if (s.hasPermission("ServerControl.WalkSpeedOnJoin"))
			s.setWalkSpeed();
		if (s.hasTempFlyEnabled())
			s.enableTempFly();
		else {
			if (s.hasFlyEnabled() && s.hasPermission("servercontrol.flyonjoin") && s.hasPermission("servercontrol.fly"))
				s.enableFly();
		}
		if (s.hasGodEnabled() && s.hasPermission("servercontrol.godonjoin") && s.hasPermission("servercontrol.god"))
			s.enableGod();
		
		int j = d.getInt("Joins");
		j=j+1;
		d.setAndSave("Joins", j);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void playerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (setting.leave) {
			if (TheAPI.isVanished(p))
				e.setQuitMessage(null);
			else
				e.setQuitMessage(TheAPI.colorize(replaceAll(Loader.getTranslation("Quit").toString(), p)));
		}
		User d = TheAPI.getUser(p);
		d.set("LastLeave", setting.format_date_time.format(new Date()));
		d.set("DisconnectWorld", p.getWorld().getName());
		p.setFlying(false);
		p.setAllowFlight(false);
	}
}