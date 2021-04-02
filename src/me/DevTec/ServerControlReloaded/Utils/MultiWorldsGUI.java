package me.DevTec.ServerControlReloaded.Utils;


import com.google.common.collect.Lists;
import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.cooldownapi.CooldownAPI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.GUI.ClickType;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.worldsapi.WorldsAPI;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	private static ItemGUI create, delete, load, unload, setspawn, list, teleport, set, backMain
	, setGenNormal, setGenNether, setGenEnd, setGenFlat, setGenVoid, backCreate, backMainFromCreate,backSet;
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
		setGenNormal=new ItemGUI(createItem("&2Normal", XMaterial.GRASS_BLOCK, Arrays.asList("&7Default generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "NORMAL");
				openInvCreate(s);
			}
		};
		setGenNether=new ItemGUI(createItem("&cNether", XMaterial.NETHERRACK, Arrays.asList("&7Nether generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "NETHER");
				openInvCreate(s);
			}
		};
		setGenEnd=new ItemGUI(createItem("&8The End", XMaterial.END_STONE, Arrays.asList("&7The End generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "THE_END");
				openInvCreate(s);
			}
		};
		setGenFlat=new ItemGUI(createItem("&aFlat", XMaterial.GRASS_BLOCK, Arrays.asList("&7Flat generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "FLAT");
				openInvCreate(s);
			}
		};
		setGenVoid=new ItemGUI(createItem("&7Void", XMaterial.GLASS, Arrays.asList("&7Void generator"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				TheAPI.getUser(s).setAndSave("MultiWorlds-Generator", "THE_VOID");
				openInvCreate(s);
			}
		};
		create=new ItemGUI(createItem("&aCreate", XMaterial.GREEN_WOOL,
				Arrays.asList("&7Click to &acreate &7new world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvCreate(s);
			}
		};
		delete=new ItemGUI(createItem("&cDelete", XMaterial.RED_WOOL,
				Arrays.asList("&7Click to &cdelete &7existing world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvDelete(s);
			}
		};
		load=new ItemGUI(createItem("&bLoad", XMaterial.LIGHT_BLUE_WOOL,
				Arrays.asList("&7Click to &bload &7unloaded world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvLoad(s);
			}
		};
		unload=new ItemGUI(createItem("&eUnload", XMaterial.YELLOW_WOOL,
				Arrays.asList("&7Click to &eunload &7loaded world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvUnload(s);
			}
		};
		teleport=new ItemGUI(createItem("&5Teleport", XMaterial.ENDER_PEARL,
			  Arrays.asList("&7Click to &5teleport &7to another world"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvTeleport(s);
			}
		};
		list=new ItemGUI(createItem("&eList of Worlds", XMaterial.BOOK,
				Arrays.asList("&7Click to open &elist &7of worlds"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvList(s);
			}
		};
		set=new ItemGUI(createItem("&2World settings", 
				Arrays.asList("&7Click to open &2world settings"))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				if (Loader.has(s,"MultiWorlds","Other"))
					openInvSet(s);
			}
		};
		setspawn=new ItemGUI(createItem("&eSetSpawn", XMaterial.COMPASS,
				Arrays.asList("&7Click to &eset spawn &7of world at your location"))) {
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
					try {
						s.getWorld().setSpawnLocation(s.getLocation());
					}catch(NoSuchMethodError err) {
						s.getWorld().setSpawnLocation(s.getLocation().getBlockX(), s.getLocation().getBlockY(), s.getLocation().getBlockZ());
					}
					Loader.sendMessages(s, "MultiWorld.Spawn.Set", Placeholder.c().add("%world%", world));
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
			lore.add("&7 - Entities: " + w.getEntities().size());
			lore.add("&7 - Players: " + w.getPlayers().size());
			a.addItem(new ItemGUI(createItem(start + w.getName(), XMaterial.valueOf(m), lore)) {
				@Override
				public void onClick(Player s, HolderGUI g, ClickType c) {}
			});
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
					(w.getEnvironment() == Environment.NETHER?XMaterial.NETHERRACK:XMaterial.END_STONE), Arrays.asList("&7Click to delete world"))) {
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
				(w.getEnvironment() == Environment.NETHER?XMaterial.NETHERRACK:XMaterial.END_STONE), Arrays.asList("&7Click to load world"))) {
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
				(w.getEnvironment() == Environment.NETHER?XMaterial.NETHERRACK:XMaterial.END_STONE), Arrays.asList("&7Click to teleport to world '"+w.getName()+"'"))) {
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
					s.teleport(loc);
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
				(w.getEnvironment() == Environment.NETHER?XMaterial.NETHERRACK:XMaterial.END_STONE), Arrays.asList("&7Click to open setting of world '"+w.getName()+"'"))) {
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
		a.setItem(0,new ItemGUI(createItem("&6Difficulty", XMaterial.FEATHER, Arrays.asList(w.getDifficulty().name()))) {
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
					w.setDifficulty(Difficulty.valueOf(dif));
					this.setItem(createItem("&6Difficulty", XMaterial.FEATHER, Arrays.asList(dif)));
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
					w.setDifficulty(Difficulty.valueOf(dif));
					this.setItem(createItem("&6Difficulty", XMaterial.FEATHER, Arrays.asList(dif)));
					g.setItem(0, this);
					break;
				default:
					break;
				}
			}
		});
		a.setItem(1,new ItemGUI(createItem("&6GameMode", XMaterial.BRICKS, Arrays.asList(Loader.mw.exists("WorldsSettings." + w.getName() + ".GameMode")?Loader.mw.getString("WorldsSettings." + w.getName() + ".GameMode"):"SURVIVAL"))) {
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
					for (Player p : TheAPI.getOnlinePlayers())
						if (p.getWorld() == w)
							p.setGameMode(GameMode.valueOf(g));
					this.setItem(createItem("&6GameMode", XMaterial.BRICKS, Arrays.asList(g)));
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
					for (Player p : TheAPI.getOnlinePlayers())
						if (p.getWorld() == w)
							p.setGameMode(GameMode.valueOf(g));
					this.setItem(createItem("&6GameMode", XMaterial.BRICKS, Arrays.asList(g)));
					gui.setItem(1, this);
					break;
				default:
					break;
				}
			}
		});
		a.setItem(2,new ItemGUI(createItem("&6Keep Spawn In Memory", XMaterial.MAP, Arrays.asList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".KeepSpawnInMemory") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sf = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".KeepSpawnInMemory");
				sf=!sf;
				Loader.mw.set("WorldsSettings." + w.getName() + ".KeepSpawnInMemory", sf);
				w.setKeepSpawnInMemory(sf);
				this.setItem(createItem("&6Keep Spawn In Memory", XMaterial.MAP, Arrays.asList(sf + "")));
				g.setItem(2, this);
			}
		});

		a.setItem(3,new ItemGUI(createItem("&6Auto Save", XMaterial.EMERALD_BLOCK, Arrays.asList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".AutoSave") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sa = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".AutoSave");
				sa=!sa;
				Loader.mw.set("WorldsSettings." + w.getName() + ".AutoSave", sa);
				w.setAutoSave(sa);
				this.setItem(createItem("&6Auto Save", XMaterial.EMERALD_BLOCK, Arrays.asList(sa + "")));
				g.setItem(3, this);
			}
		});

		a.setItem(4,new ItemGUI(createItem("&6PvP", XMaterial.DIAMOND_SWORD, Arrays.asList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".PvP") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sas = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".PvP");
				sas=!sas;
				Loader.mw.set("WorldsSettings." + w.getName() + ".PvP", sas);
				w.setPVP(sas);
				this.setItem(createItem("&6PvP", XMaterial.DIAMOND_SWORD, Arrays.asList(sas + "")));
				g.setItem(4, this);
			}
		});
		a.setItem(5,new ItemGUI(createItem("&6Can be portal created in this world", XMaterial.OBSIDIAN, Arrays.asList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".CreatePortal") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sass = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".CreatePortal");
				sass=!sass;
				Loader.mw.set("WorldsSettings." + w.getName() + ".CreatePortal", sass);
				this.setItem(createItem("&6Can be portal created in this world", XMaterial.OBSIDIAN, Arrays.asList(sass + "")));
				g.setItem(5, this);
			}
		});
		a.setItem(6,new ItemGUI(createItem("&6Can be portal used in this world", XMaterial.ENDER_PEARL, Arrays.asList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".PortalTeleport") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sasss = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".PortalTeleport");
				sasss=!sasss;
				Loader.mw.set("WorldsSettings." + w.getName() + ".PortalTeleport", sasss);
				this.setItem(createItem("&6Can be portal used in this world", XMaterial.ENDER_PEARL, Arrays.asList(sasss + "")));
				g.setItem(6, this);
			}
		});
		a.setItem(7,new ItemGUI(createItem("&6Can be portal used in this world", XMaterial.CREEPER_HEAD, Arrays.asList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".NoMobs") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean sassw = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".NoMobs");
				sassw=!sassw;
				Loader.mw.set("WorldsSettings." + w.getName() + ".NoMobs", sassw);
				this.setItem(createItem("&6Can be portal used in this world", XMaterial.CREEPER_HEAD, Arrays.asList(sassw + "")));
				g.setItem(7, this);
			}
		});
		a.setItem(8,new ItemGUI(createItem("&6Do Fire Damage", XMaterial.FLINT_AND_STEEL, Arrays.asList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoFireDamage") + ""))) {
			boolean fire = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoFireDamage");
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				fire=!fire;
				Loader.mw.set("WorldsSettings." + w.getName() + ".DoFireDamage", fire);
				this.setItem(createItem("&6Do Fire Damage", XMaterial.FLINT_AND_STEEL, Arrays.asList(fire + "")));
				g.setItem(8, this);
			}
		});
		a.setItem(9,new ItemGUI(createItem("&6Do Drowning Damage", XMaterial.WATER_BUCKET, Arrays.asList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoDrownDamage") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean dfire = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoDrownDamage");
				dfire=!dfire;
				Loader.mw.set("WorldsSettings." + w.getName() + ".DoDrownDamage", dfire);
				this.setItem(createItem("&6Do Drowning Damage", XMaterial.WATER_BUCKET, Arrays.asList(dfire + "")));
				g.setItem(9, this);
			}
		});
		a.setItem(10,new ItemGUI(createItem("&6Do Fall Damage", XMaterial.IRON_BOOTS, Arrays.asList(Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoFallDamage") + ""))) {
			@Override
			public void onClick(Player s, HolderGUI g, ClickType c) {
				boolean ddfire = Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".DoFallDamage");
				ddfire=!ddfire;
				Loader.mw.set("WorldsSettings." + w.getName() + ".DoFallDamage", ddfire);
				this.setItem(createItem("&6Do Fall Damage", XMaterial.IRON_BOOTS, Arrays.asList(ddfire + "")));
				g.setItem(10, this);
			}
		});
		int i = 10;
		if(Loader.mw.exists("WorldsSettings." + w.getName() + ".Gamerule"))
		for (String ds : Loader.mw.getKeys("WorldsSettings." + w.getName() + ".Gamerule")) {
			++i;
			int slot = i;
			XMaterial x = null;
			switch (ds) {
			case "COMMAND_BLOCK_OUTPUT":
				x = XMaterial.COMMAND_BLOCK;
				break;
			case "DO_TRADER_SPAWNING":
				x = XMaterial.LEAD;
				break;
			case "NATURAL_REGENERATION":
				x = XMaterial.RED_DYE;
				break;
			case "FORGIVE_DEAD_PLAYERS":
				x = XMaterial.GOLD_NUGGET;
				break;
			case "DISABLE_ELYTRA_MOVEMENT_CHECK":
				x = XMaterial.ELYTRA;
				break;
			case "DO_IMMEDIATE_RESPAWN":
				x = XMaterial.RED_BED;
				break;
			case "DO_INSOMNIA":
				x = XMaterial.RED_BED;
				break;
			case "DO_PATROL_SPAWNING":
				x = XMaterial.ENDER_EYE;
				break;
			case "DISABLE_RAIDS":
				x = XMaterial.IRON_AXE;
				break;
			case "DO_DAYLIGHT_CYCLE":
				x = XMaterial.CLOCK;
				break;
			case "DO_ENTITY_DROPS":
				x = XMaterial.ROTTEN_FLESH;
				break;
			case "DO_FIRE_TICK":
				x = XMaterial.FLINT_AND_STEEL;
				break;
			case "DO_LIMITED_CRAFTING":
				x = XMaterial.CRAFTING_TABLE;
				break;
			case "DO_MOB_LOOT":
				x = XMaterial.PORKCHOP;
				break;
			case "DO_TILE_DROPS":
				x = XMaterial.CHEST;
				break;
			case "DO_WEATHER_CYCLE":
				x = XMaterial.WATER_BUCKET;
				break;
			case "KEEP_INVENTORY":
				x = XMaterial.CHEST;
				break;
			case "LOG_ADMIN_COMMANDS":
				x = XMaterial.PAPER;
				break;
			case "MAX_COMMAND_CHAIN_LENGTH":
				x = XMaterial.CHAIN_COMMAND_BLOCK;
				break;
			case "MAX_ENTITY_CRAMMING":
				x = XMaterial.ZOMBIE_SPAWN_EGG;
				break;
			case "MOB_GRIEFING":
				x = XMaterial.OAK_DOOR;
				break;
			case "RANDOM_TICK_SPEED":
				x = XMaterial.GOLDEN_HOE;
				break;
			case "REDUCED_DEBUG_INFO":
				x = XMaterial.EGG;
				break;
			case "SEND_COMMAND_FEEDBACK":
				x = XMaterial.BOOK;
				break;
			case "SHOW_DEATH_MESSAGES":
				x = XMaterial.MOSSY_COBBLESTONE;
				break;
			case "SPAWN_RADIUS":
				x = XMaterial.GOLDEN_APPLE;
				break;
			case "SPECTATORS_GENERATE_CHUNKS":
				x = XMaterial.GRASS_BLOCK;
				break;
			case "ANNOUNCE_ADVANCEMENTS":
				x = XMaterial.DIAMOND;
				break;
			}
			XMaterial d = x;
			String name = WordUtils.capitalize(d.name().replace("_", " "));
			a.setItem(slot, new ItemGUI(createItem("&6" + name, d ,Arrays.asList(Loader.mw.getString("WorldsSettings." + w.getName() + ".Gamerule." + ds)))) {
				
				@Override
				public void onClick(Player p, HolderGUI g, ClickType c) {
					if(ds.equalsIgnoreCase("MAX_COMMAND_CHAIN_LENGTH")||ds.equalsIgnoreCase("MAX_ENTITY_CRAMMING")||ds.equalsIgnoreCase("RANDOM_TICK_SPEED")||ds.equalsIgnoreCase("SPAWN_RADIUS")) {
						switch(c) {
						case LEFT_PICKUP:
						case SHIFT_LEFT_DROP:
						case SHIFT_LEFT_PICKUP:
							Loader.mw.set("WorldsSettings." + w.getName() + ".Gamerule." + ds,
									Loader.mw.getInt("WorldsSettings." + w.getName() + ".Gamerule." + ds) + 1);
							Loader.mw.save();
							w.setGameRuleValue(ds,
									"" + (Loader.mw.getInt("WorldsSettings." + w.getName() + ".Gamerule." + ds) + 1));
							this.setItem(createItem("&6"+name, d, Arrays.asList(Loader.mw.getString("WorldsSettings." + w.getName() + ".Gamerule." + ds))));
							g.setItem(slot, this);
							break;
						case RIGHT_PICKUP:
						case SHIFT_RIGHT_DROP:
						case SHIFT_RIGHT_PICKUP:
							Loader.mw.set("WorldsSettings." + w.getName() + ".Gamerule." + ds,
									Loader.mw.getInt("WorldsSettings." + w.getName() + ".Gamerule." + ds) - 1);
							Loader.mw.save();
							w.setGameRuleValue(ds,
									"" + (Loader.mw.getInt("WorldsSettings." + w.getName() + ".Gamerule." + ds) - 1));
							this.setItem(createItem("&6"+name, d, Arrays.asList(Loader.mw.getString("WorldsSettings." + w.getName() + ".Gamerule." + ds))));
							g.setItem(slot, this);
							break;
						default:
							break;
						}
					}else {
						Loader.mw.set("WorldsSettings." + w.getName() + ".Gamerule." + ds, !Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".Gamerule." + ds));
						Loader.mw.save();
						w.setGameRuleValue(ds, "" + (!Loader.mw.getBoolean("WorldsSettings." + w.getName() + ".Gamerule." + ds)));
						this.setItem(createItem("&6"+name, d, Arrays.asList(Loader.mw.getString("WorldsSettings." + w.getName() + ".Gamerule." + ds))));
						g.setItem(slot, this);
					}
				}});
	}}
	
	public static ItemGUI empty=new ItemGUI(createItem(" ", XMaterial.BLACK_STAINED_GLASS_PANE, null)) {public void onClick(Player s, HolderGUI g, ClickType c) {};};

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
