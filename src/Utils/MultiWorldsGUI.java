package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.GUICreatorAPI;
import me.Straiker123.GUICreatorAPI.Options;
import me.Straiker123.ItemCreatorAPI;
import me.Straiker123.TheAPI;

public class MultiWorldsGUI {
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem(String name, XMaterial material, List<String> lore) {
	
		 ItemCreatorAPI a=TheAPI.getItemCreatorAPI(new ItemStack(material.parseMaterial()));
		 try {
		 if(material.getData()!=0)
		 a =TheAPI.getItemCreatorAPI(new ItemStack(material.parseMaterial()
				,1,(short)1,material.getData()));
		 }catch(Exception e) {
		 a =TheAPI.getItemCreatorAPI(material.parseMaterial());
		 }
		a.setLore(lore);
        a.setDisplayName(name); 
        return a.create();
    }

	public static ItemStack createItem(String name,List<String> lore) {
		Material torch =null;
		if(TheAPI.isNewVersion())
			torch=Material.matchMaterial("REDSTONE_TORCH");
		else
			torch = Material.matchMaterial("REDSTONE_TORCH_ON");
		ItemCreatorAPI a=TheAPI.getItemCreatorAPI(torch);
		a.setLore(lore);
        a.setDisplayName(name); 
        return a.create();
    }
	
