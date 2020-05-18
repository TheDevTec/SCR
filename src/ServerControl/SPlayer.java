package ServerControl;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.earth2me.essentials.Essentials;

import Events.AFKPlus;
import Utils.setting;
import me.Straiker123.LoaderClass;
import me.Straiker123.PlayerAPI;
import me.Straiker123.TheAPI;
import me.Straiker123.User;

public class SPlayer {
	Player s;
	PlayerAPI d;

	public SPlayer(Player p) {
		s = p;
		d = TheAPI.getPlayerAPI(p);
	}

	@SuppressWarnings("deprecation")
	public void setHP() {
		d.setHealth(s.getMaxHealth());
	}

	public void heal() {
		setHP();
		setFood();
		setAir();
		setFire();
		for (PotionEffect e : s.getActivePotionEffects())
			s.removePotionEffect(e.getType());
	}

	public void setFood() {
		d.setFood(20);
	}

	public void setAir() {
		d.setAir(s.getMaximumAir());
	}

	public void enableTempFly(int stop) {
		User s = TheAPI.getUser(this.s);
		enableTempFly();
		Loader.msg(
				Loader.s("Prefix")
						+ Loader.s("TempFly.Enabled").replace("%time%", TheAPI.getStringUtils().setTimeToString(stop)),
				getPlayer());
		s.set("TempFly.Start", System.currentTimeMillis());
		s.set("TempFly.Time", stop);
		if (!hasTempFlyEnabled()) {
			List<String> w = LoaderClass.data.getStringList("TempFly");
			w.add(getName());
			LoaderClass.data.set("TempFly", w);
			LoaderClass.data.save();
		}

	}

	public void enableTempFly() {
		d.setFly(true, true);
	}

	public void enableFly() {
		if (hasTempFlyEnabled()) {
			List<String> w = LoaderClass.data.getStringList("TempFly");
			w.remove(getName());
			LoaderClass.data.set("TempFly", w);
			LoaderClass.data.save();
		}
		d.setFly(true, true);
	}

	public void disableFly() {
		if (hasTempFlyEnabled()) {
			List<String> w = LoaderClass.data.getStringList("TempFly");
			w.remove(getName());
			LoaderClass.data.set("TempFly", w);
			LoaderClass.data.save();
		}
		d.setFly(false, false);
	}

	public boolean isAFK() {
		try {
			if (TheAPI.getPluginsManagerAPI().isEnabledPlugin("AFKPlus") && AFKPlus.AFKPlus.get(s).isAFK())
				return true;
		} catch (Exception er) {
		}
		try {
			if (TheAPI.getPluginsManagerAPI().isEnabledPlugin("Essentials")
					&& ((Essentials) TheAPI.getPluginsManagerAPI().getPlugin("Essentials")).getUser(s) != null
					&& ((Essentials) TheAPI.getPluginsManagerAPI().getPlugin("Essentials")).getUser(s).isAfk())
				return true;
		} catch (Exception er) {
		}
		return Loader.afk.containsKey(getName())
				? (Loader.afk.get(getName()).isAfk() || Loader.afk.get(getName()).isManualAfk())
				: false;
	}

	public void msg(String msg) {
		TheAPI.msg(msg, s);
	}

	public void setAFK(boolean afk) {
		if (!afk) {
			Loader.afk.get(s.getName()).save();
		} else {
			Loader.afk.get(s.getName()).setAFK();
		}
	}

	public void setFire() {
		s.setFireTicks(-20);
	}

	public String getName() {
		return s.getName();
	}

	public String getDisplayName() {
		return s.getDisplayName();
	}

	public String getCustomName() {
		return s.getDisplayName();
	}

	public int getFoodLevel() {
		return s.getFoodLevel();
	}

	public double getHealth() {
		return s.getHealth();
	}

	public Player getPlayer() {
		return s;
	}

