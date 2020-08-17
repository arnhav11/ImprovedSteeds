package net.runeage.simplesteeds.listeners;

import net.runeage.simplesteeds.util.HorseAttributes;
import net.runeage.simplesteeds.util.HorseUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class HorseInventoryListener implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event){
        Inventory inventory = event.getInventory();
        if (!(inventory instanceof HorseInventory)) return;
        if (inventory.getHolder() == null) return;
        HorseInventory horseInventory = (HorseInventory) inventory;
        Horse horse = (Horse) horseInventory.getHolder();

        HorseAttributes ha = new HorseAttributes(horse);

        ItemStack saddle = horseInventory.getSaddle();
        if (saddle == null) return;
        saddle = saddle.clone();
        ItemMeta itemMeta = saddle.getItemMeta();
        itemMeta.setLore(Arrays.asList(
                " ",
                ChatColor.GRAY + "Health: " + ChatColor.WHITE + ((int)(ha.getHealth() / 2)) + ChatColor.RED + " ❤",
                ChatColor.GRAY + "Speed: " + ChatColor.WHITE + ha.getAdjustedSpeed(),
                ChatColor.GRAY + "Jump: " + ChatColor.WHITE + ha.getAdjustedJump(),
                ChatColor.GRAY + "Color: " + ChatColor.WHITE + WordUtils.capitalize(horse.getColor().toString().toLowerCase()),
                " ",
                ChatColor.YELLOW + "[Right Click to Pick up Horse]"
        ));
        saddle.setItemMeta(itemMeta);
        horseInventory.setSaddle(saddle);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        if (!(inventory instanceof HorseInventory)) return;
        if (inventory.getHolder() == null) return;
        HorseInventory horseInventory = (HorseInventory) inventory;
        Horse horse = (Horse) horseInventory.getHolder();

        if (event.getSlot() != 0) return;

        ItemStack cursor = event.getCursor();

        if (cursor != null && cursor.getType() == Material.SADDLE && !HorseUtil.isSteedSaddle(cursor)){
            horseInventory.setSaddle(cursor.clone());
            player.setItemOnCursor(null);
            return;
        }

        event.setCancelled(true);

        if (event.getClick() != ClickType.RIGHT) return;

        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getEyeLocation(), HorseUtil.createSaddle(horse, 0));
        } else {
            player.getInventory().addItem(HorseUtil.createSaddle(horse, 0));
        }

        if (horse.isLeashed())
            player.getWorld().dropItem(horse.getLocation(), new ItemStack(Material.LEAD));

        horse.remove();

    }

}
