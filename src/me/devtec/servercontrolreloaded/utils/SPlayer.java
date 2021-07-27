package me.devtec.servercontrolreloaded.utils;

import java.lang.reflect.Method;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.reflections.Ref;

public class SPlayer {
	public boolean lock;
	private final String s;
	public Player player;
	public int kick, afk;
	public boolean bc, manual;
	public String reply, type;
	private User f;
	public Location l;
	
	public SPlayer(Player p) {
		this.player=p;
		s = p.getName();
		f=TheAPI.getUser(p);
	}
	
	public SPlayer(String p) {
		s = p;
		f=TheAPI.getUser(p);
	}

	public User getUser() {
		return f;
	}
	
	public void setHP() {
		if(getPlayer()==null)return;
		getPlayer().setHealth(((Damageable)getPlayer()).getMaxHealth());
	}

	public void heal() {
		if(getPlayer()==null)return;
		setHP();
		setFood();
		setAir();
		setFire();
		for (PotionEffect e : getPlayer().getActivePotionEffects())
			getPlayer().removePotionEffect(e.getType());
	}

	public void setFood() {
		if(getPlayer()==null)return;
		getPlayer().setFoodLevel(20);
	}

	public void setAir() {
		if(getPlayer()==null)return;
		getPlayer().setRemainingAir(getPlayer().getMaximumAir());
	}

	public void enableTempGameMode(long time,GameMode g,boolean t) {
		f.set("TempGamemode.Start",System.currentTimeMillis());
		f.set("TempGamemode.Time",time);
		f.set("TempGamemode.Prev",getPlayer().getGameMode());
		f.save();
		if(getPlayer()==null)return;
		if(!hasGameMode()){
			f.setAndSave("TempGamemode.Use",true);
			getPlayer().setGameMode(g);
			if(t==false){
				Loader.sendMessages(getPlayer(), "GameMode.Temp.You", Placeholder.c().add("%time%", StringUtils.timeToString(time)).add("%gamemode%",g.toString().toLowerCase()));
			}else{
				Loader.sendMessages(getPlayer(), "GameMode.Temp.Reciever", Placeholder.c().add("%time%", StringUtils.timeToString(time)).add("%gamemode%",g.toString().toLowerCase()));
			}
		}
	}

	public boolean hasGameMode(){
		return f.getBoolean("TempGamemode.Use");
	}

	public void enableTempFly(long stop) {
		f.set("TempFly.Start", System.currentTimeMillis());
		f.set("TempFly.Time", stop);
		if (!hasTempFlyEnabled()) {
			f.setAndSave("TempFly.Use", true);
			enableTempFly();
		}else f.save();
		if(getPlayer()==null)return;
		Loader.sendMessages(getPlayer(), "Fly.Temp.Enabled.You", Placeholder.c().add("%time%", StringUtils.setTimeToString(stop)));
	}

	public void enableTempFly() {
		if(getPlayer()==null)return;
		if (hasTempFlyEnabled()) {
			getPlayer().setAllowFlight(true);
			getPlayer().setFlying(true);
		}
	}

	public void enableFly() {
		if (hasTempFlyEnabled()) {
			f.setAndSave("TempFly.Use", false);
		}
		if(!f.getBoolean("Fly"))
		f.setAndSave("Fly", true);
		if(getPlayer()==null)return;
		getPlayer().setAllowFlight(true);
		getPlayer().setFlying(true);
	}

	public void disableFly() {
		f.remove("TempFly");
		f.remove("Fly");
		f.save();
		if(getPlayer()==null)return;
		getPlayer().setFlying(false);
		getPlayer().setAllowFlight(false);
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
		return (Loader.getInstance.isAFK(this) || Loader.getInstance.isManualAfk(this));
	}

	public void setAFK(boolean afk) {
		if(getPlayer()==null)return;
		if (!afk) {
			Loader.getInstance.save(getPlayer());
		} else {
			Loader.getInstance.setAFK(this);
		}
	}
	public void setAFK(boolean afk, String reason) {
		if(getPlayer()==null)return;
		if (!afk) {
			Loader.getInstance.save(getPlayer());
		} else {
			Loader.getInstance.setAFK(this, reason);
		}
	}

	public void setFire() {
		if(getPlayer()==null)return;
		getPlayer().setFireTicks(-20);
	}

	public String getName() {
		return s;
	}

	public String getDisplayName() {
		return getPlayer()!=null?getPlayer().getDisplayName():s;
	}

	public String getCustomName() {
		return getPlayer()!=null?getPlayer().getDisplayName():s;
	}

	public int getFoodLevel() {
		return getPlayer()!=null?getPlayer().getFoodLevel():-1;
	}

	public double getHealth() {
		return getPlayer()!=null?((Damageable)getPlayer()).getHealth():-1;
	}

