package me.devtec.servercontrolreloaded.utils.guis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.utils.HDBSupport;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.XMaterial;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.EnchantmentAPI;
import me.devtec.theapi.guiapi.GUI.ClickType;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.Data;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;

public class Layout {
	List<String> lines;
	Map<Character, ItemBuilder> builder = new HashMap<>();
	
	public Layout(Data file) {
		for(String key : file.getKeys("items"))
			builder.put(key.toCharArray()[0], new ItemBuilder(file, "items."+key));
		lines=file.getStringList("layout");
	}
	
	public ItemBuilder find(Player p, HolderGUI gui, Character c) {
		ItemBuilder start = builder.get(c);
		if(start!=null) {
			if(!start.cons(p, gui))return null;
			for(ItemBuilder b : start.conditions)
				if(b.cons(p, gui))
					return cons(p,gui, b);
			return start;
		}
		return null;
	}
	
	public ItemBuilder cons(Player p, HolderGUI gui, ItemBuilder prev) {
		for(ItemBuilder b : prev.conditions)
			if(b.cons(p, gui))return cons(p, gui, b);
		return prev;
	}

	static ItemStack none = new ItemStack(Material.STONE);

	private static Method get = Ref.method(Ref.getClass("com.google.common.collect.ForwardingMultimap"), "get", Object.class);
	private static Method set = Ref.method(Ref.getClass("com.google.common.collect.ForwardingMultimap"), "put", Object.class, Object.class);
	
	static {
		if(get==null)
			get = Ref.method(Ref.getClass("net.minecraft.util.com.google.common.collect.ForwardingMultimap"), "get", Object.class);
		if(set==null)
			set = Ref.method(Ref.getClass("net.minecraft.util.com.google.common.collect.ForwardingMultimap"), "put", Object.class, Object.class);
	}
	
	public class ItemBuilder {
		//NULLABLE
		private final String condition;
		private final List<String> positive;
		
		//SUB-ITEM BUILDERS
		private final List<ItemBuilder> conditions = new ArrayList<>();
		
		//ACTIONS
		private final Data file;
		private final String path;
		
		//ITEMSTACK
		private final String displayName, amount, extraNbt, modelData, material, head, bookOwner, bookTitle, bookGen, armorColor, durability;
		private final List<String> lore, enchants, itemFlags, potionEffects;
		public ItemBuilder(Data file, String path) {
			this.file=file;
			this.path=path;
			// &4Cool &ccolors
			if(file.getString(path+".display.displayName")==null)
				displayName=file.getString(path+".display.name");
			else
				displayName=file.getString(path+".display.displayName");
			// 31
			amount=file.getString(path+".display.amount");
			// 16
			durability=file.getString(path+".display.durability");
			// {"myNbt":[{"ofc":1,"notWorking :)":2}]}
			extraNbt=file.getString(path+".display.nbt");
			// 34154
			modelData=file.getString(path+".display.modelData");
			//stone:1, andesite
			if(file.getString(path+".display.type")==null)
				material=file.getString(path+".display.material");
			else
				material=file.getString(path+".display.type");
			// hdb:id, https:/URL.png, playerName, VALUES...
			head=file.getString(path+".display.head");
			// StraikerinaCZ
			bookOwner=file.getString(path+".display.book.author");
			// &lCoolest cool book ever
			bookTitle=file.getString(path+".display.book.title");
			// ORIGINAL, TATTERED, COPY_OF_COPY, COPY_OF_ORIGINAL
			bookGen=file.getString(path+".display.book.generation");
			// #5c4d9e
			armorColor=file.getString(path+".display.color");
			// - "&7"
			// - "&8&l| &eStraiker's book"
			// - "&7"
			lore=file.getStringList(path+".display.lore");
			// - "SHARPNESS:1"
			// - "UNBREAKING:5"
			enchants=file.getStringList(path+".display.enchants");
			// - "HIDE_ENCHANTS"
			itemFlags=file.getStringList(path+".display.itemFlags");
			// - "ABSORPTION"
			potionEffects=file.getStringList(path+".display.potionEffects");

			condition=file.getString(path+".value");
			positive=file.getStringList(path+".positive");
			
			for(String con : file.getKeys(path+".conditions"))
				conditions.add(new ItemBuilder(file, path+".conditions."+con));
		}
		
