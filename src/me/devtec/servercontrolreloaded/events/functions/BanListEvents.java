package me.devtec.servercontrolreloaded.events.functions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.cooldownapi.CooldownAPI;
import me.devtec.theapi.punishmentapi.Punishment;
import me.devtec.theapi.punishmentapi.Punishment.PunishmentType;
import me.devtec.theapi.utils.StringUtils;

public class BanListEvents implements Listener {
	
	public Punishment lookup(String name, String ip, Punishment.PunishmentType type) {
		for(Punishment p : TheAPI.getPunishmentAPI().getPunishments(name))
			if(p.getType()==type)
				return p;
		for(Punishment p : TheAPI.getPunishmentAPI().getPunishmentsIP(ip))
			if(p.getType()==type)
				return p;
		return null;
	}
	
	public Punishment lookupCmds(String name, String ip) {
		for(Punishment p : TheAPI.getPunishmentAPI().getPunishments(name))
			if(p.getType()==PunishmentType.MUTE||p.getType()==PunishmentType.JAIL)
				return p;
		for(Punishment p : TheAPI.getPunishmentAPI().getPunishmentsIP(ip))
			if(p.getType()==PunishmentType.MUTE||p.getType()==PunishmentType.JAIL)
				return p;
		return null;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void banlistLogin(AsyncPlayerPreLoginEvent e) {
		if(e.getLoginResult()!=Result.ALLOWED)return;
		Punishment banlist = lookup(e.getName(), e.getAddress().toString().replaceAll("[^0-9.]+", ""), PunishmentType.BAN);
		if(banlist!=null) {
			e.setLoginResult(Result.KICK_BANNED);
			e.setKickMessage(banlist.getDuration()!=0?TabList.replace(banlist.getReason().replace("%player%", e.getName()).replace("%time%", StringUtils.timeToString(banlist.getExpire())), null, true):
				TabList.replace(banlist.getReason().replace("%player%", e.getName()), null, true));
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void banlistChat(AsyncPlayerChatEvent e) {
		if(e.isCancelled())return;
		Punishment banlist = lookup(e.getPlayer().getName(), TheAPI.getPunishmentAPI().getIp(e.getPlayer().getName()), PunishmentType.MUTE);
		if(banlist!=null) {
			e.setCancelled(true);
			if(antiSpam(e.getPlayer(), "mute"))TheAPI.msg(banlist.getDuration()!=0?TabList.replace(banlist.getReason().replace("%player%", e.getPlayer().getName()).replace("%time%", StringUtils.timeToString(banlist.getExpire())), e.getPlayer(), true):
				TabList.replace(banlist.getReason().replace("%player%", e.getPlayer().getName()),e.getPlayer(),true), e.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void banlistBreakBlock(BlockBreakEvent e) {
		if(e.isCancelled())return;
		Punishment banlist = lookup(e.getPlayer().getName(), TheAPI.getPunishmentAPI().getIp(e.getPlayer().getName()), PunishmentType.JAIL);
		if(banlist!=null) {
			e.setCancelled(true);
			if(antiSpam(e.getPlayer(), "jail"))TheAPI.msg(banlist.getDuration()!=0?TabList.replace(banlist.getReason().replace("%player%", e.getPlayer().getName()).replace("%time%", StringUtils.timeToString(banlist.getExpire())), e.getPlayer(), true):
				TabList.replace(banlist.getReason().replace("%player%", e.getPlayer().getName()),e.getPlayer(),true), e.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void banlistPlaceBlock(BlockPlaceEvent e) {
		if(e.isCancelled())return;
		Punishment banlist = lookup(e.getPlayer().getName(), TheAPI.getPunishmentAPI().getIp(e.getPlayer().getName()), PunishmentType.JAIL);
		if(banlist!=null) {
			e.setCancelled(true);
			if(antiSpam(e.getPlayer(), "jail"))TheAPI.msg(banlist.getDuration()!=0?TabList.replace(banlist.getReason().replace("%player%", e.getPlayer().getName()).replace("%time%", StringUtils.timeToString(banlist.getExpire())), e.getPlayer(), true):
				TabList.replace(banlist.getReason().replace("%player%", e.getPlayer().getName()),e.getPlayer(),true), e.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void banlistProcessCommand(PlayerCommandPreprocessEvent e) {
		if(e.isCancelled())return;
		Punishment banlist = lookupCmds(e.getPlayer().getName(), TheAPI.getPunishmentAPI().getIp(e.getPlayer().getName()));
		if(banlist!=null) {
			if(isForbidden(banlist.getTypeName().contains("mute")?(banlist.getTypeName().startsWith("temp")?"TempMute":"Mute"):(banlist.getTypeName().startsWith("temp")?"TempJail":"Jail"), e.getMessage().substring(1).toLowerCase())) {
				e.setCancelled(true);
				if(antiSpam(e.getPlayer(), banlist.getTypeName().contains("mute")?"mute":"jail"))TheAPI.msg(banlist.getDuration()!=0?TabList.replace(banlist.getReason().replace("%player%", e.getPlayer().getName()).replace("%time%", StringUtils.timeToString(banlist.getExpire())), e.getPlayer(), true):
					TabList.replace(banlist.getReason().replace("%player%", e.getPlayer().getName()),e.getPlayer(),true), e.getPlayer());
				return;
			}
		}
	}
	
	private boolean isForbidden(String name, String cmd) {
		for(String c : Loader.config.getStringList("BanSystem."+name+".ForbiddenCommands"))
			if(cmd.startsWith(c.toLowerCase()))return true;
		return false;
	}

	public boolean antiSpam(Player player, String name) {
		CooldownAPI c = TheAPI.getCooldownAPI(player);
		if(c.expired(name)) {
			c.createCooldown(name, 20 * 3); //3s
			return true;
		}
		return false;
	}
}