	public Player getPlayer() {
		return TheAPI.getPlayerOrNull(s);
	}

	public void toggleGod(CommandSender toggler) {
		if(getPlayer()==null)return;
		if (hasGodEnabled()) {
			Loader.sendMessages(getPlayer(), "God.Disabled.Other.Receiver");
			if (toggler != null)
			Loader.sendMessages(toggler, "God.Disabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getPlayer().getDisplayName()).add("%customname%", getPlayer().getCustomName()));
			disableGod();
		} else {
			Loader.sendMessages(getPlayer(), "God.Enabled.Other.Receiver");
			if (toggler != null)
			Loader.sendMessages(toggler, "God.Enabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getPlayer().getDisplayName()).add("%customname%", getPlayer().getCustomName()));
			enableGod();
		}
	}

	public void toggleFly(CommandSender toggler) {
		if(getPlayer()==null)return;
		if (hasFlyEnabled()) {
			Loader.sendMessages(getPlayer(), "Fly.Disabled.Other.Receiver");
			if (toggler != null)
			Loader.sendMessages(toggler, "Fly.Disabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getPlayer().getDisplayName()).add("%customname%", getPlayer().getCustomName()));
			disableFly();
		} else {
			Loader.sendMessages(getPlayer(), "Fly.Enabled.Other.Receiver");
			if (toggler != null)
			Loader.sendMessages(toggler, "Fly.Enabled.Other.Sender", Placeholder.c().add("%player%", getName()).add("%playername%", getPlayer().getDisplayName()).add("%customname%", getPlayer().getCustomName()));
			enableFly();
		}
	}

	public void setWalkSpeed() {
		if(getPlayer()==null)return;
		if (f.exist("WalkSpeed")) {
			Player g = getPlayer();
			if (f.getDouble("WalkSpeed") < 0.0)
				g.setWalkSpeed(0);
			else if (f.getDouble("WalkSpeed") > 10.0)
				g.setWalkSpeed(10);
			else
				g.setWalkSpeed(f.getFloat("WalkSpeed"));
		}
	}

	public void setFlySpeed() {
		if(getPlayer()==null)return;
		if (f.exist("FlySpeed")) {
			Player g = getPlayer();
			if (f.getDouble("FlySpeed") < 0.0)
				g.setFlySpeed(0);
			else if (f.getDouble("FlySpeed") > 10.0)
				g.setFlySpeed(10);
			else
				g.setFlySpeed(f.getFloat("FlySpeed"));
		}
	}

	public void enableGod() {
		if(!f.getBoolean("God"))
		f.setAndSave("God", true);
		setHP();
		setFood();
		setFire();
	}

	public boolean hasPermission(String perm) {
		return hasPerm(perm);
	}

	public boolean hasPerm(String perm) {
		if(getPlayer()==null)return false;
		return getPlayer().hasPermission(perm);
	}

	public void disableGod() {
		f.remove("God");
		f.save();
	}

	public void createEconomyAccount() {
		if (setting.eco_multi && EconomyAPI.getEconomy() != null)
			EconomyAPI.createAccount(s);
	}

	public void setGamamode() {
		if(getPlayer()==null)return;
		if (!getPlayer().hasPermission("SCR.Other.GamemodeChangePrevent")) {
			if (Loader.mw.exists("WorldsSettings." + getPlayer().getWorld().getName() + ".GameMode"))
				try {
				getPlayer().setGameMode(GameMode.valueOf(Loader.mw.getString("WorldsSettings." + getPlayer().getWorld().getName() + ".GameMode").toUpperCase()));
				}catch(Exception | NoSuchFieldError err) {}
			}
	}

	public boolean hasFlyEnabled() {
		return f.getBoolean("Fly")&&(getPlayer()==null||getPlayer()!=null&&getPlayer().getAllowFlight());
	}

	public boolean hasGodEnabled() {
		return f.getBoolean("God");
	}

	public boolean hasTempFlyEnabled() {
		return f.getBoolean("TempFly.Use");
	}
	
	public void addPlayTime(long seconds) {
		GameMode g = player.getGameMode();

		f.set("Statistics.PlayTime", f.getLong("Statistics.PlayTime")+seconds);
		f.set("Statistics."+g.name()+".PlayTime", f.getLong("Statistics."+g.name()+".PlayTime")+seconds);
		f.set("Statistics."+g.name()+"."+player.getWorld().getName()+".PlayTime", f.getLong("Statistics."+g.name()+"."+player.getWorld().getName()+".PlayTime")+seconds);
		f.set("Statistics."+player.getWorld().getName()+".PlayTime", f.getLong("Statistics."+player.getWorld().getName()+".PlayTime")+seconds);
		f.save();
		
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

	public long getPlayTime(String path) {
		if(f.exist("Statistics."+path))
			return f.getLong("Statistics."+path);
		else
			return 0;
	}
	
	
	
	
}
