package me.devtec.servercontrolreloaded.utils;

import java.lang.reflect.Method;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;

public class SPlayer {
	public boolean lock;
	private final String s;
	public Player player;
	public int kick, afk;
	public boolean bc, manual;
	public String reply, type;
	public Location l;
	
	public SPlayer(Player p) {
		this.player=p;
		s = p.getName();
	}
	
	public SPlayer(String p) {
		s = p;
		player=TheAPI.getPlayerOrNull(p);
	}

	public User getUser() {
		return TheAPI.getUser(s);
	}
	
	public void setHP() {
		Player s = getPlayer();
		if(s==null)return;
		s.setHealth(((Damageable)s).getMaxHealth());
	}

	public void heal() {
		Player s = getPlayer();
		if(s==null)return;
		setHP();
		setFood();
		setAir();
		setFire();
		for (PotionEffect e : s.getActivePotionEffects())
			s.removePotionEffect(e.getType());
	}

	public void setFood() {
		Player s = getPlayer();
		if(s==null)return;
		s.setFoodLevel(20);
	}

	public void setAir() {
		Player s = getPlayer();
		if(s==null)return;
		s.setRemainingAir(s.getMaximumAir());
	}

	public void enableTempGameMode(long time,GameMode g,boolean t) {
		User u = getUser();
		u.set("TempGamemode.Start",System.currentTimeMillis());
		u.set("TempGamemode.Time",time);
		u.save();
		Player s = getPlayer();
		if(s==null) {
			u.save();
			return;
		}
		u.set("TempGamemode.Prev",s.getGameMode());
		if(!hasGameMode()){
			u.set("TempGamemode.Use",true);
			u.save();
			s.setGameMode(g);
			if(!t){
				Loader.sendMessages(s, "GameMode.Temp.You", Placeholder.c().add("%time%", StringUtils.timeToString(time)).add("%gamemode%",g.toString().toLowerCase()));
			}else{
				Loader.sendMessages(s, "GameMode.Temp.Reciever", Placeholder.c().add("%time%", StringUtils.timeToString(time)).add("%gamemode%",g.toString().toLowerCase()));
			}
		}
	}

	public boolean hasGameMode(){
		return getUser().getBoolean("TempGamemode.Use");
	}

	public void enableTempFly(long stop) {
		User u = getUser();
		u.set("TempFly.Start", System.currentTimeMillis());
		u.set("TempFly.Time", stop);
		Player s = getPlayer();
		if (!hasTempFlyEnabled()) {
			u.set("TempFly.Use", true);
			u.save();
			if(s==null)return;
			s.setAllowFlight(true);
			s.setFlying(true);
			Loader.sendMessages(s, "Fly.Temp.Enabled.You", Placeholder.c().add("%time%", StringUtils.setTimeToString(stop)));
			return;
		}
		u.save();
		if(s==null)return;
		Loader.sendMessages(s, "Fly.Temp.Enabled.You", Placeholder.c().add("%time%", StringUtils.setTimeToString(stop)));
	}

	public void enableTempFly() {
		Player s = getPlayer();
		if(s==null)return;
		if (hasTempFlyEnabled()) {
			s.setAllowFlight(true);
			s.setFlying(true);
		}
	}

	public void enableFly() {
		User u = getUser();
		boolean save = false;
		if (hasTempFlyEnabled()) {
			u.set("TempFly.Use", false);
			save=true;
		}
		if(!getUser().getBoolean("Fly")) {
			u.set("Fly", true);
			save=true;
		}
		if(save)u.save();
		Player s = getPlayer();
		if(s==null)return;
		s.setAllowFlight(true);
		s.setFlying(true);
	}

	public void disableFly() {
		User u = getUser();
		u.remove("TempFly");
		u.remove("Fly");
		u.save();
		Player s = getPlayer();
		if(s==null)return;
		s.setFlying(false);
		s.setAllowFlight(false);
	}

	private static final Class<?> ess = Ref.getClass("com.earth2me.essentials.Essentials");
	private static final Method getUser = Ref.method(ess, "getUser", Player.class);
	
