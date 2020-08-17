package net.runeage.simplesteeds.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;

public class HorseAttributes {

    private double maxSpeed = 0.3375;
    private double minSpeed = 0.1125;

    private double maxJump = 1.0;
    private double minJump = 0.4;

    private double currentSpeed;
    private double currentJump;
    private double health;

    public HorseAttributes(Horse horse){
        currentSpeed = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        currentJump = horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue();
        health = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    }

    public double getHealth() {
        return health;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public double getCurrentJump() {
        return currentJump;
    }

    public int getAdjustedSpeed() {
        return (int) (((currentSpeed - minSpeed) / (maxSpeed - minSpeed)) * 10);
    }

    public int getAdjustedJump() {
        return (int) (((currentJump - minJump) / (maxJump - minJump)) * 10);
    }
}
