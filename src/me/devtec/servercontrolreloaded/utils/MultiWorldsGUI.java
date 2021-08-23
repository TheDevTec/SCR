package me.devtec.servercontrolreloaded.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.cooldownapi.CooldownAPI;
import me.devtec.theapi.guiapi.EmptyItemGUI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.GUI.ClickType;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.worldsapi.WorldsAPI;

public class MultiWorldsGUI {

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
	private static final ItemGUI load;
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
		load=new ItemGUI(createItem("&bLoad", XMaterial.LIGHT_BLUE_WOOL,
				Collections.singletonList("&7Click to &bload &7unloaded world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvLoad(s);
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
					Loader.mw.set("WorldsSettings." + world + ".Spawn.X", s.getLocation().getX());
					Loader.mw.set("WorldsSettings." + world + ".Spawn.Y", s.getLocation().getY());
					Loader.mw.set("WorldsSettings." + world + ".Spawn.Z", s.getLocation().getZ());
					Loader.mw.set("WorldsSettings." + world + ".Spawn.X_Pos_Head",
							s.getLocation().getYaw());
					Loader.mw.set("WorldsSettings." + world+ ".Spawn.Z_Pos_Head",
							s.getLocation().getPitch());
					Loader.mw.save();
					try {
						s.getWorld().setSpawnLocation(s.getLocation());
					}catch(NoSuchMethodError err) {
						s.getWorld().setSpawnLocation(s.getLocation().getBlockX(), s.getLocation().getBlockY(), s.getLocation().getBlockZ());
					}
					Loader.sendMessages(s, "MultiWorld.Spawn.Set", Placeholder.c().add("%world%", world)
							.add("%x%", StringUtils.fixedFormatDouble(s.getLocation().getX()))
							.add("%y%", StringUtils.fixedFormatDouble(s.getLocation().getY()))
							.add("%z%", StringUtils.fixedFormatDouble(s.getLocation().getZ()))
							.add("%pitch%", StringUtils.fixedFormatDouble(s.getLocation().getPitch()))
							.add("%yaw%", StringUtils.fixedFormatDouble(s.getLocation().getYaw())));
				}
			}
		};
	}

	public static void openInv(Player p) {
		GUI a = new GUI("&eMultiWorlds Editor",54,p);
		prepareInv(a);
		a.setItem(20, create);
		a.setItem(21, delete);
		a.setItem(23, load);
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
				if (Loader.mw.getString("WorldsSettings." + w.getName() + ".Generator") == null)
					lore.add("&7 - Normal");
				else {
					if (Loader.mw.getString("WorldsSettings." + w.getName() + ".Generator").equalsIgnoreCase("flat")) {
						lore.add("&7 - Flat");
						start = "&a";
					}
					if (Loader.mw.getString("WorldsSettings." + w.getName() + ".Generator")
							.equalsIgnoreCase("normal")) {
						lore.add("&7 - Normal");
					}
					if (Loader.mw.getString("WorldsSettings." + w.getName() + ".Generator")
							.equalsIgnoreCase("the_void")) {
						lore.add("&7 - The Void");
						start = "&8";
						m = "GLASS";
					}
				}
			}
			if (w.getEnvironment() == Environment.NETHER) {
				lore.add("&7 - Nether");
				start = "&c";
				m = "NETHERRACK";
			}
			if (w.getEnvironment() == Environment.THE_END) {
				lore.add("&7 - The End");
				start = "&e";
				m = "END_STONE";
			}
			lore.add("&7 - PvP: " + w.getPVP());
			lore.add("&7 - No Mobs: " + Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".NoMobs"));
			lore.add("&7 - AutoSave: " + w.isAutoSave());
			String gm = "SURVIVAL";
			if (Loader.mw.getString("WorldsSettings." + w.getName() + ".GameMode") != null)
				gm = Loader.mw.getString("WorldsSettings." + w.getName() + ".GameMode");
			lore.add("&7 - Keep Spawn In Memory: " + w.getKeepSpawnInMemory());
			lore.add("&7 - Difficulty: " + w.getDifficulty());
			lore.add("&7 - GameMode: " + gm);
			lore.add("&7 - Loaded Chunks: " + w.getLoadedChunks().length);
			if(TheAPI.isNewerThan(16)) {
				int i = 0;
				Iterator<?> tt = ((Iterable<?>) Ref.invoke(Ref.get(Ref.get(Ref.world(w), Ref.field(Ref.nmsOrOld("server.level.WorldServer","WorldServer"),"G")),"g"),"a")).iterator();
				for (; tt.hasNext(); tt.next())
					++i;
				lore.add("&7 - Entities: " + i);
			}else
				lore.add("&7 - Entities: " + w.getEntities().size());
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

	public static void openInvLoad(Player p) {
		GUI a = new GUI("&bWorld loader",54,p);
		prepareInv(a);
		if (Loader.mw.exists("Unloaded-Worlds"))
			for (String w : Loader.mw.getStringList("Unloaded-Worlds")) {
				String biome = Loader.mw.getString("WorldsSettings." + w + ".Generator");
				if (biome == null)
					continue;
				a.addItem(new ItemGUI(createItem("&2" + w, biome.equalsIgnoreCase("NORMAL")?XMaterial.GRASS_BLOCK:(
						biome.equalsIgnoreCase("THE_END")?XMaterial.END_STONE:
							(biome.equalsIgnoreCase("NETHER")?XMaterial.NETHERRACK:(biome.equalsIgnoreCase("FLAT")
									?XMaterial.GREEN_STAINED_GLASS_PANE:XMaterial.GLASS))),
						Arrays.asList("&7Click to load world", "&7 - "+(biome.equalsIgnoreCase("NORMAL")?"Normal":(
								biome.equalsIgnoreCase("THE_END")?"The End":(biome.equalsIgnoreCase("NETHER")?"Nether":
									(biome.equalsIgnoreCase("FLAT")?"Flat":"The Void")))
								)))) {
					@Override
					public void onClick(Player s, HolderGUI g, ClickType c) {
						MultiWorldsUtils.loadWorld(w, s);
						openInvLoad(s);
					}
				});
			}
		a.setItem(49, backMain);
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
					Loader.mw.set("WorldsSettings." + TheAPI.getUser(p).getString("MultiWorlds-Create") + ".Generator",
							TheAPI.getUser(p).getString("MultiWorlds-Generator"));
					new Tasker() {
						public void run() {
							String name = TheAPI.getUser(p).getString("MultiWorlds-Create");
							Loader.mw.set("WorldsSettings." + name + ".Generator", TheAPI.getUser(p).getString("MultiWorlds-Generator"));
							Loader.mw.save();
							NMSAPI.postToMainThread(() -> MultiWorldsUtils.createWorld(name, p));
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
						List<String> ww = Loader.mw.getStringList("Unloaded-Worlds");
						List<String> worlds = Loader.mw.getStringList("Worlds");
						Loader.mw.remove("WorldsSettings."+w.getName());
						worlds.remove(w.getName());
						ww.remove(w.getName());
						Loader.mw.set("Worlds", worlds);
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
					MultiWorldsUtils.unloadWorld(w.getName(), s);
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
					int x = Loader.mw.getInt("WorldsSettings." + w.getName() + ".Spawn.X");
					int y = Loader.mw.getInt("WorldsSettings." + w.getName() + ".Spawn.Y");
					int z = Loader.mw.getInt("WorldsSettings." + w.getName() + ".Spawn.Z");
					int x_head = Loader.mw.getInt("WorldsSettings." + w.getName() + ".Spawn.X_Pos_Head");
					int z_head = Loader.mw.getInt("WorldsSettings." + w.getName() + ".Spawn.Z_Pos_Head");
					Location loc = new Location(w, x,y, z, x_head, z_head);
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
		GUI a = new GUI("&6World setting - " + w.getName(),54,p);
		smallInv(a);
		a.setItem(49, backSet);
		a.setItem(0,new ItemGUI(createItem("&6Difficulty", XMaterial.FEATHER, Collections.singletonList(w.getDifficulty().name()))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
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
					Loader.mw.set("WorldsSettings." + w.getName() + ".Difficulty", dif);
					Loader.mw.save();
					w.setDifficulty(Difficulty.valueOf(dif));
					this.setItem(createItem("&6Difficulty", XMaterial.FEATHER, Collections.singletonList(dif)));
					g.setItem(0, this);
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
					Loader.mw.set("WorldsSettings." + w.getName() + ".Difficulty", dif);
					Loader.mw.save();
					w.setDifficulty(Difficulty.valueOf(dif));
					this.setItem(createItem("&6Difficulty", XMaterial.FEATHER, Collections.singletonList(dif)));
					g.setItem(0, this);
					break;
				default:
					break;
				}
			}
		});
		a.setItem(1,new ItemGUI(createItem("&6GameMode", XMaterial.BRICKS, Collections.singletonList(Loader.mw.exists("WorldsSettings." + w.getName() + ".GameMode") ? Loader.mw.getString("WorldsSettings." + w.getName() + ".GameMode") : "SURVIVAL"))) {
			@Override
			public void onClick(Player s, HolderGUI gui, ClickType c) {
				String g = Loader.mw.exists("WorldsSettings." + w.getName() + ".GameMode")?Loader.mw.getString("WorldsSettings." + w.getName() + ".GameMode"):"SURVIVAL";
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
					Loader.mw.set("WorldsSettings." + w.getName() + ".GameMode", g);
					Loader.mw.save();
					GameMode gg = GameMode.valueOf(g);
					Object gm = Ref.invokeStatic(MultiWorldsUtils.getById,MultiWorldsUtils.toId(g));
					MultiWorldsUtils.gamemodesnms.put(w, gm);
					NMSAPI.postToMainThread(() -> {
						for (Player p : TheAPI.getOnlinePlayers())
							if (p.getWorld() == w)p.setGameMode(gg);
					});
					this.setItem(createItem("&6GameMode", XMaterial.BRICKS, Collections.singletonList(g)));
					gui.setItem(1, this);
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
					Loader.mw.set("WorldsSettings." + w.getName() + ".GameMode", g);
					Loader.mw.save();
					gg = GameMode.valueOf(g);
					gm = Ref.invokeStatic(MultiWorldsUtils.getById,MultiWorldsUtils.toId(g));
					MultiWorldsUtils.gamemodesnms.put(w, gm);
					NMSAPI.postToMainThread(() -> {
						for (Player p : TheAPI.getOnlinePlayers())
							if (p.getWorld() == w)p.setGameMode(gg);
					});
					this.setItem(createItem("&6GameMode", XMaterial.BRICKS, Collections.singletonList(g)));
					gui.setItem(1, this);
					break;
				default:
					break;
				}
			}
		});
		a.setItem(2,new ItemGUI(createItem("&6Keep Spawn In Memory", XMaterial.MAP, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".KeepSpawnInMemory") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sf = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".KeepSpawnInMemory");
				sf=!sf;
				Loader.mw.set("WorldsSettings." + w.getName() + ".KeepSpawnInMemory", sf);
				Loader.mw.save();
				boolean state = sf;
				NMSAPI.postToMainThread(() -> w.setKeepSpawnInMemory(state));
				this.setItem(createItem("&6Keep Spawn In Memory", XMaterial.MAP, Collections.singletonList(sf + "")));
				g.setItem(2, this);
			}
		});

		a.setItem(3,new ItemGUI(createItem("&6Auto Save", XMaterial.EMERALD_BLOCK, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".AutoSave") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sa = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".AutoSave");
				sa=!sa;
				Loader.mw.set("WorldsSettings." + w.getName() + ".AutoSave", sa);
				Loader.mw.save();
				w.setAutoSave(sa);
				this.setItem(createItem("&6Auto Save", XMaterial.EMERALD_BLOCK, Collections.singletonList(sa + "")));
				g.setItem(3, this);
			}
		});

		a.setItem(4,new ItemGUI(createItem("&6PvP", XMaterial.DIAMOND_SWORD, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".PvP") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sas = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".PvP");
				sas=!sas;
				Loader.mw.set("WorldsSettings." + w.getName() + ".PvP", sas);
				Loader.mw.save();
				w.setPVP(sas);
				this.setItem(createItem("&6PvP", XMaterial.DIAMOND_SWORD, Collections.singletonList(sas + "")));
				g.setItem(4, this);
			}
		});
		a.setItem(5,new ItemGUI(createItem("&6Can be portal created in this world", XMaterial.OBSIDIAN, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".CreatePortal") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sass = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".CreatePortal");
				sass=!sass;
				Loader.mw.set("WorldsSettings." + w.getName() + ".CreatePortal", sass);
				Loader.mw.save();
				this.setItem(createItem("&6Can be portal created in this world", XMaterial.OBSIDIAN, Collections.singletonList(sass + "")));
				g.setItem(5, this);
			}
		});
		a.setItem(6,new ItemGUI(createItem("&6Can be portal used in this world", XMaterial.ENDER_PEARL, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".PortalTeleport") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sasss = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".PortalTeleport");
				sasss=!sasss;
				Loader.mw.set("WorldsSettings." + w.getName() + ".PortalTeleport", sasss);
				Loader.mw.save();
				this.setItem(createItem("&6Can be portal used in this world", XMaterial.ENDER_PEARL, Collections.singletonList(sasss + "")));
				g.setItem(6, this);
			}
		});
		a.setItem(7,new ItemGUI(createItem("&6No mobs", XMaterial.CREEPER_HEAD, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".NoMobs") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sassw = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".NoMobs");
				sassw=!sassw;
				Loader.mw.set("WorldsSettings." + w.getName() + ".NoMobs", sassw);
				Loader.mw.save();
				this.setItem(createItem("&6No mobs", XMaterial.CREEPER_HEAD, Collections.singletonList(sassw + "")));
				g.setItem(7, this);
			}
		});
		a.setItem(8,new ItemGUI(createItem("&6Do Fire Damage", XMaterial.FLINT_AND_STEEL, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoFireDamage") + ""))) {
			boolean fire = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoFireDamage");
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				fire=!fire;
				Loader.mw.set("WorldsSettings." + w.getName() + ".DoFireDamage", fire);
				Loader.mw.save();
				this.setItem(createItem("&6Do Fire Damage", XMaterial.FLINT_AND_STEEL, Collections.singletonList(fire + "")));
				g.setItem(8, this);
			}
		});
		a.setItem(9,new ItemGUI(createItem("&6Do Drowning Damage", XMaterial.WATER_BUCKET, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoDrownDamage") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean dfire = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoDrownDamage");
				dfire=!dfire;
				Loader.mw.set("WorldsSettings." + w.getName() + ".DoDrownDamage", dfire);
				Loader.mw.save();
				this.setItem(createItem("&6Do Drowning Damage", XMaterial.WATER_BUCKET, Collections.singletonList(dfire + "")));
				g.setItem(9, this);
			}
		});
		a.setItem(10,new ItemGUI(createItem("&6Do Fall Damage", XMaterial.IRON_BOOTS, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoFallDamage") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean ddfire = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoFallDamage");
				ddfire=!ddfire;
				Loader.mw.set("WorldsSettings." + w.getName() + ".DoFallDamage", ddfire);
				Loader.mw.save();
				this.setItem(createItem("&6Do Fall Damage", XMaterial.IRON_BOOTS, Collections.singletonList(ddfire + "")));
				g.setItem(10, this);
			}
		});
		a.setItem(11,new ItemGUI(createItem("&6Hardcore", XMaterial.REDSTONE, Collections.singletonList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".Hardcore") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean ddfire = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".Hardcore");
				ddfire=!ddfire;
				Loader.mw.set("WorldsSettings." + w.getName() + ".Hardcore", ddfire);
				Loader.mw.save();
				w.setHardcore(ddfire);
				this.setItem(createItem("&6Hardcore", XMaterial.REDSTONE, Collections.singletonList(ddfire + "")));
				g.setItem(11, this);
			}
		});
		int i = 11;
		if(Loader.mw.exists("WorldsSettings." + w.getName() + ".Gamerule"))
		for (String ds : Loader.mw.getKeys("WorldsSettings." + w.getName() + ".Gamerule")) {
			if(Arrays.asList(toUpper(w.getGameRules())).contains(ds.toUpperCase())) {
			int slot = ++i;
			XMaterial x = null;
			switch (ds.toUpperCase()) {
			case "COMMAND_BLOCK_OUTPUT":
			case "COMMANDBLOCKOUTPUT":
				x = XMaterial.COMMAND_BLOCK;
				break;
			case "DO_TRADER_SPAWNING":
			case "DOTRADERSPAWNING":
				x = XMaterial.LEAD;
				break;
			case "NATURAL_REGENERATION":
			case "NATURALREGENERATION":
				x = XMaterial.RED_DYE;
				break;
			case "FORGIVE_DEAD_PLAYERS":
			case "FORGIVEDEADPLAYERS":
				x = XMaterial.GOLD_NUGGET;
				break;
			case "FIREDAMAGE":
			case "FIRE_DAMAGE":
				x = XMaterial.BLAZE_POWDER;
				break;
			case "DROWNINGDAMAGE":
			case "DROWNING_DAMAGE":
				x = XMaterial.WATER_BUCKET;
				break;
			case "DISABLE_ELYTRA_MOVEMENT_CHECK":
			case "DISABLEELYTRAMOVEMENTCHECK":
				x = XMaterial.ELYTRA;
				break;
			case "UNIVERSAL_ANGER":
			case "UNIVERSALANGER":
				x = XMaterial.GOLD_NUGGET;
				break;
			case "DO_IMMEDIATE_RESPAWN":
			case "DOIMMEDIATERESPAWN":
				x = XMaterial.RED_BED;
				break;
			case "DO_INSOMNIA":
			case "DOINSOMNIA":
				x = XMaterial.RED_BED;
				break;
			case "DO_PATROL_SPAWNING":
			case "DOPATROLSPAWNING":
				x = XMaterial.ENDER_EYE;
				break;
			case "DISABLE_RAIDS":
			case "DISABLERAIDS":
				x = XMaterial.IRON_AXE;
				break;
			case "DO_DAYLIGHT_CYCLE":
			case "DODAYLIGHTCYCLE":
				x = XMaterial.CLOCK;
				break;
			case "DO_ENTITY_DROPS":
			case "DOENTITYDROPS":
				x = XMaterial.ROTTEN_FLESH;
				break;
			case "DO_FIRE_TICK":
			case "DOFIRETICK":
				x = XMaterial.FLINT_AND_STEEL;
				break;
			case "DO_LIMITED_CRAFTING":
			case "DOLIMITEDCRAFTING":
				x = XMaterial.CRAFTING_TABLE;
				break;
			case "DO_MOB_LOOT":
			case "DOMOBLOOT":
				x = XMaterial.PORKCHOP;
				break;
			case "DO_TILE_DROPS":
			case "DOTILEDROPS":
				x = XMaterial.CHEST;
				break;
			case "DO_MOB_SPAWNING":
			case "DOMOBSPAWNING":
				x = XMaterial.ZOMBIE_HEAD;
				break;
			case "FALL_DAMAGE":
			case "FALLDAMAGE":
				x = XMaterial.IRON_BOOTS;
				break;
			case "DO_WEATHER_CYCLE":
			case "DOWEATHERCYCLE":
				x = XMaterial.WATER_BUCKET;
				break;
			case "KEEP_INVENTORY":
			case "KEEPINVENTORY":
				x = XMaterial.CHEST;
				break;
			case "LOG_ADMIN_COMMANDS":
			case "LOGADMINCOMMANDS":
				x = XMaterial.PAPER;
				break;
			case "MAX_COMMAND_CHAIN_LENGTH":
			case "MAXCOMMANDCHAINLENGTH":
				x = XMaterial.CHAIN_COMMAND_BLOCK;
				break;
			case "MAX_ENTITY_CRAMMING":
			case "MAXENTITYCRAMMING":
				x = XMaterial.ZOMBIE_SPAWN_EGG;
				break;
			case "MOB_GRIEFING":
			case "MOBGRIEFING":
				x = XMaterial.OAK_DOOR;
				break;
			case "RANDOM_TICK_SPEED":
			case "RANDOMTICKSPEED":
				x = XMaterial.GOLDEN_HOE;
				break;
			case "REDUCED_DEBUG_INFO":
			case "REDUCEDDEBUGINFO":
				x = XMaterial.EGG;
				break;
			case "SEND_COMMAND_FEEDBACK":
			case "SENDCOMMANDFEEDBACK":
				x = XMaterial.BOOK;
				break;
			case "SHOW_DEATH_MESSAGES":
			case "SHOWDEATHMESSAGES":
				x = XMaterial.MOSSY_COBBLESTONE;
				break;
			case "SPAWN_RADIUS":
			case "SPAWNRADIUS":
				x = XMaterial.GOLDEN_APPLE;
				break;
			case "SPECTATORS_GENERATE_CHUNKS":
			case "SPECTATORSGENERATECHUNKS":
				x = XMaterial.GRASS_BLOCK;
				break;
			case "ANNOUNCE_ADVANCEMENTS":
			case "ANNOUNCEADVANCEMENTS":
				x = XMaterial.DIAMOND;
				break;
			}
			XMaterial d = x;
			if(d==null)continue;
			String name = WordUtils.capitalize(ds.replace("_", " "));
			a.setItem(slot, new ItemGUI(createItem("&6" + name, d , Collections.singletonList(Loader.mw.getString("WorldsSettings." + w.getName() + ".Gamerule." + ds) == null ? "" : Loader.mw.getString("WorldsSettings." + w.getName() + ".Gamerule." + ds)))) {
				
				@Override
				public void onClick(Player p, HolderGUI g, ClickType c) {
					if(ds.equalsIgnoreCase("MAX_COMMAND_CHAIN_LENGTH")||ds.equalsIgnoreCase("MAX_ENTITY_CRAMMING")||ds.equalsIgnoreCase("RANDOM_TICK_SPEED")||ds.equalsIgnoreCase("SPAWN_RADIUS")
							||
							ds.equalsIgnoreCase("MAXCOMMANDCHAINLENGTH")||ds.equalsIgnoreCase("MAXENTITYCRAMMING")||ds.equalsIgnoreCase("RANDOMTICKSPEED")||ds.equalsIgnoreCase("SPAWNRADIUS")) {
						switch(c) {
						case LEFT_PICKUP:
						case SHIFT_LEFT_DROP:
						case SHIFT_LEFT_PICKUP:
							Loader.mw.set("WorldsSettings." + w.getName() + ".Gamerule." + ds,
									Loader.mw.getInt("WorldsSettings." + w.getName() + ".Gamerule." + ds) + 1);
							Loader.mw.save();
							NMSAPI.postToMainThread(() -> w.setGameRuleValue(ds,
									"" + (Loader.mw.getInt("WorldsSettings." + w.getName() + ".Gamerule." + ds) + 1)));
							this.setItem(createItem("&6"+name, d, Collections.singletonList(Loader.mw.getString("WorldsSettings." + w.getName() + ".Gamerule." + ds))));
							g.setItem(slot, this);
							break;
						case RIGHT_PICKUP:
						case SHIFT_RIGHT_DROP:
						case SHIFT_RIGHT_PICKUP:
							Loader.mw.set("WorldsSettings." + w.getName() + ".Gamerule." + ds,
									Loader.mw.getInt("WorldsSettings." + w.getName() + ".Gamerule." + ds) - 1);
							Loader.mw.save();
							NMSAPI.postToMainThread(() -> w.setGameRuleValue(ds,
									"" + (Loader.mw.getInt("WorldsSettings." + w.getName() + ".Gamerule." + ds) - 1)));
							this.setItem(createItem("&6"+name, d, Collections.singletonList(Loader.mw.getString("WorldsSettings." + w.getName() + ".Gamerule." + ds))));
							g.setItem(slot, this);
							break;
						default:
							break;
						}
					}else {
						Loader.mw.set("WorldsSettings." + w.getName() + ".Gamerule." + ds, !Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".Gamerule." + ds));
						Loader.mw.save();
						NMSAPI.postToMainThread(() -> w.setGameRuleValue(ds,
								 "" + (!Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".Gamerule." + ds))));
						this.setItem(createItem("&6"+name, d, Collections.singletonList(Loader.mw.getString("WorldsSettings." + w.getName() + ".Gamerule." + ds))));
						g.setItem(slot, this);
					}
				}});
			}
	}}
	
	private static String[] toUpper(String[] gameRules) {
		for(int i = 0; i < gameRules.length; ++i)
			gameRules[i]=gameRules[i].toUpperCase();
		return gameRules;
	}

	public static final ItemGUI empty=new ItemGUI(createItem(" ", XMaterial.BLACK_STAINED_GLASS_PANE, null)) {public void onClick(Player s, HolderGUI g, ClickType c) {}
	};

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
