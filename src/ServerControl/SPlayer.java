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
		TheAPI.getPlayer(s).setHealth(TheAPI.getPlayer(s).getMaxHealth());
	}

	public void heal() {
		setHP();
		setFood();
		setAir();
		setFire();
		for (PotionEffect e : TheAPI.getPlayer(s).getActivePotionEffects())
			TheAPI.getPlayer(s).removePotionEffect(e.getType());
	}

	public void setFood() {
		TheAPI.getPlayer(s).setFoodLevel(20);
	}

	public void setAir() {
		TheAPI.getPlayer(s).setRemainingAir(TheAPI.getPlayer(s).getMaximumAir());
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
			TheAPI.getPlayer(s).setAllowFlight(true);
			TheAPI.getPlayer(s).setFlying(true);
		}
	}

	public void enableFly() {
		if (hasTempFlyEnabled()) {
			TheAPI.getUser(s).setAndSave("TempFly.Use", false);
		}
		TheAPI.getPlayer(s).setAllowFlight(true);
		TheAPI.getPlayer(s).setFlying(true);
		TheAPI.getUser(s).setAndSave("Fly", true);
	}

	public void disableFly() {
		if (hasTempFlyEnabled()) {
			TheAPI.getUser(s).setAndSave("TempFly.Use", false);
		}
		TheAPI.getPlayer(s).setFlying(false);
		TheAPI.getPlayer(s).setAllowFlight(false);
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
		TheAPI.msg(msg, TheAPI.getPlayer(s));
	}

	public void setAFK(boolean afk) {
		if (!afk) {
			AFK.save(this);
		} else {
			AFK.setAFK(this);
		}
	}

	public void setFire() {
		TheAPI.getPlayer(s).setFireTicks(-20);
	}

	public String getName() {
		return s;
	}

	public String getDisplayName() {
		return TheAPI.getPlayer(s).getDisplayName();
	}

	public String getCustomName() {
		return TheAPI.getPlayer(s).getDisplayName();
	}

	public int getFoodLevel() {
		return TheAPI.getPlayer(s).getFoodLevel();
	}

	public double getHealth() {
		return TheAPI.getPlayer(s).getHealth();
	}

	public Player getPlayer() {
		return TheAPI.getPlayer(s);
	}

	public void toggleGod(CommandSender toggler) {
		if (hasGodEnabled()) {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("God.Disabled").replace("%player%", getName())
					.replace("%playername%", TheAPI.getPlayer(s).getDisplayName()), TheAPI.getPlayer(s));
			if (toggler != null)
				TheAPI.msg(Loader.s("Prefix") + Loader.s("God.SpecifiedPlayerGodDisabled")
						.replace("%player%", getName()).replace("%playername%", TheAPI.getPlayer(s).getDisplayName()), toggler);
			disableGod();
		} else {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("God.Enabled").replace("%player%", getName())
					.replace("%playername%", TheAPI.getPlayer(s).getDisplayName()), TheAPI.getPlayer(s));
			if (toggler != null)
				TheAPI.msg(Loader.s("Prefix") + Loader.s("God.SpecifiedPlayerGodEnabled").replace("%player%", getName())
						.replace("%playername%", TheAPI.getPlayer(s).getDisplayName()), toggler);
			enableGod();
		}
	}

	public void toggleFly(CommandSender toggler) {
		if (hasFlyEnabled()) {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Disabled").replace("%player%", getName())
					.replace("%playername%",TheAPI.getPlayer(s).getDisplayName()), TheAPI.getPlayer(s));
			if (toggler != null)
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.SpecifiedPlayerFlyDisabled")
						.replace("%player%", getName()).replace("%playername%", TheAPI.getPlayer(s).getDisplayName()), toggler);
			disableFly();
		} else {
			TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.Enabled").replace("%player%", getName())
					.replace("%playername%", TheAPI.getPlayer(s).getDisplayName()), TheAPI.getPlayer(s));
			if (toggler != null)
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Fly.SpecifiedPlayerFlyEnabled").replace("%player%", getName())
						.replace("%playername%", TheAPI.getPlayer(s).getDisplayName()), toggler);
			enableFly();
		}
	}

	public void setWalkSpeed() {
		User d = TheAPI.getUser(this.s);
		if (TheAPI.getPlayer(s).hasPermission("ServerControl.WalkSpeed") && d.exist("WalkSpeed")) {
			if (d.getDouble("WalkSpeed") == 0.0)
				TheAPI.getPlayer(s).setWalkSpeed(2);
			else if (d.getDouble("WalkSpeed") > 10.0)
				TheAPI.getPlayer(s).setWalkSpeed(10);
			else if (d.getDouble("WalkSpeed") < 10.0)
				TheAPI.getPlayer(s).setWalkSpeed((float) d.getDouble("WalkSpeed"));
		}
	}

	public void setFlySpeed() {
		User d = TheAPI.getUser(this.s);
		if (TheAPI.getPlayer(s).hasPermission("ServerControl.FlySpeed") && d.exist("FlySpeed")) {
			if (d.getDouble("FlySpeed") == 0.0)
				TheAPI.getPlayer(s).setWalkSpeed(2);
			else if (d.getDouble("FlySpeed") > 10.0)
				TheAPI.getPlayer(s).setWalkSpeed(10);
			else if (d.getDouble("FlySpeed") < 10.0)
				TheAPI.getPlayer(s).setWalkSpeed((float) d.getDouble("FlySpeed"));
		}
	}

	public void enableGod() {
		TheAPI.getPlayerAPI(TheAPI.getPlayer(s)).setGod(true);
		heal();
	}

	public boolean hasPermission(String perm) {
		return TheAPI.getPlayer(s).hasPermission(perm);
	}

	public boolean hasPerm(String perm) {
		return TheAPI.getPlayer(s).hasPermission(perm);
	}

	public void disableGod() {
		TheAPI.getPlayerAPI(TheAPI.getPlayer(s)).setGod(false);
	}

	public void createEconomyAccount() {
		if (setting.eco_multi && TheAPI.getEconomyAPI().getEconomy() != null)
			TheAPI.getEconomyAPI().createAccount(s);
	}

	public void setGamamode() {
		if (!TheAPI.getPlayer(s).hasPermission("ServerControl.GamemodeChangePrevent")) {
			if (Loader.mw.getString("WorldsSettings." + TheAPI.getPlayer(s).getWorld().getName() + ".GameMode") != null && GameMode
					.valueOf(Loader.mw.getString("WorldsSettings." + TheAPI.getPlayer(s).getWorld().getName() + ".GameMode")) != null)
				TheAPI.getPlayer(s).setGameMode(GameMode
						.valueOf(Loader.mw.getString("WorldsSettings." + TheAPI.getPlayer(s).getWorld().getName() + ".GameMode")));
		}
	}

	public boolean hasFlyEnabled() {
		return TheAPI.getPlayerAPI(TheAPI.getPlayer(s)).allowedFly();
	}

	public boolean hasGodEnabled() {
		return TheAPI.getPlayerAPI(TheAPI.getPlayer(s)).allowedGod();
	}

	public boolean hasTempFlyEnabled() {
		return TheAPI.getUser(s).getBoolean("TempFly.Use");
	}

	public boolean hasVanish() {
		return TheAPI.isVanished(s);
	}

	public void setVanish(boolean v) {
		TheAPI.vanish(TheAPI.getPlayer(s), "ServerControl.Vanish", v);
	}
}
