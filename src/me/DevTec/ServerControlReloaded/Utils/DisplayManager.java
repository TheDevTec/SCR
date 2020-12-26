package me.DevTec.ServerControlReloaded.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.scoreboardapi.ScoreboardAPI;
import me.devtec.theapi.scoreboardapi.SimpleScore;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.reflections.Ref;

public class DisplayManager {
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

	@SuppressWarnings("unchecked")
	public static void removeCache(Player p) {
		for(DisplayType t : DisplayType.values()) {
			ignore.get(t).remove(p.getName());
			hide.get(t).remove(p.getName());
		}
		TheAPI.sendActionBar(p, "");
		if(TheAPI.getBossBar(p)!=null)
		TheAPI.removeBossBar(p);
		if(((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).containsKey(p.getName()))
		((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).get(p.getName()).destroy();
		((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).remove(p.getName());
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
	
	@SuppressWarnings("unchecked")
	public static void hide(Player s, DisplayType type) {
		TheAPI.getUser(s).setAndSave("SCR."+type.name(), true);
		if(!ignore.get(type).contains(s.getName()))
			ignore.get(type).add(s.getName());
		switch(type) {
			case ACTIONBAR:{
				Loader.sendMessages(s, "DisplayManager.ActionBar.Hide");
				if(!isToggleable(s, DisplayType.ACTIONBAR)) {
					String text = "Text";
					if(Loader.ac.exists("PerPlayer."+s.getName())) {
						text="PerPlayer."+s.getName()+".Text";
					}else {
						if(Loader.ac.exists("PerWorld."+s.getWorld().getName())) {
							text="PerWorld."+s.getWorld().getName()+".Text";
						}
					}
					TheAPI.sendActionBar(s, AnimationManager.replace(s, Loader.ac.getString(text)));
					return;
				}
				hide.get(DisplayType.ACTIONBAR).add(s.getName());
				TheAPI.sendActionBar(s, ""); //remove
			}
			break;
			case BOSSBAR:{
				Loader.sendMessages(s, "DisplayManager.BossBar.Hide");
				if(!isToggleable(s, DisplayType.BOSSBAR)) {
					String text = "Text";
					String stage = "Stage";
					if(Loader.bb.exists("PerPlayer."+s.getName())) {
						text="PerPlayer."+s.getName()+".Text";
						stage="PerPlayer."+s.getName()+".Stage";
					}else {
						if(Loader.bb.exists("PerWorld."+s.getWorld().getName())) {
							text="PerWorld."+s.getWorld().getName()+".Text";
							stage="PerWorld."+s.getWorld().getName()+".Stage";
						}
					}
					TheAPI.sendBossBar(s, AnimationManager.replace(s, Loader.bb.getString(text)), StringUtils.calculate(PlaceholderAPI.setPlaceholders(s, Loader.bb.getString(stage))).doubleValue()/100);
					return;
				}
				hide.get(DisplayType.BOSSBAR).add(s.getName());
				TheAPI.removeBossBar(s); //remove
			}
			break;
			case SCOREBOARD:{
				Loader.sendMessages(s, "DisplayManager.Scoreboard.Hide");
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
					score.setTitle(AnimationManager.replace(s, Loader.sb.getString(name)));
					for(String line : Loader.sb.getStringList(lines)) {
						score.addLine(AnimationManager.replace(s, line));
					}
					score.send(s);
					return;
				}
				hide.get(DisplayType.SCOREBOARD).add(s.getName());
				if(((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).containsKey(s.getName())) {
					((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).get(s.getName()).destroy();
					((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).remove(s.getName()); //remove
				}
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
		if(Loader.ac.getBoolean("Enabled"))
		tasks.add(new Tasker() {
			public void run() {
				for(Player s : TheAPI.getOnlinePlayers()) {
					try {
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
								TheAPI.sendActionBar(s, AnimationManager.replace(s, Loader.ac.getString(text)));
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
								TheAPI.sendActionBar(s, AnimationManager.replace(s, Loader.ac.getString(text)));
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
						TheAPI.sendActionBar(s, AnimationManager.replace(s, Loader.ac.getString(text)));
						continue;
					}
					}catch(Exception er) {}
				}
			}
		}.runRepeating(0, StringUtils.calculate(Loader.ac.getString("RefleshTick")).longValue()));
		if(Loader.bb.getBoolean("Enabled"))
		tasks.add(new Tasker() {
			public void run() {
				for(Player s : TheAPI.getOnlinePlayers()) {
					try {
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
								if(Loader.bb.exists("PerPlayer."+s.getName())) {
									text="PerPlayer."+s.getName()+".Text";
									stage="PerPlayer."+s.getName()+".Stage";
								}else {
									if(Loader.bb.exists("PerWorld."+s.getWorld().getName())) {
										text="PerWorld."+s.getWorld().getName()+".Text";
										stage="PerWorld."+s.getWorld().getName()+".Stage";
									}
								}
								TheAPI.sendBossBar(s, AnimationManager.replace(s, Loader.bb.getString(text)), StringUtils.calculate(PlaceholderAPI.setPlaceholders(s, Loader.bb.getString(stage))).doubleValue()/100);
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
								if(Loader.bb.exists("PerPlayer."+s.getName())) {
									text="PerPlayer."+s.getName()+".Text";
									stage="PerPlayer."+s.getName()+".Stage";
								}else {
									if(Loader.bb.exists("PerWorld."+s.getWorld().getName())) {
										text="PerWorld."+s.getWorld().getName()+".Text";
										stage="PerWorld."+s.getWorld().getName()+".Stage";
									}
								}
								TheAPI.sendBossBar(s, AnimationManager.replace(s, Loader.bb.getString(text)), StringUtils.calculate(PlaceholderAPI.setPlaceholders(s, Loader.bb.getString(stage))).doubleValue()/100);
								continue;
							}
							//already gone
							continue;
						}
					}else {
						hide.get(DisplayType.BOSSBAR).remove(s.getName());
						String text = "Text";
						String stage = "Stage";
						if(Loader.bb.exists("PerPlayer."+s.getName())) {
							text="PerPlayer."+s.getName()+".Text";
							stage="PerPlayer."+s.getName()+".Stage";
						}else {
							if(Loader.bb.exists("PerWorld."+s.getWorld().getName())) {
								text="PerWorld."+s.getWorld().getName()+".Text";
								stage="PerWorld."+s.getWorld().getName()+".Stage";
							}
						}
						TheAPI.sendBossBar(s, AnimationManager.replace(s, Loader.bb.getString(text)), StringUtils.calculate(PlaceholderAPI.setPlaceholders(s, Loader.bb.getString(stage))).doubleValue()/100);
						continue;
					}
					}catch(Exception er) {}
				}
			}
		}.runRepeating(0, StringUtils.calculate(Loader.bb.getString("RefleshTick")).longValue()));
		if (setting.sb)
		tasks.add(new Tasker() {
			@SuppressWarnings("unchecked")
			public void run() {
				for(Player s : TheAPI.getOnlinePlayers()) {
					try {
					if(Loader.sb.getStringList("Options.ForbiddenWorlds").contains(s.getWorld().getName())) {
						if(!hide.get(DisplayType.SCOREBOARD).contains(s.getName())) {
							hide.get(DisplayType.SCOREBOARD).add(s.getName());
							if(((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).containsKey(s.getName())) {
								((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).get(s.getName()).destroy();
								((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).remove(s.getName()); //remove
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
								score.setTitle(AnimationManager.replace(s, Loader.sb.getString(name)));
								for(String line : Loader.sb.getStringList(lines)) {
									score.addLine(AnimationManager.replace(s, line));
								}
								score.send(s);
								continue;
							}
							hide.get(DisplayType.SCOREBOARD).add(s.getName());
							if(((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).containsKey(s.getName())) {
								((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).get(s.getName()).destroy();
								((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).remove(s.getName()); //remove
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
								score.setTitle(AnimationManager.replace(s, Loader.sb.getString(name)));
								for(String line : Loader.sb.getStringList(lines)) {
									score.addLine(AnimationManager.replace(s, line));
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
						score.setTitle(AnimationManager.replace(s, Loader.sb.getString(name)));
						for(String line : Loader.sb.getStringList(lines)) {
							score.addLine(AnimationManager.replace(s, line));
						}
						score.send(s);
						continue;
					}
					}catch(Exception er) {}
				}
			}
		}.runRepeating(0, StringUtils.calculate(Loader.sb.getString("Options.RefleshTick")).longValue()));
		
	}
	
	@SuppressWarnings("unchecked")
	public static void unload() {
		for(int i : tasks)
			Scheduler.cancelTask(i);
		for(Player s : TheAPI.getOnlinePlayers()) {
			TheAPI.sendActionBar(s, "");
			if(TheAPI.getBossBar(s)!=null)
			TheAPI.removeBossBar(s);
			if(((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).containsKey(s.getName())) {
				((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).get(s.getName()).destroy();
				((Map<String, ScoreboardAPI>)Ref.getNulled(SimpleScore.class,"scores")).remove(s.getName()); //remove
			}
		}
		tasks.clear();
		for(DisplayType t : DisplayType.values()) {
			ignore.get(t).clear();
			hide.get(t).clear();
		}
	}
}
