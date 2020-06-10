package fr.entasia.animtools.utils;

import fr.entasia.animtools.Main;
import fr.entasia.animtools.utils.objs.EventPlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class Utils {

	public static World animWorld;
	public static Location eventSpawn;
	public static HashMap<UUID, ItemStack[]> invs = new HashMap<>();

	public static HashMap<String, EventPlayer> playerCache = new HashMap<>();


	public static void setSpawn(Player p){
		if(p.getLocation().getWorld()==animWorld) {
			eventSpawn = p.getLocation();
			Main.spawn.set("x", eventSpawn.getBlockX());
			Main.spawn.set("y", eventSpawn.getBlockY());
			Main.spawn.set("z", eventSpawn.getBlockZ());
			Main.main.saveConfig();
			p.sendMessage("§aSpawn de l'event défini !");
		}else p.sendMessage("§cTu n'es pas dans le monde animateur !");
	}


	public static EventPlayer getEventPlayer(Player p){
		EventPlayer ep = playerCache.get(p.getName());
		if(ep==null) {
			ep = new EventPlayer(p);
			playerCache.put(p.getName(), ep);
		}
		return ep;
	}
}
