package Events;

import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import Utils.setting;
import me.Straiker123.TheAPI;

public class FarmingSystem implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
	
		if(e.isCancelled()||!setting.farming)return;
		if(e.getClickedBlock().getBlockData() instanceof Ageable) {
			Ageable s = (Ageable) e.getClickedBlock().getBlockData();
			if(s.getAge()!=s.getMaximumAge())return;
			if(e.getClickedBlock().getType()==Material.WHEAT) {
				s.setAge(0);
				e.getClickedBlock().setBlockData(s);
				int random = TheAPI.generateRandomInt(3);
				if(random ==0)random=1;
				TheAPI.giveItem(e.getPlayer(), new ItemStack(Material.WHEAT_SEEDS,random));
				TheAPI.giveItem(e.getPlayer(), new ItemStack(Material.WHEAT,1));
			}
			if(e.getClickedBlock().getType()==Material.BEETROOTS) {
				s.setAge(0);
				e.getClickedBlock().setBlockData(s);
				int random = TheAPI.generateRandomInt(4);
				if(random ==0)random=1;
				TheAPI.giveItem(e.getPlayer(), new ItemStack(Material.BEETROOT_SEEDS,random));
				TheAPI.giveItem(e.getPlayer(), new ItemStack(Material.BEETROOT,1));
			}
			if(e.getClickedBlock().getType()==Material.POTATOES) {
				s.setAge(0);
				e.getClickedBlock().setBlockData(s);
				int random = TheAPI.generateRandomInt(5);
				if(random ==0)random=1;
				TheAPI.giveItem(e.getPlayer(), new ItemStack(Material.POTATO,random));
			}
			if(e.getClickedBlock().getType()==Material.CARROTS) {
				s.setAge(0);
				e.getClickedBlock().setBlockData(s);
				int random = TheAPI.generateRandomInt(5);
				if(random ==0)random=1;
				TheAPI.giveItem(e.getPlayer(), new ItemStack(Material.CARROT,random));
			}
		}
	}
}
