package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;
import me.Straiker123.TheAPI;

public class BanSystem {
	public static String ip(String p) {
		if(Loader.me.getString("Players."+p+".IPAdress")!=null)return Loader.me.getString("Players."+p+".IPAdress");
		return null;
	}
	public enum BanType {
		JAIL,
		TEMPJAIL,
		KICK
	}
	public static String BuildString(int s, int d, String[] args) {
		String msg = "";
		  for (int i = s; i < args.length; ++i) {
            msg = String.valueOf(msg) + args[i] + " ";
        }
        msg=msg.substring(0,msg.length()-d);
        return msg;
	}
	public static int Warns(String player) {
		return Loader.ban.getInt("Warn."+player+".Amount");
	}
	public static void resetWarns(String player) {
		Loader.me.set("Players."+player+".WarnFinished",null);
		Loader.ban.set("Warn."+player+".Amount", 0);
		Configs.chatme.save();
		Configs.bans.save();
	}
	public static void KickMaxWarns(String player) {
		if(Loader.ban.getString("Warn."+player)!=null) {
			if(Loader.config.getString("BanSystem.Warn.Operations."+Warns(player))!=null) {
				if(!Loader.me.getBoolean("Players."+player+".WarnFinished."+Warns(player))) {
					String reason = Loader.config.getString("BanSystem.Warn.Reason");
					if(Loader.ban.getString("Warn."+player+".WarnLater.Message")!=null)
						reason=Loader.ban.getString("Warn."+player+".WarnLater.Message");
				for(String s:Loader.config.getStringList("BanSystem.Warn.Operations."+Warns(player)+".Messages")) {
					if(TheAPI.getPlayer(player)!=null)TheAPI.getPlayer(player).sendMessage(TheAPI.colorize(s
							.replace("%player%", player)
							.replace("%playername%", getName(player))
							.replace("%time%", Loader.ban.getString("Warn."+player+".WarnLater.Time"))
							.replace("%warnedby%", Loader.ban.getString("Warn."+player+".WarnLater.WarnedBy"))
							.replace("%reason%", reason)));
				}
				for(String s:Loader.config.getStringList("BanSystem.Warn.Operations."+Warns(player)+".Commands")) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), TheAPI.colorize(s
							.replace("%player%", player)
							.replace("%playername%", getName(player))
							.replace("%time%", Loader.ban.getString("Warn."+player+".WarnLater.Time"))
							.replace("%warnedby%", Loader.ban.getString("Warn."+player+".WarnLater.WarnedBy"))
							.replace("%reason%", reason)));
				}
				Loader.me.set("Players."+player+".WarnFinished."+Warns(player), true);

				Configs.chatme.save();
				}
				
			}
		}
	}
	public static String getLaterWarn(String player) {
		if(Loader.ban.getString("Warn."+player+".WarnLater")!=null)return Loader.s("BanSystem.WarnLater").replace("%player%", player)
				.replace("%warnedby%", Loader.ban.getString("Warn."+player+".WarnLater.WarnedBy"))
				.replace("%time%", Loader.ban.getString("Warn."+player+".WarnLater.Time")).replace("%playername%", getName(player))
				.replace("%prefix%", Loader.s("Prefix")).replace("%reason%", Loader.ban.getString("Warn."+player+".WarnLater.Reason"));
		return null;
	}
	public static String getName(String s) {
		if(TheAPI.getPlayer(s)!=null)return TheAPI.getPlayer(s).getDisplayName();
		return s;
	}

	public static boolean isArrested(String p) {
		if(Loader.me.getString("Players."+p+".Jail")!=null)return true;
		return false;
	}
	public static boolean isTempArrested(String p) {
		if(Loader.me.getString("Players."+p+".TempJail")!=null)return true;
		return false;
	}
	public static String getJailReason(String p) {
		if(Loader.me.getString("Players."+p+".Jail.Reason")!=null)return Loader.me.getString("Players."+p+".Jail.Reason");
		return "";
	}
	public static String getTempJailReason(String p) {
		if(Loader.me.getString("Players."+p+".TempJail.Reason")!=null)return Loader.me.getString("Players."+p+".TempJail.Reason");
		return "";
	}
	public static String getKickReason(String p) {
		if(Loader.ban.getString("Bans."+p+".Kick")!=null)return Loader.ban.getString("Bans."+p+".Kick");
		return "";
	}

	public static void notExist(CommandSender s, String[] args) {
		if(Loader.me.getString("Players."+args[0])!=null) Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.PlayerHasNotBan")
		.replace("%player%", args[0]).replace("%playername%", args[0]),s);else
			Loader.msg(Loader.PlayerNotEx(args[0]),s);
	}
	public static void notMuted(CommandSender s, String[] args) {
		Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.PlayerNotMuted").replace("%player%", args[0])
				.replace("%playername%", getName(args[0])),s);
	}
	public static void setPlayer(BanType type, String player, String reason, long time, long start, CommandSender s) {
		String a = s.getName();
		if(a != player) {
			switch(type) {
		case KICK:
			Loader.ban.set("Bans."+player+".Kick", reason);
			break;
		case JAIL:
			Loader.me.set("Players."+player+".Jail.Reason", reason);
			break;
		case TEMPJAIL:
			Loader.me.set("Players."+player+".TempJail.Reason", reason);
			Loader.me.set("Players."+player+".TempJail.Start", start);
			Loader.me.set("Players."+player+".TempJail.Time", time);
			break;
			}
			Configs.chatme.save();
			Configs.bans.save();
	}}
	@SuppressWarnings("deprecation")
	public static void kickPlayer(CommandSender s, String player,BanType type) {
		String a = s.getName();
		switch(type) {
		case JAIL:
			if(a == Bukkit.getOfflinePlayer(player).getName()) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.CantJailYourself").replace("%player%", s.getName())
						.replace("%playername%", getName(s.getName())),s);
			}else {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.Jail").replace("%player%", player)
					.replace("%playername%", getName(player))
					.replace("%reason%", getJailReason(player)),s);
			if(TheAPI.getPlayer(player)!=null) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.Arrested").replace("%player%", player)
					.replace("%playername%", getName(player))
					.replace("%reason%", getJailReason(player)),TheAPI.getPlayer(player));
			TheAPI.getPlayer(player).teleport((Location) Loader.config.get("Jails."+Loader.me.getString("Players."+player+".Jail.Location")));
			}
			}
		break;
		case TEMPJAIL:
			if(a == Bukkit.getOfflinePlayer(player).getName()) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.CantJailYourself").replace("%player%", s.getName())
						.replace("%playername%", getName(s.getName())),s);
			}else {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.TempJail").replace("%player%", player)
					.replace("%playername%", getName(player))
					.replace("%reason%", getTempJailReason(player))
					.replace("%time%",TheAPI.getStringUtils().setTimeToString(API.getBanSystemAPI().getTempJailTime(player))),s);
			if(TheAPI.getPlayer(player)!=null) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.TempArrested").replace("%player%", player)
					.replace("%playername%", getName(player))
					.replace("%time%",TheAPI.getStringUtils().setTimeToString(API.getBanSystemAPI().getTempJailTime(player)))
					.replace("%reason%", getTempJailReason(player)),TheAPI.getPlayer(player));
			TheAPI.getPlayer(player).teleport((Location) Loader.config.get("Jails."+Loader.me.getString("Players."+player+".TempJail.Location")));
			}
			}
		break;
	   case KICK:
			if(a == Bukkit.getOfflinePlayer(player).getName()) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.CantKickYourself").replace("%player%", s.getName())
						.replace("%playername%", getName(s.getName())),s);
			}else {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.Kick").replace("%player%", player)
					.replace("%playername%", getName(player))
					.replace("%reason%", getKickReason(player)),s);
			if(TheAPI.getPlayer(player)!=null)TheAPI.getPlayer(player).kickPlayer(TheAPI.colorize(Loader.config.getString("Format.Kick")
					.replace("%reason%", getKickReason(player)).replace("%player%", player).replace("%playername%", getName(player))));
		}break;}}}