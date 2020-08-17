package net.runeage.simplesteeds;

import net.runeage.simplesteeds.listeners.HorseInventoryListener;
import net.runeage.simplesteeds.listeners.HorseListener;
import net.runeage.simplesteeds.util.Recipes;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class ImprovedSteeds extends JavaPlugin {

    public static ConsoleCommandSender console;

    public static ImprovedSteeds improvedSteeds;

    @Override
    public void onEnable() {
        // Plugin startup logic

        console = getServer().getConsoleSender();

        improvedSteeds = this;

        console.sendMessage(ChatColor.GOLD + "[" + this.getName() + "] " + ChatColor.AQUA + "+===================+");
        console.sendMessage(ChatColor.GOLD + "[" + this.getName() + "] " + ChatColor.AQUA + "Plugin has been enabled!");
        console.sendMessage(ChatColor.GOLD + "[" + this.getName() + "] " + ChatColor.AQUA + "Version: " + this.getDescription().getVersion());
        console.sendMessage(ChatColor.GOLD + "[" + this.getName() + "] " + ChatColor.AQUA + "+===================+");

        getServer().addRecipe(Recipes.saddle());
        getServer().addRecipe(Recipes.leatherHorseArmor());
        getServer().addRecipe(Recipes.ironHorseArmor());
        getServer().addRecipe(Recipes.goldHorseArmor());
        getServer().addRecipe(Recipes.diamondHorseArmor());

        getServer().getPluginManager().registerEvents(new HorseListener(), this);
        getServer().getPluginManager().registerEvents(new HorseInventoryListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        console.sendMessage(ChatColor.GOLD + "[" + this.getName() + "] " + ChatColor.AQUA + "+===================+");
        console.sendMessage(ChatColor.GOLD + "[" + this.getName() + "] " + ChatColor.AQUA + "Plugin has been disabled!");
        console.sendMessage(ChatColor.GOLD + "[" + this.getName() + "] " + ChatColor.AQUA + "+===================+");

        getServer().removeRecipe(Recipes.saddle);
        getServer().removeRecipe(Recipes.leatherhorsearmor);
        getServer().removeRecipe(Recipes.ironhorsearmor);
        getServer().removeRecipe(Recipes.goldhorsearmor);
        getServer().removeRecipe(Recipes.diamondhorsearmor);
    }
}
