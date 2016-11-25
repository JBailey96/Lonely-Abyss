package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * Created by jdevl on 25/11/2016.
 */

public class unimonCard extends Card {
    private String type;
    private int health, mana, stamina, armour, absorption;

    public unimonCard(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen,
                      Bitmap cardGeneric, Bitmap icon, Bitmap background, String name,
                      String type, String type1, int health, int mana, int stamina, int armour, int absorption) {
        super(x, y, width, height, bitmap, gameScreen, icon, background, name, type);
        this.type = type1;
        this.health = health;
        this.mana = mana;
        this.stamina = stamina;
        this.armour = armour;
        this.absorption = absorption;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
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