		public ItemStack build(Player p) {
			if(material==null)return none;
			try {
				XMaterial mat = XMaterial.matchXMaterial(PlaceholderAPI.setPlaceholders(p, material).toUpperCase());
				if(mat==null)return none;
				ItemStack stack;
				
				if(head!=null) {
					String t =PlaceholderAPI.setPlaceholders(p, head);
					if(t.toLowerCase().startsWith("hdb:")) {
						stack = HDBSupport.parse(t);
						if(extraNbt!=null)
							stack=NMSAPI.setNBT(stack, extraNbt);
					}else {
					stack = new ItemStack(XMaterial.PLAYER_HEAD.getMaterial());
					if(t.startsWith("http://")||t.startsWith("https://")) {
						if(extraNbt!=null)
							stack=NMSAPI.setNBT(stack, extraNbt);
						SkullMeta m = (SkullMeta) stack.getItemMeta();
						try {
							Object profile = Ref.createGameProfile(null, "TheAPI");
							byte[] encodedData = null;
							try {
								encodedData = Base64.getEncoder().encode(("{textures:{SKIN:{url:\"" + t + "\"}}}").getBytes());
							} catch (Exception err) {
							}
							Ref.invoke(Ref.invoke(profile, "getProperties"), set,
									"textures", Ref.createProperty("textures", new String(encodedData)));
							Ref.set(m, "profile", profile);
						} catch (Exception | NoSuchMethodError e) {
						}
						stack.setItemMeta(m);
					}else {
						if(extraNbt!=null)
							stack=NMSAPI.setNBT(stack, extraNbt);
					if(t.length()<=16) {
						SkullMeta m = (SkullMeta) stack.getItemMeta();
						m.setOwner(t);
						stack.setItemMeta(m);
					}else {
						SkullMeta m = (SkullMeta) stack.getItemMeta();
						try {
							Object profile = Ref.createGameProfile(null, "TheAPI");
							Ref.invoke(Ref.invoke(profile, "getProperties"), set,
									"textures", Ref.createProperty("textures", t));
							Ref.set(m, "profile", profile);
						} catch (Exception | NoSuchMethodError e) {
						}
						stack.setItemMeta(m);
					}
					}}
				}else
					stack = mat.parseItem();
				stack.setDurability((short)StringUtils.getInt(PlaceholderAPI.setPlaceholders(p, durability)));
				stack.setAmount(StringUtils.getInt(PlaceholderAPI.setPlaceholders(p, amount)));
				if(stack.getAmount()<=0)stack.setAmount(1);
				for(String e : enchants){
					int level = StringUtils.getInt(e);
					e=e.split(":")[0];
					if(EnchantmentAPI.byName(e)!=null)
						stack.addUnsafeEnchantment(EnchantmentAPI.byName(e).getEnchantment(), level<=0?1:level);
				}
				
				ItemMeta meta = stack.getItemMeta();
				
				if(mat == XMaterial.WRITTEN_BOOK) {
					BookMeta book = (BookMeta) meta;
					if(bookOwner!=null)
					book.setAuthor(TheAPI.colorize(PlaceholderAPI.setPlaceholders(p, bookOwner)));
					if(bookTitle!=null)
					book.setTitle(TheAPI.colorize(PlaceholderAPI.setPlaceholders(p, bookTitle)));
					try {
						book.setGeneration(Generation.valueOf(PlaceholderAPI.setPlaceholders(p, bookGen).toUpperCase()));
					}catch(Exception | NoSuchFieldError | NoSuchMethodError e) {}
					meta=book;
				}else if(mat.name().contains("POTION")) {
					PotionMeta pot = (PotionMeta) meta;
					if(armorColor!=null)
					pot.setColor(Color.fromRGB(Integer.decode(PlaceholderAPI.setPlaceholders(p, armorColor))));
					for(String ench : potionEffects)
						try {
							pot.addCustomEffect(new PotionEffect(PotionEffectType.getByName(PlaceholderAPI.setPlaceholders(p, ench)), 0, 1), true);
						}catch(Exception | NoSuchFieldError | NoSuchMethodError e) {}
					meta=pot;
				}
				if(displayName!=null)
				meta.setDisplayName(TheAPI.colorize(PlaceholderAPI.setPlaceholders(p, displayName)));
				if(!lore.isEmpty()) {
				List<String> clone = new ArrayList<>(lore);
				clone.replaceAll(a -> TheAPI.colorize(PlaceholderAPI.setPlaceholders(p, a)));
				meta.setLore(clone);
				}
				for(String a : itemFlags)
					try {
						meta.addItemFlags(ItemFlag.valueOf(PlaceholderAPI.setPlaceholders(p, a)));
					}catch(NoSuchFieldError e) {}
				if(TheAPI.isNewerThan(12) && modelData!=null)
					meta.setCustomModelData(StringUtils.getInt(PlaceholderAPI.setPlaceholders(p, modelData)));
				stack.setItemMeta(meta);
				return stack;
			}catch(Exception er) {
				er.printStackTrace();
				return none;
			}
		}
		
		public boolean cons(Player p, HolderGUI gui) {
			return cons(p, gui, condition, positive);
		}
		
