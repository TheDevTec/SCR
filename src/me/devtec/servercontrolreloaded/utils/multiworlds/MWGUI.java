package me.devtec.servercontrolreloaded.utils.multiworlds;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.XMaterial;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.cooldownapi.CooldownAPI;
import me.devtec.theapi.guiapi.EmptyItemGUI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.GUI.ClickType;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.worldsapi.WorldsAPI;

public class MWGUI {

	public static ItemStack createItem(String name, XMaterial material, List<String> lore) {
		return ItemCreatorAPI.create(material.getMaterial(),1,name,lore,material.getData());
	}

	public static ItemStack createItem(String name, XMaterial material) {
		return ItemCreatorAPI.create(material.getMaterial(),1,name,material.getData());
	}

	public static ItemStack createItem(String name, List<String> lore) {
		return ItemCreatorAPI.create(TheAPI.isNewVersion()?Material.matchMaterial("REDSTONE_TORCH"):Material.matchMaterial("REDSTONE_TORCH_ON"),1,name,lore);
	}
	
	private static final ItemGUI create;
	private static final ItemGUI delete;
	private static final ItemGUI unload;
	private static final ItemGUI setspawn;
	private static final ItemGUI list;
	private static final ItemGUI teleport;
	private static final ItemGUI set;
	private static final ItemGUI backMain;
	private static final ItemGUI setGenNormal;
	private static final ItemGUI setGenNether;
	private static final ItemGUI setGenEnd;
	private static final ItemGUI setGenFlat;
	private static final ItemGUI setGenVoid;
	private static final ItemGUI backCreate;
	private static final ItemGUI backMainFromCreate;
	private static final ItemGUI backSet;
	static {
		backMain=new ItemGUI(createItem("&cBack", XMaterial.BARRIER, Lists.newArrayList())) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				openInv(s);
			}
		};
		backSet=new ItemGUI(createItem("&cBack", XMaterial.BARRIER, Lists.newArrayList())) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				openInvSet(s);
			}
		};
		backMainFromCreate=new ItemGUI(createItem("&cCancel", XMaterial.BARRIER, Lists.newArrayList())) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).remove("MultiWorlds-Generator");
				TheAPI.getUser(s).remove("MultiWorlds-Create");
				TheAPI.getUser(s).save();
				new CooldownAPI(s.getName()).removeCooldown("world-create");
				openInv(s);
			}
		};
		backCreate=new ItemGUI(createItem("&cBack", XMaterial.BARRIER, Lists.newArrayList())) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				openInvCreate(s);
			}
		};
		setGenNormal=new ItemGUI(createItem("&2Normal", XMaterial.GRASS_BLOCK, Collections.singletonList("&7Default generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "NORMAL");
				openInvCreate(s);
			}
		};
		setGenNether=new ItemGUI(createItem("&cNether", XMaterial.NETHERRACK, Collections.singletonList("&7Nether generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "NETHER");
				openInvCreate(s);
			}
		};
		setGenEnd=new ItemGUI(createItem("&8The End", XMaterial.END_STONE, Collections.singletonList("&7The End generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "THE_END");
				openInvCreate(s);
			}
		};
		setGenFlat=new ItemGUI(createItem("&aFlat", XMaterial.GRASS_BLOCK, Collections.singletonList("&7Flat generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "FLAT");
				openInvCreate(s);
			}
		};
		setGenVoid=new ItemGUI(createItem("&7Void", XMaterial.GLASS, Collections.singletonList("&7Void generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "THE_VOID");
				openInvCreate(s);
			}
		};
		create=new ItemGUI(createItem("&aCreate", XMaterial.GREEN_WOOL,
				Collections.singletonList("&7Click to &acreate &7new world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvCreate(s);
			}
		};
		delete=new ItemGUI(createItem("&cDelete", XMaterial.RED_WOOL,
				Collections.singletonList("&7Click to &cdelete &7existing world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvDelete(s);
			}
		};
		unload=new ItemGUI(createItem("&eUnload", XMaterial.YELLOW_WOOL,
				Collections.singletonList("&7Click to &eunload &7loaded world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvUnload(s);
			}
		};
		teleport=new ItemGUI(createItem("&5Teleport", XMaterial.ENDER_PEARL,
				Collections.singletonList("&7Click to &5teleport &7to another world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvTeleport(s);
			}
		};
		list=new ItemGUI(createItem("&eList of Worlds", XMaterial.BOOK,
				Collections.singletonList("&7Click to open &elist &7of worlds"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvList(s);
			}
		};
		set=new ItemGUI(createItem("&2World settings",
				Collections.singletonList("&7Click to open &2world settings"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvSet(s);
			}
		};
		setspawn=new ItemGUI(createItem("&eSetSpawn", XMaterial.COMPASS,
				Collections.singletonList("&7Click to &eset spawn &7of world at your location"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other")) {
					String world = s.getWorld().getName();
					Location loc = s.getLocation();
					Loader.mw.set("settings." + world + ".spawn", new Position(loc).toString());
					Loader.mw.save();
					try {
						s.getWorld().setSpawnLocation(loc);
					}catch(NoSuchMethodError err) {
						s.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
					}
					Loader.sendMessages(s, "MultiWorld.Spawn.Set", Placeholder.c().add("%world%", world)
							.add("%x%", StringUtils.fixedFormatDouble(loc.getX()))
							.add("%y%", StringUtils.fixedFormatDouble(loc.getY()))
							.add("%z%", StringUtils.fixedFormatDouble(loc.getZ()))
							.add("%pitch%", StringUtils.fixedFormatDouble(loc.getPitch()))
							.add("%yaw%", StringUtils.fixedFormatDouble(loc.getYaw())));
				}
			}
		};
	}

	public static void openInv(Player p) {
		GUI a = new GUI("&eMultiWorlds Editor",54,p);
		prepareInv(a);
		a.setItem(20, create);
		a.setItem(21, delete);
		a.setItem(24, unload);
		a.setItem(30, setspawn);
		a.setItem(29, set);
		a.setItem(33, teleport);
		a.setItem(32, list);
	}

	public static void openInvList(Player p) {
		GUI a = new GUI("&bWorlds list",54,p);
		prepareInv(a);
		for (World w : Bukkit.getWorlds()) {
			List<String> lore = new ArrayList<>();
			String m = "GRASS_BLOCK";
			String start = "&2";
			if (w.getEnvironment() == Environment.NORMAL) {
				if (Loader.mw.getString("settings." + w.getName() + ".generator") != null)
					if (Loader.mw.getString("settings." + w.getName() + ".generator").equalsIgnoreCase("flat")) {
						lore.add("&7 - Generator: Flat");
						start = "&a";
					}
					if (Loader.mw.getString("settings." + w.getName() + ".generator")
							.equalsIgnoreCase("the_void")) {
						lore.add("&7 - Generator: The Void");
						start = "&8";
						m = "GLASS";
					}
			}else
			if (w.getEnvironment() == Environment.NETHER) {
				lore.add("&7 - Generator: Nether");
				start = "&c";
				m = "NETHERRACK";
			}else
			if (w.getEnvironment() == Environment.THE_END) {
				lore.add("&7 - Generator: The End");
				start = "&e";
				m = "END_STONE";
			}
			if(lore.isEmpty())
				lore.add("&7 - Generator: Normal");
			lore.add("&7 - PvP: " + w.getPVP());
			lore.add("&7 - AutoSave: " + w.isAutoSave());
			lore.add("&7 - Allow Animals: " + w.getAllowAnimals());
			lore.add("&7 - Allow Monsters: " + w.getAllowMonsters());
			try {
			lore.add("&7 - HardCore: " + w.isHardcore());
			}catch(NoSuchMethodError e) {}
			String gm = Loader.mw.getString("settings." + w.getName() + ".gamemode");
			lore.add("&7 - Portals Create: " + Loader.mw.getBoolean("settings." + w.getName() + ".portals.create"));
			lore.add("&7 - Portals Teleport: " + Loader.mw.getBoolean("settings." + w.getName() + ".portals.teleport"));
			lore.add("&7 - Difficulty: " + w.getDifficulty());
			lore.add("&7 - GameMode: " + gm);
			lore.add("&7 - Loaded Chunks: " + w.getLoadedChunks().length);
			lore.add("&7 - Players: " + w.getPlayers().size());
			a.addItem(new EmptyItemGUI(createItem(start + w.getName(), XMaterial.valueOf(m), lore)));
		}
		a.setItem(49,backMain);
	}

	public static void openInvGen(Player p) {
		GUI a = new GUI("&2World creator - Generator",54,p);
		prepareInv(a);
		a.setItem(20, setGenNormal);
		a.setItem(22, setGenNether);
		a.setItem(31, setGenEnd);
		a.setItem(29, setGenFlat);
		a.setItem(33, setGenVoid);
		a.setItem(49, backCreate);
	}

	public static void openInvCreate(Player p) {
		GUI a = new GUI("&2World creator",54,p);
		prepareInv(a);
			a.setItem(20, new ItemGUI(createItem("&aWorld Name", TheAPI.getUser(p).exist("MultiWorlds-Create")?XMaterial.GREEN_WOOL:XMaterial.RED_WOOL, Arrays.asList("&7World Name", TheAPI.getUser(p).exist("MultiWorlds-Create")?"&7 - " + "&a" + TheAPI.getUser(p).getString("MultiWorlds-Create"):"&7 - &cnone"))) {
				@Override
				public void onClick(Player s, HolderGUI g, ClickType c) {
					g.close(s);
					TheAPI.sendTitle(s,"&2Write world name", "&2To the chat");
					TheAPI.sendActionBar(s, "&6Type &0'&ccancel&0' &6to cancel.");
					TheAPI.getCooldownAPI(s.getName()).createCooldown("world-create", 30*20);
				}
			});
		a.setItem(24, new ItemGUI(createItem("&aGenerator type", TheAPI.getUser(p).exist("MultiWorlds-Generator")?XMaterial.GREEN_WOOL:XMaterial.RED_WOOL, Arrays.asList("&7World generator", TheAPI.getUser(p).exist("MultiWorlds-Generator")?"&7 - &a" + TheAPI.getUser(p).getString("MultiWorlds-Generator"):"&7 - &cnone"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				openInvGen(s);
			}
		});
		String aads = "&cnone";
		if (TheAPI.getUser(p).exist("MultiWorlds-Generator"))
			aads = "&a" + TheAPI.getUser(p).getString("MultiWorlds-Generator");
		String aad = "&cnone";
		if (TheAPI.getUser(p).exist("MultiWorlds-Create"))
			aad = "&a" + TheAPI.getUser(p).getString("MultiWorlds-Create");
		String m = "RED_TERRACOTTA";
		if (!TheAPI.getUser(p).exist("MultiWorlds-Create") && TheAPI.getUser(p).exist("MultiWorlds-Generator")
				|| TheAPI.getUser(p).exist("MultiWorlds-Create") && !TheAPI.getUser(p).exist("MultiWorlds-Generator"))
			m = "ORANGE_TERRACOTTA";
		if (TheAPI.getUser(p).exist("MultiWorlds-Create")
				&& TheAPI.getUser(p).exist("MultiWorlds-Generator"))
			m = "GREEN_TERRACOTTA";
		a.setItem(40, new ItemGUI(createItem("&aCreate", XMaterial.valueOf(m),
				Arrays.asList("&7Current options", "&7 - &aGenerator: &6" + aads, "&7 - &aWorld name: &6" + aad))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (TheAPI.getUser(p).exist("MultiWorlds-Create") && TheAPI.getUser(p).exist("MultiWorlds-Generator")) {
					g.close(s);
					new Tasker() {
						public void run() {
							String name = TheAPI.getUser(p).getString("MultiWorlds-Create");
							Loader.mw.set("settings." + name + ".generator", TheAPI.getUser(p).getString("MultiWorlds-Generator"));
							Loader.mw.save();
							NMSAPI.postToMainThread(() -> MWAPI.createWorld(name, p));
							TheAPI.getUser(p).remove("MultiWorlds-Generator");
							TheAPI.getUser(p).remove("MultiWorlds-Create");
							TheAPI.getUser(p).save();
						}
					}.runLater(5);
				}
			}
		});
		a.setItem(49, backMainFromCreate);
	}

	public static void openInvDelete(Player p) {
		GUI a = new GUI("&cWorld remover",54,p);
		prepareInv(a);
		for (World w : Bukkit.getWorlds()) {
				a.addItem(new ItemGUI(createItem(w.getName(), w.getEnvironment() == Environment.NORMAL?XMaterial.GRASS_BLOCK:
					(w.getEnvironment() == Environment.NETHER?XMaterial.NETHERRACK:XMaterial.END_STONE), Collections.singletonList("&7Click to delete world"))) {
					@Override
					public void onClick(Player s, HolderGUI g, ClickType c) {
						openInvDelete(s);
						List<String> worlds = Loader.mw.getStringList("worlds");
						Loader.mw.remove("settings."+w.getName());
						worlds.remove(w.getName());
						Loader.mw.set("worlds", worlds);
						Loader.mw.save();
						if (WorldsAPI.delete(w, true))
							Loader.sendMessages(s, "MultiWorld.Delete", Placeholder.c().add("%world%", w.getName()));
					}
				});
		}
		a.setItem(49, backMain);
	}

	public static void openInvUnload(Player p) {
		GUI a = new GUI("&eWorld unloader",54,p);
		prepareInv(a);
		for (World w : Bukkit.getWorlds()) {
			a.addItem(new ItemGUI(createItem(w.getName(), w.getEnvironment() == Environment.NORMAL?XMaterial.GRASS_BLOCK:
				(w.getEnvironment() == Environment.NETHER?XMaterial.NETHERRACK:XMaterial.END_STONE), Collections.singletonList("&7Click to load world"))) {
				@Override
				public void onClick(Player s, HolderGUI g, ClickType c) {
					openInvUnload(s);
					MWAPI.unloadWorld(w.getName(), s);
				}
			});
		}
		a.setItem(49, backMain);
	}

	public static void openInvTeleport(Player p) {
		GUI a = new GUI("&5World teleporter",54,p);
		prepareInv(a);
		for (World w : Bukkit.getWorlds()) {
			a.addItem(new ItemGUI(createItem(w.getName(), w.getEnvironment() == Environment.NORMAL?XMaterial.GRASS_BLOCK:
				(w.getEnvironment() == Environment.NETHER?XMaterial.NETHERRACK:XMaterial.END_STONE), Collections.singletonList("&7Click to teleport to world '" + w.getName() + "'"))) {
				@Override
				public void onClick(Player s, HolderGUI g, ClickType c) {
					g.close(s);
					Position loc = Position.fromString(Loader.mw.getString("settings." + w.getName() + ".spawn"));
					API.setBack(s);
					s.setNoDamageTicks(20);
					NMSAPI.postToMainThread(() -> API.teleport(s, loc));
					Loader.sendMessages(s, "MultiWorld.Teleport.You", Placeholder.c().add("%world%", w.getName()));
				}
			});
		}
		a.setItem(49, backMain);
	}

	public static void openInvSet(Player p) {
		GUI a = new GUI("&6World setting",54,p);
		prepareInv(a);
		for (World w : Bukkit.getWorlds()) {
			a.addItem(new ItemGUI(createItem(w.getName(), w.getEnvironment() == Environment.NORMAL?XMaterial.GRASS_BLOCK:
				(w.getEnvironment() == Environment.NETHER?XMaterial.NETHERRACK:XMaterial.END_STONE), Collections.singletonList("&7Click to open setting of world '" + w.getName() + "'"))) {
					@Override
				public void onClick(Player s, HolderGUI g, ClickType c) {
						openInvSetWorld(s, w);
				}
			});
		}
		a.setItem(49, backMain);
	}

	public static void openInvSetWorld(Player p, World w) {
		LinkedHashMap<ItemGUI, Integer> items = new LinkedHashMap<>();
		items.put(new ItemGUI(createItem("&6Difficulty", XMaterial.RED_CONCRETE, Collections.singletonList(w.getDifficulty().name()))) {
			@Override
			public void onClick(Player s, HolderGUI gui, ClickType c) {
				String dif = w.getDifficulty().name();
				switch(c) {
				case LEFT_PICKUP:
				case SHIFT_LEFT_DROP:
				case SHIFT_LEFT_PICKUP:
					if (dif.contains("EASY"))
						dif="NORMAL";
					else if (dif.contains("NORMAL"))
						dif="HARD";
					else if (dif.contains("HARD"))
						dif="PEACEFUL";
					else dif="EASY";
					Loader.mw.set("settings." + w.getName() + ".difficulty", dif);
					Loader.mw.save();
					w.setDifficulty(Difficulty.valueOf(dif));
					this.setItem(createItem("&6Difficulty", XMaterial.RED_CONCRETE, Collections.singletonList(dif)));
					gui.setItem(items.get(this), this);
					break;
				case RIGHT_PICKUP:
				case SHIFT_RIGHT_DROP:
				case SHIFT_RIGHT_PICKUP:
					if (dif.contains("EASY"))
						dif="PEACEFUL";
					else if (dif.contains("NORMAL"))
						dif="EASY";
					else if (dif.contains("HARD"))
						dif="NORMAL";
					else dif="HARD";
					Loader.mw.set("settings." + w.getName() + ".difficulty", dif);
					Loader.mw.save();
					w.setDifficulty(Difficulty.valueOf(dif));
					this.setItem(createItem("&6Difficulty", XMaterial.RED_CONCRETE, Collections.singletonList(dif)));
					gui.setItem(items.get(this), this);
					break;
				default:
					break;
				}
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6GameMode", XMaterial.RED_CONCRETE, Collections.singletonList(Loader.mw.getString("settings." + w.getName() + ".gamemode")))) {
			@Override
			public void onClick(Player s, HolderGUI gui, ClickType c) {
				String g = Loader.mw.getString("settings." + w.getName() + ".gamemode");
				switch(c) {
				case LEFT_PICKUP:
				case SHIFT_LEFT_DROP:
				case SHIFT_LEFT_PICKUP:
					if (g.contains("SURVIVAL"))
						g="CREATIVE";
					else if (g.contains("CREATIVE"))
						g="ADVENTURE";
					else if (g.contains("ADVENTURE"))
						g="SPECTATOR";
					else g="SURVIVAL";
					Loader.mw.set("settings." + w.getName() + ".gamemode", g);
					Loader.mw.save();
					GameMode gg = GameMode.valueOf(g);
					Object gm = Ref.invokeStatic(MWAPI.getById,MWAPI.toId(g));
					MWAPI.gamemodesnms.put(w, gm);
					NMSAPI.postToMainThread(() -> {
						for (Player p : p.getWorld().getPlayers())p.setGameMode(gg);
					});
					this.setItem(createItem("&6GameMode", XMaterial.RED_CONCRETE, Collections.singletonList(g)));
					gui.setItem(items.get(this), this);
					break;
				case RIGHT_PICKUP:
				case SHIFT_RIGHT_DROP:
				case SHIFT_RIGHT_PICKUP:
					if (g.contains("SURVIVAL"))
						g="SPECTATOR";
					else if (g.contains("CREATIVE"))
						g="SURVIVAL";
					else if (g.contains("ADVENTURE"))
						g="CREATIVE";
					else g="SURVIVAL";
					Loader.mw.set("settings." + w.getName() + ".gamemode", g);
					Loader.mw.save();
					gg = GameMode.valueOf(g);
					gm = Ref.invokeStatic(MWAPI.getById,MWAPI.toId(g));
					MWAPI.gamemodesnms.put(w, gm);
					NMSAPI.postToMainThread(() -> {
						for (Player p : p.getWorld().getPlayers())p.setGameMode(gg);
					});
					this.setItem(createItem("&6GameMode", XMaterial.RED_CONCRETE, Collections.singletonList(g)));
					gui.setItem(items.get(this), this);
					break;
				default:
					break;
				}
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6Keep Spawn In Memory", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".keepSpawnInMemory") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sf = Loader.mw.getBoolean("settings." + w.getName() + ".keepSpawnInMemory");
				sf=!sf;
				Loader.mw.set("settings." + w.getName() + ".keepSpawnInMemory", sf);
				Loader.mw.save();
				boolean state = sf;
				NMSAPI.postToMainThread(() -> w.setKeepSpawnInMemory(state));
				this.setItem(createItem("&6Keep Spawn In Memory", XMaterial.BLUE_CONCRETE, Collections.singletonList(sf + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6Auto Save", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".autoSave") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sa = Loader.mw.getBoolean("settings." + w.getName() + ".autoSave");
				sa=!sa;
				Loader.mw.set("settings." + w.getName() + ".autoSave", sa);
				Loader.mw.save();
				w.setAutoSave(sa);
				this.setItem(createItem("&6Auto Save", XMaterial.BLUE_CONCRETE, Collections.singletonList(sa + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6PvP", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".pvp") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sas = Loader.mw.getBoolean("settings." + w.getName() + ".pvp");
				sas=!sas;
				Loader.mw.set("settings." + w.getName() + ".pvp", sas);
				Loader.mw.save();
				w.setPVP(sas);
				this.setItem(createItem("&6PvP", XMaterial.BLUE_CONCRETE, Collections.singletonList(sas + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6Can be portal created in this world", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".portals.create") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sass = Loader.mw.getBoolean("settings." + w.getName() + ".portals.create");
				sass=!sass;
				Loader.mw.set("settings." + w.getName() + ".portals.create", sass);
				Loader.mw.save();
				this.setItem(createItem("&6Can be portal created in this world", XMaterial.BLUE_CONCRETE, Collections.singletonList(sass + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6Can be portal used in this world", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".portals.teleport") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sasss = Loader.mw.getBoolean("settings." + w.getName() + ".portals.teleport");
				sasss=!sasss;
				Loader.mw.set("settings." + w.getName() + ".portals.teleport", sasss);
				Loader.mw.save();
				this.setItem(createItem("&6Can be portal used in this world", XMaterial.BLUE_CONCRETE, Collections.singletonList(sasss + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6Allow spawn of animals", XMaterial.BLUE_CONCRETE, Collections.singletonList(w.getAllowAnimals()+ ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sasss = Loader.mw.getBoolean("settings." + w.getName() + ".allow.animals");
				sasss=!sasss;
				Loader.mw.set("settings." + w.getName() + ".allow.animals", sasss);
				Loader.mw.save();
				w.setSpawnFlags(w.getAllowMonsters(), sasss);
				this.setItem(createItem("&6Allow spawn of animals", XMaterial.BLUE_CONCRETE, Collections.singletonList(sasss + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6Allow spawn of monsters", XMaterial.BLUE_CONCRETE, Collections.singletonList(w.getAllowMonsters()+ ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sasss = Loader.mw.getBoolean("settings." + w.getName() + ".allow.monsters");
				sasss=!sasss;
				Loader.mw.set("settings." + w.getName() + ".allow.monsters", sasss);
				Loader.mw.save();
				w.setSpawnFlags(sasss, w.getAllowAnimals());
				this.setItem(createItem("&6Allow spawn of monsters", XMaterial.BLUE_CONCRETE, Collections.singletonList(sasss + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6Disable drops from blocks", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".disableDrops.blocks")+ ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sasss = Loader.mw.getBoolean("settings." + w.getName() + ".disableDrops.blocks");
				sasss=!sasss;
				Loader.mw.set("settings." + w.getName() + ".disableDrops.blocks", sasss);
				Loader.mw.save();
				this.setItem(createItem("&6Disable drops from blocks", XMaterial.BLUE_CONCRETE, Collections.singletonList(sasss + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6Disable drops from entities", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".disableDrops.entities")+ ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sasss = Loader.mw.getBoolean("settings." + w.getName() + ".disableDrops.entities");
				sasss=!sasss;
				Loader.mw.set("settings." + w.getName() + ".disableDrops.entities", sasss);
				Loader.mw.save();
				this.setItem(createItem("&6Disable drops from entities", XMaterial.BLUE_CONCRETE, Collections.singletonList(sasss + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		items.put(new ItemGUI(createItem("&6Disable drops from players", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".disableDrops.players")+ ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sasss = Loader.mw.getBoolean("settings." + w.getName() + ".disableDrops.players");
				sasss=!sasss;
				Loader.mw.set("settings." + w.getName() + ".disableDrops.players", sasss);
				Loader.mw.save();
				this.setItem(createItem("&6Disable drops from players", XMaterial.BLUE_CONCRETE, Collections.singletonList(sasss + "")));
				g.setItem(items.get(this), this);
			}
		}, 0);
		if(TheAPI.isOlderThan(13)) {
			items.put(new ItemGUI(createItem("&6Fire Damage", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".fireDamage") + ""))) {
				boolean fire = Loader.mw.getBoolean("settings." + w.getName() + ".fireDamage");
				@Override
				public void onClick(Player s, HolderGUI g, ClickType c) {
					fire=!fire;
					Loader.mw.set("settings." + w.getName() + ".fireDamage", fire);
					Loader.mw.save();
					this.setItem(createItem("&6Fire Damage", XMaterial.BLUE_CONCRETE, Collections.singletonList(fire + "")));
					g.setItem(items.get(this), this);
				}
			}, 0);
			items.put(new ItemGUI(createItem("&6Drown Damage", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".drownDamage") + ""))) {
				@Override
				public void onClick(Player s, HolderGUI g, ClickType c) {
					boolean dfire = Loader.mw.getBoolean("settings." + w.getName() + ".drownDamage");
					dfire=!dfire;
					Loader.mw.set("settings." + w.getName() + ".drownDamage", dfire);
					Loader.mw.save();
					this.setItem(createItem("&6Drown Damage", XMaterial.BLUE_CONCRETE, Collections.singletonList(dfire + "")));
					g.setItem(items.get(this), this);
				}
			}, 0);
			items.put(new ItemGUI(createItem("&6Fall Damage", XMaterial.BLUE_CONCRETE, Collections.singletonList(Loader.mw.getBoolean("settings." + w.getName() + ".fallDamage") + ""))) {
				@Override
				public void onClick(Player s, HolderGUI g, ClickType c) {
					boolean ddfire = Loader.mw.getBoolean("settings." + w.getName() + ".fallDamage");
					ddfire=!ddfire;
					Loader.mw.set("settings." + w.getName() + ".fallDamage", ddfire);
					Loader.mw.save();
					this.setItem(createItem("&6Fall Damage", XMaterial.BLUE_CONCRETE, Collections.singletonList(ddfire + "")));
					g.setItem(items.get(this), this);
				}
			}, 0);
		}
		try {
			items.put(new ItemGUI(createItem("&6HardCore", XMaterial.BLUE_CONCRETE, Collections.singletonList(w.isHardcore() + ""))) {
				@Override
				public void onClick(Player s, HolderGUI g, ClickType c) {
					boolean ddfire = Loader.mw.getBoolean("settings." + w.getName() + ".hardCore");
					ddfire=!ddfire;
					Loader.mw.set("settings." + w.getName() + ".hardCore", ddfire);
					Loader.mw.save();
					w.setHardcore(ddfire);
					this.setItem(createItem("&6HardCore", XMaterial.BLUE_CONCRETE, Collections.singletonList(ddfire + "")));
					g.setItem(items.get(this), this);
				}
			}, 0);
		}catch(NoSuchMethodError e) {}
		for(String ds : w.getGameRules()) {
			XMaterial d = StringUtils.isBoolean(w.getGameRuleValue(ds))?XMaterial.YELLOW_CONCRETE:XMaterial.GREEN_CONCRETE;
			String name = WordUtils.capitalize(ds.replace("_", " "));
			items.put(new ItemGUI(createItem("&6" + name, d, Collections.singletonList(Loader.mw.getString("settings." + w.getName() + ".gamerule." + ds) == null ? ""+w.getGameRuleValue(ds) : Loader.mw.getString("settings." + w.getName() + ".gamerule." + ds)))) {
				@Override
				public void onClick(Player p, HolderGUI g, ClickType c) {
					if(d==XMaterial.GREEN_CONCRETE) {
						switch(c) {
						case LEFT_PICKUP:
						case SHIFT_LEFT_DROP:
						case SHIFT_LEFT_PICKUP:
							Loader.mw.set("settings." + w.getName() + ".gamerule." + ds,Loader.mw.getInt("settings." + w.getName() + ".gamerule." + ds) + 1);
							Loader.mw.save();
							NMSAPI.postToMainThread(() -> w.setGameRuleValue(ds,"" + (Loader.mw.getInt("settings." + w.getName() + ".gamerule." + ds) + 1)));
							this.setItem(createItem("&6"+name, d, Collections.singletonList(Loader.mw.getString("settings." + w.getName() + ".gamerule." + ds))));
							g.setItem(items.get(this), this);
							break;
						case RIGHT_PICKUP:
						case SHIFT_RIGHT_DROP:
						case SHIFT_RIGHT_PICKUP:
							Loader.mw.set("settings." + w.getName() + ".gamerule." + ds,
									Loader.mw.getInt("settings." + w.getName() + ".gamerule." + ds) - 1);
							Loader.mw.save();
							NMSAPI.postToMainThread(() -> w.setGameRuleValue(ds,"" + (Loader.mw.getInt("settings." + w.getName() + ".gamerule." + ds) - 1)));
							this.setItem(createItem("&6"+name, d, Collections.singletonList(Loader.mw.getString("settings." + w.getName() + ".gamerule." + ds))));
							g.setItem(items.get(this), this);
							break;
						default:
							break;
						}
					}else {
						Loader.mw.set("settings." + w.getName() + ".gamerule." + ds, !Loader.mw.getBoolean("settings." + w.getName() + ".gamerule." + ds));
						Loader.mw.save();
						NMSAPI.postToMainThread(() -> w.setGameRuleValue(ds, "" + (!Loader.mw.getBoolean("settings." + w.getName() + ".gamerule." + ds))));
						this.setItem(createItem("&6"+name, d, Collections.singletonList(Loader.mw.getString("settings." + w.getName() + ".gamerule." + ds))));
						g.setItem(items.get(this), this);
					}
				}}, 0);
		}
		GUI a = new GUI("&6Settings - " + w.getName()+ " &71/"+(int)Math.ceil((double)items.size()/45),54);
		smallInv(a);
		a.setItem(49, backSet);
		GUI c = a;
		int nextPage = 2;
		int slot = 0;
		for(Entry<ItemGUI, Integer> item : items.entrySet()) {
			if(c.isFull()) {
				slot=0;
				GUI prev = c;
				GUI next = new GUI("&6Settings - " + w.getName()+ " &7"+(nextPage++)+"/"+(int)Math.ceil((double)items.size()/45),54);
				prev.setItem(51, new ItemGUI(createItem("&aNext page", XMaterial.ARROW, Lists.newArrayList())) {
					@Override
					public void onClick(Player s, HolderGUI g, ClickType c) {
						next.open(s);
					}
				});
				c=next;
				smallInv(c);
				c.setItem(47, new ItemGUI(createItem("&6Previous page", XMaterial.ARROW, Lists.newArrayList())) {
					@Override
					public void onClick(Player s, HolderGUI g, ClickType c) {
						prev.open(s);
					}
				});
			}
			c.addItem(item.getKey());
			item.setValue(slot++);
			
		}
		a.open(p);
	}
	
	public static final ItemGUI empty=new EmptyItemGUI(createItem("&e", XMaterial.BLACK_STAINED_GLASS_PANE, null));

	public static void smallInv(GUI a) {
		for (int i = 45; i < 54; ++i)
			a.setItem(i, empty);
	}

	public static void prepareInv(GUI a) {
		for (int i = 0; i < 10; ++i)
			a.setItem(i, empty);
		for(int b : new int[]{17,18,26,27,35,36}){
			a.setItem(b,empty);
		}
		for (int i = 44; i < 54; ++i)
			a.setItem(i, empty);
	}
}
