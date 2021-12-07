package me.devtec.servercontrolreloaded.commands.other;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.reflections.Ref;

public class Vanish implements CommandExecutor, TabCompleter{
	private static final Constructor<?> onc = Ref.constructor(Ref.nmsOrOld("network.protocol.game.PacketPlayOutPlayerInfo","PacketPlayOutPlayerInfo"), 
			Ref.nmsOrOld("network.protocol.game.PacketPlayOutPlayerInfo$EnumPlayerInfoAction","PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), Array.newInstance(Ref.nmsOrOld("server.level.EntityPlayer","EntityPlayer"), 1).getClass());
	private static final Class<?> cc = Ref.nmsOrOld("world.level.EnumGamemode","EnumGamemode")!=null?Ref.nmsOrOld("world.level.EnumGamemode","EnumGamemode"):Ref.nms("WorldSettings$EnumGamemode");
	private static final Object surv = Ref.getNulled(cc, TheAPI.isNewerThan(16)?"a":"SURVIVAL");
	private static final Object spec = Ref.getNulled(cc, TheAPI.isNewerThan(16)?"d":"SPECTATOR");
	private static final Object up = Ref.getNulled(Ref.field(Ref.nmsOrOld("network.protocol.game.PacketPlayOutPlayerInfo$EnumPlayerInfoAction","PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), TheAPI.isNewerThan(17)?"b":"UPDATE_GAME_MODE"));

	public static void moveInTab(Player player, int game, boolean vanish) {
		if(!TheAPI.isNewerThan(7)||spec==null)return;
		Object array = Array.newInstance(Ref.nmsOrOld("server.level.EntityPlayer","EntityPlayer"), 1);
		Array.set(array, 0, Ref.player(player));
		Object b = Ref.newInstance(onc, up, array);
		@SuppressWarnings("unchecked")
		List<Object> bList = (List<Object>) Ref.get(b, "b");
		int c = 0;
		for(Object o : bList) { //edit values
			int gmResult = spec!=null?(player.getGameMode()==GameMode.SPECTATOR?1:0):0; //survival or spectator (1.8+)
			if(game==0) { //vanish
				if(setting.tab_vanish && vanish) {
					gmResult=1;
				}else
					if(setting.tab_move && player.getGameMode()==GameMode.SPECTATOR)gmResult=1;
			}else { //spectator
				if(setting.tab_vanish && vanish) {
					gmResult=1;
				}else
					if(setting.tab_move && player.getGameMode()==GameMode.SPECTATOR)gmResult=1;
					else {
						c++;
						continue;
					}
			}
			Ref.set(o, "c", gmResult==0?surv:spec); //edit
			bList.set(c++, o);
		}
		Ref.set(b, "b", bList);
		List<Player> f = API.getPlayersThatCanSee(player);
		f.remove(player);
		Ref.sendPacket(f, b);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Vanish", "Other") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}
	
	public static final Map<String, Integer> task = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "Vanish", "Other")) {
			if(!CommandsManager.canUse("Other.Vanish", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Vanish", s))));
				return true;
			}
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					if (!API.hasVanish(p)) {
						API.setVanish(p, Loader.getPerm("Vanish","Other"), true);
						if(setting.tab && setting.tab_vanish)
					    	moveInTab(p, 0, true);
						Loader.sendMessages(s, "Vanish.Enabled.You");
						if(setting.vanish_action)
							task.put(p.getName(), new Tasker() {
								@Override
								public void run() {
									if(!API.hasVanish(p.getName()) || !p.isOnline()) {
										cancel();
										return;
									}
									TheAPI.sendActionBar(p, Loader.getTranslation("Vanish.Active").toString());
								}
							}.runRepeating(0, 20));
						return true;
					}
					if(task.containsKey(s.getName())) {
						Scheduler.cancelTask(task.get(s.getName()));
						task.remove(s.getName());
						TheAPI.sendActionBar(p, "");
					}
					API.setVanish(p, Loader.getPerm("Vanish","Other"), false);
					if(setting.tab && setting.tab_vanish)
				    	moveInTab(p, 0, false);
					Loader.sendMessages(s, "Vanish.Disabled.You");
					return true;
				}
				Loader.Help(s, "Vanish", "Other");
				return true;
			}
			if (Loader.has(s, "Vanish", "Other", "Other")) {
			Player t = TheAPI.getPlayer(args[0]);
			if (t != null) {
				if (!API.hasVanish(t)) {
					API.setVanish(t, Loader.getPerm("Vanish","Other"), true);
					if(setting.tab && setting.tab_vanish)
				    	moveInTab(t, 0, true);
					Loader.sendMessages(s, "Vanish.Enabled.Other.Sender", Placeholder.c().add("%player%", t.getName()).add("%playername%", t.getDisplayName()));
					Loader.sendMessages(s, "Vanish.Enabled.Other.Receiver", Placeholder.c().add("%player%", s.getName()).add("%playername%", s.getName()));
					if(setting.vanish_action)
						task.put(t.getName(),new Tasker() {
							@Override
							public void run() {
								if(!API.hasVanish(t.getName()) || !t.isOnline()) {
									cancel();
									return;
								}
								TheAPI.sendActionBar(t, Loader.getTranslation("Vanish.Active").toString());
							}
						}.runRepeating(0, 20));
					return true;
				}
				if(task.containsKey(t.getName())) {
					Scheduler.cancelTask(task.get(t.getName()));
					task.remove(t.getName());
					TheAPI.sendActionBar(t, "");
				}
				API.setVanish(t, Loader.getPerm("Vanish","Other"), false);
				if(setting.tab && setting.tab_vanish)
			    	moveInTab(t, 0, false);
				Loader.sendMessages(s, "Vanish.Disabled.Other.Sender", Placeholder.c().add("%player%", t.getName()).add("%playername%", t.getDisplayName()));
				Loader.sendMessages(s, "Vanish.Disabled.Other.Receiver", Placeholder.c().add("%player%", s.getName()).add("%playername%", s.getName()));
				return true;
			}
			Loader.notOnline(s, args[0]);
			return true;
			}
			Loader.noPerms(s, "Vanish", "Other", "Other");
			return true;
		}
		Loader.noPerms(s, "Vanish", "Other");
		return true;
	}
}