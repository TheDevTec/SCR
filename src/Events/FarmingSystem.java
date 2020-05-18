package Events;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;

import Utils.XMaterial;
import Utils.setting;
import me.Straiker123.TheAPI;

@SuppressWarnings("deprecation")
public class FarmingSystem implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent e) {

		if (e.isCancelled() || !setting.farming)
			return;
		BlockState s = e.getClickedBlock().getState();
		MaterialData md = s.getData();

		if (md instanceof Crops)
			if (((Crops) md).getState() == CropState.RIPE) {
				if (e.getClickedBlock().getType().name().equals("WHEAT")
						|| e.getClickedBlock().getType().name().equals("CROPS")) {
					((Crops) md).setState(CropState.SEEDED);
					s.update(true);
					int random = TheAPI.generateRandomInt(2);
					if (random == 0)
						random = 1;
					TheAPI.giveItem(e.getPlayer(), new ItemStack(XMaterial.WHEAT_SEEDS.parseMaterial(), random));
					TheAPI.giveItem(e.getPlayer(), new ItemStack(Material.WHEAT, 1));
				}
				try {
					if (e.getClickedBlock().getType().name().equals("BEETROOTS")) {
						((Crops) md).setState(CropState.SEEDED);
						s.update(true);
						int random = TheAPI.generateRandomInt(3);
						if (random == 0)
							random = 1;
						TheAPI.giveItem(e.getPlayer(), new ItemStack(Material.BEETROOT_SEEDS, random));
						TheAPI.giveItem(e.getPlayer(), new ItemStack(Material.BEETROOT, 1));
					}
				} catch (Exception | NoSuchFieldError ss) {
				}
				try {
					if (e.getClickedBlock().getType().name().contains("POTATO")) {
						((Crops) md).setState(CropState.SEEDED);
						s.update(true);
						int random = TheAPI.generateRandomInt(4);
						if (random == 0)
							random = 1;
						TheAPI.giveItem(e.getPlayer(), new ItemStack(XMaterial.POTATO.parseMaterial(), random));
					}
				} catch (Exception | NoSuchFieldError es) {
				}
				try {
					if (e.getClickedBlock().getType().name().contains("CARROT")) {
						((Crops) md).setState(CropState.SEEDED);
						s.update(true);
						int random = TheAPI.generateRandomInt(4);
						if (random == 0)
							random = 1;
						TheAPI.giveItem(e.getPlayer(), new ItemStack(XMaterial.CARROT.parseMaterial(), random));
					}
				} catch (Exception | NoSuchFieldError es) {
				}
			}
	}
}
