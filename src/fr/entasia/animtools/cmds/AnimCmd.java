package fr.entasia.animtools.cmds;

import fr.entasia.animtools.invs.AnimInvs;
import fr.entasia.animtools.utils.Utils;
import fr.entasia.animtools.utils.enums.EventFlags;
import fr.entasia.animtools.utils.enums.EventTeam;
import fr.entasia.animtools.utils.objs.EventPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnimCmd implements CommandExecutor {

	public static boolean chatLock;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg){
		if(!(sender instanceof Player))return false;
		if(sender.hasPermission("anim.animtools")){
			Player p = (Player)sender;
			if(arg.length==0) {
				p.sendMessage("\n\n§c------------------\n");
				p.sendMessage("§cMet un argument ! Arguments disponibles :");
				p.sendMessage("§c- menu");
				p.sendMessage("");
				p.sendMessage("§c- settp");
				p.sendMessage("§c- tp/teleport/tépééstépé [joueur]");
				p.sendMessage("");
				p.sendMessage("§c- teams [<joueur> <couleur>]");
				p.sendMessage("");
				p.sendMessage("§c- flags [<flag> <valeur>]");
				p.sendMessage("");
				p.sendMessage("§c- points [<joueur/equipe> <valeur>]");
				p.sendMessage("");
				p.sendMessage("§c- clear (supprime l'inventaire des joueurs)");
				p.sendMessage("§c- delete [joueur] (supprime les données volatiles)");
				p.sendMessage("");
				p.sendMessage("§c- chat off/on");

			}else if(arg[0].equalsIgnoreCase("chat")) {
				if (arg.length == 2) {
					if (arg[1].equalsIgnoreCase("off")) {
						for (Player z : Bukkit.getOnlinePlayers()) {
							if (z.getWorld()==Utils.animWorld) {
								z.sendMessage("§7[§6AnimTools§7] §cLe chat a été désactivé !");
							}
						}
						chatLock = true;
						p.sendMessage("§cLe chat a bien été désactivé");
					} else if (arg[1].equalsIgnoreCase("on")) {
						for (Player z : Bukkit.getOnlinePlayers()) {
							if (z.getWorld()==Utils.animWorld) {
								z.sendMessage("§7[§6AnimTools§7] §aLe chat a été activé !");
							}
						}
						chatLock = false;
						p.sendMessage("§aLe chat a bien été activé");
					} else {
						p.sendMessage("§cVeuillez choisir entre on et off");
					}
				} else {
					p.sendMessage("§cVeuillez choisir entre on et off");
				}


			}else if(arg[0].equalsIgnoreCase("menu")){
				AnimInvs.animBaseOpen((Player)sender);

			}else if(arg[0].equalsIgnoreCase("settp")){
				Utils.setSpawn(p);

			}else if(arg[0].equalsIgnoreCase("tp")||arg[0].equals("teleport")||arg[0].equals("tépééstépé")){
				if(arg.length>1){
					Player target = Bukkit.getPlayer(arg[1]);
					if(target==null)p.sendMessage("§cCe joueur n'est pas connecté !");
					else{
						target.teleport(Utils.eventSpawn);
						p.sendMessage("§e"+target.getName()+"§a à été téléporté !");
					}
				}else {
					p.teleport(Utils.eventSpawn);
					p.sendMessage("§aTu as été téléporté !");
				}

			}else if(arg[0].equalsIgnoreCase("clear")){
				for(Player lp : Bukkit.getOnlinePlayers()){
					if(lp.getWorld()==Utils.animWorld&&!lp.hasPermission("anim"))lp.getInventory().clear();
				}
				p.sendMessage("§cInventaires des joueurs supprimés !");

			}else if(arg[0].equalsIgnoreCase("delete")){
				if(arg.length>1) {
					Player target = Bukkit.getPlayer(arg[1]);
					if (target == null) p.sendMessage("§cCe joueur n'est pas connecté !");
					else {
						Utils.playerCache.remove(target.getName());
						p.sendMessage("§cLes données volatiles de "+target.getName()+" ont été supprimées !");
					}
				}else{
					Utils.invs.clear();
					Utils.playerCache.clear();
					p.sendMessage("§cToutes les données volatiles ont été supprimées !");
				}

			}else if(arg[0].equalsIgnoreCase("teams")||arg[0].equalsIgnoreCase("team")){
				if(arg.length==1) {
					p.sendMessage("§6Joueurs dans les teams :");
					for (EventPlayer ep : Utils.playerCache.values()) {
						if (ep.team == null) p.sendMessage("§7- " + ep.p.getName() + " : §fAucune team");
						else p.sendMessage("§7- " + ep.p.getName() + " : " + ep.team.getName());
					}
				}else if(arg.length==3) {
					Player target = Bukkit.getPlayer(arg[1]);
					if(target==null)p.sendMessage("§cCe joueur n'est pas connecté !");
					else{
						EventPlayer ep = Utils.getEventPlayer(target);
						arg[2] = arg[2].toUpperCase();
						if(arg[2].equals("CLEAR")){
							ep.team=null;
							p.sendMessage("§cTeam supprimé pour §e"+target.getName()+"§c !");
						}else{
							try{
								EventTeam team = EventTeam.valueOf(arg[2]);
								ep.team = team;
								p.sendMessage("§aTeam définie à "+team.getName()+"§a pour §e"+target.getName()+"§6 !");
							}catch(IllegalArgumentException e){
								p.sendMessage("§cLa team '"+arg[2]+"' est invalide");
								p.sendMessage("§6- §fCLEAR");
							}
						}
					}
				}else p.sendMessage("§cSyntaxe invalide !");

			}else if(arg[0].equalsIgnoreCase("flags")||arg[0].equalsIgnoreCase("flag")){
				if(arg.length==1){
					p.sendMessage("§cFlags :");
					for (EventFlags f : EventFlags.values()) p.sendMessage("§c- " + f.name()+" : "+f.isEnabled());
				}else if(arg.length==3){
					try {
						EventFlags flag = EventFlags.valueOf(arg[1].toUpperCase());
						arg[2] = arg[2].toLowerCase();
						if (arg[2].equals("true")) flag.setMode(true);
						else if (arg[2].equals("false")) flag.setMode(false);
						else {
							p.sendMessage("§cValeur " + arg[2] + " invalide ! Met true/false");
							return true;
						}
						p.sendMessage("§aSuccès !");
					} catch (IllegalArgumentException e) {
						p.sendMessage("§cFlag " + arg[1] + " invalide !");
					}
				}else p.sendMessage("§cSyntaxe invalide !");

			}else if(arg[0].equalsIgnoreCase("points")||arg[0].equalsIgnoreCase("point")){
				if(arg.length==1) {
					p.sendMessage("§6Points :");
					if (EventFlags.TEAMS.isEnabled()) {
						for (EventTeam et : EventTeam.values()) {
							p.sendMessage(et.getName() + "§7 : " + et.getScore() + " pts");
						}
					} else {
						for (EventPlayer ep : Utils.playerCache.values()) {
							p.sendMessage(ep.p.getName() + "§7 : " + ep.getScore() + " pts");
						}
					}
				}else if(arg.length==3){
					if (EventFlags.TEAMS.isEnabled()) {
						try{
							EventTeam team = EventTeam.valueOf(arg[1].toUpperCase());
							team.setScore(Integer.parseInt(arg[2]));
							p.sendMessage("§aLe score de l'équipe "+team.getName()+"§a à été défini à "+team.getScore()+" !");
						}catch(NumberFormatException ignore){
							p.sendMessage("§cLe nombre " + arg[2] + " est invalide !");
						}catch(IllegalArgumentException ignore){
							p.sendMessage("§cLa team '"+arg[1]+"' est invalide");
						}

					}else {
						Player target = Bukkit.getPlayer(arg[1]);
						if (target == null) p.sendMessage("§cCe joueur n'est pas connecté !");
						else {
							try {
								EventPlayer ep = Utils.getEventPlayer(target);
								ep.setScore(Integer.parseInt(arg[2]));
								p.sendMessage("§aLe score de " + ep.p.getName() + " à été défini à " + ep.getScore() + " !");
							} catch (NumberFormatException ignore) {
								p.sendMessage("§cLe nombre " + arg[2] + " est invalide !");
							}
						}
					}
				}else p.sendMessage("§cSyntaxe invalide !");
			}else p.sendMessage("§cArgument invalide !");
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}
