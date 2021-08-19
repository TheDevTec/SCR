package me.devtec.servercontrolreloaded.commands.other.guis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.devtec.servercontrolreloaded.utils.HDBSupport;
import me.devtec.theapi.apis.EnchantmentAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;

public class ItemBuilder {
	final String name;
    String head;
	final List<String> lore;
	List<ItemFlag> itemflags;
	Map<Enchantment, Integer> enchants;
	String model;
    final String amount;
    String data;
	Material type;
	
	final String path;
	public ItemBuilder(String path, Material type, String data, String amount, String name, List<String> lore, String model, List<String> itemflags, List<String> enchants) {
		this.type=type;
		this.amount=amount;
		this.name=name;
		this.data=data;
		this.lore=lore;
		this.model=model;
		this.path=path;
		List<ItemFlag> itemFlags = new ArrayList<>();
		for(String f : itemflags) {
			try {
				itemFlags.add(ItemFlag.valueOf(f.toUpperCase()));
			}catch(Exception er) {
				Bukkit.getLogger().severe("ItemFlag named "+f+" doesn't exist");
			}
		}
		Map<Enchantment, Integer> ec = new HashMap<>();
		for(String f : enchants) {
			try {
				ec.put(EnchantmentAPI.byName(f.split("[: ,0-9]+")[0]).getEnchantment(), f.contains(":")?StringUtils.getInt(f):1);
			}catch(Exception er) {
				Bukkit.getLogger().severe("Enchant named "+f+" doesn't exist");
			}
		}
	}

	public ItemBuilder(String path, String name, String amount, List<String> lore, String head, List<String> itemflags, List<String> enchants) {
		this.name=name;
		this.amount=amount;
		this.lore=lore;
		this.head=head;
		this.path=path;
	}
	
	public String getPath() {
		return path;
	}
	
	public ItemStack build() {
		return build(null);
	}
	
	public ItemStack build(Player owner) {
		if(head!=null) {
			if(lore!=null) {
				List<String> copyLore = new ArrayList<>(lore);
				copyLore.replaceAll(a -> PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(a)));
				String text = PlaceholderAPI.setPlaceholders(owner, head);
				if(text==null||text.trim().isEmpty())text=head;
				if(text.toLowerCase().startsWith("hdb:")) {
					ItemStack stack = HDBSupport.parse(PlaceholderAPI.setPlaceholders(owner, text));
					ItemMeta meta = stack.getItemMeta();
					if(name!=null)
					meta.setDisplayName(PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)));
					if(!copyLore.isEmpty())
						meta.setLore(copyLore);
					stack.setItemMeta(meta);
					stack.setAmount(fix(StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, amount))));
					return stack;
				}else
				if(text.startsWith("http://")||text.startsWith("https://"))
					return ItemCreatorAPI.createHeadByWeb(fix(StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, amount))), name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), copyLore, text);
				if(text.length()<=16)
					return ItemCreatorAPI.createHead(fix(StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, amount))), name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)),text, copyLore);
				return ItemCreatorAPI.createHeadByValues(fix(StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, amount))), name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), copyLore, text);
			}else {
				String text = PlaceholderAPI.setPlaceholders(owner, head);
				if(text==null||text.trim().isEmpty())text=head;
				if(text.length()<=16)
					return ItemCreatorAPI.createHead(fix(StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, amount))), name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)),text);
				if(text.startsWith("http://")||text.startsWith("https://"))
					return ItemCreatorAPI.createHeadByWeb(fix(StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, amount))), name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), text);
				return ItemCreatorAPI.createHeadByValues(fix(StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, amount))), name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), text);
			}
		}else
		if(lore!=null) {
			List<String> copyLore = new ArrayList<>(lore);
			copyLore.replaceAll(a -> PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(a)));
			return setModel(ItemCreatorAPI.create(type, fix(StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, amount))), name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), copyLore,StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, data))),StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, model)));
		}else {
			return setModel(ItemCreatorAPI.create(type, fix(StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, amount))), name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), null,StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, data))),StringUtils.getInt(PlaceholderAPI.setPlaceholders(owner, model)));
		}
	}
    
	private static int fix(int i) {
		return i <= 0 ? 1 : i;
	}
	
	public static ItemStack setModel(ItemStack s, int model) {
		if(model==0)return s;
		try {
			ItemMeta meta = s.getItemMeta();
			meta.setCustomModelData(model);
			s.setItemMeta(meta);
			return s;
		}catch(Exception | NoSuchMethodError | NoSuchFieldError e) {
			s.setDurability((short)model);
			return s;
		}
	}
}