	public boolean isAFK() {
		try {
			if(ess!=null) {
			Object user = Ref.invoke(Ref.cast(ess, PluginManagerAPI.getPlugin("Essentials")), getUser, s);
			if (PluginManagerAPI.isEnabledPlugin("Essentials") && user!=null&& (boolean)Ref.invoke(user, "isAfk"))
				return true;
			}
		} catch (Exception er) {
		}
		return (Loader.getInstance.time - afk <=0 || manual);
	}

	public void setAFK(boolean afk) {
		Player s = getPlayer();
		if(s==null)return;
		if (!afk) {
			Loader.getInstance.save(s);
		} else {
			Loader.getInstance.moving.put(s.getName(), s.getLocation());
			if(isAFK()) {
				for(Player canSee : API.getPlayersThatCanSee(s))
					Loader.sendMessages(canSee, s, "AFK.End");
				NMSAPI.postToMainThread(() -> {
					for(String ds : Loader.config.getStringList("Options.AFK.Action.onStopAFK"))
						TheAPI.sudoConsole(TabList.replace(ds,s,true));
				});
			}
			this.afk=0;
			kick = 0;
			bc = true;
			manual = true;
			for(Player canSee : API.getPlayersThatCanSee(s))
				Loader.sendMessages(canSee, s, "AFK.Start");
		}
	}
	public void setAFK(boolean afk, String reason) {
		Player s = getPlayer();
		if(s==null)return;
		if (!afk) {
			Loader.getInstance.save(s);
		} else {
			Loader.getInstance.moving.put(s.getName(), s.getLocation());
			if(isAFK()) {
				for(Player canSee : API.getPlayersThatCanSee(s))
					Loader.sendMessages(canSee, s, "AFK.End");
				NMSAPI.postToMainThread(() -> {
					for(String ds : Loader.config.getStringList("Options.AFK.Action.onStopAFK"))
						TheAPI.sudoConsole(TabList.replace(ds,s,true));
				});
			}
			this.afk=0;
			kick = 0;
			bc = true;
			manual = true;
			for(Player canSee : API.getPlayersThatCanSee(s))
				Loader.sendMessages(canSee, s, "AFK.Start_WithReason", Placeholder.c().add("%reason%", reason));
		}
	}

	public void setFire() {
		Player s = getPlayer();
		if(s==null)return;
		s.setFireTicks(-20);
	}

	public String getName() {
		return s;
	}

	public String getDisplayName() {
		Player s = getPlayer();
		return s!=null?getOrDefault(s.getDisplayName(),this.s):getOrDefault(TheAPI.getUser(this.s).getString("DisplayName"),this.s);
	}

	private String getOrDefault(String nullable, String nonnull) {
		return nullable!=null?nullable:nonnull;
	}

	public String getCustomName() {
		Player s = getPlayer();
		return s!=null?getOrDefault(s.getDisplayName(),this.s):this.s;
	}

	public int getFoodLevel() {
		Player s = getPlayer();
		return s!=null?s.getFoodLevel():-1;
	}

	public double getHealth() {
		Player s = getPlayer();
		return s!=null?((Damageable)s).getHealth():-1;
	}

	public Player getPlayer() {
		if(player==null||!player.isOnline())player=TheAPI.getPlayerOrNull(s);
		return player;
	}

