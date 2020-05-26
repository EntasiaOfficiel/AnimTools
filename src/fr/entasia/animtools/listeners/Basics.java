package fr.entasia.animtools.listeners;

import fr.entasia.animtools.utils.Utils;
import fr.entasia.animtools.utils.enums.EventFlags;
import fr.entasia.animtools.utils.objs.EventPlayer;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Basics implements Listener {


	@EventHandler
	public void food(FoodLevelChangeEvent e) {
		if(e.getEntity().getWorld()==Utils.animWorld){
			e.setCancelled(true);
			e.setFoodLevel(20);
		}
	}

	@EventHandler
	public void a(PlayerJoinEvent e) {
		EventPlayer ep = Utils.getEventPlayer(e.getPlayer());
		ep.p = e.getPlayer();
		if(ep.p.getWorld()==Utils.animWorld){
			ep.sb.refresh(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onWorldChange(PlayerChangedWorldEvent e) {
		EventPlayer ep = Utils.getEventPlayer(e.getPlayer());
		if(ep.p.getWorld()== Utils.animWorld){
			ep.p.setGameMode(GameMode.SURVIVAL);
			ep.sb.refresh(true);
		}else{
			ep.sb.clear();
		}
	}

	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity().getWorld()== Utils.animWorld&&e.getEntity() instanceof Player){
			if (e.getDamager() instanceof Player&&e.getEntity() instanceof Player){
				if(EventFlags.ALL_DAMAGES.isDisabled()||EventFlags.PVP_DAMAGES.isDisabled())e.setCancelled(true); // les deux conditions sont pas la pour la même raison
				else{
					Player damager = (Player)e.getDamager();
					EventPlayer victim = Utils.getEventPlayer((Player)e.getEntity());
					if(EventFlags.PVP_DAMAGES.isDisabled())e.setCancelled(true);
					else if(EventFlags.TEAMS.isEnabled() && victim.team != null && victim.team == Utils.getEventPlayer(damager).team) {
						e.setCancelled(true);
						damager.sendMessage("§cVous êtes dans la même équipe !");
					} else {
						if (victim.p.getHealth() <= e.getFinalDamage()) {
							if (damager != null) damager.sendMessage("§Tu as tué "+victim.p.getDisplayName()+" !");
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity().getWorld()== Utils.animWorld&&e.getEntity() instanceof LivingEntity){
			if(EventFlags.ALL_DAMAGES.isEnabled()){
				if(EventFlags.FALL_DAMAGES.isDisabled()&&e.getCause()==EntityDamageEvent.DamageCause.FALL)e.setCancelled(true);
				else if(EventFlags.EXPLOSION_ENTITIES.isDisabled()&&
						(e.getCause()==EntityDamageEvent.DamageCause.ENTITY_EXPLOSION||e.getCause()==EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){
					e.setCancelled(true);
				}else{
					if(e.getEntity() instanceof Player){
						if(EventFlags.PLAYER_DAMAGES.isDisabled())e.setCancelled(true);
						else{
							Player p = (Player) e.getEntity();
							if(p.getHealth()<=e.getFinalDamage()) {
								e.setCancelled(true);
								kill(Utils.getEventPlayer(p));
							}
						}
					}else{
						if(EventFlags.ENTITY_DAMAGES.isDisabled())e.setCancelled(true);
					}
				}
			}else e.setCancelled(true);
		}
	}

	public static void kill(EventPlayer ep){
		ep.p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, ep.p.getLocation(), 50, 0.4, 0.7, 0.4, 0.08);
		ep.p.setHealth(20);
		ep.p.teleport(Utils.eventSpawn);
		ep.p.sendMessage("§cTu es mort !");

	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getPlayer().getWorld()== Utils.animWorld&&EventFlags.BLOCK_PLACE.isDisabled()){
			if(!e.getPlayer().hasPermission("anim.build")){
				e.getPlayer().sendMessage("§cTu ne peux pas poser de blocks !");
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().getWorld()== Utils.animWorld&&EventFlags.BLOCK_BREAK.isDisabled()){
			if(!e.getPlayer().hasPermission("anim.build")){
				e.getPlayer().sendMessage("§cTu ne peux pas casser de blocks !");
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onExplosion(BlockExplodeEvent e) {
		if(e.getBlock().getWorld()==Utils.animWorld){
			if(EventFlags.EXPLOSION_BLOCKS.isDisabled()){
				e.blockList().clear();
			}
		}
	}

	@EventHandler
	public void onExplosion(EntityExplodeEvent e) {
		if(e.getEntity().getWorld()==Utils.animWorld){
			if(EventFlags.EXPLOSION_BLOCKS.isDisabled()){
				e.blockList().clear();
			}
		}
	}
}
