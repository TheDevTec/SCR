
package me.devtec.servercontrolreloaded.events;

import java.util.Collection;
import java.util.Date;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.devtec.servercontrolreloaded.commands.message.Mail;
import me.devtec.servercontrolreloaded.commands.other.Vanish;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.API.TeleportLocation;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.DisplayManager;
import me.devtec.servercontrolreloaded.utils.NameTagChanger;
import me.devtec.servercontrolreloaded.utils.SPlayer;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.Tasks;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.servercontrolreloaded.utils.skins.manager.SkinCallback;
import me.devtec.servercontrolreloaded.utils.skins.manager.SkinData;
import me.devtec.servercontrolreloaded.utils.skins.manager.SkinManager;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.SoundAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.punishmentapi.PlayerBanList;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.reflections.Ref;

public class OnPlayerJoin implements Listener {

	public static String replaceAll(String s, Player p) {
		String name = p.getDisplayName()==null?p.getName():p.getDisplayName();
		return PlaceholderAPI.setPlaceholders(p, s.replace("%player%", p.getName())
				.replace("%playername%", name)
				.replace("%customname%", p.getCustomName() != null ? p.getCustomName() : name)
				.replace("%prefix%", setting.prefix+"")
				.replace("%time%", setting.format_time.format(new Date()))
				.replace("%date%", setting.format_date.format(new Date()))
				.replace("%date-time%", setting.format_date_time.format(new Date())));
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if(Loader.config.getBoolean("Options.Skins.onJoin")) {
			new Tasker() {
				public void run() {
					if(Loader.config.getBoolean("Options.Skins.Custom.setOwnToAll.set")) {
						String skin = Loader.config.getString("Options.Skins.Custom.setOwnToAll.value");
						SkinManager.generateSkin(skin.replace("%player%", p.getName()), new SkinCallback() {
							@Override
							public void run(SkinData data) {
								if(!p.isOnline())return;
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
								SkinManager.loadSkin(p, data);
							}
						}, false);
					}
				}
			}.runTask();
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerJoin(PlayerJoinEvent e) {
		e.setJoinMessage("");
		Player p = e.getPlayer();
		User d = TheAPI.getUser(p);
        if(d.exists("vanish"))
        API.setVanish(d, Loader.getPerm("Vanish","Other"), d.getBoolean("vanish"));
        for (Player s : TheAPI.getOnlinePlayers())
            if(p!=s)
            if (!API.canSee(p, s.getName()))
                p.hidePlayer(s);
		new Tasker() {
			public void run() {
				try {
				boolean fly = d.getBoolean("FlyOnQuit");
				if(fly)
				d.remove("FlyOnQuit");
				SPlayer s = API.getSPlayer(p); 
				s.setFlySpeed();
				s.setWalkSpeed();
				if (s.hasTempFlyEnabled())
					s.enableTempFly();
				else if ((s.hasFlyEnabled()||fly) && Loader.has(p, "Fly", "Other")) {
					p.setAllowFlight(true);
					p.setFlying(true);
				}
				if (s.hasGodEnabled()){
					s.setHP();
					s.setFood();
					s.setFire();
				}
		        if(API.hasVanish(p) || TheAPI.isNewerThan(7) && p.getGameMode()==GameMode.SPECTATOR)
		    		LoginEvent.moveInTab(p, API.hasVanish(p)?0:1, API.hasVanish(p));
		    	DisplayManager.initializePlayer(p);
				Tasks.regPlayer(p);
				Config f = Loader.config;
				if(API.hasVanish(p.getName())) {
					if(setting.vanish_action) {
						Vanish.task.put(p.getName(), new Tasker() {
							@Override
							public void run() {
								if(!API.hasVanish(p.getName()) || !p.isOnline()) {
									cancel();
									return;
								}
								TheAPI.sendActionBar(p, Loader.getTranslation("Vanish.Active").toString());
							}
						}.runRepeating(0, 20));
					}
					Loader.sendMessages(p, "Vanish.Join");
				}
				if (!Mail.getMails(p.getName()).isEmpty())
					Loader.sendMessages(p,"Mail.Notification", Placeholder.c().add("%amount%", "" + d.getStringList("Mails").size()));
				if (setting.sound)
					SoundAPI.playSound(p, f.getString("Options.Sounds.Sound"));
				d.set("JoinTime", System.currentTimeMillis() / 1000);
				if (!d.exist("FirstJoin"))
					d.set("FirstJoin", setting.format_date_time.format(new Date()));
				if (!p.hasPlayedBefore()) {
					if (!API.hasVanish(p.getName())) {
						Object o = Loader.events.get("onJoin.First.Text");
						if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.bcMsg(replaceAll(fa+"",p));
						}}else
							if(!(""+o).isEmpty())
								TheAPI.bcMsg(replaceAll(""+o, p));
						}
					}
					Object o = Loader.events.get("onJoin.First.Messages");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.msg(replaceAll(fa+"",p),p);
						}}else
							if(!(""+o).isEmpty())
								TheAPI.msg(replaceAll(""+o, p),p);
					}
					PlayerBanList fac = PunishmentAPI.getBanList(p.getName());
					new Tasker() {
						public void run() {
							if(!fac.isJailed() && !fac.isIPJailed() && !fac.isTempJailed() && !fac.isTempIPJailed())
							API.teleportPlayer(p, TeleportLocation.SPAWN);
							Object o = Loader.events.get("onJoin.First.Commands");
							if(o!=null) {
							if(o instanceof Collection) {
								for(String fa : Loader.events.getStringList("onJoin.First.Commands")) {
									if(fa!=null)
									TheAPI.sudoConsole(TheAPI.colorize(replaceAll(fa,p)));
								}}else
							TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
							}
						}
					}.runTaskSync();
					o = Loader.events.get("onJoin.First.Broadcast");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.bcMsg(replaceAll(fa+"",p));
						}}else
							if(!(""+o).isEmpty())
								TheAPI.bcMsg(replaceAll(""+o, p));
					}
				} else {
					if (!API.hasVanish(p.getName())) {
						Object o = Loader.events.get("onJoin.Text");
						if(o!=null) {
							if(o instanceof Collection) {
							for(Object fa : (Collection<?>)o) {
								if(fa!=null)
								TheAPI.bcMsg(replaceAll(fa+"",p));
							}}else
								if(!(""+o).isEmpty())
									TheAPI.bcMsg(replaceAll(""+o, p));
						}}
					Object o = Loader.events.get("onJoin.Messages");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.msg(replaceAll(fa+"",p),p);
						}}else
							if(!(""+o).isEmpty())
								TheAPI.msg(replaceAll(""+o, p),p);
					}

					new Tasker() {
						public void run() {
					Object o = Loader.events.get("onJoin.Commands");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
								TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+fa, p)));
						}}else
							if(!(""+o).isEmpty())
								TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
					}}
					}.runTaskSync();
					o = Loader.events.get("onJoin.Broadcast");
					if(o!=null) {
						if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.bcMsg(replaceAll(fa+"",p));
						}}else
							if(!(""+o).isEmpty())
								TheAPI.bcMsg(replaceAll(""+o, p));
					}
				}
				if (!EconomyAPI.hasAccount(p))
					EconomyAPI.createAccount(p);
				if(setting.tab) {
					if(setting.tab_footer || setting.tab_header)
				TabList.setFooterHeader(p);
				if(setting.tab_nametag)
				TabList.setNameTag(p);
				TabList.update();
				}
				d.set("Joins", d.getInt("Joins")+1);
				}catch(Exception | NoSuchFieldError | NoSuchMethodError e) {}
				d.save();
			}}.runTask();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Player p = e.getPlayer();
		DisplayManager.removeCache(p);
		NameTagChanger.remove(p);
		Ref.sendPacket(p,TabList.empty);
		p.setPlayerListName(p.getName());
		p.setDisplayName(null);
		p.setCustomName(null);
		User d = TheAPI.getUser(p);
		boolean fly = p.isFlying() && p.getAllowFlight();
		p.setFlying(false);
		p.setAllowFlight(false);
		try {
		Vanish.task.remove(p.getName());
		if (!API.hasVanish(p.getName())) {
				Object o = Loader.events.get("onQuit.Text");
				if(o!=null) {
					if(o instanceof Collection) {
						for(Object fa : (Collection<?>)o) {
							if(fa!=null)
							TheAPI.bcMsg(replaceAll(fa+"",p));
						}}else
							if(!(""+o).trim().isEmpty())
								TheAPI.bcMsg(replaceAll(""+o, p));
			}}
		Object o = Loader.events.get("onQuit.Messages");
		if(o!=null) {
			if(o instanceof Collection) {
			for(Object fa : (Collection<?>)o) {
				if(fa!=null)
				TheAPI.msg(replaceAll(fa+"",p),p);
			}}else
				if(!(""+o).trim().isEmpty())
					TheAPI.msg(replaceAll(""+o, p),p);
		}
		o = Loader.events.get("onQuit.Commands");
		if(o!=null) {
			if(o instanceof Collection) {
			for(Object fa : (Collection<?>)o) {
				if(fa!=null)
					TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
			}}else
				if(!(""+o).trim().isEmpty())
					TheAPI.sudoConsole(TheAPI.colorize(replaceAll(""+o, p)));
		}
		o = Loader.events.get("onQuit.Broadcast");
		if(o!=null) {
			if(o instanceof Collection) {
				for(Object fa : (Collection<?>)o) {
					if(fa!=null)
					TheAPI.bcMsg(replaceAll(fa+"",p));
				}}else
					if(!(""+o).trim().isEmpty())
						TheAPI.bcMsg(replaceAll(""+o, p));
		}
		d.set("LastLeave", setting.format_date_time.format(new Date()));
		if(fly)
			d.set("FlyOnQuit", true);
		else
			d.remove("FlyOnQuit");
		d.set("DisconnectWorld", p.getWorld().getName());
		}catch(Exception | NoSuchFieldError | NoSuchMethodError err) {}
		new Tasker() {
			public void run() {
				d.save();
			}
		}.runTask();
		API.removeSPlayer(p);
	}
}