package fr.entasia.animtools.listeners;

import fr.entasia.animtools.invs.AnimInvs;
import fr.entasia.animtools.utils.Utils;
import fr.entasia.apis.nbt.ItemNBT;
import fr.entasia.apis.nbt.NBTComponent;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Tools implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEntityEvent e) {
		if (e.getPlayer().getWorld()== Utils.animWorld&&e.getHand() == EquipmentSlot.HAND && e.getRightClicked() instanceof Player){
			Player p = e.getPlayer();
			Player target = (Player) e.getRightClicked();
			ItemStack item = p.getInventory().getItemInMainHand();
			if(item!=null && item.getType() == Material.CHEST && item.hasItemMeta() && item.getItemMeta().hasDisplayName()&&item.getItemMeta().getDisplayName().equals("§6Inventaire")){

//				if(e.get)
				NBTComponent nbt = ItemNBT.getNBT(p.getInventory().getItemInMainHand());
				String id = nbt.getKeyString("invid");
				if(id.equals(""))p.sendMessage("§cTu n'as pas défini d'inventaire !");
				else {
					UUID uuid = UUID.fromString(id);
					target.getInventory().setContents(Utils.invs.get(uuid));
					p.sendMessage("§6Inventaire de §e"+target.getName()+" §6défini !");
				}
			}
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e){
		if(e.getPlayer().getWorld()== Utils.animWorld&&e.getHand()== EquipmentSlot.HAND&&e.getItem()!=null&&e.getItem().hasItemMeta()&&e.getItem().getItemMeta().hasDisplayName()) {
			if(e.getItem().getType() == Material.CHEST){
				if ("§6Inventaire".equals(e.getItem().getItemMeta().getDisplayName())) {
					if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
						NBTComponent nbt = ItemNBT.getNBT(e.getItem());
						String id = nbt.getKeyString("invid");
						UUID uuid;
						if (id.equals("")) {
							uuid = UUID.randomUUID();
							nbt.setKeyString("invid", uuid.toString());
							e.getPlayer().getInventory().setItemInMainHand(ItemNBT.setNBT(e.getItem(), nbt));
						} else uuid = UUID.fromString(id);
						AnimInvs.animInvManagerOpen(e.getPlayer(), uuid);
					} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
						e.getPlayer().sendMessage("§cTu n'as cliqué sur personne !");
				} else return;

			}else if (e.getItem().getType() == Material.STICK){
				switch(e.getItem().getItemMeta().getDisplayName()) {
					case "§fBoules de neige":{
						e.getPlayer().launchProjectile(Snowball.class, e.getPlayer().getLocation().getDirection().multiply(1.1));
						break;
					}
					case "§7Flèches": {
						e.getPlayer().launchProjectile(Arrow.class, e.getPlayer().getLocation().getDirection().multiply(1.4));
						break;
					}
					case "§cBoules de feu":{
						e.getPlayer().launchProjectile(SmallFireball.class, e.getPlayer().getLocation().getDirection());
						break;
					}
					default:{
						return;
					}
				}
			}else return;
			e.setCancelled(true);
		}
	}
}
