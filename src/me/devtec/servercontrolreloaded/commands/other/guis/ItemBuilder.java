package me.devtec.servercontrolreloaded.commands.other.guis;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.devtec.servercontrolreloaded.utils.XMaterial;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;

public class ItemBuilder {
	String name, head;
	List<String> lore;
	int model, amount;
	XMaterial type;
	public ItemBuilder(XMaterial type, int amount, String name, List<String> lore, int model) {
		this.type=type;
		if(amount<=0)amount=1;
		this.amount=amount;
		this.name=name;
		this.lore=lore;
		this.model=model;
	}

	public ItemBuilder(String name, int amount, List<String> lore, String head) {
		this.name=name;
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
				return ItemCreatorAPI.createHeadByValues(amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), copyLore, PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(head)));
			}else {
				return ItemCreatorAPI.createHeadByValues(amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), null, PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(head)));
			}
		}else
		if(lore!=null) {
			List<String> copyLore = new ArrayList<>(lore);
			copyLore.replaceAll(a -> PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(a)));
			return setModel(ItemCreatorAPI.create(type.getMaterial(), amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), copyLore,type.getData()),model);
		}else {
			return setModel(ItemCreatorAPI.create(type.getMaterial(), amount, name==null?null:PlaceholderAPI.setPlaceholders(owner, StringUtils.colorize(name)), null,type.getData()),model);
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
