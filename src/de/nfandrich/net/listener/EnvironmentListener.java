package de.nfandrich.net.listener;

import de.nfandrich.net.main.GameState;
import de.nfandrich.net.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EnvironmentListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if(Main.main.state != GameState.INGAME && !e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(EntityPickupItemEvent e) {
        Player p = (Player) e.getEntity();

        if(!Main.main.alive.contains(p) && !p.isOp()) {
            e.setCancelled(true);
            return;
        }
        if (Main.main.state != GameState.INGAME && !p.isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(EntityDamageByEntityEvent e) {
        if(!Main.main.alive.contains(e.getEntity())) {
            e.setCancelled(true);
            return;
        }
        if(Main.main.state != GameState.INGAME && Main.main.state != GameState.DEATHMATCH) {
            e.setCancelled(true);
        }else{
            e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(!Main.main.alive.contains(e.getEntity()) || !Main.main.alive.contains(e.getDamager())) {
            e.setCancelled(true);
        }
        if(Main.main.state == GameState.INGAME || Main.main.state == GameState.DEATHMATCH) {
            if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                Main.main.lastdmg.put((Player) e.getEntity(), (Player) e.getDamager());
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(Main.main.state == GameState.LOBBY || Main.main.state == GameState.INGAME) {
            e.setCancelled(true);
        }

        /*
        if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
            Player p = (Player) e.getEntity();
            Main.main.log("player received damage by falling");
            Main.main.lm.teleportToPlayerSpawn(p);
        }
        */
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e){
        if(Main.main.state == GameState.LOBBY || Main.main.state == GameState.INGAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(Main.main.state == GameState.INGAME) {
            Block block = e.getPlayer().getLocation().getBlock();

            if (e.getPlayer().getLocation().getY() < 25) {
                Main.main.lm.teleportToPlayerSpawn(e.getPlayer());
            }

            if (block.getType() == Material.OAK_PRESSURE_PLATE) {
                if (Main.main.checkpoints.containsKey(e.getPlayer().getDisplayName())) {
                    Main.main.checkpoints.remove(e.getPlayer().getDisplayName());
                }
                Main.main.checkpoints.put(e.getPlayer().getDisplayName(), e.getPlayer().getLocation());

                if(!Main.main.checkpointBlocks.contains(block)) {
                    Main.main.checkpointBlocks.add(block);
                    e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                }
            }else if(block.getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE){
                if(!Main.main.checkpointBlocks.contains(block)) {
                    Main.main.checkpointBlocks.add(block);
                    Bukkit.getServer().broadcastMessage(e.getPlayer().getDisplayName() + " §ehat das Ziel erreicht!");
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
                    e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                    // Set deathmatch countdown to 10
                    Bukkit.getScheduler().cancelTask(Main.main.cd.gamecd);
                    Main.main.cd.startDeathmatchCountdown();
                    Main.main.cd.deathmatch = 10;
                }
            }
        }
    }

}
