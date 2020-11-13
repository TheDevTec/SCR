package me.DevTec.ServerControlReloaded.Utils;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.DevTec.ServerControlReloaded.Events.AFKPlus;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.PluginManagerAPI;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.User;
import me.DevTec.TheAPI.Utils.Reflections.Ref;

public class SPlayer {
	private final String s;
	public int afk=0, kick=0;
	public boolean bc, mp, manual;
	
	public SPlayer(Player p) {
		s = p.getName();
	}
	
	public SPlayer(String p) {
		s = p;
	}

	
	public void setHP() {
		getPlayer().setHealth(getPlayer().getMaxHealth());
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

	public boolean isAFK() {
		if (AFKPlus.AFKPlus.containsKey(getName()) && AFKPlus.AFKPlus.get(getName()).isAFK())
			return true;
		try {
			Object user = Ref.invoke(Ref.cast(Ref.getClass("com.earth2me.essentials.Essentials"), PluginManagerAPI.getPlugin("Essentials")), Ref.method(Ref.getClass("com.earth2me.essentials.Essentials"), "getUser", Player.class), s);
			if (PluginManagerAPI.isEnabledPlugin("Essentials") && user!=null&& (boolean)Ref.invoke(user, "isAfk"))
				return true;
		} catch (Exception er) {
		}
		return (Loader.getInstance.isAfk(this) || Loader.getInstance.isManualAfk(this));
	}

	public void msg(String msg) {
		TheAPI.msg(msg, getPlayer());
	}

	public void setAFK(boolean afk) {
		if (!afk) {
			Loader.getInstance.save(this);
		} else {
			Loader.getInstance.setAFK(this);
		}
	}

	public void setFire() {
		getPlayer().setFireTicks(-20);
	}

	public String getName() {
		return s;
	}

	public String getDisplayName() {
		return getPlayer().getDisplayName();
	}

	public String getCustomName() {
		return getPlayer().getDisplayName();
	}

	public int getFoodLevel() {
		return getPlayer().getFoodLevel();
	}

	public double getHealth() {
		return getPlayer().getHealth();
	}

	public Player getPlayer() {
		return TheAPI.getPlayer(s);
	}

	public void toggleGod(CommandSender toggler) {
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
		heal();
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

	public boolean hasVanish() {
		return TheAPI.hasVanish(s);
	}

	public void setVanish(boolean v) {
		TheAPI.setVanish(s, "ServerControl.Vanish", v);
	}
}
