package de.nfandrich.net.main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Random;

public class Utils {

    /**
     * Creates default ItemStack
     * @param mat The material
     * @param amount The amount
     * @return The ItemStack
     */
    public ItemStack create(Material mat, int amount) {
        return new ItemStack(mat, amount);
    }

    /**
     * Creates custom ItemStack
     * @param mat The Material
     * @param amount The amount
     * @param id The Id
     * @param display The display name
     * @return The ItemStack
     */
    public ItemStack createCustom(Material mat, int amount, short id, String display) {
        // ItemStack it = new ItemStack(mat, amount, id);
        ItemStack it = new ItemStack(mat, amount);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName(display);
        it.setItemMeta(meta);
        return it;
    }

    /**
     * Clears player's inventory and effects
     * @param p The Player
     */
    public void clearPlayer(Player p) {
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setLevel(0);
        p.setExp(0);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        for(PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
    }

    public int randomNumber(int min, int max) {
        Random r = new Random();
        int i = r.nextInt(((max-min) + 1) +min);
        return i;
    }

}
