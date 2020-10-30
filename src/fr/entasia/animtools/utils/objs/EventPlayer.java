package fr.entasia.animtools.utils.objs;

import fr.entasia.animtools.utils.Utils;
import fr.entasia.animtools.utils.enums.EventTeam;
import org.bukkit.entity.Player;

public class EventPlayer {

	public Player p;

	public EventTeam team;
	public SBManager sb;
	private int score;

	public EventPlayer(Player p){
		this.p = p;
		this.sb = new SBManager(this);

	}

	public boolean isEligible(){
		return p.isOnline()&&p.getWorld()== Utils.animWorld;
	}

	public void addScore(){
		setScore(score+1);
	}

	public void setScore(int score){
		this.score = score;
		SBManager.refreshPlayers();
	}

	public int getScore(){
		return score;
	}
}