	public void toggleGod(CommandSender toggler) {
		if (hasGodEnabled()) {
			Loader.msg(Loader.s("Prefix") + Loader.s("God.Disabled").replace("%player%", getName())
					.replace("%playername%", s.getDisplayName()), s);
			if (toggler != null)
				Loader.msg(Loader.s("Prefix") + Loader.s("God.SpecifiedPlayerGodDisabled")
						.replace("%player%", getName()).replace("%playername%", s.getDisplayName()), toggler);
			disableGod();
		} else {
			Loader.msg(Loader.s("Prefix") + Loader.s("God.Enabled").replace("%player%", getName())
					.replace("%playername%", s.getDisplayName()), s);
			if (toggler != null)
				Loader.msg(Loader.s("Prefix") + Loader.s("God.SpecifiedPlayerGodEnabled").replace("%player%", getName())
						.replace("%playername%", s.getDisplayName()), toggler);
			enableGod();
		}
	}

	public void toggleFly(CommandSender toggler) {
		if (hasFlyEnabled()) {
			Loader.msg(Loader.s("Prefix") + Loader.s("Fly.Disabled").replace("%player%", getName())
					.replace("%playername%", s.getDisplayName()), s);
			if (toggler != null)
				Loader.msg(Loader.s("Prefix") + Loader.s("Fly.SpecifiedPlayerFlyDisabled")
						.replace("%player%", getName()).replace("%playername%", s.getDisplayName()), toggler);
			disableFly();
		} else {
			Loader.msg(Loader.s("Prefix") + Loader.s("Fly.Enabled").replace("%player%", getName())
					.replace("%playername%", s.getDisplayName()), s);
			if (toggler != null)
				Loader.msg(Loader.s("Prefix") + Loader.s("Fly.SpecifiedPlayerFlyEnabled").replace("%player%", getName())
						.replace("%playername%", s.getDisplayName()), toggler);
			enableFly();
		}
	}

	public void setWalkSpeed() {
		User d = TheAPI.getUser(this.s);
		if (s.hasPermission("ServerControl.WalkSpeed") && d.exist("WalkSpeed")) {
			if (d.getDouble("WalkSpeed") == 0.0)
				s.setWalkSpeed(2);
			else if (d.getDouble("WalkSpeed") > 10.0)
				s.setWalkSpeed(10);
			else if (d.getDouble("WalkSpeed") < 10.0)
				s.setWalkSpeed((float) d.getDouble("WalkSpeed"));
		}
	}

	public void setFlySpeed() {
		User d = TheAPI.getUser(this.s);
		if (s.hasPermission("ServerControl.FlySpeed") && d.exist("FlySpeed")) {
			if (d.getDouble("FlySpeed") == 0.0)
				s.setWalkSpeed(2);
			else if (d.getDouble("FlySpeed") > 10.0)
				s.setWalkSpeed(10);
			else if (d.getDouble("FlySpeed") < 10.0)
				s.setWalkSpeed((float) d.getDouble("FlySpeed"));
		}
	}

	public void enableGod() {
		d.setGod(true);
		heal();
	}

	public boolean hasPermission(String perm) {
		return s.hasPermission(perm);
	}

	public boolean hasPerm(String perm) {
		return s.hasPermission(perm);
	}

	public void disableGod() {
		d.setGod(false);
	}

	public void createEconomyAccount() {
		if (setting.eco_multi && TheAPI.getEconomyAPI().getEconomy() != null)
			TheAPI.getEconomyAPI().createAccount(s);
	}

	public void setGamamode() {
		if (!s.hasPermission("ServerControl.GamemodeChangePrevent")) {
			if (Loader.mw.getString("WorldsSettings." + s.getWorld().getName() + ".GameMode") != null && GameMode
					.valueOf(Loader.mw.getString("WorldsSettings." + s.getWorld().getName() + ".GameMode")) != null)
				s.setGameMode(GameMode
						.valueOf(Loader.mw.getString("WorldsSettings." + s.getWorld().getName() + ".GameMode")));
		}
	}

	public boolean hasFlyEnabled() {
		return d.allowedFly();
	}

	public boolean hasGodEnabled() {
		return d.allowedGod();
	}

	public boolean hasTempFlyEnabled() {
		return LoaderClass.data.existPath("TempFly") ? LoaderClass.data.getStringList("TempFly").contains(getName())
				: false;
	}

	public boolean hasVanish() {
		return TheAPI.isVanished(s);
	}

	public void setVanish(boolean v) {
		TheAPI.vanish(s, "ServerControl.Vanish", v);
	}
}
