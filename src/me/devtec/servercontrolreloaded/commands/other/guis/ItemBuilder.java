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
	String name, head;
	List<String> lore;
	List<ItemFlag> itemflags;
	Map<Enchantment, Integer> enchants;
	int model, amount,data;
	Material type;
	public ItemBuilder(Material type, int data, int amount, String name, List<String> lore, int model, List<String> itemflags, List<String> enchants) {
		this.type=type;
		if(amount<=0)amount=1;
		this.amount=amount;
		this.name=name;
		this.data=data;
		this.lore=lore;
		this.model=model;
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
				Bukkit.getLogger().severe("ItemFlag named "+f+" doesn't exist");
			}
		}
	}

	public ItemBuilder(String name, int amount, List<String> lore, String head, List<String> itemflags, List<String> enchants) {
		this.name=name;
		if(amount<=0)amount=1;
		this.amount=amount;
		this.lore=lore;
		this.head=head;
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
					ItemStack stack = HDBSupport.parse(text==null?null:PlaceholderAPI.setPlaceholders(owner, text));
					ItemMeta meta = stack.getItemMeta();
					if(name!=null)
					meta.setDisplayName(PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)));
					if(!copyLore.isEmpty())
						meta.setLore(copyLore);
					stack.setItemMeta(meta);
					stack.setAmount(amount);
					return stack;
				}else
				if(text.startsWith("http://")||text.startsWith("https://"))
					return ItemCreatorAPI.createHeadByWeb(amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), copyLore, text);
				if(text.length()<=16)
					return ItemCreatorAPI.createHead(amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)),text, copyLore);
				return ItemCreatorAPI.createHeadByValues(amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), copyLore, text);
			}else {
				String text = PlaceholderAPI.setPlaceholders(owner, head);
				if(text==null||text.trim().isEmpty())text=head;
				if(text.length()<=16)
					return ItemCreatorAPI.createHead(amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)),text);
				if(text.startsWith("http://")||text.startsWith("https://"))
					return ItemCreatorAPI.createHeadByWeb(amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), text);
				return ItemCreatorAPI.createHeadByValues(amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), text);
			}
		}else
		if(lore!=null) {
			List<String> copyLore = new ArrayList<>(lore);
			copyLore.replaceAll(a -> PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(a)));
			return setModel(ItemCreatorAPI.create(type, amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), copyLore,data),model);
		}else {
			return setModel(ItemCreatorAPI.create(type, amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), null,data),model);
		}
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
