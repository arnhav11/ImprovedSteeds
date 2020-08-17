package net.runeage.simplesteeds.listeners;

import net.runeage.simplesteeds.util.HorseUtil;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class HorseListener implements Listener {

    @EventHandler
    public void onHorseBreed(EntityBreedEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof Horse)) return;

        Horse father = (Horse) event.getFather();
        Horse mother = (Horse) event.getMother();

        PersistentDataContainer fpdc = father.getPersistentDataContainer();
        PersistentDataContainer mpdc = mother.getPersistentDataContainer();

        if (!fpdc.has(HorseUtil.breedTime, PersistentDataType.LONG))
            fpdc.set(HorseUtil.breedTime, PersistentDataType.LONG, 0L);
        if (!mpdc.has(HorseUtil.breedTime, PersistentDataType.LONG))
            mpdc.set(HorseUtil.breedTime, PersistentDataType.LONG, 0L);

        long dadBreedTime = fpdc.get(HorseUtil.breedTime, PersistentDataType.LONG);
        long momBreedTime = mpdc.get(HorseUtil.breedTime, PersistentDataType.LONG);

        if (dadBreedTime * (5*60*1000) < System.currentTimeMillis())
            event.setCancelled(true);
        if (momBreedTime * (5*60*1000) < System.currentTimeMillis())
            event.setCancelled(true);

        fpdc.set(HorseUtil.breedTime, PersistentDataType.LONG, System.currentTimeMillis());
        mpdc.set(HorseUtil.breedTime, PersistentDataType.LONG, System.currentTimeMillis());
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent event){
        Entity entity = event.getEntity();

        if (!(entity instanceof Horse)) return;

        Horse horse = (Horse) entity;

        horse.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(9);
    }

    @EventHandler
    public void onSummon(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack == null) return;
        if (itemStack.getType() != Material.SADDLE) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        boolean spawned = HorseUtil.spawnHorse(itemStack, player.getLocation());

        if (spawned)
            itemStack.subtract();
    }

    @EventHandler
    public void onHorseDamage(EntityDamageByEntityEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof Horse)) return;
        Horse horse = (Horse) entity;
        if (!horse.isTamed()) return;
        event.setDamage(event.getDamage() / 2);
        if (horse.isEmpty()) return;
        double finalDamage = event.getFinalDamage();
        event.setCancelled(true);
        double newHealth = horse.getHealth() - finalDamage;
        if (newHealth >= 0) {
            horse.setHealth(newHealth);
            horse.getWorld().playSound(horse.getLocation(), Sound.ENTITY_HORSE_HURT, 3.0f, 1.0f);
        } else {
            horse.setHealth(0);
            horse.getWorld().playSound(horse.getLocation(), Sound.ENTITY_HORSE_DEATH, 3.0f, 1.0f);
        }
    }

    @EventHandler
    public void onHorseDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof Horse)) return;
        Horse horse = (Horse) entity;
        if (!horse.isTamed()) return;
        event.getDrops().clear();
        if (horse.isEmpty()) {
            horse.getWorld().dropItem(horse.getLocation(), HorseUtil.createSaddle(horse, 1));
        } else {
            Player player = (Player) horse.getPassengers().get(0);
            if (player.getInventory().firstEmpty() == -1) {
                horse.getWorld().dropItem(player.getEyeLocation(), HorseUtil.createSaddle(horse, 1));
            } else {
                player.getInventory().addItem(HorseUtil.createSaddle(horse, 1));
            }
            player.sendMessage(ChatColor.GRAY + "Your horse has died! You can revive it by right clicking the saddle on a hay block.");
        }
    }

    @EventHandler
    public void onHorseRevive(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack == null) return;
        if (itemStack.getType() != Material.SADDLE) return;
        if (!HorseUtil.isHorseDead(itemStack)) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (block.getType() != Material.HAY_BLOCK) return;

        for (int i = 0; i < player.getInventory().getSize(); i++){
            ItemStack temp = player.getInventory().getItem(i);
            if (temp == null) continue;
            if (!temp.equals(itemStack)) continue;
            ItemStack tempSaddle = HorseUtil.reviveHorse(itemStack);
            if (tempSaddle == null) continue;
            player.getInventory().setItem(i, tempSaddle);
        }

        player.getWorld().playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, 3.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.BLOCK_CRACK,
                block.getLocation().clone().add(0.5, 0.5, 0.5), 50, block.getBlockData());
        block.setType(Material.AIR);
    }

}
