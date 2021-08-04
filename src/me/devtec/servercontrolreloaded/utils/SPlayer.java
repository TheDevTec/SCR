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
		getUser().set("TempGamemode.Start",System.currentTimeMillis());
		getUser().set("TempGamemode.Time",time);
		getUser().set("TempGamemode.Prev",getPlayer().getGameMode());
		if(getPlayer()==null)return;
		if(!hasGameMode()){
			getUser().set("TempGamemode.Use",true);
			getPlayer().setGameMode(g);
			if(t==false){
				Loader.sendMessages(getPlayer(), "GameMode.Temp.You", Placeholder.c().add("%time%", StringUtils.timeToString(time)).add("%gamemode%",g.toString().toLowerCase()));
			}else{
				Loader.sendMessages(getPlayer(), "GameMode.Temp.Reciever", Placeholder.c().add("%time%", StringUtils.timeToString(time)).add("%gamemode%",g.toString().toLowerCase()));
			}
		}
	}

	public boolean hasGameMode(){
		return getUser().getBoolean("TempGamemode.Use");
	}

	public void enableTempFly(long stop) {
		getUser().set("TempFly.Start", System.currentTimeMillis());
		getUser().set("TempFly.Time", stop);
		if (!hasTempFlyEnabled()) {
			getUser().set("TempFly.Use", true);
			if(getPlayer()==null)return;
			getPlayer().setAllowFlight(true);
			getPlayer().setFlying(true);
		}
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
			getUser().set("TempFly.Use", false);
		}
		if(!getUser().getBoolean("Fly")) {
			getUser().set("Fly", true);
		}
		if(getPlayer()==null)return;
		getPlayer().setAllowFlight(true);
		getPlayer().setFlying(true);
	}

	public void disableFly() {
		getUser().remove("TempFly");
		getUser().remove("Fly");
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
		return (Loader.getInstance.time - afk <=0 || manual);
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
		if(player==null||!player.isOnline())player=TheAPI.getPlayerOrNull(s);
		return player;
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
		if (getUser().exist("WalkSpeed")) {
			Player g = getPlayer();
			if (getUser().getDouble("WalkSpeed") < 0.0)
				g.setWalkSpeed(0);
			else if (getUser().getDouble("WalkSpeed") > 10.0)
				g.setWalkSpeed(10);
			else
				g.setWalkSpeed(getUser().getFloat("WalkSpeed"));
		}
	}

	public void setFlySpeed() {
		if(getPlayer()==null)return;
		if (getUser().exist("FlySpeed")) {
			Player g = getPlayer();
			if (getUser().getDouble("FlySpeed") < 0.0)
				g.setFlySpeed(0);
			else if (getUser().getDouble("FlySpeed") > 10.0)
				g.setFlySpeed(10);
			else
				g.setFlySpeed(getUser().getFloat("FlySpeed"));
		}
	}

	public void enableGod() {
		if(!getUser().getBoolean("God"))
			getUser().set("God", true);
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
		getUser().remove("God");
	}

	public void createEconomyAccount() {
		if (setting.eco_multi && EconomyAPI.getEconomy() != null)
			EconomyAPI.createAccount(s);
	}

	public boolean hasFlyEnabled() {
		return getUser().getBoolean("Fly")||getPlayer()!=null&&getPlayer().getAllowFlight();
	}

	public boolean hasGodEnabled() {
		return getUser().getBoolean("God");
	}

	public boolean hasTempFlyEnabled() {
		return getUser().getBoolean("TempFly.Use");
	}
	
	public void addPlayTime(int seconds) {
		GameMode g = player.getGameMode();
		getUser().set("Statistics.PlayTime", getUser().getInt("Statistics.PlayTime")+seconds);
		getUser().set("Statistics."+g.name()+".PlayTime", getUser().getInt("Statistics."+g.name()+".PlayTime")+seconds);
		getUser().set("Statistics."+g.name()+"."+player.getWorld().getName()+".PlayTime", getUser().getInt("Statistics."+g.name()+"."+player.getWorld().getName()+".PlayTime")+seconds);
		getUser().set("Statistics."+player.getWorld().getName()+".PlayTime", getUser().getInt("Statistics."+player.getWorld().getName()+".PlayTime")+seconds);
		
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
		if(getUser().exist("Statistics."+path))
			return getUser().getInt("Statistics."+path);
		else
			return 0;
	}
	
	
	
	
}
