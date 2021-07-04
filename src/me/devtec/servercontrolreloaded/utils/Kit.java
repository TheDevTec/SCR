package me.devtec.servercontrolreloaded.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.apis.EnchantmentAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.json.Reader;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.nms.nbt.NBTEdit;

public class Kit {
	private List<ItemStack> a;
	private List<String> b, c;
	private HashMap<Integer, ItemStack> s;
	private double cost;
	private long delay;
	public List<ItemStack> getItems() {
		return a;
	}

	public List<String> getCommands() {
		return b;
	}

	public List<String> getMessages() {
		return c;
	}
	
	public HashMap<Integer, ItemStack> getItemsWithSlots() {
		return s;
	}
	
	public double getCost() {
		return cost;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public static Kit load(String name) {
		Kit kit = new Kit();
			kit.name=name;
		if(Loader.kit.exists("Kits." + name + ".cost"))
			kit.cost=API.convertMoney(Loader.kit.getString("Kits." + name + ".cost"));
		if(Loader.kit.exists("Kits." + name + ".delay"))
			kit.delay=StringUtils.timeFromString(Loader.kit.getString("Kits." + name + ".delay"));
		kit.b=Loader.kit.getStringList("Kits." + name + ".commands");
		kit.c=Loader.kit.getStringList("Kits." + name + ".messages");
		if(Loader.kit.exists("Kits." + name + ".items.add")) {
			kit.a = new ArrayList<>();
			for (String id : Loader.kit.getKeys("Kits." + name + ".items.add")) {
				Material m = null;
				String mat = Loader.kit.getString("Kits." + name + ".items.add." + id + ".type");
				try{
					m=XMaterial.matchXMaterial(mat.toUpperCase()).getMaterial();
				}catch(Exception e) {}
				if (m == null) {
					Bukkit.getLogger().warning("Error when preparing (kit:" + name + ", id:"+id+" of material, section:add) " + mat + ", material is invalid");
					continue;
				}
				ItemCreatorAPI a = new ItemCreatorAPI(m);
				int numb = Loader.kit.getInt("Kits." + name + ".items.add." + id + ".amount");
				if(numb<1)numb=1;
				a.setAmount(numb);
				a.setLore(Loader.kit.getStringList("Kits." + name + ".items.add." + id + ".lore"));
				for (String flag : Loader.kit.getStringList("Kits." + name + ".items.add." + id + ".flags")) {
					try {
					a.addItemFlag(ItemFlag.valueOf(flag));
					}catch(Exception | NoSuchFieldError | NoSuchMethodError | NoClassDefFoundError er) {
						Bukkit.getLogger().warning("Error when preparing (kit:" + name + ", id:"+id+", section:add) of itemflag " + flag + ", itemflag is invalid");
					}
				}
				try {
				a.setUnbreakable(Loader.kit.getBoolean("Kits." + name + ".items.add." + id + ".unbreakable"));
				}catch(Exception | NoSuchMethodError err) {}
				if (Loader.kit.exists("Kits." + name + ".items.add." + id + ".color")) {
					String c = Loader.kit.getString("Kits." + name + ".items.add." + id + ".color");
					if(c.startsWith("#"))c=c.substring(1);
					a.setColor(Color.fromRGB(Integer.valueOf(c.substring(0, 2), 16), Integer.valueOf(c.substring(2, 4), 16), Integer.valueOf(c.substring(4, 6), 16)));
				}
				a.setDisplayName(Loader.kit.getString("Kits." + name + ".items.add." + id + ".name"));
				for (String enchs : Loader.kit.getStringList("Kits." + name + ".items.add." + id + ".enchants")) {
					String nonum = enchs.replaceAll("[^A-Za-z_]+", "").toUpperCase();
					if (EnchantmentAPI.byName(nonum)==null)
						Bukkit.getLogger().warning("Error when preparing (kit:" + name + ", id:"+id+", section:add) of enchantment " + enchs + " (Converted to "+nonum+"), enchantment is invalid");
					else
						a.addEnchantment(nonum, StringUtils.getInt(enchs.replaceAll("[^+0-9]+", ""))<=0?1:StringUtils.getInt(enchs.replaceAll("[^+0-9]+", "")));
				}
				kit.a.add(setNbt(a.create(),Loader.kit.getString("Kits." + name + ".items.add." + id + ".nbt")));
			}
		}
		if(Loader.kit.exists("Kits." + name + ".items.set")) {
			kit.s = new HashMap<>();
			for (String id : Loader.kit.getKeys("Kits." + name + ".items.set")) {
				Material m = null;
				String mat = Loader.kit.getString("Kits." + name + ".items.set." + id + ".type");
				try{
					m=XMaterial.matchXMaterial(mat.toUpperCase()).getMaterial();
				}catch(Exception e) {}
				if (m == null) {
					Bukkit.getLogger().warning("Error when preparing (kit:" + name + ", id:"+id+", section:set) of material " + mat + ", material is invalid");
					continue;
				}
				ItemCreatorAPI a = new ItemCreatorAPI(m);
				int numb = Loader.kit.getInt("Kits." + name + ".items.set." + id + ".amount");
				if(numb<1)numb=1;
				a.setAmount(numb);
				a.setLore(Loader.kit.getStringList("Kits." + name + ".items.set." + id + ".lore"));
				for (String flag : Loader.kit.getStringList("Kits." + name + ".items.set." + id + ".flags")) {
					try {
					a.addItemFlag(ItemFlag.valueOf(flag));
					}catch(Exception er) {
						Bukkit.getLogger().warning("Error when preparing (kit:" + name + ", id:"+id+", section:set) of itemflag " + flag + ", itemflag is invalid");
					}
				}
				try {
				a.setUnbreakable(Loader.kit.getBoolean("Kits." + name + ".items.set." + id + ".unbreakable"));
				}catch(Exception | NoSuchMethodError err) {}
				if (Loader.kit.exists("Kits." + name + ".items.set." + id + ".color")) {
					String c = Loader.kit.getString("Kits." + name + ".items.set." + id + ".color");
					if(c.startsWith("#"))c=c.substring(1);
					a.setColor(Color.fromRGB(Integer.valueOf(c.substring(0, 2), 16), Integer.valueOf(c.substring(2, 4), 16), Integer.valueOf(c.substring(4, 6), 16)));
				}
				a.setDisplayName(Loader.kit.getString("Kits." + name + ".items.set." + id + ".name"));
				for (String enchs : Loader.kit.getStringList("Kits." + name + ".items.set." + id + ".enchants")) {
					String nonum = enchs.replaceAll("[^A-Za-z_]+", "").toUpperCase();
					if (EnchantmentAPI.byName(nonum)==null)
						Bukkit.getLogger().warning("Error when preparing (kit:" + name + ", id:"+id+", section:set) of enchantment " + enchs + " (Converted to "+nonum+"), enchantment is invalid");
					else
						a.addEnchantment(nonum, StringUtils.getInt(enchs.replaceAll("[^+0-9]+", ""))<=0?1:StringUtils.getInt(enchs.replaceAll("[^+0-9]+", "")));
				}
				kit.s.put(StringUtils.getInt(id), setNbt(a.create(),Loader.kit.getString("Kits." + name + ".items.set." + id + ".nbt")));
			}
		}
		return kit;
	}

	private static ItemStack setNbt(ItemStack create, String string) {
		if(string!=null && string.startsWith("{") && string.endsWith("}")) {
			NBTEdit e = new NBTEdit(create);
			@SuppressWarnings("unchecked")
			Map<String, Object> vals = (Map<String, Object>)Reader.read(string);
			for(Entry<String, Object> a : vals.entrySet()) {
				if(a.getValue() instanceof String)
				e.setString(a.getKey(), (String) a.getValue());
				else
				if(a.getValue() instanceof Double)
				e.setDouble(a.getKey(), (Double) a.getValue());
				else
				if(a.getValue() instanceof Long)
				e.setLong(a.getKey(), (Long) a.getValue());
				else
				if(a.getValue() instanceof Integer)
				e.setInt(a.getKey(), (Integer) a.getValue());
				else
				if(a.getValue() instanceof Float)
				e.setFloat(a.getKey(), (Float) a.getValue());
				else
				if(a.getValue() instanceof int[])
				e.setIntArray(a.getKey(), (int[]) a.getValue());
				else
				if(a.getValue() instanceof byte[])
				e.setByteArray(a.getKey(), (byte[]) a.getValue());
				else
				if(a.getValue() instanceof Byte)
				e.setByte(a.getKey(), (Byte) a.getValue());
				else
				if(a.getValue() instanceof Boolean)
				e.setBoolean(a.getKey(), (Boolean) a.getValue());
				else
				e.set(a.getKey(), a.getValue());
			}
			create=NMSAPI.setNBT(create,e);
		}
		return create;
	}

	private String name;
	public String getName() {
		return name;
	}

	public String toString() {
		return "[Kit:"+name+"]";
	}
}
