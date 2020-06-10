package fr.entasia.animtools.cmds;

import fr.entasia.animtools.invs.AnimInvs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnimMenuCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg){
		if(!(sender instanceof Player))return false;
		if(sender.hasPermission("anim.menuanim"))AnimInvs.animBaseOpen((Player)sender);
		else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}
