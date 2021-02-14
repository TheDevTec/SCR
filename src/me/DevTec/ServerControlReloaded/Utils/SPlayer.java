package me.DevTec.ServerControlReloaded.Utils;

import java.lang.reflect.Method;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.reflections.Ref;

public class SPlayer {
	public boolean lock;
	private final String s;
	public int kick, afk;
	public boolean bc, mp, manual;
	public String reply, type;
	public Location l;
	
	public SPlayer(Player p) {
		s = p.getName();
	}
	
	public SPlayer(String p) {
		s = p;
	}

	
	public void setHP() {
		getPlayer().setHealth(((Damageable)getPlayer()).getMaxHealth());
	}

	public void heal() {
		setHP();
		setFood();
		setAir();
		setFire();
		for (PotionEffect e : getPlayer().getActivePotionEffects())
			getPlayer().removePotionEffect(e.getType());
	}

	public void setFood() {
		getPlayer().setFoodLevel(20);
	}

	public void setAir() {
		getPlayer().setRemainingAir(getPlayer().getMaximumAir());
	}

	public void enableTempFly(long stop) {
		User s = TheAPI.getUser(this.s);
		Loader.sendMessages(getPlayer(), "Fly.Temp.Enabled.You", Placeholder.c().add("%time%", StringUtils.setTimeToString(stop)));
		s.set("TempFly.Start", System.currentTimeMillis());
		s.set("TempFly.Time", stop);
		if (!hasTempFlyEnabled())
			s.setAndSave("TempFly.Use", true);
			enableTempFly();
	}

	public void enableTempFly() {
		if (hasTempFlyEnabled()) {
			getPlayer().setAllowFlight(true);
			getPlayer().setFlying(true);
		}
	}

	public void enableFly() {
		if (hasTempFlyEnabled()) {
			TheAPI.getUser(s).setAndSave("TempFly.Use", false);
		}
		getPlayer().setAllowFlight(true);
		getPlayer().setFlying(true);
		TheAPI.getUser(s).setAndSave("Fly", true);
	}

	public void disableFly() {
		if (hasTempFlyEnabled()) {
			TheAPI.getUser(s).setAndSave("TempFly.Use", false);
		}
		getPlayer().setFlying(false);
		getPlayer().setAllowFlight(false);
		TheAPI.getUser(s).setAndSave("Fly", false);
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
		if (!afk) {
			Loader.getInstance.save(getPlayer());
		} else {
			Loader.getInstance.setAFK(this);
		}
	}
	public void setAFK(boolean afk, String reason) {
		if (!afk) {
			Loader.getInstance.save(getPlayer());
		} else {
			Loader.getInstance.setAFK(this, reason);
		}
	}

	public void setFire() {
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
		return getPlayer()!=null?((Damageable)getPlayer()).getHealth() : -1;
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
		User d = TheAPI.getUser(this.s);
		if (getPlayer().hasPermission("ServerControl.WalkSpeed") && d.exist("WalkSpeed")) {
			if (d.getDouble("WalkSpeed") == 0.0)
				getPlayer().setWalkSpeed(2);
			else if (d.getDouble("WalkSpeed") > 10.0)
				getPlayer().setWalkSpeed(10);
			else if (d.getDouble("WalkSpeed") < 10.0)
				getPlayer().setWalkSpeed((float) d.getDouble("WalkSpeed"));
		}
	}

	public void setFlySpeed() {
		if(getPlayer()==null)return;
		User d = TheAPI.getUser(this.s);
		if (getPlayer().hasPermission("ServerControl.FlySpeed") && d.exist("FlySpeed")) {
			if (d.getDouble("FlySpeed") == 0.0)
				getPlayer().setWalkSpeed(2);
			else if (d.getDouble("FlySpeed") > 10.0)
				getPlayer().setWalkSpeed(10);
			else if (d.getDouble("FlySpeed") < 10.0)
				getPlayer().setWalkSpeed((float) d.getDouble("FlySpeed"));
		}
	}

	public void enableGod() {
		TheAPI.getUser(s).setAndSave("God", true);
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
		TheAPI.getUser(s).setAndSave("God", false);
	}

	public void createEconomyAccount() {
		if (setting.eco_multi && EconomyAPI.getEconomy() != null)
			EconomyAPI.createAccount(s);
	}

	public void setGamamode() {
		if (!getPlayer().hasPermission("ServerControl.GamemodeChangePrevent")) {
			if (Loader.mw.getString("WorldsSettings." + getPlayer().getWorld().getName() + ".GameMode") != null && GameMode
					.valueOf(Loader.mw.getString("WorldsSettings." + getPlayer().getWorld().getName() + ".GameMode")) != null)
				getPlayer().setGameMode(GameMode
						.valueOf(Loader.mw.getString("WorldsSettings." + getPlayer().getWorld().getName() + ".GameMode")));
		}
	}

	public boolean hasFlyEnabled() {
		return TheAPI.getUser(s).getBoolean("Fly")||getPlayer()!=null&&getPlayer().isFlying();
	}

	public boolean hasGodEnabled() {
		return TheAPI.getUser(s).getBoolean("God");
	}

	public boolean hasTempFlyEnabled() {
		return TheAPI.getUser(s).getBoolean("TempFly.Use");
	}
}
