package uk.ac.qub.eeecs.gage.world;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;
import java.util.ArrayList;

/**
 * Created by jdevl on 19/11/2016.
 */

public class Card {

    private String name, type;
    private int health, mana, stamina, armour, absorption;

    public Card (String name, String type, int health, int mana, int stamina, int armour, int absorption) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.mana = mana;
        this.stamina = stamina;
        this.armour = armour;
        this.absorption = absorption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getArmour() {
        return armour;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }

    public int getAbsorption() {
        return absorption;
    }

    public void setAbsorption(int absorption) {
        this.absorption = absorption;
    }













}