	public static void openInv(Player p) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&cMultiWorlds Editor");
		a.setSize(54);
		prepareInv(a);
		HashMap<Options, Object> create = new HashMap<Options, Object>();
		create.put(Options.CANT_BE_TAKEN,true);
		create.put(Options.CANT_PUT_ITEM,true);
		create.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(API.hasPerm(p, "ServerControl.MultiWorld.Create")) {
					openInvCreate(p);
				}
			}});
		
		HashMap<Options, Object> delete = new HashMap<Options, Object>();
		delete.put(Options.CANT_BE_TAKEN,true);
		delete.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(API.hasPerm(p, "ServerControl.MultiWorld.Delete")) {
					openInvDelete(p);
				}}});
		
		HashMap<Options, Object> setspawn = new HashMap<Options, Object>();
		setspawn.put(Options.CANT_BE_TAKEN,true);
		setspawn.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(API.hasPerm(p, "ServerControl.MultiWorld.SetSpawn")) {
					Loader.mw.set("WorldsSettings."+p.getWorld().getName()+".Spawn.World", p.getWorld().getName());
					Loader.mw.set("WorldsSettings."+p.getWorld().getName()+".Spawn.X", p.getLocation().getX());
					Loader.mw.set("WorldsSettings."+p.getWorld().getName()+".Spawn.Y", p.getLocation().getY());
					Loader.mw.set("WorldsSettings."+p.getWorld().getName()+".Spawn.Z", p.getLocation().getZ());
					Loader.mw.set("WorldsSettings."+p.getWorld().getName()+".Spawn.X_Pos_Head", p.getLocation().getYaw());
					Loader.mw.set("WorldsSettings."+p.getWorld().getName()+".Spawn.Z_Pos_Head", p.getLocation().getPitch());
					Configs.mw.save();Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.SpawnSet")
					.replace("%world%",p.getWorld().getName()),p);
				}
			}});
		
		a.setItem(20, createItem("&aCreate", XMaterial.GREEN_WOOL, Arrays.asList("&7This will send you to World &acreate &7menu")),create);
		a.setItem(21, createItem("&cDelete", XMaterial.RED_WOOL, Arrays.asList("&7This will send you to World &cdelete &7menu")),delete);
		HashMap<Options, Object> load = new HashMap<Options, Object>();
		load.put(Options.CANT_BE_TAKEN,true);
		load.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(API.hasPerm(p, "ServerControl.MultiWorld.Load")) {
				openInvLoad(p);
				}
			}});
		HashMap<Options, Object> unload = new HashMap<Options, Object>();
		unload.put(Options.CANT_BE_TAKEN,true);
		unload.put(Options.RUNNABLE, new Runnable() {
				@Override
				public void run() {
					if(API.hasPerm(p, "ServerControl.MultiWorld.Unload")) {
						openInvUnload(p);
					}
				}});
		a.setItem(23, createItem("&bLoad", XMaterial.LIGHT_BLUE_WOOL, Arrays.asList("&7This will send you to World &bload &7menu")),load);
		a.setItem(24, createItem("&eUnload", XMaterial.YELLOW_WOOL, Arrays.asList("&7This will send you to World &eunload &7menu")),unload);
		HashMap<Options, Object> set = new HashMap<Options, Object>();
		set.put(Options.CANT_BE_TAKEN,true);
		set.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(API.hasPerm(p, "ServerControl.MultiWorld.Set")) {
					openInvSet(p);
				}
			}});
		a.setItem(29, createItem("&2World settings", Arrays.asList("&7This will send you to World &2Set &7menu")),set);
		a.setItem(30,createItem("&6&lSetSpawn", XMaterial.COMPASS, Arrays.asList("&7This will &6set world spawn &7to your location")),setspawn);
		HashMap<Options, Object> tp = new HashMap<Options, Object>();
		tp.put(Options.CANT_BE_TAKEN,true);
		tp.put(Options.CANT_PUT_ITEM,true);
		tp.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(API.hasPerm(p, "ServerControl.MultiWorld.Tp")) {
					openInvTeleport(p);
				}
			}});
		a.setItem(33,createItem("&5Teleport", XMaterial.ENDER_PEARL, Arrays.asList("&7This will &5teleport you to world")),tp);
		HashMap<Options, Object> list = new HashMap<Options, Object>();
		list.put(Options.CANT_BE_TAKEN,true);
		list.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(API.hasPerm(p, "ServerControl.MultiWorld.List")) {
					openInvList(p);
				}
			}});
		a.setItem(32,createItem("&eWorld List and Info",XMaterial.BOOK, Arrays.asList("&7Click to see", "&7all worlds and informations about worlds")),list);
		a.open();
	}
	public static void openInvList(Player p) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&bWorlds list");
		a.setSize(54);
		prepareInv(a);
		for(World w:Bukkit.getWorlds()) {
		HashMap<Options, Object> help = new HashMap<Options, Object>();
		help.put(Options.CANT_BE_TAKEN,true);
		help.put(Options.CANT_PUT_ITEM,true);
		List<String> lore = new ArrayList<String>();
		String m = "GRASS_BLOCK";
		String start = "&2";
		if(w.getEnvironment()==Environment.NORMAL) {
			if(Loader.mw.getString("WorldsSettings."+w.getName()+".Generator")==null)
				lore.add("&7 - Normal");
			else {
				if(Loader.mw.getString("WorldsSettings."+w.getName()+".Generator").equalsIgnoreCase("flat")) {
					lore.add("&7 - Flat");
					start="&a";
				}
				if(Loader.mw.getString("WorldsSettings."+w.getName()+".Generator").equalsIgnoreCase("normal")) {
					lore.add("&7 - Normal");
				}
				if(Loader.mw.getString("WorldsSettings."+w.getName()+".Generator").equalsIgnoreCase("the_void")) {
					lore.add("&7 - The Void");
					start="&8";
					m="GLASS";
				}
			}
		}
		if(w.getEnvironment()==Environment.NETHER) {
			lore.add("&7 - Nether");
			start="&c";
			m= "NETHERRACK";
		}
		if(w.getEnvironment()==Environment.THE_END) {
			lore.add("&7 - The End");
			start="&e";
			m= "END_STONE";
		}
		lore.add("&7 - PvP: "+w.getPVP());
		lore.add("&7 - No Mobs: "+Loader.mw.getBoolean("WorldsSettings."+w.getName()+".NoMobs"));
		lore.add("&7 - AutoSave: "+w.isAutoSave());
		String gm = "SURVIVAL";
		if(Loader.mw.getString("WorldsSettings."+w.getName()+".GameMode")!=null)
			gm=Loader.mw.getString("WorldsSettings."+w.getName()+".GameMode");
		lore.add("&7 - Keep Spawn In Memory: "+w.getKeepSpawnInMemory());
		lore.add("&7 - Difficulty: "+w.getDifficulty());
		lore.add("&7 - GameMode: "+gm);
		lore.add("&7 - Loaded Chunks: "+w.getLoadedChunks().length);
		lore.add("&7 - Entities: "+w.getEntities().size());
		lore.add("&7 - Players: "+w.getPlayers().size());
		
		a.addItem(createItem(start+w.getName(),XMaterial.valueOf(m), lore),help);
		}
		HashMap<Options, Object> back = new HashMap<Options, Object>();
		back.put(Options.CANT_BE_TAKEN,true);
		back.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInv(p);
			}});
		a.setItem(49, createItem("&cBack", XMaterial.BARRIER, null),back);
	
		a.open();
	}
	public static void openInvGen(Player p) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&2World creator - Generator");
		a.setSize(54);
		prepareInv(a);
		HashMap<Options, Object> d = new HashMap<Options, Object>();
		d.put(Options.CANT_BE_TAKEN,true);

		d.put(Options.CANT_PUT_ITEM,true);
		d.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				Loader.me.set("Players."+p.getName()+".MultiWorlds-Generator","NORMAL");
				openInvCreate(p);
			}});
		a.setItem(20, createItem("&2Normal", XMaterial.GRASS_BLOCK, Arrays.asList("&7Default generator")), d);


		HashMap<Options, Object> n = new HashMap<Options, Object>();
		n.put(Options.CANT_BE_TAKEN,true);
		n.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				Loader.me.set("Players."+p.getName()+".MultiWorlds-Generator","NETHER");
				openInvCreate(p);
			}});
		a.setItem(22, createItem("&cNether", XMaterial.NETHERRACK, Arrays.asList("&7Nether generator")), n);
		HashMap<Options, Object> t = new HashMap<Options, Object>();
		t.put(Options.CANT_BE_TAKEN,true);
		t.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				Loader.me.set("Players."+p.getName()+".MultiWorlds-Generator","THE_END");
				openInvCreate(p);
			}});
		a.setItem(31, createItem("&8The End", XMaterial.END_STONE, Arrays.asList("&7The End generator")), t);
		HashMap<Options, Object> f = new HashMap<Options, Object>();
		f.put(Options.CANT_BE_TAKEN,true);
		f.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				Loader.me.set("Players."+p.getName()+".MultiWorlds-Generator","FLAT");
				openInvCreate(p);
			}});
		a.setItem(29, createItem("&aFlat", XMaterial.GRASS_BLOCK, Arrays.asList("&7Flat generator")), f);
		HashMap<Options, Object> v = new HashMap<Options, Object>();
		v.put(Options.CANT_BE_TAKEN,true);
		v.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				Loader.me.set("Players."+p.getName()+".MultiWorlds-Generator","THE_VOID");
				openInvCreate(p);
			}});
		a.setItem(33, createItem("&7Void", XMaterial.GLASS, Arrays.asList("&7Void generator")), v);
		
		HashMap<Options, Object> back = new HashMap<Options, Object>();
		back.put(Options.CANT_BE_TAKEN,true);
		back.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInvCreate(p);
			}});
		a.setItem(49, createItem("&cBack", XMaterial.BARRIER, null),back);
		a.open();
	}
	public static void openInvLoad(Player p) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&bWorld loader");
		a.setSize(54);
		prepareInv(a);
		if(Loader.mw.getString("Unloaded-Worlds")!=null && Loader.mw.getStringList("Unloaded-Worlds").isEmpty()==false)
		for(String w:Loader.mw.getStringList("Unloaded-Worlds")) {
		HashMap<Options, Object> help = new HashMap<Options, Object>();
		help.put(Options.CANT_BE_TAKEN,true);
		help.put(Options.CANT_PUT_ITEM,true);
		help.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				MultiWorldsUtils.LoadWorld(w, p);
				openInvLoad(p);
			}});
		String biome =Loader.mw.getString("WorldsSettings."+w+".Generator");
		if(biome==null)return;
		
		if(biome.equalsIgnoreCase("NORMAL")) {
			a.addItem(createItem("&2"+w,XMaterial.GRASS_BLOCK, Arrays.asList("&7Click to load world","&7 - Normal")),help);	
		}else
		if(biome.equalsIgnoreCase("THE_END")) {
			a.addItem(createItem("&5"+w,XMaterial.END_STONE, Arrays.asList("&7Click to load world","&7 - The End")),help);
		}else
		if(biome.equalsIgnoreCase("NETHER")) {
			a.addItem(createItem("&4"+w,XMaterial.NETHERRACK, Arrays.asList("&7Click to load world","&7 - Nether")),help);
		}else
		if(biome.equalsIgnoreCase("FLAT")) {
			a.addItem(createItem("&a"+w,XMaterial.GREEN_STAINED_GLASS_PANE, Arrays.asList("&7Click to load world","&7 - flat")),help);
		}else
		if(biome.equalsIgnoreCase("THE_VOID")) {
			a.addItem(createItem("&7"+w,XMaterial.GLASS, Arrays.asList("&7Click to load world","&7 - The Void")),help);
		}
		HashMap<Options, Object> back = new HashMap<Options, Object>();
		back.put(Options.CANT_BE_TAKEN,true);
		back.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInv(p);
			}});
		a.setItem(49, createItem("&cBack", XMaterial.BARRIER, null),back);
	
		a.open();
	}}
	public static void openInvCreate(Player p) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&2World creator");
		a.setSize(54);
		prepareInv(a);
		HashMap<Options, Object> help = new HashMap<Options, Object>();
		help.put(Options.CANT_BE_TAKEN,true);
		help.put(Options.CANT_PUT_ITEM,true);
		help.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				p.closeInventory();
				TheAPI.getPlayerAPI(p).sendTitle("&2Write world name", "&2To the chat.");
				TheAPI.sendActionBar(p,"&6Type &0'&ccancel&0' &6to cancel.");
				TheAPI.getCooldownAPI("world-create").createCooldown(p.getName(), 30);
			}});
		String ad = "&cnone";
		if(Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create")!=null) {
			ad="&a"+Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create");
		String name = "&7 - "+ad;
		a.setItem(20, createItem("&aWorld Name", XMaterial.GREEN_WOOL, Arrays.asList("&7World name", name)), help);
		}else {
			String name = "&7 - "+ad;
			a.setItem(20, createItem("&aWorld Name", XMaterial.RED_WOOL, Arrays.asList("&7World name", name)), help);
		}
		
		HashMap<Options, Object> w = new HashMap<Options, Object>();
		w.put(Options.CANT_BE_TAKEN,true);
		w.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInvGen(p);
			}});
		
		String ads = "&cnone";
		if(Loader.me.getString("Players."+p.getName()+".MultiWorlds-Generator")!=null) {
			ads="&a"+Loader.me.getString("Players."+p.getName()+".MultiWorlds-Generator");
		String get = "&7 - "+ads;
		a.setItem(24, createItem("&aGenerator type", XMaterial.GREEN_WOOL, Arrays.asList("&7World generator",get)),w);
	}else {
		String get = "&7 - "+ads;
		a.setItem(24, createItem("&aGenerator type", XMaterial.RED_WOOL, Arrays.asList("&7World generator", get)), w);
	}
		HashMap<Options, Object> c = new HashMap<Options, Object>();
		c.put(Options.CANT_BE_TAKEN,true);
		c.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create") != null && Loader.me.getString("Players."+p.getName()+".MultiWorlds-Generator")!=null) {
				Loader.mw.set("WorldsSettings."+Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create")+".Generator",Loader.me.getString("Players."+p.getName()+".MultiWorlds-Generator"));
				MultiWorldsUtils.CreateWorld(Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create"), p);
				Loader.me.set("Players."+p.getName()+".MultiWorlds-Generator",null);
				Loader.me.set("Players."+p.getName()+".MultiWorlds-Create",null);
				Configs.chatme.save();
				Configs.mw.save();
				openInv(p);
				}
			}});
		String aads="&cnone";
		if(Loader.me.getString("Players."+p.getName()+".MultiWorlds-Generator")!=null)
			 aads="&a"+Loader.me.getString("Players."+p.getName()+".MultiWorlds-Generator");
		String aad="&cnone";
		if(Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create")!=null)
			 aad="&a"+Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create");
		
		String m ="RED_TERRACOTTA";

		if(Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create")==null 
				&& Loader.me.getString("Players."+p.getName()+".MultiWorlds-Generator")!=null
				||Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create")!=null 
				&& Loader.me.getString("Players."+p.getName()+".MultiWorlds-Generator")==null)
		m="ORANGE_TERRACOTTA";
		
		if(Loader.me.getString("Players."+p.getName()+".MultiWorlds-Create")!=null 
				&& Loader.me.getString("Players."+p.getName()+".MultiWorlds-Generator")!=null)
		m="GREEN_TERRACOTTA";
		a.setItem(40, createItem("&aCreate", XMaterial.valueOf(m), Arrays.asList("&7Current options","&7 - &aGenerator: &6"+aads,"&7 - &aWorld name: &6"+aad)),c);
		
		HashMap<Options, Object> back = new HashMap<Options, Object>();
		back.put(Options.CANT_BE_TAKEN,true);
		back.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				Loader.me.set("Players."+p.getName()+".MultiWorlds-Generator",null);
				Loader.me.set("Players."+p.getName()+".MultiWorlds-Create",null);
				Configs.chatme.save();
				openInv(p);
			}});
		a.setItem(49, createItem("&cCancel", XMaterial.BARRIER, null),back);
	
		a.open();
	}
	public static void openInvDelete(Player p) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&cWorld remover");
		a.setSize(54);
		prepareInv(a);
		for(World w:Bukkit.getWorlds()) {
		HashMap<Options, Object> help = new HashMap<Options, Object>();
		help.put(Options.CANT_BE_TAKEN,true);
		help.put(Options.CANT_PUT_ITEM,true);
		help.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				List<String> ww = Loader.mw.getStringList("Unloaded-Worlds");
				List<String> worlds = Loader.mw.getStringList("Worlds");
				worlds.remove(w.getName());
				ww.remove(w.getName());
				Loader.mw.set("Worlds", worlds);
				Configs.mw.save();
				if(TheAPI.getWorldsManager().delete(w, true)) {
					Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.Deleted").replace("%world%", w.getName()), p);
					}else {
						Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.CantBeDeleted").replace("%world%", w.getName()), p);
					}
				openInvDelete(p);
			}});
			if(w.getEnvironment()==Environment.NORMAL)
			a.addItem(createItem(w.getName(),XMaterial.GRASS_BLOCK, Arrays.asList("&7Click to delete world")),help);
			else
			if(w.getEnvironment()==Environment.NETHER)
			a.addItem(createItem(w.getName(),XMaterial.NETHERRACK, Arrays.asList("&7Click to delete world")),help);
			else
			if(w.getEnvironment()==Environment.THE_END)
			a.addItem(createItem(w.getName(),XMaterial.END_STONE, Arrays.asList("&7Click to delete world")),help);
		}
		HashMap<Options, Object> back = new HashMap<Options, Object>();
		back.put(Options.CANT_BE_TAKEN,true);
		back.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInv(p);
			}});
		a.setItem(49, createItem("&cBack", XMaterial.BARRIER, null),back);
	
		a.open();
	}
	public static void openInvUnload(Player p) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&eWorld unloader");
		a.setSize(54);
		prepareInv(a);
		for(World w:Bukkit.getWorlds()) {
		HashMap<Options, Object> help = new HashMap<Options, Object>();
		help.put(Options.CANT_BE_TAKEN,true);
		help.put(Options.CANT_PUT_ITEM,true);
		help.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				MultiWorldsUtils.UnloadWorld(w.getName(), p);
				openInvUnload(p);
			}});
			if(w.getEnvironment()==Environment.NORMAL)
			a.addItem(createItem(w.getName(),XMaterial.GRASS_BLOCK, Arrays.asList("&7Click to load world")),help);
			else
			if(w.getEnvironment()==Environment.NETHER)
			a.addItem(createItem(w.getName(),XMaterial.NETHERRACK, Arrays.asList("&7Click to load world")),help);
			else
			if(w.getEnvironment()==Environment.THE_END)
			a.addItem(createItem(w.getName(),XMaterial.END_STONE, Arrays.asList("&7Click to load world")),help);
		}
		HashMap<Options, Object> back = new HashMap<Options, Object>();
		back.put(Options.CANT_BE_TAKEN,true);
		back.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInv(p);
			}});
		a.setItem(49, createItem("&cBack", XMaterial.BARRIER, null),back);
	
		a.open();
	}
	public static void openInvTeleport(Player p) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&5World teleporter");
		a.setSize(54);
		prepareInv(a);
		for(World w:Bukkit.getWorlds()) {
		HashMap<Options, Object> help = new HashMap<Options, Object>();
		help.put(Options.CANT_BE_TAKEN,true);
		help.put(Options.CANT_PUT_ITEM,true);
		help.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				p.getOpenInventory().close();
				int x =Loader.mw.getInt("WorldsSettings."+w.getName()+".Spawn.X");
				int y = Loader.mw.getInt("WorldsSettings."+w.getName()+".Spawn.Y");
				int z =Loader.mw.getInt("WorldsSettings."+w.getName()+".Spawn.Z");
				int x_head = Loader.mw.getInt("WorldsSettings."+w.getName()+".Spawn.X_Pos_Head");
				int z_head = Loader.mw.getInt("WorldsSettings."+w.getName()+".Spawn.Z_Pos_Head");
				Location loc = new Location(Bukkit.getWorld(Loader.mw.getString("WorldsSettings."+w.getName()+".Spawn.World")), x, y, z, x_head, z_head);
				API.setBack(p);
				TheAPI.getPlayerAPI(p).setGodOnTime(20);
				TheAPI.getPlayerAPI(p).teleport(loc);
				
				Loader.msg(Loader.s("Prefix")+Loader.s("MultiWorld.TeleportedWorld")
				.replace("%world%", w.getName()),p);
			}});
			if(w.getEnvironment()==Environment.NORMAL)
			a.addItem(createItem(w.getName(),XMaterial.GRASS_BLOCK, Arrays.asList("&7Click to teleport to the world '"+w.getName()+"'")),help);
			else
			if(w.getEnvironment()==Environment.NETHER)
			a.addItem(createItem(w.getName(),XMaterial.NETHERRACK, Arrays.asList("&7Click to teleport to the world '"+w.getName()+"'")),help);
			else
			if(w.getEnvironment()==Environment.THE_END)
			a.addItem(createItem(w.getName(),XMaterial.END_STONE, Arrays.asList("&7Click to teleport to the world '"+w.getName()+"'")),help);
		}
		HashMap<Options, Object> back = new HashMap<Options, Object>();
		back.put(Options.CANT_BE_TAKEN,true);
		back.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInv(p);
			}});
		a.setItem(49, createItem("&cBack", XMaterial.BARRIER, null),back);
	
		a.open();
	}
	public static void openInvSet(Player p) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&6World setting");
		a.setSize(54);
		prepareInv(a);
		HashMap<Options, Object> back = new HashMap<Options, Object>();
		back.put(Options.CANT_BE_TAKEN,true);
		back.put(Options.CANT_PUT_ITEM,true);
		back.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInv(p);
			}});
		a.setItem(49, createItem("&cBack", XMaterial.BARRIER, null),back);
		for(World w:Bukkit.getWorlds()) {
		HashMap<Options, Object> help = new HashMap<Options, Object>();
		help.put(Options.CANT_BE_TAKEN,true);
		help.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInvSetWorld(p, w);
			}});
		if(w.getEnvironment()==Environment.NORMAL)
			a.addItem(createItem(w.getName(),XMaterial.GRASS_BLOCK, Arrays.asList("&7Click to open setting of world '"+w.getName()+"'")),help);
			else
			if(w.getEnvironment()==Environment.NETHER)
			a.addItem(createItem(w.getName(),XMaterial.NETHERRACK, Arrays.asList("&7Click to open setting of world '"+w.getName()+"'")),help);
			else
			if(w.getEnvironment()==Environment.THE_END)
			a.addItem(createItem(w.getName(),XMaterial.END_STONE, Arrays.asList("&7Click to open setting of world '"+w.getName()+"'")),help);
		}
		a.open();
	}
	public static void openInvSetWorld(Player p, World w) {
		GUICreatorAPI a = TheAPI.getGUICreatorAPI(p);
		a.setTitle("&6World setting - "+w.getName());
		a.setSize(54);
		prepareInv(a);
		HashMap<Options, Object> back = new HashMap<Options, Object>();
		back.put(Options.CANT_BE_TAKEN,true);
		back.put(Options.CANT_PUT_ITEM,true);
		back.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				openInvSet(p);
			}});
		a.setItem(49, createItem("&cBack", XMaterial.BARRIER, null),back);
		

		String dif = w.getDifficulty().name();
		HashMap<Options, Object> df = new HashMap<Options, Object>();
		df.put(Options.CANT_BE_TAKEN,true);
		df.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(Difficulty.valueOf(dif)==Difficulty.EASY) {
					Loader.mw.set("WorldsSettings."+w.getName()+".Difficulty", "NORMAL");
					w.setDifficulty(Difficulty.NORMAL);
				}else
					if(Difficulty.valueOf(dif)==Difficulty.NORMAL) {
						Loader.mw.set("WorldsSettings."+w.getName()+".Difficulty", "HARD");
						w.setDifficulty(Difficulty.HARD);
					}else
						if(Difficulty.valueOf(dif)==Difficulty.HARD) {
							Loader.mw.set("WorldsSettings."+w.getName()+".Difficulty", "PEACEFUL");
							w.setDifficulty(Difficulty.PEACEFUL);
						}else
							if(Difficulty.valueOf(dif)==Difficulty.PEACEFUL) {
								Loader.mw.set("WorldsSettings."+w.getName()+".Difficulty", "EASY");
								w.setDifficulty(Difficulty.EASY);
							}
				Configs.mw.save();
				openInvSetWorld(p,w);
			}});
		a.setItem(10, createItem("&6Difficulty", XMaterial.FEATHER, Arrays.asList(dif)),df);

		String g ="SURVIVAL";
		if(Loader.mw.getString("WorldsSettings."+w.getName()+".GameMode")!=null)
		 g=Loader.mw.getString("WorldsSettings."+w.getName()+".GameMode");
		HashMap<Options, Object> gg = new HashMap<Options, Object>();
		gg.put(Options.CANT_BE_TAKEN,true);
		gg.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {

				String g ="SURVIVAL";
				if(Loader.mw.getString("WorldsSettings."+w.getName()+".GameMode")!=null)
				 g=Loader.mw.getString("WorldsSettings."+w.getName()+".GameMode");
				if(GameMode.valueOf(g)==GameMode.SURVIVAL) {
					Loader.mw.set("WorldsSettings."+w.getName()+".GameMode", "CREATIVE");
					for(Player p:TheAPI.getOnlinePlayers())if(p.getWorld()==w)p.setGameMode(GameMode.CREATIVE);
				}else
					if(GameMode.valueOf(g)==GameMode.CREATIVE) {
						Loader.mw.set("WorldsSettings."+w.getName()+".GameMode", "SPECTATOR");
						for(Player p:TheAPI.getOnlinePlayers())if(p.getWorld()==w)p.setGameMode(GameMode.SPECTATOR);
					}else
						if(GameMode.valueOf(g)==GameMode.SPECTATOR) {
							Loader.mw.set("WorldsSettings."+w.getName()+".GameMode", "ADVENTURE");
							for(Player p:TheAPI.getOnlinePlayers())if(p.getWorld()==w)p.setGameMode(GameMode.ADVENTURE);
						}else
							if(GameMode.valueOf(g)==GameMode.ADVENTURE) {
								Loader.mw.set("WorldsSettings."+w.getName()+".GameMode", "SURVIVAL");
								for(Player p:TheAPI.getOnlinePlayers())if(p.getWorld()==w)p.setGameMode(GameMode.SURVIVAL);
							}
				Configs.mw.save();
				openInvSetWorld(p,w);
			}});
		a.setItem(11, createItem("&6Gamemode", XMaterial.BRICKS, Arrays.asList(g)),gg);
		
		boolean s = Loader.mw.getBoolean("WorldsSettings."+w.getName()+".KeepSpawnInMemory");
		HashMap<Options, Object> k = new HashMap<Options, Object>();
		k.put(Options.CANT_BE_TAKEN,true);
		k.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(s==true) {
					Loader.mw.set("WorldsSettings."+w.getName()+".KeepSpawnInMemory", false);
					w.setKeepSpawnInMemory(false);
				}else {
					Loader.mw.set("WorldsSettings."+w.getName()+".KeepSpawnInMemory", true);
				w.setKeepSpawnInMemory(true);}
				Configs.mw.save();
				openInvSetWorld(p,w);
			}});
		a.setItem(12, createItem("&6Keep Spawn In Memory", XMaterial.MAP, Arrays.asList(s+"")),k);
		

		boolean sa = Loader.mw.getBoolean("WorldsSettings."+w.getName()+".AutoSave");
		HashMap<Options, Object> ka = new HashMap<Options, Object>();
		ka.put(Options.CANT_BE_TAKEN,true);
		ka.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(sa==true) {
					Loader.mw.set("WorldsSettings."+w.getName()+".AutoSave", false);
					w.setAutoSave(false);
				}else {
					Loader.mw.set("WorldsSettings."+w.getName()+".AutoSave", true);
					w.setAutoSave(true);
				}
				Configs.mw.save();
				openInvSetWorld(p,w);
			}});
		a.setItem(13, createItem("&6Auto Save", XMaterial.EMERALD_BLOCK, Arrays.asList(sa+"")),ka);


		boolean sas = Loader.mw.getBoolean("WorldsSettings."+w.getName()+".PvP");
		HashMap<Options, Object> kas = new HashMap<Options, Object>();
		kas.put(Options.CANT_BE_TAKEN,true);
		kas.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(sas==true) {
					Loader.mw.set("WorldsSettings."+w.getName()+".PvP", false);
					w.setPVP(false);
				}else {
					Loader.mw.set("WorldsSettings."+w.getName()+".PvP", true);
					w.setPVP(true);
				}
				Configs.mw.save();
				openInvSetWorld(p,w);
			}});
		a.setItem(14, createItem("&6PvP", XMaterial.DIAMOND_SWORD, Arrays.asList(sas+"")),kas);

		boolean sass = Loader.mw.getBoolean("WorldsSettings."+w.getName()+".CreatePortal");
		HashMap<Options, Object> kass = new HashMap<Options, Object>();
		kass.put(Options.CANT_BE_TAKEN,true);
		kass.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(sass==true) {
					Loader.mw.set("WorldsSettings."+w.getName()+".CreatePortal", false);
				}else
					Loader.mw.set("WorldsSettings."+w.getName()+".CreatePortal", true);
				Configs.mw.save();
				openInvSetWorld(p,w);
			}});
		a.setItem(15, createItem("&6Can be portal created in this world", XMaterial.OBSIDIAN, Arrays.asList(sass+"")),kass);

		boolean sasss = Loader.mw.getBoolean("WorldsSettings."+w.getName()+".PortalTeleport");
		HashMap<Options, Object> kasss = new HashMap<Options, Object>();
		kasss.put(Options.CANT_BE_TAKEN,true);
		kasss.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(sasss==true) {
					Loader.mw.set("WorldsSettings."+w.getName()+".PortalTeleport", false);
				}else
					Loader.mw.set("WorldsSettings."+w.getName()+".PortalTeleport", true);
				Configs.mw.save();
				openInvSetWorld(p,w);
			}});
		a.setItem(16, createItem("&6Can be portal used in this world", XMaterial.ENDER_PEARL, Arrays.asList(sasss+"")),kasss);
		boolean sassw = Loader.mw.getBoolean("WorldsSettings."+w.getName()+".NoMobs");
		HashMap<Options, Object> kassw = new HashMap<Options, Object>();
		kassw.put(Options.CANT_BE_TAKEN,true);
		kassw.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				if(sassw==true) {
					Loader.mw.set("WorldsSettings."+w.getName()+".NoMobs", false);
				}else
					Loader.mw.set("WorldsSettings."+w.getName()+".NoMobs", true);
				Configs.mw.save();
				openInvSetWorld(p,w);
			}});
		a.setItem(19, createItem("&6No Mobs", XMaterial.CREEPER_HEAD, Arrays.asList(sassw+"")),kassw);
		a.open();
		}
	public static void prepareInv(GUICreatorAPI a) {
    	HashMap<Options, Object> notake = new HashMap<Options, Object>();
		notake.put(Options.CANT_BE_TAKEN,true);
		ItemStack item = createItem(" ", XMaterial.BLACK_STAINED_GLASS_PANE, null);
    	for(int i =0; i<10; ++i)
            a.setItem(i, item, notake);
        a.setItem(17, item, notake);
        a.setItem(18, item, notake);
        a.setItem(26, item, notake);
        a.setItem(27, item, notake);
        a.setItem(35, item, notake);
        a.setItem(36, item, notake);
    	for(int i =44; i<54; ++i)
        a.setItem(i, item, notake);
    }
}
