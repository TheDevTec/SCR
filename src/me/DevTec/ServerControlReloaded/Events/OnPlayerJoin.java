
package me.DevTec.ServerControlReloaded.Events;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.DevTec.ServerControlReloaded.Commands.Message.Mail;
import me.DevTec.ServerControlReloaded.Commands.Other.Vanish;
import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.API.TeleportLocation;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.DisplayManager;
import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.DevTec.ServerControlReloaded.Utils.Tasks;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.DevTec.ServerControlReloaded.Utils.Skins.Manager.SkinCallback;
import me.DevTec.ServerControlReloaded.Utils.Skins.Manager.SkinData;
import me.DevTec.ServerControlReloaded.Utils.Skins.Manager.SkinManager;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.SoundAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.datakeeper.User;

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
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if(Loader.config.getBoolean("Options.Skins.onJoin")) {
			if(Loader.config.getBoolean("Options.Skins.Custom.setOwnToAll.set")) {
				String skin = Loader.config.getString("Options.Skins.Custom.setOwnToAll.value");
				SkinManager.generateSkin(skin.replace("%player%", p.getName()), new SkinCallback() {
					@Override
					public void run(SkinData data) {
						if(!p.isOnline())return;
						SkinManager.setSkin(p.getName(), data);
						SkinManager.loadSkin(p, data);
					}
				}, false);
			}else {
				String skin = TheAPI.getUser(p).getString("skin");
				if(skin==null)skin=Loader.config.getString("Options.Skins.Custom.default"); //non null
				SkinManager.generateSkin(skin.replace("%player%", p.getName()), new SkinCallback() {
					@Override
					public void run(SkinData data) {
						if(!p.isOnline())return;
						SkinManager.setSkin(p.getName(), data);
						SkinManager.loadSkin(p, data);
					}
				}, false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");
		Player p = e.getPlayer();
		DisplayManager.initializePlayer(p);
		new Tasker() {
			public void run() {
				LoginEvent.moveInTab(p);
				Loader.setupChatFormat(p);
				Tasks.regPlayer(p);
				User d = TheAPI.getUser(p);
				Config f = Loader.config;
				if(TheAPI.hasVanish(e.getPlayer().getName())) {
					if(setting.vanish_action) {
						Vanish.task.put(e.getPlayer().getName(), new Tasker() {
							@Override
							public void run() {
								if(!TheAPI.hasVanish(e.getPlayer().getName()) || !e.getPlayer().isOnline()) {
									cancel();
									return;
								}
								TheAPI.sendActionBar(e.getPlayer(), Loader.getTranslation("Vanish.Active").toString());
							}
						}.runRepeating(0, 20));
					}
					Loader.sendMessages(p, "Vanish.Join");
					return;
				}
				if (!Mail.getMails(p.getName()).isEmpty())
					Loader.sendMessages(p,"Mail.Notification", Placeholder.c().add("%amount%", "" + d.getStringList("Mails").size()));
				if (setting.sound)
					SoundAPI.playSound(p, f.getString("Options.Sounds.Sound"));
				d.set("JoinTime", System.currentTimeMillis() / 1000);
				if (!d.exist("FirstJoin"))
					d.set("FirstJoin", setting.format_date_time.format(new Date()));
				if (!p.hasPlayedBefore()) {
					if (!TheAPI.hasVanish(p.getName())) {
							Object o = Loader.events.get("onJoin.First.Text");
							if(o!=null) {
							if(o instanceof Collection) {
							for(String fa : Loader.events.getStringList("onJoin.First.Text")) {
								if(fa!=null)
								TheAPI.bcMsg(replaceAll(fa,p));
							}}else
								TheAPI.bcMsg(replaceAll(""+o, p));
						}
					}
					Object o = Loader.events.get("onJoin.First.Messages");
					if(o!=null) {
					if(o instanceof Collection) {
						for(String fa : Loader.events.getStringList("onJoin.First.Messages")) {
							if(fa!=null)
							TheAPI.msg(replaceAll(fa,p), p);
						}}else
					TheAPI.msg(replaceAll(""+o, p), p);
					}
					new Tasker() {
						public void run() {
							API.teleportPlayer(p, TeleportLocation.SPAWN);
					Object o = Loader.events.get("onJoin.First.Commands");
					if(o!=null) {
					if(o instanceof Collection) {
						for(String fa : Loader.events.getStringList("onJoin.First.Commands")) {
							if(fa!=null)
							TheAPI.sudoConsole(TheAPI.colorize(replaceAll(fa,p)));
						}}else
					TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
					}}
					}.runTaskSync();
					o = Loader.events.get("onJoin.First.Broadcast");
					if(o!=null) {
					if(o instanceof Collection) {
						for(String fa : Loader.events.getStringList("onJoin.First.Broadcast")) {
							if(fa!=null) {
								String replace = replaceAll(fa,p);
								if(replace!=null)
							TheAPI.bcMsg(replace);
							}
						}}else {
							String replace = replaceAll(""+o, p);
							if(replace!=null)
								TheAPI.bcMsg(replace);
						}
					}
				} else {
					if (!TheAPI.hasVanish(p.getName())) {
							Object o = Loader.events.get("onJoin.Text");
							if(o!=null) {
							if(o instanceof Collection) {
							for(String fa : Loader.events.getStringList("onJoin.Text")) {
								if(fa!=null)
								TheAPI.bcMsg(replaceAll(fa,p));
							}}else
								TheAPI.bcMsg(replaceAll(""+o, p));
						}}
					Object o = Loader.events.get("onJoin.Messages");
					if(o!=null) {
					if(o instanceof Collection) {
						for(String fa : Loader.events.getStringList("onJoin.Messages")) {
							TheAPI.msg(replaceAll(fa,p), p);
						}}else
					TheAPI.msg(replaceAll(""+o, p), p);
					}

					new Tasker() {
						public void run() {
					Object o = Loader.events.get("onJoin.Commands");
					if(o!=null) {
					if(o instanceof Collection) {
						for(String fa : Loader.events.getStringList("onJoin.Commands")) {
							TheAPI.sudoConsole(TheAPI.colorize(replaceAll(fa,p)));
						}}else
					TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
					}}
					}.runTaskSync();
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
				if(setting.tab) {
					if(setting.tab_footer || setting.tab_header)
				TabList.setFooterHeader(p);
				if(setting.tab_nametag)
				TabList.setName(p);
				}
				SPlayer s = API.getSPlayer(p); 
				new Tasker() {
					
					@Override
					public void run() {
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
					}
				}.runTaskSync();
				d.setAndSave("Joins", d.getInt("Joins")+1);
			}}.runTask();
	}

	Method cdd = null;
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Player p = e.getPlayer();
		DisplayManager.removeCache(p);
		p.setScoreboard(p.getServer().getScoreboardManager().getNewScoreboard());
		new Tasker() {
			public void run() {
				Vanish.task.remove(e.getPlayer().getName());
				if (!TheAPI.hasVanish(p.getName())) {
						Object o = Loader.events.get("onQuit.Text");
						if(o!=null) {
						if(o instanceof Collection) {
						for(String fa : Loader.events.getStringList("onQuit.Text")) {
							TheAPI.bcMsg(replaceAll(fa,p));
						}}else
							TheAPI.bcMsg(replaceAll(""+o, p));
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
				d.save();
			}
		}.runTask();
		p.setFlying(false);
		p.setAllowFlight(false);
	}
}