		public boolean cons(Player p, HolderGUI gui, String condition, List<String> positive) {
			if(condition==null||positive.isEmpty())
				return true;
			String result = PlaceholderAPI.setPlaceholders(p, condition.replace("%title%", gui.getTitle()));
			for(String con : positive) {
				con=PlaceholderAPI.setPlaceholders(p, con);
				//NUMBERS
				if(con.startsWith("=>")||con.startsWith(">=")) {
					con=con.substring(2);
					return StringUtils.getNumber(con).floatValue() <= StringUtils.getNumber(result).floatValue();
				}
				if(con.startsWith("=<")||con.startsWith("<=")) {
					con=con.substring(2);
					return StringUtils.getNumber(con).floatValue() >= StringUtils.getNumber(result).floatValue();
				}
				if(con.startsWith("=")) {
					con=con.substring(1);
					return StringUtils.getNumber(con).floatValue() == StringUtils.getNumber(result).floatValue();
				}
				if(con.startsWith(">")) {
					con=con.substring(1);
					return StringUtils.getNumber(con).floatValue() < StringUtils.getNumber(result).floatValue();
				}
				if(con.startsWith("<")) {
					con=con.substring(1);
					return StringUtils.getNumber(con).floatValue() > StringUtils.getNumber(result).floatValue();
				}
				if(con.startsWith("=!")||con.startsWith("!=")) {
					con=con.substring(2);
					return StringUtils.getNumber(con).floatValue() != StringUtils.getNumber(result).floatValue();
				}
				if(result.equals(con))return true;
			}
			return positive.contains(result);
		}
		
		public void process(Player player, HolderGUI gui, ClickType click) {
			Object get = file.get(path+".actions");
			if(get instanceof Collection) {
				NMSAPI.postToMainThread(() -> {
				for(String key : file.getStringList(path+".actions"))
					if(processAction(player, gui, click, key, false))break;
				});
			}
			NMSAPI.postToMainThread(() -> {
			for(String key : file.getStringList(path+".actions.default")) {
				if(processAction(player, gui, click, key, true))break;
			}
			});
		}
		
		Pattern reqVal = Pattern.compile("(any|shift_right|shift_left|middle|left|right) (cmd|msg|connect|open) (.*)"),
				aditVal = Pattern.compile("(any|shift_right|shift_left|middle|left|right) (con:.*?|perm:.*?) (.*)"),
				nonVal = Pattern.compile("(any|shift_right|shift_left|middle|left|right) (close|anim:.*)");
		
		public boolean processAction(Player player, HolderGUI gui, ClickType click, String action, boolean subActions) {
			Matcher matcher = nonVal.matcher(action);
			if(matcher.find()) {
				String key = convertToString(click);
				if(matcher.group(1).equals("any")||matcher.group(1).equals(key)) {
					if(matcher.group(2).equals("close")) {
						gui.close(player);
					}else {
						startAnimation(matcher.group(2).substring(5));
					}
				}
				return false;
			}
			
			matcher = aditVal.matcher(action);
			if(matcher.find()) {
				String key = convertToString(click);
				if(matcher.group(1).equals("any")||matcher.group(1).equals(key)) {
					if(matcher.group(2).startsWith("con:")) {
						String con = matcher.group(2).substring(4);
						if(matcher.group(3)!=null) {
							String actions = matcher.group(3);
							boolean result = !cons(player, gui, file.getString(path+".action-conditions."+con+".value"), file.getStringList(path+".action-conditions."+con+".positive"));
							if(result) {
								for(String keys : file.getStringList(path+".actions."+actions))
									if(processAction(player, gui, click, keys, true))break;
								return true;
							}
							return false;
						}
						//res is con
						return !cons(player, gui, file.getString(path+".action-conditions."+con+".value"), file.getStringList(path+".action-conditions."+con+".positive"));
					}else {
						String perm = matcher.group(2).substring(5);
						if(matcher.group(3)!=null) {
							String actions = matcher.group(3);
							boolean result = !player.hasPermission(perm);
							if(result) {
								for(String keys : file.getStringList(path+".actions."+actions))
									if(processAction(player, gui, click, keys, true))break;
								return true;
							}
							return false;
						}
						//res is perm
						return !player.hasPermission(perm);
					}
				}
				return false;
			}
			matcher = reqVal.matcher(action);
			if(matcher.find()) {
				String key = convertToString(click);
				if(matcher.group(1).equals("any")||matcher.group(1).equals(key)) {
					if(matcher.group(2).startsWith("connect")) {
						String res = matcher.group(3);
						API.send(player,res);
						return false;
					}else {
						if(matcher.group(2).startsWith("open")) {
							String res = matcher.group(3);
							GUIManager.open(player,res);
							return false;
						}
						if(matcher.group(2).startsWith("cmd")) {
							String res = matcher.group(3);
							TheAPI.sudoConsole(TabList.replace(res, player, true));
							return false;
						}else {
							String res = matcher.group(3);
							TheAPI.msg(TabList.replace(res, player, false), player);
							return false;
						}
					}
				}
				return false;
			}
			return false;
		}

		private String convertToString(ClickType click) {
			switch(click) {
			case LEFT_DROP:
			case LEFT_PICKUP:
				return "left";
			case RIGHT_DROP:
			case RIGHT_PICKUP:
				return "right";
			case SHIFT_LEFT_DROP:
			case SHIFT_LEFT_PICKUP:
				return "shift_left";
			case SHIFT_RIGHT_DROP:
			case SHIFT_RIGHT_PICKUP:
				return "shift_right";
			case MIDDLE_PICKUP:
				return "middle";
			}
			return "any";
		}

		public void startAnimation(String name) {
			//title
			//layout
		}
	}
}
