package me.devtec.servercontrolreloaded.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.bossbar.BarColor;
import me.devtec.theapi.bossbar.BarStyle;
import me.devtec.theapi.bossbar.BossBar;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.scoreboardapi.ScoreboardAPI;
import me.devtec.theapi.scoreboardapi.SimpleScore;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.reflections.Ref;

public class DisplayManager {
	@SuppressWarnings("unchecked")
	private static Map<String, ScoreboardAPI> map = (Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores");
	
	public static enum DisplayType {
		ACTIONBAR,
		BOSSBAR,
		SCOREBOARD
	}

	public static void initializePlayer(Player p) {
		for(DisplayType t : DisplayType.values()) {
			if(TheAPI.getUser(p).getBoolean("SCR."+t.name()) && !ignore.get(t).contains(p.getName()))
				ignore.get(t).add(p.getName());
		}
	}

	public static void removeCache(Player p) {
		for(DisplayType t : DisplayType.values()) {
			ignore.get(t).remove(p.getName());
			hide.get(t).remove(p.getName());
		}
		TheAPI.sendActionBar(p, "");
		if(TheAPI.getBossBar(p)!=null)
		TheAPI.removeBossBar(p);
		if(map.containsKey(p.getName()))
		map.remove(p.getName()).destroy();
	}
	
	public static void show(Player p, DisplayType type) {
		TheAPI.getUser(p).setAndSave("SCR."+type.name(), false);
		ignore.get(type).remove(p.getName());
		hide.get(type).remove(p.getName());
		switch(type) {
			case ACTIONBAR:{
				Loader.sendMessages(p, "DisplayManager.ActionBar.Show");
			}
			break;
			case BOSSBAR:{
				Loader.sendMessages(p, "DisplayManager.BossBar.Show");
			}
			break;
			case SCOREBOARD:{
				Loader.sendMessages(p, "DisplayManager.Scoreboard.Show");
			}
			break;
		}
	}
	
	private static SimpleScore score = new SimpleScore();
	protected static AnimationManager ac = new AnimationManager(), bb= new AnimationManager(), sb=new AnimationManager();
	
	public static void hide(Player s, DisplayType type) {
		TheAPI.getUser(s).setAndSave("SCR."+type.name(), true);
		if(!ignore.get(type).contains(s.getName()))
			ignore.get(type).add(s.getName());
		switch(type) {
			case ACTIONBAR:{
				Loader.sendMessages(s, "DisplayManager.ActionBar.Hide");
				if(!isToggleable(s, DisplayType.ACTIONBAR)) {
					return;
				}
				hide.get(DisplayType.ACTIONBAR).add(s.getName());
				TheAPI.sendActionBar(s, ""); //remove
			}
			break;
			case BOSSBAR:{
				Loader.sendMessages(s, "DisplayManager.BossBar.Hide");
				if(!isToggleable(s, DisplayType.BOSSBAR)) {
					return;
				}
				hide.get(DisplayType.BOSSBAR).add(s.getName());
				TheAPI.removeBossBar(s); //remove
			}
			break;
			case SCOREBOARD:{
				Loader.sendMessages(s, "DisplayManager.Scoreboard.Hide");
				if(!isToggleable(s, DisplayType.SCOREBOARD)) {
					return;
				}
				hide.get(DisplayType.SCOREBOARD).add(s.getName());
				if(map.containsKey(s.getName()))
					map.remove(s.getName()).destroy();
			}
			break;
		}
	}
	
	public static boolean has(Player p, DisplayType type) {
		return TheAPI.getUser(p).exists("SCR."+type.name())?TheAPI.getUser(p).getBoolean("SCR."+type.name()):true;
	}

	public static boolean isToggleable(Player s, DisplayType type) {
		switch(type) {
			case ACTIONBAR:
				return Loader.ac.exists("PerPlayer."+s.getName())?Loader.ac.getBoolean("PerPlayer."+s.getName()+".Toggleable"):(Loader.ac.exists("PerWorld."+s.getWorld().getName())?Loader.ac.getBoolean("PerWorld."+s.getWorld().getName()+".Toggleable"):Loader.ac.getBoolean("Toggleable"));
			case BOSSBAR:
				return Loader.bb.exists("PerPlayer."+s.getName())?Loader.bb.getBoolean("PerPlayer."+s.getName()+".Toggleable"):(Loader.bb.exists("PerWorld."+s.getWorld().getName())?Loader.bb.getBoolean("PerWorld."+s.getWorld().getName()+".Toggleable"):Loader.bb.getBoolean("Toggleable"));
			case SCOREBOARD:
				return Loader.sb.exists("PerPlayer."+s.getName())?Loader.sb.getBoolean("PerPlayer."+s.getName()+".Toggleable"):(Loader.sb.exists("PerWorld."+s.getWorld().getName())?Loader.sb.getBoolean("PerWorld."+s.getWorld().getName()+".Toggleable"):Loader.sb.getBoolean("Options.Toggleable"));
		}
		return true;
	}

	public static boolean hasToggled(Player s, DisplayType type) {
		return TheAPI.getUser(s).getBoolean("SCR."+type.name());
	}
	
	private static Set<Integer> tasks = new HashSet<>();
	private static Map<DisplayType, Set<String>> ignore = new HashMap<>(), hide = new HashMap<>();
	static {
		for(DisplayType t : DisplayType.values()) {
			ignore.put(t, new HashSet<>());
			hide.put(t, new HashSet<>());
		}
	}
	
	public static void load() {
		for(Player s : TheAPI.getOnlinePlayers())
			initializePlayer(s);
		if(Loader.ac.getBoolean("Enabled")) {
		tasks.add(new Tasker() {
			public void run() {
				for(Player s : TheAPI.getOnlinePlayers()) {
					try {
					if(!s.hasPermission(Loader.ac.getString("Permission"))) {
						if(!hide.get(DisplayType.ACTIONBAR).contains(s.getName())) {
							hide.get(DisplayType.ACTIONBAR).add(s.getName());
							TheAPI.sendActionBar(s, "");
						}
						continue;
					}
					if(Loader.ac.getStringList("ForbiddenWorlds").contains(s.getWorld().getName())) {
						if(!hide.get(DisplayType.ACTIONBAR).contains(s.getName())) {
							hide.get(DisplayType.ACTIONBAR).add(s.getName());
							TheAPI.sendActionBar(s, "");
							continue;
						}
						continue;
					}
					if(ignore.get(DisplayType.ACTIONBAR).contains(s.getName())) {
						if(!hide.get(DisplayType.ACTIONBAR).contains(s.getName())) {
							if(!isToggleable(s, DisplayType.ACTIONBAR)) {
								String text = "Text";
								if(Loader.ac.exists("PerPlayer."+s.getName())) {
									text="PerPlayer."+s.getName()+".Text";
								}else {
									if(Loader.ac.exists("PerWorld."+s.getWorld().getName())) {
										text="PerWorld."+s.getWorld().getName()+".Text";
									}
								}
								TheAPI.sendActionBar(s, ac.replace(s, Loader.ac.getString(text)));
								continue;
							}
							hide.get(DisplayType.ACTIONBAR).add(s.getName());
							TheAPI.sendActionBar(s, ""); //remove
							continue;
						}else {
							if(!isToggleable(s, DisplayType.ACTIONBAR)) {
								hide.get(DisplayType.ACTIONBAR).remove(s.getName());
								String text = "Text";
								if(Loader.ac.exists("PerPlayer."+s.getName())) {
									text="PerPlayer."+s.getName()+".Text";
								}else {
									if(Loader.ac.exists("PerWorld."+s.getWorld().getName())) {
										text="PerWorld."+s.getWorld().getName()+".Text";
									}
								}
								TheAPI.sendActionBar(s, ac.replace(s, Loader.ac.getString(text)));
								continue;
							}
							//already gone
							continue;
						}
					}else {
						hide.get(DisplayType.ACTIONBAR).remove(s.getName());
						String text = "Text";
						if(Loader.ac.exists("PerPlayer."+s.getName())) {
							text="PerPlayer."+s.getName()+".Text";
						}else {
							if(Loader.ac.exists("PerWorld."+s.getWorld().getName())) {
								text="PerWorld."+s.getWorld().getName()+".Text";
							}
						}
						TheAPI.sendActionBar(s, ac.replace(s, Loader.ac.getString(text)));
						continue;
					}
					}catch(Exception er) {}
				}
				ac.update();
			}
		}.runRepeating(0, (long) StringUtils.calculate(Loader.ac.getString("RefleshTick"))));
		}
		if(Loader.bb.getBoolean("Enabled"))
		tasks.add(new Tasker() {
			int cc = 0;
			public void run() {
				for(Player s : TheAPI.getOnlinePlayers()) {
					try {
					if(!s.hasPermission(Loader.bb.getString("Permission"))) {
						if(!hide.get(DisplayType.BOSSBAR).contains(s.getName())) {
							hide.get(DisplayType.BOSSBAR).add(s.getName());
							TheAPI.removeBossBar(s);
						}
						continue;
					}	
					if(Loader.bb.getStringList("ForbiddenWorlds").contains(s.getWorld().getName())) {
						if(!hide.get(DisplayType.BOSSBAR).contains(s.getName())) {
							hide.get(DisplayType.BOSSBAR).add(s.getName());
							TheAPI.removeBossBar(s);
							continue;
						}
						continue;
					}
					if(ignore.get(DisplayType.BOSSBAR).contains(s.getName())) {
						if(!hide.get(DisplayType.BOSSBAR).contains(s.getName())) {
							if(!isToggleable(s, DisplayType.BOSSBAR)) {
								String text = "Text";
								String stage = "Stage";
								String style = "Style";
								String color = "Color";
								if(Loader.bb.exists("PerPlayer."+s.getName())) {
									text="PerPlayer."+s.getName()+".Text";
									stage="PerPlayer."+s.getName()+".Stage";
									style="PerPlayer."+s.getName()+".Style";
									color="PerPlayer."+s.getName()+".Color";
								}else {
									if(Loader.bb.exists("PerWorld."+s.getWorld().getName())) {
										text="PerWorld."+s.getWorld().getName()+".Text";
										stage="PerWorld."+s.getWorld().getName()+".Stage";
										style="PerWorld."+s.getWorld().getName()+".Style";
										color="PerWorld."+s.getWorld().getName()+".Color";
									}
								}
								BossBar b = TheAPI.sendBossBar(s, bb.replace(s, Loader.bb.getString(text)), (double)StringUtils.calculate(PlaceholderAPI.setPlaceholders(s, Loader.bb.getString(stage)))/100);
								if(Loader.bb.getString(color)!=null)
									try {
										if(Loader.bb.getString(color).toUpperCase().equals("RANDOM")) {
											b.setColor(BarColor.values()[cc]);
										}else
										b.setColor(BarColor.valueOf(Loader.bb.getString(color).toUpperCase()));
									}catch(Exception | NoSuchFieldError e) {}
								if(Loader.bb.getString(style)!=null)
									try {
										b.setStyle(BarStyle.valueOf(Loader.bb.getString(style).toUpperCase()));
									}catch(Exception | NoSuchFieldError e) {}
								continue;
							}
							hide.get(DisplayType.BOSSBAR).add(s.getName());
							TheAPI.removeBossBar(s); //remove
							continue;
						}else {
							if(!isToggleable(s, DisplayType.BOSSBAR)) {
								hide.get(DisplayType.BOSSBAR).remove(s.getName());
								String text = "Text";
								String stage = "Stage";
								String style = "Style";
								String color = "Color";
								if(Loader.bb.exists("PerPlayer."+s.getName())) {
									text="PerPlayer."+s.getName()+".Text";
									stage="PerPlayer."+s.getName()+".Stage";
									style="PerPlayer."+s.getName()+".Style";
									color="PerPlayer."+s.getName()+".Color";
								}else {
									if(Loader.bb.exists("PerWorld."+s.getWorld().getName())) {
										text="PerWorld."+s.getWorld().getName()+".Text";
										stage="PerWorld."+s.getWorld().getName()+".Stage";
										style="PerWorld."+s.getWorld().getName()+".Style";
										color="PerWorld."+s.getWorld().getName()+".Color";
									}
								}
								BossBar b = TheAPI.sendBossBar(s, bb.replace(s, Loader.bb.getString(text)), (double)StringUtils.calculate(PlaceholderAPI.setPlaceholders(s, Loader.bb.getString(stage)))/100);
								if(Loader.bb.getString(color)!=null)
									try {
										if(Loader.bb.getString(color).toUpperCase().equals("RANDOM")) {
											b.setColor(BarColor.values()[cc]);
										}else
										b.setColor(BarColor.valueOf(Loader.bb.getString(color).toUpperCase()));
									}catch(Exception | NoSuchFieldError e) {}
								if(Loader.bb.getString(style)!=null)
									try {
										b.setStyle(BarStyle.valueOf(Loader.bb.getString(style).toUpperCase()));
									}catch(Exception | NoSuchFieldError e) {}
								continue;
							}
							//already gone
							continue;
						}
					}else {
						hide.get(DisplayType.BOSSBAR).remove(s.getName());
						String text = "Text";
						String stage = "Stage";
						String style = "Style";
						String color = "Color";
						if(Loader.bb.exists("PerPlayer."+s.getName())) {
							text="PerPlayer."+s.getName()+".Text";
							stage="PerPlayer."+s.getName()+".Stage";
							style="PerPlayer."+s.getName()+".Style";
							color="PerPlayer."+s.getName()+".Color";
						}else {
							if(Loader.bb.exists("PerWorld."+s.getWorld().getName())) {
								text="PerWorld."+s.getWorld().getName()+".Text";
								stage="PerWorld."+s.getWorld().getName()+".Stage";
								style="PerWorld."+s.getWorld().getName()+".Style";
								color="PerWorld."+s.getWorld().getName()+".Color";
							}
						}
						BossBar b = TheAPI.sendBossBar(s, bb.replace(s, Loader.bb.getString(text)), (double)StringUtils.calculate(PlaceholderAPI.setPlaceholders(s, Loader.bb.getString(stage)))/100);
						if(Loader.bb.getString(color)!=null)
							try {
								if(Loader.bb.getString(color).toUpperCase().equals("RANDOM")) {
									b.setColor(BarColor.values()[cc]);
								}else
								b.setColor(BarColor.valueOf(Loader.bb.getString(color).toUpperCase()));
							}catch(Exception | NoSuchFieldError e) {}
						if(Loader.bb.getString(style)!=null)
							try {
								b.setStyle(BarStyle.valueOf(Loader.bb.getString(style).toUpperCase()));
							}catch(Exception | NoSuchFieldError e) {}
						continue;
					}
					}catch(Exception er) {}
				}
				if(cc==6)cc=0;
				else ++cc;
				bb.update();
			}
		}.runRepeating(0, (long) StringUtils.calculate(Loader.bb.getString("RefleshTick"))));
		if (setting.sb)
		tasks.add(new Tasker() {
			public void run() {
				for(Player s : TheAPI.getOnlinePlayers()) {
					try {
					if(!s.hasPermission(Loader.sb.getString("Options.Permission"))) {
						if(!hide.get(DisplayType.SCOREBOARD).contains(s.getName())) {
							hide.get(DisplayType.SCOREBOARD).add(s.getName());
							if(map.containsKey(s.getName())) {
								map.remove(s.getName()).destroy();
							}
						}
						continue;
					}
					if(Loader.sb.getStringList("Options.ForbiddenWorlds").contains(s.getWorld().getName())) {
						if(!hide.get(DisplayType.SCOREBOARD).contains(s.getName())) {
							hide.get(DisplayType.SCOREBOARD).add(s.getName());
							if(map.containsKey(s.getName())) {
								map.remove(s.getName()).destroy();
							}
							continue;
						}
						continue;
					}
					if(ignore.get(DisplayType.SCOREBOARD).contains(s.getName())) {
						if(!hide.get(DisplayType.SCOREBOARD).contains(s.getName())) {
							if(!isToggleable(s, DisplayType.SCOREBOARD)) {
								String name = "Name";
								String lines = "Lines";
								if(Loader.sb.exists("PerPlayer."+s.getName())) {
									name="PerPlayer."+s.getName()+".Name";
									lines="PerPlayer."+s.getName()+".Lines";
								}else if(Loader.sb.exists("PerWorld."+s.getWorld().getName())) {
									name="PerWorld."+s.getWorld().getName()+".Name";
									lines="PerWorld."+s.getWorld().getName()+".Lines";
								}
								score.setTitle(sb.replace(s, Loader.sb.getString(name)));
								for(String line : Loader.sb.getStringList(lines)) {
									score.addLine(sb.replace(s, line));
								}
								score.send(s);
								continue;
							}
							hide.get(DisplayType.SCOREBOARD).add(s.getName());
							if(map.containsKey(s.getName())) {
								map.remove(s.getName()).destroy();
							}
							continue;
						}else {
							if(!isToggleable(s, DisplayType.SCOREBOARD)) {
								hide.get(DisplayType.SCOREBOARD).remove(s.getName());
								String name = "Name";
								String lines = "Lines";
								if(Loader.sb.exists("PerPlayer."+s.getName())) {
									name="PerPlayer."+s.getName()+".Name";
									lines="PerPlayer."+s.getName()+".Lines";
								}else if(Loader.sb.exists("PerWorld."+s.getWorld().getName())) {
									name="PerWorld."+s.getWorld().getName()+".Name";
									lines="PerWorld."+s.getWorld().getName()+".Lines";
								}
								score.setTitle(sb.replace(s, Loader.sb.getString(name)));
								for(String line : Loader.sb.getStringList(lines)) {
									score.addLine(sb.replace(s, line));
								}
								score.send(s);
								continue;
							}
							//already gone
							continue;
						}
					}else {
						hide.get(DisplayType.SCOREBOARD).remove(s.getName());
						String name = "Name";
						String lines = "Lines";
						if(Loader.sb.exists("PerPlayer."+s.getName())) {
							name="PerPlayer."+s.getName()+".Name";
							lines="PerPlayer."+s.getName()+".Lines";
						}else if(Loader.sb.exists("PerWorld."+s.getWorld().getName())) {
							name="PerWorld."+s.getWorld().getName()+".Name";
							lines="PerWorld."+s.getWorld().getName()+".Lines";
						}
						score.setTitle(sb.replace(s, Loader.sb.getString(name)));
						for(String line : Loader.sb.getStringList(lines)) {
							score.addLine(sb.replace(s, line));
						}
						score.send(s);
						continue;
					}
					}catch(Exception er) {}
				}
				sb.update();
			}
		}.runRepeating(0, (long) StringUtils.calculate(Loader.sb.getString("Options.RefleshTick"))));
		
	}
	
	public static void unload() {
		for(int i : tasks)
			Scheduler.cancelTask(i);
		tasks.clear();
		for(DisplayType t : DisplayType.values()) {
			ignore.get(t).clear();
			hide.get(t).clear();
		}
		for(Player s : TheAPI.getOnlinePlayers()) {
			TheAPI.sendActionBar(s, "");
			if(TheAPI.getBossBar(s)!=null)
			TheAPI.removeBossBar(s);
			if(map.containsKey(s.getName()))
				map.remove(s.getName()).destroy();
		}
	}
}