	public void toggleGod(CommandSender toggler) {
		Player s = getPlayer();
		if(s==null) {
			if (hasGodEnabled()) {
				if (toggler != null)
					Loader.sendMessages(toggler, "God.Disabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getDisplayName()).add("%customname%", getCustomName()));
					disableGod();
			} else {
				if (toggler != null)
					Loader.sendMessages(toggler, "God.Enabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getDisplayName()).add("%customname%", getCustomName()));
					enableGod();
			}
			return;
		}
		if (hasGodEnabled()) {
			Loader.sendMessages(s, "God.Disabled.Other.Receiver");
			if (toggler != null)
			Loader.sendMessages(toggler, "God.Disabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getDisplayName()).add("%customname%", getCustomName()));
			disableGod();
		} else {
			Loader.sendMessages(s, "God.Enabled.Other.Receiver");
			if (toggler != null)
			Loader.sendMessages(toggler, "God.Enabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getDisplayName()).add("%customname%", getCustomName()));
			enableGod();
		}
	}

	public void toggleFly(CommandSender toggler) {
		Player s = getPlayer();
		if(s==null) {
			if (hasFlyEnabled(false)) {
				if (toggler != null)
					Loader.sendMessages(toggler, "Fly.Disabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getDisplayName()).add("%customname%", getCustomName()));
				disableFly();
			} else {
				if (toggler != null)
					Loader.sendMessages(toggler, "Fly.Enabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getDisplayName()).add("%customname%", getCustomName()));
				enableFly();
			}
			return;
		}
		if (hasFlyEnabled(true)) {
			Loader.sendMessages(s, "Fly.Disabled.Other.Receiver");
			if (toggler != null)
				Loader.sendMessages(toggler, "Fly.Disabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getDisplayName()).add("%customname%", getCustomName()));
			disableFly();
		} else {
			Loader.sendMessages(s, "Fly.Enabled.Other.Receiver");
			if (toggler != null)
				Loader.sendMessages(toggler, "Fly.Enabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getDisplayName()).add("%customname%", getCustomName()));
			enableFly();
		}
	}

	public void setWalkSpeed() {
		Player s = getPlayer();
		if(s==null)return;
		User u = getUser();
		if (u.exist("WalkSpeed")) {
			if (u.getFloat("WalkSpeed") < 0.0)
				s.setWalkSpeed(0);
			else if (u.getFloat("WalkSpeed") > 10.0)
				s.setWalkSpeed(10);
			else
				s.setWalkSpeed(u.getFloat("WalkSpeed"));
		}
	}

	public void setFlySpeed() {
		Player s = getPlayer();
		if(s==null)return;
		User u = getUser();
		if (u.exist("FlySpeed")) {
			if (u.getFloat("FlySpeed") < 0.0)
				s.setFlySpeed(0);
			else if (u.getFloat("FlySpeed") > 10.0)
				s.setFlySpeed(10);
			else
				s.setFlySpeed(u.getFloat("FlySpeed"));
		}
	}

	public void enableGod() {
		User u = getUser();
		if(!u.getBoolean("God")) {
			u.set("God", true);
			u.save();
		}
		heal();
	}

	public boolean hasPermission(String perm) {
		return hasPerm(perm);
	}

	public boolean hasPerm(String perm) {
		Player s = getPlayer();
		if(s==null)return false;
		return s.hasPermission(perm);
	}

	public void disableGod() {
		getUser().remove("God");
	}

	public void createEconomyAccount() {
		if (setting.eco_multi && EconomyAPI.getEconomy() != null)
			EconomyAPI.createAccount(s);
	}

	public boolean hasFlyEnabled(boolean checkEnabled) {
		Player s = getPlayer();
		if(!checkEnabled)
			return getUser().getBoolean("Fly")||s!=null&&s.getAllowFlight();
		else {
			return s!=null&&s.getAllowFlight();
		}
	}

	public boolean hasGodEnabled() {
		return getUser().getBoolean("God");
	}

	public boolean hasTempFlyEnabled() {
		return getUser().getBoolean("TempFly.Use");
	}
	
	public void addPlayTime(int seconds) {
		User u = getUser();
		GameMode g = player.getGameMode();
		u.set("Statistics.PlayTime", (u.getInt("Statistics.PlayTime")+seconds) );
		u.set("Statistics."+g.name()+".PlayTime", (u.getInt("Statistics."+g.name()+".PlayTime")+seconds));
		u.set("Statistics."+g.name()+"."+player.getWorld().getName()+".PlayTime", (u.getInt("Statistics."+g.name()+"."+player.getWorld().getName()+".PlayTime")+seconds));
		u.set("Statistics."+player.getWorld().getName()+".PlayTime", (u.getInt("Statistics."+player.getWorld().getName()+".PlayTime")+seconds));
		u.save();
		/*
		 * Statistics:
		 *   PlayTime:
		 *   <GAMEMODE>:
		 *     PlayTime:
		 *     <WORLD>:
		 *       PlayTime:
		 *   <WORLD>:
		 *     PlayTime:
		 */
	}

	public int getPlayTime(String path) {
		return getUser().getInt("Statistics."+path);
	}
	
	
	
	
}
