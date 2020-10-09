package Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.APIs.EnchantmentAPI;
import me.DevTec.TheAPI.APIs.ItemCreatorAPI;

public class Kit {
	private List<ItemStack> a = new ArrayList<>();
	private double cost;
	private long delay;
	public List<ItemStack> getItems() {
		return a;
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
		kit.cost=API.convertMoney(Loader.kit.getString("Kits." + name + ".Price"));
		kit.delay=StringUtils.timeFromString(Loader.kit.getString("Kits." + name + ".Cooldown"));
		for (String def : Loader.kit.getKeys("Kits." + name + ".Items")) {
			Material m = null;
			try{
				m=XMaterial.matchXMaterial(def.toUpperCase()).get().parseMaterial();
			}catch(Exception e) {}
			if (m == null) {
				Bukkit.getLogger().warning("Error when giving kit '" + name + "', material '" + def + "' is invalid !");
				continue;
			}

			ItemCreatorAPI a = new ItemCreatorAPI(m);
			int numb = 1;
			if (Loader.kit.getInt("Kits." + name + ".Items." + def + ".Amount") != 0)
				numb = Loader.kit.getInt("Kits." + name + ".Items." + def + ".Amount");
			a.setAmount(numb);
			a.setLore(Loader.kit.getStringList("Kits." + name + ".Items." + def + ".Lore"));
			if (Loader.kit.getBoolean("Kits." + name + ".Items." + def + ".HideEnchants"))
				a.addItemFlag(ItemFlag.HIDE_ENCHANTS);
			if (Loader.kit.getBoolean("Kits." + name + ".Items." + def + ".HideAttributes"))
				a.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
			if (Loader.kit.getBoolean("Kits." + name + ".Items." + def + ".HideUnbreakable"))
				a.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
			if (Loader.kit.getBoolean("Kits." + name + ".Items." + def + ".Unbreakable"))
				a.setUnbreakable(true);
			a.setDisplayName(Loader.kit.getString("Kits." + name + ".Items." + def + ".CustomName"));
			if (Loader.kit.getString("Kits." + name + ".Items." + def + ".Enchantments") != null)
				for (String enchs : Loader.kit.getStringList("Kits." + name + ".Items." + def + ".Enchantments")) {
					String nonum = enchs.replace(":", "").replaceAll("[0-9 ]+", "").toUpperCase();
					if (EnchantmentAPI.byName(nonum)==null)
						Bukkit.getLogger().warning("Error when giving kit '" + name + "', enchant '" + nonum + "' is invalid !");
					else
						a.addEnchantment(nonum, StringUtils.getInt(enchs.replace(":", "").replace("_", "").replace(" ", "")));
				}
			kit.a.add(a.create());
		}
		Loader.getInstance.kits.put(name.toLowerCase(), kit);
		return kit;
	}

	private String name;
	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}
