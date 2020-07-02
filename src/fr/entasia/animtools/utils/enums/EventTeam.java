package fr.entasia.animtools.utils.enums;

import fr.entasia.animtools.utils.objs.SBManager;
import fr.entasia.apis.utils.TextUtils;

public enum  EventTeam {
	BLEU("§3"), VERT("§a"), ROUGE("§c"), JAUNE("§e");

	public String prefix;
	private int score;
	public String exScore="";

	EventTeam(String prefix){
		this.prefix = prefix;
	}

	public String getName(){
		return prefix+ TextUtils.firstLetterUpper(name());
	}

//	public static void updateScores(){
//		if(EventFlags.TEAMS.mode){
//			int len = values().length;
//			Integer[] scores = new Integer[len];
//			for(int i=0;i<len;i++)scores[i] = 0;
//			for(EventPlayer ep : Utils.playerCache.values()){
//				if(ep.isEligible()&&ep.team!=null){
//					scores[ep.team.ordinal()]++;
//				}
//			}
//		}else ServerUtils.permMsg("errorlog", "§6AnimTools : §cUpdate des scores de teams non valide ! Prévenir iTrooz_");
//	}

	public int getScore(){
		return score;
	}

	public void addScore(){
		setScore(score+1);
	}
	public void setScore(int score){
		this.score = score;
		SBManager.refreshTeams();
	}
}
