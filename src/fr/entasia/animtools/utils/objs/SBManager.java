package fr.entasia.animtools.utils.objs;

import fr.entasia.animtools.utils.Utils;
import fr.entasia.animtools.utils.enums.EventFlags;
import fr.entasia.animtools.utils.enums.EventTeam;
import fr.entasia.apis.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SBManager {

	private static final Comparator<EventTeam> teamComp = Comparator.comparingInt(EventTeam::getScore).reversed();
	private static final Comparator<EventPlayer> playerComp = Comparator.comparingInt(EventPlayer::getScore).reversed();

	public EventPlayer ep;
	public Scoreboard scoreboard;
	public Objective objective;
	public String exScore="";

	public SBManager(EventPlayer ep){
		this.ep = ep;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = scoreboard.registerNewObjective("hub", "dummy", "§eÉvenement");
	}

	public static void refreshPlayers(){
		ArrayList<EventPlayer> values = new ArrayList<>(Utils.playerCache.values());
		values.sort(playerComp);
		int score = 49;
		String newScore;
		for(EventPlayer show_ep : values) {
			if (show_ep.isEligible()) {
				newScore = "§7" + show_ep.p.getDisplayName() + "§7 : " + show_ep.getScore();
				for (EventPlayer ep : values) { // apply this line for everyone
					if(ep.isEligible()){
						ep.sb.scoreboard.resetScores(show_ep.sb.exScore);
						ep.sb.objective.getScore(newScore).setScore(score);
					}
				}
				show_ep.sb.exScore = newScore;
				score--;
				if (score == 40) break;
			}
		}
	}

	public static void refreshTeams(){
		EventTeam[] values = EventTeam.values();
		Arrays.sort(values, teamComp);
		int score = 49;
		String newScore;
		for(EventTeam et : values){
			newScore = et.getName()+"§7 : "+et.getScore();
			for(EventPlayer ep : Utils.playerCache.values()){
				if(ep.isEligible()){
					ep.sb.scoreboard.resetScores(et.exScore);
					ep.sb.objective.getScore(newScore).setScore(score);
				}
			}
			et.exScore = newScore;
			score--;
		}
	}


	public void clear(){
		scoreboard.getEntries().forEach(a -> scoreboard.resetScores(a));
	}

	public void refresh(boolean scores){
		ep.p.setScoreboard(scoreboard);
		clear();
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		if(EventFlags.TEAMS.mode){
			objective.getScore("§cÉquipes").setScore(50);
			if(scores)refreshTeams();
		}else {
			objective.getScore("§cJoueurs").setScore(50);
			if (scores) refreshPlayers();
		}
		objective.getScore("§b§m----------- ").setScore(40);
		objective.getScore("§bplay.enta§7sia.fr").setScore(39);
	}

}
