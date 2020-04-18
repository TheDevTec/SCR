package ServerControl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import Utils.Configs;
import me.Straiker123.Utils.Packets;

public class PlayerData {
	private String s;
	private FileConfiguration a = Loader.me;
	public PlayerData(String player) {
		s=player;
	}
	@SuppressWarnings("deprecation")
	public void createPlayer() {
		if(Loader.econ!=null && !Loader.econ.hasAccount(s))
			Loader.econ.createPlayerAccount(s);
	}
	public boolean existPath(String path) {
		return getString(path) != null;
	}
	public boolean isString(String path) {
		return a.isString(path);
	}
	public boolean isDouble(String path) {
		return a.isDouble(path);
	}
	public boolean isInt(String path) {
		return a.isInt(path);
	}
	public boolean isLong(String path) {
		return a.isLong(path);
	}
	public boolean isBoolean(String path) {
		return a.isBoolean(path);
	}
	public boolean getBoolean(String path) {
		return a.getBoolean("Players."+s+"."+path);
	}

	public ItemStack getItemStack(String path) {
		return a.isItemStack("Players."+s+"."+path)?a.getItemStack("Players."+s+"."+path)
				: fromString(a.getString("Players."+s+"."+path));
	}

	public void setItemStack(String path, ItemStack item) {
		set(path,toString(item));
	}

	public Set<String> getConfigurationSection(String section, boolean keys) {
		return a.getConfigurationSection(section).getKeys(keys);
	}


	public void set(String path, Object value) {
		a.set("Players."+s+"."+path, value);
	}
	
	public static void save() {
		Configs.chatme.save();
	}
	
    private ItemStack fromString(String data){
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        ItemStack itemStack = null;
        try {
            Class<?> nbtTagCompoundClass = Packets.getNMSClass("NBTTagCompound");
            Class<?> nmsItemStackClass = Packets.getNMSClass("ItemStack");
            Object nbtTagCompound = Packets.getNMSClass("NBTCompressedStreamTools").getMethod("a", DataInputStream.class).invoke(null, dataInputStream);
            Object craftItemStack = nmsItemStackClass.getMethod("createStack", nbtTagCompoundClass).invoke(null, nbtTagCompound);
            itemStack = (ItemStack) Packets.getBukkitClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, craftItemStack);
        } catch(Exception e) {
        }
     
        return itemStack;
    }
   
    private String toString(ItemStack item) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
       
        try {
            Class<?> nbtTagCompoundClass = Packets.getNMSClass("NBTTagCompound");
            Constructor<?> nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor();
            Object nbtTagCompound = nbtTagCompoundConstructor.newInstance();
            Object nmsItemStack = Packets.getBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            Packets.getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass).invoke(nmsItemStack, nbtTagCompound);
            Packets.getNMSClass("NBTCompressedStreamTools").getMethod("a", nbtTagCompoundClass, DataOutput.class).invoke(null, nbtTagCompound, (DataOutput)dataOutput);

        } catch (Exception e) {
        }
        return new BigInteger(1, outputStream.toByteArray()).toString(32);
    }

	public String getString(String path) {
		return a.getString("Players."+s+"."+path);
	}
	public void remove(String path) {
		 set(path,null);
	}
	public void removePlayer() {
		 a.set("Players."+s,null);
	}

	public int getInt(String path) {
		return a.getInt("Players."+s+"."+path);
	}

	public double getDouble(String path) {
		return a.getDouble("Players."+s+"."+path);
	}
	public Object get(String path) {
		return a.get("Players."+s+"."+path);
	}

	public long getLong(String path) {
		return a.getLong("Players."+s+"."+path);
	}
	
	public static List<String> getPlayers(){
		List<String> p = new ArrayList<String>();
		if(Loader.me.getString("Players")!=null)
		for(String d : Loader.me.getConfigurationSection("Players").getKeys(false))p.add(d);
		return p;
	}
}
