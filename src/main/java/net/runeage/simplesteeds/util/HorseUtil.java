package net.runeage.simplesteeds.util;

import net.runeage.simplesteeds.ImprovedSteeds;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class HorseUtil {

    private static NamespacedKey speed = new NamespacedKey(ImprovedSteeds.improvedSteeds, "speed");
    private static NamespacedKey jump = new NamespacedKey(ImprovedSteeds.improvedSteeds, "jump");
    private static NamespacedKey maxHealth = new NamespacedKey(ImprovedSteeds.improvedSteeds, "maxHealth");
    private static NamespacedKey health = new NamespacedKey(ImprovedSteeds.improvedSteeds, "health");
    private static NamespacedKey armor = new NamespacedKey(ImprovedSteeds.improvedSteeds, "armor");
    private static NamespacedKey color = new NamespacedKey(ImprovedSteeds.improvedSteeds, "color");
    private static NamespacedKey style = new NamespacedKey(ImprovedSteeds.improvedSteeds, "style");
    private static NamespacedKey dead = new NamespacedKey(ImprovedSteeds.improvedSteeds, "dead");
    public static NamespacedKey breedTime = new NamespacedKey(ImprovedSteeds.improvedSteeds, "breedTime");

    public static ItemStack createSaddle(Horse horse, int ded){
        ItemStack itemStack = new ItemStack(Material.SADDLE).clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        HorseAttributes ha = new HorseAttributes(horse);

        ItemStack horsearmor = horse.getInventory().getArmor();
        if (horsearmor == null){
            horsearmor = new ItemStack(Material.AIR);
        }

        PersistentDataContainer hpdc = horse.getPersistentDataContainer();

        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        pdc.set(speed, PersistentDataType.DOUBLE, ha.getCurrentSpeed());
        pdc.set(jump, PersistentDataType.DOUBLE, ha.getCurrentJump());
        pdc.set(maxHealth, PersistentDataType.DOUBLE, ha.getHealth());
        pdc.set(health, PersistentDataType.DOUBLE, horse.getHealth());
        pdc.set(armor, PersistentDataType.STRING, horsearmor.getType().toString());
        pdc.set(color, PersistentDataType.STRING, horse.getColor().toString());
        pdc.set(style, PersistentDataType.STRING, horse.getStyle().toString());
        pdc.set(dead, PersistentDataType.INTEGER, ded);

        if (!hpdc.has(breedTime, PersistentDataType.LONG)){
            pdc.set(breedTime, PersistentDataType.LONG, 0L);
        } else {
            pdc.set(breedTime, PersistentDataType.LONG, hpdc.get(breedTime, PersistentDataType.LONG));
        }

        itemMeta.setDisplayName(horse.getCustomName());

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Health: " + ChatColor.WHITE + ((int)(ha.getHealth() / 2)) + ChatColor.RED + " ❤");
        lore.add(ChatColor.GRAY + "Speed: " + ChatColor.WHITE + ha.getAdjustedSpeed());
        lore.add(ChatColor.GRAY + "Jump: " + ChatColor.WHITE + ha.getAdjustedJump());
        lore.add(ChatColor.GRAY + "Color: " + ChatColor.WHITE + WordUtils.capitalize(horse.getColor().toString().toLowerCase()));
        if (ded == 1) {
            lore.add(" ");
            lore.add(ChatColor.RED + "[DEAD - ☠]");
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean spawnHorse(ItemStack itemStack, Location location){
        if (!isSteedSaddle(itemStack)) return false;

        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

        double s = pdc.get(speed, PersistentDataType.DOUBLE);
        double j = pdc.get(jump, PersistentDataType.DOUBLE);
        double mh = pdc.get(maxHealth, PersistentDataType.DOUBLE);
        double h = pdc.get(health, PersistentDataType.DOUBLE);
        Material a = Material.getMaterial(pdc.get(armor, PersistentDataType.STRING));
        Horse.Color c = Horse.Color.valueOf(pdc.get(color, PersistentDataType.STRING));
        Horse.Style st = Horse.Style.valueOf(pdc.get(style, PersistentDataType.STRING));
        int ded = pdc.get(dead, PersistentDataType.INTEGER);
        Long lastBreedTime = pdc.get(breedTime, PersistentDataType.LONG);

        if (ded == 1) return false;

        Horse horse = (Horse) location.getWorld().spawnEntity(location, EntityType.HORSE);

        horse.setCustomName(itemMeta.getDisplayName());

        horse.setColor(c);
        horse.setStyle(st);

        horse.setTamed(true);

        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(s);
        horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(j);
        horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mh);
        horse.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(9);

        horse.setHealth(h);

        HorseInventory horseInventory = horse.getInventory();
        horseInventory.setSaddle(new ItemStack(Material.SADDLE));
        horseInventory.setArmor(new ItemStack(a));

        if (lastBreedTime == null)
            lastBreedTime = 0L;

        PersistentDataContainer hpdc = horse.getPersistentDataContainer();

        hpdc.set(breedTime, PersistentDataType.LONG, lastBreedTime);

        return true;
    }

    public static ItemStack reviveHorse(ItemStack itemStack){
        if (itemStack == null) return null;
        if (itemStack.getType() != Material.SADDLE) return null;
        itemStack = itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        if (!pdc.has(dead, PersistentDataType.INTEGER)) return null;
        int ded = pdc.get(dead, PersistentDataType.INTEGER);
        if (ded != 1) return null;
        pdc.set(dead, PersistentDataType.INTEGER, 0);
        pdc.set(health, PersistentDataType.DOUBLE, 1.0);
        List<String> lore = itemMeta.getLore();
        lore.remove(lore.size() - 2);
        lore.remove(lore.size() - 1);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean isSteedSaddle(ItemStack itemStack){
        if (itemStack == null) return false;
        if (itemStack.getType() != Material.SADDLE) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        if (!pdc.has(speed, PersistentDataType.DOUBLE)) return false;
        return true;
    }

    public static boolean isHorseDead(ItemStack itemStack){
        if (itemStack == null) return false;
        if (itemStack.getType() != Material.SADDLE) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        if (!pdc.has(dead, PersistentDataType.INTEGER)) return false;
        return pdc.get(dead, PersistentDataType.INTEGER) == 1;
    }

}
