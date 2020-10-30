package fr.entasia.animtools.invs;

import fr.entasia.animtools.utils.Utils;
import fr.entasia.apis.menus.MenuClickEvent;
import fr.entasia.apis.menus.MenuCloseEvent;
import fr.entasia.apis.menus.MenuCreator;
import fr.entasia.apis.menus.MenuFlag;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class AnimInvs {

	public static MenuCreator animBase = new MenuCreator() {

		@Override
		public void onMenuClick(MenuClickEvent e) {
			switch(e.item.getType()){
				case OAK_DOOR:{
					e.player.teleport(Utils.eventSpawn);
					break;
				}
				case ENDER_PEARL:{
					Utils.setSpawn(e.player);
					e.player.closeInventory();
					break;
				}
				case IRON_SHOVEL:
				case CHEST:
				case STICK:{
					e.player.setItemOnCursor(e.item.clone());
					break;
				}
			}
		}
	};

	public static void animBaseOpen(Player p) {
		Inventory inv = animBase.createInv(3, "§7Outils :");


		ItemStack item = new ItemStack(Material.OAK_DOOR);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§7Se téléporter à la zone event");
		item.setItemMeta(meta);
		inv.setItem(1, item);

		item = new ItemStack(Material.ENDER_PEARL);
		meta = item.getItemMeta();
		meta.setDisplayName("§7Définir le Spawn ici");
		item.setItemMeta(meta);
		inv.setItem(2, item);

		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		for(int i=9;i<18;i++)inv.setItem(i, item);

		item = new ItemStack(Material.STICK);
		meta = item.getItemMeta();
		meta.setDisplayName("§fBoules de neige");
		item.setItemMeta(meta);
		inv.setItem(20, item);

		meta.setDisplayName("§cBoules de feu");
		item.setItemMeta(meta);
		inv.setItem(21, item);

		meta.setDisplayName("§7Flèches");
		item.setItemMeta(meta);
		inv.setItem(22, item);
		item.setItemMeta(meta);

		item = new ItemStack(Material.IRON_SHOVEL);
		meta = item.getItemMeta();
		meta.setDisplayName("§3Boules de neige spéciales");
		item.setItemMeta(meta);
		inv.setItem(23, item);
		item.setItemMeta(meta);

		item = new ItemStack(Material.CHEST);
		meta = item.getItemMeta();
		meta.setDisplayName("§6Inventaire");
		item.setItemMeta(meta);
		inv.setItem(24, item);
		item.setItemMeta(meta);

		p.openInventory(inv);
	}

	public static int[] slots = new int[41];

	static{
		for(int i=0;i<41;i++)slots[i] = i;
	}



	public static MenuCreator animInvManager = new MenuCreator() {

		@Override
		public void onMenuClose(MenuCloseEvent e) {
			ItemStack[] items = e.inv.getContents();
			ItemStack[] inv = new ItemStack[41];
			int j=0;
			for(int i=27;i<36;i++){
				inv[j] = items[i];
				j++;
			}

			for(int i=0;i<27;i++){
				inv[j] = items[i];
				j++;
			}

			for(int i=36;i<40;i++){
				inv[j] = items[i];
				j++;
			}

			inv[40] = items[40];

			Utils.invs.put((UUID)e.data, inv);
		}
	}.setFlags(MenuFlag.NoReturnUnlockedItems).setFreeSlots(slots);

	public static void animInvManagerOpen(Player p, UUID uuid) {


		ItemStack[] items = Utils.invs.getOrDefault(uuid, new ItemStack[41]);


		Inventory inv = animInvManager.createInv(5, "§7Gérer l'inventaire", uuid);

		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cSlot condamné");
		item.setItemMeta(meta);
		for(int i=41;i<45;i++)inv.setItem(i, item);

		int j=0;
		for(int i=27;i<36;i++){
			inv.setItem(i, items[j]);
			j++;
		}

		for(int i=0;i<27;i++){
			inv.setItem(i, items[j]);
			j++;
		}

		for(int i=36;i<40;i++){
			inv.setItem(i, items[j]);
			j++;
		}
		inv.setItem(40, items[40]);

		p.openInventory(inv);
	}
}
