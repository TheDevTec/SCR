package ServerControl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import Commands.BanSystem.BanSystem;
import Commands.BanSystem.BanSystem.BanType;
import Utils.Configs;
import me.Straiker123.TheAPI;

public class BanSystemAPI {
	 public boolean hasJail(String p) {
		 return BanSystem.isArrested(p);
	 }
	 public boolean hasJail(Player p) {
		 return BanSystem.isArrested(p.getName());
	 }
	 public void setJail(CommandSender sender,String player, String jail, String reason) {
		 BanSystem.setPlayer(BanSystem.BanType.JAIL, player, reason, 0, System.currentTimeMillis(), sender);
		 Loader.me.set("Players."+player+".Jail.Location", jail);
		 Configs.chatme.save();
		BanSystem.kickPlayer(sender,player,BanType.JAIL);
	 }
	 public void setJail(CommandSender sender,Player player, String jail, String reason) {
		 BanSystem.setPlayer(BanSystem.BanType.JAIL, player.getName(), reason, 0, System.currentTimeMillis(), sender);
		 Loader.me.set("Players."+player.getName()+".Jail.Location", jail);
		 Configs.chatme.save();
		BanSystem.kickPlayer(sender,player.getName(),BanType.JAIL);
	 }

	 public void setKick(CommandSender sender,String player, String reason) {
		 BanSystem.setPlayer(BanSystem.BanType.KICK, player, reason, 0, System.currentTimeMillis(), sender);
			BanSystem.kickPlayer(sender,player,BanType.KICK);
	 }
	 public void setKick(CommandSender sender,Player player, String reason) {
		 BanSystem.setPlayer(BanSystem.BanType.KICK, player.getName(), reason, 0, System.currentTimeMillis(), sender);
			BanSystem.kickPlayer(sender,player.getName(),BanType.KICK);
	 }
	 
	public void processBanSystem(Player p,PlayerLoginEvent e) {
		String n = p.getName();
		String r = e.getAddress().toString();
		Loader.me.set("Players."+n+".IPAdress", r.replace('.', '_'));
		 Configs.chatme.save();
			Commands.BanSystem.BanSystem.KickMaxWarns(n);
			if (Loader.config.getBoolean("MaxNumberOfKicks.Enabled")==true) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Loader.getInstance, new Runnable() {
				public void run() {
			int Kicker = Loader.config.getInt("MaxNumberOfKicks.Number");
		int Kick = Loader.me.getInt("Players."+n+".Kicks");
		
	if(Kick >= Kicker) {
		Loader.me.set("Players."+n+".Kicks" ,Kick - Kicker);
		 Configs.chatme.save();
		if(Loader.config.getBoolean("MaxNumberOfKicks.Message.Enabled")==true) {
			List<String> cmd = Loader.config.getStringList("MaxNumberOfKicks.Message.Messages");
			    	for(String cmds: cmd) {
			    		Loader.msg(cmds.replace("%player%", n).replace("%number%", String.valueOf(Kicker)),p);
	    	}}
	         if(Loader.config.getBoolean("MaxNumberOfKicks.Commands.Enabled")==true) {
	new BukkitRunnable() {
		@Override
		public void run() {
			List<String> cmd = Loader.config.getStringList("MaxNumberOfKicks.Commands.Commands");
	    	for(String cmds: cmd) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), TheAPI.colorize(cmds.replace("%player%", n).replace("%number%", String.valueOf(Kicker).toString())));
		  }}
	}.runTask(Loader.getInstance);{
	}}}}}, 20);}
			if(BanSystem.getLaterWarn(n)!=null && Loader.ban.getBoolean("Warn."+n+".WarnLater.Wait")==true) {
				Loader.ban.set("Warn."+n+".WarnLater.Wait", false);
				 Configs.bans.save();
				 Loader.msg(BanSystem.getLaterWarn(n), p);
				return;
			}
	 }
	 public void setWarn(String player,CommandSender sender, String reason) {
			int warns = Loader.ban.getInt("Warn."+player+".Amount");
			Loader.ban.set("Warn."+player+".Amount",warns+1);
			Loader.ban.set("Warn."+player+".WarnLater.Reason",reason);
			Loader.ban.set("Warn."+player+".WarnLater.WarnedBy",sender.getName());
			Loader.ban.set("Warn."+player+".WarnLater.Time",(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date())));
			Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.Warn").replace("%reason%", reason).replace("%player%", player).replace("%playername%", BanSystem.getName(player)).replace("%warnedby%", sender.getName()), Bukkit.getPlayer(player));
			if(Bukkit.getPlayer(player)!=null)
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.Warned").replace("%reason%", reason).replace("%player%", player).replace("%playername%", BanSystem.getName(player)).replace("%warnedby%", sender.getName()), Bukkit.getPlayer(player));
			else
				Loader.ban.set("Warn."+player+".WarnLater.Wait",true);
			 Configs.bans.save();
			BanSystem.KickMaxWarns(player);
	 }
}
