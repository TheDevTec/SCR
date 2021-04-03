package me.DevTec.ServerControlReloaded.Events;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.ChatFormatter;
import me.DevTec.ServerControlReloaded.Utils.Tasks;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.utils.StreamUtils;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.json.Reader;
import me.devtec.theapi.utils.reflections.Ref;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPreLoginEvent;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginEvent implements Listener {
	private static Class<?> cc = Ref.nms("EnumGamemode")!=null?Ref.nms("EnumGamemode"):Ref.nms("WorldSettings$EnumGamemode");
	private static Object surv = Ref.getNulled(cc, "SURVIVAL"), spec = Ref.getNulled(cc, "SPECTATOR");
	private static Object up = Ref.getNulled(Ref.field(Ref.nms("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), "UPDATE_GAME_MODE"));

	public static void moveInTab(Player player, int game, boolean vanish) {
		Object array = Array.newInstance(Ref.nms("EntityPlayer"), 1);
		Array.set(array, 0, Ref.player(player));
		Object b = Ref.newInstance(Ref.constructor(Ref.nms("PacketPlayOutPlayerInfo"), Ref.nms("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), array.getClass()), up, array);
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
					else gmResult=0;
			}
			Ref.set(o, "c", gmResult==0?surv:spec); //edit
			bList.set(c++, o);
		}
		Ref.set(b, "b", bList);
		List<Player> f = TheAPI.getOnlinePlayers();
		f.remove(player);
		Ref.sendPacket(f, b);
	}

	private void bc(Player p) {
		if (setting.vip_join) {
			TheAPI.broadcastMessage(
					Loader.config.getString("Options.VIPSlots.Text.BroadcastVIPJoin")
							.replace("%players_max%", String.valueOf(TheAPI.getMaxPlayers()
									+ (setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)))
							.replace("%online%", String.valueOf(TheAPI.getOnlinePlayers().size()))
							.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
							.replace("%prefix%", setting.prefix)
							.replace("%time%", setting.format_time.format(new Date()))
							.replace("%date%", setting.format_date.format(new Date()))
							.replace("%date-time%", setting.format_date_time.format(new Date())));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void JoinEvent(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		ChatFormatter.setupName(p);
		if (setting.lock_server && !Loader.has(p, "Other", "Maintenance", "Bypass")) {
			e.disallow(Result.KICK_OTHER, TheAPI.colorize(StringUtils.join(Loader.config.getStringList("Options.Maintenance.KickMessages"), "\n").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())));
			return;
		}
		if (setting.vip && TheAPI.getMaxPlayers() == TheAPI.getOnlinePlayers().size() - 1) {
			Config f = Loader.config;
			boolean has = p.hasPermission("SCR.Other.JoinFullServer");
			int max = TheAPI.getMaxPlayers() + (setting.vip_add ? f.getInt("Options.VIPSlots.SlotsToAdd") : 0);
			Player randomPlayer = Tasks.players.isEmpty() ? null : TheAPI.getRandomPlayer();
			if (has) {
				if (TheAPI.getMaxPlayers() > max && randomPlayer == null) {
					e.disallow(Result.KICK_FULL, TheAPI.colorize(f.getString("Options.VIPSlots.FullServer")));
					return;
				}
				if (setting.vip_kick) {
					if (TheAPI.getMaxPlayers() > max && randomPlayer != null) {
						Tasks.players.remove(randomPlayer.getName());
						randomPlayer.kickPlayer(TheAPI.colorize(f.getString("Options.VIPSlots.Text.Kick")));
						bc(p);
						e.allow();
						return;
					}
				}
			} else if (TheAPI.getOnlinePlayers().size() >= TheAPI.getMaxPlayers()) {
				e.disallow(Result.KICK_FULL, TheAPI.colorize(f.getString("Options.VIPSlots.Text.Kick")));
				return;
			}
		}
	}
	public static Map<String, Object> getCountry(String a) {
		try {
			URL url = new URL("http://ip-api.com/json/" + a.replace("_", "."));
			return (Map<String,Object>) Reader.read(StreamUtils.fromStream(url.openStream()));
		} catch (Exception e) {}
		return null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void AsyncPreLoginEvent(AsyncPlayerPreLoginEvent e){
		if(Loader.config.getBoolean("CountryBlocker.Enabled")){
			Map<String,Object>country = getCountry(e.getAddress().getHostAddress());
			for(String a : Loader.config.getStringList("CountryBlocker.List")){
				if(country.getOrDefault("countryCode","UNKNOWN").equals(a.toUpperCase())){
					for(String b:Loader.config.getStringList("CountryBlocker.Whitelist")){
						if(e.getName().equalsIgnoreCase(b)){
							e.setResult(PlayerPreLoginEvent.Result.ALLOWED);
							break;
						}else{
							e.setKickMessage(TheAPI.colorize(Loader.config.getString("CountryBlocker.KickMessage")));
							e.setResult(PlayerPreLoginEvent.Result.KICK_WHITELIST);
						}
					}
				}else{
					e.setResult(PlayerPreLoginEvent.Result.ALLOWED);
				}
			}
		}
	}
}