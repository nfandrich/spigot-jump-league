package de.nfandrich.net.listener;

import de.nfandrich.net.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class NavigatorListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if(e.getClickedInventory() != null && e.getCurrentItem() != null) {
            if (!Main.main.alive.contains(p)) {
                e.setCancelled(true);
                if(e.getView().getTitle().equalsIgnoreCase("§bTeleporter")){
                // if(e.getClickedInventory().getType() == InventoryType.PLAYER) {
                    String playerName = e.getCurrentItem().getItemMeta().getDisplayName();
                    if(Main.main.alive.contains(Bukkit.getServer().getPlayer(playerName))) {
                        Player target = Bukkit.getServer().getPlayer(playerName);
                        p.teleport(target);
                        p.sendMessage("§eDu beobachtest nun " + target.getName());
                    }else{
                        p.sendMessage("§cDieser Spieler ist nicht mehr am Leben.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(!Main.main.alive.contains(p)){
            if(p.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                int lenght = (Main.main.alive.size() / 9) + 1;
                Inventory inv = Bukkit.getServer().createInventory(null, 9*lenght, "§bTeleporter");
                for(Player alive : Main.main.alive) {
                    // ItemStack head = new ItemStack(Material.SKULL_ITEM, 1 , (short) 3;
                    ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
                    SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
                    skullMeta.setOwningPlayer(p);
                    skullMeta.setDisplayName(alive.getName());
                    head.setItemMeta(skullMeta);
                    inv.addItem(head);
                }
                p.openInventory(inv);
            }
        }
    }

}
