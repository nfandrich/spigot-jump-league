package de.nfandrich.net.listener;

import de.nfandrich.net.main.GameState;
import de.nfandrich.net.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class ChestListener implements Listener {

    private HashMap<Location, Inventory> chests;
    private ArrayList<ItemStack> loot;

    public ChestListener() {
        chests = new HashMap<Location, Inventory>();
        loot = new ArrayList<ItemStack>();
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.NOTE_BLOCK) {
            e.setCancelled(true);
            // Main.main.log("Clicked note block");
            if(Main.main.alive.contains(e.getPlayer()) && Main.main.state == GameState.INGAME) {
                // Main.main.log("alive list contains player & game state is 'ingame'");
                if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if(!chests.containsKey(e.getClickedBlock().getLocation())) {
                        // Main.main.log("chest isn't contained in chests hashmap");
                        registerLoot();
                        Inventory inv = Bukkit.getServer().createInventory(null, 9*3, "§eTruhe");
                        for (int i = 0; i < Main.main.utils.randomNumber(4, 7); i++) {
                            // Adds random item to inventory
                            inv.setItem(
                                    Main.main.utils.randomNumber(1, inv.getSize() - 1),
                                    loot.get(Main.main.utils.randomNumber(1, loot.size() - 1))
                            );
                        }
                        chests.put(e.getClickedBlock().getLocation(), inv);
                    }
                    e.getPlayer().openInventory(chests.get(e.getClickedBlock().getLocation()));
                }
            }
        }
    }

    /**
     * Registers possible chest loos
     */
    public void registerLoot() {
        loot.clear();
        loot.add(Main.main.utils.create(Material.WOODEN_SWORD, 1));
        loot.add(Main.main.utils.create(Material.WOODEN_AXE, 1));
        loot.add(Main.main.utils.create(Material.STONE_SWORD, 1));
        loot.add(Main.main.utils.create(Material.APPLE, Main.main.utils.randomNumber(1, 5)));
        loot.add(Main.main.utils.create(Material.FISHING_ROD, 1));
        loot.add(Main.main.utils.create(Material.BREAD, Main.main.utils.randomNumber(3,6)));
    }

}
