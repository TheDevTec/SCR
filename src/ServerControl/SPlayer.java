package ServerControl;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import Utils.Configs;
import me.Straiker123.PlayerAPI;
import me.Straiker123.TheAPI;

public class SPlayer {
	Player s;
	PlayerAPI d;
	public SPlayer(Player p) {
		s=p;
		d=TheAPI.getPlayerAPI(s);
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
		for(PotionEffect e:s.getActivePotionEffects())s.removePotionEffect(e.getType());
	}
	
	public void setFood() {
		d.setFood(20);
	}
	public void setAir() {
		d.setAir(s.getMaximumAir());
	}
	public void enableTempFly(int stop) {
		enableFly();
        Loader.msg(Loader.s("Prefix")+Loader.s("TempFly.Enabled").replace("%time%", TheAPI.getTimeConventorAPI().setTimeToString(stop)), getPlayer());
        
        Loader.tempfly.put(s.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(Loader.getInstance,new Runnable(){
        	int run = 0;
        	@Override 
            public void run(){
                ++run;
                if(run==stop-15) {
                	d.sendTitle("&615 seconds", "&eTo turn off flying");
                }
                if(run==stop-5) {
                	d.sendTitle("&25 seconds", "&eTo turn off flying");
                }
                if(run==stop-4) {
                	d.sendTitle("&a4 seconds", "&eTo turn off flying");
                }
                if(run==stop-3) {
                	d.sendTitle("&e3 seconds", "&eTo turn off flying");
                }
                if(run==stop-2) {
                	d.sendTitle("&c2 seconds", "&eTo turn off flying");
                }
                if(run==stop-1) {
                	d.sendTitle("&41 second", "&eTo turn off flying");
                }
                if(run>=stop) {
                     disableFly();
                     d.sendTitle("&6Your TempFly", "&ehas ended");
                }
            }}
        ,20,20));
	}
	
	public void enableFly() {
		if(Loader.tempfly.containsKey(getName())) {
        	Bukkit.getScheduler().cancelTask(Loader.tempfly.get(s.getName()));
			Loader.tempfly.remove(getName());
		}
		d.setFly(true, true);
	}
	public void disableFly() {
		if(Loader.tempfly.containsKey(getName())) {
        	Bukkit.getScheduler().cancelTask(Loader.tempfly.get(s.getName()));
			Loader.tempfly.remove(getName());
		}
		d.setFly(false, false);
	}
	public boolean isAFK() {
		return Utils.AFK.isAFK(s);
	}
	
	public void setAFK(boolean afk) {
		 if(!afk) {
				Loader.getInstance.afk(s, true);
				Utils.AFK.save(s);
				Loader.me.set("Players."+getName()+".AFK-Manual",null);
	   		 	Loader.me.set("Players."+getName()+".AFK-Broadcast", null);
	   			Configs.chatme.save();
				}else {
				Loader.getInstance.afk(s, false);
				Utils.AFK.save(s);
	   		 	Loader.me.set("Players."+getName()+".AFK-Broadcast", true);
				Loader.me.set("Players."+getName()+".AFK-Manual",true);
				Configs.chatme.save();
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
		if(hasGodEnabled()) {
			Loader.msg(Loader.s("Prefix")+Loader.s("God.Disabled").replace("%player%", getName()).replace("%playername%", s.getDisplayName()), s);
			if(toggler!=null)
				Loader.msg(Loader.s("Prefix")+Loader.s("God.SpecifiedPlayerGodDisabled").replace("%player%",getName()).replace("%playername%", s.getDisplayName()), toggler);
			disableGod();
		}else {
			Loader.msg(Loader.s("Prefix")+Loader.s("God.Enabled").replace("%player%", getName()).replace("%playername%", s.getDisplayName()), s);
			if(toggler!=null)
				Loader.msg(Loader.s("Prefix")+Loader.s("God.SpecifiedPlayerGodEnabled").replace("%player%", getName()).replace("%playername%", s.getDisplayName()), toggler);
			enableGod();
		}
		}

	public void toggleFly(CommandSender toggler) {
		if(hasFlyEnabled()) {
			Loader.msg(Loader.s("Prefix")+Loader.s("Fly.Disabled").replace("%player%", getName()).replace("%playername%", s.getDisplayName()), s);
			if(toggler!=null)
				Loader.msg(Loader.s("Prefix")+Loader.s("Fly.SpecifiedPlayerFlyDisabled").replace("%player%",getName()).replace("%playername%", s.getDisplayName()), toggler);
			disableFly();
		}else {
			Loader.msg(Loader.s("Prefix")+Loader.s("Fly.Enabled").replace("%player%", getName()).replace("%playername%", s.getDisplayName()), s);
			if(toggler!=null)
				Loader.msg(Loader.s("Prefix")+Loader.s("Fly.SpecifiedPlayerFlyEnabled").replace("%player%", getName()).replace("%playername%", s.getDisplayName()), toggler);
			enableFly();
		}
		}
	
	public void setWalkSpeed() {
		if(s.hasPermission("ServerControl.WalkSpeed")&&Loader.me.getString("Players."+s.getName()+".WalkSpeed")!=null) {
			if(Loader.me.getDouble("Players."+s.getName()+".WalkSpeed")==0.0)s.setWalkSpeed(2);
			else
			if(Loader.me.getDouble("Players."+s.getName()+".WalkSpeed")>10.0)s.setWalkSpeed(10);
			else
			if(Loader.me.getDouble("Players."+s.getName()+".WalkSpeed")<10.0)s.setWalkSpeed((float)Loader.me.getDouble("Players."+s.getName()+".WalkSpeed"));
		}
	}
	public void setFlySpeed(){
		if(s.hasPermission("ServerControl.WalkSpeed")&&Loader.me.getString("Players."+s.getName()+".WalkSpeed")!=null) {
			if(Loader.me.getDouble("Players."+s.getName()+".WalkSpeed")==0.0)s.setWalkSpeed(2);
			else
			if(Loader.me.getDouble("Players."+s.getName()+".WalkSpeed")>10.0)s.setWalkSpeed(10);
			else
			if(Loader.me.getDouble("Players."+s.getName()+".WalkSpeed")<10.0)s.setWalkSpeed((float)Loader.me.getDouble("Players."+s.getName()+".WalkSpeed"));
		}
	}
	public void enableGod() {
		d.setGod(true);
		heal();
	}
	public boolean hasPermission(String perm) {
		return s.hasPermission(perm);
	}
	
	public void disableGod() {
		d.setGod(false);
	}
	
	public void createEconomyAccount() {
		if(Loader.config.getBoolean("MultiEconomy.Enabled") && Loader.econ != null)
			Loader.econ.createPlayerAccount(s);
	}
	
	public void setGamamode() {
		if(!s.hasPermission("ServerControl.GamemodeChangePrevent")) {
			if(Loader.mw.getString("WorldsSettings."+s.getWorld().getName()+".GameMode")!=null &&GameMode.valueOf(Loader.mw.getString("WorldsSettings."+s.getWorld().getName()+".GameMode"))!=null)
				s.setGameMode(GameMode.valueOf(Loader.mw.getString("WorldsSettings."+s.getWorld().getName()+".GameMode")));
			}
	}

	public boolean hasFlyEnabled() {
		return d.allowedFly();
	}

	public boolean hasGodEnabled() {
		return d.allowedGod();
	}
}
