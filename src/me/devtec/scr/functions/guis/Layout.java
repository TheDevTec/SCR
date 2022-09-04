package me.devtec.scr.functions.guis;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;

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

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.devtec.scr.commands.message.Sudo;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.API;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.json.Json;
import me.devtec.shared.placeholders.PlaceholderAPI;
import me.devtec.shared.utility.StreamUtils;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.gui.GUI.ClickType;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.game.ItemMaker;
import me.devtec.theapi.bukkit.gui.HolderGUI;
import me.devtec.theapi.bukkit.xseries.XMaterial;

public class Layout {
	
	List<String> lines;
	Map<Character, ItemBuilder> builder = new HashMap<>();
	
	public Layout(Config file) {
		for(String key : file.getKeys("items")) //Loading starting items
			builder.put(key.toCharArray()[0], new ItemBuilder(file, "items."+key));
		lines=file.getStringList("layout");
	}
	
	public ItemBuilder find(Player p, HolderGUI gui, Character c) {
		ItemBuilder start = builder.get(c);
		if(start!=null) {
			if(!start.cons(p, gui)) //If can show item in GUI
				return null;
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
		private final Config file;
		private final String path;
		
		//ITEMSTACK
		private final String displayName, amount, extraNbt, modelData, material, head, bookOwner, bookTitle, bookGen, armorColor, durability;
		private final List<String> lore, enchants, itemFlags, potionEffects;
		
		public ItemBuilder(Config file, String path) {
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

		public ItemStack build(Player p) { //TODO - placeholders
			return ItemMaker.loadFromConfig(file, path);
		}
		
		/*public ItemStack build(Player p) {
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
							stack=LoaderClass.nmsProvider.setNBT(stack, extraNbt);
					}else {
					stack = new ItemStack(XMaterial.PLAYER_HEAD.getMaterial());
					if(extraNbt!=null)
						stack=LoaderClass.nmsProvider.setNBT(stack, extraNbt);
					if(t.startsWith("http://")||t.startsWith("https://")) {
						SkullMeta m = (SkullMeta) stack.getItemMeta();
						if(TheAPI.isOlderThan(8)) {
							Object profile = Ref.createGameProfile(null, "SpigotDevTec");
							SkinData data = generateSkin(t);
							Ref.invoke(Ref.invoke(profile, "getProperties"), set, "textures", Ref.createProperty("textures", data.value, data.signature));
							Ref.set(m, "profile", profile);
						}else {
							GameProfile profile = new GameProfile(UUID.randomUUID(), "SpigotDevTec");
							SkinData data = generateSkin(t);
							profile.getProperties().put("textures", new Property("textures", data.value, data.signature));
							Ref.set(m, "profile", profile);
							if(TheAPI.isNewerThan(15))
								Ref.invoke(m, setProfile, profile);
						}
						stack.setItemMeta(m);
					}else {
						if(t.length()<=16) {
							if(!t.matches("^[A-Za-z0-9_]") && !t.contains("%")) {
								SkullMeta m = (SkullMeta) stack.getItemMeta();
								m.setOwner(t);
								stack.setItemMeta(m);
							}
						}else {
							if(!t.contains("%")) {
								SkullMeta m = (SkullMeta) stack.getItemMeta();
								if(TheAPI.isOlderThan(8)) {
									Object profile = Ref.createGameProfile(null, "SpigotDevTec");
									Ref.invoke(Ref.invoke(profile, "getProperties"), set, "textures", Ref.createProperty("textures", t));
									Ref.set(m, "profile", profile);
								}else {
									GameProfile profile = new GameProfile(UUID.randomUUID(), "SpigotDevTec");
									profile.getProperties().put("textures", new Property("textures", t));
									Ref.set(m, "profile", profile);
									if(TheAPI.isNewerThan(15))
										Ref.invoke(m, setProfile, profile);
								}
								stack.setItemMeta(m);
							}
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
		}*/
		
		public boolean cons(Player p, HolderGUI gui) {
			return ConditionPositive(p, gui, condition, positive);
		}
		public boolean canShowItem(Player p, HolderGUI gui) {
			return true;
		}
		
		//Conditions check
		public boolean ConditionPositive(Player p, HolderGUI gui, String condition, List<String> positive) {
			if(condition==null||positive.isEmpty())
				return true;
			String result = PlaceholderAPISupport.replace(condition.replace("%title%", gui.getTitle()), p);
			for(String con : positive) {
				con=PlaceholderAPISupport.replace(con, p);
				//NUMBERS
				try {
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
				}catch(Exception err) {
				}
				if(result.equals(con))return true;
			}
			return positive.contains(result);
		}
		
		public void process(Player player, HolderGUI gui, ClickType click) {
			Object get = file.get(path+".actions");
			if(get instanceof Collection) {
				BukkitLoader.getNmsProvider().postToMainThread(() -> {
				for(String key : file.getStringList(path+".actions"))
					if(processAction(player, gui, click, key, false))break;
				});
			}
			BukkitLoader.getNmsProvider().postToMainThread(() -> {
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
							boolean result = !ConditionPositive(player, gui, file.getString(path+".action-conditions."+con+".value"), file.getStringList(path+".action-conditions."+con+".positive"));
							if(result) {
								for(String keys : file.getStringList(path+".actions."+actions))
									if(processAction(player, gui, click, keys, true))break;
								return true;
							}
							return false;
						}
						//res is con
						return !ConditionPositive(player, gui, file.getString(path+".action-conditions."+con+".value"), file.getStringList(path+".action-conditions."+con+".positive"));
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
					/*if(matcher.group(2).startsWith("connect")) { //TODO - bungee support
						String res = matcher.group(3);
						API.send(player,res);
						return false;
					}else {*/
						if(matcher.group(2).startsWith("open")) {
							String res = matcher.group(3);
							GUIManager.open(player,res);
							return false;
						}
						if(matcher.group(2).startsWith("cmd")) {
							String res = matcher.group(3);
							Sudo.sudoConsole(PlaceholderAPISupport.replace(res, player, true));
							return false;
						}else {
							String res = matcher.group(3);
							//TheAPI.msg(PlaceholderAPISupport.replace(res, player, false), player);
							return false;
						}
					//}
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
			default:
				return "middle";
			}
		}

		public void startAnimation(String name) {
			//title
			//layout
		}
	}
	
	
	

	/*private static final String URL_FORMAT = "https://api.mineskin.org/generate/url?url=%s&%s",
			USER_FORMAT="https://api.ashcon.app/mojang/v2/user/%s";
	private static String getSkinType(java.awt.image.BufferedImage image) {
		final byte[] pixels = ((java.awt.image.DataBufferByte) image.getRaster().getDataBuffer()).getData();
		int argb = ((int) pixels[4002] & 0xff);
		argb += (((int) pixels[4003] & 0xff) << 8);
		argb += (((int) pixels[4004] & 0xff) << 16);
		return argb==2631720?"steve":"alex";
    }
	
	@SuppressWarnings("unchecked")
	public static synchronized SkinData generateSkin(String urlOrName) {
		if(urlOrName==null)return null;
		if(urlOrName.toLowerCase().startsWith("https://")||urlOrName.toLowerCase().startsWith("http://")) {
			try {
				java.net.URLConnection connection = new URL(urlOrName).openConnection();
				connection.setRequestProperty("User-Agent", "ServerControlReloaded-JavaClient");
				HttpURLConnection conn = (HttpURLConnection)new URL(String.format(URL_FORMAT, urlOrName, "name=DevTec&model="+getSkinType(ImageIO.read(connection.getInputStream()))+"&visibility=1")).openConnection();
				conn.setRequestProperty("User-Agent", "TheAPI-JavaClient");
				conn.setRequestProperty("Accept-Encoding", "gzip");
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(1000);
				conn.setReadTimeout(1000);
				conn.connect();
				Map<String, Object> text = (Map<String, Object>) Json.reader().simpleRead(StreamUtils.fromStream(new GZIPInputStream(conn.getInputStream())));
				SkinData data = new SkinData();
				if(!text.containsKey("error")) {
					data.signature=(String) ((Map<String, Object>)((Map<String, Object>)text.get("data")).get("texture")).get("signature");
					data.value=(String) ((Map<String, Object>)((Map<String, Object>)text.get("data")).get("texture")).get("value");
				}
				return data;
			}catch(Exception err) {}
		}
		try {
			HttpURLConnection conn = (HttpURLConnection)new URL(String.format(USER_FORMAT, urlOrName)).openConnection();
			conn.setRequestProperty("User-Agent", "TheAPI-JavaClient");
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(1000);
			conn.connect();
			Map<String, Object> text = (Map<String, Object>) Json.reader().simpleRead(StreamUtils.fromStream(conn.getInputStream()));
			SkinData data = new SkinData();
			if(!text.containsKey("error")) {
				data.signature=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("raw")).get("signature");
				data.value=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("raw")).get("value");
			}
			return data;
		}catch(Exception err) {}
		return null;
	}
	
	static Method setProfile = Ref.method(Ref.craft("inventory.CraftMetaSkull"), "setProfile", Ref.getClass("com.mojang.authlib.GameProfile") != null
			? Ref.getClass("com.mojang.authlib.GameProfile")
			: Ref.getClass("net.minecraft.util.com.mojang.authlib.GameProfile"));*/
	
}
