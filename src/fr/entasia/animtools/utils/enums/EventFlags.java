package fr.entasia.animtools.utils.enums;

import fr.entasia.animtools.Main;
import fr.entasia.animtools.utils.SBTask;
import fr.entasia.apis.ServerUtils;

public enum EventFlags {
	ALL_DAMAGES, ENTITY_DAMAGES, PLAYER_DAMAGES, PVP_DAMAGES, FALL_DAMAGES,
  	EXPLOSION_ENTITIES, EXPLOSION_BLOCKS,
	BLOCK_BREAK, BLOCK_PLACE,
	TEAMS{
		@Override
		public void setMode(boolean a) {
			super.setMode(a);
			SBTask.execute();
		}
	};

	public boolean mode = false;

	public void setMode(boolean a){
		if(a!=mode){
			mode = a;
			Main.flags.set(name(), mode);
			Main.main.saveConfig();
			ServerUtils.permMsg("anim.flagnotify", "§6AnimTools : Le flag §c"+name()+"§6 à été mis en §c"+a+"§6 !");
		}
	}

	public boolean isEnabled(){
		return mode;
	}
	public boolean isDisabled(){
		return !mode;
	}

}
