
package me.DevTec.ServerControlReloaded.Events;

import java.util.Collection;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.DevTec.ServerControlReloaded.Commands.Message.Mail;
import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.API.TeleportLocation;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.ServerControlReloaded.Utils.Tasks;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.SoundAPI;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.PlaceholderAPI.PlaceholderAPI;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class OnPlayerJoin implements Listener {

	public static String replaceAll(String s, Player p) {
		String name = p.getDisplayName();
		return PlaceholderAPI.setPlaceholders(p, s.replace("%player%", p.getName())
				.replace("%playername%", name)
				.replace("%customname%", p.getCustomName() != null ? p.getCustomName() : name)
				.replace("%prefix%", setting.prefix).replace("%time%", setting.format_time.format(new Date()))
				.replace("%date%", setting.format_date.format(new Date()))
				.replace("%date-time%", setting.format_date_time.format(new Date())));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Loader.setupChatFormat(p);
		Tasks.regPlayer(p);
		User d = TheAPI.getUser(p);
		e.setJoinMessage(null);
		Config f = Loader.config;
		if (!Mail.getMails(p.getName()).isEmpty())
			Loader.sendMessages(p,"Mail.Notification", Placeholder.c().add("%amount%", "" + d.getStringList("Mails").size()));
		if (setting.sound)
			SoundAPI.playSound(p, f.getString("Options.Sounds.Sound"));
		d.set("JoinTime", System.currentTimeMillis() / 1000);
		if (!d.exist("FirstJoin"))
			d.set("FirstJoin", setting.format_date_time.format(new Date()));
		if (!p.hasPlayedBefore()) {
			if (!TheAPI.hasVanish(p.getName()))
				for(Player dd : TheAPI.getOnlinePlayers()) {
					Object o = Loader.events.get("onJoin.FirstJoin.Text");
					if(o!=null) {
					if(o instanceof Collection) {
					for(String fa : Loader.events.getStringList("onJoin.FirstJoin.Text")) {
						TheAPI.msg(replaceAll(fa,dd), dd);
					}}else
						TheAPI.msg(replaceAll(""+o, dd), dd);
				}}
			Object o = Loader.events.get("onJoin.FirstJoin.Messages");
			if(o!=null) {
			if(o instanceof Collection) {
				for(String fa : Loader.events.getStringList("onJoin.FirstJoin.Messages")) {
					TheAPI.msg(replaceAll(fa,p), p);
				}}else
			TheAPI.msg(replaceAll(""+o, p), p);
			}
			o = Loader.events.get("onJoin.FirstJoin.Commands");
			if(o!=null) {
			if(o instanceof Collection) {
				for(String fa : Loader.events.getStringList("onJoin.FirstJoin.Commands")) {
					TheAPI.sudoConsole(TheAPI.colorize(replaceAll(fa,p)));
				}}else
			TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
			}
			o = Loader.events.get("onJoin.FirstJoin.Broadcast");
			if(o!=null) {
			if(o instanceof Collection) {
				for(String fa : Loader.events.getStringList("onJoin.FirstJoin.Broadcast")) {
					TheAPI.bcMsg(replaceAll(fa,p));
				}}else
			TheAPI.bcMsg(replaceAll(""+o, p));
			}
			API.teleportPlayer(p, TeleportLocation.SPAWN);
		} else {
			if (!TheAPI.hasVanish(p.getName()))
				for(Player dd : TheAPI.getOnlinePlayers()) {
					Object o = Loader.events.get("onJoin.Text");
					if(o!=null) {
					if(o instanceof Collection) {
					for(String fa : Loader.events.getStringList("onJoin.Text")) {
						TheAPI.msg(replaceAll(fa,dd), dd);
					}}else
						TheAPI.msg(replaceAll(""+o, dd), dd);
				}}
			Object o = Loader.events.get("onJoin.Messages");
			if(o!=null) {
			if(o instanceof Collection) {
				for(String fa : Loader.events.getStringList("onJoin.Messages")) {
					TheAPI.msg(replaceAll(fa,p), p);
				}}else
			TheAPI.msg(replaceAll(""+o, p), p);
			}
			o = Loader.events.get("onJoin.Commands");
			if(o!=null) {
			if(o instanceof Collection) {
				for(String fa : Loader.events.getStringList("onJoin.Commands")) {
					TheAPI.sudoConsole(TheAPI.colorize(replaceAll(fa,p)));
				}}else
			TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
			}
			
			o = Loader.events.get("onJoin.Broadcast");
			if(o!=null) {
			if(o instanceof Collection) {
				for(String fa : Loader.events.getStringList("onJoin.Broadcast")) {
					TheAPI.bcMsg(replaceAll(fa,p));
				}}else
			TheAPI.bcMsg(replaceAll(""+o, p));
			}
		}
		if (!EconomyAPI.hasAccount(p))
			EconomyAPI.createAccount(p);
		SPlayer s = API.getSPlayer(p);
			s.setFlySpeed();
			s.setWalkSpeed();
		if (s.hasTempFlyEnabled())
			s.enableTempFly();
		else {
			if (s.hasFlyEnabled() && Loader.has(p, "Fly", "Other"))
				s.enableFly();
		}
		if (s.hasGodEnabled() && Loader.has(p, "God", "Other"))
			s.enableGod();
		d.setAndSave("Joins", d.getInt("Joins")+1);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void playerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		e.setQuitMessage(null);
		if (!TheAPI.hasVanish(p.getName()))
			for(Player dd : TheAPI.getOnlinePlayers()) {
				Object o = Loader.events.get("onQuit.Text");
				if(o!=null) {
				if(o instanceof Collection) {
				for(String fa : Loader.events.getStringList("onQuit.Text")) {
					TheAPI.msg(replaceAll(fa,dd), dd);
				}}else
					TheAPI.msg(replaceAll(""+o, dd), dd);
			}}
		Object o = Loader.events.get("onQuit.Messages");
		if(o!=null) {
		if(o instanceof Collection) {
			for(String fa : Loader.events.getStringList("onQuit.Messages")) {
				TheAPI.msg(replaceAll(fa,p), p);
			}}else
		TheAPI.msg(replaceAll(""+o, p), p);
		}
		o = Loader.events.get("onQuit.Commands");
		if(o!=null) {
		if(o instanceof Collection) {
			for(String fa : Loader.events.getStringList("onQuit.Commands")) {
				TheAPI.sudoConsole(TheAPI.colorize(replaceAll(fa,p)));
			}}else
		TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
		}
		o = Loader.events.get("onQuit.Broadcast");
		if(o!=null) {
		if(o instanceof Collection) {
			for(String fa : Loader.events.getStringList("onQuit.Broadcast")) {
				TheAPI.bcMsg(replaceAll(fa,p));
			}}else
		TheAPI.bcMsg(replaceAll(""+o, p));}
		User d = TheAPI.getUser(p);
		d.set("LastLeave", setting.format_date_time.format(new Date()));
		d.set("DisconnectWorld", p.getWorld().getName());
		p.setFlying(false);
		p.setAllowFlight(false);
	}
}