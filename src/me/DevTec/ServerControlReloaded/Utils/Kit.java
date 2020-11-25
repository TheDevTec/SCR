package me.DevTec.ServerControlReloaded.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.APIs.EnchantmentAPI;
import me.DevTec.TheAPI.APIs.ItemCreatorAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Kit {
	private List<ItemStack> a;
	private HashMap<Integer, ItemStack> s;
	private double cost;
	private long delay;
	public List<ItemStack> getItems() {
		return a;
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
		if(Loader.kit.exists("Kits." + name + ".items.add")) {
			kit.a = new ArrayList<>();
			for (String id : Loader.kit.getKeys("Kits." + name + ".items.add")) {
				Material m = null;
				String mat = Loader.kit.getString("Kits." + name + ".items.add." + id + ".type");
				try{
					m=XMaterial.matchXMaterial(mat.toUpperCase()).get().parseMaterial();
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
					}catch(Exception er) {
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
				kit.a.add(a.create());
			}
		}
		if(Loader.kit.exists("Kits." + name + ".items.set")) {
			kit.s = new HashMap<>();
			for (String id : Loader.kit.getKeys("Kits." + name + ".items.set")) {
				Material m = null;
				String mat = Loader.kit.getString("Kits." + name + ".items.set." + id + ".type");
				try{
					m=XMaterial.matchXMaterial(mat.toUpperCase()).get().parseMaterial();
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
				kit.s.put(StringUtils.getInt(id), a.create());
			}
		}
		Loader.getInstance.kits.put(name.toLowerCase(), kit);
		return kit;
	}

	private String name;
	public String getName() {
		return name;
	}

	public String toString() {
		return "[Kit:"+name+"]";
	}
}
