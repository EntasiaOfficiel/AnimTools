package fr.entasia.animtools.listeners;

import fr.entasia.animtools.cmds.AnimCmd;
import fr.entasia.animtools.invs.AnimInvs;
import fr.entasia.animtools.utils.Utils;
import fr.entasia.apis.nbt.ItemNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.nbt.NBTTypes;
import fr.entasia.apis.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.UUID;

public class Tools implements Listener {


	@EventHandler
	public void onClick(PlayerInteractEntityEvent e) {
		if (e.getPlayer().getWorld()== Utils.animWorld&&e.getHand() == EquipmentSlot.HAND && e.getRightClicked() instanceof Player){
			Player p = e.getPlayer();
			Player target = (Player) e.getRightClicked();
			ItemStack item = p.getInventory().getItemInMainHand();
			if(item!=null && item.getType() == Material.CHEST && item.hasItemMeta() && item.getItemMeta().hasDisplayName()&&item.getItemMeta().getDisplayName().equals("§6Inventaire")){
				NBTComponent nbt = ItemNBT.getNBT(p.getInventory().getItemInMainHand());
				String id = (String) nbt.getValue(NBTTypes.String, "invid");
				if(id==null)return;
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
		if(e.getPlayer().getWorld()== Utils.animWorld&&e.getHand()== EquipmentSlot.HAND&&e.getItem()!=null&& ItemUtils.hasName(e.getItem())) {
			if(e.getAction()==Action.PHYSICAL)return;
			if(e.getItem().getType() == Material.CHEST){ // items doubles
				if (e.getItem().getItemMeta().getDisplayName().equals("§6Inventaire")) {
					if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
						NBTComponent nbt = ItemNBT.getNBT(e.getItem());
						if(nbt==null)return;
						String id = (String) nbt.getValue(NBTTypes.String,"invid");
						if(id==null)return;
						UUID uuid;
						if (id.equals("")) {
							uuid = UUID.randomUUID();
							nbt.setValue(NBTTypes.String, "invid", uuid.toString());
							e.getPlayer().getInventory().setItemInMainHand(ItemNBT.setNBT(e.getItem(), nbt));
						} else uuid = UUID.fromString(id);
						AnimInvs.animInvManagerOpen(e.getPlayer(), uuid);
					} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
						e.getPlayer().sendMessage("§cTu n'as cliqué sur personne !");
				} else if (e.getItem().getItemMeta().getDisplayName().equals("§3Boules de neige spéciales")) {
					e.getPlayer().launchProjectile(Snowball.class, e.getPlayer().getLocation().getDirection().multiply(1.1)).setCustomName("Wi");
				}else return;

			}else if(e.getAction() == Action.LEFT_CLICK_AIR){  // que click droit
				if (e.getItem().getType() == Material.STICK){
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
				}else if (e.getItem().getType() == Material.IRON_SPADE) {
					switch (e.getItem().getItemMeta().getDisplayName()) {
						case "§3Boules de neige spéciales": {
							e.getPlayer().launchProjectile(Snowball.class, e.getPlayer().getLocation().getDirection().multiply(1.1)).setCustomName("Wi");
							break;
						}
					}
				}else return;
			}
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void projectile(ProjectileHitEvent e){
		if(e.getHitBlock() != null&& e.getHitBlock().getType() == Material.SNOW_BLOCK) {
			if("Wi".equals(e.getEntity().getCustomName())) {
				e.getHitBlock().setType(Material.AIR);
				e.getHitBlock().getWorld().playSound(e.getHitBlock().getLocation(), Sound.BLOCK_SNOW_BREAK, 10, 2);
			}
		}
	}

	@EventHandler
	public void chat(AsyncPlayerChatEvent e){
		if(AnimCmd.chatLock) {
			if (e.getPlayer().getWorld()==Utils.animWorld) {
				if (!e.getPlayer().hasPermission("anim.chat")) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("§cLe chat a été désactivé !");
				}
			}
		}
	}
}