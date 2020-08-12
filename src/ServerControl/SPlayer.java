package ServerControl;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import Events.AFKPlus;
import Utils.AFK;
import Utils.setting;
import me.DevTec.TheAPI;
import me.DevTec.Other.Ref;
import me.DevTec.Other.User;

public class SPlayer {
	private final String s;
	public int afk=0, kick=0;
	public boolean bc, mp, manual;
	
	public SPlayer(Player p) {
		s = p.getName();
	}

	@SuppressWarnings("deprecation")
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

	public void enableTempFly(int stop) {
		User s = TheAPI.getUser(this.s);
		enableTempFly();
		TheAPI.msg(
				Loader.s("Prefix")
						+ Loader.s("TempFly.Enabled").replace("%time%", TheAPI.getStringUtils().setTimeToString(stop)),
				getPlayer());
		s.set("TempFly.Start", System.currentTimeMillis());
		s.set("TempFly.Time", stop);
		if (!hasTempFlyEnabled())
			s.setAndSave("TempFly.Use", true);
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
			Object user = Ref.invoke(Ref.cast(Ref.getClass("com.earth2me.essentials.Essentials"), TheAPI.getPluginsManagerAPI().getPlugin("Essentials")), Ref.method(Ref.getClass("com.earth2me.essentials.Essentials"), "getUser", Player.class), s);
			if (TheAPI.getPluginsManagerAPI().isEnabledPlugin("Essentials") && user!=null&& (boolean)Ref.invoke(user, "isAfk"))
				return true;
		} catch (Exception er) {
		}
		return (AFK.isAfk(this) || AFK.isManualAfk(this));
	}

	public void msg(String msg) {
		TheAPI.msg(msg, getPlayer());
	}

	public void setAFK(boolean afk) {
		if (!afk) {
			AFK.save(this);
		} else {
			AFK.setAFK(this);
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
			TheAPI.msg(Loader.s("Prefix") + Loader.s("God.Disabled").replace("%player%", getName())
					.replace("%playername%", getPlayer().getDisplayName()), getPlayer());
			if (toggler != null)
				TheAPI.msg(Loader.s("Prefix") + Loader.s("God.SpecifiedPlayerGodDisabled")
						.replace("%player%", getName()).replace("%playername%", getPlayer().getDisplayName()), toggler);
			disableGod();
		} else {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("God.Enabled").replace("%player%", getName())
					.replace("%playername%", getPlayer().getDisplayName()), getPlayer());
			if (toggler != null)
				TheAPI.msg(Loader.s("Prefix") + Loader.s("God.SpecifiedPlayerGodEnabled").replace("%player%", getName())
						.replace("%playername%", getPlayer().getDisplayName()), toggler);
			enableGod();
		}
	}

	public void toggleFly(CommandSender toggler) {
		if (hasFlyEnabled()) {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Disabled").replace("%player%", getName())
					.replace("%playername%",getPlayer().getDisplayName()), getPlayer());
			if (toggler != null)
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.SpecifiedPlayerFlyDisabled")
						.replace("%player%", getName()).replace("%playername%", getPlayer().getDisplayName()), toggler);
			disableFly();
		} else {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Enabled").replace("%player%", getName())
					.replace("%playername%", getPlayer().getDisplayName()), getPlayer());
			if (toggler != null)
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.SpecifiedPlayerFlyEnabled").replace("%player%", getName())
						.replace("%playername%", getPlayer().getDisplayName()), toggler);
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
		TheAPI.getPlayerAPI(getPlayer()).setGod(true);
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
		TheAPI.getPlayerAPI(getPlayer()).setGod(false);
	}

	public void createEconomyAccount() {
		if (setting.eco_multi && TheAPI.getEconomyAPI().getEconomy() != null)
			TheAPI.getEconomyAPI().createAccount(s);
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
		return TheAPI.getPlayerAPI(getPlayer()).allowedFly();
	}

	public boolean hasGodEnabled() {
		return TheAPI.getPlayerAPI(getPlayer()).allowedGod();
	}

	public boolean hasTempFlyEnabled() {
		return TheAPI.getUser(s).getBoolean("TempFly.Use");
	}

	public boolean hasVanish() {
		return TheAPI.isVanished(s);
	}

	public void setVanish(boolean v) {
		TheAPI.vanish(getPlayer(), "ServerControl.Vanish", v);
	}
}
