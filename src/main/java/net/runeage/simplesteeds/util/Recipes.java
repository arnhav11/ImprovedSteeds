package net.runeage.simplesteeds.util;

import net.runeage.simplesteeds.ImprovedSteeds;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Recipes {

    public static NamespacedKey saddle = new NamespacedKey(ImprovedSteeds.improvedSteeds, "saddle");
    public static NamespacedKey leatherhorsearmor = new NamespacedKey(ImprovedSteeds.improvedSteeds, "leatherhorsearmor");
    public static NamespacedKey ironhorsearmor = new NamespacedKey(ImprovedSteeds.improvedSteeds, "ironhorsearmor");
    public static NamespacedKey goldhorsearmor = new NamespacedKey(ImprovedSteeds.improvedSteeds, "goldhorsearmor");
    public static NamespacedKey diamondhorsearmor = new NamespacedKey(ImprovedSteeds.improvedSteeds, "diamondhorsearmor");

    public static ShapedRecipe saddle(){
        ShapedRecipe shapedRecipe = new ShapedRecipe(saddle, new ItemStack(Material.SADDLE));
        shapedRecipe.shape(
                "lll",
                " t ");
        shapedRecipe.setIngredient('l', Material.LEATHER);
        shapedRecipe.setIngredient('t', Material.TRIPWIRE_HOOK);
        return shapedRecipe;
    }

    public static ShapedRecipe leatherHorseArmor(){
        ShapedRecipe shapedRecipe = new ShapedRecipe(leatherhorsearmor, new ItemStack(Material.LEATHER_HORSE_ARMOR));
        shapedRecipe.shape(
                "  l",
                "lll",
                "l l");
        shapedRecipe.setIngredient('l', Material.LEATHER);
        return shapedRecipe;
    }

    public static ShapedRecipe ironHorseArmor(){
        ShapedRecipe shapedRecipe = new ShapedRecipe(ironhorsearmor, new ItemStack(Material.IRON_HORSE_ARMOR));
        shapedRecipe.shape(
                "  l",
                "lll",
                "l l");
        shapedRecipe.setIngredient('l', Material.IRON_INGOT);
        return shapedRecipe;
    }

    public static ShapedRecipe goldHorseArmor(){
        ShapedRecipe shapedRecipe = new ShapedRecipe(goldhorsearmor, new ItemStack(Material.GOLDEN_HORSE_ARMOR));
        shapedRecipe.shape(
                "  l",
                "lll",
                "l l");
        shapedRecipe.setIngredient('l', Material.GOLD_INGOT);
        return shapedRecipe;
    }

    public static ShapedRecipe diamondHorseArmor(){
        ShapedRecipe shapedRecipe = new ShapedRecipe(diamondhorsearmor, new ItemStack(Material.DIAMOND_HORSE_ARMOR));
        shapedRecipe.shape(
                "  l",
                "lll",
                "l l");
        shapedRecipe.setIngredient('l', Material.DIAMOND);
        return shapedRecipe;
    }

}
