package fr.entasia.animtools;

import fr.entasia.animtools.cmds.AnimCmd;
import fr.entasia.animtools.cmds.AnimMenuCmd;
import fr.entasia.animtools.listeners.Basics;
import fr.entasia.animtools.listeners.Tools;
import fr.entasia.animtools.utils.SBTask;
import fr.entasia.animtools.utils.Utils;
import fr.entasia.animtools.utils.enums.EventFlags;
import fr.entasia.errors.EntasiaException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


	public static Main main;
	public static ConfigurationSection spawn;
	public static ConfigurationSection flags;

	@Override
	public void onEnable() {
		try {
			main = this;
			saveDefaultConfig();

			Utils.animWorld = Bukkit.getWorld(getConfig().getString("world"));
			if(Utils.animWorld==null)throw new EntasiaException("World "+getConfig().getString("world")+" Not found");
			spawn = getConfig().getConfigurationSection("spawn");
			Utils.eventSpawn = Utils.animWorld.getSpawnLocation();
			Utils.eventSpawn.setX(spawn.getInt("x")+0.5);
			Utils.eventSpawn.setY(spawn.getInt("y")+0.5);
			Utils.eventSpawn.setZ(spawn.getInt("z")+0.5);

			flags = getConfig().getConfigurationSection("flags");
			for(String i : flags.getKeys(false)){
				try{
					EventFlags.valueOf(i).mode = flags.getBoolean(i);
				}catch(IllegalArgumentException ignore){
					getLogger().warning("Option invalide : "+i+". L'option à été supprimée");
					flags.set(i, null);
				}
			}
			saveConfig();

			getCommand("anim").setExecutor(new AnimCmd());
			getCommand("menuanim").setExecutor(new AnimMenuCmd());

			getServer().getPluginManager().registerEvents(new Tools(), this);
			getServer().getPluginManager().registerEvents(new Basics(), this);

			new SBTask().runTaskTimerAsynchronously(this, 0, 20*60*2);

		}catch(Throwable e){
			e.printStackTrace();
			getLogger().severe("LE SERVEUR VA S'ETEINDRE !");
			Bukkit.getServer().shutdown();
		}
	}
}
