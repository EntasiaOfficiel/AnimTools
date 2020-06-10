package fr.entasia.animtools.utils;

import fr.entasia.animtools.utils.enums.EventFlags;
import fr.entasia.animtools.utils.objs.EventPlayer;
import fr.entasia.animtools.utils.objs.SBManager;
import org.bukkit.scheduler.BukkitRunnable;

public class SBTask extends BukkitRunnable {
	@Override
	public void run() {
		execute();
	}

	public static void execute(){
		for(EventPlayer ep : Utils.playerCache.values()){
			if(ep.isEligible()){
				ep.sb.refresh(false);
			}
			if(EventFlags.TEAMS.isEnabled())SBManager.refreshTeams();
			else SBManager.refreshPlayers();
		}
	}
}
